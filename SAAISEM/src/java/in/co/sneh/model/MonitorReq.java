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
public class MonitorReq {
    private Integer id;
    private Date fecha;
    private String claUni;
    private Date creacion;

    public MonitorReq() {
    }

    public MonitorReq(Integer id, Date fecha, String claUni, Date creacion) {
        this.id = id;
        this.fecha = fecha;
        this.claUni = claUni;
        this.creacion = creacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClaUni() {
        return claUni;
    }

    public void setClaUni(String claUni) {
        this.claUni = claUni;
    }

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }
    
    
}
