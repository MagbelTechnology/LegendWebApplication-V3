package com.magbel.fixedassetcreationTest.bus;
 
import legend.admin.handlers.CompanyHandler;
 



import com.magbel.util.DatetimeFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 
 
public class Jobs4
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs4.class);
    CompanyHandler comp = new CompanyHandler();
    com.magbel.util.DatetimeFormat df;
    public Jobs4()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {  
	  System.out.println("-->sessionTimeOut >--> ");
//	  df.getDateTime().substring(10);
  	String sessionTimeOut = comp.getCodeName("select session_timeout from am_gb_company");
//	String query = "update gb_user_login set time_out = '"+df.getDateTime().substring(10)+"' where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > "+sessionTimeOut+"";
		
	     java.util.ArrayList list =comp.getUsernotSignOutRecords(sessionTimeOut);
	     System.out.println("-->size in Job 4 Asset >--> "+list.size());
	     //Iso Starts	

	     for(int i=0;i<list.size();i++)
	     {
	    	 String userId=(String) list.get(i);
	    		 comp.updateUsernotSignOutRecords(userId);
	     }	

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

