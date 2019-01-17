package com.mits.edelweis.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.mits.edelweiss.VWUtils.PropertiesUtil;

public class FinalReport{
	Logger log = Logger.getLogger(FinalReport.class);


	public <report> HSSFWorkbook finalreport()
	{
		
    //	Logger log = Logger.getLogger(FinalReport.class);

	Properties props = PropertiesUtil.getInstance();
		EdelweissOperation exportdata  = new EdelweissOperation();

		EdelweissOperation eventdata  = new EdelweissOperation();
        DBdatacapreport dbreport= new DBdatacapreport();
		String pbcount=dbreport.pendingCount();

		JSONArray finalJsonArrayReportData;
		{

			finalJsonArrayReportData = exportdata.Rosterexport();
			log.info("++++++++++++++++++++"+finalJsonArrayReportData);
			HSSFWorkbook exportdata1 = new HSSFWorkbook();
			HSSFSheet sheet = exportdata1.createSheet("Document Count Report");
			HSSFSheet sheet1 = exportdata1.createSheet("Workflow TAT");
			//HeadersDocument Count Report

			// exportdata1.set
			 HSSFCellStyle my_style = exportdata1.createCellStyle();
             /* Create HSSFFont object from the workbook */
             HSSFFont my_font=exportdata1.createFont();
             /* set the weight of the font */
             my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
             /* attach the font to the style created earlier */
             my_style.setFont(my_font);
			
			HSSFRow createRow2=sheet.createRow(0);

			log.info("Final json array object data:::::::::::::"+finalJsonArrayReportData);
			createRow2.createCell(3).setCellValue("Status");
			createRow2.createCell(4).setCellValue("Count");
			log.info("Final json array object data"+finalJsonArrayReportData);
			
			
			JSONArray statusList = new JSONArray();
			JSONArray finalStatusList = new JSONArray();
		
			for (int i = 0; i < finalJsonArrayReportData.size(); i++) {
				
				JSONObject object = (JSONObject) finalJsonArrayReportData.get(i);

				log.info("status of work item:::"+object.get("Status").toString());
				
				statusList.add(object.get("Status").toString());
				//uniqSBU.add(object.get("Status").toString());
			}
			JSONArray uniqSBU= new JSONArray();
			uniqSBU.add("Pending");
			uniqSBU.add("In Progress");
			uniqSBU.add("Pending for dispatch");
			uniqSBU.add("Dispatched");
			uniqSBU.add("Archived");
			
				Iterator<String> iterator = uniqSBU.iterator();
				
				int temp=1;
				while(iterator.hasNext()){
					String next = iterator.next();
					log.info("next"+next);
					int count=0;
					for(int i=0 ;i<statusList.size(); i++){
						if(next.equals(statusList.get(i))){
							count++;
						}
						
					}
					HSSFRow createRow1 = sheet.createRow(temp);
					
					if("Pending".equals(props.getProperty("Status."+next.replace(" ","")))){
						createRow1.createCell(3).setCellValue(props.getProperty("Status."+next.replace(" ","")));
						
						log.info("Status of pending count:::::::::::: "+"Status."+next.trim());
						
						createRow1.createCell(4).setCellValue(pbcount);
					}else{
					createRow1.createCell(3).setCellValue(props.getProperty("Status."+next.replace(" ","")));
					
					log.info("Status of count :::::::::::::::::::::::"+"Status."+next.trim());
					
					createRow1.createCell(4).setCellValue(count);
					}
					temp++;
					
				}
			
			HSSFRow createRow = sheet1.createRow(0);
			createRow.createCell(4).setCellValue(props.getProperty("LOB"));
			createRow.createCell(5).setCellValue(props.getProperty("Product"));
			createRow.createCell(3).setCellValue(props.getProperty("SBU"));
			createRow.createCell(2).setCellValue(props.getProperty("Entity"));
			createRow.createCell(6).setCellValue(props.getProperty("RecordName"));
			createRow.createCell(7).setCellValue(props.getProperty("WFCreatedDate"));
			createRow.createCell(1).setCellValue(props.getProperty("Client"));
			createRow.createCell(12).setCellValue(props.getProperty("DateofArchival"));
			createRow.createCell(10).setCellValue(props.getProperty("DispatchDate"));
			createRow.createCell(8).setCellValue(props.getProperty("ScanDate"));
			createRow.createCell(9).setCellValue(props.getProperty("TagDate"));
			createRow.createCell(0).setCellValue(props.getProperty("UniqueId"));
			createRow.createCell(13).setCellValue(props.getProperty("Status"));
			createRow.createCell(11).setCellValue(props.getProperty("ProcessCompleteDate"));



			for (int i = 0; i < finalJsonArrayReportData.size(); i++) {
				Row createRow1 = sheet1.createRow(i+1);
				JSONObject object = (JSONObject) finalJsonArrayReportData.get(i);
				for (int j = 0; j < object.size(); j++) {
					createRow1.createCell(4).setCellValue(object.get("LOB").toString());
					createRow1.createCell(5).setCellValue(object.get("Product").toString());
					createRow1.createCell(3).setCellValue(object.get("SBU").toString());
					createRow1.createCell(2).setCellValue(object.get("Entity").toString());
					createRow1.createCell(6).setCellValue(object.get("RecordName").toString());
					createRow1.createCell(7).setCellValue(object.get("WFCreatedate").toString());
					createRow1.createCell(1).setCellValue(object.get("Client").toString());
					createRow1.createCell(12).setCellValue(object.get("DateofArchival").toString());
					createRow1.createCell(10).setCellValue(object.get("DispatchDate").toString());
					createRow1.createCell(8).setCellValue(object.get("ScanDate").toString());
					createRow1.createCell(9).setCellValue(object.get("TagDate").toString());
					createRow1.createCell(0).setCellValue(object.get(props.getProperty("UniqueId")).toString());
					createRow1.createCell(13).setCellValue(object.get("Status").toString());
					createRow1.createCell(11).setCellValue(object.get("ProcessCompleteDate").toString());

				}
			}
			FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream("./Exportdata.xls");
			exportdata1.write(fileOut);
			fileOut.close();
			//Closing the workbook
			exportdata1 = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			//report exportdata1 = null;
			return exportdata1;
	}
		
	}
	
	public static void main(String[] args) {
		
		FinalReport report = new FinalReport();
		
		report.finalreport();
	}
	}


