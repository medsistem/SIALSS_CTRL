<%@page import="conn.ConectionDB_Linux"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<table border="1">
    <tr>
        <td>Clave</td>
        <td>Descrip</td>
        <td>Piezas x Caja</td>
    </tr>
    <%
        try {

            ConectionDB_Linux con = new ConectionDB();

            con.conectar();
            ResultSet rset = con.consulta("select c.*, m.F_DesPro from tb_compraregistro c, tb_medica m, tb_marca mar where mar.F_ClaMar c.F_ClaPro = m.F_ClaPro order by c.F_ClaPro");
            while (rset.next()) {
                double PzsCaja=0.0, Pzs=0.0, Cajas=0.0, Resto=0.0;
                Pzs = rset.getDouble("F_Pz");
                Cajas = rset.getDouble("F_Cajas");
                Resto = rset.getDouble("F_Resto");
                        if (Resto !=0){
                            Pzs = Pzs-Resto;
                            PzsCaja =Pzs/(Cajas-1) ;
                        } else {
                            PzsCaja =Pzs/(Cajas) ;
                        }
                %>
    <tr>
        <td><%=rset.getString("c.F_ClaPro")%></td>
        <td><%=rset.getString("m.F_DesPro")%></td>
        <td><%=PzsCaja%></td>
    </tr>
    <%
            }
            con.cierraConexion();
        } catch (Exception e) {

        }
    %>
</table>