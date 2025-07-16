package com.magbel.InventoryService.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import com.magbel.InventoryService.bus.CronTriggerExample;

public class PostilionStart 

{
	
 
	CronTriggerExample jub2; 
public PostilionStart()
{
 
	jub2 = new CronTriggerExample(); 
}
public void start()
{
	      	
	try
	{ 
		 jub2.run();
	 
	}
	catch(Exception c)
	{
		c.printStackTrace();
	}
	
 
}
 
}
