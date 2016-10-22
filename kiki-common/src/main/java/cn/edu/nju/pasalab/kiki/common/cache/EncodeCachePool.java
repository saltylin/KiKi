package cn.edu.nju.pasalab.kiki.common.cache;

import javax.annotation.concurrent.ThreadSafe;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class EncodeCachePool extends AbstractCachePool {
  private final ConcurrentHashMap<BytesHolder, Long> bytesToIDMap;

  public EncodeCachePool(long capacity) {
    super(capacity);
    bytesToIDMap = new ConcurrentHashMap<>();
  }

  @Override
  public void compress() {
    // TODO Currently we simply remove all the cached data in the cache pool. Investigate a more
    // smart compressing policy
    Enumeration<BytesHolder> enumeration = bytesToIDMap.keys();
    while (enumeration.hasMoreElements()) {
      remove(enumeration.nextElement().getKey());
    }
  }

  /**
   * @param key the key to be encoded
   * @return the ID of the key, null if not cached
   */
  public Long get(byte[] key) {
    return bytesToIDMap.get(new BytesHolder(key));
  }

  public boolean put(byte[] key, long ID) {
    if (getSize() + key.length > getCapacity()) {
      return false;
    }
    // Note: At the concurrent scenarios, the size after put may be little greater than the capacity
    if (bytesToIDMap.putIfAbsent(new BytesHolder(key), ID) == null) {
      increaseSize(key.length);
    }
    return true;
  }

  public void remove(byte[] key) {
    if (bytesToIDMap.remove(new BytesHolder(key)) != null) {
      decreaseSize(key.length);
    }
  }
}
