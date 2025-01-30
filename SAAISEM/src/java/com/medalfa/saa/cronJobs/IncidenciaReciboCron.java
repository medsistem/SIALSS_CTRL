/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.cronJobs;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author SISTEMAS
 */
public class IncidenciaReciboCron implements ServletContextListener {

    private Thread t = null;
    private ServletContext context;
    private Scheduler sch;
    private SchedulerFactory schFactory;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        try {
            
            JobDetail jda = JobBuilder.newJob(IncidenciaReciboQuartz.class).build();
            
            Trigger triggera = TriggerBuilder.newTrigger().withIdentity("incidenciaReciboJob")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8-18 * * ?"))
                    .build();    
            schFactory = new StdSchedulerFactory();
            sch = schFactory.getScheduler();
            sch.start();
            sch.scheduleJob(jda, triggera);
        } catch (SchedulerException ex) {           
            ex.getMessage();
    }   catch (ParseException ex) {
            Logger.getLogger(IncidenciaReciboCron.class.getName()).log(Level.SEVERE, null, ex);
        }

}
    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        try {
            sch.shutdown();
        } catch (SchedulerException ex) {
            Logger.getLogger(IncidenciaReciboCron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
