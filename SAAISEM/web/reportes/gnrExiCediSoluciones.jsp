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
    int Total = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias_Soluciones.xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>                        
                        <td>Lote</td>
                        <td>Caducidad</td>                        
                        <td>Cantidad</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                String Ubicaciones = "";
                                ResultSet rset = null;
                                ResultSet Consulta = null;
                                ResultSet rsetMed = null;

                                Consulta = con.consulta("SELECT F_Ubica FROM tb_ubicasoluciones;");
                                if (Consulta.next()) {
                                    Ubicaciones = Consulta.getString(1);
                                }

                                rset = con.consulta("SELECT M.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F_ExiLot) AS F_ExiLot FROM tb_medica M INNER JOIN tb_lote L ON M.F_ClaPro=L.F_ClaPro WHERE M.F_N30=1 AND L.F_ExiLot>0 AND L.F_Ubica IN (" + Ubicaciones + ") GROUP BY M.F_ClaPro,L.F_ClaLot,L.F_FecCad;");
                                while (rset.next()) {

                    %>
                    <tr>                                        
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=rset.getString(3)%></td>
                        <td><%=rset.getString(4)%></td>
                        <td><%=formatter.format(rset.getInt(5))%></td>
                    </tr>
                    <%

                                }

                            } catch (Exception e) {
                                con.cierraConexion();
                            }

                        } catch (Exception e) {
                            con.cierraConexion();
                        }
                    %>

                </tbody>
            </table>
        </div>
    </div>
</div>