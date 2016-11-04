package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.server.meta.DecodeQuery;
import cn.edu.nju.pasalab.kiki.server.resource.QueryPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class DBDecodeQuerier extends AbstractDBQuerier {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final QueryPool<DecodeQuery> queryPool;

  public DBDecodeQuerier(int tableID, QueryPool<DecodeQuery> queryPool) throws IOException {
    super(tableID);
    this.queryPool = queryPool;
  }

  @Override
  public void query() throws Exception {
    DecodeQuery query = queryPool.pollQuery();
    while (query == null && !isClosed()) {
      query = queryPool.pollQuery();
    }
    if (isClosed()) {
      return;
    }
    byte[] res = getDBStore().get(query.keyBytes());
    if (res == null) {
      // Illegal query ID
      query.fail(String.format("No key is associated with the given ID %d", query.getID()));
    } else {
      query.reply(res);
    }
  }
}
