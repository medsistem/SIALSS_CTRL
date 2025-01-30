<%-- 
    Document   : descargaInventario
    Created on : 28/11/2014, 08:45:23 AM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    ConectionDB con = new ConectionDB();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias_" + df2.format(new Date()) + "_.xls\"");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <table border="1">
            <tr>
                <td>PROYECTO</td>
                <td>CLAVE</td>
                <td>Descripción</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Fec Fab</td>
                <td>Marca</td>
                <td>Proveedor</td>
                <td>Existencia</td>
                <td>Ubicacion</td>
                <td>Origen</td>
                <td>CB</td>
            </tr>
            <%
                try {
                    con.conectar();
                    String UbicaCross = "";
                    ResultSet rset2 = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                    if (rset2.next()) {
                        UbicaCross = rset2.getString(1);
                    }
                    UbicaCross = "'CROSSDOCKMORELIA'," + UbicaCross;
                    ResultSet rset = con.consulta("select * from v_existencias where F_ExiLot!=0 AND F_Ubica IN (" + UbicaCross + ");");
                    while (rset.next()) {
            %>
            <tr>
                <td><%=rset.getString("F_DesProy")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_ClaPro")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_DesPro")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_ClaLot")%></td>
                <td><%=rset.getString("F_FecCad")%></td>
                <td><%=rset.getString("F_FecFab")%></td>
                <td><%=rset.getString("F_DesMar")%></td>
                <td><%=rset.getString("F_NomPro")%></td>
                <td><%=rset.getString("F_ExiLot")%></td>
                <td><%=rset.getString("F_DesUbi")%></td>
                <td><%=rset.getString("F_DesOri")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_Cb")%></td>
            </tr>
            <%
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
        </table>
    </body>
</html>