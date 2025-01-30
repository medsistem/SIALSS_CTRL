/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HP-MEDALFA
 */
public class RemisionCostosView {
    private String noFolio;
    private String unidad;
    private String proyecto;
    private String estatus;
    private String factura;
    private String fecEntrega;
    private int totalSurt;
    private int totalClaves;
    private int idProyecto;
    private double costoTotal;

    public RemisionCostosView(){
    }    
    
    public RemisionCostosView(ResultSet res) throws SQLException{
        this.noFolio= res.getString("F_ClaDoc");
        this.unidad = res.getString("F_NomCli");
        this.proyecto = res.getString("F_DesProy");
        this.estatus = res.getString("F_StsFact");
        this.fecEntrega = res.getString("fecEnt");
        this.totalSurt = res.getInt("cantSur");
        this.totalClaves = res .getInt("Claves");
        this.costoTotal = res.getDouble("TOTAL");
        this.idProyecto = res.getInt("F_Proyecto");
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }
    
    public String getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(String noFolio) {
        this.noFolio = noFolio;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getFecEntrega() {
        return fecEntrega;
    }

    public void setFecEntrega(String fecEntrega) {
        this.fecEntrega = fecEntrega;
    }

    public int getTotalSurt() {
        return totalSurt;
    }

    public void setTotalSurt(int totalSurt) {
        this.totalSurt = totalSurt;
    }

    public int getTotalClaves() {
        return totalClaves;
    }

    public void setTotalClaves(int totalClaves) {
        this.totalClaves = totalClaves;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }
    
    
}
