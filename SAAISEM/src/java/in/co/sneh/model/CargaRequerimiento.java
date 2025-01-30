/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.model;

import java.util.Date;

/**
 *
 * @author HP-MEDALFA
 */
public class CargaRequerimiento extends Requerimiento{
    private String user;
    private Date hoy; //  ????????????????

    public CargaRequerimiento() {
    }

    public CargaRequerimiento(String user, Date hoy, String claUni, String claPro, Integer cajasReq, Integer piezasReq, Date fechaCarga, Integer id, Integer status, Date fecha, Integer solicitado, String observaciones) {
        super(claUni, claPro, cajasReq, piezasReq, fechaCarga, id, status, fecha, solicitado, observaciones);
        this.user = user;
        this.hoy = hoy;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }
    
    
}
