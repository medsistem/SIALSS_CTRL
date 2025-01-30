<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="conn.ConectionDB"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();

    Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema = formateador.format(fechaActual);
    String Folio1 = "", Folio2 = "", Unidad = "", FechaI = "", FechaF = "", usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        Folio1 = request.getParameter("folio1");
        Folio2 = request.getParameter("folio2");
        Unidad = request.getParameter("Unidad");
        FechaI = request.getParameter("fecha_ini");
        FechaF = request.getParameter("fecha_fin");

    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    if (Folio1 == null) {
        Folio1 = "";
    }
    if (Folio2 == null) {
        Folio2 = "";
    }
    if (Unidad == null) {
        Unidad = "";
    }
    if (FechaI == null) {
        FechaI = "";
    }
    if (FechaF == null) {
        FechaF = "";
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet"  />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link href="css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" >
        <link href="css/select2.css" rel="stylesheet" />
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIAALS</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="row">
                <h4 class="col-sm-3">Reporte incidencias en farmacia</h4>
            </div>
            <form name="forma1" id="forma1" action="Reporte_incidencia.jsp" method="post">
                <div class="panel-heading">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Folios</label>
                        <div class="col-lg-2">
                            <input class="form-control" id="folio1" name="folio1" type="text" value="" onchange="habilitar(this.value);" />
                        </div>
                        <div class="col-lg-2">
                            <input class="form-control" id="folio2" name="folio2" type="text" value="" onchange="habilitar(this.value);"/>
                        </div>
                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" value="" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" value="" type="date" onchange="habilitar(this.value);"/>
                        </div>
                    </div>
                    <br />
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Unidad</label>
                        <div class="col-sm-6">
                            <select name="Unidad" id="Unidad" class="form-control">
                                <option value="0">--Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT A.unidad, U.F_NomCli FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad  = U.F_IdReporte GROUP BY A.unidad;");

                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>

                            </select>
                        </div>
                    </div>
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <button class="btn btn-block btn-info" name="Accion" value="mostrar" >MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                        </div>
                        <div class="col-sm-6">
                            <a class="btn btn-block btn-primary" href="Reporte_incidenciagnr.jsp?Folio1=<%=Folio1%>&Folio2=<%=Folio2%>&Unidad=<%=Unidad%>&FechaI=<%=FechaI%>&FechaF=<%=FechaF%>" target="_blank">DESCARGAR&nbsp;<label class="glyphicon glyphicon-download"></label></a>
                        </div>
                    </div>
                </div>  
                <br/>
              </form>
        </div>
        <div class="container-fluid ">            
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped table-responsive"  id="datosComprasIF" >
                            <thead class="bg-success">
                                <tr>
                                    <th class="text-center">Fecha</th>
                                    <th class="text-center">No. Folio</th>
                                    <th class="text-center">Nombre Unidad</th>
                                    <th class="text-center">Clave</th>
                                    <th class="text-center">Tipo de Insumo</th>
                                    <th class="text-center">Lote</th>
                                    <th class="text-center">Caducidad</th>
                                    <th class="text-center">Requerido Farmacia</th>
                                    <th class="text-center">Remisionado</th>
                                    <th class="text-center">Entregado Farmacia</th>
                                    <th class="text-center">Diferencia Remisionado-Requerido</th>
                                    <th class="text-center">Diferencia Entregado-Remisionado</th>
                                    <th class="text-center">Diferencia Requerido-Entregado</th>
                                    <th class="text-center">Tipo</th>
                                    <th class="text-center">Observación</th>
                                    <th class="text-center">Tipificacion</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        String Condicion = "";
                                        if ((!(Unidad.equals("0"))) && (!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.unidad='" + Unidad + "' AND A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";

                                        } else if ((!(Unidad.equals("0"))) && (!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.unidad='" + Unidad + "' AND A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals(""))) && (!(Unidad.equals("0")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "' AND A.unidad='" + Unidad + "' ";

                                        } else if ((!(Folio1.equals(""))) && (!(Folio2.equals("")))) {
                                            Condicion = "A.abasto BETWEEN '" + Folio1 + "' AND '" + Folio2 + "'";

                                        } else if (!(Unidad.equals("0"))) {
                                            Condicion = "A.unidad='" + Unidad + "'";
                                        } else if ((!(FechaI.equals(""))) && (!(FechaF.equals("")))) {
                                            Condicion = "A.fecha BETWEEN '" + FechaI + "' AND '" + FechaF + "' ";
                                        }
                                        // ResultSet rset = con.consulta("SELECT DATE_FORMAT(A.fecha,'%d/%m/%Y') AS fecha, A.abasto, U.F_NomCli, A.clave, A.lote, DATE_FORMAT(A.caducidad, '%d/%m/%Y') AS caducidad, A.cantidad, A.observacion, F.F_CantReq, F.F_CantSur,(A.cantidad - F.F_CantSur ) AS DIFERENCIA , CASE WHEN F.F_Ubicacion = 'CONTROLADO'  THEN 'CONTROLADO' WHEN F.F_Ubicacion = 'APE'  THEN 'ALTO COSTO' WHEN F.F_Ubicacion = 'REDFRIA'  THEN  'RED FRIA' ELSE 'ORDINARIO'  END AS 'TIPO INSUMO' ,TI.F_DetalleIncidencia AS TIPO, TI.F_IdTdI FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad = U.F_IdReporte INNER JOIN tb_tipoinc AS TI ON A.tipo = TI.F_IdTdI INNER JOIN tb_factura AS F ON F.F_ClaDoc = A.abasto collate utf8_unicode_ci and F_ClaPro = A.clave collate utf8_unicode_ci WHERE " + Condicion + " GROUP BY A.unidad, A.abasto, A.clave, A.lote, A.caducidad, A.observacion, A.fecha;");
                                          ResultSet rset = con.consulta("SELECT DATE_FORMAT(A.fecha,'%d/%m/%Y') AS fecha, A.abasto, U.F_NomCli, A.clave, A.lote, DATE_FORMAT(A.caducidad, '%d/%m/%Y') AS caducidad, A.cantidad, A.observacion, F.F_CantReq, F.F_CantSur,(F.F_CantSur - F.F_CantReq ) AS 'DIFERENCIAr-r' , CASE WHEN F.F_Ubicacion = 'CONTROLADO'  THEN 'CONTROLADO' WHEN F.F_Ubicacion = 'APE'  THEN 'ALTO COSTO' WHEN F.F_Ubicacion = 'REDFRIA'  THEN  'RED FRIA' ELSE 'ORDINARIO'  END AS 'TIPO INSUMO' ,TI.F_DetalleIncidencia AS TIPO, TI.F_IdTdI,	( A.cantidad - F.F_CantSur ) AS 'DIFERENCIAe-r',(A.cantidad - F.F_CantReq) AS 'DIFERENCIAr-e' FROM tb_abastos_incidencias A INNER JOIN tb_uniatn U ON A.unidad = U.F_IdReporte INNER JOIN tb_tipoinc AS TI ON A.tipo = TI.F_IdTdI INNER JOIN tb_factura AS F ON F.F_ClaDoc = A.abasto collate utf8_unicode_ci and F_ClaPro = A.clave collate utf8_unicode_ci WHERE " + Condicion + " GROUP BY A.unidad, A.abasto, A.clave, A.lote, A.caducidad, A.observacion, A.fecha;");

                                        while (rset.next()) {
                                %>
                                <tr>
                                    <td class="text-center"><%=rset.getString(1)%></td>
                                    <td class="text-center"><%=rset.getString(2)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <td class="text-center"><%=rset.getString(4)%></td>
                                    <td><%=rset.getString(12)%></td>
                                    <td class="text-center"><%=rset.getString(5)%></td>
                                    <td class="text-center"><%=rset.getString(6)%></td>
                                    <td class="text-center"><%=rset.getString(9)%></td>
                                    <td class="text-center"><%=rset.getString(10)%></td>
                                    <td class="text-center"><%=rset.getString(7)%></td>
                                    <td class="text-center"><%=rset.getString(11)%></td>
                                    <td class="text-center"><%=rset.getString(15)%></td>
                                    <td class="text-center"><%=rset.getString(16)%></td>
                                    <td><%=rset.getString(13)%></td>
                                    <td><%=rset.getString(8)%></td>
                                    <td class="text-center"><%=rset.getString(14)%></td>
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
        <script src="js/bootstrap-datepicker.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/select2.js"></script>
        <script src="js/reporteador/datatables.min.js"></script>
        <script>
    $(document).ready(function () {
              
                 $('#datosComprasIF').DataTable({
                
                "deferRender": true,
                "scrollX": true,
                "scrollY": 550,
                
                "scroller": true,
                
                "columnDefs": [
                    {
                        "targets": [0],
                        "visible": false,
                        "searchable": false
                    }
                ]
            });
                                });
        </script>
    </body>
</html>
