package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public abstract class AbstractMessage implements Message {
  private final Type type;

  public AbstractMessage(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  static Message decode(ByteBuf in) {
    Type type = Type.decode(in);
    switch (type) {
      default:
        throw new IllegalArgumentException(
            String.format("No corresponding message for type %s", type));
    }
  }
}
