/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

/**
 * Interface redistribución ubicaciones transaccional
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public interface RedistribucionDao {

    public boolean RedistribucionUbica(String Ubicacion, String IdLote, int CantMov, String Usuario,String Origen);
}
