<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%

    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipalCompra.jspf" %>

            <h4><strong> Reporteador Estadística de Entrega </strong></h4>
            <br>
            <form id="formParametros" action="../entregas/v2" method="post">
                <div class="row">

                    <div class=" col-sm-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Cifras</i></strong></h3>
                            </div>
                            <div class="panel-body">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="unisur" id="unisur" value="unisur" /> SURTIDO
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="unireq" id="unireq" value="unireq" /> REQUERIDO
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="costo" id="costo" value="costo" /> COSTO
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class=" col-sm-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Clasificación Unidad de Atención</i></strong></h3>
                            </div>
                            <div class="panel-body">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="juris" id="juris" value="juris" /> JURISDICIÓN/HOSP
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="nivel" id="nivel" value="nivel" /> NIVEL 
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="municipio" id="municipio" value="municipio" /> MUNICIPIO
                                </label>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row">

                    <div class=" col-sm-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Categoría</i></strong></h3>
                            </div>
                            <div class="panel-body">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="unidad" id="unidad" value="unidad"> UNIDAD
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="producto" id="producto" value="producto"> INSUMO
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class=" col-sm-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Facturas</i></strong></h3>
                            </div>
                            <div class="panel-body">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="doct" id="doct" value="doct" /> DOCT
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="lote" id="lote" value="lote" /> LOTE
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class=" col-sm-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Clasificación medicamentos</i></strong></h3>
                            </div>
                            <div class="panel-body">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="origen" id="origen" value="origen" /> ORIGEN
                                </label>
                                <!--label class="checkbox-inline">
                                    <input type="checkbox" name="tipofact" id="tipofact" value="tipofact" /> TIPO REM 
                                </label-->
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="proyecto" id="proyecto" value="proyecto" /> PROYECTO
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="cause" id="cause" value="cause" /> TIPO CAUSES
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class=" col-sm-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><strong><i>Búsqueda</i></strong></h3>
                            </div>
                            <div class="panel-body">

                                <div class="row">
                                    <label class="control-label col-sm-2 col-sm-offset-1" for="fecha_ini">Fechas</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" />
                                    </div>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" />
                                    </div>
                                </div>  
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1" for="fecha_ini">Proyecto</label>
                                    <div class="col-sm-3">
                                        <input type="text" name="ListProyect" class="form-control" id="ListProyect" readonly="true" />
                                    </div>
                                    <select name="SelectProyect" id="SelectProyect" class="col-sm-5">
                                        <option>--Seleccione Proyecto--</option>
                                        <%                                            try {
                                                con.conectar();
                                                ResultSet Proyect = con.consulta("SELECT P.F_Id,P.F_DesProy FROM tb_uniatn U INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id GROUP BY P.F_Id;");
                                                while (Proyect.next()) {
                                        %>
                                        <option value="<%=Proyect.getString(1)%>"><%=Proyect.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                            }
                                        %>
                                    </select>
                                </div>  
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1">Jurisdicción</label>
                                    <div class="col-sm-3">
                                        <input type="text" name="ListJuris" class="form-control" id="ListJuris" readonly="true" />
                                    </div>
                                    <select name="SelectJuris" id="SelectJuris" class="col-sm-5">
                                    </select>
                                </div>
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1">Municipio</label>
                                    <div class="col-sm-3">
                                        <input type="text" name="ListMuni" class="form-control" id="ListMuni" readonly="true" />
                                    </div>
                                    <select name="SelectMuni" id="SelectMuni" class="col-sm-5">
                                    </select>
                                </div>
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1">Unidad</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" name="ListUni" id="ListUni" readonly="true" />
                                    </div>
                                    <select name="SelectUnidad" id="SelectUnidad" class="col-sm-5">
                                    </select>
                                </div>
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1">Insumo</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" name="ListClave" id="ListClave" readonly="true" />
                                    </div>
                                    <select name="SelectClave" id="SelectClave" class="col-sm-5">
                                        <option>--Seleccione--</option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet Clave = con.consulta("SELECT F_ClaPro,CONCAT(F_ClaPro,' [',F_DesPro,']') AS F_DesPro FROM tb_medica");
                                                while (Clave.next()) {
                                        %>
                                        <option value="<%=Clave.getString(1)%>"><%=Clave.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="row">
                                    <br>
                                    <label class="col-sm-2 col-sm-offset-1">Tipo Unidad</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" name="LisTipUni" id="LisTipUni" readonly="true" />
                                    </div>
                                    <select name="SelectTipoUnidad" id="SelectTipoUnidad" class="col-sm-5">
                                    </select>
                                </div>
                                <div class="row">
                                    <br>
                                    <div class="col-sm-4 col-sm-offset-2">
                                        <input type="hidden" name="Reporte" id="Reporte" class="form-control" value="2" />
                                        <button class="btn btn-block btn-primary" id="accion" name="accion" value="Generar" >Generar <i class="glyphicon glyphicon-search"></i></button>
                                    </div>
                                    <div class="col-sm-4">
                                        <a class="btn btn-block btn-info" href="ReporteadorCompras.jsp">Limpiar&nbsp;<i class="glyphicon glyphicon-trash"></i></a>                        
                                    </div>    
                                </div>
                            </div>
                        </div>
                    </div>
                </div> 
            </form>
        </div>


        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/select2.js" type="text/javascript"></script>
        <script src="../js/reporteador/Reporteador.js" type="text/javascript"></script>

        <script>
            $(document).ready(function () {

                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                $("#SelectJuris").select2();
                $("#SelectMuni").select2();
                $("#SelectUnidad").select2();
                $("#SelectClave").select2();
                $("#SelectTipoUnidad").select2();
                $("#SelectProyect").select2();


                $('#SelectProyect').change(function () {
                    $('#proyecto').prop('checked', true);
                    var Lista = $('#ListProyect').val();
                    var valor = $('#SelectProyect').val();
                    if (Lista != "") {
                        $('#ListProyect').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#ListProyect').val("'" + valor + "'");
                    }
                });
                $('#SelectJuris').change(function () {
                    $('#juris').prop('checked', true);
                    var Lista = $('#ListJuris').val();
                    var valor = $('#SelectJuris').val();
                    if (Lista != "") {
                        $('#ListJuris').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#ListJuris').val("'" + valor + "'");
                    }
                });

                $('#SelectMuni').change(function () {
                    $('#municipio').prop('checked', true);
                    var Lista = $('#ListMuni').val();
                    var valor = $('#SelectMuni').val();
                    if (Lista != "") {
                        $('#ListMuni').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#ListMuni').val("'" + valor + "'");
                    }
                });

                $('#SelectUnidad').change(function () {
                    $('#unidad').prop('checked', true);
                    var Lista = $('#ListUni').val();
                    var valor = $('#SelectUnidad').val();
                    if (Lista != "") {
                        $('#ListUni').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#ListUni').val("'" + valor + "'");
                    }
                });

                $('#SelectClave').change(function () {
                    $('#producto').prop('checked', true);
                    var Lista = $('#ListClave').val();
                    var valor = $('#SelectClave').val();
                    if (Lista != "") {
                        $('#ListClave').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#ListClave').val("'" + valor + "'");
                    }
                });

                $('#SelectTipoUnidad').change(function () {
                    var Lista = $('#LisTipUni').val();
                    var valor = $('#SelectTipoUnidad').val();
                    if (Lista != "") {
                        $('#LisTipUni').val(Lista + ",'" + valor + "'");
                    } else {
                        $('#LisTipUni').val("'" + valor + "'");
                    }
                });

                $('#formParametros').submit(function () {
                    $('#accion').prop('disabled', true);
                    $('#accion').text("...Generando...");
                });

            });
        </script>
    </body>
</html>

