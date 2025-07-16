package com.magbel.fixedassetcreationLegendPlus.bus;
 
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
 
public class JobsStockBal
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs6.class);
    CompanyHandler comp = new CompanyHandler();

    public JobsStockBal()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	     System.out.println("<<<<<<<<====Stock Balance Total Records=====>> ");

	     String deleteRecord = "DELETE FROM ST_INVENTORY_TOTALS";
			boolean result = comp.deleteObject(deleteRecord);
			if(result){comp.insertStockTotalRecords();}

     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

