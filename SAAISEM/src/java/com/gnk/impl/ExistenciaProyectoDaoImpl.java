/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.ExistenciaProyectoDao;
import conn.ConectionDBTrans;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author CEDIS TOLUCA3
 */
public class ExistenciaProyectoDaoImpl implements ExistenciaProyectoDao {

//almacen
    public static String BuscaProyecto = "SELECT * FROM tb_proyectos;";
    
    public static String BuscaProyectoCompras = "SELECT * FROM tb_proyectos where F_id not in (5);";
    
    public static String BuscaProyectoConsulta = "SELECT * FROM tb_proyectos WHERE F_Id IN (%s);";
    
    public static String ExistenciaProyectoGlobal = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot - (IFNULL(A.F_Cant, 0))) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,ifnull(null,' '),tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng, IFNULL(NULL, '') AS documento, IFNULL(NULL, '') AS OC, IFNULL(NULL, '') AS remision, IFNULL(NULL, '') AS ordSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (SELECT SUM(IFNULL(F_Cant, 0)) AS F_Cant, F_Status, F_IdLote FROM tb_apartado where  F_Status = 1 GROUP BY F_IdLote) A ON L.F_IdLote = A.F_IdLote  INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0  GROUP BY L.F_ClaPro, L.F_Proyecto;";
    public static String apartadoProyectoGlobal =   "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,ifnull(null,' '),tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng, IFNULL(NULL, '') AS documento, IFNULL(NULL, '') AS OC, IFNULL(NULL, '') AS remision, IFNULL(NULL, '') AS ordSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id inner JOIN (SELECT SUM(F_Cant) as F_Cant, F_Status, F_IdLote from tb_apartado group by F_IdLote, F_Status) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1 INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0  GROUP BY L.F_ClaPro, L.F_Proyecto;";
    
    public static String ExistenciaProyectoLote = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot - IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng, IFNULL(NULL, '') AS documento, IFNULL(NULL, '') AS OC, IFNULL(NULL, '') AS remision, IFNULL(NULL, '') AS ordSuministro  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (SELECT SUM(IFNULL(F_Cant, 0)) AS F_Cant, F_Status, F_IdLote FROM tb_apartado where  F_Status = 1 GROUP BY F_IdLote) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1 INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0  GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto,tb_proveedor.F_NomPro;";
    public static String apartadoProyectoLote = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, ifnull(null,' '),tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng, IFNULL(NULL, '') AS documento, IFNULL(NULL, '') AS OC, IFNULL(NULL, '') AS remision, IFNULL(NULL, '') AS ordSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN (SELECT SUM(F_Cant) as F_Cant, F_Status, F_IdLote from tb_apartado group by F_IdLote, F_Status) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1 INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0  GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto,tb_proveedor.F_NomPro;";
    
    public static String ExistenciaProyectoUbicacion = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR( M.F_DesPro, 1, 40 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT( L.F_FecCad, '%d/%m/%Y' ) AS F_FecCad,	SUM( L.F_ExiLot - IFNULL( A.F_Cant, 0 ) ) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND( ( SUM( L.F_ExiLot ) * M.F_Costo ), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, tb_proveedor.F_NomPro, CASE WHEN ( `L`.`F_Ubica` LIKE 'ST%' OR `L`.`F_Ubica` LIKE '%SANTIN%' OR `L`.`F_Ubica` LIKE 'PS%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MD%' OR `L`.`F_Ubica` LIKE '%1N%' OR `L`.`F_Ubica` LIKE '%AERO%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MICH%' OR `L`.`F_Ubica` LIKE 'CROSS%' ) THEN'Bajio' ELSE 'Bajio' END AS `lugar`, DATE_FORMAT( fm.fechaMov, '%d/%m/%Y' ) AS fechaMov, DATE_FORMAT( fi.fechaIng, '%d/%m/%Y' ) AS fechaIng, IF( lo.documento='null', '',lo.documento ) AS documento, IF( lo.OC='null', '',lo.OC ) AS OC, IF( lo.remision='null', '',lo.remision ) AS remision, IF( lo.OrdSuministro='null', '',lo.OrdSuministro ) ordSuministro FROM tb_lote AS L INNER JOIN tb_medica AS M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen AS O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos AS P ON L.F_Proyecto = P.F_Id LEFT JOIN ( SELECT SUM( IFNULL( F_Cant, 0 ) ) AS F_Cant, F_Status, F_IdLote FROM tb_apartado WHERE F_Status = 1 GROUP BY F_IdLote ) AS A ON L.F_IdLote = A.F_IdLote AND A.F_Status = 1 LEFT JOIN ( SELECT l.F_FolLot,l.F_Ubica, MAX( m.F_FecMov ) AS fechaMov, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov <> 51 GROUP BY l.F_IdLote) AS fm ON L.F_FolLot = fm.F_FolLot AND L.F_Ubica = fm.F_Ubica AND fm.F_ClaPro = L.F_ClaPro AND fm.F_origen = L.F_Origen LEFT JOIN (SELECT l.F_FolLot, l.F_Ubica, MIN( m.F_FecMov ) AS fechaIng, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov BETWEEN 1 AND 50 GROUP BY l.F_IdLote) AS fi ON L.F_FolLot = fi.F_FolLot AND L.F_Ubica = fi.F_Ubica AND L.F_ClaPro = fi.F_ClaPro AND L.F_Origen = fi.F_Origen LEFT JOIN (SELECT l.F_FolLot, l.F_Ubica, c.F_ClaDoc AS documento, c.F_FolRemi AS remision, c.F_OrdCom AS OC, c.F_OrdenSuministro AS OrdSuministro, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov INNER JOIN tb_compra AS c ON c.F_Lote = l.F_FolLot AND c.F_ClaDoc = m.F_DocMov GROUP BY l.F_IdLote, c.F_IdCom ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica AND lo.F_ClaPro = L.F_ClaPro AND lo.F_Origen = L.F_Origen INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE	L.F_Proyecto = ? AND L.F_ExiLot > 0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica, L.F_IdLote;";
    public static String apartadoProyectoUbicacion = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR( M.F_DesPro, 1, 40 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT( L.F_FecCad, '%d/%m/%Y' ) AS F_FecCad, SUM( IFNULL( A.F_Cant, 0 ) ) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND( ( SUM( L.F_ExiLot ) * M.F_Costo ), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, tb_proveedor.F_NomPro, CASE WHEN ( `L`.`F_Ubica` LIKE 'ST%' OR `L`.`F_Ubica` LIKE '%SANTIN%' OR `L`.`F_Ubica` LIKE 'PS%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MD%' OR `L`.`F_Ubica` LIKE '%1N%' OR `L`.`F_Ubica` LIKE '%AERO%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MICH%' OR `L`.`F_Ubica` LIKE 'CROSS%' ) THEN 'Bajio' ELSE 'Bajio' END AS `lugar`, DATE_FORMAT( fm.fechaMov, '%d/%m/%Y' ) AS fechaMov, DATE_FORMAT( fi.fechaIng, '%d/%m/%Y' ) AS fechaIng, IFNULL( lo.documento, '' ) AS documento, IFNULL( lo.OC, '' ) AS OC, IFNULL( lo.remision, '' ) AS remision, IFNULL( lo.OrdSuministro, '' ) ordSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri	INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN ( SELECT SUM( F_Cant ) AS F_Cant, F_Status, F_IdLote FROM tb_apartado GROUP BY F_IdLote, F_Status ) A ON L.F_IdLote = A.F_IdLote AND A.F_Status = 1 LEFT JOIN (SELECT l.F_FolLot,	l.F_Ubica, MAX( m.F_FecMov ) AS fechaMov, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov <> 51 GROUP BY l.F_IdLote) AS fm ON L.F_FolLot = fm.F_FolLot AND L.F_Ubica = fm.F_Ubica AND fm.F_ClaPro = L.F_ClaPro AND fm.F_origen = L.F_Origen LEFT JOIN (SELECT	l.F_FolLot, l.F_Ubica, MIN( m.F_FecMov ) AS fechaIng, l.F_ClaPro, l.F_Origen FROM	tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov BETWEEN 1 AND 50 GROUP BY l.F_IdLote) AS fi ON L.F_FolLot = fi.F_FolLot AND L.F_Ubica = fi.F_Ubica AND L.F_ClaPro = fi.F_ClaPro AND L.F_Origen = fi.F_Origen LEFT JOIN (SELECT	l.F_FolLot, l.F_Ubica, c.F_ClaDoc AS documento, c.F_FolRemi AS remision, c.F_OrdCom AS OC, c.F_OrdenSuministro AS OrdSuministro, l.F_ClaPro, l.F_Origen FROM tb_lote AS l	INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov INNER JOIN tb_compra AS c ON c.F_Lote = l.F_FolLot AND c.F_ClaDoc = m.F_DocMov GROUP BY l.F_IdLote, c.F_IdCom) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica AND lo.F_ClaPro = L.F_ClaPro AND lo.F_Origen = L.F_Origen INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ?	AND L.F_ExiLot > 0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad,	L.F_Origen, L.F_Proyecto, L.F_Ubica, L.F_IdLote;";
    
    
    public static String ExistenciaProyecto1N = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot - IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,ifnull(null,' '),tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar` FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (SELECT SUM(F_Cant) as F_Cant, F_Status, F_IdLote from tb_apartado group by F_IdLote, F_Status) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1 INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_Origen=8 GROUP BY L.F_ClaPro, L.F_Proyecto;";
    
        public static String ExistenciaProyectoUnops = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot - IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,ifnull(null,' '),tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar` FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (SELECT SUM(F_Cant) as F_Cant, F_Status, F_IdLote from tb_apartado group by F_IdLote, F_Status) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1 INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_Origen=7 GROUP BY L.F_ClaPro, L.F_Proyecto;";
   
    public static String ExistenciaProyectoTodosUnops = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica ,os as F_OrdenSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (select c.F_Lote,GROUP_CONCAT(IF(c.F_OrdenSuministro = NULL,'',c.F_OrdenSuministro)SEPARATOR' ') as os FROM tb_compra as c GROUP BY c.F_Lote) as c ON L.F_FolLot =  c.F_Lote WHERE L.F_ExiLot>0 AND L.F_Origen = 7 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
    public static String apartadoProyectoUnops = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(IFNULL(A.F_Cant, 0)) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica ,os as F_OrdenSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN (SELECT SUM(F_Cant) as F_Cant, F_Status, F_IdLote from tb_apartado group by F_IdLote, F_Status) A ON L.F_IdLote = A.F_IdLote AND A.F_Status= 1  LEFT JOIN (select c.F_Lote,GROUP_CONCAT(IF(c.F_OrdenSuministro = NULL,'',c.F_OrdenSuministro)SEPARATOR' ') as os FROM tb_compra as c GROUP BY c.F_Lote) as c ON L.F_FolLot =  c.F_Lote WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_Origen = 7 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
   
    
    public static String ExistenciaProyectoCompraGlobal = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_Proyecto;";

    public static String ExistenciaProyectoCompraLote = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";

    public static String ExistenciaProyectoCompraUbicacion = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";

    public static String ContarClaveProyecto = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0;";

    public static String ContarClaveProyectoCompra = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_Proyecto = ? AND L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') ;";

    public final String ExistenciaDisp = "SELECT p.F_DesProy AS F_DesProy, l.F_ClaPro AS F_ClaPro, substr(m.F_DesPro, 1, 40) AS F_DesPro, l.F_ClaLot AS F_ClaLot, date_format(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, sum(l.F_ExiLot) AS F_ExiLot, o.F_DesOri AS F_DesOri, l.F_Ubica AS F_Ubica, l.F_Proyecto AS F_Proyecto, substr(m.F_PrePro, 1, 40) AS F_PrePro FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE l.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ( 'AT10', 'AT11', 'AT12', 'AT13', 'AT14', 'AT15', 'AT16', 'AT17', 'AT18', 'AT19', 'AT2', 'AT20', 'AT21', 'AT22', 'AT23', 'AT24', 'AT25', 'AT26', 'AT27', 'AT28', 'AT29', 'AT3', 'AT30', 'AT31', 'AT32', 'AT4', 'AT5', 'AT6', 'AT7', 'AT8', 'AT9', 'ATI', 'CADUCADOS', 'MERMA', 'EXTRA_ORDINARIA', 'NUEVA' ) GROUP BY l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Proyecto HAVING F_ExiLot > 0;";

    public final String ExistenciaDispT = "SELECT p.F_DesProy AS F_DesProy, l.F_ClaPro AS F_ClaPro, substr(m.F_DesPro, 1, 40) AS F_DesPro, l.F_ClaLot AS F_ClaLot, date_format(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, sum(l.F_ExiLot) AS F_ExiLot, o.F_DesOri AS F_DesOri, l.F_Ubica AS F_Ubica, l.F_Proyecto AS F_Proyecto, substr(m.F_PrePro, 1, 40) AS F_PrePro FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE l.F_Proyecto IN (%s) AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ( 'AT10', 'AT11', 'AT12', 'AT13', 'AT14', 'AT15', 'AT16', 'AT17', 'AT18', 'AT19', 'AT2', 'AT20', 'AT21', 'AT22', 'AT23', 'AT24', 'AT25', 'AT26', 'AT27', 'AT28', 'AT29', 'AT3', 'AT30', 'AT31', 'AT32', 'AT4', 'AT5', 'AT6', 'AT7', 'AT8', 'AT9', 'ATI', 'CADUCADOS', 'MERMA', 'EXTRA_ORDINARIA', 'NUEVA' ) GROUP BY l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Proyecto HAVING F_ExiLot > 0;";

    public static String ContarClaveTodos = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_ExiLot > 0;";

//clientes  
    public static String BuscaProyectoCliente = "SELECT * FROM tb_proyectos WHERE F_Id IN (%s);";
    public static String ContarClaveTodosClientes = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_ExiLot > 0 AND L.F_Proyecto IN (%s);";
    public static String ExistenciaProyectoTodosClientes = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot>0 AND L.F_Proyecto IN (%s) GROUP BY L.F_ClaPro, L.F_Proyecto;";


    ///Compras
    //public static String ContarClaveTodosCompra = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA') AND L.F_FecCad > DATE_ADD(CURDATE(), INTERVAL 6 MONTH);";
    public static String ContarClaveTodosCompra = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_ExiLot > 0 AND F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA');";

    public static String ContarClaveTodosCosulta = "SELECT COUNT(DISTINCT(F_ClaPro)) AS CONTAR FROM tb_lote L WHERE L.F_ExiLot > 0 AND L.F_Proyecto IN (%s);";

    public static String ExistenciaProyectoTodosGlobal = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica,tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot>0 GROUP BY L.F_ClaPro, L.F_Proyecto;";
    public static String ExistenciaProyectoTodosLote = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica,tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, IFNULL( NULL, ' ' ) AS fechaMov, IFNULL( NULL, ' ' ) AS fechaIng FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot>0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto,tb_proveedor.F_NomPro;";
    public static String ExistenciaProyectoTodosUbicacion = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica,tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, DATE_FORMAT(lo.fechaMov, '%d/%m/%Y' ) AS fechaMov , DATE_FORMAT(lot.fechaIng, '%d/%m/%Y') AS fechaIng  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, Max(m.F_FecMov) AS fechaMov FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov GROUP BY l.F_IdLote ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, MIN(m.F_FecMov) AS fechaIng FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov  GROUP BY l.F_IdLote) AS lot ON L.F_FolLot = lot.F_FolLot AND L.F_Ubica = lot.F_Ubica INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id  INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot>0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";

    
    public static String ExistenciaProyectoTodosCompraGlobal = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot>0 AND L.F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";

    public static String ExistenciaProyectoTodosCompraLote = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot>0 AND L.F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";

    public static String ExistenciaProyectoTodosCompraUbicacion = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot>0 AND L.F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";

    public static String ExistenciaProyectoConsulta = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot>0 AND L.F_Proyecto IN ( %d )   GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
    
   
    
    
    
    private final ConectionDBTrans con = new ConectionDBTrans();
    private PreparedStatement psBuscaProyecto;
    private PreparedStatement psConsulta;
    private PreparedStatement psConsultaDisp;
    private ResultSet rs;
    private ResultSet rsDisp;

    @Override
    public JSONArray ObtenerProyectos() {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            psBuscaProyecto = con.getConn().prepareStatement(BuscaProyecto);
            rs = psBuscaProyecto.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonObj.put("Nombre", rs.getString(2));
                jsonArray.add(jsonObj);
            }
            //psBuscaProyecto.close();
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }
    
    public JSONArray ObtenerProyectosCompras() {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            psBuscaProyecto = con.getConn().prepareStatement(BuscaProyectoCompras);
            rs = psBuscaProyecto.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonObj.put("Nombre", rs.getString(2));
                jsonArray.add(jsonObj);
            }
            //psBuscaProyecto.close();
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }


    @Override
    public JSONArray MostrarRegistros(String Proyecto, String Tipo) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        String consultaApartado= "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();
            switch (Tipo) {
                case "1":
                    ConsultaExi = ExistenciaProyectoGlobal;
                    consultaApartado = apartadoProyectoGlobal;
                    break;
                case "2":
                    ConsultaExi = ExistenciaProyectoLote;
                    consultaApartado = apartadoProyectoLote;
                    break;
                case "3":
                    ConsultaExi = ExistenciaProyectoUbicacion;
                    consultaApartado = apartadoProyectoUbicacion;
                    break;
                case "4":
                    ConsultaExi = ExistenciaProyecto1N;
                    consultaApartado = apartadoProyectoUnops;
                    break;
                case "5":
                    ConsultaExi = ExistenciaProyectoUnops;
                    consultaApartado = apartadoProyectoUnops;
                    break;
                default:
                    break;
            }

            psConsulta = con.getConn().prepareStatement(ContarClaveProyecto);
            psConsulta.setString(1, Proyecto);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(ConsultaExi);
            psConsulta.setString(1, Proyecto);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", formatter.format(rs.getInt(6)));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Presentacion", rs.getString(8));
                jsonObj.put("Costo", rs.getString(9));
                jsonObj.put("Monto", rs.getString(10));
                jsonObj.put("IdProyecto", rs.getString(11));
                jsonObj.put("Ubicacion", rs.getString(12));
                jsonObj.put("Proveedor", rs.getString(13));
                jsonObj.put("Lugar", rs.getString(14));
                jsonObj.put("FechaMov", rs.getString(15));
                jsonObj.put("FechaIng", rs.getString(16));
                jsonObj.put("Documento", rs.getString(17));
                jsonObj.put("Oc", rs.getString(18));
                jsonObj.put("Remision", rs.getString(19));
                jsonObj.put("OrdSuministro", rs.getString(20));
                jsonObj.put("Tipo", Tipo);
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                                  
                
                jsonArray.add(jsonObj);
            }
            
            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(consultaApartado);
            psConsulta.setString(1, Proyecto);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
               jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3) + " - <b>APARTADO</b>");
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", formatter.format(rs.getInt(6)));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Presentacion", rs.getString(8));
                jsonObj.put("Costo", rs.getString(9));
                jsonObj.put("Monto", rs.getString(10));
                jsonObj.put("IdProyecto", rs.getString(11));
                jsonObj.put("Ubicacion", rs.getString(12));
                jsonObj.put("Proveedor", rs.getString(13));
                jsonObj.put("Lugar", rs.getString(14));               
                jsonObj.put("FechaMov", rs.getString(15));               
                jsonObj.put("FechaIng", rs.getString(16));
                jsonObj.put("Documento", rs.getString(17));
                jsonObj.put("Oc", rs.getString(18));
                jsonObj.put("Remision", rs.getString(19));
                jsonObj.put("OrdSuministro", rs.getString(20));
                jsonObj.put("Tipo", Tipo);
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarTodos(String Tipo) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExiTodo = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            switch (Tipo) {
                case "1":
                    ConsultaExiTodo = ExistenciaProyectoTodosGlobal;
                    break;
                case "2":
                    ConsultaExiTodo = ExistenciaProyectoTodosLote;
                    break;
                case "3":
                    ConsultaExiTodo = ExistenciaProyectoTodosUbicacion;
                    break;
                case "4":
                    ConsultaExiTodo = ExistenciaProyectoTodosUnops;
                default:
                    System.out.println("e");
                    ConsultaExiTodo = ExistenciaProyectoTodosUbicacion;
                    break;
            }

            psConsulta = con.getConn().prepareStatement(ContarClaveTodos);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(ConsultaExiTodo);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", formatter.format(rs.getInt(6)));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Ubicacion", rs.getString(8));
                jsonObj.put("Tipo", Tipo);
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                jsonObj.put("OrdenSuministro", rs.getString(9));
                jsonObj.put("Proveedor", rs.getString(9));
                jsonObj.put("Lugar", rs.getString(10));
                jsonObj.put("FechaMov", rs.getString(11));
                jsonObj.put("FechaIng", rs.getString(12));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray ObtenerProyectosConsulta(String Proyecto) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            psBuscaProyecto = con.getConn().prepareStatement(String.format(BuscaProyectoConsulta, Proyecto));
            rs = psBuscaProyecto.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonObj.put("Nombre", rs.getString(2));
                jsonArray.add(jsonObj);
            }
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarTodosConsulta(String Proyecto) {
        DecimalFormat formatters = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(String.format(ContarClaveTodosCosulta, Proyecto));
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(String.format(ExistenciaProyectoConsulta, Proyecto));
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", rs.getString(6));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatters.format(CantidadT));
                jsonObj.put("ContarClave", formatters.format(ContarClave));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    
    ///compras
    
    @Override
    public JSONArray MostrarTodosCompra(String Tipo) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExiTodo = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            switch (Tipo) {
                case "1":
                    ConsultaExiTodo = ExistenciaProyectoTodosCompraGlobal;
                    break;
                case "2":
                    ConsultaExiTodo = ExistenciaProyectoTodosCompraLote;
                    break;
                case "3":
                    ConsultaExiTodo = ExistenciaProyectoTodosCompraUbicacion;
                    break;
                default:
                    break;
            }

            psConsulta = con.getConn().prepareStatement(ContarClaveTodosCompra);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(ConsultaExiTodo);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", rs.getString(6));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Ubicacion", rs.getString(8));
                jsonObj.put("Tipo", Tipo);
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarRegistrosCompra(String Proyecto, String Tipo) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();
            switch (Tipo) {
                case "1":
                    ConsultaExi = ExistenciaProyectoCompraGlobal;
                    break;
                case "2":
                    ConsultaExi = ExistenciaProyectoCompraLote;
                    break;
                case "3":
                    ConsultaExi = ExistenciaProyectoCompraUbicacion;
                    break;
                default:
                    break;
            }
            psConsulta = con.getConn().prepareStatement(ContarClaveProyectoCompra);
            psConsulta.setString(1, Proyecto);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psConsulta = con.getConn().prepareStatement(ConsultaExi);
            psConsulta.setString(1, Proyecto);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", formatter.format(rs.getInt(6)));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Ubicacion", rs.getString(8));
                jsonObj.put("Tipo", Tipo);
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray ObtenerProyectosClientes(String ProyectoCL) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            psBuscaProyecto = con.getConn().prepareStatement(String.format(BuscaProyectoCliente, ProyectoCL));
            rs = psBuscaProyecto.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonObj.put("Nombre", rs.getString(2));
                jsonArray.add(jsonObj);
            }
            //psBuscaProyecto.close();
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarTodosClientes(String ProyectoCL) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(String.format(ContarClaveTodosClientes, ProyectoCL));
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                ContarClave = rs.getInt(1);
            }
            psConsulta.clearParameters();
            psConsulta = con.getConn().prepareStatement(String.format(ExistenciaProyectoTodosClientes, ProyectoCL));
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", rs.getString(6));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(ContarClave));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarRegistrosCompraDisp(String Proyecto) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        int Contar = 0, CantidadT = 0;
        try {
            con.conectar();
            psConsultaDisp = con.getConn().prepareStatement(ExistenciaDisp);
            psConsultaDisp.setString(1, Proyecto);
            rsDisp = psConsultaDisp.executeQuery();
            System.out.println(psConsulta);
            while (rsDisp.next()) {
                Contar++;
                CantidadT = CantidadT + rsDisp.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rsDisp.getString(1));
                jsonObj.put("ClaPro", rsDisp.getString(2));
                jsonObj.put("Descripcion", rsDisp.getString(3));
                jsonObj.put("Lote", rsDisp.getString(4));
                jsonObj.put("Caducidad", rsDisp.getString(5));
                jsonObj.put("Cantidad", formatter.format(rsDisp.getInt(6)));
                jsonObj.put("Origen", rsDisp.getString(7));
                jsonObj.put("Ubicacion", rsDisp.getString(8));
                jsonObj.put("Presentacion", rsDisp.getString(10));
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(Contar));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray MostrarTodosCompraDisp(String Proyecto) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        int Contar = 0, CantidadT = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(String.format(ExistenciaDispT, Proyecto));
            rs = psConsulta.executeQuery();
            System.out.println(psConsulta);
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(6);
                jsonObj = new JSONObject();
                jsonObj.put("Proyecto", rs.getString(1));
                jsonObj.put("ClaPro", rs.getString(2));
                jsonObj.put("Descripcion", rs.getString(3));
                jsonObj.put("Lote", rs.getString(4));
                jsonObj.put("Caducidad", rs.getString(5));
                jsonObj.put("Cantidad", rs.getString(6));
                jsonObj.put("Origen", rs.getString(7));
                jsonObj.put("Ubicacion", rs.getString(8));
                jsonObj.put("Contar", Contar);
                jsonObj.put("CantidadT", formatter.format(CantidadT));
                jsonObj.put("ContarClave", formatter.format(Contar));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

}
