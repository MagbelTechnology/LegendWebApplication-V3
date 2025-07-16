package com.magbel.fixedassetcreationLegendPlusFIXFAC.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
 
public class Jobs3
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs3.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs3()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	  
	     
	     String alertperiod =comp.getCodeName("select DepreciateAlert  from am_gb_company");
	     java.util.ArrayList fullydeplist =comp.getSqlAssetFullyDepr(alertperiod);
	     System.out.println("<<<<<<<< fullydeplist.size>> "+fullydeplist.size() );
	      //================================
	     String calMonth =comp.getCodeName("SELECT (SELECT MONTH((SELECT CONVERT(VARCHAR(10),GETDATE(),102))))");
	     String alertMonth =comp.getCodeName("SELECT DISTINCT (SELECT MONTH((SELECT CONVERT(VARCHAR(10),ALERT_DATE,102)))) FROM ASSET_ALERT_LOG ");
//	     System.out.println("<<<<<<<<calMonth>> "+calMonth+"    alertMonth: "+alertMonth);
	     if(!calMonth.equals(alertMonth)){
	         String qw = "DELETE FROM ASSET_ALERT_LOG";
	         comp.updateAssetStatusChange(qw);
	         }
	     for(int f=0;f<fullydeplist.size();f++)
	     {
//	    	 System.out.println("<<<<<<<<J Loop>>>>>>  "+f+"  list.size>> "+fullydeplist.size() );
	     com.magbel.legend.vao.newAssetTransaction  fullydep = (com.magbel.legend.vao.newAssetTransaction)fullydeplist.get(f);    	 
			String asset_Id =  fullydep.getAssetId();
			String depdate = fullydep.getEffectiveDate();
			double costprice = fullydep.getCostPrice();
			double nbv = fullydep.getNbv();
			String email1 = fullydep.getSpare1();
			String email2 = fullydep.getSpare2();
			String assetCode = fullydep.getAssetCode();
			 String alert_No =comp.getCodeName("SELECT DATEDIFF(MONTH, (SELECT CAST(GETDATE() AS DATE)),Dep_End_Date) FROM AM_ASSET WHERE ASSET_ID =  '"+asset_Id+"'");
			 String recNo =comp.getCodeName("SELECT COUNT(*) FROM ASSET_ALERT_LOG WHERE ASSET_ID = '"+asset_Id+"' AND (SELECT MONTH((SELECT CONVERT(VARCHAR(10),GETDATE(),102)))) = (SELECT MONTH(coalesce((CONVERT(VARCHAR(10), ALERT_DATE,102)),'')))");
			 int alertNo = Integer.parseInt(alert_No)+1;
//			 System.out.println("<<<<<<recNo "+recNo);
//			System.out.println("<<<<<<asset_Id "+asset_Id+"   email1: "+email1+"   email2: "+email2+"  costprice: "+costprice+"  nbv: "+nbv+"  depdate: "+depdate);
	     //Start advising management of their NBV with email notification to users
//	     String payment =comp.getCodeName("select nbv+(cost_price*.01) AS Payment  from am_asset where asset_id = '"+asset_Id+"'");
	     depdate = depdate.substring(0,10);
	     double paymentvalue = nbv+(costprice*.01);
	     String mailsubject ="Asset User Advise";
			String usermail = email1+','+email2;
			System.out.println("<<<<<<<<<<<<usermail: "+usermail+"   paymentvalue: "+paymentvalue);
//			String otherparam = "requisitionApproval&tranId="+mtid+"&ReqnID="+reqnID+"&transaction_level=1&approval_level_count=0";
		String []usermaillist = usermail.split(",");
		int No = usermaillist.length;
		String assetUsermail = "";
		String msgText12 = "Good day,\n"
		         + "Please be informed that your status car (with details below) will be fully depreciated by the end of this month - "+depdate+"\n"
		         + "You will be required to pay the takeover value ("+paymentvalue+") by the end of the month when the last depreciation must have been charged.\n"
		         + "You can liaise with Procurement Unit for the replacement or HR – Compensation & Benefit Unit for the monetized option after the payment of the takeover value\n"
		         + "by end of the month.\n"
		         + "Thank you.\n"
		         + "Note: This is an auto notification from Admin - Fixed Asset Management unit.";
//		System.out.println("<<<<<<<<<<<<msgText12: "+msgText12+"   No: "+No);
		for(int j=0;j<No;j++){
			if(j == 0){
			assetUsermail = assetUsermail+usermaillist[j];}
			else{assetUsermail = assetUsermail+"; "+usermaillist[j];}
		}
		
//		System.out.println("<<<<<<<<<<<<assetUsermail: "+assetUsermail);
		if(usermail!="" || usermail!=null){
			if(Integer.parseInt(recNo)<2){comp.assetalertNotifications(asset_Id,Integer.parseInt(assetCode),alertNo);}
			comp.sendAssetManagementMail(usermail, mailsubject, msgText12);	
		}
	     }   
	     //======================================
     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    //fetch today records from am_raisentry_transaction where iso not equals 000
    //compare record against finacle iso values
    //if 000 update am_raisentry_transaction with value
    

    
    
}

