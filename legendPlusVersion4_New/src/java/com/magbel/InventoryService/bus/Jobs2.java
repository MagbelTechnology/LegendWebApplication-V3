package com.magbel.InventoryService.bus;
 
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;    
import java.util.Date;

import com.magbel.ia.legend.CompanyHandler;
import com.magbel.ia.vao.Customer;
import com.magbel.ia.vao.Transaction;
import com.magbel.ia.vao.Result;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import sun.net.www.URLConnection;

import java.net.HttpURLConnection;
import java.net.URL;
public class Jobs2 
    implements Job
{  
      
    private static Log _log = LogFactory.getLog(Jobs2.class);
    CompanyHandler comp = new CompanyHandler();
    com.magbel.util.DatetimeFormat df;
    public Jobs2()
    {
    
    }
  
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  try  
        {  
	 // Transaction trans = new Transaction();
	     java.util.ArrayList list =comp.getRfidRecords();
	     //System.out.println("Stock with new RFID Tag in the Store");
	     java.util.ArrayList list1 =comp.getRNewRfidRecords();
	     ArrayList listc = new ArrayList();
	     ArrayList classarmlist = new ArrayList();
	//     System.out.println("-->Inventory size>--> "+list.size()+"  list1 2: "+list1.size());
	     String subject = "ILLEGAL STOCK MOVEMENT";
	     
		//  df.getDateTime().substring(10);
		  	String sessionTimeOut = comp.getValue("select session_timeout from am_gb_company");
			     java.util.ArrayList listsignout =comp.getUsernotSignOutRecords(sessionTimeOut);
			 //    System.out.println("<<<<<<<======sessionTimeOut: "+sessionTimeOut+"     list1.size(): "+list1.size());
			     //Iso Starts	
	     
	     for(int j=0;j<list1.size();j++)
	     {
	    //	 System.out.println("-->Inventory size Loop>--> "+list1.size());
	    	 String errorMessage = "";
	    	 String processGTag = "N";
	    	 com.magbel.legend.vao.RFID  trans = (com.magbel.legend.vao.RFID)list1.get(j);
			String rfid = trans.getRfidTag();
			int mtid = trans.getMtid();
			String locationCode = trans.getLocation();
			System.out.println("Display rfid in Service: " + rfid+"   locationCode "+locationCode);
	    	String location = comp.getValue("SELECT LOCATION FROM AM_GB_LOCATION WHERE LOCATION_CODE = '"+locationCode+"' ");
	    	String existissuetag = comp.getValue("SELECT COUNT(*) FROM ST_STOCK WHERE BAR_CODE = '"+rfid+"' AND ASSET_STATUS = 'ACCEPTED' ");
	    	String existalltag = comp.getValue("SELECT COUNT(*) FROM ST_STOCK WHERE BAR_CODE = '"+rfid+"' ");
	    	String existforUtiltag = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_MATERIAL_USED WHERE RFID_TAG = '"+rfid+"' ");
	    	String usetagexist = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_RFID_USED WHERE RFID_TAG = '"+rfid+"' ");
	    	
	    	String existNewtag = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = '"+rfid+"' ");
	    	String tagsNotAccepted = comp.getValue("SELECT COUNT(*) FROM ST_STOCK WHERE BAR_CODE = '"+rfid+"' AND ASSET_STATUS = 'ACTIVE' ");
	//    	System.out.println("Display Read Tag  in Service: " + existtag);
	    	String existNewrec = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = '"+rfid+"' ");
	    	String existusedtag = comp.getValue("SELECT COUNT(*) FROM ST_STOCK_DISTRBUTE WHERE RFID_TAG = '"+rfid+"' ");
	    	String existusedrec = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_RFID_USED WHERE RFID_TAG = '"+rfid+"' ");
	    	if(existforUtiltag.equalsIgnoreCase("0") && !existissuetag.equalsIgnoreCase("0")){
	    		System.out.println("Display existtag in  Material Utilization: " + existforUtiltag+"   Used  RFID Tag: "+rfid);
	    		 boolean usedrec=comp.RFIDMaterialUtilizedTagList(rfid); 
	    		 processGTag = "Y";
	    	//	 System.out.println("Display usedrec in  Material Utilization: " + usedrec);
	    		 if(usedrec==true){comp.updateHandReaderRfidRecords(rfid);}
	    	 }
	    	if(tagsNotAccepted.equalsIgnoreCase("0") && usetagexist.equalsIgnoreCase("0")  && (!existNewtag.equalsIgnoreCase("0"))){
	    		System.out.println("Display existtag in Service: " + existissuetag+"   Used  RFID Tag: "+rfid);
	    		 boolean usedrec=comp.RFIDUsedTagList(rfid); 
	    		 processGTag = "Y";
	    	//	 System.out.println("Display existtag and usedrec in Service: " + usedrec+"   Used  RFID Tag: "+rfid);
	    		 if(usedrec==true){comp.updateHandReaderRfidRecords(rfid);}
	    	 }
	    	if(!tagsNotAccepted.equalsIgnoreCase("0") && existusedtag.equalsIgnoreCase("0")){
	    		System.out.println("Display Tags for Distributions in Service: " + tagsNotAccepted+"   Used  RFID Tag: "+rfid);
	    		 boolean usedrec=comp.RFIDTagForDistributionList(rfid); 
	    		 processGTag = "Y";
	    	//	 System.out.println("Display usedrec for Distributions in Service: " + usedrec);
	    		 if(usedrec==true){comp.updateHandReaderRfidRecords(rfid);}
	    	 }	    	
	    	if((existalltag.equalsIgnoreCase("0"))  && (existNewtag.equalsIgnoreCase("0"))  && (existNewrec.equalsIgnoreCase("0"))){
	    		 System.out.println("Display new Tag in Service: " + existalltag+"   New  RFID Tag: "+rfid);
	    		 String existAssignTag = comp.getValue("SELECT *FROM ST_INVENTORY_NEW_RFID WHERE RFID_TAG = '"+rfid+"' ");
	    		if(existAssignTag.equalsIgnoreCase("")){ boolean newrec=comp.RFIDNewTagList(rfid);}
	    		 else{comp.updateHandReaderRfidRecords(rfid);}
	    		processGTag = "Y";
	    	 }
	    	comp.updateHandReaderRfidRecords(rfid);
	    //	if(processGTag.equalsIgnoreCase("N")){comp.updateHandReaderRfidRecords(rfid);}
	    	System.out.println("Display Existing Tag Status: " + processGTag);
        }	
	     for(int i=0;i<list.size();i++)
	     {
	    	 String errorMessage = "";
	    	 String distributionTag1 = "";
	    	 String distributionTag2 = "";
	    	// String transactionId=(String) list.get(i);
	    	// System.out.println("-->>--> "+transactionId);
	    	 com.magbel.legend.vao.RFID  trans = (com.magbel.legend.vao.RFID)list.get(i);
			String rfid = trans.getRfidTag();
			int mtid = trans.getMtid();
			String locationCode = trans.getLocation();
			String scannStatus = trans.getScannStatus();
			String scannType = trans.getScannType();
			String groupid = trans.getGroupid();
			String createDateTime = trans.getCreateDateTime();
			String createDate = trans.getCreateDate();	   
			String processed = trans.getProcessed();
			String stockName = comp.getValue("SELECT DESCRIPTION FROM ST_STOCK WHERE BAR_CODE = '"+rfid+"' ");
	    	String location = comp.getValue("SELECT LOCATION FROM AM_GB_LOCATION WHERE LOCATION_CODE = '"+locationCode+"' ");
	   // 	String msgText1 = comp.getValue("SELECT MAIL_DESCRIPTION FROM AM_MAIL_STATEMENT WHERE MAIL_CODE = '2' ");
	    	String msgText1 = "Stock with Name "+stockName+" has been illegaly taken out";
	    	String newtagCount = comp.getValue("SELECT COUNT(*) FROM ST_INVENTORY_RFID WHERE RFID_TAG = '"+rfid+"' ");
	    	if(newtagCount.equalsIgnoreCase("0")){distributionTag1 = comp.getValue("SELECT COUNT(*) FROM ST_DISTRIBUTION_ITEM WHERE RFID_TAG = '"+rfid+"' ");}
	    	if(newtagCount.equalsIgnoreCase("0")){distributionTag2 = comp.getValue("SELECT COUNT(*) FROM ST_DISTRIBUTION_ITEM WHERE RFID_TAG = '"+rfid+"' AND Accept_Reject != 'ACCEPTED' ");}
	    	String emailaddress = comp.getValue("SELECT MAIL_ADDRESS FROM AM_MAIL_STATEMENT WHERE MAIL_CODE = '2' ");
	//    	 System.out.println("Jobs2 Tranaction processed "+processed+"     scannStatus: "+scannStatus);
	    	//  if((processed.equalsIgnoreCase("N")) && (scannStatus.equalsIgnoreCase("O")) && (!accept.equalsIgnoreCase("ACCEPTED"))){
	    	//if((processed.equalsIgnoreCase("N")) && (scannStatus.equalsIgnoreCase("O"))){
	    	 if(distributionTag1.equalsIgnoreCase("0") || distributionTag2.equalsIgnoreCase("0")){
	//    		 System.out.println("I have sent an alarm Message for the stock with Tag No.: "+rfid);
	    		 comp.updateRfidVerifyRecords(rfid);
	    //		 System.out.println("-->>email--> "+emailaddress);
	   // 		 System.out.println("-->>msgText1--> "+msgText1);
//    	Alarm off Today 24/09/2018	 
	    		 comp.sendAlarm();
	    		 comp.sendMail(emailaddress, subject, msgText1);
	    		 
	     }
        }    
//	    System.out.println("Outside of Overall Tag Processing");

	     for(int i=0;i<listsignout.size();i++)
	     {
	    	 String userId=(String) listsignout.get(i);
	//    	 System.out.println("-->>userId--> "+userId);
	    		 comp.updateUsernotSignOutRecords(userId);
	     }	

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}

