package cn.edu.nju.pasalab.kiki.client.network;

import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.network.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KikiClientChannelHandler extends SimpleChannelInboundHandler<Message> {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final NetworkProxy networkProxy;

  public KikiClientChannelHandler(NetworkProxy networkProxy) {
    this.networkProxy = networkProxy;
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, Message msg) {
    networkProxy.signalResponse(msg);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
    LOG.error("Exception thrown", e);
    ctx.close();
  }
}
