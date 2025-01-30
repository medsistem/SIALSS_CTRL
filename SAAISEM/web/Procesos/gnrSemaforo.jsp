<%-- 
    Document   : descargaInventario
    Created on : 28/11/2014, 08:45:23 AM
    Author     : Americo
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatter2 = new DecimalFormat("#,###,###.##");
    String Kardex = "";
    try {
        Kardex = request.getParameter("Kardex");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    String Semaforo = "";
    if (Kardex.equals("1")) {
        Semaforo = "Menor o Igual 6 meses";
    }
    if (Kardex.equals("2")) {
        Semaforo = "Mayor a 6 meses";
    }
    if (Kardex.equals("4")) {
        Semaforo = "CADUCADOS";
    } else {
        Semaforo = "Mayor a 9 meses";
    }
    ConectionDB con = new ConectionDB();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Semaforizacion_" + df2.format(new Date()) + "_.xls\"");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <table>
            <%
            Date dNow = new Date();
            DateFormat ft = new SimpleDateFormat("dd/MM/yyyy'  'HH:mm:ss");
            String fechaDia = ft.format(dNow);
            %>
            <tr>
               <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                <td colspan="5"><h4><%=fechaDia%></h4></td>
            </tr>
            <tr></tr><tr></tr>
            <tr valign="center">
                <th colspan="7"><h1>Semaforización</h1></th>
            </tr>            
        </table>
        <table border="1">
            <tr>
                <td colspan="7" class="text-center" style="alignment-adjust: central"><%=Semaforo%></td>
            </tr>
            <tr>
                <td>Clave</td>
                <td>Descripción</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Cantidad</td>
                <td>Origen</td>
                <td>Proyecto</td>
            </tr>
            <%
                try {
                    con.conectar();
                    ResultSet rset = null;
                   if (Kardex.equals("1")) {
                           rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad >= CURDATE() AND F_FecCad <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND l.F_Ubica not in ('CADUCADOS') GROUP BY l.F_ClaPro, l.F_ClaLot,l.F_FecCad, l.F_Origen,l.F_Proyecto  ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");
                       } else if (Kardex.equals("2")) {
                           rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad > DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND l.F_Ubica NOT IN ('AT', 'A0T','AT2','AT3','AT4') GROUP BY  l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");
                       } else if (Kardex.equals("4")) {
                           rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad < CURDATE() AND l.F_Ubica NOT IN ('AT', 'A0T','AT2','AT3','AT4') GROUP BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");
                       }
                       while (rset.next()) {
            %>
            <tr>
                <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                <td><%=rset.getString(2)%></td>
                <td style="mso-number-format:'@';"><%=rset.getString(3)%></td>
                <td><%=rset.getString(4)%></td>
                <td><%=formatter.format(rset.getInt(5))%></td>
                <td><%=rset.getString(7)%></td>
                <td><%=rset.getString(9)%></td>
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