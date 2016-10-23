package cn.edu.nju.pasalab.kiki.client;

import java.io.IOException;

public final class KikiFactory {
  private KikiFactory() {} // Prevents initialization

  public static KikiClient getKikiClient(String serverHostName, int serverPort) throws IOException {
    return new KikiClient(serverHostName, serverPort);
  }

  public static KikiStore getKikiStore(String serverHostName, int serverPort, int storeID)
      throws IOException {
    return new DefaultKikiStore(serverHostName, serverPort, storeID);
  }
}
