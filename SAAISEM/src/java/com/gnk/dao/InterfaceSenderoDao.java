/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Interface ingreso de compras transaccional
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public interface InterfaceSenderoDao {

    public boolean ActualizarDatos(String usuario, String lote, String Caducidad, int cantidad, String cb, String marca, int id, int tarimas, int cajas, int pzacaja, int cajasi, int resto, int tarimasI, String Costo, Integer factorEmpaque);

    public boolean Actualizarlerma(String ordenCompra, String remision, String usuarioIngreso, String UbicaN);

    public boolean IngresarExtra(String IdUsu, String usuarioIngreso);

    public boolean ActualizarlermaCross(String ordenCompra, String remision, String usuarioIngreso);

    public boolean insertSendero(String ordenCompra, String remision);

    public boolean agregarSendero(String ordenCompra, String remision, String usuarioIngreso);

    public JSONObject datosAditar(int id);

    public JSONArray getRegistro(String vOrden, String vRemi);

    public boolean IngresoParcial(String IdReg, String ordenCompra, String remision, String usuarioIngreso, String UbicaN);
    
    public boolean ActualizarDatosRes(String usuario, String lote, String Caducidad, int cantidad, String cb, String marca, int id, int tarimas, int cajas, String Costo);

}
