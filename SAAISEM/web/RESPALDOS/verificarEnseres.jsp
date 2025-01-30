<%-- 
    Document   : verificarEnseres
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
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <title>CEDIS CENTRAL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <form action="verificarEnseres.jsp" method="post">
                <br/>
                <div class="row">
                    <h3>Validación ENSERES</h3>
                    <h4 class="col-sm-2">Elegir oc: </h4>
                    <div class="col-sm-9">
                        <select class="form-control" name="NoCompra" onchange="this.form.submit();">
                            <option value="">-- Proveedor -- Orden de Compra --</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rset = null;

                                    rset = con.consulta("SELECT o.F_Oc, o.F_IdProveedor, CONCAT(o.F_Oc, ' - ', p.F_Proveedor), SUM(o.F_Cant) AS F_Cant, IFNULL(r.F_Cantidad, 0) AS F_Cantidad, ( SUM(o.F_Cant) - IFNULL(r.F_Cantidad, 0)) AS Dif FROM tb_enseresoc o INNER JOIN tb_enseresproveedor p ON o.F_IdProveedor = p.F_Id LEFT JOIN ( SELECT F_Oc, F_IdProvee, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserescompra GROUP BY F_Oc, F_IdProvee ) AS r ON o.F_Oc = r.F_Oc AND o.F_IdProveedor = r.F_IdProvee WHERE o.F_Sts = '1' GROUP BY o.F_Oc HAVING Dif > 0;");

                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>,<%=rset.getString(2)%>"><%=rset.getString(3)%></option>
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
                                ResultSet rset = con.consulta("SELECT i.F_Oc, DATE_FORMAT(i.F_FecEnt, '%d/%m/%Y') AS F_FecSur, p.F_Proveedor, p.F_Id, SUM(i.F_Cant) AS F_Cant, IFNULL(r.F_Cantidad, 0) AS cantingresado, ( SUM(i.F_Cant) - IFNULL(r.F_Cantidad, 0)) AS Dif FROM tb_enseresoc i INNER JOIN tb_enseresproveedor p ON i.F_IdProveedor = p.F_Id LEFT JOIN ( SELECT F_Oc, F_IdProvee, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserescompra WHERE F_Oc = '" + vOrden + "' AND F_IdProvee = '" + vRemi + "' ) AS r ON i.F_Oc = r.F_Oc AND i.F_IdProveedor = r.F_IdProvee WHERE F_Sts = '1' AND i.F_Oc = '" + vOrden + "' AND F_IdProveedor = '" + vRemi + "' GROUP BY F_Oc HAVING Dif > 0;");
                                while (rset.next()) {
                        %>
                        <div class="row">
                            <h4 class="col-sm-2">Folio Orden de Compra:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=vOrden%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <h4 class="col-sm-1">Proveedor:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=rset.getString(3)%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <h4 class="col-sm-2">Fecha Entrega:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=rset.getString(2)%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <!--div class="col-sm-2"><a href="verificaCompragnr.jsp?oc=<%=vOrden%>&remision=<%=vRemi%>" class="btn btn-info form-control">Exportar&nbsp;<span class="glyphicon glyphicon-download"></span></a></div-->
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
                    </form>
                </div>

                <form action="nuevoAutomaticaLotes" method="post">
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td>Enseres</td>
                                    <td>Cantidad OC</td>
                                    <td>Cantidad Recibida</td>
                                    <td>Pendiente</td>
                                    <!--td>Editar</td-->
                                </tr>
                                <%
                                    int banBtn = 0;
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT O.F_Id, O.F_Oc, E.F_Insumos, O.F_Cant, IFNULL(R.F_Cantidad, 0) AS Recibido, O.F_Cant - IFNULL(R.F_Cantidad, 0) AS Cantidad FROM tb_enseresoc O INNER JOIN tb_enseres E ON O.F_IdEnseres = E.F_Id LEFT JOIN ( SELECT F_Oc, F_IdProvee, F_IdEnseres, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserescompra WHERE F_Oc = '" + vOrden + "' AND F_IdProvee = '" + vRemi + "' GROUP BY F_IdEnseres ) AS R ON O.F_Oc = R.F_Oc AND O.F_IdProveedor = R.F_IdProvee AND O.F_IdEnseres = R.F_IdEnseres WHERE O.F_Oc = '" + vOrden + "' AND O.F_IdProveedor = '" + vRemi + "' AND O.F_Sts = 1 HAVING Cantidad > 0;");
                                        while (rset.next()) {
                                            banBtn = 1;
                                %>
                                <tr>
                                    <td><%=rset.getString(3)%></td>
                                    <td><%=rset.getString(4)%></td>
                                    <td><%=rset.getString(5)%></td>
                                    <td>
                                        <input class="form-control" value="<%=rset.getString(6)%>" name="Cantidad_<%=rset.getString(1)%>" id="Cantidad_<%=rset.getString(1)%>"/>
                                    </td>
                                    <!--td>
                                        <button class="btn btn-block btn-info" id="btnEdit" type="button" onclick="editar(<%=rset.getString(1)%>)" ><span class="glyphicon glyphicon-edit" ></span></button>
                                    </td-->
                                </tr>
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

                    <div class="panel-body table-responsive">
                        <div class="row">
                            <input name="vOrden" id="vOrden" type="text" style="" class="hidden" value='<%=vOrden%>' />
                            <input name="vRemi" id="vRemi" type="text" style="" class="hidden" value='<%=vRemi%>' />
                            <button  value="Prueba" name="accion" type="button" id="validarRemision" class="btn btn-primary  btn-block">Confirmar Remisión</button>
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
                            <label  for="CantidadNuevo" >Costo U</label>
                            <input class="form-control" id="costo" type="text" readonly="">
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
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/recepcion/recepcionEditEnseres.js"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
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
                    Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger("verifarCompraAuto.jsp").log(Level.SEVERE, null, ex);
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
                        $("#costo").val(data.costo);
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
