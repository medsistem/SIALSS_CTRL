/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.model;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP-MEDALFA
 */
public class Lote {

    Date fecFabD;
    Date fecCadD;
    Integer idLote;
    String claPro;
    String claLot;
    String fecCad;
    Integer existencia;
    Integer folLot;
    String ubicacion;
    String fechaFab;
    String cb;
    String marca;
    Integer claMar;
    Integer origen;
    String proveedor;
    Integer claProvee;
    String uniMed;
    Integer proyecto;

    public Lote(){}
    
    public static Lote creatLote(Vector data){
        Lote l = new Lote();
        l.claPro = data.get(1).toString();
        l.claLot = data.get(2).toString();
        l.fecCad = data.get(3).toString();
        l.existencia = Integer.parseInt(data.get(4).toString());
        l.ubicacion = data.get(5).toString();
        l.fechaFab = data.get(6).toString();
        l.cb = data.get(7).toString();
        l.claMar = Integer.parseInt(data.get(8).toString());
        l.claProvee = Integer.parseInt(data.get(9).toString());
        l.uniMed = "131";
        l.proyecto = Integer.parseInt(data.get(10).toString());
        
        return l;
    }
    
    public Lote(Vector data) {
//        this.claPro = data.get(1).toString();
//        this.claLot = data.get(2).toString();
//        this.fecCad = data.get(3).toString();
//        this.existencia = Integer.parseInt(data.get(4).toString());
//        this.ubicacion = data.get(5).toString();
//        this.fechaFab = data.get(6).toString();
//        this.cb = data.get(7).toString();
//        this.marca = data.get(8).toString();
//        this.proveedor = data.get(9).toString();
//        this.uniMed = "131";
//        this.proyecto = Integer.parseInt(data.get(1).toString());
        
        this.claPro = data.get(1).toString();
        this.claLot = data.get(2).toString();
        this.fecCad = data.get(3).toString();
        this.claMar = Double.valueOf(Double.parseDouble(data.get(4).toString())).intValue();
        this.claProvee = Double.valueOf(Double.parseDouble(data.get(5).toString())).intValue();
        this.cb = data.get(6).toString();
        this.ubicacion = data.get(7).toString();
        this.existencia = Double.valueOf(Double.parseDouble(data.get(11).toString())).intValue();
        
        //this.fechaFab = data.get(6).toString();
        
        this.uniMed = "131";
        this.proyecto = 2;
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public String getClaPro() {
        return claPro;
    }

    public void setClaPro(String claPro) {
        this.claPro = claPro;
    }

    public String getClaLot() {
        return claLot;
    }

    public void setClaLot(String claLot) {
        this.claLot = claLot;
    }

    public String getFecCad() {
        return fecCad;
    }

    public void setFecCad(String fecCad) {
        this.fecCad = fecCad;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public Integer getFolLot() {
        return folLot;
    }

    public void setFolLot(Integer folLot) {
        this.folLot = folLot;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFechaFab() {
        return fechaFab;
    }

    public void setFechaFab(String fechaFab) {
        this.fechaFab = fechaFab;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getOrigen() {
        return origen;
    }

    public void setOrigen(Integer origen) {
        this.origen = origen;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUniMed() {
        return uniMed;
    }

    public void setUniMed(String uniMed) {
        this.uniMed = uniMed;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getClaProvee() {
        return claProvee;
    }

    public void setClaProvee(Integer claProvee) {
        this.claProvee = claProvee;
    }

    public Integer getClaMar() {
        return claMar;
    }

    public void setClaMar(Integer claMar) {
        this.claMar = claMar;
    }

    public Date getFecFabD() {
        return fecFabD;
    }

    public void setFecFabD(Date fecFabD) {
        this.fecFabD = fecFabD;
    }

    public Date getFecCadD() {
        return fecCadD;
    }

    public void setFecCadD(Date fecCadD) {
        this.fecCadD = fecCadD;
    }
    
    
}
