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

        String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "", fecha_ini = "", fecha_fin = "", folio1 = "", folio2 = "";
        try {
            if (request.getParameter("accion").equals("buscar")) {
                //fol_gnkl = request.getParameter("fol_gnkl");
                // fol_remi = request.getParameter("fol_remi");
                //orden_compra = request.getParameter("orden_compra");
                //fecha = request.getParameter("fecha");
                fecha_ini = request.getParameter("fecha_ini");
                fecha_fin = request.getParameter("fecha_fin");
                folio1 = request.getParameter("folio1");
                folio2 = request.getParameter("folio2");
            }
                } catch (Exception e) {

                }
                if (fecha_ini == null) {
                    fecha_ini = "";
                }
                if (fecha_fin == null) {
                    fecha_fin = "";
                }
                if (folio1 == null) {
                    folio1 = "";
                }
                if (folio2 == null) {
                    folio2 = "";
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

            </div>
            <form action="reimp_movimientos.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="Folio_ini">Folios</label>
                        <div class="col-lg-1">
                            <input class="form-control" id="folio1" name="folio1" type="text" value="" onchange="habilitar(this.value);" />
                        </div>
                        <div class="col-lg-1">
                            <input class="form-control" id="folio2" name="folio2" type="text" value="" onchange="habilitar(this.value);"/>
                        </div>

                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2 bottom-left">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                        </div>

                        <div class="col-sm-2 ">
                            <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();" value="buscar" name="accion">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                        </div>

                    </div>   
                </div>

</form>
                <div>
                    <br />
                    <div class="panel panel-info">
                        <div class="panel-body table-responsive">
                            <table class="table table-bordered table-striped" id="datosCompras">
                                <thead>
                                    <tr>
                                        <!--td></td-->
                                        <th>No. Folio</th>
                                        <th>Punto de Entrega</th>
                                        <th>Fecha de Entrega</th>
                                        <th>Folio</th>
                                        <th>Exportar Ubicación</th>                                    

                                    </tr>
                                </thead>
                                <tbody>
<%
    try {
        con.conectar();
        try {

            String QFolio = "", QFecha = "", Query = "";

            int ban = 0, ban2 = 0, ban3 = 0;

            ResultSet rset;

            if (folio1 != "" && folio2 != "") {
                ban = 1;
            }
            if (fecha_ini != "" && fecha_fin != "") {
                ban2 = 1;
            }

            if (ban == 1) {
                QFolio = " F_DocMov between '" + folio1 + "' and '" + folio2 + "' ";
            }

            if (ban2 == 1 && ban == 0) {

                QFecha = " F_FecMov between '" + fecha_ini + "' and '" + fecha_fin + "' ";
            } else if (ban2 == 1 && ban == 1) {
                QFecha = "AND F_FecMov between '" + fecha_ini + "' and '" + fecha_fin + "' ";
            }

            Query = QFolio + QFecha;
            System.out.println("Query: " + Query);

            if (Query.isEmpty()) {
                System.out.println("sin datos");
                rset = con.consulta("SELECT F_DocMov,c.F_IdCon,c.F_DesCon,F_FecMov FROM tb_movinv m INNER JOIN tb_coninv c on m.F_ConMov=c.F_IdCon WHERE F_ConMov NOT IN (3,11,1000,1,51,1001,4,5,60,9) and F_FecMov BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE()  GROUP BY F_DocMov,F_FecMov,F_ConMov ORDER BY F_DocMov+0;");
            } else {
                System.out.println("datos fecha");
                rset = con.consulta("SELECT F_DocMov,c.F_IdCon,c.F_DesCon,F_FecMov FROM tb_movinv m INNER JOIN tb_coninv c on m.F_ConMov=c.F_IdCon WHERE F_ConMov NOT IN (3,11,1000,1,51,1001,4,5,60,9) and " + Query + "  GROUP BY F_DocMov,F_FecMov,F_ConMov ORDER BY F_DocMov+0;");
            }
            int contar = 0;
            while (rset.next()) {

%>
                                    <tr>

                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(4)%></td>
                               
                                    <td>
                                         <form action="../reportes/reimpreMovimiento.jsp" target="_blank" >
                                        <input class="hidden" name="fol_Mov" value="<%=rset.getString(1)%>">
                                        <input class="hidden" name="fecha" value="<%=rset.getString(4)%>">
                                        <input class="hidden" name="concepto" value="<%=rset.getString(2)%>">
                                        <button class="btn btn-block btn-primary" id="btn_<%=contar%>" ><span class="glyphicon glyphicon-print"></span></button>
                                         </form>
                                    </td>
                                
                                <td><a class="btn btn-info btn-block" href="../reportes/MovimientoUbicacion.jsp?F_ClaDoc=<%=rset.getString(1)%>&ConInv=<%=rset.getString(2)%>&fecha=<%=rset.getString(4)%>"><span class="glyphicon glyphicon-download"></span></a></td>

                                    </tr>
<%
    contar = contar ++;      
    }
        } catch (Exception e) {
            e.getMessage();
        }
        con.cierraConexion();
    } catch (Exception e) {
        e.getMessage();
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
                                $(document).ready(function () {
                                    $('#datosCompras').dataTable();
                                    
                                    $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
                                });
        </script>

        <script>
            function habilitar(value) {


                if (value == "si") {
                    document.getElementById("fecha_ini").disabled = true;
                    document.getElementById("fecha_fin").disabled = true;
                    document.getElementById("folio1").disabled = false;
                    document.getElementById("folio2").disabled = false;
                    document.getElementById("fecha_ini").value = "";
                    document.getElementById("fecha_fin").value = "";

                } else if (value == "no") {
                    document.getElementById("folio1").disabled = true;
                    document.getElementById("folio2").disabled = true;
                    document.getElementById("folio1").value = "";
                    document.getElementById("folio2").value = "";
                    document.getElementById("fecha_ini").disabled = false;
                    document.getElementById("fecha_fin").disabled = false;
                }
            }
        </script>
    </body>
</html>
