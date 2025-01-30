<%-- 
    Document   : verFoliosIsem2017.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ver OC</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <link href="css/select2.css" rel="stylesheet">
    </head>
    <body onload="focusLocus();">
        <div class="container">
            <div class="row">
                <h1>SIALSS_CTRL</h1>
                <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
                <%@include file="jspf/menuPrincipalCompra.jspf" %>
                <h4>Ver OC Enseres</h4>
            </div>
            <br/>
            <div class="row">
                <form method="post" action="verOcEnseres.jsp">
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
                                        ResultSet rset = con.consulta("SELECT O.F_IdProveedor, P.F_Proveedor FROM tb_enseresoc O INNER JOIN tb_enseresproveedor P ON O.F_IdProveedor = P.F_Id GROUP BY O.F_IdProveedor ORDER BY P.F_Proveedor;");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
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
        <div class="row" style="width: 90%; margin: auto;">
            <div class="col-sm-6">
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
                                <td>No. Orden</td>
                                <td>Capturó</td>
                                <td>Proveedor</td>
                                <td>Fecha Entrega</td>
                                <td>Ver</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                String fecha = "", Usuario = "", Proveedor = "";
                                Proveedor = request.getParameter("Proveedor");
                                try {
                                    con.conectar();
                                    ResultSet rset = null;

                                    if (!(Proveedor.equals(""))) {
                                        rset = con.consulta("SELECT o.F_Oc, u.F_Usu, p.F_Proveedor, F_FecEnt FROM tb_enseresoc o INNER JOIN tb_enseresproveedor p ON o.F_IdProveedor = p.F_Id INNER JOIN tb_usuariocompra u ON u.F_IdUsu = o.F_Usuario WHERE o.F_IdProveedor = '" + request.getParameter("Proveedor") + "' GROUP BY o.F_Oc;");

                                    }

                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td>
                                    <button class="btn btn-primary text-center" name="NoCompra" value="<%=rset.getString(1)%>"><span class="glyphicon glyphicon-search"></span></button>
                                </td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="col-sm-6">
                <div class="panel panel-info">
                    <div class="panel-heading">
                    </div>
                    <div class="panel-body">
                        <%                try {
                                con.conectar();
                                ResultSet rset = con.consulta("SELECT o.F_Oc, u.F_Usu, p.F_Proveedor, DATE_FORMAT(o.F_FecEnt, '%d/%m/%Y'), e.F_Insumos, o.F_Cant, o.F_Sts, DATE_FORMAT( DATE(o.F_Captura), '%d/%m/%Y' ), o.F_Recibido FROM tb_enseresoc o INNER JOIN tb_enseresproveedor p ON o.F_IdProveedor = p.F_Id INNER JOIN tb_usuariocompra u ON u.F_IdUsu = o.F_Usuario INNER JOIN tb_enseres e ON o.F_IdEnseres = e.F_Id WHERE o.F_Oc = '" + NoCompra + "' GROUP BY o.F_Oc;");
                                while (rset.next()) {
                        %>
                        <div class="panel-heading">
                            Orden: <%=NoCompra%>
                        </div>

                        <div class="col-sm-1">
                            <a class="btn btn-default" target="_blank" href="imprimeOrdenCompraEnseres.jsp?ordenCompra=<%=NoCompra%>"><span class="glyphicon glyphicon-print"></span></a>
                        </div>
                        <div class="panel-body">
                            <form name="FormBusca" action="CapturaPedidos" method="post">
                                <div class="row">
                                    <h4 class="col-sm-2">Orden No. </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(1)%>" name="NoCompra" id="NoCompra" readonly="" />
                                    </div>
                                    <h4 class="col-sm-3">Capturó: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString("F_Usu")%>" readonly="" />
                                    </div>

                                </div>
                                <%
                                    if (rset.getString(7).equals("2")) {
                                %>
                                <h4 class="text-success">FOLIO CANCELADO</h4>
                                <%
                                    }
                                %>
                                <div class="row">
                                    <h4 class="col-sm-3">Proveedor: </h4>
                                    <div class="col-sm-9">
                                        <input class="form-control" value="<%=rset.getString(3)%>" readonly="" />
                                    </div>
                                </div>
                                <div class="row">
                                    <h4 class="col-sm-3">Fecha de Captura: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(8)%>" readonly="" />
                                    </div>
                                    <h4 class="col-sm-3">Fecha de Entrega: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(4)%>" readonly="" />
                                    </div>
                                </div>
                                <br/>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                        }
                                    }
                                %>
                            </form>
                            <div class="row">
                                <br/>
                                <table class="table table-bordered table-condensed table-striped">
                                    <tr>
                                        <td><strong>Enseres</strong></td>
                                        <td><strong>Cantidad</strong></td>
                                        <td><strong>Recibido</strong></td>
                                        <td><strong>Pendiente</strong></td>
                                        <!--td><strong>Cant. Modificar</strong></td>
                                        <td><strong>Modificar</strong></td>
                                        <td><strong>Quitar</strong></td-->
                                    </tr>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = con.consulta("SELECT e.F_Insumos, o.F_Cant, IFNULL(c.F_Cantidad, 0), o.F_Cant - IFNULL(c.F_Cantidad, 0), o.F_Id, o.F_Sts FROM tb_enseresoc o INNER JOIN tb_enseres e ON o.F_IdEnseres = e.F_Id LEFT JOIN ( SELECT F_IdEnseres, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserescompra WHERE F_Oc = '" + NoCompra + "' GROUP BY F_IdEnseres ) c ON o.F_IdEnseres = c.F_IdEnseres WHERE o.F_Oc = '" + NoCompra + "';");
                                            while (rset.next()) {
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=formatter.format(rset.getInt(4))%></td>
                                    </tr>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                        } finally {
                                            try {
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                Logger.getLogger("verOcEnseres.jsp").log(Level.SEVERE, null, e);
                                            }
                                        }
                                    %>

                                </table>
                            </div>
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
                                <h4 class="col-sm-12">Cambiar Fecha Oc <%=NoCompra%></h4>
                                <input type="hidden" class="form-control" name="NoCompra" id="NoCompra" value="<%=NoCompra%>" />

                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar fecha:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="date" class="form-control" required name="F_FecEnt" id="F_FecEnt" />
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
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                <button class="btn btn-primary" name="accion" value="recalendarizar" onclick="return confirm('¿Seguro que desea cambiar la fecha y hora?')">Confirmar</button>
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
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script src="js/jquery.alphanum.js" type="text/javascript"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script src="js/select2.js"></script>
    <script>
                                    $(document).ready(function () {
                                        $('#datosCompras').dataTable();
                                        $('#Proveedor').select2();
                                        $('#Usuario').select2();
                                    });

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
                                        if (ClaPro === "" |DesPro === "" || CanPro === "") {
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

                                    function aplicarModificacion(valor) {

                                        var detalleModificar = $(valor).attr('value');
                                        var CantidadM = $('#cantidadM_' + detalleModificar).val();
                                        var Recibido = $('#Recibido_' + detalleModificar).val();
                                        var CantSol = $('#CantSol_' + detalleModificar).val();
                                        if (CantidadM != "") {
                                            if (CantidadM > 0) {
                                                if (parseInt(CantidadM) > parseInt(Recibido)) {
                                                    if (detalleModificar !== "") {

                                                        var r = confirm("¿Desea realizar la modificación?");
                                                        if (r) {
                                                            var $form = $(this);
                                                            $.ajax({
                                                                type: "POST",
                                                                url: "CapturaPedidos?accion=Modificar&CantidadM=" + CantidadM + "&CantSol=" + CantSol + "&detalleModificar=" + detalleModificar,
                                                                dataType: "json",
                                                                success: function (data) {
                                                                    console.log(data.msg);
                                                                    if (data.msg === "ok") {
                                                                        alert('Cantidad Modificada');
                                                                        location.reload();
                                                                    } else {
                                                                        alert('Ocurrio un irreor intente de nuevo');
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                } else {
                                                    alert("Favor de agregar cantidad Mayor a Recibido");
                                                }
                                            } else {
                                                alert("Favor de agregar cantidad Mayor a Cero");
                                            }
                                        } else {
                                            alert("Favor de agregar cantidad");
                                        }
                                    }

                                    function aplicarEliminar(valor) {

                                        var detalleEliminar = $(valor).attr('value');


                                        if (detalleEliminar !== "") {

                                            var r = confirm("¿Desea realizar la Eliminación?");
                                            if (r) {
                                                var $form = $(this);
                                                $.ajax({
                                                    type: "POST",
                                                    url: "CapturaPedidos?accion=Eliminar&detalleModificar=" + detalleEliminar,
                                                    dataType: "json",
                                                    success: function (data) {
                                                        console.log(data.msg);
                                                        if (data.msg === "ok") {
                                                            alert('Registro Eliminado');
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
