<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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

    String Folio1 = "", Folio2 = "", Unidad = "", FechaI = "", FechaF = "";
    try {
        Folio1 = request.getParameter("Folio1");
        Folio2 = request.getParameter("Folio2");
        Unidad = request.getParameter("Unidad");
        FechaI = request.getParameter("FechaI");
        FechaF = request.getParameter("FechaF");
    } catch (Exception e) {

    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Reporte_Incidencia.xls\"");
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
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
                    String fechaDia = ft.format(dNow);
                %>
                <tr>
                  <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                  <td colspan="7"><h4> <%=fechaDia%></h4></td>
                </tr><tr></tr>
                <tr>
                    <th colspan="8"><h1>Reporte de Incidencia en Farmacia</h1></th>
                </tr><tr></tr>
            </table>
            <div class="panel panel-info">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead class="bg-info">
                                <tr>
                                    <th class="text-center">Fecha</th>
                                    <th class="text-center">No. Folio</th>
                                    <th class="text-center">Nombre Unidad</th>
                                    <th class="text-center">Clave</th>
                                    <th class="text-center">Surtidor</th>
                                    <th class="text-center">Auditor/Verificador</th>
                                    <th class="text-center">Operador</th>
                                    <th class="text-center">Tipo de Insumo</th>
                                    <th class="text-center">Lote</th>
                                    <th class="text-center">Caducidad</th>
                                    <th class="text-center">Requerido Farmacia</th>
                                    <th class="text-center">Remisionado</th>
                                    <th class="text-center">Entregado Farmacia</th>
                                    <th class="text-center">Diferencia Remisionado-Requerido</th>
                                    <th class="text-center">Diferencia Entregado-Remisionado</th>
                                    <th class="text-center">Diferencia Requerido-Entregado</th>
                                    <th class="text-center">Tipo</th>
                                    <th class="text-center">Observación</th>
                                    <th class="text-center">Tipificacion</th>
                                </tr>
                            </thead>
                            
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        String Condicion = "";
                                        if ((!(Unidad.equals("0"))) && (!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.unidad='" + Unidad + "' AND A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";

                                        } else if ((!(Unidad.equals("0"))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.unidad='" + Unidad + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(Unidad.equals("0")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.unidad='" + Unidad + "' ";

                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals("")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "'";

                                        } else if (!(Unidad.equals("0"))) {
                                            Condicion = "A.unidad='" + Unidad + "'";
                                        } else if ((!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        }

                                        //ResultSet rset = con.consulta("SELECT DATE_FORMAT(A.fecha,'%d/%m/%Y') AS fecha, A.abasto, U.F_NomCli, A.clave, A.lote, DATE_FORMAT(A.caducidad, '%d/%m/%Y') AS caducidad, A.cantidad, A.observacion FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad = U.F_IdReporte WHERE " + Condicion + " GROUP BY A.unidad, A.abasto, A.clave, A.lote, A.caducidad, A.observacion, A.fecha;");
                                       //ResultSet rset = con.consulta("SELECT DATE_FORMAT(A.fecha,'%d/%m/%Y') AS fecha, A.abasto, U.F_NomCli, A.clave, A.lote, DATE_FORMAT(A.caducidad, '%d/%m/%Y') AS caducidad, A.cantidad, A.observacion, F.F_CantReq, F.F_CantSur FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad = U.F_IdReporte INNER JOIN tb_factura AS F ON F.F_ClaDoc = A.abasto collate utf8_unicode_ci and F_ClaPro = A.clave collate utf8_unicode_ci WHERE " + Condicion + " GROUP BY A.unidad, A.abasto, A.clave, A.lote, A.caducidad, A.observacion, A.fecha;");
                                        ResultSet rset = con.consulta("SELECT DATE_FORMAT(A.fecha,'%d/%m/%Y') AS fecha, A.abasto, U.F_NomCli, A.clave, A.lote, DATE_FORMAT(A.caducidad, '%d/%m/%Y') AS caducidad, A.cantidad, A.observacion, F.F_CantReq, F.F_CantSur,(F.F_CantSur - F.F_CantReq ) AS 'DIFERENCIAr-r' , CASE WHEN F.F_Ubicacion = 'CONTROLADO'  THEN 'CONTROLADO' WHEN F.F_Ubicacion = 'APE'  THEN 'ALTO COSTO' WHEN F.F_Ubicacion = 'REDFRIA'  THEN  'RED FRIA' ELSE 'ORDINARIO'  END AS 'TIPO INSUMO' ,TI.F_DetalleIncidencia AS TIPO, TI.F_IdTdI,	( A.cantidad - F.F_CantSur ) AS 'DIFERENCIAe-r',(A.cantidad - F.F_CantReq) AS 'DIFERENCIAr-e' FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad = U.F_IdReporte INNER JOIN tb_tipoinc AS TI ON A.tipo = TI.F_IdTdI INNER JOIN tb_factura AS F ON F.F_ClaDoc = A.abasto collate utf8_unicode_ci and F_ClaPro = A.clave collate utf8_unicode_ci WHERE " + Condicion + " GROUP BY A.unidad, A.abasto, A.clave, A.lote, A.caducidad, A.observacion, A.fecha;");
  
                                        while (rset.next()) {
                            %>
                            <tr>
                                 <td><%=rset.getString(1)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                 <td><%=rset.getString(12)%></td>
                                    <td class="text-center" style="mso-number-format:'@';"><%=rset.getString(5)%></td>
                                    <td class="text-center"><%=rset.getString(6)%></td>
                                    <td class="text-center"><%=rset.getString(9)%></td>
                                    <td class="text-center"><%=rset.getString(10)%></td>
                                    <td class="text-center"><%=rset.getString(7)%></td>
                                    <td class="text-center"><%=rset.getString(11)%></td>
                                    <td class="text-center"><%=rset.getString(15)%></td>
                                    <td class="text-center"><%=rset.getString(16)%></td>
                                    <td><%=rset.getString(13)%></td>
                                    <td><%=rset.getString(8)%></td>
                                    <td class="text-center"><%=rset.getString(14)%></td>
                                
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