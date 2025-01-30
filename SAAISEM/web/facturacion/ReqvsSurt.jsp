<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%DecimalFormat format = new DecimalFormat("####,###");%>
<%DecimalFormat Decilmal = new DecimalFormat("####0.00");%>
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

    String fecha_ini = "", fecha_fin = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
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

            <div class="panel-heading">
                <h3 class="panel-title">Productos Negados por Tipo de Unidad Proyecto Michoacán</h3>
            </div>
            <form action="ReqvsSurt.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                        </div>
                    </div>   
                </div>

                <div class="panel-body">
                    <div class="row">
                        <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
            <%
                int Contar = 0;
                try {
                    con.conectar();
                    try {
                        ResultSet rset = null;
                        if (fecha_ini != "" && fecha_fin != "") {
                            rset = con.consulta("SELECT SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur FROM tb_factura F WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A';");

                            if (rset.next()) {
                                Contar = rset.getInt(1);
                            }
                        } else {
                            Contar = 0;
                        }

                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }

            %>

            <%                if (Contar > 0) {
            %>
            <div class="row">
                <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />   
                <div class="col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrNivelAbastoDiario.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>">Exportar Nivel Abasto<span class="glyphicon glyphicon-save"></span></a>
                </div>
                <div class="col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrReporteAbastoDiario.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>">Exportar Reporte Abasto<span class="glyphicon glyphicon-save"></span></a>
                </div>
                <br />
            </div>
            <%}%>
            <div>
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <div style="width:100%; height:400px; overflow:auto;">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <td>Nivel</td>
                                        <td>No. Unidades</td>
                                        <td>Requerido</td>
                                        <td>Surtido</td>
                                        <td>No. Surtido</td>
                                        <td>Fill Ride</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                ResultSet rset = null;
                                                int CantReq = 0, CantSur = 0, Diferencia = 0, Unidades = 0;
                                                if (fecha_ini != "" && fecha_fin != "") {
                                                    rset = con.consulta("SELECT U.F_Tipo, CONT.CONTAR, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO, ROUND((( SUM(F.F_CantSur) / (SUM(F.F_CantReq))) * 100 ), 2 ) AS Porcentaje FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN ( SELECT U.F_Tipo, COUNT(DISTINCT TIPO.F_ClaCli) AS CONTAR FROM tb_uniatn U INNER JOIN ( SELECT U.F_Tipo, U.F_ClaCli FROM tb_factura F LEFT JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_ClaCli ) AS TIPO ON U.F_Tipo = TIPO.F_Tipo GROUP BY U.F_Tipo ) AS CONT ON U.F_Tipo = CONT.F_Tipo WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_Tipo;");

                                                    while (rset.next()) {
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=format.format(rset.getInt(2))%></td>
                                        <td><%=format.format(rset.getInt(3))%></td>
                                        <td><%=format.format(rset.getInt(4))%></td>
                                        <td><%=format.format(rset.getInt(5))%></td>
                                        <td><%=rset.getString(6)%>%</td>
                                    </tr>
                                    <%
                                            Unidades = Unidades + rset.getInt(2);
                                            CantReq = CantReq + rset.getInt(3);
                                            CantSur = CantSur + rset.getInt(4);
                                            Diferencia = Diferencia + rset.getInt(5);
                                        }
                                    %>
                                    <tr>
                                        <td>Total</td>
                                        <td><%=format.format(Unidades)%></td>
                                        <td><%=format.format(CantReq)%></td>
                                        <td><%=format.format(CantSur)%></td>
                                        <td><%=format.format(Diferencia)%></td>
                                        <td><%=Decilmal.format(((float) CantSur / CantReq) * 100)%>%</td>
                                    </tr>
                                    <%
                                                    CantReq = 0;
                                                    CantSur = 0;
                                                    Unidades = 0;
                                                    Diferencia = 0;
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
                            $(document).ready(function () {
                                $('#datosCompras').dataTable();
                                $("#fecha").datepicker();
                                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                                //$('#btnRecalendarizar').attr('disabled', true);
                                //$('#btnImpMult').attr('disabled', true);
                            });
        </script>
        <script>

        </script>
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset = con.consulta("SELECT F_NomCli FROM tb_uniatn");
                        while (rset.next()) {
                            out.println("'" + rset.getString(1) + "',");
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
                ];
                $("#NombreUnidad").autocomplete({
                    source: availableTags
                });
            });
        </script>        
        <script>
            $(document).ready(function () {
                $('#selectjur').change(function () {
                    var Juris = $('#juris').val();
                    var valor = $('#selectjur').val();
                    if (Juris != "") {
                        $('#juris').val(Juris + "," + valor);
                    } else {
                        $('#juris').val(valor);
                    }
                });
            });
        </script>
    </body>
</html>

