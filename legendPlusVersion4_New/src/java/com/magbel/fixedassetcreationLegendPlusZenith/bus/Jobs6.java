package com.magbel.fixedassetcreationLegendPlusZenith.bus;
 
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

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.newAssetTransaction;
 
public class Jobs6
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs6.class);
    CompanyHandler comp = new CompanyHandler();
    EmailSmsServiceBus email = new EmailSmsServiceBus();

    public Jobs6()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {
	  
	     java.util.ArrayList malilist =comp.getSendMailSqlRecords();
//	     System.out.println("<<<<<<<< Mail To send List Number>> "+malilist.size() );
//	     boolean duplicate = comp.raisentry_postDuplicate();
//	     boolean duplicate2 = comp.asset_approvalDuplicate();
	      //================================
	     for(int i=0;i<malilist.size();i++)
	     {
	     com.magbel.legend.vao.SendMail  sendmail = (com.magbel.legend.vao.SendMail)malilist.get(i);    	 
			int id =  sendmail.getId();
			String address =  sendmail.getAddress();
			String header = sendmail.getHeader();
			String body = sendmail.getBody();
			String status = sendmail.getStatus();
	//	System.out.println("<<<<<<<<<<<<body: "+body);	
		if(!address.equals("")){
			//boolean sent = comp.sendRecordMail(address, header, body);	
			boolean sent = email.sendRecordMail(address, header, body);	
			if(sent==true){comp.updateSendMailRecords(id);}
	     }
	     }   
	     //======================================
     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

