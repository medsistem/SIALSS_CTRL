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
    String usua = "", tipo = "", ProyectoCL = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        ProyectoCL = (String) sesion.getAttribute("ProyectoCL");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String FechaIni = "", FechaFin = "";
    try {
        FechaIni = request.getParameter("FechaIni");
        FechaFin = request.getParameter("FechaFin");
    } catch (Exception e) {

    }
    if (FechaIni == null) {
        FechaIni = "";
    }
    if (FechaFin == null) {
        FechaFin = "";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <hr/>

            <div>
                <h3>Reimpresion de folios de Compras</h3>
                <form name="FormOC" action="reimpresionCliente.jsp" method="Post">
                    <div class="row">
                        <h4 class="col-sm-2">Rango de Fecha</h4>
                        <div class="col-sm-2">
                            <input class="form-control" name="FechaIni" id="FechaIni" type="date"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" name="FechaFin" id="FechaFin" type="date"/>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-primary" name="accion" value="Clave">Por Fecha</button>
                        </div>
                    </div>
                    <br />
                </form>
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <table class="table table-bordered table-striped" id="datosCompras">
                                <thead>
                                    <tr>
                                        <td>No. Folio</td>
                                        <td>Folio Remisión</td>
                                        <td>Orden de Compra</td>
                                        <td>Fecha Ingreso</td>
                                        <td>Usuario</td>
                                        <td>Proveedor</td>
                                        <td>Compra</td>
                                        <th>Ver Compra</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            if ((FechaIni != "") && (FechaFin != "")) {
                                                ResultSet rset = con.consulta("SELECT c.F_ClaDoc, CASE WHEN c.F_FolRemi = '' THEN c.F_FolFac ELSE c.F_FolRemi END  AS Remision, c.F_OrdCom, c.F_FecApl, c.F_User, p.F_NomPro FROM tb_compra c, tb_proveedor p where c.F_ProVee = p.F_ClaProve AND c.F_FecApl BETWEEN '" + FechaIni + "' AND '" + FechaFin + "' AND c.F_Proyecto = 2 GROUP BY F_OrdCom, F_FolRemi,c.F_FecApl,F_ClaDoc ORDER BY F_FolRemi ASC; ");
                                                  // ResultSet rset = con.consulta("SELECT c.F_ClaDoc, c.F_FolRemi, c.F_OrdCom, c.F_FecApl, c.F_User, p.F_NomPro FROM tb_compra c, tb_proveedor p where c.F_ProVee = p.F_ClaProve AND c.F_FecApl BETWEEN '" + FechaIni + "' AND '" + FechaFin + "' AND F_Proyecto IN (" + ProyectoCL + ") GROUP BY F_OrdCom, F_FolRemi,c.F_FecApl ORDER BY F_FolRemi ASC; ");
                                                while (rset.next()) {
                                    %>
                                    <tr>

                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=df3.format(df2.parse(rset.getString(4)))%></td>
                                        <td><%=rset.getString(5)%></td>
                                        <td><%=rset.getString(6)%></td>
                                        <td>
                                            <a href="reimpReporte.jsp?F_FolRemi=<%=rset.getString("Remision")%>&F_OrdCom=<%=rset.getString("F_OrdCom")%>&fol_gnkl=<%=rset.getString(1)%>&fecha=<%=rset.getString(4)%>" target="_blank" class="btn btn-block btn-primary">ACUSE MDF</a>
                                        </td>
                                        <td>
                                            <form action="verCompra.jsp" method="post">
                                                <input class="hidden" name="F_FolRemi" value="<%=rset.getString("Remision")%>">
                                                <input class="hidden" name="F_OrdCom" value="<%=rset.getString("F_OrdCom")%>">
                                                <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                                 <input class="hidden" name="F_FecApl" value="<%=rset.getString(4)%>">                                              
                                                <button class="btn btn-block btn-default">Ver Compra</button>
                                            </form>
                                        </td>
                                    </tr>
                                    <%
                                                }
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
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
        <script>
            $(document).ready(function () {
                $('#datosCompras').dataTable();
            });
        </script>
        <script>
            $(function () {
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });

            function ponerFolio(id) {
                document.getElementById('idCom').value = id;
                document.getElementById('F_FolRemi').value = document.getElementById("F_FR" + id).value;
                document.getElementById('F_OrdCom').value = document.getElementById("F_OC" + id).value;
            }

            function validaISEM() {
                if (document.getElementById('NoContrato').value === "") {
                    alert('Capture el número de contrato');
                    return false;
                }
                if (document.getElementById('NoFolio').value === "") {
                    alert('Capture el número de folio');
                    return false;
                }
                if (document.getElementById('fecRecepcionISEM').value === "") {
                    alert('Capture la fecha');
                    return false;
                }
            }

            function ponerRemision(id) {
                var elem = id.split(',');
                document.getElementById('idRem').value = elem[0];
                document.getElementById('remiIncorrecta').value = elem[1];
            }

            function validaRemision() {
                var remiCorrecta = document.getElementById('remiCorrecta').value;
                var fecRemision = document.getElementById('fecRemision').value;

                if (remiCorrecta === "" && fecRemision === "") {
                    alert('Ingrese al menos una corrección')
                    return false;
                }
            }

            function validaContra(elemento) {
                //alert(elemento);
                var pass = document.getElementById(elemento).value;
                //alert(pass);
                if (pass === "GnKlTolu2014") {
                    document.getElementById('actualizaRemi').disabled = false;
                } else {
                    document.getElementById('actualizaRemi').disabled = true;
                }
            }
        </script>
    </body>
</html>