<%-- 
    Document   : gnkKardexClave
    Created on : 22-oct-2014, 8:39:49
    Author     : amerikillo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    ConectionDB con = new ConectionDB();
    String FechaIni = request.getParameter("FechaIni");
    String FechaFin = request.getParameter("FechaFin");

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"PlantillaSAE_" + FechaIni + "_" + FechaFin + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <table border="1">
            <thead> 
                <tr>
                    <td>Clave</td>
                    <td>Proveedor</td>
                    <td>Referencia_proveedor</td>
                    <td>Fecha_documento</td>
                    <td>Fecha_recepcion</td>
                    <td>Entregar_A</td>
                    <td>Numero_almacen_cabecera</td>
                    <td>Numero_moneda</td>
                    <td>Tipo_Cambio</td>
                    <td>Descuento_financiero</td>
                    <td>Observaciones</td>
                    <td>Clave_articulo</td>
                    <td>Cantidad</td>
                    <td>Costo</td>
                    <td>Impuesto1</td>
                    <td>Impuesto2</td>
                    <td>Impuesto3</td>
                    <td>IVA</td>
                    <td>Descuento_Partida</td>
                    <td>Unidad_venta</td>
                    <td>Factor_conversion</td>
                    <td>Clave_esquema_impuestos</td>
                    <td>Observaciones_partida</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Fabricacion</td>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        con.conectar();
                        ResultSet rset = null;
                        rset = con.consulta("SELECT C.F_ClaDoc AS Clave, C.F_ProVee AS Proveedor, C.F_FolRemi AS Referencia_proveedor, DATE_FORMAT(C.F_FecApl, '%d/%m/%Y') AS Fecha_documento, DATE_FORMAT(C.F_FecApl, '%d/%m/%Y') AS Fecha_recepcion, '' AS Entregar_A, '1' AS Numero_almacen_cabecera, '1' AS Numero_moneda, '1' AS Tipo_Cambio, '0.00' AS Descuento_financiero, C.F_Obser AS Observaciones, C.F_ClaPro AS Clave_articulo, SUM(C.F_CanCom) AS Cantidad, C.F_Costo AS Costo, '0.00' AS Impuesto1, '0.00' AS Impuesto2, '0.00' AS Impuesto3, CASE WHEN M.F_TipMed = 2504 THEN '0.00' ELSE '16' END AS IVA, '0.00' AS Descuento_Partida, IFNULL(M.F_PrePro, '') AS Unidad_venta, '' AS Factor_conversion, '1' AS Clave_esquema_impuestos, C.F_Obser AS Observaciones_partida, L.F_ClaLot AS Lote, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS Caducidad, DATE_FORMAT(L.F_FecFab, '%d/%m/%Y') AS Fabricacion FROM tb_compra C LEFT JOIN tb_medica M ON C.F_ClaPro = M.F_ClaPro INNER JOIN tb_lote L ON C.F_ClaPro = L.F_ClaPro AND C.F_Lote = L.F_FolLot AND C.F_Proyecto = L.F_Proyecto WHERE C.F_FecApl BETWEEN '" + FechaIni + "' AND '" + FechaFin + "' GROUP BY C.F_ClaDoc, C.F_ProVee, C.F_FecApl, C.F_FolRemi, C.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_FecFab;;");

                        while (rset.next()) {
                %>
                <tr>
                    <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(3)%></td>
                    <td ><%=rset.getString(4)%></td>
                    <td ><%=rset.getString(5)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(6)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(7)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(8)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(9)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(10)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(11)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(12)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(13)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(14)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(15)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(16)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(17)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(18)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(19)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(20)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(21)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(22)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(23)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(24)%></td>
                    <td ><%=rset.getString(25)%></td>
                    <td ><%=rset.getString(26)%></td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                %>
            </tbody>
        </table>        
    </body>
</html>
