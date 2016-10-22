package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class DecodeResponse extends AbstractMessage {
  private final int storeID;
  private final long ID;
  private final byte[] key;

  public DecodeResponse(int storeID, long ID, byte[] key) {
    this.storeID = storeID;
    this.ID = ID;
    this.key = key;
  }

  public int getStoreID() {
    return storeID;
  }

  public long getID() {
    return ID;
  }

  public byte[] getKey() {
    return key;
  }

  @Override
  public Type getType() {
    return Type.DECODE_REQUEST;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 12 + key.length;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
    out.writeLong(ID);
    out.writeBytes(key);
  }

  public static DecodeResponse decode(ByteBuf in) {
    int storeID = in.readInt();
    long ID = in.readLong();
    int len = in.readableBytes();
    byte[] key = new byte[len];
    in.readBytes(key);
    return new DecodeResponse(storeID, ID, key);
  }
}
