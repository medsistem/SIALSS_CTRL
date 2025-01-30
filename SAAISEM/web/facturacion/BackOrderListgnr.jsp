<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

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

    String fecha_ini = "", fecha_fin = "", Proyecto = "", DesProyecto = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        Proyecto = request.getParameter("Proyecto");
        DesProyecto = request.getParameter("DesProyecto");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }

    if (Proyecto == null) {
        Proyecto = "0";
    }

    if (Proyecto.equals("0")) {
        DesProyecto = "TODOS";
    } else {
        DesProyecto = DesProyecto;
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"BackOrderList.xls\"");
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
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Fecha Consulta:</td>
                                <td colspan="2"><%=fecha_ini%> AL <%=fecha_fin%></td>
                            </tr>
                            <tr>
                                <td>Proyecto</td>
                                <td><%=DesProyecto%></td>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <br />
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Proyecto</td>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Requerido</td>
                                <td>Surtido</td>
                                <td>Pendiente</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        String FechaFol = "", Clave = "", Concep = "", Query = "", AND = "", ANDOrigen = "";
                                        int ban = 0, ban1 = 0, ban2 = 0;

                                        if (fecha_ini != "" && fecha_fin != "") {
                                            ban1 = 1;
                                            FechaFol = " AND F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' ";
                                        }
                                        if (!(Proyecto.equals("0"))) {
                                            ban2 = 1;
                                            if (Proyecto.equals("2")) {
                                                Concep = " AND U.F_Proyecto = '" + Proyecto + "' ";
                                            } else {
                                                Concep = " AND F.F_Proyecto = '" + Proyecto + "' ";
                                            }
                                        }

                                        if (ban1 == 1 && ban2 == 1) {
                                            Query = FechaFol + Concep;
                                        } else if (ban1 == 1) {
                                            Query = FechaFol;
                                        } else if (ban2 == 1) {
                                            Query = Concep;
                                        }

                                        if (Query != "") {
                                            ResultSet rset = con.consulta("SELECT P.F_DesProy, F.F_ClaPro, M.F_DesPro, FORMAT(SUM(F.F_CantReq), 0) AS F_CantReq, FORMAT(SUM(F.F_CantSur), 0) AS F_CantSur, FORMAT( SUM(F.F_CantReq) - SUM(F.F_CantSur), 0 ) AS FALTANTE FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto = P.F_Id INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_StsFact = 'A' " + Query + " GROUP BY F.F_ClaPro, F.F_Proyecto;");
                                            while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>
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
                <br />
                <br />
                <!--div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosfirmas" border="0">
                        <tr>
                            <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                            <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><h5>RESPONSABLE MEDICO</h5></td>
                            <td colspan="3"><h5>COORDINADOR O ADMINISTRADOR MUNICIPAL</h5></td>
                        </tr>
                    </table>
                </div>
                </div-->


            </div>
        </div>
    </body>
</html>