package cn.edu.nju.pasalab.kiki.common.cache;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class DecodeCachePool extends AbstractCachePool {
  private final ConcurrentHashMap<Long, byte[]> IDToBytesMap;

  public DecodeCachePool(long capacity) {
    super(capacity);
    IDToBytesMap = new ConcurrentHashMap<>();
  }

  @Override
  public void compress() {
    // TODO Currently we simply remove all the cached data in the cache pool. Investigate a more
    // smart compressing policy
    Enumeration<Long> enumeration = IDToBytesMap.keys();
    while (enumeration.hasMoreElements()) {
      remove(enumeration.nextElement());
    }
  }

  public byte[] get(long ID) {
    return IDToBytesMap.get(ID);
  }

  public boolean put(long ID, byte[] key) {
    if (getSize() + key.length > getCapacity()) {
      return false;
    }
    // Note: At the concurrent scenarios, the size after put may be little greater than the capacity
    if (IDToBytesMap.putIfAbsent(ID, key) == null) {
      increaseSize(key.length);
    }
    return true;
  }

  public void remove(long ID) {
    byte[] key = IDToBytesMap.remove(ID);
    if (key != null) {
      decreaseSize(key.length);
    }
  }
}
