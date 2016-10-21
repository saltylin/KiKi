package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class DeleteIDStoreRequest extends AbstractMessage {
  private final int storeID;

  public DeleteIDStoreRequest(int storeID) {
    this.storeID = storeID;
  }

  public int getStoreID() {
    return storeID;
  }

  @Override
  public Type getType() {
    return Type.DELETE_ID_STORE_REQUEST;
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

  public static DeleteIDStoreRequest decode(ByteBuf in) {
    return new DeleteIDStoreRequest(in.readInt());
  }
}
