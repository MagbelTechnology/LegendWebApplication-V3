package com.magbel.fixedassetcreationLegendPlusZenith.bus;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CreationStart {

    public static Scheduler scheduler;

    public CreationStart() {
        
    }

    public static synchronized void start() {

        try {

           
            if (scheduler != null && scheduler.isStarted()) {
                return;
            }

            scheduler = StdSchedulerFactory.getDefaultScheduler();

           
            scheduleJobs();

            scheduler.start();

            System.out.println("Quartz Scheduler started successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void scheduleJobs() throws SchedulerException {

        // ========================
        // JOB 3 – Every 3 Hours
        // ========================
        JobKey jobKey3 = new JobKey("Jobs3", "group3");

        if (!scheduler.checkExists(jobKey3)) {

            JobDetail job3 = JobBuilder.newJob(Jobs3.class)
                    .withIdentity(jobKey3)
                    .storeDurably()
                    .build();

            Trigger trigger3 = TriggerBuilder.newTrigger()
                    .withIdentity("triggerJobs3", "group3")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0 0/3 * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(job3, trigger3);
        }

        // ========================
        // JOB 4 – Every 5 Minutes
        // ========================
        JobKey jobKey4 = new JobKey("Jobs4", "group4");

        if (!scheduler.checkExists(jobKey4)) {

            JobDetail job4 = JobBuilder.newJob(Jobs4.class)
                    .withIdentity(jobKey4)
                    .storeDurably()
                    .build();

            Trigger trigger4 = TriggerBuilder.newTrigger()
                    .withIdentity("triggerJobs4", "group4")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0/2 * * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(job4, trigger4);
        }

        // ========================
        // JOB 6 – Every 10 Minutes
        // ========================
        JobKey jobKey6 = new JobKey("Jobs6", "group6");

        if (!scheduler.checkExists(jobKey6)) {

            JobDetail job6 = JobBuilder.newJob(Jobs6.class)
                    .withIdentity(jobKey6)
                    .storeDurably()
                    .build();

            Trigger trigger6 = TriggerBuilder.newTrigger()
                    .withIdentity("triggerJobs6", "group6")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0/10 * * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(job6, trigger6);
        }

        // ========================
        // JOB 11 – Every 1 Hour
        // ========================
        JobKey jobKey11 = new JobKey("Jobs11", "group11");

        if (!scheduler.checkExists(jobKey11)) {

            JobDetail job11 = JobBuilder.newJob(Jobs11.class)
                    .withIdentity(jobKey11)
                    .storeDurably()
                    .build();

            Trigger trigger11 = TriggerBuilder.newTrigger()
                    .withIdentity("triggerJobs11", "group11")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(job11, trigger11);
        }

        // ========================
        // Drop Temp Table – 11:59 PM
        // ========================
        JobKey jobKeyDrop = new JobKey("DropTemptable", "group19");

        if (!scheduler.checkExists(jobKeyDrop)) {

            JobDetail jobDrop = JobBuilder.newJob(jobsDropTempTable.class)
                    .withIdentity(jobKeyDrop)
                    .storeDurably()
                    .build();

            Trigger triggerDrop = TriggerBuilder.newTrigger()
                    .withIdentity("triggerDropTemp", "group19")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 59 23 * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(jobDrop, triggerDrop);
        }
        
        // ========================
        // Drop Branch Table – 11:59 PM
        // ========================
        JobKey jobKeyBranchTable = new JobKey("jobsBranchTable", "group20");

        if (!scheduler.checkExists(jobKeyBranchTable)) {

            JobDetail jobKeyBranchDrop = JobBuilder.newJob(jobsBranchTable.class)
                    .withIdentity(jobKeyBranchTable)
                    .storeDurably()
                    .build();

            Trigger trigger20 = TriggerBuilder.newTrigger()
                    .withIdentity("jobsBranchTable", "group20")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 59 23 * * ?")
                            .withMisfireHandlingInstructionDoNothing())
                    
                    .build();

            scheduler.scheduleJob(jobKeyBranchDrop, trigger20);
        }
    }

    public static synchronized void shutdown() {
        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown(true);
                System.out.println("Quartz Scheduler stopped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
