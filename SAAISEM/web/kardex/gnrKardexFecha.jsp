<%-- 
    Document   : gnkKardexClave
    Created on : 22-oct-2014, 8:39:49
    Author     : amerikillo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.medalfa.saa.db.ConnectionManager"%>
<%@page import="com.medalfa.saa.db.Source"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.medalfa.saa.querys.kardexQuerys"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    
    String fecha =  request.getParameter("fecha");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Trazabilidad_" + request.getParameter("fecha") + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <table>
            <%
                Date dNow = new Date();
                DateFormat ft = new SimpleDateFormat("dd/MM/YYYY' 'HH:mm");
                String fechaDia = ft.format(dNow);
            %>
            <tr>
                <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                <td colspan="6"><h4><%=fechaDia%></h4></td>
            </tr>
            <tr></tr>
            <tr>
                <th colspan="9"><h1>Kardex por Dia</h1></th>
            </tr>
            
            <tr>
                <td colspan="3"><h3>Fecha de Trazabilidad:  <%=request.getParameter("fecha")%></h3></td>
            </tr>
            <tr>
                <td colspan="3"><h4>Ingresos y Egresos</h4></td>
            </tr>
        </table>
    
    <table border="1">
        <thead> 
            <tr>
                <th>No. Mov</th>
                <th>Usuario</th>
                <th>Usuario que captura</th>
                <th>Usuario que libera</th>
                <th>Orden Compra</th>
                <th>Remisión</th>
                <th>Proveedor</th>
                <th>Folio o documento</th>
                <th>Folio Referencia</th>
                <th>Punto de Entrega</th>
                <th>Concepto</th>
                <th>Clave</th>
                <th>Lote</th>
                <th>Caducidad</th>
                <th>Cantidad</th>
                <th>Ubicación</th>
                <th>Origen</th>
                <th>Proyecto</th>
                <th>Fecha Aplicacion</th>
                <th>Fecha Entrega</th>
                <th>Hora</th>
                <th>Orden de suministro</th>
                <th>Comentario</th>
            </tr>
        </thead>
        <tbody>
            <%
                try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();
                        PreparedStatement ps = connection.prepareStatement(kardexQuerys.OBTENER_KARDEX_POR_FECHA)) {

                    ps.setString(1, fecha);
                    ps.setString(2, fecha);
                    ps.setString(3, fecha);
                    ps.setString(4, fecha);
                    ps.setString(5, fecha);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            
            %>
            <tr>
                <td><%=rs.getInt("noMov")%></td>
                <td><%=rs.getString("usuario")%></td>
                <td><%=rs.getString("usuariocapt")%></td>
                <td><%=rs.getString("usuariolib")%></td>
                <td><%=rs.getString("ori")%></td>
                <td><%=rs.getString("remision")%></td>
                <td><%=rs.getString("proveedor")%></td>
                <td><%=rs.getString("folioSalida")%></td>
                <td><%=rs.getString("folioReferencia")%></td>
                <td><%=rs.getString("puntoEntrega")%></td>
                <td><%=rs.getString("concepto")%></td>
                <td style="mso-number-format:'@';"><%=rs.getString("clave")%></td>
                <td style="mso-number-format:'@';"><%=rs.getString("lote")%></td>
                <td><%=rs.getString("caducidad")%></td>
                <td><%=formatter.format(rs.getInt("cantidadMovimiento"))%></td>
                <td><%=rs.getString("ubicacion")%></td>
                <td><%=rs.getString("origen")%></td>
                <td><%=rs.getString("proyecto")%></td>
                <td><%=rs.getString("fechaMovimiento")%></td>
                <td><%=rs.getString("fechaEnt")%></td>
                <td><%=rs.getString("hora")%></td>
                <td><%=rs.getString("ordSuministro")%></td>
                <td><%=rs.getString("comentarios")%></td>
            </tr>
            <%
                        }
                    }
                } catch (SQLException | NamingException ex) {
                    Logger.getLogger("gnrKardexFecha.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                }
            %>
        </tbody>
    </table>
    <h4>Redistribución en Almacén</h4>
    <table border="1">
        <thead> 
            <tr>
                <th>No. Mov</th>
                <th>Usuario</th>
                <th>Documento</th>
                <th>Remisión</th>
                <th>Proveedor</th>
                <th>Folio Salida</th>
                <th>Folio Referencia</th>
                <th>Punto de Entrega</th>
                <th>Concepto</th>
                <th>Clave</th>
                <th>Lote</th>
                <th>Caducidad</th>
                <th>Cantidad</th>
                <th>Ubicación</th>
                <th>Origen</th>
                <th>Proyecto</th>
                <th>Fecha Aplicacion</th>
                <th>Fecha Entrega</th>
                <th>Hora</th>
                <th>Comentario</th>
            </tr>
        </thead>
        <tbody>
            <%
                try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();
                        PreparedStatement ps = connection.prepareStatement(kardexQuerys.OBTENER_KARDEX_REDISTRIBUCION_POR_FECHA)) {

                    ps.setString(1, fecha);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
            %>
            <tr>
                <td><%=rs.getInt("idMovimiento")%></td>
                <td><%=rs.getString("usuario")%></td>
                <td><%=rs.getString("documento")%></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><%=rs.getString("concepto")%></td>
                <td style="mso-number-format:'@';"><%=rs.getString("clave")%></td>
                <td style="mso-number-format:'@';"><%=rs.getString("lote")%></td>
                <td><%=rs.getString("caducidad")%></td>
                <td><%=formatter.format(rs.getInt("cantidad"))%></td>
                <td><%=rs.getString("ubicacion")%></td>
                <td><%=rs.getString("origen")%></td>
                <td><%=rs.getString("descProyecto")%></td>
                <td><%=rs.getString("fechaMovimiento")%></td>
                <td><%=rs.getString("fechaMovimiento")%></td>
                <td><%=rs.getString("hora")%></td>
                <td><%=rs.getString("comentarios")%></td>
            </tr>
            <%
                        }
                    }
                } catch (SQLException | NamingException ex) {
                    Logger.getLogger("gnrKardexFecha.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                }
            %>
        </tbody>
    </table>
</body>
</html>
