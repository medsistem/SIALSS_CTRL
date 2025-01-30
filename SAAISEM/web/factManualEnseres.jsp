<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<%@page import="Facturacion.FacturacionManual" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    FacturacionManual fact = new FacturacionManual();
    HttpSession sesion = request.getSession();
    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
    String usua = "", tipo = "", Desproyecto = "", ProyectoU="";
    int Proyecto = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "";

    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
    } catch (Exception e) {

    }

    if (ClaCli == null) {
        ClaCli = "";
    }
    if (FechaEnt == null) {
        FechaEnt = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
    }
    if (DesPro == null) {
        DesPro = "";
    }
    try {

        con.conectar();
        ResultSet rsetProy = con.consulta("SELECT P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE PU.F_Usuario = '" + usua + "';");
        if (rsetProy.next()) {
            Proyecto = rsetProy.getInt(1);
            ProyectoU = rsetProy.getString(1);
            Desproyecto = rsetProy.getString(2);
        }

        ResultSet rset = con.consulta("SELECT F_Folio, F_Sts, F_ClaCli, F_Fecha, F_Usuario FROM tb_enserestemp WHERE F_Usuario = '" + usua + "';");
        rset.last();
        //while (rset.next()) {
        if (rset.getString("F_StsFact").equals("3") && rset.getString("F_User").equals(usua)) {
            sesion.setAttribute("F_IndGlobal", rset.getString(1));
            F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
            ClaCli = rset.getString("F_ClaCli");
            sesion.setAttribute("ClaCliFM", ClaCli);
            FechaEnt = rset.getString("F_FecEnt");
        }
        //}

        con.cierraConexion();

    } catch (Exception e) {

    }

    try {
        if (request.getParameter("accion").equals("nuevoFolio")) {
            sesion.setAttribute("F_IndGlobal", fact.dameIndGlobalEnseres(ProyectoU) + "");
            F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
        }
    } catch (Exception e) {

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
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <link href="css/select2.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>CEDIS CENTRAL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="row">
                <div class="col-sm-12">
                    <h2>Facturación Manual Enseres</h2>
                </div>
            </div>
            <hr/>
            <%                if (F_IndGlobal == null) {
            %>
            <form action="factManualEnseres.jsp" method="post">
                <button class="btn btn-block btn-primary" name="accion" value="nuevoFolio">Nuevo Folio Enseres</button>
            </form>
            <%
            } else {
            %>
            <form action="FacturacionManual" method="post">
                <h4 class="text-muted">Folio: <%=F_IndGlobal%><input name="folio" id="folio" class="form-control" type="hidden" value="<%=F_IndGlobal%>" /></h4>
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Unidad:</h4>
                    </div>
                    <div class="col-sm-4">
                        <input value="<%=ClaCli%>"  class="form-control" name="ClaCli" id="ClaCli"  />
                        <!--select class="form-control" name="ClaCli" id="ClaCli">
                            <option value="">-Seleccione Unidad-</option>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn");
                                while (rset.next()) {
                        %>
                        <option value="<%=rset.getString(1)%>"
                        <%
                            if (rset.getString(1).equals(ClaCli)) {
                                out.println("selected");
                            }
                        %>
                        ><%=rset.getString(2)%></option>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                out.println(e.getMessage());
                            }
                        %>
                    </select-->
                    </div>
                    <div class="col-sm-2">
                        <h4>Fecha de Entrega</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" name="FechaEnt" id="FechaEnt" min="<%=df2.format(new Date())%>" value="<%=FechaEnt%>"/>
                    </div>
                    <div class="col-sm-1">
                        <h4>Proyecto</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="text" readonly="" class="form-control" name="DesProyecto" id="DesProyecto" value="<%=Desproyecto%>"/>
                        <input type="hidden" class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                    </div>
                </div> 
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Seleccione:</h4>
                            </div>
                            <div class="col-sm-6">
                                <select class="form-control" name="ClaPro" id="ClaPro">
                                </select>
                            </div>
                        </div>
                        <hr />
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Cantidad:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="Cantidad" id="Cantidad" onKeyPress="return justNumbers(event);"/>
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-block btn-info" type="button" value="GuardarTemp" id="GuardarTemp" >Seleccionar</button>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="panel panel-primary table-responsive" id="folDetalle"  >
                    <br/>
                    <div class="panel-body" id="tableTot" >
                        <div id="dynamic"></div>
                    </div>
                </div>

                <div class="row">
                    <h4 class="col-sm-2">Observaciones</h4>
                    <div class="col-sm-4">
                        <textarea class="form-control" name="obs" id="obs"></textarea>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-sm-6">
                        <button class="btn btn-block btn-primary" name="accion" type="button" id="BtnConfirmar" value="ConfirmarFactura">Confirmar Factura</button>
                        <!--button class="btn btn-block btn-primary" name="accion" id="BtnConfirmar" value="ConfirmarFactura" onclick="return confirm('Seguro de confirmar la Factura?')">Confirmar Factura</button-->
                    </div>
                    <div class="col-sm-6">
                        <button class="btn btn-block btn-info" type="button" id="CancelarFactura">Cancelar Factura</button>
                    </div>
                </div>

            </form>
            <%
                }
            %>
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
        <script src="js/bootstrap-datepicker.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/funcIngresos.js"></script>
        <script src="js/facturajs/FacturacionEnseres.js"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script src="js/select2.js" type="text/javascript"></script>
        <script>

                            $(document).ready(function () {
                                $("#ClaPro").select2();
                            });
                            $(function () {
                                $("#ClaCli").keyup(function () {
                                    var nombre = $("#ClaCli").val();

                                    $("#ClaCli").autocomplete({
                                        source: "JQInvenCiclico?accion=BuscarUnidad&nom_uni=" + nombre,
                                        select: function (event, ui) {
                                            $("#ClaCli").val(ui.item.nom_com);
                                            return false;
                                        }
                                    }).data("ui-autocomplete")._renderItem = function (ul, item) {
                                        return $("<li>")
                                                .data("ui-autocomplete-item", item)
                                                .append("<a>" + item.nom_com + "</a>")
                                                .appendTo(ul);
                                    };
                                });
                            });

                            function justNumbers(e)
                            {
                                var keynum = window.event ? window.event.keyCode : e.which;
                                if ((keynum === 8) || (keynum === 46))
                                    return true;
                                return /\d/.test(String.fromCharCode(keynum));
                            }

                            function cambiaLoteCadu(elemento) {
                                var indice = elemento.selectedIndex;
                                document.getElementById('SelectCadu').selectedIndex = indice;
                            }

                            function validaBuscar() {
                            }


                            function validaSeleccionar() {
                                var DesSel = document.getElementById('DesSel').value;
                                if (DesSel === "") {
                                    alert('Favor de Capturar Toda la información');
                                    return false;
                                }
                                var cantidad = document.getElementById('Cantidad').value;
                                if (cantidad === "") {
                                    alert('Escriba una cantidad');
                                    return false;
                                }

                            }



                            function validaUni() {

                                var Unidad = document.getElementById('ClaCli').value;
                                if (Unidad === "") {
                                    alert('Seleccione Unidad');
                                    return false;
                                }

                                var FechaEnt = document.getElementById('FechaEnt').value;
                                if (FechaEnt === "") {
                                    alert('Seleccione Fecha de Entrega');
                                    return false;
                                }
                                var clave = document.getElementById('ClaPro').value;
                                if (clave === "") {
                                    alert('Escriba una Clave');
                                    return false;
                                }

                                var causes = document.getElementById('ClaCli').value;

                                if (causes === "") {
                                    alert('Capture Diagnóstico válido');
                                    e.focus();
                                    return false;
                                }
                                var causesArray = causes.split(" - ");
                                causes = causesArray[0];
                                var causesTodos = "";
            <%
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn ");
                    while (rset.next()) {
            %>
                                causesTodos = causesTodos + "<%=rset.getString("F_ClaCli")%>-";
            <%
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
                                var causesTodosArray = causesTodos.split('-');
                                var ban1 = 0;
                                for (i = 0; i <= causesTodosArray.length; i++) {
                                    if (causes === causesTodosArray[i]) {
                                        return true;
                                        ban1 = 1;
                                    }
                                }
                                if (ban1 === 0) {
                                    alert('Capture Unidad válida');
                                    return false;
                                }
                            }
        </script>
    </body>

</html>

