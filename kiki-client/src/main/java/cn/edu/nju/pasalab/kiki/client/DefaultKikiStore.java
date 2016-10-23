package cn.edu.nju.pasalab.kiki.client;

import cn.edu.nju.pasalab.kiki.common.network.message.DecodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.EncodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeRequest;

import java.io.IOException;

public final class DefaultKikiStore extends KikiStore {
  public DefaultKikiStore(String serverHostName, int serverPort, int storeID) throws IOException {
    super(serverHostName, serverPort, storeID);
  }

  @Override
  public long encode(byte[] key) throws IOException {
    try {
      return getNetworkProxy().encode(new EncodeRequest(getStoreID(), key)).getID();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  @Override
  public byte[] decode(long ID) throws IOException {
    try {
      return getNetworkProxy().decode(new DecodeRequest(getStoreID(), ID)).getKey();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  @Override
  public long getIDStoreSize() throws IOException {
    try {
      return getNetworkProxy().getIDStoreSize(new IDStoreSizeRequest(getStoreID())).getSize();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
