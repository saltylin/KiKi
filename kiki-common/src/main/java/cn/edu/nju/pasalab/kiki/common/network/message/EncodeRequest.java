package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class EncodeRequest extends AbstractMessage {
  private final byte[] key;

  public EncodeRequest(byte[] key) {
    this.key = key;
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
    return getType().getEncodedLength() + key.length;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeBytes(key);
  }

  public static EncodeRequest decode(ByteBuf in) {
    int len = in.readableBytes();
    byte[] key = new byte[len];
    in.readBytes(key);
    return new EncodeRequest(key);
  }
}
