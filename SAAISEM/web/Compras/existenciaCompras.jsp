<%-- 
    Document   : existenciaCompras
    Created on : 10/05/2019, 09:20:05 PM
    Author     : IngMa
--%>
<%@page import="com.medalfa.saa.utils.StaticText" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../jspf/header.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
        <%@include file="../jspf/librerias_css.jspf" %>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <% if (ingreso.equals("normal")) {%>
            <%@include file="../jspf/menuPrincipal.jspf" %>
            <%} else {%>
            <%@include file="../jspf/menuPrincipalCompra.jspf" %>
            <%}%>
            <h5>
                Consulta Existencias
            </h5>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <div class="row">
                        <h5 class="col-sm-2">Seleccione Proyecto</h5>
                        <div class="col-sm-3">
                            <select id="Nombre" name="Nombre" class="form-control">
                            </select>
                        </div>                       
<!--                        <div class="col-sm-2">
                            <button type="button" class="btn btn-primary btn-sm form-control" id="Mostrar">Mostrar Todos</button>
                        </div>-->
                        <div class="col-sm-2">
                            <button type="button" class="btn btn-info btn-sm form-control" id="Descargar">Descargar</button>
                        </div>
                    </div>
                </div>
                <div class="panel panel-primary table-responsive" id="folDetalle"  >
                    <br/>
                    <div class="panel-body" id="tableTot" >
                        <div id="dynamic"></div>
                    </div>
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
            var buscar_por_proyecto =<%=StaticText.OBTENER_INFORMACION_POR_PROYECTO%>;
        </script>
        <script src="${generalContext}/js/compras/existenciaCompras.js" ></script>
    </body>
</html>
