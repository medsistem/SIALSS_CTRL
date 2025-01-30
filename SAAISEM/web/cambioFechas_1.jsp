<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
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

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("fecha");
        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
    }
    String fecha_ini="",fecha_fin="",folio1="",folio2="",radio="";
    try {
        fecha_ini = request.getParameter("fecha_ini");        
        fecha_fin = request.getParameter("fecha_fin");
        folio1 = request.getParameter("folio1");        
        folio2 = request.getParameter("folio2");
        radio = request.getParameter("radio");        
    } catch (Exception e) {

    }
    if(fecha_ini==null){
        fecha_ini="";
    }
    if(fecha_fin==null){
        fecha_fin="";
    }
    if(folio1 == null){
        folio1="";
    }
    if(folio2 == null){
        folio2 = "";
    }
    
    String UsuaJuris = "(";

    try {

        con.conectar();
        String F_Juris = "";
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "'");
        while (rset.next()) {
            F_Juris = rset.getString("F_Juris");
        }

        for (int i = 0; i < F_Juris.split(",").length; i++) {
            if (i == F_Juris.split(",").length - 1) {
                UsuaJuris = UsuaJuris + "FR.F_Ruta like 'R" + F_Juris.split(",")[i] + "%' ";
            } else {
                UsuaJuris = UsuaJuris + "FR.F_Ruta like 'R" + F_Juris.split(",")[i] + "%' or ";
            }
        }

        UsuaJuris = UsuaJuris + ")";
        System.out.println(UsuaJuris);
        con.cierraConexion();
    } catch (Exception e) {

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
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>
            
            <div class="panel-heading">
                <h3 class="panel-title">Recalendarizar/Imprimir Múltiples Remisiones</h3>
            </div>
            <form action="cambioFechas.jsp" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-lg-2" for="fecha_ini"><input type="radio" id="radio" name="radio" value="si" onchange="habilitar(this.value);" checked >Por Folio</label>
                    <div class="col-lg-2">
                        <input class="form-control" id="folio1" name="folio1" type="text" value="" onchange="habilitar(this.value);" />
                    </div>
                    <div class="col-lg-2">
                        <input class="form-control" id="folio2" name="folio2" type="text" value="" onchange="habilitar(this.value);"/>
                    </div>
                </div>
                <br>
                <div class="row">                                    
                    <label class="control-label col-lg-2" for="fecha_ini"><input type="radio" id="radio" name="radio" value="no" onchange="habilitar(this.value);" >Por Fecha</label>
                    <div class="col-lg-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-lg-2">
                        <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                    </div>
                </div>   
            </div>
                <div class="panel-body">
                    <div class="row">
                            <button class="btn btn-block btn-info" id="btn_capturar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
            <%
            int Contar=0;
            try {
                con.conectar();
                try {
                    String FechaFol="";
                    if(radio.equals("si")){
                        FechaFol = " WHERE F.F_ClaDoc between '"+folio1+"' and '"+folio2+"' ";
                    }else{
                        FechaFol = " WHERE F.F_FecEnt between '"+fecha_ini+"' and '"+fecha_fin+"' ";
                    }
                    ResultSet rset = con.consulta("SELECT count(F.F_ClaDoc) FROM tb_factura F "+FechaFol+";");
                    if (rset.next()) {
                        Contar = rset.getInt(1);
                    }
                } catch (Exception e) {

                }
                con.cierraConexion();
            } catch (Exception e) {

            }

            %>
            <form action="../Facturacion" method="post" id="formCambioFechas">
                <%
                if(Contar > 0){
                %>
                <div class="row">
                    <input class="form-control" id="radio1" name="radio1" type="hidden" value="<%=radio%>" />
                    <input class="form-control" id="foio11" name="folio11" type="hidden" value="<%=folio1%>" />
                    <input class="form-control" id="folio21" name="folio21" type="hidden" value="<%=folio2%>" />
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />
                    
                    <label class="control-label col-sm-2" for="imprera">Seleccione Impresora</label>
                    <div class="col-sm-2 col-sm-2">                       
                        <select id="impresora" name="impresora">
                            <option value="">--Seleccione Impresora--</option>
                            <%
                            String Nom = "";
                            PrintService[] impresoras = PrintServiceLookup.lookupPrintServices(null, null);
                            for (PrintService printService : impresoras) {
                                Nom = printService.getName();
                                //System.out.println("impresora" + Nom);                            
                            %>
                            <option value="<%=Nom%>"><%=Nom%></option>                            
                            <%}%>
                        </select>                        
                    </div>
                    <div class="col-sm-2 col-sm-offset-2">                       
                        <button type="submit" class="btn btn-info btn-block" id="btnImpMult" name="accion" value="impRemisMultples" >Impresiones Múltiples</button>
                    </div>
                    <div class="col-sm-2">                       
                        <button type="submit" class="btn btn-info btn-block" id="btnImpMult" name="accion" value="ImpRelacion" >Imprime Relación</button>
                    </div>    
                    <div class="col-sm-2">                      
                        <button type="button" class="btn btn-primary btn-block" data-toggle="modal" data-target="#modalCambioFecha" id="btnRecalendarizar" >Recalendarizar</button>
                    </div>
                </div>
                    <%}%>
                    
                    
                <div>
                    <input class="hidden" name="accion" value="recalendarizarRemis"  />
                    <input class="hidden" id="F_FecEnt" name="F_FecEnt" value=""  />
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                    <tr>
                                        <td></td>
                                        <td>No. Folio</td>
                                        <td>Punto de Entrega</td>
                                        <td>Fecha de Entrega</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                String FechaFol="";
                                                if(radio.equals("si")){
                                                    FechaFol = " AND F.F_ClaDoc between '"+folio1+"' and '"+folio2+"' ";
                                                }else{
                                                    FechaFol = " AND F.F_FecEnt between '"+fecha_ini+"' and '"+fecha_fin+"' ";
                                                }
                                                ResultSet rset = con.consulta("SELECT F.F_ClaDoc,F.F_ClaCli,U.F_NomCli,DATE_FORMAT(F.F_FecApl,'%d/%m/%Y') AS F_FecApl,SUM(F.F_Monto) AS F_Costo,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt, O.F_Tipo, O.F_Req FROM tb_factura F, tb_uniatn U, tb_obserfact O, tb_fecharuta FR where FR.F_ClaUni = U.F_ClaCli and  F.F_ClaDoc=O.F_IdFact AND F.F_ClaCli=U.F_ClaCli and " + UsuaJuris + " "+FechaFol+" GROUP BY F.F_ClaDoc ORDER BY F.F_ClaDoc+0;");
                                                while (rset.next()) {

                                    %>
                                    <tr>
                                        <td>
                                            <div class="checkbox">
                                                <label>
                                                    <input type="checkbox" name="checkRemis" checked="true" onchange="activarBtnReCal();" value="<%=rset.getString(1)%>">
                                                </label>
                                            </div>
                                        </td>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString("F_FecEnt")%></td>
                                    </tr>
                                    <%
                                                }
                                            } catch (Exception e) {

                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }
                                    %>
                                </tbody>
                            </table>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="modalCambioFecha" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <form>
                        <div class="modal-header">
                            <div class="row">
                                <h4 class="col-sm-12">Cambiar Fecha</h4>
                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar fecha:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="date" class="form-control" required name="" id="ModalFecha" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                <img src="imagenes/ajax-loader-1.gif" height="150" />
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="return confirmaModal();" name="accion" value="recalendarizarRemis">Recalendarizar</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal -->

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
                                    $(document).ready(function() {
                                        $('#datosCompras').dataTable();
                                        $("#fecha").datepicker();
                                        $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                                        //$('#btnRecalendarizar').attr('disabled', true);
                                        //$('#btnImpMult').attr('disabled', true);
                                    });

                                    /*function activarBtnReCal() {
                                        $('#btnRecalendarizar').attr('disabled', false);
                                        $('#btnImpMult').attr('disabled', false);
                                    }*/

                                    function confirmaModal() {
                                        var valida = confirm('Seguro que desea cambiar la fecha de entrega?');
                                        if ($('#ModalFecha').val() === "") {
                                            alert('Falta la fecha');
                                            return false;
                                        } else {
                                            if (valida) {
                                                $('#F_FecEnt').val($('#ModalFecha').val());
                                                $('#formCambioFechas').submit();
                                            } else {
                                                return false;
                                            }
                                        }
                                    }
        </script>
        <script>
    function habilitar(value){
        /*
        var fol1 = document.getElementById("folio1").value;
        var fol2 = document.getElementById("folio1").value;
        var fecha1 = document.getElementById("fecha_ini").value;
        var fecha2 = document.getElementById("fecha_fin").value; 
        
        if (fol1 !="" || fol2 !=""){
            document.getElementById("fecha_ini").disabled=true;
            document.getElementById("fecha_fin").disabled=true;            
            document.getElementById("fecha_ini").value="";
            document.getElementById("fecha_fin").value="";
        }else{
            document.getElementById("fecha_ini").disabled=false;
            document.getElementById("fecha_fin").disabled=false;
            
        }
        
        if (fecha1 !="" || fecha2 !=""){
            document.getElementById("folio1").disabled=true;
            document.getElementById("folio2").disabled=true;            
            document.getElementById("folio1").value="";
            document.getElementById("folio2").value="";
        }else{
            document.getElementById("folio1").disabled=false;
            document.getElementById("folio2").disabled=false;
        }*/
        
        if(value=="si"){
            document.getElementById("fecha_ini").disabled=true;
            document.getElementById("fecha_fin").disabled=true;
            document.getElementById("folio1").disabled=false;
            document.getElementById("folio2").disabled=false;
            document.getElementById("fecha_ini").value="";
            document.getElementById("fecha_fin").value="";

        }else if(value=="no"){
            document.getElementById("folio1").disabled=true;
            document.getElementById("folio2").disabled=true;
            document.getElementById("folio1").value="";
            document.getElementById("folio2").value="";
            document.getElementById("fecha_ini").disabled=false;
            document.getElementById("fecha_fin").disabled=false;            
        }
    }
    </script>
    </body>
</html>

