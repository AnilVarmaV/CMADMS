package com.connection;

import javax.security.auth.Subject;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Logger;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.edel.config.ApplicationConfig;
import com.edel.constants.ASPConstants;
import com.filenet.api.core.Connection;
import com.filenet.api.exception.EngineRuntimeException;

public class CEConnection {

	static Logger logger = Logger.getLogger(CEConnection.class);
	
	static ObjectStore objectStore =null;
	
	public ObjectStore getObjectStore(ApplicationConfig config){
		
		//Connection connection =null;
		String encPassword=config.getProperty(ASPConstants.FN_PASSWORD);
		System.out.println("Ecrypted Password:::::::::::::"+encPassword);
		String userName =config.getProperty(ASPConstants.FN_USER_NAME);
		String stanzaName=config.getProperty(ASPConstants.STANZA);
		
		String decPassword = CryptoUtil.decryptPhrase(encPassword);
		
		System.out.println("Decrypted Password:::::::::::::"+decPassword);
		
		try{
			
			System.out.println("Login user1 name  and URI"+ config.getProperty(ASPConstants.FN_SERVER_URI)  +"----"+config.getProperty(ASPConstants.FN_USER_NAME)+"____"+config.getProperty(ASPConstants.OBJECTSTORE));
			
			Connection connection = Factory.Connection.getConnection(config.getProperty(ASPConstants.FN_SERVER_URI));
			
			 //connection = Factory.Connection.getConnection("http://172.16.8.75:9080/wsi/FNCEWS40MTOM");
			
			//creating the subject using usercontext to populate the credentials to maintaune user specific state
			  // Subject subject=UserContext.createSubject(connection,"fnp8admin",decPassword,"FileNetP8WSI");
			 Subject subject=UserContext.createSubject(connection,userName,decPassword,stanzaName);
			   //getting the usercontext object
			   UserContext userContext=UserContext.get();
			  
			   //pushing the subject to the usercontext stack
			   userContext.pushSubject(subject);
			  
			   Domain domain = Factory.Domain.fetchInstance(connection,null,null);
			   
			   System.out.println("domain::"+domain);
			   System.out.println("Object Store Name: " +config.getProperty(ASPConstants.OBJECTSTORE));
			   objectStore = Factory.ObjectStore.fetchInstance(domain,config.getProperty(ASPConstants.OBJECTSTORE),null);
			   System.out.println("Objectstore in con class::::::::::::"+objectStore);
			   
		}catch(Exception e){
			 System.out.println("Error in getting ObjectStore ::::: "+e.getMessage());
			 System.out.println(e);
		
		}
		
		return objectStore;
	}
	
	public ObjectStore returnObjectStore(ApplicationConfig config){
		
		if(objectStore==null){
			getObjectStore(config);
		}
		
		return objectStore;
	}
	
	public void logOff(){
		UserContext userContext=UserContext.get();
		if(userContext!=null){
			userContext.popSubject();
			objectStore=null;
		}
	}
	
	public Domain getDomain(ApplicationConfig config){
		
		Domain domain = null;
		
		try{
			String encPassword=config.getProperty(ASPConstants.FN_PASSWORD);
			
			System.out.println("Ecrypted Password1:::::::::::::"+encPassword);
			
			String decPassword = CryptoUtil.decryptPhrase(encPassword);
			
			System.out.println("Decrypted Password1:::::::::::::"+decPassword);
			
			System.out.println("Config user13 name  and URI::"+ config.getProperty(ASPConstants.FN_SERVER_URI)  +"----"+config.getProperty(ASPConstants.FN_USER_NAME)+"____"+config.getProperty(ASPConstants.OBJECTSTORE));
			
			Connection con = Factory.Connection.getConnection(config.getProperty(ASPConstants.FN_SERVER_URI));
			
			 
			// connection = Factory.Connection.getConnection("http://172.16.8.75:9080/wsi/FNCEWS40MTOM");
			 
			//creating the subject using usercontext to populate the credentials to maintaune user specific state
			 
			   Subject subject=UserContext.createSubject(con,config.getProperty(ASPConstants.FN_USER_NAME),decPassword,config.getProperty(ASPConstants.STANZA));
			   
			  
			   //getting the usercontext object
			   UserContext userContext=UserContext.get();
			  
			   //pushing the subject to the usercontext stack
			   userContext.pushSubject(subject);
			  
			   domain = Factory.Domain.fetchInstance(con,null,null);
			   
			   System.out.println("domain::"+domain);
			  /* System.out.println("Object Store Name: " +config.getProperty(ASPConstants.OBJECTSTORE));
			   objectStore = Factory.ObjectStore.fetchInstance(domain,config.getProperty(ASPConstants.OBJECTSTORE),null);
			   System.out.println("Objectstore in con class::::::::::::"+objectStore);*/
			   
		}catch(Exception e){
			 System.out.println("Error in getting ObjectStore ::::: "+e.getMessage());
			 System.out.println(e);
		
		}
		
		
		return domain;
	}
}
