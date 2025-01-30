/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class RequerimientoFarmacia {
    private Long idRequerimiento;
    private Integer proyecto;
    private String claCli;
    private String fecReq;
    private Integer status;
    private Integer folio;
    private Integer priority;
    private String fecCreacion;
    
    private List<DetalleRequerimiento> detalles;

    public Long getIdRequerimiento() {
        return idRequerimiento;
    }

    public void setIdRequerimiento(Long idRequerimiento) {
        this.idRequerimiento = idRequerimiento;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }

    public String getClaCli() {
        return claCli;
    }

    public void setClaCli(String claCli) {
        this.claCli = claCli;
    }

    public String getFecReq() {
        return fecReq;
    }

    public void setFecReq(String fecReq) {
        this.fecReq = fecReq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(String fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public List<DetalleRequerimiento> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRequerimiento> detalles) {
        this.detalles = detalles;
    }
    
    public void addDetalle(DetalleRequerimiento d){
        if(this.detalles == null){
            this.detalles = new ArrayList<DetalleRequerimiento>();
        }
        this.detalles.add(d);
    }

}
