package cn.edu.nju.pasalab.kiki.client;

import java.io.IOException;

public interface KikiClient {
  long encode(byte[] key) throws IOException;

  byte[] decode(long id) throws IOException;

  void close() throws IOException;

  static class Factory {
    public static void createIDStore(int storeID) throws IOException {

    }

    public static KikiClient get(int storeID) throws IOException {
      return new KikiClient() {
        public long encode(byte[] key) throws IOException {
          return 0;
        }

        public byte[] decode(long id) throws IOException {
          return new byte[0];
        }

        public void close() throws IOException {

        }
      };
    }
  }
}
