<%-- 
    Document   : gnrExistModula
    Created on : 3/07/2015, 12:11:36 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

    /**
     * descarga las existencias en módula
     */
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    ConectionDB con = new ConectionDB();
    ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
    ResultSet rset = null;

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias Modula.xls\"");
%>
<h3>Modula</h3>
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
            int totalModula = 0;
            try {

                conModula.conectar();
                con.conectar();
                /**
                 * Vista donde está la existencia en modula
                 */
                ResultSet rset4 = conModula.consulta("select * from VIEW_MODULA_UBICACION where SCO_GIAC!=0 order by SCO_ARTICOLO");
                while (rset4.next()) {
                    totalModula = totalModula + rset4.getInt("SCO_GIAC");
                    String Descrip = "";
                    /**
                     * Se saca la descripción de tb_medica
                     *
                     */

                    ResultSet rset5 = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + rset4.getString("SCO_ARTICOLO") + "'");
                    while (rset5.next()) {
                        Descrip = rset5.getString(1);
                    }
        %>
        <tr>
            <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset4.getString("SCO_ARTICOLO")%></td>
            <td><%=Descrip%></td>
            <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset4.getString("SCO_SUB1")%></td>
            <td><%=rset4.getString("SCO_DSCAD")%></td>
            <td><%=rset4.getString("UDC_UDC")%></td>
            <td><%=rset4.getString("SCO_POSI")%></td>
            <td><%=formatter.format(rset4.getInt("SCO_GIAC"))%></td>
        </tr>
        <%
                }
                conModula.cierraConexion();
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        %>
    </tbody>
</table>
<h3>Total Modula: <%=formatter.format(totalModula)%></h3>