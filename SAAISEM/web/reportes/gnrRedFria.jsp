<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    int Total = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_fin = "",fecha_ini = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }

    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Red_Fria_"+ fecha_ini +"_" + fecha_fin + ".xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table>
                <tr>
               <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
               <th > <h1>Reporte de Red Fria</h1></th>
                </tr>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                    %>
                <tr>
                    <td colspan="4"><h4><%=fechaDia%></h4></td>
                </tr>
                <tr></tr>
            </table>
            
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Folio</td>
                        <td>Nombre Unidad</td>
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>                        
                        <td>Lote</td>
                        <td>Caducidad</td>                        
                        <td>Cantidad</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                
                                ResultSet Consulta = null;
                                
                             

                                    Consulta = con.consulta("SELECT F.F_ClaDoc,U.F_NomCli,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,Sum(F.F_CantSur) AS F_CantSur FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_ClaPro=L.F_ClaPro AND F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_redfria R ON F.F_ClaPro=R.F_ClaPro WHERE F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F.F_StsFact='A' AND F.F_CantSur>0 GROUP BY F.F_ClaDoc,U.F_NomCli,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,L.F_FecCad;");
                                    while (Consulta.next()) {
                                        

                    %>
                    <tr>                                        
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(5)%></td>
                        <td><%=Consulta.getString(6)%></td>
                        <td><%=Consulta.getString(7)%></td>
                    </tr>
                    <%
                            
                        }
                     
                            } catch (Exception e) {

                            }
                            con.cierraConexion();
                        } catch (Exception e) {

                        }


                    %>

                </tbody>
            </table>
        </div>       
    </div>
</div>