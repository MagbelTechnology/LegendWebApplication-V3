package com.magbel.fixedassetcreationLegendPlusZenith.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
import com.magbel.legend.bus.Report;
public class Jobs11
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs11.class);
    CompanyHandler comp = new CompanyHandler();
//    Report rep = new Report();

    public Jobs11()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        { 
      boolean done;
      Connection con;
      PreparedStatement ps;
      String query;
      done = true;
      con = null;
      ps = null;      
      
//		String dp = "DELETE FROM DEPRECIATIONCHARGE";
//		comp.updateAssetStatusChange(dp);
		
	  	String chargeYear = comp.findObject("SELECT SUBSTRING(CONVERT(varchar, PROCESSING_DATE, 23),1,4) FROM AM_GB_COMPANY ");
//	  	String reportDate = comp.findObject("SELECT SUBSTRING(CONVERT(varchar, getdate(), 23),1,7)+'-'+'22' ");
	  	String reportDate = comp.findObject("SELECT SUBSTRING(CONVERT(varchar, PROCESSING_DATE, 23),1,10) FROM AM_GB_COMPANY ");
	  	String existMonth = comp.findObject("SELECT COUNT(*) FROM monthly_Depr_Charge WHERE REPORT_DATE = '"+reportDate+"' AND REPORT_GENERATE = 'Y'");
//	  	System.out.println("======>>>>>chargeYear: "+chargeYear+"  reportDate: "+reportDate+"   existMonth: "+existMonth);
//	  	System.out.println("-->existMonth in Creation size in Job 11>--> "+existMonth);
	  	if(existMonth.equals("0")){comp.monthlyDeprChargeComplete(reportDate);}
//	  	System.out.println("=======>existMonth--> "+existMonth);
	  	if(existMonth.equals("0")){
	  	String ColQuery ="SELECT *FROM MonthlyDeprCharcges_View WHERE CHARGEYEAR = '"+chargeYear+"' AND DEP_DATE = '"+reportDate+"'";
//	  	System.out.println("======>ColQuery: "+ColQuery);
	     java.util.ArrayList list =comp.getDepreciationChargesExportRecords(ColQuery);
//	     System.out.println("-->Iso Creation size Job 11>--> "+list.size());
	      //================================
//	 	System.out.println("<<<====About to Add Record======>>>");
	     comp.createMonthlyDeprCharges(list);
//	     System.out.println("<<<====End of Record Addition======>>>");
	     existMonth = "1";
	     //======================================
	  	}
	  	
     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

