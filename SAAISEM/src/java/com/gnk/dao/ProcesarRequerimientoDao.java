/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

/**
 *
 * @author Anibal GNKL
 */
public interface ProcesarRequerimientoDao {

    public boolean ConfirmarRequerimiento(String Usuario,  String Unidad, String ClaCli, int Tipo, String fecha);
    
    public boolean actualizaRequerimiento(int id, int cantidad);
    
    public boolean agregaFecha(int folio, String unidad, String fecha);
}
