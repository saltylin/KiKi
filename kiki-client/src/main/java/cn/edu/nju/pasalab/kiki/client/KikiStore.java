package cn.edu.nju.pasalab.kiki.client;

import java.io.IOException;

public abstract class KikiStore extends AbstractKikiClient {
  private final int storeID;

  public KikiStore(String serverHostName, int serverPort, int storeID) throws IOException {
    super(serverHostName, serverPort);
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  public abstract long encode(byte[] key) throws IOException;

  public abstract byte[] decode(long ID) throws IOException;

  public abstract long getIDStoreSize() throws IOException;
}
