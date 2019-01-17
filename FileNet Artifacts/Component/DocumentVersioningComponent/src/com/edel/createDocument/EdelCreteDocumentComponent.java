package com.edel.createDocument;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.connection.CEConnection;
import com.connection.SessionClass;
import com.edel.config.ApplicationConfig;
import com.edel.constants.ASPConstants;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.property.Properties;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWAttachmentType;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWLibraryType;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;

public class EdelCreteDocumentComponent {
	static Logger log = Logger.getLogger(EdelCreteDocumentComponent.class);

	public int edelCreateDocument(VWAttachment attachment)
	{

		return 1;
	}
	public int edelCreateDocument(VWAttachment attachment,String workFlowNumber){

		String clientIdValue=null;
		String productvalue=null;
		String uniqueIdgenerate=null;
		String mimeType=null;
		int noOfWorkitemsLaunched=0;
		ApplicationConfig config=ApplicationConfig.getApplicationConfig();
		CEConnection ceConnection =new CEConnection();

		System.out.println("Inside attachments --------------->"+attachment);

		//		ObjectStore objectStore = ceConnection.getObjectStore(config);

		Domain domain = ceConnection.getDomain(config);

		ObjectStore objStore = ceConnection.getObjectStore(config);

		System.out.println("After objectstore:::::::::::::::::::");
		//		attachment.

		System.out.println("library Name::" + attachment.getLibraryName());

		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain, attachment.getLibraryName());

		String id = attachment.getId();

		String attName = attachment.getAttachmentName();

		System.out.println("Attachment ID "+id);

		VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id( id), null);
		System.out.println("After version series::::::");
		Document attachDocument = (Document) vs.get_CurrentVersion();
		String versionseriesID = attachDocument.get_VersionSeries().get_Id().toString();
		mimeType = attachDocument.get_MimeType();
		String attachmentName = attachDocument.get_Name();
		System.out.println("Vesrion series id of new attcahment"+versionseriesID);
		System.out.println("Attachment Name :::::::::::"+attachmentName);
		try{
			Id docID = attachDocument.get_Id();

			//UpdateDocumentSecurity(objectStore,docID);

			System.out.println("Document ID:::::::::::"+docID);
			Properties properties = attachDocument.getProperties();
			properties.putValue("isWorkflowLaunched", true);
			attachDocument.save(RefreshMode.REFRESH);
			clientIdValue =properties.getStringValue(config.getProperty(ASPConstants.CLIENT));
			System.out.println("Client Value is:::::::::::::"+clientIdValue);
			productvalue = properties.getStringValue(config.getProperty(ASPConstants.PRODUCT));
			System.out.println("Product Value is:::::::::::::"+productvalue);

			//In Progress

			uniqueIdgenerate = clientIdValue+productvalue;

			attachDocument.save(RefreshMode.REFRESH);
			//folder creation Start
			System.out.println("Entered into folder creation:::::::::::::");

			//	String folderlocation = "/" + clientIdValue + "/" + productvalue + "/" + uniqueIdgenerate;

			String folderlocation = "/" + clientIdValue + "/" + productvalue;

			String fnFolders[] = folderlocation.split("/");

			String folderParent = "";

			for (int i = 1; i < fnFolders.length; i++) {

				if (i == 1) {
					folderParent = "/";
				}

				Connection conn= null;

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
						folderParent =folderParent + fnFolder;
					} else {
						folderParent = folderParent + "/" + fnFolder;
					}

				}

				System.out.println("folderParent :" + folderParent);
			}

			String filenetFolderLocation = folderParent;

			Document document = Factory.Document.fetchInstance(objStore, docID, null);

			System.out.println("filenetFolderLocation : " + filenetFolderLocation);

			Folder folder = Factory.Folder.fetchInstance(objStore, filenetFolderLocation, null);

			System.out.println("Document Filed in location : " + filenetFolderLocation);

			//Folder Creation end

			String mySQLString = "SELECT * FROM DMS WHERE Client = '"+clientIdValue+"' AND Product = '"+productvalue+"' AND  [VersionStatus] = 1";
			SearchSQL searchSQL = new SearchSQL(mySQLString);

			SearchScope searchScope = new SearchScope(objectStore);

			//RepositoryRowSet fetchRows = searchScope.fetchRows(searchSQL, null, null, null);
			IndependentObjectSet fetchObjects = searchScope.fetchObjects(searchSQL, null, null, null);

			Iterator iter= fetchObjects.iterator();

			int i=0;
			int noOfUniqDocsInSytem =0;
			while (iter.hasNext()) {
				noOfUniqDocsInSytem++;
				System.out.println("I Value:::::::::::::::::"+i++);

				IndependentObject rr = (IndependentObject) iter.next();

				System.err.println(rr.getProperties());

				com.filenet.api.property.Properties props = rr.getProperties();

				String ID = props.getIdValue("Id").toString();

				String statusValue = props.getStringValue(config.getProperty(ASPConstants.STATUS));

				System.out.println("Document Status is::::::::::::::::::::"+statusValue);

				System.out.println("Old Document ID is:::::::::::"+ID+" New Document ID is:::::::::::"+docID);

				String status=config.getProperty(ASPConstants.STATUS_VALUES);

				String emptyStatus=config.getProperty(ASPConstants.EMPTY_STATUS);

				boolean emptyvalueCheck=false;

				if(emptyStatus.equalsIgnoreCase(statusValue)){
					emptyvalueCheck=true;
					System.out.println("Inside Empty value Check and Status Value Is:::"+emptyStatus+"Status Value On Document"+statusValue);
				}

				System.out.println("Status is::::::::::::::"+status);
				//"In Process,Archive"
				String[] configStatus=status.split(",");

				boolean doCheckin=false;

				for (String sts : configStatus) {

					if(sts.equalsIgnoreCase(statusValue))

					{
						doCheckin=true;
					}
				}

				if(ID.equalsIgnoreCase(docID.toString())){

					System.out.println("same Document :::::::::::::::::");

				}

				else if(!ID.equalsIgnoreCase(docID.toString()) && (doCheckin || emptyvalueCheck)){

					System.out.println("Inside else if Condition");

					Document oldDocument = Factory.Document.fetchInstance(objectStore,ID,null);

					oldDocument = (Document)oldDocument.get_CurrentVersion();

					oldDocument.checkout(ReservationType.EXCLUSIVE, null, null, null);

					oldDocument.save(RefreshMode.REFRESH);

					System.out.println("Document Checkout Completed::::::::::::::::::::::::::");

					ContentElementList contentList =Factory.ContentElement.createList();

					ContentElementList newDocContentElementList = attachDocument.get_ContentElements();

					Iterator iterator3 = newDocContentElementList.iterator();

					while (iterator3.hasNext()) {

						ContentTransfer object1 = (ContentTransfer) iterator3.next();

						InputStream accessContentStream = object1.accessContentStream();

						ContentTransfer content =Factory.ContentTransfer.createInstance();

						content.setCaptureSource(accessContentStream);

						content.set_RetrievalName(attachment.getAttachmentName());

						contentList.add(content);


						System.out.println("New Content element added:::");

					}
					Document reservation = (Document)oldDocument.get_Reservation();

					reservation.set_ContentElements(contentList);

					reservation.set_MimeType(mimeType);

					reservation.checkin(AutoClassify.AUTO_CLASSIFY,CheckinType.MAJOR_VERSION);

					reservation.save(RefreshMode.REFRESH);

					System.out.println("Old Document saved:::::::::::::::::::::");

					/*attachDocument.delete();

					attachDocument.save(RefreshMode.REFRESH);*/

					System.out.println("Newly added Document Deleted:::::::::::::::::::");

					VWSession peSession = SessionClass.get_Session();

					String indexName = null;

					Object[] firstValues = null;

					Object[] lastValues = null;

					int queryFlags = VWRoster.QUERY_NO_OPTIONS;

					String taskfilter="UniqueId="+'\''+uniqueIdgenerate +'\'';

					int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;

					VWRoster roster = peSession.getRoster(config.getProperty(ASPConstants.ROSTER));

					VWRosterQuery rosterQuery = roster.createQuery(indexName, firstValues, lastValues, queryFlags, taskfilter, null, fetchType);

					noOfWorkitemsLaunched=rosterQuery.fetchCount();
					System.out.println("Number Of Workitems Launched :::::::::"+noOfWorkitemsLaunched);
					if(noOfWorkitemsLaunched >1)
					{

						while(rosterQuery.hasNext()){

							VWWorkObject workObj = (VWWorkObject) rosterQuery.next();

							String newStepName = workObj.getStepName();

							System.out.println("Inside While WorkFlowNumber"+workFlowNumber);

							if(workObj.getWorkflowNumber().equalsIgnoreCase(workFlowNumber))
							{
								System.out.println("Same Workflow Item ");
							}
							else
							{

								VersionSeries get_VersionSeries = reservation.get_VersionSeries();

								Id reservationDocID = get_VersionSeries.get_Id();
								String reservationVesrionID = reservationDocID.toString();
								VWStepElement stepElement =	workObj.fetchStepElement();
								stepElement.doLock(true);
								VWAttachment att = new VWAttachment();

								att.setAttachmentName(attName);
								att.setId(reservationVesrionID);
								att.setType(VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT);
								att.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
								att.setLibraryName(config.getProperty(ASPConstants.OBJECTSTORE));
								System.out.println("attachment::::" +att);
								String stepName = stepElement.getStepName();
								String[] parameterNames = stepElement.getParameterNames();

								for (int j = 0; j < parameterNames.length; j++) {

									String parameters = parameterNames[j];

									if(parameters.equalsIgnoreCase(config.getProperty(ASPConstants.ATTACHMENT_NAME))){

										stepElement.setParameterValue(config.getProperty(ASPConstants.ATTACHMENT_NAME), att, true);
									}
									
								}
								stepElement.doSave(true);
								
							}
						}
					}

					Document doc = Factory.Document.fetchInstance(objStore,docID, null);
					doc.delete();
					doc.save(RefreshMode.REFRESH);
				}

				else
				{
					//More Than two work flows launched or already work flow processed from In Process
					noOfWorkitemsLaunched=2;
					//Duplicate documents should not be fields in any folder

				}

			}
			System.out.println(i+ "times While Called & noOfWorkitemsLaunched ::"+noOfWorkitemsLaunched);
			System.out.println(" Number Of Uniq Docs In Sytem ::"+noOfUniqDocsInSytem);
			if(noOfUniqDocsInSytem==1)
			{
				ReferentialContainmentRelationship rel = folder.file(document, AutoUniqueName.AUTO_UNIQUE, attachmentName,DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);

				rel.save(RefreshMode.REFRESH);
			}


		}
		catch(Exception e)
		{

			log.error("exception has raised"+e.getMessage());

			System.out.println("exception has raised"+e.getMessage()); 

			System.out.println("exception has raised"+e); 

			e.printStackTrace();
		}


		return noOfWorkitemsLaunched;
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
			CustomObject proxyObj=getDMSFolderSecurityProxy(objStore);
			folderObj.getProperties().putObjectValue("CustomProxyTemplate", proxyObj);
			// setting the Folder Location
			folderObj.set_Parent(folderlocation);
			// save the folder
			folderObj.save(RefreshMode.REFRESH);
			System.out.println("createfnFolder Start");
			flag = true;
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	public boolean checkfnFolder(String fnFolder, ObjectStore objStore, Connection fn) {
		// TODO Auto-generated method stub
		System.out.println("checkfnFolder Start");
		boolean flag = false;
		try {
			Folder folderObj = Factory.Folder.fetchInstance(objStore, fnFolder, null);
			flag = true;
			System.out.println("checkfnFolder End");
		} catch (Exception e) {
			System.err.println("checkfnFolder folder not found ::");
			flag = false;
		}
		return flag;
	}


	public void DocumentStringPropertyUpdate(VWAttachment attachment,String symbolicName,String value){

		ApplicationConfig config=ApplicationConfig.getApplicationConfig();
		CEConnection ceConnection =new CEConnection();
		System.out.println("Inside DocumentStringPropertyUpdate :::::: Start::::::::::"+attachment);
		//		ObjectStore objectStore = ceConnection.getObjectStore(config);

		Domain domain = ceConnection.getDomain(config);
		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain, attachment.getLibraryName());
		System.out.println("After objectstore:::::::::::::::::::"+objectStore);
		//		attachment.
		String id = attachment.getId();
		VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id( id), null);

		Document attachDocument = (Document) vs.get_CurrentVersion();
		attachDocument.getProperties().putValue(symbolicName, value);

		attachDocument.save(RefreshMode.REFRESH);
		System.out.println("Inside DocumentStringPropertyUpdate :::::: End::::::::::"+attachment);

	}

	public CustomObject getDMSFolderSecurityProxy(ObjectStore os) {


		log.info("getCustomObj start");
		CustomObject custObj=null;
		SearchSQL sqlObject = new SearchSQL();
		String sqlstr ="SELECT [This], [Id] FROM [CustomObjectProxy] WHERE [Title] = 'DMS Folder Proxy' OPTIONS(TIMELIMIT 180)";
		sqlObject.setQueryString(sqlstr);
		log.info("CE SQL: " + sqlObject.toString()); 
		SearchScope searchscope=new SearchScope(os);
		RepositoryRowSet reprowset=searchscope.fetchRows(sqlObject,null, null, true);
		Iterator it=reprowset.iterator();
		while (it.hasNext()) {
			RepositoryRow rep1=(RepositoryRow)it.next();
			custObj=Factory.CustomObject.fetchInstance(os, rep1.getProperties().get("id").getIdValue(), null);

			log.info("getCustomObj is Availabe :: custObj"+custObj);
			return custObj;
		}
		log.info("getCustomObj  is end ::");

		return custObj;


	}

	public void UpdateDocumentSecurity(ObjectStore os,Id docID) {


		CustomObject proxyObj=getDMSFolderSecurityProxy(os);
		Document document = Factory.Document.fetchInstance(os, docID, null);

		document.getProperties().putObjectValue("CustomProxyTemplate", proxyObj);
		document.save(RefreshMode.REFRESH);

	}
	public void updateSecurityProxy(VWAttachment attachment)
	{

		ApplicationConfig config=ApplicationConfig.getApplicationConfig();
		CEConnection ceConnection =new CEConnection();
		System.out.println("Inside DocumentStringPropertyUpdate :::::: Start::::::::::"+attachment);
		//		ObjectStore objectStore = ceConnection.getObjectStore(config);

		Domain domain = ceConnection.getDomain(config);
		ObjectStore objectStore = Factory.ObjectStore.getInstance(domain, attachment.getLibraryName());

		String id = attachment.getId();

		String attName = attachment.getAttachmentName();

		System.out.println("Attachment ID "+id);

		VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id( id), null);
		System.out.println("After version series::::::");
		Document attachDocument = (Document) vs.get_CurrentVersion();
		String versionseriesID = attachDocument.get_VersionSeries().get_Id().toString();
		System.out.println("Vesrion series id of new attcahment"+versionseriesID);


		Id docID = attachDocument.get_Id();

		UpdateDocumentSecurity(objectStore,docID);

	}
	public CustomObject getDMSDocumentSecurityProxy(ObjectStore os) {


		log.info("getCustomObj start");
		CustomObject custObj=null;
		SearchSQL sqlObject = new SearchSQL();
		String sqlstr ="SELECT [This], [Id] FROM [CustomObjectProxy] WHERE [Title] = 'DMS Document Proxy' OPTIONS(TIMELIMIT 180)";
		sqlObject.setQueryString(sqlstr);
		log.info("CE SQL: " + sqlObject.toString()); 
		SearchScope searchscope=new SearchScope(os);
		RepositoryRowSet reprowset=searchscope.fetchRows(sqlObject,null, null, true);
		Iterator it=reprowset.iterator();
		while (it.hasNext()) {
			RepositoryRow rep1=(RepositoryRow)it.next();
			custObj=Factory.CustomObject.fetchInstance(os, rep1.getProperties().get("id").getIdValue(), null);

			log.info("getCustomObj is Availabe :: custObj"+custObj);
			return custObj;
		}
		log.info("getCustomObj  is end ::");

		return custObj;


	}


}
