<%-- 
    Document   : kardexPorDia
    Created on : 8/08/2019, 05:11:25 PM
    Author     : IngMa
--%>
<%@page import="com.medalfa.saa.utils.StaticText" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../jspf/header.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <%@include file="../jspf/librerias_css.jspf" %>

    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <% if (ingreso.equals("normal")) {%>
            <%@include file="../jspf/menuPrincipal.jspf" %>
            <%} else {
                %>
            <%@include file="../jspf/menuPrincipalCompra.jspf" %>
            <%}%>
        </div>

        <div style="width: 90%; margin: auto;">

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="text-center">
                        Kardex de Insumo por día
                    </h3>
                </div>

                <div class="panel-body" >
                    <div class="row">
                        <div class="col-lg-2 col-md-2 col-sm-2" >

                        </div>

                        <label class="col-lg-1 col-md-2 col-sm-2" style="margin-top: 5px" for="fechaBusquedaInput">Fecha:</label>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input type="text" class="form-control" id="fechaBusquedaInput" readonly >
                        </div>
                        <div class="col-lg-1 col-md-1 col-sm-1" >
                            <button type="button" class="btn btn-block btn-primary" id="searchByDay" >
                                <span class="glyphicon glyphicon-search"></span>
                            </button> 
                        </div>
                        <div class="col-lg-1 col-md-1 col-sm-1" >
                            <button type="button" class="btn btn-block btn-primary" id="downloadKardexByFecha" ><span class="glyphicon glyphicon-save"></span></button>
                        </div>
                    </div>
                </div>
            </div>
            <br/>

            <div class="panel panel-info" style="overflow:scroll; width:100%;" >
                <h3>Ingresos / Egresos</h3>
                <div class="panel-body table-responsive" >
                    <div id="dynamic"></div>
                </div>
            </div>
            <div class="panel panel-info" style="overflow:scroll; width:100%;" >
                <h3>Redistribución en Almacén</h3>
                <div class="panel-body table-responsive" >
                    <div id="dynamicRedistribucion"></div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"></h4>
                    </div>
                    <div class="modal-body">
                        <div class="text-center" id="imagenCarga">
                            <img src="${generalContext}/imagenes/ajax-loader-1.gif" alt="" />
                        </div>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
        <%@include file="../jspf/piePagina.jspf" %>
        <%@include file="../jspf/librerias_js.jspf" %>
        <script>
            $("#fechaBusquedaInput").datepicker({dateFormat: 'yy-mm-dd'});
            var porFecha = <%=StaticText.KARDEX_POR_FECHA%>;
        </script>
        <script src="${generalContext}/js/kardex/kardexByFecha.js"></script>
    </body>
</html>
