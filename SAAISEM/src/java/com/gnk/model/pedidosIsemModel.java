/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

/**
 * Modelo de pedidos compras isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class pedidosIsemModel {

    private int id;
    private String noCompra;
    private String proveedor;
    private String clave;
    private String cb;
    private String priori;
    private String lote;
    private String caducidad;
    private int cantidad;
    private String observacion;
    private String fecha;
    private String fechaSur;
    private String hora;
    private String idUsu;
    private int status;
    private int recibido;

    public pedidosIsemModel() {
    }

    public pedidosIsemModel(int id, String noCompra, String proveedor, String clave, String cb, String priori, String lote, String caducidad, int cantidad, String observacion, String fecha, String fechaSur, String hora, String idUsu, int status, int recibido) {
        this.id = id;
        this.noCompra = noCompra;
        this.proveedor = proveedor;
        this.clave = clave;
        this.cb = cb;
        this.priori = priori;
        this.lote = lote;
        this.caducidad = caducidad;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.fecha = fecha;
        this.fechaSur = fechaSur;
        this.hora = hora;
        this.idUsu = idUsu;
        this.status = status;
        this.recibido = recibido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoCompra() {
        return noCompra;
    }

    public void setNoCompra(String noCompra) {
        this.noCompra = noCompra;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getPriori() {
        return priori;
    }

    public void setPriori(String priori) {
        this.priori = priori;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaSur() {
        return fechaSur;
    }

    public void setFechaSur(String fechaSur) {
        this.fechaSur = fechaSur;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(String idUsu) {
        this.idUsu = idUsu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRecibido() {
        return recibido;
    }

    public void setRecibido(int recibido) {
        this.recibido = recibido;
    }

}
