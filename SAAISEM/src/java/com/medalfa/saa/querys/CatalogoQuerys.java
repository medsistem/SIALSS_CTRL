/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.querys;

/**
 *
 * @author IngMa
 */
public class CatalogoQuerys {
    
    // Se obtiene la información de clave y descripción para autocomplete
    public static final String OBTENER_CLAVE_DESCRIPCION = "SELECT F_ClaPro AS clave, F_DesPro AS descripcion FROM tb_medica;";
    //public static final String OBTENER_CLAVE_DESCRIPCION = "SELECT F_ClaPro AS clave, REPLACE( REPLACE ( F_DesPro, '\\\\r', ''), '\\\\n', '' ) AS descripcion FROM tb_medica;";
    
    // Se obtiene encabezado con existencia por descripción y clave
    public static final String OBTENER_ENCABEZADO_DESCRIPCION = "SELECT l.F_ClaPro AS clave, m.F_DesPro AS descripcion, SUM(l.F_ExiLot) AS existencia FROM tb_lote l INNER JOIN tb_medica m ON m.F_DesPro = ? AND l.F_ClaPro = m.F_ClaPro AND l.F_ExiLot >= 0 GROUP BY l.F_ClaPro";

    public static final String OBTENER_ENCABEZADO_CLAVE = "SELECT  m.F_ProMov AS clave,d.F_DesPro AS descripcion,(SUM(m.F_CantMov*m.F_SigMov)) AS existencia FROM tb_movinv AS m INNER JOIN tb_medica as d ON m.F_ProMov = d.F_ClaPro WHERE m.F_ProMov = ?;";
//    public static final String OBTENER_ENCABEZADO_CLAVE = "SELECT m.F_ProMov AS clave,lot.F_DesPro AS descripcion,(SUM(m.F_CantMov*m.F_SigMov)-lot.apartado) AS existencia FROM tb_movinv AS m INNER JOIN (select l.F_ClaPro,l.F_ClaLot,l.F_Ubica, l.F_FolLot,IFNULL(sum(a.F_Cant),0) as apartado, m.F_DesPro FROM tb_lote l LEFT JOIN tb_apartado AS a ON l.F_IdLote =a.F_IdLote INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = l.F_Ubica INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro WHERE l.F_ClaPro = ? AND l.F_ExiLot <> 0 GROUP BY l.F_IdLote ) as lot on lot.F_ClaPro = m.F_ProMov and lot.F_FolLot = m.F_LotMov AND lot.F_Ubica = m.F_UbiMov;"; 
  
    // Se obtienen los lotes de la clave seleccionada
    public static final String OBTENER_LOTES_POR_CLAVE = "SELECT l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Origen AS idOrigen, o.F_DesOri AS origen, l.F_FolLot AS idLote FROM tb_lote l INNER JOIN tb_origen o ON l.F_ClaPro =? AND l.F_ClaLot <> 'X' AND l.F_Origen = o.F_ClaOri GROUP BY l.F_ClaLot, l.F_FecCad, l.F_Origen;";
            
    // Obtener información por el FolLot
    public static final String OBTENER_INFORMACION_POR_FOLLOT="SELECT l.F_ClaPro AS clave, m.F_DesPro AS descripcion, l.F_FecCad AS caducidad, l.F_Origen AS idOrigen, o.F_DesOri AS descripcionOrigen, SUM( l.F_ExiLot) AS existencia FROM tb_lote l INNER JOIN tb_origen o ON l.F_FolLot = ? INNER JOIN tb_medica m on m.F_ClaPro = l.F_ClaPro AND l.F_Origen = o.F_ClaOri";
    public static final String OBTENER_INFORMACION_POR_LOTE_CADUCIDAD="SELECT l.F_ClaPro AS clave, m.F_DesPro AS descripcion, l.F_FecCad AS caducidad, l.F_Origen AS idOrigen, o.F_DesOri AS descripcionOrigen, SUM( l.F_ExiLot) AS existencia FROM tb_lote l INNER JOIN tb_origen o ON l.F_ClaPro = ? AND l.F_ClaLot = ? AND l.F_FecCad = ? AND l.F_Origen = ? AND l.F_Origen = o.F_ClaOri INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro GROUP BY l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Origen";
    
    public static final String OBTENER_ENCABEZADO_CLAVE_FECHAS = "SELECT L.F_ClaPro, M.F_DesPro, Mov.CantMov FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN( SELECT F_ProMov, SUM(F_CantMov * F_SigMov) AS CantMov FROM tb_movinv M WHERE M.F_ProMov = ? AND M.F_ConMov != 1000 AND M.F_FecMov <= ? GROUP BY F_ProMov HAVING CantMov > 0) Mov ON L.F_ClaPro = Mov.F_ProMov WHERE l.F_ClaPro=?  GROUP BY L.F_ClaPro";
    public static final String OBTENER_ENCABEZADO_CLAVE_LOTE_CADUCIDAD_FECHAS = "SELECT SUM(m.F_CantMov * m.F_SigMov) AS existencia FROM tb_movinv m INNER JOIN( SELECT F_FolLot FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Origen = ? GROUP BY F_FolLot) lote ON m.F_ProMov = ? AND M.F_ConMov != 1000 AND M.F_FecMov <= ? AND lote.F_FolLot = m.F_LotMov;";
}
