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
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
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
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <hr/>
            <h3>Comparativo Cíclico</h3>
            <a href="capturaClave.jsp" class="btn btn-default">Regresar</a>
            <hr/>
            <table class="table table-striped table-bordered table-condensed">
                <tr>
                    <td>Clave</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <td>Sistema</td>
                    <td>Inv Cíclico</td>
                    <td>Diferencia</td>
                </tr>
                <%
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select *, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as FechaCad from tb_loteinv where F_ExiLot !=0;");
                        while (rset.next()) {
                            int Diferencia = 0, CantSistema = 0;
                            ResultSet rset2 = con.consulta("select * from tb_lote where F_IdLote ='" + rset.getString("F_IdLote") + "';");
                            while (rset2.next()) {
                                CantSistema = rset2.getInt("F_ExiLot");
                            }

                            Diferencia = CantSistema - rset.getInt("F_ExiLot");
                %>
                <tr>
                    <td><%=rset.getString("F_ClaPro")%></td>
                    <td><%=rset.getString("F_ClaLot")%></td>
                    <td><%=rset.getString("FechaCad")%></td>
                    <td><%=rset.getString("F_Ubica")%></td>
                    <td><%=formatter.format(CantSistema)%></td>
                    <td><%=formatter.format(rset.getInt("F_ExiLot"))%></td>
                    <td><%=formatter.format(Diferencia)%></td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                %>
            </table>

            <br><br><br>
            <div class="navbar  navbar-inverse">
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
    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/funcInvCiclico.js"></script>
    <script src="../js/jquery-ui.js"></script>
</html>

