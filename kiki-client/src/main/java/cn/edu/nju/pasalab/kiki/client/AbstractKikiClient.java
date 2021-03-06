package cn.edu.nju.pasalab.kiki.client;

import cn.edu.nju.pasalab.kiki.client.network.KikiClientChannelHandler;
import cn.edu.nju.pasalab.kiki.client.network.NetworkProxy;
import cn.edu.nju.pasalab.kiki.client.network.NetworkUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

abstract class AbstractKikiClient implements Closeable {
  private final String serverHostName;
  private final int serverPort;
  private final KikiClientChannelHandler channelHandler;
  private final Channel channel;
  private final EventLoopGroup workerGroup;
  private final NetworkProxy networkProxy;

  protected AbstractKikiClient(String serverHostName, int serverPort) throws IOException {
    this.serverHostName = serverHostName;
    this.serverPort = serverPort;
    networkProxy = new NetworkProxy();
    channelHandler = new KikiClientChannelHandler(networkProxy);
    workerGroup = NetworkUtils.createWorkerGroup();
    Bootstrap bootstrap = NetworkUtils.createBootstrap(workerGroup, channelHandler);

    try {
      ChannelFuture future =
          bootstrap.connect(InetAddress.getByName(serverHostName), serverPort).sync();
      channel = future.channel();
      networkProxy.initChannel(channel);
    } catch (UnknownHostException | InterruptedException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() {
    channel.close();
    workerGroup.shutdownGracefully();
  }

  public String getServerHostName() {
    return serverHostName;
  }

  public int getServerPort() {
    return serverPort;
  }

  protected NetworkProxy getNetworkProxy() {
    return networkProxy;
  }
}
