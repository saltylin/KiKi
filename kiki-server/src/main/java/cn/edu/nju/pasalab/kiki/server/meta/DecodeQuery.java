package cn.edu.nju.pasalab.kiki.server.meta;

import cn.edu.nju.pasalab.kiki.common.network.message.DecodeResponse;
import cn.edu.nju.pasalab.kiki.common.util.BytesUtils;
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

  @Override
  public byte[] keyBytes() {
    return BytesUtils.toBytes(ID);
  }

  @Override
  public void reply(byte[] value) {
    getChannel().writeAndFlush(new DecodeResponse(getStoreID(), ID, value));
  }
}
