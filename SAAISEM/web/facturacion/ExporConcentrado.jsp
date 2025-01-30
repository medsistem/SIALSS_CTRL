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
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String Fecha = request.getParameter("Fecha");
    int Ban = Integer.parseInt(request.getParameter("Ban"));
    String Nombre = "";
    if (Ban == 0) {
        Nombre = "ConcentradoRuta_Globla";
    } else if (Ban == 1) {
        Nombre = "ConcentradoRuta_CSR";
    } else if (Ban == 2) {
        Nombre = "ConcentradoRuta_CSU";
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"" + Nombre + ".xls");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-condensed table-striped" id="tablaMovMod">
                <thead>
                    <tr class="text-center">
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>
                        <td>Lote</td>
                        <td>Caducidad</td>
                        <td>Ubicaci&oacute;n</td>
                        <td>Cantidad</td>                    
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                ResultSet rset = null;
                                if (Ban == 0) {
                                    rset = con.consulta("SELECT F.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, F.F_Ubicacion, SUM(F_CantSur) FROM tb_factura F INNER JOIN tb_lote L ON F.F_ClaPro = L.F_ClaPro AND F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_FecEnt = '" + Fecha + "' AND F.F_CantSur > 0 AND U.F_Tipo IN ('RURAL','CSU') AND F.F_StsFact='A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, F.F_Ubicacion;");
                                } else if (Ban == 1) {
                                    rset = con.consulta("SELECT F.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, F.F_Ubicacion, SUM(F_CantSur) FROM tb_factura F INNER JOIN tb_lote L ON F.F_ClaPro = L.F_ClaPro AND F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_FecEnt = '" + Fecha + "' AND F.F_CantSur > 0 AND U.F_Tipo = 'RURAL' AND F.F_StsFact='A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, F.F_Ubicacion;");
                                } else if (Ban == 2) {
                                    rset = con.consulta("SELECT F.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, F.F_Ubicacion, SUM(F_CantSur) FROM tb_factura F INNER JOIN tb_lote L ON F.F_ClaPro = L.F_ClaPro AND F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_FecEnt = '" + Fecha + "' AND F.F_CantSur > 0 AND U.F_Tipo = 'CSU' AND F.F_StsFact='A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, F.F_Ubicacion;");
                                }

                                while (rset.next()) {


                    %>
                    <tr>
                        <td style="mso-number-format:'@'"><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>
                        <td style="mso-number-format:'@'"><%=rset.getString(3)%></td>
                        <td><%=rset.getString(4)%></td>
                        <td><%=rset.getString(5)%></td>
                        <td class="text-center" style="mso-number-format:'#,##0'"><%=rset.getString(6)%></td>

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