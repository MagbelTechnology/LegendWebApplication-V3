package com.magbel.fixedassetcreationLegendPlusFIXFAC.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

import legend.admin.handlers.CompanyHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
 
public class JobsBraVisit
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs6.class);
    CompanyHandler comp = new CompanyHandler();

    public JobsBraVisit()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	  String currentDate =comp.findObject(" SELECT SUBSTRING((select CONVERT(VARCHAR(20),(SELECT getdate()),120)),1,17)+''+'00' ");
	     java.util.ArrayList slalist =comp.getBranchVisitSqlRecords();
	     System.out.println("<<<<<<<<====SLA Records=====>> "+slalist.size() );
	      //================================
	     for(int i=0;i<slalist.size();i++)
	     {
	    	 magma.net.vao.BranchVisit  brVisit = (magma.net.vao.BranchVisit )slalist.get(i);    	 
			String id =  brVisit.getId();
//			System.out.println("<<<<<<<<==id===>> "+id);
			String branchCode = brVisit.getBranchCode();
			String inspectedBy = brVisit.getInspectedBy();
			String dateInspect = brVisit.getDateInspect();
			String dueDate = brVisit.getDueDate();
			String visitSummary = brVisit.getVisitsummary();
			String strstatus = brVisit.getStatus();
			String transDate = brVisit.getTransDate();
			String sNo = brVisit.getSNo();
			String element = brVisit.getElement();
			String remark = brVisit.getRemark();
			String actionby = brVisit.getActionby();
			String condition = brVisit.getCondition();
			String status =  brVisit.getStatus();
			String userId = brVisit.getUserId();

//			 String processTransCount =comp.findObject("SELECT COUNT(*) FROM SLA_PROCESSING  WHERE CRITERIA_ID = '"+id+"' AND ALERTSTARTDATE = '"+alertStartDate+"'");

		       String resolveresultDate =comp.findObject(" SELECT DATEDIFF(MINUTE, '"+currentDate+"', '"+dueDate+"') ");
		      String header = "FACILITY BRANCH VISIT MANAGEMENT";
		      String body = visitSummary;
 //System.out.println("start_date: " + start_date + " end_date: "+end_date+"   resultvalue: "+resultvalue+"   resultDate: "+resultDate);
//		        System.out.println("alertStartDateTest: " + alertStartDateTest + " currentDate: "+currentDate+"    Test: "+test);
			String eMail = comp.findObject("SELECT email FROM am_gb_User WHERE USER_ID = '"+userId+"'");
//			System.out.println("=====alertStartDate.compareTo(currentDate): "+alertStartDate.compareTo(currentDate));
//			System.out.println("=====alertStartDateTest.compareTo(Test): "+alertStartDateTest.compareTo(test)+"    processTransCount: "+processTransCount);
//		System.out.println("<<<<<<<<<<<<body: "+body+"  alertStartDate: "+alertStartDate+"    currentDate: "+currentDate+"   status: "+status+"   userName: "+userName+"    resolveDay: "+resolveDay+"   endDate: "+endDate+"    email: "+email+"    resolveresultDate: "+resolveresultDate);	
		if(!eMail.equals("") && (Integer.parseInt(resolveresultDate) < 1) && !status.equalsIgnoreCase("DONE")){
			comp.insertMailRecords(eMail,header,body); 
//			System.out.println("=====resolveHour: "+resolveHour+"    ======resolveMinutes: "+resolveMinutes+"  processTransCount: "+processTransCount);
//			 String nextalertDate =comp.findObject(" (SELECT DATEADD(day, "+alertFreq+", '"+alertStartDate.substring(0,10)+"')) ");
//			 System.out.println("=====nextalertDate: "+nextalertDate+"    ======alterFreq: "+alertFreq);
//			 nextalertDate = nextalertDate.substring(0,10) +" "+resolveHour+":"+resolveMinutes+":"+"00";
//			 System.out.println("<<<<<<<<<<<<nextalertDate: "+nextalertDate);
//			 comp.insertSLAProcessingTransaction(id,alertFreq,alertStart,remainDay,alertStartDate); 
			status = "DONE";
			comp.updateBranchVisitJobRecords(id,status);
	     }
//		String escalatealertDate =comp.findObject("(SELECT DATEADD(day, "+Integer.parseInt(resolveDay)+", '"+endDate+"')) ");
//		 String escalateresultDate =comp.findObject(" SELECT DATEDIFF(MINUTE, '"+currentDate+"', '"+escalatealertDate+"') ");
////		System.out.println("=====escalatealertDate: "+escalatealertDate+"    ======currentDate: "+currentDate+"   escalateresultDate: "+escalateresultDate);
////		System.out.println("=====escalatealertDate.compareTo(currentDate): "+escalatealertDate.compareTo(currentDate));
//		if(!email.equals("") && Integer.parseInt(escalateresultDate) < 1){
//			comp.insertMailRecords(escalateMail,header,body); 
//		}
	     }  
	     //======================================
     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
 public String findDifference(String start_date,
            String end_date)
{
// SimpleDateFormat converts the
// string format to date object
SimpleDateFormat sdf
= new SimpleDateFormat(
"dd-MM-yyyy HH:mm:ss");
String result = "";
String datevalue = "";
// Try Class
try {

// parse method is used to parse
// the text from a string to
// produce the date
Date d1 = sdf.parse(start_date);
Date d2 = sdf.parse(end_date);

//Calculate time difference
// in milliseconds
long difference_In_Time
= d2.getTime() - d1.getTime();

//Calculate time difference in seconds,
// minutes, hours, years, and days
long difference_In_Seconds
= TimeUnit.MILLISECONDS
   .toSeconds(difference_In_Time)
% 60;

long difference_In_Minutes
= TimeUnit
   .MILLISECONDS
   .toMinutes(difference_In_Time)
% 60;

long difference_In_Hours
= TimeUnit
   .MILLISECONDS
   .toHours(difference_In_Time)
% 24;

long difference_In_Days
= TimeUnit
   .MILLISECONDS
   .toDays(difference_In_Time)
% 365;

long difference_In_Years
= TimeUnit
   .MILLISECONDS
   .toDays(difference_In_Time)
/ 365l;

// Print the date difference in
// years, in days, in hours, in
// minutes, and in seconds
System.out.print(
"Difference"
+ " between two dates is: ");

// Print result
System.out.println(
difference_In_Years
+ " years, "
+ difference_In_Days
+ " days, "
+ difference_In_Hours
+ " hours, "
+ difference_In_Minutes
+ " minutes, "
+ difference_In_Seconds
+ " seconds");
result = difference_In_Years+""+difference_In_Days+":"+difference_In_Hours+":"+difference_In_Minutes+":"+difference_In_Seconds;
}
catch (ParseException e) {
e.printStackTrace();
}
return result;
}
}

