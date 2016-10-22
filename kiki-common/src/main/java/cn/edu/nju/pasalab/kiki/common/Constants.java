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

  public static final String SERVER_REDIS_HOST_NAME = "kiki.server.redis.host.name";
  public static final String SERVER_REDIS_PORT = "kiki.server.redis.port";
  public static final String SERVER_REDIS_QUERY_SLEEP_MS = "kiki.server.redis.query.sleep.ms";
}
