package com.magbel.fixedassetcreationLegendPlus.bus;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
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
 
public class jobsSBUTable
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(jobsBranchTable.class);
    CompanyHandler comp = new CompanyHandler();

    public jobsSBUTable()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
			Properties prop = new Properties();
			File file = new File("C:\\Property\\LegendPlus.properties");
			FileInputStream input = new FileInputStream(file);
			prop.load(input);
	
			String BankingApp = prop.getProperty("BankingApp");
//			System.out.println("BankingApp: " + BankingApp);
//	     System.out.println("<<<<<<<<====New Branch From Legacy System table=====>> ");
	  		 java.util.ArrayList list = null;
//	  		 if(BankingApp.equals("FLEXICUBE")) {list = comp.getNewSBURecordsFromFinacleLegacySystem();}
		     
	  		if(BankingApp.equals("FINACLE")) {list = comp.getNewSBURecordsFromFinacleLegacySystem();}
		     
//		     System.out.println("<<<<<<<< Select Records for Executions>> "+malilist.size() );

		      //================================
		     for(int i=0;i<list.size();i++)
		     {
		    	 legend.admin.objects.Sbu_branch  sbu = (legend.admin.objects.Sbu_branch)list.get(i);    	 
				String sbuCode =  sbu.getSbucode();	
				String sbuName = sbu.getSbuname();
				String sbuContact = sbu.getSbucontact();
				String status = sbu.getSbustatus();
				String branchExist =comp.findObject(" SELECT  COUNT(*) FROM Sbu_SetUp WHERE SBU_CODE = '"+sbuCode+"' ");
			if(branchExist.equals("0")){				
//				if(BankingApp.equals("FLEXICUBE")) {boolean sent = comp.InsertNewBranch(branchCode, branchName, branchAddress, stateId);}	
				if(BankingApp.equals("FINACLE")) {boolean sent = comp.InsertNewSbu(sbuCode, sbuName, sbuContact, status);}
		     }
		     }   
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

