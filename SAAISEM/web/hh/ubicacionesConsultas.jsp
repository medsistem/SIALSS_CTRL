<%-- 
    Document   : ubicacionesConsultas
    Created on : 26/11/2014, 07:39:54 AM
    Author     : Americo
--%>

<%@page import="conn.ConectionDB_SQLServer"%>
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
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    ConectionDB con = new ConectionDB();
    ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
    ConectionDB conMysql = new ConectionDB();
    int banConsulta = 0;
    int totalPiezas = 0;
    ResultSet rset = null;
    ResultSet rset2 = null;
    String qry1 = "select sum(F_ExiLot) as totalPiezas from tb_lote";
    String qry2 = "";
    try {
       if (request.getParameter("accion").equals("buscar")) {
            if (!request.getParameter("F_ClaPro").equals("") && request.getParameter("F_ClaPro") != null) {
                qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where F_ClaPro = '" + request.getParameter("F_ClaPro") + "' and F_ExiLot!=0";
                qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where F_ClaPro = '" + request.getParameter("F_ClaPro") + "'";
            }
            if (!request.getParameter("F_ClaLot").equals("") && request.getParameter("F_ClaLot") != null) {
                qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where F_ClaLot = '" + request.getParameter("F_ClaLot") + "' and F_ExiLot!=0";
                qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where F_ClaLot = '" + request.getParameter("F_ClaLot") + "'";
            }
            if (!request.getParameter("F_Ubica").equals("") && request.getParameter("F_Ubica") != null) {
                qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where F_CBUbica = '" + request.getParameter("F_Ubica") + "' and F_ExiLot!=0";
                qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where F_CBUbica = '" + request.getParameter("F_Ubica") + "'";
            }
            if (!request.getParameter("F_Cb").equals("") && request.getParameter("F_Cb") != null) {
                qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where F_Cb = '" + request.getParameter("F_Cb") + "' and F_ExiLot!=0";
                qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where F_Cb = '" + request.getParameter("F_Cb") + "'";
            }
        }
        if (request.getParameter("accion").equals("porUbicar")) {

            qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where  F_Ubica='NUEVA' and F_ExiLot!=0";
            qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where  F_Ubica='NUEVA'";
        }

        if (request.getParameter("accion").equals("porUbicarCross")) {

            qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where  F_Ubica='NUEVACROSS' and F_ExiLot!=0";
            qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias where  F_Ubica='NUEVACROSS'";
        }

        if (request.getParameter("accion").equals("mostrarTodas")) {
            qry2 = "select F_ClaPro, F_DesPro,F_PrePro, F_ClaLot,  DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_DesUbi, F_ExiLot, F_IdLote, F_Ubica, F_FolLot, F_Origen, F_DesOri, F_DesProy, apartado, disponible,F_NomPro,Lugar from v_existencias where F_ExiLot!=0;";
            qry1 = "select sum(F_ExiLot) as totalPiezas from v_existencias";
        }

    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    try {
        con.conectar();
        rset = con.consulta(qry1);
        while (rset.next()) {
            totalPiezas = rset.getInt("totalPiezas");
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
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
        <form action="ubicacionesConsultas.jsp" method="post">
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
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="porUbicar">Por Ubicar</button>
                </div>
                <div class="col-sm-2">
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="porUbicarCross">Por Ubicar Crossdock</button>
                </div>
                <div class="col-sm-2">
                    <button class="btn btn-block btn-primary btn-sm" name="accion" value="mostrarTodas">Mostrar Todas</button>
                </div>
                <div class="col-sm-2">
                    <a class="btn btn-block btn-primary btn-sm" href="../Procesos/descargaInventario.jsp">Descargar Inventario</a>
                </div>
            </div>
        </form>
        <br/><br/>
        <table class="table table-condensed table-bordered table-striped" id="tablaUbicaciones">
            <thead>
                <tr>
                    <td>Proyecto</td>
                    <td>CLAVE</td>
                    <td>Presentacion</td>
                    <td>Origen</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <td>Disponible</td>
                    <td>Apartado</td>
                    <td>Existencia</td>
                    <td>Proveedor</td>
                    <td>Bodega</td>
                    <td>Modula</td>
                    <td>Cantidad</td>
                    <!--td></td-->
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        con.conectar();
                        rset2 = con.consulta(qry2);
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
                    <td><%=rset2.getString("F_PrePro")%></td>
                    <td><%=rset2.getString("F_DesOri")%></td>
                    <td><%=rset2.getString("F_ClaLot")%></td>
                    <td><%=rset2.getString("F_FecCad")%></td>
                    <td><%=rset2.getString("F_DesUbi")%></td>
                    <td><%=formatter.format(rset2.getInt("disponible"))%></td>
                    <td><%=formatter.format(rset2.getInt("apartado"))%></td>
                    <td><%=formatter.format(rset2.getInt("F_ExiLot"))%></td>
                    <td><%=rset2.getString("F_NomPro")%></td>
                    <td><%=rset2.getString("Lugar")%></td>
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
                    <!--<td>
                        <form action="../Ubicaciones/indexValida.jsp" method="post">
                            <input name="folio" value="<%=rset2.getString("F_FolLot")%>" class="hidden" />
                            <input name="ubicacion" value="<%=rset2.getString("F_Ubica")%>" class="hidden" />
                            <input name="id" value="<%=rset2.getString("F_IdLote")%>" class="hidden" />
                            <button class="btn btn-block btn-info btn-sm" id="folio" name="accion" value="Modificar"><span class="glyphicon glyphicon-edit"></span></button>
                        </form>
                    </td>-->
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
        
        <!--
        <h3>Modula<div class="col-sm-2"><a href="gnrExistModula.jsp" class="btn btn-info glyphicon glyphicon-download-alt"></a></div></h3>
        <table border="1" class="table table-bordered table-condensed table-striped" id="existModula">
            <thead>
                <tr>
                    <td>Clave</td>
                    <td>Descrip</td>
                    <td>Lote</td>
                    <td>Cadu</td>
                    <td>Cajón</td>
                    <td>Posición</td>
                    <td>Cant</td>
                </tr>
            </thead>
            
            <tbody>
               <%
                   // int totalModula = 0;
                //    try {
                      //  conModula.conectar();
                      //  con.conectar();
                      //  ResultSet rset4 = conModula.consulta("select * from VIEW_MODULA_UBICACION where SCO_GIAC!=0 order by SCO_ARTICOLO");
                     //   while (rset4.next()) {
                      //      totalModula = totalModula + rset4.getInt("SCO_GIAC");
                      //      String Descrip = "";
                      //      ResultSet rset5 = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + rset4.getString("SCO_ARTICOLO") + "'");
                       //     while (rset5.next()) {
                       //         Descrip = rset5.getString(1);
                       //     }
                %>
                <tr>
                    <td>< %=rset4.getString("SCO_ARTICOLO")%></td>
                    <td>< %=Descrip%></td>
                    <td>< %=rset4.getString("SCO_SUB1")%></td>
                    <td>< %=rset4.getString("SCO_DSCAD")%></td>
                    <td>< %=rset4.getString("UDC_UDC")%></td>
                    <td>< %=rset4.getString("SCO_POSI")%></td>
                    <td>< %=formatter.format(rset4.getInt("SCO_GIAC"))%></td>
                </tr>
                <%
                   //     }
                   //     conModula.cierraConexion();
                  //      con.cierraConexion();
                  //  } catch (Exception e) {
                 // //      System.out.println(e.getMessage());
                 //   }

                %>
            </tbody>
        </table>
        <h3>Total Modula= < %=totalModula%></h3>-->
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
