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
        response.sendRedirect("../index.jsp");
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
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
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
                        </button>
                        <a class="navbar-brand" href="main_menu.jsp">Inicio</a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Entradas<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="../captura.jsp">Entrada Manual</a></li>
                                    <li><a href="../compraAuto2.jsp">Entrada Automática OC</a></li>
                                    <li class="divider"></li>
                                    <li><a href="hh/compraAuto3.jsp">HANDHELD | Entrada Automática OC</a></li>
                                    <li class="divider"></li>
                                    <%
                                        if (tipo.equals("2") || tipo.equals("3") || tipo.equals("5")) {
                                    %>
                                    <li><a href="../verificarCompraAuto.jsp">Verificar OC</a></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="#" onclick="window.open('../reimpresion.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Reimpresión de Compras</a></li>
                                    <li><a href="#"  onclick="window.open('../ordenesCompra.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Órdenes de Compras</a></li>
                                    <li><a href="#"  onclick="window.open('../kardexClave.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Kardex Claves</a></li>
                                    <li><a href="#"  onclick="window.open('../Ubicaciones/Consultas.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Ubicaciones</a></li>
                                    <li><a href="#"  onclick="window.open('../creaMarbetes.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Generar Marbetes</a></li>
                                    <li class="divider"></li>
                                        <%
                                            if (tipo.equals("5")) {
                                        %>
                                    <li><a href="hh/insumoNuevoRedist.jsp">Entrada Manual</a></li>
                                        <%
                                            }
                                        %>
                                    <!--li><a href="#"  onclick="window.open('verDevolucionesEntrada.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Imprimir Devoluciones</a></li>
                                    <li><a href="#"  onclick="window.open('devolucionesInsumo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Devolver</a></li-->
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Facturación<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="../requerimiento.jsp">Carga de Requerimiento</a></li>
                                    <li><a href="../factura.jsp">Facturación Automática</a></li>
                                    <li><a href="../validacionSurtido.jsp">Validación Surtido</a></li>
                                    <li><a href="../validacionAuditores.jsp">Validación Auditores</a></li>
                                        <%
                                            if (tipo.equals("7")) {
                                        %>
                                    <li><a href="../remisionarCamion.jsp">Generar Remisiones</a></li>
                                        <%
                                            }
                                        %>
                                    <li><a href="../facturacionManual.jsp">Facturación Manual</a></li>
                                    <li><a href="../reimp_factura.jsp">Administrar Remisiones</a></li>
                                    <li><a href="../reimpConcentrado.jsp">Reimpresión Concentrados Globales</a></li>
                                    <li><a href="../comparativoGlobal.jsp">Comparativo Global</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Inventario<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#"  onclick="window.open('../Ubicaciones/Inventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Inventario</a></li>
                                    <li><a href="#"  onclick="window.open('../movimientosUsuarioInventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Movimientos por Usuario</a></li>
                                    <li><a href="#"  onclick="window.open('semaforo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Semaforización</a></li>
                                    <li><a href="#"  onclick="window.open('invenCiclico/nuevoInventario.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Inventario Ciclico</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Catálogos<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#" onclick="window.open('../medicamento.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Medicamento</a></li>
                                    <li><a href="#" onclick="window.open('../catalogo.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Proveedores</a></li>
                                    <li><a href="#" onclick="window.open('../marcas.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Catálogo de Marcas</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Reportes<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#" onclick="window.open('../Entrega.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Fecha de Recibo en CEDIS</a></li> 
                                    <li><a href="#" onclick="window.open('../historialOC.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Historial OC</a></li>                                                  <li><a href="#" onclick="window.open('../ReporteF.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no')">Reporte por Fecha Proveedor</a></li>     
                                </ul>
                            </li>
                            <!--li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">ADASU<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="captura.jsp">Captura de Insumos</a></li>
                                    <li class="divider"></li>
                                    <li><a href="catalogo.jsp">Catálogo de Proveedores</a></li>
                                    <li><a href="reimpresion.jsp">Reimpresión de Docs</a></li>
                                </ul>
                            </li-->
                            <%
                                if (usua.equals("root")) {
                            %>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Usuario<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="../usuarios/usuario_nuevo.jsp">Nuevo Usuario</a></li>
                                    <li><a href="../usuarios/edita_usuario.jsp">Edicion de Usuarios</a></li>
                                </ul>
                            </li>
                            <%                                }
                            %>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="#"><span class="glyphicon glyphicon-user"></span> <%=usua%></a></li>
                            <li class="active"><a href="../index.jsp"><span class="glyphicon glyphicon-log-out"></span></a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <h3>Abastecer Modula</h3>
                </div>
            </div>
            <div class="row">
                <form action="../AbasteceModula" method="post">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-sm-2">
                                    <h4>Ingrese la Clave:</h4>
                                </div>
                                <div class="col-sm-2">
                                    <input class="form-control" name="ClaPro" id="ClaPro"/>
                                </div>
                                <div class="col-sm-2">
                                    <button class="btn btn-primary btn-block" name="accion" value="btnClave" id="btnClave" onclick="return validaBuscar();">Buscar</button>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-1">
                                    <h4>Clave:</h4>
                                </div>
                                <div class="col-sm-2">
                                    <input class="form-control" readonly="" value="<%=ClaPro%>" id="ClaveSel"/>
                                </div>
                                <div class="col-sm-2">
                                    <h4>Descripción:</h4>
                                </div>
                                <div class="col-sm-7">
                                    <textarea class="form-control" readonly="" id="DesSel"><%=DesPro%></textarea>
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
                                    <input class="form-control" name="Cantidad" id="Cantidad" onKeyPress="return justNumbers(event);"/>
                                </div>
                                <div class="col-sm-2">
                                    <button class="btn btn-block btn-primary" name="accion" value="SeleccionaLote" onclick="return validaSeleccionar();">Seleccionar</button>
                                </div>
                            </div>

                        </div>
                    </div>
                    <table class="table table-condensed table-striped table-bordered table-responsive">
                        <tr>
                            <td>Clave</td>
                            <td>Lote</td>
                            <td>Caducidad</td>
                            <td>Cantidad</td>
                            <td>Remover</td>
                        </tr>
                        <%
                            int banBtn = 0;
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, u.F_DesUbi, a.F_Cant, a.F_Id from tb_abasmodtemp a, tb_lote l, tb_medica m, tb_ubica u WHERE a.F_IdLote=l.F_IdLote and l.F_ClaPro = m.F_ClaPro and l.F_Ubica = u.F_ClaUbi and a.F_Usuario='" + sesion.getAttribute("nombre") + "' and a.F_Sts=0;");
                                while (rset.next()) {
                                    banBtn = 1;
                        %>
                        <tr>
                            <td><%=rset.getString(1)%></td>
                            <td><%=rset.getString(2)%></td>
                            <td><%=rset.getString(3)%></td>
                            <td><%=rset.getString(4)%></td>
                            <td>
                                <button class="btn btn-block btn-info" name="accionEliminar" value="<%=rset.getString("F_Id")%>" onclick="return confirm('Seguro que desea eliminar esta clave?')"><span class="glyphicon glyphicon-remove"></span></button>
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
                    <%
                        if (banBtn == 1) {
                    %>
                    <div class="row">
                        <div class="col-sm-6">
                            <button class="btn btn-block btn-primary" name="accion" value="CargarAbasto" onclick="return confirm('Seguro de cargar el Abasto?')">Cargar Abasto</button>
                        </div>
                        <div class="col-sm-6">
                            <button class="btn btn-block btn-info" name="accion" value="CancelarAbasto" onclick="return confirm('Seguro de CANCELAR el Abasto?')">Cancelar Abasto</button>
                        </div>
                    </div>

                    <%
                        }
                    %>

                </form>
            </div>
        </div>
        <br><br><br>
        <div class="navbar navbar-inverse">
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
    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/jquery-ui-1.10.3.custom.js"></script>

</html>

