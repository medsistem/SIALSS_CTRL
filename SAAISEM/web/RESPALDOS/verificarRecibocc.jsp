<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
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
    ConectionDB_Linux Lerma = new ConectionDB_Linux();
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
            <form action="verificarRecibocc.jsp" method="post">
                <br/>
                <div class="row">
                    <h3>Validación Recibo LERMA</h3>
                    <h4 class="col-sm-2">Elegir Remisión: </h4>
                    <div class="col-sm-9">
                        <select class="form-control" name="NoCompra" onchange="this.form.submit();">
                            <option value="">-- Proveedor -- Orden de Compra --</option>
                            <%                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT c.F_OrdCom, p.F_NomPro, c.F_FolRemi FROM tb_compratempcc c, tb_proveedor p WHERE c.F_Provee = p.F_ClaProve and c.F_Estado = '2' GROUP BY c.F_OrdCom, c.F_FolRemi");
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

                
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td>Id</td>
                                    <td>Remisión</td>
                                    <td>CLAVE</td>
                                    <td>Descripción</td>
                                    <td>Ori</td>
                                    <td>Lote</td>                      
                                    <td>Cantidad a Recibir</td>                      
                                    <td>Cantidad a Validar</td>                      
                                    <td>Costo U</td>                     
                                    <td>IVA</td>                       
                                    <td>Importe</td>
                                    <td>Código de Barras</td>
                                    <td>Caducidad</td>  
                                    <td>Marca</td>
                                    <td>Modificar</td>
                                    <td></td>
                                </tr>
                                <%
                                    int banBtn = 0,Marca=0;
                                    try {
                                        con.conectar();
                                        ResultSet DatosMarc = con.consulta("SELECT F_Marca FROM tb_compratempcc WHERE F_OrdCom='" + vOrden + "' GROUP BY F_Marca");
                                        while(DatosMarc.next()){
                                            ResultSet ConsulMarc = con.consulta("SELECT COUNT(F_ClaMar) FROM tb_marca WHERE F_ClaMar='"+DatosMarc.getString(1)+"';");
                                            if(ConsulMarc.next()){
                                                Marca = ConsulMarc.getInt(1);
                                            }
                                            if(Marca == 0){
                                                try{
                                                    Lerma.conectar();
                                                    ResultSet MarcLer = Lerma.consulta("SELECT * FROM tb_marca WHERE F_ClaMar='"+DatosMarc.getString(1)+"';");
                                                    if(MarcLer.next()){
                                                        con.insertar("INSERT INTO tb_marca VALUES('"+MarcLer.getString(1)+"','"+MarcLer.getString(2)+"')");
                                                    }
                                                }catch (Exception e) {
                                                     Logger.getLogger("verificarRecibocc.jsp").log(Level.SEVERE, null, e);
                                                } finally {
                                                    try {
                                                        Lerma.cierraConexion();                
                                                    } catch (Exception ex) {
                                                        Logger.getLogger("verificarRecibocc.jsp").log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        }
                                        ResultSet rset = con.consulta("SELECT C.F_Cb,C.F_ClaPro,M.F_DesPro,C.F_Lote,C.F_FecCad,C.F_Pz,F_IdCom, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_Obser, C.F_Origen, MAR.F_ClaMar, MAR.F_DesMar,C.F_CantSur FROM tb_compratempcc C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + vOrden + "' and F_FolRemi = '" + vRemi + "'  and F_Estado = '2'");
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
                                    <td class="id"><%=rset.getString(7)%></td>
                                    <td class="folio"><%=rset.getString("C.F_FolRemi")%></td>
                                    <td class="clave"><%=rset.getString(2)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <td><%=rset.getString("F_Origen")%></td>
                                    <td class="lote"><%=rset.getString(4)%></td>
                                    <td class="cantidad"><%=rset.getString(6)%></td>
                                    <td><%=rset.getString(16)%></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_Costo"))%></td>
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ImpTo"))%></td>          
                                    <td><%=formatterDecimal.format(rset.getDouble("C.F_ComTot"))%></td>
                                    <td><%=F_Cb%></td>
                                    <td class="caducidad">
                                        <%
                                            if (F_FecCad.equals("")) {
                                        %>
                                        <%=rset.getString("F_IdCom")%>
                                        <%
                                        } else {
                                        %><%=F_FecCad%>
                                      
                                        <%
                                            }
                                        %>
                                    </td>
                                    <td><%=F_Marca%></td>
                                    <td>
                                        <button class="btn btn-primary rowButton" data-toggle="modal" data-target="#ModiOc"><span class="glyphicon glyphicon-pencil" ></span></button>                                        
                                    </td>
                                    <td>
                                        <form action="ValidaReciboCC" method="post">
                                        <input type="hidden" value="<%=rset.getString("C.F_FolRemi")%>" name="folio" id="folio">
                                        <input type="hidden" value="<%=rset.getString(7)%>" name="idreg" id="idreg">
                                        <button class="btn btn-sm btn-info glyphicon glyphicon-ok" name="accion" id="accion" value="Eliminar"></button>
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
                                <tr>
                                    <td colspan="13">

                                    </td>
                                </tr>

                            </table>
                        </div>
                        <hr/>
                    </div>
               
                <div class="panel-footer">
                    <form action="ValidaReciboCC" method="post">
                        <input type="hidden" value="<%=vOrden%>" name="folio" id="folio">
                        <button class="btn btn-block btn-primary btn-lg glyphicon glyphicon-trash" name="accion" id="accion" value="Limpiar" onclick="return validaCompra();">&nbsp;Limpiar Datos</button>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- Modal -->
        <div id="ModiOc" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Envío de Factura</h4>
                    </div>                    
                    <form name="formEditOC" action="ValidaReciboCC" method="Post">
                        <div class="modal-body">
                            <div class="row">
                                <h4 class="col-sm-2">Folio:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="folio" id="Folio" type="text" value="" readonly required/>
                                </div>
                                <h4 class="col-sm-2">Id:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="idreg" id="Id" type="text" value="" readonly="" required=""/>
                                </div>
                            </div>                            
                            <div class="row">                                
                                <h4 class="col-sm-2">Clave:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Clave" id="Clave" type="text" value="" readonly="" required=""/>
                                </div>
                                <h4 class="col-sm-2">Lote:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Lote" id="Lote" type="text" value="" readonly="" required=""/>
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Cantidad Recibír:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Cantidad" id="Cantidad" type="text" value="" readonly="" required=""/>
                                </div>
                                <h4 class="col-sm-2">Cantidad Validar:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="CantV" id="CantV" type="text" value="" required="" data-behavior="only-num"/>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="Actualizar">Actualizar</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- Fin Modal -->


        <%@include file="jspf/piePagina.jspf" %>

        

        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script type="text/javascript">
            

                                function descripMarc() {
            <%
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT F_IdCom FROM tb_compratempcc C INNER JOIN tb_medica M  ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_marca MAR ON C.F_Marca = MAR.F_ClaMar  WHERE F_OrdCom='" + vOrden + "' and F_FolRemi = '" + vRemi + "'  and F_Estado = '2'");
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
        <script>
        $(".rowButton").click(function () {
            var $row = $(this).closest("tr");    // Find the row
            var $folio = $row.find("td.folio").text(); // Find the text   
            var $id = $row.find("td.id").text(); // Find the text   
            var $cantidad = $row.find("td.cantidad").text(); // Find the text 
            var $clave = $row.find("td.clave").text(); // Find the text 
            var $lote = $row.find("td.lote").text(); // Find the text 
            var $caducidad = $row.find("td.caducidad").text(); // Find the text 

            $("#Folio").val($folio);
            $("#Id").val($id);
            $("#Cantidad").val($cantidad);
            $("#Clave").val($clave);
            $("#Lote").val($lote);
            $("#Caducidad1").val($caducidad);

        });
        
        $("[data-behavior~=only-alphanum]").alphanum({
            allowSpace: false,
            allowOtherCharSets: false,
            allow: '.'
        });
        $("[data-behavior~=only-alphanum-caps]").alphanum({
            allowSpace: false,
            allowOtherCharSets: false,
            forceUpper: true
        });
        $("[data-behavior~=only-alphanum-caps-15]").alphanum({
            allowSpace: false,
            allowOtherCharSets: false,
            forceUpper: true,
            maxLength: 15
        });
        $("[data-behavior~=only-alphanum-white]").alphanum({
            allow: '.',
            disallow: "'",
            allowSpace: true
        });
        $("[data-behavior~=only-num]").numeric({
            allowMinus: false,
            allowThouSep: false
        });

        $("[data-behavior~=only-alpha]").alphanum({
            allowNumeric: false,
            allowSpace: false,
            allowOtherCharSets: true
        });
        
        </script>
    </body>



</html>
