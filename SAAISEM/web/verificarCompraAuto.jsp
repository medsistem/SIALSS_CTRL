<%-- 
    Document   : verificarCompraAuto
    Created on : 17/02/2014, 03:34:46 PM
    Author     : GNK
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.logging.Level"%>
<%@page import="conn.ConectionDB"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
    String tipo = "";
    int origenProy = 0;          
    int Proyecto = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String vOrden = "", vRemi = "", tipoRV = "", UbicaN = "";


    try {
        vOrden = (String) sesion.getAttribute("vOrden");
        vRemi = (String) sesion.getAttribute("vRemi");
        tipoRV = (String) sesion.getAttribute("tipoRV");
        UbicaN = (String) sesion.getAttribute("UbicaN");
    } catch (Exception e) {
    }

    try {
        String Folio = "";
        String folio[] = null;
        Folio = request.getParameter("NoCompra");
        if (!Folio.equals("")) {
            folio = Folio.split(",");
            sesion.setAttribute("vOrden", folio[0]);
            sesion.setAttribute("vRemi", folio[1]);
            sesion.setAttribute("tipoRV", folio[2]);
            vOrden = folio[0];
            vRemi = folio[1];
            tipoRV = folio[2];
        }
    } catch (Exception e) {
    }
    String Oc = "", tipoIns = "";
   int banBtn = 0;
   String Ori = "";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet">
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <title>SAA SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL CEDIS CENTRAL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <form action="verificarCompraAuto.jsp" method="post">
                <br/>
                <div class="row">
                    <h3>Validación Recibo</h3>
                    <h4 class="col-sm-2">Elegir Remisión: </h4>
                    <div class="col-sm-9">
                        <select class="form-control" name="NoCompra" onchange="this.form.submit();">
                            <option value="">-- Proveedor -- Orden de Compra --</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                    

                                    rset = con.consulta("SELECT c.F_OrdCom, p.F_NomPro, c.F_FolRemi, c.F_FolFac, c.F_Origen, c.F_Proyectos FROM tb_compratemp c, tb_proveedor p WHERE c.F_Provee = p.F_ClaProve and c.F_Estado = '2' GROUP BY c.F_OrdCom, c.F_FolRemi");

                                    while (rset.next()) {
                                        Oc = rset.getString(1);
                                        origenProy = rset.getInt(5);
                                        Proyecto = rset.getInt(6);
                                        if (!rset.getString(3).equals("")) {
                            %>
                            <option value="<%=rset.getString(1)%>,<%=rset.getString(3)%>,1"><%=rset.getString(2)%> - <%=rset.getString(1)%> - <%=rset.getString(3)%></option>
                            <%              } else {%>
                            <option value="<%=rset.getString(1)%>,<%=rset.getString(4)%>,2"><%=rset.getString(2)%> - <%=rset.getString(1)%> - <%=rset.getString(4)%></option>
                            <%

                                        }
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                        </select>
                    </div>
                </div>
                <br/>    
                <div class="row">    
                    <h4 class="col-sm-2">Elegir Ubicacion: </h4>
                    <div class="col-sm-3">
                        <select class="form-control" name="UbicaN" id="UbicaN" >
                            <option value="">-- Ubicacion --</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rsetU = null;
                                     ResultSet rset2 = null;
                                    rset2 = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar,C.F_FolRemi, CASE WHEN C.F_ClaPro = CON.F_ClaPro THEN 'CONTROLADO' WHEN C.F_ClaPro = V.F_ClaPro THEN 'VACUNA' ELSE 'X' END 'Tipo' FROM tb_compratemp C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar LEFT JOIN tb_controlados AS CON ON CON.F_ClaPro = M.F_ClaPro LEFT JOIN tb_vacunas AS V ON V.F_ClaPro = M.F_ClaPro WHERE F_OrdCom='" + vOrden + "' and (F_FolRemi = '" + vRemi + "' or F_FolFac = '" + vRemi + "')  and F_Estado = '2';");
                        if (rset2.next()) {
                                System.out.println("origen: " + rset2.getString("F_Origen")); 
                            }
                                    System.out.println("orden de compra: " + vOrden);
                                    System.out.println("origenProy: " + origenProy);
                                   
                                    System.out.println("Proyecto: " + Proyecto);
                                  /*  if (vOrden.contains("A1N110")) {
                                        rsetU = con.consulta("SELECT u.IdUbi, u.DescUbi FROM tb_ubicanueva AS u WHERE u.DescUbi = 'NUEVAA1N' AND u.DescUbi NOT LIKE 'NUEVA_RESGUARDO%' ORDER BY u.IdUbi ASC");

                                    } else if (Ori.equals("27") || rset2.getString("F_Origen").equals("27")) {
                                        rsetU = con.consulta("SELECT u.IdUbi, u.DescUbi FROM tb_ubicanueva AS u WHERE u.DescUbi LIKE 'NUEVA_RESGUARDO' ORDER BY u.IdUbi ASC");
                                    } else {*/
                                        rsetU = con.consulta("SELECT u.IdUbi, u.DescUbi FROM tb_ubicanueva AS u WHERE u.DescUbi LIKE '%NUEVA%' AND u.DescUbi NOT LIKE 'NUEVA_RESGUARDO%' ORDER BY u.IdUbi ASC");

                                   /* }*/
                                    while (rsetU.next()) {
                            %>
                            <option value="<%=rsetU.getString(2)%>" > <%=rsetU.getString(2)%> </option>
                            <%

                            %>
                            <%                                    }
                                    UbicaN = rsetU.getString(2);
                                } catch (Exception e) {
                                    Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                        </select>
                    </div> 
                </div>
                <br/>
            </form>
        </div>
        <div style="width: 90%; margin: auto;">
            <br/>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <!--form action="CompraAutomatica" method="get" name="formulario1"-->
                    <%
                        try {
                            con.conectar();
                            ResultSet rset = con.consulta("SELECT i.F_NoCompra, DATE_FORMAT(i.F_FecSur, '%d/%m/%Y') as F_FecSur, i.F_HorSur, p.F_NomPro, p.F_ClaProve from tb_pedido_sialss i, tb_proveedor p where i.F_Provee = p.F_ClaProve and F_StsPed = '1' and F_NoCompra = '" + vOrden + "'  and F_recibido='0' group by F_NoCompra");
                            while (rset.next()) {
                    %>
                    <div class="row">
                        <h4 class="col-sm-2">Folio Orden de Compra:</h4>
                        <div class="col-sm-2"><input class="form-control" value="<%=vOrden%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <%
                                if (tipoRV.equals("1") || tipoRV.equals("null")) {
                            %>
                        <h4 class="col-sm-1">Remisión:</h4>
                        <%} else {  %>     
                        <h4 class="col-sm-1">Factura:</h4>
                        <%}%>
                        <div class="col-sm-2"><input class="form-control" value="<%=vRemi%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                        <div class="col-sm-2"><a href="verificaCompragnr.jsp?oc=<%=vOrden%>&remision=<%=vRemi%>" class="btn btn-info form-control">Exportar&nbsp;<span class="glyphicon glyphicon-download"></span></a></div>
                    </div>
                    <div class="row">
                        <h4 class="col-sm-12">Proveedor: <%=rset.getString("p.F_NomPro")%></h4>
                    </div>
                    <div class="row">
                        <h4 class="col-sm-9">Fecha y Hora de Entrega: <%=rset.getString("F_FecSur")%> <%=rset.getString("i.F_HorSur")%></h4>
                        <!--div class="col-sm-2">
                            <a class="btn btn-default" href="compraAuto2.jsp">Agregar Clave al Inventario</a>
                        </div>-->
                    </div>
                    <%
                            }
                        } catch (Exception e) {
                            Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                        } finally {
                            try {
                                con.cierraConexion();
                            } catch (SQLException ex) {
                                Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
                            }
                        }
                    %>
                    <!--/form-->
                </div>

                <form action="nuevoAutomaticaLotes" method="post">
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <th class="hidden">TipoIsumo</th>
                                <th>Folio</th>
                                <th>Clave</th>
                                <th>Descripción</th>
                                <th>Ori</th>
                                <th>Lote</th>
                                <th>Cantidad</th>
                                <th>Costo U</th>
                                <th>IVA</th>
                                <th>Importe</th>
                                <th>Caducidad</th>
                                <th>Marca</th>
                                <th>Editar</th>
                                <th>Validar</th>
                                </thead>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar,C.F_FolRemi, CASE WHEN C.F_ClaPro = CON.F_ClaPro THEN 'CONTROLADO' WHEN C.F_ClaPro = V.F_ClaPro THEN 'VACUNA' ELSE 'X' END 'Tipo' FROM tb_compratemp C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar LEFT JOIN tb_controlados AS CON ON CON.F_ClaPro = M.F_ClaPro LEFT JOIN tb_vacunas AS V ON V.F_ClaPro = M.F_ClaPro WHERE F_OrdCom='" + vOrden + "' and (F_FolRemi = '" + vRemi + "' or F_FolFac = '" + vRemi + "')  and F_Estado = '2';");
                                        while (rset.next()) {
                                            tipoIns = rset.getString("Tipo");
                                            banBtn = 1;
                                            String F_FecCad = "", F_Cb = "", F_Marca = "";
                                            try {
                                                F_FecCad = rset.getString(5);

                                                F_Cb = rset.getString("F_Cb");
                                                if (F_Cb.equals("")) {

                                                    ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_lote WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' AND F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                    while (rset2.next()) {
                                                        F_Cb = rset2.getString("F_Cb");
                                                        F_Marca = rset2.getString("F_ClaMar");
                                                    }
                                                }

                                                if (F_Cb.equals("")) {
                                                    ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_cb WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                    while (rset2.next()) {
                                                        F_Cb = rset2.getString("F_Cb");
                                                        F_Marca = rset2.getString("F_ClaMar");
                                                    }
                                                }
                                                F_Marca = rset.getString("F_DesMar");
                                                if (F_Marca.equals("")) {
                                                    ResultSet rset2 = con.consulta("SELECT F_DesMar FROM tb_marca WHERE F_ClaMar = '" + F_Marca + "'");
                                                    while (rset2.next()) {
                                                        F_Marca = rset2.getString("F_DesMar");
                                                    }
                                                }

                                                if (F_Cb.equals(" ")) {
                                                    F_Cb = "";
                                                }
                                            } catch (Exception e) {
                                                e.getMessage();
                                            }
                                            Ori = rset.getString("F_Origen");
                                %>
                                <tbody>
                                <td class="hidden" id="tipoI"><%=rset.getString("Tipo")%></td>
                                <td><%=rset.getString("C.F_FolRemi")%></td>
                                <td><%=rset.getString("F_ClaPro")%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString("F_Origen")%></td>
                                <td><input class="form-control" value="<%=rset.getString(4)%>" name="F_Lote<%=rset.getString("F_IdCom")%>" readonly  /></td>
                                <td><input class="form-control" value="<%=rset.getString(6)%>" type="number" name="F_Cant<%=rset.getString("F_IdCom")%>" readonly  /></td>
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>
                                <td>
                                    <%
                                        if (F_FecCad.equals("")) {
                                    %>
                                    <input type="date" class="form-control" name="F_FecCad<%=rset.getString("F_IdCom")%>" readonly />
                                    <%
                                    } else {
                                    %>
                                    <input type="date" class="form-control" name="F_FecCad<%=rset.getString("F_IdCom")%>"  value="<%=F_FecCad%>" readonly />
                                    <%
                                        }
                                    %>
                                </td>
                                <td>
                                    <input value="<%=F_Marca%>" class="form-control" name="F_Marca<%=rset.getString("F_IdCom")%>" id="marca<%=rset.getString("F_IdCom")%>" readonly/>
                                </td>
                                <td>
                                    <button class="btn btn-block btn-primary" id="btnEdit" type="button" onclick="editar(<%=rset.getString("F_IdCom")%>)" ><span class="glyphicon glyphicon-edit" ></span></button>
                                </td>
                                <td>
                                    <% if (rset.getString("Tipo").equals("X")) {%>
                                    <button type="button" class="btn btn-info form-control glyphicon glyphicon-ok" id="Validar_<%=rset.getString("F_IdCom")%>"></button>
                                    <% } %>
                                </td>
                                </tbody>
                                <%
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (SQLException ex) {
                                            Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }

                                %>

                            </table>
                        </div>
                        <hr/>
                    </div>
                    <%                                if (banBtn == 1) {
                    %>

                    <div class="panel-footer">
                        <div class="row">

                            <input name="vOrden" id="vOrden" type="text" style="" class="hidden" value='<%=vOrden%>' />
                            <input name="vRemi" id="vRemi" type="text" style="" class="hidden" value='<%=vRemi%>' />
                            <input name="vRemi" id="tipoRV" type="text" style="" class="hidden" value='<%=tipoRV%>' />
                            <input name="UbicaN" id="UbicaN2" type="text" style="" class="hidden" value='<%=UbicaN%>' />
                            <input name="Origen" id="Origen" type="text" style="" class="hidden" value='<%=Ori%>' />

                            <div class="col-lg-3 col-lg-offset-3">
                                <button  value="EliminarVerifica" id="CancelarRemision" name="accion" class="btn btn-cancel btn-block" onclick="return confirm('Seguro que desea eliminar la compra?');">
                                    <%
                                        if (tipoRV.equals("1") || tipoRV.equals("null")) {
                                    %>
                                    Cancelar Remisión
                                    <%} else {%>
                                    Cancelar Factúra
                                    <%}%>
                                </button>
                            </div>
                            <div class="col-lg-3">
                                <button  value="GuardarAbiertaVerifica" name="accion" id="ConfirmarRemision"  class="btn btn-primary  btn-block" onclick="return confirm('Seguro que desea liberar la compra?')">
                                    <%
                                        if (tipoRV.equals("1") || tipoRV.equals("null")) {
                                    %>
                                    Confirmar Remisión
                                    <%} else {%>
                                    Confirmar Factúra
                                    <%}%>
                                </button>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>

                </form>
            </div>
        </div>

        <!--////////MODAL PARA EDITA/////-->
        <button type="button" class="btn hidden" id="btnModal" data-toggle="modal" data-target="#myModal"></button>
        <div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h3 class="modal-title text-center text-success" id="myModalLabel1">Editar registro</h3>
                        <input id="idCompraTemporal" type="hidden">
                        <input id="UserActual" type="hidden" value="<%=usua%>">
                    </div>
                    <div class="modal-body">
                        <div class="form-group" >
                            <label  for="loteNuevo" >Lote</label>
                            <input class="form-control" id="loteNuevo">
                            <label  for="CaducidadNuevo" >Caducidad</label>
                            <input class="form-control" id="CaducidadNuevo" >                            
                            <label  for="CbNuevo" >Cb</label>
                            <input class="form-control" id="CbNuevo">
                            <label  for="marcaNuevo" >Marca</label>
                            <input class="form-control" id="marcaNuevo" onkeyup="descripMarc()">
                            <label  for="CantidadNuevo" >Cantidad</label>
                            <input class="form-control" id="CantidadNuevo" type="number" min="1" readonly="">
                            <label  for="CantidadNuevo" >Costo U</label>
                            <input class="form-control" id="costo" type="text" readonly="">
                        </div>
                    </div>
                    <% if (Proyecto == 7) { %>
                    <div class="modal-body">
                        <div class="form-group" >

                            <input class="col-sm-1" id="tarimasNuevo" type="hidden">
                            <input class="col-sm-1" id="pzacajasNuevo" type="hidden">

                            <label  for="cajasNuevo" class="col-sm-2" >Cajas</label>
                            <input class="col-sm-1" id="cajasNuevo" type="number" min="1">
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default glyphicon glyphicon-refresh" id="btnRes1" >Guardar</button>
                                <button type="button" class="btn btn-info" data-dismiss="modal" id="btnCancel" >Cancelar</button>
                            </div>
                        </div>
                    </div>
                    <%    } else { %>
                    <div class="modal-body">
                        <div class="form-group" >
                            <label  for="tarimasNuevo" class="col-sm-1" >Tarimas</label>
                            <input class="col-sm-1" id="tarimasNuevo" type="number" min="1">
                            <label  for="cajasNuevo" class="col-sm-2" >Cajas x Tarima</label>
                            <input class="col-sm-1" id="cajasNuevo" type="number" min="1">
                            <label  for="pzacajasNuevo" class="col-sm-2" >Piezas x Caja</label>
                            <input class="col-sm-1" id="pzacajasNuevo" type="number" min="1">
                        </div>

                    </div>
                    <div class="modal-body">
                        <div class="form-group" >   
                            <label  for="cajasiNuevo" class="col-sm-2" >Cajas x Tarima Inconpleta</label>
                            <input class="col-sm-1" id="cajasiNuevo" type="number" min="1">
                            <label  for="restoNuevo" class="col-sm-2" >Resto</label>
                            <input class="col-sm-1" id="restoNuevo" type="number" min="1">
                            <label  for="factorEmpaqueNuevo" class="col-sm-2" >Factor de Empaque</label>
                            <input class="col-sm-1" id="factorEmpaqueNuevo" type="number" min="1">
                        </div>
                        <div class="col-sm-12">
                            <label for="cantPedido" class="hidden"> cantidad Pedido</label>
                            <input class="col-sm-0" id="cantPedido" type="hidden">
                            <label for="cantCompra" class="hidden"> cantidad Compra</label>
                            <input class="col-sm-0" id="cantCompra" type="hidden">
                            <label for="cantidadTemp" class="hidden"> cantidad Temp</label>
                            <input class="col-sm-0" id="cantidadTemp" type="hidden">

                        </div>
                    </div>
                    <br/>
                    <hr/>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" id="btnSave1" >Guardar</button>
                        <button type="button" class="btn btn-info" data-dismiss="modal" id="btnCancel" >Cancelar</button>

                    </div>


                    <%    }  %>

                </div>
            </div>
        </div>
        <!--////////FIN DE MODAL PARA EDITA/////-->
        <%@include file="jspf/piePagina.jspf" %>

        <!--////////MODAL PARA RECHAZAR/////-->
        <div class="modal fade" id="Rechazar" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="Rechazos" method="get">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-5">
                                    <h4 class="modal-title" id="myModalLabel">Rechazar Orden de Compra</h4>
                                </div>
                                <div class="col-sm-2">
                                    <input name="NoCompraRechazo" id="NoCompraRechazo" value="" class="form-control" readonly="" />
                                </div>
                            </div>
                            <div class="row">

                                <div class="col-sm-12">
                                    Proveedor:
                                </div>
                                <div class="col-sm-12">
                                    Fecha y Hora 
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Observaciones de Rechazo</h4>
                                </div>
                                <div class="col-sm-12">
                                    <textarea class="form-control" placeholder="Observaciones" name="rechazoObser" id="rechazoObser" rows="5"></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Fecha de nueva recepción</h4>
                                </div>
                                <div class="col-sm-6">
                                    <input type="date" class="form-control" id="FechaOrden" name="FechaOrden" />
                                </div>
                                <div class="col-sm-6">
                                    <select class="form-control" id="HoraOrden" name="HoraOrden">
                                        <option value=":00">:00</option>
                                        <option value=":30">:30</option>
                                        <option value=":00">:00</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Correo del proveedor</h4>
                                    <input type="email" class="form-control" id="correoProvee" name="correoProvee" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <h4>Claves a Cancelar</h4>
                                    <h6>*Deseleccione las claves que no va a cancelar</h6>
                                </div>
                                <div class="col-sm-6">
                                    <div class="checkbox">
                                        <h4><input type="checkbox" checked name="todosChk" id="todosChk" onclick="checkea(this)">Seleccionar todas</h4>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <table class="table table-bordered">

                                        <tr>

                                            <td>
                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" checked="" name="chkCancela" value="">
                                                    </label>
                                                </div>
                                            </td>

                                        </tr>

                                    </table>
                                </div>
                            </div>
                            <div class="text-center" id="imagenCarga" style="display: none;" > 
                                <img src="imagenes/ajax-loader-1.gif" alt="loader">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" onclick="return validaRechazo();
                                    " name="accion" value="Rechazar">Rechazar OC</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!--////////FIN MODAL PARA RECHAZAR/////-->


        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/recepcion/recepcionEdit.js"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

        <script type="text/javascript">

                                $('#ConfirmarRemision').click(function () {
                                    var tipoI = $("tipoI").val();
                                    if ( tipoI != "CONTROLADO"){
                                    var UbicaN = $("#UbicaN option:selected").val();
                                    if ($('#UbicaN').val().trim() === '') {
                                        alert('Debe seleccionar una ubicación');
                                        return false;
                                    } else {
                                        alert('La ubicación es;' + UbicaN);
                                    }
                                }});


                                function descripMarc() {
            <%
                try {
                    con.conectar();

            %>
                                    var availableTags = [
            <%                ResultSet rset1 = con.consulta("select F_DesMar from tb_marca");
                while (rset1.next()) {
                    out.println("'" + rset1.getString(1) + "',");
                }
            %>
                                    ];
                                    $("#marcaNuevo").autocomplete({
                                        source: availableTags
                                    });
            <%
                } catch (Exception e) {
                    Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                    e.printStackTrace();
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
                }
            %>
                                }

        </script>
        <script>
            function editar(valor)
            {

                $.ajax({
                    url: "${pageContext.servletContext.contextPath}/recepcionTransaccional",
                    data: {accion: "Editar", id: valor},
                    type: 'POST',
                    dataType: 'JSON',
                    async: true,
                    success: function (data)
                    {
            <% if (Proyecto == 7) { %>
                        $("#loteNuevo").val(data.lote);
                        $("#CantidadNuevo").val(data.cantidad);
                        $("#CbNuevo").val(data.cb);
                        $("#CaducidadNuevo").val(data.caducidad);
                        $("#marcaNuevo").val(data.marca);
                        $("#tarimasNuevo").val(data.tarimas);
                        $("#cajasNuevo").val(data.cajas);
                        $("#pzacajasNuevo").val(data.cajas);
                        $("#costo").val(data.costo);
                        $("#idCompraTemporal").val(valor);
                        $("#cantPedido").val(data.cantPedido);
                        $("#cantCompra").val(data.cantCompra);
                        $("#cantidadTemp").val(data.cantidadTemp);
                        $("#btnModal").click();
            <%    } else {  %>
                        //alert(data.cb);
                        $("#loteNuevo").val(data.lote);
                        $("#CantidadNuevo").val(data.cantidad);
                        $("#CbNuevo").val(data.cb);
                        $("#CaducidadNuevo").val(data.caducidad);
                        $("#marcaNuevo").val(data.marca);
                        $("#tarimasNuevo").val(data.tarimas);
                        $("#cajasNuevo").val(data.cajas);
                        $("#pzacajasNuevo").val(data.pzacajas);
                        $("#cajasiNuevo").val(data.cajasi);
                        $("#restoNuevo").val(data.resto);
                        $("#costo").val(data.costo);
                        $("#factorEmpaqueNuevo").val(data.factorEmpaque);
                        $("#idCompraTemporal").val(valor);
                        $("#cantPedido").val(data.cantPedido);
                        $("#cantCompra").val(data.cantCompra);
                        $("#cantidadTemp").val(data.cantidadTemp);
                        $("#btnModal").click();


            <%    }%>




                    }, error: function (jqXHR, textStatus, errorThrown) {
                        alert("Error en sistema");
                    }
                });
            }
            $("#UbicaN").on('change', function (val)
            {
                $("#UbicaN2").val($("#UbicaN").val());
            }
            );



        </script>
    </body>
</html>
