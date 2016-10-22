package cn.edu.nju.pasalab.kiki.server.resource;

import cn.edu.nju.pasalab.kiki.server.meta.AbstractQuery;

import com.google.common.base.Preconditions;

import javax.annotation.concurrent.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@ThreadSafe
public class QueryPool<T extends AbstractQuery> {
  private final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<T>> storeIDToQueueMap;
  private final ConcurrentLinkedQueue<Integer> queryStoreIDQueue;

  public QueryPool() {
    storeIDToQueueMap = new ConcurrentHashMap<>();
    queryStoreIDQueue = new ConcurrentLinkedQueue<>();
  }

  public boolean hasStore(int storeID) {
    return storeIDToQueueMap.containsKey(storeID);
  }

  public void addStore(int storeID) {
    Preconditions.checkArgument(!hasStore(storeID), String.format("The store %d has already been in"
        + " the query pool", storeID));
    storeIDToQueueMap.put(storeID, new ConcurrentLinkedQueue<T>());
  }

  public void addQuery(int storeID, T query) {
    Preconditions.checkArgument(hasStore(storeID), String.format("The store %d isn't in the query "
        + "pool", storeID));
    storeIDToQueueMap.get(storeID).add(query);
    queryStoreIDQueue.add(storeID);
  }

  public ConcurrentLinkedQueue<T> getQueryQueue(int storeID) {
    Preconditions.checkArgument(hasStore(storeID), String.format("The store %d isn't in the query "
        + "pool", storeID));
    return storeIDToQueueMap.get(storeID);
  }

  public boolean hasQuery() {
    return !queryStoreIDQueue.isEmpty();
  }

  public T pollQuery() {
    Integer storeID = queryStoreIDQueue.poll();
    if (storeID == null) {
      return null;
    }
    Preconditions.checkArgument(hasStore(storeID), String.format("The store %d isn't in the query "
        + "pool", storeID));
    return storeIDToQueueMap.get(storeID).poll();
  }

}
