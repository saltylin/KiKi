package cn.edu.nju.pasalab.kiki.server.db;

import java.io.Closeable;

public abstract class AbstractDBStore implements DBStore, Closeable {
  private final int tableID;

  public AbstractDBStore(int tableID) {
    this.tableID = tableID;
  }

  public int getTableID() {
    return tableID;
  }
}
