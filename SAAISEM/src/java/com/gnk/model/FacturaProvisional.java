/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Israel
 */
public class FacturaProvisional {

    private Integer id;
    private Integer folio;
    private String claCli;
    private Date fecEnt;
    private Date fecApl;
    private String contrato;
    private String oc;
    private String cause;
    private Integer proyecto;
    private Integer status;
    private List<FacturaProvisionalDetail> details;

    public FacturaProvisional(){
        this.details = new ArrayList<FacturaProvisionalDetail>();
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

    public String getClaCli() {
        return claCli;
    }

    public void setClaCli(String claCli) {
        this.claCli = claCli;
    }

    public Date getFecEnt() {
        return fecEnt;
    }

    public void setFecEnt(Date fecEnt) {
        this.fecEnt = fecEnt;
    }

    public Date getFecApl() {
        return fecApl;
    }

    public void setFecApl(Date fecApl) {
        this.fecApl = fecApl;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getOc() {
        return oc;
    }

    public void setOc(String oc) {
        this.oc = oc;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<FacturaProvisionalDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FacturaProvisionalDetail> details) {
        this.details = details;
    }

    public void addDetail(FacturaProvisionalDetail detail){
        this.details.add(detail);
    }
}
