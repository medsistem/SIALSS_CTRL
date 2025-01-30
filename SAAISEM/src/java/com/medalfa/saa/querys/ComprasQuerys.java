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
public class ComprasQuerys 
{
    // Obtener existencias de ubicaciones temporales y mostrando existencias disponibles
    public static final String OBTENER_EXISTENCIAS_POR_PROYECTO = "SELECT existenciaTotal.clave AS clave, existenciaTotal.descripcion AS descripcion, IFNULL( existenciaTemporal.letra, 'NA') AS letra,( existenciaTotal.existencia - IFNULL( existenciaTemporal.existenciaTmp, 0 ) ) AS existenciaDisponible, IFNULL( existenciaTemporal.existenciaTmp, 0 ) AS existenciaTemporal, existenciaTotal.existencia AS existenciaTotal FROM ( SELECT l.F_ClaPro AS clave, m.F_DesPro AS descripcion, SUM( l.F_ExiLot ) AS existencia FROM tb_lote l INNER JOIN tb_medica m ON l.F_Proyecto = ? AND l.F_ExiLot > 0 AND m.F_ClaPro = l.F_ClaPro GROUP BY l.F_ClaPro ) AS existenciaTotal LEFT JOIN ( SELECT ubica.F_ClaPro, GROUP_CONCAT( ubica.letra ) AS letra, SUM( ubica.existencia ) AS existenciaTmp FROM ( SELECT l.F_ClaPro, CONCAT( ubicaTemp.letra, '-', SUM( l.F_ExiLot ) ) AS letra, SUM( l.F_ExiLot ) AS existencia FROM tb_lote l INNER JOIN ( SELECT u.F_ClaUbi, u.F_DesUbi, u.letra FROM tb_ubica u WHERE u.es_temporal = 1 ) AS ubicaTemp ON l.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_Ubica = ubicaTemp.F_ClaUbi GROUP BY l.F_ClaPro, ubicaTemp.letra ) AS ubica GROUP BY ubica.F_ClaPro ) AS existenciaTemporal ON existenciaTemporal.F_ClaPro = existenciaTotal.clave";
    
}
