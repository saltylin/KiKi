package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class IDStoreSizeResponse extends AbstractMessage {
  private final int storeID;
  private final long size;

  public IDStoreSizeResponse(int storeID, long size) {
    this.storeID = storeID;
    this.size = size;
  }

  public int getStoreID() {
    return storeID;
  }

  public long getSize() {
    return size;
  }

  @Override
  public Type getType() {
    return Type.ID_STORE_SIZE_RESPONSE;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 12;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
    out.writeLong(size);
  }

  public static IDStoreSizeResponse decode(ByteBuf in) {
    int storeID = in.readInt();
    long size = in.readLong();
    return new IDStoreSizeResponse(storeID, size);
  }
}
