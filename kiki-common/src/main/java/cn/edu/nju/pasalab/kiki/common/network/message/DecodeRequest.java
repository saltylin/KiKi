package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public final class DecodeRequest extends AbstractMessage {
  private final long ID;

  public DecodeRequest(long ID) {
    this.ID = ID;
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
    return getType().getEncodedLength() + 4;
  }

  @Override
  public void encode(ByteBuf out) {
    getType().encode(out);
    out.writeLong(ID);
  }

  public static DecodeRequest decode(ByteBuf in) {
    return new DecodeRequest(in.readLong());
  }
}
