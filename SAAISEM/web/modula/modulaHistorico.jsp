<%-- 
    Document   : requerimentoModula
    Created on : 16-oct-2014, 16:26:54
    Author     : Amerikillo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB_SQLServer"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); %>
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
    ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
        <hr/>
        <h4>Salidas Modula</h4>
        <table class="table table-bordered table-condensed table-striped" id="tablaMovMod">
            <thead>
                <tr>
                    <td>No Mov</td>
                    <td>Folio Remisión</td>
                    <td>Clave</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Cant Sol</td>
                    <td>Cant Sur</td>
                    <td>Charola</td>
                    <td>Charola</td>
                    <td>Código Comp.</td>
                    <td>Exi Final</td>
                    <td>Fecha / Hora</td>
                </tr>
            </thead>
            <tbody>
                <%                    
                    try {
                        conModula.conectar();
                        con.conectar();
                        //ResultSet rset = conModula.consulta("select STO_ID, STO_TIME, STO_ORDINE, STO_ARTICOLO, STO_SUB1, STO_SUB2, STO_DSCAD, STO_QTAR, STO_UDC, STO_SCOMPARTO, STO_GIAC, STO_POSI from VIEW_MODULA_STORICO_INTRATA");
                        //ResultSet rset = conModula.consulta("select STO_ID, STO_TIME, STO_ORDINE, STO_ARTICOLO, STO_SUB1, STO_SUB2, STO_DSCAD, STO_QTAR, STO_UDC, STO_SCOMPARTO, STO_GIAC, STO_POSI from VIEW_MODULA_STORICO_INVENTARIO");
                        ResultSet rset = conModula.consulta("select STO_ID, STO_TIME, STO_ORDINE, STO_ARTICOLO, STO_SUB1, STO_SUB2, STO_DSCAD, STO_QTAR, STO_UDC, STO_SCOMPARTO, STO_GIAC, STO_POSI from VIEW_MODULA_STORICO");
                        while (rset.next()) {
                            String F_DesPro = "";
                            String Folio = rset.getString("STO_ORDINE");
                            if (Folio.equals("_IMM.PRELIEVO_")) {
                                Folio="INMEDIATO";
                            }
                            ResultSet rset2 = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + rset.getString("STO_ARTICOLO") + "'");
                            while (rset2.next()) {
                                F_DesPro = rset2.getString("F_DesPro");
                            }
                %>
                <tr>
                    <td><%=rset.getString("STO_ID")%></td>
                    <td><%=Folio%></td>
                    <td title="<%=F_DesPro%>"><%=rset.getString("STO_ARTICOLO")%></td>
                    <td><%=rset.getString("STO_SUB1")%></td>
                    <td><%=df3.format(df2.parse(rset.getString("STO_DSCAD")))%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("STO_QTAR"))%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("STO_QTAR"))%></td>
                    <td><%=rset.getString("STO_UDC")%></td>
                    <td><%=rset.getString("STO_POSI")%></td>
                    <td><%=rset.getString("STO_SCOMPARTO")%></td>
                    <td class="text-right"><%=formatter.format(rset.getInt("STO_GIAC"))%></td>
                    <td><%=rset.getString("STO_TIME")%></td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                        conModula.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        out.print(e.getMessage());
                    }
                %>
            </tbody>
        </table>
    </body>
    <!-- ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/jquery-ui-1.10.3.custom.js"></script>
    <script src="../js/jquery.dataTables.js"></script>
    <script src="../js/dataTables.bootstrap.js"></script>
    <script>
        $(document).ready(function () {
            $('#tablaMovMod').dataTable();
        });
    </script>
</html>
