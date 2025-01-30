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
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "", Descripcion = "", tipo = "", ProyectoCL = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        ProyectoCL = (String) sesion.getAttribute("ProyectoCL");
    } else {
        response.sendRedirect("index.jsp");
    }

    Clave = request.getParameter("Clave");
    Descripcion = request.getParameter("Descripcion");

    if (Clave == null) {
        Clave = "";
    }
    if (Descripcion == null) {
        Descripcion = "";
    }

    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catálogo</h3>
                </div>
                <div class="panel-footer">
                    <form method="post" action="Medicamentos">
                        <table class="table table-striped table-bordered" id="datosProv">
                            <thead>
                                <tr>
                                    <td>CLAVE</td>
                                    <td>Descripción</td>
                                    <td>Sts</td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = null;
                                        int tipoM = 0;
                                        String DesTipo = "", Campo = "";

                                        rset = con.consulta("SELECT GROUP_CONCAT(CONCAT(F_Campo,'= 1')) AS F_Campo FROM tb_proyectos WHERE F_Id IN (" + ProyectoCL + ");");
                                        if (rset.next()) {
                                            Campo = rset.getString(1);
                                        }
                                        
                                        Campo = Campo.replace(",", " OR ");

                                        if (Descripcion != "") {
                                            rset = con.consulta("SELECT F_ClaPro, F_DesPro, F_StsPro FROM tb_medica where F_DesPro = '" + Descripcion + "' AND "+Campo+" ORDER BY F_ClaPro+0 ASC");
                                        } else if (Clave != "") {
                                            rset = con.consulta("SELECT F_ClaPro, F_DesPro, F_StsPro FROM tb_medica where F_ClaPro = '" + Clave + "' AND "+Campo+" ORDER BY F_ClaPro+0 ASC");
                                        } else {
                                            rset = con.consulta("SELECT F_ClaPro, F_DesPro, F_StsPro FROM tb_medica WHERE "+Campo+" ORDER BY F_ClaPro+0 ASC");
                                        }

                                        while (rset.next()) {
                                %>
                                <tr>
                                    <td><small><%=rset.getString(1)%></small></td>
                                    <td><small><%=rset.getString(2)%></small></td>
                                    <td><small><%=rset.getString(3)%></small></td>
                                </tr>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>
                            </tbody>
                        </table>                        
                    </form>
                </div>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <!-- 
================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script>
                                    $(document).ready(function () {
                                        $('#datosProv').dataTable();
                                    });
        </script>
        <script>


            function isNumberKey(evt, obj)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode;
                if (charCode === 13 || charCode > 31 && (charCode < 48 || charCode > 57)) {
                    if (charCode === 13) {
                        frm = obj.form;
                        for (i = 0; i < frm.elements.length; i++)
                            if (frm.elements[i] === obj)
                            {
                                if (i === frm.elements.length - 1)
                                    i = -1;
                                break
                            }
                        /*ACA ESTA EL CAMBIO*/
                        if (frm.elements[i + 1].disabled === true)
                            tabular(e, frm.elements[i + 1]);
                        else
                            frm.elements[i + 1].focus();
                        return false;
                    }
                    return false;
                }
                return true;

            }



        </script>
        <script language="javascript">
            function justNumbers(e)
            {
                var keynum = window.event ? window.event.keyCode : e.which;
                if ((keynum == 8) || (keynum == 46))
                    return true;

                return /\d/.test(String.fromCharCode(keynum));
            }
            otro = 0;
            function LP_data() {
                var key = window.event.keyCode;//codigo de tecla. 
                if (key < 48 || key > 57) {//si no es numero 
                    window.event.keyCode = 0;//anula la entrada de texto. 
                }
            }
            function anade(esto) {
                if (esto.value.length === 0) {
                    if (esto.value.length == 0) {
                        esto.value += "(";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 4) {
                        esto.value += ") ";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 9) {
                        esto.value += "-";
                    }
                }
                if (esto.value.length < otro) {
                    if (esto.value.length == 4 || esto.value.length == 9) {
                        esto.value = esto.value.substring(0, esto.value.length - 1);
                    }
                }
                otro = esto.value.length
            }


            function tabular(e, obj)
            {
                tecla = (document.all) ? e.keyCode : e.which;
                if (tecla != 13)
                    return;
                frm = obj.form;
                for (i = 0; i < frm.elements.length; i++)
                    if (frm.elements[i] == obj)
                    {
                        if (i == frm.elements.length - 1)
                            i = -1;
                        break
                    }
                /*ACA ESTA EL CAMBIO*/
                if (frm.elements[i + 1].disabled == true)
                    tabular(e, frm.elements[i + 1]);
                else
                    frm.elements[i + 1].focus();
                return false;
            }

        </script>
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset = con.consulta("SELECT F_DesPro FROM tb_medica ORDER BY F_DesPro ASC");
                        while (rset.next()) {
                            out.println("'" + rset.getString(1) + "',");
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
                ];
                $("#Descripcion").autocomplete({
                    source: availableTags
                });
            });
        </script>
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset = con.consulta("SELECT F_ClaPro FROM tb_medica ORDER BY F_ClaPro+0");
                        while (rset.next()) {
                            out.println("'" + rset.getString(1) + "',");
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
                ];
                $("#Clave").autocomplete({
                    source: availableTags
                });
            });
        </script>
    </body>
</html>



