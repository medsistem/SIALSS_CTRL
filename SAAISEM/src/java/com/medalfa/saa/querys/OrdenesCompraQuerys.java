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
public class OrdenesCompraQuerys 
{
    public static final String OBTENER_ORDENES_COMPRA = "SELECT pi.F_NoCompra FROM tb_pedido_sialss pi WHERE pi.F_Proyecto in(2,14,15) AND pi.F_NoCompra NOT IN(SELECT no_orden_compra FROM estatus_compras WHERE estatus='CERRADO') GROUP BY pi.F_NoCompra";
    public static final String OBTENER_PROVEEDORES_PEDIDOS = "SELECT pi.F_Provee, p.F_NomPro FROM tb_pedido_sialss pi INNER JOIN estatus_compras ec ON ec.no_orden_compra <> pi.F_NoCompra INNER JOIN tb_proveedor p ON pi.F_Provee = p.F_ClaProve WHERE pi.F_Proyecto in(2,14,15) GROUP BY pi.F_Provee";
    public static final String OBTENER_POR_PROVEEDOR = "SELECT pi.F_NoCompra, pi.F_FecSur, p.F_NomPro FROM tb_pedido_sialss pi INNER JOIN tb_proveedor p ON p.F_ClaProve = pi.F_Provee WHERE pi.F_Provee = ? AND pi.F_Proyecto in(2,14,15) AND pi.F_NoCompra NOT IN(SELECT no_orden_compra FROM estatus_compras WHERE estatus='CERRADO') GROUP BY pi.F_NoCompra ORDER BY pi.F_FecSur";
    public static final String OBTENER_POR_NO_ORDEN = "SELECT pi.F_NoCompra, pi.F_FecSur, p.F_NomPro FROM tb_pedido_sialss pi INNER JOIN tb_proveedor p ON p.F_ClaProve = pi.F_Provee WHERE pi.F_NoCompra = ? AND pi.F_Proyecto in(2,14,15) GROUP BY pi.F_NoCompra ORDER BY pi.F_FecSur";
    public static final String INSERT_ESTATUS_ORDEN = "INSERT INTO estatus_compras SET no_orden_compra = ?, usuario_cierre = ? ,fecha_cerrado = NOW(), estatus='CERRADO';";
    public static final String OBTENER_REPORTE_ORDEN_CERRADAS = "SELECT ec.no_orden_compra AS noOrden, ec.fecha_cerrado AS fechaCerrado, CONCAT(tbu.F_Nombre,' ',tbu.F_Apellido, ' ', tbu.F_ApellidoM) AS usuario FROM estatus_compras ec INNER JOIN tb_usuario tbu ON ec.usuario_cierre = tbu.F_IdUsu WHERE ec.estatus = 'CERRADO' AND ec.no_orden_compra <> '-';";
}
