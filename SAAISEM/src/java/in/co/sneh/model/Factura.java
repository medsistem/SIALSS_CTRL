/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.model;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author HP-MEDALFA
 */
public class Factura {
    private Integer id;
    private Integer claDoc;
    private String claCli;
    private String status;
    private Date fechaApl;
    private String claPro;
    private Integer cantReq;
    private Integer cantSur;
    private Double costo;
    private Double iva;
    private Double monto;
    private Integer lote;
    private Date fechaEnt;
    private Date hora;
    private String usuario;
    private String ubicacion;
    private String obs;
    private String docAnt;
    private Integer proyecto;
    private String contrato;
    private String oc;
    private Integer cause;
    private Lote loteData;
    private Medica medicaLote;

    public Factura() {
    }

    public Factura(Integer id, Integer claDoc, String claCli, String status, Date fechaApl, String claPro, Integer cantReq, Integer cantSur, Double costo, Double iva, Double monto, Integer lote, Date fechaEnt, Time hora, String usuario, String ubicacion, String obs, String docAnt, Integer proyecto, String contrato, String oc, Integer cause) {
        this.id = id;
        this.claDoc = claDoc;
        this.claCli = claCli;
        this.status = status;
        this.fechaApl = fechaApl;
        this.claPro = claPro;
        this.cantReq = cantReq;
        this.cantSur = cantSur;
        this.costo = costo;
        this.iva = iva;
        this.monto = monto;
        this.lote = lote;
        this.fechaEnt = fechaEnt;
        this.hora = hora;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
        this.obs = obs;
        this.docAnt = docAnt;
        this.proyecto = proyecto;
        this.contrato = contrato;
        this.oc = oc;
        this.cause = cause;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClaDoc() {
        return claDoc;
    }

    public void setClaDoc(Integer claDoc) {
        this.claDoc = claDoc;
    }

    public String getClaCli() {
        return claCli;
    }

    public void setClaCli(String claCli) {
        this.claCli = claCli;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaApl() {
        return fechaApl;
    }

    public void setFechaApl(Date fechaApl) {
        this.fechaApl = fechaApl;
    }

    public String getClaPro() {
        return claPro;
    }

    public void setClaPro(String claPro) {
        this.claPro = claPro;
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

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public Date getFechaEnt() {
        return fechaEnt;
    }

    public void setFechaEnt(Date fechaEnt) {
        this.fechaEnt = fechaEnt;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getDocAnt() {
        return docAnt;
    }

    public void setDocAnt(String docAnt) {
        this.docAnt = docAnt;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
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

    public Integer getCause() {
        return cause;
    }

    public void setCause(Integer cause) {
        this.cause = cause;
    }

    public Lote getLoteData() {
        return loteData;
    }

    public void setLoteData(Lote loteData) {
        this.loteData = loteData;
    }

    public Medica getMedicaLote() {
        return medicaLote;
    }

    public void setMedicaLote(Medica medicaLote) {
        this.medicaLote = medicaLote;
    }
    
}
