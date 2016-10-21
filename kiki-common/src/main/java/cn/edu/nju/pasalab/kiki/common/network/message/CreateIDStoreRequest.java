package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class CreateIDStoreRequest extends AbstractMessage {
  private final int storeID;

  public CreateIDStoreRequest(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.CREATE_ID_STORE_REQUEST;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 4;
  }

  public static Message decode(ByteBuf in) {
    return new CreateIDStoreRequest(in.readInt());
  }
}
