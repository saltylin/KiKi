package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.server.db.redis.RedisManager;

import java.io.IOException;

public interface DBManager {
  void createDB(int storeID) throws IOException;
  DBStore openDB(int storeID) throws IOException;
  void deleteDB(int storeID) throws IOException;

  class Factory {
    public static DBManager get() {
      return new RedisManager();
    }
  }
}
