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
 
public class JobsSLA
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs6.class);
    CompanyHandler comp = new CompanyHandler();

    public JobsSLA()
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
	     java.util.ArrayList slalist =comp.getSLASqlRecords();
	     System.out.println("<<<<<<<<====SLA Records=====>> "+slalist.size() );
	      //================================
	     for(int i=0;i<slalist.size();i++)
	     {
	     legend.admin.objects.Sla  sla = (legend.admin.objects.Sla )slalist.get(i);    	 
			String id =  sla.getSla_ID();
//			System.out.println("<<<<<<<<==id===>> "+id);
			String fullName =  sla.getSlaFullName();
			String email = sla.getSlaEmail();
//			System.out.println("<<<<<<<<==email===>> "+email);
			String startDate = sla.getSlaStartDate();
			String endDate = sla.getSlaEndDate();
			int alertFreq = sla.getAlertFreq();
///			System.out.println("<<<<<<<<==alterFreq===>> "+alterFreq);
			int alertStart = sla.getAlertStart();
//			System.out.println("<<<<<<<<==alertStart===>> "+alertStart);
			int diffDate = sla.getDiffDate();
			int remainDay = sla.getRemainDay();
//			System.out.println("<<<<<<<<==remainDay===>> "+remainDay);
			String alertStartDate = sla.getSlaAlertStartDate();
			alertStartDate = alertStartDate.substring(0,19);
			String status = sla.getStatus();
			String header = sla.getSla_name();
			String body = sla.getSla_description(); 
			String resolveDay = sla.getRESOLVE_DAY();
			String resolveHour = sla.getRESOLVE_HOUR();
			String resolveMinutes = sla.getRESOLVE_MINUTE();
			String escalate = sla.getSlaEscalateName();
			String[] escalateSplit = escalate.split("-");
			String userName = escalateSplit[1];
			String escalatedName = escalateSplit[0];
			 String processTransCount =comp.findObject("SELECT COUNT(*) FROM SLA_PROCESSING  WHERE CRITERIA_ID = '"+id+"' AND ALERTSTARTDATE = '"+alertStartDate+"'");
//			System.out.println("=====escalatedName: "+escalatedName+"    processTransCount: "+processTransCount);
/*
			String alertStartDateTest = "2023-04-05 25:30:00";
			String test = "2023-04-05 25:30:00";
			alertStartDateTest = currentDate;
			Date date1 = new Date(2023, 04, 06, 20, 10);
			Date date2 = new Date(2023, 04, 05, 20, 07);
			Date date3 = new Date(2023, 04, 04, 20, 10);
			Date date4 = new Date(2023, 04, 05, 20, 07); 
			int result = date1.compareTo(date2);
			System.out.println("=====result: "+result+"     date1: "+date1+"     date2: "+date2);
			int result2 = date3.compareTo(date4);
			System.out.println("=====result: "+result2+"     date3: "+date3+"     date4: "+date4);
			
		      Calendar calendar1 = Calendar.getInstance();
		      Calendar calendar2 = Calendar.getInstance();
		      calendar1.set(2012, 04, 02);
		      calendar2.set(2012, 04, 02);
		      long milsecs1= calendar1.getTimeInMillis();
		      long milsecs2 = calendar2.getTimeInMillis();
		      long diff = milsecs2 - milsecs1;
		      long dsecs = diff / 1000;
		      long dminutes = diff / (60 * 1000);
		      long dhours = diff / (60 * 60 * 1000);
		      long ddays = diff / (24 * 60 * 60 * 1000);

		      System.out.println("Your Day Difference="+ddays);
		      System.out.println("date1: " + calendar2.compareTo(calendar1) + " date2."+calendar1.compareTo(calendar2));
		      
		        Date date13 = new Date(2023, 04, 10);
		        Date date14 = new Date(2023, 05, 01);
		        Calendar calendar3 = Calendar.getInstance();
		        Calendar calendar4 = Calendar.getInstance();
		        calendar1.setTime(date1);
		        calendar2.setTime(date2);
		        long milliseconds1 = calendar3.getTimeInMillis();
		        long milliseconds2 = calendar4.getTimeInMillis();
		        long diff1 = milliseconds2 - milliseconds1;
		        long diffSeconds = diff1 / 1000;
		        long diffMinutes = diff1 / (60 * 1000);
		        long diffHours = diff1 / (60 * 60 * 1000);
		        long diffDays = diff1 / (24 * 60 * 60 * 1000);
		        System.out.println("\nThe Date Different Example");
		        System.out.println("Time in milliseconds: " + diff1 + " milliseconds.");
		        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
		        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
		        System.out.println("Time in hours: " + diffHours + " hours.");
		        System.out.println("Time in days: " + diffDays + " days.");
		        System.out.println("date13: " + date13 + " date14."+date14);
		        
		     // Given start_date
		        String start_date
		            = "10-01-2023 06:30:50";
		 
		        // Given end_date
		        String end_date
		            = "10-01-2023 06:30:58";
		 
		        // Function Call
		       String resultvalue = findDifference(start_date,
		                       end_date);
		       */
		       String resolveresultDate =comp.findObject(" SELECT DATEDIFF(MINUTE, '"+currentDate+"', '"+alertStartDate+"') ");
		      
 //System.out.println("start_date: " + start_date + " end_date: "+end_date+"   resultvalue: "+resultvalue+"   resultDate: "+resultDate);
//		        System.out.println("alertStartDateTest: " + alertStartDateTest + " currentDate: "+currentDate+"    Test: "+test);
			String escalateMail = comp.findObject("SELECT email FROM am_gb_User WHERE USER_NAME = '"+userName+"'");
//			System.out.println("=====alertStartDate.compareTo(currentDate): "+alertStartDate.compareTo(currentDate));
//			System.out.println("=====alertStartDateTest.compareTo(Test): "+alertStartDateTest.compareTo(test)+"    processTransCount: "+processTransCount);
//		System.out.println("<<<<<<<<<<<<body: "+body+"  alertStartDate: "+alertStartDate+"    currentDate: "+currentDate+"   status: "+status+"   userName: "+userName+"    resolveDay: "+resolveDay+"   endDate: "+endDate+"    email: "+email+"    resolveresultDate: "+resolveresultDate);	
		if(!email.equals("") && (Integer.parseInt(resolveresultDate) < 1) && processTransCount.equals("0")){
			comp.insertMailRecords(email,header,body); 
//			System.out.println("=====resolveHour: "+resolveHour+"    ======resolveMinutes: "+resolveMinutes+"  processTransCount: "+processTransCount);
			 String nextalertDate =comp.findObject(" (SELECT DATEADD(day, "+alertFreq+", '"+alertStartDate.substring(0,10)+"')) ");
//			 System.out.println("=====nextalertDate: "+nextalertDate+"    ======alterFreq: "+alertFreq);
			 nextalertDate = nextalertDate.substring(0,10) +" "+resolveHour+":"+resolveMinutes+":"+"00";
//			 System.out.println("<<<<<<<<<<<<nextalertDate: "+nextalertDate);
			 comp.insertSLAProcessingTransaction(id,alertFreq,alertStart,remainDay,alertStartDate); 
			comp.updateSlaJobRecords(id,nextalertDate);
	     }
		String escalatealertDate =comp.findObject("(SELECT DATEADD(day, "+Integer.parseInt(resolveDay)+", '"+endDate+"')) ");
		 String escalateresultDate =comp.findObject(" SELECT DATEDIFF(MINUTE, '"+currentDate+"', '"+escalatealertDate+"') ");
//		System.out.println("=====escalatealertDate: "+escalatealertDate+"    ======currentDate: "+currentDate+"   escalateresultDate: "+escalateresultDate);
//		System.out.println("=====escalatealertDate.compareTo(currentDate): "+escalatealertDate.compareTo(currentDate));
		if(!email.equals("") && Integer.parseInt(escalateresultDate) < 1){
			comp.insertMailRecords(escalateMail,header,body); 
		}
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

