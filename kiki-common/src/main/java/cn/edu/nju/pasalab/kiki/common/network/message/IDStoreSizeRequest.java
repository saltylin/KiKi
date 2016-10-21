package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class IDStoreSizeRequest extends AbstractMessage {
  private final int storeID;

  public IDStoreSizeRequest(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.ID_STORE_SIZE_REQUEST;
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

  public static IDStoreSizeRequest decode(ByteBuf in) {
    return new IDStoreSizeRequest(in.readInt());
  }
}
