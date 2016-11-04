package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractDBQuerier implements Runnable, Closeable {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final int tableID;
  private final DBStore dbStore;
  private volatile boolean closed = false;

  public AbstractDBQuerier(int tableID) throws IOException {
    this.tableID = tableID;
    dbStore = DBManager.Factory.get().openDB(tableID);
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
  public void close() throws IOException {
    closed = true;
    dbStore.close();
  }

  public boolean isClosed() {
    return closed;
  }

  abstract protected void query() throws Exception;

  protected DBStore getDBStore() {
    return dbStore;
  }
}
