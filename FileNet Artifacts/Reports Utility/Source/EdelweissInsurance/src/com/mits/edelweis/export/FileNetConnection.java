package com.mits.edelweis.export;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.mits.edelweiss.VWUtils.PropertiesUtil;

import filenet.vw.api.VWQueue;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWSession;

public class FileNetConnection {
	
	 Logger log = Logger.getLogger(FileNetConnection.class);

	Properties props = PropertiesUtil.getInstance();
	private VWSession session = null;
	public VWSession getSession() {
		return session;
	}
	public void setSession(VWSession session) {
		this.session = session;
	}	
	
	 public ObjectStore getCEConnection() throws Exception{
			try {
				log.info("Entering GetSession::::::::::::");

				
							
				String objStoreId = props.getProperty("OS_NAME");
				
				//String jassLogin = props.getProperty("JAAS_LOGIN");
			//	String wasLocation = props.getProperty("WAS_LOCATION");
				String ceURI = props.getProperty("CE_URI");
				String ceUser = props.getProperty("CE_USER_ID");
				String cePwd = props.getProperty("CE_PASSWORD");
				String decryptPW = CryptoUtil.decryptPhrase(cePwd);
				String stanzName = props.getProperty("STANZ_NAME");
				
				log.info("ceURI ::::::"+ceURI);
				log.info("ceUser ::::::"+ceUser);
				log.info("cePwd ::::::"+cePwd);
				log.info("stanzName"+stanzName);
				log.info("objStoreId:::::::::::"+objStoreId);
				
			//	logger.debug("JAAS Login - " + jassLogin + ", WAS - " + wasLocation+ ", CE URI - " + ceURI);
			//	System.setProperty("java.security.auth.login.config", jassLogin);
			//	System.setProperty("wasp.location", wasLocation);
				//System.setProperty("filenet.pe.bootstrap.ceuri", ceURI);
				Connection conn = Factory.Connection.getConnection(ceURI);
				UserContext uc = UserContext.get();
				uc.pushSubject(UserContext.createSubject(conn, ceUser,decryptPW, stanzName));
				
				log.info("CE Conn ::::::"+conn);
//				UserContext uc = UserContext.get();
//				Subject sub =  Subject.getSubject(AccessController.getContext()); 
//				uc.pushSubject(sub);
				
				Domain domain = Factory.Domain.fetchInstance(conn,props.getProperty("Domain"), null);
				log.info("domain ::::::::"+domain);
				ObjectStore os = Factory.ObjectStore.fetchInstance(domain, objStoreId, null);
				
				log.info(domain+"..."+os);
				return os;
			} catch (Exception e) {
				log.info("Exception caused at getSessionConnection() method  :: "+e.getMessage(),e);
				e.printStackTrace();
				throw e;
			}
			
		}
	
	public VWSession createPEConnection()throws Exception {
		VWSession vwsession=null;
		try {
			Properties props = PropertiesUtil.getInstance();
			
			String jassLogin = props.getProperty("JAAS_LOGIN");
			String wasLocation = props.getProperty("WAS_LOCATION");
			String ceURI = props.getProperty("CE_URI");
			String ceUser = props.getProperty("CE_USER_ID");
			String cePwd = props.getProperty("CE_PASSWORD");
			log.info("Encry :::::::pw"+cePwd);
			String decryptPW = CryptoUtil.decryptPhrase(cePwd);
			log.info("decryptPW :::::::pw"+decryptPW);
			String connPoint = props.getProperty("ROUTER");
			/*
			logger.debug("Entering createPEConnection");
			System.setProperty("java.security.auth.login.config", jassLogin);
			System.setProperty("wasp.location", wasLocation);
			System.setProperty("filenet.pe.bootstrap.ceuri", ceURI);
		
			session = new VWSession(ceUser, cePwd, connPoint);
			setSession(session);*/
			
			vwsession=new VWSession();
			vwsession.setBootstrapCEURI(ceURI);
			vwsession.logon(ceUser, decryptPW, connPoint);
			
			
			log.info(vwsession.isLoggedOn());
			log.info("VWSession="+vwsession);//FNCP
			log.info("Log on succesful");
			return vwsession;
		} catch(Exception e) {
			log.error("Exception Creating PE Connection - " + e.getMessage());
			throw e;
		}
				
	}
	public static void main(String[] args) throws Exception {
		FileNetConnection fcConnection = new FileNetConnection();
		fcConnection.getCEConnection();
		VWSession createPEConnection = fcConnection.createPEConnection();
		//VWRoster roster = createPEConnection.getRoster("DefaultRoster");
	//log.info("roster:::::::"+createPEConnection);
	//	
		
		
	}

}
