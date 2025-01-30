<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"ComparativaOCClave.xls\"");
%>
<table border="1">
    <tr>
        <td>Clave</td>
        <td>Proveedor</td>
        <td>Fecha Cap</td>
        <td>Clave</td>
        <td>Cant Recibir</td>
        <td>Cant Recibida</td>
        <td>Diferencia</td>
    </tr>
    <%
        try {

            ConectionDB con = new ConectionDB();

            con.conectar();
            ResultSet rset = con.consulta("SELECT o.F_NoCompra, p.F_NomPro, DATE_FORMAT(o.F_Fecha, '%d/%m/%Y') AS F_Fecha, o.F_Clave, SUM(o.F_Cant), DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') AS F_FecSur, o.F_StsPed FROM tb_pedidoisem o, tb_proveedor p WHERE o.F_Provee = F_ClaProve AND F_StsPed = 1 GROUP BY o.F_Clave, o.F_NoCompra ORDER BY o.F_NoCompra;");
            while (rset.next()) {
                int cantRecbida = 0;
                ResultSet rset2 = con.consulta("SELECT F_OrdCom, F_ClaPro, SUM(F_CanCom) FROM tb_compra where F_OrdCom = '" + rset.getString("F_NoCompra") + "' and F_ClaPro = '" + rset.getString("F_Clave") + "'  GROUP BY F_ClaPro ;");
                while (rset2.next()) {
                    cantRecbida = rset2.getInt(3);
                }
                
                int recibir = rset.getInt(5)-cantRecbida;
    %>
    <tr>
        <td><%=rset.getString(1)%></td>
        <td><%=rset.getString(2)%></td>
        <td><%=rset.getString(3)%></td>
        <td><%=rset.getString(4)%></td>
        <td><%=rset.getString(5)%></td>
        <td><%=cantRecbida%></td>
        <td><%=recibir%></td>
    </tr>
    <%
            }
            con.cierraConexion();
        } catch (Exception e) {

        }
    %>
</table>