/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class ThomasantSurtido {
    private String folioConcentrate;
    private String folioRemission;
    private String userAttended;
    private String userConcentrate;
    private String unit;
    private Integer project;
    private List<RequirementProduct> products;
    

    public String getFolioConcentrate() {
        return folioConcentrate;
    }

    public void setFolioConcentrate(String folioConcentrate) {
        this.folioConcentrate = folioConcentrate;
    }

    public String getFolioRemission() {
        return folioRemission;
    }

    public void setFolioRemission(String folioRemission) {
        this.folioRemission = folioRemission;
    }

    public String getUserAttended() {
        return userAttended;
    }

    public void setUserAttended(String userAttended) {
        this.userAttended = userAttended;
    }

    public String getUserConcentrate() {
        return userConcentrate;
    }

    public void setUserConcentrate(String userConcentrate) {
        this.userConcentrate = userConcentrate;
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
    
     public void addProduct(RequirementProduct product){
        if(this.products ==null){
            this.products = new ArrayList<>();
        }
        this.products.add(product);
    }

    public List<RequirementProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RequirementProduct> products) {
        this.products = products;
    }
     
     
    
}
