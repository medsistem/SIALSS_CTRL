<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
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

    String fecha_fin = "", clave = "", Descrip = "", Radio = "";
    try {
        fecha_fin = request.getParameter("fecha_fin");
        clave = request.getParameter("clave");
        Descrip = request.getParameter("Descrip");
        Radio = request.getParameter("Radio");
    } catch (Exception e) {

    }

    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (clave == null) {
        clave = "";
    }
    if (Descrip == null) {
        Descrip = "";
    }
    if (Radio == null) {
        Radio = "Lote";
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias_Cedis_" + fecha_fin + ".xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table>
                <tr>
               <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                </tr><tr></tr><tr></tr>
                <tr>
                    <th colspan="3"><h1>Consulta de Existencia en Cedis</h1></th>
                </tr>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                
                    String fechaDia = ft.format(dNow);
                    %>
                <tr>
                    <td colspan="3"><%=fechaDia%></td>
                </tr>
            </table>
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Clave</td>
                        <td>Descripci&oacute;n</td>
                        <%if (Radio.equals("Lote")) {%>
                        <td>Lote</td>
                        <td>Caducidad</td>
                        <%}%>
                        <td>Cantidad</td>
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                String FechaFol = "", Clave = "", Concep = "", Query = "", Cadu = "", Lote = "", TotalMed = "";
                                int ban = 0, ban1 = 0, ban2 = 0, Existencia = 0;
                                if (clave != "") {
                                    ban = 1;
                                    Clave = " M.F_ProMov='" + clave + "' ";
                                }
                                if (fecha_fin != "") {
                                    ban1 = 1;
                                    FechaFol = " M.F_FecMov <= '" + fecha_fin + "' ";
                                }
                                if (Descrip != "") {
                                    ban2 = 1;
                                    Concep = " MD.F_DesPro = '" + Descrip + "' ";
                                }
                                if (ban == 1 && ban1 == 1) {
                                    Query = Clave + " AND " + FechaFol;
                                } else if (ban1 == 1 && ban2 == 1) {
                                    Query = FechaFol + " AND " + Concep;
                                } else if (ban == 1) {
                                    Query = Clave;
                                } else if (ban1 == 1) {
                                    Query = FechaFol;
                                } else if (ban2 == 1) {
                                    Query = Concep;
                                }

                                ResultSet rset = null;
                                ResultSet Consulta = null;
                                ResultSet rsetMed = null;

                                if (Radio.equals("Lote")) {

                                    rset = con.consulta("SELECT MD.F_ClaPro,M.F_LotMov,MD.F_DesPro,SUM(M.F_CantMov*M.F_SigMov) FROM tb_movinv M INNER JOIN tb_medica MD ON M.F_ProMov=MD.F_ClaPro WHERE " + Query + " GROUP BY M.F_ProMov,M.F_LotMov;");
                                    while (rset.next()) {
                                        Existencia = rset.getInt(4);
                                        Consulta = con.consulta("SELECT DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS F_FecCad,F_ClaLot FROM tb_lote WHERE F_ClaPro='" + rset.getString(1) + "' AND F_FolLot='" + rset.getString(2) + "';");
                                        if (Consulta.next()) {
                                            Cadu = Consulta.getString(1);
                                            Lote = Consulta.getString(2);
                                        }
                                        if (Existencia > 0) {
                                            Total = Total + Existencia;

                    %>
                    <tr>                                        
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(3)%></td>
                        <td style='mso-number-format:"@"'><%=Lote%></td>
                        <td><%=Cadu%></td>
                        <td><%=formatter.format(Existencia)%></td>
                    </tr>
                    <%
                            }
                        }
                    } else if (Radio.equals("Clave")) {
                        if ((clave != "") || (Descrip != "")) {
                            rset = con.consulta("SELECT MD.F_ClaPro,M.F_LotMov,MD.F_DesPro,SUM(M.F_CantMov*M.F_SigMov) FROM tb_movinv M INNER JOIN tb_medica MD ON M.F_ProMov=MD.F_ClaPro WHERE " + Query + " GROUP BY M.F_ProMov;");
                            while (rset.next()) {
                                Existencia = rset.getInt(4);
                                if (Existencia > 0) {
                                    Total = Total + Existencia;

                    %>
                    <tr>                                        
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(3)%></td>
                        <td><%=formatter.format(Existencia)%></td>
                    </tr>
                    <%
                            }
                        }
                    } else {

                        rset = con.consulta("SELECT M.F_ClaPro,M.F_DesPro,SUM(F_CantMov*F_SigMov) FROM tb_medica M LEFT JOIN tb_movinv MV ON M.F_ClaPro=MV.F_ProMov WHERE M.F_StsPro='A' AND MV.F_FecMov<='" + fecha_fin + "' AND F_ConMov<'1000' GROUP BY M.F_ClaPro;");

                        while (rset.next()) {
                            TotalMed = rset.getString(3);

                            if (TotalMed == null) {
                                Existencia = 0;
                            } else if (TotalMed == "") {
                                Existencia = 0;
                            } else {
                                Existencia = Integer.parseInt(TotalMed);
                                if (Existencia < 0) {
                                    Existencia = 0;
                                }
                            }

                            Total = Total + Existencia;

                    %>
                    <tr>                                        
                        <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>                                           
                        <td><%=formatter.format(Existencia)%></td>
                    </tr>
                    <%
                            Existencia = 0;
                            TotalMed = "";
                        }
                        Consulta = con.consulta("SELECT F_ClaPro,F_DesPro FROM tb_medica WHERE F_ClaPro NOT IN (SELECT F_ProMov FROM tb_movinv WHERE F_FecMov<=CURDATE() AND F_ConMov<1000 GROUP BY F_ProMov);");
                        while (Consulta.next()) {
                    %>
                    <tr>
                        <td class="col-xs-3"><%=Consulta.getString(1)%></td>
                        <td class="col-xs-6"><%=Consulta.getString(2)%></td> 
                        <td class="col-xs-3">0</td>
                    </tr>
                    <%
                                        }
                                    }
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
                    <td colspan="3"><img src=" http: //187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                    <td colspan="3"><img src=" http: //187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
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