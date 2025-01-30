<%-- 
    Document   : descargaInventario
    Created on : 28/11/2014, 08:45:23 AM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
        <table>
            <%
            Date dNow = new Date();
            DateFormat ft = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
            String fechaDia = ft.format(dNow);            
            %>
            <tr>
            <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
           <td colspan="3"><h4><%=fechaDia%></h4></td>
            </tr>
            
            <tr>
                <th colspan="5"><h1>Existencia</h1></th>
            </tr>
            <tr></tr>
        </table>

        <table border="1">
            <tr>
                <td>PROYECTO</td>
                <td>Ubicación</td>
                <td>IdLote</td>
                <td>CLAVE</td>
                <td>Descripción</td>
                <td>Presentación</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Fec Fab</td>
                <td>Marca</td>
                <td>Proveedor</td>
                <td>Disponible</td>
                <td>Apartado</td>
                <td>Existencia</td>
                <td>Origen</td>
                <td>Bodega</td>
                <td>CB</td>
            </tr>
            <%
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from v_existencias where F_ExiLot!=0");
                    while (rset.next()) {
            %>
            <tr>
                <td><%=rset.getString("F_DesProy")%></td>
                <td><%=rset.getString("F_DesUbi")%></td>
                <td><%=rset.getString("F_IdLote")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_ClaPro")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_DesPro")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_PrePro")%></td>
                <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset.getString("F_ClaLot")%></td>
                <td><%=rset.getString("F_FecCad")%></td>
                <td><%=rset.getString("F_FecFab")%></td>
                <td><%=rset.getString("F_DesMar")%></td>
                <td><%=rset.getString("F_NomPro")%></td>
                <td><%=rset.getString("disponible")%></td>
                <td><%=rset.getString("apartado")%></td>
                <td><%=rset.getString("F_ExiLot")%></td>
                <td><%=rset.getString("F_DesOri")%></td>
                <td><%=rset.getString("Lugar")%></td>
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