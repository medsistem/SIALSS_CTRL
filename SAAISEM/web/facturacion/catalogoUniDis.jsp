<%-- 
    Document   : cargarArtIS
    Created on : 23-ene-2015, 8:02:25
    Author     : amerikillo
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
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <!-- Estilos CSS -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <%@include file="../jspf/menuPrincipal.jspf" %>

            <h4>Catálogo de Unidades</h4>
            <hr/>
            <form action="CargaUnidadesServlet" method="post" enctype="multipart/form-data" name="form1">
                <div class="row">
                    <h4 class="col-sm-3">Seleccione el Excel a Cargar</h4>
                    <div class="col-lg-4">
                        <input class="form-control" type="file" name="file1" id="file1" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-block btn-primary">Actualizar</button>
                    </div>
                    <div class="col-sm-2">
                        <a href="CatUniAlta.jsp" class="btn btn-block btn-info">Alta Unidad&nbsp;<span class="glyphicon glyphicon-floppy-save"></span></a>                        
                    </div>
                </div>
            </form>
            <br/>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-heading">

                </div>
                <div class="panel-body table-responsive">
                    <table class="table table-bordered table-condensed table-striped" id="tbArtIS">
                        <thead>
                            <tr>
                                <td>Clave UniIS</td>
                                <td>Descripción</td>
                                <td>Loc UniIS</td>
                                <td>Mun UniIS</td>
                                <td>Jur UniIS</td>
                                <td>Reg UniIS</td>
                                <td>Niv UniIS</td>
                                <td>Coo UniIS</td>
                                <td>Modificar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%                            try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("select * from tb_unidis order by F_ClaUniIS");
                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString("F_ClaUniIS")%></td>
                                <td><%=rset.getString("F_DesUniIS")%></td>
                                <td><%=rset.getString("F_LocUniIS")%></td>
                                <td><%=rset.getString("F_MunUniIS")%></td>
                                <td><%=rset.getString("F_JurUniIS")%></td>
                                <td><%=rset.getString("F_RegUniIS")%></td>
                                <td><%=rset.getString("F_NivUniIS")%></td>
                                <td><%=rset.getString("F_CooUniIS")%></td>
                                <td><a href="ModiCatUni.jsp?Id=<%=rset.getString("F_Id")%>" class="btn btn-block btn-info"><span class="glyphicon glyphicon-pencil"></span></a></td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    out.println(e.getMessage());
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->

        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function () {
                $('#tbArtIS').dataTable();
            });
        </script>
    </body>
</html>
