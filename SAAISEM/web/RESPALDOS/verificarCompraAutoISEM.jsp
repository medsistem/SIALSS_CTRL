<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%java.text.DateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
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
    String vOrden = "", vRemi = "";

    try {
        vOrden = (String) sesion.getAttribute("vOrden");
        vRemi = (String) sesion.getAttribute("vRemi");
    } catch (Exception e) {
    }

    try {
        String Folio = "";
        String folio[] = null;
        Folio = request.getParameter("NoCompra");
        if (!Folio.equals("")) {
            folio = Folio.split(",");
            sesion.setAttribute("vOrden", folio[0]);
            sesion.setAttribute("vRemi", folio[1]);
            vOrden = folio[0];
            vRemi = folio[1];
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
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <form action="verificarCompraAutoISEM.jsp" method="post">
                <br/>
                <div class="row">
                    <h3>Validación Recibo</h3>
                    <h4 class="col-sm-2">Elegir Remisión: </h4>
                    <div class="col-sm-9">
                        <select class="form-control" name="NoCompra" onchange="this.form.submit();">
                            <option value="">-- Proveedor -- Orden de Compra --</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT c.F_OrdCom, p.F_NomPro, c.F_FolRemi FROM tb_compratempisem c, tb_proveedor p WHERE c.F_Provee = p.F_ClaProve and c.F_Estado = '2' GROUP BY c.F_OrdCom, c.F_FolRemi");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>,<%=rset.getString(3)%>"><%=rset.getString(2)%> - <%=rset.getString(1)%> - <%=rset.getString(3)%></option>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {

                                }
                            %>
                        </select>
                    </div>
                </div>
                <br/>
            </form>
        </div>
        <div style="width: 90%; margin: auto;">
            <br/>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <form action="CompraAutomatica" method="get" name="formulario1">
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select i.F_NoCompra, DATE_FORMAT(i.F_FecSur, '%d/%m/%Y') as F_FecSur, i.F_HorSur, p.F_NomPro, p.F_ClaProve from tb_pedidoisem i, tb_proveedor p where i.F_Provee = p.F_ClaProve and F_StsPed = '1' and F_NoCompra = '" + vOrden + "'  and F_recibido='0' group by F_NoCompra");
                                while (rset.next()) {
                        %>
                        <div class="row">
                            <h4 class="col-sm-2">Folio Orden de Compra:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=vOrden%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                            <h4 class="col-sm-1">Remisión:</h4>
                            <div class="col-sm-2"><input class="form-control" value="<%=vRemi%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-12">Proveedor: <%=rset.getString("p.F_NomPro")%></h4>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-9">Fecha y Hora de Entrega: <%=rset.getString("F_FecSur")%> <%=rset.getString("i.F_HorSur")%></h4>
                            <div class="col-sm-2">
                                <a class="btn btn-default" href="compraAuto2.jsp">Agregar Clave al Inventario</a>
                            </div>
                        </div>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                //e.getMessage();
                            }
                        %>
                    </form>
                </div>

                <form action="nuevoAutomaticaLotes" method="post">
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td>Remisión</td>
                                    <td>CLAVE</td>
                                    <td>Descripción</td>
                                    <td>Ori</td>
                                    <td>Lote</td>                      
                                    <td>Cantidad</td>                      
                                    <td>Costo U</td>                     
                                    <td>IVA</td>                       
                                    <td>Importe</td>
                                    <td>Código de Barras</td>
                                    <td>Caducidad</td>  
                                    <td>Marca</td>
                                </tr>
                                <%
                                    int banBtn = 0;
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar FROM tb_compratempisem C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + vOrden + "' and F_FolRemi = '" + vRemi + "'  and F_Estado = '2'");
                                        while (rset.next()) {
                                            banBtn = 1;
                                            String F_FecCad = "", F_Cb = "", F_Marca = "";
                                            try {
                                                F_FecCad = rset.getString(5);
                                            } catch (Exception e) {
                                                //tln(e.getMessage());
                                            }

                                            F_Cb = rset.getString("F_Cb");
                                            if (F_Cb.equals("")) {

                                                ResultSet rset2 = con.consulta("select F_Cb, F_ClaMar from tb_lote where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                while (rset2.next()) {
                                                    F_Cb = rset2.getString("F_Cb");
                                                    F_Marca = rset2.getString("F_ClaMar");
                                                }
                                            }

                                            if (F_Cb.equals("")) {
                                                ResultSet rset2 = con.consulta("select F_Cb, F_ClaMar from tb_cb where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_Lote") + "' group by F_ClaPro");
                                                while (rset2.next()) {
                                                    F_Cb = rset2.getString("F_Cb");
                                                    F_Marca = rset2.getString("F_ClaMar");
                                                }
                                            }
                                            F_Marca = rset.getString("F_DesMar");
                                            if (F_Marca.equals("")) {
                                                ResultSet rset2 = con.consulta("select F_DesMar from tb_marca where F_ClaMar = '" + F_Marca + "'");
                                                while (rset2.next()) {
                                                    F_Marca = rset2.getString("F_DesMar");
                                                }
                                            }

                                            if(F_Cb.equals(" ")){
                                                F_Cb="";
                                            }
                                %>
                                <tr>
                                    <td><%=rset.getString("C.F_FolRemi")%></td>
                                    <td><%=rset.getString(2)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <td><%=rset.getString("F_Origen")%></td>
                                    <td><input class="form-control" value="<%=rset.getString(4)%>" name="F_Lote<%=rset.getString("F_IdCom")%>" required  /></td>
                                    <td><input class="form-control" value="<%=rset.getString(6)%>" type="number" name="F_Cant<%=rset.getString("F_IdCom")%>" required  /></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>
                                    <td><input class="form-control" value="<%=F_Cb%>" name="F_Cb<%=rset.getString("F_IdCom")%>" required  /></td>
                                    <td>
                                        <%
                                            if (F_FecCad.equals("")) {
                                        %>
                                        <input type="date" class="form-control" name="F_FecCad<%=rset.getString("F_IdCom")%>" required />
                                        <%
                                        } else {
                                        %>
                                        <input type="date" class="form-control" name="F_FecCad<%=rset.getString("F_IdCom")%>"  value="<%=F_FecCad%>" required />
                                        <%
                                            }
                                        %>
                                    </td>
                                    <td>
                                        <input value="<%=F_Marca%>" class="form-control" name="F_Marca<%=rset.getString("F_IdCom")%>" id="marca<%=rset.getString("F_IdCom")%>" onkeyup="descripMarc()" required />
                                        <!--form method="get" action="Modificaciones">
                                            <input name="id" type="text" style="" class="hidden" value="<%=rset.getString(7)%>" />
                                            <button class="btn btn-info" name="accion" value="modificarVerifica"><span class="glyphicon glyphicon-pencil" ></span></button>
                                            <button class="btn btn-info" onclick="return confirm('¿Seguro de que desea eliminar?');" name="accion" value="eliminarVerifica"><span class="glyphicon glyphicon-remove"></span></button>
                                        </form-->
                                    </td>
                                </tr>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }

                                %>
                                <tr>
                                    <td colspan="13">

                                    </td>
                                </tr>

                            </table>
                        </div>
                        <hr/>
                    </div>
                    <%                                if (banBtn == 1) {
                    %>

                    <div class="panel-body table-responsive">
                        <div class="row">
                            <input name="vOrden" type="text" style="" class="hidden" value='<%=vOrden%>' />
                            <input name="vRemi" type="text" style="" class="hidden" value='<%=vRemi%>' />
                            <div class="col-lg-3 col-lg-offset-3">
                                <button  value="EliminarVerificaISEM" name="accion" class="btn btn-info btn-block" onclick="return confirm('Seguro que desea eliminar la compra?');">Cancelar Remisión</button>
                            </div>
                            <div class="col-lg-3">
                                <button  value="GuardarAbiertaVerificaISEM" name="accion" class="btn btn-info  btn-block" onclick="return confirm('Seguro que desea realizar la compra?');
                                        return validaCompra();">Remisión Abierta</button>
                            </div>
                            <div class="col-lg-3">
                                <button  value="GuardarVerificaISEM" name="accion" class="btn btn-primary  btn-block" onclick="return confirm('Seguro que desea realizar la compra?');
                                        return validaCompra();">Confirmar Remisión</button>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>

                </form>
                <!--div class="panel-footer">
                    <button class="btn btn-block btn-primary btn-lg" name="accion" id="accion" value="confirmar" onclick="return validaCompra();">Confirmar Compra</button>
                </div-->
            </div>
        </div>


        <%@include file="jspf/piePagina.jspf" %>

        <!--
        Modal
        -->
        <div class="modal fade" id="Rechazar" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="Rechazos" method="get">
                        <div class="modal-header">
                            <div class="row">
                                <div class="col-sm-5">
                                    <h4 class="modal-title" id="myModalLabel">Rechazar Orden de Compra</h4>
                                </div>
                                <div class="col-sm-2">
                                    <input name="NoCompraRechazo" id="NoCompraRechazo" value="" class="form-control" readonly="" />
                                </div>
                            </div>
                            <div class="row">

                                <div class="col-sm-12">
                                    Proveedor:
                                </div>
                                <div class="col-sm-12">
                                    Fecha y Hora 
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Observaciones de Rechazo</h4>
                                </div>
                                <div class="col-sm-12">
                                    <textarea class="form-control" placeholder="Observaciones" name="rechazoObser" id="rechazoObser" rows="5"></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Fecha de nueva recepción</h4>
                                </div>
                                <div class="col-sm-6">
                                    <input type="date" class="form-control" id="FechaOrden" name="FechaOrden" />
                                </div>
                                <div class="col-sm-6">
                                    <select class="form-control" id="HoraOrden" name="HoraOrden">
                                        <option value=":00">:00</option>
                                        <option value=":30">:30</option>
                                        <option value=":00">:00</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <h4>Correo del proveedor</h4>
                                    <input type="email" class="form-control" id="correoProvee" name="correoProvee" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <h4>Claves a Cancelar</h4>
                                    <h6>*Deseleccione las claves que no va a cancelar</h6>
                                </div>
                                <div class="col-sm-6">
                                    <div class="checkbox">
                                        <h4><input type="checkbox" checked name="todosChk" id="todosChk" onclick="checkea(this)">Seleccionar todas</h4>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <table class="table table-bordered">

                                        <tr>

                                            <td>
                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" checked="" name="chkCancela" value="">
                                                    </label>
                                                </div>
                                            </td>

                                        </tr>

                                    </table>
                                </div>
                            </div>
                            <div class="text-center" id="imagenCarga" style="display: none;" > 
                                <img src="imagenes/ajax-loader-1.gif">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" onclick="return validaRechazo();
                                    " name="accion" value="Rechazar">Rechazar OC</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!--
        /Modal
        -->

        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        <script type="text/javascript">

                                function descripMarc() {
            <%
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT F_IdCom FROM tb_compratempisem C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + vOrden + "' and F_FolRemi = '" + vRemi + "'  and F_Estado = '2'");
                    while (rset.next()) {

            %>

                                    // alert('hola')
                                    var availableTags<%=rset.getString("F_IdCom")%> = [
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset1 = con.consulta("select F_DesMar from tb_marca");
                        while (rset1.next()) {
                            out.println("'" + rset1.getString(1) + "',");
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
                                    ];
                                    $("#marca<%=rset.getString("F_IdCom")%>").autocomplete({
                                        source: availableTags<%=rset.getString("F_IdCom")%>
                                    });
            <%
                    }
                } catch (Exception e) {

                }
            %>

                                }
        </script>
    </body>



</html>
