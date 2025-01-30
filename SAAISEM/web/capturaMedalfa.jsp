<%-- 
    Document   : capturaISEM.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<!--%@page import="conn.ConectionDB_Linux"%-->
<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,##0.00");
    DecimalFormat formNoCom = new DecimalFormat("000");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", IdUsu = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        IdUsu = (String) sesion.getAttribute("IdUsu");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();
    CapturaPedidos indice = new CapturaPedidos();
    String proveedor = "", fecEnt = "", horEnt = "", claPro = "", desPro = "", NoCompra = "", origen = "", cedis = "", Proyecto = "", Desproyecto = "", Campo = "", fechaVencimiento = "", dias = "", NomOC = "", IdOC = "";
    int Ban = 0;
    try {
        Proyecto = request.getParameter("Proyecto");
        Desproyecto = request.getParameter("DesProyecto");
        Campo = request.getParameter("Campo");
        NomOC = request.getParameter("NomOC");
        IdOC = request.getParameter("IdOC");
        Ban = Integer.parseInt(request.getParameter("Ban"));
        NoCompra = (String) sesion.getAttribute("NoCompra");
        proveedor = (String) sesion.getAttribute("proveedor");
        fecEnt = (String) sesion.getAttribute("fec_entrega");
        horEnt = (String) sesion.getAttribute("hor_entrega");
        claPro = (String) sesion.getAttribute("clave");
        desPro = (String) sesion.getAttribute("descripcion");
        origen = (String) sesion.getAttribute("origen");
        cedis = (String) sesion.getAttribute("cedis");
        fechaVencimiento = (String) sesion.getAttribute("FechaVencimiento");
        dias = (String) sesion.getAttribute("dias");

        if (Ban == 1) {
            NoCompra = NomOC + IdOC;
        } else {
            NoCompra = NoCompra;
        }

    } catch (Exception e) {

    }
    if (proveedor == null) {
        proveedor = "";
        fecEnt = "";
        horEnt = "";
    }
    if (claPro == null) {
        claPro = "";
        desPro = "";
        origen = "";
    }

    if (NoCompra == null) {
        NoCompra = "";
    }

    if (Desproyecto == null) {
        Desproyecto = "";
    }

    if (Campo == null) {
        Campo = "";
    }

    if (NomOC == null) {
        NomOC = "";
    }

    if (IdOC == null) {
        IdOC = "";
    }

    if (cedis == null) {
        cedis = request.getParameter("cedis");
    }

    /*if (NoCompra == null || NoCompra.equals("")) {
        try {
            con.conectar();

            ResultSet rset = con.consulta("SELECT maxi.F_NoCompra, p.F_StsPed FROM tb_pedido_sialss AS p JOIN ( SELECT MAX(DISTINCT F_NoCompra) AS F_NoCompra FROM tb_pedido_sialss WHERE F_IdUsu =" + IdUsu + ") AS maxi ON p.F_NoCompra = maxi.F_NoCompra;");
            while (rset.next()) {
                if (rset.getInt("F_StsPed") == 0) {
                    NoCompra = rset.getString("F_NoCompra");
                }
            }
            if (NoCompra == null || NoCompra.equals("")) {
                rset = con.consulta("select MAX(F_NoCompra) as F_NoCompra from tb_pedido_sialss");
                int F_IndIsem = 0, maxIndice = 0;
                while (rset.next()) {
                    String NoMax[] = null;
                    if (rset.getString("F_NoCompra") == null || rset.getString("F_NoCompra").equals("")) {
                        NoMax = "1".split("-");
                    } else {
                        NoMax = rset.getString(1).split("-");
                    }

                    maxIndice = Integer.parseInt(NoMax[0]);
                }
                rset = con.consulta("select F_IndIsem from tb_indice");
                while (rset.next()) {
                    F_IndIsem = rset.getInt("F_IndIsem");
                }

                
                NoCompra = indice.noCompra();
                NoCompra = formNoCom.format(Integer.parseInt(NoCompra)) + "-2017";
                sesion.setAttribute("NoCompra", NoCompra);
            }

        } catch (Exception e) {
            Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
            }
        }
    }*/
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Captura</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="shortcut icon"
              href="imagenes/system-settings-icon_31831.png" />
        <link href="css/select2.css" rel="stylesheet">

    </head>
    <body onload="focusLocus();
            SelectProve(FormBusca);">
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <h4>Captura OC Proyecto <%=Desproyecto%></h4>
            <!--div class="row">
                <div class="col-sm-11">
                    <a class="btn btn-default" href="capturaMedalfa.jsp">Captura de Órdenes de Compra</a>
                    <a class="btn btn-default" href="verFoliosIsem2017.jsp">Ver Órdenes de Compra</a>
                </div>
            </div-->
            <hr/>
            <br/>

            <form name="FormBusca" action="CapturaPedidos" method="post">
                <div class="row">
                    <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                    <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                    <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                    <input type="hidden" class="form-control" id="NomOC" name="NomOC" value="<%=NomOC%>" />
                    <input type="hidden" class="form-control" id="IdOC" name="IdOC" value="<%=IdOC%>" />
                    <input type="hidden" class="form-control" id="BanOC" name="BanOC" value="<%=Ban%>" />
                    <label class="col-sm-3 col-sm-offset-5 text-right">
                        <h4>Número de Orden de Compra</h4>
                    </label>
                    <input class="hidden" value="<%=cedis%>" name="nomCedis" >
                    <div class="col-sm-2">
                        <input type="text" class="form-control" id="NoCompra" name="NoCompra" value="<%=NoCompra%>" />
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-primary btn-block" name="accion" value="LimpiarOC">Limpiar NO OC</button>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <label class="col-sm-1">
                        <h4>Proveedor:</h4>
                    </label>
                    <div class="col-sm-8">
                        <select class="form-control" name="Proveedor" id="Proveedor" onchange="SelectProve(this.form);
                                document.getElementById('Fecha').focus()">
                            <option value="">--Proveedor--</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT F_ClaProve,F_NomPro FROM tb_proveedor;");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"
                                    <%
                                        if (proveedor.equals(rset.getString(1))) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString(2)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>

                        </select>


                    </div>


                </div>
                <div class="row">
                    <label class="col-sm-2">
                        <h4>Fecha de Entrega:</h4>
                    </label>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" id="Fecha" name="Fecha" value="<%=fecEnt%>" onchange="document.getElementById('Hora').focus()" />
                    </div>
                    <label class="col-sm-2">
                        <h4>Hora de Entrega:</h4>
                    </label>
                    <div class="col-sm-2">
                        <select class="form-control" id="Hora" name="Hora" onchange="document.getElementById('Clave').focus()">
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
                </div>
                <br/>
                <div class="row">
                    <label class="col-sm-2">
                        <h4>Fecha de Vencimiento:</h4>
                    </label>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" id="FechaVencimiento" name="FechaVencimiento" value="<%=fechaVencimiento%>" onchange="document.getElementById('Hora').focus()" />
                    </div>
                    <label class="col-sm-2">
                        <h4>Plazo de pago(dias)</h4>
                    </label>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" id="dias" name="dias" value="<%=dias%>" onchange="document.getElementById('Hora').focus()" />
                    </div>
                </div>
                <br/>
                <div class="row">

                    <label class="col-sm-1 text-right">
                        <h4>Clave:</h4>
                    </label>
                    <div class="col-sm-8">
                        <select name="Clave" id="Clave" class="form-control">
                            <option>-- Seleccione --</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT F_ClaPro,CONCAT(F_ClaPro,' - ',F_DesPro) FROM tb_medica WHERE " + Campo + "= 1 AND F_StsPro='A';");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"
                                    <%
                                        if (proveedor.equals(rset.getString(1))) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString(2)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-sm-1">
                        <button class="btn btn-primary btn-block" onclick="return validaClaDes(this);" name="accion" value="Clave">Clave</button>
                    </div>

                </div>
            </form>
            <br/>
            <form name="FormCaptura" action="CapturaPedidos" method="post">
                <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                <input type="hidden" class="form-control" id="NomOC" name="NomOC" value="<%=NomOC%>" />
                <input type="hidden" class="form-control" id="IdOC" name="IdOC" value="<%=IdOC%>" />
                <input type="hidden" class="form-control" id="BanOC" name="BanOC" value="<%=Ban%>" />
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="row">
                            <label class="col-sm-1 text-right">
                                <h4>Clave:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=claPro%>" name="ClaPro" id="ClaPro"/>
                            </div>
                            <label class="col-sm-2">
                                <h4>Descripción:</h4>
                            </label>
                            <div class="col-sm-7">
                                <input type="text" class="form-control" readonly value="<%=desPro%>" name="DesPro" id="DesPro"/>
                            </div>
                        </div>

                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select pp.F_CantMax, pp.F_CantMin, m.F_PrePro from tb_prodprov2017 pp, tb_medica m where m.F_ClaPro = pp.F_ClaPro and pp.F_ClaPro = '" + claPro + "'and pp.F_ClaProve = '" + proveedor + "' ");
                                if (rset.next()) {
                                    int cantUsada = 0;
                                    int cantMax = 0;
                                    cantMax = rset.getInt(1);
                                    ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_pedido_sialss where F_Clave='" + claPro + "' and F_StsPed !='2'  AND F_Provee='" + proveedor + "'");
                                    while (rset2.next()) {
                                        cantUsada = rset2.getInt(1);
                                    }
                                    int cantRestante = cantMax - cantUsada;
                        %>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Cantidad Enviada:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantUsada)%>" name="" id=""/>
                            </div>
                            <label class="col-sm-2">
                                <h4>Cantidad Máxima:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantMax)%>" name="" id=""/>
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Cantidad Restante:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantRestante)%>" name="CantRest" id="CantRest"/>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-2">
                                <h4>Presentación</h4>
                            </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" readonly value="<%=rset.getString(3)%>" name="" id=""/>
                            </div>
                        </div>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                            } finally {
                                try {
                                    con.cierraConexion();
                                } catch (SQLException ex) {
                                    Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                                }
                            }
                        %>
                        <div class="row">
                            <%
                                String cantidad = "0";
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta(" select SUM(F_ExiLot) from tb_lote WHERE F_Proyecto ='" + Proyecto + "' AND F_ClaPro = '" + claPro + "' group by F_ClaPro  ");
                                    while (rset.next()) {
                                        cantidad = rset.getString(1);
                                    }
                                    if (cantidad == null) {
                                        cantidad = "0";
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                            <label class="col-sm-2 text-center">
                                <h4>Exist. en Almacén:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="CantAlm" id="CantAlm" readonly="" value="<%=formatter.format(Integer.parseInt(cantidad))%>" />
                            </div>
                            <label class="col-sm-2 text-center">
                                <h4>No. de Entrega:</h4>
                            </label>
                            <div class="col-sm-2">
                                <select  class="form-control" name="Prioridad" id="Prioridad" onchange="document.getElementById('CanPro').focus()" >
                                    <option selected="">1-2022</option>
                                    <option>2-2022</option>
                                    <option>3-2022</option>
                                    <option>4-2022</option>
                                    <option>5-2022</option>
                                    <option>6-2022</option>
                                    <option>ND</option>
                                </select>
                            </div>
                            <label class="hidden">
                                <h4>Lote</h4>
                            </label>
                            <div class="hidden">
                                <input type="text" class="form-control" name="LotPro" id="LotPro" />
                            </div>
                            <label class="hidden">
                                <h4>Caducidad</h4>
                            </label>
                            <div class="hidden">
                                <input type="text" class="form-control" data-date-format="dd/mm/yyyy" readonly="" name="CadPro" id="CadPro"/>
                            </div><label class="col-sm-2 text-right">
                                <h4>Pzs a Entregar:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="CanPro" id="CanPro" data-behavior="only-num" />
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Precio:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" onchange="calculaTotal()" name="Precio" id="Precio" data-behavior="only-num" required="" />
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Descuento %:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" onchange="calculaTotal()" name="descuento" id="descuento" data-behavior="only-num" required="" value="0"/>
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>IVA %:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" onchange="calculaTotal()" name="iva" id="iva" data-behavior="only-num" required="" value="0"/>
                            </div>
                        </div>
                            <hr>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Subtotal:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="subtotalDis" data-behavior="only-num" disabled />
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Monto Descuento:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control"  id="descuentoDis" data-behavior="only-num" disabled/>
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Monto IVA:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control"  id="ivaDis" data-behavior="only-num" disabled/>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Total:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control"  id="totalDis" data-behavior="only-num" disabled />
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Observaciones</h4>
                            </label>
                            <div class="col-sm-10">
                                <textarea id="Observaciones" name="Observaciones" class="form-control" rows="1" data-behavior="only-alphanum-white" maxlength="20"></textarea>
                            </div>
                        </div>
                        <br/>
                        <button class="btn btn-block btn-primary" name="accion" value="capturar" onclick="return validaCaptura();">Capturar</button>

                    </div>

                </div>
            </form>
            <br/>
            <table class="table table-bordered table-condensed table-striped">
                <tr>
                    <td><strong>Clave</strong></td>
                    <td><strong>Descripción</strong></td>
                    <td><strong>Fecha</strong></td>
                    <td><strong>Hora</strong></td>
                    <td><strong>Cantidad</strong></td>
                    <td><strong>Precio</strong></td>
                    <td><strong>Descuento</strong></td>
                    <td><strong>IVA</strong></td>
                    <td></td>
                </tr>
                <%
                    int banConfirma = 0;
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select s.F_Clave, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem, DATE_FORMAT(F_FecSur, '%d/%m/%Y'), F_HorSur, F_Iva, F_Descuento, F_Precio from tb_pedido_sialss s, tb_medica m where s.F_Clave = m.F_ClaPro and F_IdUsu = '" + (String) sesion.getAttribute("IdUsu") + "' and F_NoCompra = '" + NoCompra + "' and F_StsPed = '0' ");
                        while (rset.next()) {
                            banConfirma = 1;
                %>
                <tr>
                    <td><%=rset.getString(1)%></td>
                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getString(7)%></td>
                    <td><%=rset.getString(8)%></td>
                    <td><%=formatter.format(rset.getInt(5))%></td>
                    <td><%=formatter.format(rset.getDouble(11))%></td>
                    <td><%=formatter.format(rset.getDouble(10))%></td>
                    <td><%=formatter.format(rset.getDouble(9))%></td>
                    <td>
                        <form action="CapturaPedidos" method="post">
                            <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                            <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                            <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                            <input type="hidden" class="form-control" id="NomOC" name="NomOC" value="<%=NomOC%>" />
                            <input type="hidden" class="form-control" id="IdOC" name="IdOC" value="<%=IdOC%>" />
                            <input type="hidden" class="form-control" id="BanOC" name="BanOC" value="<%=Ban%>" />
                            <input name="id" value="<%=rset.getString(6)%>" class="hidden" />
                            <button class="btn btn-info" name="accion" value="eliminaClave"><span class="glyphicon glyphicon-remove"></span></button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                    } finally {
                        try {
                            con.cierraConexion();
                        } catch (SQLException ex) {
                            Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                        }
                    }
                %>

            </table>
            <div class="row">
                <%
                    if (banConfirma == 1) {
                %>
                <form name="FormCaptura" action="CapturaPedidos" method="post">
                    <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                    <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                    <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                    <input type="hidden" class="form-control" id="NomOC" name="NomOC" value="<%=NomOC%>" />
                    <input type="hidden" class="form-control" id="IdOC" name="IdOC" value="<%=IdOC%>" />
                    <input type="hidden" class="form-control" id="BanOC" name="BanOC" value="<%=Ban%>" />
                    <div class="col-sm-6">
                        <button class="btn btn-info btn-block" name="accion" value="cancelar" onclick="return confirm('¿Seguro que desea CANCELAR el pedido?')">Cancelar</button>
                    </div>
                    <div class="col-sm-6">
                        <input class="hidden" name="NoCompra" value="<%=NoCompra%>"/>
                        <button class="btn btn-primary btn-block" name="accion" value="confirmar" onclick="return confirm('¿Seguro que desea CONFIRMAR el pedido?')">Confirmar Orden de Compra</button>
                    </div>
                </form>
                <%
                    }
                %>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/select2.js" type="text/javascript"></script>


        <script type="text/javascript">

                            $("#Proveedor").select2();
                            $("#Clave").select2();
                            var cedis = "<%=cedis%>";

                            $("#cedis option[value=" + cedis + "] ").prop('selected', 'selected');
                            $("#cedis").val(cedis);
                            $("[data-behavior~=only-alphanum-white]").alphanum({
                                allow: '.',
                                disallow: "'",
                                allowSpace: true
                            });
                            $("[data-behavior~=only-num]").numeric({
                                allowMinus: false,
                                allowThouSep: false
                            });




                            function focusLocus() {
                                document.getElementById('Proveedor').focus();
                                if (document.getElementById('Fecha').value !== "") {
                                    document.getElementById('Clave').focus();
                                }
                                if (document.getElementById('ClaPro').value !== "") {
                                    document.getElementById('Prioridad').focus();
                                }
                            }

                            function validaClaDes(boton) {


                                var btn = boton.value;
                                var prove = document.getElementById('Proveedor').value;
                                var fecha = document.getElementById('Fecha').value;
                                var fechaVe = document.getElementById('FechaVencimiento').value;
                                var fechaConvertida = fecha.replace(/-/g, ''); // Cualquier número que quieras
                                var fechaVeConvertida = fechaVe.replace(/-/g, ''); // Cualquier número que quieras

                                if (fechaConvertida >= fechaVeConvertida) {
                                    if (confirm("La fecha de vencimiento es menor a la fecha de entrega desea cambiarla?.")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }

                                var hora = document.getElementById('Hora').value;
                                var cedis = document.getElementById('cedis').value;
                                var NoCompra = document.getElementById('NoCompra').value;
                                var fechaVe = document.getElementById('FechaVencimiento').value;
                                var fechaConvertida = fecha.replace(/-/g, ''); // Cualquier número que quieras
                                var fechaVeConvertida = fechaVe.replace(/-/g, ''); // Cualquier número que quieras

                                if (prove === "" || fecha === "" || hora === "0:00" || NoCompra === "" || cedis === "") {
                                    alert("Complete los datos");

                                    return false;
                                }
                                var valor = "";
                                var mensaje = "";
                                if (btn === "Clave") {
                                    valor = document.getElementById('Clave').value;
                                    mensaje = "Introduzca la clave";
                                }
                                if (btn === "Descripcion") {
                                    valor = document.getElementById('Descripcion').value;
                                    mensaje = "Introduzca la descripcion";
                                }
                                if (valor === "") {
                                    alert(mensaje);
                                    return false;
                                }
                            }

                            function validaCaptura() {
                                var ClaPro = document.getElementById('ClaPro').value;
                                var DesPro = document.getElementById('DesPro').value;
                                var CanPro = document.getElementById('CanPro').value;
                                var proveedor = document.getElementById('Proveedor').value;
                                var Precio = document.getElementById('Precio').value;
                                if (ClaPro === "" |DesPro === "" || CanPro === "" || Precio === "") {
                                    alert("Complete los datos");
                                    return false;
                                }
                                var CanRes = document.getElementById('CantRest').value;
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                if (parseInt(CanRes) < parseInt(CanPro) && proveedor !== '63' && proveedor !== '19') {
                                    alert("La Cantidad Solicitada no puede ser mayor a la Cantidad Restante");
                                    return false;
                                }
                                return true;
                            }
                            function SelectProve(form) {
            <%
                /*try {
                    con.conectar();
                    ResultSet rset3 = con.consulta("SELECT DISTINCT F_ClaProve FROM tb_prodprov2017");
                    while (rset3.next()) {
                        out.println("if (form.Proveedor.value == '" + rset3.getString(1) + "') {");
                        out.println("var select = document.getElementById('Clave');");
                        out.println("select.options.length = 0;");
                        int i = 1;
                        ResultSet rset4 = con.consulta("SELECT F_ClaPro FROM tb_prodprov2017  WHERE  F_ClaProve='" + rset3.getString(1) + "';");

                        out.println("select.options[select.options.length] = new Option('-Seleccione-', '');");
                        while (rset4.next()) {
                            out.println("select.options[select.options.length] = new Option('" + rset4.getString(1) + "', '" + rset4.getString(1) + "');");
                            i++;
                        }
                        out.println("}");
                    }
                } catch (Exception e) {
                    Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (Exception ex) {
                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                    }
                }*/
            %>
                            }
                            function calculaTotal(){
                                var CanPro = document.getElementById('CanPro').value;
                                var costoUnitario = document.getElementById('Precio').value;
                                var descuento = document.getElementById('descuento').value;
                                var iva = document.getElementById('iva').value;
                                var total=0;
                                var subtotal=0;
                                var descuentoCalc=0;
                                var ivaCalc=0;
                                CanPro = parseFloat(CanPro);
                                if(costoUnitario !== ""){
                                    costoUnitario = parseFloat(costoUnitario);
                                    subtotal = costoUnitario * CanPro;
                                    total = subtotal;
                                    if(descuento !== ""){
                                        descuento = parseFloat(descuento);
                                        descuentoCalc=costoUnitario -(costoUnitario * (1 - (descuento/100.00)));
                                        subtotal =costoUnitario * (1 - (descuento/100.00));
                                        subtotal = subtotal * CanPro;
                                        total= subtotal;
                                    }
                                    if(iva !== ""){
                                        ivaCalc= subtotal*(parseFloat(iva)/100.00);
                                        total = subtotal*(1+ (parseFloat(iva)/100.00));
                                    }
//                                    document.getElementById('subtotal').value= subtotal;
                                    document.getElementById('subtotalDis').value= subtotal.toFixed(2);
                                    document.getElementById('ivaDis').value= ivaCalc.toFixed(2);
                                    document.getElementById('totalDis').value= total.toFixed(2);
                                    document.getElementById('descuentoDis').value= descuentoCalc.toFixed(2);
                                }
                            }
        </script>
    </body>

</html>
