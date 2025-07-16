package com.magbel.fixedassetserver.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import com.magbel.fixedassetcreation.bus.CronTriggerExample;
import com.magbel.fixedassetcreation.bus.CronTriggerMail;

public class PostilionStart 

{
	
 
	CronTriggerExample jub2; 
	CronTriggerMail jub3; 
public PostilionStart()
{
 
	jub2 = new CronTriggerExample(); 
	jub3 = new CronTriggerMail(); 
}
public void start()
{
	      
	
	try
	{ 
		 jub2.run();
		 jub3.run();
	  
	
	}
	catch(Exception c)
	{
		c.printStackTrace();
	}
	
 
}
 
}
