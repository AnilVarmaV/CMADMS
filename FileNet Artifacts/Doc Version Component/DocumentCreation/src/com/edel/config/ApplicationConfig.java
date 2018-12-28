


/**
 * 
 */
package com.edel.config;

/**
 * @author P00109639
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.edel.constants.ASPConstants;

public  class ApplicationConfig 
{

	private static Properties properties;
	private static ApplicationConfig config;

	private ApplicationConfig() 
	{
		try
		{	
			properties = new Properties();
			properties.load(new FileInputStream(ASPConstants.PROPERTY_FILE_PATH));
		}
		catch (IOException ex) 
		{
			ex.printStackTrace();
			Logger.getLogger(ApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
		}
 
	}

	public static ApplicationConfig getApplicationConfig() 
	{
		if(config == null)
			config = new ApplicationConfig();
		return config;
	}

	public String getProperty(String propName) 
	{
		return properties.getProperty(propName);
	}

	public void doEmptyProps(){
		
		if(properties!=null){
			properties=null;
		}
		
		if(config!=null){
			config=null;
		}
	}
}
