package com.magbel.fixedassetcreationLegendPlusZenith.bus;
 
import java.util.Date;
import java.util.List;

import legend.admin.handlers.CompanyHandler;
 





import com.magbel.util.DatetimeFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

//@DisallowConcurrentExecution
//public class Jobs4
//    implements Job
//{  
//        
//    private static Log _log = LogFactory.getLog(Jobs4.class);
//    CompanyHandler comp = new CompanyHandler();
//    com.magbel.util.DatetimeFormat df;
//    public Jobs4()
//    {
//      
//    }
//
//    public void execute(JobExecutionContext context)
//        throws JobExecutionException
//    {
//  //  	int statux = 0;
////		int statuk = 0;
//  try
//        {  
////	  System.out.println("-->sessionTimeOut >--> ");
////	  df.getDateTime().substring(10);
//  	String sessionTimeOut = comp.getCodeName("select session_timeout from am_gb_company");
////	String query = "update gb_user_login set time_out = '"+df.getDateTime().substring(10)+"' where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > "+sessionTimeOut+"";
//  	sessionTimeOut = "1";
//	     java.util.ArrayList list =comp.getUsernotSignOutRecords(sessionTimeOut);
////	     System.out.println("-->size in Job 4 Asset >--> "+list.size());
//	     //Iso Starts	
//
//	     for(int i=0;i<list.size();i++)
//	     {  
//	    	 String userId=(String) list.get(i);
////	    	 System.out.println("======>>>>>> userId in Jbos4: "+userId);
//	    	 if(!userId.equals(null)){
//	    		 String mtid = comp.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = " + userId + " ");
////	    		 System.out.println("======> mtid in Jbos4: "+mtid);
//	    		 comp.updateUsernotSignOutRecords(userId,mtid);
//	    	 }
//	     }	
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//}


@DisallowConcurrentExecution
public class Jobs4 implements Job {

    private static Log _log = LogFactory.getLog(Jobs4.class);
    CompanyHandler comp = new CompanyHandler();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            // Fetch session timeout
            String sessionTimeout = comp.getCodeName("SELECT session_timeout FROM am_gb_company");
            if (sessionTimeout == null) sessionTimeout = "1";

            // Get all users not signed out
            List<String> usersNotSignedOut = comp.getUsernotSignOutRecords(sessionTimeout);
        //    System.out.println("-->size in Job 4 Asset >--> "+usersNotSignedOut.size());
            if (usersNotSignedOut.isEmpty()) return;

            // Loop through each user and update their mtid individually
            for (String userId : usersNotSignedOut) {
                if (userId != null && !userId.trim().isEmpty()) {
                    // Fetch MAX(mtid) for this user
                    String mtid = comp.getCodeName("SELECT MAX(mtid) FROM gb_user_login WHERE USER_ID = " + userId);
                    if (mtid != null) {
                        comp.updateUsernotSignOutRecords(userId, mtid);
                    }
                }
            }

        } catch (Exception e) {
            _log.error("Error executing Jobs4", e);
        }
    }
}

