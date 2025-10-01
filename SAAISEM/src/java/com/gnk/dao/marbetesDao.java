/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

/**
 *
 * @author juan
 */
public interface marbetesDao {

    public String nombreUnidad(int folio, int Proyecto, int Cause);

    public boolean insertar(String unidad, int folio, int marbetes, int Proyecto, String rutaParam, int Cause);
    
    public boolean insertarSur(String unidad, int folio, int marbetes, int Proyecto, String rutaParam);
}
