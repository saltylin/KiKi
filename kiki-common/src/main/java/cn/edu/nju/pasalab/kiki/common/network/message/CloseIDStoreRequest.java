package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class CloseIDStoreRequest extends AbstractMessage {
  private final int storeID;

  public CloseIDStoreRequest(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.CLOSE_ID_STORE_REQUEST;
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

  public static CloseIDStoreRequest decode(ByteBuf in) {
    return new CloseIDStoreRequest(in.readInt());
  }
}
