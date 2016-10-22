package cn.edu.nju.pasalab.kiki.server.resource;

import cn.edu.nju.pasalab.kiki.server.meta.AbstractQuery;

import javax.annotation.concurrent.ThreadSafe;

import java.util.concurrent.ConcurrentLinkedQueue;

@ThreadSafe
public class QueryPool<T extends AbstractQuery> {
  private final int storeID;
  private final ConcurrentLinkedQueue<T> queryQueue;

  public QueryPool(int storeID) {
    this.storeID = storeID;
    queryQueue = new ConcurrentLinkedQueue<>();
  }

  public int getStoreID() {
    return storeID;
  }

  public void addQuery(T query) {
    queryQueue.add(query);
  }

  public boolean hasQuery() {
    return !queryQueue.isEmpty();
  }

  public T pollQuery() {
    return queryQueue.poll();
  }

}
