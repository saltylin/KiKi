package cn.edu.nju.pasalab.kiki.common.cache;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractCachePool implements CachePool {
  private final long capacity;
  private final AtomicLong size;

  public AbstractCachePool(long capacity) {
    this.capacity = capacity;
    size = new AtomicLong(0);
  }

  @Override
  public long getCapacity() {
    return capacity;
  }

  @Override
  public long getSize() {
    return size.get();
  }

  protected void increaseSize(long delta) {
    size.addAndGet(delta);
  }

  protected void decreaseSize(long delta) {
    size.addAndGet(-delta);
  }
}
