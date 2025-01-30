<%-- 
    Document   : requerimentoModula
    Created on : 16-oct-2014, 16:26:54
    Author     : Amerikillo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB_SQLServer"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); %>
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
    ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
        <hr/>
        <h4>Avisos de Entrada - Modula</h4>
        <table class="table table-bordered table-condensed table-striped" id="tablaMovMod">
            <thead>
                <tr>
                    <td>ID</td>
                    <td>Ord - Remi</td>
                    <td>Clave</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>CB</td>
                    <td>Estado</td>
                    <td>Cant</td>
                    <td>En ejecución</td>
                    <td>Completado</td>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        conModula.conectar();
                        con.conectar();
                        ResultSet rset = conModula.consulta("select RIG_RIGA, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_DSCAD, RIG_REQ_NOTE, RIG_STARIORD, RIG_QTAR, RIG_QTAI, RIG_QTAE, RIG_ORDINE from VIEW_MODULA_AVVISOINGRESO");
                        while (rset.next()) {
                            String F_DesPro = "", estado="";
                            ResultSet rset2 = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + rset.getString("RIG_ARTICOLO") + "'");
                            while (rset2.next()) {
                                F_DesPro = rset2.getString("F_DesPro");
                            }
                            if (rset.getString("RIG_STARIORD").equals("W")){
                                estado = "En Espera";
                            }
                            if (rset.getString("RIG_STARIORD").equals("I")){
                                estado = "Incompleto";
                            }
                            if (rset.getString("RIG_STARIORD").equals("E")){
                                estado = "En Ejecución";
                            }
                            if (rset.getString("RIG_STARIORD").equals("C")){
                                estado = "Completo";
                            }
                %>
                <tr>
                    <td><%=rset.getString("RIG_RIGA")%></td>
                    <td><%=rset.getString("RIG_ORDINE")%></td>
                    <td title="<%=F_DesPro%>"><%=rset.getString("RIG_ARTICOLO")%></td>
                    <td><%=rset.getString("RIG_SUB1")%></td>
                    <td><%=df3.format(df2.parse(rset.getString("RIG_DSCAD")))%></td>
                    <td><%=rset.getString("RIG_REQ_NOTE")%></td>
                    <td><%=estado%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("RIG_QTAR"))%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("RIG_QTAI"))%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("RIG_QTAE"))%></td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                        conModula.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        out.print(e.getMessage());
                    }
                %>
            </tbody>
        </table>
    </body>
    <!-- ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/jquery-ui-1.10.3.custom.js"></script>
    <script src="../js/jquery.dataTables.js"></script>
    <script src="../js/dataTables.bootstrap.js"></script>
    <script>
        $(document).ready(function () {
            $('#tablaMovMod').dataTable();
        });
    </script>
</html>
