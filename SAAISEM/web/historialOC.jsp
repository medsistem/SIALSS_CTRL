<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
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
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        //response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String Fecha = "";
    String fechaCap = "";
    String Proveedor = "",imagen="";
    try {
        fechaCap = df2.format(df3.parse(request.getParameter("Fecha")));
        Fecha = request.getParameter("Fecha");
    } catch (Exception e) {

    }
    if(fechaCap==null){
        fechaCap="";
    }
    try {
        Proveedor = request.getParameter("Proveedor");
    } catch (Exception e) {

    }
    if(Proveedor==null){
        Proveedor="";
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
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <!--div class="navbar navbar-default">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
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
                                    <li><a href="captura.jsp">Entrada Manual</a></li>
                                    <li><a href="compraAuto2.jsp">Entrada Automática OC</a></li>
                                    <li><a href="reimpresion.jsp">Reimpresión de Compras</a></li>
                                    <li><a href="ordenesCompra.jsp">Órdenes de Compras</a></li>
                                    <li><a href="kardexClave.jsp">Kardex Claves</a></li>
                                    <li><a href="Ubicaciones/Consultas.jsp">Ubicaciones</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Facturación<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="requerimiento.jsp">Carga de Requerimiento</a></li>
                                    <li><a href="factura.jsp">Facturación Automática</a></li>
                                     <li><a href="reimp_factura.jsp">Administrar Remisiones</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Catálogos<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="medicamento.jsp">Catálogo de Medicamento</a></li>
                                    <li><a href="catalogo.jsp">Catálogo de Proveedores</a></li>
                                    <li><a href="marcas.jsp">Catálogo de Marcas</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Fecha Recibo<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="Entrega.jsp">Fecha de Recibo en CEDIS</a></li>    
                                    <li><a href="historialOC.jsp">Historial OC</a></li>                                     
                                </ul>
                            </li>
                            
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href=""><span class="glyphicon glyphicon-user"></span> <%=usua%></a></li>
                            <li class="active"><a href="index.jsp"><span class="glyphicon glyphicon-log-out"></span></a></li>
                        </ul>
                    </div><!--/.nav-collapse>
                </div>
            </div-->
            <hr/>

            <div>
                <h3>Historial de Órdenes de Compra</h3>
                <div class="row">
                    <form action="historialOC.jsp" method="post">
                        <h4 class="col-sm-2">Proveedor</h4>
                        <div class="col-sm-5">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="this.form.submit();">
                                <option value="">--Proveedor--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("select F_ClaProve, F_NomPro from tb_proveedor order by F_NomPro");
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
                        <h4 class="col-sm-2">Fecha de Entrega</h4>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha"  onchange="this.form.submit();" />
                        </div>
                        <a class="btn btn-primary" href="historialOC.jsp">Todo</a>
                    </form>
                </div>
            </div>
        </div>
        <br />
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td class="text-center">No. Orden</td>
                                <td class="text-center">Proveedor</td>
                                <td class="text-center">Fecha de Captura</td>
                                <td class="text-center">Cant x Recibir</td>
                                <td class="text-center">Fecha a Recibir</td>
                                <td class="text-center">Cancelado</td>
                                <td class="text-center">Pendiente x Recibir</td>
                                <td class="text-center">Abierta</td>
                                <td class="text-center">Recibido</td>
                                <td class="text-center">Fecha Recepción</td>
                                <td class="text-center">Cant Recibida</td>
                                <td class="text-center">Ver OC</td>
                                <td class="text-center">Ver Rechazo</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("select o.F_NoCompra, p.F_NomPro, DATE_FORMAT(o.F_Fecha, '%d/%m/%Y') AS F_Fecha, SUM(o.F_Cant), DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') AS F_FecSur, o.F_StsPed from tb_pedidoisem o, tb_proveedor p where o.F_Provee = F_ClaProve and o.F_FecSur like '%" + fechaCap + "%' and p.F_ClaProve like '%"+Proveedor+"%' and F_StsPed != 0 group by  o.F_NoCompra");
                                    while (rset.next()) {
                                        String pendiente="", abierta="";
                                        String cancelado = "";
                                        if (rset.getString(6).equals("2")) {
                                            cancelado = "X";
                                        }
                                        String recibido = "", fecRecibo = "";
                                        int cantRecib = 0;
                                        ResultSet rset2 = con.consulta("select F_OrdCom, SUM(F_CanCom),F_FecApl from compras where F_OrdCom = '" + rset.getString(1) + "' group by F_OrdCom ");
                                        while (rset2.next()) {
                                            recibido = "X";
                                            cantRecib = rset2.getInt(2);
                                            fecRecibo = rset2.getString(3);
                                        }
                                        
                                        if(rset.getInt(4)>cantRecib && cantRecib!=0){
                                            recibido = "";
                                            abierta="X";
                                        } 
                                        if(cantRecib==0){
                                            pendiente="X";
                                        }
                                        if (cancelado.equals("X")){
                                            pendiente="";
                                        }
                                        /*ResultSet rset3 = con.consulta("SELECT F_Ima FROM TB_ImaRe where F_OrdCom = '" + rset.getString(1) + "'");
                                        while(rset3.next())
                                        {
                                        imagen = rset3.getString("F_Ima");
                                        }*/
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td class="text-right"><%=formatter.format(rset.getInt(4))%></td>
                                <td><%=rset.getString(5)%></td>
                                <td class="text-center"><%=cancelado%></td>
                                <td class="text-center"><%=pendiente%></td>
                                <td class="text-center"><%=abierta%></td>
                                <td class="text-center"><%=recibido%></td>
                                <td><%=fecRecibo%></td>
                                <td class="text-right"><%=formatter.format(cantRecib)%></td>
                                <td><button class="btn btn-primary" onclick="javascript:window.open('verOrdenCompra.jsp?NoCompra=<%=rset.getString(1)%>','','width=600,height=400,left=50,top=50,toolbar=no')"><span class="glyphicon glyphicon-search"></span></button></td>
                                <%if(imagen.equals("")){%>
                                <td>&nbsp;</td>
                                <%}else{%>
                                <td><a href="Rechazo/<%=imagen%>.jpg"  rel="lightbox" target="_black"><button class="btn btn-primary"><span class="glyphicon glyphicon-picture"></span></button></a></td>
                                <%}%>
                            </tr>
                            <%
                            imagen="";
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
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
                                $(document).ready(function() {
                                    $('#datosCompras').dataTable();
                                });
</script>
<script>
    $(function() {
        $("#Fecha").datepicker();
        $("#Fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
    });
</script>