package cn.edu.nju.pasalab.kiki.common.network;


import cn.edu.nju.pasalab.kiki.common.network.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public final class MessageEncoder extends MessageToMessageEncoder<Message> {
  public MessageEncoder() {}

  @Override
  public void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
    int frameLength = 4; // The frame length itself contains 4 bytes
    frameLength += msg.getEncodedLength();
    ByteBuf buf = ctx.alloc().buffer(frameLength);
    buf.writeInt(frameLength);
    msg.encode(buf);
    out.add(buf);
  }
}
