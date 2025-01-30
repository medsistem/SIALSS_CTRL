/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

/**
 *
 * @author HP-MEDALFA
 */
public class FacturaTemporal {
    private int folioFactura;
    private String unidad;
    private String clave;
    private int solicitado;
    private int surtido;
    private double costo;
    private double ivaPro;
    private double montoIva;
    private int folLot;
    private String fecEnt;
    private String usuario;
    private String ubicacion;
    private String observaciones;
    private int proyecto;
    private String contrato;
    private String oc;
    private int id;

    public FacturaTemporal(){
        
    }

    public FacturaTemporal(int folioFactura, String unidad, String clave, int solicitado, int surtido, double costo, double ivaPro, double montoIva, int folLot, String fecEnt, String usuario, String ubicacion, String observaciones, int proyecto, String contrato, String oc, int id) {
        this.folioFactura = folioFactura;
        this.unidad = unidad;
        this.clave = clave;
        this.solicitado = solicitado;
        this.surtido = surtido;
        this.costo = costo;
        this.ivaPro = ivaPro;
        this.montoIva = montoIva;
        this.folLot = folLot;
        this.fecEnt = fecEnt;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
        this.observaciones = observaciones;
        this.proyecto = proyecto;
        this.contrato = contrato;
        this.oc = oc;
        this.id = id;
    }
    
    
    
    public int getFolioFactura() {
        return folioFactura;
    }

    public void setFolioFactura(int folioFactura) {
        this.folioFactura = folioFactura;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(int solicitado) {
        this.solicitado = solicitado;
    }

    public int getSurtido() {
        return surtido;
    }

    public void setSurtido(int surtido) {
        this.surtido = surtido;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getIvaPro() {
        return ivaPro;
    }

    public void setIvaPro(double ivaPro) {
        this.ivaPro = ivaPro;
    }

    public double getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(double montoIva) {
        this.montoIva = montoIva;
    }

    public int getFolLot() {
        return folLot;
    }

    public void setFolLot(int folLot) {
        this.folLot = folLot;
    }

    public String getFecEnt() {
        return fecEnt;
    }

    public void setFecEnt(String fecEnt) {
        this.fecEnt = fecEnt;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
