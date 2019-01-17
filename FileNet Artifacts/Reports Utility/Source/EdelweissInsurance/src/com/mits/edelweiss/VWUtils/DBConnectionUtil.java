package com.mits.edelweiss.VWUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DBConnectionUtil {
	
	private static String DRIVER_CLASS;
	private static String DB_URL;
	private static String DB_USER;
	private static String DB_PASSWORD;
	
	private Logger logger = Logger.getLogger(DBConnectionUtil.class);
	private Connection dbConnection;
	
	private void readDBProperties() throws Exception{
		InputStream inputStream = null;
		try {
			
			Properties props = PropertiesUtil.getInstance();
			
			logger.debug("Properties file initialised");
			DRIVER_CLASS = props.getProperty("DRIVER_CLASS");
			DB_USER = props.getProperty("USER_NAME");
			DB_PASSWORD = props.getProperty("PASSWORD");
			DB_URL = props.getProperty("DATABASE_URL");
//			DB_URL = "jdbc:db2://192.168.5.69:50000/MGUSER";
//			DB_PASSWORD = "filenet@123";
			//schema_name = props.getProperty("SCHEMA_NAME");
			logger.debug("Driver Class: " + DRIVER_CLASS);
			logger.debug("Driver URL: " + DB_URL);
			logger.debug("Driver USER: " + DB_USER);
			logger.debug("Driver PASSWORD: " + DB_PASSWORD);

		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if(inputStream!=null){
				inputStream.close();
			}
		}
	}
	
	/**
	 * Connects to database and returns the connection object
	 * @return connection object
	 * @throws Exception
	 */
	public Connection connectToDatabase() throws Exception{

		try {
			logger.info("Method DBConnection.dBConn()");
		
			readDBProperties();
			
			Class.forName(DRIVER_CLASS);
			Connection connection = DriverManager.getConnection(DB_URL,
						DB_USER, DB_PASSWORD);
			setDbConnection(connection);
			
			return connection;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DB Connection issue,URl is :" + DB_URL,e);
			throw new Exception("Error while connecting to database");
		}

		
	}
	/**
	 * Close DB Connection
	 * @param dbConn
	 */
	public void closeDBConnection(){
		try {
			Connection dbConn =getDbConnection();
			if(dbConn!=null && !dbConn.isClosed()){
				dbConn.close();
				dbConn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public Connection getDbConnection() {
		return dbConnection;
	}
	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

}
