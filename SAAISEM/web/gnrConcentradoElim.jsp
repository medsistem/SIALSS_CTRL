<%-- 
    Document   : gnrConcentrado
    Created on : 4/09/2014, 11:10:51 AM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ConectionDB con = new ConectionDB();
    String cli = "", fec = "";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"ConcentradoGlobal.xls\"");
%>
<table border="1">
    <tr>
        <td>Punto de Entrega</td>
        <td>Fecha de Entrega</td>
        <td>CB</td>
        <td>Clave</td>
        <td>Descripción</td>
        <td>Lote</td>
        <td>Caducidad</td>
        <td>Cajas</td>
        <td>Resto</td>
        <td>Piezas Totales</td>
        <td>Ubicación</td>
    </tr>
    <%        try {
            con.conectar();
            try {
                ResultSet rset = con.consulta("SELECT u.F_NomCli, DATE_FORMAT(f.F_FecEnt, '%d/%m/%Y') as FecEnt, l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') as FecCad, (f.F_Cant+0) as F_Cant, l.F_Ubica, f.F_IdFact, l.F_Cb, p.F_Pzs, (f.F_Cant DIV p.F_Pzs) as Cajas, (f.F_Cant MOD p.F_Pzs) as Resto, m.F_DesPro, f.F_Id, f.F_FecEnt FROM tb_facttemp_elim f, tb_lote l, tb_uniatn u, tb_pzxcaja p, tb_medica m WHERE f.F_IdLot = l.F_IdLote AND f.F_ClaCli = u.F_ClaCli AND p.F_ClaPro = l.F_ClaPro AND m.F_ClaPro = l.F_ClaPro AND f.F_IdFact='" + request.getParameter("fol_gnkl") + "' group by f.F_Id;");
                while (rset.next()) {
                    cli = rset.getString("F_NomCli");
                    fec = rset.getString("F_FecEnt");
    %>
    <tr>
        <td><%=rset.getString("F_NomCli")%></td>
        <td><%=rset.getString("FecEnt")%></td>
        <td><%=rset.getString("F_Cb")%></td>
        <td><%=rset.getString("F_ClaPro")%></td>
        <td><%=rset.getString("F_DesPro")%></td>
        <td><%=rset.getString("F_ClaLot")%></td>
        <td><%=rset.getString("FecCad")%></td>
        <td><%=rset.getString("Cajas")%></td>
        <td><%=rset.getString("Resto")%></td>
        <td><%=rset.getString("F_Cant")%></td>
        <td><%=rset.getString("F_Ubica")%></td>
    </tr>
    <%
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            con.cierraConexion();
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    %>
</table>