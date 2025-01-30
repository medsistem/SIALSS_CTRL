/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HP-MEDALFA
 */

public class DetalleFactura {

    private String clave;
    private Integer piezas;
    private Integer solicitado;
    private Integer folioLote;
    private String ubicaLote;
    private String observaciones;
    private Integer proyectoSelect;
    private String contratoSelect;
    private Double costo;
    private Double iva;
    private Double monto;
    private String oc;
    private String fecEnt;
    private Integer existencia;
    private Integer contar;
    private String usuario;

    public DetalleFactura() {

    }

    public DetalleFactura(ResultSet rset) throws SQLException {
        this.clave = rset.getString("F_ClaPro");
        this.piezas = rset.getInt("piezas");
        this.solicitado = rset.getInt("F_Solicitado");
        this.existencia = rset.getInt("EXISTENCIAS");
        this.folioLote = rset.getInt("F_FolLot");
        this.ubicaLote = rset.getString("F_Ubica");
        this.observaciones = rset.getString("F_Obs");
    }
    
    public static DetalleFactura buildDetalleFactura(ResultSet rset) throws SQLException{
        DetalleFactura detalle = new DetalleFactura();
        detalle.setClave(rset.getString("F_ClaPro"));
        detalle.setSolicitado(rset.getInt("F_CantReq"));
        detalle.setPiezas(rset.getInt("F_CantSur"));
        detalle.setFolioLote(rset.getInt("F_Lote"));
        detalle.setUbicaLote(rset.getString("F_Ubicacion"));
        detalle.setProyectoSelect(rset.getInt("F_Proyecto"));
        detalle.setCosto(rset.getDouble("F_Costo"));
        detalle.setIva(rset.getDouble("F_Iva"));
        detalle.setMonto(rset.getDouble("F_Monto"));
        detalle.setOc(rset.getString("F_OC"));
        detalle.setContratoSelect(rset.getString("F_Contrato"));
        return detalle;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }
    
    public String getFecEnt() {
        return fecEnt;
    }

    public void setFecEnt(String fecEnt) {
        this.fecEnt = fecEnt;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getPiezas() {
        return piezas;
    }

    public void setPiezas(Integer piezas) {
        this.piezas = piezas;
    }

    public Integer getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(Integer solicitado) {
        this.solicitado = solicitado;
    }

    public Integer getFolioLote() {
        return folioLote;
    }

    public void setFolioLote(Integer folioLote) {
        this.folioLote = folioLote;
    }

    public String getUbicaLote() {
        return ubicaLote;
    }

    public void setUbicaLote(String ubicaLote) {
        this.ubicaLote = ubicaLote;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getProyectoSelect() {
        return proyectoSelect;
    }

    public void setProyectoSelect(Integer proyectoSelect) {
        this.proyectoSelect = proyectoSelect;
    }

    public String getContratoSelect() {
        return contratoSelect;
    }

    public void setContratoSelect(String contratoSelect) {
        this.contratoSelect = contratoSelect;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
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

    public String getOc() {
        return oc;
    }

    public void setOc(String oc) {
        this.oc = oc;
    }

    public Integer getContar() {
        return contar;
    }

    public void setContar(Integer contar) {
        this.contar = contar;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString(){
        return this.clave +", "+ this.folioLote;
    }
}
