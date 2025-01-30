<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
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
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        //response.sendRedirect("index.jsp");
    }
    ConectionDB_Nube con = new ConectionDB_Nube();
    String Fecha = "";
    String fechaCap = "";
    String Proveedor = "";
    try {
        fechaCap = request.getParameter("Fecha");
        Fecha = request.getParameter("Fecha");
    } catch (Exception e) {

    }
    if (fechaCap == null) {
        fechaCap = "";
    }
    try {
        Proveedor = request.getParameter("Proveedor");
    } catch (Exception e) {

    }
    if (Proveedor == null) {
        Proveedor = "";
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
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <!--div class="navbar navbar-default">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="main_menu.jsp">Inicio</a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Entradas<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="captura.jsp">Entrada Manual</a></li>
                                    <li><a href="compraAuto2.jsp">Entrada Automática OC</a></li>
                                    <li><a href="reimpresion.jsp">Reimpresión de Compras</a></li>
                                    <li><a href="ordenesCompra.jsp">Órdenes de Compras</a></li>
                                    <li><a href="kardexClave.jsp">Kardex Claves</a></li>
                                    <li><a href="Ubicaciones/Consultas.jsp">Ubicaciones</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Facturación<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="requerimiento.jsp">Carga de Requerimiento</a></li>
                                    <li><a href="factura.jsp">Facturación Automática</a></li>
                                     <li><a href="reimp_factura.jsp">Administrar Remisiones</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Catálogos<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="medicamento.jsp">Catálogo de Medicamento</a></li>
                                    <li><a href="catalogo.jsp">Catálogo de Proveedores</a></li>
                                    <li><a href="marcas.jsp">Catálogo de Marcas</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Fecha Recibo<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="Entrega.jsp">Fecha de Recibo en CEDIS</a></li>    
                                    <li><a href="historialOC.jsp">Historial OC</a></li>                                     
                                </ul>
                            </li>
                            
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href=""><span class="glyphicon glyphicon-user"></span> <%=usua%></a></li>
                            <li class="active"><a href="index.jsp"><span class="glyphicon glyphicon-log-out"></span></a></li>
                        </ul>
                    </div><!--/.nav-collapse>
                </div>
            </div-->
            <hr/>

            <div>
                <h3>FECHA DE RECIBO POR PROVEEDOR</h3>
                <div class="row">
                    <form action="Entrega.jsp" method="post">
                        <h4 class="col-sm-2">Proveedor</h4>
                        <div class="col-sm-5">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="this.form.submit();">
                                <option value="">--Proveedor--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("select F_Provee from TB_FecEnt GROUP BY F_Provee ORDER BY F_Provee ASC");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>"><%=rset.getString(1)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>

                            </select>
                        </div>
                        <h4 class="col-sm-2">Fecha de Recibo</h4>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha"  onchange="this.form.submit();" />
                        </div>
                        <a class="btn btn-primary" href="Entrega.jsp">Todo</a>
                    </form>
                </div>
            </div>
        </div>
        <br />
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>                                
                                <td class="text-center">Proveedor</td>
                                <td class="text-center">Fecha Entrega1</td>
                                <td class="text-center">Hora Entrega1</td> 
                                <td class="text-center">Fecha Entrega2</td>
                                <td class="text-center">Hora Entrega2</td> 
                                <td class="text-center">Bodega Recibo CEDIS MEDALFA</td> 
                                <td class="text-center">Observación</td> 
                                <td class="text-center"></td> 
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                    if ((Proveedor.equals("")) && (fechaCap.equals(""))) {

                                        rset = con.consulta("select F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs,F_Id from TB_FecEnt");
                                    } else if (!(Proveedor.equals("")) && (fechaCap.equals(""))) {

                                        rset = con.consulta("select F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs,F_Id from TB_FecEnt WHERE F_Provee = '" + Proveedor + "'");
                                    } else {

                                        rset = con.consulta("select F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs,F_Id from TB_FecEnt WHERE F_F1 like '%" + fechaCap + "%' or F_F2 like '%" + fechaCap + "%' ");
                                    }
                                    while (rset.next()) {

                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td class="text-center"><%=rset.getString(2)%></td>
                                <td class="text-center"><%=rset.getString(3)%></td>
                                <td class="text-center"><%=rset.getString(4)%></td>
                                <td class="text-center"><%=rset.getString(5)%></td>
                                <td class="text-center"><%=rset.getString(6)%></td>
                                <td><textarea class="form-control" rows="5" cols="60" readonly=""><%=rset.getString(7)%></textarea></td>
                                <td><a href="#" class="btn btn-info btn-block" data-toggle="modal" data-target="#recalendarizar<%=rset.getString("F_Id")%>"><span class="glyphicon glyphicon-calendar"></span></a></td>
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
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>
        <!--
        Modal
        -->
        <%
            try {
                con.conectar();
                ResultSet rset = null;
                if ((Proveedor.equals("")) && (fechaCap.equals(""))) {

                    rset = con.consulta("select F_Id, F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs from TB_FecEnt");
                } else if (!(Proveedor.equals("")) && (fechaCap.equals(""))) {

                    rset = con.consulta("select F_Id, F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs from TB_FecEnt WHERE F_Provee = '" + Proveedor + "'");
                } else {

                    rset = con.consulta("select F_Id, F_Provee,F_F1,F_H1,F_F2,F_H2,F_Bodega,F_obs from TB_FecEnt WHERE F_F1 like '%" + fechaCap + "%' or F_F2 like '%" + fechaCap + "%' ");
                }
                while (rset.next()) {

        %>
        <div class="modal fade" id="recalendarizar<%=rset.getString(1)%>" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="Rechazos" method="get">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-7">
                                    <h4 class="modal-title" id="myModalLabel">Recalendarizar Orden de Compra</h4>
                                </div>
                                <div class="">
                                    <input name="NoRecalendarizar" id="NoRecalendarizar" value="<%=rset.getString(1)%>" class="hidden" readonly="" />
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h4>Fecha Entrega 1</h4>
                                </div>
                                <div class="col-sm-6">
                                    <h4>Nueva Fecha 1</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="<%=rset.getString(3)%>" readonly="" name="FechaA1_<%=rset.getString(1)%>" />
                                </div>
                                <div class="col-sm-6">
                                    <input type="date" class="form-control" id="FechaOrden1_<%=rset.getString(1)%>" name="FechaOrden1_<%=rset.getString(1)%>" value="" />
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="<%=rset.getString(4)%>" readonly="" name="HoraA1_<%=rset.getString(1)%>"/>
                                </div>
                                <div class="col-sm-6">
                                    <select class="form-control" id="HoraOrden1_<%=rset.getString(1)%>" name="HoraOrden1_<%=rset.getString(1)%>">
                                        <%
                                            for (int i = 0; i < 24; i++) {
                                                if (i != 24) {
                                        %>
                                        <option value="<%=i%>:00"><%=i%>:00</option>
                                        <option value="<%=i%>:30"><%=i%>:30</option>
                                        <%
                                        } else {
                                        %>
                                        <option value="<%=i%>:00"><%=i%>:00</option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <hr/>
                            <div class="row">
                                <div class="col-sm-6">
                                    <h4>Fecha Entrega 2</h4>
                                </div>
                                <div class="col-sm-6">
                                    <h4>Nueva Fecha 2</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="<%=rset.getString(5)%>" readonly="" name="FechaA2_<%=rset.getString(1)%>"/>
                                </div>
                                <div class="col-sm-6">
                                    <input type="date" class="form-control" id="FechaOrden2_<%=rset.getString(1)%>" name="FechaOrden2_<%=rset.getString(1)%>" value="" />
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="<%=rset.getString(6)%>" readonly="" name="HoraA2_<%=rset.getString(1)%>"/>
                                </div>
                                <div class="col-sm-6">
                                    <select class="form-control" id="HoraOrden2_<%=rset.getString(1)%>" name="HoraOrden2_<%=rset.getString(1)%>">
                                        <%
                                            for (int i = 0; i < 24; i++) {
                                                if (i != 24) {
                                        %>
                                        <option value="<%=i%>:00"><%=i%>:00</option>
                                        <option value="<%=i%>:30"><%=i%>:30</option>
                                        <%
                                        } else {
                                        %>
                                        <option value="<%=i%>:00"><%=i%>:00</option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <hr/>
                            
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Bodega</h4>
                                </div>
                                <div class="col-sm-12">
                                    <select name="Bode_<%=rset.getString(1)%>" id="Bode_<%=rset.getString(1)%>" class="form-control">
                                        <option value="">--Seleccione--</option>
                                        <option <% if (rset.getString("F_Bodega").equals("Industria Automotriz No. 18 interior 3-C")){out.println("Selected");} %>>Industria Automotriz No. 18 interior 3-C</option>
                                        <option <% if (rset.getString("F_Bodega").equals("Avenida Emiliano Zapata Lote 2")){out.println("Selected");} %>>Avenida Emiliano Zapata Lote 2</option>
                                    </select>
                                </div>
                            </div>
                                
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Observaciones de Recalendarización</h4>
                                </div>
                                <div class="col-sm-12">
                                    <textarea class="form-control" placeholder="Observaciones" name="recaObser_<%=rset.getString(1)%>" id="recaObser_<%=rset.getString(1)%>" rows="5"></textarea>
                                </div>
                            </div>
                            <hr/>
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Correo del proveedor</h4>
                                    <input type="email" class="form-control" id="correoProvee_<%=rset.getString(1)%>" name="correoProvee_<%=rset.getString(1)%>" />
                                </div>
                            </div>
                            <div class="text-center" id="imagenCarga" style="display: none;" > 
                                <img src="imagenes/ajax-loader-1.gif">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" onclick="return validaRecalendariza(<%=rset.getString(1)%>);
                                    " name="accion" value="Recalendarizar">RECALENDARIZAR OC</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        %>
        <!--
        /Modal
        -->
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
                                function validaRecalendariza(id) {
                                    var fecha1 = document.getElementById('FechaOrden1_' + id).value;
                                    var hora1 = document.getElementById('HoraOrden1_' + id).value;
                                    var fecha2 = document.getElementById('FechaOrden2_' + id).value;
                                    var hora2 = document.getElementById('HoraOrden2_' + id).value;
                                    var observaciones = document.getElementById('recaObser_' + id).value;
                                    var correo = document.getElementById('correoProvee_' + id).value;
                                    var bodega = document.getElementById('Bode_' + id).value;

                                    if (fecha1 === "" && hora1 !== "0:00") {
                                        alert("Capture la Fecha 1");
                                        return false;
                                    }
                                    if (fecha1 !== "" && hora1 === "0:00") {
                                        alert("Capture la Hora 1");
                                        return false;
                                    }


                                    if (fecha2 === "" && hora2 !== "0:00") {
                                        alert("Capture la Fecha 2");
                                        return false;
                                    }
                                    if (fecha2 !== "" && hora2 === "0:00") {
                                        alert("Capture la Hora 2");
                                        return false;
                                    }


                                    if (fecha1 === "" && hora1 === "0:00" && fecha2 === "" && hora2 === "0:00") {
                                        alert("Seleccione las fechas a recalendarizar");
                                        return false;
                                    }

                                    if (observaciones === "") {
                                        alert("Ingrese las Observaciones");
                                        return false;
                                    }
                                    
                                    if (correo === "") {
                                        alert("Ingrese correo");
                                        return false;
                                    }
                                    
                                    if (bodega === "") {
                                        alert("Seleccione una Bodega");
                                        return false;
                                    }
                                    document.getElementById('imagenCarga').style.display = 'block';

                                }
                                $(document).ready(function() {
                                    $('#datosCompras').dataTable();
                                });

                                $(function() {
                                    $("#Fecha").datepicker();
                                    $("#Fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
                                });
</script>