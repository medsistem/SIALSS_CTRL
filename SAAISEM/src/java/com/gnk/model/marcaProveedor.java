/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

/**
 * Modelo de marcas por proveedor
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class marcaProveedor {

    private int claveMarca;
    private int claveProveedor;
    private String descripMarca;
    private String descripProveedor;

    public marcaProveedor() {
    }

    public marcaProveedor(int claveMarca, int claveProveedor, String descripMarca, String descripProveedor) {
        this.claveMarca = claveMarca;
        this.claveProveedor = claveProveedor;
        this.descripMarca = descripMarca;
        this.descripProveedor = descripProveedor;
    }

    public int getClaveMarca() {
        return claveMarca;
    }

    public void setClaveMarca(int claveMarca) {
        this.claveMarca = claveMarca;
    }

    public int getClaveProveedor() {
        return claveProveedor;
    }

    public void setClaveProveedor(int claveProveedor) {
        this.claveProveedor = claveProveedor;
    }

    public String getDescripMarca() {
        return descripMarca;
    }

    public void setDescripMarca(String descripMarca) {
        this.descripMarca = descripMarca;
    }

    public String getDescripProveedor() {
        return descripProveedor;
    }

    public void setDescripProveedor(String descripProveedor) {
        this.descripProveedor = descripProveedor;
    }

}
