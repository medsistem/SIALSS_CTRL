<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%DecimalFormat format = new DecimalFormat("####,###");%>
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

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "",Origen="";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("fecha");
            Origen = request.getParameter("selectOri");
            System.out.println("Origen-"+Origen);
        }
    } catch (Exception e) {

    }
    try {
        
            
            Origen = request.getParameter("selectOri");
            System.out.println("Origen-"+Origen);
        
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
    }
    String fecha_ini="",fecha_fin="",folio1="",folio2="",radio="",unidad="",unidad2="";
    try {
        fecha_ini = request.getParameter("fecha_ini");        
        fecha_fin = request.getParameter("fecha_fin");
        folio1 = request.getParameter("folio1");        
        folio2 = request.getParameter("folio2");
        radio = request.getParameter("radio");   
        unidad = request.getParameter("NombreUnidad");
        unidad2 = request.getParameter("NombreUnidad2");
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
    if(unidad == null){
        unidad="";
    }
    if(unidad2 == null){
        unidad2="";
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
        
        if(unidad !=""){
         ResultSet RUni = con.consulta("SELECT F_ClaCli FROM tb_uniatn WHERE F_NomCli='"+unidad+"'");
         if(RUni.next()){
             unidad = RUni.getString(1);
         }
        }
        if(unidad2 !=""){
         ResultSet RUni2 = con.consulta("SELECT F_ClaCli FROM tb_uniatn WHERE F_NomCli='"+unidad2+"'");
         if(RUni2.next()){
             unidad2 = RUni2.getString(1);
         }
        }
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
                <h3 class="panel-title">Requerido Vs. Surtido</h3>
            </div>
            <form action="Reporteador.jsp" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-sm-2">
                        <select name="selectOri" id="selectOri">
                            <option value="">--Selecciona Origen--</option>                            
                            <option value="1">ADMON</option>                                                        
                            <option value="2">VD</option>                                                        
                        </select>
                    </div>
                </div>   
            </div>
                
                <div class="panel-body">
                    <div class="row">
                            <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
            <%
            int Contar=0;
            try {
                con.conectar();
                try {
                    ResultSet rset = null;
                    if(fecha_ini !="" && fecha_fin !=""){
                        if(Origen !=""){
                            rset = con.consulta("SELECT COUNT(l.F_ClaPro) FROM tb_factura f INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' and l.F_Origen='"+Origen+"' GROUP BY F_StsFact;");
                        }else{
                            rset = con.consulta("SELECT COUNT(l.F_ClaPro) FROM tb_factura f INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' GROUP BY F_StsFact;");
                        }
                    
                        if (rset.next()) {
                            Contar = rset.getInt(1);
                        }    
                    }else{
                        Contar = 0;
                    }
                    
                } catch (Exception e) {

                }
                con.cierraConexion();
            } catch (Exception e) {

            }

            %>
            
                <%
            System.out.println("Contar: "+Contar);
                if(Contar > 0){
                %>
                <div class="row">
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />                    
                    <input class="form-control" id="origen" name="origen" type="hidden" value="<%=Origen%>" />                    
                    <a class="btn btn-block btn-info" href="gnrReport.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>&Origen=<%=Origen%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
                    <br />                    
                </div>
                    <%}%>
                    
                    
                <div>
                    
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                    <tr>                                        
                                        <td>Clave</td>
                                        <td>Descripción</td>
                                        <td>Origen</td>
                                        <td>Cantidad Req.</td>
                                        <td>Cantidad Sur.</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                ResultSet rset = null;
                                                int CantReq=0,CantSur=0;
                                                if(fecha_ini !="" && fecha_fin !=""){                        
                                                  
                                                //ResultSet rset = con.consulta("SELECT F.F_ClaDoc,F.F_ClaCli,U.F_NomCli,DATE_FORMAT(F.F_FecApl,'%d/%m/%Y') AS F_FecApl,SUM(F.F_Monto) AS F_Costo,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt, O.F_Tipo, O.F_Req FROM tb_factura F, tb_uniatn U, tb_obserfact O, tb_fecharuta FR where FR.F_ClaUni = U.F_ClaCli and  F.F_ClaDoc=O.F_IdFact AND F.F_ClaCli=U.F_ClaCli and " + UsuaJuris + " "+Query+" GROUP BY F.F_ClaDoc ORDER BY F.F_ClaDoc+0;");
                                                    if(Origen !=""){
                                                        rset = con.consulta("SELECT f.F_ClaPro,m.F_DesPro,FORMAT(SUM(F_CantReq),0),FORMAT(SUM(F_CantSur),0),SUM(F_CantReq),SUM(F_CantSur),l.F_Origen FROM tb_factura f LEFT JOIN tb_medica m on f.F_ClaPro=m.F_ClaPro INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE f.F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' and l.F_Origen='"+Origen+"' GROUP BY f.F_ClaPro,l.F_Origen;");
                                                    }else{
                                                        rset = con.consulta("SELECT f.F_ClaPro,m.F_DesPro,FORMAT(SUM(F_CantReq),0),FORMAT(SUM(F_CantSur),0),SUM(F_CantReq),SUM(F_CantSur),l.F_Origen FROM tb_factura f LEFT JOIN tb_medica m on f.F_ClaPro=m.F_ClaPro INNER JOIN tb_lote l on f.F_Lote=l.F_FolLot AND f.F_ClaPro=l.F_ClaPro AND f.F_Ubicacion=l.F_Ubica WHERE f.F_FecEnt BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND F_StsFact='A' GROUP BY f.F_ClaPro,l.F_Origen;");
                                                    }
                                                    
                                                    
                                                while (rset.next()) {
                                                    
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%></td>
                                        <td><%=rset.getString(7)%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(4)%></td>
                                    </tr>
                                    <%
                                                CantReq =CantReq + rset.getInt(5);
                                                CantSur =CantSur + rset.getInt(6);
                                                }
                                     %>
                                     <tr>
                                        <td></td>
                                        <td>Total</td>
                                        <td></td>
                                        <td><%=format.format(CantReq)%></td>
                                        <td><%=format.format(CantSur)%></td>
                                    </tr>
                                     <%
                                                    CantReq=0;
                                                    CantSur=0;
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
            
        </div>

        
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
        </script>
        <script>
    
    </script>
    <script type="text/javascript">
          $(function() {
               var availableTags = [
          <%
            try {
                con.conectar();
                try {
                    ResultSet rset = con.consulta("SELECT F_NomCli FROM tb_uniatn");
                    while (rset.next()) {
                        out.println("'" + rset.getString(1) + "',");
                    }
                } catch (Exception e) {

                }
                con.cierraConexion();
            } catch (Exception e) {

            }
        %>
               ];
               $("#NombreUnidad").autocomplete({
                   source: availableTags
               });
          });
        </script>        
        <script>
            $(document).ready(function(){
                $('#selectjur').change(function(){
                    var Juris = $('#juris').val();
                     var valor = $('#selectjur').val();
                     if(Juris !=""){
                     $('#juris').val(Juris+","+valor);
                     }else{
                         $('#juris').val(valor);
                     }
                 });
            });
       </script>
    </body>
</html>

