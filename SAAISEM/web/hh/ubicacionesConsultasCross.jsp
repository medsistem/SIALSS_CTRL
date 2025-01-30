<%-- 
    Document   : ubicacionesConsultasCross
    Created on : 26/11/2014, 07:39:54 AM
    Author     : Americo
--%>

<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    PreparedStatement ps;
    PreparedStatement ps2;
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    ConectionDB con = new ConectionDB();
    int banConsulta = 0, totalPiezas = 0;
    String UbicaCross = "";
    ResultSet rset = null;
    ResultSet rset2 = null;


%>
<!DOCTYPE html>
<html>
    <head>
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIALSS_CTRL</title>
    </head>
    <body class="container">
        <form action="ubicacionesConsultasCross.jsp" method="post">
            <h1>SIALSS_CTRL</h1>
            <%@include file="../jspf/menuPrincipal.jspf" %>
            <hr />
            <div class="row small">
                <h5 class="col-sm-1">Clave</h5>
                <div class="col-sm-2">
                    <input class="form-control input-sm" placeholder="Clave" name="F_ClaPro" />
                </div>
                <h5 class="col-sm-1">Lote</h5>
                <div class="col-sm-2">
                    <input class="form-control input-sm" placeholder="Lote" name="F_ClaLot" />
                </div>
                <h5 class="col-sm-1">CB Ubi</h5>
                <div class="col-sm-2">
                    <input class="form-control input-sm" placeholder="CB Ubicación" name="F_Ubica" />
                </div>
                <h5 class="col-sm-1">CB Med</h5>
                <div class="col-sm-2">
                    <input class="form-control input-sm" placeholder="CB Insumo" name="F_Cb" />
                </div>
            </div>
            <br/>
            <div class="row small">
                <div class="col-sm-2">
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="buscar">Buscar</button>
                </div>
                <div class="col-sm-2">
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="porUbicarCross">Por Ubicar Crossdock</button>
                </div>
                <div class="col-sm-2">
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="mostrarTodas">Mostrar Todas</button>
                </div>
                <div class="col-sm-2">
                    <a class="btn btn-block btn-primary btn-sm" href="../Procesos/descargaInventarioCross.jsp">Descargar Inventario</a>
                </div>
            </div>
        </form>
        <br/><br/>
        <table class="table table-condensed table-bordered table-striped" id="tablaUbicaciones">
            <thead>
                <tr>
                    <td>Proyecto</td>
                    <td>CLAVE</td>
                    <td>Origen</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <td>Piezas</td>
                    <td>Modula</td>
                    <td>Cantidad</td>
                    <!--td></td-->
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        con.conectar();
                        rset = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                        if (rset.next()) {
                            UbicaCross = rset.getString(1);
                        }
                        UbicaCross = "'CROSSDOCKMORELIA'," + UbicaCross;

                        if (request.getParameter("accion").equals("buscar")) {
                            if (!request.getParameter("F_ClaPro").equals("") && request.getParameter("F_ClaPro") != null) {
                                rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where F_ClaPro = '" + request.getParameter("F_ClaPro") + "' and F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                                rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias where F_ClaPro = '" + request.getParameter("F_ClaPro") + "' AND F_Ubica IN (" + UbicaCross + ");");
                            }
                            if (!request.getParameter("F_ClaLot").equals("") && request.getParameter("F_ClaLot") != null) {
                                rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where F_ClaLot = '" + request.getParameter("F_ClaLot") + "' and F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                                rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias where F_ClaLot = '" + request.getParameter("F_ClaLot") + "' AND F_Ubica IN (" + UbicaCross + ");");
                            }
                            if (!request.getParameter("F_Ubica").equals("") && request.getParameter("F_Ubica") != null) {
                                rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where F_CBUbica = '" + request.getParameter("F_Ubica") + "' and F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                                rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias where F_CBUbica = '" + request.getParameter("F_Ubica") + "' AND F_Ubica IN (" + UbicaCross + ");");
                            }
                            if (!request.getParameter("F_Cb").equals("") && request.getParameter("F_Cb") != null) {
                                rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where F_Cb = '" + request.getParameter("F_Cb") + "' and F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                                rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias where F_Cb = '" + request.getParameter("F_Cb") + "' AND F_Ubica IN (" + UbicaCross + ");");
                            }
                        }

                        if (request.getParameter("accion").equals("porUbicarCross")) {

                            rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where  F_Ubica='NUEVACROSS' and F_ExiLot!=0;");
                            rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias where  F_Ubica='NUEVACROSS';");
                        }

                        if (request.getParameter("accion").equals("mostrarTodas")) {
                            rset2 = con.consulta("select F_ClaPro, F_DesPro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy from v_existencias where F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                            rset = con.consulta("select sum(F_ExiLot) as totalPiezas from v_existencias WHERE F_Ubica IN (" + UbicaCross + ");");
                        }

                        while (rset.next()) {
                            totalPiezas = rset.getInt("totalPiezas");
                        }

                        while (rset2.next()) {

                            String F_StsMod = "0", F_CantMod = "";
                            ResultSet rset3 = con.consulta("select SUM(F_Cant) as F_Cant, F_StsMod from tb_facttemp where F_IdLot = '" + rset2.getString("F_IdLote") + "' and F_StsFact<5");
                            while (rset3.next()) {
                                F_StsMod = rset3.getString("F_StsMod");
                                F_CantMod = rset3.getString("F_Cant");
                            }
                            if (F_StsMod == null) {
                                F_StsMod = "";
                                F_CantMod = "";
                            }
                %>
                <tr <%
                    if (F_StsMod.equals("1") || F_StsMod.equals("2")) {
                        out.println("class='info'");
                    }
                    %>
                    >
                    <td><%=rset2.getString("F_DesProy")%></td>
                    <td><a href="#" title="<%=rset2.getString("F_DesPro")%>"><%=rset2.getString("F_ClaPro")%></a></td>
                    <td><%=rset2.getString("F_DesOri")%></td>
                    <td><%=rset2.getString("F_ClaLot")%></td>
                    <td><%=rset2.getString("F_FecCad")%></td>
                    <td><%=rset2.getString("F_DesUbi")%></td>
                    <td><%=formatter.format(rset2.getInt("F_ExiLot"))%></td>
                    <td>
                        <%
                            if (F_StsMod.equals("1")) {
                                out.println("En Abasto");
                            }
                        %>

                        <%
                            if (F_StsMod.equals("2")) {
                                out.println("En Modula");
                            }
                        %>
                    </td>
                    <td>
                        <%
                            if (F_StsMod.equals("1") || F_StsMod.equals("2")) {
                                out.println(F_CantMod);
                            }
                        %>
                    </td>
                    <!--td>
                        <form action="../Ubicaciones/indexValida.jsp" method="post">
                            <input name="folio" value="<%//=rset2.getString("F_FolLot")%>" class="hidden" />
                            <input name="ubicacion" value="<%//=rset2.getString("F_Ubica")%>" class="hidden" />
                            <input name="id" value="<%//=rset2.getString("F_IdLote")%>" class="hidden" />
                            <button class="btn btn-block btn-info btn-sm" id="folio" name="accion" value="Modificar"><span class="glyphicon glyphicon-edit"></span></button>
                        </form>
                    </td-->
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
        <hr/>
        <h3>Total de Piezas: <%=formatter.format(totalPiezas)%></h3>
        <hr />
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function () {
                $('#tablaUbicaciones').dataTable();
                $('#existModula').dataTable();
            });
        </script>
    </body>
</html>
