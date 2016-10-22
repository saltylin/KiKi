package cn.edu.nju.pasalab.kiki.common;

import cn.edu.nju.pasalab.kiki.common.util.FormatUtils;

import com.google.common.base.Preconditions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Configuration {
  /** The default property file name. */
  private static final String DEFAULT_PROPERTY_FILE = "kiki-default.properties";
  /** The singleton maintains all the properties. */
  private static final Properties PROPERTIES = new Properties();
  /** The {@link Configuration} singleton. */
  private static final Configuration CONF = new Configuration();

  static {
    // Default properties
    InputStream is =
        Configuration.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTY_FILE);
    Preconditions.checkNotNull(is);
    try {
      PROPERTIES.load(is);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load the property file", e);
    }

    // Properties in kiki.properties
    String userPropertyFileName = System.getProperty(Constants.CONF_FILE);
    Properties userProperties = loadUserProperties(userPropertyFileName);
    if (userProperties != null) {
      PROPERTIES.putAll(userProperties);
    }

    // System properties
    Properties systemProperties = System.getProperties();
    PROPERTIES.putAll(systemProperties);
  }

  private Configuration() {} // Prevent initialization

  /**
   * Load the user specified property file.
   *
   * @param path the property file path
   * @return the user specified properties
   */
  private static Properties loadUserProperties(String path) {
    try {
      Properties ret = new Properties();
      FileInputStream in = new FileInputStream(path);
      ret.load(in);
      return ret;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * @return the configuration singleton
   */
  public static Configuration getInstance() {
    return CONF;
  }

  /**
   * Gets the bytes of the value for the given key.
   *
   * @param attr the key to get the value for
   * @return the bytes of the value for the given key
   */
  public long getBytes(String attr) {
    String rawValue = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(rawValue);
    return FormatUtils.parseSpaceSize(rawValue);
  }

  /**
   * Gets the boolean representation of the value for the given key.
   *
   * @param attr the key to get the value for
   * @return the value for the given key as a {@code boolean}
   */
  public boolean getBoolean(String attr) {
    String rawValue = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(rawValue);
    return Boolean.parseBoolean(rawValue);
  }

  /**
   * Gets an int property.
   *
   * @param attr the attribute
   * @return an int value
   */
  public int getInt(String attr) {
    String value = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(value);
    return Integer.parseInt(value);
  }

  /**
   * Gets a long property.
   *
   * @param attr the attribute
   * @return a long value
   */
  public long getLong(String attr) {
    String value = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(value);
    return Long.parseLong(value);
  }

  /**
   * Gets a float property.
   *
   * @param attr the attribute
   * @return a float value
   */
  public float getFloat(String attr) {
    String value = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(value);
    return Float.parseFloat(value);
  }

  /**
   * Gets a double property.
   *
   * @param attr the attribute
   * @return a double value
   */
  public double getDouble(String attr) {
    String value = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(value);
    return Double.parseDouble(value);
  }

  /**
   * Gets a string property.
   *
   * @param attr the attribute
   * @return a string value
   */
  public String getString(String attr) {
    String value = PROPERTIES.getProperty(attr);
    Preconditions.checkNotNull(value);
    return value;
  }
}
