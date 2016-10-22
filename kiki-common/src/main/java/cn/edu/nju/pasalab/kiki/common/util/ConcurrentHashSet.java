package cn.edu.nju.pasalab.kiki.common.util;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<T> {
  private final ConcurrentHashMap<T, Integer> data = new ConcurrentHashMap<>();

  public ConcurrentHashSet() {}

  public boolean contains(T elem) {
    return data.containsKey(elem);
  }

  public void add(T elem) {
    data.put(elem, null);
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public void remove(T elem) {
    data.remove(elem);
  }

  public int size() {
    return data.size();
  }
}
