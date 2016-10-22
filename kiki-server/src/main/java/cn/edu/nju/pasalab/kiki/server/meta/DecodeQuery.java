package cn.edu.nju.pasalab.kiki.server.meta;

import io.netty.channel.Channel;

public final class DecodeQuery extends AbstractQuery {
  private final long ID;

  public DecodeQuery(int storeID, Channel channel, long ID) {
    super(storeID, channel, QueryType.DECODE_QUERY);
    this.ID = ID;
  }

  public long getID() {
    return ID;
  }
}
