package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public final class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
  public MessageDecoder() {}

  @Override
  public void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    out.add(AbstractMessage.decode(msg));
  }
}
