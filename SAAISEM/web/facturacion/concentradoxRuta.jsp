<%-- 
    Document   : concentradoxRuta
    Created on : 27/03/2015, 09:19:56 AM
    Author     : Americo
--%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
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
    
System.out.println("Concentrado por ruta");
%>
<html>
    <head>
        <!-- Estilos CSS -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <%@include file="../jspf/menuPrincipal.jspf" %>

        <h4>Concentrados por Ruta</h4>
        
        <hr/>        
        <div class="row" id="divImagen">
            <div class="text-center">
                <!--img src="imagenes/ajax-loader-1.gif" width="100" /-->
            </div>
        </div>
        <table class="table table-bordered table-condensed table-striped" id="tbConcentrados">
            <thead>
                <tr>
                    <td>Fecha</td>
                    <td>Imprimir Global</td>
                    <td>Enviar Req. Rurales RURAL, CSU</td>
                    <!--td>Exportar CSRD</td>
                    <td>Concentrado CSU</td-->
                    <td>Exportar Global RURAL, CSU</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${concentrado}" var="detallef">
                <tr>                    
                    <td><c:out value="${detallef.fechaconvert}" /></td>                        
                    <td><a class="btn btn-block btn-primary btn-sm" target="_blank" href="reimpRutaConcentrado.jsp?F_FecSur=<c:out value="${detallef.fechas}" />"><span class="glyphicon glyphicon-print"></span></a></td>                    
                            <c:set var="contarCSRD" value="${detallef.csrd}" />
                    <td>
                        <c:if test="${contarCSRD > 0}">
                            <form action="FacturacionManual" method="post" onsubmit="muestraImagen()">
                            <input class="hidden" name="F_FecEnt" value="${detallef.fechas}">
                            <button id="btnFacts" class="btn btn-block btn-info btn-sm" name="accion" value="ReenviarConcentradoRequerimientos" onclick="return confirm('Seguro de Reenviar estas remisiones RURALES y CSU?'); muestraImagen()"><span class="glyphicon glyphicon-upload"></span></button>
                        </form>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${contarCSRD > 0}">
                        <a class="btn btn-block btn-info btn-sm" target="_blank" href="facturacion/ExporConcentrado.jsp?Fecha=<c:out value="${detallef.fechas}" />&Ban=0"><span class="glyphicon glyphicon-export"></span></a>
                        </c:if>
                        </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>

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
        <script>
                                $(document).ready(function () {
                                    $('#tbConcentrados').dataTable();
                                });
                                
                                $('#divImagen').toggle();
                                
                                function muestraImagen(){
                                    //$('#btnFacts').attr('disabled', true);
                                    //$('#btnConct').attr('disabled', true);
                                    $('#divImagen').toggle();
                                }
        </script>
    </body>
</html>
