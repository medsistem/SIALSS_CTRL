/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import Correo.CorreoConfirmaRemision;
import com.gnk.dao.InterfaceSenderoDao;
import com.gnk.model.comprasModel;
import com.gnk.model.marcaProveedor;
import com.gnk.model.pedidosIsemModel;
//import conn.ConectionDB;
import conn.ConectionDBTrans;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class Implementación de InterfaceSenderoDao (ingreso de compra transaccional)
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class InterfaceSendetoDaoImpl implements InterfaceSenderoDao {

    public static String COMPRA_TEMPORAL_LERMA = "SELECT * FROM tb_compratemp WHERE F_OrdCom=? AND F_FolRemi=?;";
    
    public static String PEDIDO_ISEM_2017 = "SELECT * FROM tb_pedido_sialss WHERE F_NoCompra=?";
    
    public static String MARCA_SENDERO = "SELECT F_ClaMar FROM tb_marca WHERE F_ClaMar=?";
    public static String PROVEEDOR_SENDERO = "SELECT F_ClaProve FROM tb_proveedor WHERE F_ClaProve=?";
    public static String INSERT_MARCA = "INSERT INTO tb_marca SET F_ClaMar=?,F_DesMar=?; ";
    public static String INSERT_PROVEEDOR = "INSERT INTO tb_proveedor SET F_ClaProve=?,F_NomPro=?,F_Dir='-',F_Col='-',F_Cp='-',F_Tel='-',F_Rfc='-',F_Fax='-',F_Mail='-',F_Poblacion='-',F_ClaSap='-';";
    public static String DATOS_MARCA_PROVEEDOR = "SELECT c.F_Marca AS claveMarca, m.F_DesMar AS descripMarca, c.F_Provee AS claveProveedor, p.F_NomPro AS nombreProveedor FROM tb_compratemp c, tb_marca m, tb_proveedor p WHERE c.F_OrdCom=? AND c.F_FolRemi=? AND c.F_Marca=m.F_ClaMar AND c.F_Provee=p.F_ClaProve GROUP BY c.F_Marca,c.F_Provee;";
//    public static String UPDATE_COMPRA_TEMPORAL = "UPDATE tb_compratemp SET F_Lote=?, F_FecCad=?, F_Marca=?, F_Pz=?, F_Cb=?, F_Obser=?, F_Tarimas=?, F_Cajas=?, F_PzaCaja=?, F_CajasI=?, F_Resto=?, F_TarimasI=?, F_ComTot = ? WHERE F_IdCom=?";
//    public static String DATOS_COMPRA_TEMPORAL_PARA_EDITAR = "SELECT c.F_Lote AS lote,c.F_Pz AS cantidad,c.F_Cb AS cb,c.F_FecCad AS caducidad,m.F_DesMar AS marca, m.F_ClaMar AS claMar, c.F_Obser as observaciones,F_Tarimas,F_Cajas,F_PzaCaja,F_CajasI,F_Resto,F_TarimasI, F_Costo FROM tb_compratemp c, tb_marca m WHERE c.F_IdCom=? AND c.F_Marca=m.F_ClaMar;";
    public static String UPDATE_COMPRA_TEMPORAL = "UPDATE tb_compratemp SET F_Lote=?, F_FecCad=?, F_Marca=?, F_Pz=?, F_Cb=?, F_Obser=?, F_Tarimas=?, F_Cajas=?, F_PzaCaja=?, F_CajasI=?, F_Resto=?, F_TarimasI=?, F_ComTot = ?, F_FactorEmpaque = ? WHERE F_IdCom=?";
    public static String DATOS_COMPRA_TEMPORAL_PARA_EDITAR = "SELECT c.F_Lote AS lote, c.F_Pz AS cantidad, c.F_Cb AS cb, c.F_FecCad AS caducidad, m.F_DesMar AS marca, m.F_ClaMar AS claMar, c.F_Obser AS observaciones, c.F_Tarimas, c.F_Cajas, c.F_PzaCaja, c.F_CajasI, c.F_Resto, c.F_TarimasI, c.F_Costo, c.F_FactorEmpaque, p.F_Cant AS cantPedido, IFNULL( CO.cantCompra, 0 ) AS cantCompra, IFNULL( COM.cantidadTemp, 0 ) AS cantidadTemp FROM tb_compratemp AS c INNER JOIN tb_marca AS m ON c.F_Marca = m.F_ClaMar LEFT JOIN tb_pedido_sialss AS p ON c.F_OrdCom = p.F_NoCompra AND c.F_ClaPro = p.F_Clave LEFT JOIN (SELECT comptemp.F_IdCom, SUM( com.F_CanCom ) AS cantCompra FROM tb_compratemp AS comptemp INNER JOIN tb_compra AS com ON comptemp.F_ClaPro = com.F_ClaPro AND comptemp.F_OrdCom = com.F_OrdCom WHERE comptemp.F_IdCom = ? GROUP BY com.F_ClaPro) AS CO ON CO.F_IdCom = c.F_IdCom LEFT JOIN (SELECT SUM( ctemp.F_Pz ) AS cantidadTemp, ctemp.F_OrdCom, ctemp.F_ClaPro FROM tb_compratemp AS ctemp WHERE ctemp.F_IdCom <> ? GROUP BY ctemp.F_ClaPro) AS COM ON COM.F_OrdCom = c.F_OrdCom AND COM.F_ClaPro = c.F_ClaPro WHERE c.F_IdCom = ?";
    public static String ID_MARCA = "SELECT F_ClaMar,F_DesMar FROM tb_marca WHERE F_DesMar=?";

    public static String DATOS_ACTUALIZA_LERMA = "SELECT c.F_ClaPro AS clave, c.F_Lote AS lote, c.F_FecCad AS caducidad, IF ( m.F_TipMed = 2505, DATE_ADD(c.F_FecCad, INTERVAL - 5 YEAR), DATE_ADD(c.F_FecCad, INTERVAL - 3 YEAR)) AS fechaFabricacion, c.F_Marca AS marca, c.F_Provee AS proveedor, c.F_Cb AS cb, c.F_Tarimas AS tarimas, c.F_Cajas AS cajas, SUM(c.F_Pz) AS F_Pz, SUM(c.F_Resto) AS F_Resto, c.F_Costo AS costo, SUM(c.F_ImpTo) AS F_ImpTo, SUM(c.F_ComTot) AS F_ComTot, c.F_FolRemi AS remision, c.F_OrdCom AS ordernCompra, c.F_ClaOrg AS claOrg, c.F_User AS usuario, c.F_Obser AS observaciones, c.F_Origen AS origen, m.F_TipMed AS tipoMedicamento, IFNULL(lote.folLot, 0) AS folLot, IFNULL(lote.existencia, 0) AS ExistenciaUbicacion, IF ( lote.folLot IS NOT NULL, lote.existencia + SUM(c.F_Pz), SUM(c.F_Pz)) AS cantidadActualizada, c.F_TarimasI, c.F_PzaCaja, c.F_CajasI, c.F_Proyectos, c.F_FecApl as FecApl, c.F_Hora AS horaCaptura FROM tb_medica m, tb_compratemp c LEFT JOIN ( SELECT l.F_ClaPro AS lotClave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Cb AS cb, l.F_FolLot AS folLot, l.F_Origen AS origen, l.F_ExiLot AS existencia, l.F_ClaPro, l.F_Proyecto,l.F_ClaPrv FROM tb_lote l WHERE l.F_ClaLot <> 'X' AND l.F_Ubica = ? GROUP BY l.F_FolLot, l.F_Proyecto ) AS lote ON c.F_ClaPro = lote.lotClave AND c.F_Lote = lote.lote AND c.F_FecCad = lote.caducidad AND c.F_Cb = lote.cb AND c.F_Origen = lote.origen AND c.F_Proyectos = lote.F_Proyecto AND c.F_Provee=lote.F_ClaPrv WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? AND c.F_ClaPro = m.F_ClaPro GROUP BY c.F_ClaPro, c.F_Proyectos, c.F_Lote, c.F_FecCad, c.F_Origen, c.F_Proyectos, c.F_Provee;";

    public static String DATOS_EXTRAORDINARIO = "SELECT c.F_ClaPro AS clave, c.F_Lote AS lote, c.F_FecCad AS caducidad, IF ( m.F_TipMed = 2505, DATE_ADD(c.F_FecCad, INTERVAL - 5 YEAR), DATE_ADD(c.F_FecCad, INTERVAL - 3 YEAR)) AS fechaFabricacion, c.F_Marca AS marca, c.F_Provee AS proveedor, c.F_Cb AS cb, c.F_Cant, c.F_NoCompra AS ordernCompra, uc.F_Usu AS usuario, m.F_TipMed AS tipoMedicamento, IFNULL(lote.folLot, 0) AS folLot, IFNULL(lote.existencia, 0) AS ExistenciaUbicacion, IF ( lote.folLot IS NOT NULL, lote.existencia + SUM(c.F_Cant), SUM(c.F_Cant)) AS cantidadActualizada, c.F_Proyecto, c.F_Fecha, c.F_Hora AS horaCaptura, c.F_Costo as costo, c.F_ImpTo as impTo, c.F_ComTot as comTot, c.F_FactorEmpaque as FactorEmpaque FROM tb_medica m, tb_capturaextra c LEFT JOIN ( SELECT l.F_ClaPro AS lotClave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Cb AS cb, l.F_FolLot AS folLot, l.F_Origen AS origen, l.F_ExiLot AS existencia, l.F_ClaPro, l.F_Proyecto, l.F_ClaPrv FROM tb_lote l WHERE l.F_ClaLot <> 'X' AND l.F_Ubica = 'EXTRA_ORDINARIA' GROUP BY l.F_FolLot, l.F_Proyecto ) AS lote ON c.F_ClaPro = lote.lotClave AND c.F_Lote = lote.lote AND c.F_FecCad = lote.caducidad AND c.F_Cb = lote.cb AND c.F_Origen = lote.origen AND c.F_Proyecto = lote.F_Proyecto AND c.F_Provee = lote.F_ClaPrv INNER JOIN tb_usuariocompra uc ON c.F_IdUsu = uc.F_IdUsu WHERE c.F_IdUsu = ? AND c.F_StsPed = ? AND c.F_ClaPro = m.F_ClaPro GROUP BY c.F_ClaPro, c.F_Proyecto, c.F_Lote, c.F_FecCad, c.F_Provee;";

//    public static String DATOS_ACTUALIZA_PARCIAL = "SELECT c.F_ClaPro AS clave, c.F_Lote AS lote, c.F_FecCad AS caducidad, IF ( m.F_TipMed = 2505, DATE_ADD(c.F_FecCad, INTERVAL - 5 YEAR), DATE_ADD(c.F_FecCad, INTERVAL - 3 YEAR)) AS fechaFabricacion, c.F_Marca AS marca, c.F_Provee AS proveedor, c.F_Cb AS cb, c.F_Tarimas AS tarimas, c.F_Cajas AS cajas, SUM(c.F_Pz) AS F_Pz, SUM(c.F_Resto) AS F_Resto, c.F_Costo AS costo, SUM(c.F_ImpTo) AS F_ImpTo, SUM(c.F_ComTot) AS F_ComTot, c.F_FolRemi AS remision, c.F_OrdCom AS ordernCompra, c.F_ClaOrg AS claOrg, c.F_User AS usuario, c.F_Obser AS observaciones, c.F_Origen AS origen, m.F_TipMed AS tipoMedicamento, IFNULL(lote.folLot, 0) AS folLot, IFNULL(lote.existencia, 0) AS ExistenciaUbicacion, IF ( lote.folLot IS NOT NULL, lote.existencia + SUM(c.F_Pz), SUM(c.F_Pz)) AS cantidadActualizada, c.F_TarimasI, c.F_PzaCaja, c.F_CajasI, c.F_Proyectos, c.F_FecApl as FecApl, c.F_Hora AS horaCaptura FROM tb_medica m, tb_compratemp c LEFT JOIN ( SELECT l.F_ClaPro AS lotClave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Cb AS cb, l.F_FolLot AS folLot, l.F_Origen AS origen, l.F_ExiLot AS existencia, l.F_ClaPro, l.F_Proyecto,l.F_ClaPrv FROM tb_lote l WHERE l.F_ClaLot <> 'X' AND l.F_Ubica = ? GROUP BY l.F_FolLot, l.F_Proyecto ) AS lote ON c.F_ClaPro = lote.lotClave AND c.F_Lote = lote.lote AND c.F_FecCad = lote.caducidad AND c.F_Cb = lote.cb AND c.F_Origen = lote.origen AND c.F_Proyectos = lote.F_Proyecto AND c.F_Provee=lote.F_ClaPrv WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? AND c.F_IdCom = ? AND c.F_ClaPro = m.F_ClaPro GROUP BY c.F_ClaPro, c.F_Proyectos, c.F_Lote, c.F_FecCad, c.F_Origen, c.F_Proyectos, c.F_Provee;";
    public static String DATOS_ACTUALIZA_PARCIAL = "SELECT c.F_ClaPro AS clave, c.F_Lote AS lote, c.F_FecCad AS caducidad, IF ( m.F_TipMed = 2505, DATE_ADD(c.F_FecCad, INTERVAL - 5 YEAR), DATE_ADD(c.F_FecCad, INTERVAL - 3 YEAR)) AS fechaFabricacion, c.F_Marca AS marca, c.F_Provee AS proveedor, c.F_Cb AS cb, c.F_Tarimas AS tarimas, c.F_Cajas AS cajas, SUM(c.F_Pz) AS F_Pz, SUM(c.F_Resto) AS F_Resto, c.F_Costo AS costo, SUM(c.F_ImpTo) AS F_ImpTo, SUM(c.F_ComTot) AS F_ComTot, c.F_FolRemi AS remision, c.F_OrdCom AS ordernCompra, c.F_ClaOrg AS claOrg, c.F_User AS usuario, c.F_Obser AS observaciones, c.F_Origen AS origen, m.F_TipMed AS tipoMedicamento, IFNULL(lote.folLot, 0) AS folLot, IFNULL(lote.existencia, 0) AS ExistenciaUbicacion, IF ( lote.folLot IS NOT NULL, lote.existencia + SUM(c.F_Pz), SUM(c.F_Pz)) AS cantidadActualizada, c.F_TarimasI, c.F_PzaCaja, c.F_CajasI, c.F_Proyectos, c.F_FecApl as FecApl, c.F_Hora AS horaCaptura, c.F_FactorEmpaque, c.F_OrdenSuministro FROM tb_medica m, tb_compratemp c LEFT JOIN ( SELECT l.F_ClaPro AS lotClave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Cb AS cb, l.F_FolLot AS folLot, l.F_Origen AS origen, l.F_ExiLot AS existencia, l.F_ClaPro, l.F_Proyecto,l.F_ClaPrv FROM tb_lote l WHERE l.F_ClaLot <> 'X' AND l.F_Ubica = ? GROUP BY l.F_FolLot, l.F_Proyecto ) AS lote ON c.F_ClaPro = lote.lotClave AND c.F_Lote = lote.lote AND c.F_FecCad = lote.caducidad AND c.F_Cb = lote.cb AND c.F_Origen = lote.origen AND c.F_Proyectos = lote.F_Proyecto AND c.F_Provee=lote.F_ClaPrv WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? AND c.F_IdCom = ? AND c.F_ClaPro = m.F_ClaPro GROUP BY c.F_ClaPro, c.F_Proyectos, c.F_Lote, c.F_FecCad, c.F_Origen, c.F_Proyectos, c.F_Provee;";
    
    public static String DATOS_ACTUALIZA_LERMACross = "SELECT c.F_ClaPro AS clave, c.F_Lote AS lote, c.F_FecCad AS caducidad, IF ( m.F_TipMed = 2505, DATE_ADD(c.F_FecCad, INTERVAL - 5 YEAR), DATE_ADD(c.F_FecCad, INTERVAL - 3 YEAR)) AS fechaFabricacion, c.F_Marca AS marca, c.F_Provee AS proveedor, c.F_Cb AS cb, c.F_Tarimas AS tarimas, c.F_Cajas AS cajas, SUM(c.F_Pz) AS F_Pz, SUM(c.F_Resto) AS F_Resto, c.F_Costo AS costo, SUM(c.F_ImpTo) AS F_ImpTo, SUM(c.F_ComTot) AS F_ComTot, c.F_FolRemi AS remision, c.F_OrdCom AS ordernCompra, c.F_ClaOrg AS claOrg, c.F_User AS usuario, c.F_Obser AS observaciones, c.F_Origen AS origen, m.F_TipMed AS tipoMedicamento, IFNULL(lote.folLot, 0) AS folLot, IFNULL(lote.existencia, 0) AS ExistenciaUbicacion, IF ( lote.folLot IS NOT NULL, lote.existencia + SUM(c.F_Pz), SUM(c.F_Pz)) AS cantidadActualizada, c.F_TarimasI, c.F_PzaCaja, c.F_CajasI, c.F_Proyectos, c.F_FecApl as FecApl, c.F_Hora AS horaCaptura FROM tb_medica m, tb_compratemp c LEFT JOIN ( SELECT l.F_ClaPro AS lotClave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, l.F_Cb AS cb, l.F_FolLot AS folLot, l.F_Origen AS origen, l.F_ExiLot AS existencia, l.F_ClaPro, l.F_Proyecto,l.F_ClaPrv FROM tb_lote l WHERE l.F_ClaLot <> 'X' AND l.F_Ubica = 'NUEVACROSS' GROUP BY l.F_FolLot, l.F_Proyecto ) AS lote ON c.F_ClaPro = lote.lotClave AND c.F_Lote = lote.lote AND c.F_FecCad = lote.caducidad AND c.F_Cb = lote.cb AND c.F_Origen = lote.origen AND c.F_Proyectos = lote.F_Proyecto AND c.F_Provee=lote.F_ClaPrv WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? AND c.F_ClaPro = m.F_ClaPro GROUP BY c.F_ClaPro, c.F_Proyectos, c.F_Lote, c.F_FecCad, c.F_Origen, c.F_Proyectos, c.F_Provee;";
    private static final String ACTUALIZAR_TB_LOTE = "UPDATE tb_lote SET F_ExiLot=? WHERE F_FolLot=? AND F_Ubica=? and F_Proyecto = ?;";
    private static final String INDICE_LOTE = "SELECT F_IndCom,F_IndLote FROM tb_indice;";
    private static final String INSERTAR_TB_LOTE = "INSERT INTO tb_lote SET F_ClaPro=?, F_ClaLot=?, F_FecCad=?, F_ExiLot=?, F_FolLot=?, F_ClaOrg=?, F_Ubica=?, F_FecFab=?, F_Cb=?, F_ClaMar=?, F_Origen=?, F_ClaPrv=?, F_UniMed=?, F_Proyecto=?;";
    private static final String ACTUALIZA_INDICE_MOVIMIENTO = "UPDATE tb_indice SET F_IndMov=F_IndMov+1";
    private static final String ACTUALIZA_INDICE_LOTE = "UPDATE tb_indice SET F_IndLote=?;";
    private static final String ACTUALIZAR_INDICE_COMPRA = "UPDATE tb_indice SET F_IndCom=F_IndCom+1;";
    
    private static String INSERTAR_KARDEX = "INSERT INTO tb_movinv SET F_FecMov=NOW(), F_DocMov=?,F_ConMov=1,F_ProMov=?,F_CantMov=?,F_CostMov=?,F_TotMov=?,F_SigMov=1,F_LotMov=?,F_UbiMov=?,F_ClaProve=?,F_hora=CURTIME(),F_User=?,F_Comentarios = 'Entrada Parcial' ;";
    private static String INSERTAR_KARDEXTRA = "INSERT INTO tb_movinv SET F_FecMov=NOW(), F_DocMov=?,F_ConMov=1,F_ProMov=?,F_CantMov=?,F_CostMov=?,F_TotMov=?,F_SigMov=1,F_LotMov=?,F_UbiMov='EXTRA_ORDINARIA',F_ClaProve=?,F_hora=CURTIME(),F_User=?;";
    private static String INSERTAR_KARDEXCross = "INSERT INTO tb_movinv SET F_FecMov=NOW(), F_DocMov=?,F_ConMov=1,F_ProMov=?,F_CantMov=?,F_CostMov=?,F_TotMov=?,F_SigMov=1,F_LotMov=?,F_UbiMov='NUEVACROSS',F_ClaProve=?,F_hora=CURTIME(),F_User=?;";
//    private static String INSERTAR_COMPRA = "INSERT INTO tb_compra SET F_ClaDoc=?,F_ProVee=?,F_StsCom='A',F_FecApl=NOW(),F_ClaPro=?,F_CanCom=?,F_Costo=?,F_Cajas=?,F_ImpTo=?,F_ComTot=?,F_FolRemi=?,F_OrdCom=?,F_ClaOrg=?,F_Cb=?,F_Hora=CURTIME(),F_User=?,F_Obser=?,F_Web=0,F_Lote=?,F_Tarimas=?,F_TarimasI=?,F_CajasI=?,F_Pz=?,F_Resto=?,F_Proyecto=?, F_Fecha_Pago = DATE_ADD(curdate(), INTERVAL ? DAY), F_UniMed=?,F_Presentacion=?,F_Origen=?,F_FileFac=?, F_FecCad=?, F_Marca=?,F_FileRemi=?,F_FolFac=?, F_UserIngreso=?, F_FecCaptura=?, F_HoraCaptura = ?;";
    private static String INSERTAR_COMPRA = "INSERT INTO tb_compra SET F_ClaDoc=?,F_ProVee=?,F_StsCom='A',F_FecApl=NOW(),F_ClaPro=?,F_CanCom=?,F_Costo=?,F_Cajas=?,F_ImpTo=?,F_ComTot=?,F_FolRemi=?,F_OrdCom=?,F_ClaOrg=?,F_Cb=?,F_Hora=CURTIME(),F_User=?,F_Obser=?,F_Web=0,F_Lote=?,F_Tarimas=?,F_TarimasI=?,F_CajasI=?,F_Pz=?,F_Resto=?,F_Proyecto=?, F_Fecha_Pago = DATE_ADD(curdate(), INTERVAL ? DAY), F_UniMed=?,F_Presentacion=?,F_Origen=?,F_FileFac=?, F_FecCad=?, F_Marca=?,F_FileRemi=?,F_FolFac=?, F_UserIngreso=?, F_FecCaptura=?, F_HoraCaptura = ?, F_FactorEmpaque = ?,F_OrdenSuministro = ?, Lote=?;";
    private static String INSERTAR_COMPRA_REGISTRO = "INSERT INTO tb_compraregistro SET F_FecApl=NOW(),F_ClaPro=?,F_Lote=?,F_FecCad=?,F_FecFab=?,F_Marca=?,F_ProVee=?,F_Cb=?,F_Tarimas=?,F_Cajas=?,F_Pz=?,F_TarimasI=0,F_CajasI=0,F_Resto=?,F_Costo=?,F_ImpTo=?,F_ComTot=?,F_Obser='',F_FolRemi=?,F_OrdCom=?,F_ClaOrg=?,F_User=?,F_Proyecto=?;";
    private static String INSERTAR_TB_CB = "INSERT INTO tb_cb SET F_Cb=?,F_ClaPro=?,F_ClaLot=?,F_FecCad=?,F_FecFab=?,F_ClaMar=?;";
    private static String BUSCA_TB_PEDIDO_ISEM_2017 = "SELECT c.F_ClaPro as clave, sum(c.F_CanCom) as totalCom, p17.F_Cant as solicitado, (p17.F_Cant - sum(c.F_CanCom )) AS Dif FROM tb_compra AS c INNER JOIN tb_pedido_sialss AS p17 ON c.F_OrdCom = p17.F_NoCompra AND c.F_ClaPro = p17.F_Clave WHERE c.F_ClaPro = ? AND c.F_OrdCom = ? GROUP BY c.F_ClaPro, c.F_OrdCom";
    private static String UPDATE_TB_PEDIDO_ISEM_2017 = "UPDATE tb_pedido_sialss SET F_Recibido ='1' WHERE F_NoCompra =? and F_Clave =?";
    private static String UPDATE_EXTRAORDINARIO = "UPDATE tb_capturaextra SET F_StsPed = '3' WHERE F_NoCompra = ? AND F_IdUsu = ? and F_ClaPro = ? and F_StsPed = ?;";
    private static String UPDATE_TB_PEDIDO = "UPDATE tb_pedidoisem SET F_Recibido ='1' WHERE F_NoCompra =? and F_Clave =?";
    private static String DELETE_FROM_COMPRA_TEMPORAL = "DELETE FROM tb_compratemp WHERE F_OrdCom=? AND F_FolRemi=?;";
    private static String DELETE_FROM_COMPRA_TEMPORALParcial = "DELETE FROM tb_compratemp WHERE F_OrdCom = ? AND F_FolRemi = ? AND F_IdCom = ?;";

    private static String BusquedaIdReg = "SELECT F_IdCom FROM tb_compratemp WHERE F_OrdCom = ? AND F_FolRemi = ?;";

    private static String BusquedaQFB = "SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_controlados as ct on c.F_Clapro = ct.F_Clapro WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? order by c.F_ClaPro;";
    private static String clavesQFBR = "SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_redfria AS r ON c.F_ClaPro = r.F_ClaPro WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? order by c.F_ClaPro;";
    private static String clavesQFBA = "SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_ape AS a ON c.F_ClaPro = a.F_ClaPro WHERE c.F_OrdCom = ? AND c.F_FolRemi = ? order by c.F_ClaPro;";

    CorreoConfirmaRemision correoConfirma = new CorreoConfirmaRemision();
    private final ConectionDBTrans con = new ConectionDBTrans();
//    private final ConectionDB conSendero = new ConectionDB();
    private ResultSet rs;
    private ResultSet rss;
    private PreparedStatement ps;
    private PreparedStatement psInsertLote;
    private PreparedStatement psUpdateIndice;
    private PreparedStatement psInsertKardex;
    private PreparedStatement psInsertCompra;
    private PreparedStatement psInsertCompraRegistro;
    private PreparedStatement psTbCb;
    private PreparedStatement psOperaciones;
    private PreparedStatement psMarcaSendero;
    private PreparedStatement psProveedorSendero;
    private PreparedStatement psPedidoIsem;
    private PreparedStatement psUpdatePI;
    private PreparedStatement especialQuery;
    private PreparedStatement psBuscaP17;
    private String query;

    @Override
    public boolean insertSendero(String ordenCompra, String remision) {
        boolean save = false;
        List<comprasModel> lM = new ArrayList<>();
        List<pedidosIsemModel> lP = new ArrayList<>();
        List<marcaProveedor> lMP = new ArrayList<>();
       
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(COMPRA_TEMPORAL_LERMA);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel c = new comprasModel();
                c.setIdCom(0);
                c.setFecha(rs.getString(2));
                c.setClave(rs.getString(3));
                c.setLote(rs.getString(4));
                c.setCaducidad(rs.getString(5));
                c.setFabricacion(rs.getString(6));
                c.setMarca(rs.getInt(7));
                c.setProveedor(rs.getInt(8));
                c.setCb(rs.getString(9));
                c.setTarimas(rs.getInt(10));
                c.setCajas(rs.getInt(11));
                c.setPz(rs.getInt(12));
                c.setTarimasUno(rs.getInt(13));
                c.setCajasUno(rs.getInt(14));
                c.setResto(rs.getInt(15));
                c.setCosto(rs.getDouble(16));
                c.setImporteTotal(rs.getDouble(17));
                c.setCompraTotal(rs.getDouble(18));
                c.setObservaciones(rs.getString(19));
                c.setFolRemi(rs.getString(20));
                c.setOrdenCompra(rs.getString(21));
                c.setClaOrg(rs.getInt(22));
                c.setUser(rs.getString(23));
                c.setEstado(rs.getInt(24));
                c.setOrigen(rs.getInt(25));
                c.setFecha(rs.getString(1));
                c.setHora(rs.getString(31));
                c.setFactorEmpaque(rs.getInt(32));
                c.setOrdenSuministro(rs.getString(33));
                lM.add(c);
            }
            ps.clearParameters();
            ps = con.getConn().prepareStatement(PEDIDO_ISEM_2017);
            ps.setString(1, ordenCompra);
            rs = ps.executeQuery();
            while (rs.next()) {
                pedidosIsemModel p = new pedidosIsemModel();
                p.setId(0);
                p.setNoCompra(rs.getString(2));
                p.setProveedor(rs.getString(3));
                p.setClave(rs.getString(4));
                p.setCb(rs.getString(5));
                p.setPriori(rs.getString(6));
                p.setLote(rs.getString(7));
                p.setCaducidad(rs.getString(8));
                p.setCantidad(rs.getInt(9));
                p.setObservacion(rs.getString(10));
                p.setFecha(rs.getString(11));
                p.setFechaSur(rs.getString(12));
                p.setHora(rs.getString(13));
                p.setIdUsu(rs.getString(14));
                p.setStatus(rs.getInt(15));
                p.setRecibido(rs.getInt(16));
                lP.add(p);

            }
            ps.clearParameters();

            ps = con.getConn().prepareStatement(DATOS_MARCA_PROVEEDOR);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            while (rs.next()) {
                marcaProveedor mP = new marcaProveedor();
                mP.setClaveMarca(rs.getInt("claveMarca"));
                mP.setClaveProveedor(rs.getInt("claveProveedor"));
                mP.setDescripMarca(rs.getString("descripmarca"));
                mP.setDescripProveedor(rs.getString("nombreProveedor"));
                lMP.add(mP);
            }
            ps.clearParameters();
//            conSendero.conectar();

            for (marcaProveedor mp : lMP) {
                psMarcaSendero = con.getConn().prepareStatement(MARCA_SENDERO);
                psMarcaSendero.setInt(1, mp.getClaveMarca());
                rs = psMarcaSendero.executeQuery();
                if (!rs.next()) {
                    con.getConn().setAutoCommit(false);
                    ps = con.getConn().prepareStatement(INSERT_MARCA);
                    ps.setInt(1, mp.getClaveMarca());
                    ps.setString(2, mp.getDescripMarca());
                    ps.execute();
                    con.getConn().commit();
                    ps.clearParameters();
                }
                psProveedorSendero = con.getConn().prepareStatement(PROVEEDOR_SENDERO);
                psProveedorSendero.setInt(1, mp.getClaveProveedor());
                rs = psProveedorSendero.executeQuery();
                if (!rs.next()) {
                    con.getConn().setAutoCommit(false);
                    ps = con.getConn().prepareStatement(INSERT_PROVEEDOR);
                    ps.setInt(1, mp.getClaveProveedor());
                    ps.setString(2, mp.getDescripProveedor());
                    ps.execute();
                    con.getConn().commit();
                    ps.clearParameters();
                }

            }

            con.getConn().setAutoCommit(false);

//            query = "INSERT INTO tb_pedidoisem VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//            for (pedidosIsemModel p : lP) {
//                ps = con.getConn().prepareStatement(query);
//                ps.setInt(1, p.getId());
//                ps.setString(2, p.getNoCompra());
//                ps.setString(3, p.getProveedor());
//                ps.setString(4, p.getClave());
//                ps.setString(5, p.getCb());
//                ps.setString(6, p.getPriori());
//                ps.setString(7, p.getLote());
//                ps.setString(8, p.getCaducidad());
//                ps.setInt(9, p.getCantidad());
//                ps.setString(10, p.getObservacion());
//                ps.setString(11, p.getFecha());
//                ps.setString(12, p.getFechaSur());
//                ps.setString(13, p.getHora());
//                ps.setString(14, p.getIdUsu());
//                ps.setInt(15, p.getStatus());
//                ps.setInt(16, p.getRecibido());
//                ps.execute();
//                ps.clearParameters();
//
//            }

            query = "INSERT INTO tb_compratemp VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, curtime(),?,?)";
            for (comprasModel c : lM) {
                ps = con.getConn().prepareStatement(query);
                ps.setInt(1, c.getIdCom());
                ps.setString(2, c.getFecha());
                ps.setString(3, c.getClave());
                ps.setString(4, c.getLote());
                ps.setString(5, c.getCaducidad());
                ps.setString(6, c.getFabricacion());
                ps.setInt(7, c.getMarca());
                ps.setInt(8, c.getProveedor());
                ps.setString(9, c.getCb());
                ps.setInt(10, c.getTarimas());
                ps.setInt(11, c.getCajas());
                ps.setInt(12, c.getPz());
                ps.setInt(13, c.getTarimasUno());
                ps.setInt(14, c.getCajasUno());
                ps.setInt(15, c.getResto());
                ps.setDouble(16, c.getCosto());
                ps.setDouble(17, c.getImporteTotal());
                ps.setDouble(18, c.getCompraTotal());
                ps.setString(19, c.getObservaciones());
                ps.setString(20, c.getFolRemi());
                ps.setString(21, c.getOrdenCompra());
                ps.setInt(22, c.getClaOrg());
                ps.setString(23, c.getUser());
                ps.setInt(24, c.getEstado());
                ps.setInt(25, c.getOrigen());
                ps.setInt(27, c.getFactorEmpaque());
                ps.setString(28, c.getOrdenSuministro());
                
                
                ps.execute();
                ps.clearParameters();
            }
            save = true;
            con.getConn().commit();
            return save;

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    
    //////////////////////////////////////////////////////
    @Override
    public boolean agregarSendero(String ordenCompra, String remision, String usuarioIngreso) {
        boolean save = false;
        int indiceLote = 0;
        int indiceCompra = 0;
        List<comprasModel> lM = new ArrayList<>();
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(INDICE_LOTE);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceLote = rs.getInt("F_IndLote");
                indiceCompra = rs.getInt("F_IndCom");

            }
            ps = con.getConn().prepareStatement(DATOS_ACTUALIZA_LERMA);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel compras = new comprasModel();
                compras.setClave(rs.getString(1));
                compras.setLote(rs.getString(2));
                compras.setCaducidad(rs.getString(3));
                compras.setFabricacion(rs.getString(4));
                compras.setMarca(rs.getInt(5));
                compras.setProveedor(rs.getInt(6));
                compras.setCb(rs.getString(7));
                compras.setTarimas(rs.getInt(8));
                compras.setCajas(rs.getInt(9));
                compras.setPz(rs.getInt(10));
                compras.setResto(rs.getInt(11));
                compras.setCosto(rs.getDouble(12));
                compras.setImporteTotal(rs.getDouble(13));
                compras.setCompraTotal(rs.getDouble(14));
                compras.setFolRemi(rs.getString(15));
                compras.setOrdenCompra(rs.getString(16));
                compras.setClaOrg(rs.getInt(17));
                compras.setUser(rs.getString(18));
                compras.setObservaciones(rs.getString(19));
                compras.setOrigen(rs.getInt(20));
                compras.setFolLot(rs.getInt(22));
                compras.setExistenciasUbicacion(rs.getInt(23));
                compras.setExistenciaInsertar(rs.getInt(24));
                compras.setProyecto(rs.getInt(28));
                
                if (compras.getFolLot() == 0) {
                    compras.setIndiceAInsertar(indiceLote);
                    indiceLote = indiceLote + 1;
                }
                compras.setFecha(rs.getString("fecApl"));
                compras.setHora(rs.getString("horaCaptura"));
                lM.add(compras);

            }
            con.getConn().setAutoCommit(false);
            for (comprasModel c : lM) {
                psInsertKardex = con.getConn().prepareStatement(INSERTAR_KARDEX);
                psInsertKardex.setInt(1, indiceCompra);
                psInsertKardex.setString(2, c.getClave());
                psInsertKardex.setInt(3, c.getPz());
                psInsertKardex.setDouble(4, c.getCosto());
                psInsertKardex.setDouble(5, c.getCompraTotal());
                psInsertKardex.setInt(7, c.getProveedor());
                psInsertKardex.setString(8, usuarioIngreso);

                psInsertCompra = con.getConn().prepareStatement(INSERTAR_COMPRA);
                psInsertCompra.setInt(1, indiceCompra);
                psInsertCompra.setInt(2, c.getProveedor());
                psInsertCompra.setString(3, c.getClave());
                psInsertCompra.setInt(4, c.getPz());
                psInsertCompra.setDouble(5, c.getCosto());
                psInsertCompra.setInt(6, c.getCajas());
                psInsertCompra.setDouble(7, c.getImporteTotal());
                psInsertCompra.setDouble(8, c.getCompraTotal());
                psInsertCompra.setString(9, c.getFolRemi());
                psInsertCompra.setString(10, c.getOrdenCompra());
                psInsertCompra.setInt(11, c.getClaOrg());
                psInsertCompra.setString(12, c.getCb());
                psInsertCompra.setString(13, c.getUser());
                psInsertCompra.setString(14, c.getObservaciones());

                if (c.getFolLot() != 0) {
                    psInsertLote = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psInsertLote.setInt(1, c.getExistenciaInsertar());
                    psInsertLote.setInt(2, c.getFolLot());
                    psInsertLote.setString(3, "NUEVA");
                    psInsertLote.setInt(4, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getFolLot());
                    psInsertCompra.setInt(15, c.getFolLot());

                } else {
                    psInsertLote = con.getConn().prepareStatement(INSERTAR_TB_LOTE);
                    psInsertLote.setString(1, c.getClave());
                    psInsertLote.setString(2, c.getLote());
                    psInsertLote.setString(3, c.getCaducidad());
                    psInsertLote.setInt(4, c.getExistenciaInsertar());
                    psInsertLote.setInt(5, c.getIndiceAInsertar());
                    psInsertLote.setInt(6, c.getClaOrg());
                    psInsertLote.setString(7, "NUEVA");
                    psInsertLote.setString(8, c.getFabricacion());
                    psInsertLote.setString(9, c.getCb());
                    psInsertLote.setInt(10, c.getMarca());
                    psInsertLote.setInt(11, c.getOrigen());
                    psInsertLote.setInt(12, c.getProveedor());
                    psInsertLote.setInt(13, 131);
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getIndiceAInsertar());
                    psInsertCompra.setInt(15, c.getIndiceAInsertar());

                }
                psInsertCompra.setInt(16, c.getTarimas());
                psInsertCompra.setInt(17, c.getTarimasI());
                psInsertCompra.setInt(18, c.getCajasI());
                psInsertCompra.setInt(19, c.getPzaCajas());
                psInsertCompra.setInt(20, c.getResto());
                psInsertCompra.setInt(21, c.getProyecto());
                psInsertCompra.setString(31, usuarioIngreso);
                psInsertCompra.setString(32, c.getFecha());
                psInsertCompra.setString(33, c.getHora());
                
                System.out.println("******  1  "+psInsertCompra.toString());
                psInsertCompra.execute();
                psInsertCompra.clearParameters();

                psInsertCompraRegistro = con.getConn().prepareStatement(INSERTAR_COMPRA_REGISTRO);
                psInsertCompraRegistro.setString(1, c.getClave());
                psInsertCompraRegistro.setString(2, c.getLote());
                psInsertCompraRegistro.setString(3, c.getCaducidad());
                psInsertCompraRegistro.setString(4, c.getFabricacion());
                psInsertCompraRegistro.setInt(5, c.getMarca());
                psInsertCompraRegistro.setInt(6, c.getProveedor());
                psInsertCompraRegistro.setString(7, c.getCb());
                psInsertCompraRegistro.setInt(8, c.getTarimas());
                psInsertCompraRegistro.setInt(9, c.getCajas());
                psInsertCompraRegistro.setInt(10, c.getPz());
                psInsertCompraRegistro.setInt(11, c.getResto());
                psInsertCompraRegistro.setDouble(12, c.getCosto());
                psInsertCompraRegistro.setDouble(13, c.getImporteTotal());
                psInsertCompraRegistro.setDouble(14, c.getCompraTotal());
                psInsertCompraRegistro.setString(15, c.getFolRemi());
                psInsertCompraRegistro.setString(16, c.getOrdenCompra());
                psInsertCompraRegistro.setInt(17, c.getClaOrg());
                psInsertCompraRegistro.setString(18, c.getUser());
                psInsertCompraRegistro.setInt(19, c.getProyecto());

                psInsertCompraRegistro.execute();
                psInsertCompraRegistro.close();

                psInsertKardex.execute();
                psInsertKardex.close();

                psTbCb = con.getConn().prepareStatement(INSERTAR_TB_CB);
                psTbCb.setString(1, c.getCb());
                psTbCb.setString(2, c.getClave());
                psTbCb.setString(3, c.getLote());
                psTbCb.setString(4, c.getCaducidad());
                psTbCb.setString(5, c.getFabricacion());
                psTbCb.setInt(6, c.getMarca());
                psTbCb.execute();
                psTbCb.clearParameters();

                psOperaciones = con.getConn().prepareStatement(UPDATE_TB_PEDIDO);
                psOperaciones.setString(1, c.getOrdenCompra());
                psOperaciones.setString(2, c.getClave());
                psOperaciones.execute();
                psOperaciones.clearParameters();

                psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_MOVIMIENTO);
                psUpdateIndice.execute();
                psUpdateIndice.clearParameters();

            }
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_LOTE);
            psUpdateIndice.setInt(1, indiceLote);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZAR_INDICE_COMPRA);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(DELETE_FROM_COMPRA_TEMPORAL);
            psUpdateIndice.setString(1, ordenCompra);
            psUpdateIndice.setString(2, remision);
            psUpdateIndice.execute();

            psUpdateIndice.close();

            con.getConn().commit();
            save = true;

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean Actualizarlerma(String ordenCompra, String remision, String usuarioIngreso, String UbicaN) {
        boolean save = false;
        int indiceLote = 0;
        int indiceCompra = 0;
        String UbicaNS = UbicaN;
        
        List<comprasModel> lM = new ArrayList<>();
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(INDICE_LOTE);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceLote = rs.getInt("F_IndLote");
                indiceCompra = rs.getInt("F_IndCom");

            }
            ps = con.getConn().prepareStatement(DATOS_ACTUALIZA_LERMA);
            ps.setString(2, ordenCompra);
            ps.setString(3, remision);
             ps.setString(1, UbicaNS);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel compras = new comprasModel();
                compras.setClave(rs.getString(1));
                compras.setLote(rs.getString(2));
                compras.setCaducidad(rs.getString(3));
                compras.setFabricacion(rs.getString(4));
                compras.setMarca(rs.getInt(5));
                compras.setProveedor(rs.getInt(6));
                compras.setCb(rs.getString(7));
                compras.setTarimas(rs.getInt(8));
                compras.setCajas(rs.getInt(9));
                compras.setPz(rs.getInt(10));
                compras.setResto(rs.getInt(11));
                compras.setCosto(rs.getDouble(12));
                compras.setImporteTotal(rs.getDouble(13));
                compras.setCompraTotal(rs.getDouble(14));
                compras.setFolRemi(rs.getString(15));
                compras.setOrdenCompra(rs.getString(16));
                compras.setClaOrg(rs.getInt(17));
                compras.setUser(rs.getString(18));
                compras.setObservaciones(rs.getString(19));
                compras.setOrigen(rs.getInt(20));
                compras.setFolLot(rs.getInt(22));
                compras.setExistenciasUbicacion(rs.getInt(23));
                compras.setExistenciaInsertar(rs.getInt(24));
                compras.setTarimasI(rs.getInt(25));
                compras.setPzaCajas(rs.getInt(26));
                compras.setCajasI(rs.getInt(27));
                compras.setProyecto(rs.getInt(28));
                if (compras.getFolLot() == 0) {
                    compras.setIndiceAInsertar(indiceLote);
                    indiceLote = indiceLote + 1;
                }
                compras.setFecha(rs.getString("FecApl"));
                compras.setHora(rs.getString("horaCaptura"));
                lM.add(compras);

            }
            con.getConn().setAutoCommit(false);
            for (comprasModel c : lM) {
                psInsertKardex = con.getConn().prepareStatement(INSERTAR_KARDEX);
                psInsertKardex.setInt(1, indiceCompra);
                psInsertKardex.setString(2, c.getClave());
                psInsertKardex.setInt(3, c.getPz());
                psInsertKardex.setDouble(4, c.getCosto());
                psInsertKardex.setDouble(5, c.getCompraTotal());
                psInsertKardex.setInt(8, c.getProveedor());
                psInsertKardex.setString(9, usuarioIngreso);
                psInsertKardex.setString(7, UbicaNS);//parametro de ubicacion

                psInsertCompra = con.getConn().prepareStatement(INSERTAR_COMPRA);
                psInsertCompra.setInt(1, indiceCompra);
                psInsertCompra.setInt(2, c.getProveedor());
                psInsertCompra.setString(3, c.getClave());
                psInsertCompra.setInt(4, c.getPz());
                psInsertCompra.setDouble(5, c.getCosto());
                psInsertCompra.setInt(6, c.getCajas());
                psInsertCompra.setDouble(7, c.getImporteTotal());
                psInsertCompra.setDouble(8, c.getCompraTotal());
                psInsertCompra.setString(9, c.getFolRemi());
                psInsertCompra.setString(10, c.getOrdenCompra());
                psInsertCompra.setInt(11, c.getClaOrg());
                psInsertCompra.setString(12, c.getCb());
                psInsertCompra.setString(13, c.getUser());
                psInsertCompra.setString(14, c.getObservaciones());

                if (c.getFolLot() != 0) {
                    psInsertLote = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psInsertLote.setInt(1, c.getExistenciaInsertar());
                    psInsertLote.setInt(2, c.getFolLot());
                    psInsertLote.setString(3, UbicaNS);//Ubicacion
                    psInsertLote.setInt(4, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getFolLot());
                    psInsertCompra.setInt(15, c.getFolLot());

                } else {
                    psInsertLote = con.getConn().prepareStatement(INSERTAR_TB_LOTE);
                    psInsertLote.setString(1, c.getClave());
                    psInsertLote.setString(2, c.getLote());
                    psInsertLote.setString(3, c.getCaducidad());
                    psInsertLote.setInt(4, c.getExistenciaInsertar());
                    psInsertLote.setInt(5, c.getIndiceAInsertar());
                    psInsertLote.setInt(6, c.getClaOrg());
                    psInsertLote.setString(7, UbicaNS);//Ubicacion
                    psInsertLote.setString(8, c.getFabricacion());
                    psInsertLote.setString(9, c.getCb());
                    psInsertLote.setInt(10, c.getMarca());
                    psInsertLote.setInt(11, c.getOrigen());
                    psInsertLote.setInt(12, c.getProveedor());
                    psInsertLote.setInt(13, 131);
                    psInsertLote.setInt(14, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getIndiceAInsertar());
                    psInsertCompra.setInt(15, c.getIndiceAInsertar());

                }
                psInsertCompra.setInt(16, c.getTarimas());
                psInsertCompra.setInt(17, c.getTarimasI());
                psInsertCompra.setInt(18, c.getCajasI());
                psInsertCompra.setInt(19, c.getPzaCajas());
                psInsertCompra.setInt(20, c.getResto());
                psInsertCompra.setInt(21, c.getProyecto());
                psInsertCompra.setString(31, usuarioIngreso);
                psInsertCompra.setString(32, c.getFecha());
                psInsertCompra.setString(33, c.getHora());
                System.out.println("******  2  "+psInsertCompra.toString());
                psInsertCompra.execute();
                psInsertCompra.clearParameters();

                psInsertCompraRegistro = con.getConn().prepareStatement(INSERTAR_COMPRA_REGISTRO);
                psInsertCompraRegistro.setString(1, c.getClave());
                psInsertCompraRegistro.setString(2, c.getLote());
                psInsertCompraRegistro.setString(3, c.getCaducidad());
                psInsertCompraRegistro.setString(4, c.getFabricacion());
                psInsertCompraRegistro.setInt(5, c.getMarca());
                psInsertCompraRegistro.setInt(6, c.getProveedor());
                psInsertCompraRegistro.setString(7, c.getCb());
                psInsertCompraRegistro.setInt(8, c.getTarimas());
                psInsertCompraRegistro.setInt(9, c.getCajas());
                psInsertCompraRegistro.setInt(10, c.getPz());
                psInsertCompraRegistro.setInt(11, c.getResto());
                psInsertCompraRegistro.setDouble(12, c.getCosto());
                psInsertCompraRegistro.setDouble(13, c.getImporteTotal());
                psInsertCompraRegistro.setDouble(14, c.getCompraTotal());
                psInsertCompraRegistro.setString(15, c.getFolRemi());
                psInsertCompraRegistro.setString(16, c.getOrdenCompra());
                psInsertCompraRegistro.setInt(17, c.getClaOrg());
                psInsertCompraRegistro.setString(18, c.getUser());
                psInsertCompraRegistro.setInt(19, c.getProyecto());

                psInsertCompraRegistro.execute();
                psInsertCompraRegistro.close();

                psInsertKardex.execute();
                psInsertKardex.close();

                psTbCb = con.getConn().prepareStatement(INSERTAR_TB_CB);
                psTbCb.setString(1, c.getCb());
                psTbCb.setString(2, c.getClave());
                psTbCb.setString(3, c.getLote());
                psTbCb.setString(4, c.getCaducidad());
                psTbCb.setString(5, c.getFabricacion());
                psTbCb.setInt(6, c.getMarca());
                psTbCb.execute();
                psTbCb.clearParameters();

                 //cerrar OC en Pedidos ISem
//                ps = con.getConn().prepareStatement(BUSCA_TB_PEDIDO_ISEM_2017);
//                ps.setString(1, c.getClave());
//                ps.setString(2, ordenCompra);
//                rs = ps.executeQuery();
//                int Dif = 0;
//                while (rs.next()) {
//                    Dif = Integer.parseInt(rs.getString(4));
//                }
//               
//                if (Dif > 0) {
                
                psOperaciones = con.getConn().prepareStatement(UPDATE_TB_PEDIDO_ISEM_2017);
                psOperaciones.setString(1, c.getOrdenCompra());
                psOperaciones.setString(2, c.getClave());
                psOperaciones.execute();
                psOperaciones.clearParameters();
//                }
//                psOperaciones.clearParameters();

                psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_MOVIMIENTO);
                psUpdateIndice.execute();
                psUpdateIndice.clearParameters();
            }
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_LOTE);
            psUpdateIndice.setInt(1, indiceLote);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZAR_INDICE_COMPRA);
            psUpdateIndice.execute();

            psUpdateIndice = con.getConn().prepareStatement(DELETE_FROM_COMPRA_TEMPORAL);
            psUpdateIndice.setString(1, ordenCompra);
            psUpdateIndice.setString(2, remision);
            psUpdateIndice.execute();

            psUpdateIndice.close();
            con.getConn().commit();
            
             /*****para enviar correo*****/
            ps = con.getConn().prepareStatement(BusquedaQFB);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            String Bclave = "";
            while (rs.next()) {
               Bclave = rs.getString(1);               
               }
            if(!Bclave.equals("")){
                correoConfirma.enviaCorreo(ordenCompra,remision);
            }
            
            save = true;

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }

    @Override
    public boolean ActualizarDatos(String usuario, String lote, String Caducidad, int cantidad, String cb, String marca, int id, int tarimas, int cajas, int pzacaja, int cajasi, int resto, int tarimasI, String Costo, Integer factorEmpaque) {
//           public boolean ActualizarDatos(String usuario, String lote, String Caducidad, int cantidad, String cb, String marca, int id, int tarimas, int cajas, int pzacaja, int cajasi, int resto, int tarimasI, String Costo, int factorEmpaque) {
            boolean save = false;
        String loteTemporal = "";
        String caducidadTemporal = "";
        int cantidadTemporal = 0;
        String cbTemporal = "";
        int marcaTemporal = 0;
        int marcaClave = 0;
        int tarimaTemporal = 0;
        int cajasTemporal = 0;
        int pzacajaTemporal = 0;
        int cajasiTemporal = 0;
        int restoTemporal = 0;
        int tarimaITemporal = 0;
        String observaciones = "";
        String marcaDescripcion = "";
        String marcaDescripcionTemporal = "";
        int factorEmpaqueTemporal= 0;
        int tcajasC = cajas * tarimas;
        try {
            con.conectar();

            ps = con.getConn().prepareStatement(ID_MARCA);
            ps.setString(1, marca);
            rs = ps.executeQuery();
            while (rs.next()) {
                marcaClave = rs.getInt("F_ClaMar");
                marcaDescripcionTemporal = rs.getString("F_DesMar");
            }

            ps.clearParameters();

            ps = con.getConn().prepareStatement(DATOS_COMPRA_TEMPORAL_PARA_EDITAR);
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                loteTemporal = rs.getString("lote");
                caducidadTemporal = rs.getString("caducidad");
                cbTemporal = rs.getString("cb");
                cantidadTemporal = rs.getInt("cantidad");
                marcaTemporal = rs.getInt("claMar");
                marcaDescripcion = rs.getString("marca");
                observaciones = rs.getString("observaciones");
                tarimaTemporal = rs.getInt(8);
                cajasTemporal = rs.getInt(9);
                pzacajaTemporal = rs.getInt(10);
                cajasiTemporal = rs.getInt(11);
                restoTemporal = rs.getInt(12);
                tarimaITemporal = rs.getInt(13);
            }
            if (!loteTemporal.equals(lote)) {
                observaciones += "- Lote Actualizado:" + loteTemporal + " cambia a " + lote;
            }
            if (!caducidadTemporal.equals(Caducidad)) {
                observaciones += "- Caducidad Actualizada:" + caducidadTemporal + " cambia a " + Caducidad;
            }
            if (cantidadTemporal != cantidad) {
                observaciones += "- Cantidad Actualizada:" + cantidadTemporal + " cambia a " + cantidad;
            }
            if (!cbTemporal.equals(cb)) {
                observaciones += "- Cb Actualizado:" + cbTemporal + " cambia a " + cb;
            }
            if (marcaTemporal != marcaClave) {
                observaciones += "- Marca Actualizada:" + marcaDescripcion + " cambia a " + marca;
            }
            if (tarimaTemporal != tarimas) {
                observaciones += "- Tarima Actualizada:" + tarimaTemporal + " cambia a " + tarimas;
            }
            if (cajasTemporal != cajas) {
                observaciones += "- Cajas Actualizada:" + marcaDescripcion + " cambia a " + cajas;
            }
            if (pzacajaTemporal != pzacaja) {
                observaciones += "- Pizas Cajas Actualizada:" + pzacajaTemporal + " cambia a " + pzacaja;
            }
            if (cajasiTemporal != cajasi) {
                observaciones += "- Cajas x tarima incompleta Actualizada:" + cajasiTemporal + " cambia a " + cajasi;
            }
            if (restoTemporal != resto) {
                observaciones += "- Resto Actualizada:" + restoTemporal + " cambia a " + resto;
            }
            if (tarimaITemporal != tarimasI) {
                observaciones += "- Tarima Incompleta Actualizada:" + tarimaITemporal + " cambia a " + tarimasI;
            }
            if (factorEmpaqueTemporal != factorEmpaque) {
            observaciones += "- Factor de Empaque Actualizado:" + factorEmpaqueTemporal + " cambia a " + factorEmpaque;
            }
            observaciones += " " + usuario;

            ps.clearParameters();
            con.getConn().setAutoCommit(false);
            ps = con.getConn().prepareStatement(UPDATE_COMPRA_TEMPORAL);
            ps.setString(1, lote);
            ps.setString(2, Caducidad);
            ps.setInt(3, marcaClave);
            ps.setInt(4, cantidad);
            ps.setString(5, cb);
            ps.setString(6, observaciones);
            ps.setInt(7, tarimas);
            ps.setInt(8, tcajasC);
            ps.setInt(9, pzacaja);
            ps.setInt(10, cajasi);
            ps.setInt(11, resto);
            ps.setInt(12, tarimasI);
            ps.setDouble(13, cantidad * Double.parseDouble(Costo));
//            ps.setInt(14, id);
            ps.setInt(14, factorEmpaque);
            ps.setInt(15, id);
            ps.execute();
            con.getConn().commit();
            save = true;
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean ActualizarDatosRes(String usuario, String lote, String Caducidad, int cantidad, String cb, String marca, int id, int tarimas, int cajas, String Costo) {
         boolean save2 = false;
        String loteTemporal = "";
        String caducidadTemporal = "";
        int cantidadTemporal = 0;
        String cbTemporal = "";
        int marcaTemporal = 0;
        int marcaClave = 0;
        int tarimaTemporal = 0;
        int cajasTemporal = 0;
        int pzacajaTemporal = 0;
        int cajasiTemporal = 0;
        int restoTemporal = 0;
        int tarimaITemporal = 0;
        String observaciones = "";
        String marcaDescripcion = "";
        String marcaDescripcionTemporal = "";
        int factorEmpaqueTemporal= 0;
        int tcajasC = cajas * tarimas;
        try {
            con.conectar();

            ps = con.getConn().prepareStatement(ID_MARCA);
            ps.setString(1, marca);
            rs = ps.executeQuery();
            while (rs.next()) {
                marcaClave = rs.getInt("F_ClaMar");
                marcaDescripcionTemporal = rs.getString("F_DesMar");
            }

            ps.clearParameters();

            ps = con.getConn().prepareStatement(DATOS_COMPRA_TEMPORAL_PARA_EDITAR);
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                loteTemporal = rs.getString("lote");
                caducidadTemporal = rs.getString("caducidad");
                cbTemporal = rs.getString("cb");
                cantidadTemporal = rs.getInt("cantidad");
                marcaTemporal = rs.getInt("claMar");
                marcaDescripcion = rs.getString("marca");
                observaciones = rs.getString("observaciones");
                tarimaTemporal = rs.getInt(8);
                cajasTemporal = rs.getInt(9);
                pzacajaTemporal = rs.getInt(10);
                cajasiTemporal = rs.getInt(11);
                restoTemporal = rs.getInt(12);
                tarimaITemporal = rs.getInt(13);
            }
            if (!loteTemporal.equals(lote)) {
                observaciones += "- Lote Actualizado:" + loteTemporal + " cambia a " + lote;
            }
            if (!caducidadTemporal.equals(Caducidad)) {
                observaciones += "- Caducidad Actualizada:" + caducidadTemporal + " cambia a " + Caducidad;
            }
            if (cantidadTemporal != cantidad) {
                observaciones += "- Cantidad Actualizada:" + cantidadTemporal + " cambia a " + cantidad;
            }
            if (!cbTemporal.equals(cb)) {
                observaciones += "- Cb Actualizado:" + cbTemporal + " cambia a " + cb;
            }
            if (marcaTemporal != marcaClave) {
                observaciones += "- Marca Actualizada:" + marcaDescripcion + " cambia a " + marca;
            }
            if (tarimaTemporal != tarimas) {
                observaciones += "- Tarima Actualizada:" + tarimaTemporal + " cambia a " + tarimas;
            }
            if (cajasTemporal != cajas) {
                observaciones += "- Cajas Actualizada:" + marcaDescripcion + " cambia a " + cajas;
            }
          
            observaciones += " " + usuario;

            ps.clearParameters();
            con.getConn().setAutoCommit(false);
            ps = con.getConn().prepareStatement(UPDATE_COMPRA_TEMPORAL);
            ps.setString(1, lote);
            ps.setString(2, Caducidad);
            ps.setInt(3, marcaClave);
            ps.setInt(4, cantidad);
            ps.setString(5, cb);
            ps.setString(6, observaciones);
            ps.setInt(7, cantidad);
            ps.setInt(8, cantidad);
            ps.setInt(9, 1);
            ps.setInt(10, 0);
            ps.setInt(11, 0);
            ps.setInt(12, 0);
            ps.setDouble(13, cantidad * Double.parseDouble(Costo));

            ps.setInt(14, 1);
            ps.setInt(15, id);
            ps.execute();
            con.getConn().commit();
            save2 = true;
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save2;
    }
    
    @Override
    public JSONObject datosAditar(int id) {
        JSONObject json = new JSONObject();

        try {
            con.conectar();
            ps = con.getConn().prepareStatement(DATOS_COMPRA_TEMPORAL_PARA_EDITAR);
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int tarimas = rs.getInt(8);
                int tcajasC = rs.getInt(9);
                int cajasC = tcajasC/tarimas;
                json.put("lote", rs.getString(1));
                json.put("cantidad", rs.getInt(2));
                json.put("cb", rs.getString(3));
                json.put("caducidad", rs.getString(4));
                json.put("marca", rs.getString(5));
                json.put("tarimas", rs.getString(8));
                json.put("cajas", cajasC);
                json.put("pzacajas", rs.getString(10));
                json.put("cajasi", rs.getString(11));
                json.put("resto", rs.getString(12));
                json.put("costo", rs.getString(14));
                json.put("factorEmpaque", rs.getString("F_FactorEmpaque"));
                json.put("cantPedido", rs.getString("cantPedido"));
                json.put("cantCompra", rs.getString("cantCompra")); 
                json.put("cantidadTemp", rs.getString("cantidadTemp"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);

        } finally {
            try {
                con.cierraConexion();

            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return json;
    }

    @Override
    public boolean ActualizarlermaCross(String ordenCompra, String remision, String usuarioIngreso) {
        boolean save = false;
        int indiceLote = 0;
        int indiceCompra = 0;
        List<comprasModel> lM = new ArrayList<>();
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(INDICE_LOTE);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceLote = rs.getInt("F_IndLote");
                indiceCompra = rs.getInt("F_IndCom");

            }
            ps = con.getConn().prepareStatement(DATOS_ACTUALIZA_LERMACross);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel compras = new comprasModel();
                compras.setClave(rs.getString(1));
                compras.setLote(rs.getString(2));
                compras.setCaducidad(rs.getString(3));
                compras.setFabricacion(rs.getString(4));
                compras.setMarca(rs.getInt(5));
                compras.setProveedor(rs.getInt(6));
                compras.setCb(rs.getString(7));
                compras.setTarimas(rs.getInt(8));
                compras.setCajas(rs.getInt(9));
                compras.setPz(rs.getInt(10));
                compras.setResto(rs.getInt(11));
                compras.setCosto(rs.getDouble(12));
                compras.setImporteTotal(rs.getDouble(13));
                compras.setCompraTotal(rs.getDouble(14));
                compras.setFolRemi(rs.getString(15));
                compras.setOrdenCompra(rs.getString(16));
                compras.setClaOrg(rs.getInt(17));
                compras.setUser(rs.getString(18));
                compras.setObservaciones(rs.getString(19));
                compras.setOrigen(rs.getInt(20));
                compras.setFolLot(rs.getInt(22));
                compras.setExistenciasUbicacion(rs.getInt(23));
                compras.setExistenciaInsertar(rs.getInt(24));
                compras.setTarimasI(rs.getInt(25));
                compras.setPzaCajas(rs.getInt(26));
                compras.setCajasI(rs.getInt(27));
                compras.setProyecto(rs.getInt(28));
                if (compras.getFolLot() == 0) {
                    compras.setIndiceAInsertar(indiceLote);
                    indiceLote = indiceLote + 1;
                }
                compras.setFecha(rs.getString("FecApl"));
                compras.setHora(rs.getString("horaCaptura"));
                lM.add(compras);

            }
            con.getConn().setAutoCommit(false);
            for (comprasModel c : lM) {
                psInsertKardex = con.getConn().prepareStatement(INSERTAR_KARDEXCross);
                psInsertKardex.setInt(1, indiceCompra);
                psInsertKardex.setString(2, c.getClave());
                psInsertKardex.setInt(3, c.getPz());
                psInsertKardex.setDouble(4, c.getCosto());
                psInsertKardex.setDouble(5, c.getCompraTotal());
                psInsertKardex.setInt(7, c.getProveedor());
                psInsertKardex.setString(8, c.getUser());

                psInsertCompra = con.getConn().prepareStatement(INSERTAR_COMPRA);
                psInsertCompra.setInt(1, indiceCompra);
                psInsertCompra.setInt(2, c.getProveedor());
                psInsertCompra.setString(3, c.getClave());
                psInsertCompra.setInt(4, c.getPz());
                psInsertCompra.setDouble(5, c.getCosto());
                psInsertCompra.setInt(6, c.getCajas());
                psInsertCompra.setDouble(7, c.getImporteTotal());
                psInsertCompra.setDouble(8, c.getCompraTotal());
                psInsertCompra.setString(9, c.getFolRemi());
                psInsertCompra.setString(10, c.getOrdenCompra());
                psInsertCompra.setInt(11, c.getClaOrg());
                psInsertCompra.setString(12, c.getCb());
                psInsertCompra.setString(13, c.getUser());
                psInsertCompra.setString(14, c.getObservaciones());

                if (c.getFolLot() != 0) {
                    psInsertLote = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psInsertLote.setInt(1, c.getExistenciaInsertar());
                    psInsertLote.setInt(2, c.getFolLot());
                    psInsertLote.setString(3, "NUEVACROSS");
                    psInsertLote.setInt(4, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getFolLot());
                    psInsertCompra.setInt(15, c.getFolLot());

                } else {
                    psInsertLote = con.getConn().prepareStatement(INSERTAR_TB_LOTE);
                    psInsertLote.setString(1, c.getClave());
                    psInsertLote.setString(2, c.getLote());
                    psInsertLote.setString(3, c.getCaducidad());
                    psInsertLote.setInt(4, c.getExistenciaInsertar());
                    psInsertLote.setInt(5, c.getIndiceAInsertar());
                    psInsertLote.setInt(6, c.getClaOrg());
                    psInsertLote.setString(7, "NUEVACROSS");
                    psInsertLote.setString(8, c.getFabricacion());
                    psInsertLote.setString(9, c.getCb());
                    psInsertLote.setInt(10, c.getMarca());
                    psInsertLote.setInt(11, c.getOrigen());
                    psInsertLote.setInt(12, c.getProveedor());
                    psInsertLote.setInt(13, 131);
                    psInsertLote.setInt(14, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getIndiceAInsertar());
                    psInsertCompra.setInt(15, c.getIndiceAInsertar());

                }
                psInsertCompra.setInt(16, c.getTarimas());
                psInsertCompra.setInt(17, c.getTarimasI());
                psInsertCompra.setInt(18, c.getCajasI());
                psInsertCompra.setInt(19, c.getPzaCajas());
                psInsertCompra.setInt(20, c.getResto());
                psInsertCompra.setInt(21, c.getProyecto());
                psInsertCompra.setString(31, usuarioIngreso);
                psInsertCompra.setString(32, c.getFecha());
                psInsertCompra.setString(33, c.getHora());
                System.out.println("******  3  "+psInsertCompra.toString());
                psInsertCompra.execute();
                psInsertCompra.clearParameters();

                psInsertCompraRegistro = con.getConn().prepareStatement(INSERTAR_COMPRA_REGISTRO);
                psInsertCompraRegistro.setString(1, c.getClave());
                psInsertCompraRegistro.setString(2, c.getLote());
                psInsertCompraRegistro.setString(3, c.getCaducidad());
                psInsertCompraRegistro.setString(4, c.getFabricacion());
                psInsertCompraRegistro.setInt(5, c.getMarca());
                psInsertCompraRegistro.setInt(6, c.getProveedor());
                psInsertCompraRegistro.setString(7, c.getCb());
                psInsertCompraRegistro.setInt(8, c.getTarimas());
                psInsertCompraRegistro.setInt(9, c.getCajas());
                psInsertCompraRegistro.setInt(10, c.getPz());
                psInsertCompraRegistro.setInt(11, c.getResto());
                psInsertCompraRegistro.setDouble(12, c.getCosto());
                psInsertCompraRegistro.setDouble(13, c.getImporteTotal());
                psInsertCompraRegistro.setDouble(14, c.getCompraTotal());
                psInsertCompraRegistro.setString(15, c.getFolRemi());
                psInsertCompraRegistro.setString(16, c.getOrdenCompra());
                psInsertCompraRegistro.setInt(17, c.getClaOrg());
                psInsertCompraRegistro.setString(18, c.getUser());
                psInsertCompraRegistro.setInt(19, c.getProyecto());

                psInsertCompraRegistro.execute();
                psInsertCompraRegistro.close();

                psInsertKardex.execute();
                psInsertKardex.close();

                psTbCb = con.getConn().prepareStatement(INSERTAR_TB_CB);
                psTbCb.setString(1, c.getCb());
                psTbCb.setString(2, c.getClave());
                psTbCb.setString(3, c.getLote());
                psTbCb.setString(4, c.getCaducidad());
                psTbCb.setString(5, c.getFabricacion());
                psTbCb.setInt(6, c.getMarca());
                psTbCb.execute();
                psTbCb.clearParameters();

                psOperaciones = con.getConn().prepareStatement(UPDATE_TB_PEDIDO_ISEM_2017);
                psOperaciones.setString(1, c.getOrdenCompra());
                psOperaciones.setString(2, c.getClave());
                psOperaciones.execute();
                psOperaciones.clearParameters();

                psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_MOVIMIENTO);
                psUpdateIndice.execute();
                psUpdateIndice.clearParameters();
            }
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_LOTE);
            psUpdateIndice.setInt(1, indiceLote);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZAR_INDICE_COMPRA);
            psUpdateIndice.execute();

            psUpdateIndice = con.getConn().prepareStatement(DELETE_FROM_COMPRA_TEMPORAL);
            psUpdateIndice.setString(1, ordenCompra);
            psUpdateIndice.setString(2, remision);
            psUpdateIndice.execute();

            psUpdateIndice.close();
            con.getConn().commit();
            save = true;

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }

    @Override
    public JSONArray getRegistro(String vOrden, String vRemi) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;

        try {
            con.conectar();
            ps = con.getConn().prepareStatement(BusquedaIdReg);
            ps.setString(1, vOrden);
            ps.setString(2, vRemi);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("IdReg", rs.getString(1));
                jsonArray.add(jsonObj);
            }
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

        ///////////LIBERACION PARCIAL
    @Override
    public boolean IngresoParcial(String IdReg, String ordenCompra, String remision, String usuarioIngreso, String UbicaN) {
        boolean save = false;
        int indiceLote = 0;
        int indiceCompra = 0;
        String UbicaNS = UbicaN;
        List<comprasModel> lM = new ArrayList<>();
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(INDICE_LOTE);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceLote = rs.getInt("F_IndLote");
                indiceCompra = rs.getInt("F_IndCom");

            }
            ps = con.getConn().prepareStatement(DATOS_ACTUALIZA_PARCIAL);
            ps.setString(2, ordenCompra);
            ps.setString(3, remision);
            ps.setString(4, IdReg);
            ps.setString(1, UbicaNS);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel compras = new comprasModel();
                compras.setClave(rs.getString(1));
                compras.setLote(rs.getString(2));
                compras.setCaducidad(rs.getString(3));
                compras.setFabricacion(rs.getString(4));
                compras.setMarca(rs.getInt(5));
                compras.setProveedor(rs.getInt(6));
                compras.setCb(rs.getString(7));
                compras.setTarimas(rs.getInt(8));
                compras.setCajas(rs.getInt(9));
                compras.setPz(rs.getInt(10));
                compras.setResto(rs.getInt(11));
                compras.setCosto(rs.getDouble(12));
                compras.setImporteTotal(rs.getDouble(13));
                compras.setCompraTotal(rs.getDouble(14));
                compras.setFolRemi(rs.getString(15));
                compras.setOrdenCompra(rs.getString(16));
                compras.setClaOrg(rs.getInt(17));
                compras.setUser(rs.getString(18));
                compras.setObservaciones(rs.getString(19));
                compras.setOrigen(rs.getInt(20));
                compras.setFolLot(rs.getInt(22));
                compras.setExistenciasUbicacion(rs.getInt(23));
                compras.setExistenciaInsertar(rs.getInt(24));
                compras.setTarimasI(rs.getInt(25));
                compras.setPzaCajas(rs.getInt(26));
                compras.setCajasI(rs.getInt(27));
                compras.setProyecto(rs.getInt(28));
                if (compras.getFolLot() == 0) {
                    compras.setIndiceAInsertar(indiceLote);
                    indiceLote = indiceLote + 1;
                }
                String queryperdido = "select F_Presentacion, F_UniMed, Plazo_Pago from tb_pedido_sialss where F_NoCompra = '" + ordenCompra + "' and F_Clave = '" + rs.getString(1) + "' ";
                String especial = "select  F_FileFac, F_FecCad, F_Marca,F_FileRemi,F_FolFac, F_FecApl, F_Hora,F_FactorEmpaque,F_OrdenSuministro from tb_compratemp where F_IdCom='" + IdReg + "' ";
                psPedidoIsem = con.getConn().prepareStatement(queryperdido);
                especialQuery = con.getConn().prepareStatement(especial);
                rss = psPedidoIsem.executeQuery();
                while (rss.next()) {
                    compras.setPresentacion(rss.getString(1));
                    compras.setUnimedida(rss.getString(2));
                    compras.setPlazo(rss.getString(3));
                }
                rss = especialQuery.executeQuery();
                while (rss.next()) {
                    compras.setFileFac(rss.getString(1));
                    compras.setCaducidad(rss.getString(2));
                    compras.setMarca(Integer.parseInt(rss.getString(3)));
                    compras.setFileRemi(rss.getString(4));
                    compras.setFolFac(rss.getString(5));
                    compras.setFecha(rss.getString(6));
                    compras.setHora(rss.getString(7));
                    compras.setFactorEmpaque(Integer.parseInt(rss.getString(8)));
                    compras.setOrdenSuministro(rss.getString(9));
                }
                lM.add(compras);

            }
            con.getConn().setAutoCommit(false);
            for (comprasModel c : lM) {
                psInsertKardex = con.getConn().prepareStatement(INSERTAR_KARDEX);
                psInsertKardex.setInt(1, indiceCompra);
                psInsertKardex.setString(2, c.getClave());
                psInsertKardex.setInt(3, c.getPz());
                psInsertKardex.setDouble(4, c.getCosto());
                psInsertKardex.setDouble(5, c.getCompraTotal());
                psInsertKardex.setInt(8, c.getProveedor());
                psInsertKardex.setString(9, usuarioIngreso);
                psInsertKardex.setString(7, UbicaNS);//parametro de ubicacion

                System.out.println("fol lote "+  c.getFolLot() + " lote  "+ c.getIndiceAInsertar() );
                psInsertCompra = con.getConn().prepareStatement(INSERTAR_COMPRA);
                psInsertCompra.setInt(1, indiceCompra);
                psInsertCompra.setInt(2, c.getProveedor());
                psInsertCompra.setString(3, c.getClave());
                psInsertCompra.setInt(4, c.getPz());
                psInsertCompra.setDouble(5, c.getCosto());
                psInsertCompra.setInt(6, c.getCajas());
                psInsertCompra.setDouble(7, c.getImporteTotal());
                psInsertCompra.setDouble(8, c.getCompraTotal());
                psInsertCompra.setString(9, c.getFolRemi());
                psInsertCompra.setString(10, c.getOrdenCompra());
                psInsertCompra.setInt(11, c.getClaOrg());
                psInsertCompra.setString(12, c.getCb());
                psInsertCompra.setString(13, c.getUser());
                psInsertCompra.setString(14, c.getObservaciones());
                
                if (c.getFolLot() != 0) {
                    psInsertLote = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psInsertLote.setInt(1, c.getExistenciaInsertar());
                    psInsertLote.setInt(2, c.getFolLot());
                    psInsertLote.setString(3, UbicaNS);//Ubicacion
                    psInsertLote.setInt(4, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getFolLot());
                    psInsertCompra.setInt(15, c.getFolLot());
                    psInsertCompra.setInt(36, c.getFolLot());

                } else {
                    psInsertLote = con.getConn().prepareStatement(INSERTAR_TB_LOTE);
                    psInsertLote.setString(1, c.getClave());
                    psInsertLote.setString(2, c.getLote());
                    psInsertLote.setString(3, c.getCaducidad());
                    psInsertLote.setInt(4, c.getExistenciaInsertar());
                    psInsertLote.setInt(5, c.getIndiceAInsertar());
                    psInsertLote.setInt(6, c.getClaOrg());
                    psInsertLote.setString(7, UbicaNS);//Ubicacion
                    psInsertLote.setString(8, c.getFabricacion());
                    psInsertLote.setString(9, c.getCb());
                    psInsertLote.setInt(10, c.getMarca());
                    psInsertLote.setInt(11, c.getOrigen());
                    psInsertLote.setInt(12, c.getProveedor());
                    psInsertLote.setInt(13, 131);
                    psInsertLote.setInt(14, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getIndiceAInsertar());
                    psInsertCompra.setInt(15, c.getIndiceAInsertar());
                    psInsertCompra.setInt(36, c.getIndiceAInsertar());

                }
                psInsertCompra.setInt(16, c.getTarimas());
                psInsertCompra.setInt(17, c.getTarimasI());
                psInsertCompra.setInt(18, c.getCajasI());
                psInsertCompra.setInt(19, c.getPzaCajas());
                psInsertCompra.setInt(20, c.getResto());
                psInsertCompra.setInt(21, c.getProyecto());
                psInsertCompra.setString(22, c.getPlazo());
                psInsertCompra.setString(23, c.getUnimedida());
                psInsertCompra.setString(24, c.getPresentacion());
                psInsertCompra.setInt(25, c.getOrigen());
                psInsertCompra.setString(26, c.getFileFac());
                psInsertCompra.setString(27, c.getCaducidad());
                psInsertCompra.setInt(28, c.getMarca());
                psInsertCompra.setString(29, c.getFileRemi());
                psInsertCompra.setString(30, c.getFolFac());
                psInsertCompra.setString(31, usuarioIngreso);
                psInsertCompra.setString(32, c.getFecha());
                psInsertCompra.setString(33, c.getHora());
                psInsertCompra.setInt(34, c.getFactorEmpaque());
                psInsertCompra.setString(35, c.getOrdenSuministro());
                System.out.println("******  4  "+psInsertCompra.toString());
                psInsertCompra.execute();
                psInsertCompra.clearParameters();

                psInsertCompraRegistro = con.getConn().prepareStatement(INSERTAR_COMPRA_REGISTRO);
                psInsertCompraRegistro.setString(1, c.getClave());
                psInsertCompraRegistro.setString(2, c.getLote());
                psInsertCompraRegistro.setString(3, c.getCaducidad());
                psInsertCompraRegistro.setString(4, c.getFabricacion());
                psInsertCompraRegistro.setInt(5, c.getMarca());
                psInsertCompraRegistro.setInt(6, c.getProveedor());
                psInsertCompraRegistro.setString(7, c.getCb());
                psInsertCompraRegistro.setInt(8, c.getTarimas());
                psInsertCompraRegistro.setInt(9, c.getCajas());
                psInsertCompraRegistro.setInt(10, c.getPz());
                psInsertCompraRegistro.setInt(11, c.getResto());
                psInsertCompraRegistro.setDouble(12, c.getCosto());
                psInsertCompraRegistro.setDouble(13, c.getImporteTotal());
                psInsertCompraRegistro.setDouble(14, c.getCompraTotal());
                psInsertCompraRegistro.setString(15, c.getFolRemi());
                psInsertCompraRegistro.setString(16, c.getOrdenCompra());
                psInsertCompraRegistro.setInt(17, c.getClaOrg());
                psInsertCompraRegistro.setString(18, c.getUser());
                psInsertCompraRegistro.setInt(19, c.getProyecto());

                psInsertCompraRegistro.execute();
                psInsertCompraRegistro.close();

                psInsertKardex.execute();
                psInsertKardex.close();

                psTbCb = con.getConn().prepareStatement(INSERTAR_TB_CB);
                psTbCb.setString(1, c.getCb());
                psTbCb.setString(2, c.getClave());
                psTbCb.setString(3, c.getLote());
                psTbCb.setString(4, c.getCaducidad());
                psTbCb.setString(5, c.getFabricacion());
                psTbCb.setInt(6, c.getMarca());
                psTbCb.execute();
                psTbCb.clearParameters();

                 //cerrar OC en Pedidos ISem
//                ps = con.getConn().prepareStatement(BUSCA_TB_PEDIDO_ISEM_2017);
//                ps.setString(1, c.getClave());
//                ps.setString(2, ordenCompra);
//                rs = ps.executeQuery();
//                int Dif = 0;
//                while (rs.next()) {
//                    Dif = Integer.parseInt(rs.getString(4));
//                }
//                if (Dif > 0) {
//                
                psOperaciones = con.getConn().prepareStatement(UPDATE_TB_PEDIDO_ISEM_2017);
                psOperaciones.setString(1, c.getOrdenCompra());
                psOperaciones.setString(2, c.getClave());
                psOperaciones.execute();
                psOperaciones.clearParameters();
//                }
//                psOperaciones.clearParameters();
                psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_MOVIMIENTO);
                psUpdateIndice.execute();
                psUpdateIndice.clearParameters();
            }
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_LOTE);
            psUpdateIndice.setInt(1, indiceLote);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZAR_INDICE_COMPRA);
            psUpdateIndice.execute();

            psUpdateIndice = con.getConn().prepareStatement(DELETE_FROM_COMPRA_TEMPORALParcial);
            psUpdateIndice.setString(1, ordenCompra);
            psUpdateIndice.setString(2, remision);
            psUpdateIndice.setString(3, IdReg);
            psUpdateIndice.execute();

            psUpdateIndice.close();
            
            String queryValida = "select( (select sum(F_Cant) from `tb_pedido_sialss` where F_noCompra = '" + ordenCompra + "' ) - (select sum(F_CanCom) from tb_compra where F_OrdCom='" + ordenCompra + "')) as res";
            System.out.println(queryValida);
            psPedidoIsem = con.getConn().prepareStatement(queryValida);
            rss = con.consulta(queryValida);
            int pivote = 0;
            ArrayList<String> listaVal = new ArrayList();
            while (rss.next()) {
                listaVal.add(rss.getString(1));
                pivote = Integer.parseInt(listaVal.get(0)==null? "-1": listaVal.get(0));
            }
            if (pivote == 0) {
                con.actualizar("update `tb_pedido_sialss` set F_Recibido = 1 where F_noCompra = '" + ordenCompra + "'");
            }
            
            con.getConn().commit();
            
             /*****para enviar correo*****/
            ps = con.getConn().prepareStatement(BusquedaQFB);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
             String Cclave = "", Rclave = "", Aclave = "";
            while (rs.next()) {
               Cclave = rs.getString(1);               
               }
             ps = con.getConn().prepareStatement(clavesQFBR);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
            while (rs.next()) {
               Rclave = rs.getString(1);               
               }
             ps = con.getConn().prepareStatement(clavesQFBA);
            ps.setString(1, ordenCompra);
            ps.setString(2, remision);
            rs = ps.executeQuery();
           
            while (rs.next()) {
               Aclave = rs.getString(1);               
               }
           
            if(!Cclave.equals("") || !Rclave.equals("") || !Aclave.equals("")){
                correoConfirma.enviaCorreo(ordenCompra,remision);
            }
             /*****fin enviar correo*****/
            save = true;

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }
    
    /*Remisionar Extraordinarios por parte de compras*/
    @Override
    public boolean IngresarExtra(String IdUsu, String usuarioIngreso) {
        boolean save = false;
        int indiceLote = 0;
        int indiceCompra = 0;
        List<comprasModel> lM = new ArrayList<>();
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(INDICE_LOTE);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceLote = rs.getInt("F_IndLote");
                indiceCompra = rs.getInt("F_IndCom");

            }
            ps = con.getConn().prepareStatement(DATOS_EXTRAORDINARIO);
            ps.setString(1, IdUsu);
            ps.setInt(2, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                comprasModel compras = new comprasModel();
                compras.setClave(rs.getString(1));
                compras.setLote(rs.getString(2));
                compras.setCaducidad(rs.getString(3));
                compras.setFabricacion(rs.getString(4));
                compras.setMarca(rs.getInt(5));
                compras.setProveedor(rs.getInt(6));
                compras.setCb(rs.getString(7));
                compras.setTarimas(1);
                compras.setCajas(1);
                compras.setPz(rs.getInt(8));
                compras.setResto(rs.getInt(8));
                compras.setCosto(rs.getDouble("costo"));
                compras.setImporteTotal(rs.getDouble("impTo"));
                compras.setCompraTotal(rs.getDouble("comTot"));
                compras.setFolRemi(rs.getString(9));
                compras.setOrdenCompra(rs.getString(9));
                compras.setClaOrg(rs.getInt(6));
                compras.setUser(rs.getString(10));
                compras.setObservaciones("Extraordinario");
                compras.setOrigen(2);
                compras.setFolLot(rs.getInt(12));
                compras.setExistenciasUbicacion(rs.getInt(13));
                compras.setExistenciaInsertar(rs.getInt(14));
                compras.setTarimasI(0);
                compras.setPzaCajas(1);
                compras.setCajasI(0);
                compras.setProyecto(rs.getInt(15));
                if (compras.getFolLot() == 0) {
                    compras.setIndiceAInsertar(indiceLote);
                    indiceLote = indiceLote + 1;
                }
                compras.setFecha(rs.getString("F_Fecha"));
                compras.setHora(rs.getString("horaCaptura"));
                compras.setFactorEmpaque(rs.getInt("FactorEmpaque"));
                lM.add(compras);

            }
            con.getConn().setAutoCommit(false);
            for (comprasModel c : lM) {
                psInsertKardex = con.getConn().prepareStatement(INSERTAR_KARDEXTRA);
                psInsertKardex.setInt(1, indiceCompra);
                psInsertKardex.setString(2, c.getClave());
                psInsertKardex.setInt(3, c.getPz());
                psInsertKardex.setDouble(4, c.getCosto());
                psInsertKardex.setDouble(5, c.getCompraTotal());
                psInsertKardex.setInt(7, c.getProveedor());
                psInsertKardex.setString(8, c.getUser());

                psInsertCompra = con.getConn().prepareStatement(INSERTAR_COMPRA);
                psInsertCompra.setInt(1, indiceCompra);
                psInsertCompra.setInt(2, c.getProveedor());
                psInsertCompra.setString(3, c.getClave());
                psInsertCompra.setInt(4, c.getPz());
                psInsertCompra.setDouble(5, c.getCosto());
                psInsertCompra.setInt(6, c.getCajas());
                psInsertCompra.setDouble(7, c.getImporteTotal());
                psInsertCompra.setDouble(8, c.getCompraTotal());
                psInsertCompra.setString(9, c.getFolRemi());
                psInsertCompra.setString(10, c.getOrdenCompra());
                psInsertCompra.setInt(11, c.getClaOrg());
                psInsertCompra.setString(12, c.getCb());
                psInsertCompra.setString(13, c.getUser());
                psInsertCompra.setString(14, c.getObservaciones());

                if (c.getFolLot() != 0) {
                    psInsertLote = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psInsertLote.setInt(1, c.getExistenciaInsertar());
                    psInsertLote.setInt(2, c.getFolLot());
                    psInsertLote.setString(3, "EXTRA_ORDINARIA");
                    psInsertLote.setInt(4, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getFolLot());
                    psInsertCompra.setInt(15, c.getFolLot());

                } else {
                    psInsertLote = con.getConn().prepareStatement(INSERTAR_TB_LOTE);
                    psInsertLote.setString(1, c.getClave());
                    psInsertLote.setString(2, c.getLote());
                    psInsertLote.setString(3, c.getCaducidad());
                    psInsertLote.setInt(4, c.getExistenciaInsertar());
                    psInsertLote.setInt(5, c.getIndiceAInsertar());
                    psInsertLote.setInt(6, c.getClaOrg());
                    psInsertLote.setString(7, "EXTRA_ORDINARIA");
                    psInsertLote.setString(8, c.getFabricacion());
                    psInsertLote.setString(9, c.getCb());
                    psInsertLote.setInt(10, c.getMarca());
                    psInsertLote.setInt(11, c.getOrigen());
                    psInsertLote.setInt(12, c.getProveedor());
                    psInsertLote.setInt(13, 131);
                    psInsertLote.setInt(14, c.getProyecto());
                    psInsertLote.execute();
                    psInsertKardex.setInt(6, c.getIndiceAInsertar());
                    psInsertCompra.setInt(15, c.getIndiceAInsertar());

                }
                psInsertCompra.setInt(16, c.getTarimas());
                psInsertCompra.setInt(17, c.getTarimasI());
                psInsertCompra.setInt(18, c.getCajasI());
                psInsertCompra.setInt(19, c.getPzaCajas());
                psInsertCompra.setInt(20, c.getResto());
                psInsertCompra.setInt(21, c.getProyecto());
                psInsertCompra.setString(22, c.getPlazo());
                psInsertCompra.setString(23, c.getUnimedida());
                psInsertCompra.setString(24, c.getPresentacion());
                psInsertCompra.setInt(25, c.getOrigen());
                psInsertCompra.setString(26, c.getFileFac());
                psInsertCompra.setString(27, c.getCaducidad());
                psInsertCompra.setInt(28, c.getMarca());
                psInsertCompra.setString(29, c.getFileRemi());
                psInsertCompra.setString(30, c.getFolFac());
                psInsertCompra.setString(31, usuarioIngreso);
                psInsertCompra.setString(32, c.getFecha());
                psInsertCompra.setString(33, c.getHora());
                psInsertCompra.setInt(34, c.getFactorEmpaque());
                psInsertCompra.setString(35, " ");
                System.out.println("******   5 "+psInsertCompra.toString());
                psInsertCompra.execute();
                psInsertCompra.clearParameters();

                psInsertCompraRegistro = con.getConn().prepareStatement(INSERTAR_COMPRA_REGISTRO);
                psInsertCompraRegistro.setString(1, c.getClave());
                psInsertCompraRegistro.setString(2, c.getLote());
                psInsertCompraRegistro.setString(3, c.getCaducidad());
                psInsertCompraRegistro.setString(4, c.getFabricacion());
                psInsertCompraRegistro.setInt(5, c.getMarca());
                psInsertCompraRegistro.setInt(6, c.getProveedor());
                psInsertCompraRegistro.setString(7, c.getCb());
                psInsertCompraRegistro.setInt(8, c.getTarimas());
                psInsertCompraRegistro.setInt(9, c.getCajas());
                psInsertCompraRegistro.setInt(10, c.getPz());
                psInsertCompraRegistro.setInt(11, c.getResto());
                psInsertCompraRegistro.setDouble(12, c.getCosto());
                psInsertCompraRegistro.setDouble(13, c.getImporteTotal());
                psInsertCompraRegistro.setDouble(14, c.getCompraTotal());
                psInsertCompraRegistro.setString(15, c.getFolRemi());
                psInsertCompraRegistro.setString(16, c.getOrdenCompra());
                psInsertCompraRegistro.setInt(17, c.getClaOrg());
                psInsertCompraRegistro.setString(18, c.getUser());
                psInsertCompraRegistro.setInt(19, c.getProyecto());

                psInsertCompraRegistro.execute();
                psInsertCompraRegistro.close();

                psInsertKardex.execute();
                psInsertKardex.close();

                psTbCb = con.getConn().prepareStatement(INSERTAR_TB_CB);
                psTbCb.setString(1, c.getCb());
                psTbCb.setString(2, c.getClave());
                psTbCb.setString(3, c.getLote());
                psTbCb.setString(4, c.getCaducidad());
                psTbCb.setString(5, c.getFabricacion());
                psTbCb.setInt(6, c.getMarca());
                psTbCb.execute();
                psTbCb.clearParameters();

                psOperaciones = con.getConn().prepareStatement(UPDATE_EXTRAORDINARIO);
                psOperaciones.setString(1, c.getOrdenCompra());
                psOperaciones.setString(2, IdUsu);
                psOperaciones.setString(3, c.getClave());
                psOperaciones.setInt(4, 1);
                psOperaciones.execute();
                psOperaciones.clearParameters();

                psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_MOVIMIENTO);
                psUpdateIndice.execute();
                psUpdateIndice.clearParameters();
            }
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZA_INDICE_LOTE);
            psUpdateIndice.setInt(1, indiceLote);
            psUpdateIndice.execute();

            psUpdateIndice.clearParameters();
            psUpdateIndice = con.getConn().prepareStatement(ACTUALIZAR_INDICE_COMPRA);
            psUpdateIndice.execute();

            psUpdateIndice.close();
            con.getConn().commit();
            save = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(InterfaceSendetoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }



}
