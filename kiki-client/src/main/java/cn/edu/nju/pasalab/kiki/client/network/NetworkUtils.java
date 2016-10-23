package cn.edu.nju.pasalab.kiki.client.network;

import cn.edu.nju.pasalab.kiki.client.ClientContext;
import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;

import cn.edu.nju.pasalab.kiki.common.network.MessageDecoder;
import cn.edu.nju.pasalab.kiki.common.network.MessageEncoder;
import cn.edu.nju.pasalab.kiki.common.network.message.AbstractMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class NetworkUtils {
  /** The client configuration. */
  private static Configuration conf = ClientContext.getConf();

  private NetworkUtils() {} // Prevents initialization

  /**
   * Helper method to create Bootstrap.
   *
   * @param workerGroup the worker group
   * @param handler the ChannelInboundHandler
   * @return a Bootstrap
   */
  public static Bootstrap createBootstrap(EventLoopGroup workerGroup,
      final ChannelInboundHandler handler) {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(workerGroup);
    bootstrap.channel(NioSocketChannel.class);
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
      @Override
      public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("Frame decoder", AbstractMessage.createFrameDecoder());
        pipeline.addLast("Message decoder", new MessageDecoder());
        pipeline.addLast("Message encoder", new MessageEncoder());
        pipeline.addLast("Message handler", handler);
      }
    });
    return bootstrap;
  }

  /**
   * Helper method to create a client worker group.
   *
   * @return a worker group
   */
  public static EventLoopGroup createWorkerGroup() {
    return new NioEventLoopGroup(conf.getInt(Constants.CLIENT_NETTY_WORKER_THREADS_NUM));
  }
}
