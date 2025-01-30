/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

import org.json.simple.JSONArray;

/**
 * Interface facturación transaccional
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public interface FacturacionTranDao {

//factura manual obtener registro
    public JSONArray getRegistro(String ClaPro,String GCUnidad);

    public JSONArray getRegistroFact(String ClaUni, int Catalogo);

    public JSONArray getRegistroFactAuto(String ClaUni, String Catalogo, String tablaUnireq);
    
    public boolean RegistraDatosFactTemp(String Folio, String ClaUni, String IdLote, int CantMov, String FechaE, String Usuario);

//factura manual confirma
    public boolean ConfirmarFactTemp(String Usuario, String Observaciones, String Tipo, int Proyecto, String OC);
    
/*modificar folio*/
    public boolean ConfirmarFactTempFOLIO(String Usuario, int Proyecto);

    public boolean ConfirmarTranferenciaProyecto(String Usuario, String Observaciones, int Proyecto, int ProyectoFinal);

    public boolean RegistrarFolios(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);
    
/**facturacion automatica*/
    public boolean ActualizaREQ(String ClaUni, String ClaPro, int Cantidad, int Catalogo, int Idreg, String Obs, int CantidadReq, String tablaUnireq);
    
    public boolean RegistrarFoliosApartarMich(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC, String tablaUnireq);
    
    public boolean RegistrarFoliosMich(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);


    public boolean RegistrarFoliosApartarMichPorMes(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);
   
/***/
    public JSONArray getRegistroFactEnseres(String ClaUni);

    public boolean ActualizaREQEnseres(String ClaUni, String ClaPro, int Cantidad, int Idreg, String Obs, int CantidadReq);

    public boolean RegistrarFoliosEnseres(String ClaUnidad, String FecEnt, String Usuario, String Observaciones, int Proyecto);
    
  //   public boolean RegistrarFoliosAnestesia(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);


//    public boolean ActualizaREQCause(String ClaUni, String ClaPro, int Cantidad, int Catalogo, int Idreg, String Obs, int CantidadReq);

//    public boolean ConfirmarSugerencia(String ObsGral, String Solicitante);
//
//    public boolean ConfirmarSugerenciaCompra(String ObsGral, String Solicitante);
    //    public boolean Registro5Folio(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);

//    public boolean RegistrarFoliosCause(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);
//    public boolean RegistrarFoliosApartarAnestesia(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);

//    public boolean RegistrarFoliosApartar5Folio(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC);

   //    public JSONArray RegistroFactAutoCause(String ClaUni, String Catalogo, int Proyecto);

//    public boolean ConfirmarFactTempSemiCause(String Usuario, String Observaciones, String Tipo, int Proyecto, String OC);

//    public boolean ConfirmarFactTempCause(String Usuario, String Observaciones, String Tipo, int Proyecto, String OC, String Cause);


}
