<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Sistemas
--%>

<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df1 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    String Secuencial = "", FechaSe = "", Factura = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();

    Integer folio = Integer.parseInt(request.getParameter("fol_gnkl"));
    String unidad = request.getParameter("Unidad");
    String claCli = request.getParameter("ClaCli");
    String nomUnidad = request.getParameter("nomUnidad");
    String fechaEntrega = "";
    try {
        fechaEntrega = df2.format(df1.parse(request.getParameter("fecha")));
    } catch (ParseException e) {
        fechaEntrega = df2.format(new Date());
    }
    System.out.println(fechaEntrega);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <link href="../css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>
        <link href="../css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>


            <div class="col-sm-12">
                <h4>Procesar Requerimientos Electr&oacute;nicos Farmacias</h4>
            </div>
            <br />
            <hr/>
            <form action="ProcesarRequeFarmacia.jsp" method="get">
                <div class="row">


                    <div class="panel-footer">
                        <div class="row">
                            <label class="control-label col-lg-12" for="jurisdiccion"><%=nomUnidad + " - Folio: " + folio%></label>
                        </div> 
                    </div>
                </div>
            </form>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <div class="col-md-3">
                        <label>Fecha tentativa de entrega</label></div>
                    <div class="col-md-2">
                        <input type="date" class="form-control" name="FechaEnt" id="fecha" min="<%=df2.format(new Date())%>" value="<%=fechaEntrega%>" onkeydown="return false"/></div>
                </div>
                <div class="panel-body table-responsive">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td style="width: 10%">Cantidad</td>
                                <td style="width: 10%">Editar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();

                                    ResultSet rset = con.consulta("SELECT R.id, R.clave, SUBSTRING(M.F_DesPro,1,100), R.requerido FROM requerimiento_lodimed R INNER JOIN tb_medica M ON R.clave = M.F_ClaPro COLLATE utf8_unicode_ci AND R.requerido > 0 AND R.clave_unidad = '" + unidad + "' AND R.folio = '" + folio + "';");
                                    while (rset.next()) {

                            %>
                            <tr>

                                <td id="noreq"><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><label id="label_<%=rset.getString(1)%>"><%=rset.getString(4)%></label> <input id="input_<%=rset.getString(1)%>" type="number" min="0" class="text-right form-control" value="<%=rset.getString(4)%>" data-behavior="only-num" style="display: none" oninput="this.value=this.value.replace(/[^0-9]/g,'');"/></td>

                                <td>
                                    <input class="hidden" name="idReq" id="idReq" value="<%=rset.getString(1)%>">
                                    <div class="btn-group" role="group" aria-label="Basic example" id="editando_<%=rset.getString(1)%>" style="display: none">
                                        <button class="btn btn-info" onclick="guardarEditado(<%=rset.getString(1)%>, true)"><span class="glyphicon glyphicon-floppy-disk"></span>
                                        </button>
                                        <button class="btn btn-info" onclick="noEditar(<%=rset.getString(1)%>, <%=rset.getString(4)%>)"><span class="glyphicon glyphicon-floppy-remove">
                                        </button>
                                    </div>
                                    <button class="btn btn-block btn-info" name="accion" type="button" id="editar_<%=rset.getString(1)%>" onclick="editar(<%=rset.getString(1)%>)"><span class="glyphicon glyphicon-edit"></span></button>
                                </td>
                            </tr>
                            <%
                                    }

                                    con.cierraConexion();

                                } catch (Exception e) {

                                }
                            %>
                        </tbody>
                    </table>
                    <hr>
                    <div class="row">
                        <div class="col-md-5"></div>
                        <div class="col-md-4">
                            <button class="btn btn-primary" onclick="guardarTodo('<%=unidad%>',<%=folio%>)"><span class="glyphicon glyphicon-floppy-disk"></span> Guardar</button>
                        </div>
                    </div>
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
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/select2.js" type="text/javascript"></script>
        <script src="../js/facturajs/ProcesaRequerimiento.js"></script>
        <script src="../js/sweetalert.min.js" type="text/javascript"></script>    
        <script>
                                $(document).ready(function () {
                                    $("#UnidadSe").select2();
                                });
        </script>
        <script>
            function editar(valor)
            {
                //alert(valor);
                $("#editando_" + valor).show();
                $("#editar_" + valor).hide();
                $("#label_" + valor).hide();
                $("#input_" + valor).show();
            }
            function noEditar(id) {
                $("#editando_" + id).hide();
                $("#editar_" + id).show();
                $("#label_" + id).show();
                $("#input_" + id).val($("#label_" + id).text());
                $("#input_" + id).hide();
            }

            $(document).ready(function () {
                $('#datosCompras').dataTable();
            });
        </script>
    </body>

</html>

