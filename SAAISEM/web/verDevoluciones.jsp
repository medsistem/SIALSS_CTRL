<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("fecha");
        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
        <hr/>
        <div class="panel panel-danger">
            <div class="panel-heading">
                Devoluciones pendientes.
            </div>

            <div class="panel-body">
                <table class="table table-condensed table-bordered table-striped" id="DevPend">
                    <thead>
                        <tr>
                            <td>Remisión</td>
                            <td>Cliente</td>
                            <td>Fecha</td>
                            <td>Insumo</td>
                            <td>Cant</td>
                            <td>Monto</td>
                            <td>Observaciones</td>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaDoc, F_ClaCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') as F_FecApl, F_ClaPro, F_CantSur, F_Monto, F_Obs from tb_factdevol where F_FactSts=0");
                                while (rset.next()) {
                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaDoc")%></td>
                            <td><%=rset.getString("F_ClaCli")%></td>
                            <td><%=rset.getString("F_FecApl")%></td>
                            <td><%=rset.getString("F_ClaPro")%></td>
                            <td><%=rset.getString("F_CantSur")%></td>
                            <td><%=rset.getString("F_Monto")%></td>
                            <td><%=rset.getString("F_Obs")%></td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">
                Devoluciones Realizadas.
            </div>

            <div class="panel-body">
                <table class="table table-condensed table-bordered table-striped" id="DevReal">
                    <thead>
                        <tr>
                            <td>Remisión</td>
                            <td>Cliente</td>
                            <td>Fecha</td>
                            <td>Insumo</td>
                            <td>Cant</td>
                            <td>Monto</td>
                            <td>Observaciones</td>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaDoc, F_ClaCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') as F_FecApl, F_ClaPro, F_CantSur, F_Monto, F_Obs from tb_factdevol where F_FactSts=1");
                                while (rset.next()) {
                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaDoc")%></td>
                            <td><%=rset.getString("F_ClaCli")%></td>
                            <td><%=rset.getString("F_FecApl")%></td>
                            <td><%=rset.getString("F_ClaPro")%></td>
                            <td><%=rset.getString("F_CantSur")%></td>
                            <td><%=rset.getString("F_Monto")%></td>
                            <td><%=rset.getString("F_Obs")%></td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </body>

    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script>
        $(document).ready(function() {
            $('#DevPend').dataTable();
        });
    </script>
    <script>
        $(document).ready(function() {
            $('#DevReal').dataTable();
        });
    </script>
</html>
