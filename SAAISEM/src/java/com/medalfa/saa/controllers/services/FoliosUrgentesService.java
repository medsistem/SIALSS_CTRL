/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import Correo.CorreoFoliosUrgentes;
import Correo.CorreoHelper;
import com.gnk.model.DetalleFactura;
import com.medalfa.saa.dao.impl.NotificacionFoliosUrgentesDAO;
import com.medalfa.saa.dao.impl.RequerimientoFarmaciaDAO;
import com.medalfa.saa.model.DetalleRequerimiento;
import com.medalfa.saa.model.NotificacionFoliosUrgentes;
import com.medalfa.saa.model.RequerimientoFarmacia;
import conn.ConectionDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class FoliosUrgentesService {
    private NotificacionFoliosUrgentesDAO dao;
    
    private RequerimientoFarmaciaDAO requerimientoDao;
    
    private Connection c;
    
    public FoliosUrgentesService(){
        ConectionDB con = new ConectionDB();
        c = con.getConn();
        this.dao = new NotificacionFoliosUrgentesDAO(c);
        this.requerimientoDao = new RequerimientoFarmaciaDAO(c);
    }
    
    public void closeConnection() throws SQLException{
        c.close();
    }
    
    public void enviarNotificacion(NotificacionFoliosUrgentes notificacion){
        CorreoHelper ch = new CorreoFoliosUrgentes(notificacion);
        ch.sendMail();
    }
    
    private void conectarBD(){
        ConectionDB con = new ConectionDB();
        c = con.getConn();
        this.dao = new NotificacionFoliosUrgentesDAO(c);
        this.requerimientoDao = new RequerimientoFarmaciaDAO(c);
    }
    
    private void testConnection(){
        try {
            if(this.c == null || this.c.isClosed()){
                this.conectarBD();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FoliosUrgentesService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public NotificacionFoliosUrgentes getNotificacion(){
        this.testConnection();
        return this.dao.getNotificacionPendiente();
    }
    
    public NotificacionFoliosUrgentes getNotificationDetails(NotificacionFoliosUrgentes notificacion){
        List<DetalleFactura> details = this.dao.getDetails(notificacion);
        notificacion.setDetails(details);
        return notificacion;
    }
    
    public void sendInfoToWMS(NotificacionFoliosUrgentes notificacion){
        RequerimientoFarmacia requerimiento = new RequerimientoFarmacia();
        requerimiento.setProyecto(2);
        requerimiento.setClaCli(notificacion.getClaCli());
        requerimiento.setFolio(notificacion.getFolio());
        requerimiento.setPriority(notificacion.getPrioridad());
        requerimiento.setStatus(notificacion.getStatus());
        
        for(DetalleFactura d: notificacion.getDetails()){
            DetalleRequerimiento dr = new DetalleRequerimiento();
            dr.setCantidad(d.getPiezas());
            dr.setClave(d.getClave());
            dr.setBatchCode(d.getOc());
            requerimiento.addDetalle(dr);
        }
        
        this.requerimientoDao.guardar(requerimiento);
    }
}
