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
 
@DisallowConcurrentExecution
public class jobsSectionTable
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(jobsBranchTable.class);
    CompanyHandler comp = new CompanyHandler();

    public jobsSectionTable()
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
		     
	  		if(BankingApp.equals("FINACLE")) {list = comp.getNewSectionRecordsFromFinacleLegacySystem();}
		     
//		     System.out.println("<<<<<<<< Select Records for Executions>> "+malilist.size() );

		      //================================
		     for(int i=0;i<list.size();i++)
		     {
		    	 legend.admin.objects.Section  section = (legend.admin.objects.Section)list.get(i);    	 
				String sectionCode =  section.getSection_code();	
				String sectionName = section.getSection_name();
				String sectionAcronym = section.getSection_acronym();
				String status = section.getSection_status();
				String sectionExist =comp.findObject(" SELECT  COUNT(*) FROM Sbu_SetUp WHERE SBU_CODE = '"+sectionCode+"' ");
			if(sectionExist.equals("0")){				
//				if(BankingApp.equals("FLEXICUBE")) {boolean sent = comp.InsertNewBranch(branchCode, branchName, branchAddress, stateId);}	
				if(BankingApp.equals("FINACLE")) {boolean sent = comp.InsertNewSection(sectionCode, sectionName, sectionAcronym, status);}
		     }
		     }   
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

