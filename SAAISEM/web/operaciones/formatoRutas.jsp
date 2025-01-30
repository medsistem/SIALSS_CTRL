<%-- 
    Document   : formatoRutas
    Created on : 20/03/2015, 03:41:15 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ConectionDB con = new ConectionDB();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"FormatoFechaRuta.xls\"");
%>
<table border="1">
    <tr>
        <td>Juris</td>
        <td>Muni</td>
        <td>Clave Uni</td>
        <td>Nom Uni</td>
        <td>Ruta</td>
        <td>Loc Plano</td>
        <td>Fecha</td>
    </tr>
    <%
        try {
            con.conectar();
            ResultSet rset = con.consulta("select * from v_fechaRutas");
            while (rset.next()) {
    %>
    <tr>
        <td><%=rset.getString("F_ClaJur")%></td>
        <td><%=rset.getString("F_DesMunIS")%></td>
        <td><%=rset.getString("F_ClaCli")%></td>
        <td><%=rset.getString("F_NomCli")%></td>
        <td><%=rset.getString("F_Ruta")%></td>
        <td><%=rset.getString("F_LocPlano")%></td>
        <td><%=rset.getString("F_Fecha")%></td>
    </tr> 
    <%
            }
            con.cierraConexion();
        } catch (Exception e) {

        }
    %>
</table>