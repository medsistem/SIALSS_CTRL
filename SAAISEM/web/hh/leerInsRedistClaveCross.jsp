<%-- 
    Document   : insumoNuevoRedist
    Created on : 6/10/2014, 10:49:37 AM
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
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        if(!tipo.equals("1")){
            response.sendRedirect("../index.jsp");
        }
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String ClaPro = "", UbiAnt = "", UbiCb = "";
    try {
        ClaPro = request.getParameter("ClaPro");
        UbiAnt = request.getParameter("UbiAnt");
        con.conectar();
        ResultSet rset = con.consulta("select l.F_Cb from tb_lote l, tb_ubica u where l.F_Ubica = u.F_ClaUbi and F_ClaPro= '" + ClaPro + "' and u.F_Cb='" + UbiAnt + "'");
        while (rset.next()) {
            //ClaPro = rset.getString("F_Cb");
        }
        con.cierraConexion();
    } catch (Exception e) {
    }

    if (ClaPro == null) {
        ClaPro = "";
    }
    if (UbiAnt == null) {
        UbiAnt = "";
    }

    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_Cb from tb_ubica where F_ClaUbi='" + UbiAnt + "'");
        while (rset.next()) {
            UbiCb = rset.getString("F_Cb");
        }
        if (!UbiCb.equals("")) {
            UbiAnt = UbiCb;
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
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>

            <%@include file="../jspf/menuPrincipal.jspf" %>

            <h4>Redistribución</h4>
            <form action="leerInsRedistClaveCross.jsp" method="post">
                <a class="btn btn-default" href="../main_menu.jsp">Regresar</a>
                <button class="btn btn-primary" type="submit" name="UbiAnt" value="PorUbicar">Por Ubicar</button>
            </form>

            <form action="leerInsRedistClaveCross.jsp" method="post">
                <div class="row">
                    <h5 class="col-lg-12">Clave del Insumo a Mover</h5>
                    <div class="col-lg-12">                        
                        <input class="form-control" name="ClaPro" value="<%=ClaPro%>" autofocus="" />
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-lg-12">
                        <button class="btn btn-block btn-primary btn-lg" type="submit" name="UbiAnt" value="Leer">Leer Insumo</button>
                    </div>
                </div>
                <div class="col-lg-12">
                    <input class="hidden" name="UbiAnt" value="<%=UbiAnt%>" />                        
                </div>
            </form>
            <hr/>
            <h4>Insumos Médicos</h4>
            <div class="panel panel-primary">
                <div class="panel-body table-responsive">
                    <table class="cell-border table table-striped table-bordered" cellspacing="0"  id="example">
                        <thead>
                            <tr>
                                <td>Proyecto</td>
                                <td>Clave</td>
                                <td>Lote</td>
                                <td>Caducidad</td>
                                <td>Ubicación</td>
                                <td>Cantidad</td>
                                <td>Origen</td>
                                <td>Acción</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    if (!UbiAnt.equals("PorUbicar")) {
                                        String UbiCross = "";
                                        con.conectar();
                                        ResultSet rsetUbica = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                                        if (rsetUbica.next()) {
                                            UbiCross = rsetUbica.getString(1);
                                        }

                                        ResultSet rset = con.consulta("SELECT u.F_DesUbi, l.F_ClaPro, l.F_ExiLot, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, l.F_IdLote, l.F_Cb, l.F_Origen, p.F_DesProy, o.F_DesOri FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_ubica u ON l.F_Ubica = u.F_ClaUbi INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE l.F_ExiLot != 0 AND ( l.F_ClaPro LIKE '%" + ClaPro + "%' OR l.F_ClaLot = '" + ClaPro + "' ) AND l.F_Ubica IN (" + UbiCross + ",'CROSSDOCKMORELIA');");
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString("F_DesProy")%></td>
                                <td><%=rset.getString("F_ClaPro")%></td>
                                <td><%=rset.getString("F_ClaLot")%></td>
                                <td><%=rset.getString("F_FecCad")%></td>
                                <td><%=rset.getString("F_DesUbi")%></td>
                                <td><%=formatter.format(rset.getInt("F_ExiLot"))%></td>
                                <td><%=rset.getString("F_DesOri")%></td>
                                <td>
                                    <form action="ingCantRedistCross.jsp" method="post">
                                        <input class="hidden" name="UbiAnt" value="<%=UbiAnt%>" />
                                        <input class="hidden" name="ClaPro" value="<%=ClaPro%>" />
                                        <input value="<%=rset.getString("F_IdLote")%>" class="hidden" name="idLote" />
                                        <button class="btn btn-block btn-primary" type="submit">Seleccionar</button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                }
                            } else {

                                con.conectar();
                                ResultSet rset = con.consulta("SELECT u.F_DesUbi, l.F_ClaPro, l.F_ExiLot, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, l.F_IdLote, l.F_Cb, l.F_Origen, p.F_DesProy, o.F_DesOri FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_ubica u ON l.F_Ubica = u.F_ClaUbi INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE l.F_ExiLot != 0 AND u.F_ClaUbi = 'NUEVACROSS';");
                                while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString("F_DesProy")%></td>
                                <td><%=rset.getString("F_ClaPro")%></td>
                                <td><%=rset.getString("F_ClaLot")%></td>
                                <td><%=rset.getString("F_FecCad")%></td>
                                <td><%=rset.getString("F_DesUbi")%></td>
                                <td><%=formatter.format(rset.getInt("F_ExiLot"))%></td>
                                <td><%=rset.getString("F_DesOri")%></td>
                                <td>
                                    <form action="ingCantRedistCross.jsp" method="post">
                                        <input class="hidden" name="UbiAnt" value="<%=UbiAnt%>" />
                                        <input class="hidden" name="ClaPro" value="<%=ClaPro%>" />
                                        <input value="<%=rset.getString("F_IdLote")%>" class="hidden" name="idLote" />
                                        <button class="btn btn-block btn-primary" type="submit">Seleccionar</button>
                                    </form>
                                </td>
                            </tr>
                            <%

                                        }
                                        con.cierraConexion();
                                    }
                                } catch (Exception e) {

                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%@include file="../jspf/piePagina.jspf" %>
    </body>
    <!-- 
================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->

    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/jquery-ui-1.10.3.custom.js"></script>
    <script src="../js/bootstrap-datepicker.js"></script>
    <script src="../js/jquery.dataTables.js"></script>
    <script src="../js/dataTables.bootstrap.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#example').dataTable();
        });
    </script>
</html>
