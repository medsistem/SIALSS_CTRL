/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.cronJobs;

/**
 *
 * @author HP-MEDALFA
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import com.medalfa.saa.cronJobs.FoiosUrgentesQuartz;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class FoliosUrgentesCron implements ServletContextListener {

    private Thread t = null;
    private ServletContext context;
    private Scheduler sch;
    private SchedulerFactory schFactory;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        try {
            JobDetail jd = JobBuilder.newJob(FoiosUrgentesQuartz.class).withIdentity("foliosUrgentesJob").build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(10)
                                    .repeatForever())
                    .build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("crontrigger", "crontriggerFoliosUrgentes")
                    .withSchedule(CronScheduleBuilder.cronSchedule("10 * * * * ?"))
                    .build();
            
            schFactory = new StdSchedulerFactory();
            sch = schFactory.getScheduler();
            sch.start();
            sch.scheduleJob(jd, trigger);
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        try {
            sch.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(FoliosUrgentesCron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
