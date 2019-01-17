package com.mits.edelweis.export;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mits.edelweiss.VWUtils.PropertiesUtil;

public class DBdatacapreport {
	 Logger log=Logger.getLogger(DBdatacapreport.class);

	private static final String count = null;

	public String pendingCount(){
		// String urlPrefix = "jdbc:db2:";
		
		try {
			Properties props = PropertiesUtil.getInstance();

			String url;
			String user;
			String password;
			String state;
			Connection con;
			Statement stmt;
			ResultSet rs;

			log.info("**** Enter class EzJava");

			url = props.getProperty("DB_URL");
			user = props.getProperty("USER_NAME");
			password = props.getProperty("DB_PASSWORD");
			
			Class.forName(props.getProperty("DRIVER_CLASS"));
			log.info("**** Loaded the JDBC driver");

			con = DriverManager.getConnection(url, user, password);

			con.setAutoCommit(false);
			log.info("**** Created a JDBC connection to the data source"+con);

			stmt = con.createStatement();
			log.info("**** Created JDBC Statement object");

				rs = stmt.executeQuery(props.getProperty("Qery"));
			log.info("**** Created JDBC ResultSet object");

			while (rs.next()) {
				
				
				String count = rs.getString(1);
				
				log.info("Status of pending documents Count= " + count);
				

			}
			log.info("**** Fetched all rows from JDBC ResultSet");

			//	rs.close();
			log.info("**** Closed JDBC ResultSet");

			stmt.close();
			log.info("**** Closed JDBC Statement");

			con.commit();
			log.info("**** Transaction committed");

			con.close();
			log.info("**** Disconnected from data source");

			log.info("**** JDBC Exit from class EzJava - no errors");
             
		}

		catch (ClassNotFoundException e) {
			System.err.println("Could not load JDBC driver");
			log.info("Exception: " + e);
			e.printStackTrace();
		}

		catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
		return count;
		
	}
	
	public static void main(String[] args) {
		
		DBdatacapreport dbreport =new DBdatacapreport();
		
		String pbcount=dbreport.pendingCount();
		
		//log.info("pbcount :::::::::::::::"+pbcount);
	}
}
