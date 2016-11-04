package cn.edu.nju.pasalab.kiki.server.db.redis;

import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.server.ServerContext;
import cn.edu.nju.pasalab.kiki.server.db.DBStore;
import cn.edu.nju.pasalab.kiki.server.db.DBManager;

import redis.clients.jedis.Jedis;

import java.io.IOException;

public final class RedisManager implements DBManager {
  private final Configuration conf;
  private final String redisHostName;
  private final int redisPort;

  public RedisManager() {
    conf = ServerContext.getConf();
    redisHostName = conf.getString(Constants.SERVER_REDIS_HOST_NAME);
    redisPort = conf.getInt(Constants.SERVER_REDIS_PORT);
  }

  @Override
  public void createDB(int tableID) {
    // Nothing to do
  }

  @Override
  public DBStore openDB(int tableID) throws IOException {
    Jedis jedis = new Jedis(redisHostName, redisPort);
    jedis.select(tableID);
    return new RedisStore(tableID, jedis);
  }

  @Override
  public void deleteDB(int tableID) {
    Jedis jedis = new Jedis(redisHostName, redisPort);
    jedis.select(tableID);
    jedis.flushDB();
    jedis.close();
  }
}
