<%-- 
    Document   : capturaISEM.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<!--%@page import="conn.ConectionDB_Linux"%-->
<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formNoCom = new DecimalFormat("000");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "", IdUsu = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        IdUsu = (String) sesion.getAttribute("IdUsu");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();
    CapturaPedidos indice = new CapturaPedidos();
    String proveedor = "", fecEnt = "", horEnt = "", claPro = "", desPro = "", NoCompra = "", origen = "", cedis = "", Proyecto = "", Desproyecto = "", Campo = "", CodBar = "";
    try {
        Proyecto = request.getParameter("Proyecto");
        Desproyecto = request.getParameter("DesProyecto");
        Campo = request.getParameter("Campo");
        NoCompra = (String) sesion.getAttribute("NoCompra");
        proveedor = (String) sesion.getAttribute("proveedor");
        fecEnt = (String) sesion.getAttribute("fec_entrega");
        horEnt = (String) sesion.getAttribute("hor_entrega");
        claPro = (String) sesion.getAttribute("clave");
        desPro = (String) sesion.getAttribute("descripcion");
        origen = (String) sesion.getAttribute("origen");
        cedis = (String) sesion.getAttribute("cedis");
        CodBar = (String) sesion.getAttribute("CodBar");
    } catch (Exception e) {

    }
    if (proveedor == null) {
        proveedor = "";
        fecEnt = "";
        horEnt = "";
    }
    if (claPro == null) {
        claPro = "";
        desPro = "";
        origen = "";
    }

    if (NoCompra == null) {
        NoCompra = "";
    }

    if (Desproyecto == null) {
        Desproyecto = "";
    }

    if (CodBar == null) {
        CodBar = "";
    }

    if (Campo == null) {
        Campo = "";
    }

    if (cedis == null) {
        cedis = request.getParameter("cedis");
    }

  
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Captura</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="shortcut icon"
              href="imagenes/system-settings-icon_31831.png" />
        <link href="css/select2.css" rel="stylesheet">

    </head>
    <body onload="focusLocus();
            SelectProve(FormBusca);">
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <h4>Captura Extraordinario</h4>
           
           
           

            <form name="FormBusca" action="CapturaPedidos" method="post">
                 
                <div class="panel panel-default">
                    <br/>
                <div class="row">
                    <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                    <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                    <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                    <label class="col-sm-3 ">
                        <h4>No de Orden de Compra:</h4>
                    </label>
                    <input class="hidden" value="<%=cedis%>" name="nomCedis" >
                    <div class="col-sm-6"">
                        <input type="text" class="form-control" id="NoCompra" name="NoCompra" value="<%=NoCompra%>" />
                    </div>
                </div>
                <br/>
                <div class="row">
                     
                    <label class="col-sm-1">
                        <h4>Proveedor:</h4>
                    </label>
                    <div class="col-sm-8">
                        <select class="form-control" name="Proveedor" id="Proveedor" onchange="SelectProve(this.form);
                                document.getElementById('Fecha').focus()">
                            <option value="">--Proveedor--</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT F_ClaProve,F_NomPro FROM tb_proveedor;");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"
                                    <%
                                        if (proveedor.equals(rset.getString(1))) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString(2)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>

                        </select>
                    </div>
                </div>
                <div class="row">

                    <label class="col-sm-1 text-right">
                        <h4>Clave:</h4>
                    </label>
                    <div class="col-sm-8">
                        <select name="Clave" id="Clave" class="form-control">
                            <option>-- Seleccione --</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT F_ClaPro,CONCAT(F_ClaPro,' - ',F_DesPro) FROM tb_medica WHERE " + Campo + "= 1 AND F_StsPro='A';");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"
                                    <%
                                        if (proveedor.equals(rset.getString(1))) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString(2)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-primary btn-block" onclick="return validaClaDes(this);" name="accion" value="ClaveExtra">Buscar Clave</button>
                    </div>
                  </div>
                </div>
            </form>
            <br/>
            <form name="FormCaptura" action="CapturaPedidos" method="post">
                <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                <input type="hidden" class="form-control" id="NoCompra2" name="NoCompra2" value="<%=NoCompra%>" />
                <input type="hidden" class="form-control" id="proveedor2" name="proveedor2" value="<%=proveedor%>" />
                <div class="panel panel-default">
                    <div class="panel-body">

                        <div class="row">
                            <div class="col-sm-6">
                                <strong>Clave:</strong>
                                <input type="text" class="form-control" readonly value="<%=claPro%>" name="ClaPro" id="ClaPro"/>
                            </div>
                            <div class="col-sm-6">
                                <strong>Descripción:</strong>
                                <input type="text" class="form-control" readonly value="<%=desPro%>" name="DesPro" id="DesPro"/>
                            </div>
                        </div>

                        <br/>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="col-sm-6">
                                    <strong>CB</strong>
                                    <input type="text" class="form-control" name="CBPro" id="CBPro" value="<%=CodBar%>"/>
                                </div>
                                <div class="col-sm-6">
                                    <strong>Generar</strong>
                                    <button class="btn btn-primary btn-block" type="submit" name="accion" id="GeneraCodigo" value="GeneraCodigo" onclick=""><span class="glyphicon glyphicon-barcode"></span></button>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="col-sm-9">
                                    <strong>Marca</strong>
                                    <select class="form-control" name="list_marca" onKeyPress="return tabular(event, this)" id="list_marca">
                                        <option value="">Marca</option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset3 = con.consulta("SELECT F_ClaMar, F_DesMar FROM tb_marca WHERE F_DesMar !='' GROUP BY F_DesMar ORDER BY F_DesMar;");
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString(1)%>"><%=rset3.getString(2)%></option>
                                        <%

                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <button class="btn btn-block btn-primary glyphicon glyphicon-refresh" type = "submit" value = "refresh" name = "accion" ></button>
                                </div>
                                <div class="col-sm-1">
                                    <a href="marcas.jsp" target="_blank"><h4>Alta</h4></a>
                                </div>
                            </div>
                        </div>
                        <br />
                        <div class="row">
                            <div class="col-sm-6">
                                <strong>Lote</strong>
                                <input type="text" class="form-control" name="LotPro" id="LotPro" />
                            </div>
                            <div class="col-sm-6">
                                <strong>Caducidad</strong>
                                <input type="text" class="form-control" data-date-format="dd/mm/yyyy" placeholder="dd/mm/yyyy" name="CadPro" id="CadPro" onclick="" onKeyPress="
                                        return LP_data(event, this);
                                        anade(this, event);
                                        return tabular(event, this);
                                       " maxlength="10" onblur="validaCadu();"/>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-sm-6">
                                <strong>Cantidad</strong>
                                <input type="text" class="form-control" name="CanPro" id="CanPro" data-behavior="only-num" />
                            </div>
                            <div class="col-sm-6">
                                <strong>Fecha de Vencimiento</strong>
                                <input type="text" class="form-control" data-date-format="dd/mm/yyyy" placeholder="dd/mm/yyyy" name="fecVe" id="fecVe" onclick="" onKeyPress="
                                        return LP_data(event, this);
                                        anade(this, event);
                                        return tabular(event, this);
                                       " maxlength="10" onblur="validaFecVe();"/>
                            </div>
                        </div>
                        <br/>
                         <div class="row">
                        <div class="col-sm-6" >
                            <strong>Factor de Empaque</strong>
                            
                                 <input type="text" class="form-control" id="factorEmpaque" name="factorEmpaque" value="1" onKeyPress="return justNumbers(event);
                                  return handleEnter(event);"  onclick="" onfocusout="confirmaCampo('factorEmpaque')">
                                 
                        </div>
                     
                       
                            <div class="col-sm-6">
                                <strong>Plazo</strong>
                                <input type="number" class="form-control" name="dias" id="dias" data-behavior="only-num" />
                            </div>
                        </div>
                        <br/>
                        <div class="row">                            
                            <label class="col-sm-2 text-right">
                                <h4>Costo unitario:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="costoUnitario" id="costoUnitario" data-behavior="only-num" />
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>IVA:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" onchange="calculaTotal()" name="iva" id="iva" data-behavior="only-num" placeholder="%"/>
                            </div>
                        </div>
                        <div class="row">    
                            <label class="col-sm-2 text-right">
                                <h4>Subtotal:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="subtotalDis" data-behavior="only-num" disabled/>
                                <input type="text" class="hidden" name="subtotal" id="subtotal" data-behavior="only-num"/>
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Total:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="total" id="totalDis" data-behavior="only-num" disabled="">
                                <input type="text" class="hidden" name="total" id="total" data-behavior="only-num"/>
                            </div>
                        </div>
                        <button class="btn btn-block btn-primary" name="accion" value="capturarExtra" onclick="return validaCaptura();">Capturar</button>

                    </div>

                </div>
            </form>
            <br/>
            <table class="table table-bordered table-condensed table-striped">
                <tr>
                    <th class="text-center"><strong>Clave</strong></th>
                    <th class="text-center"><strong>Lote</strong></th>
                    <th class="text-center"><strong>Caducidad</strong></th>
                    <th class="text-center"><strong>Cantidad</strong></th>
                    <th class="text-center"><strong>Cancelar</strong></th>
                </tr>
                
                <%
                    int banConfirma = 0;
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("SELECT F_ClaPro, F_Lote, DATE_FORMAT(F_FecCad, '%d/%m/%Y') AS F_Cadu, F_Cant, F_Id FROM tb_capturaextra WHERE F_StsPed = 1 AND F_IdUsu = '" + (String) sesion.getAttribute("IdUsu") + "';");
                        while (rset.next()) {
                            banConfirma = 1;
                %>
                <tr>
                    <td class="text-center"><%=rset.getString(1)%></td>
                    <td class="text-center"><%=rset.getString(2)%></td>
                    <td class="text-center"><%=rset.getString(3)%></td>
                    <td class="text-center"><%=formatter.format(rset.getInt(4))%></td>
                    <td class="text-center">
                        <form action="CapturaPedidos" method="post">
                            <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                            <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                            <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                            <input name="id" value="<%=rset.getString(5)%>" class="hidden" />
                            <button class="btn btn-info" name="accion" value="eliminaClaveXtra"  style="align-content: center"><span class="glyphicon glyphicon-remove"></span></button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, e);
                    } finally {
                        try {
                            con.cierraConexion();
                        } catch (SQLException ex) {
                            Logger.getLogger("capturaExtraordinario.jsp").log(Level.SEVERE, null, ex);
                        }
                    }
                %>

            </table>
            <div class="row">
                <%
                    if (banConfirma == 1) {
                %>
                <form name="FormCaptura" action="CapturaPedidos" method="post">
                    <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                    <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                    <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                    <input type="hidden" class="form-control" id="IdUsu" name="IdUsu" value="<%=IdUsu%>" />
                    <div class="col-sm-6">
                        <input class="hidden" name="NoCompra" value="<%=NoCompra%>"/>
                        <button class="btn btn-primary btn-block" type="button" id="validarRemision" onclick="return confirm('¿Seguro que desea CONFIRMAR el pedido?')">Confirmar Pedido</button>
                    </div>
                    <div class="col-sm-6">
                        <button class="btn btn-info btn-block" name="accion" value="cancelarExtra" onclick="return confirm('¿Seguro que desea CANCELAR el pedido?')">Eliminar Todo</button>
                    </div>
                </form>
                <%
                    }
                %>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/select2.js" type="text/javascript"></script>
        <script src="js/recepcion/recepcionExtra.js"></script>
        <script type="text/javascript">

                            $("#Proveedor").select2();
                            $("#Clave").select2();
                            $("#list_marca").select2();
                            var cedis = "<%=cedis%>";

                            $("#cedis option[value=" + cedis + "] ").prop('selected', 'selected');
                            $("#cedis").val(cedis);
                            $("[data-behavior~=only-alphanum-white]").alphanum({
                                allow: '.',
                                disallow: "'",
                                allowSpace: true
                            });
                            $("[data-behavior~=only-num]").numeric({
                                allowMinus: false,
                                allowThouSep: false
                            });

                            otro = 0;
                            function LP_data(e, esto) {
                                var key = (document.all) ? e.keyCode : e.which; //codigo de tecla. 
                                if (key < 48 || key > 57)//si no es numero 
                                    return false; //anula la entrada de texto.
                                else
                                    anade(esto);
                            }

                            function anade(esto) {

                                if (esto.value.length > otro) {
                                    if (esto.value.length === 2) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length > otro) {
                                    if (esto.value.length === 5) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length < otro) {
                                    if (esto.value.length === 2 || esto.value.length === 5) {
                                        esto.value = esto.value.substring(0, esto.value.length - 1);
                                    }
                                }
                                otro = esto.value.length;
                            }


                            function focusLocus() {
                                document.getElementById('Proveedor').focus();
                                if (document.getElementById('Fecha').value !== "") {
                                    document.getElementById('Clave').focus();
                                }
                                if (document.getElementById('ClaPro').value !== "") {
                                    document.getElementById('Prioridad').focus();
                                }
                            }

                            function validaClaDes(boton) {
                                var btn = boton.value;
                                var prove = document.getElementById('Proveedor').value;
                                var cedis = document.getElementById('cedis').value;
                                var NoCompra = document.getElementById('NoCompra').value;
                                if (prove === "" || NoCompra === "" || cedis === "") {
                                    alert("Complete los datos");
                                    return false;
                                }
                                var valor = "";
                                var mensaje = "";
                                if (btn === "Clave") {
                                    valor = document.getElementById('Clave').value;
                                    mensaje = "Introduzca la clave";
                                }
                                if (btn === "Descripcion") {
                                    valor = document.getElementById('Descripcion').value;
                                    mensaje = "Introduzca la descripcion";
                                }
                                if (valor === "") {
                                    alert(mensaje);
                                    return false;
                                }
                                return true;
                            }

                            function validaCaptura() {
                                var ClaPro = document.getElementById('ClaPro').value;
                                var DesPro = document.getElementById('DesPro').value;
                                var CanPro = document.getElementById('CanPro').value;
                                var CBPro = document.getElementById('CBPro').value;
                                var marca = document.getElementById('list_marca').value;
                                var CadPro = document.getElementById('CadPro').value;
                                var fecVe = document.getElementById('fecVe').value;
                                var dias = document.getElementById('dias').value;
                                var proveedor = document.getElementById('Proveedor').value;
                                if (ClaPro === "" |DesPro === "" || CanPro === "" || CBPro === "" || marca === "" || CadPro === "" || fecVe === "" || dias === "") {
                                    alert("Complete los datos");
                                    return false;
                                }
                                var CanRes = document.getElementById('CantRest').value;
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                CanRes = CanRes.replace(",", "");
                                if (parseInt(CanRes) < parseInt(CanPro) && proveedor !== '63' && proveedor !== '19') {
                                    alert("La Cantidad Solicitada no puede ser mayor a la Cantidad Restante");
                                    return false;
                                }
                                return true;
                            }

                            function validaCadu() {
                                var cad = document.getElementById('CadPro').value;
                                if (cad === "") {
                                    alert("Falta Caducidad");
                                    document.getElementById('CadPro').focus();
                                    return false;
                                } else {
                                    if (cad.length < 10) {
                                        alert("Caducidad Incorrecta");
                                        document.getElementById('CadPro').focus();
                                        return false;
                                    } else {
                                        var dtFechaActual = new Date();
                                        var sumarDias = parseInt(0);
                                        dtFechaActual.setDate(dtFechaActual.getDate() + sumarDias);
                                        var fechaSpl = cad.split("/");
                                        var Caducidad = fechaSpl[2] + "-" + fechaSpl[1] + "-" + fechaSpl[0];

                                        if (Date.parse(dtFechaActual) >= Date.parse(Caducidad)) {
                                            alert("Verifique la caducidad");
                                            document.getElementById('CadPro').focus();
                                            return false;
                                        }
                                    }
                                }
                            }

                            function validaFecVe() {
                                var cad = document.getElementById('fecVe').value;
                                if (cad === "") {
                                    alert("Falta Vencimiento");
                                    document.getElementById('fecVe').focus();
                                    return false;
                                } else {
                                    if (cad.length < 10) {
                                        alert("Vencimiento Incorrecta");
                                        document.getElementById('fecVe').focus();
                                        return false;
                                    } else {
                                        var dtFechaActual = new Date();
                                        var sumarDias = parseInt(0);
                                        dtFechaActual.setDate(dtFechaActual.getDate() + sumarDias);
                                        var fechaSpl = cad.split("/");
                                        var Caducidad = fechaSpl[2] + "-" + fechaSpl[1] + "-" + fechaSpl[0];

                                        if (Date.parse(dtFechaActual) >= Date.parse(Caducidad)) {
                                            alert("Verifique el vencimiento");
                                            document.getElementById('fecVe').focus();
                                            return false;
                                        }
                                    }
                                }
                            }

                            function tabular(e, obj) {
                                tecla = (document.all) ? e.keyCode : e.which;
                                if (tecla !== 13)
                                    return;
                                frm = obj.form;
                                for (i = 0; i < frm.elements.length; i++)
                                    if (frm.elements[i] === obj)
                                    {
                                        if (i === frm.elements.length - 1)
                                            i = -1;
                                        break
                                    }
                                if (frm.elements[i + 1].disabled === true)
                                    tabular(e, frm.elements[i + 1]);
                                else
                                    frm.elements[i + 1].focus();
                                return false;
                            }
                            function calculaTotal() {
                                var CanPro = document.getElementById('CanPro').value;
                                var costoUnitario = document.getElementById('costoUnitario').value;
                                var iva = document.getElementById('iva').value;
                                var total = 0;
                                var subtotal = 0;
                                CanPro = parseFloat(CanPro);
                                if (costoUnitario !== "") {
                                    costoUnitario = parseFloat(costoUnitario);
                                    subtotal = costoUnitario * CanPro;
                                    total = subtotal;
                                    if (iva !== "") {
                                        total = subtotal * (1 + (parseFloat(iva) / 100.00));
                                    }
                                    document.getElementById('subtotal').value = subtotal;
                                    document.getElementById('subtotalDis').value = subtotal;
                                    document.getElementById('total').value = total.toFixed(2);
                                    document.getElementById('totalDis').value = total.toFixed(2);
                                }
                            }
                            
                            function justNumbers(e)
                                {
                                    var keynum = window.event ? window.event.keyCode : e.which;
                                    if ((keynum === 8) || (keynum === 46))
                                        return true;
                                    return /\d/.test(String.fromCharCode(keynum));
                                }
        </script>
    </body>

</html>
