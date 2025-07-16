package com.magbel.fixedassetcreationLegendPlusFIXFAC.bus;
 
import legend.admin.handlers.CompanyHandler;
 






import com.magbel.util.DatetimeFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 
 
public class Jobs15
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs4.class);
    CompanyHandler comp = new CompanyHandler();
    com.magbel.util.DatetimeFormat df;
    public Jobs15()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	   java.util.ArrayList list_iso =comp.getSqlRecords();
	   System.out.println("-->Iso Creation size Job 15>--> "+list_iso.size());
	   
	     for(int i=0;i<list_iso.size();i++)
	     {
	    	 com.magbel.legend.vao.newAssetTransaction  newtrans = (com.magbel.legend.vao.newAssetTransaction)list_iso.get(i);    	 
				String TransId =  newtrans.getAssetId();
				String finacleTransId =  newtrans.getTranType();
	    //	 System.out.println("-->>--> "+finacleTransId);
	    	 String isoValue=comp.getFinacleRecords(finacleTransId); 
//	    	 System.out.println("-->isoValue from Finacle Record>--> "+isoValue);
	    	 if(isoValue.equalsIgnoreCase("000")){
	    		 System.out.println("-->isoValue from Finacle Record>--> "+isoValue);
	    		 comp.updateSqlRecords(isoValue, finacleTransId,TransId);}
	     }	
	     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

