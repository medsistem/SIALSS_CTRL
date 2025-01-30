<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    String usua = "", tipo = "";
    String username = "";
    if (sesion.getAttribute("nombre") != null ) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        username = (String) sesion.getAttribute("Usuario");
        if(!tipo.equals("1") && !tipo.equals("7")){
            response.sendRedirect("./index.jsp");
        }} else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <hr/>
        </div>
        <div class="container">
            <h3>
                Cambio Físico
            </h3>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    Insumo a Cambiar
                </div>
                <div class="panel-body">
                    <table class="table table-condensed table-bordered table-striped ">
                        <tr>
                            <td>Clave</td>
                            <td>Lote</td>
                            <td>Caducidad</td>
                            <td>Cantidad</td>
                            <td>Ubicación</td>
                            <td>Devolver</td>
                        </tr>

                        <%                        try {
                                con.conectar();
                                ResultSet rset = con.consulta("select l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad,'%d/%m/%Y') AS F_FecCad, l.F_ExiLot, l.F_Ubica, l.F_IdLote from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro and l.F_Ubica='REJA_DEVOL' and l.F_ExiLot!=0");
                                while (rset.next()) {
                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaPro")%></td>
                            <td><%=rset.getString("F_ClaLot")%></td>
                            <td><%=rset.getString("F_FecCad")%></td>
                            <td><%=rset.getString("F_ExiLot")%></td>
                            <td><%=rset.getString("F_Ubica")%></td>
                            <td>
                                <%
                                    if (tipo.equals("3")) {
                                %>

                                <a class="btn btn-block btn-info" data-toggle="modal" data-target="#Devolucion<%=rset.getString("F_IdLote")%>"><span class="glyphicon glyphicon-remove-circle"></span></a></a>
                                    <%
                                        }
                                    %>
                            </td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>

                    </table>
                </div>
            </div>
        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>




        <!--
                Modal
        -->
        <%
            try {
                con.conectar();
                try {
                    ResultSet rset = con.consulta("select l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad,'%d/%m/%Y') AS F_FecCad, l.F_ExiLot, l.F_Ubica, l.F_IdLote from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro and l.F_Ubica='REJA_DEVOL' and l.F_ExiLot!=0");
                    while (rset.next()) {
        %>
        <div class="modal fade" id="Devolucion<%=rset.getString("F_IdLote")%>" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <form action="Devoluciones">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-5">
                                    Cambio Físico
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <input id="IdLote" name="IdLote" value="<%=rset.getString("F_IdLote")%>" class="hidden">
                            <div class="row">
                                <div class="col-sm-3">
                                    Clave: <%=rset.getString("F_ClaPro")%>
                                </div>
                                <div class="col-sm-9">
                                    Descripción: <%=rset.getString("F_DesPro")%>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4">
                                    Lote: <%=rset.getString("F_ClaLot")%>
                                </div>
                                <div class="col-sm-4">
                                    Caducidad: <%=rset.getString("F_FecCad")%>
                                </div>
                                <div class="col-sm-4">
                                    Ubicación: <%=rset.getString("F_Ubica")%>
                                </div>
                            </div>
                            <hr/>
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Cantidad a Cambiar:<%=rset.getString("F_ExiLot")%></h4>
                                </div>
                            </div>
                            <hr/>
                            Ingrese los nuevos datos del insumo
                            <div class="row">
                                <h4 class="col-sm-2">
                                    Lote:
                                </h4>
                                <div class="col-sm-4">
                                    <input class="form-control" id="Lote<%=rset.getString("F_IdLote")%>" name="F_ClaLot" />
                                </div>
                                <h4 class="col-sm-2">
                                    Caducidad:
                                </h4>
                                <div class="col-sm-4">
                                    <input class="form-control" type="date" id="Cadu<%=rset.getString("F_IdLote")%>" name="F_FecCad"/>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Observaciones</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <textarea name="Obser" id="Obser<%=rset.getString("F_IdLote")%>" class="form-control"></textarea>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Contraseña</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input name="ContraDevo<%=rset.getString("F_IdLote")%>" id="ContraDevo<%=rset.getString("F_IdLote")%>" class="form-control" type="password" onkeyup="validaContra(this.id);" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" id="<%=rset.getString("F_IdLote")%>" disabled onclick="return validaDevolucion(this.id);" name="accion" value="devolucion">Devolver</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <%
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        %>
        <!--
        /Modal
        -->
    </body>
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
                                    function validaDevolucion(e) {
                                        var id = e;

                                        if (document.getElementById('Obser' + id).value === "" || document.getElementById('Lote' + id).value === "" || document.getElementById('Cadu' + id).value === "") {
                                            alert("Ingrese la información necesaria")
                                            return false;
                                        }
                                    }


                                    function validaContra(elemento) {
                                        //alert(elemento);
                                        var pass = document.getElementById(elemento).value;
                                        var id = elemento.split("ContraDevo");
                                        if (pass === "30090824102411oscar") {
                                            //alert(pass);
                                            document.getElementById(id[1]).disabled = false;
                                            //$(id[1]).prop("disabled", false);
                                        } else {
                                            document.getElementById(id[1]).disabled = true;
                                            //$(id[1]).prop("disabled", true);
                                        }
                                    }
    </script>
</html>

