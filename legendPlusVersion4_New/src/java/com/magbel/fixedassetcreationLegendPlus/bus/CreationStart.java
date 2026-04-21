package com.magbel.fixedassetcreationLegendPlus.bus;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CreationStart {

    private static Scheduler scheduler;

    public static void init() {
        if (scheduler == null) {
            start();
        }
    }

    private static void start() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // Start scheduler
            scheduler.start();

            // Register all jobs
//            registerJob(Jobs1.class, "Jobs1", "group1", "0/5 * * * * ?");
//            registerJob(Jobs2.class, "Jobs2", "group2", "0/5 * * * * ?");
            registerJob(Jobs3.class, "Jobs3", "group3", "0/5 * * * * ?");
            registerJob(Jobs4.class, "Jobs4", "group4", "0/5 * * * * ?");
            registerJob(Jobs5.class, "Jobs5", "group5", "0/5 * * * * ?");
            registerJob(Jobs6.class, "Jobs6", "group6", "0/5 * * * * ?");
            registerJob(Jobs7.class, "Jobs7", "group7", "0/5 * * * * ?");
            registerJob(Jobs8.class, "Jobs8", "group8", "0/5 * * * * ?");
            registerJob(Jobs9.class, "Jobs9", "group9", "0/5 * * * * ?");
            registerJob(Jobs10.class, "Jobs10", "group10", "0/5 * * * * ?");
            registerJob(Jobs11.class, "Jobs11", "group11", "0/5 * * * * ?");
            registerJob(Jobs12.class, "Jobs12", "group12", "0/5 * * * * ?");
            registerJob(Jobs13.class, "Jobs13", "group13", "0/5 * * * * ?");
            registerJob(Jobs14.class, "Jobs14", "group14", "0/5 * * * * ?");
            registerJob(Jobs15.class, "Jobs15", "group15", "0/5 * * * * ?");
            registerJob(JobsSLA.class, "JobsSLA", "group16", "0 0/20 * * * ?");
            registerJob(JobsBraVisit.class, "JobsBraVisit", "group17", "0/5 * * * * ?");

            // Special cron jobs
            registerJob(JobsStockBal.class, "JobsStockBal", "group18", "0 24 23 * * ?");
            registerJob(jobsDropTempTable.class, "DropTempTable", "group19", "0 24 23 * * ?");

            registerJob(jobsBranchTable.class, "jobsBranchTable", "group20", "0/5 * * * * ?");
            registerJob(jobsSBUTable.class, "jobsSBUTable", "group21", "0/5 * * * * ?");
            registerJob(jobsSectionTable.class, "jobsSectionTable", "group22", "0/5 * * * * ?");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to register jobs
     */
    private static void registerJob(Class<? extends Job> jobClass,
                                    String jobName,
                                    String group,
                                    String cronExpression) throws SchedulerException {

        JobKey jobKey = new JobKey(jobName, group);

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}