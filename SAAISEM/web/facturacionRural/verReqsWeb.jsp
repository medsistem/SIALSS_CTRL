<%-- 
    Document   : verReqsWeb
    Created on : 11/12/2014, 04:34:41 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    ConectionDB_ReqRur conRur = new ConectionDB_ReqRur();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="../stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

        <%@include file="../jspf/menuPrincipal.jspf" %>
        <form method="post" action="verReqsWeb.jsp">
            <select name="F_IdReq" class="form-control" onchange="this.form.submit();">
                <option value="">--Seleccione--</option>
                <%
                    try {
                        conRur.conectar();
                        ResultSet rset = conRur.consulta("select * from v_requerimientos where F_StsReq='4' group by F_NomCli, F_IdReq");
                        while (rset.next()) {
                %>
                <option value="<%=rset.getString("F_IdReq")%>"><%=rset.getString("F_IdReq")%> - <%=rset.getString("F_NomCli")%></option>
                <%
                        }
                        conRur.cierraConexion();
                    } catch (Exception e) {

                    }
                %>
            </select>
        </form>
        <%
            if (request.getParameter("F_IdReq") != null && !request.getParameter("F_IdReq").equals("")) {
        %>

        <hr/>
        <h3>Folio:<%=request.getParameter("F_IdReq")%></h3>
        <%
            try {
                conRur.conectar();
                ResultSet rset = conRur.consulta("select F_NomCli, sum(F_Cant) as F_Cant, F_ClaCli from v_requerimientos where F_IdReq = '" + request.getParameter("F_IdReq") + "' group by F_IdReq");
                while (rset.next()) {
        %>
        <h3>Unidad: <%=rset.getString("F_ClaCli")%> - <%=rset.getString("F_NomCli")%></h3>
        <h3>Piezas: <%=rset.getString("F_Cant")%></h3>
        <div class="row">
            <form action="../DescargaReqRural" method="post">
                <div class="col-sm-2 col-sm-offset-8">
                </div>
                <div class="col-sm-2">
                    <input name="F_IdReq" class="hidden" value="<%=request.getParameter("F_IdReq")%>" />
                    <input name="F_ClaCli" class="" value="<%=rset.getString("F_ClaCli")%>" />
                    <button class="btn btn-primary" data-toggle="modal" id="muestraModal" data-target="#myModal">Descargar Folio</button>
                    <button class="hidden" type="submit" name="accion" value="cargaReqRur" id="cargaReqRur">Descargar Folio</button>
                </div>  
            </form>
        </div>
        <hr/>
        <%
                }
                conRur.cierraConexion();
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        %>
        <table class="table table-bordered table-condensed table-striped" id="detReqRur">
            <thead>
                <tr>
                    <td>Clave</td>
                    <td>Cantidad</td>
                    <td>Observaciones</td>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        conRur.conectar();
                        ResultSet rset = conRur.consulta("select F_ClaPro, F_DesPro, SUM(F_Cant) as F_Cant, F_Obs from v_requerimientos where F_Cant!=0 and F_IdReq = '" + request.getParameter("F_IdReq") + "' group by F_Id");
                        while (rset.next()) {
                %>
                <tr>
                    <td title="<%=rset.getString("F_DesPro")%>"><%=rset.getString("F_ClaPro")%></td>
                    <td><%=rset.getString("F_Cant")%></td>
                    <td><%=rset.getString("F_Obs")%></td>
                </tr>
                <%
                        }
                        conRur.cierraConexion();
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                %>
            </tbody>
        </table>
        <%
            }
        %>

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Seguro de descargar este requerimiento?</h4>
                    </div>
                    <div class="modal-body">
                        <button class="btn btn-primary btn-block" name="accion" value="cargaReqRur" id="btnModal" onclick="verificaDescarga()">Descargar Folio</button>
                        <div class="text-center" id="imagenCarga">
                        <img src="../imagenes/ajax-loader-1.gif" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>



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
                            $(document).ready(function() {
                                $('#detReqRur').dataTable();
                                $('#imagenCarga').hide();
                            });

                            function verificaDescarga() {
                                document.getElementById('btnModal').disabled=true;
                                document.getElementById('cargaReqRur').click();
                                $('#imagenCarga').show();
                            }
        </script>
    </body>
</html>
