package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class CompleteIDStoreRequest extends AbstractMessage {
  private final int storeID;

  public CompleteIDStoreRequest(int storeID) {
    this.storeID  = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.COMPLETE_ID_STORE_REQUEST;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 4;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(getStoreID());
  }

  public static CompleteIDStoreRequest decode(ByteBuf in) {
    return new CompleteIDStoreRequest(in.readInt());
  }
}
