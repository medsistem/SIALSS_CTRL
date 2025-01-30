<%-- 
    Document   : gnrInventario
    Created on : 16/04/2015, 03:30:11 PM
    Author     : Americo
--%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        //response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
    //ConectionDB_Inventarios conInv = new ConectionDB_Inventarios();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Inventario" + request.getParameter("idInv") + ".xls\"");

%>
<table  border = '1'>
    <thead>
        <tr>
            <td>Cla Uni</td>
            <td>Cla Pro</td>
            <td>Fecha</td>
            <td>Cantidad</td>
        </tr>
    </thead>
    <tbody>
        <%            try {
                con.conectar();
                ResultSet rset = con.consulta("select cla_mod, cla_prod, DATE_FORMAT(hora_ini, '%d/%m/%Y') as hora_ini, cant from v_inventarios where idInv = '" + request.getParameter("idInv") + "' and cla_mod = '" + request.getParameter("cla_mod") + "'");
                while (rset.next()) {
        %>
        <tr>
            <td><%=rset.getString("cla_mod")%></td>
            <td><%=rset.getString("cla_prod")%></td>
            <td><%=rset.getString("hora_ini")%></td>
            <td><%=rset.getString("cant")%></td>
        </tr>  
        <%
                }
                con.cierraConexion();
            } catch (Exception e) {
                out.println(e);
            }
        %>
    </tbody>
</table>