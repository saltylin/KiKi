package cn.edu.nju.pasalab.kiki.server.meta;

import cn.edu.nju.pasalab.kiki.common.network.message.ErrorMessage;

import io.netty.channel.Channel;

public abstract class AbstractQuery {
  private final int storeID;
  private final Channel channel;
  private final QueryType type;

  public AbstractQuery(int storeID, Channel channel, QueryType type) {
    this.storeID = storeID;
    this.channel = channel;
    this.type = type;
  }

  public int getStoreID() {
    return storeID;
  }

  public Channel getChannel() {
    return channel;
  }

  public QueryType getQueryType() {
    return type;
  }

  public abstract byte[] keyBytes();

  public abstract void reply(byte[] value);

  public void fail(String errorMsg) {
    getChannel().writeAndFlush(new ErrorMessage(errorMsg));
  }
}
