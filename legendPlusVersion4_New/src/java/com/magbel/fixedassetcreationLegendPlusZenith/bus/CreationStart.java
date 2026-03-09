package com.magbel.fixedassetcreationLegendPlusZenith.bus;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CreationStart {

    private static Scheduler scheduler;

    public static synchronized void start() {

        try {

            if (scheduler != null && scheduler.isStarted() && !scheduler.isShutdown()) {
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

        scheduleJob("Jobs3", "group3", Jobs3.class,
                "triggerJobs3", "0 0 0/3 * * ?");

        scheduleJob("Jobs4", "group4", Jobs4.class,
                "triggerJobs4", "0 0/5 * * * ?");

        scheduleJob("Jobs6", "group6", Jobs6.class,
                "triggerJobs6", "0 0/10 * * * ?");

        scheduleJob("Jobs11", "group11", Jobs11.class,
                "triggerJobs11", "0 0 * * * ?");

        scheduleJob("DropTemptable", "group19", jobsDropTempTable.class,
                "triggerDropTemp", "0 59 23 * * ?");

        scheduleJob("jobsBranchTable", "group20", jobsBranchTable.class,
                "triggerJobsBranchTable", "0 59 23 * * ?");
    }

    private static void scheduleJob(
            String jobName,
            String group,
            Class<? extends Job> jobClass,
            String triggerName,
            String cron) throws SchedulerException {

        JobKey jobKey = new JobKey(jobName, group);

        if (!scheduler.checkExists(jobKey)) {

            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, group)
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule(cron)
                                    .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(job, trigger);
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

