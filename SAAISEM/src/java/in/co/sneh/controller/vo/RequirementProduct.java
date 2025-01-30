/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller.vo;

import java.util.Date;

/**
 *
 * @author HP-MEDALFA
 */
public class RequirementProduct {
    private Integer quantity;
    private String name;
    private String key;
    private String lotKey;
    private Long lotExpiredDate;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLotKey() {
        return lotKey;
    }

    public void setLotKey(String lotKey) {
        this.lotKey = lotKey;
    }

    public Long getLotExpiredDate() {
        return lotExpiredDate;
    }

    public void setLotExpiredDate(Long lotExpiredDate) {
        this.lotExpiredDate = lotExpiredDate;
    }


}
