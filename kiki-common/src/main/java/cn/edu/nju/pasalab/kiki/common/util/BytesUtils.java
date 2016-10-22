package cn.edu.nju.pasalab.kiki.common.util;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.nio.ByteBuffer;

/**
 * Map between other types and byte array.
 */
public final class BytesUtils {
  private BytesUtils() {} // Prevent initialization

  /**
   * Maps an int value to a byte array.
   *
   * @param value an int value
   * @return a byte array representing the input int value
   */
  public static byte[] toBytes(int value) {
    return ByteBuffer.allocate(Ints.BYTES).putInt(value).array();
  }

  /**
   * Maps a long value to a byte array.
   *
   * @param value long
   * @return byte array
   */
  public static byte[] toBytes(long value) {
    return ByteBuffer.allocate(Longs.BYTES).putLong(value).array();
  }

  /**
   * Maps a byte array to an int.
   *
   * @param bytes the byte array
   * @return an int value
   */
  public static int bytesToInt(byte[] bytes) {
    Preconditions.checkArgument(bytes.length == Ints.BYTES, "The byte array length must be 4 in"
        + " order to be converted to an int.");
    return ByteBuffer.wrap(bytes).getInt();
  }

  /**
   * Maps a byte array to a long.
   *
   * @param bytes the byte array
   * @return a long value
   */
  public static long bytesToLong(byte[] bytes) {
    Preconditions.checkArgument(bytes.length == Longs.BYTES, "The byte array length must be 8 in"
        + " order to be converted to a long.");
    return ByteBuffer.wrap(bytes).getLong();
  }
}
