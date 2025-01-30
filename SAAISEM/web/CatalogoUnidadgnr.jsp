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
        //response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String Unidad = "";
    try {
        Unidad = request.getParameter("Unidad");
    } catch (Exception e) {

    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Catalogo_Unidad.xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
              <div class="panel panel-info">
                <div class="panel-body">
                    <table>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                    %>
                <tr>
                    <!--<td><img src="https://4.bp.blogspot.com/-QPFcJij97lE/XkwwIpM6omI/AAAAAAAABcA/GwpuompAg60ucAtDnYPBGkf-A6SwPHAYwCLcBGAsYHQ/s1600/logoMdf.png" id="LogoMdf"</td>-->
                <td colspan="5"> <%=fechaDia%></td>
                </tr><tr></tr><tr></tr>
                <tr>
                    <th colspan="6"><h1>Catálogo por Unidad</h1></th>
                </tr>
                <tr></tr>                
            </table>
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Clave Unidad</td>
                                <td>Nombre Unidad</td>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Cantidad</td>
                                <td>Autorizado</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        ResultSet rset = null;
                                        String Condicion = "";
                                        
                                        if (Unidad.equals("")) {
                                            rset = con.consulta("SELECT U.F_ClaCli, U.F_NomCli, M.F_ClaPro, MD.F_DesPro, M.F_CantMax, CASE WHEN M.F_Autorizado = 1 THEN 'SI' ELSE 'NO' END AS STS, F_Id, F_Autorizado FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE U.F_Proyecto = 2 AND M.F_Tipo !='RURAL';");
                                            System.out.println("Vacio= SELECT U.F_ClaCli, U.F_NomCli, M.F_ClaPro, MD.F_DesPro, M.F_CantMax, CASE WHEN M.F_Autorizado = 1 THEN 'SI' ELSE 'NO' END AS STS, F_Id, F_Autorizado FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE U.F_Proyecto = 2 AND M.F_Tipo !='RURAL';");
                                        } else {
                                            rset = con.consulta("SELECT U.F_ClaCli, U.F_NomCli, M.F_ClaPro, MD.F_DesPro, M.F_CantMax, CASE WHEN M.F_Autorizado = 1 THEN 'SI' ELSE 'NO' END AS STS, F_Id, F_Autorizado FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_ClaUni='" + Unidad + "';");
                                            System.out.println("Datos= SELECT U.F_ClaCli, U.F_NomCli, M.F_ClaPro, MD.F_DesPro, M.F_CantMax, CASE WHEN M.F_Autorizado = 1 THEN 'SI' ELSE 'NO' END AS STS, F_Id, F_Autorizado FROM tb_medicaunidad M INNER JOIN tb_uniatn U ON M.F_ClaUni = U.F_ClaCli INNER JOIN tb_medica MD ON M.F_ClaPro = MD.F_ClaPro WHERE M.F_ClaUni='" + Unidad + "';");
                                        }
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>
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
    </body>
</html>