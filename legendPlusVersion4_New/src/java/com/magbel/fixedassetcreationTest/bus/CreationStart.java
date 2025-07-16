package com.magbel.fixedassetcreationTest.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

//import com.magbel.fixedassetcreation.bus.CronTriggerMail;
//import com.magbel.fixedassetcreation.bus.CronTriggerExample;;
public class CreationStart
{
static Scheduler scheduler = null;
  
//{ 
   
public static void init(){
System.out.println(" 0 The 	scheduler ==" + scheduler) ;

	if(scheduler == null){
System.out.println(" 1 The 	scheduler ==" + scheduler) ;
		start();
	}
}

public CreationStart()
{  

}


public static void start()
{
	try
	{

		System.out.println(" Here is calling the scheduler==");
		 scheduler = StdSchedulerFactory.getDefaultScheduler();
//		 JobDetail jobA = newJob(Jobs2.class).withIdentity("cronJob", "jobs2").build();
		 System.out.println(" Calling the scheduler Start 1");
//		 JobDetail jobB = newJob(Jobs3.class).withIdentity("cronJobB", "jobs3").build();
		 System.out.println(" Calling the scheduler Start 2");
	//	 JobDetail jobC = newJob(Jobs4.class).withIdentity("cronJobC", "jobs4").build();
		 System.out.println(" Calling the scheduler Start 3");
        String startDateStr = "2013-09-27 00:00:00.0";
        String endDateStr 	= "2023-09-27 00:00:00.0";


        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);
//        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
//        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);

      System.out.println(" Here is calling the startDateStr==" + startDate);
  	JobKey jobKeyA = new JobKey("Jobs2", "group1");
  	System.out.println(" Here is Runing the Jobs2==");
	JobDetail jobA = JobBuilder.newJob(Jobs2.class)
	.withIdentity(jobKeyA).build();
	
	JobKey jobKeyB = new JobKey("Jobs3", "group1");
  	System.out.println(" Here is Runing the Jobs3==");
	JobDetail jobB = JobBuilder.newJob(Jobs3.class)
	.withIdentity(jobKeyB).build();

	JobKey jobKeyC = new JobKey("Jobs4", "group1");
  	System.out.println(" Here is Runing the Jobs4==");
	JobDetail jobC = JobBuilder.newJob(Jobs4.class)
	.withIdentity(jobKeyC).build();


	Trigger trigger1 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName1", "group1")
	.withSchedule(
		CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
	.build();
	
	Trigger trigger2 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName2", "group1")
	.withSchedule(
		CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
	.build();
	    	
	Trigger trigger3 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName3", "group1")
	.withSchedule(
		CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
	.build();
	
	Scheduler scheduler = new StdSchedulerFactory().getScheduler();

	scheduler.start();
	scheduler.scheduleJob(jobA, trigger1);
	scheduler.scheduleJob(jobB, trigger2);
	scheduler.scheduleJob(jobC, trigger3);

	}
	catch(Exception c)
	{
		c.printStackTrace();
	}


}

}
