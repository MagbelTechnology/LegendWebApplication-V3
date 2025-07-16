package com.magbel.fixedassetserver.bus;
 
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
 
public class Jobs2
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs2.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs2()
    {
    
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  try
        {  
	     
	     java.util.ArrayList list =comp.getSqlRecords();
	     System.out.println("-->size>--> "+list.size());
	     //java.util.ArrayList list_ =comp.getNewAssetSqlRecords();
	     for(int i=0;i<list.size();i++)
	     {
	    	 String finacleTransId=(String) list.get(i);
	    	 System.out.println("-->>--> "+finacleTransId);
	    	 String isoValue=comp.getFinacleRecords(finacleTransId); 
	    	 System.out.println("-->isoValue>--> "+isoValue);
	    	 if(isoValue.equalsIgnoreCase("000"))
	    		 comp.updateSqlRecords(isoValue, finacleTransId);
	     }	     
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

