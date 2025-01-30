/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.model;

import com.gnk.model.DetalleFactura;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class NotificacionFoliosUrgentes {

    private Integer id;
    private Integer folio;
    private Integer status;
    private String unidad;
    private String claCli;
    private Integer prioridad;
    private Integer tipo;
    
    private List<DetalleFactura> details;

    public NotificacionFoliosUrgentes() {
    }

    public NotificacionFoliosUrgentes(Integer id, Integer folio, Integer status, Integer tipo) {
        this.id = id;
        this.folio = folio;
        this.status = status;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public List<DetalleFactura> getDetails() {
        return details;
    }

    public void setDetails(List<DetalleFactura> details) {
        this.details = details;
    }
    
    public void addDetail(DetalleFactura detail){
        if(this.details == null){
            this.details = new ArrayList<DetalleFactura>();
        }
        this.details.add(detail);
    }

    public String getClaCli() {
        return claCli;
    }

    public void setClaCli(String claCli) {
        this.claCli = claCli;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
}
