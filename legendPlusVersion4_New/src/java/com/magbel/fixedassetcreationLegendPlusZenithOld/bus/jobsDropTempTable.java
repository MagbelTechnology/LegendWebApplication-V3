package com.magbel.fixedassetcreationLegendPlusZenith.bus;
 
import java.io.File;
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
import magma.net.vao.Stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
 
public class jobsDropTempTable
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(jobsDropTempTable.class);
    CompanyHandler comp = new CompanyHandler();

    public jobsDropTempTable()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	     System.out.println("<<<<<<<<====Delete Asset Verification Mobile Temporary tables=====>> ");

	     String dropRecord = "INSERT INTO DROP_TABLE(RECORD) SELECT 'DROP TABLE ' + NAME from sys.tables "
	     		+ "WHERE NAME LIKE '%Temp%' and substring(name,1,4) = 'Temp' GROUP BY NAME";
			boolean result = comp.dropObject(dropRecord);
			
		     java.util.ArrayList malilist =comp.getDropTableRecords();
		     System.out.println("<<<<<<<< Select Records for Executions>> "+malilist.size() );
//		     boolean duplicate = comp.raisentry_postDuplicate();
//		     boolean duplicate2 = comp.asset_approvalDuplicate();
		      //================================
		     for(int i=0;i<malilist.size();i++)
		     {
		     com.magbel.legend.vao.SendMail  sendmail = (com.magbel.legend.vao.SendMail)malilist.get(i);    	 
				String record =  sendmail.getAddress();	
				int id = sendmail.getId();
			if(!record.equals("")){
//				System.out.println("<<<<<<<< Select Records for Executions in jobsDropTempTable>> "+record);
				boolean sent = comp.runDropTables(record);	
				String query = "DELETE FROM DROP_TABLE WHERE ID = "+id+"";
//				System.out.println("<<<<<<<< Select query for Executions in jobsDropTempTable>> "+query);
				if(sent==true){comp.deleteObject(query);}
		     }
		     }   
		     //======================================
	     
			
//			if(result){comp.insertStockTotalRecords();}
			
			File f1 = new File("\\*.xls");
			System.out.print("Absolute Path:>>> "+f1.getAbsolutePath());
			if (f1.delete()) {
			    System.out.println("File " + f1.getName() + " is deleted.");
			} else {
			    System.out.println("File " + f1.getName() + " not found.");
			}

     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

