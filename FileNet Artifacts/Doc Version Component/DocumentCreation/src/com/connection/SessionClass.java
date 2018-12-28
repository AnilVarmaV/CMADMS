package com.connection;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.edel.constants.ASPConstants;

import filenet.vw.api.VWSession;

public  class SessionClass {
	
	private static Properties properties;

	static Logger logger = Logger.getLogger(SessionClass.class);

	public static VWSession get_Session()
	{
		VWSession session=null;
		try
		{
			
			properties = new Properties();
			File file= new File(ASPConstants.PROPERTY_FILE_PATH);
			FileInputStream input=new FileInputStream(file);
			properties.load(input);
			session=new VWSession();
			session.setBootstrapCEURI(properties.getProperty("FN_SERVER_URI"));
			session.logon(properties.getProperty("FN_USER_NAME"),properties.getProperty("FN_PASSWORD"),properties.getProperty("FN_CONNECTIONPOINT"));
			System.out.println(" PE Session::::::"+session.isLoggedOn());
			
		


		}catch(Exception e)
		{
			e.printStackTrace();
		}

		return session;

	}
	
	public static void main(String[] args) {
		
		get_Session();
		
		
	}
}







