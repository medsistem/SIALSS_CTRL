/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 * Modelo DevolucionesFact
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class DevolucionesFact {

    String claveuni, unidad, usuario, tipou, fechaentrega;
    String clavepro, descripcion, lote, caducidad, requerido, ubicacion, surtido, devolucion, costo, monto, documento, iddocumento;
    int folio,origen,proyecto,proyectoM;
    String fechamov, docreferencia, docmovimiento, iddevolucion;
    private String desproy;

    public String getIddevolucion() {
        return iddevolucion;
    }

    public void setIddevolucion(String iddevolucion) {
        this.iddevolucion = iddevolucion;
    }

    public String getFechamov() {
        return fechamov;
    }

    public void setFechamov(String fechamov) {
        this.fechamov = fechamov;
    }

    public String getDocreferencia() {
        return docreferencia;
    }

    public void setDocreferencia(String docreferencia) {
        this.docreferencia = docreferencia;
    }

    public String getDocmovimiento() {
        return docmovimiento;
    }

    public void setDocmovimiento(String docmovimiento) {
        this.docmovimiento = docmovimiento;
    }

    public String getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(String fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public String getClavepro() {
        return clavepro;
    }

    public void setClavepro(String clavepro) {
        this.clavepro = clavepro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getRequerido() {
        return requerido;
    }

    public void setRequerido(String requerido) {
        this.requerido = requerido;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getSurtido() {
        return surtido;
    }

    public void setSurtido(String surtido) {
        this.surtido = surtido;
    }

    public String getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(String devolucion) {
        this.devolucion = devolucion;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipou() {
        return tipou;
    }

    public void setTipou(String tipou) {
        this.tipou = tipou;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getClaveuni() {
        return claveuni;
    }

    public void setClaveuni(String claveuni) {
        this.claveuni = claveuni;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
    }

    public int getProyectoM() {
        return proyectoM;
    }

    public void setProyectoM(int proyectoM) {
        this.proyectoM = proyectoM;
    }

    /**
     * @return the desproy
     */
    public String getDesproy() {
        return desproy;
    }

    /**
     * @param desproy the desproy to set
     */
    public void setDesproy(String desproy) {
        this.desproy = desproy;
    }
    
}
