package cn.edu.nju.pasalab.kiki.server.meta;

import cn.edu.nju.pasalab.kiki.common.network.message.EncodeResponse;
import cn.edu.nju.pasalab.kiki.common.util.BytesUtils;
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

  @Override
  public byte[] keyBytes() {
    return key;
  }

  @Override
  public void reply(byte[] value) {
    long ID = BytesUtils.bytesToLong(value);
    getChannel().writeAndFlush(new EncodeResponse(getStoreID(), ID, key));
  }
}
