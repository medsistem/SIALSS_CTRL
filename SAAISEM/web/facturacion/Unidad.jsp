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
    String fecha_ini="",fecha_fin="",folio1="",folio2="",radio="",unidad="",unidad2="",NombreUnidad="",NombreUnidad2="";
    try {
        
        NombreUnidad = request.getParameter("unidad");
        NombreUnidad2 = request.getParameter("unidad2");
        
    } catch (Exception e) {

    }
    
    if(unidad == null){
        unidad="";
    }
    if(NombreUnidad == null){
       NombreUnidad="";
    }
    if(NombreUnidad2 == null){
       NombreUnidad2="";
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

            
            
            <div class="panel-heading">
                <h3 class="panel-title">Selecciona Unidad</h3>
            </div>
            <form action="cambioFechas.jsp" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-sm-1" for="fecha_ini">Unidad</label>
                    <div class="col-lg-6">
                        <input class="form-control" id="NombreUnidad" name="NombreUnidad" type="text" value="<%=unidad%>" onkeyup="descripUni()" onchange="habilitar2(this.value);" onclick="habilitar(this.value);"/>
                    </div>
                </div>   
            </div>
                
            </form>
            
            
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
          function habilitar(value){
          var unidad = $("#NombreUnidad").val();
          if(unidad !=""){
             window.location = "cambioFechas.jsp?NombreUnidad="+unidad+"&NombreUnidad2=<%=NombreUnidad2%>";
          }
          }
        </script>
    </body>
</html>

