package com.edel.createDocument;

import java.util.Date;

import com.connection.CEConnection;
import com.edel.config.ApplicationConfig;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;

import filenet.vw.api.VWAttachment;

public class UpdateArchivalDateInCE {

	public void UpdateArchivalDate(Date archivaldate, VWAttachment attach){
		try{

			System.out.println("UpdateArchival Date on Document Start:::::::::::::::::");

			System.out.println("Date at archival step1:::::::::::"+archivaldate);

			ApplicationConfig config=ApplicationConfig.getApplicationConfig();

			CEConnection ceConnection =new CEConnection();

			System.out.println("Inside attachment --------------->"+attach);

			//		ObjectStore objectStore = ceConnection.getObjectStore(config);

			Domain domain = ceConnection.getDomain(config);

			ObjectStore objectStore = Factory.ObjectStore.getInstance(domain, attach.getLibraryName());

			System.out.println("ObjectStore::::::::::::::::::::::"+objectStore);

			String id = attach.getId();

			System.out.println("Attachment ID "+id);

			VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id( id), null);

			System.out.println("Inside Vesrion series::::::");

			Document attachDocument = (Document) vs.get_CurrentVersion();

			//String versionseriesID = attachDocument.get_VersionSeries().get_Id().toString();

			Id docID = attachDocument.get_Id();

			Document document = Factory.Document.fetchInstance(objectStore,docID, null);

			System.out.println("Document Id to update Archival Date:::::::::::"+docID);

			Properties properties = document.getProperties();

			properties.putValue("DateofArchival", archivaldate);

			document.save(RefreshMode.REFRESH);

			System.out.println("After updating values:::"+document.getProperties().getDateTimeValue("DateofArchival"));

			System.out.println("UpdateArchival Date on Document End:::::::::::::::::");
		}

		catch(Exception e)
		{

			System.out.println("exception has raised"+e.getMessage());

			System.out.println("exception has raised"+e.getMessage()); 

			System.out.println("exception has raised"+e); 

			e.printStackTrace();
		}


	}
}
