package com.magbel.fixedassetcreation.bus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

// Referenced classes of package com.magbel.epostserver.bus:
//            Jobs, Jobs2
 
public class CronTriggerExample
{ 

    public CronTriggerExample()
    { 
    } 
 
    public void run()
        throws Exception
    { 
        Log log = LogFactory.getLog(CronTriggerExample.class);
        // log.info("------- Initializing -------------------");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        //log.info("------- checking for initial --------");
        if(sched.isInStandbyMode())
        {
          //  log.info("------- Initialization Complete --------");
          //  log.info("------- Scheduling Jobs ----------------");
            //job 1
            JobDetail job = new JobDetail("job1", "group1", Jobs2.class); 
            CronTrigger trigger = new CronTrigger("trigger1", "group1", "job1", "group1", "0/10 * * * * ?");
            sched.addJob(job, true);
            java.util.Date ft = sched.scheduleJob(trigger);
            log.info((new StringBuilder(String.valueOf(job.getFullName()))).append(" has been scheduled  to run at: ").append(ft).append(" and repeat based on expression: ").append(trigger.getCronExpression()).toString());
         
            sched.start(); 
        }
       // log.info("-- ----- Started Scheduler -----------------");
        try
        {
            Thread.sleep(1000L);
        }
        catch(Exception exception) { }
        SchedulerMetaData metaData = sched.getMetaData();
     //   log.info((new StringBuilder("Executed ")).append(metaData.numJobsExecuted()).append(" jobs.").toString());
    }

    public static void main(String args[])
        throws Exception
    {
        CronTriggerExample example = new CronTriggerExample();
        example.run();
    }
}