package cn.edu.nju.pasalab.kiki.server.meta;

import io.netty.channel.Channel;

public final class EncodeQuery extends AbstractQuery {
  private final byte[] key;

  public EncodeQuery(int storeID, Channel channel, byte[] key) {
    super(storeID, channel, QueryType.ENCODE_QUERY);
    this.key = key;
  }

  public byte[] getKey() {
    return key;
  }
}
