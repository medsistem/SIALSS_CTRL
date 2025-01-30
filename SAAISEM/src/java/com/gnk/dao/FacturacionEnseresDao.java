/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

import org.json.simple.JSONArray;

/**
 *
 * @author Anibal GNKL
 */
public interface FacturacionEnseresDao {

    public boolean RegistrarEnseres(String Usuario, String Unidad, String ClaProE, String FechaEntE, int CantidadE, int Folio);

    public JSONArray MostrarRegistros(String Usuario, String Unidad, int Folio);

    public boolean EliminarCapEnseres(String Usuario, String Unidad, int Folio);

    public boolean EliminaRegistroEnseres(String IdReg);

    public boolean ConfirmarFactTempEnseres(String Usuario, String Observaciones, String Proyecto);

    public JSONArray MostrarEnseres();

    public JSONArray Registro(String OrdenCompra, String Proveedor);

    public JSONArray ActualizaReq(String OrdenCompra, String Proveedor, String Cantidad, String IdRegistro);

    public boolean AutorizarEnseres(String ordenCompra, String Proveedor, String Usuario);
}
