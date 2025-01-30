<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", username = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        username = (String) sesion.getAttribute("Usuario");
        tipo = (String) sesion.getAttribute("Tipo");
        

    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        
        <!--<link href="css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>

        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="text-center">
                <%
                    if (tipo.equals("1")) {
                %>

                <div class="panel panel-info">
                    <div class="panel-heading">
                        Devoluciones Realizadas.
                    </div>

                    <div class="panel-body">
                        <table class="table table-condensed table-bordered table-striped">
                            <tr>
                                <td>Remisión</td>
                                <td >Cliente</td>
                                <td>Fecha</td>
                                <td>Insumo</td>
                                <td>Cant</td>                  
                                <td>Observaciones</td>
                                <td>Usuario</td>
                            </tr>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT f.F_ClaDoc, atn.F_NomCli, DATE_FORMAT( f.F_FecApl , '%d/%m/%Y' ) AS F_FecApl, f.F_ClaPro, f.F_CantSur, f.F_Obs,f.F_User FROM tb_factura AS f INNER JOIN tb_uniatn AS atn ON f.F_ClaCli = atn.F_ClaCli WHERE f.F_StsFact = 'C' AND f.F_FecApl BETWEEN(SELECT DATE_FORMAT((CURDATE() - 1),'%Y-%m-%d')) and (SELECT DATE_FORMAT(CURDATE(),'%Y-%m-%d'))");
                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString("f.F_ClaDoc")%></td>
                                <td><%=rset.getString("atn.F_NomCli")%></td>
                                <td><%=rset.getString("F_FecApl")%></td>
                                <td><%=rset.getString("f.F_ClaPro")%></td>
                                <td><%=rset.getString("f.F_CantSur")%></td>
                                <td><%=rset.getString("f.F_Obs")%></td>
                                <td><%=rset.getString("f.F_User")%></td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {

                                }
                            %>
                        </table>
                    </div>
                </div>
                <%
                    }
                %>
                <br /><br /><br />
                <br/><br/>
            </div>

        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script
        <%
        String message = (String) sesion.getAttribute("mensaje");
        if (message != null) {
        %>
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            swal({
                title: "<%=message%>",
                text: "",
                type: "warning"
            }, function () {
                //location.reload();
//                window.location = "facturaAtomaticaMich.jsp";
            });
        </script>
        <%
            sesion.removeAttribute("mensaje");
        }
        %>
    </body>

</html>

