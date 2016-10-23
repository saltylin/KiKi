package cn.edu.nju.pasalab.kiki.common;

public final class Constants {
  private Constants() {} // Prevents initialization

  public static final int KB = 1024;
  public static final int MB = KB * 1024;
  public static final int GB = MB * 1024;
  public static final long TB = GB * 1024L;
  public static final long PB = TB * 1024L;

  public static final String LOGGER_TYPE = "kiki.logger.type";
  public static final String CONF_FILE = "kiki.conf.file";

  public static final String SERVER_HOST_NAME = "kiki.server.host.name";
  public static final String SERVER_PORT = "kiki.server.port";

  public static final String CLIENT_REQUEST_TIMEOUT_MS = "kiki.client.request.timeout.ms";
  public static final String CLIENT_NETTY_WORKER_THREADS_NUM =
      "kiki.client.netty.worker.threads.num";

  public static final String SERVER_NETTY_BOSS_THREADS_NUM = "kiki.server.netty.boss.threads.num";
  public static final String SERVER_NETTY_WORKER_THREADS_NUM =
      "kiki.server.netty.worker.threads.num";
  public static final String SERVER_REDIS_HOST_NAME = "kiki.server.redis.host.name";
  public static final String SERVER_REDIS_PORT = "kiki.server.redis.port";
  public static final String SERVER_REDIS_QUERY_SLEEP_MS = "kiki.server.redis.query.sleep.ms";
  public static final String SERVER_ENCODE_REDIS_QUERY_THREADS_NUM =
      "kiki.server.encode.redis.threads.num";
  public static final String SERVER_DECODE_REDIS_QUERY_THREADS_NUM =
      "kiki.server.decode.redis.threads.num";
}
