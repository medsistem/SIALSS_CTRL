<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

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
            
        //}
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";        
    }
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Concentrado_del_dia_"+fecha_ini+".xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>
                        <td>Cantidad Req.</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                int CantReq=0,CantSur=0;
                                ResultSet rset = null;
                                if ( fecha_ini !=""){
                                        rset = con.consulta("SELECT ur.F_ClaPro,m.F_DesPro,SUM(ur.F_PiezasReq) FROM tb_unireq ur INNER JOIN tb_uniatn un on ur.F_ClaUni=un.F_ClaCli INNER JOIN tb_medica m on ur.F_ClaPro=m.F_ClaPro WHERE F_Fecha='"+fecha_ini+"' AND F_Status='0' AND un.F_Tipo='3' GROUP BY ur.F_ClaPro;");
                                    
                                    
                                while (rset.next()) {
                    %>
                    <tr>
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>
                        <td><%=rset.getString(3)%></td>                        
                    </tr>
                    <%
                            CantReq =CantReq + rset.getInt(3);
                                }
                     %>
                    <tr>
                       <td></td>
                       <td>Total</td>
                       <td><%=formatter.format(CantReq)%></td>
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
    </div>
</div>