package com.edel.createDocument;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.connection.CEConnection;
import com.edel.config.ApplicationConfig;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

public class CreateDocument implements EventActionHandler {
	static Logger log = Logger.getLogger(CreateDocument.class);

	@Override
	public void onEvent(ObjectChangeEvent event, Id id) throws EngineRuntimeException {

		String clientIdValue=null;
		String productvalue=null;
		String uniqueIdgenerate=null;
		String uniqueIdValue=null;
		ContentTransfer contentTransfer=null;
		
		try
		{
			Document newDocument=(Document)event.get_SourceObject();

			String docId = newDocument.get_Id().toString();

			String name = newDocument.get_Name();

			System.out.println("Id1 is :::::::::::::::::::::"+id);

			System.out.println("Document11 is====="+name+"and the newDocument Id is======"+docId);

			Properties properties = newDocument.getProperties();

			Iterator iterator= properties.iterator();
			System.out.println("iterater:::::::::::::"+iterator.hasNext());

			while(iterator.hasNext()){
				System.out.println("Inside while::::::::::::::::");
				Property property = (Property) iterator.next();
				
				if (property != null && property.getPropertyName().equals("ClientId")) {

					clientIdValue = property.getStringValue();
					System.out.println("ClientId is===="+clientIdValue);
				}
				if (property != null && property.getPropertyName().equals("Product")) {

					productvalue = property.getStringValue();
					System.out.println("Product is===="+productvalue);

				}

				uniqueIdgenerate = clientIdValue+productvalue;

			//	document.getProperties().putValue("UniqueId",uniqueIdgenerate);

			}

			ObjectStore objectStore = event.getObjectStore();

			System.out.println("ObjectStore is:::::::::::::::::::::"+objectStore);

			String mySQLString = "SELECT Id FROM Demo WHERE ClientId = '"+clientIdValue+"' AND Product = '"+productvalue+"'";
			SearchSQL searchSQL = new SearchSQL(mySQLString);

			SearchScope searchScope = new SearchScope(objectStore);

			//RepositoryRowSet fetchRows = searchScope.fetchRows(searchSQL, null, null, null);
			IndependentObjectSet fetchObjects = searchScope.fetchObjects(searchSQL, null, null, null);

			System.out.println(fetchObjects.isEmpty()+"--------------fetchObjects.isEmpty()-----------------");
		

			Iterator iter= fetchObjects.iterator();
			int i=0;
			while (iter.hasNext()) {
                System.out.println("I Value:::::::::::::::::"+i++);
				IndependentObject rr = (IndependentObject) iter.next();
				System.err.println(rr.getProperties());
				com.filenet.api.property.Properties props = rr.getProperties();

				String ID = props.getIdValue("Id").toString();
				System.out.println("Document Id is::::::::::::::::::::"+ID);
				System.out.println("Pnly ID:::::::::::"+ID+"docID:::::::::::"+docId);
				// System.out.println(properties.getIdValue(prp));
				if(ID.equalsIgnoreCase(docId)){

					System.out.println("Consisting only single document with the given property values::::::::::::::::::::::");

				}else{

					Document oldDocument = Factory.Document.fetchInstance(objectStore,ID,null);

					//if (!doc.get_IsReserved().booleanValue()){ 
					oldDocument = (Document)oldDocument.get_CurrentVersion();
					oldDocument.checkout(ReservationType.EXCLUSIVE, null, null, null);
						
					oldDocument.save(RefreshMode.REFRESH);
				//	}
					System.out.println("After Checkout::::::::::::::::::::::::::");

					ContentElementList contentList =Factory.ContentElement.createList();

					ContentElementList newDocContentElementList = newDocument.get_ContentElements();

					Iterator iterator3 = newDocContentElementList.iterator();

					while (iterator3.hasNext()) {

						ContentTransfer object1 = (ContentTransfer) iterator3.next();
						InputStream accessContentStream = object1.accessContentStream();
						ContentTransfer content =Factory.ContentTransfer.createInstance();
						content.setCaptureSource(accessContentStream);
						contentList.add(content);
						System.out.println("New Content element added:::");

					}

					Document reservation = (Document)oldDocument.get_Reservation();
					reservation.set_ContentElements(contentList);
					System.out.println("Is reserverd::::"+oldDocument.get_IsReserved());

					reservation.checkin(AutoClassify.AUTO_CLASSIFY,CheckinType.MAJOR_VERSION);

					reservation.save(RefreshMode.REFRESH);
					//doc.checkin(AutoClassify.AUTO_CLASSIFY,CheckinType.MAJOR_VERSION);
					oldDocument.save(RefreshMode.REFRESH);
					System.out.println("Old Document saved:::::::::::::::::::::");

					Document newDoc = Factory.Document.fetchInstance(objectStore,docId,null);

					newDoc.delete();

					newDoc.save(RefreshMode.REFRESH);
					System.out.println("New Document Deleted:::::::::::::::::::");
					break;

				}

				System.out.println(i+ "time While Called");

			}


		}
		catch(Exception e)
		{

			log.error("exception has raised"+e.getMessage());
			System.out.println("exception has raised"+e.getMessage()); 
			System.out.println("exception has raised"+e); 
			e.printStackTrace();
		}

	}

}
