package cn.edu.nju.pasalab.kiki.server.db;

import java.io.Closeable;

public abstract class AbstractDBClient implements DBClient, Closeable {
  private final int storeID;

  public AbstractDBClient(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }
}
