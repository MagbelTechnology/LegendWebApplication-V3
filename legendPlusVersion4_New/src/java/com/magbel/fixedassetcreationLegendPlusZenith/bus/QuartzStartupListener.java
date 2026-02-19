package com.magbel.fixedassetcreationLegendPlusZenith.bus;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class QuartzStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CreationStart.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        CreationStart.shutdown();
    }
}