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
    String F_Cb = "", F_Clave = "", Clave = "";

    try {
        F_Cb = request.getParameter("F_Cb");
        Clave = request.getParameter("Nombre");
    } catch (Exception e) {

    }

    try {
        F_Clave = request.getParameter("F_Clave");
    } catch (Exception e) {

    }

    if (F_Clave == null) {
        F_Clave = "";
    }
    if (F_Cb == null) {
        F_Cb = "";
    }
    if (Clave == null) {
        Clave = "";
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
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            
            <%@include file="../jspf/menuPrincipal.jspf" %>

            <h3>
                Folios por Remisionar
            </h3>
            <hr/>
            <div class="row">
                <h4 class="col-sm-3">Generar Concentrado por día</h4>
                <div class="col-sm-2">
                    <input type="date" class="form-control" />
                </div>
            </div>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <form method="post" action="comparativoGlobal.jsp">
                        <div class="row">
                        </div>

                        <div class="row">
                            <h4 class="col-sm-3">Seleccione el proveedor</h4>
                            <div class="col-sm-5">
                                <select id="Nombre" name="Nombre" class="form-control">
                                    <option value="">Unidad</option>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = con.consulta("select u.F_ClaCli, u.F_NomCli, f.F_IdFact from tb_uniatn u, tb_facttemp f where u.F_StsCli = 'A' and f.F_ClaCli = u.F_ClaCli group by f.F_IdFact");
                                            while (rset.next()) {
                                    %>
                                    <option value="<%=rset.getString(3)%>"
                                            <%
                                                if (Clave.equals(rset.getString(1))) {
                                                    out.println("selected");
                                                }
                                            %>
                                            ><%=rset.getString(3)%> - <%=rset.getString(2)%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-block btn-primary">Buscar</button>
                            </div>
                        </div>
                    </form>
                    <%
                        try {
                            con.conectar();
                            ResultSet rset = null;
                            rset = con.consulta("SELECT	u.F_NomCli,	DATE_FORMAT(f.F_FecEnt, '%d/%m/%Y') as Fecha,	l.F_ClaPro,	l.F_ClaLot,	DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') as feccad,	(f.F_Cant+0) as F_Cant,	l.F_Ubica,	f.F_IdFact,	l.F_Cb,	p.F_Pzs,	(f.F_Cant DIV p.F_Pzs) as cajas,	(f.F_Cant MOD p.F_Pzs) as resto FROM	tb_facttemp f,	tb_lote l,	tb_uniatn u,	tb_pzxcaja p WHERE	f.F_IdLot = l.F_IdLote AND f.F_ClaCli = u.F_ClaCli AND p.F_ClaPro = l.F_ClaPro and f.F_IdFact = '" + Clave + "' group by f.F_IdFact;");
                            while (rset.next()) {
                    %>
                    <div class="row">
                        <h5 class="col-sm-8">Proveedor: <%=rset.getString("F_NomCli")%></h5>
                        <h5 class="col-sm-2">Fecha de Surtido: <%=rset.getString("Fecha")%> </h5>
                    </div>
                    <%
                            }
                            con.cierraConexion();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    %>
                </div>
                <div class="panel-footer">
                    <table class="table table-bordered table-condensed table-responsive table-striped" id="datosProv">
                        <thead>
                            <tr>
                                <td>CB</td>
                                <td>Clave</td>
                                <td>Lote</td>
                                <td>Caducidad</td>
                                <td>Ubicación</td>
                                <td>Cajas</td>
                                <td>Resto</td>
                                <td>Piezas</td>
                                <td>Picking</td>
                                <td>Val Sur</td>
                                <td>Aud</td>
                                <td>Emb</td>
                                <td>Remisionado</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                    rset = con.consulta("SELECT	u.F_NomCli,	DATE_FORMAT(f.F_FecEnt, '%d/%m/%Y') as Fecha,	l.F_ClaPro,	l.F_ClaLot,	DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') as feccad,	(f.F_Cant+0) as F_Cant,	l.F_Ubica,	f.F_IdFact,	l.F_Cb,	p.F_Pzs,	(f.F_Cant DIV p.F_Pzs) as cajas,	(f.F_Cant MOD p.F_Pzs) as resto, f.F_Id, f.F_StsFact, m.F_DesPro FROM	tb_facttemp f,	tb_lote l,	tb_uniatn u,	tb_pzxcaja p, tb_medica m WHERE l.F_ClaPro = m.F_ClaPro and f.F_IdLot = l.F_IdLote AND f.F_ClaCli = u.F_ClaCli AND p.F_ClaPro = l.F_ClaPro and f.F_IdFact='" + Clave + "' group by f.F_Id;");
                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString("F_Cb")%></td>
                                <td><a href="#" title="<%=rset.getString("F_DesPro")%>"><%=rset.getString("F_ClaPro")%></a></td>
                                <td><%=rset.getString("F_ClaLot")%></td>
                                <td><%=rset.getString("feccad")%></td>
                                <td><%=rset.getString("F_Ubica")%></td>
                                <td><%=rset.getString("cajas")%></td>
                                <td><%=rset.getString("resto")%></td>
                                <td><%=rset.getString("F_Cant")%></td>
                                <td class="text-center">
                                    <%
                                        if (rset.getString("F_StsFact").equals("0")) {
                                            out.println("X");
                                        }
                                    %>
                                </td>
                                <td class="text-center">
                                    <%
                                        if (rset.getString("F_StsFact").equals("1")) {
                                            String valido = "";
                                            ResultSet rset2 = con.consulta("select F_Usuario from tb_regvalida where F_idFactTemp='" + rset.getString("F_Id") + "'");
                                            while (rset2.next()) {
                                                valido = valido + "Validó: " + rset2.getString("F_Usuario") + "\n";
                                            }
                                    %>
                                    <a title="<%=valido%>" href="#">X</a>
                                    <%
                                        }
                                    %>
                                </td>
                                <td class="text-center">
                                    <%
                                        if (rset.getString("F_StsFact").equals("2")) {
                                            String valido = "";
                                            int cont = 1;
                                            ResultSet rset2 = con.consulta("select F_Usuario from tb_regvalida where F_idFactTemp='" + rset.getString("F_Id") + "'");
                                            while (rset2.next()) {
                                                valido = valido + "Validó " + cont + " :" + rset2.getString("F_Usuario") + "\n";
                                                cont++;
                                            }
                                    %>
                                    <a title="<%=valido%>" href="#">X</a>
                                    <%
                                        }
                                    %>
                                </td>
                                <td class="text-center">
                                    <%
                                        if (rset.getString("F_StsFact").equals("4")) {
                                            String valido = "";
                                            ResultSet rset2 = con.consulta("select F_Usuario from tb_regvalida where F_idFactTemp='" + rset.getString("F_Id") + "'");
                                            while (rset2.next()) {
                                                valido = valido + "Validó: " + rset2.getString("F_Usuario") + "\n";
                                            }
                                    %>
                                    <a title="<%=valido%>" href="#">X</a>
                                    <%
                                        }
                                    %>
                                </td>
                                <td class="text-center">
                                    <%
                                        if (rset.getString("F_StsFact").equals("5")) {
                                            String valido = "";
                                            ResultSet rset2 = con.consulta("select F_Usuario from tb_regvalida where F_idFactTemp='" + rset.getString("F_Id") + "'");
                                            while (rset2.next()) {
                                                valido = valido + "Validó: " + rset2.getString("F_Usuario") + "\n";
                                            }
                                    %>
                                    <a title="<%=valido%>" href="#">X</a>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
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
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script>
                                        $(document).ready(function() {
                                            $('#datosProv').dataTable();
                                        });
    </script>
</html>

