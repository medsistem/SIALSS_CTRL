<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
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

    String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "", Cantidad = "";

    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
        Cantidad = (String) request.getAttribute("Cantidad");
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
    if (Cantidad == null) {
        Cantidad = "";
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
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
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

            <div class="row">
                <div class="col-sm-12">
                    <h2>Facturación Manual</h2>
                </div>
            </div>
            <hr/>
            <form action="FacturacionManual" method="post">
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Unidad:</h4>
                    </div>
                    <div class="col-sm-5">
                        <select class="form-control" name="ClaCli" id="ClaCli">
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

                                }
                            %>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <h4>Fecha de Entrega</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" name="FechaEnt" id="FechaEnt" min="<%=df2.format(new Date())%>" value="<%=FechaEnt%>"/>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">

                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-1">
                                <h4>Clave:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" readonly="" value="<%=ClaPro%>"/>
                            </div>
                            <div class="col-sm-2">
                                <h4>Descripción:</h4>
                            </div>
                            <div class="col-sm-7">
                                <textarea class="form-control" readonly=""><%=DesPro%></textarea>
                            </div>
                        </div>
                        <br/>

                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Cantidad a Facturar:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="Cantidad" id="Cantidad" value="<%=Cantidad%>"/>
                            </div>
                            <div class="col-sm-2 col-sm-offset-6">
                                <a class="btn btn-block btn-default" href="modula/cargaInsumo.jsp">Regresar</a>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
            <table class="table table-condensed table-striped table-bordered table-responsive">
                <tr>
                    <td>Clave</td>
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <td>Cantidad</td>
                    <td>Seleccionar</td>
                </tr>
                <%
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y'), F_Ubica, F_ExiLot, F_IdLote, F_FolLot from tb_lote where F_ClaPro = '" + ClaPro + "' and F_ExiLot!=0 order by F_FecCad asc  ");
                        while (rset.next()) {
                            int cant = 0, cantTemp = 0;
                            int cantLot = rset.getInt(5);
                            ResultSet rset2 = con.consulta("select F_Cant from tb_abasmodtemp where F_IdLote = '" + rset.getString("F_IdLote") + "' and F_Sts='0' ");
                            while (rset2.next()) {
                                cantTemp = rset2.getInt(1);
                            }

                            cant = cantLot - cantTemp;
                %>
                <tr>
                    <td><%=rset.getString(1)%></td>
                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getString(3)%></td>
                    <td><%=rset.getString(4)%></td>
                    <td><%=cant%></td>
                    <td>
                        <form action="AbasteceModula" method="post">
                            <input name="FolLot" value="<%=rset.getString(7)%>" class="hidden" readonly=""/>
                            <input name="IdLot" value="<%=rset.getString(6)%>" class="hidden" readonly=""/>
                            <input class="hidden" name="Cant" id="Cant<%=rset.getString(6)%>" value=""/>
                            <input class="hidden" name="CantAlm_<%=rset.getString(6)%>" id="CantAlm_<%=rset.getString(6)%>" value="<%=cant%>"/>
                            <button name="accion" value="AgregarClave" id="<%=rset.getString(6)%>" class="btn btn-block btn-primary" onclick="return validaCantidad(this.id);"><span class="glyphicon glyphicon-ok"></span></button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                %>
            </table>

        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>
    </body>
    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script>
                                function cambiaLoteCadu(elemento) {
                                    var indice = elemento.selectedIndex;
                                    document.getElementById('SelectCadu').selectedIndex = indice;
                                }

                                function validaCantidad(e) {
                                    var cantidadSol = document.getElementById('Cantidad').value;
                                    document.getElementById('Cant' + e).value = cantidadSol;
                                    var cantidadAlm = document.getElementById('CantAlm_' + e).value;
                                    if (parseInt(cantidadSol) > parseInt(cantidadAlm)) {
                                        alert('La cantidad a facturar no puede ser mayor a la cantidad de esa ubicación');
                                        return false;
                                    }

                                    /*var confirma = confirm('Seguro de usar esta ubicación?');
                                     if (confirma === false) {
                                     return false;
                                     }*/
                                }
    </script>
</html>

