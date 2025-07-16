package com.magbel.fixedassetcreationWithNewImprovement.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import com.magbel.fixedassetcreationWithNewImprovement.bus.CronTriggerExample;

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
