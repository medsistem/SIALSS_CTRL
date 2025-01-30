<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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

<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
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
    ConectionDB_Linux Lerma = new ConectionDB_Linux();

    int TotalLerma = 0, TotalSend = 0, TotalDife = 0;
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
        <link rel="stylesheet" type="text/css" href="../css/table-fixed.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf"%>

            <div class="panel-heading">
                <h3 class="panel-title">Consulta Folios Lerma vs. Sendero</h3>
            </div>
            <form action="../Facturacion" method="post" id="formCambioFechas">
                <div class="row col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrFolioEnt.jsp">Exportar Global<span class="glyphicon glyphicon-save"></span></a>
                </div>                
                <div>                    
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <table class="table  table-fixed">
                                <thead>
                                    <tr>
                                        <th class="col-xs-2">Fec. Ela. Lerma</th>
                                        <th class="col-xs-2">Fec. Ent. Lerma</th>
                                        <th class="col-xs-2">Fec. Ing. Sendero</th>
                                        <th class="col-xs-2">Fol. Lerma</th>                                            
                                        <th class="col-xs-1">Cant. Lerma</th>
                                        <th class="col-xs-1">Cant. Sendero</th>                                            
                                        <th class="col-xs-1">Diferencias</th> 
                                        <th class="col-xs-1">Descargar</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%                                            try {
                                            String Recibo = "", TRecibo = "", FechaCap = "", TFechaCap = "", Clave = "";
                                            con.conectar();
                                            Lerma.conectar();
                                            ResultSet Consulta = null;
                                            ResultSet ConsultaF = null;
                                            ResultSet ConsultaLerma = null;

                                            con.actualizar("DELETE FROM tb_folioentrega;");

                                            ConsultaLerma = Lerma.consulta("SELECT DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F_ClaDoc,F_ClaPro,SUM(F_CantSur) AS F_CantSur FROM tb_factura WHERE F_ClaCli='S001' AND F_FecApl>'2016-04-30' AND F_StsFact='A' GROUP BY F_ClaDoc,F_ClaPro,F_FecApl,F_FecEnt;");
                                            while (ConsultaLerma.next()) {
                                                Clave = ConsultaLerma.getString(4);
                                                if (Clave.equals("0801.01")) {
                                                    Clave = "801.01";
                                                }
                                                con.insertar("INSERT INTO tb_folioentrega VALUES('" + ConsultaLerma.getString(1) + "','" + ConsultaLerma.getString(2) + "','" + ConsultaLerma.getString(3) + "','" + Clave + "','" + ConsultaLerma.getString(5) + "','0','',0)");
                                                Clave = "";
                                            }

                                            Consulta = con.consulta("SELECT F_ClaDoc,F_ClaPro FROM tb_folioentrega;");
                                            while (Consulta.next()) {
                                                ConsultaF = con.consulta("SELECT SUM(F_CanCom),DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl FROM tb_compra WHERE F_FolRemi='" + Consulta.getString(1) + "' AND F_ClaPro='" + Consulta.getString(2) + "';");
                                                if (ConsultaF.next()) {
                                                    Recibo = ConsultaF.getString(1);
                                                    FechaCap = ConsultaF.getString(2);
                                                }
                                                if (Recibo == "") {
                                                    TRecibo = "0";
                                                } else if (Recibo == null) {
                                                    TRecibo = "0";
                                                } else {
                                                    TRecibo = Recibo;
                                                }

                                                if (FechaCap == null) {
                                                    TFechaCap = "";
                                                } else {
                                                    TFechaCap = FechaCap;
                                                }

                                                con.actualizar("UPDATE tb_folioentrega SET F_CantSen='" + TRecibo + "',F_FeCap='" + TFechaCap + "' WHERE F_ClaDoc='" + Consulta.getString(1) + "' AND F_ClaPro='" + Consulta.getString(2) + "';");
                                                Recibo = "";
                                                TRecibo = "";
                                                FechaCap = "";
                                                TFechaCap = "";
                                            }

                                            Consulta = con.consulta("SELECT F_FecApl,F_FecEnt,F_FeCap,F_ClaDoc,SUM(F_CantLerma) AS F_CantLerma,SUM(F_CantSen) AS F_CantSen,(SUM(F_CantLerma)-SUM(F_CantSen)) AS F_Dife FROM tb_folioentrega GROUP BY F_ClaDoc ORDER BY F_ClaDoc+0;");
                                            while (Consulta.next()) {
                                                TotalLerma = TotalLerma + Consulta.getInt(5);
                                                TotalSend = TotalSend + Consulta.getInt(6);
                                                TotalDife = TotalDife + Consulta.getInt(7);
                                    %>
                                    <tr>                                        
                                        <td class="col-xs-2"><%=Consulta.getString(1)%></td>
                                        <td class="col-xs-2"><%=Consulta.getString(2)%></td>
                                        <td class="col-xs-2"><%=Consulta.getString(3)%></td>
                                        <td class="col-xs-2"><%=Consulta.getString(4)%></td>
                                        <td class="col-xs-1"><%=formatter.format(Consulta.getInt(5))%></td>                                            
                                        <td class="col-xs-1"><%=formatter.format(Consulta.getInt(6))%></td>
                                        <td class="col-xs-1"><%=formatter.format(Consulta.getInt(7))%></td>
                                        <td class="col-xs-1"><a class="btn btn-block btn-info" href="gnrFolioEntD.jsp?Docu=<%=Consulta.getString(4)%>"><span class="glyphicon glyphicon-save"></span></a></td>         
                                    </tr>
                                    <%
                                            }
                                            con.cierraConexion();
                                            Lerma.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }

                                    %>
                                    <tr>
                                        <td class="col-xs-8" style="text-align: right; "><strong>Total Piezas Lerma Vs. Sendero</strong></td>
                                        <td class="col-xs-1"><%=formatter.format(TotalLerma)%></td>
                                        <td class="col-xs-1"><%=formatter.format(TotalSend)%></td>
                                        <td class="col-xs-1"><%=formatter.format(TotalDife)%></td>
                                        <td class="col-xs-1"></td>
                                    </tr>
                                </tbody>
                            </table>
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
        <script src="../js/bootstrap3-typeahead.js" type="text/javascript"></script>
        <script>
            $(document).ready(function() {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });

        </script>
        <script>
            $(document).ready(function() {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });

        </script>
        <script>
            $(document).ready(function() {

                $("#clave").typeahead({
                    source: function(request, response) {

                        $.ajax({
                            url: "../AutoCompleteMedicamentos",
                            dataType: "json",
                            data: request,
                            success: function(data, textStatus, jqXHR) {
                                //console.log(data);
                                var items = data;
                                response(items);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }

                });
                $("#descripcion").typeahead({
                    source: function(request, response) {

                        $.ajax({
                            url: "../AutoCompleteMedicamentosDesc",
                            dataType: "json",
                            data: request,
                            success: function(data, textStatus, jqXHR) {
                                //console.log(data);
                                var items = data;
                                response(items);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }

                });

            });
        </script>

    </body>
</html>

