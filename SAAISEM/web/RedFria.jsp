<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();

    Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema = formateador.format(fechaActual);
    int dia = 0, mes = 0, ano = 0;
    String Fecha1 = "", Fecha2 = "", Fecha11 = "", Fecha22 = "", dia1 = "", mes1 = "";
    String Folio1 = "", Folio2 = "";
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    try {

        con.conectar();

        ResultSet RFecha = con.consulta("SELECT DAY(CURDATE()) as dia,MONTH(CURDATE()) AS mes,YEAR(CURDATE()) AS ano,CURDATE() AS fecha;");
        if (RFecha.next()) {
            Fecha22 = RFecha.getString(4);
            Fecha11 = RFecha.getString(4);
        }
        try {
            Fecha1 = (String) sesion.getAttribute("fecha_ini");
            Fecha2 = (String) sesion.getAttribute("fecha_fin");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if ((Fecha1 == "") || (Fecha1 == null)) {
            Fecha1 = Fecha11;
        } else {
            Fecha1 = Fecha1;
        }

        if ((Fecha2 == "") || (Fecha2 == null)) {
            Fecha2 = Fecha22;
        } else {
            Fecha2 = Fecha2;
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
                <h3 class="col-sm-3">Red Fría</h3>

            </div>
            <form name="forma1" id="forma1" action="RedFriaReporte" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" value="<%=Fecha1%>" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" value="<%=Fecha2%>" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2 navbar-right">
                           <a class="btn btn-block btn-default " href="" onclick="window.open('/SIALSS_CTRL/AltaClaveFria.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Alta Clave °C&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-pencil"></span></a>     
                        </div>
                    </div> 
                        
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-4">
                            <button class="btn btn-block btn-info" name="accion" value="mostrar" >MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                        </div>
                        <div class="col-sm-4">
                            <a class="btn btn-block btn-info " data-toggle="modal" data-target="#Temperatura" title="Cambio de temperatura °C">Temperatura °C&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-pencil"></span></a>                            
                        </div>
                        <div class="col-sm-4">
                            <a class="btn btn-block btn-primary" href="reportes/gnrRedFria.jsp?fecha_ini=<%=Fecha1%>&fecha_fin=<%=Fecha2%>" title="Exportar Concentrado">Exportar&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-export"></span></a>                            
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
                                        <td>No. Folio</td>
                                        <td>Punto de Entrega</td>
                                        <td>Estatus</td>
                                        <td>Fec Ent</td>
                                        <td>Folio</td>           
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            String F_StsFact = "", F_FecEnt = "", F_FecApl = "", Query = "", TipoU = "";

                                            try {

                                                ResultSet rset = con.consulta("SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,F_StsFact,DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,u.F_Tipo FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaPro IN (SELECT F_ClaPro FROM tb_redfria) AND F_FecEnt BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_CantSur>0 AND F_StsFact='A' GROUP BY F_ClaDoc,f.F_ClaCli,F_StsFact ORDER BY F.F_ClaDoc+0;");
                                                while (rset.next()) {
                                                    F_StsFact = rset.getString("F_StsFact");
                                                    F_FecApl = rset.getString("F_FecApl");
                                                    Date fechaDate1 = formateador.parse(F_FecApl);
                                                    Date fechaDate2 = formateador.parse(fechaSistema);
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%> - <%=rset.getString(3)%></td>
                                        <td><%=rset.getString("F_StsFact")%></td>
                                        <td><%=rset.getString("F_FecEnt")%></td>
                                        <td>
                                            <form action="RedFriaReporte" method="post">
                                                <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                                <button class="btn btn-block btn-info" name="accion" value="ModiRed"><span class="glyphicon glyphicon-arrow-left"></span></button>
                                            </form>
                                        </td>

                                    </tr>
                                    <%
                                                }

                                            } catch (Exception e) {
                                                System.out.println(e);

                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <!--
                Modal temperatura
        -->
        <div id="Temperatura" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>
                            Modificar Devoluci&oacute;n
                        </h4>
                    </div>
                    <form name="Temperatura" action="RedFriaReporte" method="Post">
                        <div class="modal-body">                            
                            <div class="row">
                                <h4 class="col-sm-2">Temperatura °C</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="tempe" id="tempe" type="text" value="" required="">
                                </div>                                
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="ModificarTemp">Guardar</button>
                            <button type="submit" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </div>
                    </form>
                </div>
            </div>            
        </div>
        <!--
        /Modal
        -->                        

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
                                $(document).ready(function() {
                                    $('#datosCompras').dataTable();
                                });
        </script>
        <script>
            $(function() {
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });
            function validaCancel(e) {
                var id = e;
                if (document.getElementById('Obser' + id).value === "") {
                    alert("Ingrese las observaciones de la devolución")
                    return false;
                }
            }
            function validaContra(elemento) {
                //alert(elemento);
                var pass = document.getElementById(elemento).value;
                var id = elemento.split("ContraCancel");
                if (pass === "rosalino") {
                    //alert(pass);
                    document.getElementById(id[1]).disabled = false;
                    //$(id[1]).prop("disabled", false);
                } else {
                    document.getElementById(id[1]).disabled = true;
                    //$(id[1]).prop("disabled", true);
                }
            }
        </script>
    </body>
</html>
