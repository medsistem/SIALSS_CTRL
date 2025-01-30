<%-- 
    Document   : verificarCompraAuto
    Created on : 17/02/2014, 03:34:46 PM
    Author     : MEDALFA
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
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String vOrden = "", vRemi = "";

    try {
        vOrden = (String) sesion.getAttribute("vOrden");
        vRemi = (String) sesion.getAttribute("vRemi");
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
            vOrden = folio[0];
            vRemi = folio[1];
        }
    } catch (Exception e) {
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet">
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <form action="verificarCompraAutoCross.jsp" method="post">
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

                                    rset = con.consulta("SELECT c.F_OrdCom, p.F_NomPro, c.F_FolRemi FROM tb_compratemp c, tb_proveedor p WHERE c.F_Provee = p.F_ClaProve and c.F_Estado = '3' GROUP BY c.F_OrdCom, c.F_FolRemi");

                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>,<%=rset.getString(3)%>"><%=rset.getString(2)%> - <%=rset.getString(1)%> - <%=rset.getString(3)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, ex);
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
                    <form action="CompraAutomatica" method="get" name="formulario1">
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("SELECT i.F_NoCompra, DATE_FORMAT(i.F_FecSur, '%d/%m/%Y') as F_FecSur, i.F_HorSur, p.F_NomPro, p.F_ClaProve from tb_pedido_sialss i, tb_proveedor p where i.F_Provee = p.F_ClaProve and F_StsPed = '1' and F_NoCompra = '" + vOrden + "'  and F_recibido='0' group by F_NoCompra");
                                while (rset.next()) {
                        %>
                        <div class="row">
                            <h4 class="col-sm-2">Folio Orden de Compra:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=vOrden%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <h4 class="col-sm-1">Remisión:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=vRemi%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-12">Proveedor: <%=rset.getString("p.F_NomPro")%></h4>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-9">Fecha y Hora de Entrega: <%=rset.getString("F_FecSur")%> <%=rset.getString("i.F_HorSur")%></h4>
                            <!--div class="col-sm-2">
                                <a class="btn btn-default" href="compraAuto2.jsp">Agregar Clave al Inventario</a>
                            </div-->
                        </div>
                        <%
                                }
                            } catch (Exception e) {
                                Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, e);
                            } finally {
                                try {
                                    con.cierraConexion();
                                } catch (SQLException ex) {
                                    Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, ex);
                                }
                            }
                        %>
                    </form>
                </div>

                <form action="nuevoAutomaticaLotes" method="post">
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td>Remisión</td>
                                    <td>Clave</td>
                                    <td>Ori</td>
                                    <td>Lote</td>                      
                                    <td>Cantidad</td>                      
                                    <td>Costo U</td>                     
                                    <td>IVA</td>                       
                                    <td>Importe</td>
                                    <td>Código de Barras</td>
                                    <td>Caducidad</td>  
                                    <td>Marca</td>
                                    <td>Editar</td>
                                </tr>
                                <%
                                    int banBtn = 0;
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar FROM tb_compratemp C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + vOrden + "' and F_FolRemi = '" + vRemi + "'  and F_Estado = '3';");
                                        while (rset.next()) {
                                            banBtn = 1;
                                            String F_FecCad = "", F_Cb = "", F_Marca = "";
                                            try {
                                                F_FecCad = rset.getString(5);
                                            } catch (Exception e) {
                                            }

                                            F_Cb = rset.getString("F_Cb");
                                            if (F_Cb.equals("")) {

                                                ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_lote WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' AND F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro;");
                                                while (rset2.next()) {
                                                    F_Cb = rset2.getString("F_Cb");
                                                    F_Marca = rset2.getString("F_ClaMar");
                                                }
                                            }

                                            if (F_Cb.equals("")) {
                                                ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_cb WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro;");
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
                                %>
                                <tr>
                                    <td><%=rset.getString("C.F_FolRemi")%></td>
                                    <td><%=rset.getString("F_ClaPro")%></td>
                                    <td><%=rset.getString("F_Origen")%></td>
                                    <td><input class="form-control" value="<%=rset.getString(4)%>" name="F_Lote<%=rset.getString("F_IdCom")%>" readonly  /></td>
                                    <td><input class="form-control" value="<%=rset.getString(6)%>" type="number" name="F_Cant<%=rset.getString("F_IdCom")%>" readonly  /></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>
                                    <td><input class="form-control" value="<%=F_Cb%>" name="F_Cb<%=rset.getString("F_IdCom")%>" readonly  /></td>
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
                                        <input value="<%=F_Marca%>" class="form-control" name="F_Marca<%=rset.getString("F_IdCom")%>" id="marca<%=rset.getString("F_IdCom")%>"/>
                                    </td>
                                    <td>
                                        <button class="btn btn-block btn-info" id="btnEdit" type="button" onclick="editar(<%=rset.getString("F_IdCom")%>)" ><span class="glyphicon glyphicon-edit" ></span></button>
                                    </td>
                                </tr>
                                <%
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (SQLException ex) {
                                            Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }

                                %>

                            </table>
                        </div>
                        <hr/>
                    </div>
                    <%                                if (banBtn == 1) {
                    %>

                    <div class="panel-body table-responsive">
                        <div class="row">
                            <input name="vOrden" id="vOrden" type="text" style="" class="hidden" value='<%=vOrden%>' />
                            <input name="vRemi" id="vRemi" type="text" style="" class="hidden" value='<%=vRemi%>' />
                            <div class="col-lg-3 col-lg-offset-3">
                                <button  value="EliminarVerifica" name="accion" class="btn btn-info btn-block" onclick="return confirm('Seguro que desea eliminar la compra?');">Cancelar Remisión</button>
                            </div>
                            <div class="col-lg-3">
                                <button  value="Prueba" name="accion" type="button" id="validarRemisionCross" class="btn btn-primary  btn-block" onclick="return confirm('Seguro que desea realizar la compra?');
                                        return validaCompra();">Confirmar Remisión</button>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>

                </form>
            </div>
        </div>

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
                        </div>
                    </div>
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
                        </div>
                    </div>
                    <br/>
                    <hr/>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" id="btnSave1" >Guardar</button>
                        <button type="button" class="btn btn-info" data-dismiss="modal" id="btnCancel" >Cancelar</button>

                    </div>
                </div>
            </div>
        </div>

        <%@include file="jspf/piePagina.jspf" %>

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


        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/recepcion/recepcionEdit.js"></script>
        <script type="text/javascript">
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
                    Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger("verificarCompraAutoCross.jsp").log(Level.SEVERE, null, ex);
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
                        $("#idCompraTemporal").val(valor);
                        $("#btnModal").click();

                    }, error: function (jqXHR, textStatus, errorThrown) {
                        alert("Error en sistema");
                    }
                });
            }
        </script>
    </body>
</html>
