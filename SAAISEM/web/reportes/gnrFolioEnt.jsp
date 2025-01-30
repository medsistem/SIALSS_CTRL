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
    int Total = 0, TotalMes = 0, TotalIni = 0, TotalComp = 0, TotalDev = 0, TotalOMov = 0, TotalFact = 0, TotalSMov = 0, Cant = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Comparativa_Folios_Lerma_Vs_Sendero.xls\"");
    try {
        con.conectar();
        ResultSet Consulta = null;
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Fecha Elaboraci&oacute;n Lerma</td>
                        <td>Fecha Entrega Lerma</td>
                        <td>Fecha Ingreso Sendero</td>
                        <td>Folio Lerma</td>
                        <td>Cant. Lerma</td>
                        <td>Cant. Sendero</td>
                        <td>Diferencia</td>
                    </tr>            
                </thead>
                <tbody>
                    <%
                        int Lerma = 0, Sende = 0, Dife = 0;
                        Consulta = con.consulta("SELECT F_FecApl,F_FecEnt,F_FeCap,F_ClaDoc,SUM(F_CantLerma) AS F_CantLerma,SUM(F_CantSen) AS F_CantSen,(SUM(F_CantLerma)-SUM(F_CantSen)) AS F_Dife FROM tb_folioentrega GROUP BY F_ClaDoc ORDER BY F_ClaDoc+0;");
                        while (Consulta.next()) {
                            Lerma = Lerma + Consulta.getInt(5);
                            Sende = Sende + Consulta.getInt(6);
                            Dife = Dife + Consulta.getInt(7);
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Consulta.getInt(5))%></td>
                        <td><%=formatter.format(Consulta.getInt(6))%></td>                        
                        <td><%=formatter.format(Consulta.getInt(7))%></td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: right">Total Piezas Lerma Vs. Sendero</td>
                        <td><%=formatter.format(Lerma)%></td>
                        <td><%=formatter.format(Sende)%></td>
                        <td><%=formatter.format(Dife)%></td>
                    </tr>


                </tbody>
            </table>
        </div>
    </div>
</div>
<%
        con.cierraConexion();
    } catch (Exception e) {

    }


%>