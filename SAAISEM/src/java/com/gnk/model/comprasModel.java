/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.model;

/**
 * Modelo de compras ingreso
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class comprasModel {

    private int idCom;
    private String fecha;
    private String clave;
    private String lote;
    private String caducidad;
    private String fabricacion;
    private int marca;
    private int proveedor;
    private String cb;
    private int tarimas;
    private int cajas;
    private int pz;
    private int tarimasUno;
    private int cajasUno;
    private int resto;
    private double costo;
    private double importeTotal;
    private double compraTotal;
    private String observaciones;
    private String folRemi;
    private String ordenCompra;
    private int claOrg;
    private String user;
    private int estado;
    private int origen;
    private int folLot;
    private int existenciasUbicacion;
    private int existenciaInsertar;
    private int indiceAInsertar;
    private int TarimasI;
    private int PzaCajas;
    private int CajasI;
    private int proyecto;
    private String clavess;
    private String plazo;
    private String unimedida;
    private String presentacion;
    private String fileFac;
    private String fileRemi;
    private String folFac;
    private String hora;
    private Integer factorEmpaque;
    private String ordenSuministro;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public comprasModel() {
    }

    public comprasModel(int idCom, String fecha, String clave, String lote, String caducidad, String fabricacion, int marca, int proveedor, String cb, int tarimas, int cajas, int pz, int tarimasUno, int cajasUno, int resto, double costo, double importeTotal, double compraTotal, String observaciones, String folRemi, String ordenCompra, int claOrg, String user, int estado, int origen, int folLot, int existenciasUbicacion, int existenciaInsertar, int indiceAInsertar, int TarimasI, int PzaCajas, int CajasI, String clavess, int proyecto,String plazo,String unimedida,String presentacion, String hora) {
        this.idCom = idCom;
        this.fecha = fecha;
        this.clave = clave;
        this.lote = lote;
        this.caducidad = caducidad;
        this.fabricacion = fabricacion;
        this.marca = marca;
        this.proveedor = proveedor;
        this.cb = cb;
        this.tarimas = tarimas;
        this.cajas = cajas;
        this.pz = pz;
        this.tarimasUno = tarimasUno;
        this.cajasUno = cajasUno;
        this.resto = resto;
        this.costo = costo;
        this.importeTotal = importeTotal;
        this.compraTotal = compraTotal;
        this.observaciones = observaciones;
        this.folRemi = folRemi;
        this.ordenCompra = ordenCompra;
        this.claOrg = claOrg;
        this.user = user;
        this.estado = estado;
        this.origen = origen;
        this.folLot = folLot;
        this.existenciasUbicacion = existenciasUbicacion;
        this.existenciaInsertar = existenciaInsertar;
        this.indiceAInsertar = indiceAInsertar;
        this.TarimasI = TarimasI;
        this.PzaCajas = PzaCajas;
        this.CajasI = CajasI;
        this.clavess = clavess;
        this.proyecto = proyecto;
        this.presentacion = presentacion;
        this.unimedida = unimedida;
        this.plazo = plazo;
        this.hora= hora;
        
    }

    public int getIndiceAInsertar() {
        return indiceAInsertar;
    }

    public void setIndiceAInsertar(int indiceAInsertar) {
        this.indiceAInsertar = indiceAInsertar;
    }

    public int getFolLot() {
        return folLot;
    }

    public void setFolLot(int folLot) {
        this.folLot = folLot;
    }

    public int getExistenciasUbicacion() {
        return existenciasUbicacion;
    }

    public void setExistenciasUbicacion(int existenciasUbicacion) {
        this.existenciasUbicacion = existenciasUbicacion;
    }

    public int getExistenciaInsertar() {
        return existenciaInsertar;
    }

    public void setExistenciaInsertar(int existenciaInsertar) {
        this.existenciaInsertar = existenciaInsertar;
    }

    public int getIdCom() {
        return idCom;
    }

    public void setIdCom(int idCom) {
        this.idCom = idCom;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public String getFabricacion() {
        return fabricacion;
    }

    public void setFabricacion(String fabricacion) {
        this.fabricacion = fabricacion;
    }

    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    public int getProveedor() {
        return proveedor;
    }

    public void setProveedor(int proveedor) {
        this.proveedor = proveedor;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public int getTarimas() {
        return tarimas;
    }

    public void setTarimas(int tarimas) {
        this.tarimas = tarimas;
    }

    public int getCajas() {
        return cajas;
    }

    public void setCajas(int cajas) {
        this.cajas = cajas;
    }

    public int getPz() {
        return pz;
    }

    public void setPz(int pz) {
        this.pz = pz;
    }

    public int getTarimasUno() {
        return tarimasUno;
    }

    public void setTarimasUno(int tarimasUno) {
        this.tarimasUno = tarimasUno;
    }

    public int getCajasUno() {
        return cajasUno;
    }

    public void setCajasUno(int cajasUno) {
        this.cajasUno = cajasUno;
    }

    public int getResto() {
        return resto;
    }

    public void setResto(int resto) {
        this.resto = resto;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public double getCompraTotal() {
        return compraTotal;
    }

    public void setCompraTotal(double compraTotal) {
        this.compraTotal = compraTotal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFolRemi() {
        return folRemi;
    }

    public void setFolRemi(String folRemi) {
        this.folRemi = folRemi;
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public int getClaOrg() {
        return claOrg;
    }

    public void setClaOrg(int claOrg) {
        this.claOrg = claOrg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public int getTarimasI() {
        return TarimasI;
    }

    public void setTarimasI(int TarimasI) {
        this.TarimasI = TarimasI;
    }

    public int getPzaCajas() {
        return PzaCajas;
    }

    public void setPzaCajas(int PzaCajas) {
        this.PzaCajas = PzaCajas;
    }

    public int getCajasI() {
        return CajasI;
    }

    public void setCajasI(int CajasI) {
        this.CajasI = CajasI;
    }

    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
    }

    public String getClavess() {
        return clavess;
    }

    public void setClavess(String clavess) {
        this.clavess = clavess;
    }

    
    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }
    
    public String getUnimedida() {
        return unimedida;
    }

    public void setUnimedida(String unimedida) {
        this.unimedida = unimedida;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
    
    public String getFileFac() {
        return fileFac;
    }

    public void setFileFac(String fileFac) {
        this.fileFac = fileFac;
    }
    
    public String getFileRemi() {
        return fileRemi;
    }

    public void setFileRemi(String fileRemi) {
        this.fileRemi = fileRemi;
    }
    
    public String getFolFac() {
        return folFac;
    }

    public void setFolFac(String folFac) {
        this.folFac = folFac;
    }

    /**
     * @return the factorEmpaque
     */
    public Integer getFactorEmpaque() {
        return factorEmpaque;
    }

    /**
     * @param factorEmpaque the factorEmpaque to set
     */
    public void setFactorEmpaque(Integer factorEmpaque) {
        this.factorEmpaque = factorEmpaque;
    }

    public String getOrdenSuministro() {
        return ordenSuministro;
    }

    public void setOrdenSuministro(String ordenSuministro) {
        this.ordenSuministro = ordenSuministro;
    }
    
}
