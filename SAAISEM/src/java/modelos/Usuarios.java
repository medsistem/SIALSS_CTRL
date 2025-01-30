/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 * Modelo Usuario
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Usuarios {

    String id, usuario, nombre, apaterno, amaterno, correo, sts, fechasusp, fechaact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getFechasusp() {
        return fechasusp;
    }

    public void setFechasusp(String fechasusp) {
        this.fechasusp = fechasusp;
    }

    public String getFechaact() {
        return fechaact;
    }

    public void setFechaact(String fechaact) {
        this.fechaact = fechaact;
    }

}
