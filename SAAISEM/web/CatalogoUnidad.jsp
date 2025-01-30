<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="conn.ConectionDB"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();

    Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema = formateador.format(fechaActual);
    String Id = "", Unidad = "", Unidad1 = "", usua = "", tipo = "", AccionS = "", AccionA = "", AccionST = "", AccionAT = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        Unidad = request.getParameter("Unidad");
        Unidad1 = request.getParameter("Unidad1");
        Id = request.getParameter("Id");
        AccionS = request.getParameter("AccionS");
        AccionA = request.getParameter("AccionA");
        AccionST = request.getParameter("AccionST");
        AccionAT = request.getParameter("AccionAT");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    if (Unidad == null) {
        Unidad = "";
    }
    if (Unidad1 == null) {
        Unidad1 = "";
    }
    if (Id == null) {
        Id = "";
    }
    if (AccionA == null) {
        AccionA = "";
    }
    if (AccionS == null) {
        AccionS = "";
    }
    if (AccionAT == null) {
        AccionAT = "";
    }
    if (AccionST == null) {
        AccionST = "";
    }
    if (!(Unidad.equals("0"))) {
        Unidad = Unidad;
    } else if (Unidad1 != "") {
        Unidad = Unidad1;
    }

    ConectionDB con = new ConectionDB();

    //System.out.println("S = " + AccionST + " A = " + AccionAT);
    try {
        con.conectar();
        if (!(AccionS.equals(""))) {
            con.actualizar("UPDATE tb_medicaunidad SET F_Autorizado = 0 WHERE F_ClaUni = '" + Unidad + "' AND F_Id = '" + AccionS + "';");
        } else if (!(AccionA.equals(""))) {
            con.actualizar("UPDATE tb_medicaunidad SET F_Autorizado = 1 WHERE F_ClaUni = '" + Unidad + "' AND F_Id = '" + AccionA + "';");
        } else if (!(AccionAT.equals(""))) {
            con.actualizar("UPDATE tb_medicaunidad SET F_Autorizado = 1 WHERE F_ClaUni = '" + Unidad + "';");
        } else if (!(AccionST.equals(""))) {
            con.actualizar("UPDATE tb_medicaunidad SET F_Autorizado = 0 WHERE F_ClaUni = '" + Unidad + "';");
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
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
        <link rel="stylesheet" href="css/select2.css" />
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIAALS</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="row">
                <h4 class="col-sm-3">Catálogo por Unidad</h4>
            </div>
            <form name="forma1" id="forma1" action="CatalogoUnidad.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Unidad</label>
                        <div class="col-sm-6">
                            <select name="Unidad" id="Unidad" class="form-control">
                                <option value="0">--Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT M.F_ClaUni, CONCAT( M.F_ClaUni, ' - ', U.F_NomCli ) AS F_NomCli FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli WHERE U.F_Proyecto = 2 AND M.F_Tipo !='RURAL' GROUP BY M.F_ClaUni;");
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
                    </div>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-primary" name="Accion" value="mostrar" >MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                        </div>
                        <div class="col-sm-2">
                            <a class="btn btn-block btn-primary" href="CatalogoUnidadgnr.jsp?Unidad=<%=Unidad%>" target="_blank">DESCARGAR&nbsp;1x1<label class="glyphicon glyphicon-download"></label></a>
                        </div>
                        <div class="col-sm-2">
                            <a class="btn btn-block btn-info" href="CatalogoUnidadgnr.jsp?Unidad=" target="_blank">DESCARGAR&nbsp;Todo<label class="glyphicon glyphicon-download"></label></a>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-info" name="AccionAT" value="AccionAT">Activar Todas</button>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-info" name="AccionST" value="AccionST">Suspender Todas</button>
                        </div>
                    </div>
                </div>  
                <div>
                    <br />
                    <%
                        int Total = 0, Medicamento = 0, MaterialC = 0;
                        try {
                            con.conectar();
                            ResultSet rset = con.consulta("SELECT COUNT(*), COUNT( IF (MD.F_TipMed = 2504, 1, NULL)) AS MED, COUNT( IF (MD.F_TipMed = 2505, 1, NULL)) AS MAT FROM tb_medicaunidad M INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_ClaUni = '" + Unidad + "' AND M.F_Autorizado = 1;");
                            while (rset.next()) {
                                Total = rset.getInt(1);
                                Medicamento = rset.getInt(2);
                                MaterialC = rset.getInt(3);
                            }
                            con.cierraConexion();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    %>
                    <label>Total Insumos = <%=Total%>&nbsp;&nbsp;&nbsp;Medicamento = <%=Medicamento%>&nbsp;&nbsp;&nbsp;&nbsp;Material Curación = <%=MaterialC%></label>
                    <div class="panel panel-info">
                        <input type="hidden" class="form-control" name="Unidad1" id="Unidad1" value="<%=Unidad%>" />
                        <div class="panel-body table-responsive">
                            <table class="table table-bordered table-striped" id="datosCompras">
                                <thead>
                                    <tr>
                                        <td>Clave Unidad</td>
                                        <td>Nombre Unidad</td>
                                        <td>Clave</td>
                                        <td>Descripción</td>
                                        <td>Cantidad</td>
                                        <td>Autorizado</td>
                                        <td>Modificar</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = con.consulta("SELECT U.F_ClaCli, U.F_NomCli, M.F_ClaPro, MD.F_DesPro, M.F_CantMax, CASE WHEN M.F_Autorizado = 1 THEN 'SI' ELSE 'NO' END AS STS, F_Id, F_Autorizado FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_ClaUni='" + Unidad + "';");
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
                                            <%if (rset.getInt(8) == 1) {%>
                                            <button class="btn btn-block btn-info" name="AccionS" value="<%=rset.getString(7)%>">Suspender</button>
                                            <%} else {%>
                                            <button class="btn btn-block btn-info" name="AccionA" value="<%=rset.getString(7)%>">Activar</button>
                                            <%}%>

                                        </td>
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
            </form>
        </div>
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
        <script src="js/select2.js"></script>
        <script>
            $(document).ready(function () {
                $('#datosCompras').dataTable();
                $('#Unidad').select2();
            });
        </script>
    </body>
</html>
