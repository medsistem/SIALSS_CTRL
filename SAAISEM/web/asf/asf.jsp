<%-- 
    Document   : asf
    Created on : 26/11/2018, 08:58:41 PM
    Author     : IngMa
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    DecimalFormat dosDecimas = new DecimalFormat("###,###,###.##");

    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    dosDecimas.setDecimalFormatSymbols(custom);

    Date date = new Date();
//Caso 3: obtenerhora y fecha y salida por pantalla con formato:
    DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//System.out.println("Hora y fecha: "+hourdateFormat.format(date));
    String Proyecto = "", Texto = "";

    try {
        Proyecto = request.getParameter("proyecto");
    } catch (Exception e) {

    }
    switch (Proyecto) {
        case "1":
            Texto = "ISEM";
            break;
        case "2":
            Texto = "MICHOACÁN";
            break;
        case "3":
            Texto = "ISSEMYM";
            break;
        default:
            break;
    }

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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../css/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="../jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container-fluid" >
            <div class="row">
                <input type="hidden" name="proyecto" id="proyecto" value="<%=Proyecto%>" />
                <div class="col-sm-2"><!--img src="imagenes/isem_original.png" alt=""/></div-->
                    <div class="col-sm-9">
                        <h1>LODIMED</h1>
                        <h4>Logística y distribución de medicamentos</h4>
                    </div>
                </div>
                <br/>
                <h2>Inventario en el Centro de Distribución <%=Texto%></h2>
                <h4 style="color: green" class="text-right">Actualizado al:&nbsp;<%=hourdateFormat.format(date)%></h4>
                <hr/>
                <div>

                    <label>Total de Claves Autorizadas: <span id="clavesAutorizadas" ></span></label>
                    <br />
                    <label>Total de Claves Medicamentos: <span id="medicamento" ></span></label>&nbsp;&nbsp;&nbsp;
                    <label>Total de Claves Material de Curación: <span id="materialCuracion" ></span></label>&nbsp;&nbsp;&nbsp;
                    <label>Total de Claves Soluciones: <span id="soluciones" ></span></label>&nbsp;&nbsp;&nbsp;
                    <!--label>Total de Claves Agotar Existencias: </label>&nbsp;&nbsp;&nbsp;
                    <label>Total de Claves inhabilitadas 2014/2015/2016 con Existencias: </label-->&nbsp;&nbsp;&nbsp;
                    <br />

                    <label>Total de Claves en Ceros: <span id="clavesEnCero" ></span></label>&nbsp;&nbsp;&nbsp;
                    <label>Total de Piezas Disponibles: <span id="existenciasTotales" ></span></label>

                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div class="row" >
                            <div class="col-sm-3" >
                                <a href="../menu.jsp?proyecto=<%=Proyecto%>&nombre=<%=Texto%>" class="btn btn-info"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;&nbsp;Regresar</a>
                                <button class="btn btn-info" type="button" id="downloadAsf"><span class="glyphicon glyphicon-download"></span> Descargar</button>
                            </div>
                            <label for="meses" class=" col-sm-2 text-right" style="margin-top: 5px;" >Prioridad:</label>   
                            <div class="col-sm-2" >
                                <input type="number" min="0" id="prioridad" class="form-control" placeholder="Prioridad"/>
                            </div>
                            <label for="meses" class=" col-sm-2 text-right" style="margin-top: 5px;" >Meses a consultar:</label>   
                            <div class="col-sm-2" >
                                <input type="number" min="0" id="meses" class="form-control" placeholder="Meses"/>
                            </div>

                            <div class="col-sm-1" >
                                <button type="button" id="consultarMeses" class="btn btn-block btn-primary" >Consultar</button>
                            </div>
                        </div>

                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered table-condensed table-striped" id="datosProv">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>            
                <p>*Costo unitario promedio en claves de Licitaci&oacute;n publica</p>
                <p>**CPD = Consumo Promedio Diario</p>
                <p>***Licitaci&oacute;n publica = LP, Compra Consolidada = CC, Soluciones = SO, Compra Consolidada + Soluciones = CC/SO</p>
            </div>
        </div> 

        <%@include file="../jspf/piePagina.jspf" %>
        <script src="../js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/bootstrap.js"></script>
        <script src="../js/asf/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/utils/loader.js" type="text/javascript"></script>
        <script>
            var proyecto = "<%=Proyecto%>";
            var currentContext = "${pageContext.servletContext.contextPath}";
        </script>
        <script src="../js/asf/asf.js"></script>
    </body>
</html>
