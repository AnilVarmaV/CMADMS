package com.mits.edelweis.export;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.mits.edelweiss.VWUtils.PropertiesUtil;

import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWLog;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWLogQuery;
import filenet.vw.api.VWSession;

public class Eventlogreport {

	
	 Logger log = Logger.getLogger(Eventlogreport.class);

	Properties props = PropertiesUtil.getInstance();

	
	
	public  JSONArray Eventlogexport(){

		final String F_Subject = props.getProperty("F_Subject");
		//	private static final int F_EventType = 160;

		final String F_EventType = props.getProperty("F_EventType");

		final String F_StartTime = props.getProperty("F_StartTime");
		int archivedcount3;

	FileNetConnection filenetConn = null;
	@SuppressWarnings("rawtypes")
	String indexName = null;

	Object[] firstValues = null;
	Object[] lastValues = null;
	String filter ="F_Subject ='"+F_Subject+"' and F_EventType='"+F_EventType+"' and F_StartTime>='"+F_StartTime+"'";
	Object[] substitutionVars=null;
	int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;

	JSONArray jsonArray = new JSONArray();
	try {
		filenetConn = new FileNetConnection();
		log.info(filenetConn);
		VWSession session = filenetConn.createPEConnection();
		//logger.info("eForm :"+eForm);
		log.info("createPEConnection+++++++"+session);
		log.info("this is session++++"+session.isLoggedOn());
		//	VWQueue queue = session.getQueue("Archiving");
		//log.info("=======queue Name======"+queue.getName());
		log.info("Before Event log");
		int queryFlags = VWLog.QUERY_NO_OPTIONS;
		/*	VWQueueQuery queueQuery  = queue.createQuery(indexName, firstValues, lastValues, queryFlags, filter, substitutionVars, fetchType);
		int fetchCount = queueQuery.fetchCount();*/

		VWLog eventLog = session.fetchEventLog(props.getProperty("EventLog"));

		VWLogQuery eventQuery = eventLog.startQuery(indexName, firstValues, lastValues, queryFlags, filter, null);
		//log.info("Fetch Count:::::::"+fetchCount);
		int counter = 0;
		VWLogElement logElement;
		String[] fieldNames;
		int i;
		log.info("Log Element Count: "+eventQuery.fetchCount());

		//int archivedcount3 = 0;
		
		
		while(eventQuery.hasNext()) {
			logElement = (VWLogElement) eventQuery.next();
			JSONObject jsonObject = new JSONObject();

		
			log.info("Client value:::::"+logElement.getDataField("Client").toString());

			if("Archived".equalsIgnoreCase(logElement.getDataField(props.getProperty("Status")).toString()))
			{


			jsonObject.put("Client", logElement.getDataField("Client").toString());		        
			jsonObject.put("LOB", logElement.getDataField("LOB").toString());
			jsonObject.put("Entity", logElement.getDataField(props.getProperty("Entity"))!= null?  logElement.getDataField(props.getProperty("Entity")).toString():"");
			jsonObject.put("SBU", logElement.getDataField(props.getProperty("SBU"))!= null?  logElement.getDataField(props.getProperty("SBU")).toString():"");
			jsonObject.put("Product", logElement.getDataField(props.getProperty("Product"))!= null?  logElement.getDataField(props.getProperty("Product")).toString():"");
			jsonObject.put("Status",  logElement.getDataField(props.getProperty("Status"))!= null? logElement.getDataField(props.getProperty("Status")).toString():"");
			jsonObject.put("RecordName",  logElement.getDataField(props.getProperty("RecordName"))!= null? logElement.getDataField(props.getProperty("RecordName")).toString():"");	
			//jsonObject.put("ScanDate",  logElement.getDataField(props.getProperty("ScanDate"))!= null? logElement.getDataField(props.getProperty("ScanDate")).toString():"");	
			//jsonObject.put("TagDate",  logElement.getDataField(props.getProperty("TagDate"))!= null? logElement.getDataField(props.getProperty("TagDate")).toString():"");	
			jsonObject.put("UniqueId",  logElement.getDataField(props.getProperty("UniqueId"))!= null? logElement.getDataField(props.getProperty("UniqueId")).toString():"");	


			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate = format.parse((logElement.getDataField(props.getProperty("WFCreatedate")).toString()));
			log.info("WF_Created date::::::::::::::"+logElement.getDataField(props.getProperty("WFCreatedate")).toString());
			log.info("jsonObject::::::::::::::"+jsonObject);

			SimpleDateFormat print = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format2 = print.format(parsedDate);

			Date date=new Date(format2);

			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate1 = format1.parse(logElement.getDataField(("DateofArchival")).toString());
			SimpleDateFormat print1 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format3 = print1.format(parsedDate1);

			Date date1=new Date(format3);

			SimpleDateFormat format4 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate2 = format4.parse(logElement.getDataField(("DispatchDate")).toString());
			SimpleDateFormat print2 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format5 = print2.format(parsedDate2);

			Date date2=new Date(format5);
			SimpleDateFormat format6 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate3 = format6.parse(logElement.getDataField(("ScanDate")).toString());
			SimpleDateFormat print3 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format7 = print3.format(parsedDate3);
			Date date3=new Date(format7);

			SimpleDateFormat format8 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate4 = format8.parse(logElement.getDataField(("TagDate")).toString());
			SimpleDateFormat print4 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format9 = print4.format(parsedDate4);
			Date date4=new Date(format9);

			SimpleDateFormat format10 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date parsedDate5 = format10.parse(logElement.getDataField(("ProcessCompleteDate")).toString());
			SimpleDateFormat print5 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
			String format11 = print5.format(parsedDate5);
			Date date5=new Date(format11);
			log.info("date value "+date);
			jsonObject.put("WFCreatedate",date.toString());
			jsonObject.put("DateofArchival",date1.toString());
			jsonObject.put("DispatchDate",date2.toString());
			jsonObject.put("ScanDate",date3.toString());
			jsonObject.put("TagDate",date4.toString());
			jsonObject.put("ProcessCompleteDate", date5.toString());
			jsonArray.add(jsonObject);			

			//logElement.getDataField(props.getProperty("Status");
			String field = logElement.getDataField("Status") != null ? logElement.getDataField("Status").toString():"";

			if(field.equalsIgnoreCase("Archived")){
				//archivedcount3++;
			}
		log.info("JSONarray:::::"+jsonArray+":::: event log Count::::");
			}
	}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return jsonArray;
	}
}
