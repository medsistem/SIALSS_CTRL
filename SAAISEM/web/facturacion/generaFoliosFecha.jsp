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

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Folio_" + request.getParameter("F_ClaDoc") + "_" + request.getParameter("Unidad") + "_" + request.getParameter("Nombre") + "_" + request.getParameter("Tipo") + ".xls\"");
    
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
                        <td>Cantidad Sur.</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                ResultSet rset = con.consulta("SELECT F.F_ClaPro,M.F_DesPro,SUM(F.F_CantReq),SUM(F.F_CantSur) FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_ClaPro=L.F_ClaPro AND F.F_Ubicacion=L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro WHERE F_ClaDoc='" + request.getParameter("F_ClaDoc") + "' AND F_StsFact='A' GROUP BY F.F_ClaPro;");
                                while (rset.next()) {
                    %>
                    <tr>
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>                        
                        <td><%=rset.getString(3)%></td>  
                        <td><%=rset.getString(4)%></td>  
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


