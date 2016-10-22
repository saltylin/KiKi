package cn.edu.nju.pasalab.kiki.server;

import io.netty.channel.Channel;

import java.io.IOException;

public final class ClientRequestHandler {
  private final KikiServiceManager kikiServiceManager;

  public ClientRequestHandler(KikiServiceManager kikiServiceManager) {
    this.kikiServiceManager = kikiServiceManager;
  }

  public void createStore(int storeID) throws IOException {
    kikiServiceManager.createStore(storeID);
  }

  public void encode(int storeID, byte[] key, Channel channel) throws IOException {
    kikiServiceManager.encode(storeID, key, channel);
  }

  public void decode(int storeID, long ID, Channel channel) throws IOException {
    kikiServiceManager.decode(storeID, ID, channel);
  }

  public void completeStore(int storeID) throws IOException {
    kikiServiceManager.completeStore(storeID);
  }

  public void deleteStore(int storeID) throws IOException {
    kikiServiceManager.deleteStore(storeID);
  }

  public long getStoreSize(int storeID) throws IOException {
    return kikiServiceManager.getStoreSize(storeID);
  }
}
