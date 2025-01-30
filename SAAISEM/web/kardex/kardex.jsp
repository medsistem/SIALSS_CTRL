<%-- 
    Document   : kardex
    Created on : 10/04/2019, 09:36:54 PM
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
            <%} else {%>
            <%@include file="../jspf/menuPrincipalCompra.jspf" %>
            <%}%>
        </div>
        <div style="width: 90%; margin: auto;">
            <h3>
                Kardex de Insumo
            </h3>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    Criterios de Búsqueda
                </div>
                <div class="panel-body" >

                    <div class="row">
                        <!-- <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="cb">Cb:</label>
                             <div class="col-lg-2 col-md-2 col-sm-2" >
                             <input type="text" class="form-control" id="cbBusqueda" >
                        </div>-->
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="clave">Clave:</label>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input type="text" class="form-control" id="claveBusqueda" >
                        </div>
                        <label class="col-lg-1 col-md-2 col-sm-2" style="margin-top: 5px" for="descripcion">Descripción:</label>
                        <div class="col-lg-7 col-md-6 col-sm-6" >
                            <input type="text" class="form-control" id="descripcionBusqueda" >
                        </div>
                        <div class="col-lg-1 col-md-1 col-sm-1" >
                            <button type="button" class="btn btn-block btn-primary" id="searchByClaveKardex" >
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </div>
                    </div>
                    <br/>
                    <div class="row" >
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="fechaInicial">Fecha Inicial:</label>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input type="text" class="form-control" id="fechaInicial" >
                        </div>
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="fechaFinal">Fecha Final:</label>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input type="text" class="form-control" id="fechaFinal" >
                        </div>
                    </div>
                    <br/>
                    <div class="row" >
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="loteKardex">Lote:</label> 
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <select class="form-control" id="loteKardex" onchange="cambiaLoteCadu(this);" ></select>
                        </div>
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="caducidadKardex">Caducidad:</label> 
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <select class="form-control" id="caducidadKardex" ></select>
                        </div>
                        <label class="col-lg-1 col-md-1 col-sm-1" style="margin-top: 5px" for="origenKardex">Origen:</label> 
                        <div class="col-lg-3 col-md-3 col-sm-3" >
                            <input class="form-control" type="text" id="origenKardex" readonly />
                            <input class="form-control" type="hidden" id="origenKardexId" />                            
                        </div>
                        <div class="col-lg-1 col-md-1 col-sm-1" >
                            <button type="button" class="btn btn-block btn-primary" id="searchByLoteKardex" ><span class="glyphicon glyphicon-search"></span></button>
                        </div>
                        <div class="col-lg-1 col-md-1 col-sm-1" >
                            <button type="button" class="btn btn-block btn-primary" id="downloadKardex" ><span class="glyphicon glyphicon-save"></span></button>
                        </div>
                        <% if (tipo.equals("1")) {
                        %>
                         <div class="hidden" >
                            <button type="hidden" class="btn btn-block btn-primary" id="downloadKardex_1" ><span class="glyphicon glyphicon-save"></span></button>
                        </div>
                        <% } %> 
                    </div>
                    <hr/>
                    <h3>Clave: <span id="claveResult" ></span></h3>
                    <h4>Lote: <span id="loteResult" ></span></h4>
                    <h4>Caducidad: <span id="caducidadResult" ></span></h4> 
                    <h4><span id="descripcionResult" ></span></h4>
                    <br/>
                    <h4>Existencia Actual: <span id="existenciaResult" ></span></h4>
                    <br/>

                    <div class="panel panel-info" style="overflow:scroll; width:100%;" >
                        <h3>Ingresos / Egresos</h3>
                        <div class="panel-body table-responsive" >
                            <div id="dynamic"></div>
                        </div>
                    </div>
                      <% if (ingreso.equals("normal")) {%>
                    
                    <div class="panel panel-info"  style="overflow:scroll; width:100%;">
                        <h3>Redistribución en Almacén</h3>
                        <div class="panel-body table-responsive" >
                            <div id="dynamicRedistribucion"></div>
                        </div>
                    </div>
                    <% } %> 

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
          <% if (ingreso.equals("normal")) {%>   
         <script src="${generalContext}/js/kardex/kardexReload.js" ></script>
         <% } else if (ingreso.equals("compras")) { %>
          <script src="${generalContext}/js/kardex/kardexReload.js" ></script>
        <% } %>
        <script>

            var porClave = <%=StaticText.KARDEX_POR_CLAVE%>;
            var porLote = <%=StaticText.KARDEX_POR_LOTE%>;
            var infoPorLote = <%=StaticText.OBTENER_INFORMACION_LOTE_CADUCIAD_ORIGEN%>;

            var claves_productos = [];
            var descripcion_productos = [];

            $("#fechaInicial").datepicker(
                    {
                        dateFormat: 'yy-mm-dd',
                        changeYear: true,
                        changeMonth: true,
                        onClose: function (selectedDate) {
                            $("#fechaFinal").datepicker("option", "minDate", selectedDate);
                        }
                    });

            $("#fechaFinal").datepicker({
                dateFormat: 'yy-mm-dd',
                changeYear: true,
                changeMonth: true,
                onClose: function (selectedDate) {
                    $("#fechaInicial").datepicker("option", "maxDate", selectedDate);
                }

            });




            $("#searchByClaveKardex").click(function ()
            {
                var clave = $("#claveBusqueda").val();
                var descripcion = $("#descripcionBusqueda").val();
                var fechaInicial = $("#fechaInicial").val();
                var fechaFinal = $("#fechaFinal").val();

                if (clave !== '')
                {
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_CLAVE%>", clave, fechaInicial, fechaFinal);
                } else if (descripcion !== '')
                {
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_DESCRIPCION%>", descripcion, fechaInicial, fechaFinal);
                }                
                else
                {
                    swal("Atención", "Favor de verificar criterios de búsqueda", "warning");
                }


            });

            $("#searchByLoteKardex").click(function ()
            {
                var clave = $("#claveResult").text();
                var lote = $("#loteKardex option:selected").text();
                var caducidad = $("#caducidadKardex option:selected").text();
                var origen = $("#origenKardexId").val();
                var fechaInicial = $("#fechaInicial").val();
                var fechaFinal = $("#fechaFinal").val();


                if (clave === "" || lote === "")
                {
                    swal("Atención", "Favor de verificar criterios de búsqueda", "warning");

                } else
                {
                    showInformationByLote(clave, lote, caducidad, origen, fechaInicial, fechaFinal);


                }


            });

            $("#downloadKardex").click(function ()
            {
                var clave = $("#claveResult").text();
                var lote = $("#loteKardex option:selected").text();
                var caducidad = $("#caducidadKardex option:selected").text();
                var origen = $("#origenKardexId").val();
                var accion = "";                
                var fechaInicial = $("#fechaInicial").val();
                var fechaFinal = $("#fechaFinal").val();

                if (clave === "")
                {
                    swal("Atención", "Favor de verificar cricterios de búsqueda", "warning");
                    return false;
                }

                if (clave !== "" && lote === "-Seleccione-")
                {
                    accion = "Clave";
                }

                window.open(context + "/kardex/gnrKardexClaveReload.jsp?Clave=" + clave + "&Lote=" + lote + "&Cadu=" + caducidad + "&Btn=" 
                                    + accion + "&origen=" + origen + "&fechaInicial=" + fechaInicial + "&fechaFinal=" + fechaFinal + "&ProyectoCL=");



            });
            
            
             $("#downloadKardex_1").click(function ()
            {
                var clave = $("#claveResult").text();
                var lote = $("#loteKardex option:selected").text();
                var caducidad = $("#caducidadKardex option:selected").text();
                var origen = $("#origenKardexId").val();
                var accion = "";                
                var fechaInicial = $("#fechaInicial").val();
                var fechaFinal = $("#fechaFinal").val();

                if (clave === "")
                {
                    swal("Atención", "Favor de verificar cricterios de búsqueda", "warning");
                    return false;
                }

                if (clave !== "" && lote === "-Seleccione-")
                {
                    accion = "Clave";
                }

                window.open(context + "/kardex/gnrKardexClaveReload_1.jsp?Clave=" + clave + "&Lote=" + lote + "&Cadu=" + caducidad + "&Btn=" 
                                    + accion + "&origen=" + origen + "&fechaInicial=" + fechaInicial + "&fechaFinal=" + fechaFinal + "&ProyectoCL=");



            });

            $.ajax({
                url: "${generalContext}/CatalogoController",
                data: {accion: <%=StaticText.OBTENER_CATALOGO_CLAVE%>},
                type: 'POST',
                async: true,
                dataType: 'json',
                success: function (data)
                {
                    data.forEach(function (value, id, arr) {
                        claves_productos.push(value.clave);
                        descripcion_productos.push(value.descripcion);
                    });

                }, error: function (jqXHR, textStatus, errorThrown) {

                    alert("Error Contactar al departamento de sistemas");

                }
            });
            
            $("#claveBusqueda").autocomplete({
                source: claves_productos,
                select: function (event, ui)
                {
                    var fechaInicial = $("#fechaInicial").val();
                    var fechaFinal = $("#fechaFinal").val();
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_CLAVE%>", ui.item.value, fechaInicial, fechaFinal);
                }
            });
            
            $("#descripcionBusqueda").autocomplete({
                source: descripcion_productos,
                select: function (event, ui)
                {
                    var fechaInicial = $("#fechaInicial").val();
                    var fechaFinal = $("#fechaFinal").val();
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_DESCRIPCION%>", ui.item.value, fechaInicial, fechaFinal);
                }

            });


            function cambiaLoteCadu(elemento) {
                var indice = elemento.selectedIndex;
                document.getElementById('caducidadKardex').selectedIndex = indice;
                $.ajax({
                    url: "${generalContext}/CatalogoController",
                    data: {accion: <%=StaticText.BUSCAR_INFORMACION_FOLLOT%>, folLot: elemento.value},
                    type: 'POST',
                    async: true,
                    dataType: 'json',
                    success: function (data)
                    {
                        $("#origenKardex").val(data.origen);
                        $("#origenKardexId").val(data.idOrigen);

                    }, error: function (jqXHR, textStatus, errorThrown) {

                        alert("Error Contactar al departamento de sistemas");

                    }
                });


            }
        </script>
       
    </body>
</html>
