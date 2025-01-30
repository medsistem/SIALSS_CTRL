<%-- 
    Document   : compraAuto3
    Created on : 17/02/2014, 03:34:46 PM
    Author     : GNK
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="conn.ConectionDB"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
    String proyecto = "";
    int cantRecibida = 0;
    int cantidad = 0;
    int cantidad2 = 0;
    String claveProducto = "", claveProductoSS = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
    try {
        if (request.getParameter("accion").equals("Buscar")) {
            sesion.setAttribute("posClave", "0");
            sesion.setAttribute("folioRemi", "");
            sesion.setAttribute("CodBar", "");
        }
    } catch (Exception er) {

    }

    int totalClaves = 0, clavesCapturadas = 0;
    String fecha = "", noCompra = "", Proveedor = "", Fecha = "";
    fecha = request.getParameter("Fecha");
    noCompra = request.getParameter("NoCompra");
    Proveedor = request.getParameter("Proveedor");
    if (fecha == null) {
        fecha = "";
    }
    if (noCompra == null) {
        noCompra = (String) sesion.getAttribute("NoCompra");
        if (noCompra == null) {
            noCompra = "";
        }
    }
    if (Proveedor == null) {
        Proveedor = "";
    }

    String posClave = "0";
    try {
        posClave = sesion.getAttribute("posClave").toString();
    } catch (Exception e) {
        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
    }
    if (posClave == null || posClave.equals("")) {
        posClave = "0";
    }

    try {
        if (request.getParameter("accion").equals("buscaCompra")) {
            posClave = "0";
        }
    } catch (Exception e) {
        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
    }

    String folioRemi = "";

    try {
        folioRemi = (String) sesion.getAttribute("folioRemi");
    } catch (Exception e) {
        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
    }

    if (folioRemi == null) {
        folioRemi = "";
    }
    Fecha = request.getParameter("Fecha");
    if (Fecha == null) {
        Fecha = "";
    }

    String CodBar = "", Lote = "", Cadu = "";
    CodBar = (String) sesion.getAttribute("CodBar");
    Lote = (String) sesion.getAttribute("Lote");
    Cadu = (String) sesion.getAttribute("Cadu");

    if (CodBar == null) {
        CodBar = "";
    }
    if (Lote == null) {
        Lote = "";
    }
    if (Cadu == null) {
        Cadu = "";
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/select2.css" rel="stylesheet">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
        <link href="../css/bootstrap.css" rel="stylesheet" />
        <link href="../css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" />
        <link href="../css/jquery.dataTables.css" rel="stylesheet" type="text/css" />
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>
        <link href="../css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <title>SIALSS_CTRL MULTI</title>
        <script src="../js/jquery-1.12.4.js" type="text/javascript"></script>
        <script src="../js/funcIngresos.js"></script>
        <script src="../js/jquery-ui.js" type="text/javascript"></script>
        <script src="../js/select2.js"></script>
        <script src="../js/jquery-2.2.4.min.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/select2.js" type="text/javascript"></script>
        <script src="../js/sweetalert.min.js" type="text/javascript"></script>
        <script src="../js/utils/formato_numero.js" type="text/javascript"></script>
        <script>
            var currentValue = 0;
            function handleClick(myRadio) {
                //alert('Old value: ' + currentValue);
                //alert('New value: ' + myRadio.value);
                currentValue = myRadio.value;
                if (currentValue == 1) {
                    document.getElementById("divremision").style.display = 'block';
                    document.getElementById("divfactura").style.display = 'none';
                    $("#folioRemi").prop("disabled", false);
                    $("#file1").prop("disabled", false);
                }

                if (currentValue == 2) {
                    document.getElementById("divfactura").style.display = 'block';
                    document.getElementById("divremision").style.display = 'none';
                }
            }
        </script>
    </head>
    <body onload="muestraCalendario()">
        <div class="container">
            <div class="modal fade in" id="loadingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display: block;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                            <h4 class="modal-title" id="myModalLabel"></h4>
                        </div>
                        <div class="modal-body">
                            <div class="text-center" id="imagenCarga">
                                <img src="../imagenes/ajax-loader-1.gif" alt="">
                            </div>
                        </div>
                        <div class="modal-footer">
                        </div>
                    </div>
                </div>
            </div>
            <script>
                $('#loadingModal').modal('show');
            </script>
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>
            <form action="compraAuto3.jsp" method="post">
                <div class="row">
                    <label class="col-sm-2 text-right">
                        <h4>Proveedor</h4>
                    </label>
                    <div class="col-sm-5">
                        <select class="form-control" name="Proveedor" id="Proveedor" onchange="SelectProve(this.form);">
                            <option value="">--Proveedor--</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rset = null;

                                    rset = con.consulta("SELECT p.F_ClaProve, p.F_NomPro FROM tb_proveedor p INNER JOIN tb_pedido_sialss pv ON p.F_ClaProve = pv.F_Provee WHERE F_StsPed = '1' AND F_Recibido = '0' AND pv.F_Proyecto != 2 GROUP BY pv.F_Provee ORDER BY p.F_NomPro;");

                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception ex) {
                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>

                        </select>
                    </div>
                    <label class="col-sm-2 text-right">
                        <h4>Fecha</h4>
                    </label>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha" value="<%=Fecha%>" onkeypress="return tabular(event, this)" />
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <button class="btn btn-primary btn-block" name="accion" value="Buscar">Buscar</button>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <label class="col-sm-2">
                        <h4>&Oacute;rdenes de Compra: </h4>
                    </label>
                    <div class="col-sm-9">
                        <select class="form-control" name="NoCompra" id="NoCompra" onchange="this.form.submit();">
                            <option value="">-- Proveedor -- Orden de Compra --</option>
                            <%
                                //  System.out.println("try nocompra 1");
                                try {
                                    fecha = df1.format(df3.parse(Fecha));
                                } catch (Exception e) {
                                    //Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                }
                                //   System.out.println("try nocompra 2");
                                try {
                                    con.conectar();
                                    ResultSet rset = null, rsetcerraroc = null;

                                    rsetcerraroc = con.consulta("UPDATE tb_pedido_sialss as p1 INNER JOIN(SELECT p.F_IdIsem,p.F_NoCompra,p.F_Clave,p.F_Cant AS CantPed, p.F_Recibido,p.F_Proyecto,c.F_OrdCom,c.F_ClaPro,Sum(c.F_CanCom) AS CantCom,c.F_Proyecto AS ProyCom,if(p.F_Cant <= Sum(c.F_CanCom) ,1,0) AS recibo FROM tb_pedido_sialss AS p INNER JOIN tb_compra AS c ON p.F_NoCompra = c.F_OrdCom AND c.F_ClaPro = p.F_Clave AND c.F_Proyecto = p.F_Proyecto GROUP BY  p.F_NoCompra,p.F_Clave,c.F_OrdCom,c.F_ClaPro ORDER BY p.F_NoCompra ASC, p.F_Clave ASC,c.F_OrdCom ASC,c.F_ClaPro ASC) as k On k.F_Clave = k.F_ClaPro set p1.F_Recibido = k.recibo WHERE p1.F_IdIsem = k.F_IdIsem");
                                    rsetcerraroc.next();

                                    rset = con.consulta("SELECT o.F_NoCompra, p.F_NomPro FROM tb_pedido_sialss o INNER JOIN tb_proveedor p ON o.F_Provee = p.F_ClaProve WHERE o.F_FecSur LIKE '%" + fecha + "%' AND o.F_Provee LIKE '%" + request.getParameter("Proveedor") + "' AND F_StsPed != '2' AND F_Recibido = 0 AND o.F_Proyecto != 2 GROUP BY o.F_NoCompra;");

                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%> - <%=rset.getString(1)%></option>
                            <%
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception ex) {
                                        System.out.println("Excepcion:" + ex.getStackTrace());
                                        //Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                                System.out.println("Termina Proceso seleccionar orden");
                            %>
                        </select>
                    </div>
                </div>
                <br/>
            </form>
            <form action="../FileUploadServletOC" method="post" name="formulario1" enctype="multipart/form-data">
                <br/>
                <%
                    System.out.println("try formulario1 1");
                    try {
                        int fechaActualVen = 0;
                        con.conectar();
                        ResultSet rset = con.consulta("select i.F_NoCompra, i.F_FecSur, i.F_HorSur, p.F_NomPro, p.F_ClaProve, i.F_Proyecto,i.Fecha_Vencimiento from tb_pedido_sialss i, tb_proveedor p where i.F_Provee = p.F_ClaProve and F_StsPed = '1' and F_NoCompra = '" + noCompra + "' and F_recibido='0' group by F_NoCompra;");
                        while (rset.next()) {
                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = dateFormat.format(date);
                            Integer fechaActualInt = Integer.parseInt(strDate.replaceAll("-", ""));
                            proyecto = rset.getString("F_Proyecto");
                            System.out.println("proyecto " + proyecto);
                            try {
                                fechaActualVen = Integer.parseInt(rset.getString(7).replaceAll("-", ""));
                            } catch (Exception e) {

                            }
                            System.out.println("********************** " + fechaActualVen);
                            System.out.println(fechaActualVen > 0);
                            //  if (fechaActualVen == 0 || fechaActualVen >= fechaActualInt) {
                            //                            if (fechaActualVen == 0 || fechaActualVen >= fechaActualInt) {
                            if (true) {
                %>
                <div class="row">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <h4 class="col-sm-3">Folio Orden de Compra:</h4>
                                <div class="col-sm-3"><input class="form-control" value="<%=rset.getString(1)%>" readonly="" name="folio" id="folio" onkeypress="return tabular(event, this)" /></div>
                                <div class="col-sm-2">
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-12">Proveedor: <%=rset.getString(4)%></h4>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-12">Fecha y Hora de Entrega: <%=df3.format(df2.parse(rset.getString(2)))%> <%=rset.getString(3)%></h4>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-1">CLAVE:</h4>
                                <div class="col-sm-3">
                                    <select class="form-control" name="selectClave" id="selectClave">
                                        <option>-Seleccione-</option>
                                        <%
                                            /**
                                             * Arroja las claves faltantes por
                                             * ingresar al sistema
                                             */
                                            System.out.println("try formulario1 2");
                                            try {
                                                con.conectar();
                                                ResultSet rset2 = con.consulta("SELECT F_IdIsem, F_Clave, CONCAT(F_Clave, ' - ', F_ClaveSS) AS F_ClaveSS FROM tb_pedido_sialss WHERE F_NoCompra = '" + noCompra + "' AND F_Recibido = '0' group by F_Clave;");
                                                while (rset2.next()) {
                                        %>
                                        <option value="<%=rset2.getString(1)%>"><%=rset2.getString(3)%></option>
                                        <%
                                                    totalClaves++;
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="col-sm-1">
                                    <button class="btn btn-primary btn-block" name="accion" value="seleccionaClave">CLAVE</button>
                                </div>
                                <div class="col-sm-5">
                                    <%
                                        System.out.println("try formulario1 3");
                                        try {
                                            con.conectar();
                                            ResultSet rset5 = con.consulta("SELECT IF((Sum(c.F_CanCom) - pi.F_Cant) = 0,1,0) as claves FROM tb_compra AS c INNER JOIN tb_pedido_sialss AS pi ON c.F_ClaPro = pi.F_Clave AND c.F_OrdCom = pi.F_NoCompra WHERE pi.F_NoCompra = '" + noCompra + "' GROUP BY pi.F_Clave");
                                            while (rset5.next()) {
                                                if (rset5.getInt(1) == 1) {
                                                    clavesCapturadas++;
                                                }
                                            }
                                            ResultSet rset6 = con.consulta("SELECT  count(*) FROM tb_pedido_sialss pi WHERE pi.F_NoCompra = '" + noCompra + "'");
                                            while (rset6.next()) {
                                                totalClaves = rset6.getInt(1);
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                        } finally {
                                            try {
                                                con.cierraConexion();
                                            } catch (Exception ex) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    %>

                                    <h4>Insumos Ingresados <%=clavesCapturadas%>/<%=totalClaves%></h4>
                                </div>
                                <div class="col-sm-2">
                                    <a href="#" class="btn btn-info btn-block" data-toggle="modal" data-target="#Rechazar">Rechazar</a>
                                </div>

                            </div>
                        </div>
                        <div class="panel-body">
                            <%
                                System.out.println("try formulario1 4");
                                try {
                                    con.conectar();
                                    ResultSet rset2 = con.consulta("select s.F_Clave, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem, F_Obser from tb_pedido_sialss s, tb_medica m where s.F_Clave = m.F_ClaPro and F_NoCompra = '" + rset.getString(1) + "' and F_StsPed = '1'");
                                    while (rset2.next()) {
                                        rset2.last();
                                    }
                                    rset2 = con.consulta("SELECT s.F_Clave, s.F_ClaveSS, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem, F_Obser, F_Proyecto, F_Precio , M.F_PrePro FROM tb_pedido_sialss s, tb_medica m WHERE s.F_Clave = m.F_ClaPro and F_NoCompra = '" + rset.getString(1) + "' AND s.F_Proyecto='" + rset.getString(6) + "' and s.F_IdIsem = '" + sesion.getAttribute("claveSeleccionada") + "' ");
                                    while (rset2.next()) {
                                        claveProducto = rset2.getString(1);
                                        claveProductoSS = rset2.getString(2);
                            %>
                            <h4 class="bg-primary" style="padding: 5px"> CLAVE | <%=rset2.getString(1)%> - <%=rset2.getString(2)%> - <%=rset2.getString(3)%> </h4>
                            <h4 class="bg-primary" style="padding: 5px">PRESENTACION | <%=rset2.getString(11)%></h4>

                            <div class="row">
                                <div class="col-sm-6">
                                    <strong>Cantidad a Recibir</strong>
                                    <input type="text" value="<%=formatter.format(rset2.getInt(6))%>" class="form-control" name="cantRecibir" id="cantRecibir" onclick="" readonly=""  onkeypress="return tabular(event, this)"/>
                                </div>
                                <div class="col-sm-6">
                                    <strong>Cantidad Recibida</strong>
                                    <%
                                        System.out.println("try formulario1 5");

                                        try {
                                            con.conectar();
                                            ResultSet rset3 = con.consulta("select sum(F_CanCom) from tb_compra where F_OrdCom = '" + rset.getString(1) + "' and F_ClaPro = '" + rset2.getString(1) + "';");
                                            while (rset3.next()) {
                                                cantRecibida = cantRecibida + rset3.getInt(1);
                                            }
                                            rset3 = con.consulta("select sum(F_Pz) from tb_compratemp where F_OrdCom = '" + rset.getString(1) + "' and F_ClaPro = '" + rset2.getString(1) + "'; ");
                                            while (rset3.next()) {
                                                cantRecibida = cantRecibida + rset3.getInt(1);
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                        } finally {
                                            try {
                                                con.cierraConexion();
                                            } catch (Exception ex) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    %>
                                    <input type="text" value="<%=formatter.format(cantRecibida)%>" class="form-control" name="cantRecibida" id="cantRecibida" onkeypress="return tabular(event, this)" onclick="" readonly=""/>
                                    <input type="text" value="<%=rset2.getString(1)%>" class="hidden" name="ClaPro" id="ClaPro" onclick="" readonly=""  onkeypress="return tabular(event, this)"/>
                                    <input type="text" value="<%=rset2.getString(2)%>" class="hidden" name="ClaProSS" id="ClaProSS" onclick="" readonly=""  onkeypress="return tabular(event, this)"/>
                                </div>
                            </div>
                            <hr>
                            <div class ="row">
                                <div class="col-md-6">
                                    <div class="row"><div class="col-md-12">
                                            <strong>Código de Barras:</strong>
                                            <input type="text" value="<%=CodBar%>" oncopy="return false" onpaste="return false" class="form-control" name="codbar" id="codbar" onclick="" onkeypress="return checkKey(event, this);" style="-webkit-text-security: disc;"/>
                                        </div>
                                    </div>
                                    <div class="row"><div class="col-md-6">
                                            <button class="btn btn-primary btn-block" type="submit" name="accion" id="CodigoBarras" value="CodigoBarras" onclick="">CB</button>
                                        </div>
                                        <div class="col-md-6">
                                            <button class="btn btn-primary btn-block" type="submit" name="accion" id="GeneraCodigo" value="GeneraCodigo" onclick=""><span class="glyphicon glyphicon-barcode"></span></button>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="row"><div class="col-md-12">
                                            <strong>Confirmar Código de Barras:</strong>
                                            <input type="text" value="<%=CodBar%>" class="form-control" id="confirm-codbar" oncopy="return false" onpaste="return false" onclick="" onkeypress="return checkKey(event, this);" onpaste="return false" onfocusout="confirmaCampo('codbar')" style="-webkit-text-security: disc;" />
                                        </div>
                                    </div>

                                </div>

                            </div>

                            <%
                                int contadorLotes = 0;
                                String idMarca = "";
                                if (!CodBar.equals("")) {
                                    try {
                                        con.conectar();
                                        ResultSet rset3 = con.consulta("select F_Cb, F_ClaPro, F_ClaLot, F_FecCad, F_FecFab, F_ClaMar from tb_lote where F_Cb='" + CodBar + "' AND F_ClaPro='" + claveProducto + "' group by F_ClaPro,F_ClaLot, F_FecCad");
                                        while (rset3.next()) {
                                            contadorLotes++;
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception ex) {
                                            Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                if (contadorLotes > 1) {
                            %>
                            <div class="row">
                                <div class="col-md-6">
                                    <strong>Lote:</strong>
                                    <input type="text" value="<%=Lote%>" class="form-control" name="lot" id="lot" oncopy="return false" onpaste="return false" onkeypress="return tabular(event, this)"/>
                                    <select class="form-control" name="list_lote" id="list_lote"  onchange="cambiaLoteCadu(this);" onkeypress="return tabular(event, this)">
                                        <option>--Lote--</option>
                                        <%
                                            if (!CodBar.equals("")) {
                                                try {
                                                    con.conectar();
                                                    ResultSet rset3 = con.consulta("select F_Cb, F_ClaPro, F_ClaLot, F_FecCad, F_FecFab, F_ClaMar from tb_lote where F_Cb='" + CodBar + "' AND F_ClaPro='" + claveProducto + "' group by F_ClaLot, F_FecCad");
                                                    while (rset3.next()) {
                                                        idMarca = rset3.getString(6);
                                        %>
                                        <option><%=rset3.getString(3)%></option>
                                        <%
                                                    }
                                                } catch (Exception e) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                                } finally {
                                                    try {
                                                        con.cierraConexion();
                                                    } catch (Exception ex) {
                                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <!--<div class="col-md-6">
                                    <strong>Confirmar Lote:</strong>
                                    <input type="text" class="form-control" id="confirm-lot" onkeypress="return tabular(event, this)" onpaste="return false" onfocusout="confirmaCampo('lot')"/>

                                </div>  -->
                            </div>


                            <div class="row">
                                <div class="col-md-6">
                                    <strong>Caducidad:</strong>
                                    <input type="text" value="<%=Cadu%>" data-date-format="dd/mm/yyyy" class="form-control" name="cad" id="cad" oncopy="return false" onpaste="return false" onkeypress="return false;" maxlength="10" />
                                    <select class="form-control" name="list_cadu" id="list_cadu">
                                        <option>--Caducidad--</option>
                                        <%
                                            if (!CodBar.equals("")) {
                                                try {
                                                    con.conectar();
                                                    ResultSet rset3 = con.consulta("select F_Cb, F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad,'%d/%m/%Y'), F_FecFab, F_ClaMar from tb_lote where F_Cb='" + CodBar + "' AND F_ClaPro='" + claveProducto + "'  group by F_ClaLot, F_FecCad");
                                                    while (rset3.next()) {
                                        %>
                                        <option><%=rset3.getString(4)%></option>
                                        <%
                                                    }
                                                } catch (Exception e) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                                } finally {
                                                    try {
                                                        con.cierraConexion();
                                                    } catch (Exception ex) {
                                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        %>
                                    </select>

                                    <%
                                    } else {
                                    %>
                                    <%
                                        if (!CodBar.equals("")) {
                                            try {
                                                con.conectar();
                                                ResultSet rset3 = con.consulta("select F_Cb, F_ClaPro, F_ClaLot, F_FecCad, F_FecFab, F_ClaMar from tb_lote where F_Cb='" + CodBar + "' AND F_ClaPro='" + claveProducto + "' group by F_ClaLot, F_FecCad");
                                                while (rset3.next()) {
                                                    idMarca = rset3.getString(6);
                                                    Lote = rset3.getString(3);
                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    %>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <strong>Lote</strong>
                                    <input type="text" value="<%=Lote%>" class="form-control" name="lot" id="lot" oncopy="return false" onpaste="return false" onkeypress="return tabular(event, this)" style="-webkit-text-security: disc;" autocomplete="off"/>
                                    <%
                                        if (!CodBar.equals("")) {
                                            try {
                                                con.conectar();
                                                ResultSet rset3 = con.consulta("select F_Cb, F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad,'%d/%m/%Y'), F_FecFab, F_ClaMar from tb_lote where F_Cb='" + CodBar + "' AND F_ClaPro='" + claveProducto + "' group by F_ClaLot, F_FecCad");
                                                while (rset3.next()) {
                                                    Cadu = rset3.getString(4);
                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    %>
                                </div>
                                <div class="col-md-6">
                                    <strong>Confirmar Lote</strong>
                                    <input type="text" class="form-control" name="lot" id="confirm-lot" oncopy="return false" onpaste="return false" onkeypress="return tabular(event, this)" onpaste="return false" onfocusout="confirmaCampo('lot')" style="-webkit-text-security: disc;"  autocomplete="off" placeholder="1a2b3c"/>
                                </div>
                                <div class="col-md-6">
                                    <strong>Caducidad</strong>
                                    <input type="text" value="<%=Cadu%>" data-date-format="dd/mm/yyyy" class="form-control" name="cad" id="cad" oncopy="return false" onpaste="return false" onkeypress="return false;" maxlength="10" style="-webkit-text-security: disc;" autocomplete="off" placeholder="dd/mm/aaaa"/>

                                </div>
                                <div class="col-md-6">
                                    <strong>Confirmar Caducidad</strong>

                                    <input type="text" data-date-format="dd/mm/yyyy" onkeypress="return false;" class="form-control" id="confirm-cad" oncopy="return false" onpaste="return false" maxlength="10" onpaste="return false" onchange="confirmaCampo('cad')" style="-webkit-text-security: disc;"  autocomplete="off" placeholder="dd/mm/aaaa"/>



                                    <%
                                        }
                                    %>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <strong>Marca</strong>
                                    <select class="form-control" name="list_marca" onKeyPress="return tabular(event, this)" id="list_marca">
                                        <option value="">Marca</option>
                                        <%
                                            try {
                                                con.conectar();
                                                //ResultSet rset3 = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca order by F_DesMar");
                                                ResultSet rset3 = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca where F_DesMar <> '' order by F_DesMar");
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString("F_ClaMar")%>"
                                                <%
                                                    if (rset3.getString("F_ClaMar").equals(idMarca)) {
                                                        out.println("selected");
                                                    }
                                                %>
                                                ><%=rset3.getString("F_DesMar")%></option>
                                        <%

                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <strong>Confirmar Marca</strong>
                                    <select class="form-control"  onKeyPress="return tabular(event, this)" id="confirm-list_marca" oncopy="return false" onpaste="return false" onfocusout="confirmaCampo('list_marca')">
                                        <option value="">Marca</option>
                                        <%
                                            try {
                                                con.conectar();
                                                //ResultSet rset3 = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca order by F_DesMar");
                                                ResultSet rset3 = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca where F_DesMar <> '' order by F_DesMar");
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString("F_ClaMar")%>"
                                                <%
                                                    if (rset3.getString("F_ClaMar").equals(idMarca)) {
                                                        out.println("selected");
                                                    }
                                                %>
                                                ><%=rset3.getString("F_DesMar")%></option>
                                        <%

                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3">
                                    <button class="btn btn-block btn-primary glyphicon glyphicon-refresh" type = "submit" value = "refresh" name = "accion" ></button>
                                </div>
                                <div class="col-sm-3">
                                    <a href="../marcas.jsp" target="_blank"><h4>Alta de Nueva Marca</h4></a>
                                </div>
                                <input value="<%=rset.getString("p.F_ClaProve")%>" name="claPro" id="claPro" oncopy="return false" onpaste="return false" class="hidden" onkeypress="return tabular(event, this)" />
                            </div>
                            <hr>

                            <div class="row">
                               
                                <div class="col-sm-3">
                                    <strong>Origen:</strong>
                                    <select class="form-control" name="F_Origen" id="F_Origen">
                                        <%
                                            try {
                                                con.conectar();
                                                String clave = rset2.getString(1);
                                                ResultSet rset3 = null;
                                             //   if (!proyecto.equals("5")) {
                                                    rset3 = con.consulta("select F_ClaOri, F_DesOri from tb_origen ");
                                            //    } else {
                                           //         rset3 = con.consulta("select F_ClaOri, F_DesOri from tb_origen WHERE F_ClaOri IN ('25')");
                                            //    }
                                                
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString(1)%>"><%=rset3.getString(2)%></option>
                                        <%
                                                }

                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                      

                                <div class="col-sm-2">
                                    <strong>Costo:</strong>
                                    <input name="F_Costo" id="F_Costo" type="text" class="form-control" value="<%=rset2.getString(10)%>" />
                                   
                                </div>
                                <input name="F_Proyectos" id="F_Proyectos" type="text" style="display:none;"/>
                                <div class="col-sm-3">
                                    <strong>Proyecto:</strong>
                                    <select name="Proyectos" id="Proyectos" class="form-control" >
                                        <%                                            try {
                                                con.conectar();
                                                String clave = rset2.getString(1);
                                                ResultSet rset3 = con.consulta("SELECT * FROM tb_proyectos where F_Id='" + rset2.getString(9) + "';");
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString(1)%>"><%=rset3.getString(2)%></option>
                                        <%
                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <%if (rset2.getString(9).equals("2")) {    %>        
                            </br>        
                            <div class="row">
                                <div class="col-sm-4">
                                    <strong>Orden de Suministro:</strong>
                                    <input name="ordenSuministro" id="ordenSuministro" type="text" class="form-control" onkeypress="return tabular(event, this)" style="-webkit-text-security: disc;" autocomplete="off" />
                                </div>
                                <div class="col-sm-4">
                                    <strong>Confirmar Orden de Suministro:</strong>
                                    <input name="confirm-ordenSuministro" id="confirm-ordenSuministro" type="text" class="form-control" onkeypress="return tabular(event, this)" onpaste="return false" onfocusout="confirmaCampo('ordenSuministro')" autocomplete="off"/>
                                </div>
                            </div>  
                            <% }%>       
                            <strong>Observaciones</strong>
                            <textarea class="form-control" readonly><%=rset2.getString("F_Obser")%></textarea>



                            <h5><strong>Tarimas Completas</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="tarimas">Tarimas</label>
                                        <input  class="form-control" id="TarimasC" name="TarimasC" placeholder="0" onKeyPress="return justNumbers(event);
                                            return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="tarimas">Cajas x Tarima</label>
                                        <input  class="form-control" id="CajasxTC" name="CajasxTC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick="" 
                                               style="-webkit-text-security: disc;" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="PzsxCC">Piezas x Caja</label>
                                        <input  class="form-control" id="PzsxCC" name="PzsxCC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick=""
                                               style="-webkit-text-security: disc;" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                            </div>
                            <!--                            <div class="row">
                            
                                                            <label for="Cajas" class="col-sm-2 control-label">Tarimas</label>
                                                            <div class="col-sm-1">
                                                                <input type="Cajas" class="form-control" id="TarimasC" name="TarimasC" placeholder="0" onKeyPress="return justNumbers(event);
                                                                        return handleEnter(event);" onkeyup="totalPiezas()" onclick="" />
                                                            </div>
                                                            <label for="pzsxcaja" class="col-sm-2 control-label">Cajas x Tarima</label>
                                                            <div class="col-sm-1">
                                                                <input type="text" class="form-control" id="CajasxTC" name="CajasxTC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick="" />
                                                            </div>
                                                            <label for="Resto" class="col-sm-2 control-label">Piezas x Caja</label>
                                                            <div class="col-sm-1">
                                                                <input type="text" class="form-control" id="PzsxCC" name="PzsxCC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick="" />
                                                            </div>
                                                        </div>-->
                            <hr>
                            <h5><strong>Tarimas Incompletas</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="tarimasI">Tarimas</label>
                                        <input  class="form-control" id="TarimasI" name="TarimasI" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;" onpaste="return false" min="0" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="CajasxTI">Cajas x Tarima</label>
                                        <input  class="form-control" id="CajasxTI" name="CajasxTI" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;" onpaste="return false" min="0" type="number" > 
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="Resto">Resto</label>
                                        <input class="form-control" id="Resto" name="Resto" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;" onpaste="return false" min="0" type="number">
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="factor">Factor de Empaque</label>
                                        <input  class="form-control" id="factorEmpaque" name="factorEmpaque" value="1" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;" onpaste="return false" min="1" type="number">
                                    </div>
                                </div>

                            </div>

                            <!--                                <label for="Cajas" class="hidden">Tarimas</label>
                                                            <div class="hidden">
                                                                <input type="Cajas" class="form-control" id="TarimasI" name="TarimasI" placeholder="0" onKeyPress="return justNumbers(event);
                                                                        return handleEnter(event);" onkeyup="totalPiezas();" onclick="" />
                                                            </div>
                                                            <label for="pzsxcaja" class="col-sm-2 control-label">Cajas x Tarima</label>
                                                            <div class="col-sm-1">
                                                                <input type="pzsxcaja" class="form-control" id="CajasxTI" name="CajasxTI" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick=""/>
                                                            </div>
                                                            <label for="pzsxcaja" class="col-sm-2 control-label">Resto</label>
                                                            <div class="col-sm-1">
                                                                <input type="pzsxcaja" class="form-control" id="Resto" name="Resto" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick=""/>
                                                            </div>-->



                            <hr>
                            <h5><strong>Totales</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="Tarimas">Tarimas</label>
                                        <input type="text" class="form-control" id="Tarimas" name="Tarimas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick=""  >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="Cajas">Cajas Completas</label>
                                        <input type="text" class="form-control" id="Cajas" name="Cajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="CajasIn">Cajas Incompletas</label>
                                        <input type="text" class="form-control" id="CajasIn" name="CajasIn" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="">
                                    </div>
                                </div>
                                <br>
                            </div>

                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="TCajas">Total Cajas</label>
                                        <input type="text" class="form-control" id="TCajas" name="TCajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="">
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="Piezas">Piezas</label>
                                        <input type="text" class="form-control" id="Piezas" name="Piezas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="">
                                    </div>
                                </div>
                                <!--                                <label for="Cajas" class="col-sm-1 control-label">Tarimas</label>
                                                                <div class="col-sm-1">
                                                                    <input type="text" class="form-control" id="Tarimas" name="Tarimas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                                                            return handleEnter(event);" onkeyup="totalPiezas();" onclick="" />
                                                                </div>
                                                                <label for="pzsxcaja" class="col-sm-1 control-label">Cajas Completas</label>
                                                                <div class="col-sm-1">
                                                                    <input type="text" class="form-control" id="Cajas" name="Cajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick=""/>
                                                                </div>
                                                                <label for="CajasIn" class="col-sm-1 control-label">Cajas Incompletas</label>
                                                                <div class="col-sm-1">
                                                                    <input type="text" class="form-control" id="CajasIn" name="CajasIn" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick=""/>
                                                                </div>
                                                                <label for="TCajas" class="col-sm-1 control-label">Total Cajas</label>
                                                                <div class="col-sm-1">
                                                                    <input type="text" class="form-control" id="TCajas" name="TCajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick=""/>
                                                                </div>
                                                                <label for="Resto" class="col-sm-1 control-label">Piezas</label>
                                                                <div class="col-sm-2">
                                                                    <input type="text" class="form-control" id="Piezas" name="Piezas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas();" onclick="" />
                                                                </div>-->
                            </div>
                            <hr>


                            <hr>
                            <h4><strong>Confirmar cantidades</strong></h4>
                            <h5><strong>Tarimas Completas</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-tarimas">Tarimas</label>
                                        <input  class="form-control" id="confirm-TarimasC" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onfocusout="confirmaCampo('TarimasC')" onclick="" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-">Cajas x Tarima</label>
                                        <input  class="form-control" id="confirm-CajasxTC" placeholder="0" onKeyPress="return justNumbers(event);" onclick="" 
                                               onfocusout="confirmaCampo('CajasxTC')" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="tarimas">Piezas x Caja</label>
                                        <input  class="form-control" id="confirm-PzsxCC"  placeholder="0" onKeyPress="return justNumbers(event);" onclick=""
                                               onfocusout="confirmaCampo('PzsxCC')" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <h5><strong>Tarimas Incompletas</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-tarimasI">Tarimas</label>
                                        <input  class="form-control" id="confirm-TarimasI"  placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onfocusout="confirmaCampo('TarimasI')" onpaste="return false" min="0" type="number" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-CajasxTI">Cajas x Tarima</label>
                                        <input  class="form-control" id="confirm-CajasxTI" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onclick="" onfocusout="confirmaCampo('CajasxTI')" onpaste="return false" min="0" type="number" > 
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-Resto">Resto</label>
                                        <input  class="form-control" id="confirm-Resto"  placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onclick="" onfocusout="confirmaCampo('Resto')" onpaste="return false" min="0" type="number" >
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="confirm-factor">Factor de Empaque</label>
                                        <input  class="form-control" id="confirm-factorEmpaque"  value="1" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onclick="" onfocusout="confirmaCampo('factorEmpaque')" onpaste="return false" min="1" type="number" >
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <h5><strong>Observaciones</strong></h5>
                            <textarea class="form-control" id="Obser" name="Obser"></textarea>

                            <div class="row" style="padding: 15px">
                                <%
                                    //if (tipo.equals("2") || tipo.equals("3") || tipo.equals("1")) {
                                %>                      
                                <div class="col-sm-10">
                                    <input type="radio" id="idFactura" name="tipofolio" onclick="handleClick(this);" value="2" style="width: 6%;"> Factura
                                    <input type="radio" id="idRemision" name="tipofolio" onclick="handleClick(this);" value="1" style="width: 6%;"> Remisi&oacute;n

                                    <div style="margin-left: 22%;margin-top: -3.1%;">
                                        <div class="col-sm-5" id="divremision" style="display:block; width:58%;">
                                            <h4 class="col-sm-6 text-right" style="font-size: 14px">Folio de Remisión:</h4>
                                            <div style="    margin-left: 50%;margin-right: -38%;">
                                                <input disabled class="form-control" value="" name="folioRemi" id="folioRemi"/>
                                            </div>
                                            <!--h4 class="col-sm-6 text-right" style="font-size: 14px">Remisión:</h4>
                                            <input disabled class="form-control" type="file" name="file1" id="file1" /-->
                                        </div>

                                        <div class="col-sm-5" id="divfactura" style="display:none; width:58%;">
                                            <h4 class="col-sm-6 text-right" style="font-size: 14px">Folio de Factura:</h4>
                                            <div style="    margin-left: 50%;margin-right: -38%;">
                                                <input class="form-control" value="" name="folioFac" id="folioFac" />
                                            </div>
                                            <!--h4 class="col-sm-6 text-right" style="font-size: 14px">Factura:</h4>
                                            <input class="form-control" type="file" name="file2" id="file2"/-->
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-5"><br>&nbsp;</div>

                                <div class="col-sm-10" style="width:100%;">
                                    <br>&nbsp;
                                    <button class="btn btn-block btn-primary" type="submit" name="accion" id="accion" value="guardarLote" onclick="return validaCompra();" disabled>Guardar Lote</button>

                                </div>
                            </div>
                            <hr/>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                } finally {
                                    try {
                                        con.cierraConexion();
                                    } catch (Exception ex) {
                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                    }
                                }
                            %>
                        </div>
                        </form>
                        <%} else {  %>      

                        <div class="row">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="row">
                                        <h4 class="col-sm-3">Fecha vencida:</h4>
                                        <div class="col-sm-2" style="width: 61%;"><h4>La orden de compra que desea ver ya ha llegado a su fecha de vencimiento si desea continuar porfavor de comunicarse con soporte a compras.</h4></div>
                                    </div>
                                </div>
                                <hr/>
                            </div>
                        </div>

                        <%    }

                                }
                            } catch (Exception e) {
                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                            } finally {
                                try {
                                    con.cierraConexion();
                                } catch (Exception ex) {
                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                }
                            }
                        %>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset3 = con.consulta("SELECT count(*)  FROM tb_compratemp C INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro WHERE F_OrdCom='" + noCompra + "' and F_Estado = '1' and F_FolFac ='' ");
                                ResultSet rset4 = con.consulta("SELECT count(*)  FROM tb_compratemp C INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro WHERE F_OrdCom='" + noCompra + "' and F_Estado = '1' and F_FolRemi = '' ");
                                while (rset3.next()) {
                                    cantidad = rset3.getInt(1);
                                }
                                while (rset4.next()) {
                                    cantidad2 = rset4.getInt(1);
                                }
                            } catch (Exception e) {
                                Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                            } finally {
                                try {
                                    con.cierraConexion();
                                } catch (Exception ex) {
                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                }
                            }
                        %>
                        <%
                            if (cantidad
                                    > 0) {%>
                        <div class="col-sm-1" style=" width: 25%;margin-left: -1%;margin-bottom: 1%;">
                            <button class="btn btn-primary btn-block" type="button" onclick="nuevoArchivo(1)">Adjuntar archivo Remision</button>
                        </div>
                        <%}
                            if (cantidad2
                                    > 0) {%>
                        <div class="col-sm-1" style=" width: 25%;margin-left: -1%;margin-bottom: 1%;">
                            <button class="btn btn-primary btn-block" type="button" onclick="nuevoArchivo(2)">Adjuntar archivo Factura</button>
                        </div>
                        <%}%>

                        <!--ARCHIVO GENERAL-->    
                        <button type="button" style="display:none;" id="btnModalNuevo" data-toggle="modal" data-target="#myModalN"></button>
                        <div class="modal fade bs-example-modal-lg" id="myModalN" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                            <form id="nuevoArchivo" method="POST" action="../FileUploadServletNuevoPDF" enctype="multipart/form-data">  
                                <div class="modal-dialog modal-lg" role="document">
                                    <div class="modal-content"     style="margin: 25%;">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h3 class="modal-title text-center text-success" id="myModalLabel1">Seleccionar Archivo para Factura/Remisión</h3>
                                            <input id="idCompraTemporal" type="hidden">
                                            <input id="UserActual" type="hidden" value="<%=usua%>">
                                        </div>
                                        <div class="modal-body">
                                            <div class="row" style="margin-left: 75px;">
                                                <label for="nuevoArchivoS" style=" width: 142px; height: 52px;  border-radius: 100px;cursor: pointer;">
                                                    <a class="iborrainputfile" style="background: white;border: white; color:black"><b><u>Click aqui</u></b></a>
                                                </label>
                                                <span id="fichero_seleccionado" ></span>      


                                                <input type="file" name="nuevoArchivoS" id="nuevoArchivoS" class="inputfile inputfile-1" >
                                                <input id="escondido" type="hidden" name="tipo" id="tipo" >

                                            </div>
                                        </div>
                                        <br/>
                                        <hr/>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-info" data-dismiss="modal" id="btnCancel2" >Cancelar</button>
                                            <button type="button" onclick="envioNuevo()" class="btn btn-primary" id="btnSave1" name="btnSave1" >Guardar</button>

                                        </div>
                                    </div>
                                </div>
                            </form>  
                        </div>



                        <div class="table-responsive">
                            <table id="tablaPintado" class="table table-bordered table-striped" style="width: 100%">
                                <thead>
                                <th>Folio</th>
                                <th><a name="ancla"></a>Código de Barras</th>
                                <th>CLAVE</th>
                                <th>Descripción</th>
                                <th>Presentacion</th>
                                <th>Lote</th>
                                <th>Caducidad</th>                        
                                <th>Cantidad</th>                      
                                <th>Costo U</th>                     
                                <th>IVA</th>                       
                                <th>Importe</th>

                                <th></th>
                                <th>Archivo PDF</th>
                                </head>
                                <%
                                    int banCompra = 0;
                                    String archivoNombre = "";

                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_FolFac,C.F_FileRemi,C.F_FileFac,M.F_PrePro  FROM tb_compratemp C INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro WHERE F_OrdCom='" + noCompra + "' and F_Estado = '1'");

                                        while (rset.next()) {
                                            banCompra = 1;
                                %>
                                <tbody>
                                    <tr data-id-row="<%=rset.getString(7)%>">
                                        <td><%=rset.getString("C.F_FolRemi") + rset.getString("C.F_FolFac")%></td>
                                        <td><%=rset.getString(1)%></td>                           
                                        <td><%=rset.getString(2)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(15)%></td>
                                        <td><%=rset.getString(4)%></td>
                                        <td><%=df3.format(df2.parse(rset.getString(5)))%></td>
                                        <td><%=formatter.format(rset.getDouble(6))%></td>           
                                        <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                        <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                        <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td> 

                                        <td>

                                            <form method="get" action="../ModificacionesHH">
                                                <input name="id" type="text" style="" class="hidden" value="<%=rset.getString(7)%>" />
                                                <button class="btn btn-info" name="accion" value="modificarCompraAuto"><span class="glyphicon glyphicon-pencil" ></span></button>
                                                <button class="btn btn-info" onclick="return confirm('¿Seguro de que desea eliminar?');" name="accion" value="eliminarCompraAuto"><span class="glyphicon glyphicon-remove"></span>
                                                </button>
                                            </form>
                                        </td>
                                        <%
                                            if (rset.getString(14).equals("") && !rset.getString(13).equals("")) {
                                                archivoNombre = rset.getString(13).substring(rset.getString(13).lastIndexOf("/"));
                                        %>
                                        <td >
                                            <a style="padding: 25px;" href="../pdf<%=rset.getString(13).substring(rset.getString(13).lastIndexOf("/"))%>" target="_blank"></a>
                                            <div style="margin-left: 68px;margin-top: -37px; cursor: pointer;"><i onclick="editar('<%=archivoNombre%>',<%=rset.getString(7)%>)" class="far fa-edit" style='color:black'></i></div>

                                        </td>
                                        <%} else if (rset.getString(13).equals("") && !rset.getString(14).equals("")) {
                                            archivoNombre = rset.getString(14).substring(rset.getString(14).lastIndexOf("/"));
                                        %>
                                        <td >
                                            <a style="padding: 25px;" href="../pdf<%=rset.getString(14).substring(rset.getString(14).lastIndexOf("/"))%>" target="_blank"></a>
                                            <div style="margin-left: 68px;margin-top: -37px; cursor: pointer;"><i  onclick="editar('<%=archivoNombre%>',<%=rset.getString(7)%>)" class="far fa-edit" style='color:black'></i></div>
                                        </td>
                                        <%} else { %>
                                        <td id="imagenPDF">
                                        </td>

                                        <% }%>
                                    </tr>
                                </tbody>
                                <%
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception ex) {
                                            Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }

                                %>
                            </table>
                        </div>

                        <!--Edicion de archivo directo-->    
                        <button type="button" style="display:none;" id="btnModal" data-toggle="modal" data-target="#myModal"></button>
                        <div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                            <form id="edicionArchivo" method="POST" action="../FileUploadServletEdicionPDF" enctype="multipart/form-data"> 

                                <div class="modal-dialog modal-lg" role="document">
                                    <div class="modal-content"     style="margin: 25%;">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h3 class="modal-title text-center text-success" id="myModalLabel1">Editar Archivo PDF</h3>
                                            <input id="UserActual" type="hidden" value="<%=usua%>">
                                        </div>
                                        <div class="modal-body">
                                            <div class="row" style="margin-left: 75px;">
                                                <label for="archivoEditar" style=" width: 142px; height: 52px;  border-radius: 100px;cursor: pointer;">
                                                    <span class="iborrainputfile">Subir archivo</span>
                                                </label>
                                                <span id="fichero_seleccionado2" ></span>      


                                                <input type="file" name="archivoEditar" id="archivoEditar"  class="inputfile inputfile-1" >
                                                <input type="hidden" name="idCo" id="idCo"  >
                                            </div>
                                        </div>
                                        <br/>
                                        <hr/>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-info" data-dismiss="modal" id="btnCancel" >Cancelar</button>
                                            <button type="button" onclick="envio()" class="btn btn-primary" id="btnSave2" name="btnSave2" >Guardar</button>

                                        </div>
                                    </div>
                                </div>
                            </form>  
                        </div>


                        <%                    if (banCompra
                                    == 1) {
                        %>
                        <div class="row">
                            <div class="col-lg-6">
                                <form action="../nuevoAutomaticaLotes" method="post">
                                    <input name="fol_gnkl" type="text" style="" class="hidden" value="<%=noCompra%>" />
                                    <button  value="Eliminar" name="accion" class="btn btn-info btn-block" onclick="return confirm('¿Seguro que desea eliminar la compra?');">Cancelar Compra</button>
                                </form>
                            </div>
                            <div class="col-lg-6">
                                <form id="ingresarModificacionesHH" action="../ModificacionesHH" method="post">
                                    <input name="fol_gnkl" type="text" style="" class="hidden" value="<%=noCompra%>" />
                                    <button  value="verificarCompraAuto" name="accion" class="btn btn-primary btn-block" onclick="return  validarOC()">Ingresar OC</button>
                                </form>
                            </div>
                        </div> 
                        <%
                            }
                        %>

                    </div>



                    <!--
                    Modal de Rechazo
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
                                                <input name="NoCompraRechazo" id="NoCompraRechazo" value="<%=noCompra%>" class="form-control" readonly="" />
                                            </div>
                                        </div>
                                        <div class="row">
                                            <%

                                                try {
                                                    con.conectar();
                                                    ResultSet rset = con.consulta("select i.F_NoCompra, i.F_FecSur, i.F_HorSur, p.F_NomPro, p.F_ClaProve from tb_pedido_sialss i, tb_proveedor p where i.F_Provee = p.F_ClaProve and F_StsPed = '1' and F_NoCompra = '" + noCompra + "' and F_recibido='0' group by F_NoCompra");
                                                    while (rset.next()) {
                                            %>
                                            <div class="col-sm-12">
                                                Proveedor:<%=rset.getString("p.F_NomPro")%>
                                            </div>
                                            <div class="col-sm-12">
                                                Hora <%=rset.getString("i.F_FecSur")%> - <%=rset.getString("i.F_HorSur")%>
                                            </div>
                                            <%
                                                    }
                                                } catch (Exception e) {
                                                    Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                                } finally {
                                                    try {
                                                        con.cierraConexion();
                                                    } catch (Exception ex) {
                                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            %>
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
                                                <input type="date" min="<%=df2.format(new Date())%>" class="form-control" id="FechaOrden" name="FechaOrden" />
                                            </div>
                                            <div class="col-sm-6">
                                                <select class="form-control" id="HoraOrden" name="HoraOrden">
                                                    <%
                                                        for (int i = 0;
                                                                i < 24; i++) {
                                                            if (i != 24) {
                                                    %>
                                                    <option value="<%=i%>:00"><%=i%>:00</option>
                                                    <option value="<%=i%>:30"><%=i%>:30</option>
                                                    <%
                                                    } else {
                                                    %>
                                                    <option value="<%=i%>:00"><%=i%>:00</option>
                                                    <%
                                                            }
                                                        }
                                                    %>
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
                                                    <label>
                                                        <h4><input type="checkbox" checked name="todosChk" id="todosChk" onclick="checkea(this)">Seleccionar todas</h4>
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <%
                                                    try {
                                                        con.conectar();
                                                        ResultSet rset = con.consulta("select F_Clave from tb_pedido_sialss where F_NoCompra = '" + noCompra + "' ");
                                                        int columna = 1;
                                                        while (rset.next()) {
                                                            if (columna == 1) {
                                                            }
                                                %>

                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" checked="" name="chkCancela" value="<%=rset.getString(1)%>"><%=rset.getString(1)%>
                                                    </label>
                                                </div>
                                                <%
                                                            if (columna % 5 == 0) {
                                                                columna = 0;
                                                            }
                                                            columna++;
                                                        }
                                                    } catch (Exception e) {
                                                        Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, e);
                                                    } finally {
                                                        try {
                                                            con.cierraConexion();
                                                        } catch (Exception ex) {
                                                            Logger.getLogger("compraAuto3.jsp").log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                %>

                                            </div>
                                        </div>
                                        <div class="text-center" id="imagenCarga" style="display: none;" > 
                                            <img src="../imagenes/ajax-loader-1.gif">
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary" onclick="return validaRechazo();" name="accion" value="Rechazar">Rechazar OC</button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    </body>

                    <script type="text/javascript">

                        $("#selectClave").select2();
                        $("#list_marca").select2();
                        $("#Proveedor").select2();
                        $("#NoCompra").select2();
                        function checkea(obj) {
                            var cbs = document.getElementsByName('chkCancela');
                            if (obj.checked) {
                                for (var i = 0; i < cbs.length; i++)
                                {
                                    cbs[i].checked = true;
                                }
                            } else {
                                for (var i = 0; i < cbs.length; i++)
                                {
                                    cbs[i].checked = false;
                                }
                            }
                        }

                        function validaRechazo() {
                            var obser = document.getElementById('rechazoObser').value;
                            var fechaN = document.getElementById('FechaOrden').value;
                            var horaN = document.getElementById('HoraOrden').value;
                            var correoProvee = document.getElementById('correoProvee').value;
                            if (obser === "") {
                                alert('Ingrese las observaciones del rechazo.');
                                return false;
                            }
                            if (fechaN === "") {
                                alert('Ingrese nueva fecha de recepción.');
                                return false;
                            }
                            if (horaN === "0:00") {
                                alert('Ingrese nueva hora de recepción.');
                                return false;
                            }
                            if (correoProvee === "") {
                                alert('Ingrese correo de proveedor.');
                                return false;
                            }
                            var con = confirm('¿Seguro que desea rechazar la OC?');
                            if (con === false) {
                                return false;
                            }
                            document.getElementById('imagenCarga').style.display = 'block';
                        }

                        function justNumbers(e)
                        {
                            var keynum = window.event ? window.event.keyCode : e.which;
                            if ((keynum === 8) || (keynum === 46))
                                return true;
                            return /\d/.test(String.fromCharCode(keynum));
                        }

                        otro = 0;
                        function LP_data(e, esto) {
                            var key = (document.all) ? e.keyCode : e.which; //codigo de tecla. 
                            //if (key < 48 || key > 57 )//si no es numero 
                            return false; //anula la entrada de texto.
                            //else
                            //    anade(esto);
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

                        var formatNumber = {
                            separador: ",", sepDecimal: '.',
                            formatear: function (num) {
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
                            new : function (num, simbol) {
                                this.simbol = simbol || '';
                                return this.formatear(num);
                            }
                        };
                        function totalPiezas() {
                            var totalTarimas = 0;
                            var TarimasC = document.getElementById('TarimasC').value;
                            var CajasxTC = document.getElementById('CajasxTC').value;
                            var PzsxCC = document.getElementById('PzsxCC').value;
                            var TarimasI = document.getElementById('TarimasI').value;
                            var CajasxTI = document.getElementById('CajasxTI').value;
                            var Resto = document.getElementById('Resto').value;
                            //alert("Resto = "+Resto);
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

                            var totalCajas = parseInt(CajasxTC) * parseInt(TarimasC) + parseInt(CajasxTI);
                            document.getElementById('TCajas').value = formatNumber.new(totalCajas);
                            if (Resto == 0) {

                                document.getElementById('CajasIn').value = formatNumber.new(0);
                            } else {

                                document.getElementById('TCajas').value = formatNumber.new(totalCajas + 1);
                                document.getElementById('CajasIn').value = formatNumber.new(1);

                            }


                            if (parseInt(CajasxTI) !== parseInt(0)) {
                                TarimasI = parseInt(TarimasI);
                            }

                            if ((parseInt(CajasxTI) === parseInt(0)) && (parseInt(TarimasC) !== parseInt(0))) {
                                totalTarimas = parseInt(TarimasC);
                            } else {
                                totalTarimas = parseInt(TarimasC) + parseInt(TarimasI);
                            }


                            if (totalTarimas === 0 && Resto !== 0) {
                                totalTarimas = totalTarimas + 1;
                            }

                            document.getElementById('Tarimas').value = formatNumber.new(totalTarimas);
                            var totalCajas = parseInt(CajasxTC) * parseInt(TarimasC) + parseInt(CajasxTI) * TarimasI;
                            document.getElementById('Cajas').value = formatNumber.new(totalCajas);
                            var totalPiezas = parseInt(PzsxCC) * parseInt(totalCajas);
                            document.getElementById('Piezas').value = formatNumber.new(totalPiezas + parseInt(Resto));
                        }


                        /* Inicialización en español para la extensión 'UI date picker' para jQuery. */
                        /* Traducido por Vester (xvester@gmail.com). */
                        (function (factory) {
                            if (typeof define === "function" && define.amd) {

                                // AMD. Register as an anonymous module.
                                define(["../widgets/datepicker"], factory);
                            } else {

                                // Browser globals
                                factory(jQuery.datepicker);
                            }
                        }(function (datepicker) {

                            datepicker.regional.es = {
                                closeText: "Cerrar",
                                prevText: "&#x3C;Ant",
                                nextText: "Sig&#x3E;",
                                currentText: "Hoy",
                                monthNames: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
                                monthNamesShort: ["ene", "feb", "mar", "abr", "may", "jun",
                                    "jul", "ago", "sep", "oct", "nov", "dic"],
                                dayNames: ["domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"],
                                dayNamesShort: ["dom", "lun", "mar", "mié", "jue", "vie", "sáb"],
                                dayNamesMin: ["D", "L", "M", "X", "J", "V", "S"],
                                weekHeader: "Sm",
                                dateFormat: "dd/mm/yy",
                                firstDay: 1,
                                isRTL: false,
                                showMonthAfterYear: false,
                                yearSuffix: ""};
                            datepicker.setDefaults(datepicker.regional.es);

                            return datepicker.regional.es;

                        }));

                        /*
                         * 
                         * @Calendario por fecha
                         * 
                         * Angel Flores
                         * 
                         * Muestra calendario empleando el plugin de  datepicker
                         * 
                         */
                        // $.datepicker.setDefaults($.datepicker.regional['es']);
                        function muestraCalendario() {
                            $("#cad").datepicker({
                                dateFormat: 'dd/mm/yy',
                                minDate: 1,
                                changeMonth: true,
                                changeYear: true
                            });
                            $("#confirm-cad").datepicker({
                                dateFormat: 'dd/mm/yy',
                                minDate: 1,
                                changeMonth: true,
                                changeYear: true
                            });
                        }

                        function validaCompra() {

                            /*
                             var folioRemi = document.getElementById('folioRemi').value;
                             if (folioRemi === "") {
                             alert("Falta Folio de Remisión");
                             document.getElementById('folioRemi').focus();
                             return false;
                             }
                             */
                            var codBar = document.getElementById('codbar').value.trim();
                            if (codBar === "") {
                                alert("Falta Código de Barras");
                                document.getElementById('codbar').focus();
                                return false;
                            }

                            var lot = document.getElementById('lot').value.trim();
                            if (lot === "" || lot === "-") {
                                alert("Falta Lote");
                                document.getElementById('lot').focus();
                                return false;
                            }

                            var marca = document.getElementById('list_marca').value.trim();
                            if (marca === "" || marca === "-") {
                                alert("Falta Marca");
                                document.getElementById('list_marca').focus();
                                return false;
                            }

                            var F_Costo = document.getElementById('F_Costo').value.trim();
                            if (F_Costo === "" || F_Costo === "-") {
                                alert("Falta Costo");
                                document.getElementById('F_Costo').focus();
                                return false;
                            }

                            var Proy = document.getElementById('Proyectos').value;
                            //alert("Proyectos:"+Proy)
                            if (Proy === "" || Proy === "-") {
                                alert("Seleccione Proyecto");
                                document.getElementById('F_Proyectos').focus();
                                return false;
                            } else {
                                document.getElementById('F_Proyectos').value = Proy;
                            }


                            var cad = document.getElementById('cad').value.trim();
                            if (cad === "") {
                                alert("Falta Caducidad");
                                document.getElementById('cad').focus();
                                return false;
                            } else {
                                var dtFechaActual = new Date();
                                var sumarDias = parseInt(0);
                                dtFechaActual.setDate(dtFechaActual.getDate() + sumarDias);
                                var fechaSpl = cad.split("/");
                                var Caducidad = fechaSpl[2] + "-" + fechaSpl[1] + "-" + fechaSpl[0];

                                if (Date.parse(dtFechaActual) >= Date.parse(Caducidad)) {
                                    alert("Verifique la caducidad");
                                    document.getElementById('cad').focus();
                                    return false;
                                }

                            }

                            var Piezas = document.getElementById('Piezas').value;
                            if (Piezas === "" || Piezas === "0") {
                                document.getElementById('Piezas').focus();
                                alert("Favor de llenar todos los datos");
                                return false;
                            }

                            var factor = document.getElementById('factorEmpaque').value;
                            if (factor === "" || factor === "0") {
                                document.getElementById('factor').focus();
                                alert("Favor de llenar todos los datos");
                                return false;
                            }

                            var cantRecibida = document.getElementById('cantRecibida').value;
                            var cantTotal = document.getElementById('cantRecibir').value;
                            cantRecibida = cantRecibida.replace(/,/gi, "");
                            cantTotal = cantTotal.replace(/,/gi, "");
                            Piezas = Piezas.replace(/,/gi, "");
                            var nCantidad = parseInt(Piezas) + parseInt(cantRecibida);
                            //alert("Cantidad"+nCantidad+" / "+cantRecibida+" / "+cantTotal+" / "+cantRecibida);
                            if (nCantidad > parseInt(cantTotal)) {
                                alert("Excede la cantidad a recibir, favor de verificar");
                                return false;
                            }

                            /*
                             31/01/2018
                             Angel Flores
                             Se realiza validacion para asegurar que sea enviado un folio de Remision o Factura
                             */
                            if (currentValue > 0) {
                                var remision = $("#idRemision").is(':checked');
                                var factura = $("#idFactura").is(':checked');
                                var valorRemision = $("#folioRemi").val().trim();
                                //var valorArRemision = $("#file1").val();
                                var valorFactura = $("#folioFac").val().trim();
                                //var valorArFactura = $("#file2").val();
                                var bandera = false;
                                var mensaje = "Favor de introducir los siguientes campos faltantes: \n";
                                if (remision && !factura) {
                                    /* let extension = valorArRemision.substring(valorArRemision.lastIndexOf("."));
                                     if(valorRemision === "" || valorRemision === null) {
                                     mensaje += "Folio de Remision \n";
                                     bandera = true;
                                     }
                                     if(valorArRemision === "" || valorRemision === null){
                                     mensaje += "Archivo de remision \n"
                                     bandera = true;
                                     }
                                     if(valorArRemision != "" && extension != ".pdf"){
                                     mensaje += "Archivo invalido debe ser un pdf.";
                                     bandera = true;
                                     }*/
                                    $("#folioFac").val("");
                                    if (valorRemision === "" || valorRemision === null) {
                                        mensaje += "Folio de Remision \n";
                                        bandera = true;
                                    }
                                    if (bandera) {
                                        alert(mensaje);
                                        return false;
                                    }
                                } else {
                                    /*let extension = valorArFactura.substring(valorArFactura.lastIndexOf("."));
                                     if(valorFactura === "" || valorFactura === null) {
                                     mensaje += "Folio de Factura \n";
                                     bandera = true;
                                     }
                                     if(valorArFactura === "" || valorArFactura === null){
                                     mensaje += "Archivo de factura \n"
                                     bandera = true;
                                     }
                                     if(valorArFactura != "" && extension != ".pdf"){
                                     mensaje += "Archivo invalido debe ser un pdf.";
                                     bandera = true;
                                     }*/
                                    $("#folioRemi").val("");
                                    if (valorFactura === "" || valorFactura === null) {
                                        mensaje += "Folio de Factura \n";
                                        bandera = true;
                                    }
                                    if (bandera) {
                                        alert(mensaje);
                                        return false;
                                    }
                                }
                            } else {
                                alert("Favor de seleccionar una Remision o Factura");
                                return false;
                            }

                        }


                        function tabular(e, obj)
                        {
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

                        function cambiaLoteCadu(elemento) {
                            var indice = elemento.selectedIndex;
                            document.getElementById('list_cadu').selectedIndex = indice;
                            document.getElementById('lot').value = document.getElementById('list_lote').value;
                            document.getElementById('cad').value = document.getElementById('list_cadu').value;
                        }


                        function checkKey(e, obj) {
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
                            document.getElementById('CodigoBarras').click();
                            return false;
                        }

                    </script>



                    <script>
                        var archivoNombreGeneral = "";
                        var archivoNombreNuevo = "";

                        function nuevoArchivo(tipo)
                        {
                            $("#escondido").val(tipo);
                            $("#btnModalNuevo").click();
                        }

                        function editar(valor, id)
                        {
                            archivoNombreGeneral = valor.replace('/', '');
                            $("#fichero_seleccionado2").text(archivoNombreGeneral);
                            $("#idCo").val(id);
                            $("#btnModal").click();
                        }

                        $("#archivoEditar").change(function () {
                            filename = this.files[0].name
                            $("#fichero_seleccionado2").text(filename);
                            archivoNombreNuevo = filename;
                        });

                        $("#nuevoArchivoS").change(function () {
                            filename = this.files[0].name
                            $("#fichero_seleccionado").text(filename);
                            archivoNombreNuevo = filename;
                        });

                        $("#btnCancel").click(function () {
                            $("#fichero_seleccionado").text("");
                            $("#fichero_seleccionado2").text("");
                        });
                        $("#btnCancel2").click(function () {
                            $("#fichero_seleccionado").text("");
                            $("#fichero_seleccionado2").text("");
                        });


                        function envio() {
                            if (archivoNombreGeneral != archivoNombreNuevo && archivoNombreNuevo != "") {

                                $("#edicionArchivo").submit();
                            } else {
                                alert("El archivo no puede ser el mismo ni estar vacio");
                            }
                        }

                        function envioNuevo() {
                            if (archivoNombreNuevo) {
                                if (archivoNombreNuevo.substring(archivoNombreNuevo.indexOf(".pdf")) === ".pdf") {
                                    $("#nuevoArchivo").submit();
                                } else {
                                    alert("Favor de subir un archivo tipo pdf");
                                }
                            } else {
                                alert("Debe seleccionar un archivo");
                            }

                        }

                        function validarOC() {
                            let fileSelectionCount = $('#tablaPintado >tbody >tr >td#imagenPDF').length;
                            if (fileSelectionCount > 0) {
                                alert("Debe subir todos los archivos correspondientes.");
                                return false;
                            } else {
                                if (confirm("¿Desea ingresar las siguientes OC.?")) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }

                        function confirmaCampo(idCampo) {
                            var valor = $("#" + idCampo).val();
                            var confirm = $("#confirm-" + idCampo).val();
                            if (valor !== confirm) {
                                $("#accion").prop("disabled", true);
                                document.getElementById("confirm-" + idCampo).style.border = "3px solid #a94442";
                            } else {
                                $("#confirm-" + idCampo).css("border-color", "#3c763d");
                                document.getElementById("confirm-" + idCampo).style.border = "1px solid #3c763d";
                                buttonEnable(true);
                            }
                        }

                        function buttonEnable() {
                            $("#accion").prop("disabled", false);
                            var fields = ['list_marca', 'cad', 'lot', 'TarimasC', 'CajasxTC', 'PzsxCC', 'TarimasI', 'CajasxTI', 'Resto'];
                            for (var i = 0; i < fields.length; i++) {
                                var idCampo = fields[i];
                                var enable = ($("#" + idCampo).val() === $("#confirm-" + idCampo).val()) && $("#" + idCampo).val();
                                if (!enable) {
                                    $("#accion").prop("disabled", true);
                                    break;
                                }
                            }
                            //            var enable= $("#").val() === $("#confirm-"+idCampo).val();

                        }
                        $('#loadingModal').modal('hide');
                    </script>
                    <style>

                        .iborrainputfile {
                            font-size: 14px;
                            font-weight: normal;
                            position: absolute;
                            margin-top: 36px;
                            margin-left: 42px;
                            border: 1px solid grey;
                            padding: 5px;
                            background: lightgrey;
                        }

                        .inputfile {
                            width: 0.1px;
                            height: 0.1px;
                            opacity: 0;
                            overflow: hidden;
                            position: absolute;
                            z-index: -1;
                        }
                    </style>
                    </html>
