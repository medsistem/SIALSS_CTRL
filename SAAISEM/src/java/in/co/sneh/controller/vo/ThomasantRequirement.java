/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller.vo;

import in.co.sneh.model.CargaRequerimiento;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class ThomasantRequirement {
    private String folio;
    private String user;
    private Integer priority;
    private String comments;
    private String unit;
    private Integer project;
    private List<RequirementProduct> products;

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public List<RequirementProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RequirementProduct> products) {
        this.products = products;
    }
    
    public void addProduct(RequirementProduct product){
        if(this.products ==null){
            this.products = new ArrayList<>();
        }
        this.products.add(product);
    }
    
    public List<CargaRequerimiento> buildCR(){
        List<CargaRequerimiento> requerimientos = new ArrayList<>();
        for(RequirementProduct p: this.products){
            CargaRequerimiento c = new CargaRequerimiento();
            c.setCajasReq(0);
            c.setClaPro(p.getKey());
            c.setClaUni(this.unit);
            c.setFecha(new Date());
            c.setFechaCarga(new Date());
            c.setHoy(new Date());
            c.setId(0);
            c.setObservaciones(this.comments);
            c.setPiezasReq(p.getQuantity());
            c.setSolicitado(p.getQuantity());
            c.setStatus(0);
            c.setUser(this.user);
            c.setObservaciones(this.folio);
            requerimientos.add(c);
        }
        return requerimientos;
    }
}
