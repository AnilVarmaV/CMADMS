package com.mits.edelweis.export;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

public class ReadProperty
{
  private static Properties props = null;
  private static ReadProperty readProp = new ReadProperty();
  private static final String path = "/util.properties";
  
  public final Properties getAsProperties()
  {
    Properties props = null;
    try
    {
      props = new Properties();
      // Dev path
      
      InputStream input = null;
		input = getClass().getResourceAsStream(path);
		
      props.load(input);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Error loading properties for ");
    }
    return props;
  }
  
  private synchronized Properties getDetails()
  {
    if (props != null) {
      return props;
    }
    try
    {
      props = getAsProperties();
      return props;
    }
    catch (Exception e)
    {
      props = null;
      throw new RuntimeException("Exception while reading config.properties:" + e.getMessage());
    }
  }
  
  public static String getProperty(String key)
  {
    return readProp.getDetails().getProperty(key);
  }
}
