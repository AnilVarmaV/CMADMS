package com.edel.createDocument;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.connection.CEConnection;
import com.edel.config.ApplicationConfig;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.apiimpl.query.parser.ParseException;

public class Delete {
	
	public static void main(String[] args) throws ParseException {
		
		Date archivaldate=null;
	ApplicationConfig config=ApplicationConfig.getApplicationConfig();

	CEConnection ceConnection =new CEConnection();

	System.out.println("Inside attachment --------------->");
    ObjectStore objectStore = ceConnection.getObjectStore(config);

	Domain domain = ceConnection.getDomain(config);

	
	Document document = Factory.Document.fetchInstance(objectStore,"{76862EDA-8835-CF2F-8667-67CA3FA00000}", null);
	
	Properties properties = document.getProperties();
	
	SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	
		        String strdate2 = "02-04-2013 11:35:42";
	
		        try {
	
		            Date newdate = dateformat2.parse(strdate2);
	
		            System.out.println(newdate);
		            properties.putValue("DateofArchival", newdate);

		        	document.save(RefreshMode.REFRESH);
		        } catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	
	
	

}
}
