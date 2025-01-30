/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.querys;

/**
 *
 * @author DotorMedalfa
 */
public class FoliosNoOficialesQuerys {
    
     public static final String OBTENER_ENCABEZADO_PROYECTO = "SELECT F_Nomenclatura, F_Encabezado, F_DesProy FROM tb_proyectos WHERE F_Id = 2;";
     
     public static final String OBTENER_CLAVE_UNIDAD = "SELECT F_ClaCli, F_ClaDoc FROM tb_factura WHERE F_ClaDoc = ?";
     
     public static final String BUSCA_TIP_CLAVE = "SELECT m.F_ClaPro, m.F_DesPro, m.F_PrePro, m.F_Costo, CASE  WHEN  r.F_ClaPro = m.F_ClaPro  THEN 'RedFria' WHEN  c.F_ClaPro = m.F_ClaPro  THEN 'Controlado' WHEN  a.F_ClaPro = m.F_ClaPro  THEN 'Ape' WHEN o.F_ClaPro = m.F_ClaPro THEN 'Oncología' else 'Normal' END as StatuAT FROM tb_medica AS m LEFT JOIN tb_redfria AS r ON  m.F_ClaPro = r.F_ClaPro LEFT JOIN tb_controlados AS c ON  m.F_ClaPro = c.F_ClaPro  LEFT JOIN tb_ape AS a ON  m.F_ClaPro = a.F_ClaPro LEFT JOIN tb_onco AS o ON m.F_ClaPro = o.F_ClaPro WHERE m.F_ClaPro = ? LIMIT 1;";
     
     public static final String REMOVE_FOLIO = "DELETE FROM tb_imprefolio WHERE F_Folio = ? ;";
     
     public static final String BUSCA_UNIDAD = "SELECT u.F_NomCli,u.F_Direc, j.F_DesJurIS,m.F_DesMunIS,u.F_Razon FROM tb_uniatn AS u INNER JOIN tb_juriis as j ON u.F_ClaJur = j.F_ClaJurIS INNER JOIN tb_muniis as m ON u.F_ClaMun = m.F_ClaMunIS WHERE u.F_ClaCli = ?"; 
    
     public static final String BUSCA_LOTE = "SELECT l.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_FolLot,l.F_Origen FROM tb_lote AS l WHERE l.F_ClaPro = ?  AND l.F_ClaLot = ? GROUP BY l.F_Origen LIMIT 1";

      public static final String INSERT_IMPFOLIO = "INSERT INTO tb_imprefolio VALUES(?,?,?,?,?,?,?,?,?,?,?,0,0,'ok',?,?,'Reimprime',?,'',0,0,0,'','','','',2,?,?,?,?,?,0)";
     
     /////pARA iNSERT FOLIOS NO OFICIALES. 
      
     public static final String INSERT_FACTAUD = "INSERT INTO tb_factura_auditoria VALUES(0,?,?,?,?,?,?,?,0,0,0,?,?,?,?,?,?,?,2,?,'',0)";  
    
     public static final String BUSCA_FACTAUDCONT = "SELECT COUNT(*) FROM tb_factura AS f WHERE f.F_ClaDoc = ? AND f.F_ClaPro = ? AND f.F_Lote = ?;";  
     
     public static final String BUSCA_FACTAUDLOT = "SELECT f.F_ClaCli, f.F_StsFact, f.F_ClaPro, f.F_CantReq, f.F_FecEnt, f.F_Hora, f.F_User, f.F_Ubicacion, f.F_Obs, f.F_DocAnt, f.F_Contrato FROM tb_factura AS f WHERE f.F_ClaDoc = ? AND f.F_ClaPro = ? AND f.F_Lote = ?;";  
     
     public static final String BUSCA_FACTAUD = "SELECT f.F_ClaCli, f.F_StsFact, f.F_ClaPro, f.F_CantReq, f.F_FecEnt, f.F_Hora, f.F_User, f.F_Ubicacion, f.F_Obs, f.F_DocAnt, f.F_Contrato FROM tb_factura AS f WHERE f.F_ClaDoc = ? AND f.F_ClaPro = ? ;";  
     
      public static final String BUSCA_LOTEAUD = "SELECT l.F_FolLot FROM tb_lote AS l WHERE l.F_ClaPro = ?  AND l.F_ClaLot = ? AND l.F_FecCad = ? AND l.F_Proyecto = 2 GROUP BY l.F_Origen LIMIT 1";

     
}