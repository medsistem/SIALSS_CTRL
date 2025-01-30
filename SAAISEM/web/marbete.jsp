<%-- 
    Document   : InventarioExcel
    Created on : 12/10/2015, 04:45:46 PM
    Author     : Mario
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<%
    /**
     * Para cargar el excel del requerimiento
     */
    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/bootstrap.css" rel="stylesheet">
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            
            <%@include file="jspf/menuPrincipal.jspf"%>
        </div>
        <div class="panel container">
            
            <div class="panel-primary">
                <div class="panel-heading">
                    Generar Marbetes
                </div>                
                    <form method="post" class="jumbotron"  action="MarbeteCat" name="form1">
                        <div class="row">
                            <label for="Nombre" class="col-sm-1 control-label">Clave:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="clave" id="clave" value="" required/>                                    
                            </div>
                            <label for="Nombre" class="col-sm-1 control-label">Lote:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="lote" id="lote" value="" required/>                                    
                            </div>
                            <label for="Nombre" class="col-sm-1 control-label">Caducidad:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="date" name="cadu" id="cadu" value="" required/>                                    
                            </div>
                            
                        </div>
                        <br>
                        <br>
                        <div class="row">
                            <label for="Nombre" class="col-sm-1 control-label">CB:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="cb" id="cb" value="" required/>                                    
                            </div>
                            <!--label for="Nombre" class="col-sm-1 control-label">Tarimas:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="tarimas" id="tarimas" value="" required/>                                    
                            </div>
                            <label for="Nombre" class="col-sm-1 control-label">Cajas:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="cajas" id="cajas" value="" required/>                                    
                            </div>
                            <label for="Nombre" class="col-sm-1 control-label">PiezasxCajas:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="pcajas" id="pcajas" value="" required/>                                    
                            </div>
                        </div>
                        <br>   
                         <br>   
                        <div class="row">
                            <!--label for="Nombre" class="col-sm-1 control-label">TarimasI:</label-->
                            <!--div class="col-sm-2">
                                <input class="form-control" type="text" name="tarimasi" id="tarimasi" value="" required/>                                    
                            </div-->
                            <label for="Nombre" class="col-sm-1 control-label">Resto:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="text" name="resto" id="resto" value="" required/>                                    
                            </div>                            
                        </div>
                         
                        <br>
                        <br>
                        <br>
                        <div class="form-group col-sm-6">
                            <button class="btn btn-block btn-info" type="submit" name="accion" value="generarCarta"><span class="glyphicon glyphicon-refresh"></span>Generar Carta</button>
                        </div>
                        <div class="form-group col-sm-6">
                            <button class="btn btn-block btn-info" type="submit" name="accion" value="generarMedia"><span class="glyphicon glyphicon-refresh"></span>Generar Media Carta</button>
                        </div>
                    </form>
                </div>
            </div>
            <br>
            <br>
            
        </div>
        <br><br><br>
        <!--div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div-->
        <%@include file="jspf/piePagina.jspf"%>
        <!-- Modal -->
        <div id="ModiOc" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Modificar Clave</h4>
                    </div>

                    <form name="formEditOC" action="capturarInventario" method="Post">

                        <div class="modal-body">
                            <input class="form-control hidden" name="idMod" id="idMod" type="text" value="" readonly />

                            <div class="row">
                                <h4 class="col-sm-2">Clave:</h4>
                                <div class="col-sm-3">
                                    <input class="form-control" name="claveMod" id="claveMod" type="text" value="" readonly required/>
                                </div>
                                <h4 class="col-sm-3">Descripción</h4>
                                <div class="col-sm-3">
                                    <input class="form-control" name="descMod" id="descMod" type="text" value="" readonly required/>
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-1">Lote:</h4>
                                <div class="col-sm-3">
                                    <input class="form-control" name="loteMod" id="loteMod" type="text" value="" required/>
                                </div>
                                <h4 class="col-sm-1">Caducidad:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="caduMod" id="caduMod" type="date" value="" required/>
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-1">Cantidad:</h4>
                                <div class="col-sm-2">
                                    <input class="form-control" name="cantMod" id="cantMod" type="number" min="1" required/>
                                </div>
                                <!--h4 class="col-sm-2">Costo:</h4>
                                <div class="col-sm-3">
                                    <input class="form-control" name="costoMod" id="costoMod" type="number" value="" required/>
                                </div-->
                            </div>
                            <div class="row">
                                <h4 class="col-sm-3">CB:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="cbMod" id="cbMod" type="number" value="" required/>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="editar2">Guardar</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </div>

                    </form>

                </div>

            </div>
        </div>
        <div id="ModiEli" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">

                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Eliminar Clave</h4>
                    </div>

                    <form name="formEliminar" action="capturarInventario" method="Post">

                        <div class="modal-body">
                            <input class="form-control hidden" name="idEli" id="idEli" type="text" value="" readonly />

                            <div class="row">
                                <h4 class="col-sm-12">¿Seguro que desea eliminar la clave?</h4>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="eliminar2">Si</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                        </div>

                    </form>

                </div>

            </div>
        </div>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/bootstrap3-typeahead.js" type="text/javascript"></script>

        <script>
            $(document).ready(function () {

                $("#clave").typeahead({
                    source: function (request, response) {

                        $.ajax({
                            url: "AutoCompleteMedicamentos",
                            dataType: "json",
                            data: request,
                            success: function (data, textStatus, jqXHR) {
                                console.log(data);
                                var items = data;
                                response(items);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }

                });
                $("#descripcion").typeahead({
                    source: function (request, response) {

                        $.ajax({
                            url: "AutoCompleteMedicamentosDesc",
                            dataType: "json",
                            data: request,
                            success: function (data, textStatus, jqXHR) {
                                console.log(data);
                                var items = data;
                                response(items);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }

                });


            });

            //Obtener la caducidad cuando segun el lote
            $(".rowButton").click(function () {

                var $row = $(this).closest("tr");    // Find the row
                var $clave = $row.find("td.clave").text(); // Find the text             
                var $lote = $row.find("td.lote").text(); // Find the text
                var $cadu = $row.find("td.cadu").text(); // Find the text
                var $cant = $row.find("td.cantidad").text(); // Find the text
                var $id = $row.find("td.id").text(); // Find the text
                var $desc = $row.find("td.desc").text(); // Find the text
                //var $costo = $row.find("td.costo").text(); // Find the text
                var $cb = $row.find("td.cb").text(); // Find the text

                $("#claveMod").val($clave);
                $("#descMod").val($desc);
                $("#loteMod").val($lote);
                $("#caduMod").val(formatDate($cadu));
                $("#cantMod").val($cant);
                //$("#costoMod").val($costo);
                $("#cbMod").val($cb);
                $("#idMod").val($id);

            });
            $(".rowButtonEli").click(function () {

                var $row = $(this).closest("tr");    // Find the row
                var $id = $row.find("td.id").text(); // Find the text
                $("#idEli").val($id);

            });

            function stopRKey(evt) {
                var evt = (evt) ? evt : ((event) ? event : null);
                var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
                if ((evt.keyCode === 13)) {
                    return false;
                }
            }

            function formatDate(input) {
                var datePart = input.match(/\d+/g),
                        year = datePart[2], // get only two digits
                        month = datePart[1], day = datePart[0];

                //return day + '/' + month + '/' + year;
                return year + '-' + month + '-' + day;
            }

            document.onkeypress = stopRKey;

        </script>
    </body>
</html>

