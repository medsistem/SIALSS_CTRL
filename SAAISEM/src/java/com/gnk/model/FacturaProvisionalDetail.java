/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

import java.util.Date;

/**
 *
 * @author HP-MEDALFA
 */
public class FacturaProvisionalDetail {
    private Integer id;
    private Integer idFactura;
    private Integer idLote;
    private Date fecApl;
    private String user;
    private Integer status;
    private Integer cantReq;
    private Integer cantSur;
    private Double cost;
    private Double iva;
    private Double monto;
    private Date hora;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Date getFecApl() {
        return fecApl;
    }

    public void setFecApl(Date fecApl) {
        this.fecApl = fecApl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCantReq() {
        return cantReq;
    }

    public void setCantReq(Integer cantReq) {
        this.cantReq = cantReq;
    }

    public Integer getCantSur() {
        return cantSur;
    }

    public void setCantSur(Integer cantSur) {
        this.cantSur = cantSur;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }
    
    
}
