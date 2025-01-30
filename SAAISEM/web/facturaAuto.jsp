<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
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
    String usua = "", Clave = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        Clave = (String) session.getAttribute("clave");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    if (Clave == null) {
        Clave = "";
    }
    ConectionDB con = new ConectionDB();
    String UsuaJuris = "";
    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "'");
        while (rset.next()) {
            UsuaJuris = rset.getString("F_Juris");
        }
        con.cierraConexion();
    } catch (Exception e) {

    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
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
                    <h3 class="panel-title">Facturación aútomatica</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="Facturacion">
                        <div class="form-group">
                            <div class="form-group">
                                <!--label for="Clave" class="col-xs-2 control-label">Clave*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Clave" name="Clave" placeholder="Clave" onKeyPress="return tabular(event, this)" autofocus >
                                </div-->
                                <label for="Nombre" class="col-xs-1 control-label">Clave Unidad</label>
                                <div class="col-xs-7">
                                    <select id="Nombre" name="Nombre" class="form-control">
                                        <option value="">Unidad</option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn u, tb_unireq r where u.F_ClaCli = r.F_ClaUni and F_Status=0 and F_StsCli = 'A' and F_Tipo = 'CEAPS' group by r.F_ClaUni");
                                                while (rset.next()) {
                                        %>
                                        <option value="<%=rset.getString(1)%>"
                                                <%
                                                    if (Clave.equals(rset.getString(1))) {
                                                        out.println("selected");
                                                    }
                                                %>
                                                ><%=rset.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {

                                            }
                                        %>
                                    </select>
                                </div>

                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"><button class="btn btn-block btn-primary" type="submit" name="accion" value="consultarAuto" onclick="return valida_clave();" > Consultar</button></div>
                            </div>

                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label for="FecFab" class="col-sm-1 control-label">Fec Entrega</label>
                                <div class="col-sm-2">
                                    <input type="date" class="form-control" id="FecFab" name="F_FecEnt" />
                                </div>
                            </div>
                        </div>

                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="guardarGlobalAuto" onclick="return valida_alta();">Generar Concentrado</button> 
                        <br/><br/>
                        <button class="btn btn-block btn-info" type="submit" name="accion" value="cancelar" onclick="return confirm('¿Seguro que desea CANCELAR esta orden?');">Cancelar</button> 
                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
                <div class="panel-footer">
                    <table class="table table-bordered table-condensed table-striped">
                        <tr>
                            <td>No Unidad</td>
                            <td>Nombre</td>
                            <td>Ver detalle</td>
                            <td>Eliminar</td>
                        </tr>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaCli from tb_uniatn u where F_ClaCli = '" + Clave + "' ");
                                while (rset.next()) {
                                    String F_NomCli = "";
                                    int banReq = 0;
                                    ResultSet rset2 = con.consulta("select  F_NomCli from tb_uniatn where F_ClaCli = '" + rset.getString("F_ClaCli") + "'");
                                    while (rset2.next()) {
                                        F_NomCli = rset2.getString("F_NomCli");
                                    }

                                    rset2 = con.consulta("select F_ClaUni from tb_unireq where F_Status = '0' and F_ClaUni = '" + rset.getString("F_ClaCli") + "'");
                                    while (rset2.next()) {
                                        banReq = 1;
                                    }
                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaCli")%></td>
                            <td><%=F_NomCli%></td>
                            <td>
                                <%
                                    if (banReq == 1) {
                                %>
                                <form action="detRequerimiento.jsp" method="post">
                                    <input name="pagina" class="hidden" value="facturaAuto.jsp">
                                    <input name="F_ClaUni" value="<%=rset.getString("F_ClaCli")%>" class="hidden" />
                                    <input name="F_FecEnt" value="<%=request.getParameter("F_FecEnt")%>" class="hidden" />
                                    <button class="btn btn-block btn-sm btn-primary"  ><span class="glyphicon glyphicon-search"></span></button>

                                </form>
                                <%
                                    }
                                %>
                            </td>
                            <td>

                                <%
                                    if (banReq == 1) {
                                %>
                                <form action="Facturacion" method="post">
                                    <input name="F_ClaUni" value="<%=rset.getString("F_ClaCli")%>" class="hidden" />
                                    <input name="F_FecEnt" value="<%=request.getParameter("F_FecEnt")%>" class="hidden" />
                                    <button class="btn btn-block btn-info" name="accion" value="cancelar"><span class="glyphicon glyphicon-remove"></span></button>
                                </form>
                                <%
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                out.println(e.getMessage());
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

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"></h4>
                    </div>
                    <div class="modal-body">
                        <div class="text-center" id="imagenCarga">
                            <img src="imagenes/ajax-loader-1.gif" />
                        </div>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        <script>
                            $(document).ready(function() {
                                $('#datosProv').dataTable();
                            });
                            function validaRemision() {
                                var confirmacion = confirm('Seguro que desea generar los Folios');
                                if (confirmacion === true) {
                                    $('#myModal').modal();
                                    $('#btnGeneraFolio').prop('disabled', true);
                                    return true;
                                } else {
                                    return false;
                                }
                            }

        </script> 

    </body>
</html>

