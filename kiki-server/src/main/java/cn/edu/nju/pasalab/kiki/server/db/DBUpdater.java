package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.util.BytesUtils;
import cn.edu.nju.pasalab.kiki.server.StoreMeta;
import cn.edu.nju.pasalab.kiki.server.meta.EncodeQuery;
import cn.edu.nju.pasalab.kiki.server.resource.QueryPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public final class DBUpdater implements Runnable, Closeable {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /** An end flag to indicate that no more queries exist. */
  private static final EncodeQuery END_FLAG = new EncodeQuery(0, null, null);

  /**
   * The index used to be set as the ID of the input key. Since now for each store there's only one
   * {@link DBUpdater}, no concurrent problems will occur.
   */
  private volatile long index;
  private final int encodeStoreID;
  private final int decodeStoreID;
  private final DBClient dbEncodeClient;
  private final DBClient dbDecodeClient;
  private final LinkedBlockingQueue<EncodeQuery> encodeQueryQueue;
  private final QueryPool<EncodeQuery> encodeQueryPool;
  private volatile boolean closed = false;

  public DBUpdater(int encodeStoreID, int decodeStoreID, LinkedBlockingQueue<EncodeQuery>
      encodeQueryQueue, QueryPool<EncodeQuery> encodeQueryPool) throws IOException {
    this.encodeStoreID = encodeStoreID;
    this.decodeStoreID = decodeStoreID;
    this.encodeQueryQueue = encodeQueryQueue;
    this.encodeQueryPool = encodeQueryPool;
    dbEncodeClient = DBManager.Factory.get().openDB(encodeStoreID);
    dbDecodeClient = DBManager.Factory.get().openDB(decodeStoreID);
  }

  public long getIndex() {
    return index;
  }

  @Override
  public void run() {
    EncodeQuery query;
    try {
      while (!closed) {
        query = encodeQueryQueue.take();
        if (query == END_FLAG) {
          return;
        }
        byte[] IDBytes = BytesUtils.toBytes(index);
        if (dbEncodeClient.putIfNotExist(query.keyBytes(), IDBytes)) {
          // The key is successfully encoded
          dbDecodeClient.put(IDBytes, query.getKey());
          query.reply(IDBytes);
          ++index;
        } else {
          // The key has already encoded before, so drop this query into the revolved query pool
          // again for better performance
          encodeQueryPool.addQuery(query);
        }
      }
    } catch (IOException | InterruptedException e) {
      LOG.error("Failed to update DB store", e);
    }
  }

  @Override
  public void close() {
    closed = true;
    // Prevents deadlock
    encodeQueryQueue.add(END_FLAG);
  }
}
