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
    response.setHeader("Content-Disposition", "attachment;filename=\"Nivel_Abasto_Diario_" + fecha_ini + "_al_" + fecha_fin + ".xls\"");
%>
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
                     <td colspan="5"> <%=fechaDia%></td>
                </tr><tr></tr><tr></tr>
                <tr>
                    <th colspan="8"><h1>Reporte de Nivel de Abasto</h1></th>
                </tr>
                <tr></tr>                
            </table>
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Nivel</td>
                        <td>No. Unidades</td>
                        <td>Requerido</td>
                        <td>Surtido</td>
                        <td>No Surtido</td>
                        <td>Fill Ride</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                int CantReq = 0, CantSur = 0, Unidades = 0, Diferencia = 0;
                                ResultSet rset = null;
                                if (fecha_ini != "" && fecha_fin != "") {
                                    rset = con.consulta("SELECT U.F_Tipo, CONT.CONTAR, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO, ROUND((( SUM(F.F_CantSur) / (SUM(F.F_CantReq))) * 100 ), 2 ) AS Porcentaje FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN ( SELECT U.F_Tipo, COUNT(DISTINCT TIPO.F_ClaCli) AS CONTAR FROM tb_uniatn U INNER JOIN ( SELECT U.F_Tipo, U.F_ClaCli FROM tb_factura F LEFT JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_ClaCli ) AS TIPO ON U.F_Tipo = TIPO.F_Tipo GROUP BY U.F_Tipo ) AS CONT ON U.F_Tipo = CONT.F_Tipo WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_Tipo;");

                                    while (rset.next()) {
                    %>
                    <tr>
                        <td><%=rset.getString(1)%></td>
                        <td><%=formatter.format(rset.getInt(2))%></td>
                        <td><%=formatter.format(rset.getInt(3))%></td>
                        <td><%=formatter.format(rset.getInt(4))%></td>
                        <td><%=formatter.format(rset.getInt(5))%></td>
                        <td><%=rset.getString(6)%>%</td>
                    </tr>
                    <%
                            Unidades = Unidades + rset.getInt(2);
                            CantReq = CantReq + rset.getInt(3);
                            CantSur = CantSur + rset.getInt(4);
                            Diferencia = Diferencia + rset.getInt(5);
                        }
                    %>
                    <tr>
                        <td>Total</td>
                        <td><%=formatter.format(Unidades)%></td>
                        <td><%=formatter.format(CantReq)%></td>
                        <td><%=formatter.format(CantSur)%></td>
                        <td><%=formatter.format(Diferencia)%></td>
                        <td><%=formatterDecimal.format(((float) CantSur / CantReq) * 100)%>%</td>
                    </tr>
                    <%
                                    CantReq = 0;
                                    CantSur = 0;
                                    Unidades = 0;
                                    Diferencia = 0;

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
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-striped" id="datosCompras" ></table>
        </div>
    </div>
</div>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-striped" id="datosCompras"></table>
        </div>
    </div>
</div>

<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Clave</td>
                        <td>Cant. Req.</td>
                        <td>Cant. Surt.</td>
                        <td>No Surt.</td>
                        <td>Juris</td>
                        <td>Rural</td>
                        <td>CSU</td>
                        <td>H General</td>
                        <td>H Integral</td>
                        <td>H Regional</td>
                        <td>Comunitario</td>
                        <td>Existencia CD</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                int CantReq = 0, CantSur = 0, Unidades = 0, Diferencia = 0;
                                int SJR=0,SRURAL=0,SCSU=0,SHG=0,SHI=0,SHC=0,SHR=0,EXIST=0;
                                ResultSet rset = null;
                                if (fecha_ini != "" && fecha_fin != "") {
                                    rset = con.consulta("SELECT F.F_ClaPro, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO, IFNULL(JR.pzasNO, 0) AS SJR, IFNULL(RURAL.pzasNO, 0) AS SRURAL, IFNULL(CSU.pzasNO, 0) AS SCSU, IFNULL(HG.pzasNO, 0) AS SHG, IFNULL(HI.pzasNO, 0) AS SHI, IFNULL(HR.pzasNO, 0) AS SHR, IFNULL(HC.pzasNO, 0) AS SHC, IFNULL(EXIS.F_ExiLot, 0) AS SEXIST FROM tb_factura F LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'RURAL' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS RURAL ON F.F_ClaPro = RURAL.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'CENTRO_DE_SALUD_URBANOS' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS CSU ON F.F_ClaPro = CSU.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'HOSPITAL_GENERAL' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS HG ON F.F_ClaPro = HG.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'HOSPITAL_INTEGRAL' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS HI ON F.F_ClaPro = HI.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'HOSPITAL_REGIONAL' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS HR ON F.F_ClaPro = HR.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'HOSPITAL_COMUNITARIO' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS HC ON F.F_ClaPro = HC.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' AND U.F_Tipo = 'JURISDICCION_SANITARIA' GROUP BY F.F_ClaPro HAVING pzasNO > 0 ) AS JR ON F.F_ClaPro = JR.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS F_ExiLot FROM tb_lote WHERE F_Proyecto = 2 AND F_Ubica NOT IN ('CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA','CROSSDOCKMORELIA','INGRESOS_V','CUARENTENA','FALTANTE_ORIGEN','AT10','AT11','AT12','AT13','AT14','AT15','AT16','AT17','AT18','AT19','AT2','AT20','AT21','AT22','AT23','AT24','AT25','AT26','AT27','AT28','AT29','AT3','AT30','AT31','AT32','AT4','AT5','AT6','AT7','AT8','AT9') GROUP BY F_ClaPro HAVING F_ExiLot > 0 ) AS EXIS ON F.F_ClaPro = EXIS.F_ClaPro WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro HAVING pzasNO > 0;");

                                    while (rset.next()) {
                    %>
                    <tr>
                        <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                        <td><%=formatter.format(rset.getInt(2))%></td>
                        <td><%=formatter.format(rset.getInt(3))%></td>
                        <td><%=formatter.format(rset.getInt(4))%></td>
                        <td><%=formatter.format(rset.getInt(5))%></td>
                        <td><%=formatter.format(rset.getInt(6))%></td>
                        <td><%=formatter.format(rset.getInt(7))%></td>
                        <td><%=formatter.format(rset.getInt(8))%></td>
                        <td><%=formatter.format(rset.getInt(9))%></td>
                        <td><%=formatter.format(rset.getInt(10))%></td>
                        <td><%=formatter.format(rset.getInt(11))%></td>
                        <td><%=formatter.format(rset.getInt(12))%></td>
                    </tr>
                    <%
                            CantReq += rset.getInt(2);
                            CantSur += rset.getInt(3);
                            Diferencia += rset.getInt(4);
                            SJR += rset.getInt(5);
                            SRURAL += rset.getInt(6);
                            SCSU += rset.getInt(7);
                            SHG += rset.getInt(8);
                            SHI += rset.getInt(9);
                            SHR += rset.getInt(10);
                            SHC += rset.getInt(11);
                            EXIST += rset.getInt(12);
                        }
                    %>
                    <tr>
                        <td>Total</td>
                        <td><%=formatter.format(CantReq)%></td>
                        <td><%=formatter.format(CantSur)%></td>
                        <td><%=formatter.format(Diferencia)%></td>
                        <td><%=formatter.format(SJR)%></td>
                        <td><%=formatter.format(SRURAL)%></td>
                        <td><%=formatter.format(SCSU)%></td>
                        <td><%=formatter.format(SHG)%></td>
                        <td><%=formatter.format(SHI)%></td>
                        <td><%=formatter.format(SHR)%></td>
                        <td><%=formatter.format(SHC)%></td>
                        <td><%=formatter.format(EXIST)%></td>
                    </tr>
                    <%
                                    CantReq = 0;
                                    CantSur = 0;
                                    Unidades = 0;
                                    Diferencia = 0;

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