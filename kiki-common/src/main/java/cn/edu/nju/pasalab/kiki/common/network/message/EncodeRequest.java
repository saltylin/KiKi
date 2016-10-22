package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class EncodeRequest extends AbstractMessage {
  private final int storeID;
  private final byte[] key;

  public EncodeRequest(int storeID, byte[] key) {
    this.storeID = storeID;
    this.key = key;
  }

  public int getStoreID() {
    return storeID;
  }

  public byte[] getKey() {
    return key;
  }

  @Override
  public Type getType() {
    return Type.ENCODE_REQUEST;
  }

  @Override
  public int getEncodedLength() {
    return getType().getEncodedLength() + 4 + key.length;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeInt(storeID);
    out.writeBytes(key);
  }

  public static EncodeRequest decode(ByteBuf in) {
    int storeID = in.readInt();
    int len = in.readableBytes();
    byte[] key = new byte[len];
    in.readBytes(key);
    return new EncodeRequest(storeID, key);
  }
}
