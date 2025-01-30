<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    ConectionDB con = new ConectionDB();
    HttpSession sesion = request.getSession();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"AbastoModula_" + df2.format(new Date()) + ".xls\"");
%>

<table>
    <tr>
        <td>Clave</td>
        <td>Descripcion</td>
        <td>Lote</td>
        <td>Caducidad</td>
        <td>Ubicacion</td>
        <td>Cantidad</td>
    </tr>
    <%        
        try {
            con.conectar();
            ResultSet rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FecCad, u.F_DesUbi, a.F_Cant, a.F_Id from tb_abasmodtemp a, tb_lote l, tb_medica m, tb_ubica u WHERE a.F_IdLote=l.F_IdLote and l.F_ClaPro = m.F_ClaPro and l.F_Ubica = u.F_ClaUbi and a.F_Id between '" + request.getParameter("ini") + "' and '" + request.getParameter("fin") + "' and a.F_Usuario = '" + sesion.getAttribute("nombre") + "'  ");
            while (rset.next()) {
    %>
    <tr>
        <td><%=rset.getString(1)%></td>
        <td><%=rset.getString(2)%></td>
        <td><%=rset.getString(3)%></td>
        <td><%=rset.getString(4)%></td>
        <td><%=rset.getString(5)%></td>
        <td><%=rset.getString(6)%></td>
    </tr>
    <%
            }
            con.cierraConexion();
        } catch (Exception e) {
            
        }
    %>
</table>