package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractDBQuerier implements Runnable, Closeable {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final int storeID;
  private final DBClient dbClient;
  private volatile boolean closed = false;

  public AbstractDBQuerier(int storeID) throws IOException {
    this.storeID = storeID;
    dbClient = DBManager.Factory.get().openDB(storeID);
  }

  @Override
  public void run() {
    try {
      while (!closed) {
        query();
      }
    } catch (Exception e) {
      LOG.error("Failed to query ID store", e);
    }
  }

  @Override
  public void close() {
    closed = true;
  }

  public boolean isClosed() {
    return closed;
  }

  abstract protected void query() throws Exception;

  protected DBClient getDBClient() {
    return dbClient;
  }
}
