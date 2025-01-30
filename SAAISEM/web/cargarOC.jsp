<%-- 
    Document   : capturaISEM.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<!--%@page import="conn.ConectionDB_Linux"%-->
<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formNoCom = new DecimalFormat("000");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", IdUsu = "", Proyecto = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        IdUsu = (String) sesion.getAttribute("IdUsu");
        tipo = (String) sesion.getAttribute("Tipo");
        Proyecto = (String) sesion.getAttribute("Proyecto");
    } else {
        response.sendRedirect("indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Captura</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="shortcut icon"
              href="imagenes/system-settings-icon_31831.png" />
        <link href="css/select2.css" rel="stylesheet">

    </head>
    <body onload="focusLocus();
            SelectProve(FormBusca);">
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <h4>Selecciona Proyecto a Cargar OC</h4>
            <hr/>
            <br/>
            <div class="row">
                <%                    try {
                        con.conectar();
                        String Class = "";
                        int Contar = 0;
                        ResultSet rset = con.consulta("SELECT F_Id, F_DesProy, F_Campo FROM tb_proyectos  WHERE F_Id IN (" + Proyecto + ") ORDER BY F_DesProy ASC;");
                        while (rset.next()) {
                            Contar++;
                            if (Contar == 1) {
                                Class = "btn-info";
                            } else if (Contar == 2) {
                                Class = "btn-info";
                            } else if (Contar == 3) {
                                Class = "btn-primary";
                            } else if (Contar == 4) {
                                Class = "btn-primary";
                                Contar = 0;
                            }

                %>
                <div class="col-sm-3">
                    <a href="CargaOC?Proyecto=<%=rset.getString(1)%>&DesProyecto=<%=rset.getString(2)%>&Campo=<%=rset.getString(3)%>" class="btn btn-block form-control <%=Class%>">
                        <%=rset.getString(2)%></a>
                </div>
                <br />
                <%if (Contar > 4) {%>
                <br />
                <%
                                Contar = 0;
                            }
                        }

                    } catch (Exception e) {
                        Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, e);
                    } finally {
                        try {
                            con.cierraConexion();
                        } catch (SQLException ex) {
                            Logger.getLogger("capturaMedalfa.jsp").log(Level.SEVERE, null, ex);
                        }
                    }
                %>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/select2.js" type="text/javascript"></script>
    </body>

</html>
