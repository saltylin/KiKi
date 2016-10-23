package cn.edu.nju.pasalab.kiki.server.db;

import java.io.Closeable;
import java.io.IOException;

public interface DBStore extends Closeable {
  /**
   * Puts a key-value pair into the DB store.
   *
   * @param key the key
   * @param value the value
   * @throws IOException
   */
  void put(byte[] key, byte[] value) throws IOException;

  /**
   * Gets the value of an input key.
   *
   * @param key the specified key
   * @return the value, <tt>null</tt> if the key is not in the DB store
   * @throws IOException
   */
  byte[] get(byte[] key) throws IOException;

  /**
   * Whether the input key is in the DB store.
   *
   * @param key the key
   * @return whether the input key is in the DB store.
   * @throws IOException
   */
  boolean containsKey(byte[] key) throws IOException;

  /**
   * Put the key-value pair if the key is not in the DB store.
   *
   * @param key the key
   * @param value the value
   * @return whether the key-value pair is put
   * @throws IOException
   */
  boolean putIfNotExist(byte[] key, byte[] value) throws IOException;
}
