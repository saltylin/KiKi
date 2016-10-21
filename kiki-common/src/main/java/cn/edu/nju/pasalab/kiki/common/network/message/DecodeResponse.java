package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class DecodeResponse extends AbstractMessage {
  private final long ID;
  private final byte[] key;

  public DecodeResponse(long ID, byte[] key) {
    this.ID = ID;
    this.key = key;
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
    return getType().getEncodedLength() + 4 + key.length;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeLong(ID);
    out.writeBytes(key);
  }

  public static DecodeResponse decode(ByteBuf in) {
    long ID = in.readLong();
    int len = in.readableBytes();
    byte[] key = new byte[len];
    in.readBytes(key);
    return new DecodeResponse(ID, key);
  }
}
