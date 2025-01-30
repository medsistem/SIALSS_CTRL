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

    String fol_remi = "", orden_compra = "";
    try {
        fol_remi = request.getParameter("remision");
        orden_compra = request.getParameter("oc");
    } catch (Exception e) {

    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Exportar_OC_" + orden_compra + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <table>
            <%
            Date dNow = new Date();
            DateFormat ft = new SimpleDateFormat("dd/MM/yyyy'  'HH:mm:ss");
            String fechaDia = ft.format(dNow);
            %>
            <tr>
              <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
               <td colspan="8"><%=fechaDia%></td>
            </tr>
            <tr></tr>
            <tr valign="center">
                <th colspan="11"><h1>Validación OC</h1></th>
            </tr>
            <tr>
                <td colspan="5"><h4>No. OC = <%=orden_compra%></h4></td>
                <td colspan="5"> <h4>Remisión = <%=fol_remi%></h4> </td>
            </tr>            
        </table>
           <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Remisión</td>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Origen</td>
                                <td>Lote</td>
                                <td>Cantidad</td>
                                <td>Costo U</td>
                                <td>IVA</td>
                                <td>Importe</td>
                                <td>Caducidad</td>
                                <td>Marca</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        int banBtn = 0;
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar FROM tb_compratemp C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + orden_compra + "' and F_FolRemi = '" + fol_remi + "'  and F_Estado = '2';");
                                        while (rset.next()) {
                                            banBtn = 1;
                                            String F_FecCad = "", F_Cb = "", F_Marca = "";
                                            try {
                                                F_FecCad = rset.getString(5);
                                            } catch (Exception e) {
                                            }

                                            F_Cb = rset.getString("F_Cb");
                                            if (F_Cb.equals("")) {

                                                ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_lote WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' AND F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                while (rset2.next()) {
                                                    F_Cb = rset2.getString("F_Cb");
                                                    F_Marca = rset2.getString("F_ClaMar");
                                                }
                                            }

                                            if (F_Cb.equals("")) {
                                                ResultSet rset2 = con.consulta("SELECT F_Cb, F_ClaMar FROM tb_cb WHERE F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                while (rset2.next()) {
                                                    F_Cb = rset2.getString("F_Cb");
                                                    F_Marca = rset2.getString("F_ClaMar");
                                                }
                                            }
                                            F_Marca = rset.getString("F_DesMar");
                                            if (F_Marca.equals("")) {
                                                ResultSet rset2 = con.consulta("SELECT F_DesMar FROM tb_marca WHERE F_ClaMar = '" + F_Marca + "'");
                                                while (rset2.next()) {
                                                    F_Marca = rset2.getString("F_DesMar");
                                                }
                                            }

                                            if (F_Cb.equals(" ")) {
                                                F_Cb = "";
                                            }

                            %>
                            <tr>
                                <td><%=rset.getString("C.F_FolRemi")%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString("F_ClaPro")%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString("F_Origen")%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
                                <td><%=rset.getString(6)%></td>
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>
                                <td><%=F_FecCad%></td>
                                <td><%=F_Marca%></td>
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