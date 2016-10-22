package cn.edu.nju.pasalab.kiki.common.cache;

public interface CachePool {
  long getCapacity();
  long getSize();
  void compress();
}
