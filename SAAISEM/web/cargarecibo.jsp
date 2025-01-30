<%-- 
    Document   : requerimiento.jsp
    Created on : 17/02/2014, 03:34:46 PM
    Author     : MEDALFA
--%>
<%@page import="conn.ConectionDB"%>
<%@page import="com.mysql.jdbc.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();
     ConectionDB con = new ConectionDB();
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
                    <h3 class="panel-title">Cargar OC</h3>
                </div>
                <div class="panel-body ">
                    <form method="post" class="jumbotron"  action="FileUploadServletRecibo" enctype="multipart/form-data" name="form1">
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
                    <div class="table-responsive">
                       <!-- 
                        <table class="table table-bordered table-responsive">
                            <thead class="thead-dark">
                                <tr>
                                    <th scope="col">No_OC</th>
                                    <th scope="col">Proveedor</th>
                                    <th scope="col">Clave</th>
                                    <th scope="col">Cantidad</th>
                                    <th scope="col">Proyecto</th>
                                    <th scope="col">Problema clave</th>
                                    <th scope="col">Problema Proveedor</th>
                                    <th scope="col">Problema Proyecto</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                con.conectar();
                        ResultSet rset = null;
                         String qryERRORoc = "";
                      //rset = con.consulta(qryERRORoc);
                                 //Consulta = con.consulta("qwe");
                                %>
                                <tr>
                                    <th>1</th>
                                    <td>Mark</td>
                                    <td>Otto</td>
                                    <td>@mdo</td>
                                </tr>
                                
                            </tbody>
                        </table>


                    </div>-->
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
