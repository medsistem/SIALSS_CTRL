<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "", Proyec = "", DesProyecto = "";
    int Proyecto = 0;
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        Proyec = request.getParameter("Proyecto");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (Proyec == null) {
        Proyec = "0";
    }
    Proyecto = Integer.parseInt(Proyec);
    try {
        con.conectar();
        ResultSet rset = con.consulta("SELECT F_DesProy FROM tb_proyectos WHERE F_Id='" + Proyecto + "' ;");
        while (rset.next()) {
            DesProyecto = rset.getString(1);
        }
        con.cierraConexion();
    } catch (Exception e) {
        out.println(e.getMessage());
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

            <%@include file="../jspf/menuPrincipalCompra.jspf"%>

            <div class="panel-heading">
                <h3 class="panel-title">Reporte Back Order List</h3>
            </div>
            <form action="BackOrderList.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Fecha</label>
                        <div class="col-lg-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" />
                        </div>
                        <div class="col-lg-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date"/>
                        </div>
                        <div class="col-sm-3">
                            <select class="form-control" name="Proyecto" id="Proyecto">
                                <option value="0">-Seleccione Proyecto-</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT * FROM tb_proyectos WHERE F_Id = 2;");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        out.println(e.getMessage());
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="row ">
                        <button class="btn btn-block btn-primary" id="btn_capturar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>

                </div>  
            </form>
            <%
                int Contar = 0;
                try {
                    con.conectar();
                    try {
                        String FechaFol = "", Clave = "", Concep = "", Query = "";
                        int ban = 0, ban1 = 0, ban2 = 0;

                        if (fecha_ini != "" && fecha_fin != "") {
                            ban1 = 1;
                            FechaFol = " AND F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' ";
                        }
                        if (Proyecto > 0) {
                            ban2 = 1;
                            if (Proyecto == 2) {
                                Concep = " AND U.F_Proyecto = '" + Proyecto + "' ";
                            } else {
                                Concep = " AND F.F_Proyecto = '" + Proyecto + "' ";
                            }
                        }

                        if (ban1 == 1 && ban2 == 1) {
                            Query = FechaFol + Concep;
                        } else if (ban1 == 1) {
                            Query = FechaFol;
                        } else if (ban2 == 1) {
                            Query = Concep;
                        }

                        if (Query != "") {
                            ResultSet rset = con.consulta("SELECT COUNT(F.F_ClaPro) FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto = P.F_Id INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_StsFact = 'A' " + Query + ";");
                            if (rset.next()) {
                                Contar = rset.getInt(1);
                            }
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }

            %>
            <form action="../Facturacion" method="post" id="formCambioFechas">
                <%                    if (Contar > 0) {
                %>
                <div class="row col-sm-6">
                    <a class="btn btn-block btn-info" href="BackOrderListgnr.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>&Proyecto=<%=Proyecto%>&DesProyecto=<%=DesProyecto%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
                </div>
                <%}%>


                <div>
                    <input class="hidden" name="accion" value="recalendarizarRemis"  />
                    <input class="hidden" id="F_FecEnt" name="F_FecEnt" value=""  />
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <td>Proyecto</td>
                                            <td>Clave</td>
                                            <td>Descripción</td>
                                            <td>Requerido</td>
                                            <td>Surtido</td>
                                            <td>Pendiente</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            try {
                                                con.conectar();
                                                try {

                                                    String FechaFol = "", Clave = "", Concep = "", Query = "", AND = "", ANDOrigen = "";
                                                    int ban = 0, ban1 = 0, ban2 = 0;

                                                    if (fecha_ini != "" && fecha_fin != "") {
                                                        ban1 = 1;
                                                        FechaFol = " AND F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' ";
                                                    }
                                                    if (Proyecto > 0) {
                                                        ban2 = 1;
                                                        if (Proyecto == 2) {
                                                            Concep = " AND U.F_Proyecto = '" + Proyecto + "' ";
                                                        } else {
                                                            Concep = " AND F.F_Proyecto = '" + Proyecto + "' ";
                                                        }
                                                    }

                                                    if (ban1 == 1 && ban2 == 1) {
                                                        Query = FechaFol + Concep;
                                                    } else if (ban1 == 1) {
                                                        Query = FechaFol;
                                                    } else if (ban2 == 1) {
                                                        Query = Concep;
                                                    }
                                                    if (Query != "") {
                                                        ResultSet rset = con.consulta("SELECT P.F_DesProy, F.F_ClaPro, M.F_DesPro, FORMAT(SUM(F.F_CantReq), 0) AS F_CantReq, FORMAT(SUM(F.F_CantSur), 0) AS F_CantSur, FORMAT( SUM(F.F_CantReq) - SUM(F.F_CantSur), 0 ) AS FALTANTE FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto = P.F_Id INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_StsFact = 'A' " + Query + " GROUP BY F.F_ClaPro, F.F_Proyecto;");
                                                        while (rset.next()) {

                                        %>
                                        <tr>
                                            <td><%=rset.getString(1)%></td>
                                            <td><%=rset.getString(2)%></td>
                                            <td><%=rset.getString(3)%></td>
                                            <td><%=rset.getString(4)%></td>
                                            <td><%=rset.getString(5)%></td>
                                            <td><%=rset.getString(6)%></td>
                                        </tr>
                                        <%
                                                        }
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
            </form>
        </div>




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
            $(document).ready(function () {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });

        </script>
        <script>
            $(document).ready(function () {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });

        </script>
        <script>
            $(document).ready(function () {
                $('#ClaCli').change(function () {
                    var Concep = $('#conceptos').val();
                    var valor = $('#ClaCli').val();
                    if (Concep != "") {
                        $('#conceptos').val(Concep + "," + valor);
                    } else {
                        $('#conceptos').val(valor);
                    }
                });
            });
        </script>

    </body>
</html>

