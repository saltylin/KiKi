package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class CloseIDStoreResponse extends AbstractMessage {
  private final int storeID;

  public CloseIDStoreResponse(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.CLOSE_ID_STORE_RESPONSE;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 4;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
  }

  public static CloseIDStoreResponse decode(ByteBuf in) {
    return new CloseIDStoreResponse(in.readInt());
  }
}
