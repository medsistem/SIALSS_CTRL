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
        if(!tipo.equals("1") && !tipo.equals("2") && !tipo.equals("3") && !tipo.equals("9") && !tipo.equals("11") && !tipo.equals("22") && !tipo.equals("25") && !tipo.equals("26")){
            response.sendRedirect("../index.jsp");
        }
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String idLote = "";
    try {
        idLote = request.getParameter("idLote");
    } catch (Exception e) {

    }
    if (idLote == null) {
        idLote = "";
    }
    String ClaPro = "", UbiAnt = "";
    try {
        ClaPro = request.getParameter("ClaPro");
        UbiAnt = request.getParameter("UbiAnt");
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
        ResultSet rset = con.consulta("select F_Ubica from tb_lote where F_IdLote='" + idLote + "'");
        while (rset.next()) {
        UbiAnt=rset.getString(1);
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
        <link href="../css/sweetalert.css" rel="stylesheet" type="text/css"/>
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
                <input class="hidden" name="UbiAnt" value="<%=UbiAnt%>" />
                <input class="hidden" name="ClaPro" value="<%=ClaPro%>" />
                <button class="btn btn-default" type="submit">Regresar</button>
            </form>
            <%
                try {
                    int canApartada = 0;
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT u.F_DesUbi, l.F_ClaPro, l.F_ExiLot, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, l.F_IdLote, u.F_ClaUbi, u.F_Cb AS CbUbica, l.F_Origen, p.F_DesProy, o.F_DesOri FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_ubica u ON l.F_Ubica = u.F_ClaUbi INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE l.F_ExiLot != 0 AND l.F_IdLote = '" + idLote + "';");
                    while (rset.next()) {
                        int banAlerta = 0;
                        ResultSet rset2 = con.consulta("select F_IdLot, SUM(F_Cant) from tb_facttemp where F_IdLot = '" + idLote + "' and F_StsFact <5 group by F_IdLot");
                        while (rset2.next()) {
                            banAlerta = 1;
                            canApartada = rset2.getInt(2);
                        }
            %>
            <form action="../Ubicaciones">
                <h5>
                    Proyecto: <%=rset.getString("F_DesProy")%>
                    <br/>
                    Origen: <%=rset.getString("F_DesOri")%>
                    <br/>
                    Ubicación: <%=rset.getString("F_DesUbi")%>
                    <br/>
                    Clave: <%=rset.getString("F_ClaPro")%>
                    <br/>
                    Cantidad: <%=formatter.format(rset.getInt("F_ExiLot"))%>
                    <input name="CantAnt" id="CantAnt" class="hidden" value="<%=(rset.getInt("F_ExiLot") - canApartada)%>" />
                    <br/>
                    Descripción: <%=rset.getString("F_DesPro")%>
                    <br/>
                Origen: <%=rset.getString("F_Origen")%>
                    <br/>
                    Lote: <%=rset.getString("F_ClaLot")%>
                    <br/>
                    Caducidad: <%=rset.getString("F_FecCad")%>
                    <br/>
                </h5>
                <div class="row">
                    <h5 class="col-lg-12">Cantidad a Mover:</h5>
                    <div class="col-lg-12">
                        <input class="form-control" placeholder="Cantidad a Mover" type="number" name="CantMov" id="CantMov" min="1" max="<%=(rset.getInt("F_ExiLot") - canApartada)%>" />
                    </div>
                </div>
                <div class="row">
                    <h5 class="col-lg-12">CB de Nueva Ubicación:</h5>
                    <div class="col-lg-12">
                        <input class="form-control" id="F_ClaUbi" name="F_ClaUbi" placeholder="CB de Nueva Ubicación" type="text" />
                        <input class="hidden" id="F_IdLote" name="F_IdLote" value="<%=idLote%>" />
                        <input id="aClaUbi" class="hidden" value="<%=rset.getString("F_ClaUbi")%>"/>
                        <input id="aCbUbica" class="hidden" value="<%=rset.getString("CbUbica")%>"/>
                        <input id="ClaveUbica" class="hidden" value="<%=rset.getString("F_ClaPro")%>"/>
                    </div>
                </div>
                <br/>
                <%
                    if (banAlerta == 1) {
                %>
                <div class="alert alert-danger">
                    <strong>Este insumo está apartado con <%=canApartada%> piezas</strong>
                </div>
                <div class="alert alert-warning">
                    <strong>Cantidad máxima a mover: <%=(rset.getInt("F_ExiLot") - canApartada)%> piezas</strong>
                </div>
                <%
                    }
                %>
                <div class="row">
                    <div class="col-lg-12">
                        <button class="btn btn-block btn-primary btn-lg" onclick="return validaRedist();" name="accion" type="button" id="Redistribucion" value="Redistribucion">Redistribuir</button>
                    </div>
                </div>
            </form>
            <%
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
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
    <!--script src="../js/funcRedistribucion.js"></script-->
    <script src="../js/redistrubucion/redistribuirdatosCross.js"></script>
    <script src="../js/sweetalert.min.js" type="text/javascript"></script>
</html>
