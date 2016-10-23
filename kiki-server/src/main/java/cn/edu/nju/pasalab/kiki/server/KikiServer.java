package cn.edu.nju.pasalab.kiki.server;

import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.network.message.AbstractMessage;
import cn.edu.nju.pasalab.kiki.common.network.MessageDecoder;
import cn.edu.nju.pasalab.kiki.common.network.MessageEncoder;

import com.google.common.base.Throwables;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class KikiServer {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  public static void main(String[] args) {
    Configuration conf = ServerContext.getConf();
    KikiServiceManager manager = new KikiServiceManager();
    ClientRequestHandler clientRequestHandler = new ClientRequestHandler(manager);
    final KikiServerChannelHandler channelHandler =
        new KikiServerChannelHandler(clientRequestHandler);
    String hostName = conf.getString(Constants.SERVER_HOST_NAME);
    int port = conf.getInt(Constants.SERVER_PORT);
    int numBossThreads = conf.getInt(Constants.SERVER_NETTY_BOSS_THREADS_NUM);
    int numWorkerThreads = conf.getInt(Constants.SERVER_NETTY_WORKER_THREADS_NUM);

    EventLoopGroup bossGroup = new NioEventLoopGroup(numBossThreads);
    EventLoopGroup workerGroup = new NioEventLoopGroup(numWorkerThreads);
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("Frame Decoder", AbstractMessage.createFrameDecoder());
            pipeline.addLast("Message Decoder", new MessageDecoder());
            pipeline.addLast("Message Encoder", new MessageEncoder());
            pipeline.addLast("Channel handler", channelHandler);
          }
        })
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.SO_BACKLOG, 128);

    try {
      ChannelFuture future = bootstrap.bind(InetAddress.getByName(hostName), port).sync();
      LOG.info("KiKi server has launched successfully");
      future.channel().closeFuture().sync();
    } catch (UnknownHostException | InterruptedException e) {
      LOG.error("Error occurred in KiKi server", e);
      Throwables.propagate(e);
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
