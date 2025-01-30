
<%-- 
    Document   : requerimiento.jsp
    Created on : 17/02/2014, 03:34:46 PM
    Author     : MEDALFA
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
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
        <title>SIALSS_CTRL MULTI</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Cargar productos caducados</h3>
                </div>
                <div class="panel-body ">
                    <form method="post" class="jumbotron"  action="FileUploadServletCaducos" enctype="multipart/form-data" name="form1">
                        <div class="form-group">
                            <div class="form-group">
                                <div class="col-lg-4 text-success">
                                    <h4>Seleccione el Excel a Cargar</h4>
                                </div>
                                <label for="file1" class="col-xs-2 control-label">Nombre Archivo*</label>
                                <div class="col-sm-5">
                                    <input class="form-control" type="file" name="file1" id="file1" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>                                    
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="guardar" onclick="return valida_alta();"> Cargar Archivo</button>
                    </form>
                    <div style="display: none;" class="text-center" id="Loader">
                        <img src="imagenes/ajax-loader-1.gif" height="150" alt="gif" />
                    </div>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
            </div>
        </div>
        <br><br><br>
        <%@include file="jspf/piePagina.jspf" %>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script>
                            function valida_alta() {
                                var Nombre = document.getElementById('file1').value;
                                if (Nombre === "") {
                                    alert("Seleccione un archivo por favor");
                                    return false;
                                }
                                document.getElementById('Loader').style.display = 'block';
                            }
        </script>
    </body>
</html>

<%-- 
    Document   : requerimiento.jsp
    Created on : 17/02/2014, 03:34:46 PM
    Author     : MEDALFA

 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
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
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title">Cargar productos caducados</h3>
                </div>
                <div class="panel-body ">
                    <form method="post" class="jumbotron"  action="FileUploadServletCaducos" enctype="multipart/form-data" name="form1">
                        <div class="form-group">
                            <div class="form-group">
                                <div class="col-lg-4 text-success">
                                    <h4>Seleccione el Excel a Cargar</h4>
                                </div>
                                <label for="file1" class="col-xs-2 control-label">Nombre Archivo*</label>
                                <div class="col-sm-5">
                                    <input class="form-control" type="file" name="file1" id="file1" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>                                    
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="guardar" onclick="return valida_alta();"> Cargar Archivo</button>
                    </form>
                    <div style="display: none;" class="text-center" id="Loader">
                        <img src="imagenes/ajax-loader-1.gif" height="150" alt="gif" />
                    </div>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
            </div>
        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2019 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script>
                            function valida_alta() {
                                var Nombre = document.getElementById('file1').value;
                                if (Nombre === "") {
                                    alert("Seleccione un archivo por favor");
                                    return false;
                                }
                                document.getElementById('Loader').style.display = 'block';
                            }
        </script>
    </body>
</html>
--%>
