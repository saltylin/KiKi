package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.server.db.redis.RedisManager;

import java.io.IOException;

public interface DBManager {
  void createDB(int tableID) throws IOException;
  DBStore openDB(int tableID) throws IOException;
  void deleteDB(int tableID) throws IOException;

  class Factory {
    public static DBManager get() {
      return new RedisManager();
    }
  }
}
