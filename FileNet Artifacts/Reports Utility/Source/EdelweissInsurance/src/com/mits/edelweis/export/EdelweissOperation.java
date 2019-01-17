package com.mits.edelweis.export;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.mits.edelweiss.VWUtils.PropertiesUtil;

import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWLog;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWLogQuery;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;
import filenet.vw.base.logging.VWEventLogHandler;



public class EdelweissOperation {

	 Logger log = Logger.getLogger(EdelweissOperation.class);



	Properties props = PropertiesUtil.getInstance();

	private static int inprogresscount;
	private static int pendingfordispatchcount1;
	private static int dispatchedcount2;
	private static int archivedcount3;



	@SuppressWarnings({ "hiding", "deprecation" })
	public  JSONArray Rosterexport(){


		final String F_Subject = props.getProperty("F_Subject");

		FileNetConnection filenetConn = null;
		@SuppressWarnings("rawtypes")
		String indexName = null;
		Object[] firstValues = null;
		Object[] lastValues = null;
		String filter ="F_Subject ='"+F_Subject+"'";
		Object[] substitutionVars=null;
		int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;

		JSONArray jsonArray = new JSONArray();

		try {
			filenetConn = new FileNetConnection();
			log.info(filenetConn);
			VWSession session = filenetConn.createPEConnection();
			log.info("createPEConnection+++++++"+session);
			log.info("this is session++++"+session.isLoggedOn());
			int queryFlags = VWRoster.QUERY_NO_OPTIONS;;
			VWRoster roster = session.getRoster(props.getProperty("Roster"));
			VWRosterQuery rosterQuery = roster.createQuery(indexName, firstValues, lastValues, queryFlags, filter, null, fetchType);
			//log.info("Fetch Count:::::::"+fetchCount);
			while (rosterQuery.hasNext()) {
				//temp++;
				JSONObject jsonObject = new JSONObject();
				VWWorkObject workWob = (VWWorkObject)rosterQuery.next();
				log.info("wob param value:::::::::::::"+workWob.getDataField(props.getProperty("LOB")));
				log.info("wob param value Client:::::::::::::"+workWob.getDataField("Client"));
				log.info("wob param value PacketId:::::::::::::"+workWob.getDataField("PacketId"));
				log.info("value:::::::::::"+workWob.getDataField(props.getProperty("Client")));
				jsonObject.put("Client", workWob.getDataField("Client").toString());
				jsonObject.put("LOB", workWob.getDataField(props.getProperty("LOB"))!= null? workWob.getDataField(props.getProperty("LOB")).toString():"");
				jsonObject.put("Entity", workWob.getDataField(props.getProperty("Entity"))!= null?  workWob.getDataField(props.getProperty("Entity")).toString():"");
				jsonObject.put("SBU", workWob.getDataField(props.getProperty("SBU"))!= null?  workWob.getDataField(props.getProperty("SBU")).toString():"");
				jsonObject.put("Product", workWob.getDataField(props.getProperty("Product"))!= null?  workWob.getDataField(props.getProperty("Product")).toString():"");
				jsonObject.put("Status",  workWob.getDataField(props.getProperty("Status"))!= null? workWob.getDataField(props.getProperty("Status")).toString():"");
				jsonObject.put("RecordName",  workWob.getDataField(props.getProperty("RecordName"))!= null? workWob.getDataField(props.getProperty("RecordName")).toString():"");	
				jsonObject.put("ScanDate",  workWob.getDataField(props.getProperty("ScanDate"))!= null? workWob.getDataField(props.getProperty("ScanDate")).toString():"");	
				jsonObject.put("TagDate",  workWob.getDataField(props.getProperty("TagDate"))!= null? workWob.getDataField(props.getProperty("TagDate")).toString():"");	
				jsonObject.put("UniqueId",  workWob.getDataField(props.getProperty("UniqueId"))!= null? workWob.getDataField(props.getProperty("UniqueId")).toString():"");	
				//	jsonObject.put("ProcessCompleteDate", workWob.getFieldValue("ProcessCompleteDate"));	


				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate = format.parse((workWob.getDataField(props.getProperty("WFCreatedate")).toString()));
				log.info("jsonObject::::::::::::::"+jsonObject);
				SimpleDateFormat print = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format2 = print.format(parsedDate);

				Date date=new Date(format2);

				/*SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate1 = format1.parse(workWob.getDataField(("DateofArchival")).toString());
				SimpleDateFormat print1 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format3 = print1.format(parsedDate1);

				Date date1=new Date(format3);
				 */


				SimpleDateFormat format4 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate2 = format4.parse(workWob.getDataField(("DispatchDate")).toString());

				SimpleDateFormat print2 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format5 = print2.format(parsedDate2);

				Date date2=new Date(format5);

				SimpleDateFormat format6 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate3 = format6.parse(workWob.getDataField(("ScanDate")).toString());
				SimpleDateFormat print3 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format7 = print3.format(parsedDate3);

				Date date3=new Date(format7);

				SimpleDateFormat format8 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate4 = format8.parse(workWob.getDataField(("TagDate")).toString());
				SimpleDateFormat print4 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format9 = print4.format(parsedDate4);

				Date date4=new Date(format9);
				SimpleDateFormat format10 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date parsedDate5 = format10.parse(workWob.getDataField(("ProcessCompleteDate")).toString());
				SimpleDateFormat print5 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
				String format11 = print5.format(parsedDate5);

				Date date5=new Date(format11);

				log.info("date value "+date);

				jsonObject.put("WFCreatedate",date.toString());
				String field = workWob.getDataField("Status").toString();

				log.info("field values"+field);

				Date date1 = null;
				if(field.equalsIgnoreCase("In Progress")||field.equalsIgnoreCase("Pending for dispatch")||field.equalsIgnoreCase("Dispatched")){

					log.info("field values after if");


					SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date parsedDate1 = format1.parse(workWob.getDataField(("DateofArchival")).toString());
					SimpleDateFormat print1 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
					String format3 = print1.format(parsedDate1);

					date1=new Date(format3);

					log.info("Date of Archival:::::::::::"+date1);

				}

				if(field.equalsIgnoreCase("In Progress")||field.equalsIgnoreCase("Pending for dispatch")||field.equalsIgnoreCase("Dispatched"))
				{
					jsonObject.put("DateofArchival","");
				}
				else
				{
					jsonObject.put("DateofArchival",date1.toString());

				}
				if(field.equalsIgnoreCase("In Progress")||field.equalsIgnoreCase("Pending for dispatch"))
				{
					jsonObject.put("DispatchDate","");
				}
				else
				{
					jsonObject.put("DispatchDate",date2.toString());

				}
				if(field.equalsIgnoreCase("In Progress"))
				{
					jsonObject.put("ProcessCompleteDate","");
				}
				else
				{
					jsonObject.put("ProcessCompleteDate",date5.toString());

				}
				//	jsonObject.put("DispatchDate",date2.toString());
				jsonObject.put("ScanDate",date3.toString());
				jsonObject.put("TagDate",date4.toString());
				jsonArray.add(jsonObject);			

				String field1 = workWob.getDataField("Status") != null ? workWob.getDataField("Status").toString():"";

				if(field1.equalsIgnoreCase("In Progress")||field1.equalsIgnoreCase("Pending for dispatch")||field1.equalsIgnoreCase("Dispatched")){
					inprogresscount++;
				}
			}
			log.info("JSONNarray:::::"+jsonArray+"::::Roster In Progress Count::::"+inprogresscount);

			Eventlogreport elreport = new Eventlogreport();

			JSONArray eventlogexport = elreport.Eventlogexport();

			for (int i = 0; i < eventlogexport.size(); i++) {
				jsonArray.add((JSONObject)eventlogexport.get(i));
			}

			log.info("Final JONarray:::::"+jsonArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
}
