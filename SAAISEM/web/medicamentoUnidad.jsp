<%-- 
    Document   : requerimiento.jsp
    Created on : 17/02/2014, 03:34:46 PM
    Author     : MEDALFA
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
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
    String SelectUnidad = "";

    SelectUnidad = request.getParameter("SelectUnidad");

    if (SelectUnidad == null) {
        SelectUnidad = "";
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
        <link rel="stylesheet" type="text/css" href="css/select2.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <hr/>
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">Catalogo de Insumo por Unidad</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="medicamentoUnidad.jsp">
                        <div class="row">
                            <div class="form-group">
                                <label for="Clave" class="col-xs-1 control-label">Unidad</label>
                                <div class="col-sm-6">
                                    <select name="SelectUnidad" id="SelectUnidad" class="form-control">
                                        <option value="0">--Seleccione--</option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = null;
                                                rset = con.consulta("SELECT F_ClaCli ,F_NomCli FROM tb_uniatn ;");
                                                while (rset.next()) {
                                        %>
                                        <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                System.out.println(e.getMessage());
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <button name="btnUnidad" id="btnUnidad" class="btn btn-block btn-warning form-control">Por Unidad</button>
                                </div>
                                <%if (!(SelectUnidad.equals("0"))) {%>
                                <div class="col-sm-2">
                                    <a href="gnrMedUnidad.jsp?Unidad=<%=SelectUnidad%>" target="_black" class="btn btn-block btn-success form-control">Exportar</a>
                                </div>
                                <%}%>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="panel-footer">
                    <table class="table table-striped table-bordered" id="datosMedicamento">
                        <thead>
                            <tr>
                                <td>Unidad</td>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Tipo Medicamento</td>
                                <td>Autorizado</td>
                                <td>CPM</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                        rset = con.consulta("SELECT U.F_NomCli, M.F_ClaPro, MD.F_DesPro, CASE WHEN F_TipMed = '2505' THEN 'MAT. CURACION' ELSE 'MEDICAMENTO' END AS F_TipMed, CASE WHEN M.F_Autorizado = '1' THEN 'SI' ELSE 'NO' END AS F_Autorizado, M.F_CantMax FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_Autorizado != 2 AND U.F_Proyecto = 2 AND M.F_ClaUni = '" + SelectUnidad + "';");
                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><small><%=rset.getString(1)%></small></td>
                                <td><small><%=rset.getString(2)%></small></td>
                                <td><small><%=rset.getString(3)%></small></td>
                                <td><small><%=rset.getString(4)%></small></td>
                                <td><small><%=rset.getString(5)%></small></td>
                                <td><small><%=rset.getString(6)%></small></td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/select2.js"></script>
        <script>
            $(document).ready(function () {
                $('#datosMedicamento').dataTable();
                $('#SelectUnidad').select2();
            });
        </script>
    </body>
</html>



