package cn.edu.nju.pasalab.kiki.server;

import cn.edu.nju.pasalab.kiki.common.Configuration;

public final class ServerContext {
  private ServerContext() {} // Prevents initialization

  public static Configuration getConf() {
    return Configuration.getInstance();
  }
}
