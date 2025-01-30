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
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String folio_gnk = "", fecha = "", folio_remi = "", orden = "", provee = "", recib = "", entrega = "", origen = "", coincide = "", observaciones = "", clave = "", descrip = "", cod_bar = "", um = "", lote = "", cadu = "", cajas = "", piezas = "", tarimas = "", marca = "", fec_fab = "", proveedor = "", tarimasInc = "";
    int PzxCaja = 0, tarimasC = 0, tarimasI = 0, cajasPorTarimaC = 0, cajasPorTarimaI = 0, resto = 0, cajasI = 0;

    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca, F_Cb, F_Cajas, F_Pz, F_Tarimas, F_Resto, F_TarimasI, F_CajasI, F_FolRemi, F_Provee from tb_compratemp where F_IdCom = '" + ((String) sesion.getAttribute("id")) + "' ");
        while (rset.next()) {
            folio_gnk = rset.getString("F_FolRemi");
            fecha = (String) session.getAttribute("fecha");
            folio_remi = rset.getString("F_FolRemi");
            orden = rset.getString("F_FolRemi");
            provee = rset.getString("F_Provee");
            recib = (String) session.getAttribute("recib");
            entrega = (String) session.getAttribute("entrega");
            clave = (String) session.getAttribute("clave");
            descrip = (String) session.getAttribute("descrip");
            clave = rset.getString("F_ClaPro");
            lote = rset.getString("F_Lote");
            cadu = df3.format(df2.parse(rset.getString("F_FecCad")));
            fec_fab = df3.format(df2.parse(rset.getString("F_FecFab")));
            cod_bar = rset.getString("F_Cb");
            cajas = rset.getString("F_Cajas");
            piezas = rset.getString("F_Pz");
            tarimas = rset.getString("F_Tarimas");
            tarimasInc = rset.getString("F_TarimasI");
            cajasI = rset.getInt("F_CajasI");
            resto = rset.getInt("F_Resto");
            ResultSet rset2 = con.consulta("select F_DesPro, F_PrePro from tb_medica where F_ClaPro = '" + clave + "' ");
            while (rset2.next()) {
                descrip = rset2.getString(1);
                um = rset2.getString(2);
            }

            rset2 = con.consulta("select F_DesMar from tb_marca where F_ClaMar = '" + rset.getString("F_Marca") + "' ");
            while (rset2.next()) {
                marca = rset2.getString(1);
            }
            PzxCaja = (Integer.parseInt(piezas) - resto) / Integer.parseInt(cajas);
            tarimasC = Integer.parseInt(tarimas) - Integer.parseInt(tarimasInc);
            tarimasI = Integer.parseInt(tarimasInc);
            cajasPorTarimaC = (Integer.parseInt(cajas) - cajasI) / tarimasC;
            cajasPorTarimaI = cajasI;
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    try {

        con.conectar();
        ResultSet rset = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + provee + "' ");
        while (rset.next()) {
            proveedor = rset.getString(1);
        }
        con.cierraConexion();

    } catch (Exception e) {
    }
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
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
        <!-- -->

    </head>

    <body>
        <div class="container">
            <h1>SIE</h1>
            <div class="navbar navbar-default">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <span clss="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="main_menu.jsp">Inicio</a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Entradas<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <%
                                        if (tipo.equals("2") || tipo.equals("3") || tipo.equals("1")) {
                                    %>

                                    <li><a href="captura.jsp">Entrada Manual</a></li>
                                    <li><a href="compraAuto2.jsp">Entrada Automática OC</a></li>
                                    <li class="divider"></li>
                                    <li><a href="hh/compraAuto3.jsp">HANDHELD | Entrada Automática OC</a></li>
                                    <li class="divider"></li>
                                        <%
                                            }
                                            if (tipo.equals("2") || tipo.equals("3") || tipo.equals("5")) {
                                        %>
                                    <li><a href="verificarCompraAuto.jsp">Verificar OC</a></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="#" onclick="window.open('reimpresion.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Reimpresión de Compras</a></li>
                                    <li><a href="#"  onclick="window.open('ordenesCompra.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Órdenes de Compras</a></li>
                                    <li><a href="#"  onclick="window.open('kardexClave.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Kardex Claves</a></li>
                                    <li><a href="#"  onclick="window.open('Ubicaciones/Consultas.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Ubicaciones</a></li>
                                    <li><a href="#"  onclick="window.open('creaMarbetes.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Generar Marbetes</a></li>
                                    
                                        <%
                                            if (tipo.equals("5") || tipo.equals("3")) {
                                        %>
                                    <li class="divider"></li>
                                    <li><a href="hh/insumoNuevoRedist.jsp">Redistribución HH</a></li>
                                    <li class="divider"></li>
                                        <%
                                            }
                                        %>
                                        <%
                                            if (usua.equals("oscar")) {
                                        %>
                                    <li class="divider"></li>
                                    <li><a href="#"  onclick="window.open('devolucionesInsumo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Cambio Físico</a></li>
                                    <li class="divider"></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="#"  onclick="window.open('Ubicaciones/index_Marbete.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Marbete de Salida</a></li>
                                    <li><a href="#"  onclick="window.open('Ubicaciones/index_Marbete_resto.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Marbete de Resto</a></li>
                                    <!--li><a href="#"  onclick="window.open('verDevolucionesEntrada.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Imprimir Devoluciones</a></li>
                                    <li><a href="#"  onclick="window.open('devolucionesInsumo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Devolver</a></li-->
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Facturación<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <%
                                        if (tipo.equals("7") || tipo.equals("3")) {
                                    %>
                                    <li><a href="requerimiento.jsp">Carga de Requerimiento</a></li>
                                    <li><a href="factura.jsp">Facturación Automática</a></li>
                                        <%
                                            }
                                        %>
                                        <%
                                           if (tipo.equals("5") || tipo.equals("3") || tipo.equals("7") || tipo.equals("2")) {
                                        %>
                                    <li><a href="validacionSurtido.jsp">Validación Surtido</a></li>
                                    <li><a href="validacionAuditores.jsp">Validación Auditores</a></li>
                                        <%
                                            }
                                        %>
                                        <%
                                            if (tipo.equals("7")) {
                                        %>
                                    <li><a href="remisionarCamion.jsp">Generar Remisiones</a></li>
                                    <li><a href="facturacionManual.jsp">Facturación Manual</a></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="reimp_factura.jsp">Administrar Remisiones</a></li>
                                    <li><a href="reimpConcentrado.jsp">Reimpresión Concentrados Globales</a></li>
                                    <li><a href="comparativoGlobal.jsp">Comparativo Global</a></li>

                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Inventario<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#"  onclick="window.open('Ubicaciones/Inventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Inventario</a></li>
                                        <%
                                           if (tipo.equals("5") || tipo.equals("3") || tipo.equals("7") || tipo.equals("2")) {
                                        %>
                                    <li><a href="#"  onclick="window.open('movimientosUsuarioInventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Movimientos por Usuario</a></li>
                                    <li><a href="#"  onclick="window.open('semaforo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Semaforización</a></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="#"  onclick="window.open('invenCiclico/nuevoInventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Inventario Ciclico</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Catálogos<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#" onclick="window.open('medicamento.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Medicamento</a></li>
                                    <li><a href="#" onclick="window.open('catalogo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Proveedores</a></li>
                                    <li><a href="#" onclick="window.open('marcas.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Marcas</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Reportes<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#" onclick="window.open('Entrega.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Fecha de Recibo en CEDIS</a></li> 
                                    <li><a href="#" onclick="window.open('historialOC.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Historial OC</a></li>
                                    <li><a href="#" onclick="window.open('ReporteF.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Reporte por Fecha Proveedor</a></li>

                                </ul>
                            </li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="#"><span class="glyphicon glyphicon-user"></span> <%=usua%></a></li>
                            <li class="active"><a href="index.jsp"><span class="glyphicon glyphicon-log-out"></span></a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Edición de Insumo</h3>
                </div>
                <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="Modificaciones">
                    <div class="panel-body">
                        <div class="form-group">
                            <div class="form-group">
                                <label for="folio" class="col-sm-2 control-label">Folio CEDIS CENTRAL</label>
                                <div class="col-sm-2">
                                    <input type="folio" class="form-control" id="folio" name="folio" placeholder="Folio" readonly value="<%=folio_gnk%>"/>
                                </div>
                                <label for="fecha" class="col-sm-1 control-label">ID</label>
                                <div class="col-sm-2">
                                    <input type="fecha" class="form-control" id="id" name="id" placeholder="Fecha" readonly value="<%=((String) sesion.getAttribute("id"))%>">
                                </div>
                                <div class="col-sm-2">
                                    <a class="btn btn-block btn-primary"  href="verificarCompraAuto.jsp" >Regresar</a>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label for="fol_rem" class="col-sm-2 control-label">Folio Remisión</label>
                                <div class="col-sm-3">
                                    <input type="fol_rem" class="form-control" id="folio_remi" name="folio_remi" placeholder="Folio Remisión" onKeyPress="return tabular(event, this)"  value="<%=folio_remi%>" >
                                </div>
                                <label for="orden" class="col-sm-2 control-label">Orden de Compra</label>
                                <div class="col-sm-3">
                                    <input type="orden" class="form-control" id="orden" name="orden" placeholder="Orden de Compra" onKeyPress="return tabular(event, this)" value="<%=orden%>"/ readonly="readonly">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label for="prov" class="col-sm-2 control-label">Proveedor</label>
                                <div class="col-sm-3">
                                    <input type="prov" class="form-control" id="provee" name="provee" placeholder="Proveedor" onKeyPress="return tabular(event, this)" value="<%=proveedor%>" readonly />
                                </div>

                                <label for="recib" class="col-sm-2 control-label">Recibido por</label>
                                <div class="col-sm-3">
                                    <input type="recib" class="form-control" id="recib" name="recib" placeholder="Recibe" onKeyPress="return tabular(event, this)" value = "<%=usua%>" readonly>
                                </div>

                            </div>
                        </div>

                        <!-- En duda -->
                        <!--button class="btn btn-block btn-info">Guardar</button-->
                        <!-- En duda -->
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <label for="clave1" class="col-sm-1 control-label">Clave</label>
                            <div class="col-sm-2">
                                <input type="clave1" class="form-control" id="clave1" name="clave1" placeholder="Clave" value="<%=clave%>" readonly onKeyPress="return tabular(event, this)">
                            </div>
                            <label for="descr1" class="col-sm-1 control-label">Descripción</label>
                            <div class="col-sm-3">
                                <textarea class="form-control" name="descripci" id="descripci" readonly onKeyPress="return tabular(event, this)"><%=descrip%></textarea>
                            </div>
                            <label for="cb" class="col-sm-2 control-label">Código de Barras</label>
                            <div class="col-sm-2">
                                <input type="cb" class="form-control" id="cb" name="cb" placeholder="C. B." onKeyPress="return tabular(event, this)" value="<%=cod_bar%>" />
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <label for="Marca" class="col-sm-1 control-label">Marca</label>
                            <div class="col-sm-2">
                                <input type="Marca" class="form-control" id="Marca" name="Marca" readonly="" placeholder="Marca" onKeyPress="return tabular(event, this)" value="<%=marca%>" />
                            </div>
                            <label for="pres" class="col-sm-1 control-label">Presentación</label>
                            <div class="col-sm-2">
                                <textarea class="form-control" placeholder="Presentación" name="pres" readonly="" onKeyPress="return tabular(event, this)" ><%=um%></textarea>
                            </div>
                            <label for="Lote" class="col-sm-1 control-label">Lote</label>
                            <div class="col-sm-2">
                                <input type="Lote" class="form-control" id="Lote" name="Lote" placeholder="Lote" onKeyPress="return tabular(event, this)" value="<%=lote%>" />
                            </div>
                            <label for="FecFab" class="col-sm-1 control-label">Fec Fab</label>
                            <div class="col-sm-2">
                                <input data-date-format="dd/mm/yyyy" readonly="readonly" type="text" class="form-control" id="FecFab" name="FecFab" placeholder="FecFab" onKeyPress="LP_data();
                                        anade(this);
                                        return tabular(event, this)" maxlength="10" value="<%=fec_fab%>" />
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <label for="Caducidad" class="col-sm-1 control-label">Cadu</label>
                            <div class="col-sm-2">
                                <input data-date-format="dd/mm/yyyy" type="text" readonly="readonly" class="form-control" id="Caducidad" name="Caducidad" placeholder="Caducidad" onKeyPress="return tabular(event, this)" value="<%=cadu%>" />
                            </div>

                        </div>
                        <br/>
                        <h5><strong>Tarimas Completas</strong></h5>
                        <div class="row">

                            <label for="Cajas" class="col-sm-2 control-label">Tarimas</label>
                            <div class="col-sm-1">
                                <input type="Cajas" class="form-control" id="TarimasC" name="TarimasC" placeholder="0" onKeyPress="return justNumbers(event);
                                        return handleEnter(even);" onkeyup="totalPiezas();" value="<%=tarimasC%>" />
                            </div>
                            <label for="pzsxcaja" class="col-sm-2 control-label">Cajas x Tarima</label>
                            <div class="col-sm-1">
                                <input type="pzsxcaja" class="form-control" id="CajasxTC" name="CajasxTC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" value="<%=cajasPorTarimaC%>" />
                            </div>
                            <label for="Resto" class="col-sm-2 control-label">Piezas x Caja</label>
                            <div class="col-sm-1">
                                <input type="Resto" class="form-control" id="PzsxCC" name="PzsxCC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" value="<%=PzxCaja%>" />
                            </div>
                        </div>
                        <br/>
                        <h5><strong>Tarimas Incompletas</strong></h5>
                        <div class="row">

                            <label for="Cajas" class="col-sm-2 control-label">Tarimas</label>
                            <div class="col-sm-1">
                                <input type="Cajas" class="form-control" id="TarimasI" name="TarimasI" placeholder="0" onKeyPress="return justNumbers(event);
                                        return handleEnter(even);" onkeyup="totalPiezas();" value="<%=tarimasI%>" />
                            </div>
                            <label for="pzsxcaja" class="col-sm-2 control-label">Cajas x Tarima</label>
                            <div class="col-sm-1">
                                <input type="pzsxcaja" class="form-control" id="CajasxTI" name="CajasxTI" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" value="<%=cajasPorTarimaI%>" />
                            </div>
                            <label for="pzsxcaja" class="col-sm-2 control-label">Resto</label>
                            <div class="col-sm-1">
                                <input type="pzsxcaja" class="form-control" id="Resto" name="Resto" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" value="<%=resto%>" />
                            </div>
                        </div>
                        <br/>
                        <h5><strong>Totales</strong></h5>
                        <div class="row">

                            <label for="Cajas" class="col-sm-2 control-label">Tarimas</label>
                            <div class="col-sm-1">
                                <input type="text" class="form-control" id="Tarimas" name="Tarimas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                        return handleEnter(even);" onkeyup="totalPiezas();"  value="<%=tarimas%>" />
                            </div>
                            <label for="pzsxcaja" class="col-sm-2 control-label">Cajas x Tarima</label>
                            <div class="col-sm-1">
                                <input type="text" class="form-control" id="Cajas" name="Cajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" value="<%=cajas%>"  />
                            </div>
                            <label for="Resto" class="col-sm-2 control-label">Piezas x Caja</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="Piezas" name="Piezas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();"  value="<%=piezas%>" />
                            </div>
                        </div>
                        <br/>
                        <!-- En duda -->
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="actualizarVerifica" onclick="return (validaCapturaVacios());">Actualizar</button>
                        <!-- En duda -->
                    </div>
                </form>
            </div>
        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->

<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script>
                            $(function() {
                                $("#Caducidad").datepicker();
                                $("#Caducidad").datepicker('option', {dateFormat: 'dd/mm/yy'});
                            });
                            $(function() {
                                $("#FecFab").datepicker();
                                $("#FecFab").datepicker('option', {dateFormat: 'dd/mm/yy'});
                            });
                            $(function() {
                                var availableTags = [
    <%
        try {
            con.conectar();
            ResultSet rset = con.consulta("select descrip from clave_med");
            while (rset.next()) {
                out.println("\'" + rset.getString("descrip") + "\',");
            }
            con.cierraConexion();
        } catch (Exception e) {
        }
    %>
                                ];
                                $("#descr").autocomplete({
                                    source: availableTags
                                });
                            });
                            $(function() {
                                var availableTags = [
    <%
        try {
            con.conectar();
            ResultSet rset = con.consulta("select f_nomprov from provee_all");
            while (rset.next()) {
                out.println("\'" + rset.getString("f_nomprov") + "\',");
            }
            con.cierraConexion();
        } catch (Exception e) {
        }
    %>
                                ];
                                $("#provee").autocomplete({
                                    source: availableTags
                                });
                            });
                            function ubi() {
                                var ubi = document.formulario1.ubica.value;
                                document.formulario1.ubicacion.value = ubi;
                            }
                            function proveedor() {
                                var proveedor = document.formulario1.list_provee.value;
                                document.formulario1.provee.value = proveedor;
                            }
                            function orig() {
                                var origen = document.formulario1.ori.value;
                                document.formulario1.origen.value = origen;
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

                            function foco() {
                                if (document.formulario1.folio_remi.value !== "") {
                                    document.formulario1.clave.focus();
                                }
                                if (document.formulario1.clave1.value !== "") {
                                    document.formulario1.cb.focus();
                                    document.location.href = "#ancla";
                                }
                            }


                            function validaCapturaVacios() {
                                var mensaje = "\n";
                                var RegExPattern = /^\d{1,2}\/\d{1,2}\/\d{4,4}$/;
                                var folio_remi = document.formulario1.folio_remi.value;
                                if (folio_remi === "")
                                    mensaje = mensaje + "Folio de Remisión vacío \n";
                                var orden = document.formulario1.orden.value;
                                if (orden === "")
                                    mensaje = mensaje + "Orden de compra vacía \n";
                                var provee = document.formulario1.provee.value;
                                if (provee === "")
                                    mensaje = mensaje + "Proveedor vacío \n";
                                var recib = document.formulario1.recib.value;
                                if (recib === "")
                                    mensaje = mensaje + "Recibe vacío \n";
                                var entrega = document.formulario1.entrega.value;
                                if (entrega === "")
                                    mensaje = mensaje + "Entrega vacío \n";
                                var clave1 = document.formulario1.clave1.value;
                                if (clave1 === "")
                                    mensaje = mensaje + "Clave de producto vacía \n";
                                var descripci = document.formulario1.descripci.value;
                                if (descripci === "")
                                    mensaje = mensaje + "Descripción vacía \n";
                                var cb = document.formulario1.cb.value;
                                if (cb === "")
                                    mensaje = mensaje + "Código de Barras vacío \n";
                                var Caducidad = document.formulario1.Caducidad.value;
                                if (Caducidad === "")
                                    mensaje = mensaje + "Caducidad vacía \n";
                                var FecFab = document.formulario1.FecFab.value;
                                if (FecFab === "")
                                    mensaje = mensaje + "Fecha de elaboración vacía \n";
                                var Marca = document.formulario1.Marca.value;
                                if (Marca === "")
                                    mensaje = mensaje + "Campo de Marca vacío \n";
                                var pres = document.formulario1.pres.value;
                                if (pres === "")
                                    mensaje = mensaje + "Campo Presentación vacío \n";
                                var Lote = document.formulario1.Lote.value;
                                if (Lote === "")
                                    mensaje = mensaje + "Campo Lote vacío \n";
                                var Obser = document.formulario1.observaciones.value;
                                if (Obser === "")
                                    mensaje = mensaje + "Campo de observaciones vacío \n";


                                var Cajas = document.formulario1.Cajas.value;
                                if (Cajas === "")
                                    Cajas = parseInt(0);
                                var pzsxcaja = document.formulario1.pzsxcaja.value;
                                if (pzsxcaja === "")
                                    pzsxcaja = parseInt(0);
                                var Resto = document.formulario1.Resto.value;
                                if (Resto === "")
                                    Resto = parseInt(0);
                                var total = (Cajas * pzsxcaja) + Resto;

                                if (folio_remi === "" || orden === "" || provee === "" || recib === "" || entrega === "" || clave1 === "" |Descripci === "" || cb === "" || Caducidad === "" || Obser === "" || Marca === "" || pres === "" || Lote === "") {
                                    alert("Tiene campos vacíos, verifique." + mensaje + "");
                                    return false;
                                }

                                if (parseInt(total) === 0) {
                                    alert("El total de piezas no puede ser \'0\' ");
                                    return false;
                                }

                                var dtFechaActual = new Date();
                                var dtFechaActualFB = new Date();
                                var sumarDias = parseInt(93);
                                dtFechaActual.setDate(dtFechaActual.getDate() + sumarDias);
                                var cadu = Caducidad.split('/');
                                var cad = cadu[2] + '-' + cadu[1] + "-" + cadu[0]

                                var fecfa = FecFab.split('/');
                                var fecf = fecfa[2] + '-' + fecfa[1] + "-" + fecfa[0]

                                if (Date.parse(dtFechaActual) > Date.parse(cad)) {
                                    alert("La fecha de caducidad no puede ser menor a tres meses próximos");
                                    return false;
                                }
                                if (Date.parse(dtFechaActualFB) < Date.parse(fecf)) {
                                    alert("La fecha de fabricacion no puede ser mayor a la fecha actual.");
                                    return false;
                                }

                                if ((Caducidad.match(RegExPattern)) && (Caducidad != '')) {
                                } else {
                                    alert("Caducidad Incorrecta, verifique.");
                                    return false;
                                }

                                return true;

                            }


                            function validaCompra() {
                                var RegExPattern = /^\d{1,2}\/\d{1,2}\/\d{2,4}$/;
                                var folio_remi = document.formulario1.folio_remi.value;
                                var orden = document.formulario1.orden.value;
                                var provee = document.formulario1.provee.value;
                                var recib = document.formulario1.recib.value;
                                var entrega = document.formulario1.entrega.value;
                                var Obser = document.formulario1.observaciones.value;
                                if (folio_remi === "" || orden === "" || provee === "" || recib === "" || entrega === "" || Obser === "") {
                                    alert("Tiene campos vacíos, verifique.");
                                    return false;
                                }
                                return true;
                            }
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
                                if (esto.value.length > otro) {
                                    if (esto.value.length == 2) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length > otro) {
                                    if (esto.value.length == 5) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length < otro) {
                                    if (esto.value.length == 2 || esto.value.length == 5) {
                                        esto.value = esto.value.substring(0, esto.value.length - 1);
                                    }
                                }
                                otro = esto.value.length
                            }


                            var formatNumber = {
                                separador: ",", // separador para los miles
                                sepDecimal: '.', // separador para los decimales
                                formatear: function(num) {
                                    num += '';
                                    var splitStr = num.split('.');
                                    var splitLeft = splitStr[0];
                                    var splitRight = splitStr.length > 1 ? this.sepDecimal + splitStr[1] : '';
                                    var regx = /(\d+)(\d{3})/;
                                    while (regx.test(splitLeft)) {
                                        splitLeft = splitLeft.replace(regx, '$1' + this.separador + '$2');
                                    }
                                    return this.simbol + splitLeft + splitRight;
                                },
                                new : function(num, simbol) {
                                    this.simbol = simbol || '';
                                    return this.formatear(num);
                                }
                            }

                            function totalPiezas() {
                                var TarimasC = document.getElementById('TarimasC').value;
                                var CajasxTC = document.getElementById('CajasxTC').value;
                                var PzsxCC = document.getElementById('PzsxCC').value;
                                var TarimasI = document.getElementById('TarimasI').value;
                                var CajasxTI = document.getElementById('CajasxTI').value;
                                var Resto = document.getElementById('Resto').value;
                                if (TarimasC === "") {
                                    TarimasC = 0;
                                }
                                if (CajasxTC === "") {
                                    CajasxTC = 0;
                                }
                                if (PzsxCC === "") {
                                    PzsxCC = 0;
                                }
                                if (TarimasI === "") {
                                    TarimasI = 0;
                                }
                                if (CajasxTI === "") {
                                    CajasxTI = 0;
                                }
                                if (Resto === "") {
                                    Resto = 0;
                                }
                                var totalTarimas = parseInt(TarimasC) + parseInt(TarimasI);
                                document.getElementById('Tarimas').value = formatNumber.new(totalTarimas);
                                var totalCajas = parseInt(CajasxTC) * parseInt(TarimasC) + parseInt(CajasxTI);
                                document.getElementById('Cajas').value = formatNumber.new(totalCajas);
                                var totalTarimas = parseInt(TarimasC) + parseInt(TarimasI);
                                document.getElementById('Tarimas').value = formatNumber.new(totalTarimas);


                                var totalPiezas = parseInt(PzsxCC) * parseInt(totalCajas);
                                document.getElementById('Piezas').value = formatNumber.new(totalPiezas + parseInt(Resto));
                            }
</script> 