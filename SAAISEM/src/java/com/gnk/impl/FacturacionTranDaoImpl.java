/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import static Develuciones.ConsultaDevoDaoImpl.getFactorEmpaque;
import com.gnk.dao.FacturaProvisionalDAO;
import com.gnk.dao.FacturacionTranDao;
import com.gnk.model.DetalleFactura;
import com.gnk.model.FacturaProvisional;
import com.gnk.model.FacturaProvisionalDetail;
import com.gnk.model.FacturacionModel;
import com.gnk.util.Calendario;
//import conn.ConectionDB;
import conn.ConectionDBTrans;
import in.co.sneh.model.Apartado;
import in.co.sneh.model.FolioStatus;
import in.co.sneh.persistance.ApartadoDAOImpl;
import in.co.sneh.persistance.FolioStatusDAOImpl;
//import in.co.sneh.service.ThomasantService;
//import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class Implementación de FacturacionTranDao (Facturación automática
 * transaccional)
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class FacturacionTranDaoImpl implements FacturacionTranDao {

    //   public static String BUSCA_LOTE = "SELECT F_IdLote,F_ExiLot FROM tb_lote WHERE F_IdLote = ? AND F_ExiLot !=0 ORDER BY F_Origen, F_FecCad ASC;";
   // public static String BUSCA_LOTE = "SELECT F_IdLote,disponible FROM v_existencias WHERE F_IdLote = ? AND disponible !=0 ORDER BY F_Origen, F_FecCad ASC;";

    //public static String BUSCA_LOTE = "SELECT F_IdLote,CASE WHEN F_Origen = 19 THEN (gc.Cant_Total - v.apartado) ELSE disponible END AS disponible, F_Origen FROM v_existencias AS v LEFT JOIN tb_gasto_catastrofico AS gc ON v.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND v.F_FolLot = gc.Id_Lote AND gc.No_ClaCli = ? WHERE F_IdLote = ? AND disponible !=0 ORDER BY F_Origen, F_FecCad ASC;";
    public static String BUSCA_LOTE = "SELECT F_IdLote,CASE WHEN F_Origen = 19 THEN (gc.Cant_Total - IFNULL(fol.F_Cant,0)) ELSE disponible END AS disponible, F_Origen FROM v_existencias AS v LEFT JOIN tb_gasto_catastrofico AS gc ON v.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND v.F_FolLot = gc.Id_Lote AND gc.No_ClaCli = ?  LEFT JOIN (SELECT IFNULL(a.F_Cant,0) as F_Cant, f.F_Lote,f.F_ClaCli FROM tb_apartado AS a INNER JOIN tb_factura AS f ON f.F_ClaDoc = a.F_ClaDoc WHERE  a.F_Status = 1 GROUP BY a.F_Id) fol On fol.F_Lote = v.F_IdLote and fol.F_ClaCli = ? WHERE F_IdLote = ? AND disponible !=0 ORDER BY F_Origen, F_FecCad ASC;";
        
    public static final String INSERTAR_FACTTEM = "INSERT INTO tb_facttemp VALUES(?,?,?,?,?,3,0,?,?,0);";
    public static String BUSCA_FACTEMPID = "SELECT COUNT(*) AS IDLOTE FROM tb_facttemp WHERE F_StsFact=3 AND F_IdLot = ? GROUP BY F_IdFact;";
    public static String BUSCA_FACTEMP = "SELECT tem.F_Id, tem.F_IdFact, tem.F_ClaCli,tem.F_FecEnt, u.F_Nivel, u.F_Tipo FROM tb_facttemp AS tem LEFT JOIN tb_uniatn AS u ON tem.F_ClaCli = u.F_ClaCli WHERE tem.F_StsFact = 3 AND tem.F_User = ? GROUP BY tem.F_IdFact;";
    public static String BUSCA_INDICEFACT = "SELECT F_IndFactP%s FROM tb_indice;";
    public static String BUSCA_INDICETRANSPRODUCTO = "SELECT F_IndTProducto FROM tb_indice;";
    public static final String ACTUALIZA_INDICEFACT = "UPDATE tb_indice SET F_IndFactP%s = ?;";
    public static final String ACTUALIZA_INDICETRANSPRODUCTO = "UPDATE tb_indice SET F_IndTProducto=?;";
    public static String BUSCA_DATOSFACT = "SELECT f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol, l.F_ClaOrg, l.F_FecFab, l.F_Cb, l.F_ClaMar, l.F_Origen, l.F_UniMed FROM tb_facttemp f INNER JOIN tb_lote l ON f.F_IdLot = l.F_IdLote INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_proveedor p ON l.F_ClaOrg = p.F_ClaProve WHERE f.F_IdFact = ? and f.F_StsFact<5 AND f.F_ClaCli = ?;";
    public static String BUSCA_DATOSFACTSEMI = "SELECT f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol, l.F_ClaOrg, l.F_FecFab, l.F_Cb, l.F_ClaMar, l.F_Origen, l.F_UniMed, c.F_Cause FROM tb_facttemp f INNER JOIN tb_lote l ON f.F_IdLot = l.F_IdLote INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_proveedor p ON l.F_ClaOrg = p.F_ClaProve INNER JOIN tb_catalogoprecios c ON l.F_ClaPro=c.F_ClaPro AND l.F_Proyecto=c.F_Proyecto WHERE f.F_IdFact = ? and f.F_StsFact<5 AND f.F_ClaCli = ? AND c.F_Cause = ?;";
    public static final String ACTUALIZA_EXILOTE = "UPDATE tb_lote SET F_ExiLot=? WHERE F_IdLote=?;";
    public static final String ACTUALIZA_EXILOTEPRODUCTO = "UPDATE tb_lote SET F_ExiLot = ? WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Origen = ? AND F_Proyecto = ? AND F_FolLot = ? AND F_Ubica = ?;";
    public static String BUSCA_INDICEMOV = "SELECT F_IndMov FROM tb_indice;";
    public static final String ACTUALIZA_INDICEMOV = "UPDATE tb_indice SET F_IndMov=?;";
    public static final String INSERTA_MOVIMIENTO = "INSERT INTO tb_movinv VALUES(0,curdate(),?,?,?,?,?,?,?,?,?,?,curtime(),?,'');";
    public static final String INSERTA_FACTURA = "INSERT INTO tb_factura VALUES(0,?,?,'A',curdate(),?,?,?,?,?,?,?,?,curtime(),?,?,'',0,?,?,?,?);";
    public static final String INSERTA_FACTURATEMP = "INSERT INTO tb_facturatemp VALUES(0,?,?,'A',curdate(),?,?,?,?,?,?,?,?,curtime(),?,?,?,0,?,?,?,?);";
    public static final String ACTUALIZA_FACTTEM = "UPDATE tb_facttemp SET F_StsFact='5' WHERE F_Id=?;";

/*GASTOS CATASTROFICOS*/

public static final String ACTUALIZA_GASTOS= "UPDATE tb_gasto_catastrofico SET Cant_Total = ? , Cant_Remi = ? WHERE  Id_Gastos = ? ;";

public static final String BUSCA_GASTOS= "SELECT Id_Gastos, Cant_Total, Cant_Remi FROM tb_gasto_catastrofico WHERE Id_Lote = ? and No_clacli=?;";



    /* BUSCA_OBSERVACIONES */
    public static final String INSERTA_OBSFACTURA = "INSERT INTO tb_obserfact VALUES (?, ?, 0, 'M', ?, ?);";
    public static final String INSERTAR_OBSERVACIONES = "INSERT INTO tb_obserfact values (?, ?, 0, 'A', ?, ?)";
    public static final String BUSCA_OBSERVACIONES = " SELECT COUNT(*) FROM tb_obserfact AS ob WHERE ob.F_IdFact = ? AND ob.F_Proyecto = ?;";
    /*-------------------------------------*/

    public static String BUSCA_REQ = "SELECT U.F_ClaPro, F_ClaUni FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni in(?) AND F_Status=0 AND  F_Solicitado != 0 AND M.F_StsPro='A' AND F_N?='1';";
    public static final String ACTUALIZA_REQ = "update tb_unireq set F_PiezasReq = ? where F_ClaPro = ? and F_ClaUni = ? and F_Status='0';";
    public static final String ACTUALIZA_REQId = "update %s set F_PiezasReq = ?, F_Solicitado = ?,F_Obs=? where F_ClaPro = ? and F_ClaUni = ? and F_IdReq=? and F_Status='0';";
//    public static final String ACTUALIZA_REQIdCause = "update tb_unireq set F_PiezasReq = ?, F_Solicitado = ?,F_Obs=? where F_ClaPro = ? and F_ClaUni = ? and F_IdReq=? and F_Status='0';";
    //public static String BUSCA_PARAMETRO = "SELECT PU.F_Id,P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE F_Usuario = ?;";
    public static String BUSCA_PARAMETRO = "SELECT PU.F_Id, P.F_Id AS PROYECTO, UF.F_UbicaSQL FROM tb_parametrousuario AS PU INNER JOIN tb_proyectos AS P ON PU.F_Proyecto = P.F_Id INNER JOIN tb_ubicafact AS UF ON PU.F_Id = UF.F_idUbicaFac WHERE PU.F_Usuario = ? ;";

    public static String BUSCA_UBISOLUCION = "SELECT * FROM tb_ubicasoluciones;";
    public static String BUSCA_DATOSFACTURA = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni IN (?) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N?='1' order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    public static String BUSCA_DATOSporFACTURAR = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(LOTE.F_ExiLot,0) AS EXISTENCIAS,LOTE.F_FolLot,LOTE.F_Ubica,U.F_Obs FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ClaPro,SUM(F_ExiLot) AS F_ExiLot,F_FolLot,F_Ubica FROM tb_lote %s AND F_Proyecto = ? AND F_Origen != 8 AND F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) GROUP BY F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro left join tb_medicaunidad MU on MU.F_ClaUni = U.F_ClaUni AND MU.F_ClaPro = U.F_ClaPro WHERE U.F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d ='1'  order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    public static String BUSCA_DATOSporFACTURARMDRF = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(LOTE.F_ExiLot,0) AS EXISTENCIAS,LOTE.F_FolLot,LOTE.F_Ubica,U.F_Obs FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ClaPro,SUM(F_ExiLot) AS F_ExiLot,F_FolLot,F_Ubica FROM tb_lote %s AND F_Proyecto = ? GROUP BY F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro left join tb_medicaunidad MU on MU.F_ClaUni = U.F_ClaUni AND MU.F_ClaPro = U.F_ClaPro WHERE U.F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d ='1'  order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    
    public static String BUSCA_DATOSporFACTURARCAD = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(LOTE.F_ExiLot,0) AS EXISTENCIAS,LOTE.F_FolLot,LOTE.F_Ubica,U.F_Obs FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ClaPro,SUM(F_ExiLot) AS F_ExiLot,F_FolLot,F_Ubica FROM tb_lote %s AND F_Proyecto = ?  AND F_FecCad < CURDATE() GROUP BY F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro  WHERE U.F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d ='1'  order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    public static String BUSCA_DATOSporFACTURARN1 = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(LOTE.F_ExiLot,0) AS EXISTENCIAS,LOTE.F_FolLot,LOTE.F_Ubica,U.F_Obs FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ClaPro,SUM(F_ExiLot) AS F_ExiLot,F_FolLot,F_Ubica FROM tb_lote %s AND F_Proyecto = ? %s GROUP BY F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro %s tb_medicaunidad MU on MU.F_ClaUni = U.F_ClaUni AND MU.F_ClaPro = U.F_ClaPro WHERE U.F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d ='1'  order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    public static String BUSCA_DATOSporFACTURARGC = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(LOTE.Cant_Total,0) AS EXISTENCIAS,LOTE.Id_Lote as F_FolLot,LOTE.F_Ubica,U.F_Obs FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN ( SELECT l.F_ClaPro, Sum(l.F_ExiLot) AS F_ExiLot, gc.Id_Lote, l.F_Ubica, Sum(gc.Cant_Total) AS Cant_Total, Sum(gc.Cant_Asig) AS Cant_Asig FROM tb_lote AS l INNER JOIN tb_gasto_catastrofico AS gc ON gc.ClaPro = l.F_ClaPro COLLATE utf8_unicode_ci  AND gc.Lote = l.F_ClaLot COLLATE utf8_unicode_ci  AND  gc.Id_Lote = l.F_FolLot  INNER JOIN tb_unidgastoscata AS ugc ON gc.No_ClaCli = ugc.ClaCli %s AND l.F_Proyecto = ? %s AND ugc.ClaCli = %s GROUP BY l.F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro %s tb_medicaunidad MU on MU.F_ClaUni = U.F_ClaUni AND MU.F_ClaPro = U.F_ClaPro WHERE U.F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d ='1'  order by U.F_ClaUni ASC,U.F_ClaPro+0;";

    public static String BUSCA_DATOSporFACTURARTEMP = "SELECT F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp WHERE F_ClaCli = ? AND F_Ubicacion NOT LIKE '%CROSS%' AND F_User = ?;";

    public static String BUSCA_DATOSporFACTURARPROVISIONAL = "SELECT F_ClaPro, SUM(F_CantReq) as F_CantReq, F_CantSur, F_Lote, F_Ubicacion, F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp WHERE F_ClaCli = ? AND F_User = ? group by F_ClaPro;";

    public static String BUSCA_DATOSporFACTEMPANESTESIA = "SELECT F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp WHERE F_ClaCli = ? AND F_ClaPro NOT IN (%s) AND F_User = ?;";

    public static String BUSCA_DATOSporFACTEMP5FOLIO = "SELECT f.F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, f.F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp f WHERE F_ClaCli = ? AND f.F_DocAnt = ? AND F_User = ?;";

//    public static String BUSCA_DATOSporFACTEMP5FOLIOCAUSE = "SELECT f.F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, f.F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp f WHERE F_ClaCli = ? AND f.F_DocAnt = ? AND F_User = ? AND F_Cause = ?;";
    public static String BUSCA_DATOSporFACTURARTEMPCross = "SELECT F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp WHERE F_ClaCli = ? AND F_Ubicacion LIKE '%CROSS%' AND F_User = ?;";

    public static String BUSCA_DATOSporFACTEMPANESTESIA2 = "SELECT F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC FROM tb_facturatemp WHERE F_ClaCli = ? AND F_ClaPro IN (%s) AND F_User = ?;";

    public static String BUSCA_UNIDADFACTURA5FOLIO2 = "SELECT f.F_ClaPro, F_CantReq, F_CantSur, F_Lote, F_Ubicacion, f.F_Proyecto, F_Contrato, F_Costo, F_Iva, F_Monto, F_OC, f.F_IdFact FROM tb_facturatemp f INNER JOIN tb_lote l ON f.F_ClaPro = l.F_ClaPro AND f.F_Lote = l.F_FolLot AND f.F_Ubicacion = l.F_Ubica WHERE F_ClaCli = ? AND f.F_ClaPro IN (%s) AND F_User = ? AND l.F_Origen = 1;";

    public static String ELIMINA_DATOSporFACTURARTEMP = "DELETE FROM tb_facturatemp WHERE F_User = ?;";

//    public static String BUSCA_DATOSporFACTURARCause = "SELECT U.F_ClaUni,U.F_ClaPro, F_PiezasReq as piezas, F_IdReq,F_Solicitado as F_Solicitado,IFNULL(LOTE.F_ExiLot,0),IFNULL(MOVI.F_CantMov,0),IF(IFNULL(LOTE.F_ExiLot,0)>=IFNULL(MOVI.F_CantMov,0),IFNULL(MOVI.F_CantMov,0),IFNULL(LOTE.F_ExiLot,0)) AS EXISTENCIAS,LOTE.F_FolLot,LOTE.F_Ubica,U.F_Obs, C.F_Cause FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ClaPro,SUM(F_ExiLot) AS F_ExiLot,F_FolLot,F_Ubica FROM tb_lote %s AND F_Proyecto = ? GROUP BY F_ClaPro) AS LOTE ON U.F_ClaPro=LOTE.F_ClaPro LEFT JOIN (SELECT F_ProMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica %s AND L.F_Proyecto = ? GROUP BY F_ProMov) AS MOVI ON U.F_ClaPro=MOVI.F_ProMov INNER JOIN tb_catalogoprecios C ON U.F_ClaPro=C.F_ClaPro WHERE F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d='1' AND C.F_Proyecto = ? AND C.F_Cause = ? order by U.F_ClaUni ASC,U.F_ClaPro+0;";
    public static String BUSCA_EXITLOT = "SELECT SUM(L.F_ExiLot) AS F_ExiLot,MOVI.F_CantMov,COUNT(F_Ubica) AS Contar,IF(SUM(L.F_ExiLot)>=IF(IFNULL(MOVI.F_CantMov,0)<0,0,IFNULL(MOVI.F_CantMov,0)),IF(IFNULL(MOVI.F_CantMov,0)<0,0,IFNULL(MOVI.F_CantMov,0)),SUM(L.F_ExiLot)) AS EXILOT FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ProMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv WHERE F_ProMov=%s AND F_UbiMov IN (%s) GROUP BY F_ProMov) AS MOVI ON L.F_ClaPro=MOVI.F_ProMov WHERE L.F_Ubica IN (%s) AND L.F_ExiLot>0 AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=%s;";

    public static String BUSCA_EXITFOL = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,L.F_ExiLot,L.F_ClaOrg FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro WHERE L.F_Ubica IN (%s) AND L.F_ExiLot>0 AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=%s ORDER BY L.F_Origen,L.F_FecCad,L.F_ClaLot ASC;";

    public static String BUSCA_EXITFOLUBI = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,IF(L.F_ExiLot>=IF(IFNULL(MOVI.F_CantMov,0)<0,0,IFNULL(MOVI.F_CantMov,0)),IF(IFNULL(MOVI.F_CantMov,0)<0,0,IFNULL(MOVI.F_CantMov,0)),L.F_ExiLot) AS EXILOT,L.F_ClaOrg  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT F_ProMov,F_LotMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica %s AND F_ProMov=? AND L.F_Proyecto = ?  AND L.F_FecCad > DATE_ADD(CURDATE(),INTERVAL 7 DAY) GROUP BY F_ProMov,F_LotMov) AS MOVI ON L.F_ClaPro=MOVI.F_ProMov AND L.F_FolLot=MOVI.F_LotMov %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=? HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    public static String BUSCA_EXITFOLUBI5FOLIO = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,L.F_ExiLot AS EXILOT,L.F_ClaOrg  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=? HAVING EXILOT>0 ORDER BY L.F_Origen DESC, L.F_FecCad ASC, L.F_ClaLot ASC;";

    public static String BUSCA_EXILOTE = "SELECT F_IdLote, F_ExiLot, F_ClaOrg, F_Origen FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ? AND F_Proyecto = ?;";

    public static String BUSCA_EXILOTEU013 = "select l.*, (l.F_ExiLot - IFNULL(a.apartado, 0)) as disponible from tb_lote l inner join (select * from tb_compra where F_OrdCom like '%U013' group by F_Lote) c on l.F_FolLot = c.F_Lote LEFT JOIN (SELECT id_lote, SUM(cantSur) as apartado from folio_provisional_detail fd where fd.status = 1 group by id_lote) a on a.id_lote = l.F_IdLote where l.F_ClaPro= ? AND l.F_ExiLot > 0 AND l.F_Ubica IN (" + FacturaProvisionalDAO.ubicacionesNoU013 + ") order by l.F_FecCad;";

    public static String BUSCA_FOLIOLOTE = "SELECT F_FolLot,F_Ubica,F_Costo FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro WHERE L.F_ClaPro=? ORDER BY F_FolLot DESC;";

    public static String BUSCA_INDICELOTE = "SELECT F_IndLote FROM tb_indice;";

    public final String ACTUALIZA_INDICELOTE = "update tb_indice set F_IndLote=?;";

//////////////////////////////////////////////////////////////////////////
    public final String INSERTAR_NUEVOLOTE = "INSERT INTO tb_lote VALUES(0,?,'X','2015-01-01','0',?,'900000000','NUEVA','2013-01-01','111','10372','2','900000000','131',?)";
/////////////////////////////////////////////////////////////////////////////

//    public static String BUSCA_UNIDADES = "SELECT U.F_ClaUni FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d='1' GROUP BY U.F_ClaUni order by U.F_ClaUni ASC;";
    public static String BUSCA_UNIDADES = "SELECT U.F_ClaUni, ua.F_Nivel, ua.F_Tipo FROM %s U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro INNER JOIN tb_uniatn ua  ON U.F_ClaUni = ua.F_ClaCli   WHERE F_ClaUni IN (%s) and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N%d='1' GROUP BY U.F_ClaUni order by U.F_ClaUni ASC;";

    public static String BUSCA_UNIDADESTEMP = "SELECT U.F_ClaUni FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni IN (%s) and F_Status='2' and F_Solicitado!=0 AND M.F_StsPro='A' GROUP BY U.F_ClaUni order by U.F_ClaUni ASC;";

    public static String BUSCA_UNIDADFACTURA = "SELECT IFNULL(CRL.CantSurCRL, 0) AS CantSurCRL, IFNULL(CR.CantSurCR, 0) AS CantSurCR, F_Obs FROM tb_facturatemp F LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCR FROM tb_facturatemp WHERE F_ClaCli = ? AND F_Ubicacion LIKE '%CROSS%' AND F_User = ? ) AS CR ON F.F_ClaCli = CR.F_ClaCli LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCRL FROM tb_facturatemp WHERE F_ClaCli = ? AND F_Ubicacion NOT LIKE '%CROSS%' AND F_User = ? ) AS CRL ON F.F_ClaCli = CRL.F_ClaCli WHERE F.F_ClaCli = ? AND F_User = ? GROUP BY F.F_ClaCli;";

    public static String BUSCA_UNIDADFACTURAANESTESIA = "SELECT IFNULL(CRL.CantSurCRL, 0) AS CantSurCRL, IFNULL(CR.CantSurCR, 0) AS CantSurCR, F_Obs FROM tb_facturatemp F LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCR FROM tb_facturatemp WHERE F_ClaCli = ? AND F_ClaPro IN (%s) AND F_User = ? ) AS CR ON F.F_ClaCli = CR.F_ClaCli LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCRL FROM tb_facturatemp WHERE F_ClaCli = ? AND F_ClaPro NOT IN (%s) AND F_User = ? ) AS CRL ON F.F_ClaCli = CRL.F_ClaCli WHERE F.F_ClaCli = ? AND F_User = ? GROUP BY F.F_ClaCli;";

    public static String BUSCA_UNIDADFACTURA5FOLIO = "SELECT IFNULL(CRL.CantSurCRL, 0) AS CantSurCRL, IFNULL(CR.CantSurCR, 0) AS CantSurCR, F_Obs FROM tb_facturatemp F LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCR FROM tb_facturatemp f INNER JOIN tb_lote l ON f.F_ClaPro = l.F_ClaPro AND f.F_Lote = l.F_FolLot AND f.F_Ubicacion = l.F_Ubica WHERE F_ClaCli = ? AND f.F_ClaPro IN (%s) AND F_User = ? AND l.F_Origen = 1 ) AS CR ON F.F_ClaCli = CR.F_ClaCli LEFT JOIN ( SELECT F_ClaCli, SUM(F_CantSur) AS CantSurCRL FROM tb_facturatemp f INNER JOIN tb_lote l ON f.F_ClaPro = l.F_ClaPro AND f.F_Lote = l.F_FolLot AND f.F_Ubicacion = l.F_Ubica WHERE F_ClaCli = ? AND F_User = ? ) AS CRL ON F.F_ClaCli = CRL.F_ClaCli WHERE F.F_ClaCli = ? AND F_User = ? GROUP BY F.F_ClaCli;";

    public final String ACTUALIZA_STSREQ = "update tb_unireq set F_Status='2' where F_ClaUni=? and F_Status='0';";
//    public final String ACTUALIZA_STSREQCause = "update tb_unireq set F_Status='2' where F_ClaUni=? and F_Status='0' and F_Obs != '0'";

//    public static String DatosAbasto = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(), CASE WHEN L.F_Origen>3 THEN '0' ELSE '0' END AS ORIGEN FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_Proyecto = ? AND F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";
//    public final String InsertAbasto = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0);";
    public final String queryElimina = "DELETE FROM tb_abastoweb WHERE  F_Proyecto = ? AND F_ClaDoc = ?;";

    public static String ValidaAbasto = "SELECT COUNT(*) FROM tb_abastoweb WHERE  F_Proyecto = ? AND F_ClaDoc = ? AND F_Sts = 1;";

    public final String InsertAbasto = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0,?);";
    public static String DatosAbasto = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(), L.F_Origen AS ORIGEN, F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_Proyecto = ? AND F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

    public static String DatosAbastoByFacturaId = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(), L.F_Origen AS ORIGEN,F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen ORI ON ORI.F_ClaOri = L.F_Origen WHERE F.F_IdFact = ? GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

    public static String BuscaContrato = "SELECT F_Contrato FROM tb_proyectos WHERE F_Id = ?;";
    public static String DatosFactura = "SELECT F_Contrato, F_OC FROM tb_factura WHERE F_ClaDoc = ? LIMIT 1;";
    public static String BuscaFolioLote = "SELECT F_FolLot FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Origen = ? AND F_Proyecto = ? LIMIT 1;";
    public static String BuscaFolioLoteExist = "SELECT F_FolLot, F_ExiLot FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Origen = ? AND F_Proyecto = ? AND F_FolLot = ? AND F_Ubica = ?;";
    public final String INSERTARLOTE = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    public static String BuscaIndiceLote = "SELECT F_IndLote FROM tb_indice;";
    public final String ActualizaIndiceLote = "UPDATE tb_indice SET F_IndLote = ?;";
    public final String INSERTAtransferenciaproyecto = "INSERT INTO tb_tranferenciaproyecto VALUES (NOW(), ?, ?, ?, ?, ?, 0);";

//    public static String BuscaCause = "SELECT C.F_Cause, CONCAT('( ',T.F_Cause,' )') FROM tb_catalogoprecios C INNER JOIN tb_tipocause T ON C.F_Cause = T.F_Id WHERE F_Proyecto = ? GROUP BY C.F_Cause ORDER BY C.F_Cause + 0;";
//    public static String BuscaCauseTemp = "SELECT F.F_Cause, CONCAT('( ', T.F_Cause, ' )') FROM tb_facturatemp F INNER JOIN tb_tipocause T ON F.F_Cause = T.F_Id WHERE F_DocAnt = 0 AND F_ClaCli = ? AND F_User = ? GROUP BY F.F_Cause;";
//    public static String BuscaCauseFactAuto = "SELECT IFNULL(SUM(F_Cause), 0), IFNULL(SUM(F_PiezasReq), 0) FROM tb_unireq U INNER JOIN tb_catalogoprecios C ON U.F_ClaPro = C.F_ClaPro INNER JOIN tb_medica M ON U.F_ClaPro = M.F_ClaPro WHERE C.F_Proyecto = ? AND M.F_StsPro = 'A' AND F_Cause = ? AND U.F_ClaUni = %s AND U.F_Status = 0;";
    public static String BuscaUbicacionesCross = "SELECT F_Ubi FROM tb_ubicacrosdock;";

    public static String BuscaUbicaNoFacturar = "SELECT F_Ubi FROM tb_ubicanofacturar;";

    public static String BuscaUbicaTemporal = "SELECT F_Ubica FROM tb_ubicatemporal;";

    public static String BuscaAnestesia = "SELECT F_Clave FROM tb_isemanual;";

    public static final String RegistrarSugerencia = "INSERT INTO tb_sugerenciasaa SET F_Sugerencia = ?, F_Usuario = ?, F_TipoU = ?;";

    public static final String ActualizaIdFactura = "UPDATE tb_facturatemp SET F_DocAnt = ? WHERE F_IdFact = ?;";

//    public static final String ActualizaCause = "UPDATE tb_facturatemp U LEFT JOIN ( SELECT F_Proyecto, F_ClaPro, F_Cause, F_Costo FROM tb_catalogoprecios WHERE F_Proyecto = ? ) AS C ON U.F_ClaPro = C.F_ClaPro SET U.F_Cause = IFNULL(C.F_Cause, 0) WHERE U.F_DocAnt = 0 AND F_ClaCli = ? AND F_User = ?;";
    public static final String ActualizaFolioB = "UPDATE tb_facturatemp T INNER JOIN ( SELECT F_ClaCli, F_Cause, SUM(F_CantSur) AS CantSur FROM tb_facturatemp WHERE F_ClaCli = ? AND F_User = ? GROUP BY F_ClaCli, F_Cause HAVING CantSur > 0 LIMIT 1 ) C ON T.F_ClaCli = C.F_ClaCli SET T.F_Cause = C.F_Cause WHERE T.F_CantSur = 0 AND T.F_ClaCli = ? AND T.F_User = ?;";

    public static final String ACTUALIZA_REQIdEnseres = "UPDATE tb_enseresunireq SET F_PiezasReq = ?, F_Solicitado = ?, F_Obs =? WHERE F_ClaPro = ? AND F_ClaUni = ? AND F_IdReq =? AND F_Status = '0';";

    public static String BUSCA_UNIDADESEnseres = "SELECT U.F_ClaUni FROM tb_enseresunireq U INNER JOIN tb_enseres M ON U.F_ClaPro = M.F_Id WHERE F_ClaUni IN (%s) AND F_Status = '0' AND F_Solicitado != 0 AND M.F_Sts = 'A' GROUP BY U.F_ClaUni ORDER BY U.F_ClaUni ASC;";

    public static String BUSCA_INDICEFACTEnseres = "SELECT %s FROM tb_indice;";

    public static final String ACTUALIZA_INDICEFACTEnseres = "UPDATE tb_indice SET %s = ?;";

    public static String BUSCA_DATOSporFACTURAREnseres = "SELECT U.F_ClaUni, U.F_ClaPro, F_PiezasReq AS piezas, F_IdReq, F_Solicitado AS F_Solicitado, IFNULL(LOTE.F_ExiLot, 0), IFNULL(MOVI.F_CantMov, 0), IF ( IFNULL(LOTE.F_ExiLot, 0) >= IFNULL(MOVI.F_CantMov, 0), IFNULL(MOVI.F_CantMov, 0), IFNULL(LOTE.F_ExiLot, 0)) AS EXISTENCIAS, U.F_Obs FROM tb_enseresunireq U INNER JOIN tb_enseres M ON U.F_ClaPro = M.F_Id LEFT JOIN ( SELECT F_Id, SUM(F_Existencia) AS F_ExiLot FROM tb_enseres GROUP BY F_Id ) AS LOTE ON U.F_ClaPro = LOTE.F_Id LEFT JOIN ( SELECT F_IdEnseres, SUM(F_CantMov * F_Singo) AS F_CantMov FROM tb_enseresmovimiento M INNER JOIN tb_enseres L ON M.F_IdEnseres = L.F_Id GROUP BY F_IdEnseres ) AS MOVI ON U.F_ClaPro = MOVI.F_IdEnseres WHERE F_ClaUni IN (%s) AND F_Status = '0' AND F_Solicitado != 0 AND M.F_Sts = 'A' ORDER BY U.F_ClaUni ASC, U.F_ClaPro + 0;";

    public static String BUSCA_EXITFOLUBIEnseres = "SELECT L.F_Id, L.F_Existencia, IF ( L.F_Existencia >= IF ( IFNULL(MOVI.F_CantMov, 0) < 0, 0, IFNULL(MOVI.F_CantMov, 0)), IF ( IFNULL(MOVI.F_CantMov, 0) < 0, 0, IFNULL(MOVI.F_CantMov, 0)), L.F_Existencia ) AS EXILOT FROM tb_enseres L LEFT JOIN ( SELECT F_IdEnseres, SUM(F_CantMov * F_Singo) AS F_CantMov FROM tb_enseresmovimiento M WHERE F_IdEnseres = ? GROUP BY F_IdEnseres ) AS MOVI ON L.F_Id = MOVI.F_IdEnseres AND L.F_Existencia > 0 AND L.F_Sts = 'A' AND L.F_Id = ? HAVING EXILOT > 0;";

    public static final String ACTUALIZA_EXILOTEEnseres = "UPDATE tb_enseres SET F_Existencia =? WHERE F_Id =?;";

    public static final String INSERTA_MOVIMIENTOEnseres = "INSERT INTO tb_enseresmovimiento VALUES(0,NOW(),?,?,?,?,?,?);";

    public static final String INSERTA_FACTURAEnseres = "INSERT INTO tb_enseresfactura VALUES(0,?,?,?,?,?,?,NOW(),'A',?,?);";

    public final String ACTUALIZA_STSREQEnseres = "UPDATE tb_enseresunireq SET F_Status = '2' WHERE F_ClaUni =? AND F_Status = '0';";

    public static final String IS_ASSIGNED = "SELECT STATUS FROM federated_folios where FOLIO = ? AND ID_CLIENT = ?";

    public static String LOTES_DISPONIBLES = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv  FROM (select L.*, (F_ExiLot)- SUM(ifnull(apartado,0)) as disponible  from tb_lote L left join apartado_concentrado c on c.F_IdLote = L.F_IdLote %s AND F_ClaPro = ? AND F_Proyecto = ? group by F_IdLote) L inner join tb_lote LO on LO.F_IdLote = L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=? AND L.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) AND L.F_Origen != 8 HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    public static String LOTES_DISPONIBLESMDRF = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv  FROM (select L.*, (F_ExiLot)- SUM(ifnull(apartado,0)) as disponible  from tb_lote L left join apartado_concentrado c on c.F_IdLote = L.F_IdLote %s AND F_ClaPro = ? AND F_Proyecto = ? group by F_IdLote) L inner join tb_lote LO on LO.F_IdLote = L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=?  HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    
    public static String LOTES_DISPONIBLESN1 = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv  FROM (select L.*, (F_ExiLot)- SUM(ifnull(apartado,0)) as disponible  from tb_lote L left join apartado_concentrado c on c.F_IdLote = L.F_IdLote %s AND F_ClaPro = ? AND F_Proyecto = ? group by F_IdLote) L inner join tb_lote LO on LO.F_IdLote = L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=?  %s  HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    public static String LOTES_DISPONIBLESCAD = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv  FROM (select L.*, (F_ExiLot)- SUM(ifnull(apartado,0)) as disponible  from tb_lote L left join apartado_concentrado c on c.F_IdLote = L.F_IdLote %s AND F_ClaPro = ? AND F_Proyecto = ? group by F_IdLote) L inner join tb_lote LO on LO.F_IdLote = L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=? AND L.F_FecCad < CURDATE() HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

   // public static String LOTES_DISPONIBLESGC = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv FROM ( SELECT L.*, (gc.Cant_Total)- SUM(ifnull(apartado,0)) as disponible, gc.Id_Lote,Sum(gc.Cant_Total) AS Cant_Total, Sum(gc.Cant_Asig) AS Cant_Asig FROM tb_lote AS l INNER JOIN tb_gasto_catastrofico AS gc ON gc.ClaPro = l.F_ClaPro COLLATE utf8_unicode_ci AND gc.Lote = l.F_ClaLot COLLATE utf8_unicode_ci  AND  gc.Id_Lote = l.F_FolLot  INNER JOIN tb_unidgastoscata AS ugc ON gc.No_ClaCli = ugc.Clacli left join apartado_concentrado c on c.F_IdLote = L.F_IdLote	 %s AND l.F_Proyecto = ? %s AND ugc.ClaCli = ?  and gc.Cant_Total > 0 GROUP BY l.F_ClaPro , l.F_FolLot ) L INNER JOIN tb_lote LO on LO.F_IdLote = L.F_IdLote  INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s  AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro= ? %s HAVING EXILOT > 0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";
    public static String LOTES_DISPONIBLESGC = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv FROM ( SELECT L.*, CASE WHEN (gc.Cant_Remi != SUM( ifnull(apartado,0))) THEN gc.Cant_Total ELSE gc.Cant_Total END as disponible, gc.Id_Lote,Sum(gc.Cant_Total) AS Cant_Total, Sum(gc.Cant_Asig) AS Cant_Asig FROM tb_lote AS l INNER JOIN tb_gasto_catastrofico AS gc ON gc.ClaPro = l.F_ClaPro COLLATE utf8_unicode_ci AND gc.Lote = l.F_ClaLot COLLATE utf8_unicode_ci  AND  gc.Id_Lote = l.F_FolLot  INNER JOIN tb_unidgastoscata AS ugc ON gc.No_ClaCli = ugc.Clacli left join apartado_concentrado c on c.F_IdLote = L.F_IdLote	 %s AND l.F_Proyecto = ? %s AND ugc.ClaCli = ?  and gc.Cant_Total > 0 GROUP BY l.F_ClaPro , l.F_FolLot ) L INNER JOIN tb_lote LO on LO.F_IdLote = L.F_IdLote  INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s  AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro= ? %s HAVING EXILOT > 0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    public static String LOTES_DISPONIBLES_MES = "SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica,disponible AS EXILOT,LO.F_ClaPrv  FROM (select L.*, (F_ExiLot)- SUM(ifnull(apartado,0)) as disponible  from tb_lote L left join apartado_concentrado c on c.F_IdLote = L.F_IdLote %s AND F_ClaPro = ? AND F_Proyecto = ? group by F_IdLote) L inner join tb_lote LO on LO.F_IdLote = L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro %s AND L.F_ExiLot>0 AND L.F_Proyecto = ? AND M.F_N%d='1' AND M.F_StsPro='A' AND L.F_ClaPro=? AND L.F_FecCad BETWEEN ? AND ? HAVING EXILOT>0 ORDER BY L.F_FecCad,L.F_ClaLot ASC;";

    public static String LOTE_EN_FOLIO = "SELECT F_IdFact, F_CantSur, F_CantReq,F_ClaCli from tb_factura where F_ClaDoc= ? AND F_Lote = ? AND F_Ubicacion = ? AND F_Proyecto = ? AND F_StsFact = 'A'";

    public static String CAMBIA_CANT_FACT = "UPDATE tb_factura set F_CantReq = ?, F_CantSur = ? where F_IdFact = ? and F_StsFact = 'A'";

    private final ConectionDBTrans con = new ConectionDBTrans();

    String[] fechasIniciales = {"2021-08-01", "2021-09-01", "2021-10-01", "2021-11-01", "2021-12-01"};
    String[] fechasFinales = {"2021-08-31", "2021-09-30", "2021-10-31", "2021-11-30", "2021-12-31"};

    private ResultSet rs;
    private ResultSet rsBuscaUnidad;
    private ResultSet rsBuscaUnidadFactura;
    private ResultSet rsContrato;
    private ResultSet rsContarReg;
    private ResultSet rsBuscaFolioLot;
    private ResultSet rsBuscaExiFol;
    private ResultSet rsBuscaDatosFact;
//    private ResultSet rsCause;
//    private ResultSet rsCauseFact;
    private ResultSet rsConsulta;
    private ResultSet rsIndice;
    private ResultSet rsIndiceLote;
    private ResultSet rsTemp;
    private ResultSet rsUbicaCross;
    private ResultSet rsAnestesia;
//  private ResultSet rsUbicaNoFacturar;
    private ResultSet rsUbicaTemp;
    private ResultSet rsAbasto;

    /* observa factura */
    private ResultSet rsBuscaObsFact;
    private PreparedStatement psBuscaObsFact;
    /*--------------------------------------*/
    private PreparedStatement psBuscaLote;
    private PreparedStatement psBuscaFacTemp;
    private PreparedStatement psBuscaAnestesia;
    private PreparedStatement psBuscaContrato;
    private PreparedStatement psUbicaCrossdock;
    private PreparedStatement psUbicaTemporal;
    private PreparedStatement psUbicaNoFacturar;
    private PreparedStatement psBuscaFolioLote;
    private PreparedStatement psContarReg;
    private PreparedStatement psBuscaExiFol;
    private PreparedStatement psBuscaUnidad;
    private PreparedStatement psAbasto;
    private PreparedStatement psAbastoInsert;
    private PreparedStatement psBuscaUnidadFactura;
    private PreparedStatement psBuscaTemp;
    private PreparedStatement psBuscaDatosFact;
    private PreparedStatement psBuscaIndice;
    private PreparedStatement psBuscaIndiceLote;
    private PreparedStatement psINSERTLOTE;
    private PreparedStatement psActualizaIndice;
    private PreparedStatement psActualizaIndiceLote;
    private PreparedStatement psActualizaLote;
    private PreparedStatement psActualizaTemp;
    private PreparedStatement psActualizaReq;
    private PreparedStatement psInsertar;
    private PreparedStatement psInsertarMov;
    private PreparedStatement psInsertarFact;
    private PreparedStatement psInsertarFactTemp;
    private PreparedStatement psInsertarObs;
    private PreparedStatement psConsulta;
    private PreparedStatement psActualizaGastos;
//    private PreparedStatement psCause;
//    private PreparedStatement psCauseFact;
    private PreparedStatement psActualiza;

    /*Metodos*/

//factura manual
    @Override
    public JSONArray getRegistro(String ClaPro, String GCUnidad) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            ResultSet rs, rs2;
            PreparedStatement ps, ps2;
            int contar = 0,exi = 0, exi2 = 0;
            String query2 = "SELECT COUNT(*) FROM tb_gasto_catastrofico AS gc WHERE gc.No_ClaCli = ?;";
            con.conectar();
            ps2 = con.getConn().prepareStatement(query2);
            ps2.setString(1, GCUnidad);
            System.out.println("cliente Unidad de Gastos: "+GCUnidad);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                contar = rs2.getInt(1);
                System.out.println("Contar: " + contar);
            }
            if (contar > 0) {

                String query = "SELECT v.F_IdLote, IFNULL(( gc.Cant_Total - IFNULL(fol.F_Cant,0) ),0)  AS disponible, disponible AS disubica, F_Origen FROM v_existencias AS v LEFT JOIN tb_gasto_catastrofico AS gc ON v.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND v.F_FolLot = gc.Id_Lote AND gc.No_Clacli = ?  LEFT JOIN (SELECT IFNULL(a.F_Cant,0) as F_Cant, f.F_Lote,f.F_ClaCli FROM tb_apartado AS a INNER JOIN tb_factura AS f ON f.F_ClaDoc = a.F_ClaDoc WHERE  a.F_Status = 1 GROUP BY a.F_Id) fol On fol.F_Lote = v.F_IdLote   WHERE v.F_ClaPro = ? AND disponible !=0 ORDER BY v.F_Origen ASC,v.F_FecCad ASC;";
/*=======
                String query = "SELECT v.F_IdLote, IFNULL(( gc.Cant_Total - v.apartado ),0)  AS disponible, disponible AS disubica, F_Origen FROM v_existencias AS v LEFT JOIN tb_gasto_catastrofico AS gc ON v.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND v.F_FolLot = gc.Id_Lote AND gc.No_Clacli = ? WHERE v.F_ClaPro = ? AND v.disponible != 0   ORDER BY v.F_Origen ASC,v.F_FecCad ASC;";
>>>>>>> d437ae617a606f1924d3eb689d63f9c3590e2585*/
                ps = con.getConn().prepareStatement(query);
                ps.setString(2, ClaPro);
                ps.setString(1, GCUnidad);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jsonObj = new JSONObject();
                    jsonObj.put("IdReg", rs.getString(1));
                    if (rs.getString(4).equals("19")) {
                         jsonObj.put("Existencia", rs.getString(2));
                    }else{
                    jsonObj.put("Existencia", rs.getString(3));
                    }
                    
                    jsonObj.put("Existencia2", rs.getString(3));
                    jsonObj.put("Origen", rs.getString(4));

                    jsonArray.add(jsonObj);
                }

            } else {
                String query = "SELECT v.F_IdLote, CASE WHEN F_Origen = 19 THEN IFNULL(( gc.Cant_Total - v.apartado ),0) ELSE IFNULL(disponible,0) END AS disponible, disponible AS disubica, F_Origen FROM v_existencias AS v LEFT JOIN tb_gasto_catastrofico AS gc ON v.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND v.F_FolLot = gc.Id_Lote WHERE v.F_ClaPro = ? AND v.disponible != 0 ORDER BY v.F_Origen ASC,v.F_FecCad ASC;";
                ps = con.getConn().prepareStatement(query);
                ps.setString(1, ClaPro);
                rs = ps.executeQuery(); 
//                rs = ps.executeQuery();
         
                while (rs.next()) {
                    System.out.println("realizo el recorrido");
                    jsonObj = new JSONObject();


                    jsonObj.put("IdReg", rs.getString(1));
                    jsonObj.put("Existencia", rs.getString(2));

                    jsonArray.add(jsonObj);
                }
            }
        //String query = "SELECT F_IdLote,disponible FROM v_existencias WHERE F_ClaPro = ? AND disponible !=0 ORDER BY F_Origen, F_FecCad ASC;";
        //PreparedStatement ps;
//            ps = con.getConn().prepareStatement(query);
//            ps.setString(1, ClaPro);
           
//ResultSet rs;
//        try {
            
//            ps = con.getConn().prepareStatement(query);
//            ps.setString(1, ClaPro);
          

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray getRegistroFact(String ClaUni, int Catalogo) {

        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObj;
        JSONObject jsonObj2;
        System.out.println("Unidades:" + ClaUni);
        String query = "SELECT U.F_ClaPro, F_ClaUni FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni in ('" + ClaUni + "') AND F_Status=0 AND  F_Solicitado != 0 AND M.F_StsPro='A' AND F_N?='1';";
        PreparedStatement ps;
        ResultSet rs;
        try {
            int Contar = 0;
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, Catalogo);
            System.out.println("UNidades=" + ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                String Clave = rs.getString(1);
                Clave = Clave.replace(".", "");
                jsonObj = new JSONObject();
                jsonObj.put("IdReg", Clave);
                jsonObj.put("ClaPro", rs.getString(1));
                jsonObj.put("ClaUni", rs.getString(2));
                Contar = Contar + 1;
                jsonArray.add(jsonObj);

            }
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            jsonObj2 = new JSONObject();
            jsonObj2.put("Contar", Contar);
            jsonArray.add(jsonObj2);
            System.out.println("C=" + Contar);
            System.out.println("J=" + jsonArray);
            con.cierraConexion();

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

////facturacion manual
    @Override
    public boolean RegistraDatosFactTemp(String Folio, String ClaUni, String IdLote, int CantMov, String FechaE, String Usuario) {
        boolean save = false;
        int ExiLot = 0;
        Integer NoId = 0;

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psBuscaLote = con.getConn().prepareStatement(BUSCA_LOTE);
            psBuscaLote.setString(3, IdLote);
            psBuscaLote.setString(2, ClaUni);
            psBuscaLote.setString(1, ClaUni);
            rs = psBuscaLote.executeQuery();
            if (rs.next()) {
                ExiLot = rs.getInt(2);
            }
            psBuscaLote.clearParameters();
            psBuscaLote.close();
            rs.close();

            psBuscaFacTemp = con.getConn().prepareStatement(BUSCA_FACTEMPID);
            psBuscaFacTemp.setString(1, IdLote);
            rs = psBuscaFacTemp.executeQuery();
            if (rs.next()) {
                if (!rs.wasNull()) {
                    NoId = rs.getInt(1);
                } else {
                    NoId = 0;
                }
            }
            psBuscaFacTemp.clearParameters();
            psBuscaFacTemp.close();
            rs.close();

            System.out.println("numero en factemp: " + NoId +"  EXISTENCIA: "+ ExiLot);
            if (NoId == 0 ) {
                if (ExiLot <= 0) {
                    System.out.println("No hay existencia");
                    save = false;
                    return save;
                } else if (ExiLot <= CantMov && ExiLot > 0) {
                    System.out.println("Es menor: ");
                    psINSERTLOTE = con.getConn().prepareStatement(INSERTAR_FACTTEM);
                    psINSERTLOTE.setString(1, Folio);
                    psINSERTLOTE.setString(2, ClaUni);
                    psINSERTLOTE.setString(3, IdLote);
                    psINSERTLOTE.setInt(4, ExiLot);
                    psINSERTLOTE.setString(5, FechaE);
                    psINSERTLOTE.setString(6, Usuario);
                    psINSERTLOTE.setInt(7, CantMov);
                    psINSERTLOTE.execute();
                    psINSERTLOTE.clearParameters();
                    psINSERTLOTE.close();
                    save = true;
                    con.getConn().commit();
                    return save;
                } else {
                    psINSERTLOTE = con.getConn().prepareStatement(INSERTAR_FACTTEM);
                    psINSERTLOTE.setString(1, Folio);
                    psINSERTLOTE.setString(2, ClaUni);
                    psINSERTLOTE.setString(3, IdLote);
                    psINSERTLOTE.setInt(4, CantMov);
                    psINSERTLOTE.setString(5, FechaE);
                    psINSERTLOTE.setString(6, Usuario);
                    psINSERTLOTE.setInt(7, CantMov);
                    psINSERTLOTE.execute();
                    psINSERTLOTE.clearParameters();
                    psINSERTLOTE.close();

                    save = true;
                    con.getConn().commit();
                    return save;
                }
            } else {
                save = false;
                return save;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }

//factura manual
    @Override
    public boolean ConfirmarFactTemp(String Usuario, String Observaciones, String Tipo, int Proyecto, String OC) {
        boolean save = false;
        int ExiLot = 0, FolFact = 0, FolioFactura = 0, FolioMovi = 0, FolMov = 0, TipoMed = 0, Proyectossm=0;
        int existencia = 0, cantidad = 0, nivel = 0, nivelNR = 0;
        double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
        String IdFact = "", ClaCli = "", FechaE = "", Contrato = "", Origen = "";
        List<FacturacionModel> Factura = new ArrayList<>();
        System.out.println("listo para insertar");
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            System.out.println("Proyecto: "+Proyecto);

            psBuscaContrato = con.getConn().prepareStatement(BuscaContrato);
            psBuscaContrato.setInt(1, Proyecto);
            rsContrato = psBuscaContrato.executeQuery();
            if (rsContrato.next()) {
                Contrato = rsContrato.getString(1);
            }

            psBuscaTemp = con.getConn().prepareStatement(BUSCA_FACTEMP);
            psBuscaTemp.setString(1, Usuario);
            rsTemp = psBuscaTemp.executeQuery();

            while (rsTemp.next()) {

                IdFact = rsTemp.getString(2);
                ClaCli = rsTemp.getString(3);
                FechaE = rsTemp.getString(4);
                nivel = rsTemp.getInt(5);
                nivelNR = rsTemp.getInt(6);

                psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSFACT);
                psBuscaDatosFact.setString(1, IdFact);
                psBuscaDatosFact.setString(2, ClaCli);
                rs = psBuscaDatosFact.executeQuery();
                while (rs.next()) {
                    FacturacionModel facturacion = new FacturacionModel();
                    facturacion.setClaCli(rs.getString(1));
                    facturacion.setFolLot(rs.getString(2));
                    facturacion.setIdLote(rs.getString(3));
                    facturacion.setClaPro(rs.getString(4));
                    facturacion.setTipMed(rs.getInt(7));
                    facturacion.setCosto(rs.getDouble(8));
                    facturacion.setClaProve(rs.getString(9));
                    facturacion.setCant(rs.getInt(10));
                    facturacion.setExiLot(rs.getInt(11));
                    facturacion.setUbica(rs.getString(12));
                    facturacion.setId(rs.getString(14));
                    facturacion.setCantSol(rs.getString(16));
                    facturacion.setOrigen(rs.getString(21));
                    Factura.add(facturacion);
                }
            }
            psBuscaDatosFact.clearParameters();
            psBuscaDatosFact.close();
            psBuscaTemp.clearParameters();
            psBuscaTemp.close();
            rsTemp.close();

         
            System.out.println("FolFact: " + FolFact);
            if (ClaCli.equals("10981")) {
                System.out.println("cambiando indice al de secretaria");
                Proyectossm = 3;
            } else {
                Proyectossm = Proyecto;
            }

            psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyectossm));
            rsIndice = psBuscaIndice.executeQuery();
            if (rsIndice.next()) {
                FolioFactura = rsIndice.getInt(1);
            }
          

                FolFact = FolioFactura + 1;
            psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyectossm));
            psActualizaIndice.setInt(1, FolFact);
            psActualizaIndice.execute();

            psActualizaIndice.clearParameters();
            psBuscaIndice.clearParameters();
            ApartadoDAOImpl apartadoDAO = new ApartadoDAOImpl(con.getConn());

            for (FacturacionModel f : Factura) {

                TipoMed = f.getTipMed();
                existencia = f.getExiLot();
                cantidad = f.getCant();
                Origen = f.getOrigen();
                if (TipoMed == 2504) {
                    IVA = 0.0;
                } else {
                    IVA = 0.16;
                }

                Costo = f.getCosto();

                if (Proyecto != 5) {
                    Costo = 0.0;
                }

                int Diferencia2 = 0,Diferencia = 0;
                if (Origen.equals("19")) {
                    Diferencia = cantidad;
                } else {
                    Diferencia = existencia - cantidad;
                }
                Diferencia2 = existencia - cantidad;

                if (Diferencia >= 0) {
                    boolean picking = false;
                    // boolean picking = f.getUbica().trim().equals("AF") && Proyecto == 2;
                    if ((f.getUbica().trim().equals("AF") && Proyecto == 2) || (f.getUbica().trim().equals("AF1N") && Proyecto == 2) || (f.getUbica().trim().equals("AFGC") && Proyecto == 2)) {
                        picking = true;
                  
                    Apartado apartado = new Apartado();
                    apartado.setId(0);
                    apartado.setIdLote(Integer.parseInt(f.getIdLote()));
                    apartado.setCant(cantidad);
                    apartado.setStatus(picking ? 1 : 2);
                    apartado.setClaDoc(FolioFactura + "");
                    apartado.setProyecto(Proyecto);
                    apartadoDAO.guardar(apartado);

                    IVAPro = (cantidad * Costo) * IVA;
                    Monto = cantidad * Costo;
                    MontoIva = Monto + IVAPro;
                    }

                    //PICKING ES FALSO
                    if (!picking) {
                        System.out.println("Estoy en Picking: ");
                        psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
                        psActualizaLote.setInt(1, Diferencia2);
                        psActualizaLote.setString(2, f.getIdLote());
                        psActualizaLote.execute();
                        psActualizaLote.clearParameters();
                        psActualizaLote.close();


                        psBuscaIndice = con.getConn().prepareStatement(BUSCA_INDICEMOV);
                        rsIndice = psBuscaIndice.executeQuery();
                        if (rsIndice.next()) {
                            FolioMovi = rsIndice.getInt(1);
                        }

                        FolMov = FolioMovi + 1;

                        psActualizaIndice = con.getConn().prepareStatement(ACTUALIZA_INDICEMOV);
                        psActualizaIndice.setInt(1, FolMov);
                        psActualizaIndice.execute();
                        psActualizaIndice.clearParameters();
                        psBuscaIndice.clearParameters();
                        psBuscaIndice.close();

                        Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + Usuario + ", clave = " + f.getClaPro() + ", folio = " + FolioFactura + ", ubicacion = " + f.getUbica() + ", piezas = " + cantidad + " ");

                        psInsertar = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
                        psInsertar.setInt(1, FolioFactura);
                        psInsertar.setInt(2, 51);
                        psInsertar.setString(3, f.getClaPro());
                        psInsertar.setInt(4, cantidad);
                        psInsertar.setDouble(5, Costo);
                        psInsertar.setDouble(6, MontoIva);
                        psInsertar.setString(7, "-1");
                        psInsertar.setString(8, f.getFolLot());
                        psInsertar.setString(9, f.getUbica());
                        psInsertar.setString(10, f.getClaProve());
                        psInsertar.setString(11, Usuario);
//                        psInsertar.setString(12, " ");
                        psInsertar.execute();
                        psInsertar.clearParameters();
                        psInsertar.close();

                        ///ACTUALIZA GASTOS CATASTROFICOS FACTURACION MANUAL APE RED FRIA
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setString(1, f.getFolLot());
                        psActualizaGastos.setString(2, f.getClaCli());

                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
/*
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - cantidad));
                        psActualizaGastos.setInt(2, (cantidad + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
*/
                    }
                    System.out.println("fuera de picking: ");
/*si picking es verdadero*/
                    psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
                    psInsertarFact.setInt(1, FolioFactura);
                    psInsertarFact.setString(2, f.getClaCli());
                    psInsertarFact.setString(3, f.getClaPro());
                    psInsertarFact.setString(4, f.getCantSol());
                    psInsertarFact.setInt(5, cantidad);
                    psInsertarFact.setDouble(6, Costo);
                    psInsertarFact.setDouble(7, IVAPro);
                    psInsertarFact.setDouble(8, MontoIva);
                    psInsertarFact.setString(9, f.getFolLot());
                    psInsertarFact.setString(10, FechaE);
                    psInsertarFact.setString(11, Usuario);
                    psInsertarFact.setString(12, f.getUbica());
                    psInsertarFact.setInt(13, Proyecto);
                    psInsertarFact.setString(14, Contrato);
                    psInsertarFact.setString(15, OC);
                    psInsertarFact.setInt(16, 0);
                    psInsertarFact.execute();
                    psInsertarFact.clearParameters();
                    psInsertarFact.close();

                    psActualizaTemp = con.getConn().prepareStatement(ACTUALIZA_FACTTEM);
                    psActualizaTemp.setString(1, f.getId());
                    psActualizaTemp.execute();
                    psActualizaTemp.clearParameters();
                    psActualizaTemp.close();

                    if (Origen.equals("19")) {
                    //ACTUALIZA GASTOS CATASTROFICOS FACTURACION MANUAL AF
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setString(1, f.getFolLot());
                        psActualizaGastos.setString(2, f.getClaCli());
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - cantidad));
                        psActualizaGastos.setInt(2, (cantidad + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
                        }
                  

                } else {
                    save = false;
                    con.getConn().rollback();
                    return save;
                }

            }//fin del ciclo factura

            /*INSERTA_OBSFACTURA compra temp*/
            psInsertarObs = con.getConn().prepareStatement(INSERTA_OBSFACTURA);
            psInsertarObs.setInt(1, FolioFactura);
            psInsertarObs.setString(2, Observaciones);
            psInsertarObs.setString(3, Tipo);
            psInsertarObs.setInt(4, Proyecto);
            psInsertarObs.execute();
            psInsertarObs.clearParameters();
            psInsertarObs.close();
            /*fin INSERTA_OBSFACTURA*/

            psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);
            psAbasto = con.getConn().prepareStatement(DatosAbasto);
            psAbasto.setInt(1, Proyecto);
            psAbasto.setInt(2, FolioFactura);
            rsAbasto = psAbasto.executeQuery();
            while (rsAbasto.next()) {
                int factorEmpaque = 1;
                int folLot = rsAbasto.getInt("LOTE");
                PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                psfe.setInt(1, folLot);
                ResultSet rsfe = psfe.executeQuery();
                if (rsfe.next()) {
                    factorEmpaque = rsfe.getInt("factor");
                }
                psAbastoInsert.setString(1, rsAbasto.getString(1));
                psAbastoInsert.setString(2, rsAbasto.getString(2));
                psAbastoInsert.setString(3, rsAbasto.getString(3));
                psAbastoInsert.setString(4, rsAbasto.getString(4));
                psAbastoInsert.setString(5, rsAbasto.getString(5));
                psAbastoInsert.setString(6, rsAbasto.getString(6));
                psAbastoInsert.setString(7, rsAbasto.getString(7));
                psAbastoInsert.setString(8, rsAbasto.getString(8));
                psAbastoInsert.setString(9, rsAbasto.getString(12));
                psAbastoInsert.setString(10, rsAbasto.getString(10));
                psAbastoInsert.setString(11, Usuario);
                psAbastoInsert.setInt(12, factorEmpaque);
                System.out.println(" psAbastoInsert ConfirmarFactTemp: " + psAbastoInsert);
                psAbastoInsert.addBatch();
            }



            psAbastoInsert.executeBatch();
            save = true;
            con.getConn().commit();
            FolioStatusDAOImpl stDao = new FolioStatusDAOImpl(this.con.getConn());
            FolioStatus st = new FolioStatus();
            st.setId(0);
            st.setClaDoc(FolioFactura);
            st.setProyecto(Proyecto);
            st.setStatus(1);

//            stDao.guardar(st);
//            if (Proyecto == 9 || Proyecto == 14 || Proyecto == 15) {
//                ThomasantService ts = new ThomasantService();
//                int result = ts.enviaRemision("" + FolioFactura, Proyecto);
//            }

            return save;
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }

/*facturacion automatica segundo paso actualiza a cero unireq*/
    @Override
    public boolean ActualizaREQ(String ClaUni, String ClaPro, int Cantidad, int Catalogo, int Idreg, String Obs, int CantidadReq, String tablaUnireq) {
        System.out.println("ClaUni=" + ClaUni + " ClaPro=" + ClaPro + " Cantidad=" + Cantidad + " Catalogo=" + Catalogo);
        boolean save = false;
        int ExiLot = 0;
        if (tablaUnireq == null) {
            tablaUnireq = "tb_unireq";
        }
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psBuscaLote = con.getConn().prepareStatement(String.format(ACTUALIZA_REQId, tablaUnireq));
            psBuscaLote.setInt(1, Cantidad);
            psBuscaLote.setInt(2, CantidadReq);
            psBuscaLote.setString(3, Obs);
            psBuscaLote.setString(4, ClaPro);
            psBuscaLote.setString(5, ClaUni);
            psBuscaLote.setInt(6, Idreg);
            System.out.println("ActualizaReq=" + psBuscaLote);
            psBuscaLote.executeUpdate();
            psBuscaLote.clearParameters();
            psBuscaLote.close();
            psBuscaLote = null;
            save = true;
            con.getConn().commit();
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }
/*Factura automatica paso 1*/
    @Override
    public JSONArray getRegistroFactAuto(String ClaUni, String Catalogo, String tablaUnireq) {
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObj;
        JSONObject jsonObj2;
        System.out.println("Unidades:" + ClaUni);
        String query = "SELECT U.F_ClaPro, F_ClaUni,CONCAT(F_ClaUni,'_',REPLACE(U.F_ClaPro,'.','')) AS DATOS,F_IdReq FROM " + tablaUnireq + " U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni in (%s) AND F_Status=0 AND  F_Solicitado != 0 AND M.F_StsPro='A' AND F_N%s='1';";
        PreparedStatement ps;
        ResultSet rs;
        try {
            int Contar = 0;
            con.conectar();
            ps = con.getConn().prepareStatement(String.format(query, ClaUni, Catalogo));
            System.out.println("UNidades=" + ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("ClaPro", rs.getString(1));
                jsonObj.put("ClaUni", rs.getString(2));
                jsonObj.put("Datos", rs.getString(3));
                jsonObj.put("IdReg", rs.getString(4));
                jsonArray.add(jsonObj);

            }
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            jsonObj2 = new JSONObject();
            jsonArray.add(jsonObj2);
            con.cierraConexion();

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

///Facturacion otros proyectos
    @Override
    public boolean RegistrarFolios(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC) {
        boolean save = false;
        int UbicaModu = 0, piezas = 0, F_Solicitado = 0, ContarV = 0, Existencia = 0, FolioFactura = 0, DifeSol = 0, FolioLote = 0;
        String UbicaDesc = "", Clave = "", Unidad = "", Unidad2 = "", UbicaLote = "", Contrato = "", Ubicaciones = "", UbicaNofacturar = "", UbicacionesTemp = "";

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psBuscaContrato = con.getConn().prepareStatement(BuscaContrato);
            psBuscaContrato.setInt(1, Proyecto);
            rsContrato = psBuscaContrato.executeQuery();
            if (rsContrato.next()) {
                Contrato = rsContrato.getString(1);
            }

            psUbicaCrossdock = con.getConn().prepareStatement(BuscaUbicacionesCross);
            rsUbicaCross = psUbicaCrossdock.executeQuery();
            if (rsUbicaCross.next()) {
                Ubicaciones = rsUbicaCross.getString(1);
            }

            psUbicaTemporal = con.getConn().prepareStatement(BuscaUbicaTemporal);
            rsUbicaTemp = psUbicaTemporal.executeQuery();
            if (rsUbicaTemp.next()) {
                UbicacionesTemp = "," + rsUbicaTemp.getString(1);
            }

            UbicaDesc = UbicaDesc + UbicacionesTemp;


            if (Catalogo > 0) {
                psConsulta = con.getConn().prepareStatement(BUSCA_PARAMETRO);
                psConsulta.setString(1, Usuario);
                rsConsulta = psConsulta.executeQuery();
                rsConsulta.next();
                UbicaModu = rsConsulta.getInt(1);
                Proyecto = rsConsulta.getInt(2);
                UbicaDesc = rsConsulta.getString(3);
                psConsulta.close();
                psConsulta = null;
//                switch (UbicaModu) {
//                    case 1:
//                        UbicaDesc = " WHERE F_Ubica IN ('MODULA','A0S','APE','DENTAL','REDFRIA')";
//                        break;
//                    case 2:
//                        UbicaDesc = " WHERE F_Ubica IN ('MODULA2','A0S','APE','DENTAL','REDFRIA')";
//                        break;
//                    case 3:
//                        UbicaDesc = " WHERE F_Ubica IN ('AF','APE','DENTAL','REDFRIA','CAMARAFRIA01','CAMARAFRIA02')";
//                        break;
//                    default:
//                        UbicaDesc = " WHERE F_Ubica NOT IN ('A0S','MODULA','MODULA2','CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA','CROSSDOCKMORELIA','INGRESOS_V','CUARENTENA'," + Ubicaciones + ")";
//                        break;
//                }
            }/* else if (Catalogo == 30) {
                psSolucion = con.getConn().prepareStatement(BUSCA_UBISOLUCION);
                rsSolucion = psSolucion.executeQuery();
                rsSolucion.next();
                UbicaDesc = " WHERE F_Ubica IN (" + rsSolucion.getString(1) + ")";
                psSolucion.close();
                psSolucion = null;
                rsSolucion.close();
                rsSolucion = null;
            } else {
                UbicaDesc = " WHERE F_Ubica NOT IN ('MODULA','MODULA2','CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA')";
            }*/

            psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));
            psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
            psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
            psActualizaIndiceLote = con.getConn().prepareStatement(ACTUALIZA_INDICELOTE);
            psINSERTLOTE = con.getConn().prepareStatement(INSERTAR_NUEVOLOTE);
            psActualizaReq = con.getConn().prepareStatement(ACTUALIZA_STSREQ);
            psInsertarObs = con.getConn().prepareStatement(INSERTAR_OBSERVACIONES);
            psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

            psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADES, "tb_unireq", ClaUnidad, Catalogo));
            rsBuscaUnidad = psBuscaUnidad.executeQuery();
            while (rsBuscaUnidad.next()) {
                Unidad = rsBuscaUnidad.getString(1);
                Unidad2 = rsBuscaUnidad.getString(1);
                Unidad = "'" + Unidad + "'";
                psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
                rsIndice = psBuscaIndice.executeQuery();
                if (rsIndice.next()) {
                    FolioFactura = rsIndice.getInt(1);
                }

                /*psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSFACTURA);
                psBuscaDatosFact.setString(1, Unidad);
                psBuscaDatosFact.setInt(2, Catalogo);*/
                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURAR, "tb_unireq", UbicaDesc, Unidad, Catalogo));
                psBuscaDatosFact.setInt(1, Proyecto);
//                psBuscaDatosFact.setInt(2, Proyecto);
                System.out.println("Datos Facturas" + psBuscaDatosFact);
                rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                while (rsBuscaDatosFact.next()) {
                    Clave = rsBuscaDatosFact.getString(2);
                    piezas = rsBuscaDatosFact.getInt(3);
                    F_Solicitado = rsBuscaDatosFact.getInt(5);
                    Existencia = rsBuscaDatosFact.getInt(8);
                    FolioLote = rsBuscaDatosFact.getInt(9);
                    UbicaLote = rsBuscaDatosFact.getString(10);
                    Observaciones = rsBuscaDatosFact.getString(11);

                    if ((piezas > 0) && (Existencia > 0)) {

                        int F_IdLote = 0, F_FolLot = 0, Tipo = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0;
                        int Facturado = 0, Contar = 0;
                        String Ubicacion = "";
                        double Costo = 0.0, IVA = 0.0, IVAPro = 0.0, Monto = 0.0, MontoIva = 0.0;

                        System.out.println("Busca Existenci" + BUSCA_EXITFOLUBI);
                        psContarReg = con.getConn().prepareStatement(String.format(BUSCA_EXITFOLUBI, UbicaDesc, UbicaDesc, Catalogo));
                        psContarReg.setString(1, Clave);
                        psContarReg.setInt(2, Proyecto);
                        psContarReg.setInt(3, Proyecto);
                        psContarReg.setString(4, Clave);
                        rsContarReg = psContarReg.executeQuery();
                        while (rsContarReg.next()) {
                            Contar++;
                        }

                        psBuscaExiFol = con.getConn().prepareStatement(String.format(BUSCA_EXITFOLUBI, UbicaDesc, UbicaDesc, Catalogo));
                        psBuscaExiFol.setString(1, Clave);
                        psBuscaExiFol.setInt(2, Proyecto);
                        psBuscaExiFol.setInt(3, Proyecto);
                        psBuscaExiFol.setString(4, Clave);
                        //psBuscaExiFol = con.getConn().prepareStatement(String.format(BUSCA_EXITFOL, UbicaDesc, Catalogo, Clave));

                        System.out.println("BuscaExistenciaDetalle=" + psBuscaExiFol);
                        rsBuscaExiFol = psBuscaExiFol.executeQuery();
                        while (rsBuscaExiFol.next()) {
                            F_IdLote = rsBuscaExiFol.getInt(1);
                            F_ExiLot = rsBuscaExiFol.getInt(7);
                            F_FolLot = rsBuscaExiFol.getInt(3);
                            Tipo = rsBuscaExiFol.getInt(4);
                            Costo = rsBuscaExiFol.getDouble(5);
                            Ubicacion = rsBuscaExiFol.getString(6);
                            ClaProve = rsBuscaExiFol.getInt(8);
                            if (Tipo == 2504) {
                                IVA = 0.0;
                            } else {
                                IVA = 0.16;
                            }

                            if (Proyecto != 5) {
                                Costo = 0.0;
                            }

                            if ((F_ExiLot >= piezas) && (piezas > 0)) {
                                Contar = Contar - 1;
                                diferencia = F_ExiLot - piezas;
                                CanSur = piezas;

                                psActualizaLote.setInt(1, diferencia);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                IVAPro = (CanSur * Costo) * IVA;
                                Monto = CanSur * Costo;
                                MontoIva = Monto + IVAPro;

                                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + Usuario + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + CanSur + " ");
                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setDouble(5, Costo);
                                psInsertarMov.setDouble(6, MontoIva);
                                psInsertarMov.setString(7, "-1");
                                psInsertarMov.setInt(8, F_FolLot);
                                psInsertarMov.setString(9, Ubicacion);
                                psInsertarMov.setInt(10, ClaProve);
                                psInsertarMov.setString(11, Usuario);
                                System.out.println("Mov1" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setInt(1, FolioFactura);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setInt(4, F_Solicitado);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setDouble(6, Costo);
                                psInsertarFact.setDouble(7, IVAPro);
                                psInsertarFact.setDouble(8, MontoIva);
                                psInsertarFact.setInt(9, F_FolLot);
                                psInsertarFact.setString(10, FecEnt);
                                psInsertarFact.setString(11, Usuario);
                                psInsertarFact.setString(12, Ubicacion);
                                psInsertarFact.setInt(13, Proyecto);
                                psInsertarFact.setString(14, Contrato);
                                psInsertarFact.setString(15, OC);
                                psInsertarFact.setInt(16, 0);
                                System.out.println("fact1" + psInsertarFact);
                                psInsertarFact.addBatch();

                                piezas = 0;
                                F_Solicitado = 0;
                                break;

                            } else if ((piezas > 0) && (F_ExiLot > 0)) {
                                Contar = Contar - 1;
                                diferencia = piezas - F_ExiLot;
                                CanSur = F_ExiLot;
                                if (F_ExiLot >= F_Solicitado) {
                                    DifeSol = F_Solicitado;
                                } else if (Contar > 0) {
                                    DifeSol = F_ExiLot;
                                } else {
                                    DifeSol = F_Solicitado - F_ExiLot;
                                }

                                psActualizaLote.setInt(1, 0);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote2=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                IVAPro = (CanSur * Costo) * IVA;
                                Monto = CanSur * Costo;
                                MontoIva = Monto + IVAPro;

                                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + Usuario + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + CanSur + " ");

                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setDouble(5, Costo);
                                psInsertarMov.setDouble(6, MontoIva);
                                psInsertarMov.setString(7, "-1");
                                psInsertarMov.setInt(8, F_FolLot);
                                psInsertarMov.setString(9, Ubicacion);
                                psInsertarMov.setInt(10, ClaProve);
                                psInsertarMov.setString(11, Usuario);
                                System.out.println("Mov2" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setInt(1, FolioFactura);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setInt(4, DifeSol);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setDouble(6, Costo);
                                psInsertarFact.setDouble(7, IVAPro);
                                psInsertarFact.setDouble(8, MontoIva);
                                psInsertarFact.setInt(9, F_FolLot);
                                psInsertarFact.setString(10, FecEnt);
                                psInsertarFact.setString(11, Usuario);
                                psInsertarFact.setString(12, Ubicacion);
                                psInsertarFact.setInt(13, Proyecto);
                                psInsertarFact.setString(14, Contrato);
                                psInsertarFact.setString(15, OC);
                                psInsertarFact.setInt(16, 0);
                                System.out.println("fact2" + psInsertarFact);
                                psInsertarFact.addBatch();

                                F_Solicitado = F_Solicitado - CanSur;

                                piezas = piezas - CanSur;
                                F_ExiLot = 0;

                            }
                            if (Contar == 0) {
                                if (F_Solicitado > 0) {
                                    psInsertarFact.setInt(1, FolioFactura);
                                    psInsertarFact.setString(2, Unidad2);
                                    psInsertarFact.setString(3, Clave);
                                    psInsertarFact.setInt(4, F_Solicitado);
                                    psInsertarFact.setInt(5, 0);
                                    psInsertarFact.setDouble(6, Costo);
                                    psInsertarFact.setDouble(7, IVAPro);
                                    psInsertarFact.setDouble(8, MontoIva);
                                    psInsertarFact.setInt(9, F_FolLot);
                                    psInsertarFact.setString(10, FecEnt);
                                    psInsertarFact.setString(11, Usuario);
                                    psInsertarFact.setString(12, Ubicacion);
                                    psInsertarFact.setInt(13, Proyecto);
                                    psInsertarFact.setString(14, Contrato);
                                    psInsertarFact.setString(15, OC);
                                    psInsertarFact.setInt(16, 0);
                                    System.out.println("fact3" + psInsertarFact);
                                    psInsertarFact.addBatch();
                                    F_Solicitado = 0;
                                }
                            }

                        }

                    } else if ((FolioLote > 0) && (UbicaLote != "")) {
                        psInsertarFact.setInt(1, FolioFactura);
                        psInsertarFact.setString(2, Unidad2);
                        psInsertarFact.setString(3, Clave);
                        psInsertarFact.setInt(4, F_Solicitado);
                        psInsertarFact.setInt(5, 0);
                        psInsertarFact.setDouble(6, 0);
                        psInsertarFact.setDouble(7, 0);
                        psInsertarFact.setDouble(8, 0);
                        psInsertarFact.setInt(9, FolioLote);
                        psInsertarFact.setString(10, FecEnt);
                        psInsertarFact.setString(11, Usuario);
                        psInsertarFact.setString(12, UbicaLote);
                        psInsertarFact.setInt(13, Proyecto);
                        psInsertarFact.setString(14, Contrato);
                        psInsertarFact.setString(15, OC);
                        psInsertarFact.setInt(16, 0);
                        System.out.println("fact4" + psInsertarFact);
                        psInsertarFact.addBatch();
                    } else {
                        int FolioL = 0, IndiceLote = 0;
                        String Ubicacion = "";
                        double Costo = 0.0;

                        psBuscaIndiceLote = con.getConn().prepareStatement(BUSCA_INDICELOTE);
                        rsIndiceLote = psBuscaIndiceLote.executeQuery();
                        rsIndiceLote.next();
                        FolioL = rsIndiceLote.getInt(1);

                        IndiceLote = FolioL + 1;

                        psActualizaIndiceLote.setInt(1, IndiceLote);
                        psActualizaIndiceLote.addBatch();

                        psINSERTLOTE.setString(1, Clave);
                        psINSERTLOTE.setInt(2, FolioL);
                        psINSERTLOTE.setInt(3, Proyecto);
                        System.out.println("InsertarLote" + psINSERTLOTE);
                        psINSERTLOTE.addBatch();

                        psInsertarFact.setInt(1, FolioFactura);
                        psInsertarFact.setString(2, Unidad2);
                        psInsertarFact.setString(3, Clave);
                        psInsertarFact.setInt(4, F_Solicitado);
                        psInsertarFact.setInt(5, 0);
                        psInsertarFact.setDouble(6, Costo);
                        psInsertarFact.setDouble(7, 0);
                        psInsertarFact.setDouble(8, 0);
                        psInsertarFact.setInt(9, FolioL);
                        psInsertarFact.setString(10, FecEnt);
                        psInsertarFact.setString(11, Usuario);
                        psInsertarFact.setString(12, "NUEVA");
                        psInsertarFact.setInt(13, Proyecto);
                        psInsertarFact.setString(14, Contrato);
                        psInsertarFact.setString(15, OC);
                        psInsertarFact.setInt(16, 0);
                        System.out.println("fact5" + psInsertarFact);
                        psInsertarFact.addBatch();
                    }

                }

                psActualizaIndice.setInt(1, FolioFactura + 1);
                psActualizaIndice.addBatch();

                psActualizaReq.setString(1, Unidad2);
                psActualizaReq.addBatch();;
                /*psInsertarObs*/
                psInsertarObs.setInt(1, FolioFactura);
                psInsertarObs.setString(2, Observaciones);
                psInsertarObs.setString(3, Tipos);
                psInsertarObs.setInt(4, Proyecto);
                psInsertarObs.addBatch();
                /**/
                psActualizaIndice.executeBatch();
                psActualizaLote.executeBatch();
                psINSERTLOTE.executeBatch();
                psInsertarMov.executeBatch();
                psInsertarFact.executeBatch();
                psActualizaIndiceLote.executeBatch();
                psActualizaReq.executeBatch();
                /**/
                psInsertarObs.executeBatch();
                /**/

                psAbasto = con.getConn().prepareStatement(DatosAbasto);
                psAbasto.setInt(1, Proyecto);
                psAbasto.setInt(2, FolioFactura);
                rsAbasto = psAbasto.executeQuery();
                while (rsAbasto.next()) {
                    int factorEmpaque = 1;
                    int folLot = rsAbasto.getInt("LOTE");
                    PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                    psfe.setInt(1, folLot);
                    ResultSet rsfe = psfe.executeQuery();
                    if (rsfe.next()) {
                        factorEmpaque = rsfe.getInt("factor");
                    }

                    psAbastoInsert.setString(1, rsAbasto.getString(1));
                    psAbastoInsert.setString(2, rsAbasto.getString(2));
                    psAbastoInsert.setString(3, rsAbasto.getString(3));
                    psAbastoInsert.setString(4, rsAbasto.getString(4));
                    psAbastoInsert.setString(5, rsAbasto.getString(5));
                    psAbastoInsert.setString(6, rsAbasto.getString(6));
                    psAbastoInsert.setString(7, rsAbasto.getString(7));
                    psAbastoInsert.setString(8, rsAbasto.getString(8));
                    psAbastoInsert.setString(9, rsAbasto.getString(12));
                    psAbastoInsert.setString(10, rsAbasto.getString(10));
                    psAbastoInsert.setString(11, Usuario);
                    psAbastoInsert.setInt(12, factorEmpaque);
                    System.out.println("psAbastoInsert 2" + psAbastoInsert);
                    psAbastoInsert.addBatch();
                }

                psAbastoInsert.executeBatch();
                save = true;
                con.getConn().commit();
                System.out.println("Terminó Unidad= " + Unidad + " Con el Folio= " + FolioFactura);

            }
//            psBuscaExiFol.close();
//            psBuscaExiFol = null;
//            rsBuscaExiFol.close();
//            rsBuscaExiFol = null;
//            psBuscaDatosFact.close();
//            psBuscaDatosFact = null;
//            rsBuscaDatosFact.close();
//            rsBuscaDatosFact = null;
//            psUbicaCrossdock.close();
//            psUbicaCrossdock = null;
//            rsUbicaCross.close();
//            rsUbicaCross = null;
//            rsUbicaNoFacturar.close();
//            rsUbicaNoFacturar = null;

//            if (Proyecto == 9 || Proyecto == 14 || Proyecto == 15) {
//                ThomasantService ts = new ThomasantService();
//                int result = ts.enviaRemision("" + FolioFactura, Proyecto);
//            }

            return save;
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    ///modificar folio agregar clave
    @Override
    public boolean ConfirmarFactTempFOLIO(String usuario, int Proyecto) {
        boolean save = false;
        int ExiLot = 0, FolFact = 0, FolioFactura = 0, FolioMovi = 0, FolMov = 0, TipoMed = 0;
        int existencia = 0, cantidad = 0;
        double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
        String IdFact = "", ClaCli = "", FechaE = "", Contrato = "", OC = "";
        List<FacturacionModel> factura = new ArrayList<>();
        List<DetalleFactura> folio = new ArrayList<>();
        List<DetalleFactura> prefolio = new ArrayList<>();
        System.out.println("Aui estoy ante de todo");
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
            psBuscaTemp = con.getConn().prepareStatement(BUSCA_FACTEMP);
            psBuscaTemp.setString(1, usuario);
            System.out.println(psBuscaTemp);
            rsTemp = psBuscaTemp.executeQuery();

            while (rsTemp.next()) {

                psBuscaContrato = con.getConn().prepareStatement(DatosFactura);
                psBuscaContrato.setString(1, rsTemp.getString(2));
                rsContrato = psBuscaContrato.executeQuery();
                System.out.println(psBuscaContrato);
                if (rsContrato.next()) {
                    Contrato = rsContrato.getString(1);
                    OC = rsContrato.getString(2);
                }

                IdFact = rsTemp.getString(2);
                ClaCli = rsTemp.getString(3);
                FechaE = rsTemp.getString(4);

                psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSFACT);
                psBuscaDatosFact.setString(1, IdFact);
                psBuscaDatosFact.setString(2, ClaCli);
                rs = psBuscaDatosFact.executeQuery();
                System.out.println(psBuscaDatosFact);
                while (rs.next()) {
                    FacturacionModel facturacion = new FacturacionModel();
                    facturacion.setClaCli(rs.getString(1));
                    facturacion.setFolLot(rs.getString(2));
                    facturacion.setIdLote(rs.getString(3));
                    facturacion.setClaPro(rs.getString(4));
                    facturacion.setTipMed(rs.getInt(7));
                    facturacion.setCosto(rs.getDouble(8));
                    facturacion.setClaProve(rs.getString(9));
                    facturacion.setCant(rs.getInt(10));
                    facturacion.setExiLot(rs.getInt(11));
                    facturacion.setUbica(rs.getString(12));
                    facturacion.setId(rs.getString(14));
                    facturacion.setCantSol(rs.getString(16));
                    factura.add(facturacion);
                }
                psBuscaDatosFact.clearParameters();
                psBuscaDatosFact.close();
            }
            psBuscaTemp.clearParameters();
            psBuscaTemp.close();
            rsTemp.close();

            FolioFactura = Integer.parseInt(IdFact);
            boolean disponible = false;
            for (FacturacionModel f : factura) {

                TipoMed = f.getTipMed();
                existencia = f.getExiLot();
                cantidad = f.getCant();

                if (TipoMed == 2504) {
                    IVA = 0.0;
                } else {
                    IVA = 0.16;
                }

                Costo = f.getCosto();

                if (Proyecto != 5) {
                    Costo = 0.0;
                }

                IVAPro = (cantidad * Costo) * IVA;
                Monto = cantidad * Costo;
                MontoIva = Monto + IVAPro;

                psActualizaTemp = con.getConn().prepareStatement(ACTUALIZA_FACTTEM);
                psActualizaTemp.setString(1, f.getId());
                psActualizaTemp.execute();
                psActualizaTemp.clearParameters();
                psActualizaTemp.close();

                DetalleFactura detalle = new DetalleFactura();
                detalle.setClave(f.getClaPro());
                detalle.setContratoSelect(Contrato);
                detalle.setCosto(f.getCosto());
                detalle.setExistencia(f.getExiLot());
                detalle.setFecEnt(FechaE);
                detalle.setFolioLote(Integer.parseInt(f.getFolLot()));
                detalle.setIva(IVA);
                detalle.setMonto(Monto);
                detalle.setObservaciones("");
                detalle.setOc(OC);
                detalle.setPiezas(f.getCant());
                detalle.setProyectoSelect(Proyecto);
                detalle.setSolicitado(Integer.parseInt(f.getCantSol()));
                detalle.setUbicaLote(f.getUbica());
                detalle.setUsuario(usuario);

                if (!this.sobreescribeFactura(detalle, FolioFactura + "")) {
                    System.out.println("si entre a sobrescribir");
                    if ((detalle.getUbicaLote().compareTo("AF") == 0 && Proyecto == 2) || (detalle.getUbicaLote().compareTo("AF1N") == 0 && Proyecto == 2) || (detalle.getUbicaLote().compareTo("AFGC") == 0 && Proyecto == 2)) {
                        prefolio.add(detalle);
                        System.out.println("si prefolio");
                    } else {
                        folio.add(detalle);
                        System.out.println("si folio");
                    }
                }
                if ((detalle.getUbicaLote().compareTo("AF") == 0 && Proyecto == 2) || (detalle.getUbicaLote().compareTo("AF1N") == 0 && Proyecto == 2) || (detalle.getUbicaLote().compareTo("AFGC") == 0 && Proyecto == 2)) {

                    disponible = this.folioAsignadoPicking(FolioFactura, Proyecto);
                } else {
                    disponible = true;
                }
            }

          

 
            if (disponible) {
                System.out.println("Creando Folio con ");

                this.crearFolio(folio, "", "", usuario, FolioFactura, ClaCli, OC, "");
                System.out.println("Creando Prefolio");
                this.crearPrefolio(prefolio, "", "", usuario, FolioFactura, ClaCli, OC, "");

                int Cuenta = 0;

                psUbicaCrossdock = con.getConn().prepareStatement(ValidaAbasto);
                psUbicaCrossdock.setInt(1, Proyecto);
                psUbicaCrossdock.setInt(2, FolioFactura);
                rsUbicaCross = psUbicaCrossdock.executeQuery();
                if (rsUbicaCross.next()) {
                    Cuenta = rsUbicaCross.getInt(1);
                }

                psUbicaCrossdock.clearParameters();

                if (Cuenta == 0) {
                    psUbicaCrossdock = con.getConn().prepareStatement(queryElimina);
                    psUbicaCrossdock.setInt(1, Proyecto);
                    psUbicaCrossdock.setInt(2, FolioFactura);
                    psUbicaCrossdock.execute();

                    psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

                    psAbasto = con.getConn().prepareStatement(DatosAbasto);
                    psAbasto.setInt(1, Proyecto);
                    psAbasto.setInt(2, FolioFactura);
                    rsAbasto = psAbasto.executeQuery();
                    while (rsAbasto.next()) {
                        int factorEmpaque = 1;
                        int folLot = rsAbasto.getInt("LOTE");
                        PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                        psfe.setInt(1, folLot);
                        ResultSet rsfe = psfe.executeQuery();
                        if (rsfe.next()) {
                            factorEmpaque = rsfe.getInt("factor");
                        }

                        psAbastoInsert.setString(1, rsAbasto.getString(1));
                        psAbastoInsert.setString(2, rsAbasto.getString(2));
                        psAbastoInsert.setString(3, rsAbasto.getString(3));
                        psAbastoInsert.setString(4, rsAbasto.getString(4));
                        psAbastoInsert.setString(5, rsAbasto.getString(5));
                        psAbastoInsert.setString(6, rsAbasto.getString(6));
                        psAbastoInsert.setString(7, rsAbasto.getString(7));
                        psAbastoInsert.setString(8, rsAbasto.getString(8));
                        psAbastoInsert.setString(9, rsAbasto.getString(12));
                        psAbastoInsert.setString(10, rsAbasto.getString(10));
                        psAbastoInsert.setString(11, usuario);
                        psAbastoInsert.setInt(12, factorEmpaque);
                        System.out.println("psAbastoInsert ConfirmarFactTempFOLIO:" + psAbastoInsert);
                        psAbastoInsert.addBatch();
                    }

                    psAbastoInsert.executeBatch();

                }
//                else{
//                   String mensaje = "Folio ya fue cargado";
//                  
//                }
//        

                save = true;
                con.getConn().commit();
//                if (Proyecto == 9 || Proyecto == 14 || Proyecto == 15) {
//                    ThomasantService ts = new ThomasantService();
//                    int result = ts.enviaRemision("" + FolioFactura, Proyecto);
//                }
                return save;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s", ex.getMessage()), ex);
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }

    @Override
    public boolean ConfirmarTranferenciaProyecto(String Usuario, String Observaciones, int Proyecto, int ProyectoFinal) {
        boolean save = false;
        int ExiLot = 0, FolFact = 0, FolioFactura = 0, FolioMovi = 0, FolMov = 0, TipoMed = 0, FolioLoteT = 0, FolioLoteT2 = 0, Existencia = 0, ExisTotal = 0;
        int existencia = 0, cantidad = 0, IndLote = 0;
        double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
        String IdFact = "", ClaCli = "", FechaE = "";
        List<FacturacionModel> Factura = new ArrayList<>();

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psBuscaTemp = con.getConn().prepareStatement(BUSCA_FACTEMP);
            psBuscaTemp.setString(1, Usuario);
            rsTemp = psBuscaTemp.executeQuery();

            while (rsTemp.next()) {

                IdFact = rsTemp.getString(2);
                ClaCli = rsTemp.getString(3);
                FechaE = rsTemp.getString(4);

                psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSFACT);
                psBuscaDatosFact.setString(1, IdFact);
                psBuscaDatosFact.setString(2, ClaCli);
                rs = psBuscaDatosFact.executeQuery();
                while (rs.next()) {
                    FacturacionModel facturacion = new FacturacionModel();
                    facturacion.setClaCli(rs.getString(1));
                    facturacion.setFolLot(rs.getString(2));
                    facturacion.setIdLote(rs.getString(3));
                    facturacion.setClaPro(rs.getString(4));
                    facturacion.setLote(rs.getString(5));
                    facturacion.setCaducidad(rs.getString(6));
                    facturacion.setTipMed(rs.getInt(7));
                    facturacion.setCosto(rs.getDouble(8));
                    facturacion.setClaProve(rs.getString(9));
                    facturacion.setCant(rs.getInt(10));
                    facturacion.setExiLot(rs.getInt(11));
                    facturacion.setUbica(rs.getString(12));
                    facturacion.setId(rs.getString(14));
                    facturacion.setCantSol(rs.getString(16));
                    facturacion.setClaOrg(rs.getString(17));
                    facturacion.setFecFab(rs.getString(18));
                    facturacion.setCb(rs.getString(19));
                    facturacion.setClaMar(rs.getString(20));
                    facturacion.setOrigen(rs.getString(21));
                    facturacion.setUniMed(rs.getString(22));
                    Factura.add(facturacion);
                }
            }
//            psBuscaDatosFact.clearParameters();
//            psBuscaDatosFact.close();
//            psBuscaTemp.clearParameters();
//            psBuscaTemp.close();
//            rsTemp.close();

            psBuscaIndice = con.getConn().prepareStatement(BUSCA_INDICETRANSPRODUCTO);
            rsIndice = psBuscaIndice.executeQuery();
            if (rsIndice.next()) {
                FolioFactura = rsIndice.getInt(1);
            }
            FolFact = FolioFactura + 1;

            psActualizaIndice = con.getConn().prepareStatement(ACTUALIZA_INDICETRANSPRODUCTO);
            psActualizaIndice.setInt(1, FolFact);
            psActualizaIndice.execute();

            psActualizaIndice.clearParameters();
            psBuscaIndice.clearParameters();

            for (FacturacionModel f : Factura) {

                TipoMed = f.getTipMed();
                existencia = f.getExiLot();
                cantidad = f.getCant();

                if (TipoMed == 2504) {
                    IVA = 0.0;
                } else {
                    IVA = 0.16;
                }

                Costo = f.getCosto();

                if (Proyecto != 5) {
                    Costo = 0.0;
                }

                int Diferencia = existencia - cantidad;

                if (Diferencia >= 0) {
                    if (Diferencia == 0) {
                        psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
                        psActualizaLote.setInt(1, 0);
                        psActualizaLote.setString(2, f.getIdLote());
                        psActualizaLote.execute();
                        psActualizaLote.clearParameters();
                        psActualizaLote.close();
                    } else {
                        psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
                        psActualizaLote.setInt(1, Diferencia);
                        psActualizaLote.setString(2, f.getIdLote());
                        psActualizaLote.execute();
                        psActualizaLote.clearParameters();
                        psActualizaLote.close();
                    }

                    IVAPro = (cantidad * Costo) * IVA;
                    Monto = cantidad * Costo;
                    MontoIva = Monto + IVAPro;

                    psBuscaIndice = con.getConn().prepareStatement(BUSCA_INDICEMOV);
                    rsIndice = psBuscaIndice.executeQuery();
                    if (rsIndice.next()) {
                        FolioMovi = rsIndice.getInt(1);
                    }

                    FolMov = FolioMovi + 1;

                    psActualizaIndice = con.getConn().prepareStatement(ACTUALIZA_INDICEMOV);
                    psActualizaIndice.setInt(1, FolMov);
                    psActualizaIndice.execute();
                    psActualizaIndice.clearParameters();
                    psBuscaIndice.clearParameters();
                    psBuscaIndice.close();

                    psInsertar = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
                    psInsertar.setInt(1, FolioFactura);
                    psInsertar.setInt(2, 60);
                    psInsertar.setString(3, f.getClaPro());
                    psInsertar.setInt(4, cantidad);
                    psInsertar.setDouble(5, Costo);
                    psInsertar.setDouble(6, MontoIva);
                    psInsertar.setString(7, "-1");
                    psInsertar.setString(8, f.getFolLot());
                    psInsertar.setString(9, f.getUbica());
                    psInsertar.setString(10, f.getClaProve());
                    psInsertar.setString(11, Usuario);
                    psInsertar.execute();
                    psInsertar.clearParameters();
                    psInsertar.close();

                    psBuscaFolioLote = con.getConn().prepareStatement(BuscaFolioLote);
                    psBuscaFolioLote.setString(1, f.getClaPro());
                    psBuscaFolioLote.setString(2, f.getLote());
                    psBuscaFolioLote.setString(3, f.getCaducidad());
                    psBuscaFolioLote.setString(4, f.getOrigen());
                    psBuscaFolioLote.setInt(5, ProyectoFinal);
                    rsBuscaFolioLot = psBuscaFolioLote.executeQuery();
                    if (rsBuscaFolioLot.next()) {
                        FolioLoteT = rsBuscaFolioLot.getInt(1);
                    }

                    psBuscaFolioLote.clearParameters();

                    if (FolioLoteT != 0) {
                        psBuscaFolioLote = con.getConn().prepareStatement(BuscaFolioLoteExist);
                        psBuscaFolioLote.setString(1, f.getClaPro());
                        psBuscaFolioLote.setString(2, f.getLote());
                        psBuscaFolioLote.setString(3, f.getCaducidad());
                        psBuscaFolioLote.setString(4, f.getOrigen());
                        psBuscaFolioLote.setInt(5, ProyectoFinal);
                        psBuscaFolioLote.setInt(6, FolioLoteT);
                        psBuscaFolioLote.setString(7, f.getUbica());
                        rsBuscaFolioLot = psBuscaFolioLote.executeQuery();
                        if (rsBuscaFolioLot.next()) {
                            FolioLoteT2 = rsBuscaFolioLot.getInt(1);
                            Existencia = rsBuscaFolioLot.getInt(2);
                        }
                        if (FolioLoteT2 != 0) {
                            psBuscaFolioLote.clearParameters();
                            ExisTotal = Existencia + cantidad;

                            psBuscaFolioLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTEPRODUCTO);
                            psBuscaFolioLote.setInt(1, ExisTotal);
                            psBuscaFolioLote.setString(2, f.getClaPro());
                            psBuscaFolioLote.setString(3, f.getLote());
                            psBuscaFolioLote.setString(4, f.getCaducidad());
                            psBuscaFolioLote.setString(5, f.getOrigen());
                            psBuscaFolioLote.setInt(6, ProyectoFinal);
                            psBuscaFolioLote.setInt(7, FolioLoteT);
                            psBuscaFolioLote.setString(8, f.getUbica());
                            psBuscaFolioLote.execute();
                        } else {
                            psBuscaFolioLote.clearParameters();
                            psBuscaFolioLote = con.getConn().prepareStatement(INSERTARLOTE);
                            psBuscaFolioLote.setInt(1, 0);
                            psBuscaFolioLote.setString(2, f.getClaPro());
                            psBuscaFolioLote.setString(3, f.getLote());
                            psBuscaFolioLote.setString(4, f.getCaducidad());
                            psBuscaFolioLote.setInt(5, cantidad);
                            psBuscaFolioLote.setInt(6, FolioLoteT);
                            psBuscaFolioLote.setString(7, f.getClaOrg());
                            psBuscaFolioLote.setString(8, f.getUbica());
                            psBuscaFolioLote.setString(9, f.getFecFab());
                            psBuscaFolioLote.setString(10, f.getCb());
                            psBuscaFolioLote.setString(11, f.getClaMar());
                            psBuscaFolioLote.setString(12, f.getOrigen());
                            psBuscaFolioLote.setString(13, f.getClaOrg());
                            psBuscaFolioLote.setInt(14, 131);
                            psBuscaFolioLote.setInt(15, ProyectoFinal);
                            psBuscaFolioLote.execute();
                        }

                        psInsertar = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
                        psInsertar.setInt(1, FolioFactura);
                        psInsertar.setInt(2, 9);
                        psInsertar.setString(3, f.getClaPro());
                        psInsertar.setInt(4, cantidad);
                        psInsertar.setDouble(5, Costo);
                        psInsertar.setDouble(6, MontoIva);
                        psInsertar.setString(7, "1");
                        psInsertar.setInt(8, FolioLoteT);
                        psInsertar.setString(9, f.getUbica());
                        psInsertar.setString(10, f.getClaProve());
                        psInsertar.setString(11, Usuario);
                        psInsertar.execute();
                        psInsertar.clearParameters();
                        psInsertar.close();

                    } else {

                        psBuscaFolioLote.clearParameters();
                        psBuscaFolioLote = con.getConn().prepareStatement(BuscaIndiceLote);
                        rs = psBuscaFolioLote.executeQuery();
                        if (rs.next()) {
                            IndLote = rs.getInt(1);
                        }

                        psBuscaFolioLote.clearParameters();

                        psBuscaFolioLote = con.getConn().prepareStatement(ActualizaIndiceLote);
                        psBuscaFolioLote.setInt(1, IndLote + 1);
                        psBuscaFolioLote.execute();

                        if (f.getUbica().equals("NUEVA")) {
                            psBuscaFolioLote.clearParameters();
                            psBuscaFolioLote = con.getConn().prepareStatement(INSERTARLOTE);
                            psBuscaFolioLote.setInt(1, 0);
                            psBuscaFolioLote.setString(2, f.getClaPro());
                            psBuscaFolioLote.setString(3, f.getLote());
                            psBuscaFolioLote.setString(4, f.getCaducidad());
                            psBuscaFolioLote.setInt(5, cantidad);
                            psBuscaFolioLote.setInt(6, IndLote);
                            psBuscaFolioLote.setString(7, f.getClaOrg());
                            psBuscaFolioLote.setString(8, f.getUbica());
                            psBuscaFolioLote.setString(9, f.getFecFab());
                            psBuscaFolioLote.setString(10, f.getCb());
                            psBuscaFolioLote.setString(11, f.getClaMar());
                            psBuscaFolioLote.setString(12, f.getOrigen());
                            psBuscaFolioLote.setString(13, f.getClaOrg());
                            psBuscaFolioLote.setInt(14, 131);
                            psBuscaFolioLote.setInt(15, ProyectoFinal);
                            psBuscaFolioLote.execute();
                        } else {
                            psBuscaFolioLote.clearParameters();
                            psBuscaFolioLote = con.getConn().prepareStatement(INSERTARLOTE);
                            psBuscaFolioLote.setInt(1, 0);
                            psBuscaFolioLote.setString(2, f.getClaPro());
                            psBuscaFolioLote.setString(3, f.getLote());
                            psBuscaFolioLote.setString(4, f.getCaducidad());
                            psBuscaFolioLote.setInt(5, 0);
                            psBuscaFolioLote.setInt(6, IndLote);
                            psBuscaFolioLote.setString(7, f.getClaOrg());
                            psBuscaFolioLote.setString(8, "NUEVA");
                            psBuscaFolioLote.setString(9, f.getFecFab());
                            psBuscaFolioLote.setString(10, f.getCb());
                            psBuscaFolioLote.setString(11, f.getClaMar());
                            psBuscaFolioLote.setString(12, f.getOrigen());
                            psBuscaFolioLote.setString(13, f.getClaOrg());
                            psBuscaFolioLote.setInt(14, 131);
                            psBuscaFolioLote.setInt(15, ProyectoFinal);
                            psBuscaFolioLote.execute();

                            psBuscaFolioLote.clearParameters();
                            psBuscaFolioLote = con.getConn().prepareStatement(INSERTARLOTE);
                            psBuscaFolioLote.setInt(1, 0);
                            psBuscaFolioLote.setString(2, f.getClaPro());
                            psBuscaFolioLote.setString(3, f.getLote());
                            psBuscaFolioLote.setString(4, f.getCaducidad());
                            psBuscaFolioLote.setInt(5, cantidad);
                            psBuscaFolioLote.setInt(6, IndLote);
                            psBuscaFolioLote.setString(7, f.getClaOrg());
                            psBuscaFolioLote.setString(8, f.getUbica());
                            psBuscaFolioLote.setString(9, f.getFecFab());
                            psBuscaFolioLote.setString(10, f.getCb());
                            psBuscaFolioLote.setString(11, f.getClaMar());
                            psBuscaFolioLote.setString(12, f.getOrigen());
                            psBuscaFolioLote.setString(13, f.getClaOrg());
                            psBuscaFolioLote.setInt(14, 131);
                            psBuscaFolioLote.setInt(15, ProyectoFinal);
                            psBuscaFolioLote.execute();
                        }

                        psInsertar = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
                        psInsertar.setInt(1, FolioFactura);
                        psInsertar.setInt(2, 9);
                        psInsertar.setString(3, f.getClaPro());
                        psInsertar.setInt(4, cantidad);
                        psInsertar.setDouble(5, Costo);
                        psInsertar.setDouble(6, MontoIva);
                        psInsertar.setString(7, "1");
                        psInsertar.setInt(8, IndLote);
                        psInsertar.setString(9, f.getUbica());
                        psInsertar.setString(10, f.getClaProve());
                        psInsertar.setString(11, Usuario);
                        psInsertar.execute();

                    }

                    psActualizaTemp = con.getConn().prepareStatement(ACTUALIZA_FACTTEM);
                    psActualizaTemp.setString(1, f.getId());
                    psActualizaTemp.execute();
                    psActualizaTemp.clearParameters();
                    psActualizaTemp.close();
                    psBuscaFolioLote.close();

                } else {
                    save = false;
                    con.getConn().rollback();
                    return save;
                }

            }

//            psInsertar.clearParameters();
            psInsertar = con.getConn().prepareStatement(INSERTAtransferenciaproyecto);
            psInsertar.setInt(1, FolioFactura);
            psInsertar.setInt(2, Proyecto);
            psInsertar.setInt(3, ProyectoFinal);
            psInsertar.setString(4, Observaciones);
            psInsertar.setString(5, Usuario);
            psInsertar.execute();

            save = true;
            con.getConn().commit();
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }

    ///Facturacion Automatica  Michoacan Confirmar
    @Override
    public boolean RegistrarFoliosApartarMich(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC, String tablaUnireq) {
        boolean save = false;
        int UbicaModu = 0, piezas = 0, F_Solicitado = 0, ContarV = 0, Existencia = 0, FolioFactura = 0, DifeSol = 0, FolioLote = 0, Nivel = 0, NivelTR = 0;
        String UbicaDesc = "", Clave = "", Unidad = "", Unidad2 = "", UbicaLote = "", Contrato = "", Ubicaciones = "", UbicaNofacturar = "", UbicacionesTemp = "", filtroOrigen = "", filtroRural = "";

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psActualizaTemp = con.getConn().prepareStatement(ELIMINA_DATOSporFACTURARTEMP);
            psActualizaTemp.setString(1, Usuario);
            psActualizaTemp.execute();
            psActualizaTemp.clearParameters();

            psBuscaContrato = con.getConn().prepareStatement(BuscaContrato);
            psBuscaContrato.setInt(1, Proyecto);
            rsContrato = psBuscaContrato.executeQuery();
            psBuscaContrato.clearParameters();
            if (rsContrato.next()) {
                Contrato = rsContrato.getString(1);
            }

            psUbicaTemporal = con.getConn().prepareStatement(BuscaUbicaTemporal);
            rsUbicaTemp = psUbicaTemporal.executeQuery();
            if (rsUbicaTemp.next()) {
                UbicacionesTemp = "," + rsUbicaTemp.getString(1);
            }


            if (Catalogo > 0) {
                psConsulta = con.getConn().prepareStatement(BUSCA_PARAMETRO);
                psConsulta.setString(1, Usuario);
                rsConsulta = psConsulta.executeQuery();
                rsConsulta.next();
                UbicaModu = rsConsulta.getInt(1);
                Proyecto = rsConsulta.getInt(2);
                UbicaDesc = rsConsulta.getString(3);
                psConsulta.close();
                psConsulta = null;
                System.out.println("proy:" + Proyecto);

            }


//            if (ClaUnidad.equals("10981")) {
//                Proyecto = 3 ;
//            }

            psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));
            psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
            psInsertarFactTemp = con.getConn().prepareStatement(INSERTA_FACTURATEMP);
            psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
            psActualizaIndiceLote = con.getConn().prepareStatement(ACTUALIZA_INDICELOTE);
            psINSERTLOTE = con.getConn().prepareStatement(INSERTAR_NUEVOLOTE);
            psActualizaReq = con.getConn().prepareStatement(ACTUALIZA_STSREQ);
            psInsertarObs = con.getConn().prepareStatement(INSERTAR_OBSERVACIONES);

            psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADES, tablaUnireq, ClaUnidad, Catalogo));
            rsBuscaUnidad = psBuscaUnidad.executeQuery();
            while (rsBuscaUnidad.next()) {
                Unidad = rsBuscaUnidad.getString(1);
                Unidad2 = rsBuscaUnidad.getString(1);
                Unidad = "'" + Unidad + "'";
                Nivel = rsBuscaUnidad.getInt(2);
                NivelTR = rsBuscaUnidad.getInt(3);

                ///valida datos para facturar primer nivel y segundo
                switch (UbicaModu) {
//                    case 6:
//                        if (Nivel == 1) {
//                            if (NivelTR == 1) {
//                                filtroOrigen = "INNER JOIN";
//                                filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
//                                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                                psBuscaDatosFact.setInt(1, Proyecto);
//                            } else {
//                                filtroOrigen = "LEFT JOIN";
//                                filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
//                                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                                psBuscaDatosFact.setInt(1, Proyecto);
//                            }
//                        } else {
//                            System.out.println("<script>alert('Validar Parametro de Facturacion para 1er nivel')</script>");
//                            return false;
//                        }
//                        break;
//
//                    case 7:
//                        if (Nivel == 1) {
//                            filtroOrigen = "LEFT JOIN";
//                            filtroRural = " AND F_FecCad >= CURDATE() ";
//                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//
//                        } else {
//                            System.out.println("<script>alert('Validar Parametro de Facturacion para 1er nivel')</script>");
//                            return false;
//                        }
//                        break;
//                    case 8:
//                        if (Nivel != 1) {
//                            if (NivelTR == 1) {
//                                filtroOrigen = "INNER JOIN";
//                                filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
//                                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                                psBuscaDatosFact.setInt(1, Proyecto);
//                            } else {
//                                filtroOrigen = "LEFT JOIN";
//                                filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
//                                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                                psBuscaDatosFact.setInt(1, Proyecto);
//
//                            }
//                        } else {
//                            filtroOrigen = "LEFT JOIN";
//                            filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) AND F_Origen != 8 ";
//                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//                        }
//
//                        break;
//                    case 9:
//                        if (Nivel != 1) {
//                            filtroOrigen = "LEFT JOIN";
//                            filtroRural = " AND F_FecCad >= CURDATE() AND F_Origen != 8 ";
//                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//
//                        } else {
//                            System.out.println("<script>alert('Validar Parametro de Facturacion para 2 y 3 nivel')</script>");
//                            return false;
//
//                        }
//
//                        break;
//                    case 11:
///*GASTOS CATASTROFICOS*/
//                        if (Nivel != 1) {
//                            filtroOrigen = "INNER JOIN";
//                            filtroRural = " AND F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) AND F_Origen = 19 ";
//                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARGC, tablaUnireq, UbicaDesc, filtroRural, Unidad, filtroOrigen,  Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//
//                        } else {
//                            System.out.println("<script>alert('Validar Parametro de Facturacion para 2 y 3 nivel')</script>");
//                            return false;
//
//                        }
//
//                        break;
//                    case 12:
//                     
//                            filtroOrigen = "LEFT JOIN";
//                            filtroRural = " AND F_FecCad > CURDATE() ";
//                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARN1, tablaUnireq, UbicaDesc, filtroRural, filtroOrigen, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//
//                       
//
//                        break;
//                    case 34:
//                    case 35:
//                    case 36:
//                    case 37:
//                    case 38:
//                        
//                          psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARCAD, tablaUnireq, UbicaDesc, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//                        
//                        break;
//                    case 39:
//                        psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURARMDRF, tablaUnireq, UbicaDesc, Unidad, Catalogo));
//                            psBuscaDatosFact.setInt(1, Proyecto);
//                       break;
                    default:
                        if (Nivel != 1) {
                            psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURAR, tablaUnireq, UbicaDesc, Unidad, Catalogo));
                            psBuscaDatosFact.setInt(1, Proyecto);
                        } else {
                            System.out.println("<script>alert('Validar Parametro de Facturacion para 2 y 3 nivel')</script>");
                            return false;

                        }
                }


                System.out.println("DATOS POR FACTURAR: "+psBuscaDatosFact);
                rsBuscaDatosFact = psBuscaDatosFact.executeQuery();

                List<DetalleFactura> folioAF = new ArrayList<DetalleFactura>();
                List<DetalleFactura> folioControlado = new ArrayList<DetalleFactura>();
                List<DetalleFactura> folioRedFria = new ArrayList<DetalleFactura>();
                List<DetalleFactura> folioOtro = new ArrayList<DetalleFactura>();

                while (rsBuscaDatosFact.next()) {
                    Clave = rsBuscaDatosFact.getString(2);
                    piezas = rsBuscaDatosFact.getInt(3);
                    F_Solicitado = rsBuscaDatosFact.getInt(5);
                    Existencia = rsBuscaDatosFact.getInt(7);
                    FolioLote = rsBuscaDatosFact.getInt(8);
                    UbicaLote = rsBuscaDatosFact.getString(9);
                    Observaciones = rsBuscaDatosFact.getString(10);
                    Existencia = this.getExistenciaByClave(Clave, Proyecto, UbicaDesc);
                    DetalleFactura detalle = new DetalleFactura(rsBuscaDatosFact);
                    System.out.println("rsBuscaDatosFact: "+rsBuscaDatosFact);
                    System.out.println("piezas: "+piezas+"Existencia: "+Existencia);
                    if ((piezas > 0) && (Existencia > 0)) {

                        int F_IdLote = 0, F_FolLot = 0, Tipo = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0;
                        int Facturado = 0, Contar = 0;
                        String Ubicacion = "", c = "";
                        PreparedStatement p = null;

                        double Costo = 0.0, IVA = 0.0, IVAPro = 0.0, Monto = 0.0, MontoIva = 0.0;

                        switch (UbicaModu) {
//                            case 6:
//                                if (Nivel == 1) {
//                                    if (NivelTR == 1) {
//                                        filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
//                                    } else {
//                                        filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
//                                    }
//                                    c = String.format(LOTES_DISPONIBLESN1, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                }
//                                break;
//                            case 7:
//                                if (Nivel == 1) {
//                                    filtroRural = " AND L.F_FecCad >= CURDATE() ";
//                                    c = String.format(LOTES_DISPONIBLESN1, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                }
//                                break;
//                            case 8:
//                                if (Nivel == 1) {
//                                    if (NivelTR == 1) {
//                                        filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
//                                    } else {
//                                        filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY)";
//                                    }
//                                } else {
//                                    filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) AND L.F_Origen != 8 ";
//
//                                }
//                                c = String.format(LOTES_DISPONIBLESN1, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
//                                System.out.println(c);
//                                p = con.getConn().prepareStatement(c);
//                                p.setString(1, Clave);
//                                p.setInt(2, Proyecto);
//                                p.setInt(3, Proyecto);
//                                p.setString(4, Clave);
//
//                                break;
//                            case 9:
//                                if (Nivel != 1) {
//                                    filtroRural = " AND L.F_FecCad >= CURDATE() AND L.F_Origen != 8 ";
//                                    c = String.format(LOTES_DISPONIBLESN1, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                }
//                                break;
///*LOTES PARA GASTOS*/
//                            case 11:
//                                if (Nivel != 1) {
//                                    filtroRural = " AND L.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) AND L.F_Origen = 19 ";
//                                    c = String.format(LOTES_DISPONIBLESGC, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"),filtroRural, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
////                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setInt(1, Proyecto);
//                                    p.setString(2, ClaUnidad.replace("'", ""));
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                }
//                                break;
//                            case 12:
//                                
//                                    filtroRural = " AND L.F_FecCad > CURDATE() ";
//                                    c = String.format(LOTES_DISPONIBLESN1, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo, filtroRural);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                
//                                break;
//                            case 34:
//                            case 35:
//                            case 36:
//                            case 37:
//                            case 38:    
//                                  c = String.format(LOTES_DISPONIBLESCAD, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                            
//                            
//                            break;
//                            case 39:
//                             c = String.format(LOTES_DISPONIBLESMDRF, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo);
//                                    System.out.println(c);
//                                    p = con.getConn().prepareStatement(c);
//                                    p.setString(1, Clave);
//                                    p.setInt(2, Proyecto);
//                                    p.setInt(3, Proyecto);
//                                    p.setString(4, Clave);
//                                break;
                            default:
                                if (Nivel != 1) {
                                    c = String.format(LOTES_DISPONIBLES, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo);
                                    System.out.println(c);
                                    p = con.getConn().prepareStatement(c);
                                    p.setString(1, Clave);
                                    p.setInt(2, Proyecto);
                                    p.setInt(3, Proyecto);
                                    p.setString(4, Clave);
                                }
                        }//fin del switch

                        System.out.println("Query de lotes"+p);
                        rsBuscaExiFol = p.executeQuery();

                        if (rsBuscaExiFol.last()) {
                            System.out.println("Estoy en contar");
                            Contar = rsBuscaExiFol.getRow();
                            rsBuscaExiFol.beforeFirst();
                        }
                        System.out.println("Contar: "+Contar);
//                        System.out.println(" rsBuscaExiFol: "+ rsBuscaExiFol);

                        while (rsBuscaExiFol.next()) {
                            System.out.println("si entre a bucas esxistencia: ");
                            F_IdLote = rsBuscaExiFol.getInt(1);
                            F_ExiLot = rsBuscaExiFol.getInt(7);
                            F_FolLot = rsBuscaExiFol.getInt(3);
                            Tipo = rsBuscaExiFol.getInt(4);
                            Costo = rsBuscaExiFol.getDouble(5);
                            Ubicacion = rsBuscaExiFol.getString(6).trim();
                            ClaProve = rsBuscaExiFol.getInt(8);

                            if (Tipo == 2504) {
                                IVA = 0.0;
                            } else {
                                IVA = 0.16;
                            }

                            if (Proyecto != 5) {
                                Costo = 0.0;
                            }
                            detalle.setExistencia(F_ExiLot);
                            detalle.setFolioLote(F_FolLot);
                            detalle.setCosto(Costo);
                            detalle.setUbicaLote(Ubicacion);
                            detalle.setContar(Contar);
                            System.out.println(Ubicacion);
                            switch (Ubicacion) {
                                case "CONTROLADO":
//                                case "CONTROLADO1N":
                                case "GNKCONTROLADO":    
//                                case "MERE-CTRL":
//                                case "APE":
//                                case "APE1N":
//                                case "APEGC":
                                    folioControlado.add(detalle);
                                    break;
//                                case "AF1N":
//                                case "AFGC":
                                case "AF":
                                    folioAF.add(detalle);
                                    break;
                                case "REDFRIA":
//                                case "REDFRIA1N":
//                                case "GNKREDFRIA":
//                                case "REDFRIAGC":
//                                case "SSMREDFRIA":
                                    folioRedFria.add(detalle);
                                    break;
                                default:
                                    folioOtro.add(detalle);
                            }

                            if ((F_ExiLot >= piezas) && (piezas > 0)) {
                                Contar = Contar - 1;
                                diferencia = F_ExiLot - piezas;
                                CanSur = piezas;
                                IVAPro = (CanSur * Costo) * IVA;
                                Monto = CanSur * Costo;
                                MontoIva = Monto + IVAPro;
                                psInsertarFactTemp.setInt(1, FolioFactura);
                                psInsertarFactTemp.setString(2, Unidad2);
                                psInsertarFactTemp.setString(3, Clave);
                                psInsertarFactTemp.setInt(4, F_Solicitado);
                                psInsertarFactTemp.setInt(5, CanSur);
                                psInsertarFactTemp.setDouble(6, Costo);
                                psInsertarFactTemp.setDouble(7, IVAPro);
                                psInsertarFactTemp.setDouble(8, MontoIva);
                                psInsertarFactTemp.setInt(9, F_FolLot);
                                psInsertarFactTemp.setString(10, FecEnt);
                                psInsertarFactTemp.setString(11, Usuario);
                                psInsertarFactTemp.setString(12, Ubicacion);
                                psInsertarFactTemp.setString(13, Observaciones);
                                psInsertarFactTemp.setInt(14, Proyecto);
                                psInsertarFactTemp.setString(15, Contrato);
                                psInsertarFactTemp.setString(16, OC);
                                psInsertarFactTemp.setInt(17, 0);
                                psInsertarFactTemp.addBatch();
                                System.out.println("INSERTA factura tem: "+psInsertarFactTemp);

                                piezas = 0;
                                F_Solicitado = 0;
                                break;

                            } else if ((piezas > 0) && (F_ExiLot > 0)) {
                                
                                diferencia = piezas - F_ExiLot;
                                CanSur = F_ExiLot;
                                if (F_ExiLot >= F_Solicitado) {
                                    DifeSol = F_Solicitado;
                                } else if (Contar > 0) {
                                    DifeSol = F_ExiLot;
                                } else {
                                    DifeSol = F_Solicitado - F_ExiLot;
                                }

                                IVAPro = (CanSur * Costo) * IVA;
                                Monto = CanSur * Costo;
                                MontoIva = Monto + IVAPro;
                                
                                psInsertarFactTemp.setInt(1, FolioFactura);
                                psInsertarFactTemp.setString(2, Unidad2);
                                psInsertarFactTemp.setString(3, Clave);
                                psInsertarFactTemp.setInt(4, DifeSol);
                                psInsertarFactTemp.setInt(5, CanSur);
                                psInsertarFactTemp.setDouble(6, Costo);
                                psInsertarFactTemp.setDouble(7, IVAPro);
                                psInsertarFactTemp.setDouble(8, MontoIva);
                                psInsertarFactTemp.setInt(9, F_FolLot);
                                psInsertarFactTemp.setString(10, FecEnt);
                                psInsertarFactTemp.setString(11, Usuario);
                                psInsertarFactTemp.setString(12, Ubicacion);
                                psInsertarFactTemp.setString(13, Observaciones);
                                psInsertarFactTemp.setInt(14, Proyecto);
                                psInsertarFactTemp.setString(15, Contrato);
                                psInsertarFactTemp.setString(16, OC);
                                psInsertarFactTemp.setInt(17, 0);
                                psInsertarFactTemp.addBatch();

                                F_Solicitado = F_Solicitado - CanSur;

                                piezas = piezas - CanSur;
                                F_ExiLot = 0;
                                Contar = Contar - 1;
                                System.out.println("factura tem2: "+psInsertarFactTemp);
                            }
                            if (Contar == 0) {
                                if (F_Solicitado > 0) {
                                    System.out.println("aqui cuando todo es cero");
                                    psInsertarFactTemp.setInt(1, FolioFactura);
                                    psInsertarFactTemp.setString(2, Unidad2);
                                    psInsertarFactTemp.setString(3, Clave);
                                    psInsertarFactTemp.setInt(4, F_Solicitado);
                                    psInsertarFactTemp.setInt(5, 0);
                                    psInsertarFactTemp.setDouble(6, Costo);
                                    psInsertarFactTemp.setDouble(7, IVAPro);
                                    psInsertarFactTemp.setDouble(8, MontoIva);
                                    psInsertarFactTemp.setInt(9, F_FolLot);
                                    psInsertarFactTemp.setString(10, FecEnt);
                                    psInsertarFactTemp.setString(11, Usuario);
                                    psInsertarFactTemp.setString(12, Ubicacion);
                                    psInsertarFactTemp.setString(13, Observaciones);
                                    psInsertarFactTemp.setInt(14, Proyecto);
                                    psInsertarFactTemp.setString(15, Contrato);
                                    psInsertarFactTemp.setString(16, OC);
                                    psInsertarFactTemp.setInt(17, 0);
                                    psInsertarFactTemp.addBatch();
                                    F_Solicitado = 0;
                                }
                            }

                        }

                    } else if ((FolioLote > 0) && (UbicaLote != "")) {
                        System.out.println("no hay");
                        psInsertarFactTemp.setInt(1, FolioFactura);
                        psInsertarFactTemp.setString(2, Unidad2);
                        psInsertarFactTemp.setString(3, Clave);
                        psInsertarFactTemp.setInt(4, F_Solicitado);
                        psInsertarFactTemp.setInt(5, 0);
                        psInsertarFactTemp.setDouble(6, 0);
                        psInsertarFactTemp.setDouble(7, 0);
                        psInsertarFactTemp.setDouble(8, 0);
                        psInsertarFactTemp.setInt(9, FolioLote);
                        psInsertarFactTemp.setString(10, FecEnt);
                        psInsertarFactTemp.setString(11, Usuario);
                        psInsertarFactTemp.setString(12, UbicaLote);
                        psInsertarFactTemp.setString(13, Observaciones);
                        psInsertarFactTemp.setInt(14, Proyecto);
                        psInsertarFactTemp.setString(15, Contrato);
                        psInsertarFactTemp.setString(16, OC);
                        psInsertarFactTemp.setInt(17, 0);
                        psInsertarFactTemp.addBatch();
                        System.out.println("factura tem3: "+psInsertarFactTemp);
                    } else {
                        System.out.println("si no existe");
                        int FolioL = 0, IndiceLote = 0;
                        String Ubicacion = "";
                        double Costo = 0.0;

                        psBuscaIndiceLote = con.getConn().prepareStatement(BUSCA_INDICELOTE);
                        rsIndiceLote = psBuscaIndiceLote.executeQuery();
                        rsIndiceLote.next();
                        FolioL = rsIndiceLote.getInt(1);

                        IndiceLote = FolioL + 1;

                        psActualizaIndiceLote.setInt(1, IndiceLote);
                        psActualizaIndiceLote.addBatch();

                        psINSERTLOTE.setString(1, Clave);
                        psINSERTLOTE.setInt(2, FolioL);
                        psINSERTLOTE.setInt(3, Proyecto);
                        System.out.println("InsertarLote" + psINSERTLOTE);
                        psINSERTLOTE.addBatch();

                        psInsertarFactTemp.setInt(1, FolioFactura);
                        psInsertarFactTemp.setString(2, Unidad2);
                        psInsertarFactTemp.setString(3, Clave);
                        psInsertarFactTemp.setInt(4, F_Solicitado);
                        psInsertarFactTemp.setInt(5, 0);
                        psInsertarFactTemp.setDouble(6, Costo);
                        psInsertarFactTemp.setDouble(7, 0);
                        psInsertarFactTemp.setDouble(8, 0);
                        psInsertarFactTemp.setInt(9, FolioL);
                        psInsertarFactTemp.setString(10, FecEnt);
                        psInsertarFactTemp.setString(11, Usuario);
                        psInsertarFactTemp.setString(12, "NUEVA");
                        psInsertarFactTemp.setString(13, Observaciones);
                        psInsertarFactTemp.setInt(14, Proyecto);
                        psInsertarFactTemp.setString(15, Contrato);
                        psInsertarFactTemp.setString(16, OC);
                        psInsertarFactTemp.setInt(17, 0);
                        psInsertarFactTemp.addBatch();
                    }

                }
                System.out.println("fin");

                psActualizaReq.setString(1, Unidad2);
                psActualizaReq.addBatch();
                psINSERTLOTE.executeBatch();
                psInsertarFactTemp.executeBatch();
                psActualizaIndiceLote.executeBatch();
                psActualizaReq.executeBatch();
                save = true;
                con.getConn().commit();

            }

            psBuscaDatosFact.close();
            psBuscaDatosFact = null;
            rsBuscaDatosFact.close();
            rsBuscaDatosFact = null;
//            rsUbicaNoFacturar.close();
//            rsUbicaNoFacturar = null;
            psActualizaReq.close();
            psActualizaReq = null;
            psINSERTLOTE.close();
            psINSERTLOTE = null;
            psInsertarFactTemp.close();
            psInsertarFactTemp = null;
            psActualizaIndiceLote.close();
            psActualizaIndiceLote = null;
            psBuscaUnidad.close();
            psBuscaUnidad = null;
            rsBuscaUnidad.close();
            rsBuscaUnidad = null;
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean RegistrarFoliosApartarMichPorMes(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC) {
        boolean save = false;
        int UbicaModu = 0, piezas = 0, F_Solicitado = 0, ContarV = 0, Existencia = 0, FolioFactura = 0, DifeSol = 0, FolioLote = 0;
        String UbicaDesc = "", Clave = "", Unidad = "", Unidad2 = "", UbicaLote = "", Contrato = "", Ubicaciones = "", UbicaNofacturar = "", UbicacionesTemp = "";

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psActualizaTemp = con.getConn().prepareStatement(ELIMINA_DATOSporFACTURARTEMP);
            psActualizaTemp.setString(1, Usuario);
            psActualizaTemp.execute();
            psActualizaTemp.clearParameters();

            psBuscaContrato = con.getConn().prepareStatement(BuscaContrato);
            psBuscaContrato.setInt(1, Proyecto);
            rsContrato = psBuscaContrato.executeQuery();
            psBuscaContrato.clearParameters();
            if (rsContrato.next()) {
                Contrato = rsContrato.getString(1);
            }

            psUbicaTemporal = con.getConn().prepareStatement(BuscaUbicaTemporal);
            rsUbicaTemp = psUbicaTemporal.executeQuery();
            if (rsUbicaTemp.next()) {
                UbicacionesTemp = "," + rsUbicaTemp.getString(1);
            }

            if (Catalogo > 0) {
                psConsulta = con.getConn().prepareStatement(BUSCA_PARAMETRO);
                psConsulta.setString(1, Usuario);
                rsConsulta = psConsulta.executeQuery();
                rsConsulta.next();
                UbicaModu = rsConsulta.getInt(1);
                Proyecto = rsConsulta.getInt(2);
                UbicaDesc = rsConsulta.getString(3);
                psConsulta.close();
                psConsulta = null;
                System.out.println("proy:" + Proyecto);
            }

// if (ClaUnidad.equals("10981")) {
//                Proyecto = 3 ;
//            }
            psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));
            psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
            psInsertarFactTemp = con.getConn().prepareStatement(INSERTA_FACTURATEMP);
            psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
            psActualizaIndiceLote = con.getConn().prepareStatement(ACTUALIZA_INDICELOTE);
            psINSERTLOTE = con.getConn().prepareStatement(INSERTAR_NUEVOLOTE);
            psActualizaReq = con.getConn().prepareStatement(ACTUALIZA_STSREQ);
            psInsertarObs = con.getConn().prepareStatement(INSERTAR_OBSERVACIONES);

            psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADES, "tb_unireq", ClaUnidad, Catalogo));
            rsBuscaUnidad = psBuscaUnidad.executeQuery();
            while (rsBuscaUnidad.next()) {
                Unidad = rsBuscaUnidad.getString(1);
                Unidad2 = rsBuscaUnidad.getString(1);
                Unidad = "'" + Unidad + "'";

                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURAR, "tb_unireq", UbicaDesc, Unidad, Catalogo));
                psBuscaDatosFact.setInt(1, Proyecto);
//                psBuscaDatosFact.setInt(2, Proyecto);
                rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                while (rsBuscaDatosFact.next()) {
                    Clave = rsBuscaDatosFact.getString(2);
                    piezas = rsBuscaDatosFact.getInt(3);
                    F_Solicitado = rsBuscaDatosFact.getInt(5);
                    Existencia = rsBuscaDatosFact.getInt(8);
                    FolioLote = rsBuscaDatosFact.getInt(9);
                    UbicaLote = rsBuscaDatosFact.getString(10);
                    Observaciones = rsBuscaDatosFact.getString(11);
                    Existencia = this.getExistenciaByClave(Clave, Proyecto, UbicaDesc);
                    DetalleFactura detalle = new DetalleFactura(rsBuscaDatosFact);
                    int prorrateado = F_Solicitado / 5;
                    int ultimoMes = prorrateado + (F_Solicitado % 5);
                    int restante = 0, cantidad = 0;
                    List<Integer> conExistencia = new ArrayList();
                    if ((piezas > 0) && (Existencia > 0)) {
                        for (int i = 0; i < 5; i++) {
                            cantidad = i == 4 ? ultimoMes : prorrateado;
//                            cantidad = cantidad + restante;
                            restante = this.surtirMes(UbicaDesc, Clave, Proyecto, i, Catalogo, F_Solicitado, cantidad, FolioFactura, Unidad2, FecEnt, Usuario, Observaciones, Contrato, OC);
                            if (restante == 0) {
                                conExistencia.add(i);
                            }
                        }
                        /*while (restante > 0 && restante < F_Solicitado) {
                            prorrateado = restante / conExistencia.size();
                            ultimoMes = prorrateado + (restante % conExistencia.size());
                            List<Integer> conExistenciaAux = new ArrayList();
                            restante = 0;
                            for (int i = 0; i < conExistencia.size(); i++) {
                                cantidad = i == (conExistencia.size() - 1) ? ultimoMes : prorrateado;
                                cantidad = cantidad + restante;
                                restante = this.surtirMes(UbicaDesc, Clave, Proyecto, conExistencia.get(i), Catalogo, F_Solicitado, cantidad, FolioFactura, Unidad2, FecEnt, Usuario, Observaciones, Contrato, OC);
                                if (restante == 0) {
                                    conExistenciaAux.add(conExistencia.get(i));
                                }
                            }
                            conExistencia = conExistenciaAux;
                        }*/
                    } else if ((FolioLote > 0) && (UbicaLote != "")) {
                        System.out.println("no hay");
                        psInsertarFactTemp.setInt(1, FolioFactura);
                        psInsertarFactTemp.setString(2, Unidad2);
                        psInsertarFactTemp.setString(3, Clave);
                        psInsertarFactTemp.setInt(4, F_Solicitado);
                        psInsertarFactTemp.setInt(5, 0);
                        psInsertarFactTemp.setDouble(6, 0);
                        psInsertarFactTemp.setDouble(7, 0);
                        psInsertarFactTemp.setDouble(8, 0);
                        psInsertarFactTemp.setInt(9, FolioLote);
                        psInsertarFactTemp.setString(10, FecEnt);
                        psInsertarFactTemp.setString(11, Usuario);
                        psInsertarFactTemp.setString(12, UbicaLote);
                        psInsertarFactTemp.setString(13, Observaciones);
                        psInsertarFactTemp.setInt(14, Proyecto);
                        psInsertarFactTemp.setString(15, Contrato);
                        psInsertarFactTemp.setString(16, OC);
                        psInsertarFactTemp.setInt(17, 0);
                        psInsertarFactTemp.addBatch();
                    } else {
                        System.out.println("si no existe");
                        int FolioL = 0, IndiceLote = 0;
                        String Ubicacion = "";
                        double Costo = 0.0;

                        psBuscaIndiceLote = con.getConn().prepareStatement(BUSCA_INDICELOTE);
                        rsIndiceLote = psBuscaIndiceLote.executeQuery();
                        rsIndiceLote.next();
                        FolioL = rsIndiceLote.getInt(1);

                        IndiceLote = FolioL + 1;

                        psActualizaIndiceLote.setInt(1, IndiceLote);
                        psActualizaIndiceLote.addBatch();

                        psINSERTLOTE.setString(1, Clave);
                        psINSERTLOTE.setInt(2, FolioL);
                        psINSERTLOTE.setInt(3, Proyecto);
                        System.out.println("InsertarLote" + psINSERTLOTE);
                        psINSERTLOTE.addBatch();

                        psInsertarFactTemp.setInt(1, FolioFactura);
                        psInsertarFactTemp.setString(2, Unidad2);
                        psInsertarFactTemp.setString(3, Clave);
                        psInsertarFactTemp.setInt(4, F_Solicitado);
                        psInsertarFactTemp.setInt(5, 0);
                        psInsertarFactTemp.setDouble(6, Costo);
                        psInsertarFactTemp.setDouble(7, 0);
                        psInsertarFactTemp.setDouble(8, 0);
                        psInsertarFactTemp.setInt(9, FolioL);
                        psInsertarFactTemp.setString(10, FecEnt);
                        psInsertarFactTemp.setString(11, Usuario);
                        psInsertarFactTemp.setString(12, "NUEVA");
                        psInsertarFactTemp.setString(13, Observaciones);
                        psInsertarFactTemp.setInt(14, Proyecto);
                        psInsertarFactTemp.setString(15, Contrato);
                        psInsertarFactTemp.setString(16, OC);
                        psInsertarFactTemp.setInt(17, 0);
                        psInsertarFactTemp.addBatch();
                    }

                }
                System.out.println("fin");

                psActualizaReq.setString(1, Unidad2);
                psActualizaReq.addBatch();
                psINSERTLOTE.executeBatch();
                psInsertarFactTemp.executeBatch();
                psActualizaIndiceLote.executeBatch();
                psActualizaReq.executeBatch();
                save = true;
                con.getConn().commit();

            }

            psBuscaDatosFact.close();
            psBuscaDatosFact = null;
            rsBuscaDatosFact.close();
            rsBuscaDatosFact = null;
//            rsUbicaNoFacturar.close();
//            rsUbicaNoFacturar = null;
            psActualizaReq.close();
            psActualizaReq = null;
            psINSERTLOTE.close();
            psINSERTLOTE = null;
            psInsertarFactTemp.close();
            psInsertarFactTemp = null;
            psActualizaIndiceLote.close();
            psActualizaIndiceLote = null;
            psBuscaUnidad.close();
            psBuscaUnidad = null;
            rsBuscaUnidad.close();
            rsBuscaUnidad = null;
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    /*facturacion automatica REGISTRO DE FOLIOS DONDE DIVIDIR FOLIO POR UBICACION
     */
    @Override
    public boolean RegistrarFoliosMich(String ClaUnidad, int Catalogo, String Tipos, String FecEnt, String Usuario, String Observaciones, int Proyecto, String OC) {
        boolean save = false;
        int UbicaModu = 0, piezas = 0, F_Solicitado = 0, ContarV = 0, Existencia = 0, FolioFactura = 0, DifeSol = 0, FolioLote = 0;
        String UbicaDesc = "", Clave = "", Unidad = "", Unidad2 = "", UbicaLote = "", Contrato = "", Ubicaciones = "", UbicaNofacturar = "",ClaUnidadSmm = "";

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            int parametroUsuario = this.getParametroUsuario(Usuario, con);
            if (parametroUsuario == 13) {
                boolean saved = this.crearFolioProvisional(con, Usuario, ClaUnidad, FecEnt);
                con.getConn().commit();
                return saved;
            }
            ClaUnidadSmm = ClaUnidad;
            if (ClaUnidadSmm.equals("'10981'")) {
                Proyecto = 3;
                System.out.println("Proyecto: " + Proyecto);
                psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));

            } else {
                psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));
            }
            System.out.println("ClaUnidad: " + ClaUnidad + " Proyecto: " + Proyecto);

            //    psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACT, Proyecto));
            System.out.println("si ando aqui RegistrarFoliosMich");
            psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
            psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
            psInsertarObs = con.getConn().prepareStatement(INSERTAR_OBSERVACIONES);
            psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

            /*psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADESTEMP, ClaUnidad));
            psBuscaUnidad.setString(1, Usuario);*/
            psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADESTEMP, ClaUnidad));
            rsBuscaUnidad = psBuscaUnidad.executeQuery();
            while (rsBuscaUnidad.next()) {
            System.out.println("si ando aqui en espera: "+psBuscaUnidad);
                Unidad = rsBuscaUnidad.getString(1);
                Unidad2 = rsBuscaUnidad.getString(1);
                Unidad = "'" + Unidad + "'";
                String Obs = "", ContratoSelect = "";
                double Costo = 0.0, IVA = 0.0, IVAPro = 0.0, Monto = 0.0, MontoIva = 0.0;
                int CantSur = 0, CantSurCR = 0, ProyectoSelect = 0;
                psBuscaUnidadFactura = con.getConn().prepareStatement(BUSCA_UNIDADFACTURA);
                psBuscaUnidadFactura.setString(1, Unidad2);
                psBuscaUnidadFactura.setString(2, Usuario);
                psBuscaUnidadFactura.setString(3, Unidad2);
                psBuscaUnidadFactura.setString(4, Usuario);
                psBuscaUnidadFactura.setString(5, Unidad2);
                psBuscaUnidadFactura.setString(6, Usuario);
                rsBuscaUnidadFactura = psBuscaUnidadFactura.executeQuery();
                System.out.println("si ando aqui en espera2: "+psBuscaUnidadFactura);
                if (rsBuscaUnidadFactura.next()) {
                    CantSur = rsBuscaUnidadFactura.getInt(1);
                    CantSurCR = rsBuscaUnidadFactura.getInt(2);
                    Obs = rsBuscaUnidadFactura.getString(3);
                    Observaciones = Obs;
                }
  
                if (CantSur > 0) {
                System.out.println("si ando aqui 1");
                    List<DetalleFactura> folioZiporex = new ArrayList<DetalleFactura>();
                    List<DetalleFactura> folioControlado = new ArrayList<DetalleFactura>();
                    List<DetalleFactura> folioRedFria = new ArrayList<DetalleFactura>();
                    List<DetalleFactura> folioOtro = new ArrayList<DetalleFactura>();

                    psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSporFACTURARTEMP);
                    psBuscaDatosFact.setString(1, Unidad2);
                    psBuscaDatosFact.setString(2, Usuario);
                    System.out.println(psBuscaDatosFact);
                    rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                    System.out.println("Empieza consulta ");
                    while (rsBuscaDatosFact.next()) {
                        UbicaLote = rsBuscaDatosFact.getString(5);
                        DetalleFactura detalle = DetalleFactura.buildDetalleFactura(rsBuscaDatosFact);
                        detalle.setFecEnt(FecEnt);
                        detalle.setOc(OC);
                        System.out.println("Ubicación: " + UbicaLote);
                        switch (UbicaLote.trim()) {
                            case "NUEVA"://CERO
                            case "APE":
                            case "APEGC":
                            case "AF":
                            case "AFGC":
                                folioZiporex.add(detalle);
                                break;

                            case "AF1N":
                            case "APE1N":
                            case "RUTAORDINARIA":
                                folioZiporex.add(detalle);
                                break;

                            case "CONTROLADO":
                            case "CONTROLADO1N":
                            case "GNKCONTROLADO":
                            case "MERE-CTRL":    
                                folioControlado.add(detalle);
                                break;

                            case "REDFRIA":
                            case "REDFRIA1N":
                            case "GNKREDFRIA":
                            case "REDFRIAGC":
                            case "SSMREDFRIA":
                                folioRedFria.add(detalle);
                                break;

                            default:
                                folioOtro.add(detalle);

                        }
                    }

                    if (!folioControlado.isEmpty()) {
                        System.out.println("Creando folio controlado, claves:" + folioControlado.size());

                        if (Unidad2.equals("10981")) {
                            System.out.println("cambiando indice al de secretaria");
                            Proyecto = 3;
                        }
                       // System.out.println("Folio controlado ClaUnidad: " + ClaUnidad + " Proyecto: " + Proyecto);
                        psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
//                        System.out.println("Consulta folioFactura " + psBuscaIndice);
                        rsIndice = psBuscaIndice.executeQuery();
                        if (rsIndice.next()) {
                            FolioFactura = rsIndice.getInt(1);
                        }
//                        System.out.println("Indice Factura: " + FolioFactura);
                        this.crearFolio(folioControlado, UbicaDesc, Contrato, Usuario, FolioFactura, Unidad2, Tipos, Observaciones);
                        psActualizaIndice.setInt(1, FolioFactura + 1);
                        psActualizaIndice.execute();
                    }
                    System.out.println("Creando folio AF, claves:" + folioZiporex.size());
                    if (!folioZiporex.isEmpty()) {

                        if (Unidad2.equals("10981")) {
//                            System.out.println("cambiando indice al de secretaria");
                            Proyecto = 3;
                        }
                        psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
                        rsIndice = psBuscaIndice.executeQuery();
                        if (rsIndice.next()) {
                            FolioFactura = rsIndice.getInt(1);
                        }
                        this.crearPrefolio(folioZiporex, UbicaDesc, Contrato, Usuario, FolioFactura, Unidad2, Tipos, Observaciones);
                        psActualizaIndice.setInt(1, FolioFactura + 1);
                        psActualizaIndice.execute();
                    }
                    System.out.println("Creando folio RF, claves:" + folioRedFria.size());
                    if (!folioRedFria.isEmpty()) {
                        if (Unidad2.equals("10981")) {
//                            System.out.println("cambiando indice al de secretaria");
                            Proyecto = 3;
                        }
                        psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
                        rsIndice = psBuscaIndice.executeQuery();
                        if (rsIndice.next()) {
                            FolioFactura = rsIndice.getInt(1);
                        }
                        this.crearFolio(folioRedFria, UbicaDesc, Contrato, Usuario, FolioFactura, Unidad2, Tipos, Observaciones);
                        psActualizaIndice.setInt(1, FolioFactura + 1);
                        psActualizaIndice.execute();
                    }
                    System.out.println("Creando folio Otros, claves:" + folioOtro.size());
                    if (!folioOtro.isEmpty()) {

//                        if (Unidad2.equals("10981")) {
////                            System.out.println("cambiando indice al de secretaria");
//                            Proyecto = 3;
//                        }
                        psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
                        rsIndice = psBuscaIndice.executeQuery();
                        if (rsIndice.next()) {
                            FolioFactura = rsIndice.getInt(1);
                        }
                        this.crearFolio(folioOtro, UbicaDesc, Contrato, Usuario, FolioFactura, Unidad2, Tipos, Observaciones);
                        psActualizaIndice.setInt(1, FolioFactura + 1);
                        psActualizaIndice.execute();
                    }

                    /*Cross dock*/
                    if (CantSurCR == 0) {
                        psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSporFACTURARTEMPCross);
                        psBuscaDatosFact.setString(1, Unidad2);
                        psBuscaDatosFact.setString(2, Usuario);
                        rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                        while (rsBuscaDatosFact.next()) {
                            Clave = rsBuscaDatosFact.getString(1);
                            F_Solicitado = rsBuscaDatosFact.getInt(2);
                            piezas = rsBuscaDatosFact.getInt(3);
                            FolioLote = rsBuscaDatosFact.getInt(4);
                            UbicaLote = rsBuscaDatosFact.getString(5);
                            ProyectoSelect = rsBuscaDatosFact.getInt(6);
                            ContratoSelect = rsBuscaDatosFact.getString(7);
                            Costo = rsBuscaDatosFact.getDouble(8);
                            IVA = rsBuscaDatosFact.getDouble(9);
                            Monto = rsBuscaDatosFact.getDouble(10);
                            OC = rsBuscaDatosFact.getString(11);

                            psInsertarFact.setInt(1, FolioFactura);
                            psInsertarFact.setString(2, Unidad2);
                            psInsertarFact.setString(3, Clave);
                            psInsertarFact.setInt(4, F_Solicitado);
                            psInsertarFact.setInt(5, 0);
                            psInsertarFact.setDouble(6, 0.00);
                            psInsertarFact.setDouble(7, 0.00);
                            psInsertarFact.setDouble(8, 0.00);
                            psInsertarFact.setInt(9, FolioLote);
                            psInsertarFact.setString(10, FecEnt);
                            psInsertarFact.setString(11, Usuario);
                            psInsertarFact.setString(12, UbicaLote);
                            psInsertarFact.setInt(13, ProyectoSelect);
                            psInsertarFact.setString(14, ContratoSelect);
                            psInsertarFact.setString(15, OC);
                            psInsertarFact.setInt(16, 0);
                            System.out.println("fact1" + psInsertarFact);
                            psInsertarFact.addBatch();

                        }
                    }

                    psActualizaIndice.executeBatch();
                    psActualizaLote.executeBatch();
                    psInsertarMov.executeBatch();
                    psInsertarFact.executeBatch();

                    psAbasto = con.getConn().prepareStatement(DatosAbasto);
                    psAbasto.setInt(1, Proyecto);
                    psAbasto.setInt(2, FolioFactura);
                    rsAbasto = psAbasto.executeQuery();

//                    //factor de empaque
//                    while (rsAbasto.next()) {
//                        int factorEmpaque = 1;
//                        int folLot = rsAbasto.getInt("LOTE");
//                        PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
//                        psfe.setInt(1, folLot);
//                        ResultSet rsfe = psfe.executeQuery();
//                        if (rsfe.next()) {
//                            factorEmpaque = rsfe.getInt("factor");
//                        }
//                        psAbastoInsert.setString(1, rsAbasto.getString(1));
//                        psAbastoInsert.setString(2, rsAbasto.getString(2));
//                        psAbastoInsert.setString(3, rsAbasto.getString(3));
//                        psAbastoInsert.setString(4, rsAbasto.getString(4));
//                        psAbastoInsert.setString(5, rsAbasto.getString(5));
//                        psAbastoInsert.setString(6, rsAbasto.getString(6));
//                        psAbastoInsert.setString(7, rsAbasto.getString(7));
//                        psAbastoInsert.setString(8, rsAbasto.getString(8));
//                        psAbastoInsert.setString(9, rsAbasto.getString(12));
//                        psAbastoInsert.setString(10, rsAbasto.getString(10));
//                        psAbastoInsert.setString(11, Usuario);
//                        psAbastoInsert.setInt(12, factorEmpaque);
//                        System.out.println("psAbastoInsert 4" + psAbastoInsert);
//                        psAbastoInsert.addBatch();
//                    }
//
//                    psAbastoInsert.executeBatch();
                    save = true;
                    con.getConn().commit();
                    System.out.println("Terminó Unidad= " + Unidad + " Con el Folio= " + FolioFactura);
                }
  
                if (CantSurCR > 0) {
                    System.out.println("si ando aqui 2");
                        if (Unidad2.equals("10981")) {
                            System.out.println("cambiando indice al de secretaria");
                            Proyecto = 3;
                        }
                    psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACT, Proyecto));
                    rsIndice = psBuscaIndice.executeQuery();
                    if (rsIndice.next()) {
                        FolioFactura = rsIndice.getInt(1);
                    }

                    psActualizaIndice.setInt(1, FolioFactura + 1);
                    psActualizaIndice.addBatch();

                    psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSporFACTURARTEMPCross);
                    psBuscaDatosFact.setString(1, Unidad2);
                    psBuscaDatosFact.setString(2, Usuario);
                    rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                    while (rsBuscaDatosFact.next()) {
                        Clave = rsBuscaDatosFact.getString(1);
                        F_Solicitado = rsBuscaDatosFact.getInt(2);
                        piezas = rsBuscaDatosFact.getInt(3);
                        FolioLote = rsBuscaDatosFact.getInt(4);
                        UbicaLote = rsBuscaDatosFact.getString(5);
                        ProyectoSelect = rsBuscaDatosFact.getInt(6);
                        ContratoSelect = rsBuscaDatosFact.getString(7);
                        Costo = rsBuscaDatosFact.getDouble(8);
                        IVA = rsBuscaDatosFact.getDouble(9);
                        Monto = rsBuscaDatosFact.getDouble(10);
                        OC = rsBuscaDatosFact.getString(11);

                        int F_IdLote = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0;

                        psBuscaExiFol = con.getConn().prepareStatement(BUSCA_EXILOTE);
                        psBuscaExiFol.setString(1, Clave);
                        psBuscaExiFol.setInt(2, FolioLote);
                        psBuscaExiFol.setString(3, UbicaLote);
                        psBuscaExiFol.setInt(4, ProyectoSelect);

                        System.out.println("BuscaExistenciaDetalle=" + psBuscaExiFol);
                        rsBuscaExiFol = psBuscaExiFol.executeQuery();
                        while (rsBuscaExiFol.next()) {
                            F_IdLote = rsBuscaExiFol.getInt(1);
                            F_ExiLot = rsBuscaExiFol.getInt(2);
                            ClaProve = rsBuscaExiFol.getInt(3);

                            if ((F_ExiLot >= piezas) && (piezas > 0)) {
                                diferencia = F_ExiLot - piezas;
                                CanSur = piezas;

                                psActualizaLote.setInt(1, diferencia);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + Usuario + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + UbicaLote + ", piezas = " + CanSur + " ");
                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setDouble(5, Costo);
                                psInsertarMov.setDouble(6, Monto);
                                psInsertarMov.setString(7, "-1");
                                psInsertarMov.setInt(8, FolioLote);
                                psInsertarMov.setString(9, UbicaLote);
                                psInsertarMov.setInt(10, ClaProve);
                                psInsertarMov.setString(11, Usuario);
                                System.out.println("Mov1" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setInt(1, FolioFactura);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setInt(4, F_Solicitado);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setDouble(6, Costo);
                                psInsertarFact.setDouble(7, IVA);
                                psInsertarFact.setDouble(8, Monto);
                                psInsertarFact.setInt(9, FolioLote);
                                psInsertarFact.setString(10, FecEnt);
                                psInsertarFact.setString(11, Usuario);
                                psInsertarFact.setString(12, UbicaLote);
                                psInsertarFact.setInt(13, ProyectoSelect);
                                psInsertarFact.setString(14, ContratoSelect);
                                psInsertarFact.setString(15, OC);
                                psInsertarFact.setInt(16, 0);
                                System.out.println("fact1" + psInsertarFact);
                                psInsertarFact.addBatch();

                                piezas = 0;
                                F_Solicitado = 0;
                                break;

                            } else if ((piezas > 0) && (F_ExiLot > 0)) {
                                diferencia = piezas - F_ExiLot;
                                CanSur = F_ExiLot;

                                psActualizaLote.setInt(1, 0);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote2=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + Usuario + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + UbicaLote + ", piezas = " + CanSur + " ");
                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setDouble(5, Costo);
                                psInsertarMov.setDouble(6, Monto);
                                psInsertarMov.setString(7, "-1");
                                psInsertarMov.setInt(8, FolioLote);
                                psInsertarMov.setString(9, UbicaLote);
                                psInsertarMov.setInt(10, ClaProve);
                                psInsertarMov.setString(11, Usuario);
                                System.out.println("Mov2" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setInt(1, FolioFactura);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setInt(4, F_Solicitado);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setDouble(6, Costo);
                                psInsertarFact.setDouble(7, IVA);
                                psInsertarFact.setDouble(8, Monto);
                                psInsertarFact.setInt(9, FolioLote);
                                psInsertarFact.setString(10, FecEnt);
                                psInsertarFact.setString(11, Usuario);
                                psInsertarFact.setString(12, UbicaLote);
                                psInsertarFact.setInt(13, ProyectoSelect);
                                psInsertarFact.setString(14, ContratoSelect);
                                psInsertarFact.setString(15, OC);
                                psInsertarFact.setInt(16, 0);
                                System.out.println("fact2" + psInsertarFact);
                                psInsertarFact.addBatch();

                                F_Solicitado = F_Solicitado - CanSur;

                                piezas = piezas - CanSur;
                                F_ExiLot = 0;

                            } else if ((piezas == 0) && (F_ExiLot == 0)) {

                                psInsertarFact.setInt(1, FolioFactura);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setInt(4, F_Solicitado);
                                psInsertarFact.setInt(5, 0);
                                psInsertarFact.setDouble(6, 0.00);
                                psInsertarFact.setDouble(7, 0.00);
                                psInsertarFact.setDouble(8, 0.00);
                                psInsertarFact.setInt(9, FolioLote);
                                psInsertarFact.setString(10, FecEnt);
                                psInsertarFact.setString(11, Usuario);
                                psInsertarFact.setString(12, UbicaLote);
                                psInsertarFact.setInt(13, ProyectoSelect);
                                psInsertarFact.setString(14, ContratoSelect);
                                psInsertarFact.setString(15, OC);
                                psInsertarFact.setInt(16, 0);
                                System.out.println("fact2" + psInsertarFact);
                                psInsertarFact.addBatch();

                                F_Solicitado = F_Solicitado - CanSur;

                                piezas = piezas - CanSur;
                                F_ExiLot = 0;

                            }

                        }

                    }//TERMINA CROSS

                    if (CantSur == 0) {
                        psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSporFACTURARTEMP);
                        psBuscaDatosFact.setString(1, Unidad2);
                        psBuscaDatosFact.setString(2, Usuario);
                        rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                        while (rsBuscaDatosFact.next()) {
                            Clave = rsBuscaDatosFact.getString(1);
                            F_Solicitado = rsBuscaDatosFact.getInt(2);
                            piezas = rsBuscaDatosFact.getInt(3);
                            FolioLote = rsBuscaDatosFact.getInt(4);
                            UbicaLote = rsBuscaDatosFact.getString(5);
                            ProyectoSelect = rsBuscaDatosFact.getInt(6);
                            ContratoSelect = rsBuscaDatosFact.getString(7);
                            Costo = rsBuscaDatosFact.getDouble(8);
                            IVA = rsBuscaDatosFact.getDouble(9);
                            Monto = rsBuscaDatosFact.getDouble(10);
                            OC = rsBuscaDatosFact.getString(11);

                            psInsertarFact.setInt(1, FolioFactura);
                            psInsertarFact.setString(2, Unidad2);
                            psInsertarFact.setString(3, Clave);
                            psInsertarFact.setInt(4, F_Solicitado);
                            psInsertarFact.setInt(5, 0);
                            psInsertarFact.setDouble(6, 0.00);
                            psInsertarFact.setDouble(7, 0.00);
                            psInsertarFact.setDouble(8, 0.00);
                            psInsertarFact.setInt(9, FolioLote);
                            psInsertarFact.setString(10, FecEnt);
                            psInsertarFact.setString(11, Usuario);
                            psInsertarFact.setString(12, UbicaLote);
                            psInsertarFact.setInt(13, ProyectoSelect);
                            psInsertarFact.setString(14, ContratoSelect);
                            psInsertarFact.setString(15, OC);
                            psInsertarFact.setInt(16, 0);
                            System.out.println("fact1" + psInsertarFact);
                            psInsertarFact.addBatch();

                        }
                    }

                    psInsertarObs.setInt(1, FolioFactura);
                    psInsertarObs.setString(2, Obs);
                    psInsertarObs.setString(3, Tipos);
                    psInsertarObs.setInt(4, Proyecto);
                    psInsertarObs.addBatch();

                    psActualizaIndice.executeBatch();
                    psActualizaLote.executeBatch();
                    psInsertarMov.executeBatch();
                    psInsertarFact.executeBatch();
                    psInsertarObs.executeBatch();

                    psAbasto = con.getConn().prepareStatement(DatosAbasto);
                    psAbasto.setInt(1, Proyecto);
                    psAbasto.setInt(2, FolioFactura);
                    rsAbasto = psAbasto.executeQuery();
                    while (rsAbasto.next()) {
                        int factorEmpaque = 1;
                        int folLot = rsAbasto.getInt("LOTE");
                        PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                        psfe.setInt(1, folLot);
                        ResultSet rsfe = psfe.executeQuery();
                        if (rsfe.next()) {
                            factorEmpaque = rsfe.getInt("factor");
                        }
                        psAbastoInsert.setString(1, rsAbasto.getString(1));
                        psAbastoInsert.setString(2, rsAbasto.getString(2));
                        psAbastoInsert.setString(3, rsAbasto.getString(3));
                        psAbastoInsert.setString(4, rsAbasto.getString(4));
                        psAbastoInsert.setString(5, rsAbasto.getString(5));
                        psAbastoInsert.setString(6, rsAbasto.getString(6));
                        psAbastoInsert.setString(7, rsAbasto.getString(7));
                        psAbastoInsert.setString(8, rsAbasto.getString(8));
                        psAbastoInsert.setString(9, rsAbasto.getString(12));
                        psAbastoInsert.setString(10, rsAbasto.getString(10));
                        psAbastoInsert.setString(11, Usuario);
                        psAbastoInsert.setInt(12, factorEmpaque);
                        System.out.println("psAbastoInsert 6" + psAbastoInsert);
                        psAbastoInsert.addBatch();
                    }

                    psAbastoInsert.executeBatch();

                    save = true;
                    con.getConn().commit();
                    System.out.println("Terminó Unidad= " + Unidad + " Con el Folio= " + FolioFactura);
                }
            }
  System.out.println("me sali");
//            psBuscaExiFol.close();
//            psBuscaExiFol = null;
//            rsBuscaExiFol.close();
//            rsBuscaExiFol = null;
//            psBuscaDatosFact.close();
//            psBuscaDatosFact = null;
//            rsBuscaDatosFact.close();
//            rsBuscaDatosFact = null;
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(FacturacionTranDaoImpl.class
                            .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    /*enseres*/
    @Override
    public JSONArray getRegistroFactEnseres(String ClaUni) {
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObj;
        JSONObject jsonObj2;
        System.out.println("Unidades:" + ClaUni);
        String query = "SELECT U.F_ClaPro, F_ClaUni, CONCAT( F_ClaUni, '_', REPLACE (U.F_ClaPro, '.', '')) AS DATOS, F_IdReq FROM tb_enseresunireq U INNER JOIN tb_enseres M ON U.F_ClaPro = M.F_Id WHERE F_ClaUni IN (%s) AND F_Status = 0 AND F_Solicitado != 0 AND M.F_Sts = 'A';";
        PreparedStatement ps;
        ResultSet rs;
        try {
            int Contar = 0;
            con.conectar();
            ps = con.getConn().prepareStatement(String.format(query, ClaUni));
            System.out.println("UNidades=" + ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("ClaPro", rs.getString(1));
                jsonObj.put("ClaUni", rs.getString(2));
                jsonObj.put("Datos", rs.getString(3));
                jsonObj.put("IdReg", rs.getString(4));
                jsonArray.add(jsonObj);

            }
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            jsonObj2 = new JSONObject();
            jsonArray.add(jsonObj2);
            con.cierraConexion();

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public boolean ActualizaREQEnseres(String ClaUni, String ClaPro, int Cantidad, int Idreg, String Obs, int CantidadReq) {
        System.out.println("ClaUni=" + ClaUni + " ClaPro=" + ClaPro + " Cantidad=" + Cantidad);
        boolean save = false;
        int ExiLot = 0;

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psBuscaLote = con.getConn().prepareStatement(ACTUALIZA_REQIdEnseres);
            psBuscaLote.setInt(1, Cantidad);
            psBuscaLote.setInt(2, CantidadReq);
            psBuscaLote.setString(3, Obs);
            psBuscaLote.setString(4, ClaPro);
            psBuscaLote.setString(5, ClaUni);
            psBuscaLote.setInt(6, Idreg);
            System.out.println("ActualizaReq=" + psBuscaLote);
            psBuscaLote.execute();
            psBuscaLote.clearParameters();
            psBuscaLote.close();
            psBuscaLote = null;
            save = true;
            con.getConn().commit();
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;

    }

    @Override
    public boolean RegistrarFoliosEnseres(String ClaUnidad, String FecEnt, String Usuario, String Observaciones, int Proyecto) {
        boolean save = false;
        int UbicaModu = 0, piezas = 0, F_Solicitado = 0, ContarV = 0, Existencia = 0, FolioFactura = 0, DifeSol = 0, FolioLote = 0;
        String UbicaDesc = "", Clave = "", Unidad = "", Unidad2 = "", UbicaLote = "", Contrato = "", Ubicaciones = "", UbicaNofacturar = "", UbicacionesTemp = "";

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            String CampoI = "";

            if (Proyecto == 2) {
                CampoI = "F_IndFactPEMICH";
            } else if ((Proyecto == 1) || (Proyecto == 4) || (Proyecto == 5) || (Proyecto == 6) || (Proyecto == 7)) {
                CampoI = "F_IndFactPEISEM";
            }

            psActualizaIndice = con.getConn().prepareStatement(String.format(ACTUALIZA_INDICEFACTEnseres, CampoI));
            psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTEEnseres);
            psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTOEnseres);
            psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURAEnseres);
            psActualizaReq = con.getConn().prepareStatement(ACTUALIZA_STSREQEnseres);

            psBuscaUnidad = con.getConn().prepareStatement(String.format(BUSCA_UNIDADESEnseres, ClaUnidad));
            rsBuscaUnidad = psBuscaUnidad.executeQuery();
            while (rsBuscaUnidad.next()) {
                Unidad = rsBuscaUnidad.getString(1);
                Unidad2 = rsBuscaUnidad.getString(1);
                Unidad = "'" + Unidad + "'";
                psBuscaIndice = con.getConn().prepareStatement(String.format(BUSCA_INDICEFACTEnseres, CampoI));
                rsIndice = psBuscaIndice.executeQuery();
                if (rsIndice.next()) {
                    FolioFactura = rsIndice.getInt(1);
                }

                psActualizaIndice.setInt(1, FolioFactura + 1);
                psActualizaIndice.addBatch();

                psBuscaDatosFact = con.getConn().prepareStatement(String.format(BUSCA_DATOSporFACTURAREnseres, Unidad));
                rsBuscaDatosFact = psBuscaDatosFact.executeQuery();
                while (rsBuscaDatosFact.next()) {
                    Clave = rsBuscaDatosFact.getString(2);
                    piezas = rsBuscaDatosFact.getInt(3);
                    F_Solicitado = rsBuscaDatosFact.getInt(5);
                    Existencia = rsBuscaDatosFact.getInt(8);
                    FolioLote = rsBuscaDatosFact.getInt(2);
                    Observaciones = rsBuscaDatosFact.getString(9);

                    if ((piezas > 0) && (Existencia > 0)) {

                        int F_IdLote = 0, F_FolLot = 0, Tipo = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0;
                        int Facturado = 0, Contar = 0;
                        String Ubicacion = "";
                        double Costo = 0.0, IVA = 0.0, IVAPro = 0.0, Monto = 0.0, MontoIva = 0.0;

                        psContarReg = con.getConn().prepareStatement(BUSCA_EXITFOLUBIEnseres);
                        psContarReg.setString(1, Clave);
                        psContarReg.setString(2, Clave);
                        rsContarReg = psContarReg.executeQuery();
                        while (rsContarReg.next()) {
                            Contar++;
                        }

                        psBuscaExiFol = con.getConn().prepareStatement(BUSCA_EXITFOLUBIEnseres);
                        psBuscaExiFol.setString(1, Clave);
                        psBuscaExiFol.setString(2, Clave);

                        System.out.println("BuscaExistenciaDetalle=" + psBuscaExiFol);
                        rsBuscaExiFol = psBuscaExiFol.executeQuery();
                        while (rsBuscaExiFol.next()) {
                            F_IdLote = rsBuscaExiFol.getInt(1);
                            F_ExiLot = rsBuscaExiFol.getInt(3);
                            F_FolLot = rsBuscaExiFol.getInt(1);

                            if ((F_ExiLot >= piezas) && (piezas > 0)) {
                                Contar = Contar - 1;
                                diferencia = F_ExiLot - piezas;
                                CanSur = piezas;

                                psActualizaLote.setInt(1, diferencia);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setString(5, "-1");
                                psInsertarMov.setString(6, Usuario);
                                System.out.println("Mov1" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setString(1, Usuario);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setString(4, FecEnt);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setInt(6, FolioFactura);
                                psInsertarFact.setString(7, Observaciones);
                                psInsertarFact.setInt(8, Proyecto);

                                //psInsertarFact.setInt(4, F_Solicitado);
                                System.out.println("fact1" + psInsertarFact);
                                psInsertarFact.addBatch();

                                piezas = 0;
                                F_Solicitado = 0;
                                break;

                            } else if ((piezas > 0) && (F_ExiLot > 0)) {
                                Contar = Contar - 1;
                                diferencia = piezas - F_ExiLot;
                                CanSur = F_ExiLot;
                                if (F_ExiLot >= F_Solicitado) {
                                    DifeSol = F_Solicitado;
                                } else if (Contar > 0) {
                                    DifeSol = F_ExiLot;
                                } else {
                                    DifeSol = F_Solicitado - F_ExiLot;
                                }

                                psActualizaLote.setInt(1, 0);
                                psActualizaLote.setInt(2, F_IdLote);
                                System.out.println("ActualizaLote2=" + psActualizaLote + " Clave=" + Clave);
                                psActualizaLote.addBatch();

                                psInsertarMov.setInt(1, FolioFactura);
                                psInsertarMov.setInt(2, 51);
                                psInsertarMov.setString(3, Clave);
                                psInsertarMov.setInt(4, CanSur);
                                psInsertarMov.setString(5, "-1");
                                psInsertarMov.setString(6, Usuario);
                                System.out.println("Mov2" + psInsertarMov);
                                psInsertarMov.addBatch();

                                psInsertarFact.setString(1, Usuario);
                                psInsertarFact.setString(2, Unidad2);
                                psInsertarFact.setString(3, Clave);
                                psInsertarFact.setString(4, FecEnt);
                                psInsertarFact.setInt(5, CanSur);
                                psInsertarFact.setInt(6, FolioFactura);
                                psInsertarFact.setString(7, Observaciones);
                                psInsertarFact.setInt(8, Proyecto);
                                System.out.println("fact2" + psInsertarFact);
                                psInsertarFact.addBatch();

                                F_Solicitado = F_Solicitado - CanSur;

                                piezas = piezas - CanSur;
                                F_ExiLot = 0;

                            }
                        }

                    }

                }

                psActualizaReq.setString(1, Unidad2);
                psActualizaReq.addBatch();;

                psActualizaIndice.executeBatch();
                psActualizaLote.executeBatch();

                psInsertarMov.executeBatch();
                psInsertarFact.executeBatch();
                psActualizaReq.executeBatch();

                save = true;
                con.getConn().commit();
                System.out.println("Terminó Unidad= " + Unidad + " Con el Folio= " + FolioFactura);

            }
            psBuscaExiFol.close();
            psBuscaExiFol = null;
            rsBuscaExiFol.close();
            rsBuscaExiFol = null;
            psBuscaDatosFact.close();
            psBuscaDatosFact = null;
            rsBuscaDatosFact.close();
            rsBuscaDatosFact = null;
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class
                    .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();

            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(FacturacionTranDaoImpl.class
                        .getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    /**
     * ********
     */
    /*picking mich*/
    private void crearFolio(List<DetalleFactura> detalles, String ubicaDesc, String catalogo, String usuario, int folioFactura, String unidad2, String tipos, String observaciones) throws Exception {
        if (detalles.isEmpty()) {
            return;
        }
        int NoObFact = 0;
        psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
        psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
        psInsertarMov = con.getConn().prepareStatement(INSERTA_MOVIMIENTO);
        int proyecto = 0;
        ApartadoDAOImpl apartadoDAO = new ApartadoDAOImpl(con.getConn());
        FolioStatusDAOImpl stDao = new FolioStatusDAOImpl(this.con.getConn());

        for (DetalleFactura detalle : detalles) {
            String clave = detalle.getClave();
            int folioLote = detalle.getFolioLote();
            int piezas = detalle.getPiezas();
            String ubicaLote = detalle.getUbicaLote();
            int proyectoSelect = detalle.getProyectoSelect();
            proyecto = proyectoSelect;
            double costo = detalle.getCosto();
            double monto = detalle.getMonto();
            double iva = detalle.getIva();
            String fecEnt = detalle.getFecEnt();
            String contratoSelect = detalle.getContratoSelect();
            String oc = detalle.getOc();
            int solicitado = detalle.getSolicitado();
            int F_IdLote = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0, OrigenGC =0;

            System.out.println("Folio= " + folioFactura + " Clave: " + clave);
            psBuscaExiFol = con.getConn().prepareStatement(BUSCA_EXILOTE);
            psBuscaExiFol.setString(1, clave);
            psBuscaExiFol.setInt(2, folioLote);
            psBuscaExiFol.setString(3, ubicaLote);
            psBuscaExiFol.setInt(4, proyectoSelect);

            System.out.println("BuscaExistenciaDetalle=" + psBuscaExiFol);
            rsBuscaExiFol = psBuscaExiFol.executeQuery();
            while (rsBuscaExiFol.next()) {
                F_IdLote = rsBuscaExiFol.getInt(1);
                F_ExiLot = rsBuscaExiFol.getInt(2);
                ClaProve = rsBuscaExiFol.getInt(3);
                OrigenGC = rsBuscaExiFol.getInt(4);

                if ((F_ExiLot >= piezas) && (piezas > 0)) {
                    diferencia = F_ExiLot - piezas;
                    CanSur = piezas;

                    psActualizaLote.setInt(1, diferencia);
                    psActualizaLote.setInt(2, F_IdLote);
                    System.out.println("ActualizaLote=" + psActualizaLote + " Clave=" + clave);
                    psActualizaLote.addBatch();
                    // INSERTAR EN APARTADO
                    Apartado a = new Apartado();
                    a.setId(0);
                    a.setIdLote(F_IdLote);
                    a.setCant(CanSur);
                    a.setStatus(2);
                    a.setProyecto(proyectoSelect);
                    a.setClaDoc(folioFactura + "");
                    apartadoDAO.guardar(a);

                    Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + usuario + ", clave = " + clave + ", folio = " + folioFactura + ", ubicacion = " + ubicaLote + ", piezas = " + CanSur + " ");
                    psInsertarMov.setInt(1, folioFactura);
                    psInsertarMov.setInt(2, 51);
                    psInsertarMov.setString(3, clave);
                    psInsertarMov.setInt(4, CanSur);
                    psInsertarMov.setDouble(5, costo);
                    psInsertarMov.setDouble(6, monto);
                    psInsertarMov.setString(7, "-1");
                    psInsertarMov.setInt(8, folioLote);
                    psInsertarMov.setString(9, ubicaLote);
                    psInsertarMov.setInt(10, ClaProve);
                    psInsertarMov.setString(11, usuario);
                    psInsertarMov.addBatch();

                    psInsertarFact.setInt(1, folioFactura);
                    psInsertarFact.setString(2, unidad2);
                    psInsertarFact.setString(3, clave);
                    psInsertarFact.setInt(4, solicitado);
                    psInsertarFact.setInt(5, CanSur);
                    psInsertarFact.setDouble(6, costo);
                    psInsertarFact.setDouble(7, iva);
                    psInsertarFact.setDouble(8, monto);
                    psInsertarFact.setInt(9, folioLote);
                    psInsertarFact.setString(10, fecEnt);
                    psInsertarFact.setString(11, usuario);
                    psInsertarFact.setString(12, ubicaLote);
                    psInsertarFact.setInt(13, proyectoSelect);
                    psInsertarFact.setString(14, contratoSelect);
                    psInsertarFact.setString(15, oc);
                    psInsertarFact.setInt(16, 0);
                    System.out.println("fact1" + psInsertarFact);
                    psInsertarFact.addBatch();

                         System.out.println("el origen en crearfolio es: "+OrigenGC);
                    if (OrigenGC  == 19) {
                        System.out.println("elorigen es: "+OrigenGC);
                        ///ACTUALIZA GASTOS CATASTROFICOS FACTURACION AUTOMATICA
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setInt(1, folioLote);
                        psActualizaGastos.setString(2, unidad2);
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - CanSur));
                        psActualizaGastos.setInt(2, (CanSur + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
                    }
 
                    piezas = 0;
                    solicitado = 0;
                    break;

                } else if ((piezas > 0) && (F_ExiLot > 0)) {
                    System.out.println("entro cuando es menor lo disponible");
                    diferencia = piezas - F_ExiLot;
                    CanSur = F_ExiLot;

                    psActualizaLote.setInt(1, 0);
                    psActualizaLote.setInt(2, F_IdLote);

                    Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + usuario + ", clave = " + clave + ", folio = " + folioFactura + ", ubicacion = " + ubicaLote + ", piezas = " + CanSur + " ");

                    psActualizaLote.addBatch();
                    psInsertarMov.setInt(1, folioFactura);
                    psInsertarMov.setInt(2, 51);
                    psInsertarMov.setString(3, clave);
                    psInsertarMov.setInt(4, CanSur);
                    psInsertarMov.setDouble(5, costo);
                    psInsertarMov.setDouble(6, monto);
                    psInsertarMov.setString(7, "-1");
                    psInsertarMov.setInt(8, folioLote);
                    psInsertarMov.setString(9, ubicaLote);
                    psInsertarMov.setInt(10, ClaProve);
                    psInsertarMov.setString(11, usuario);

                    psInsertarMov.addBatch();
                    Apartado a = new Apartado();
                    a.setId(0);
                    a.setIdLote(F_IdLote);
                    a.setCant(CanSur);
                    a.setStatus(2);
                    a.setProyecto(proyectoSelect);
                    a.setClaDoc(folioFactura + "");
                    apartadoDAO.guardar(a);//ERROR

                    psInsertarFact.setInt(1, folioFactura);
                    psInsertarFact.setString(2, unidad2);
                    psInsertarFact.setString(3, clave);
                    psInsertarFact.setInt(4, solicitado);
                    psInsertarFact.setInt(5, CanSur);
                    psInsertarFact.setDouble(6, costo);
                    psInsertarFact.setDouble(7, iva);
                    psInsertarFact.setDouble(8, monto);
                    psInsertarFact.setInt(9, folioLote);
                    psInsertarFact.setString(10, fecEnt);
                    psInsertarFact.setString(11, usuario);
                    psInsertarFact.setString(12, ubicaLote);
                    psInsertarFact.setInt(13, proyectoSelect);
                    psInsertarFact.setString(14, contratoSelect);
                    psInsertarFact.setString(15, oc);
                    psInsertarFact.setInt(16, 0);
                    System.out.println("fact2" + psInsertarFact);
                    psInsertarFact.addBatch();

                    solicitado = solicitado - CanSur;

                    piezas = piezas - CanSur;
                    F_ExiLot = 0;
 ///ACTUALIZA GASTOS CATASTROFICOS AUTOMATICA
                        if (OrigenGC  == 19) {
                       
                        System.out.println("Gastos Catastroficos");
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setInt(1, folioLote);
                        psActualizaGastos.setString(2, unidad2);

                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - CanSur));
                        psActualizaGastos.setInt(2, (CanSur + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
}

//cuando no hay existencia

                } else if ((piezas == 0) && (F_ExiLot == 0)) {

                    psInsertarFact.setInt(1, folioFactura);
                    psInsertarFact.setString(2, unidad2);
                    psInsertarFact.setString(3, clave);
                    psInsertarFact.setInt(4, solicitado);
                    psInsertarFact.setInt(5, 0);
                    psInsertarFact.setDouble(6, 0.00);
                    psInsertarFact.setDouble(7, 0.00);
                    psInsertarFact.setDouble(8, 0.00);
                    psInsertarFact.setInt(9, folioLote);
                    psInsertarFact.setString(10, fecEnt);
                    psInsertarFact.setString(11, usuario);
                    psInsertarFact.setString(12, ubicaLote);
                    psInsertarFact.setInt(13, proyectoSelect);
                    psInsertarFact.setString(14, contratoSelect);
                    psInsertarFact.setString(15, oc);
                    psInsertarFact.setInt(16, 0);
                    System.out.println("fact2" + psInsertarFact);
                    psInsertarFact.addBatch();
                }

            }

        }

        psInsertarFact.executeBatch();
        psInsertarFact.clearBatch();
        psInsertarMov.executeBatch();
        psInsertarMov.clearBatch();
        psActualizaLote.executeBatch();
        psActualizaLote.clearBatch();

        /*INSERTA_OBSFACTURA crear folios*/
        psBuscaObsFact = con.getConn().prepareStatement(BUSCA_OBSERVACIONES);
        psBuscaObsFact.setInt(1, folioFactura);
        psBuscaObsFact.setInt(2, proyecto);
        rsBuscaObsFact = psBuscaObsFact.executeQuery();
        System.out.println(psBuscaObsFact);
        if (rsBuscaObsFact.next()) {
            NoObFact = rsBuscaObsFact.getInt(1);
        }
        System.out.println(NoObFact);
        if (NoObFact == 0) {
            psInsertarObs = con.getConn().prepareStatement(INSERTA_OBSFACTURA);
            psInsertarObs.setInt(1, folioFactura);
            psInsertarObs.setString(2, observaciones);
            psInsertarObs.setString(3, tipos);
            psInsertarObs.setInt(4, proyecto);
            psInsertarObs.execute();
////         psInsertarObs.close();
        }
//        psInsertarObs.clearParameters();
//        psInsertarObs.close();
        /*INSERTA_OBSFACTURA crear folios*/
        FolioStatus st = new FolioStatus();
        st.setId(0);
        st.setClaDoc(folioFactura);
        st.setProyecto(proyecto);
        st.setStatus(2);

//          int Cuenta = 0;
//                
//                psUbicaCrossdock = con.getConn().prepareStatement(ValidaAbasto);
//                psUbicaCrossdock.setInt(1, proyecto);
//                psUbicaCrossdock.setInt(2, folioFactura);
//                rsUbicaCross = psUbicaCrossdock.executeQuery();
//                if (rsUbicaCross.next()) {
//                    Cuenta = rsUbicaCross.getInt(1);
//                }
//
//                psUbicaCrossdock.clearParameters();
//
//                if (Cuenta == 0) {
        psUbicaCrossdock = con.getConn().prepareStatement(queryElimina);
        psUbicaCrossdock.setInt(1, proyecto);
        psUbicaCrossdock.setInt(2, folioFactura);
        psUbicaCrossdock.execute();

        psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

        psAbasto = con.getConn().prepareStatement(DatosAbasto);
        psAbasto.setInt(1, proyecto);
        psAbasto.setInt(2, folioFactura);
        rsAbasto = psAbasto.executeQuery();
        while (rsAbasto.next()) {
            int factorEmpaque = 1;
            int folLot = rsAbasto.getInt("LOTE");
            PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
            psfe.setInt(1, folLot);
            ResultSet rsfe = psfe.executeQuery();
            if (rsfe.next()) {
                factorEmpaque = rsfe.getInt("factor");
            }

            psAbastoInsert.setString(1, rsAbasto.getString(1));
            psAbastoInsert.setString(2, rsAbasto.getString(2));
            psAbastoInsert.setString(3, rsAbasto.getString(3));
            psAbastoInsert.setString(4, rsAbasto.getString(4));
            psAbastoInsert.setString(5, rsAbasto.getString(5));
            psAbastoInsert.setString(6, rsAbasto.getString(6));
            psAbastoInsert.setString(7, rsAbasto.getString(7));
            psAbastoInsert.setString(8, rsAbasto.getString(8));
            psAbastoInsert.setString(9, rsAbasto.getString(12));
            psAbastoInsert.setString(10, rsAbasto.getString(10));
            psAbastoInsert.setString(11, usuario);
            psAbastoInsert.setInt(12, factorEmpaque);
            System.out.println("psAbastoInsert abatocreafolio :" + psAbastoInsert);
            psAbastoInsert.addBatch();
        }

        psAbastoInsert.executeBatch();
//                }else{
//                   String mensaje = "Folio ya fue cargado";
//                  
//                }

//        stDao.guardar(st);
    }



/*prefolio*/
    private void crearPrefolio(List<DetalleFactura> detalles, String ubicaDesc, String catalogo, String usuario, int folioFactura, String unidad2, String tipos, String observaciones) throws Exception {
        if (detalles.isEmpty()) {
            return;
        }
        int NoObFact = 0;
        psActualizaLote = con.getConn().prepareStatement(ACTUALIZA_EXILOTE);
        psInsertarFact = con.getConn().prepareStatement(INSERTA_FACTURA);
        int proyecto = 0;
        ApartadoDAOImpl apartadoDAO = new ApartadoDAOImpl(con.getConn());
        FolioStatusDAOImpl stDao = new FolioStatusDAOImpl(this.con.getConn());

        for (DetalleFactura detalle : detalles) {
            String clave = detalle.getClave();
            int folioLote = detalle.getFolioLote();
            int piezas = detalle.getPiezas();
            String ubicaLote = detalle.getUbicaLote();
            int proyectoSelect = detalle.getProyectoSelect();
            proyecto = proyectoSelect;
            double costo = detalle.getCosto();
            double monto = detalle.getMonto();
            double iva = detalle.getIva();
            String fecEnt = detalle.getFecEnt();
            String contratoSelect = detalle.getContratoSelect();
            String oc = detalle.getOc();
            int solicitado = detalle.getSolicitado();
            int F_IdLote = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0,OrigenGC = 0;
            System.out.println("Folio= " + folioFactura + " Clave: " + clave);

            if ((piezas == 0)) {
                psInsertarFact.setInt(1, folioFactura);
                psInsertarFact.setString(2, unidad2);
                psInsertarFact.setString(3, clave);
                psInsertarFact.setInt(4, solicitado);
                psInsertarFact.setInt(5, 0);
                psInsertarFact.setDouble(6, 0.00);
                psInsertarFact.setDouble(7, 0.00);
                psInsertarFact.setDouble(8, 0.00);
                psInsertarFact.setInt(9, folioLote);
                psInsertarFact.setString(10, fecEnt);
                psInsertarFact.setString(11, usuario);
                psInsertarFact.setString(12, ubicaLote);
                psInsertarFact.setInt(13, proyectoSelect);
                psInsertarFact.setString(14, contratoSelect);
                psInsertarFact.setString(15, oc);
                psInsertarFact.setInt(16, 0);
                System.out.println("fact2" + psInsertarFact);
                psInsertarFact.addBatch();
                continue;
            }

            psBuscaExiFol = con.getConn().prepareStatement(BUSCA_EXILOTE);
            psBuscaExiFol.setString(1, clave);
            psBuscaExiFol.setInt(2, folioLote);
            psBuscaExiFol.setString(3, ubicaLote);
            psBuscaExiFol.setInt(4, proyectoSelect);

            System.out.println("BuscaExistenciaDetalle=" + psBuscaExiFol);
            rsBuscaExiFol = psBuscaExiFol.executeQuery();
            while (rsBuscaExiFol.next()) {
                F_IdLote = rsBuscaExiFol.getInt(1);
                F_ExiLot = rsBuscaExiFol.getInt(2);
                ClaProve = rsBuscaExiFol.getInt(3);
                OrigenGC = rsBuscaExiFol.getInt(4);
                boolean fromAF = false;
                if (ubicaLote.equals("AF") || ubicaLote.equals("AF1N") || ubicaLote.equals("AFGC")) {
                    fromAF = true;
                }

                System.out.println("fromAF" + fromAF);
               // boolean surt = F_ExiLot > 0;

                if ((F_ExiLot >= piezas) && (piezas > 0)) {
                    diferencia = F_ExiLot - piezas;
                    CanSur = piezas;

                    Apartado a = new Apartado();
                    a.setId(0);
                    a.setIdLote(F_IdLote);
                    a.setCant(CanSur);
                    a.setStatus(fromAF ? 1 : 2);
                    a.setProyecto(proyectoSelect);
                    a.setClaDoc(folioFactura + "");
                    apartadoDAO.guardar(a);

                    psInsertarFact.setInt(1, folioFactura);
                    psInsertarFact.setString(2, unidad2);
                    psInsertarFact.setString(3, clave);
                    psInsertarFact.setInt(4, solicitado);
                    psInsertarFact.setInt(5, CanSur);
                    psInsertarFact.setDouble(6, costo);
                    psInsertarFact.setDouble(7, iva);
                    psInsertarFact.setDouble(8, monto);
                    psInsertarFact.setInt(9, folioLote);
                    psInsertarFact.setString(10, fecEnt);
                    psInsertarFact.setString(11, usuario);
                    psInsertarFact.setString(12, ubicaLote);
                    psInsertarFact.setInt(13, proyectoSelect);
                    psInsertarFact.setString(14, contratoSelect);
                    psInsertarFact.setString(15, oc);
                    psInsertarFact.setInt(16, 0);
                    System.out.println("fact crearPrefolio:" + psInsertarFact);
                    psInsertarFact.addBatch();

                    System.out.println("Prefolio Origen:"+OrigenGC);
                    if (OrigenGC == 19) {
///ACTUALIZA GASTOS CATASTROFICOS FACTUACION PICKING AF
                        System.out.println("Gastos Catastroficos");
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setInt(1, folioLote);
                        psActualizaGastos.setString(2, unidad2);
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - CanSur));
                        psActualizaGastos.setInt(2, (CanSur + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
                    }
                    piezas = 0;
                    solicitado = 0;

                    if (!fromAF) {
                        psActualizaLote.setInt(1, diferencia);
                        psActualizaLote.setInt(2, F_IdLote);
                        System.out.println("ActualizaLote crearPrefolio=" + psActualizaLote + " Clave=" + clave);
                        psActualizaLote.addBatch();
                        psInsertarMov.setInt(1, folioFactura);
                        psInsertarMov.setInt(2, 51);
                        psInsertarMov.setString(3, clave);
                        psInsertarMov.setInt(4, CanSur);
                        psInsertarMov.setDouble(5, costo);
                        psInsertarMov.setDouble(6, monto);
                        psInsertarMov.setString(7, "-1");
                        psInsertarMov.setInt(8, folioLote);
                        psInsertarMov.setString(9, ubicaLote);
                        psInsertarMov.setInt(10, ClaProve);
                        psInsertarMov.setString(11, usuario);
                        System.out.println("Mov2" + psInsertarMov);
                        psInsertarMov.addBatch();
/*
///ACTUALIZA GASTOS CATASTROFICOS FACTUACION PICKING AF
System.out.println("Gastos Catastroficos");
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setInt(1, folioLote);
                        psActualizaGastos.setString(2, unidad2);
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - CanSur));
                        psActualizaGastos.setInt(2, (CanSur + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

*/
                    }

                    break;

                } else if ((piezas > 0) && (F_ExiLot > 0)) {
                    System.out.println("cuando lo solicitado es mas que lo disponible");
                    diferencia = piezas - F_ExiLot;
                    CanSur = F_ExiLot;
                    if (fromAF) {
                    Apartado a = new Apartado();
                    a.setId(0);
                    a.setIdLote(F_IdLote);
                    a.setCant(CanSur);
                    a.setStatus(fromAF ? 1 : 2);
                    a.setClaDoc(folioFactura + "");
                    apartadoDAO.guardar(a);
}
                    psInsertarFact.setInt(1, folioFactura);
                    psInsertarFact.setString(2, unidad2);
                    psInsertarFact.setString(3, clave);
                    psInsertarFact.setInt(4, solicitado);
                    psInsertarFact.setInt(5, CanSur);
                    psInsertarFact.setDouble(6, costo);
                    psInsertarFact.setDouble(7, iva);
                    psInsertarFact.setDouble(8, monto);
                    psInsertarFact.setInt(9, folioLote);
                    psInsertarFact.setString(10, fecEnt);
                    psInsertarFact.setString(11, usuario);
                    psInsertarFact.setString(12, ubicaLote);
                    psInsertarFact.setInt(13, proyectoSelect);
                    psInsertarFact.setString(14, contratoSelect);
                    psInsertarFact.setString(15, oc);
                    psInsertarFact.setInt(16, 0);
                    System.out.println("fact2" + psInsertarFact);
                    psInsertarFact.addBatch();

                    solicitado = solicitado - CanSur;

                    piezas = piezas - CanSur;
                   // F_ExiLot = 0;
                    if (!fromAF) {
                        psActualizaLote.setInt(1, 0);
                        psActualizaLote.setInt(2, F_IdLote);
                        System.out.println("ActualizaLote2=" + psActualizaLote + " Clave=" + clave);
                        psActualizaLote.addBatch();
                        psInsertarMov.setInt(1, folioFactura);
                        psInsertarMov.setInt(2, 51);
                        psInsertarMov.setString(3, clave);
                        psInsertarMov.setInt(4, CanSur);
                        psInsertarMov.setDouble(5, costo);
                        psInsertarMov.setDouble(6, monto);
                        psInsertarMov.setString(7, "-1");
                        psInsertarMov.setInt(8, folioLote);
                        psInsertarMov.setString(9, ubicaLote);
                        psInsertarMov.setInt(10, ClaProve);
                        psInsertarMov.setString(11, usuario);
                        System.out.println("Mov2" + psInsertarMov);
                        psInsertarMov.addBatch();
/*
///ACTUALIZA GASTOS CATASTROFICOS AUTOMATICA PICKING AF
                        psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                        psActualizaGastos.setInt(1, folioLote);
                        psActualizaGastos.setString(2, unidad2);
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                        ResultSet rsetGastos = psActualizaGastos.executeQuery();
                        while (rsetGastos.next()) {
                            idGastos = rsetGastos.getInt(1);
                            totalGastos = rsetGastos.getInt(2);
                            remiGastos = rsetGastos.getInt(3);
                        }
                        System.out.println(" psAbastoInsert psActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.execute();
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();

                        psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                        psActualizaGastos.setInt(1, (totalGastos - CanSur));
                        psActualizaGastos.setInt(2, (CanSur + remiGastos));
                        psActualizaGastos.setInt(3, idGastos);
                        psActualizaGastos.execute();
                        System.out.println(" psAbastoInsert ActualizaGastos: " + psActualizaGastos);
                        psActualizaGastos.clearParameters();
                        psActualizaGastos.close();
*/
                    }

                }
            }

        }

        psInsertarFact.executeBatch();
        psInsertarFact.clearBatch();
        psInsertarMov.executeBatch();
        psInsertarMov.clearBatch();
        psActualizaLote.executeBatch();
        psActualizaLote.clearBatch();

        /*INSERTA_OBSFACTURA crear folios*/
        psBuscaObsFact = con.getConn().prepareStatement(BUSCA_OBSERVACIONES);
        psBuscaObsFact.setInt(1, folioFactura);
        psBuscaObsFact.setInt(2, proyecto);
        rsBuscaObsFact = psBuscaObsFact.executeQuery();
        System.out.println(psBuscaObsFact);
        if (rsBuscaObsFact.next()) {
            NoObFact = rsBuscaObsFact.getInt(1);
        }
        System.out.println("numero de obs crearPrefolio: " + NoObFact);
        if (NoObFact == 0) {
            psInsertarObs = con.getConn().prepareStatement(INSERTA_OBSFACTURA);
            psInsertarObs.setInt(1, folioFactura);
            psInsertarObs.setString(2, observaciones);
            psInsertarObs.setString(3, tipos);
            psInsertarObs.setInt(4, proyecto);
            psInsertarObs.execute();
//        psInsertarObs.close();
        }

//        psInsertarObs.clearParameters();
//        psInsertarObs.close();
/*INSERTA_OBSFACTURA crear folios*/
        FolioStatus st = new FolioStatus();
        st.setId(0);
        st.setClaDoc(folioFactura);
        st.setProyecto(proyecto);
        st.setStatus(1);

        int Cuenta = 0;
//                
//                psUbicaCrossdock = con.getConn().prepareStatement(ValidaAbasto);
//                psUbicaCrossdock.setInt(1, proyecto);
//                psUbicaCrossdock.setInt(2, folioFactura);
//                rsUbicaCross = psUbicaCrossdock.executeQuery();
//                if (rsUbicaCross.next()) {
//                    Cuenta = rsUbicaCross.getInt(1);
//                }
//
//                psUbicaCrossdock.clearParameters();

//                if (Cuenta == 0) {
        psUbicaCrossdock = con.getConn().prepareStatement(queryElimina);
        psUbicaCrossdock.setInt(1, proyecto);
        psUbicaCrossdock.setInt(2, folioFactura);
        psUbicaCrossdock.execute();

        psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

        psAbasto = con.getConn().prepareStatement(DatosAbasto);
        psAbasto.setInt(1, proyecto);
        psAbasto.setInt(2, folioFactura);
        rsAbasto = psAbasto.executeQuery();
        while (rsAbasto.next()) {
            int factorEmpaque = 1;
            int folLot = rsAbasto.getInt("LOTE");
            PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
            psfe.setInt(1, folLot);
            ResultSet rsfe = psfe.executeQuery();
            if (rsfe.next()) {
                factorEmpaque = rsfe.getInt("factor");
            }

            psAbastoInsert.setString(1, rsAbasto.getString(1));
            psAbastoInsert.setString(2, rsAbasto.getString(2));
            psAbastoInsert.setString(3, rsAbasto.getString(3));
            psAbastoInsert.setString(4, rsAbasto.getString(4));
            psAbastoInsert.setString(5, rsAbasto.getString(5));
            psAbastoInsert.setString(6, rsAbasto.getString(6));
            psAbastoInsert.setString(7, rsAbasto.getString(7));
            psAbastoInsert.setString(8, rsAbasto.getString(8));
            psAbastoInsert.setString(9, rsAbasto.getString(12));
            psAbastoInsert.setString(10, rsAbasto.getString(10));
            psAbastoInsert.setString(11, usuario);
            psAbastoInsert.setInt(12, factorEmpaque);
            System.out.println("psAbastoInsert ConfirmarFactTempFOLIO:" + psAbastoInsert);
            psAbastoInsert.addBatch();
        }

        psAbastoInsert.executeBatch();
//                }else{
//                   String mensaje = "Folio ya fue cargado";
//                  
//                }
//        stDao.guardar(st);
    }

    /**
     * ****
     */
    private Integer getExistenciaByClave(String clave, Integer proyecto, String ubicaciones) throws SQLException {
        String query = "select SUM(F_ExiLot)- SUM(ifnull(apartado,0))  from tb_lote l left join apartado_concentrado c on c.F_IdLote = l.F_IdLote " + ubicaciones + " AND F_ClaPro = '" + clave + "' AND F_Proyecto = " + proyecto + " group by F_ClaPro";
        ResultSet rs = this.con.consulta(query);
        while (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private boolean sobreescribeFactura(DetalleFactura detalle, String folioFactura) throws SQLException {
        PreparedStatement ps = this.con.getConn().prepareStatement(LOTE_EN_FOLIO);
        System.out.println("LOTE_EN_FOLIO: "+LOTE_EN_FOLIO);
        ps.setString(1, folioFactura);
        ps.setInt(2, detalle.getFolioLote());
        ps.setString(3, detalle.getUbicaLote());
        ps.setInt(4, detalle.getProyectoSelect());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Integer idFact = rs.getInt("F_IdFact");
            Integer req = rs.getInt("F_CantReq");
            Integer sur = rs.getInt("F_CantSur");
            ps = this.con.getConn().prepareStatement(CAMBIA_CANT_FACT);
            System.out.println("solicitado: "+CAMBIA_CANT_FACT);
            String clacli = rs.getString("F_ClaCli");
            ps.setInt(1, req + detalle.getSolicitado());
            ps.setInt(2, sur + detalle.getPiezas());
            ps.setInt(3, idFact);
            ps.executeUpdate();

            String findIdLote = "SELECT F_IdLote, F_ClaPrv, F_ExiLot, F_Origen from tb_lote where F_FolLot = ? AND F_Ubica = ? AND F_Proyecto = ?";
            System.out.println(findIdLote);
            Integer idLote = 0,claProve = 0,exiLot = 0, OrigenGC = 0;
            ps = this.con.getConn().prepareStatement(findIdLote);
            ps.setInt(1, detalle.getFolioLote());
            ps.setString(2, detalle.getUbicaLote());
            ps.setInt(3, detalle.getProyectoSelect());
            ResultSet rsIdLote = ps.executeQuery();
            rsIdLote.next();
            idLote = rsIdLote.getInt("F_IdLote");
            claProve = rsIdLote.getInt("F_ClaPrv");
            exiLot = rsIdLote.getInt("F_ExiLot");
            OrigenGC = rsIdLote.getInt("F_Origen");

            ApartadoDAOImpl dao = new ApartadoDAOImpl(this.con.getConn());
            Apartado a = dao.getByIdLoteAndClaDoc(idLote, folioFactura);
            if (a != null) {
                dao.updateCant(a.getId(), a.getCant() + detalle.getPiezas());
            }
            if (!((detalle.getUbicaLote().equals("AF") || detalle.getUbicaLote().equals("AF1N") || detalle.getUbicaLote().equals("AFGC")) && detalle.getProyectoSelect() == 2)) {
                psInsertarMov.setInt(1, Integer.parseInt(folioFactura));
                psInsertarMov.setInt(2, 51);
                psInsertarMov.setString(3, detalle.getClave());
                psInsertarMov.setInt(4, detalle.getPiezas());
                psInsertarMov.setDouble(5, detalle.getCosto());
                psInsertarMov.setDouble(6, detalle.getCosto() * detalle.getPiezas());
                psInsertarMov.setString(7, "-1");
                psInsertarMov.setInt(8, detalle.getFolioLote());
                psInsertarMov.setString(9, detalle.getUbicaLote());
                psInsertarMov.setInt(10, claProve);
                psInsertarMov.setString(11, detalle.getUsuario());
                psInsertarMov.addBatch();
                psInsertarMov.executeBatch();
                psInsertarMov.clearBatch();

                /*Actualiza lote*/
                ps = this.con.getConn().prepareStatement("UPDATE tb_lote SET F_ExiLot = " + (exiLot - detalle.getPiezas()) + " WHERE F_IdLote= " + idLote);

                ps.executeUpdate();

               
            }

            psUbicaCrossdock = con.getConn().prepareStatement(queryElimina);
            psUbicaCrossdock.setInt(1, detalle.getProyectoSelect());
            psUbicaCrossdock.setString(2, folioFactura);
            psUbicaCrossdock.execute();

            if (OrigenGC == 19) {
                    ///ACTUALIZA GASTOS CATASTROFICOS SOBRESCRIBIR
                    System.out.println("ACTUALIZA GASTOS CATASTROFICOS SOBRESCRIBIR");
                    psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                    psActualizaGastos.setInt(1, detalle.getFolioLote());
                    psActualizaGastos.setString(2, clacli);
                    Integer idGastos = 0, totalGastos = 0, remiGastos = 0, Piezas=0;

                    ResultSet rsetGastos = psActualizaGastos.executeQuery();
                    Piezas =detalle.getPiezas();
                    while (rsetGastos.next()) {
                        idGastos = rsetGastos.getInt(1);
                        totalGastos = rsetGastos.getInt(2);
                        remiGastos = (rsetGastos.getInt(3));

                    }
                System.out.println("idGastos: "+idGastos+" totalGastos: "+totalGastos+" remiGastos:"+remiGastos+" Piezas:"+Piezas);

                    psActualizaGastos.execute();
                    psActualizaGastos.clearParameters();
                    psActualizaGastos.close();

                    psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);
                        totalGastos = (totalGastos - Piezas);
                        remiGastos = (Piezas + remiGastos);
                System.out.println("idGastos: "+idGastos+" totalGastos: "+totalGastos+" remiGastos:"+remiGastos);

                    psActualizaGastos.setInt(3, idGastos);
                    psActualizaGastos.setInt(2, remiGastos);
                    psActualizaGastos.setInt(1, totalGastos);
                    psActualizaGastos.execute();
 System.out.println("ACTUALIZA_GASTOS sobrescritura:" + ACTUALIZA_GASTOS);
                    psActualizaGastos.clearParameters();
                    psActualizaGastos.close();
                }
            

            psAbastoInsert = con.getConn().prepareStatement(InsertAbasto);

            psAbasto = con.getConn().prepareStatement(DatosAbasto);
            psAbasto.setInt(1, detalle.getProyectoSelect());
            psAbasto.setString(2, folioFactura);
            rsAbasto = psAbasto.executeQuery();
            while (rsAbasto.next()) {
                int factorEmpaque = 1;
                int folLot = rsAbasto.getInt("LOTE");
                PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                psfe.setInt(1, folLot);
                ResultSet rsfe = psfe.executeQuery();
                if (rsfe.next()) {
                    factorEmpaque = rsfe.getInt("factor");
                }

                psAbastoInsert.setString(1, rsAbasto.getString(1));
                psAbastoInsert.setString(2, rsAbasto.getString(2));
                psAbastoInsert.setString(3, rsAbasto.getString(3));
                psAbastoInsert.setString(4, rsAbasto.getString(4));
                psAbastoInsert.setString(5, rsAbasto.getString(5));
                psAbastoInsert.setString(6, rsAbasto.getString(6));
                psAbastoInsert.setString(7, rsAbasto.getString(7));
                psAbastoInsert.setString(8, rsAbasto.getString(8));
                psAbastoInsert.setString(9, rsAbasto.getString(12));
                psAbastoInsert.setString(10, rsAbasto.getString(10));
                psAbastoInsert.setString(11, detalle.getUsuario());
                psAbastoInsert.setInt(12, factorEmpaque);
                System.out.println("psAbastoInsert sobrescritura:" + psAbastoInsert);
                psAbastoInsert.addBatch();
            }
            return true;
        }
        return false;
    }

    private int getParametroUsuario(String usuario, ConectionDBTrans con) throws SQLException {
        String query = "SELECT F_Id from tb_parametrousuario where F_Usuario ='" + usuario + "'";
        ResultSet rs = con.consulta(query);
        while (rs.next()) {
            return rs.getInt("F_Id");
        }
        return 0;
    }

    private boolean crearFolioProvisional(ConectionDBTrans con, String usuario, String claUni, String fecEnt) {
        try {
            List<String> clavesPermitidas = this.fkingNoRestrictionKeys();
            claUni = claUni.replaceAll("\'", "");
            FacturaProvisionalDAO fpdao = new FacturaProvisionalDAO(con.getConn());
            List<FacturaProvisionalDetail> controlado = new ArrayList();
            List<FacturaProvisionalDetail> redfria = new ArrayList();
            List<FacturaProvisionalDetail> normal = new ArrayList();
            FacturaProvisional fp = new FacturaProvisional();
            this.psBuscaDatosFact = con.getConn().prepareStatement(BUSCA_DATOSporFACTURARPROVISIONAL);
            psBuscaDatosFact.setString(1, claUni);
            psBuscaDatosFact.setString(2, usuario);
            ResultSet rsBusca = psBuscaDatosFact.executeQuery();
            if (rsBusca.next()) {
                DetalleFactura detalle = DetalleFactura.buildDetalleFactura(rsBusca);
                fp.setCause("");
                fp.setContrato(detalle.getContratoSelect());
                fp.setFecApl(new Date());
                fp.setFecEnt(Calendario.stringToDate(fecEnt));
                fp.setId(0);
                fp.setOc(detalle.getOc());
                fp.setProyecto(detalle.getProyectoSelect());
                fp.setStatus(1);
                fp.setClaCli(claUni);
            }
            rsBusca.beforeFirst();
            while (rsBusca.next()) {
                DetalleFactura detalle = DetalleFactura.buildDetalleFactura(rsBusca);
                if (!clavesPermitidas.contains(detalle.getClave())) {
                    continue;
                }
                PreparedStatement psExistencia = con.getConn().prepareStatement(BUSCA_EXILOTEU013);
                psExistencia.setString(1, detalle.getClave());
                ResultSet rsExistencia = psExistencia.executeQuery();
                int piezas = detalle.getSolicitado();
                while (rsExistencia.next() && piezas > 0) {
                    FacturaProvisionalDetail detail = new FacturaProvisionalDetail();
                    detail.setId(0);
                    Integer existencia = rsExistencia.getInt("disponible");
                    if (existencia >= piezas) {
                        detail.setCantReq(piezas);
                        detail.setCantSur(piezas);
                        piezas = 0;
                    } else {
                        if (existencia > 0) {
                            if (rsExistencia.isLast()) {
                                detail.setCantReq(piezas);
                            } else {
                                detail.setCantReq(existencia);
                            }
                            detail.setCantSur(existencia);
                            piezas = piezas - existencia;
                        } else {
                            continue;
                        }
                    }
                    detail.setFecApl(new Date());
                    detail.setHora(new Date());
                    detail.setIdLote(rsExistencia.getInt("F_IdLote"));
                    detail.setIva(0d);
                    detail.setMonto(0d);
                    detail.setCost(0d);
                    detail.setStatus(1);
                    detail.setUser(usuario);
                    String ubicacion = rsExistencia.getString("F_Ubica");
                    switch (ubicacion) {
                        case "CONTROLADO": {
                            controlado.add(detail);
                            break;
                        }
                        case "REDFRIA": {
                            redfria.add(detail);
                            break;
                        }
                        default: {
                            normal.add(detail);
                        }
                    }
                }
            }
            if (!controlado.isEmpty()) {
                Integer folio = fpdao.getFolio();
                fp.setFolio(folio);
                fp.setDetails(controlado);
                fp.setId(0);
                fpdao.save(fp);
                fpdao.increaseFolio();
            }
            if (!redfria.isEmpty()) {
                Integer folio = fpdao.getFolio();
                fp.setFolio(folio);
                fp.setDetails(redfria);
                fp.setId(0);
                fpdao.save(fp);
                fpdao.increaseFolio();
            }
            if (!normal.isEmpty()) {
                Integer folio = fpdao.getFolio();
                fp.setFolio(folio);
                fp.setDetails(normal);
                fp.setId(0);
                fpdao.save(fp);
                fpdao.increaseFolio();
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionTranDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static List<String> fkingNoRestrictionKeys() {
        List<String> list = new ArrayList();
        list.add("0104");
        list.add("0106");
        list.add("0108");
        list.add("0402");
        list.add("0408");
        list.add("0429");
        list.add("0472");
        list.add("0572");
        list.add("0599");
        list.add("0657");
        list.add("0804");
        list.add("0813");
        list.add("0891");
        list.add("1006");
        list.add("1007");
        list.add("1042");
        list.add("1206");
        list.add("1224");
        list.add("1242");
        list.add("1243");
        list.add("1308.01");
        list.add("1345");
        list.add("1566");
        list.add("1706");
        list.add("1711");
        list.add("1903");
        list.add("1904");
        list.add("1911");
        list.add("1926");
        list.add("1927");
        list.add("1937");
        list.add("1939");
        list.add("1971");
        list.add("1972");
        list.add("2016");
        list.add("2018");
        list.add("2111.01");
        list.add("2129");
        list.add("2133");
        list.add("2144");
        list.add("2145");
        list.add("2230");
        list.add("2301");
        list.add("2501");
        list.add("2524");
        list.add("2707");
        list.add("3407");
        list.add("3413");
        list.add("3417");
        list.add("3622");
        list.add("3623");
        list.add("4255");
        list.add("4258");
        list.add("4260");
        list.add("4359");
        list.add("5106");
        list.add("5186.01");
        list.add("600040109");
        list.add("600580153");
        list.add("600660039");
        list.add("600660872");
        list.add("600661060");
        list.add("601664279");
        list.add("601664287");
        list.add("603451386");
        list.add("603451394");
        list.add("604560383");
        list.add("604560391");
        list.add("604560409");
        list.add("605501279");
        list.add("608410478");
        list.add("608690152");
        list.add("609080015");
        list.add("609080114");
        list.add("609080122");
        list.add("609080130");

        return list;
    }

    private int surtirMes(String UbicaDesc, String Clave, Integer Proyecto, Integer i, Integer Catalogo, Integer F_Solicitado, Integer cantidad, Integer FolioFactura, String Unidad2, String FecEnt, String Usuario, String Observaciones, String Contrato, String OC) throws SQLException {
        int F_IdLote = 0, F_FolLot = 0, Tipo = 0, F_ExiLot = 0, diferencia = 0, CanSur = 0, ClaProve = 0;
        int Facturado = 0, Contar = 0;
        int DifeSol = 0;
        String Ubicacion = "";
        double Costo = 0.0, IVA = 0.0, IVAPro = 0.0, Monto = 0.0, MontoIva = 0.0;

        String c = String.format(LOTES_DISPONIBLES_MES, UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), UbicaDesc.replace("WHERE F_Ubica", "WHERE L.F_Ubica"), Catalogo);
        System.out.println(c);
        PreparedStatement p = con.getConn().prepareStatement(c);

        p = con.getConn().prepareStatement(c);
        p.setString(1, Clave);
        p.setInt(2, Proyecto);
        p.setInt(3, Proyecto);
        p.setString(4, Clave);
        p.setString(5, fechasIniciales[i]);
        p.setString(6, fechasFinales[i]);

        System.out.println(p);
        rsBuscaExiFol = p.executeQuery();
        if (rsBuscaExiFol.last()) {
            Contar = rsBuscaExiFol.getRow();
            rsBuscaExiFol.beforeFirst();
        }
        while (rsBuscaExiFol.next()) {
            System.out.println("si entre: " + Proyecto);
            F_IdLote = rsBuscaExiFol.getInt(1);
            F_ExiLot = rsBuscaExiFol.getInt(7);
            F_FolLot = rsBuscaExiFol.getInt(3);
            Tipo = rsBuscaExiFol.getInt(4);
            Costo = rsBuscaExiFol.getDouble(5);
            Ubicacion = rsBuscaExiFol.getString(6).trim();
            ClaProve = rsBuscaExiFol.getInt(8);

            if (Tipo == 2504) {
                IVA = 0.0;
            } else {
                IVA = 0.16;
            }

            if (Proyecto != 5) {
                Costo = 0.0;
            }
            System.out.println(Ubicacion);

            if ((F_ExiLot >= cantidad) && (cantidad > 0)) {
                Contar = Contar - 1;
                diferencia = F_ExiLot - cantidad;
                CanSur = cantidad;
                IVAPro = (CanSur * Costo) * IVA;
                Monto = CanSur * Costo;
                MontoIva = Monto + IVAPro;
                psInsertarFactTemp.setInt(1, FolioFactura);
                psInsertarFactTemp.setString(2, Unidad2);
                psInsertarFactTemp.setString(3, Clave);
                psInsertarFactTemp.setInt(4, F_Solicitado);
                psInsertarFactTemp.setInt(5, CanSur);
                psInsertarFactTemp.setDouble(6, Costo);
                psInsertarFactTemp.setDouble(7, IVAPro);
                psInsertarFactTemp.setDouble(8, MontoIva);
                psInsertarFactTemp.setInt(9, F_FolLot);
                psInsertarFactTemp.setString(10, FecEnt);
                psInsertarFactTemp.setString(11, Usuario);
                psInsertarFactTemp.setString(12, Ubicacion);
                psInsertarFactTemp.setString(13, Observaciones);
                psInsertarFactTemp.setInt(14, Proyecto);
                psInsertarFactTemp.setString(15, Contrato);
                psInsertarFactTemp.setString(16, OC);
                psInsertarFactTemp.setInt(17, 0);
                psInsertarFactTemp.addBatch();

                cantidad = 0;
                break;

            } else if ((cantidad > 0) && (F_ExiLot > 0)) {
                Contar = Contar - 1;
                diferencia = cantidad - F_ExiLot;
                CanSur = F_ExiLot;
                if (F_ExiLot >= F_Solicitado) {
                    DifeSol = F_Solicitado;
                } else if (Contar > 0) {
                    DifeSol = F_ExiLot;
                } else {
                    DifeSol = F_Solicitado - F_ExiLot;
                }

                IVAPro = (CanSur * Costo) * IVA;
                Monto = CanSur * Costo;
                MontoIva = Monto + IVAPro;
                System.out.println("");
                psInsertarFactTemp.setInt(1, FolioFactura);
                psInsertarFactTemp.setString(2, Unidad2);
                psInsertarFactTemp.setString(3, Clave);
                psInsertarFactTemp.setInt(4, F_Solicitado);
                psInsertarFactTemp.setInt(5, CanSur);
                psInsertarFactTemp.setDouble(6, Costo);
                psInsertarFactTemp.setDouble(7, IVAPro);
                psInsertarFactTemp.setDouble(8, MontoIva);
                psInsertarFactTemp.setInt(9, F_FolLot);
                psInsertarFactTemp.setString(10, FecEnt);
                psInsertarFactTemp.setString(11, Usuario);
                psInsertarFactTemp.setString(12, Ubicacion);
                psInsertarFactTemp.setString(13, Observaciones);
                psInsertarFactTemp.setInt(14, Proyecto);
                psInsertarFactTemp.setString(15, Contrato);
                psInsertarFactTemp.setString(16, OC);
                psInsertarFactTemp.setInt(17, 0);
                psInsertarFactTemp.addBatch();

                cantidad = cantidad - CanSur;
                F_ExiLot = 0;

            }
            if (Contar == 0) {
                if (F_Solicitado > 0) {
                    psInsertarFactTemp.setInt(1, FolioFactura);
                    psInsertarFactTemp.setString(2, Unidad2);
                    psInsertarFactTemp.setString(3, Clave);
                    psInsertarFactTemp.setInt(4, F_Solicitado);
                    psInsertarFactTemp.setInt(5, 0);
                    psInsertarFactTemp.setDouble(6, Costo);
                    psInsertarFactTemp.setDouble(7, IVAPro);
                    psInsertarFactTemp.setDouble(8, MontoIva);
                    psInsertarFactTemp.setInt(9, F_FolLot);
                    psInsertarFactTemp.setString(10, FecEnt);
                    psInsertarFactTemp.setString(11, Usuario);
                    psInsertarFactTemp.setString(12, Ubicacion);
                    psInsertarFactTemp.setString(13, Observaciones);
                    psInsertarFactTemp.setInt(14, Proyecto);
                    psInsertarFactTemp.setString(15, Contrato);
                    psInsertarFactTemp.setString(16, OC);
                    psInsertarFactTemp.setInt(17, 0);
                    psInsertarFactTemp.addBatch();
                    F_Solicitado = 0;
                }
            }
        }
        return cantidad;
    }

    private boolean folioAsignadoPicking(int FolioFactura, int Proyecto) {
        try {
            PreparedStatement surtidoSt = con.getConn().prepareStatement(IS_ASSIGNED);
            System.out.println(IS_ASSIGNED);
            surtidoSt.setInt(1, FolioFactura);
            surtidoSt.setInt(2, Proyecto);
            ResultSet surtidoRS = surtidoSt.executeQuery();
            if (surtidoRS.next()) {
                Integer status = surtidoRS.getInt("STATUS");
                if (status == 6 || status == 5) {
                    return true;
                }
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar a picking");
            return true;
        }
        return false;
    }

}
