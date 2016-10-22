package cn.edu.nju.pasalab.kiki.server.db.redis;

import cn.edu.nju.pasalab.kiki.server.db.AbstractDBClient;

import redis.clients.jedis.Jedis;

import java.io.IOException;

public class RedisClient extends AbstractDBClient {
  private final Jedis jedis;

  public RedisClient(int storeID, Jedis jedis) {
    super(storeID);
    this.jedis = jedis;
  }

  @Override
  public void put(byte[] key, byte[] value) {
    jedis.set(key, value);
  }

  @Override
  public byte[] get(byte[] key) {
    return jedis.get(key);
  }

  @Override
  public boolean containsKey(byte[] key) {
    return jedis.get(key) != null;
  }

  @Override
  public boolean putIfNotExist(byte[] key, byte[] value) throws IOException {
    Long res = jedis.setnx(key, value);
    if (res == null) {
      throw new IOException("Failed to put key-value pair");
    }
    int r = (int) (long) res;
    switch (r) {
      case 0:
        return false;
      case 1:
        return true;
      default:
        throw new IOException("Error response while putting the key-value pair");
    }
  }

  @Override
  public void close() {
    jedis.close();
  }
}
