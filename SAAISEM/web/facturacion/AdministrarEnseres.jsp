<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
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
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>

            <div class="row">
                <h3 class="col-sm-4">Administrar Movimientos</h3>
                <div class="col-sm-1 col-sm-offset-5">
                    <br/>
                </div>
                <div class="col-sm-2">
                    <br/>
                    <!--a class="btn btn-primary" href="../gnrFacturaConcentrado.jsp" target="_blank">Exportar Global</a-->
                </div>
            </div>
            <div>
                <br />
                <div class="panel panel-info">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <!--td></td-->
                                    <td>No. Folio</td>
                                    <td>Unidad</td>
                                    <td>Nombre de Entrega</td>
                                    <td>Fecha de Entrega</td>
                                    <td>Sts</td>
                                    <td>Proyecto</td>
                                    <td>Generar</td>                                    
                                    
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            ResultSet rset = con.consulta("SELECT F_Folio, E.F_ClaCli, U.F_NomCli, DATE_FORMAT( E.F_FechaEntrega, '%d/%m/%Y' ) AS F_FechaEntrega, F_Sts, P.F_DesProy, E.F_Proyecto FROM tb_enseresfactura E INNER JOIN tb_uniatn U ON E.F_ClaCli = U.F_ClaCli LEFT JOIN tb_proyectos P ON E.F_Proyecto = P.F_Id GROUP BY E.F_ClaCli, F_Folio, F_Sts, E.F_Proyecto ORDER BY F_Folio + 0;");
                                            while (rset.next()) {

                                %>
                                <tr>
                                    
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <td><%=rset.getString(4)%></td>
                                    <td><%=rset.getString(5)%></td>
                                    <td><%=rset.getString(6)%></td>
                                    <td>
                                        <form action="../reportes/reimpreEnseres.jsp" target="_blank">
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <input class="hidden" name="proyecto" value="<%=rset.getString(7)%>">
                                            <button class="btn btn-block btn-primary"><span class="glyphicon glyphicon-print"></span></button>
                                        </form>
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
        <%@include file="../jspf/piePagina.jspf" %>

        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
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
        </script>
    </body>
</html>
