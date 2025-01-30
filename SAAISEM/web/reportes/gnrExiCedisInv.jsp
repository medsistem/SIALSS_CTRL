<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
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

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias_Cedis_Mes_Global.xls\"");
    try {
                            con.conectar();
                            ResultSet Consulta = null;
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table>
                <tr>
               <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                </tr><tr></tr><tr></tr>
                <tr>
                    <th colspan="5"><h1>Reporte Global de Existencia en Cedis</h1></th>
                </tr>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                %>
                <tr>
                    <td colspan="5"><%=fechaDia%></td>
                </tr>
            
            </table>
            
            
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <th class="text-center">Mes</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><th style='mso-number-format:"@"'><%=Consulta.getString(1)%></th><%}%>                       
                    </tr>
                    <tr>
                        <th>(+)Inv. Inicio Mes</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(2))%></td><%}%> 
                    </tr>
                    <tr>
                        <th>(+)Entrada Por Inventario Inicial</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(3))%></td><%}%>     
                    </tr>
                    <tr>
                        <th>(+)Entrada Por Compra</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(4))%></td><%}%> 
                    </tr>
                    <tr>
                        <th>(+)Entrada Por Devoluci&oacute;n</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(5))%></td><%}%> 
                    </tr>
                    <tr>                        
                        <th>(+)Entrada Por Otros Movimientos</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(6))%></td><%}%> 
                    </tr>
                    <tr>                        
                        <th>SubTotal Entradas</th><%Consulta = con.consulta("SELECT SUM(F_InvMes+F_InvIni+F_Compra+F_Devo+F_EoMov) FROM tb_invmescedisglobal GROUP BY F_Mes ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(1))%></td><%}%>  
                    </tr>
                    <tr></tr>
                    <tr>                        
                        <th>(-)Salida Por Facturaci&oacute;n</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(7))%></td><%}%> 
                    </tr>
                    <tr>
                        <th>(-)Salida Por Otros Movimientos</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(8))%></td><%}%> 
                    </tr>
                    <tr>                        
                        <th>SubTotal Salidas</th><%Consulta = con.consulta("SELECT SUM(F_SalFact+F_SalOtroMov) FROM tb_invmescedisglobal GROUP BY F_Mes ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(1))%></td><%}%> 
                    </tr>
                    <tr></tr>
                    <tr>
                        <th>Inv. Final Mes</th><%Consulta = con.consulta("SELECT * FROM tb_invmescedisglobal ORDER BY F_Anno,F_Nmes+0;");
                            while (Consulta.next()) {%><td><%=formatter.format(Consulta.getInt(9))%></td><%}%>
                    </tr>                    
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <br />
       
        

    </div>
</div>
                    <%
                            
                            con.cierraConexion();
                        } catch (Exception e) {

                        }


                    %>