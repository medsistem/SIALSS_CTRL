<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "", Cantidad = "", DesProyecto="", Proyecto="";

    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
        Cantidad = (String) request.getAttribute("Cantidad");
        DesProyecto = (String) request.getAttribute("DesProyecto");
        Proyecto = (String) request.getAttribute("Proyecto");
    } catch (Exception e) {

    }
    if (ClaCli == null) {
        ClaCli = "";
    }
    if (FechaEnt == null) {
        FechaEnt = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
    }
    if (DesPro == null) {
        DesPro = "";
    }
    if (Cantidad == null) {
        Cantidad = "";
    }

    if(DesProyecto==null){
        DesProyecto="";
    }

    if(Proyecto==null){
        Proyecto="";
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
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>
            <div class="row">
                <div class="col-sm-12">
                    <h2>Facturación Manual</h2>
                </div>
            </div>
            <hr/>
            <form action="FacturacionManual" method="post">
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Unidad:</h4>
                    </div>
                    <div class="col-sm-4">
                        <input value="<%=ClaCli%>"  class="form-control" name="ClaCli" id="ClaCli" readonly="" />
                        <!--select class="form-control" name="ClaCli" id="ClaCli">
                            <option value="">-Seleccione Unidad-</option>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn");
                                while (rset.next()) {
                        %>
                        <option value="<%=rset.getString(1)%>"
                        <%
                            if (rset.getString(1).equals(ClaCli)) {
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
                    </select-->
                    </div>
                    <div class="col-sm-2">
                        <h4>Fecha de Entrega</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" name="FechaEnt" id="FechaEnt" min="<%=df2.format(new Date())%>" value="<%=FechaEnt%>"/>
                    </div>
                    <div class="col-sm-1">
                        <h4>Proyecto</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="text" readonly="" class="form-control" name="DesProyecto" id="DesProyecto" value="<%=DesProyecto%>"/>
                        <input type="hidden" readonly="" class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">

                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-1">
                                <h4>CLAVE:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" readonly="" id="ClaPro" name="ClaPro" value="<%=ClaPro%>"/>
                            </div>
                            <div class="col-sm-2">
                                <h4>Descripción:</h4>
                            </div>
                            <div class="col-sm-7">
                                <textarea class="form-control" readonly=""><%=DesPro%></textarea>
                            </div>
                        </div>
                        <br/>

                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Cantidad a Facturar:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="Cantidad" id="Cantidad" value="<%=Cantidad%>"/>
                            </div>
                            <div class="col-sm-2 col-sm-offset-6">
                                <a class="btn btn-block btn-default" href="facturacionManual.jsp">Regresar</a>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
            <table class="table table-condensed table-striped table-bordered table-responsive">
                <tr>
                    <td>CLAVE:</td>
                    <td>Origen</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <td>Cantidad</td>
                    <td>Marca</td>
                    <td>Proyecto</td>
                    <td>Seleccionar</td>
                </tr>
                <%
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, l.F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE l.F_ClaPro = '"+ClaPro+"' AND F_ExiLot != 0 AND l.F_Proyecto = '"+Proyecto+"' ORDER BY l.F_Origen, F_FecCad ASC;");
                        while (rset.next()) {
                            int cant = 0, cantTemp = 0;
                            int cantLot = rset.getInt(5);
                            ResultSet rset2 = con.consulta("select SUM(F_Cant) from tb_facttemp where F_IdLot = '" + rset.getString("F_IdLote") + "' and (F_StsFact = '0' or F_StsFact = '3')  ");
                            while (rset2.next()) {
                                cantTemp = rset2.getInt(1);
                            }

                            cant = cantLot - cantTemp;
                %>
                <tr>
                    <td><%=rset.getString(1)%></td>
                    <td><%=rset.getString("F_Origen")%></td>
                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getString(3)%></td>
                    <td><%=rset.getString(4)%></td>
                    <td><%=cant%></td>
                    <td><%=rset.getString("F_DesMar")%></td>
                    <td><%=rset.getString(10)%></td>
                    <td>
                        <form action="FacturacionManual" method="post">
                            <input name="FolLot" value="<%=rset.getString(7)%>" class="hidden" readonly=""/>
                            <input name="IdLot" value="<%=rset.getString(6)%>" class="hidden" readonly=""/>
                            <input class="hidden" name="Cant" id="Cant<%=rset.getString(6)%>" value=""/>
                            <input class="hidden" name="CantAlm_<%=rset.getString(6)%>" id="CantAlm_<%=rset.getString(6)%>" value="<%=cant%>"/>
                            <button name="accion" type="button" value="AgregarClave" id="BtnAgregar_<%=rset.getString(6)%>" class="btn btn-block btn-primary" onclick="return validaCantidad(this.id);"><span class="glyphicon glyphicon-ok"></span></button>
                            <!--button name="accion" value="AgregarClave" id="BtnAgregar_<%=rset.getString(6)%>" class="btn btn-block btn-primary" onclick="return validaCantidad(this.id);"><span class="glyphicon glyphicon-ok"></span></button-->
                        </form>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                %>
            </table>

        </div>
        <%@include file="jspf/piePagina.jspf" %>
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
    <script src="js/bootstrap-datepicker.js"></script>
    <script src="js/jquery.alphanum.js" type="text/javascript"></script>
    <script src="js/facturajs/Facturacionjs.js"></script>
    <script src="js/sweetalert.min.js" type="text/javascript"></script>    
</html>

