package com.mits.edelweiss.VWUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	public static String jaasLogin=null;
	public static String wasLocation=null;
	public static String ceURI=null;
	public static String ceUser=null;
	public static String cePwd=null;
	public static String stanzName=null;
	public static String connPoint=null;
	public static String qName=null;
	public static String rosterName=null;
	public static String objStoreId=null;
	public static String lob=null;
		
	private static Properties props =null;

	public static Properties getInstance(){
		try{
			if(props!= null ){
				return props;
			}
			props=new Properties();
			
			//InputStream inputStream = new FileInputStream("E:/DMS 2.0 DEV/MG_PTM_Jars/MG.properties");
			//InputStream inputStream = new FileInputStream("/MG_PTM_Jars/MG.properties");
			//InputStream inputStream = new FileInputStream("D:/DMS_CONFIG/PFRL.properties");
			InputStream inputStream = new FileInputStream("./UpdateValues.properties");
			//InputStream inputStream = new FileInputStream("D:/MG.properties");
			props.load(inputStream);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return props;
		
	}
		
	public static void readCEConnDetails(){
		props = getInstance();
		
		System.out.println("entered in to connetion class");
		jaasLogin = props.getProperty("JAAS_LOGIN");
		
		System.out.println(jaasLogin);
		wasLocation = props.getProperty("WAS_LOCATION");
		System.out.println(wasLocation);
		ceURI = props.getProperty("CE_URI");
		
		System.out.println(ceURI);
		
		ceUser = props.getProperty("CE_USER_ID");
		cePwd = props.getProperty("CE_PASSWORD");
		stanzName = props.getProperty("STANZ_NAME");
		connPoint = props.getProperty("ROUTER");
		rosterName = props.getProperty("ROSTER_NAME");
		objStoreId = props.getProperty("OS_NAME");
		lob = props.getProperty("LOB");

	}
	

}
