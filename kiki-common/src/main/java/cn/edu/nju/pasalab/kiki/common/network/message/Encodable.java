package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public interface Encodable {
  void encode(ByteBuf out);

  int getEncodedLength();
}
