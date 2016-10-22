package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class DecodeRequest extends AbstractMessage {
  private final int storeID;
  private final long ID;

  public DecodeRequest(int storeID, long ID) {
    this.storeID = storeID;
    this.ID = ID;
  }

  public int getStoreID() {
    return storeID;
  }

  public long getID() {
    return ID;
  }

  @Override
  public Type getType() {
    return Type.DECODE_REQUEST;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 12;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
    out.writeLong(ID);
  }

  public static DecodeRequest decode(ByteBuf in) {
    int storeID = in.readInt();
    long ID = in.readLong();
    return new DecodeRequest(storeID, ID);
  }
}
