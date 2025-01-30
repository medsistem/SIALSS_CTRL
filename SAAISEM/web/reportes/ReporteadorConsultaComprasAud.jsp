<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="ReportesPuntos.cache.CacheQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>

<%
    HttpSession sesion = request.getSession();
    String usua = "";

    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }

    request.setAttribute("inicio", sesion.getAttribute("ultimo_inicio"));
    request.setAttribute("fin", sesion.getAttribute("ultima_fin"));
    request.setAttribute("columnas", sesion.getAttribute("ultima_columnas"));

%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/reporteador/datatables.min.css" rel="stylesheet">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <hr/>

            <div>
                <h3>Reporteador Estadística de Entrega</h3>

                <h6>
                    <a class="btn btn-sm btn-default" href="ReporteadorComprasAuditoria.jsp"><span class="glyphicon glyphicon-arrow-left"></span> Regresar</a> 
                    <a class="btn btn-sm btn-primary" href="../entregas/exportar" target="_blank" ><span class="glyphicon glyphicon-save"></span> Exportar</a>
                </h6>               
                <h4>Rango de Fecha Del <c:out value="${inicio}"/> &nbsp;Al&nbsp; <c:out value="${fin}"/></h4>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras" width="100%">
                            <thead>
                                <tr>
                                    <c:forEach items="${columnas}" var="item">
                                        <td>
                                            <c:out value="${item}"/>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="../jspf/piePagina.jspf" %>

    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="../js/reporteador/datatables.min.js"></script>
<script>

    var chunk = <%=CacheQuery.CHUCK_SIZE%>;
    var tabla;
    var parte = 0;
    var limit = 0;
    var waiting = false;

    evento = function (row, data, dataIndex) {

        if ((data[0] >= limit) && (!waiting)) {
            parte++;
            getParte();
        }
    };


    $(document).ready(function () {
        setLimit(chunk);

        $.ajax({
            url: "../entregas/consulta",
            data: {"id": parte}
        }).done(function (data) {
            console.log("Va cargar tabla");
            setLimit(data.length);
            tabla = $('#datosCompras').DataTable({
                "data": data,
                "deferRender": true,
                "scrollX": true,
                "scrollY": 550,
                "scrollCollapse": true,
                "scroller": true,
                "createdRow": evento,
                "columnDefs": [
                    {
                        "targets": [0],
                        "visible": false,
                        "searchable": false
                    }
                ]
            });
            console.log("tabla completa");
        });
    });

    function getParte() {
        waiting = true;
        $.ajax({
            url: "../entregas/consulta",
            data: {"id": parte}
        }).done(function (data) {
            console.log("Carga adicional");
            setLimit(data.length);
            tabla.rows.add(data).draw(false);
            console.log("tabla completa");
            waiting = false;
        });
    }

    function setLimit(len) {
        if (len < chunk) {
            limit = 2147483647; //Maximo en 32bit
            return;
        }
        
        limit += Math.ceil(len *= 0.25);
    }
</script>
