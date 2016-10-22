package cn.edu.nju.pasalab.kiki.common.cache;

import java.util.Arrays;

public final class BytesHolder {
  private final byte[] key;

  public BytesHolder(byte[] key) {
    this.key = key;
  }

  public byte[] getKey() {
    return key;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    byte[] thatKey = ((BytesHolder) obj).getKey();
    return Arrays.equals(key, thatKey);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(key);
  }
}
