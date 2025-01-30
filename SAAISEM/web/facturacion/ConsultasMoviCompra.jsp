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

    String fecha_ini = "", fecha_fin = "", clave = "", ClaCli = "", Proyec = "", Origen = "", DesProyecto = "";
    int Proyecto = 0;
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        clave = request.getParameter("clave");
        ClaCli = request.getParameter("conceptos");
        Proyec = request.getParameter("Proyecto");
        Origen = request.getParameter("Origen");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (clave == null) {
        clave = "";
    }
    if (ClaCli == null) {
        ClaCli = "";
    }
    if (Proyec == null) {
        Proyec = "0";
    }
    if (Origen == null) {
        Origen = "0";
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
                <h3 class="panel-title">Consultas Movimientos</h3>
            </div>
            <form action="ConsultasMoviCompra.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Clave</label>
                        <div class="col-lg-2">
                            <input class="form-control" id="clave" name="clave" type="text" value=""  />
                        </div>                    
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
                        <div class="col-sm-2">
                            <select class="form-control" name="Origen" id="Origen">
                                <option value="0">-Selec Origen-</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT * FROM tb_origen WHERE F_ClaOri <> 25;");
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
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Conceptos</label>
                        <div class="col-lg-3">
                            <input class="form-control" id="conceptos" name="conceptos" type="text" readonly=""  />
                        </div>
                        <div class="col-sm-5">
                            <select class="form-control" name="ClaCli" id="ClaCli">
                                <option value="">-Seleccione Concepto Mov.-</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT F_IdCon, CONCAT('[', F_IdCon, ']  ', F_DesCon) AS F_DesCon FROM tb_coninv WHERE F_IdCon IN (1, 51);");
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
                        <div class="col-sm-2">
                            <a href="ConsultasMoviCompra.jsp" class="btn btn-block btn-info glyphicon glyphicon-trash">Limpiar</a>
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
                        if (clave != "") {
                            ban = 1;
                            Clave = " m.F_ProMov='" + clave + "' ";
                        }
                        if (fecha_ini != "" && fecha_fin != "") {
                            ban1 = 1;
                            FechaFol = " m.F_FecMov between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                        }
                        if (ClaCli != "") {
                            ban2 = 1;
                            Concep = " m.F_ConMov IN (" + ClaCli + ") ";
                        }
                        if (ban == 1 && ban1 == 1 && ban2 == 1) {
                            Query = Clave + " AND " + FechaFol + " AND " + Concep;
                        } else if (ban == 1 && ban1 == 1) {
                            Query = Clave + " AND " + FechaFol;
                        } else if (ban == 1 && ban2 == 1) {
                            Query = Clave + " AND " + Concep;
                        } else if (ban1 == 1 && ban2 == 1) {
                            Query = FechaFol + " AND " + Concep;
                        } else if (ban == 1) {
                            Query = Clave;
                        } else if (ban1 == 1) {
                            Query = FechaFol;
                        } else if (ban2 == 1) {
                            Query = Concep;
                        }

                        ResultSet rset = con.consulta("select COUNT(F_ProMov) from tb_movinv m WHERE " + Query + ";");
                        if (rset.next()) {
                            Contar = rset.getInt(1);
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
                <div class="row">                    
                    <input class="form-control" id="clave1" name="clave1" type="hidden" value="<%=clave%>" />
                    <input class="form-control" id="concep1" name="concep1" type="hidden" value="<%=ClaCli%>" />
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />

                </div>
                <div class="row col-sm-6">
                    <a class="btn btn-block btn-info" href="gnrMovCompra.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>&clave=<%=clave%>&Concepto=<%=ClaCli%>&Proyecto=<%=Proyecto%>&Origen=<%=Origen%>&DesProyecto=<%=DesProyecto%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
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
                                            <td>Des/Mov</td>
                                            <td>Clave</td>
                                            <td>Descripción</td>
                                            <td>Lote</td>
                                            <td>Caducidad</td>
                                            <td>Cantidad</td>
                                            <td>Origen</td>
                                            <td>Marca</td>
                                            <td>Proveedor</td>
                                            <td>OC</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            try {
                                                con.conectar();
                                                try {

                                                    String FechaFol = "", Clave = "", Concep = "", Query = "", AND = "", ANDOrigen = "";
                                                    int ban = 0, ban1 = 0, ban2 = 0;

                                                    if (Origen.equals("0")) {
                                                        ANDOrigen = "";
                                                    } else {
                                                        ANDOrigen = " AND l.F_Origen='" + Origen + "' ";
                                                    }

                                                    if (Proyecto == 0) {
                                                        AND = "";
                                                    } else {
                                                        AND = " AND l.F_Proyecto='" + Proyecto + "' ";
                                                    }

                                                    if (clave != "") {
                                                        ban = 1;
                                                        Clave = " m.F_ProMov='" + clave + "' ";
                                                    }
                                                    if (fecha_ini != "" && fecha_fin != "") {
                                                        ban1 = 1;
                                                        FechaFol = " m.F_FecMov between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                                                    }
                                                    if (ClaCli != "") {
                                                        ban2 = 1;
                                                        Concep = " m.F_ConMov IN (" + ClaCli + ") ";
                                                    }
                                                    if (ban == 1 && ban1 == 1 && ban2 == 1) {
                                                        Query = Clave + " AND " + FechaFol + " AND " + Concep;
                                                    } else if (ban == 1 && ban1 == 1) {
                                                        Query = Clave + " AND " + FechaFol;
                                                    } else if (ban == 1 && ban2 == 1) {
                                                        Query = Clave + " AND " + Concep;
                                                    } else if (ban1 == 1 && ban2 == 1) {
                                                        Query = FechaFol + " AND " + Concep;
                                                    } else if (ban == 1) {
                                                        Query = Clave;
                                                    } else if (ban1 == 1) {
                                                        Query = FechaFol;
                                                    } else if (ban2 == 1) {
                                                        Query = Concep;
                                                    }

                                                    ResultSet rset = con.consulta("SELECT C.F_DesCon, F_ProMov, MD.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_CantMov) AS F_CantMov, O.F_DesOri, IFNULL(mc.F_DesMar, '') AS F_DesMar, IFNULL(pr.F_NomPro, '') AS F_NomPro, IFNULL(cp.F_OrdCom,'') AS F_OrdCom FROM tb_movinv m INNER JOIN tb_lote l ON m.F_ProMov = l.F_ClaPro AND m.F_LotMov = l.F_FolLot AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_medica MD ON m.F_ProMov = MD.F_ClaPro INNER JOIN tb_coninv C ON m.F_ConMov = C.F_IdCon INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen O ON l.F_Origen = O.F_ClaOri LEFT JOIN tb_proveedor pr ON l.F_ClaPrv = pr.F_ClaProve LEFT JOIN tb_marca mc ON l.F_ClaMar = mc.F_ClaMar LEFT JOIN(SELECT F_ClaPro, F_Lote, GROUP_CONCAT(F_OrdCom) AS F_OrdCom FROM tb_compra GROUP BY F_ClaPro, F_Lote) AS cp ON l.F_ClaPro=cp.F_ClaPro AND l.F_FolLot=cp.F_Lote  WHERE " + Query + " AND m.F_ConMov IN (1, 51) " + AND + " " + ANDOrigen + " AND p.F_Id = 2 GROUP BY F_ConMov,F_ProMov,l.F_ClaLot,F_FecCad,l.F_Origen;");
                                                    while (rset.next()) {

                                        %>
                                        <tr>
                                            <td><%=rset.getString(1)%></td>
                                            <td><%=rset.getString(2)%></td>
                                            <td><%=rset.getString(3)%></td>
                                            <td><%=rset.getString(4)%></td>
                                            <td><%=rset.getString(5)%></td>
                                            <td><%=rset.getString(6)%></td>
                                            <td><%=rset.getString(7)%></td>
                                            <td><%=rset.getString(8)%></td>
                                            <td><%=rset.getString(9)%></td>
                                            <td><%=rset.getString(10)%></td>
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

