package com.magbel.fixedassetcreationLegendPlus.bus;
 
import java.text.SimpleDateFormat;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;
 
















import com.magbel.util.DatetimeFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 
 
public class Jobs5
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs5.class);
    CompanyHandler comp = new CompanyHandler();
    com.magbel.util.DatetimeFormat df;
    private SimpleDateFormat sdf;
    public Jobs5()
    {
    	 sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {  
	  System.out.println("-->PPM Calculations >--> ");
//  	String sessionTimeOut = comp.getCodeName("select session_timeout from am_gb_company");
	     java.util.ArrayList list =comp.getPPMRecords();
	     System.out.println("-->size in Job 5 Asset >--> "+list.size());
	     //Iso Starts	
	     DatetimeFormat format = new DatetimeFormat();
	     String compCode =comp.getCodeName("select company_code from am_gb_company");
			Date d1 = null;
			Date d2 = null;
			Date d12 = null;
			Date d22 = null;	
			Date d13 = null;
			Date d23 = null;	
			Date d14 = null;
			Date d24 = null;
		String alertMail =comp.getCodeName("select mail_address from am_mail_statement where transaction_type = 'PPM Alert'");
//		System.out.println("<<<<===list.size(): "+list.size());
	     for(int i=0;i<list.size();i++)
	     {
	    	magma.net.vao.FMppmAllocation  ppm = (magma.net.vao.FMppmAllocation)list.get(i);    	 
			String duration = ppm.getDuration();
			String transId =ppm.getTransId();
			String branchCode = ppm.getBranchCode();
			String subcatCode = ppm.getSubCategoryCode();
			String vendorCode = ppm.getVendorCode();
			String categoryCode = ppm.getCategoryCode();
			String lastServiceDate = ppm.getLastServiceDate();
			String description = ppm.getDescription();
			String q1DueDate = ppm.getFq_Due1();
			String q1Status = ppm.getFq_DueStatus1();
//			String q2Date = ppm.getSecondQuaterDate();
			String q2DueDate = ppm.getFq_Due2();
			String q2Status = ppm.getFq_DueStatus2();
//			String q3Date = ppm.getThirdQauterDate();
			String q3DueDate = ppm.getFq_Due3();
			String q3Status = ppm.getFq_DueStatus3();
//			String q4Date = ppm.getFourthQuaterDate();
			String q4DueDate = ppm.getFq_Due4();
			String q4Status = ppm.getFq_DueStatus4();
			String status = ppm.getStatus();
			String type = ppm.getType();
			String mailsubject ="PPM Facility Maintenance";
			String msgText12 = "Good day,\n"
			         + "Please be informed that Facilities under the category of - "+description+"\n"
			         + "are due for maintenance.\n"
			         + "Kindly go to Menu Facility Requisition List to continue with the You can liaise with Procurement Unit for the replacement or HR – Compensation & Benefit Unit the requisition\n"
			         + "Thank you.\n"
			         + "Note: This is an auto notification from Admin - Facility Management unit.";
			String transIdExist =comp.getCodeName("select count(*) from FM_PPM_AWAIT_REQUISITION where TRANSID = '"+transId+"'");
			String branchName =comp.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branchCode+"'");
			int qrtDays =Integer.parseInt(duration); 
//			System.out.println("=====>>>>transId: "+transId+"   <<<<===lastServiceDate: "+lastServiceDate+"     q1DueDate: "+q1DueDate+"   q1Status: "+q1Status);
			String currentDate = comp.findObject("SELECT GETDATE()");
			String usermail = comp.getCodeName("SELECT mail_address FROM am_mail_statement WHERE mail_code = 'FM001' ");
			currentDate = currentDate.substring(0,10);
	//		var qrtDays = parseInt(document.ppmschedule.qrtDays.value);
			String recexist = comp.getCodeName("SELECT count(*) FROM FM_PPM_LOG WHERE BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' and VENDOR_CODE = '"+vendorCode+"' ");
			if(recexist.equals("0")){comp.ppmlog(transId,branchCode,subcatCode,vendorCode,description);}
			String qtr1 = q1DueDate;
			String startDate = currentDate;
//			System.out.println("<<<<===qtr1: "+qtr1+"     currentDate: "+currentDate);
/*			System.out.println("<<<<<<<<Date Script====: "+"SELECT DATEDIFF(D, '"+q1Date+"' , '"+currentDate+"')");
			String today =comp.getCodeName("SELECT DATEDIFF(D, '"+q1Date+"' , '"+currentDate+"')");
			System.out.println("<<<<===today: "+today); */
			String yyyy = qtr1.substring(0,4);
			String month = qtr1.substring(5,7);
			String day = qtr1.substring(8,10);
			qtr1 = month+"-"+day+"-"+yyyy;
			String curyyyy = currentDate.substring(0,4);
			String curmonth = currentDate.substring(5,7);
			String curday = currentDate.substring(8,10);
			currentDate = curmonth+"-"+curday+"-"+curyyyy;
//			System.out.println("<<<<===day: "+day+"  month: "+month+"  yyyy: "+yyyy);
			String []alertmaillist = alertMail.split(",");
			d1 = sdf.parse(currentDate);
			d2 = sdf.parse(qtr1);
//			System.out.println("<<<<===qrtDays: "+qrtDays+"    currentDate: "+currentDate+"   qtr1: "+qtr1+"   D1: "+d1+"    D2: "+d2);
			//in milliseconds
			
			int today = 0;
//			System.out.println("Script=======: "+"SELECT DATEDIFF(D, '"+currentDate+"' , '"+q1DueDate+"')");
			today =Integer.parseInt(comp.getCodeName("SELECT DATEDIFF(D, '"+currentDate+"' , '"+q1DueDate+"')"));
//			System.out.println("=====>today in Q1: "+today);
			if((q1DueDate!="")&&(today<0) && (today<14)){System.out.println("Due Date Test");
			q1Status = "Due Date";}
			if((q1DueDate!="")&&(today>14 || today==14)){System.out.println("Over Due Test");
			q1Status = "Over Due";} 
			if((q1DueDate!="") && (today<0 || today==0)){System.out.println("Alert Test");
			q1Status = "Alert";
			String userId = "0";
//			System.out.println("<<<<===transIdExist: "+transIdExist);
			 if(transIdExist.equals("0")){
//				 System.out.println("<<<<===transIdExist Not True===");
	        String reqnId =  comp.generateCode2("REQUISITION", "", "", "");
//	        System.out.println("<<<<===reqnId: "+reqnId);
			boolean serviceDue = comp.fmppmserviceDue(reqnId,transId,userId,branchCode,categoryCode,subcatCode,description,compCode);
			if(usermail!="" || usermail!=null){comp.sendAssetManagementMail(usermail, mailsubject, msgText12);}
			}
			}
			if((q1DueDate!="")&&(today<0)){System.out.println("Next Due Date Test");
			q1Status = "Next Due Date";}
			String newDateToday =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
//			System.out.println("=====>newDateToday in Q1: "+newDateToday);
//			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DATE = '"+newDateToday+"',Q1_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//21-02-2021			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//			if(alertMail!="" || alertMail!=null){
////				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
////				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
//			}						
//			System.out.println("q1Status=======>>>>>"+q1Status+"     alertMail: "+alertMail+"  today: "+today);
			
//			System.out.println("Script 2=======: "+"SELECT DATEDIFF(D, '"+currentDate+"' , '"+q2DueDate+"')");
//			today =comp.getCodeName("SELECT DATEDIFF(D, '"+q2Date+"' , '"+currentDate+"')");
			today =Integer.parseInt(comp.getCodeName("SELECT DATEDIFF(D, '"+currentDate+"' , '"+q2DueDate+"')"));
//			System.out.println("=====>today in Q2: "+today);
			if((q2DueDate!="")&&(today>0) && (today<14)){System.out.println("Due Date Test");
			q2Status = "Due Date";}
			if((q2DueDate!="")&&(today>14 || today==14)){System.out.println("Over Due Test");
			q2Status = "Over Due";}
			if((q2DueDate!="")&&(today>-14 || today==-14) && (today<0 || today==0)){System.out.println("Alert Test");
			q1Status = "Alert";
			String userId = "0";
			 if(transIdExist.equals("0")){
	        String reqnId =  comp.generateCode2("REQUISITION", "", "", "");
			boolean serviceDue = comp.fmppmserviceDue(reqnId,transId,userId,branchCode,categoryCode,subcatCode,description,compCode);
			if(usermail!="" || usermail!=null){comp.sendAssetManagementMail(usermail, mailsubject, msgText12);}
			}
			}  
			if((q2DueDate!="")&&(today<0)){System.out.println("Next Due Date Test");
			q2Status = "Next Due Date";}
			mailsubject = "PPM Maintenance";
			newDateToday =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
//			System.out.println("=====>newDateToday in Q2: "+newDateToday);
//			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DATE = '"+newDateToday+"',Q1_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//21-02-2021			comp.updateAssetStatusChange("update FM_PPM set Q2_STATUS='Over Due',Q2_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//			if(alertMail!="" || alertMail!=null){
////				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
////				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
//			}						
//			System.out.println("q2Status=======>>>>>"+q2Status+"     alertMail: "+alertMail+"  today: "+today);
			
//			System.out.println("Script 3=======: "+"SELECT DATEDIFF(D, '"+currentDate+"' , '"+q3DueDate+"')");
//			today =comp.getCodeName("SELECT DATEDIFF(D, '"+q3Date+"' , '"+currentDate+"')");
			today =Integer.parseInt(comp.getCodeName("SELECT DATEDIFF(D, '"+currentDate+"' , '"+q3DueDate+"')"));
//			System.out.println("=====>today in Q3: "+today);
			if((q3DueDate!="")&&(today>0) && (today<14)){System.out.println("Due Date Test");
			q3Status = "Due Date";}
			if((q3DueDate!="")&&(today>14 || today==14)){System.out.println("Over Due Test");
			q3Status = "Over Due";}
			if((q3DueDate!="")&&(today>-14 || today==-14) && (today<0 || today==0)){System.out.println("Alert Test");
			q3Status = "Alert";
			String userId = "0";
			 if(transIdExist.equals("0")){
	        String reqnId =  comp.generateCode2("REQUISITION", "", "", "");
			boolean serviceDue = comp.fmppmserviceDue(reqnId,transId,userId,branchCode,categoryCode,subcatCode,description,compCode);
			if(usermail!="" || usermail!=null){comp.sendAssetManagementMail(usermail, mailsubject, msgText12);}
			}
			} 
			if((q3DueDate!="")&&(today<0)){System.out.println("Next Due Date Test");
			q3Status = "Next Due Date";}
			mailsubject = "PPM Maintenance";
			newDateToday =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
//			System.out.println("=====>newDateToday in Q3: "+newDateToday);
//			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DATE = '"+newDateToday+"',Q1_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//21-02-2021			comp.updateAssetStatusChange("update FM_PPM set Q3_STATUS='Over Due',Q3_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//			if(alertMail!="" || alertMail!=null){
////				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
////				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
//			}						
//			System.out.println("q3Status=======>>>>>"+q3Status+"     alertMail: "+alertMail+"  today: "+today);
			
//			System.out.println("Script 4=======: "+"SELECT DATEDIFF(D, '"+currentDate+"' , '"+q4DueDate+"')");
//			today =comp.getCodeName("SELECT DATEDIFF(D, '"+q4Date+"' , '"+currentDate+"')");
			today =Integer.parseInt(comp.getCodeName("SELECT DATEDIFF(D, '"+currentDate+"' , '"+q4DueDate+"')"));
//			System.out.println("=====>today in Q4: "+today);
			if((q4DueDate!="")&&(today>0) && (today<14)){System.out.println("Due Date Test");
			q4Status = "Due Date";}
			if((q4DueDate!="")&&(today>14 || today==14)){System.out.println("Over Due Test");
			q4Status = "Over Due";}
			if((q4DueDate!="")&&(today>-14 || today==-14) && (today<0 || today==0)){System.out.println("Alert Test");
			q4Status = "Alert";
			String userId = "0";
			 if(transIdExist.equals("0")){
	        String reqnId =  comp.generateCode2("REQUISITION", "", "", "");
			boolean serviceDue = comp.fmppmserviceDue(reqnId,transId,userId,branchCode,categoryCode,subcatCode,description,compCode);
			if(usermail!="" || usermail!=null){comp.sendAssetManagementMail(usermail, mailsubject, msgText12);}
			}
			}
			if((q4DueDate!="")&&(today<0)){System.out.println("Next Due Date Test");
			q4Status = "Next Due Date";}
			mailsubject = "PPM Maintenance";
			newDateToday =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
//			System.out.println("=====>newDateToday in Q4: "+newDateToday);
//			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DATE = '"+newDateToday+"',Q1_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//			comp.updateAssetStatusChange("update FM_PPM set Q4_STATUS='Over Due',Q4_DUE_DATE = '"+newDateToday+"' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='"+q1Status+"',Q2_STATUS='"+q2Status+"',Q3_STATUS='"+q3Status+"',Q4_STATUS='"+q4Status+"' where TRANSID = '"+transId+"' and BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
//			if(alertMail!="" || alertMail!=null){
////				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
////				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
//			}						
//			System.out.println("q4Status=======>>>>>"+q4Status+"     alertMail: "+alertMail+"  today: "+today);
								
			long diff = d2.getTime() - d1.getTime();
//			System.out.println("<<<<===diff: "+diff);
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
/*
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			*/
			/*
			System.out.println("<<<<===diffDays: "+diffDays+"     qrtDays: "+qrtDays);
//			String mailsubject = "PPM Maintenance";
			if(diffDays==qrtDays){q1Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q1Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}
			}
			System.out.println("<<<<===diffDays: "+diffDays+"     (qrtDays+1): "+(qrtDays+1));
			if(diffDays==(qrtDays+1)){q1Status = "Over Due";
						
			String newDate =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
			System.out.println("<<<<===newDate: "+newDate);
			comp.updateAssetStatusChange("update FM_PPM set Q1_STATUS='Over Due',Q1_DATE = '"+newDate+"',Q1_DUE_DATE = '"+newDate+"', where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}			
			}

			String qtr2 = q2DueDate;
			String yyyy2 = qtr2.substring(0,4);
			String month2 = qtr2.substring(5,7);
			String day2 = qtr2.substring(8,10);
			qtr1 = month2+"-"+day2+"-"+yyyy2;
	//		String curyyyy2 = currentDate.substring(0,4);
	//		String curmonth2 = currentDate.substring(5,7);
	//		String curday2 = currentDate.substring(8,10);
	//		currentDate = curmonth2+"-"+curday2+"-"+curyyyy2;
	//		System.out.println("<<<<===day2: "+day2+"  month2: "+month2+"  yyyy2: "+yyyy2);
//			String []alertmaillist = alertMail.split(",");
			d12 = sdf.parse(currentDate);
			d22 = sdf.parse(qtr2);
			System.out.println("<<<<===qrtDays: "+qrtDays+"    currentDate: "+currentDate+"   qtr1: "+qtr1+"   D1: "+d1+"    D2: "+d2);
			//in milliseconds
			
			long diff2 = d22.getTime() - d12.getTime();
			System.out.println("<<<<===diff2: "+diff2);
			long diffDays2 = diff2 / (24 * 60 * 60 * 1000);
			
			System.out.println("<<<<===diffDays2: "+diffDays2+"     qrtDays: "+qrtDays);
			String mailsubject2 = "PPM Maintenance";
			if(diffDays2==qrtDays){q2Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q2Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}
			}
			System.out.println("<<<<===diffDays2: "+diffDays2+"     (qrtDays+1): "+(qrtDays+1));
			if(diffDays2==(qrtDays+1)){q1Status = "Over Due";
						
			String newDate =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
			System.out.println("<<<<===newDate: "+newDate);
			comp.updateAssetStatusChange("update FM_PPM set Q2_STATUS='Over Due',Q2_DATE = '"+newDate+"',Q2_DUE_DATE = '"+newDate+"', where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}			
			}

			String qtr3 = q3DueDate;
			String yyyy3 = qtr3.substring(0,4);
			String month3 = qtr3.substring(5,7);
			String day3 = qtr3.substring(8,10);
			qtr3 = month3+"-"+day3+"-"+yyyy3;
//			String curyyyy3 = currentDate.substring(0,4);
//			String curmonth3 = currentDate.substring(5,7);
//			String curday3 = currentDate.substring(8,10);
//			currentDate = curmonth3+"-"+curday3+"-"+curyyyy3;
//			System.out.println("<<<<===day3: "+day3+"  month3: "+month3+"  yyyy3: "+yyyy3);
//			String []alertmaillist = alertMail.split(",");
			d13 = sdf.parse(currentDate);
			d23 = sdf.parse(qtr3);
			System.out.println("<<<<===qrtDays: "+qrtDays+"    currentDate: "+currentDate+"   qtr3: "+qtr3+"   D13: "+d13+"    D23: "+d23);
			//in milliseconds
			
			long diff3 = d23.getTime() - d13.getTime();
			System.out.println("<<<<===diff3: "+diff3);
			long diffDays3 = diff3 / (24 * 60 * 60 * 1000);

			System.out.println("<<<<===diffDays3: "+diffDays3+"     qrtDays: "+qrtDays);
			String mailsubject3 = "PPM Maintenance";
			if(diffDays3==qrtDays){q3Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q3Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject3, msgText12);	
			}
			}
			System.out.println("<<<<===diffDays3: "+diffDays3+"     (qrtDays+1): "+(qrtDays+1));
			if(diffDays3==(qrtDays+1)){q1Status = "Over Due";
						
			String newDate =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
			System.out.println("<<<<===newDate: "+newDate);
			comp.updateAssetStatusChange("update FM_PPM set Q3_STATUS='Over Due',Q3_DATE = '"+newDate+"',Q3_DUE_DATE = '"+newDate+"', where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}			
			}

			String qtr4 = q4DueDate;
			String yyyy4 = qtr4.substring(0,4);
			String month4 = qtr4.substring(5,7);
			String day4 = qtr4.substring(8,10);
			qtr4 = month4+"-"+day4+"-"+yyyy4;
//			String curyyyy4 = currentDate.substring(0,4);
//			String curmonth4 = currentDate.substring(5,7);
//			String curday4 = currentDate.substring(8,10);
//			currentDate = curmonth4+"-"+curday4+"-"+curyyyy4;
//			System.out.println("<<<<===day4: "+day4+"  month4: "+month4+"  yyyy4: "+yyyy4);
//			String []alertmaillist = alertMail.split(",");
			d14 = sdf.parse(currentDate);
			d24 = sdf.parse(qtr4);
			System.out.println("<<<<===qrtDays: "+qrtDays+"    currentDate: "+currentDate+"   qtr4: "+qtr4+"   D14: "+d14+"    D24: "+d24);
			//in milliseconds
			
			long diff4 = d24.getTime() - d14.getTime();
			System.out.println("<<<<===diff4: "+diff4);
			long diffDays4 = diff4 / (24 * 60 * 60 * 1000); 

			System.out.println("<<<<===diffDays4: "+diffDays4+"     qrtDays: "+qrtDays);
			String mailsubject4 = "PPM Maintenance";
			if(diffDays4==qrtDays){q4Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q4Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject3, msgText12);	
			}
			}
			System.out.println("<<<<===diffDays4: "+diffDays4+"     (qrtDays+1): "+(qrtDays+1));
			if(diffDays4==(qrtDays+1)){q4Status = "Over Due";
						
			String newDate =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
			System.out.println("<<<<===newDate: "+newDate);
			comp.updateAssetStatusChange("update FM_PPM set Q4_STATUS='Over Due',Q4_DATE = '"+newDate+"',Q4_DUE_DATE = '"+newDate+"', where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}			
			}  
			*/
/*			String printDisplay = "";
			if((q1Date!="")&&(Integer.parseInt(today)>0) && (Integer.parseInt(today)<14)){System.out.println("Due Date Test");
			printDisplay = "Due Date";}
			if((q1Date!="")&&(Integer.parseInt(today)>=14)){System.out.println("Over Due Test");
			printDisplay = "Over Due";}
			if((q1Date!="")&&(Integer.parseInt(today)>=-14) && (Integer.parseInt(today)<=0)){System.out.println("Alert Test");
			printDisplay = "Alert";}
			if((q1Date!="")&&(Integer.parseInt(today)<0)){System.out.println("Next Due Date Test");
			printDisplay = "Next Due Date";}*/
//			=IF(C12<>"",C12+VLOOKUP("SecurityEntranceDoor",MaintenanceItems,2,FALSE),"")
//			IF(AND(TODAY()-E12>0,TODAY()-E12<14),"Due Date",IF(TODAY()-E12>=14,"Over Due",IF(AND(TODAY()-E12>=-14,TODAY()-E12<=0),"Alert",IF(TODAY()<E12,"Next Due Date"))))
			
//			=IF(E11<>"",E11+VLOOKUP("SecurityEntranceDoor",MaintenanceItems,2,FALSE),"")
//			IF(AND(TODAY()-H11>0,TODAY()-H11<14),"Due Date",IF(TODAY()-H11>=14,"Over Due",IF(AND(TODAY()-H11>=-14,TODAY()-H11<=0),"Alert",IF(TODAY()<H11,"Next Due Date"))
	     }	

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

