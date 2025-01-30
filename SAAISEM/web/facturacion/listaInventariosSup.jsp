<%-- 
    Document   : listaInventarios
    Created on : 16/04/2015, 02:38:55 PM
    Author     : Americo
--%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); %>
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
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
    ConectionDB_Inventarios conInv = new ConectionDB_Inventarios();
    String juris = "";
    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "' ");
        while (rset.next()) {
            juris = rset.getString("F_Juris");
        }
        con.cierraConexion();
    } catch (Exception e) {

    }
%>
<html>
    <head>
        <!-- Estilos CSS -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SAA</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <%@include file="../jspf/menuPrincipal.jspf" %>

        <h3>Inventarios Supervisores</h3>
        <hr/>
        <table class="table table-bordered table-condensed table-striped" id="tablInventarios">
            <thead>
                <tr>
                    <td>Cla Uni</td>
                    <td>Unidad</td>
                    <td>Fecha</td>
                    <td>Tipo</td>
                    <td>Descargar</td>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        conInv.conectar();
                        ResultSet rset = conInv.consulta("select * from v_inventarios where cla_jur in (" + juris + ") and user in ('usuario20', 'usuario21', 'usuario22', 'usuario23', 'usuario24', 'usuario25') group by idInv");
                        while (rset.next()) {
                            String tipoInv = "Dispensario";
                            if (rset.getString("tipo").equals("2")) {
                                tipoInv = "Almacén";
                            }
                %>
                <tr>
                    <td><%=rset.getString("cla_mod")%></td>
                    <td><%=rset.getString("des_uni")%></td>
                    <td><%=df3.format(df.parse(rset.getString("hora_ini")))%></td>
                    <td><%=tipoInv%></td>
                    <td><a class="btn btn-block btn-primary btn-sm" href="gnrInventario.jsp?cla_mod=<%=rset.getString("cla_mod")%>&idInv=<%=rset.getString("idInv")%>"><span class="glyphicon glyphicon-download"></span></a></td>
                </tr>  
                <%
                        }
                        conInv.cierraConexion();
                    } catch (Exception e) {
                        out.println(e);
                    }
                %>
            </tbody>
            
        </table>

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->

        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function() {
                $('#tablInventarios').dataTable();
            });
        </script>
    </body>
</html>
