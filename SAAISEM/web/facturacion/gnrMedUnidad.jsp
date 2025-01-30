<%-- 
    Document   : gnkKardexClave
    Created on : 22-oct-2014, 8:39:49
    Author     : amerikillo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    ConectionDB con = new ConectionDB();
    String Unidad = request.getParameter("Unidad");

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Medicamento_" + Unidad + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <table border="1">
            <thead> 
                <tr>
                    <td>Unidad</td>
                    <td>Clave</td>
                    <td>Descripción</td>
                    <td>Tipo Medicamento</td>
                    <td>Autorizado</td>
                    <td>CPM</td>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        con.conectar();
                        ResultSet rset = null;
                        rset = con.consulta("SELECT U.F_NomCli, M.F_ClaPro, MD.F_DesPro, CASE WHEN F_TipMed = '2505' THEN 'MAT. CURACION' ELSE 'MEDICAMENTO' END AS F_TipMed, CASE WHEN M.F_Autorizado = '1' THEN 'SI' ELSE 'NO' END AS F_Autorizado, M.F_CantMax FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_Autorizado != 2 AND U.F_Proyecto = 2 AND M.F_ClaUni = '" + Unidad + "';");

                        while (rset.next()) {
                %>
                <tr>
                    <td ><%=rset.getString(1)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                    <td ><%=rset.getString(3)%></td>
                    <td ><%=rset.getString(4)%></td>
                    <td ><%=rset.getString(5)%></td>
                    <td ><%=rset.getString(6)%></td>
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
    </body>
</html>
