<%-- 
    Document   : reabastecerModula
    Created on : 26/03/2015, 12:54:16 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>

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
        response.sendRedirect("../index.jsp");
    }


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
        <h4>Reabastecer &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Modula 1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="MinMax.jsp"><div class="glyphicon glyphicon-export"></div></a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Modula 2&nbsp;&nbsp;&nbsp;<a href="MinMax2.jsp"><div class="glyphicon glyphicon-export"></div></a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&Aacute;rea Farmacia&nbsp;&nbsp;&nbsp;<a href="MinMaxAF.jsp"><div class="glyphicon glyphicon-export"></div></a>
        </h4>
        <hr/>
        
        <!-- ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function() {
                $('#tablaMovMod').dataTable();
            });
        </script>
    </body>
</html>
