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
    ConectionDB_SQLServer conSql = new ConectionDB_SQLServer();

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
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body class="container">
        <h1>Manda Requerimentos</h1>

        <form action="../AbasteceModula">
            <button class="btn btn-primary btn-block" name="accion" value="MandaRequerimento">Mandar</button>
        </form>
        <hr/>
        <table class="table table-striped table-bordered table-bordered">
            <tr>
                <td>Requerimento</td>
                <td>Clave</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Origen</td>
                <td>Cantidad</td>
            </tr>
            <%
                try {
                    con.conectar();
                    conSql.conectar();
                    ResultSet rset = conSql.consulta("select RIG_ORDINE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_DSCAD, RIG_QTAE from EXP_ORDINI_RIGHE");
                    while (rset.next()) {
                        String descrip = "";
                        ResultSet rset2 = con.consulta("select F_DesPro from tb_medica where F_ClaPro='" + rset.getString("RIG_ARTICOLO") + "'");
                        while (rset2.next()) {
                            descrip = rset2.getString("F_DesPro");
                        }
            %>
            <tr>
                <td><%=rset.getString("RIG_ORDINE")%></td>
                <td><a href="#" title="<%=descrip%>"><%=rset.getString("RIG_ARTICOLO")%></a></td>
                <td><%=rset.getString("RIG_SUB1")%></td>
                <td><%=rset.getString("RIG_DSCAD")%></td>
                <td><%=rset.getString("RIG_SUB2")%></td>
                <td><%=rset.getString("RIG_QTAE")%></td>
            </tr>
            <%
                    }
                    conSql.cierraConexion();
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            %>
        </table>
        <hr/>
        <table class="table table-striped table-bordered table-bordered">
            <tr>
                <td>Requerimento</td>
                <td>Cliente</td>
                <td>Fecha Entrega</td>
                <td>Clave</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Origen</td>
                <td>Cantidad</td>
            </tr>
            <%
                try {
                    con.conectar();
                    conSql.conectar();
                    ResultSet rset = conSql.consulta("select RIG_ORDINE, ORD_CLIENTE, ORD_DES, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_DSCAD, RIG_QTAE from VIEW_MODULA_CONFIRM_ORDINE");
                    while (rset.next()) {
                        String descrip = "";
                        ResultSet rset2 = con.consulta("select F_DesPro from tb_medica where F_ClaPro='" + rset.getString("RIG_ARTICOLO") + "'");
                        while (rset2.next()) {
                            descrip = rset2.getString("F_DesPro");
                        }
            %>
            <tr>
                <td><%=rset.getString("RIG_ORDINE")%></td>
                <td><%=rset.getString("ORD_CLIENTE")%></td>
                <td><%=df3.format(df.parse(rset.getString("ORD_DES")))%></td>
                <td><a href="#" title="<%=descrip%>"><%=rset.getString("RIG_ARTICOLO")%></a></td>
                <td><%=rset.getString("RIG_SUB1")%></td>
                <td><%=rset.getString("RIG_DSCAD")%></td>
                <td><%=rset.getString("RIG_SUB2")%></td>
                <td><%=rset.getString("RIG_QTAE")%></td>
            </tr>
            <%
                    }
                    conSql.cierraConexion();
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            %>
        </table>

    </body>
</html>
