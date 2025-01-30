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
    String fechaSistema=formateador.format(fechaActual);
    
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String UsuaJuris = "(";

    try {

        con.conectar();
        String F_Juris = "";
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "'");
        while (rset.next()) {
            F_Juris = rset.getString("F_Juris");
        }

        for (int i = 0; i < F_Juris.split(",").length; i++) {
            if (i == F_Juris.split(",").length - 1) {
                UsuaJuris = UsuaJuris + "FR.F_Ruta like 'R" + F_Juris.split(",")[i] + "%' ";
            } else {
                UsuaJuris = UsuaJuris + "FR.F_Ruta like 'R" + F_Juris.split(",")[i] + "%' or ";
            }
        }

        UsuaJuris = UsuaJuris + ")";
        System.out.println(UsuaJuris);
        con.cierraConexion();
    } catch (Exception e) {

    }
    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("fecha");
        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
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
                <h3 class="col-sm-3">Administrar Remisiones</h3>
                <div class="col-sm-2 col-sm-offset-5">
                    <br/>
                    <!-- class="btn btn-info" href="gnrFacturaConcentrado.jsp" target="_blank">Imprimir Multiples</a-->
                </div>
                <div class="col-sm-2">
                    <br/>
                    <a class="btn btn-primary" href="gnrFacturaConcentrado.jsp" target="_blank">Exportar Global</a>
                </div>
            </div>
            <div>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <!--td></td-->
                                    <td>No. Folio</td>
                                    <td>Punto de Entrega</td>
                                    <td>Estatus</td>
                                    <td>Fec Ent</td>
                                    <td>Tipo</td>
                                    <td>Folio</td>
                                    <td>Ver Fact</td>
                                    <td>Devolución</td>
                                    <td>Reportes</td>
                                    <% if ((usua.equals("ricardo")) || (usua.equals("sistemas"))) {
                                            out.println("<td>Reintegrar Insumo</td>");
                                        }
                                    %>
                                    <td>Cancelar</td>
                                    <td>Excel</td>
                                    <td>Modula</td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        String F_StsFact="",F_FecEnt="",F_FecApl="";
                                        try {
                                            ResultSet rset = con.consulta("SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,F_StsFact,DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,o.F_Req FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc=o.F_IdFact GROUP BY F_ClaDoc,f.F_ClaCli,F_StsFact ORDER BY F.F_ClaDoc+0;");
                                            while (rset.next()) {
                                                F_StsFact = rset.getString("F_StsFact");
                                                F_FecApl = rset.getString("F_FecApl");
                                                String F_Req1 = rset.getString("F_Req");
                                                Date fechaDate1 = formateador.parse(F_FecApl);
                                                Date fechaDate2 = formateador.parse(fechaSistema);
                                %>
                                <tr>
                                    <!--td>
                                        <input type="checkbox" name="">
                                    </td-->
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%> - <%=rset.getString(3)%></td>
                                    <td><%=rset.getString("F_StsFact")%></td>
                                    <td><%=rset.getString("F_FecEnt")%></td>
									<%if(F_Req1 == null){%>
									<td>M</td>
									<%}else{%>
									<td><%=rset.getString("F_Req")%></td>
									<%}%>
                                    
                                    <td>
                                        <form action="reimpFactura.jsp" target="_blank">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-primary"><span class="glyphicon glyphicon-print"></span></button>
                                        </form>
                                    </td>
                                    <td>
                                        <form action="verFactura.jsp" method="post">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-primary"><span class="glyphicon glyphicon-search"></span></button>
                                        </form>
                                    </td>
                                    <td>
                                        <%
                                            if ((tipo.equals("7")) || (tipo.equals("10"))) {
                                        %>
                                        <form action="devolucionesFacturas.jsp" method="post">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-info"><span class="glyphicon glyphicon-arrow-left"></span></button>
                                        </form>
                                        <%
                                            }
                                        %>
                                    </td>
                                    <td>
                                        <form class="form-horizontal" role="form" name="formulario_receta" id="formulario_receta" method="get" action="ReporteImprime">   
                                            <button class="btn btn-block btn-primary" id="btn_capturar" name="btn_capturar" value="<%=rset.getString(1)%>" onclick="return confirm('¿Esta Ud. Seguro de Iniciar proceso de Generación?')"><span class="glyphicon glyphicon-floppy-save"></span></button>
                                        </form>
                                    </td>
                                    <%
                                        if((usua.equals("ricardo")) || (usua.equals("sistemas"))) {
                                    %>
                                    <td>
                                        <%
                                            ResultSet rset2 = con.consulta("select * from tb_factdevol where F_ClaDoc = '" + rset.getString(1) + "' group by F_ClaDoc");
                                            while (rset2.next()) {
                                        %>
                                        <form action="reintegrarDevolFact.jsp" method="post">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset2.getString(2)%>">
                                            <button class="btn btn-block btn-info"><span class="glyphicon glyphicon-log-in"></span></button>  
                                        </form>
                                        <%
                                            }
                                        %>
                                    </td>
                                    <%
                                        }
                                    %>
                                    <td>
                                        <%if(F_StsFact.equals("A")){
                                            if(fechaDate1.before(fechaDate2)){                                                
                                                System.out.println("La Fecha 1 es menor "+ rset.getString(1));                             
                                            }else if(fechaDate2.before(fechaDate1)){
                                                System.out.println("La Fecha 1 es Mayor "+ rset.getString(1));
                                            }else{
                                                %>
                                                <a class="btn btn-block btn-info" data-toggle="modal" data-target="#Cancelacion<%=rset.getString(1)%>"><span class="glyphicon glyphicon-trash"></span></a>                                       
                                                <%
                                                System.out.println("Las Fechas Son iguales "+ rset.getString(1));
                                            }
                                        }%>
                                    </td>
                                    <td>
                                        <a class="btn btn-block btn-primary" href="gnrFacturaExcel.jsp?fol_gnkl=<%=rset.getString(1)%>"><span class="glyphicon glyphicon-save"></span></a>
                                    </td>
                                    
                                    <td>
                                        <form action="FacturacionManual" method="post">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString("F_ClaDoc")%>">
                                            <button class="btn btn-block btn-info" name="accion" value="ReenviarFactura" onclick="return confirm('Seguro de Reenviar este concentrado?')"><span class="glyphicon glyphicon-upload"></span></button>

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
        </div>
         
         <!--
                Modal
        -->
        <%
            try {
                con.conectar();
                try {
                    ResultSet rset = con.consulta("SELECT F.F_ClaDoc,F.F_ClaCli,U.F_NomCli,SUM(F_CantSur) AS F_CantSur FROM tb_factura F, tb_uniatn U, tb_obserfact O, tb_fecharuta FR where FR.F_ClaUni = U.F_ClaCli and  F.F_ClaDoc=O.F_IdFact AND F.F_ClaCli=U.F_ClaCli and " + UsuaJuris + " GROUP BY F.F_ClaDoc ORDER BY F.F_ClaDoc+0;");
                    while (rset.next()) {
        %>
        <div class="modal fade" id="Cancelacion<%=rset.getString(1)%>" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog">
                <form action="FacturacionManual">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-5">
                                    Cancelar Folio:
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <input id="IdFact" name="IdFact" value="<%=rset.getString(1)%>" class="hidden">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        Folio Factura: <%=rset.getString(1)%>
                                    </div>
                                    <div class="col-sm-9">
                                        Clave Cliente: <%=rset.getString(2)%>
                                    </div>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Observaciones</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <textarea name="Obser" id="Obser<%=rset.getString(1)%>" class="form-control"></textarea>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Contraseña</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input name="ContraCancel<%=rset.getString(1)%>" id="ContraCancel<%=rset.getString(1)%>" class="form-control" type="password" onkeyup="validaContra(this.id);" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" id="<%=rset.getString(1)%>" disabled onclick="return validaCancel(this.id);" name="accion" value="CancelarFolio">Cancelar</button>
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
                                                $(document).ready(function () {
                                                    $('#datosCompras').dataTable();
                                                });
        </script>
        <script>
            $(function () {
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
