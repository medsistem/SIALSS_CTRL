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
    String proveedor = "", fecEnt = "", horEnt = "", claPro = "", desPro = "", NoCompra = "", origen = "", cedis = "", Proyecto = "", Desproyecto = "", Campo = "", TipoOC = "", zona = "", OC = "";
    String F_NoCompra = "", F_Provee = "", F_NomPro = "", F_FecSur = "", F_Zona = "", F_Obser = "", F_Tipo = "";
    try {
        Proyecto = request.getParameter("Proyecto");
        Desproyecto = request.getParameter("DesProyecto");
        OC = request.getParameter("OC");
        Campo = request.getParameter("Campo");
        NoCompra = (String) sesion.getAttribute("NoCompra");
        proveedor = (String) sesion.getAttribute("proveedor");
        zona = (String) sesion.getAttribute("zona");
        fecEnt = (String) sesion.getAttribute("fec_entrega");
        horEnt = (String) sesion.getAttribute("hor_entrega");
        claPro = (String) sesion.getAttribute("clave");
        desPro = (String) sesion.getAttribute("descripcion");
        origen = (String) sesion.getAttribute("origen");
        cedis = (String) sesion.getAttribute("cedis");
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

    if (Campo == null) {
        Campo = "";
    }

    if (cedis == null) {
        cedis = request.getParameter("cedis");
    }

    try {
        con.conectar();
        ResultSet rset = null;

        rset = con.consulta("SELECT F_NoCompra, F_Provee, PR.F_NomPro, F_FecSur, F_Obser FROM tb_pedido_sialss P INNER JOIN tb_proveedor PR ON P.F_Provee = PR.F_ClaProve WHERE F_NoCompra = '" + OC + "' GROUP BY F_NoCompra;");

        while (rset.next()) {
            F_NoCompra = rset.getString(1);
            F_Provee = rset.getString(2);
            F_NomPro = rset.getString(3);
            F_FecSur = rset.getString(4);
            F_Obser = rset.getString(5);
        }
    } catch (Exception e) {
        Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, e);
    } finally {
        try {
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, ex);
        }
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
            <h1>SIAALS</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <h4>Captura OC Proyecto <%=Desproyecto%></h4>
            <hr/>
            <br/>

            <form name="FormBusca" action="CapturaPedidos" method="post">
                <div class="row">
                    <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                    <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                    <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                    <input type="hidden" class="form-control" id="OC" name="OC" value="<%=OC%>" />
                    <label class="col-sm-3 col-sm-offset-7 text-right">
                        <h4>Número de Orden de Compra</h4>
                    </label>
                    <input class="hidden" value="<%=cedis%>" name="nomCedis" >
                    <div class="col-sm-2">
                        <input type="text" class="form-control" id="NoCompra" name="NoCompra" value="<%=OC%>" readonly/>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <label class="col-sm-1">
                        <h4>Proveedor:</h4>
                    </label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" id="Provee" name="Provee" value="<%=F_NomPro%>" readonly/>
                        <input type="hidden" class="form-control" id="Proveedor" name="Proveedor" value="<%=F_Provee%>" />
                        <input type="hidden" class="form-control" id="Fecha" name="Fecha" value="<%=F_FecSur%>" />
                        <input type="hidden" class="form-control" id="Zona" name="Zona" value="<%=F_Zona%>" />
                        <input type="hidden" class="form-control" id="Obs" name="Obs" value="<%=F_Obser%>" />
                        <input type="hidden" class="form-control" id="Tipo" name="Tipo" value="<%=F_Tipo%>" />
                    </div>

                    <br/>
                    <div class="row">

                        <label class="col-sm-1 text-right">
                            <h4>Clave:<%=proveedor%></h4>
                        </label>
                        <div class="col-sm-8">
                            <select name="Clave" id="Clave" class="form-control">
                                <option>-- Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = null;
                                        rset = con.consulta("SELECT F_ClaPro,CONCAT(F_ClaPro,' - ',F_DesPro) FROM tb_medica WHERE " + Campo + "= 1 AND F_StsPro='A';");
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
                                        Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (SQLException ex) {
                                            Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="col-sm-1">
                            <button class="btn btn-primary btn-block" onclick="return validaClaDes(this);" name="accion" value="ClaveAgregar">Clave</button>
                        </div>

                    </div>
                </div>
            </form>
            <br/>
            <form name="FormCaptura" action="CapturaPedidos" method="post">
                <input type="hidden" class="form-control" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                <input type="hidden" class="form-control" id="DesProyecto" name="DesProyecto" value="<%=Desproyecto%>" />
                <input type="hidden" class="form-control" id="Campo" name="Campo" value="<%=Campo%>" />
                <input type="hidden" class="form-control" id="NoCompra" name="NoCompra" value="<%=OC%>" />
                <input type="hidden" class="form-control" id="Proveedor" name="Proveedor" value="<%=F_Provee%>" />
                <input type="hidden" class="form-control" id="Fecha" name="Fecha" value="<%=F_FecSur%>" />
                <input type="hidden" class="form-control" id="Zona" name="Zona" value="<%=F_Zona%>" />
                <input type="hidden" class="form-control" id="Obs" name="Obs" value="<%=F_Obser%>" />
                <input type="hidden" class="form-control" id="Tipo" name="Tipo" value="<%=F_Tipo%>" />
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="row">
                            <label class="col-sm-1 text-right">
                                <h4>Clave:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=claPro%>" name="ClaPro" id="ClaPro"/>
                            </div>
                            <label class="col-sm-2">
                                <h4>Descripción:</h4>
                            </label>
                            <div class="col-sm-7">
                                <input type="text" class="form-control" readonly value="<%=desPro%>" name="DesPro" id="DesPro"/>
                            </div>
                        </div>

                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select pp.F_CantMax, pp.F_CantMin, m.F_PrePro from tb_prodprov2017 pp, tb_medica m where m.F_ClaPro = pp.F_ClaPro and pp.F_ClaPro = '" + claPro + "'and pp.F_ClaProve = '" + proveedor + "' ");
                                if (rset.next()) {
                                    int cantUsada = 0;
                                    int cantMax = 0;
                                    cantMax = rset.getInt(1);
                                    ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_pedido_sialss where F_Clave='" + claPro + "' and F_StsPed !='2'  AND F_Provee='" + proveedor + "'");
                                    while (rset2.next()) {
                                        cantUsada = rset2.getInt(1);
                                    }
                                    int cantRestante = cantMax - cantUsada;
                        %>
                        <div class="row">
                            <label class="col-sm-2 text-right">
                                <h4>Cantidad Enviada:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantUsada)%>" name="" id=""/>
                            </div>
                            <label class="col-sm-2">
                                <h4>Cantidad Máxima:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantMax)%>" name="" id=""/>
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Cantidad Restante:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" readonly value="<%=formatter.format(cantRestante)%>" name="CantRest" id="CantRest"/>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-2">
                                <h4>Presentación</h4>
                            </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" readonly value="<%=rset.getString(3)%>" name="" id=""/>
                            </div>
                        </div>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, e);
                            } finally {
                                try {
                                    con.cierraConexion();
                                } catch (SQLException ex) {
                                    Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, ex);
                                }
                            }
                        %>
                        <div class="row">
                            <%
                                String cantidad = "0";
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta(" select SUM(F_ExiLot) from tb_lote WHERE F_Proyecto ='" + Proyecto + "' AND F_ClaPro = '" + claPro + "' group by F_ClaPro  ");
                                    while (rset.next()) {
                                        cantidad = rset.getString(1);
                                    }
                                    if (cantidad == null) {
                                        cantidad = "0";
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (SQLException ex) {
                                        Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                            <label class="col-sm-2 text-center">
                                <h4>Exist. en Almacén:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="CantAlm" id="CantAlm" readonly="" value="<%=formatter.format(Integer.parseInt(cantidad))%>" />
                            </div>
                            <label class="col-sm-2 text-right">
                                <h4>Pzs a Entregar:</h4>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="CanPro" id="CanPro" data-behavior="only-num" />
                            </div>
                        </div>
                        <br/>
                        <button class="btn btn-block btn-primary" name="accion" value="ClaveAgregar" onclick="return validaCaptura();">Capturar</button>

                    </div>

                </div>
            </form>
            <br/>
            
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/select2.js" type="text/javascript"></script>


        <script type="text/javascript">

                            $("#Clave").select2();
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
                                var fecha = document.getElementById('Fecha').value;
                                var hora = document.getElementById('Hora').value;
                                var cedis = document.getElementById('cedis').value;
                                var NoCompra = document.getElementById('NoCompra').value;
                                if (prove === "" || fecha === "" || hora === "0:00" || NoCompra === "" || cedis === "") {
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
                                var proveedor = document.getElementById('Proveedor').value;
                                if (ClaPro === "" |DesPro === "" || CanPro === "") {
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
                            function SelectProve(form) {
            <%
                try {
                    con.conectar();
                    ResultSet rset3 = con.consulta("SELECT DISTINCT F_ClaProve FROM tb_prodprov2017");
                    while (rset3.next()) {
                        out.println("if (form.Proveedor.value == '" + rset3.getString(1) + "') {");
                        out.println("var select = document.getElementById('Clave');");
                        out.println("select.options.length = 0;");
                        int i = 1;
                        ResultSet rset4 = con.consulta("SELECT F_ClaPro FROM tb_prodprov2017  WHERE  F_ClaProve='" + rset3.getString(1) + "';");

                        out.println("select.options[select.options.length] = new Option('-Seleccione-', '');");
                        while (rset4.next()) {
                            out.println("select.options[select.options.length] = new Option('" + rset4.getString(1) + "', '" + rset4.getString(1) + "');");
                            i++;
                        }
                        out.println("}");
                    }
                } catch (Exception e) {
                    Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (Exception ex) {
                        Logger.getLogger("AgregarMedalfa.jsp").log(Level.SEVERE, null, ex);
                    }
                }
            %>
                            }
        </script>
    </body>

</html>
