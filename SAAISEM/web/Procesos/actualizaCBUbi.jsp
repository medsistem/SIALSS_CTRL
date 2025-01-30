<%@page import="conn.ConectionDB_Linux"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<table border="1">
    <%
        try {

            ConectionDB_Linux con = new ConectionDB_Linux();

            con.conectar();
            ResultSet rset = con.consulta("select * from tb_ubitemp");
            while (rset.next()) {
                con.insertar("update tb_ubica set F_Cb = '" + rset.getString(3) + "' where F_ClaUbi = '" + rset.getString(1) + "' ");
            }
            con.cierraConexion();
        } catch (Exception e) {

        }
    %>
</table>