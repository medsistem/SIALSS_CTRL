<%-- 
    Document   : conexionModula
    Created on : 13/10/2014, 03:21:27 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="conn.ConectionDB"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB_SQLServer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    ConectionDB_SQLServer con = new ConectionDB_SQLServer();
    ConectionDB conMysql = new ConectionDB();

    if (con.conectar()) {
        //out.println("Conexión Exitosa");
        try {
            /*
             La I es de insercion
        
             */
            //con.ejecutar("insert into IMP_AVVISIINGRESSO values('I','0437','LOTE12','100','20160517','','')");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            out.println(e.getMessage());
        }
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
        <table border="1" class="table table-bordered table-condensed table-striped">
            <tr>
                <td>Clave</td>
                <td>Descrip</td>
                <td>Lote</td>
                <td>Cadu</td>
                <td>Cajón</td>
                <td>Posición</td>
                <td>Cant</td>
            </tr>
            <%
                try {
                    con.conectar();
                    conMysql.conectar();
                    ResultSet rset = con.consulta("select * from VIEW_MODULA_UBICACION where SCO_GIAC!=0 order by SCO_ARTICOLO");
                    while (rset.next()) {
                        String Descrip = "";
                        ResultSet rset2 = conMysql.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + rset.getString("SCO_ARTICOLO") + "'");
                        while (rset2.next()) {
                            Descrip = rset2.getString(1);
                        }
            %>
            <tr>
                <td><%=rset.getString("SCO_ARTICOLO")%></td>
                <td><%=Descrip%></td>
                <td><%=rset.getString("SCO_SUB1")%></td>
                <td><%=rset.getString("SCO_DSCAD")%></td>
                <td><%=rset.getString("UDC_UDC")%></td>
                <td><%=rset.getString("SCO_POSI")%></td>
                <td><%=formatter.format(rset.getInt("SCO_GIAC"))%></td>
            </tr>
            <%
                    }
                    conMysql.cierraConexion();
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            %>
        </table>

        <a class="btn btn-large btn-info btn-block" href="conexionModula.jsp">Actualizar</a>
        <br/>
        <form action="../AbasteceModula">
            <button class="btn btn-large btn-primary btn-block"  name="accion" value="AbastecerConcentrado">Abastecer</button>
        </form>

        <%
            try {
                con.conectar();
        %>
        <table class="table table-bordered table-condensed">
            <tr>
                <td>Clave</td>
                <td>Existencias</td>
                <td>Ingresos</td>
                <td>Salidas</td>
            </tr>
            <%
                ResultSet rset = con.consulta("select * from VIEW_MODULA_EXISTENCIA_MIN order by ART_ARTICOLO asc");
                while (rset.next()) {
                    //out.println(rset.getString(1) + "---" + rset.getString(2) + "---" + rset.getString(3) + "---" + rset.getString(4) + "<br/>");
                    %>
                    <tr>
                <td><%=rset.getString(1)%></td>
                <td><%=rset.getString(2)%></td>
                <td><%=rset.getString(3)%></td>
                <td><%=rset.getString(4)%></td>
            </tr>
                    <%
                }

            %>

        </table>
        <%                        rset = con.consulta("SELECT COLUMN_NAME 'All_Columns' FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='VIEW_MODULA_EXISTENCIA_MIN' ");
                while (rset.next()) {
                    out.println(rset.getString(1) + "<br/>");
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        %>
    </body>
</html>

<%
    } else {
        out.println("Conexión Fallida");
    }

%>
