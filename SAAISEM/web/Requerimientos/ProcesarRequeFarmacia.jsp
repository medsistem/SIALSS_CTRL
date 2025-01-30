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
<%java.text.DateFormat df1 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
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

    String UnidadSe = "";
    String juris = "";

    try {
        UnidadSe = request.getParameter("UnidadSe");
        juris = request.getParameter("juris");
    } catch (Exception e) {

    }

    if (UnidadSe == null) {
        UnidadSe = "";
    }
    if (juris == null) {
        juris = "";
    }
    String fechas = "";
    String fechasCap = "";
    String fecha1 = "";
    try {
        fecha1 = df2.format(df2.parse(request.getParameter("fecha1")));
    } catch (Exception e) {
        fecha1 = "";
    }
    String fechaCap1 = "";
    try {
        fechaCap1 = df2.format(df2.parse(request.getParameter("fechaCap1")));
    } catch (Exception e) {
        fechaCap1 = "";
    }
    String fechaCap2 = "";
    try {
        fechaCap2 = df2.format(df2.parse(request.getParameter("fechaCap2")));
        if(!fechaCap1.isEmpty()){
            fechasCap = "AND fecha BETWEEN '" + fechaCap1 + "' AND '" + fechaCap2 + "' ";
        }
    } catch (Exception e) {
        fechaCap2 = "";
    }
    String fecha2 = "";
    try {
        fecha2 = df2.format(df2.parse(request.getParameter("fecha2")));
        if (!fecha1.isEmpty()) {
            fechas = "AND fecha_entrega BETWEEN '" + fecha1 + "' AND '" + fecha2 + "'";
        }
    } catch (Exception e) {
        fecha2 = "";
    }


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
        <title>MEDALFA</title>
    </head>
    <body>
        <div class="container">
            <h1>SIAALS</h1>
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
                            <label class="control-label col-lg-2" for="jurisdiccion">Jurisdicción </label>
                            <div class="col-lg-5">
                                <select class="form-control" name="juris" id="juris" >
                                    <option value="">--Seleccione--</option>
                                    <%                                                try {
                                            con.conectar();
                                            try {
                                                ResultSet RsetJur = con.consulta("SELECT U.F_ClaJur, J.F_DesJurIS FROM requerimiento_lodimed R INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte COLLATE utf8_unicode_ci INNER JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS GROUP BY J.F_ClaJurIS;");
                                                while (RsetJur.next()) {
                                    %>
                                    <option value="<%=RsetJur.getString(1)%>"><%=RsetJur.getString(2)%></option>
                                    <%

                                                }

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }

                                            con.cierraConexion();
                                        } catch (Exception e) {
                                        }
                                    %>
                                </select>
                            </div>



                        </div> 
                        <br>
                        <div class="row">
                            <label class="control-label col-lg-2" for="jurisdiccion">Nombre Unidad</label>
                            <div class="col-lg-5">
                                <select class="form-control" name="UnidadSe" id="UnidadSe" >
                                    <option value="">--Seleccione--</option>
                                    <%                                                try {
                                            con.conectar();
                                            try {
                                                ResultSet RsetJur = con.consulta("SELECT R.clave_unidad, CONCAT( U.F_ClaCli, ' - ', U.F_NomCli, ' - ', J.F_DesJurIS ) FROM requerimiento_lodimed R INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte INNER JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS GROUP BY R.clave_unidad;");
                                                while (RsetJur.next()) {
                                    %>
                                    <option value="<%=RsetJur.getString(1)%>"><%=RsetJur.getString(2)%></option>
                                    <%

                                                }

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }

                                            con.cierraConexion();
                                        } catch (Exception e) {
                                        }
                                    %>
                                </select>
                            </div>


                        </div> 
                        <br>
                        <div class="row">
                            <label class="control-label col-lg-2" for="fecha">Fecha de Entrega</label>
                            <div class="col-md-4">
                                <input type="date" class="form-control" name="fecha1" id="fecha1" />
                            </div>
                            <div class="col-md-4">
                                <input type="date" class="form-control" name="fecha2" id="fecha2" />
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <label class="control-label col-lg-2" for="fecha">Fecha de Captura</label>
                            <div class="col-md-4">
                                <input type="date" class="form-control" name="fechaCap1" id="fechaCap1" />
                            </div>
                            <div class="col-md-4">
                                <input type="date" class="form-control" name="fechaCap2" id="fechaCap2" />
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <label class="control-label col-lg-2" for="btn"></label>
                            <div class="col-sm-4">
                                <button class="btn btn-block btn-info" type="submit" id="btn_capturar" name="btn_capturar" onclick="return validarFiltros()">Mostrar</button>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
            <div class="panel panel-info">
                <div class="panel-body table-responsive">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td>No.</td>
                                <td>Clave Cliente</td>
                                <td>Nombre Cliente</td>
                                <td>Sts</td>
                                <td>Fecha Captura</td>
                                <td>Claves Sol</td>  
                                <td>Cant. Sol</td>
                                <td>Fecha de Entrega</td>
                                <td>Tipo
                                <td>Descargar</td>
                                <td>Procesar</td>
                                <td>Editar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    String query = "";
                                    if(!fechasCap.isEmpty()){
                                        query = "SELECT R.folio, U.F_ClaCli, U.F_NomCli, R.estatus, DATE_FORMAT(R.fecha, '%d/%m/%Y') AS fecha, COUNT(*), SUM(R.requerido) AS requerido, R.clave_unidad, CASE WHEN R.estatus = 'RECIBIDO' THEN 1 ELSE 0 END AS PROCESADO, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega, R.tipo FROM requerimiento_lodimed R LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 " + fechas + " " +fechasCap + "GROUP BY clave_unidad, R.folio order by R.fecha desc, u.F_ClaCli;";
                                    }
                                    if (!juris.isEmpty() && UnidadSe.isEmpty()) {
                                        query = "SELECT R.folio, U.F_ClaCli, U.F_NomCli, R.estatus, DATE_FORMAT(R.fecha, '%d/%m/%Y') AS fecha, COUNT(*), SUM(R.requerido) AS requerido, R.clave_unidad, CASE WHEN R.estatus = 'RECIBIDO' THEN 1 ELSE 0 END AS PROCESADO, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega, R.tipo FROM requerimiento_lodimed R LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 AND U.F_ClaJur = '" + juris + "' AND U.F_StsCli = 'A' " + fechas + " " +fechasCap + "GROUP BY clave_unidad, R.folio order by R.fecha desc, u.F_ClaCli;";
                                    }
                                    if (!UnidadSe.isEmpty()) {
                                        query = "SELECT R.folio, U.F_ClaCli, U.F_NomCli, R.estatus, DATE_FORMAT(R.fecha, '%d/%m/%Y') AS fecha, COUNT(*), SUM(R.requerido) AS requerido, R.clave_unidad, CASE WHEN R.estatus = 'RECIBIDO' THEN 1 ELSE 0 END AS PROCESADO, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega, R.tipo FROM requerimiento_lodimed R LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 AND R.clave_unidad = '" + UnidadSe + "' AND U.F_StsCli = 'A' " + fechas + " " +fechasCap + "GROUP BY clave_unidad, R.folio order by R.fecha desc, u.F_ClaCli;";
                                    }
                                    if (!query.isEmpty()) {
                                        con.conectar();
                                        System.out.println(query);
                                        ResultSet rset = con.consulta(query);
                                        int renglon = 0;
                                        while (rset.next()) {
                                            renglon++;
                            %>
                            <tr>

                                <td id="noreq"><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>
                                <td><%=rset.getString(7)%></td>
                                <td><%=rset.getString("fecha_entrega")%></td>
                                <td><%=rset.getString(11)%></td>
                                <td>
                                    <a class="btn btn-block btn-info" href="gnrReqFarmacia.jsp?fol_gnkl=<%=rset.getString(1)%>&Unidad=<%=rset.getString(8)%>"><span class="glyphicon glyphicon-save"></span></a>
                                </td>
                                <td>
                                    <%if (rset.getInt(9) == 1) {%>
                                    <input class="hidden" name="fol_gnkl" id="fol_gnkl_<%=renglon%>" value="<%=rset.getString(1)%>">
                                    <input class="hidden" name="Unidad" id="Unidad_<%=renglon%>" value="<%=rset.getString(8)%>">
                                    <input class="hidden" name="ClaCli" id="ClaCli_<%=renglon%>" value="<%=rset.getString(2)%>">
                                    <button class="btn btn-block btn-info" name="accion" type="button" onclick="procesar(<%=renglon%>)" value="EnviarRequeFact"><span class="glyphicon glyphicon-ok"></span></button>
                                        <%}%>
                                </td>
                                <td>
                                    <%if (rset.getInt(9) == 1) {%>
                                    <form action="EditarRequeFarmacia.jsp" method="get">
                                        <input class="hidden" name="fol_gnkl" id="fol_gnkl" value="<%=rset.getString(1)%>">
                                        <input class="hidden" name="Unidad" id="Unidad" value="<%=rset.getString(8)%>">
                                        <input class="hidden" name="ClaCli" id="ClaCli" value="<%=rset.getString(2)%>">
                                        <input class="hidden" name="nomUnidad" id="nomUnidad" value="<%=rset.getString(3)%>">
                                        <input class="hidden" name="fecha"  value="<%=rset.getString("fecha_entrega")%>">
                                        <button class="btn btn-block btn-info" name="accion" type="submit" id="EditarRequerimiento" value="EditarRequerimiento"><span class="glyphicon glyphicon-edit"></span></button>
                                    </form>
                                    <%}%>
                                </td>
                            </tr>
                            <%
                                        }

                                        con.cierraConexion();
                                    }
                                } catch (Exception e) {

                                }
                            %>
                        </tbody>
                    </table>
                        <br>
                    <div class="row">
                        <div class="col-md-3">
                            <a class="btn btn-block btn-primary" href="gnrReqFarmaciaMult.jsp?UnidadSe=<%=UnidadSe%>&fecha1=<%=fecha1%>&fecha2=<%=fecha2%>&juris=<%=juris%>&fechaCap1=<%=fechaCap1%>&fechaCap2=<%=fechaCap2%>"><span class="glyphicon glyphicon-save"></span> Descargar todo</a></div>

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
                
                 $(document).ready(function() {
                                $('#datosCompras').dataTable();
                            });
                
                function editar(valor)
                {
                    //alert(valor);
                    $("#idRequerimiento").val(valor);
                    //$("#btnModal").click();

                }
            </script>
    </body>

</html>

