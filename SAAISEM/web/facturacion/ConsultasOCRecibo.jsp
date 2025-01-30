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
<%
    DecimalFormat Entero = new DecimalFormat("#,###");
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "", NOOC = "", Proveedor = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        NOOC = request.getParameter("NOOC");
        Proveedor = request.getParameter("Proveedor");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (NOOC == null) {
        NOOC = "";
    }

    if (Proveedor == null) {
        Proveedor = "";
    }

    int Solicitado = 0, Recibido = 0, Diferencia = 0;
    try {
        con.conectar();
        try {
            String FechaFol = "", NOOCS = "", Query = "", ProveedorS = "";
            int ban = 0, ban1 = 0, ban2 = 0;
            if (NOOC != "") {
                ban = 1;
                NOOCS = " AND P.F_NoCompra='" + NOOC + "' ";
            }
            if (fecha_ini != "" && fecha_fin != "") {
                ban1 = 1;
                FechaFol = " AND DATE(P.F_Fecha) BETWEEN '" + fecha_ini + "' and '" + fecha_fin + "' ";
            }
            if (Proveedor != "") {
                ban2 = 1;
                ProveedorS = " AND P.F_Provee = '" + Proveedor + "' ";
            }

            if (ban == 1 && ban1 == 1 && ban2 == 1) {
                Query = NOOCS + FechaFol + ProveedorS;
            } else if (ban == 1 && ban1 == 1) {
                Query = NOOCS + FechaFol;
            } else if (ban == 1 && ban2 == 1) {
                Query = NOOCS + ProveedorS;
            } else if (ban1 == 1 && ban2 == 1) {
                Query = FechaFol + ProveedorS;
            } else if (ban == 1) {
                Query = NOOCS;
            } else if (ban1 == 1) {
                Query = FechaFol;
            } else if (ban2 == 1) {
                Query = ProveedorS;
            }
            ResultSet rset = con.consulta("SELECT COUNT(F_NoCompra), SUM(P.F_Cant), SUM(IFNULL(C.F_CanCom,0)), ( SUM(P.F_Cant) - SUM(IFNULL(C.F_CanCom,0))) FROM tb_pedido_sialss P LEFT JOIN ( SELECT F_OrdCom, F_ClaPro, SUM(F_CanCom) AS F_CanCom, SUM(F_ComTot) AS F_ComTot FROM tb_compra GROUP BY F_OrdCom, F_ClaPro ) AS C ON P.F_NoCompra = C.F_OrdCom AND P.F_Clave = C.F_ClaPro LEFT JOIN tb_medica M ON P.F_Clave = M.F_ClaPro WHERE P.F_StsPed = 1 " + Query + ";");
            if (rset.next()) {
                Solicitado = rset.getInt(2);
                Recibido = rset.getInt(3);
                Diferencia = rset.getInt(4);
            }
        } catch (Exception e) {

        }
        con.cierraConexion();
    } catch (Exception e) {

    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/select2.css" rel="stylesheet">
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
                <h4 class="panel-title">Reporte OC vs Recibo
                    &nbsp;&nbsp;<label>Total Sol:&nbsp;<%=Entero.format(Solicitado)%></label>&nbsp;&nbsp;
                    &nbsp;&nbsp;<label>Total Recibido:&nbsp;<%=Entero.format(Recibido)%></label>&nbsp;&nbsp;
                    &nbsp;&nbsp;<label>Total Diferencia:&nbsp;<%=Entero.format(Diferencia)%></label>
                </h4>
            </div>
            <form action="ConsultasOCRecibo.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">No. OC</label>
                        <div class="col-lg-4">
                            <select name="NOOC" id="NOOC" class="form-control">
                                <option value="">-- Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT F_NoCompra FROM tb_pedido_sialss where F_Proyecto not in (5) GROUP BY F_NoCompra;");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>"><%=rset.getString(1)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        out.println(e.getMessage());
                                    }
                                %>
                            </select>
                        </div>                    
                        <div class="col-lg-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" />
                        </div>
                        <div class="col-lg-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date"/>
                        </div>
                    </div>
                    <br />
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Proveedor</label>
                        <div class="col-lg-4">
                            <select name="Proveedor" id="Proveedor" class="form-control">
                                <option value="">-- Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT P.F_Provee, PR.F_NomPro FROM tb_pedido_sialss P INNER JOIN tb_proveedor PR ON P.F_Provee = PR.F_ClaProve GROUP BY P.F_Provee;");
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
                        String FechaFol = "", NOOCS = "", Query = "", ProveedorS = "";
                        int ban = 0, ban1 = 0, ban2 = 0;
                        if (NOOC != "") {
                            ban = 1;
                            NOOCS = " AND P.F_NoCompra='" + NOOC + "' ";
                        }
                        if (fecha_ini != "" && fecha_fin != "") {
                            ban1 = 1;
                            FechaFol = " AND DATE(P.F_Fecha) BETWEEN '" + fecha_ini + "' and '" + fecha_fin + "' ";
                        }
                        if (Proveedor != "") {
                            ban2 = 1;
                            ProveedorS = " AND P.F_Provee = '" + Proveedor + "' ";
                        }

                        if (ban == 1 && ban1 == 1 && ban2 == 1) {
                            Query = NOOCS + FechaFol + ProveedorS;
                        } else if (ban == 1 && ban1 == 1) {
                            Query = NOOCS + FechaFol;
                        } else if (ban == 1 && ban2 == 1) {
                            Query = NOOCS + ProveedorS;
                        } else if (ban1 == 1 && ban2 == 1) {
                            Query = FechaFol + ProveedorS;
                        } else if (ban == 1) {
                            Query = NOOCS;
                        } else if (ban1 == 1) {
                            Query = FechaFol;
                        } else if (ban2 == 1) {
                            Query = ProveedorS;
                        }
                        ResultSet rset = con.consulta("SELECT COUNT(F_NoCompra), SUM(P.F_Cant), SUM(C.F_CanCom), SUM(P.F_Cant) - SUM(C.F_CanCom) FROM tb_pedido_sialss P LEFT JOIN ( SELECT F_OrdCom, F_ClaPro, SUM(F_CanCom) AS F_CanCom, SUM(F_ComTot) AS F_ComTot FROM tb_compra GROUP BY F_OrdCom, F_ClaPro ) AS C ON P.F_NoCompra = C.F_OrdCom AND P.F_Clave = C.F_ClaPro LEFT JOIN tb_medica M ON P.F_Clave = M.F_ClaPro WHERE P.F_StsPed = 1 " + Query + "  and  P.F_Proyecto not in (5);");
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
                    <input class="form-control" id="NOOC1" name="NOOC1" type="hidden" value="<%=NOOC%>" />
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />
                    <input class="form-control" id="Proveedor1" name="Proveedor1" type="hidden" value="<%=Proveedor%>" />
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <a class="btn btn-block btn-info" href="gnrMovOC.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>&NOOC=<%=NOOC%>&Proveedor=<%=Proveedor%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
                    </div>
                </div>
                <%}%>


                <div>
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped" id="datosCompras">
                                    <thead>
                                        <tr>
                                            <td>No OC</td>
                                            <td>Proveedor</td>
                                            <td>Clave</td>
                                            <td>Descripción</td>
                                            <td>Solicitado</td>
                                            <td>Recibido</td>
                                            <td>Monto</td>
                                            <td>Diferencia</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            try {
                                                con.conectar();
                                                try {

                                                    String FechaFol = "", NOOCS = "", Query = "", ProveedorS = "";
                                                    int ban = 0, ban1 = 0, ban2 = 0;
                                                    if (NOOC != "") {
                                                        ban = 1;
                                                        NOOCS = " AND P.F_NoCompra='" + NOOC + "' ";
                                                    }
                                                    if (fecha_ini != "" && fecha_fin != "") {
                                                        ban1 = 1;
                                                        FechaFol = " AND DATE(P.F_Fecha) BETWEEN '" + fecha_ini + "' and '" + fecha_fin + "' ";
                                                    }
                                                    if (Proveedor != "") {
                                                        ban2 = 1;
                                                        ProveedorS = " AND P.F_Provee = '" + Proveedor + "' ";
                                                    }

                                                    if (ban == 1 && ban1 == 1 && ban2 == 1) {
                                                        Query = NOOCS + FechaFol + ProveedorS;
                                                    } else if (ban == 1 && ban1 == 1) {
                                                        Query = NOOCS + FechaFol;
                                                    } else if (ban == 1 && ban2 == 1) {
                                                        Query = NOOCS + ProveedorS;
                                                    } else if (ban1 == 1 && ban2 == 1) {
                                                        Query = FechaFol + ProveedorS;
                                                    } else if (ban == 1) {
                                                        Query = NOOCS;
                                                    } else if (ban1 == 1) {
                                                        Query = FechaFol;
                                                    } else if (ban2 == 1) {
                                                        Query = ProveedorS;
                                                    }

                                                    ResultSet rset = con.consulta("SELECT F_NoCompra, F_Clave, M.F_DesPro, FORMAT(SUM(F_Cant), 0) AS F_Cant, FORMAT(IFNULL(C.F_CanCom, 0), 0) AS F_CanCom, FORMAT(IFNULL(C.F_ComTot, 0), 2) AS F_ComTot, SUM(F_Cant) - IFNULL(C.F_CanCom, 0) AS PENDIENTE, PR.F_NomPro FROM tb_pedido_sialss P LEFT JOIN ( SELECT F_OrdCom, F_ClaPro, SUM(F_CanCom) AS F_CanCom, SUM(F_ComTot) AS F_ComTot FROM tb_compra GROUP BY F_OrdCom, F_ClaPro ) AS C ON P.F_NoCompra = C.F_OrdCom AND P.F_Clave = C.F_ClaPro LEFT JOIN tb_medica M ON P.F_Clave = M.F_ClaPro LEFT JOIN tb_proveedor PR ON P.F_Provee = PR.F_ClaProve WHERE P.F_StsPed = 1 " + Query + "  AND P.F_Proyecto NOT IN (5) GROUP BY P.F_NoCompra, F_Clave, P.F_Provee ORDER BY F_NoCompra ASC, F_Clave ASC;");
                                                    while (rset.next()) {

                                        %>
                                        <tr>
                                            <td><%=rset.getString(1)%></td>
                                            <td><%=rset.getString(8)%></td>
                                            <td><%=rset.getString(2)%></td>
                                            <td><%=rset.getString(3)%></td>
                                            <td><%=rset.getString(4)%></td>
                                            <td><%=rset.getString(5)%></td>
                                            <td><%=rset.getString(6)%></td>
                                            <td><%=rset.getString(7)%></td>
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
        <script src="../js/select2.js"></script>
        <script>
            $(document).ready(function () {
                $('#datosCompras').dataTable();
                $('#NOOC').select2();
                $('#Proveedor').select2();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });
        </script>
    </body>
</html>

