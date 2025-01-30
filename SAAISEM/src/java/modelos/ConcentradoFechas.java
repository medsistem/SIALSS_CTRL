/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 * Modelo ConcentradoFechas
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ConcentradoFechas {

    String fechas, fechaconvert, tipounidad;
    int csrd, csu, ceaps;

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public String getTipounidad() {
        return tipounidad;
    }

    public void setTipounidad(String tipounidad) {
        this.tipounidad = tipounidad;
    }

    public String getFechaconvert() {
        return fechaconvert;
    }

    public void setFechaconvert(String fechaconvert) {
        this.fechaconvert = fechaconvert;
    }

    public int getCsrd() {
        return csrd;
    }

    public void setCsrd(int csrd) {
        this.csrd = csrd;
    }

    public int getCsu() {
        return csu;
    }

    public void setCsu(int csu) {
        this.csu = csu;
    }

    public int getCeaps() {
        return ceaps;
    }

    public void setCeaps(int ceaps) {
        this.ceaps = ceaps;
    }

}
