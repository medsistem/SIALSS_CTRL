<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="conn.ConectionDB"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterD = new DecimalFormat("#,###,###.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "",tipo="";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();
    String fechaCap = "";
    String Proveedor = "";
    try {
        fechaCap = df2.format(df3.parse(request.getParameter("Fecha")));
    } catch (Exception e) {

    }
    if (fechaCap == null) {
        fechaCap = "";
    }
    try {
        Proveedor = request.getParameter("Proveedor");
    } catch (Exception e) {

    }
    if (Proveedor == null) {
        Proveedor = "";
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
 <%@include file="../jspf/menuPrincipalCompra.jspf" %>
            <div>
                <h3>Reporte por Fecha Proveedor</h3>
                <div class="row">
                    <form action="ReporteFCompra.jsp" method="post">
                        <h4 class="col-sm-1">Proveedor</h4>
                        <div class="col-sm-5">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="this.form.submit();">
                                <option value="">--Proveedor--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT C.F_ProVee,P.F_NomPro FROM tb_compra C INNER JOIN tb_proveedor P ON C.F_ProVee=P.F_ClaProve WHERE  C.F_Proyecto NOT IN (5) GROUP BY C.F_ProVee ORDER BY P.F_NomPro;");
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
                        <h4 class="col-sm-2">Fecha de Recibo</h4>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha"  onchange="this.form.submit();" />
                        </div>
                        <!--a class="btn btn-primary" href="ReporteF.jsp">Todo</a-->
                        <a class="btn btn-primary" href="ReporteF_gnrCompra.jsp?provee=<%=Proveedor%>&fecha=<%=fechaCap%>">Descargar&nbsp;<label class="glyphicon glyphicon-download-alt"></label></a>
                    </form>
                </div>
            </div>
        </div>
        <br />
        <div class="container-fluid">
            <div class="panel panel-primary table-responsive">
                <div class="panel panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>                                
                                <td class="text-center">Proyecto</td>
                                <td class="text-center">Proveedor</td>
                                <td class="text-center">Clave</td>
                                <td class="text-center">Lote</td>
                                <td class="text-center">Caducidad</td>
                                <td class="text-center">Piezas</td>
                                <td class="text-center">Fecha</td>
                                <td class="text-center">OC</td> 
                                <td class="text-center">Origen</td>                                
                                <td class="text-center">No. Ingreso</td> 
                                <td class="text-center">Remisión</td>
                                <td class="text-center">Usuario</td>
                                <td class="text-center">Costo U</td>
                                <td class="text-center">Monto</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                    if ((Proveedor.equals("")) && (fechaCap.equals(""))) {

                                        rset = con.consulta("SELECT p.F_NomPro, c.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS 'fecCad', SUM(F_CanCom) 'cantidad', DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS 'fecapl', c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, pr.F_DesProy, c.F_Costo, (SUM(F_CanCom) * c.F_Costo) AS monto,l.F_DesOri FROM tb_compra c INNER JOIN tb_proveedor p ON c.F_ClaOrg = p.F_ClaProve INNER JOIN ( SELECT F_ClaPro, F_FolLot, F_ClaLot, F_FecCad,o.F_DesOri FROM tb_lote INNER JOIN tb_origen o ON F_Origen = o.F_ClaOri GROUP BY F_ClaPro, F_FolLot ) l ON c.F_Lote = l.F_FolLot AND c.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos pr ON c.F_Proyecto = pr.F_Id WHERE  c.F_Proyecto NOT IN (5) GROUP BY p.F_NomPro, c.F_ClaPro, l.F_ClaLot, c.F_FecApl, c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, c.F_Proyecto LIMIT 100; ");
                                    } else if (!(Proveedor.equals("")) && (fechaCap.equals(""))) {
                                         //   rset = con.consulta("SELECT py.F_DesProy AS Proyecto,p.F_NomPro AS Proveedor, p17.F_Clave AS Clave, IFNULL( l.F_ClaLot,'') AS Lote, IFNULL(c.F_FecCad ,'') AS Caducidad, IFNULL(c.F_CanCom,'') AS Piezas, p17.F_NoCompra AS OC, IFNULL(l.F_DesOri,'') AS Origen, IFNULL(c.F_ClaDoc,'') AS NoIngreso, IFNULL(c.F_FolRemi,'') AS Remision, IFNULL(c.F_User,'') AS Usuario_Captura, IFNULL(CONCAT(c.F_FecCaptura,' - ',c.F_HoraCaptura),'') AS FechaCaptura, IFNULL(c.F_UserIngreso,'') AS Usuario_Ingreso, IFNULL(CONCAT(c.F_FecApl,' - ',c.F_Hora),'') AS FechaIngreso, IFNULL(c.F_Costo,'') AS Costo, IFNULL((SUM(F_CanCom) * c.F_Costo),'') AS Monto,IFNULL( c.F_FecApl, '' ) AS Fecha FROM tb_pedido_sialss AS p17 LEFT JOIN tb_compra AS c ON p17.F_NoCompra = c.F_OrdCom LEFT JOIN ( SELECT lo.F_ClaPro, lo.F_FolLot, lo.F_ClaLot, lo.F_FecCad, o.F_DesOri FROM tb_lote as lo INNER JOIN tb_origen o ON lo.F_Origen = o.F_ClaOri where F_ClaPrv = '" + Proveedor + "' GROUP BY F_ClaPro, F_FolLot ) l ON c.F_Lote = l.F_FolLot AND c.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos AS py ON p17.F_Proyecto = py.F_Id INNER JOIN tb_proveedor AS p ON p17.F_Provee = p.F_ClaProve WHERE p17.F_Provee = '" + Proveedor + "' GROUP BY py.F_DesProy,p.F_NomPro,p17.F_Clave,c.F_Lote,c.F_FecCad,c.F_CanCom,p17.F_NoCompra,c.F_UserIngreso,c.F_HoraCaptura,c.F_User,c.F_FolRemi,c.F_ClaDoc ORDER BY p17.F_Fecha ASC,Remision ASC;");
                              
                                       rset = con.consulta("SELECT p.F_NomPro, c.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS 'fecCad', SUM(F_CanCom) 'cantidad', DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS 'fecapl', c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, pr.F_DesProy, c.F_Costo, (SUM(F_CanCom) * c.F_Costo) AS monto,l.F_DesOri FROM tb_compra c INNER JOIN tb_proveedor p ON c.F_ClaOrg = p.F_ClaProve INNER JOIN ( SELECT F_ClaPro, F_FolLot, F_ClaLot, F_FecCad,o.F_DesOri FROM tb_lote INNER JOIN tb_origen o ON F_Origen = o.F_ClaOri GROUP BY F_ClaPro, F_FolLot ) l ON c.F_Lote = l.F_FolLot AND c.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos pr ON c.F_Proyecto = pr.F_Id where p.F_ClaProve='" + Proveedor + "' and c.F_Proyecto NOT IN (5) GROUP BY p.F_NomPro, c.F_ClaPro, l.F_ClaLot, c.F_FecApl, c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, c.F_Proyecto;");
                                    } else {

                                        rset = con.consulta("SELECT p.F_NomPro, c.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS 'fecCad', SUM(F_CanCom) 'cantidad', DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS 'fecapl', c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, pr.F_DesProy, c.F_Costo, (SUM(F_CanCom) * c.F_Costo) AS monto,l.F_DesOri FROM tb_compra c INNER JOIN tb_proveedor p ON c.F_ClaOrg = p.F_ClaProve INNER JOIN ( SELECT F_ClaPro, F_FolLot, F_ClaLot, F_FecCad,o.F_DesOri FROM tb_lote INNER JOIN tb_origen o ON F_Origen = o.F_ClaOri GROUP BY F_ClaPro, F_FolLot ) l ON c.F_Lote = l.F_FolLot AND c.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos pr ON c.F_Proyecto = pr.F_Id  where c.F_FecApl='" + fechaCap + "' and c.F_Proyecto NOT IN (5) GROUP BY p.F_NomPro, c.F_ClaPro, l.F_ClaLot, c.F_FecApl, c.F_OrdCom, c.F_ClaDoc, c.F_FolRemi, c.F_User, c.F_Proyecto;");
                                    }
                                    while (rset.next()) {

                            %>
                            <tr>
                                <td><%=rset.getString(11)%></td>
                                <td><%=rset.getString(1)%></td>
                                <td class="text-center"><%=rset.getString(2)%></td>
                                <td class="text-center"><%=rset.getString(3)%></td>
                                <td class="text-center"><%=rset.getString(4)%></td>
                                <td class="text-center"><%=formatter.format(rset.getInt(5))%></td>
                                <td class="text-center"><%=rset.getString(6)%></td>
                                <td class="text-center"><%=rset.getString(7)%></td>
                                <td class="text-center"><%=rset.getString(14)%></td>
                                <td class="text-center"><%=rset.getString(8)%></td>
                                <td class="text-center"><%=rset.getString(9)%></td>
                                <td class="text-center"><%=rset.getString(10)%></td>
                                <td class="text-center"><%=formatterD.format(rset.getDouble(12))%></td>
                                <td class="text-center"><%=formatterD.format(rset.getDouble(13))%></td>
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
        <br><br><br>
    </body>
</html>
<%@include file="../jspf/piePagina.jspf" %>

<script src="../js/jquery-2.1.4.min.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/jquery-ui-1.10.3.custom.js"></script>
<script src="../js/bootstrap-datepicker.js"></script>
<script src="../js/jquery.dataTables.js"></script>
<script src="../js/dataTables.bootstrap.js"></script>
<script>
                                $(document).ready(function () {
                                    $('#datosCompras').dataTable();
                                    $("#Fecha").datepicker();
                                    $("#Fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
                                });
</script>