package com.magbel.fixedassetcreation.bus;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import com.magbel.fixedassetcreation.bus.CronTriggerExample;

public class CreationStart 

{
 
	CronTriggerExample job2; 
public CreationStart()
{
 
	job2 = new CronTriggerExample(); 
}
public void start()
{
	      
	
	try
	{ 
		 job2.run();
	 
	  
	
	}
	catch(Exception c)
	{
		c.printStackTrace();
	}
	
 
}
 
}
