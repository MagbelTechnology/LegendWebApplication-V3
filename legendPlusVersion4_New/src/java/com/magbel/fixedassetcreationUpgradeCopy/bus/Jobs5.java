package com.magbel.fixedassetcreationUpgrade.bus;
 
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
        
    private static Log _log = LogFactory.getLog(Jobs4.class);
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

			Date d1 = null;
			Date d2 = null;
			Date d12 = null;
			Date d22 = null;	
			Date d13 = null;
			Date d23 = null;	
			Date d14 = null;
			Date d24 = null;
		String alertMail =comp.getCodeName("select mail_address from am_mail_statement where transaction_type = 'PPM Alert'");
	     for(int i=0;i<list.size();i++)
	     {
	    	magma.net.vao.FMppmAllocation  ppm = (magma.net.vao.FMppmAllocation)list.get(i);    	 
			String duration = ppm.getDuration();
			String transId =ppm.getTransId();
			String branchCode = ppm.getBranchCode();
			String subcatCode = ppm.getSubCategoryCode();
			String vendorCode = ppm.getVendorCode();
			String q1Date = ppm.getFirstQauterDate();
			String description = ppm.getDescription();
			String q1DueDate = ppm.getFq_Due1();
			String q1Status = ppm.getFq_DueStatus1();
			String q2Date = ppm.getSecondQuaterDate();
			String q2DueDate = ppm.getFq_Due2();
			String q2Status = ppm.getFq_DueStatus2();
			String q3Date = ppm.getThirdQauterDate();
			String q3DueDate = ppm.getFq_Due3();
			String q3Status = ppm.getFq_DueStatus3();
			String q4Date = ppm.getFourthQuaterDate();
			String q4DueDate = ppm.getFq_Due4();
			String q4Status = ppm.getFq_DueStatus4();
			String status = ppm.getStatus();
			String type = ppm.getType();
			String branchName =comp.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branchCode+"'");
			int qrtDays =Integer.parseInt(duration); 
			System.out.println("<<<<===q1Date: "+q1Date+"     q1DueDate: "+q1DueDate+"   q1Status: "+q1Status);
			String currentDate = comp.findObject("SELECT GETDATE()");
			currentDate = currentDate.substring(0,10);
	//		var qrtDays = parseInt(document.ppmschedule.qrtDays.value);
			String recexist = comp.getCodeName("SELECT count(*) FROM FM_PPM_LOG WHERE BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' and VENDOR_CODE = '"+vendorCode+"' ");
			if(recexist.equals("0")){comp.ppmlog(branchCode,subcatCode,vendorCode,description);}
			String qtr1 = q1DueDate;
			String startDate = currentDate;
			System.out.println("<<<<===qtr1: "+qtr1+"     currentDate: "+currentDate);
			String yyyy = qtr1.substring(0,4);
			String month = qtr1.substring(5,7);
			String day = qtr1.substring(8,10);
			qtr1 = month+"-"+day+"-"+yyyy;
			String curyyyy = currentDate.substring(0,4);
			String curmonth = currentDate.substring(5,7);
			String curday = currentDate.substring(8,10);
			currentDate = curmonth+"-"+curday+"-"+curyyyy;
			System.out.println("<<<<===day: "+day+"  month: "+month+"  yyyy: "+yyyy);
			String []alertmaillist = alertMail.split(",");
			d1 = sdf.parse(currentDate);
			d2 = sdf.parse(qtr1);
			System.out.println("<<<<===qrtDays: "+qrtDays+"    currentDate: "+currentDate+"   qtr1: "+qtr1+"   D1: "+d1+"    D2: "+d2);
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			System.out.println("<<<<===diff: "+diff);
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
			String mailsubject = "PPM Maintenance";
			if(diffDays==qrtDays){q1Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q1Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}
			}
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

			String mailsubject2 = "PPM Maintenance";
			if(diffDays2==qrtDays){q2Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q2Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}
			}
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

			String mailsubject3 = "PPM Maintenance";
			if(diffDays3==qrtDays){q3Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q3Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject3, msgText12);	
			}
			}
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

			String mailsubject4 = "PPM Maintenance";
			if(diffDays4==qrtDays){q4Status = "Due Date";
			comp.updateAssetStatusChange("update FM_PPM set q4Status='Due Date' where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject3, msgText12);	
			}
			}
			if(diffDays4==(qrtDays+1)){q4Status = "Over Due";
						
			String newDate =comp.getCodeName("SELECT DateAdd( day, "+qrtDays+", Cast( GetDate() as Date ))");
			System.out.println("<<<<===newDate: "+newDate);
			comp.updateAssetStatusChange("update FM_PPM set Q4_STATUS='Over Due',Q4_DATE = '"+newDate+"',Q4_DUE_DATE = '"+newDate+"', where BRANCH_CODE = '"+branchCode+"' and SUB_CATEGORY_CODE = '"+subcatCode+"' ");
			if(alertMail!="" || alertMail!=null){
				String msgText12 = "Quartely Maintenenace for "+description+" in "+branchName+" branch is due Today "; 
				comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);	
			}			
			}
										
	     }	

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

