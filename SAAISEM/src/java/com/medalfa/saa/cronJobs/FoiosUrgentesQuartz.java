/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.cronJobs;

import com.medalfa.saa.controllers.services.FoliosUrgentesService;
import com.medalfa.saa.model.NotificacionFoliosUrgentes;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author HP-MEDALFA
 */
public class FoiosUrgentesQuartz implements Job {
    
    public static FoliosUrgentesService service = new FoliosUrgentesService();
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        try {
            System.out.println("Buscando Folios urgentes");
            NotificacionFoliosUrgentes notificacion = service.getNotificacion();
            if (notificacion != null) {
                System.out.println("Se notifica el folio urgente: " + notificacion.getFolio());
                notificacion = service.getNotificationDetails(notificacion);
                service.enviarNotificacion(notificacion);
                service.sendInfoToWMS(notificacion);
            }
        } catch (Exception ex) {
            Logger.getLogger(FoiosUrgentesQuartz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
