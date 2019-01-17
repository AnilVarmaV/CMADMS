package com.edel.createDocument;

import java.io.InputStream;
import java.util.Iterator;

import com.connection.CEConnection;
import com.connection.SessionClass;
import com.edel.config.ApplicationConfig;
import com.edel.constants.ASPConstants;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWAttachmentType;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWLibraryType;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;

public class Test {
	
	public void checkNoOfWorkItems(String uniqueIdgenerate,String rosterName)
	{
		int queryFlags = VWRoster.QUERY_NO_OPTIONS;
		VWSession peSession = SessionClass.get_Session();
		String taskfilter="UniqueId="+'\''+uniqueIdgenerate +'\'';

		//System.out.println("Unique Id is::::::::::"+uniqueIdgenerate);

		int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;

		VWRoster roster = peSession.getRoster(rosterName);

		VWRosterQuery rosterQuery = roster.createQuery(null, null, null, queryFlags, taskfilter, null, fetchType);

		int noOfWorkitemsLaunched=rosterQuery.fetchCount();
		
		System.out.println("noOfWorkitemsLaunched ::"+noOfWorkitemsLaunched);
	}


/*	public static void docFileInFolder() {
		
		ApplicationConfig config=ApplicationConfig.getApplicationConfig();
		CEConnection ceConnection =new CEConnection();
		Domain domain = ceConnection.getDomain(config);
		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain,"FNOS");
		System.out.println("Objectstore is"+  objectStore);



		String versionseriesID ="{A4CDC35B-6A51-C384-8E2A-67C09A600000}".toString();

		VWSession peSession = SessionClass.get_Session();

		String indexName = null;

		Object[] firstValues = null;

		Object[] lastValues = null;

		String filter="cp";

		int queryFlags = VWRoster.QUERY_NO_OPTIONS;

		String taskfilter="UniqueId="+'\''+filter+'\'';

		int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;

		VWRoster roster = peSession.getRoster("DefaultRoster");

		VWRosterQuery rosterQuery = roster.createQuery(indexName, firstValues, lastValues, queryFlags, taskfilter, null, fetchType);

		System.out.println("noOfWorkitemsLaunched :::::::::"+rosterQuery);

		while(rosterQuery.hasNext()){
			System.out.println("Inside while");
			VWWorkObject workObj = (VWWorkObject) rosterQuery.next();
			VWStepElement stepElement =	workObj.fetchStepElement();
			VWAttachment att = new VWAttachment();
			att.setAttachmentName("TestDoc");
			att.setAttachmentDescription("Added by code");
			att.setId(versionseriesID);
			att.setType(VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT);
			att.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
			att.setLibraryName(config.getProperty(ASPConstants.OBJECTSTORE));
			System.out.println("Example add  attachment" +att);
			String stepName = stepElement.getStepName();
			System.out.println("Step name is::::::::::::"+stepName);

			String[] parameterNames = stepElement.getParameterNames();

			
			stepElement.setParameterValue("SBU", "Test", true);
			String attachmentDescription = att.getAttachmentDescription();
			System.out.println(attachmentDescription+" 97");
			String id = att.getId();
			System.out.println(id+" 99");
			stepElement.setParameterValue(config.getProperty(ASPConstants.ATTACHMENT_NAME), att, true);
			for (int j = 0; j < parameterNames.length; j++) {
				System.out.println("insid for");
				String parameters = parameterNames[j];

				stepElement.doLock(true);
				if(parameters.equalsIgnoreCase("docAttachment")){
					
					System.out.println("Inside if");

					System.out.println("Doc attachment 899"+config.getProperty(ASPConstants.ATTACHMENT_NAME));
					stepElement.setParameterValue("SBU", "Test", true);
					String attachmentDescription = att.getAttachmentDescription();
					System.out.println(attachmentDescription+" 97");
					String id = att.getId();
					System.out.println(id+" 99");
					stepElement.setParameterValue(config.getProperty(ASPConstants.ATTACHMENT_NAME), att, true);
					System.out.println("Doc attachment 999");
				}

				//stepElement.doLock(true);
				//VWAttachment attachment2=	(VWAttachment)stepElement.get
				//System.out.println("Attachment is"+attachment2);
				//System.out.println("Attachment property name is"+config.getProperty(ASPConstants.ATTACHMENT_NAME));
				//stepElement.setParameterValue(config.getProperty(ASPConstants.ATTACHMENT_NAME), reservation, true);
				//stepElement.setParameterValue("docAttachment",att, true);
				stepElement.doSave(true);
			}

		}
	*/
	
	public void createFolder(String clientIdValue, String productvalue,ObjectStore objStore ) 
	{

		String folderlocation = "/" + clientIdValue + "/" + productvalue;

		String fnFolders[] = folderlocation.split("/");

		String folderParent = "";

		for (int i = 1; i < fnFolders.length; i++) {

			Connection conn= null;
			
			if (i == 1) {
				folderParent = "/";
			}

			String fnFolder = fnFolders[i];
			
			if (!fnFolder.equalsIgnoreCase("") && fnFolder != null) {
				System.out.println("parent folder :" + folderParent);
				System.out.println("fnFolder :" + fnFolder);
				boolean checkFolder = checkfnFolder(folderParent + "/" + fnFolder, objStore, conn);
				System.out.println("response of checkfnFolder :" + checkFolder);
				if (checkFolder == false && clientIdValue!=null && productvalue!=null ) {
					boolean createFolderFlag = createfnFolder(fnFolder, folderParent, objStore, conn);
				}
				if (i == 1) {
					folderParent ="/" + fnFolder;
				} else {
					folderParent = folderParent + "/" + fnFolder;
				}

			}

			System.out.println("folderParent :" + folderParent);
		}
	}
	
	public boolean createfnFolder(String fnFolder, String fnFolderLocation, ObjectStore objStore, Connection fn) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			System.out.println("createfnFolder Start");
			String classType = "Folder";
			// parent Folder instance fetch
			Folder folderlocation = Factory.Folder.fetchInstance(objStore, fnFolderLocation, null);
			// Create the Folder 
			Folder folderObj = Factory.Folder.createInstance(objStore, classType);
			// setting the Folder Name
			folderObj.set_FolderName(fnFolder);
/*			CustomObject proxyObj=getDMSFolderSecurityProxy(objStore);
			folderObj.getProperties().putObjectValue("CustomProxyTemplate", proxyObj);*/
			// setting the Folder Location
			folderObj.set_Parent(folderlocation);
			// save the folder
			folderObj.save(RefreshMode.REFRESH);
			System.out.println("createfnFolder End");
			flag = true;
		} catch (Exception e) {
			System.err.println("createfnFolder exception is ::"+e);
		}
		return flag;
	}

	public boolean checkfnFolder(String fnFolder, ObjectStore objStore, Connection fn) {
		// TODO Auto-generated method stub
		boolean flag = false;
		System.out.println("checkfnFolder Start");
		try {
			Folder folderObj = Factory.Folder.fetchInstance(objStore, fnFolder, null);
			
			flag = true;
			System.out.println("checkfnFolder end");
		} 

		catch (Exception e) {
			System.err.println("checkfnFolder folder not found ::");
			flag = false;
		}
		return flag;
	}
	

	public static void main(String[] args) {
		//new Test().checkNoOfWorkItems("CMCUSTODY FX", "DefaultRoster");
		ApplicationConfig config=ApplicationConfig.getApplicationConfig();
		CEConnection ceConnection =new CEConnection();
		Domain domain = ceConnection.getDomain(config);
		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain,"FNOS");
		System.out.println("Objectstore is"+  objectStore);
		new Test().createFolder("Test94", "Demo2", objectStore);
		
		//System.out.println(new EdelCreteDocumentComponent().getDMSFolderSecurityProxy(objectStore));
		
		
	}

	}















