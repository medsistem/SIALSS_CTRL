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
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

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
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <hr/>
        </div>
        <div class="container">
            <h3>
                Devoluciones
            </h3>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    Insumo a Devolver
                </div>
                <div class="panel-body">
                    <table class="table table-condensed table-bordered table-striped ">
                        <tr>
                            <td>Clave</td>
                            <td>Lote</td>
                            <td>Caducidad</td>
                            <td>Cantidad</td>
                            <td>Observaciones</td>
                            <td>Imprimir</td>
                        </tr>

                        <%                        try {
                                con.conectar();
                                ResultSet rset = con.consulta("select l.F_ClaPro, l.F_ClaLot,  DATE_FORMAT(l.F_FecCad,'%d/%m/%Y') AS F_FecCad, d.F_Cantidad, d.F_Id, d.F_ObsMov from tb_devolcompra d, tb_lote l where d.F_IdLote=l.F_IdLote;");
                                while (rset.next()) {
                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaPro")%></td>
                            <td><%=rset.getString("F_ClaLot")%></td>
                            <td><%=rset.getString("F_FecCad")%></td>
                            <td><%=rset.getString("F_Cantidad")%></td>
                            <td><%=rset.getString("F_ObsMov")%></td>
                            <td><a href="reimpDevolucionCompra.jsp?fol_gnkl=<%=rset.getString("F_Id")%>" target="_blank" class="btn btn-default">Imprimir</a></td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>

                    </table>
                </div>
            </div>
        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
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
    </script>
</html>

