<%-- 
    Document   : verFoliosIsem2017.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%java.text.DateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", IdUsu = "", horEnt = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        IdUsu = (String) sesion.getAttribute("IdUsu");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();
    String NoCompra = "", Fecha = "";

    Fecha = request.getParameter("Fecha");
    if (Fecha == null) {
        Fecha = "";
    }
    NoCompra = request.getParameter("NoCompra");

    if (Fecha == null) {
        NoCompra = "";
    }
    String proveedor = request.getParameter("Proveedor");

    sesion.setAttribute("proveedor", proveedor);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ver OC</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
    </head>
    <body onload="focusLocus();">
        <div class="container">
            <div class="row">
                <h1>SIALSS_CTRL</h1>
                <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

                <%if (tipo.equals("13") || tipo.equals("14")) {
                %>
                <%@include file="jspf/menuPrincipalCompra.jspf" %>
                <% } %>
                <h4>Ver OC</h4>
            </div>
            <br/>
            <div class="row">
                <form method="post" action="verFoliosM.jsp">
                    <div class="row">
                        <label class="col-sm-1">
                            <h4>Proveedor</h4>
                        </label>
                        <div class="col-sm-6">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="SelectProve(this.form);
                                    document.getElementById('Fecha').focus()">
                                <option value="">--Proveedor--</option>
                                <%                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT F_ClaProve, F_NomPro FROM tb_proveedor p, tb_pedido_sialss o WHERE p.F_ClaProve = o.F_Provee  GROUP BY o.F_Provee ORDER BY F_NomPro");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                        }
                                    }
                                %>

                            </select>
                        </div>

                        <label class="col-sm-3">
                            <h4>Fecha de Entrega:</h4>
                        </label>
                        <div class="col-sm-2">
                            <input type="date" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha" value="<%=Fecha%>" onchange="document.getElementById('Hora').focus()" />
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <label class="col-sm-1">
                            <h4>Usuario:</h4>
                        </label>
                        <div class="col-sm-6">
                            <select class="form-control" name="Usuario" id="Usuario" onchange="">
                                <option value="">--Usuarios--</option>
                                <%
                                    try {
                                        con.conectar();
                                        // ResultSet rset = con.consulta("SELECT u.F_IdUsu, u.F_Usu FROM tb_usuariocompra u, tb_pedido_sialss p WHERE F_Usu !='root' AND u.F_IdUsu = p.F_IdUsu GROUP BY F_IdUsu;");
                                        ResultSet rset = con.consulta("SELECT CASE WHEN u.F_TipUsu = '13' THEN uc.F_IdUsu ELSE u.F_IdUsu END  as Id_Usu, CASE WHEN uc.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE u.F_Usu END  as Usu FROM tb_pedido_sialss AS p LEFT JOIN tb_usuario AS u ON p.F_IdUsu = u.F_IdUsu  LEFT JOIN tb_usuariocompra AS uc ON p.F_IdUsu = uc.F_IdUsu GROUP BY p.F_IdUsu HAVING Id_Usu != 'NULL' ORDER BY uc.F_Usu ASC, u.F_Usu ASC;");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                        }
                                    }
                                %>

                            </select>
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <button class="btn btn-primary btn-block" name="accion" value="fecha">Buscar</button>
                    </div>
                </form>
            </div>
        </div>
        <br/>
        <!--Listado de oc-->
        <div class="row" style="width: 90%; margin: auto;">
            <div class="col-sm-12 table-responsive">
                <form method="post">
                    <input value="<%=Fecha%>" name="Fecha" class="hidden"/>
                    <input value="<%=request.getParameter("Usuario")%>" name="Usuario"  class="hidden"/>
                    <input value="<%=request.getParameter("Proveedor")%>" name="Proveedor"  class="hidden"/>

                    <label class="col-sm-12">
                        <h4>Órdenes de Compra: </h4>
                    </label>

                    <table class="table table-bordered table-condensed table-striped" id="datosCompras">

                        <thead>
                            <tr>
                                <th>No. Orden</th>
                                <th>Capturó</th>
                                <th>Proveedor</th>
                                <th>Fecha Entrega</th>
                                <th>Hora entrega</th>
                                <th>Proyecto</th>
                                <th>Ver</td>
                                    <!--th>Agregar</th-->
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                String fecha = "", Usuario = "", Proveedor = "";
                                fecha = request.getParameter("Fecha");
                                Usuario = request.getParameter("Usuario");
                                Proveedor = request.getParameter("Proveedor");
                                try {
                                    System.out.println("emtre");
                                    con.conectar();
                                    ResultSet rset = null;

                                    if (!(Proveedor.equals("")) && (fecha.equals("")) && (Usuario.equals(""))) {
                                        System.out.println("1");
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve and o.F_Provee = '" + request.getParameter("Proveedor") + "' group by o.F_NoCompra;");
                                    } else if (!(Proveedor.equals("")) && (!fecha.equals("")) && (Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve and o.F_Provee = '" + request.getParameter("Proveedor") + "' and o.F_FecSur =  '" + fecha + "' group by o.F_NoCompra;");

                                    } else if (!(Proveedor.equals("")) && (!fecha.equals("")) && (!Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve and o.F_Provee = '" + request.getParameter("Proveedor") + "' AND o.F_FecSur =  '" + fecha + "' AND o.F_IdUsu =  '" + Usuario + "' group by o.F_NoCompra;");

                                    } else if ((Proveedor.equals("")) && (!fecha.equals("")) && (Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve AND o.F_FecSur =  '" + fecha + "' group by o.F_NoCompra;");

                                    } else if ((Proveedor.equals("")) && (!fecha.equals("")) && (!Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve AND o.F_FecSur =  '" + fecha + "' AND o.F_IdUsu =  '" + Usuario + "' group by o.F_NoCompra;");

                                    } else if ((Proveedor.equals("")) && (fecha.equals("")) && (!Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve AND o.F_IdUsu =  '" + Usuario + "' group by o.F_NoCompra;");

                                    } else if ((!Proveedor.equals("")) && (fecha.equals("")) && (!Usuario.equals(""))) {
                                        rset = con.consulta("SELECT o.F_NoCompra, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE    u.F_Usu END  as Usu, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha, pr.F_DesProy FROM tb_pedido_sialss o, tb_proveedor p, tb_usuariocompra uc, tb_usuario u,tb_Proyectos pr WHERE o.F_Proyecto = pr.F_ID and uc.F_IdUsu = o.F_IdUsu AND u.F_IdUsu = o.F_IdUsu AND o.F_Provee = p.F_ClaProve AND o.F_IdUsu =  '" + Usuario + "' AND o.F_Provee = '" + request.getParameter("Proveedor") + "' group by o.F_NoCompra;");
                                    }

                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(7)%></td>
                                <td>
                                    <button class="btn btn-primary text-center" name="NoCompra" value="<%=rset.getString(1)%>"><span class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <!--td>
                                    <a href="AgregarMedalfa.jsp?Proyecto=1&DesProyecto=ISEM&Campo=F_ProIsem&OC=<%=rset.getString(1)%>" class="btn btn-primary text-center"><span class="glyphicon glyphicon-plus"></span></a>
                                </td-->
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>

        <!--Detalle de oc-->               
        <div class="row" style="width: 90%; margin: auto;">             
            <div class="col-sm-12 table-responsive">
                <div class="panel panel-primary">
                    <%                try {
                            con.conectar();
                            ResultSet rset = con.consulta("SELECT o.F_NoCompra, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y'), F_HorSur, CASE WHEN u.F_TipUsu = '13' THEN CONCAT(uc.F_Usu,'-','Compra') ELSE u.F_Usu END as F_Usu, F_StsPed, rec.F_Recibido,o.Fecha_Vencimiento,o.Plazo_Pago FROM tb_pedido_sialss o INNER JOIN tb_proveedor p ON o.F_Provee = p.F_ClaProve LEFT JOIN tb_usuariocompra uc ON uc.F_IdUsu = o.F_IdUsu LEFT JOIN tb_usuario u ON u.F_IdUsu = o.F_IdUsu INNER JOIN ( SELECT F_NoCompra, SUM(F_Recibido) AS F_Recibido FROM tb_pedido_sialss o WHERE F_NoCompra = '" + NoCompra + "' ) rec ON o.F_NoCompra = rec.F_NoCompra WHERE o.F_NoCompra = '" + NoCompra + "' GROUP BY o.F_NoCompra;");
                            while (rset.next()) {
                    %>
                    <div class="panel-heading">
                        <div class="col-sm-1">
                            <a class="btn btn-default" target="_blank" href="imprimeOrdenCompra.jsp?ordenCompra=<%=NoCompra%>"><span class="glyphicon glyphicon-print"></span></a>
                        </div>
                        <h4 class="text-center">  Orden: <%=NoCompra%> </h4>
                    </div>
                    <div class="row">

                        <%
                            if (rset.getString("F_StsPed").equals("2")) {
                        %>
                        <h3 class="text-danger">FOLIO CANCELADO</h3>
                        <%
                            }
                        %>
                    </div>
                    <!-- modal de encabezado-->

                    <div class="panel-body">
                        <form name="FormBusca" action="CapturaPedidos" method="post">
                            <div class="row">
                                <h4 class="col-sm-2">Proveedor: </h4>
                                <div class="col-sm-9">
                                    <input class="form-control" value="<%=rset.getString(2)%>" readonly="" />
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Orden No. </h4>
                                <div class="col-sm-4">
                                    <input class="form-control" value="<%=rset.getString(1)%>" name="NoCompra" id="NoCompra" readonly="" />
                                </div>
                                <h4 class="col-sm-1">Capturó: </h4>
                                <div class="col-sm-3">
                                    <input class="form-control" value="<%=rset.getString("F_Usu")%>" readonly="" />
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Fecha de Entrega: </h4>
                                <div class="col-sm-3">
                                    <input class="form-control" value="<%=rset.getString(3)%>" readonly="" />
                                </div>
                                <h4 class="col-sm-2">Hora de Entrega: </h4>
                                <div class="col-sm-3">
                                    <input class="form-control" value="<%=rset.getString(4)%>" readonly="" />
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2" >Fecha vencimiento: </h4>
                                <div class="col-sm-3">
                                    <input type="date" class="form-control" value="<%=rset.getString(8)%>" readonly="" />
                                </div>
                                <h4 class="col-sm-2" >Plazo de pago: </h4>
                                <div class="col-sm-3">
                                    <input class="form-control"  value="<%=rset.getString(9)%>" readonly="" />
                                </div>

                            </div>
                            <%
                                if ((!rset.getString("F_StsPed").equals("2")) && (rset.getInt(7) == 0)) {
                            %>
                            <br/>
                            <textarea class="form-control" name="Observaciones" id="Observaciones" placeholder="Observaciones para cancelar"></textarea>
                            <br>
                            <div class="row">
                                <div class="col-sm-6">
                                    <button class="btn btn-info btn-block" name="accion" value="cancelaOrden" onclick="return CancelaCompra();">CANCELAR ORDEN DE COMPRA</button>
                                </div>
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-info btn-block" data-toggle="modal" data-target="#modalCambioFecha" id="btnRecalendarizar" >CAMBIO DE FECHA Y HORA ENTREGA</button>
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-info btn-block" data-toggle="modal" data-target="#modalCambioFechaVencimiento" id="btnPlazo" >CAMBIO EN EL VENCIMIENTO</button>
                                </div>
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-info btn-block" data-toggle="modal" data-target="#modalCambioPlazo" id="btnPlazo" >CAMBIO EN EL PLAZO DE PAGO</button>
                                </div>
                            </div>
                            <%
                                }

                                if (rset.getString("F_StsPed").equals("2")) {
                                    ResultSet rset2 = con.consulta("select F_Observaciones from tb_obscancela where F_NoCompra = '" + NoCompra + "' ");
                                    while (rset2.next()) {
                            %>
                            <br/>
                            <textarea class="form-control" name="Observa" id="Observa" readonly=""><%=rset2.getString(1)%></textarea>
                            <br>
                            <%
                                    }
                                }
                            %>

                            <br/>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                    }
                                }
                            %>
                        </form>
                    </div>
                    <hr/>
                    <div class="panel panel-footer">    
                        <div class=" table table-responsive ">
                            <br/>
                            <table class="table table-bordered table-condensed table-striped" id="DetalleCompra">
                                <tr>
                                    <th><strong>Clave</strong></th>
                                    <th><strong>Descripción</strong></th>
                                    <th><strong>Solicitado</strong></th>
                                    <th><strong>Recibido</strong></th>
                                    <th><strong>Pendiente</strong></th>
                                    <th><strong>Abrir</strong></th>

                                </tr>
                                <%
                                    try {
                                        con.conectar();
                                        //ResultSet rset = con.consulta("SELECT s.F_Clave, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem FROM tb_pedido_sialss s, tb_medica m WHERE s.F_Clave = m.F_ClaPro and F_NoCompra = '" + NoCompra + "' ");
                                        //ResultSet rset = con.consulta("SELECT s.F_Clave, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem, F_Recibido FROM tb_pedido_sialss s, tb_medica m where s.F_Clave = m.F_ClaPro and F_NoCompra = '" + NoCompra + "'");
                                        ResultSet rset = con.consulta("SELECT s.F_Clave, m.F_DesPro, s.F_Cant, IFNULL(com.F_CanCom, 0) AS Ingreso, s.F_Cant - IFNULL(com.F_CanCom, 0) AS Dif, F_Recibido, s.F_StsPed, F_IdIsem, F_Obser FROM tb_pedido_sialss s INNER JOIN tb_medica m ON s.F_Clave = m.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_OrdCom = '" + NoCompra + "' GROUP BY F_ClaPro ) AS com ON s.F_Clave = com.F_ClaPro WHERE F_NoCompra = '" + NoCompra + "' AND s.F_StsPed < 3;");

                                        while (rset.next()) {
                                %>
                                <tr>
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%></td>
                                    <td><%=formatter.format(rset.getInt(3))%></td>
                                    <td><%=formatter.format(rset.getInt(4))%></td>
                                    <td><%=formatter.format(rset.getInt(5))%></td>
                                    <td>
                                        <% if (rset.getInt("F_Recibido") == 1) {%>
                                        <button class="btn btn-primary btn-block" name="detallePedido" value="<%=rset.getString("F_IdIsem")%>" onclick="aplicar(this)" > Abrir </button>
                                        <%} else {%>
                                        <button class="btn btn-primary btn-block">Abierta</button>
                                        <%}%> 
                                    </td>
                                </tr>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verFoliosM.jsp").log(Level.SEVERE, null, e);
                                        }
                                    }
                                %>

                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>              


        <!-- Modal -->
        <div class="modal fade" id="modalCambioFecha" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <form name="FormBusca" action="CapturaPedidos" method="post">
                        <div class="modal-header">
                            <div class="row">
                                <h4 class="col-sm-12">Cambiar Fecha <%=NoCompra%></h4>
                                <input type="hidden" class="form-control" name="NoCompra" id="NoCompra" value="<%=NoCompra%>" />

                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar fecha:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="date" min="<%= LocalDate.now()%>" class="form-control" required name="F_FecEnt" onkeydown="return false" id="F_FecEnt" />
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Seleccionar Hora:</h4>
                            <div class="col-sm-12">
                                <select class="form-control" id="HoraN" name="HoraN" onchange="document.getElementById('Clave').focus()">
                                    <%
                                        for (int i = 0; i < 24; i++) {
                                            if (i != 24) {
                                    %>
                                    <option value="<%=i%>:00"
                                            <%
                                                if (horEnt.equals(i + ":00")) {
                                                    out.println("selected");
                                                }
                                            %>
                                            ><%=i%>:00</option>
                                    <option value="<%=i%>:30"
                                            <%
                                                if (horEnt.equals(i + ":30")) {
                                                    out.println("selected");
                                                }
                                            %>
                                            ><%=i%>:30</option>
                                    <%
                                    } else {
                                    %>
                                    <option value="<%=i%>:00"
                                            <%
                                                if (horEnt.equals(i + ":00")) {
                                                    out.println("selected");
                                                }
                                            %>
                                            ><%=i%>:00</option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <br><br>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                <button class="btn btn-primary" name="accion" style="background-color: #5cb85c;border-color: #bd3535;" value="recalendarizar" onclick="return confirm('¿Seguro que desea cambiar la fecha y hora?')">Confirmar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal Vencimiento-->
        <div class="modal fade" id="modalCambioFechaVencimiento" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <form name="FormBusca" action="CapturaPedidos" method="post">
                        <div class="modal-header">
                            <div class="row">
                                <h4 class="col-sm-12">Cambiar vencimiento de <%=NoCompra%></h4>
                                <input type="hidden" class="form-control" name="NoCompra" id="NoCompra" value="<%=NoCompra%>" />

                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar fecha:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="date" class="form-control" required name="F_FecVen" id="F_FecVen" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                <button class="btn btn-primary" name="accion" value="vencimiento" style="background-color: #5cb85c;border-color: #bd3535;" onclick="return confirm('¿Seguro que desea cambiar la fecha de vencimiento?')">Confirmar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal Plazo-->
        <div class="modal fade" id="modalCambioPlazo" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <form name="FormBusca" action="CapturaPedidos" method="post">
                        <div class="modal-header">
                            <div class="row">
                                <h4 class="col-sm-12">Cambiar Plazo de pago de <%=NoCompra%></h4>
                                <input type="hidden" class="form-control" name="NoCompra" id="NoCompra" value="<%=NoCompra%>" />

                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar plazo:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="number" class="form-control" required name="plazo" id="plazo" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                <button class="btn btn-primary" name="accion" value="plazo" style="background-color: #5cb85c;border-color: #bd3535;" onclick="return confirm('¿Seguro que desea cambiar el plazo de pago?')">Confirmar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <%@include file="jspf/piePagina.jspf" %>
    </body>
    <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.js" type="text/javascript" ></script>
    <script src="js/jquery.alphanum.js" type="text/javascript"></script>
    <script src="js/jquery.dataTables.js" type="text/javascript"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script src="js/select2.js"></script>

    <script>

                                    $(document).ready(function () {
                                        $('#datosCompras').dataTable();
                                        $('#DetalleCompra').dataTable();
                                    });

                                    
                                    ​

                                            function CancelaCompra() {
                                                var confirma = confirm("¿Seguro que desea cancelar la orden? ");
                                                if (confirma === true) {
                                                    var obser = $('#Observaciones').val();
                                                    if (obser === "") {
                                                        alert('Favor de llenar el campo de observaciones');
                                                        document.getElementById('Observaciones').focus();
                                                        return false;
                                                    } else {
                                                        return true;
                                                    }
                                                } else {
                                                    return false;
                                                }
                                            }

                                    function focusLocus() {
                                        document.getElementById('Proveedor').focus();
                                        if ($('#Fecha').val() !== "") {
                                            document.getElementById('Clave').focus();
                                        }
                                    }

                                    function validaClaDes(boton) {
                                        var btn = boton.value;
                                        var prove = $('#Proveedor').val();
                                        var fecha = $('#Fecha').val();
                                        var hora = $('#Hora').val();
                                        var NoCompra = $('#NoCompra').val();
                                        if (prove === "" || fecha === "" || hora === "0:00" || NoCompra === "") {
                                            alert("Complete los datos");
                                            return false;
                                        }
                                        var valor = "";
                                        var mensaje = "";
                                        if (btn === "Clave") {
                                            valor = $('#Clave').val();
                                            mensaje = "Introduzca la clave";
                                        }
                                        if (btn === "Descripcion") {
                                            valor = $('#Descripcion').val();
                                            mensaje = "Introduzca la descripcion";
                                        }
                                        if (valor === "") {
                                            alert(mensaje);
                                            return false;
                                        }
                                        return true;
                                    }

                                    function validaCaptura() {

                                        var ClaPro = $('#ClaPro').val();
                                        var DesPro = $('#DesPro').val();
                                        var CanPro = $('#CanPro').val();
                                        if (ClaPro === "" | DesPro === "" || CanPro === "") {
                                            alert("Complete los datos");
                                            return false;
                                        }
                                        return true;
                                    }

                                    function confirmaModal() {
                                        var valida = confirm('Seguro que desea cambiar la fecha de entrega?');
                                        if ($('#ModalFecha').val() === "") {
                                            alert('Falta la fecha');
                                            return false;
                                        } else {
                                            if (valida) {
                                                $('#F_FecEnt').val($('#ModalFecha').val());
                                                alert($('#F_FecEnt').val($('#ModalFecha').val()));
                                                $('#formCambioFechas').submit();
                                            } else {
                                                return false;
                                            }
                                        }
                                    }
                                    function aplicar(valor) {
                                        var detallePedido;
                                        detallePedido = $(valor).attr('value');
                                        if (detallePedido !== "") {

                                            var r = confirm("¿Desea realizar el Reabir el insumo?");
                                            if (r) {
                                                var $form = $(this);
                                                $.ajax({
                                                    type: "POST",
                                                    url: "AdminOrdenesDeCompra?detallePedido=" + detallePedido,
                                                    dataType: "json",
                                                    success: function (data) {
                                                        console.log(data.msg);
                                                        if (data.msg === "ok") {
                                                            alert('Clave reabierta');
                                                            location.reload();
                                                        } else {
                                                            alert('Ocurrio un irreor intente de nuevo');
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }



    </script>
</html>
