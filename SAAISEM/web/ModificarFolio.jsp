<%-- 
    Document   : index
    Created on : 14/12/2016, 09:34:46 AM
    Author     : MEDALFA
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    
    Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema=formateador.format(fechaActual);
    int dia=0,mes=0,ano=0;
    String Fecha1="",Fecha2="",Fecha11="",Fecha22="",dia1="",mes1="";
    String Folio1="",Folio2="";
    String usua = "";
    String tipo = "";
    boolean editable = false;
    
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    if(sesion.getAttribute("editable") != null){
       editable = (Boolean)sesion.getAttribute("editable");
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
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <link href="css/select2.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <div class="row">
                <h4 class="col-sm-6">Modificar Folio</h4>
            </div>
            <form name="forma1" id="forma1" action="ModificarFolio" method="post">
                <div class="panel-footer">                
                    <div class="row">                    
                        <label class="control-label col-sm-1" for="fecha_ini">Folios</label>
                        <div class="col-sm-2">                            
                            <input name="Folio" id="Folio" class="form-control" required="true" />
                        </div>
                        <label class="control-label col-sm-1" for="fecha_ini">Proyecto</label>
                        <div class="col-sm-3">                            
                            <select name="Nombre" id="Nombre" class="form-control" required="true" >
                          
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-info" name="Accion" value="btnMostrar" onclick="return validaclaveproy();" >MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                        </div>
                        <%if(tipo.equals("1") || tipo.equals("7")) {%>
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-primary" name="Accion" value="btnUnidadFolio" onclick="return validaclaveproy();">UNIDAD&nbsp;<label class="glyphicon glyphicon-search"></label></button>     
                        </div>
                        <% } %>
                    </div>
                </div>
            </form>
            <form name="forma1" id="forma1" action="ModificarFolio" method="post">
                <c:forEach items="${DatosUnidad}" var="detalle">
                    <div class="row">
                        <label class="col-sm-1">Documento:</label>
                        <div class="col-sm-2"><c:out value="${detalle.folio}" /></div>
                        <label class="col-sm-1">Unidad:</label>
                        <div class="col-sm-5"><c:out value="${detalle.claveuni}" /> - <c:out value="${detalle.unidad}" /> </div>
                        <label class="col-sm-1">Entrega:</label>
                        <div class="col-sm-2"><c:out value="${detalle.fechaentrega}" /></div>
                    </div>                    
                </c:forEach>
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <div style="width:100%; height:400px; overflow:auto;">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Clave</th>
                                    <th>Descripción</th>
                                    <th>Lote</th>
                                    <th>Caducidad</th>
                                    <th>Req.</th>
                                    <th>Ubicación</th>
                                    <th>Ent.</th>
                                   
                                       <%
                                         if(editable){
                                        %>  
                                    <th>Modificar</th>
                                     <% }%>
                                
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listaRemision}" var="detalle2">
                                    <tr>
                                        <input class="hidden" name="IdFol" id="IdFol" value="${detalle2.iddocumento}">
                                        <input class="hidden" name="fol_gnkl" id="fol_gnkl" value="${detalle2.documento}">
                                        <td class="Clave">${detalle2.clavepro}</td>
                                        <td>${detalle2.descripcion}</td>
                                        <td class="Lote">${detalle2.lote}</td>
                                        <td class="Caducidad">${detalle2.caducidad}</td>
                                        <td>${detalle2.requerido}</td>
                                        <td>${detalle2.ubicacion}</td>
                                        <td class="Cantidad">${detalle2.surtido}</td>
                                        <!--td>${detalle2.devolucion}</td>
                                        <td>${detalle2.costo}</td>
                                        <td>${detalle2.monto}</td-->
                                           <%
                                                if(editable){
                                                %>  
                                        <td>
                                           
                                            <a class="btn btn-block btn-info rowButton" data-toggle="modal" data-target="#Devolucion${detalle2.iddocumento}"><span class="glyphicon glyphicon-pencil"></span></a>
                                                  
                                        </td>
                                         <% }%>
                                       
                                    </tr>   
                                </c:forEach>
                            </tbody>
                        </table>
                        </div>
                    </div>
                </div>
                <c:forEach items="${DatosUnidad}" var="detalle">
                    <div class="row">
                        <input class="hidden" name="foliod" id="foliod" value="${detalle.folio}">
                        <input class="hidden" name="claveuni" id="claveuni" value="${detalle.claveuni}">
                        <input class="hidden" name="unidad" id="unidad" value="${detalle.unidad}">
                        <input class="hidden" name="fechaentrega" id="fechaentrega" value="${detalle.fechaentrega}">
                        <input class="hidden" name="proyectoM" id="proyectoM" value="${detalle.proyectoM}">
                        
                        <%
                            if(editable){
                            %>    
                        <button class="btn btn-block btn-info" name="Accion" value="btnAgregarFolio" >Agregar&nbsp;<label class="glyphicon glyphicon-plus-sign"></label></button>
                        <% }else{%>
                            <label>Este folio está siendo surtido y no puede ser modificado</label>
                        <% }%>
                    </div>
                </c:forEach>
            </form>
            <!--Modal-->
            <c:forEach items="${listaRemision}" var="detalle2">
            <div id="Devolucion${detalle2.iddocumento}" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>Modificar Folio</h4>
                        </div>
                        <form name="Devolucion" action="ModificarFolio" method="Post">
                            <div class="modal-body">
                                <div class="row">
                                    <h4 class="col-sm-2">Folio</h4>
                                    <div class="col-sm-4">
                                        <input class="form-control" name="proyectoM" id="proyectoM" type="hidden" value="${detalle2.proyectoM}" >
                                        <input class="form-control" name="Folio" id="Folio" type="text" value="${detalle2.documento}" readonly="" required="">
                                    </div>
                                    <h4 class="col-sm-2">Identificador</h4>
                                    <div class="col-sm-4">
                                        <input class="form-control" name="Identi" id="Identi" type="text" value="${detalle2.iddocumento}" readonly="" required="">
                                    </div>
                                </div>
                                    <div class="row">
                                        <h4 class="col-sm-2">Clave</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Clave1" id="Clave1" type="text" value="${detalle2.clavepro}" readonly="" required="">
                                        </div>
                                        <h4 class="col-sm-2">Lote</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Lote1" id="Lote1" type="hidden" value="${detalle2.lote}">
                                            <input class="form-control" name="Lote2" id="Lote2" type="text" value="${detalle2.lote}" required="">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <h4 class="col-sm-2">Caducidad</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Caducidad1" id="Caducidad1" type="hidden" value="${detalle2.caducidad}">
                                            <input class="form-control" name="Caducidad2" id="Caducidad2" type="date" value="${detalle2.caducidad}" required="">
                                        </div>
                                        <h4 class="col-sm-2">Surtida</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Surtida" id="Surtida" type="text" value="${detalle2.surtido}" readonly="" required="">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <h4 class="col-sm-2">Origen</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Origen" id="Origen" type="text" value="${detalle2.origen}" readonly="" required="">
                                        </div>
                                        <h4 class="col-sm-2">Proyecto</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Proyecto" id="Proyecto" type="text" value="${detalle2.proyecto}" readonly="" required="">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <h4 class="col-sm-2">Ubicación</h4>
                                        <div class="col-sm-4">
                                            <input class="form-control" name="Ubicacion1" id="Ubicacion1" type="text" value="${detalle2.ubicacion}" readonly="" required="">
                                        </div>
                                    </div>
                            </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-default" name="Accion" value="btnModificar">Guardar</button>
                                        <button type="submit" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                                    </div>
                        </form>
                    </div>
                </div>            
            </div>
            </c:forEach>
            <!--Modal-->
        </div>
            
        <%@include file="jspf/piePagina.jspf" %>
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
        <script src="js/select2.js" type="text/javascript"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script src="js/modificarfolio/ModificarFolio.js" type="text/javascript"></script>
        <script>
            function validaclaveproy(){
                var fol = document.getElementById('Folio').value;
                 if (fol === "") {
                                    alert('Favor de Capturar Toda la información');
                                    return false;
                                }
                
                var py = $('#Nombre option:(0)').value;
                var selecpy = py.options[py.selectedIndex].text; 
                
                 if (selecpy === "--Selecciona Proyecto--") {
                                    alert('Favor de Capturar Proyecto');
                                    return false;
                                }
                
            }
        </script>
    </body>
</html>
