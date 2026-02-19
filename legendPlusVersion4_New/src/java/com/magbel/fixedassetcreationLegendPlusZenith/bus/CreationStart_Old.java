package com.magbel.fixedassetcreationLegendPlusZenith.bus;

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
public class CreationStart_Old
{
static Scheduler scheduler = null;

//{  
   
public static void init(){
//System.out.println(" 0 The 	scheduler ==" + scheduler) ;
 
	if(scheduler == null){
//System.out.println(" 1 The 	scheduler ==" + scheduler) ;
		start();
	}
}

public CreationStart_Old()
{  

}


public static void start()
{
	try
	{

//		System.out.println(" Here is calling the scheduler==");
//		 scheduler = StdSchedulerFactory.getDefaultScheduler();
//		 JobDetail jobA = newJob(Jobs2.class).withIdentity("cronJob", "jobs2").build();
//		 System.out.println(" Calling the scheduler Start 1");
//		 JobDetail jobB = newJob(Jobs3.class).withIdentity("cronJobB", "jobs3").build();
//		 System.out.println(" Calling the scheduler Start 2");
	//	 JobDetail jobC = newJob(Jobs4.class).withIdentity("cronJobC", "jobs4").build();
//		 System.out.println(" Calling the scheduler Start 3");
        String startDateStr = "2013-09-27 00:00:00.0";
        String endDateStr 	= "3023-09-27 00:00:00.0";

//        System.out.println(" Here is New calling the scheduler ");
        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);
//        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
//        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);
//
////      System.out.println(" Here is calling the startDateStr==" + startDate);
//  	JobKey jobKeyA = new JobKey("Jobs1", "group1");
// // 	System.out.println(" Here is Runing the Jobs1==");
//	JobDetail jobA = JobBuilder.newJob(Jobs1.class)
//	.withIdentity(jobKeyA).build();
//	 
//	JobKey jobKeyB = new JobKey("Jobs2", "group2");
////  	System.out.println(" Here is Runing the Jobs2==");
//	JobDetail jobB = JobBuilder.newJob(Jobs2.class)
//	.withIdentity(jobKeyB).build(); 

	JobKey jobKeyC = new JobKey("Jobs3", "group3");
 // 	System.out.println(" Here is Runing the Jobs3==");
	JobDetail jobC = JobBuilder.newJob(Jobs3.class)
	.withIdentity(jobKeyC).build();

	JobKey jobKeyD = new JobKey("Jobs4", "group4");
//  	System.out.println(" Here is Runing the Jobs4==");
	JobDetail jobD = JobBuilder.newJob(Jobs4.class)
	.withIdentity(jobKeyD).build();
// 
//	JobKey jobKeyE = new JobKey("Jobs5", "group5");
////  	System.out.println(" Here is Runing the Jobs5==");
//	JobDetail jobE = JobBuilder.newJob(Jobs5.class)
//	.withIdentity(jobKeyE).build();

	JobKey jobKeyF = new JobKey("Jobs6", "group6");
//  	System.out.println(" Here is Runing the Jobs6==");
	JobDetail jobF = JobBuilder.newJob(Jobs6.class)
	.withIdentity(jobKeyF).build();
//
//	JobKey jobKeyG = new JobKey("Jobs7", "group7");
//  //	System.out.println(" Here is Runing the Jobs7==");
//	JobDetail jobG = JobBuilder.newJob(Jobs7.class)
//	.withIdentity(jobKeyG).build();
//
//	JobKey jobKeyH = new JobKey("Jobs8", "group8");
//  //	System.out.println(" Here is Runing the Jobs8==");
//	JobDetail jobH = JobBuilder.newJob(Jobs8.class)
//	.withIdentity(jobKeyH).build();
//
//	JobKey jobKeyI = new JobKey("Jobs9", "group9");
// // 	System.out.println(" Here is Runing the Jobs9==");
//	JobDetail jobI = JobBuilder.newJob(Jobs9.class)
//	.withIdentity(jobKeyI).build();
//
//	JobKey jobKeyJ = new JobKey("Jobs10", "group10");
// // 	System.out.println(" Here is Runing the Jobs10==");
//	JobDetail jobJ = JobBuilder.newJob(Jobs10.class)
//	.withIdentity(jobKeyJ).build();
//		
	JobKey jobKeyK = new JobKey("Jobs11", "group11");
 // 	System.out.println(" Here is Runing the Jobs11==");
	JobDetail jobK = JobBuilder.newJob(Jobs11.class)
	.withIdentity(jobKeyK).build();
//	
//	JobKey jobKeyL = new JobKey("Jobs12", "group12");
//	// 	System.out.println(" Here is Runing the Jobs12==");
//	JobDetail jobL = JobBuilder.newJob(Jobs12.class)
//	.withIdentity(jobKeyL).build();
//	
//	JobKey jobKeyM = new JobKey("Jobs13", "group13");
//	// 	System.out.println(" Here is Runing the Jobs13==");
//	JobDetail jobM = JobBuilder.newJob(Jobs13.class)
//	.withIdentity(jobKeyM).build();
//	
//	JobKey jobKeyN = new JobKey("Jobs14", "group14");
//	// 	System.out.println(" Here is Runing the Jobs14==");
//	JobDetail jobN = JobBuilder.newJob(Jobs14.class)
//	.withIdentity(jobKeyN).build();	
//	
//	JobKey jobKeyO = new JobKey("Jobs15", "group15");
//	// 	System.out.println(" Here is Runing the Jobs14==");
//	JobDetail jobO = JobBuilder.newJob(Jobs15.class)
//	.withIdentity(jobKeyO).build();	
//	
//	JobKey jobKeySLA = new JobKey("JobsSLA", "group16");
//	// 	System.out.println(" Here is Runing the Jobs14==");
//	JobDetail jobSLA = JobBuilder.newJob(JobsSLA.class)
//	.withIdentity(jobKeySLA).build();	
//	
//	JobKey jobKeyBraVisit = new JobKey("JobsBraVisit", "group17");
//	// 	System.out.println(" Here is Runing the JobsBraVisit==");
//	JobDetail JobsBraVisit = JobBuilder.newJob(JobsBraVisit.class)
//	.withIdentity(jobKeyBraVisit).build();	
//	
//	JobKey jobKeyStockBal = new JobKey("JobsStockBal", "group18");
//	// 	System.out.println(" Here is Runing the Jobs14==");
//	JobDetail jobsStockBal = JobBuilder.newJob(JobsStockBal.class)
//	.withIdentity(jobKeyStockBal).build();	
//	
	JobKey jobKeyDropTempTab = new JobKey("DropTemptable", "group19");
	// 	System.out.println(" Here is Runing the Jobs14==");
	JobDetail jobsDropTempTable = JobBuilder.newJob(jobsDropTempTable.class)
	.withIdentity(jobKeyDropTempTab).build();
	
	JobKey jobKeyBranchTable = new JobKey("jobsBranchTable", "group20");
	// 	System.out.println(" Here is Runing the Jobs14==");
	JobDetail jobsBranchTable = JobBuilder.newJob(jobsBranchTable.class)
	.withIdentity(jobKeyBranchTable).build();	
//	
//	Trigger trigger1 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName1", "group1")
//	.withSchedule(
//		CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger2 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName2", "group2")
//	.withSchedule(
//		CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	    	
	Trigger trigger3 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName3", "group3")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 0 0/3 * * ?"))
	.build();
	
	Trigger trigger4 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName4", "group4")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 0/5 * * * ?"))
	.build();
//	
//	Trigger trigger5 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName5", "group5")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
	
	Trigger trigger6 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName6", "group6")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 0/10 * * * ?"))
	.build();
//	
//	Trigger trigger7 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName7", "group7")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//
//	Trigger trigger8 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName8", "group8")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger9 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName9", "group9")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger10 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName10", "group10")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
	
	Trigger trigger11 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName11", "group11")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?"))
	.build();
//	
//	Trigger trigger12 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group12")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger13 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group13")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger14 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group14")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger15 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group15")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger16 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group16")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger17 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group17")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//	.build();
//	
//	Trigger trigger18 = TriggerBuilder
//	.newTrigger()
//	.withIdentity("dummyTriggerName11", "group18")
//	.withSchedule(
//	CronScheduleBuilder.cronSchedule("0 24 23 * * ? *"))
//	.build();
//	
	Trigger trigger19 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName11", "group19")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 59 23 * * ? *"))
	.build();

	Trigger trigger20 = TriggerBuilder
	.newTrigger()
	.withIdentity("dummyTriggerName20", "group4")
	.withSchedule(
	CronScheduleBuilder.cronSchedule("0 59 23 * * ? *"))
	.build();
	
	//Scheduler scheduler = new StdSchedulerFactory().getScheduler();
	
	scheduler = StdSchedulerFactory.getDefaultScheduler();

	
	scheduler.start();
//	scheduler.scheduleJob(jobA, trigger1);
//	scheduler.scheduleJob(jobB, trigger2);
	scheduler.scheduleJob(jobC, trigger3);
	scheduler.scheduleJob(jobD, trigger4);	
//	scheduler.scheduleJob(jobE, trigger5);	
	scheduler.scheduleJob(jobF, trigger6);	
//	scheduler.scheduleJob(jobG, trigger7);	
//	scheduler.scheduleJob(jobH, trigger8);	
//	scheduler.scheduleJob(jobI, trigger9);	
//	scheduler.scheduleJob(jobJ, trigger10);		
	scheduler.scheduleJob(jobK, trigger11);	
//	scheduler.scheduleJob(jobL, trigger12);	
//	scheduler.scheduleJob(jobM, trigger13);	
//	scheduler.scheduleJob(jobN, trigger14);	
//	scheduler.scheduleJob(jobO, trigger15);	
//	scheduler.scheduleJob(jobSLA, trigger16);	
//	scheduler.scheduleJob(JobsBraVisit, trigger17);	
//	scheduler.scheduleJob(jobsStockBal, trigger18);	
	scheduler.scheduleJob(jobsDropTempTable, trigger19);
	scheduler.scheduleJob(jobsBranchTable, trigger20);
	}
	catch(Exception c)
	{
		c.printStackTrace();
	}


}

}
