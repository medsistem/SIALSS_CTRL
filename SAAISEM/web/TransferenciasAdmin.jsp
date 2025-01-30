<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
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
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    try{
        Fecha1 = (String) sesion.getAttribute("fecha_ini");
        Fecha2 = (String) sesion.getAttribute("fecha_fin");
        Folio1 = (String) sesion.getAttribute("folio1");
        Folio2 = (String) sesion.getAttribute("folio2");            
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
    if(Folio1 == null){Folio1="";}
    if(Folio2 == null){Folio2="";}
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

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="row">
                <h3 class="col-sm-6">Administrar Tranferencias de Productos</h3>                
            </div>
            <form name="forma1" id="forma1" action="Transferenciasentreproyectos" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" value="<%=Fecha1%>" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_fin" name="fecha_fin" value="<%=Fecha2%>" type="date" onchange="habilitar(this.value);"/>
                    </div>    
                    <div class="col-sm-3">
                            <button class="btn btn-block btn-info" name="Accion" value="btnMostrar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                        </div>
                </div>   
            </div>
            <div>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <th>Fecha Mov.</th>
                                    <th>Doc. Mov.</th>
                                    <th>De</th>
                                    <th>A</th>
                                    <th>Transferencia</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ListaTranferencia}" var="detalle">
                                <tr>
                                    <td><c:out value="${detalle.fecha}" /></td>
                                    <td><c:out value="${detalle.documento}" /></td>
                                    <td><c:out value="${detalle.proyectoini}" /></td>
                                    <td><c:out value="${detalle.proyectofin}" /></td>
                                    <td  style="align-content: center"><a href="reimpTransferencia.jsp?fol_gnkl=<c:out value="${detalle.documento}" />" target="_blank"  class="btn btn-block btn-info form-control glyphicon glyphicon-print"></a></td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            </form>
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
        <script>
        $(document).ready(function () {
        $('#datosCompras').dataTable();
        });
        </script>
        <script>
            $(function () {
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });
        
        </script>
    </body>
</html>
