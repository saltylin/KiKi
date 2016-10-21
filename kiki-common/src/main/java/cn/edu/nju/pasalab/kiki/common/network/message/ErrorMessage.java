package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

public final class ErrorMessage extends AbstractMessage {
  private static final String CHAR_SET = "utf-8";
  private final String info;

  public ErrorMessage(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }

  @Override
  public Type getType() {
    return Type.ERROR_MESSAGE;
  }

  @Override
  public int getEncodedLength() {
    try {
      return info.getBytes(CHAR_SET).length;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void encode(ByteBuf out) {

  }
}
