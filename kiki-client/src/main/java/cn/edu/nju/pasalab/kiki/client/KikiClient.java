package cn.edu.nju.pasalab.kiki.client;

import cn.edu.nju.pasalab.kiki.common.network.message.CompleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.CreateIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.DeleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeRequest;

import java.io.IOException;

public final class KikiClient extends AbstractKikiClient {
  public KikiClient(String serverHostName, int serverPort) throws IOException {
    super(serverHostName, serverPort);
  }

  public void createIDStore(int storeID) throws IOException {
    try {
      getNetworkProxy().createIDStore(new CreateIDStoreRequest(storeID));
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  public void completeIDStore(int storeID) throws IOException {
    try {
      getNetworkProxy().completeIDStore(new CompleteIDStoreRequest(storeID));
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  public void deleteIDStore(int storeID) throws IOException {
    try {
      getNetworkProxy().deleteIDStore(new DeleteIDStoreRequest(storeID));
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  public long getIDStoreSize(int storeID) throws IOException {
    try {
      return getNetworkProxy().getIDStoreSize(new IDStoreSizeRequest(storeID)).getSize();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
