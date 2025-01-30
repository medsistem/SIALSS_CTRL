<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%
    /**
     * Para verificar las compras que están en proceso de ingreso
     */
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", pedido = "", IdUsu = "", Usuario = "",Area="";

    //Verifica que este logeado el usuario
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        IdUsu = (String) sesion.getAttribute("IdUsu");
        Usuario = (String) sesion.getAttribute("Usuario");
        Area = (String) sesion.getAttribute("Area");
    } else {
        response.sendRedirect("index.jsp");
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link href="css/bootstrap-switch.css" rel="stylesheet" type="text/css"/>
        <!---->        
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administraci&oacute;n de Almacenes (SAA)</h4>
            <%@include file="../jspf/menuPrincipal.jspf"%>
            <div class="row">
                <div class="col-sm-12">
                    <h2>Reporte Usuarios Suspendidos</h2>
                </div>
            </div>
            <div class="container">                           
                <div class="row">                    
                    <c:if test="${modificadoOk!=null}">
                <div class="alert alert-success col-sm-4" role="alert">
                        <p><c:out value="${modificadoOk}"></c:out></p>
                        </div>
                        </c:if>
                    <c:if test="${altaOK!=null}">
                    <div class="alert alert-success col-sm-4" role="alert">
                        <p><c:out value="${altaOK}"></c:out></p>
                        </div>
                        </c:if>
            </div>
                <div class="row">
                    <table class="table table-bordered table-striped table-condensed" id="tablaUsuario">
                        <thead>
                            <tr>
                                <td>Id</td>
                                <td>Nombre</td>
                                <td>Fecha Suspendido</td>
                                <td>Fecha Activación</td>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listaUsuario}" var="detalle">
                            <tr>
                                <td class="idusu"><c:out value="${detalle.id}" /></td>
                                <td class="nombre"><c:out value="${detalle.nombre}" /></td>
                                <td class="sts"><c:out value="${detalle.fechasusp}" /></td>
                                <td class="sts">
                                    <c:set var="fechaVal" value="${detalle.fechaact}"/>
                                    <c:if test="${fechaVal != '00/00/0000'}">
                                    <c:out value="${detalle.fechaact}" />
                                    </c:if>
                                </td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>   
            </div>
            
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/bootstrap-switch.js" type="text/javascript"></script>
        <script>
            $(document).ready(function () {
            $("#tablaUsuario").dataTable();
        });
        </script>
    </body>
</html>
