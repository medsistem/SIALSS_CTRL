/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.model;

/**
 *
 * @author HP-MEDALFA
 */
public class Apartado {
    private Integer id;
    private Integer idLote;
    private Integer cant;
    private Integer proyecto;
    private Integer status;
    private String claDoc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClaDoc() {
        return claDoc;
    }

    public void setClaDoc(String claDoc) {
        this.claDoc = claDoc;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }
    
}
