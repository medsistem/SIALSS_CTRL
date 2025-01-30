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
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

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
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div>
                <h3>Devoluciones</h3>
                <h4>Folio de Factura: <%=request.getParameter("fol_gnkl")%></h4>
                <%
                    try {
                        con.conectar();
                        try {
                            ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantReq) as requerido,SUM(F.F_CantSur) as surtido,F.F_Costo,SUM(F.F_Monto) as importe, F.F_Ubicacion FROM tb_factdevol F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' GROUP BY F.F_ClaDoc");
                            while (rset.next()) {


                %>
                <h4>Cliente: <%=rset.getString(1)%></h4>
                <h4>Fecha de Entrega: <%=rset.getString(2)%></h4>
                <h4>Factura: <%=rset.getString(3)%></h4>
                <%
                    int req = 0, sur = 0;
                    Double imp = 0.0;
                    ResultSet rset2 = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,(F.F_CantSur) as surtido,(F.F_CantReq) as requerido,F.F_Costo,(F.F_Monto) as importe, F.F_Ubicacion FROM tb_factdevol F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' GROUP BY U.F_NomCli,F.F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,L.F_FecCad,F.F_CantReq,F.F_CantSur,F.F_Costo,F.F_Monto");
                    while (rset2.next()) {
                        req = req + rset2.getInt("requerido");
                        sur = sur + rset2.getInt("surtido");
                        imp = imp + rset2.getDouble("importe");
                        System.out.println(req);
                    }
                    int banReint = 0;
                    rset2 = con.consulta("select F_FactSts from tb_factdevol where F_ClaDoc = '" + request.getParameter("fol_gnkl") + "' and F_FactSts=0");
                    while (rset2.next()) {
                        banReint = 1;
                    }
                %>

                <div class="row">
                    <h5 class="col-sm-3">Total Solicitado: <%=formatter.format(req)%></h5>
                    <h5 class="col-sm-3">Total Surtido: <%=formatter.format(sur)%></h5>
                    <h5 class="col-sm-3">Total Importe: $ <%=formatterDecimal.format(imp)%></h5>
                    <a href="reimpReintegraInventario.jsp?fol_gnkl=<%=request.getParameter("fol_gnkl")%>" target="_blank" class="btn btn-info"><span class="glyphicon glyphicon-print"></span></a>
                    <a href="reimp_factura.jsp" class="btn btn-default">Regresar</a>
                    <%
                        if (banReint == 1) {
                    %>
                    <a href="FacturacionManual?accion=reintegrarInsumo&F_ClaDoc=<%=request.getParameter("fol_gnkl")%>" class="btn btn-primary" onclick="return confirm('Seguro que desea reintegrar el insumo a inventario?')">Reintegrar Insumo</a>
                    <%
                        }
                    %>
                </div>
                <%
                            }
                        } catch (Exception e) {

                        }
                        con.cierraConexion();
                    } catch (Exception e) {

                    }
                %>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <td>Clave</td>
                                    <td>Descripción</td>
                                    <td>Lote</td>
                                    <td>Caducidad</td>
                                    <td>Req.</td>
                                    <td>Ubicación</td>
                                    <td>Ent.</td>
                                    <td>Costo U</td>
                                    <td>Importe</td>
                                    <td>Reint</td>
                                    <td></td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,F.F_CantReq,F.F_CantSur,F.F_Costo,F.F_Monto, F.F_Ubicacion, F.F_IdFact, F.F_StsFact, F.F_Obs, F.F_FactSts FROM tb_factdevol F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' GROUP BY F.F_IdFact");
                                            while (rset.next()) {
                                %>
                                <tr>
                                    <td><%=rset.getString(4)%></td>
                                    <td><%=rset.getString(5)%></td>
                                    <td><%=rset.getString(6)%></td>
                                    <td><%=rset.getString(7)%></td>
                                    <td><%=rset.getString(8)%></td>
                                    <td><%=rset.getString(12)%></td>
                                    <td><%=rset.getString(9)%></td>
                                    <td><%=rset.getString(10)%></td>
                                    <td><%=rset.getString(11)%></td>
                                    <td><%=rset.getString("F_FactSts")%></td>
                                    <td>
                                        <%
                                            if (rset.getString("F_StsFact").equals("A")) {
                                        %>
                                        <a class="btn btn-block btn-info" data-toggle="modal" data-target="#Devolucion<%=rset.getString("F_IdFact")%>"><span class="glyphicon glyphicon-remove-circle"></span></a>
                                            <%
                                            } else {
                                            %>
                                        <a href="#" title="<%=rset.getString("F_Obs")%>">Observaciones</a>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <%
                                            }
                                        } catch (Exception e) {

                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {

                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <br><br><br>
        <%@include file="jspf/piePagina.jspf" %>



        <!--
                Modal
        -->
        <%
            try {
                con.conectar();
                try {
                    ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,F.F_CantReq,F.F_CantSur,F.F_Costo,F.F_Monto, F.F_Ubicacion, F.F_IdFact FROM tb_factdevol F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' GROUP BY F.F_IdFact");
                    while (rset.next()) {
        %>
        <div class="modal fade" id="Devolucion<%=rset.getString("F_IdFact")%>" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog">
                <form action="FacturacionManual">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-5">
                                    Devolución:
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <input id="IdFact" name="IdFact" value="<%=rset.getString("F_IdFact")%>" class="hidden">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        Clave: <%=rset.getString("F_ClaPro")%>
                                    </div>
                                    <div class="col-sm-9">
                                        Descripción: <%=rset.getString("F_DesPro")%>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Cantidad a Devolver:<%=rset.getString("F_CantSur")%></h4>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Observaciones</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <textarea name="Obser" id="Obser<%=rset.getString("F_IdFact")%>" class="form-control"></textarea>
                                </div>
                            </div>
                            <h4 class="modal-title" id="myModalLabel">Contraseña</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input name="ContraDevo<%=rset.getString("F_IdFact")%>" id="ContraDevo<%=rset.getString("F_IdFact")%>" class="form-control" type="password" onkeyup="validaContra(this.id);" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" id="<%=rset.getString("F_IdFact")%>" disabled onclick="return validaDevolucion(this.id);" name="accion" value="devolucion">Devolver</button>
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
</html>


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

    function validaDevolucion(e) {
        var id = e;
        if (document.getElementById('Obser' + id).value === "") {
            alert("Ingrese las observaciones de la devolución")
            return false;
        }
    }

    function validaContra(elemento) {
        //alert(elemento);
        var pass = document.getElementById(elemento).value;
        var id = elemento.split("ContraDevo");
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