package cn.edu.nju.pasalab.kiki.server;

import cn.edu.nju.pasalab.kiki.server.db.DBDecodeQuerier;
import cn.edu.nju.pasalab.kiki.server.db.DBEncodeQuerier;
import cn.edu.nju.pasalab.kiki.server.db.DBUpdater;
import cn.edu.nju.pasalab.kiki.server.meta.DecodeQuery;
import cn.edu.nju.pasalab.kiki.server.meta.EncodeQuery;
import cn.edu.nju.pasalab.kiki.server.resource.QueryPool;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public final class StoreMeta {
  private final int storeID;
  private volatile State state;
  private final int encodeTableID;
  private final int decodeTableID;
  private final ExecutorService encodeQueryService;
  private final ExecutorService decodeQueryService;
  private final List<DBEncodeQuerier> dbEncodeQuerierList;
  private final List<DBDecodeQuerier> dbDecodeQuerierList;
  private final QueryPool<EncodeQuery> encodeQueryPool;
  private final QueryPool<DecodeQuery> decodeQueryPool;
  private final DBUpdater dbUpdater;
  private final LinkedBlockingQueue<EncodeQuery> updateEncodeQueryQueue;

  public StoreMeta(int storeID, int encodeTableID, int decodeTableID,
      ExecutorService encodeQueryService, ExecutorService decodeQueryService,
      List<DBEncodeQuerier> dbEncodeQuerierList, List<DBDecodeQuerier> dbDecodeQuerierList,
      QueryPool<EncodeQuery> encodeQueryPool, QueryPool<DecodeQuery> decodeQueryPool,
      DBUpdater dbUpdater, LinkedBlockingQueue<EncodeQuery> updateEncodeQueryQueue) {
    this.storeID = storeID;
    this.encodeTableID = encodeTableID;
    this.decodeTableID = decodeTableID;
    this.encodeQueryService = encodeQueryService;
    this.decodeQueryService = decodeQueryService;
    this.dbEncodeQuerierList = dbEncodeQuerierList;
    this.dbDecodeQuerierList = dbDecodeQuerierList;
    this.encodeQueryPool = encodeQueryPool;
    this.decodeQueryPool = decodeQueryPool;
    this.dbUpdater = dbUpdater;
    this.updateEncodeQueryQueue = updateEncodeQueryQueue;
    state = State.UPDATING;
  }

  public void complete() throws IOException {
    try {
      dbUpdater.close();
    } finally {
      state = State.COMPLETED;
    }
  }

  public void delete() throws IOException {
    for (DBEncodeQuerier querier : dbEncodeQuerierList) {
      querier.close();
    }
    for (DBDecodeQuerier querier : dbDecodeQuerierList) {
      querier.close();
    }
    encodeQueryService.shutdown();
    decodeQueryService.shutdown();
  }

  public long getSize() {
    return dbUpdater.getIndex();
  }

  public int getStoreID() {
    return storeID;
  }

  public int getEncodeTableID() {
    return encodeTableID;
  }

  public int getDecodeTableID() {
    return decodeTableID;
  }

  public ExecutorService getEncodeQueryService() {
    return encodeQueryService;
  }

  public ExecutorService getDecodeQueryService() {
    return decodeQueryService;
  }

  public List<DBEncodeQuerier> getDBEncodeQuerierList() {
    return dbEncodeQuerierList;
  }

  public List<DBDecodeQuerier> getDBDecodeQuerierList() {
    return dbDecodeQuerierList;
  }

  public QueryPool<EncodeQuery> getEncodeQueryPool() {
    return encodeQueryPool;
  }

  public QueryPool<DecodeQuery> getDecodeQueryPool() {
    return decodeQueryPool;
  }

  public DBUpdater getDBUpdater() {
    return dbUpdater;
  }

  public LinkedBlockingQueue<EncodeQuery> getUpdateEncodeQueryQueue() {
    return updateEncodeQueryQueue;
  }

  public State getState() {
    return state;
  }

  public enum State {
    UPDATING,
    COMPLETED
  }
}
