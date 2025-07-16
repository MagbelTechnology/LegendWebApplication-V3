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
 
public class jobsBranchTable
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(jobsBranchTable.class);
    CompanyHandler comp = new CompanyHandler();

    public jobsBranchTable()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
//	     System.out.println("<<<<<<<<====New Branch From Legacy System table=====>> ");

		     java.util.ArrayList list =comp.getNewBranchRecordsFromLegacySystem();
//		     System.out.println("<<<<<<<< Select Records for Executions>> "+malilist.size() );
//		     boolean duplicate = comp.raisentry_postDuplicate();
//		     boolean duplicate2 = comp.asset_approvalDuplicate();
		      //================================
		     for(int i=0;i<list.size();i++)
		     {
		    	 legend.admin.objects.Branch  branch = (legend.admin.objects.Branch)list.get(i);    	 
				String branchCode =  branch.getBranchCode();	
				String branchAddress = branch.getBranchAddress();
				String branchName = branch.getBranchName();
				String stateId = branch.getState();
				String branchExist =comp.findObject(" SELECT  COUNT(*) FROM AM_AD_BRANCH WHERE BRANCH_CODE = '"+branchCode+"' ");
			if(branchExist.equals("0")){
				boolean sent = comp.InsertNewBranch(branchCode, branchName, branchAddress, stateId);
				if(sent) {
				boolean vendorCreate = comp.InsertNewVendor(branchCode, branchName, branchAddress, stateId);
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

