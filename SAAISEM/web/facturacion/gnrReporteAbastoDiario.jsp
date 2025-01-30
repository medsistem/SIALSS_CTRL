<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>

<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "";
    try {
        //if (request.getParameter("accion").equals("buscar")) {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");

        //}
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Reporte_Abasto_Diario_" + fecha_ini + "_al_" + fecha_fin + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                    %>
                <tr>
                    <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                     <td colspan="8"> <%=fechaDia%></td>
                </tr><tr></tr><tr></tr>
                <tr>
                    <th colspan="11"><h1>Reporte de Abasto</h1></th>
                </tr>
                <tr></tr>                
            </table>
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td colspan="3"></td>
                                <td colspan="1" style="text-align: center">Ubicación</td>
                                <td colspan="3" style="text-align: center">Hospitales</td>
                                <td></td>
                                <td colspan="1" style="text-align: center">Ubicaciones</td>
                                <td colspan="3" style="text-align: center">CSU, RURAL, ALMACÉN Y JURIS</td>
                                <td></td>
                                <td colspan="2" style="text-align: center">Inventario</td>
                            </tr>
                            <tr>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Prioridad</td>
                                <td>AF</td>
                                <td>REQUERIDO</td>
                                <td>SURTIDO</td>
                                <td>NO SURTIDO</td>
                                <td></td>
                                <td>MODULA, MODULA2, AS, APE, DENTAL Y RED FRÍA</td>
                                <td>REQUERIDO</td>
                                <td>SURTIDO</td>
                                <td>NO SURTIDO</td>
                                <td></td>
                                <td>ALMACÉN</td>
                                <td>TOTAL</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        int CantReq = 0, CantSur = 0, Unidades = 0, Diferencia = 0;
                                        int SJR = 0, SRURAL = 0, SCSU = 0, SHG = 0, SHI = 0, SHC = 0, SHR = 0, EXIST = 0;
                                        ResultSet rset = null;
                                        if (fecha_ini != "" && fecha_fin != "") {
                                            rset = con.consulta("SELECT F.F_ClaPro, M.F_DesPro, M.F_Prioridad, IFNULL(UBIAF.F_ExiLot, 0) AS EXIAF, IFNULL(UBIMOD.F_ExiLot, 0) AS EXIMOD, IFNULL(HOSP.F_CantReq, 0) AS CantReqH, IFNULL(HOSP.F_CantSur, 0) AS CantSurH, IFNULL(HOSP.DIF, 0) AS DIFH, IFNULL(RURAL.F_CantReq, 0) AS CantReqR, IFNULL(RURAL.F_CantSur, 0) AS CantSurR, IFNULL(RURAL.DIF, 0) AS DIFR, IFNULL(LT.F_ExiLot, 0) - ( IFNULL(UBIAF.F_ExiLot, 0) + IFNULL(UBIMOD.F_ExiLot, 0)) AS INVALMACEN, IFNULL(LT.F_ExiLot, 0) AS EXILOTE FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS F_CantReq, SUM(F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F_CantSur) AS DIF FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo LIKE '%HOSPITAL%' GROUP BY F.F_ClaPro ) AS HOSP ON F.F_ClaPro = HOSP.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS F_CantReq, SUM(F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F_CantSur) AS DIF FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo IN ( 'ALMACEN', 'CENTRO_DE_SALUD_URBANOS', 'JURISDICCION_SANITARIA', 'RURAL' ) GROUP BY F.F_ClaPro ) AS RURAL ON F.F_ClaPro = RURAL.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS F_ExiLot FROM tb_lote WHERE F_Proyecto = 2 AND F_Ubica NOT IN ('CADUCADOS','PROXACADUCAR','MERMA','CROSSDOCKMORELIA','INGRESOS_V','CUARENTENA','EXTRA_ORDINARIA','FALTANTE_ORIGEN','AT10','AT11','AT12','AT13','AT14','AT15','AT16','AT17','AT18','AT19','AT2','AT20','AT21','AT22','AT23','AT24','AT25','AT26','AT27','AT28','AT29','AT3','AT30','AT31','AT32','AT4','AT5','AT6','AT7','AT8','AT9') GROUP BY F_ClaPro ) AS LT ON F.F_ClaPro = LT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS F_ExiLot FROM tb_lote WHERE F_Proyecto = 2 AND F_Ubica = 'AF' GROUP BY F_ClaPro ) AS UBIAF ON F.F_ClaPro = UBIAF.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS F_ExiLot FROM tb_lote WHERE F_Proyecto = 2 AND F_Ubica IN ( 'MODULA', 'MODULA2', 'AS', 'APE', 'DENTAL', 'REDFRIA' ) GROUP BY F_ClaPro ) AS UBIMOD ON F.F_ClaPro = UBIMOD.F_ClaPro WHERE F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro;");

                                            while (rset.next()) {
                            %>
                            <tr>
                                <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=formatter.format(rset.getInt(4))%></td>
                                <td><%=formatter.format(rset.getInt(6))%></td>
                                <td><%=formatter.format(rset.getInt(7))%></td>
                                <td><%=formatter.format(rset.getInt(8))%></td>
                                <td></td>
                                <td><%=formatter.format(rset.getInt(5))%></td>
                                <td><%=formatter.format(rset.getInt(9))%></td>
                                <td><%=formatter.format(rset.getInt(10))%></td>
                                <td><%=formatter.format(rset.getInt(11))%></td>
                                <td></td>
                                <td><%=formatter.format(rset.getInt(12))%></td>
                                <td><%=formatter.format(rset.getInt(13))%></td>
                            </tr>
                            <%
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {

                                }
                            %>

                        </tbody>
                    </table>
                </div>
                <br />
            </div>
        </div>
    </body>
</html>