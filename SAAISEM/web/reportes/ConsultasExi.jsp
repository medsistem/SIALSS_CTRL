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

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "", Fecha11 = "";
    int Total = 0;
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
    String fecha_fin = "", clave = "", Descrip = "", Radio = "";
    try {
        fecha_fin = request.getParameter("fecha_fin");
        clave = request.getParameter("clave");
        Descrip = request.getParameter("descripcion");
        Radio = request.getParameter("radio");
    } catch (Exception e) {

    }
    if (Radio == null) {
        Radio = "Clave";
    }

    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (clave == null) {
        clave = "";
    }
    if (Descrip == null) {
        Descrip = "";
    }

    try {
        con.conectar();

        ResultSet RFecha = con.consulta("SELECT DAY(CURDATE()) as dia,MONTH(CURDATE()) AS mes,YEAR(CURDATE()) AS ano,CURDATE() AS fecha;");
        if (RFecha.next()) {
            Fecha11 = RFecha.getString(4);
        }

        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    if ((fecha_fin == "") || (fecha_fin == null)) {
        fecha_fin = Fecha11;
    } else {
        fecha_fin = fecha_fin;
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
        
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf"%>

            <div class="panel-heading">
                <h3 class="panel-title">Consulta Existencias en Cedis</h3>
            </div>
            <form action="ConsultasExi.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="Clave">Clave</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="clave" name="clave" type="text" value="" placeholder="Clave"  />
                        </div>                    
                        <label class="control-label col-sm-1" for="Descripción">Descripción</label>
                        <div class="col-lg-3">
                            <input class="form-control" id="descripcion" name="descripcion" type="text" placeholder="Descripción" />
                        </div>
                        <label class="control-label col-sm-1" for="Fecha">Fecha</label>
                        <div class="col-lg-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" value="<%=fecha_fin%>"/>
                        </div>  
                        <label class="control-label col-sm-1" for="Fecha">Consulta</label>
                        <%if (Radio.equals("Lote")) {%>
                        <div class="col-lg-2">
                            <input id="radio" name="radio" type="Radio" value="Lote" checked="" />Lote
                            <input id="radio" name="radio" type="Radio" value="Clave" />Clave
                        </div>  
                        <%} else if (Radio.equals("Clave")) {%>
                        <div class="col-lg-2">
                            <input id="radio" name="radio" type="Radio" value="Lote" />Lote
                            <input id="radio" name="radio" type="Radio" value="Clave" checked="" />Clave
                        </div>
                        <%}%>
                    </div>
                    <div class="row ">
                        <button class="btn btn-block btn-primary" id="btn_capturar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>

               
                    

                
            </form>
            <%                int Contar = 0;
                try {
                    con.conectar();
                    try {
                        ResultSet rset = null;
                        ResultSet rsetMed = null;
                        String FechaFol = "", Clave = "", Concep = "", Query = "";
                        int ban = 0, ban1 = 0, ban2 = 0;
                        if (clave != "") {
                            ban = 1;
                            Clave = " M.F_ProMov='" + clave + "' ";
                        }
                        if (fecha_fin != "") {
                            ban1 = 1;
                            FechaFol = " M.F_FecMov <= '" + fecha_fin + "' ";
                        }
                        if (Descrip != "") {
                            ban2 = 1;
                            Concep = " MD.F_DesPro = '" + Descrip + "' ";
                        }

                        if (ban == 1 && ban1 == 1) {
                            Query = Clave + " AND " + FechaFol;
                        } else if (ban1 == 1 && ban2 == 1) {
                            Query = FechaFol + " AND " + Concep;
                        } else if (ban == 1) {
                            Query = Clave;
                        } else if (ban1 == 1) {
                            Query = FechaFol;
                        } else if (ban2 == 1) {
                            Query = Concep;
                        }

                        rset = con.consulta("select COUNT(F_ProMov) from tb_movinv M INNER JOIN tb_medica MD ON M.F_ProMov=MD.F_ClaPro WHERE " + Query + ";");
                        if (rset.next()) {
                            Contar = rset.getInt(1);
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }

            %>
             <div class="panel-body">
            <form action="../Facturacion" method="post" id="formCambioFechas">
                <%                    if (Contar > 0) {
                %>
                <div class="row">
                    <div>
                    <input class="form-control" id="clave1" name="clave1" type="hidden" value="<%=clave%>" />
                    <input class="form-control" id="concep1" name="concep1" type="hidden" value="<%=Descrip%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />
                    <input class="form-control" id="fecha_fin1" name="radio1" type="hidden" value="<%=Radio%>" />

                </div>
                <div class="col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrExiCedis.jsp?fecha_fin=<%=fecha_fin%>&clave=<%=clave%>&Descrip=<%=Descrip%>&Radio=<%=Radio%>">Exportar Todo<span class="glyphicon glyphicon-save"></span></a>
                </div>
                <%}%>
                <!--<div class="col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrExiCediSoluciones.jsp">Exportar Soluciones<span class="glyphicon glyphicon-save"></span></a>
                </div>-->
                </div>
                
                    <input class="hidden" name="accion" value="recalendarizarRemis"  />
                    <input class="hidden" id="F_FecEnt" name="F_FecEnt" value=""  />
                    <div class="panel panel-primary">
                        <div class="panel-body">                        
                            <table class="table table-bordered table-striped" id="consultaExi">
                                <thead>
                                    <tr>
                                        <%if (Radio.equals("Clave")) {%>
                                        <th class="col-xs-3">Clave</th>
                                        <th class="col-xs-6">Descripción</th>
                                        <th class="col-xs-3">Cantidad</th>
                                            <%} else {%>
                                        <th class="col-xs-2">Clave</th>
                                        <th class="col-xs-6">Descripción</th>                                            
                                        <th class="col-xs-2">Lote</th>
                                        <th class="col-xs-1">Caducidad</th>                                            
                                        <th class="col-xs-1">Cantidad</th>
                                            <%}%>

                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                String FechaFol = "", Clave = "", Concep = "", Query = "", Cadu = "", Lote = "", TotalMed = "";
                                                int ban = 0, ban1 = 0, ban2 = 0, Existencia = 0;
                                                if (clave != "") {
                                                    ban = 1;
                                                    Clave = " M.F_ProMov='" + clave + "' ";
                                                }
                                                if (fecha_fin != "") {
                                                    ban1 = 1;
                                                    FechaFol = " M.F_FecMov <= '" + fecha_fin + "' ";
                                                }
                                                if (Descrip != "") {
                                                    ban2 = 1;
                                                    Concep = " MD.F_DesPro = '" + Descrip + "' ";
                                                }
                                                if (ban == 1 && ban1 == 1) {
                                                    Query = Clave + " AND " + FechaFol;
                                                } else if (ban1 == 1 && ban2 == 1) {
                                                    Query = FechaFol + " AND " + Concep;
                                                } else if (ban == 1) {
                                                    Query = Clave;
                                                } else if (ban1 == 1) {
                                                    Query = FechaFol;
                                                } else if (ban2 == 1) {
                                                    Query = Concep;
                                                }

                                                ResultSet rset = null;
                                                ResultSet Consulta = null;
                                                ResultSet rsetMed = null;
                                                if (Radio.equals("Lote")) {
                                                    rset = con.consulta("SELECT MD.F_ClaPro,M.F_LotMov,SUBSTRING(MD.F_DesPro, 1, 100),SUM(M.F_CantMov*M.F_SigMov) FROM tb_movinv M INNER JOIN tb_medica MD ON M.F_ProMov=MD.F_ClaPro WHERE " + Query + " GROUP BY M.F_ProMov,M.F_LotMov;");
                                                    while (rset.next()) {
                                                        Existencia = rset.getInt(4);
                                                        Consulta = con.consulta("SELECT DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS F_FecCad,F_ClaLot FROM tb_lote WHERE F_ClaPro='" + rset.getString(1) + "' AND F_FolLot='" + rset.getString(2) + "';");
                                                        if (Consulta.next()) {
                                                            Cadu = Consulta.getString(1);
                                                            Lote = Consulta.getString(2);
                                                        }

                                                        if (Existencia > 0) {
                                                            Total = Total + Existencia;

                                    %>
                                    <tr>                                        
                                        <td class="col-xs-2"><%=rset.getString(1)%></td>
                                        <td class="col-xs-3"><%=rset.getString(3)%></td>                                            
                                        <td class="col-xs-2"><%=Lote%></td>
                                        <td class="col-xs-1"><%=Cadu%></td>                                           
                                        <td class="col-xs-1"><%=formatter.format(Existencia)%></td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    } else if (Radio.equals("Clave")) {
                                        rset = con.consulta("SELECT M.F_ClaPro,SUBSTRING(M.F_DesPro, 1, 100),SUM(F_CantMov*F_SigMov) as Existencia FROM tb_medica M LEFT JOIN tb_movinv MV ON M.F_ClaPro=MV.F_ProMov WHERE M.F_StsPro='A' AND MV.F_FecMov<='" + fecha_fin + "' AND F_ConMov<'1000' GROUP BY M.F_ClaPro;");

                                        while (rset.next()) {
                                            TotalMed = rset.getString(3);

                                            if (TotalMed == null) {
                                                Existencia = 0;
                                            } else if (TotalMed == "") {
                                                Existencia = 0;
                                            } else {
                                                Existencia = Integer.parseInt(TotalMed);
                                                if (Existencia < 0) {
                                                    Existencia = 0;
                                                }
                                            }

                                            Total = Total + Existencia;

                                    %>
                                    <tr>                                        
                                        <td ><%=rset.getString(1)%></td>
                                        <td ><%=rset.getString(2)%></td>                                           
                                        <td ><%=formatter.format(Existencia)%></td>
                                    </tr>
                                    <%

                                        }
                                    } else {
                                       

                                            rset = con.consulta("SELECT MD.F_ClaPro,M.F_LotMov,MD.F_DesPro,SUM(M.F_CantMov*M.F_SigMov) as Existencia FROM tb_movinv M INNER JOIN tb_medica MD ON M.F_ProMov=MD.F_ClaPro WHERE " + Query + " GROUP BY M.F_ProMov;");
                                            while (rset.next()) {
                                                Existencia = rset.getInt(4);
                                                if (Existencia <= 0) {
                                                    Existencia = 0;
                                                }
                                                Total = Total + Existencia;
                                    %>
                                    <tr>                                        
                                        <td class="col-xs-3"><%=rset.getString(1)%></td>
                                        <td class="col-xs-3"><%=rset.getString(3)%></td>                                           
                                        <td class="col-xs-3"><%=formatter.format(Existencia)%></td>
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
                    
                <h3>Existencia:&nbsp; <%=formatter.format(Total)%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Fecha Consulta:&nbsp;<%=fecha_fin%></h3>
            </form>
        </div>
        </div>
    </body>
</html>         




        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-2.1.4.min.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/jquery-ui-1.10.3.custom.js"></script>
<script src="../js/bootstrap-datepicker.js"></script>
<script src="../js/jquery.dataTables.js"></script>
<script src="../js/dataTables.bootstrap.js"></script>
        
        

        <script>
            $(document).ready(function() {
                $('#consultaExi').dataTable();
                
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

   

