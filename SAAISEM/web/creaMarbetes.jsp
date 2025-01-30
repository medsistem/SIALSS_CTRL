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
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String ClaPro = "", CB = "";
    String DesPro = "";

    try {
        con.conectar();
        CB = request.getParameter("CB");
        ClaPro = request.getParameter("ClaPro");
        if (CB != null) {
            ResultSet rset = con.consulta("select l.F_ClaPro,m.F_DesPro from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and F_Cb = '" + CB + "' ");
            while (rset.next()) {
                DesPro = rset.getString(2);
                ClaPro = rset.getString(1);
            }
        }
        con.cierraConexion();
    } catch (Exception e) {

    }

    if (CB == null) {
        CB = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
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
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <hr/>
            <h3>Generación de Marbetes y Consulta de CB</h3>
            <div class="panel panel-default">
                <div class="panel-heading">
                    Ingrese los Datos
                </div>
                <div class="panel-body">
                    <div class="row">
                        <h4 class="col-sm-1">Clave</h4>
                        <form action="creaMarbetes.jsp" method="POST">
                            <div class="col-sm-2">
                                <input class="form-control" name="ClaPro" id="ClaPro" value="<%=ClaPro%>" />
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary btn-block"><span class="glyphicon glyphicon-search"></span></button>
                            </div>
                        </form>

                    </div>
                    <div class="row">
                        <h4 class="col-sm-1">CB:</h4>
                        <form action="creaMarbetes.jsp" method="POST">
                            <div class="col-sm-2">
                                <input class="form-control" name="CB" id="CB" value="<%=CB%>" />
                            </div>
                            <div class="col-sm-2">
                                <select class="form-control" name="selCB" id="selCB" onchange="cambiaCB(this);">
                                    <option value="">Seleccione</option>
                                    <%
                                        try {
                                            con.conectar();
                                            if (ClaPro != null) {
                                                ResultSet rset = con.consulta("select F_Cb from tb_lote where F_ClaPro = '" + ClaPro + "' group by F_Cb");
                                                while (rset.next()) {
                                    %>
                                    <option><%=rset.getString(1)%></option>
                                    <%
                                                }
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }

                                    %>
                                </select>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary btn-block"><span class="glyphicon glyphicon-search"></span></button>
                            </div>
                        </form>

                    </div>
                    <form action="reimpMarbete.jsp" method="get">
                        <div class="row">
                            <h4 class="col-sm-1">Clave:</h4>
                            <div class="col-sm-2">

                                <input class="hidden" name="CB" id="CB" value="<%=CB%>" />
                                <input class="form-control" name="Clave" id="Clave" value="<%=ClaPro%>" readonly="" />
                            </div>
                            <h4 class="col-sm-1">Descrip:</h4>
                            <div class="col-sm-7">
                                <input class="form-control" readonly name="DesPro" id="DesPro" value="<%=DesPro%>" />
                            </div>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-1">Lote:</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="LotPro" id="LotPro" />
                            </div>
                            <div class="col-sm-2">

                                <select class="form-control" name="selLote" id="selLote" onchange="cambiaLote(this);">
                                    <option value="">Seleccione</option>
                                    <%
                                        try {
                                            con.conectar();
                                            if (ClaPro != null) {
                                                ResultSet rset = con.consulta("select F_ClaLot from tb_lote where F_Cb = '" + CB + "' group by F_ClaLot");
                                                while (rset.next()) {
                                    %>
                                    <option><%=rset.getString(1)%></option>
                                    <%
                                                }
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }

                                    %>
                                </select>
                            </div>
                            <h4 class="col-sm-1">Caducidad:</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="CadPro" id="CadPro"  />
                            </div>
                            <div class="col-sm-2">
                                <select class="form-control" name="selCadu" id="selCadu" onchange="cambiaCadu(this);">
                                    <option value="">Seleccione</option>
                                    <%                                    try {
                                            con.conectar();
                                            if (ClaPro != null) {
                                                ResultSet rset = con.consulta("select DATE_FORMAT(F_FecCad, '%d/%m/%Y') from tb_lote where F_Cb = '" + CB + "' group by F_FecCad");
                                                while (rset.next()) {
                                    %>
                                    <option><%=rset.getString(1)%></option>
                                    <%
                                                }
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }

                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-2">Piezas:</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="cantidad" id="cantidad" />
                            </div>
                            <h4 class="col-sm-2">No. Compra:</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="noCompra" id="noCompra" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <input class="hidden" name="copias" id="copias" value="1" />
                                <button class="btn btn-primary btn-block">Generar Marbete</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <%@include file="jspf/piePagina.jspf" %>
    </body>
    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script>
                                    function cambiaLote(e) {
                                        var lote = e.value;
                                        document.getElementById('LotPro').value = lote;
                                    }
                                    function cambiaCadu(e) {
                                        var cadu = e.value;
                                        document.getElementById('CadPro').value = cadu;
                                    }
                                    function cambiaCB(e) {
                                        var cb = e.value;
                                        document.getElementById('CB').value = cb;
                                    }
    </script>
</html>

