/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 * Modelo Remisiones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Remisiones {

    String unidad, claveuni, sts, fechae, fechaa, tipofact, tipou, fechaini, fechafin, usuario, cancela, folio1, folio2, ver, proyecto, idproyecto, proyectfactura,ubi;
    int folio, ban;
    boolean controlado;

    private int idFolio;
    
    public boolean isControlado() {
        return controlado;
    }

    public void setControlado(boolean controlado) {
        this.controlado = controlado;
    }

    public String getFolio1() {
        return folio1;
    }

    public void setFolio1(String folio1) {
        this.folio1 = folio1;
    }

    public String getFolio2() {
        return folio2;
    }

    public void setFolio2(String folio2) {
        this.folio2 = folio2;
    }

    public String getCancela() {
        return cancela;
    }

    public void setCancela(String cancela) {
        this.cancela = cancela;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaa() {
        return fechaa;
    }

    public void setFechaa(String fechaa) {
        this.fechaa = fechaa;
    }

    public String getFechaini() {
        return fechaini;
    }

    public void setFechaini(String fechaini) {
        this.fechaini = fechaini;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
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

    public String getSts() {
        return sts;
    }
    

    public void setSts(String sts) {
        this.sts = sts;
    }
    
      public String getUbi() {
        return ubi;
    }
    

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public String getFechae() {
        return fechae;
    }

    public void setFechae(String fechae) {
        this.fechae = fechae;
    }

    public String getTipofact() {
        return tipofact;
    }

    public void setTipofact(String tipofact) {
        this.tipofact = tipofact;
    }

    public String getTipou() {
        return tipou;
    }

    public void setTipou(String tipou) {
        this.tipou = tipou;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(String idproyecto) {
        this.idproyecto = idproyecto;
    }

    public String getProyectfactura() {
        return proyectfactura;
    }

    public void setProyectfactura(String proyectfactura) {
        this.proyectfactura = proyectfactura;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public int getIdFolio() {
        return idFolio;
    }

    public void setIdFolio(int idFolio) {
        this.idFolio = idFolio;
    }

}
