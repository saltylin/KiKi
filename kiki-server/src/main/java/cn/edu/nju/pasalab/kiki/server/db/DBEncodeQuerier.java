package cn.edu.nju.pasalab.kiki.server.db;

import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.server.ServerContext;
import cn.edu.nju.pasalab.kiki.server.meta.EncodeQuery;
import cn.edu.nju.pasalab.kiki.server.resource.QueryPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public final class DBEncodeQuerier extends AbstractDBQuerier {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final QueryPool<EncodeQuery> queryPool;
  private final LinkedBlockingQueue<EncodeQuery> updateQueryQueue;
  private final long sleepMs;

  public DBEncodeQuerier(int storeID, QueryPool<EncodeQuery> queryPool,
      LinkedBlockingQueue<EncodeQuery> updateQueryQueue) throws IOException {
    super(storeID);
    this.queryPool = queryPool;
    this.updateQueryQueue = updateQueryQueue;
    sleepMs = ServerContext.getConf().getLong(Constants.SERVER_REDIS_QUERY_SLEEP_MS);
  }

  @Override
  protected void query() throws Exception {
    EncodeQuery query = queryPool.pollQuery();
    while (query == null && !isClosed()) {
      Thread.sleep(sleepMs);
      query = queryPool.pollQuery();
    }
    if (isClosed()) {
      return;
    }
    byte[] res = getDBClient().get(query.getKey());
    if (res == null) {
      // This key hasn't been encoded
      updateQueryQueue.add(query);
    } else {
      query.reply(res);
    }
  }
}
