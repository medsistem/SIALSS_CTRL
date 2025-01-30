

<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>
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
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String folio_gnk = "", fecha = "", folio_remi = "", orden = "", provee = "", recib = "", entrega = "", origen = "", coincide = "", observaciones = "", clave = "", descrip = "", Cb = "", Marca = "", Codbar2 = "", PresPro = "", proye = "";
    int Cuenta = 0;
    try {
        folio_gnk = (String) session.getAttribute("folio");
        fecha = (String) session.getAttribute("fecha");
        folio_remi = (String) session.getAttribute("folio_remi");
        orden = (String) session.getAttribute("folio_remi");
        provee = (String) session.getAttribute("provee");
        recib = (String) session.getAttribute("recib");
        origen = (String) session.getAttribute("origen");
        Codbar2 = (String) session.getAttribute("codbar2");
        clave = (String) session.getAttribute("clave");
        descrip = (String) session.getAttribute("descrip");
        Cb = (String) session.getAttribute("cb");
        Marca = (String) session.getAttribute("Marca");
        PresPro = (String) session.getAttribute("PresPro");
        Cuenta = Integer.parseInt((String) session.getAttribute("cuenta"));

    } catch (Exception e) {
    }

    if (folio_gnk == null || folio_gnk.equals("")) {
        try {
            con.conectar();
            ResultSet rset = con.consulta("select F_IndCom from tb_indice");
            while (rset.next()) {
                folio_gnk = Integer.toString(Integer.parseInt(rset.getString(1)));
            }

            con.cierraConexion();
        } catch (Exception e) {
        }
        if (folio_gnk == null || folio_gnk.equals("null")) {
            folio_gnk = "1";
        }
        fecha = "";
        folio_remi = "";
        orden = "";
        provee = "";
        recib = "";
        entrega = "";
        origen = "";
        coincide = "";
        observaciones = "";
        clave = "";
        descrip = "";
        Marca = "";
        Codbar2 = "";
        PresPro = "";
    }
   
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
       
        <link href="css/datepicker3.css" rel="stylesheet">
        <link href="css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" />
        
        <!---->
        <link href="css/bootstrap.css" rel="stylesheet">
        
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
   
        <link href="css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" />
        <link href="css/jquery.dataTables.css" rel="stylesheet" type="text/css" />
        <link href="css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
        
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        
        <script src="js/jquery-1.12.4.js" type="text/javascript"></script>
        <script src="js/jquery-ui.js" type="text/javascript"></script>      
        <script src="js/jquery-2.2.4.min.js"></script>
        
        <script src="js/bootstrap.js"></script>
      
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/utils/formato_numero.js" type="text/javascript"></script>
        
        <title>Ingreso de Entradas Proyecto</title>
    </head>
    <body onLoad="foco();
            mueveReloj();">
        <div class="container">
            
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <div class="panel panel-primary">

                <div class="panel-heading">
                    <h3 class="panel-title">Captura de Insumo Resguardo</h3>
                </div>

                <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="Altas">
                    <div class="panel-body">
                        <div class="row">
                            <label for="folio" class="col-sm-2 control-label">No. Folio CEDIS CENTRAL</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="folio" name="folio" placeholder="Folio" readonly value="<%=folio_gnk%>"/>
                            </div>
                            <label for="fecha" class="col-sm-1 control-label">Fecha</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="fecha" name="fecha" placeholder="Fecha" readonly value="<%=df3.format(new java.util.Date())%>">
                            </div>
                            <label for="hora" class="col-sm-1 control-label">Hora</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="hora" name="hora" placeholder="Hora" readonly value="<%=df3.format(new java.util.Date())%>">
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <label for="fol_rem" class="col-sm-2 control-label">Folio Remisión</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" id="folio_remi" name="folio_remi" placeholder="Folio Remisión" maxlength="50" max="50" minlength="5" oncopy="return false" onpaste="return false"  onKeyPress="return tabular(event, this)" value="<%=folio_remi%>" />
                            </div>

                        </div>
                        <br/>
                        <div class="row">
                            <label for="prov" class="col-sm-2 control-label">Proveedor</label>
                            <input type="text" class="hidden" id="provee" name="provee" placeholder="Proveedor" readonly onKeyPress="return tabular(event, this)" value="<%=provee%>" />                             
                            <div class="col-sm-3">
                                <select class="form-control" name="list_provee" onKeyPress="return tabular(event, this)" id="list_provee" onchange="proveedor();">
                                    <option value="">Proveedor</option>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = con.consulta("SELECT F_ClaProve,F_NomPro FROM tb_proveedor group by F_NomPro order by F_NomPro ");
                                            while (rset.next()) {
                                    %>
                                    <option value="<%=rset.getString("F_ClaProve")%>"
                                            <%
                                                if (rset.getString("F_ClaProve").equals(provee)) {
                                                    out.println("selected");
                                                }
                                            %>
                                            ><%=rset.getString("F_NomPro")%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                    %>
                                </select>

                            </div>
                            <div class="col-sm-1">   
                                <a class="btn btn-block btn-info glyphicon glyphicon-cloud-upload" href="catalogo.jsp" target="_blank"></a>
                                <button class="btn btn-block btn-primary glyphicon glyphicon-refresh" type = "submit" value = "refresh" name = "accion" ></button>
                            </div>


                            <label for="recib" class="col-sm-1 control-label">Recibido por</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="recib" name="recib" placeholder="Recibe" onKeyPress="return tabular(event, this)" value = "<%=usua%>" readonly>
                            </div>
                        </div>

                    </div>
                    <div class="panel-footer">
                        <div class="row">
                          
                            <%
                                if (Cuenta == 0) {
                            %>
                            <label for="clave" class="col-sm-1 control-label">CLAVE</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" id="clave" name="clave" placeholder="CLAVE" minlength="4" min="4" maxlength="30" max="30" onKeyPress="return tabular(event, this)" oncopy="return false" onpaste="return false" >
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-block btn-primary" type = "submit" value ="claveProy" name = "accion" >Busqueda CLAVE</button>
                            </div>
                        </div>
                        <br/>
                        <div class="row">

                            <label for="descr" class="col-sm-1 control-label">Descripción</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="descr" name="descr" placeholder="Descripción" onKeyPress="return tabular(event, this)">
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-block btn-primary"  type = "submit" value = "descripcionProy" name = "accion" >Busqueda Descripción</button>
                            </div>
                        </div>     
                        <hr/>
                        <div class="row">
                            <label for="clave1" class="col-sm-1 control-label">CLAVE</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="clave1" name="clave1" placeholder="CLAVE" value="<%=clave%>" readonly onKeyPress="return tabular(event, this)"/>
                            </div>

                            <label for="descr1" class="col-sm-1 control-label">Presentación</label>
                            <div class="col-sm-8">
                                <Input class="form-control" name="Presentaci" id="Presentaci" readonly onKeyPress="return tabular(event, this)" value="<%=PresPro%>">
                            </div>
                        </div> 
                        <br/>  
                        <div class="row">

                            <div class="row">
                                <label for="descr1" class="col-sm-1 control-label">Descripción</label>
                                <div class="col-sm-8">
                                    <textarea   class="form-control" name="descripci" id="descripci" readonly onKeyPress="return tabular(event, this)"><%=descrip%></textarea>
                                </div>
                            </div>
                        </div>        
                        <hr/>

                        <div class="panel panel-body" >
                            <div class="row">
                                <div class="col-sm-6">
                                    <label for="cb" class=" control-label">Código de Barras</label>
                                    <input type="text"  value="<%=Codbar2%>" class="form-control" id="cb" name="cb" oncopy="return false" onpaste="return false" placeholder="C. B." onKeyPress="return tabular(event, this)" style="-webkit-text-security: disc;"/>                                     
                                </div>
                                <div class="col-sm-6">
                                    <label for="cb" class=" control-label">Confirmar Código de Barras</label>
                                    <input type="text"  value="<%=Codbar2%>" class="form-control" id="confirm-cb" placeholder="C. B." oncopy="return false" onpaste="return false" onKeyPress="return tabular(event, this)" onfocusout="confirmaCampo('cb')" onpaste="return false"/>                                     
                                </div>
                                <div class="col-sm-3">
                                    <button class="btn btn-block btn-primary glyphicon glyphicon-barcode" type = "submit" value = "CodBarRes" name = "accion" ></button>
                                </div>
                            </div>
                            <br/>
                            <div class="row">                           
                                <div class="col-md-6">
                                    <strong>Marca</strong>
                                    <input type="text" class="hidden" id="Marca" name="Marca" readonly="true" placeholder="Marca" onKeyPress="return tabular(event, this)" value="<%=Marca%>" />
                                    <select class="form-control" name="list_marca" onKeyPress="return tabular(event, this)" id="list_marca" onchange="marca();">
                                        <option value="">Marca</option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca group by F_DesMar order by F_DesMar");
                                                while (rset.next()) {
                                        %>
                                        <option value="<%=rset.getString("F_ClaMar")%>"
                                                <%
                                                    if (rset.getString("F_ClaMar").equals(Marca)) {
                                                        out.println("selected");
                                                    }
                                                %>
                                                ><%=rset.getString("F_DesMar")%></option>
                                        <%

                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                e.getMessage();
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <strong>Confirmar Marca</strong>
                                    <select class="form-control"  onKeyPress="return tabular(event, this)" id="confirm-list_marca" onfocusout="confirmaCampo('list_marca')">
                                        <option value="">Marca</option>
                                        <%
                                            try {
                                                con.conectar();

                                                ResultSet rset3 = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_marca where F_DesMar <> '' order by F_DesMar");
                                                while (rset3.next()) {
                                        %>
                                        <option value="<%=rset3.getString("F_ClaMar")%>"
                                                <%
                                                    if (rset3.getString("F_ClaMar").equals(Marca)) {
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
                                <div class="col-sm-3">
                                    <button class="btn btn-block btn-primary glyphicon glyphicon-refresh" type = "submit" value = "refresh" name = "accion" ></button>
                                </div>
                                <div class="col-sm-3">
                                    <a href="marcas.jsp" class="btn btn-block btn-info glyphicon glyphicon-cloud-upload" target="_blank"></a>
                                
                            </div>
</div>
                                    <hr/>                           

                            <div class="row">
                                <div class="col-sm-6">
                                    <label for="Lote" class="control-label">Lote</label>
                                    <input type="text" class="form-control" id="Lote" name="Lote" placeholder="Lote" oncopy="return false" onpaste="return false" onKeyPress="return tabular(event, this)" style="-webkit-text-security: disc;" oncopy="return false" onpaste="return false" />
                                </div>
                                <div class="col-sm-6">
                                    <label for="Lote" class="control-label">Confirmar Lote</label>
                                    <input type="text" class="form-control" id="confirm-Lote" placeholder="Lote" onKeyPress="return tabular(event, this)" onfocusout="confirmaCampo('Lote')" oncopy="return false" onpaste="return false"/>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-md-6">                                    
                                    <label for="cad" class=" control-label">Caducidad</label>
                                    <input type="date" data-date-format="dd/mm/yyyy"  class="form-control" id="cad" name="cad" oncopy="return false" onpaste="return false" onKeyPress="return handleEnter(this, even)"  maxlength="10" style="-webkit-text-security: disc;" autocomplete="off" placeholder="dd/mm/aaaa"/>                                

                                </div>
                                <div class="col-md-6">
                                    <label for="confirma-cad" class="control-label">Confirmar Caducidad</label>

                                    <input data-date-format="dd/mm/yyyy" type="date" class="form-control" id="confirm-cad" name="confirm-cad" oncopy="return false" onpaste="return false"  placeholder="dd/mm/aaaa" onKeyPress="return handleEnter(this, even)" maxlength="10" onfocusout="confirmaCampo('cad')" />                                

                                </div>
                            </div>
                            <% } %>
                        <hr/>
                        <div class="row">

                            <div class="col-sm-3">
                                <strong>Proyecto:</strong>
                                <select class="form-control" name="F_Proyectos" id="F_Proyectos" onchange="validarProyecto(value)">
                                    <option value="">Seleccionar Proyecto</option>
                                    <%
                                        try {
                                            con.conectar();

                                            ResultSet rset3 = con.consulta("SELECT * FROM tb_proyectos WHERE f_Id IN (7);");
                                            while (rset3.next()) {
                                    %>

                                    <option value="<%=rset3.getString(1)%>"><%=rset3.getString(2)%></option>
                                    <%
                                                proye = rset3.getString(1);
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger("captura.jsp").log(Level.SEVERE, null, e);
                                        } finally {
                                            try {
                                                con.cierraConexion();
                                            } catch (Exception ex) {
                                                Logger.getLogger("captura.jsp").log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-sm-2">
                                <strong>Costo:</strong>
                                <input name="F_Costo" id="F_Costo" type="text" class="form-control" />
                            </div>

                            <div class="col-sm-3">
                                <strong>Origen:</strong>
                                <select class="form-control" name="F_Origen" id="F_Origen">
                                    <%
                                        try {
                                            con.conectar();

                                            ResultSet rset3 = con.consulta("select F_ClaOri, F_DesOri from tb_origen WHERE F_ClaOri IN ('27');");
                                            while (rset3.next()) {
                                    %>
                                    <option value="<%=rset3.getString("F_ClaOri")%>"><%=rset3.getString("F_DesOri")%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                                </select>
                            </div>        
                        </div>

                        <br/>    
                        <hr/>
                        <h5><strong>CANTIDADES</strong></h5>
                        <div class="panel panel-info"> 
                            <h5><strong>Tarimas Completas</strong></h5>
                            <div class="row form-inline" style="text-align: right;">
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="tarimas">Tarimas</label>
                                        <input type="text" class="form-control" id="TarimasC" name="TarimasC" placeholder="0" onKeyPress="return justNumbers(event);
                                                return handleEnter(event);" onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;"  readonly="" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="tarimas">Cajas</label>
                                        <input type="text" class="form-control" id="CajasxTC" name="CajasxTC" placeholder="0" onKeyPress="return justNumbers(event); " oncopy="return false" onpaste="return false" 
                                               onkeyup="totalPiezas()" onclick="" style="-webkit-text-security: disc;">
                                    </div>
                                </div>
                                  <input type="hidden" class="form-control" id="PzsxCC" name="PzsxCC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick=""
                                               style="-webkit-text-security: disc;">
                              <!--  <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="PzsxCC">Piezas x Caja</label>
                                        <input type="text" class="form-control" id="PzsxCC" name="PzsxCC" placeholder="0" onKeyPress="return justNumbers(event);" onkeyup="totalPiezas()" onclick=""
                                               style="-webkit-text-security: disc;">
                                    </div>-->
                            </div>


                            <hr/>

                            <div class="row">
                                <label for="Observaciones" class="col-sm-2 control-label">Observaciones</label>
                                <div class="col-sm-10">
                                    <textarea class="form-control" name="Observaciones" id="Observaciones" rows="3" onclick="" onKeyPress="return handleEnter(event);"></textarea>
                                </div>
                            </div>
                            <hr/>
                            <h4><strong>Totales</strong></h4>
                            <br/>
                            <div class="row">

                                <label for="Tarimas" class="col-sm-1 control-label">Tarimas</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control" id="Tarimas" name="Tarimas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);
                                            return handleEnter(even);" onkeyup="totalPiezas();" onclick="" />
                                </div>
                                <label for="TCajas" class="col-sm-1 control-label">Total Cajas</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control" id="TCajas" name="TCajas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" 
                                           onkeyup="totalPiezas();" onclick=""/>
                                </div>
                                <input type="hidden" class="form-control" id="Piezas" name="Piezas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" 
                                           onkeyup="totalPiezas();" onclick="" />
                                <!--
                                <label for="Resto" class="col-sm-1 control-label">Piezas</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control" id="Piezas" name="Piezas" placeholder="0" readonly="" onKeyPress="return justNumbers(event);" 
                                           onkeyup="totalPiezas();" onclick="" />
                                </div>
                                -->

                            </div>

                            <br/>
                            <div class="row">
                                <div class="col-sm-12">
                                    <!-- En duda -->
                                    <div id="imgCarga" class="text-center" style="display: none">
                                        <img src="imagenes/ajax-loader-1.gif" />
                                    </div>
                                  
                                    <button class="btn btn-block btn-primary" type="submit" name="accion" id="accion" value="capturarProyecto" onclick="return (validaCapturaVacios());" >Capturar</button>
                                   
                                    <!-- En duda -->
                                </div>
                            </div>

                        </div>

                    </div>
                </form>      
                <div class="table-responsive">
                    <table class="table table-bordered table-striped">
                        <tr>
                            <td>Remisión</td>
                            <td><a name="ancla"></a>Código de Barras</td>
                            <td>Clave</td>
                            <td>Descripción</td>
                            <td>Origen</td>                       
                            <td>Lote</td>
                            <td>Caducidad</td>                        
                            <td>Cantidad</td>                      
                            <td>Costo U</td>                     
                            <td>IVA</td>                       
                            <td>Importe</td>
                            <td></td>
                        </tr>
                        <%
                            int banCompra = 0;
                            String obser = "";
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Origen FROM tb_compratemp C INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro WHERE F_OrdCom='" + orden + "' and F_Estado = '1'");
                                while (rset.next()) {
                                    banCompra = 1;

                        %>
                        <tr>
                            <td><%=rset.getString("C.F_FolRemi")%></td>
                            <td><%=rset.getString(1)%></td>
                            <td><%=rset.getString(2)%></td>
                            <td><%=rset.getString(3)%></td>
                            <td><%=rset.getString("F_Origen")%></td>
                            <td><%=rset.getString(4)%></td>
                            <td><%=df3.format(df2.parse(rset.getString(5)))%></td>
                            <td><%=formatter.format(rset.getDouble(6))%></td>           
                            <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                            <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                            <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>              
                            <td>

                                <!--Para eliminar registro-->
                                <form method="get" action="Modificaciones">
                                    <input name="id" type="text" style="" class="hidden" value="<%=rset.getString(7)%>" />
                                    <button class="btn btn-info" onclick="return confirm('¿Seguro de que desea eliminar?');" name="accion" value="eliminarProy"><span class="glyphicon glyphicon-remove"></span>
                                    </button>

                                </form>
                            </td>
                        </tr>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        %>


                    </table>


                </div>

                <%
                    if (banCompra == 1) {
                %>
                <div class="col-lg-6">
                    <form action="Nuevo" method="post">
                        <input name="fol_gnkl" type="text" style="" class="hidden" value="<%=orden%>" />
                        <button  value="EliminarProy" name="accion" class="btn btn-info btn-block" onclick="return confirm('Seguro que desea cancelar la compra?');">Cancelar Compra</button>
                    </form>
                </div>
                <div class="col-lg-6">
                    <form action="Modificaciones" method="post">
                        <input name="fol_gnkl" type="text" style="" class="hidden" value="<%=orden%>" />
                        <button  value="verificarCompraManualProy" name="accion" class="btn btn-primary btn-block" onclick="return confirm('¿Seguro que desea verificar la compra?');">Ingresar Compra</button>
                    </form>
                </div>
                <%
                    }
                %>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
    </body>

      <script src="js/jquery-1.9.1.js"></script>
                <script src="js/bootstrap.js"></script>
                <script src="js/jquery-ui-1.10.3.custom.js"></script>
                <script src="js/bootstrap-datepicker.js"></script>
                <script src="js/jquery.maskMoney.js" type="text/javascript"></script>


    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->

   

    <script>
                            $(document).ready(function () {
                                $("#F_Costo").maskMoney();
                               // $("#cb").numeric();
                            });

                            $('#formulario1').submit(function () {
                                document.getElementById('imgCarga').style.display = "block";
                                $('#accion').css('display', 'none');
                            });

                            let today = new Date();
                            today.setDate(today.getDate() + 3);
                            today = today.toISOString().split('T')[0];
                            document.getElementsByName("cad")[0].setAttribute('min', today);
                            document.getElementsByName("confirm-cad")[0].setAttribute('min', today);

                            var formatNumber = {
                                separador: ",", // separador para los miles
                                sepDecimal: '.', // separador para los decimales
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

                            function validarProyecto(value) {

                                document.getElementById('F_Origen').value = '';

                                if (value !== "5" && value !== "7") {
                                    $("#F_Origen option[value=" + '14' + "]").show();
                                    $("#F_Origen option[value=" + '15' + "]").show();
                                    $("#F_Origen option[value=" + '16' + "]").show();
                                    $("#F_Origen option[value=" + '17' + "]").show();
                                    $("#F_Origen option[value=" + '18' + "]").show();
                                    $("#F_Origen option[value=" + '19' + "]").show();
                                    $("#F_Origen option[value=" + '20' + "]").show();
                                    $("#F_Origen option[value=" + '21' + "]").show();
                                    $("#F_Origen option[value=" + '22' + "]").show();
                                    $("#F_Origen option[value=" + '23' + "]").show();
                                    $("#F_Origen option[value=" + '24' + "]").show();
                                    $("#F_Origen option[value=" + '25' + "]").hide();
                                    $("#F_Origen option[value=" + '26' + "]").hide();
                                    $("#F_Origen option[value=" + '27' + "]").hide();

                                } else if (value === "5") {
                                    document.getElementById('F_Origen').value = '25';
                                    $("#F_Origen option[value=" + '14' + "]").hide();
                                    $("#F_Origen option[value=" + '15' + "]").hide();
                                    $("#F_Origen option[value=" + '16' + "]").hide();
                                    $("#F_Origen option[value=" + '17' + "]").hide();
                                    $("#F_Origen option[value=" + '18' + "]").hide();
                                    $("#F_Origen option[value=" + '19' + "]").hide();
                                    $("#F_Origen option[value=" + '20' + "]").hide();
                                    $("#F_Origen option[value=" + '21' + "]").hide();
                                    $("#F_Origen option[value=" + '22' + "]").hide();
                                    $("#F_Origen option[value=" + '23' + "]").hide();
                                    $("#F_Origen option[value=" + '24' + "]").hide();
                                    $("#F_Origen option[value=" + '25' + "]").show();
                                    $("#F_Origen option[value=" + '26' + "]").show();
                                    $("#F_Origen option[value=" + '27' + "]").hide();

                                } else if (value === "7") {
                                    document.getElementById('F_Origen').value = '27';
                                    $("#F_Origen option[value=" + '14' + "]").hide();
                                    $("#F_Origen option[value=" + '15' + "]").hide();
                                    $("#F_Origen option[value=" + '16' + "]").hide();
                                    $("#F_Origen option[value=" + '17' + "]").hide();
                                    $("#F_Origen option[value=" + '18' + "]").hide();
                                    $("#F_Origen option[value=" + '19' + "]").hide();
                                    $("#F_Origen option[value=" + '20' + "]").hide();
                                    $("#F_Origen option[value=" + '21' + "]").hide();
                                    $("#F_Origen option[value=" + '22' + "]").hide();
                                    $("#F_Origen option[value=" + '23' + "]").hide();
                                    $("#F_Origen option[value=" + '24' + "]").hide();
                                    $("#F_Origen option[value=" + '25' + "]").hide();
                                    $("#F_Origen option[value=" + '26' + "]").hide();
                                    $("#F_Origen option[value=" + '27' + "]").show();

                                }
                            }
                            ;


                            function totalPiezas() {
                                
                                var TarimasC = document.getElementById('TarimasC').value;
                                var CajasxTC = document.getElementById('CajasxTC').value;
                                var PzsxCC = document.getElementById('PzsxCC').value;


                                if (CajasxTC === "") {
                                    CajasxTC = 1;
                                }

                                document.getElementById('PzsxCC').value = formatNumber.new(CajasxTC);
                                document.getElementById('TarimasC').value = formatNumber.new(CajasxTC);
                                TarimasI = 0;
                                CajasxTI = 0;
                                Resto = 0;
                                
                                var totalCajas = parseInt(CajasxTC);
                                var totalTarimas = parseInt(TarimasC);
                                var totalPiezas = parseInt(totalCajas);
                                
                                document.getElementById('TCajas').value = formatNumber.new(totalCajas);
                                document.getElementById('Tarimas').value = formatNumber.new(totalCajas);
                                document.getElementById('Piezas').value = formatNumber.new(totalCajas);
                                document.getElementById('TCajas').value = formatNumber.new(totalCajas);

                            }

 

                            function cambiaLoteCadu(elemento) {
                                var indice = elemento.selectedIndex;
                                document.getElementById('list_cadu').selectedIndex = indice;
                                document.getElementById('list_fabri').selectedIndex = indice;
                                document.getElementById('cdd').value = document.getElementById('list_cadu').value;
                                document.getElementById('fdd').value = document.getElementById('list_fabri').value;
                                document.getElementById('TarimasC').focus();
                            }

                             <%
                                    /* try {
                                         con.conectar();
                                         ResultSet rset = con.consulta("SELECT F_DesPro FROM tb_medica");
                                         while (rset.next()) {
                                             out.println("\'" + rset.getString("F_DesPro") + "\',");
                                         }
                                         con.cierraConexion();
                                     } catch (Exception e) {
                                     }*/
       
                                     try {
                                         con.conectar();
                                         ResultSet rset = con.consulta("SELECT F_NomPro  FROM tb_proveedor");
                                         while (rset.next()) {
                                             out.println("\'" + rset.getString("F_NomPro") + "\',");
                                         }
                                         con.cierraConexion();
                                     } catch (Exception e) {
                                     }
        %>
                          
                            function ubi() {
                                var ubi = document.formulario1.ubica.value;
                                document.formulario1.ubicacion.value = ubi;
                            }
                            function proveedor() {
                                var proveedor = document.formulario1.list_provee.value;
                                document.formulario1.provee.value = proveedor;
                                document.formulario1.codigo.focus();
                            }
                            function marca() {
                                var marca = document.formulario1.list_marca.value;
                                document.formulario1.Marca.value = marca;
                                document.formulario1.Lote.focus();
                            }
                            function clave() {
                                var clave = document.formulario1.list_clave.value;
                                document.formulario1.clave1.value = clave;
                            }
                            function lotes() {
                                var lote = document.formulario1.list_lote.value;
                                document.formulario1.Lote.value = lote;
                            }
                            function cadu() {
                                var cadu = document.formulario1.list_cadu.value;
                                document.formulario1.cdd.value = cadu;
                            }
                            function fabri() {
                                var fabri = document.formulario1.list_fabri.value;
                                document.formulario1.fdd.value = fabri;
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
                                document.formulario1.folio_remi.focus();
                                var provee = document.formulario1.provee.value;
                                if (provee !== "") {
                                    document.formulario1.codigo.focus();
                                    window.scrollTo(0, 380);
                                }
                                if (document.getElementById('clave1').value !== "") {
                                    document.getElementById('list_marca').focus();
                                    window.scrollTo(0, 380);
                                }
                             <%
                               try {
                                 if (sesion.getAttribute("CBInex").equals("1") && Cuenta == 0) {
                            %>
                                if (document.formulario1.cb.value === "") {
                                    document.formulario1.cb.value = ('<%=(String) sesion.getAttribute("cb")%>');
                                }
                                if (provee !== "") {

                                    document.formulario1.clave.focus();
                                    window.scrollTo(0, 380);
                                }

                                if (document.getElementById('clave1').value !== "") {
                                    document.getElementById('list_marca').focus();
                                    window.scrollTo(0, 380);
                                }
        <%
                }
            } catch (Exception e) {

            }
        %>
                            }
                            //valida campos
                            function validaCompra() {
                                var RegExPattern = /^\d{1,2}\/\d{1,2}\/\d{2,4}$/;
                                var folio_remi = document.formulario1.folio_remi.value;
                                var orden = document.formulario1.orden.value;
                                var provee = document.formulario1.provee.value;
                                var recib = document.formulario1.recib.value;

                                if (folio_remi === "" || orden === "" || provee === "" || recib === "") {
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
                            function putDays()
                            {
                                var dayv = document.formulario1.cdd.value;
                                if (dayv >= 0 && dayv <= 31)
                                {
                                    var day = document.formulario1.cdd.value.length + 1;
                                    if (day <= 2)
                                    {
                                        document.formulario1.cdd.focus()
                                    } else
                                    {
                                        document.formulario1.cmm.focus()
                                    }
                                } else
                                {
                                    alert("DIA Fuera de Rango: " + dayv);
                                    document.formulario1.cdd.value = "";
                                    document.formulario1.cdd.focus();
                                }
                            }
                            function putMonthss() {
                                var month = document.formulario1.cmm.value.length + 1;
                                var monthv = document.formulario1.cmm.value;
                                if (monthv >= 0 && monthv <= 12)
                                {
                                    if (month <= 2)
                                    {
                                        document.formulario1.cmm.focus()
                                    } else {
                                        document.formulario1.caa.focus()
                                    }
                                } else {
                                    alert("MES Fuera de Rango: " + monthv);
                                    document.formulario1.cmm.value = "";
                                    document.formulario1.cmm.focus();
                                }
                            }
                            function putYearss() {
                                var year = document.formulario1.caa.value.length + 1;
                                var yearv = document.formulario1.caa.value;
                                if (year <= 4) {
                                    document.formulario1.caa.focus()
                                } 
                                if (year == 5) {
                                    var dtFechaActual = new Date();
                                    var sumarDias = parseInt(276);
                                    dtFechaActual.setDate(dtFechaActual.getDate() + sumarDias);
                                    var d = new Date();
                                    var day = document.formulario1.cdd.value;
                                    var month = document.formulario1.cmm.value;
                                    var years = document.formulario1.caa.value;
                                    var cadu = years + "-" + month + "-" + day;

                                    if (Date.parse(dtFechaActual) > Date.parse(cadu)) {
                                        alert("La fecha de caducidad no puede ser menor a 9 meses próximos");
                                        document.formulario1.cdd.value = "";
                                        document.formulario1.cmm.value = "";
                                        document.formulario1.caa.value = "";
                                        document.formulario1.cdd.focus();
                                        return false;
                                    }
                                 
                                }
                            }

                            function validaCapturaVacios() {
                                var missinginfo = "";
                                
                                if ($("#folio_remi").val() == "") {
                                    missinginfo += "\n El campo Folio Remisión no debe de estar vacío";
                                }

                                if ($("#provee").val() == "") {
                                    missinginfo += "\n El campo Proveedor no debe de estar vacío";
                                }
                                if ($("#clave1").val() == "") {
                                    missinginfo += "\n El campo Clave no debe de estar vacío";
                                }
                                if ($("#cb").val() == "") {
                                    missinginfo += "\n El campo Código Barra no debe de estar vacío";
                                }
                                if ($("#Marca").val() == "") {
                                    missinginfo += "\n El campo Marca no debe de estar vacío";
                                }
                                if ($("#F_Costo").val() == "") {
                                    missinginfo += "\n El campo Costo no debe de estar vacío";
                                }
                                if ($("#Lote").val() == "") {
                                    missinginfo += "\n El campo Lote no debe de estar vacío";
                                }
                                if ($("#cad").val() == "") {
                                    missinginfo += "\n El campo Caducidad no debe de estar vacío";
                                }
                                if ($("#F_Origen").val() == "") {
                                    missinginfo += "\n El campo Origen no debe de estar vacío";
                                }

                                if (missinginfo != "") {
                                    missinginfo = "\n TE HA FALTADO INTRODUCIR LOS SIGUIENTES DATOS PARA ENVIAR PETICIÓN DE SOPORTE:\n" + missinginfo + "\n\n ¡INGRESA LOS DATOS FALTANTES Y TRATA OTRA VEZ!\n";
                                    alert(missinginfo);

                                    return false;
                                } else {
                                    if (parseInt(caja) === 0) {
                                        missinginfo = "\n El total de piezas no puede ser \'0\'";
                                        alert(missinginfo);
                                        return false;
                                    }
                                    return true;
                                    
                                }
                            }
                            
                            
                            function validaCapturaVacioscb() {
                               
                                var missinginfo = "";
                                if ($("#folio_remi").val() == "") {
                                    missinginfo += "\n El campo Folio Remisión no debe de estar vacío";
                                }

                                if ($("#provee").val() == "") {
                                    missinginfo += "\n El campo Proveedor no debe de estar vacío";
                                }
                                if ($("#clave1").val() == "") {
                                    missinginfo += "\n El campo Clave no debe de estar vacío";
                                }
                                if ($("#cb").val() === "") {
                                    missinginfo += "\n El campo Código Barra no debe de estar vacío";
                                }
                                if ($("#Marca").val() == "") {
                                    missinginfo += "\n El campo Marca no debe de estar vacío";
                                }
                                if ($("#Lote").val() == "") {
                                    missinginfo += "\n El campo Lote no debe de estar vacío";
                                }
                                if ($("#cdd").val() == "") {
                                    missinginfo += "\n El campo Caducidad no debe de estar vacío";
                                }
                                if ($("#fdd").val() == "") {
                                    missinginfo += "\n El campo Fabricación no debe de estar vacío";
                                }
                             

                                if (missinginfo != "") {
                                    missinginfo = "\n TE HA FALTADO INTRODUCIR LOS SIGUIENTES DATOS PARA ENVIAR PETICIÓN DE SOPORTE:\n" + missinginfo + "\n\n ¡INGRESA LOS DATOS FALTANTES Y TRATA OTRA VEZ!\n";
                                    alert(missinginfo);

                                    return false;
                                } else {
                                    if (parseInt(caja) === 0) {
                                        missinginfo = "\n El total de piezas no puede ser \'0\'";
                                        alert(missinginfo);
                                        return false;
                                    }
                                    return true;
                                }
                            }


                            function mueveReloj() {
                                momentoActual = new Date()
                                hora = ((momentoActual.getHours() < 10 ? '0' : '') + momentoActual.getHours())
                                minuto = ((momentoActual.getMinutes() < 10 ? '0' : '') + momentoActual.getMinutes())
                                segundo = ((momentoActual.getSeconds() < 10 ? '0' : '') + momentoActual.getSeconds())

                                horaImprimible = hora + " : " + minuto + " : " + segundo

                                document.formulario1.hora.value = horaImprimible

                                setTimeout("mueveReloj()", 1000)
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
                                var fields = ['list_marca', 'cb', 'Lote', 'cad'];
                                for (var i = 0; i < fields.length; i++) {
                                    var idCampo = fields[i];
                                    var enable = ($("#" + idCampo).val() === $("#confirm-" + idCampo).val()) && $("#" + idCampo).val();
                                    if (!enable) {
                                        $("#accion").prop("disabled", true);
                                        break;
                                    }
                                }
                            }
                           

                           

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