<%-- 
    Document   : generaAbastoCSV
    Created on : 21/04/2015, 09:15:47 AM
    Author     : Americo
--%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=Abasto_" + request.getParameter("F_ClaDoc") + ".csv");
    ConectionDB con = new ConectionDB();
    con.conectar();
    ResultSet rset = con.consulta("select * from v_transferencias where F_ClaDoc = '" + request.getParameter("F_ClaDoc") + "'");
    while (rset.next()) {
        out.println(rset.getString("F_ClaPro") + "," + rset.getString("F_DesPro") + "," + rset.getString("F_ClaLot") + "," + rset.getString("F_FecCad") + "," + rset.getString("F_CantSur") + "," + rset.getString("F_Origen") + "," + rset.getString("F_Cb"));
    }
    con.cierraConexion();
%>