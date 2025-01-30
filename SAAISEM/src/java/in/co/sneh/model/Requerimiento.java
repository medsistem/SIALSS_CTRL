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
public class Requerimiento {
    private String claUni;
    private String claPro;
    private Integer cajasReq;
    private Integer piezasReq;
    private Date fecCarga;
    private Integer id;
    private Integer status;
    private Date fecha;
    private Integer solicitado;
    private String observaciones;

    public Requerimiento() {
    }

    public Requerimiento(String claUni, String claPro, Integer cajasReq, Integer piezasReq, Date fechaCarga, Integer id, Integer status, Date fecha, Integer solicitado, String observaciones) {
        this.claUni = claUni;
        this.claPro = claPro;
        this.cajasReq = cajasReq;
        this.piezasReq = piezasReq;
        this.fecCarga = fechaCarga;
        this.id = id;
        this.status = status;
        this.fecha = fecha;
        this.solicitado = solicitado;
        this.observaciones = observaciones;
    }

    public String getClaUni() {
        return claUni;
    }

    public void setClaUni(String claUni) {
        this.claUni = claUni;
    }

    public String getClaPro() {
        return claPro;
    }

    public void setClaPro(String claPro) {
        this.claPro = claPro;
    }

    public Integer getCajasReq() {
        return cajasReq;
    }

    public void setCajasReq(Integer cajasReq) {
        this.cajasReq = cajasReq;
    }

    public Integer getPiezasReq() {
        return piezasReq;
    }

    public void setPiezasReq(Integer piezasReq) {
        this.piezasReq = piezasReq;
    }

    public Date getFechaCarga() {
        return fecCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fecCarga = fechaCarga;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(Integer solicitado) {
        this.solicitado = solicitado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
}
