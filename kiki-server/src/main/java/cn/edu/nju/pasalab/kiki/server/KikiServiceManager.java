package cn.edu.nju.pasalab.kiki.server;

import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.util.ConcurrentHashSet;
import cn.edu.nju.pasalab.kiki.common.util.ExceptionLoggedThreadPool;
import cn.edu.nju.pasalab.kiki.common.util.Pair;
import cn.edu.nju.pasalab.kiki.server.db.DBDecodeQuerier;
import cn.edu.nju.pasalab.kiki.server.db.DBEncodeQuerier;
import cn.edu.nju.pasalab.kiki.server.db.DBManager;
import cn.edu.nju.pasalab.kiki.server.db.DBUpdater;
import cn.edu.nju.pasalab.kiki.server.meta.DecodeQuery;
import cn.edu.nju.pasalab.kiki.server.meta.EncodeQuery;
import cn.edu.nju.pasalab.kiki.server.resource.QueryPool;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class KikiServiceManager {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final Configuration conf;
  private final int encodeQueryNumThreads;
  private final int decodeQueryNumThreads;
  private final ConcurrentHashSet<Integer> storeIDSet;
  private final ConcurrentHashMap<Integer, StoreMeta> storeIDToMetaMap;
  private final DBManager dbManager;
  private final AtomicInteger tableIDGenerator;

  public KikiServiceManager() {
    conf = ServerContext.getConf();
    encodeQueryNumThreads = conf.getInt(Constants.SERVER_ENCODE_REDIS_QUERY_THREADS_NUM);
    decodeQueryNumThreads = conf.getInt(Constants.SERVER_DECODE_REDIS_QUERY_THREADS_NUM);
    storeIDSet = new ConcurrentHashSet<>();
    storeIDToMetaMap = new ConcurrentHashMap<>();
    dbManager = DBManager.Factory.get();
    tableIDGenerator = new AtomicInteger(1);
  }

  public void createStore(int storeID) throws IOException {
    if (storeIDSet.contains(storeID)) {
      // The store ID has been used
      throw new IOException(String.format("The store ID %d has already been used", storeID));
    }
    synchronized (storeIDSet) {
      if (storeIDSet.contains(storeID)) {
        // The store ID has been used
        throw new IOException(String.format("The store ID %d has already been used", storeID));
      }
      storeIDSet.add(storeID);
    }
    StoreMeta storeMeta = createStoreMeta(storeID);
    storeIDToMetaMap.put(storeID, storeMeta);
    new Thread(storeMeta.getDBUpdater()).start();
    LOG.info("Create ID store {}", storeID);
  }

  private StoreMeta createStoreMeta(int storeID) throws IOException {
    Pair<Integer, Integer> tableIDPair = generateTableIDPair();
    int encodeTableID = tableIDPair.getLeft();
    int decodeTableID = tableIDPair.getRight();
    createDBTable(encodeTableID);
    createDBTable(decodeTableID);
    ExecutorService encodeService =
        ExceptionLoggedThreadPool.newFixedThreadPool(encodeQueryNumThreads);
    ExecutorService decodeService =
        ExceptionLoggedThreadPool.newFixedThreadPool(decodeQueryNumThreads);
    List<DBEncodeQuerier> dbEncodeQuerierList = new ArrayList<>(encodeQueryNumThreads);
    List<DBDecodeQuerier> dbDecodeQuerierList = new ArrayList<>(decodeQueryNumThreads);
    QueryPool<EncodeQuery> encodeQueryPool = new QueryPool<>(storeID);
    QueryPool<DecodeQuery> decodeQueryPool = new QueryPool<>(storeID);
    LinkedBlockingQueue<EncodeQuery> updateEncodeQueryQueue =
        new LinkedBlockingQueue<>();
    DBUpdater dbUpdater =
        new DBUpdater(encodeTableID, decodeTableID, updateEncodeQueryQueue, encodeQueryPool);
    for (int i = 0; i < encodeQueryNumThreads; ++i) {
      DBEncodeQuerier encodeQuerier =
          new DBEncodeQuerier(encodeTableID, encodeQueryPool, updateEncodeQueryQueue);
      encodeService.submit(encodeQuerier);
      dbEncodeQuerierList.add(encodeQuerier);
    }
    for (int i = 0; i < decodeQueryNumThreads; ++i) {
      DBDecodeQuerier decodeQuerier = new DBDecodeQuerier(decodeTableID, decodeQueryPool);
      decodeService.submit(decodeQuerier);
      dbDecodeQuerierList.add(decodeQuerier);
    }
    return new StoreMeta(storeID, encodeTableID, decodeTableID, encodeService, decodeService,
        dbEncodeQuerierList, dbDecodeQuerierList, encodeQueryPool, decodeQueryPool, dbUpdater,
        updateEncodeQueryQueue);
  }

  private void createDBTable(int tableID) throws IOException {
    dbManager.createDB(tableID);
  }

  private void deleteDBTable(int tableID) throws IOException {
    dbManager.deleteDB(tableID);
  }

  public void encode(int storeID, byte[] key, Channel channel) throws IOException {
    checkStoreExists(storeID);
    StoreMeta storeMeta = storeIDToMetaMap.get(storeID);
    storeMeta.getEncodeQueryPool().addQuery(new EncodeQuery(storeID, channel, key));
  }

  public void decode(int storeID, long ID, Channel channel) throws IOException {
    checkStoreExists(storeID);
    StoreMeta storeMeta = storeIDToMetaMap.get(storeID);
    storeMeta.getDecodeQueryPool().addQuery(new DecodeQuery(storeID, channel, ID));
  }

  public void completeStore(int storeID) throws IOException {
    checkStoreExists(storeID);
    StoreMeta storeMeta = storeIDToMetaMap.get(storeID);
    storeMeta.complete();
    LOG.info("Complete ID store {}", storeID);
  }

  public void deleteStore(int storeID) throws IOException {
    checkStoreExists(storeID);
    try {
      StoreMeta storeMeta = storeIDToMetaMap.get(storeID);
      storeMeta.delete();
      deleteDBTable(storeMeta.getEncodeTableID());
      deleteDBTable(storeMeta.getDecodeTableID());
    } finally {
      storeIDSet.remove(storeID);
      storeIDToMetaMap.remove(storeID);
    }
    LOG.info("Delete ID store {}", storeID);
  }

  public long getStoreSize(int storeID) throws IOException {
    checkStoreExists(storeID);
    return storeIDToMetaMap.get(storeID).getSize();
  }

  private void checkStoreExists(int storeID) throws IOException {
    if (!storeIDToMetaMap.containsKey(storeID)) {
      throw new IOException(String.format("No store for ID %d", storeID));
    }
  }

  private Pair<Integer, Integer> generateTableIDPair() {
    int encodeTableID = tableIDGenerator.getAndIncrement();
    int decodeTableID = tableIDGenerator.getAndIncrement();
    return new Pair<>(encodeTableID, decodeTableID);
  }
}
