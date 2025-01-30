<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

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
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "", clave = "", ClaCli = "",Origen="";
    try {
        //if (request.getParameter("accion").equals("buscar")) {
            fecha_ini = request.getParameter("fecha_ini");
            fecha_fin = request.getParameter("fecha_fin");
            Origen = request.getParameter("Origen");
            
        //}
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";        
    }
    if (fecha_fin == null) {
        fecha_fin = "";        
    }
    if(Origen == null){
        Origen="";
    }
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Reporteador_"+fecha_ini+"_al_"+fecha_fin+".xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table>            
                <tr>
                    <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                <tr></tr>
                    <th colspan="5"><h1>Requerido Vs Surtido</h1></th>
                </tr>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                %>
                <tr valign="left">
                    <td colspan="5"><h4><%=fechaDia%></h4></td>
                </tr>
            </table>
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>
                        <td>Origen</td>
                        <td>Cantidad Req.</td>
                        <td>Cantidad Sur.</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                int CantReq=0,CantSur=0;
                                ResultSet rset = null;
                                if ( fecha_ini !="" && fecha_fin !=""){
                                    if(Origen !=""){
                                        rset = con.consulta("SELECT f.F_ClaPro,m.F_DesPro,FORMAT(SUM(F_CantReq),0),FORMAT(SUM(F_CantSur),0),SUM(F_CantReq),SUM(F_CantSur),l.F_Origen FROM tb_factura f LEFT JOIN tb_medica m on f.F_ClaPro=m.F_ClaPro INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE f.F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' and l.F_Origen='"+Origen+"' GROUP BY f.F_ClaPro;");
                                    }else{
                                        rset = con.consulta("SELECT f.F_ClaPro,m.F_DesPro,FORMAT(SUM(F_CantReq),0),FORMAT(SUM(F_CantSur),0),SUM(F_CantReq),SUM(F_CantSur),l.F_Origen FROM tb_factura f LEFT JOIN tb_medica m on f.F_ClaPro=m.F_ClaPro INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE f.F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' GROUP BY f.F_ClaPro;");
                                    }
                                    
                                while (rset.next()) {
                    %>
                    <tr>
                        <td><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>
                        <td><%=rset.getString(7)%></td>
                        <td><%=rset.getString(3)%></td>
                        <td><%=rset.getString(4)%></td>                        
                    </tr>
                    <%
                            CantReq =CantReq + rset.getInt(5);
                            CantSur =CantSur + rset.getInt(6);
                                }
                     %>
                    <tr>
                       <td></td>
                       <td>Total</td>
                       <td></td>
                       <td><%=formatter.format(CantReq)%></td>
                       <td><%=formatter.format(CantSur)%></td>
                   </tr>
                    <%
                        CantReq=0;
                        CantSur=0;           
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
        <br />
        <br />
        <br />
        <!--div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosfirmas" border="0">
                <tr>
                    <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                    <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                </tr>
                <tr>
                    <td colspan="2"><h5>RESPONSABLE MEDICO</h5></td>
                    <td colspan="3"><h5>COORDINADOR O ADMINISTRADOR MUNICIPAL</h5></td>
                </tr>
            </table>
        </div>
        </div-->
        
               
    </div>
</div>