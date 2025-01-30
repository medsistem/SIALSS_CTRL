<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "", clave = "", Concepto = "", Proyecto = "", Origen = "", DesOrigen = "", DesProyecto = "", DesConcepto = "", Desclave="";
    try {
        //if (request.getParameter("accion").equals("buscar")) {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        clave = request.getParameter("clave");
        Concepto = request.getParameter("Concepto");
        Proyecto = request.getParameter("Proyecto");
        Origen = request.getParameter("Origen");
        DesProyecto = request.getParameter("DesProyecto");
        //}
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
    if (Concepto == null) {
        Concepto = "";
    }

    if (Proyecto == null) {
        Proyecto = "0";
    }

    if (Origen == null) {
        Origen = "0";
    }
    
    if(Origen.equals("0")){
        DesOrigen = "TODOS";
    }else if(Origen.equals("1")){
        DesOrigen = "ADMINISTRACIÓN";
    }else if(Origen.equals("2")){
        DesOrigen = "VENTA";
    }
    
    if(Proyecto.equals("0")){
        DesProyecto = "TODOS";
    }else {
        DesProyecto = DesProyecto;
    }
    
    if(Concepto.equals("")){
        DesConcepto = "TODOS";
    }else if(Concepto.equals("1")){
        DesConcepto = "ENTRADA POR COMPRA";
    }else if(Concepto.equals("51")){
        DesConcepto = "SALIDA POR FACTURACIÓN";
    }
    
    if(clave.equals("")){
        Desclave = "TODOS";
    }else {
        Desclave = clave;
    }
    
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Movimientos_Entradas_Salidas.xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Fecha Consulta:</td>
                                <td colspan="2"><%=fecha_ini%> AL <%=fecha_fin%></td>
                            </tr>
                            <tr>
                                <td>Clave:</td>
                                <td><%=Desclave%></td>
                            </tr>
                            <tr>
                                <td>Proyecto</td>
                                <td><%=DesProyecto%></td>
                            </tr>
                            <tr>
                                <td>Origen</td>
                                <td><%=DesOrigen%></td>
                            </tr>
                            <tr>
                                <td>Concepto</td>
                                <td><%=DesConcepto%></td>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <br />
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Des/Mov</td>
                                <td>Clave</td>
                                <td>Descripci&oacute;n</td>
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

                                        if (Proyecto.equals("0")) {
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
                                        if (Concepto != "") {
                                            ban2 = 1;
                                            Concep = " m.F_ConMov IN (" + Concepto + ") ";
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

                                        ResultSet rset = con.consulta("SELECT C.F_DesCon, F_ProMov, MD.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_CantMov) AS F_CantMov, O.F_DesOri, IFNULL(mc.F_DesMar, '') AS F_DesMar, IFNULL(pr.F_NomPro, '') AS F_NomPro, IFNULL(cp.F_OrdCom,'') AS F_OrdCom FROM tb_movinv m INNER JOIN tb_lote l ON m.F_ProMov = l.F_ClaPro AND m.F_LotMov = l.F_FolLot AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_medica MD ON m.F_ProMov = MD.F_ClaPro INNER JOIN tb_coninv C ON m.F_ConMov = C.F_IdCon INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen O ON l.F_Origen = O.F_ClaOri LEFT JOIN tb_proveedor pr ON l.F_ClaPrv = pr.F_ClaProve LEFT JOIN tb_marca mc ON l.F_ClaMar = mc.F_ClaMar LEFT JOIN(SELECT F_ClaPro, F_Lote, GROUP_CONCAT(F_OrdCom) AS F_OrdCom FROM tb_compra GROUP BY F_ClaPro, F_Lote) AS cp ON l.F_ClaPro=cp.F_ClaPro AND l.F_FolLot=cp.F_Lote WHERE " + Query + " AND m.F_ConMov IN (1, 51) " + AND + " " + ANDOrigen + " GROUP BY F_ConMov,F_ProMov,l.F_ClaLot,F_FecCad,l.F_Origen;");
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
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
                <br />
                <br />
                <br />
            </div>
        </div>
    </body>
</html>