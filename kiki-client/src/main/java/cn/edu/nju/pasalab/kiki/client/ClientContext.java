package cn.edu.nju.pasalab.kiki.client;

import cn.edu.nju.pasalab.kiki.common.Configuration;

public final class ClientContext {
  private ClientContext() {} // Prevents initialization

  public static Configuration getConf() {
    return Configuration.getInstance();
  }
}
