package com.edel.createDocument;

import com.connection.CEConnection;
import com.edel.config.ApplicationConfig;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.util.Id;

import filenet.vw.api.VWAttachment;

public class DocumentPropertyUpdate {


	public void DocumentStringPropertyUpdate(VWAttachment attachment,String symbolicName,String value){

		ApplicationConfig config=ApplicationConfig.getApplicationConfig();

		CEConnection ceConnection =new CEConnection();

		System.out.println("Inside attachment ::::::::::::::::"+attachment);

		//		ObjectStore objectStore = ceConnection.getObjectStore(config);

		Domain domain = ceConnection.getDomain(config);

		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain, attachment.getLibraryName());

		System.out.println("After objectstore:::::::::::::::::::");
		//		attachment.

		System.out.println("library Name::" + attachment.getLibraryName());

		String id = attachment.getId();

		System.out.println("Attachment ID "+id);


		VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id( id), null);

		System.out.println("After version series:::::::::::::::::::");

		Document attachDocument = (Document) vs.get_CurrentVersion();

		attachDocument.getProperties().putValue(symbolicName, value);

		attachDocument.save(RefreshMode.REFRESH);

	}

}
