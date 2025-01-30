<%-- 
    Document   : index
    Created on : 14/12/2016, 09:34:46 AM
    Author     : MEDALFA
--%>

<%@page import="conn.ConectionDB"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    
   // Date fechaActual = new Date();
    //SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
   // String fechaSistema=formateador.format(fechaActual);
    
    String usua = "";
    String tipo = "" , Desproyecto = "";
    int Proyecto = 0;
    
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }    
       ConectionDB con = new ConectionDB();
     String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "";

    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        
       
        DesPro = (String) sesion.getAttribute("DesProFM");
    } catch (Exception e) {

    }

    System.out.println(FechaEnt);
    if (ClaCli == null) {
        ClaCli = "";
    }
    
    if (DesPro == null) {
        DesPro = "";
    }
    try {

        con.conectar();
        ResultSet rsetProy = con.consulta("SELECT P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE PU.F_Usuario = '" + usua + "';");
        if (rsetProy.next()) {
            Proyecto = rsetProy.getInt(1);
            Desproyecto = rsetProy.getString(2);
        }
        //}
        con.cierraConexion();

    } catch (Exception e) {

    }

   // try {
      //  if (request.getParameter("accion").equals("nuevoFolio")) {
         //   sesion.setAttribute("F_IndGlobal", fact.dameIndGlobal() + "");
         //   F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
       // }
   // } catch (Exception e) { }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
   
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
         <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="/jspf/menuPrincipal.jspf" %>
            <div class="row">
                <h4 class="col-sm-6">Modificar Folio Unidad</h4>
            </div>
            <form name="forma1" id="forma1" action="ModificarFolio" method="post">
                
                <div class="panel-footer">                
                    <c:forEach items="${DatosUnidad}" var="detalle">
                    <div class="row">                    
                        <label class="control-label col-sm-1" for="FolioU">Folio:</label>
                        <div class="col-sm-1">                            
                            <input name="FolioU" id="FolioU" class="form-control" value="${detalle.folio}" />
                        </div>
                        <label class="control-label col-sm-1" for="UnidadCla">Unidad:</label>
                        <div class="col-sm-2">                            
                            <input name="UnidadCla" id="UnidadCla" class="form-control" value="${detalle.claveuni}" />
                        </div>
                        <div class="col-sm-4">                            
                            <input name="UnidadA" id="UnidadA" class="form-control" value="${detalle.unidad}" />
                        </div>
                        <label class="control-label col-sm-1" for="ProyectoU">Proyecto:</label>
                        
                        <div class="col-sm-2">                            
                            <input name="ProyectoU" id="ProyectoU" class="form-control"  value="<%=Desproyecto%>" />
                        </div>
                    </div>
                  </c:forEach>
                </div>
                
                <div class="panel-footer">  
                    <div class="row">
                        <label class="control-label col-sm-2" for="UnidadN">Unidad nueva:</label>
                        <div class="col-sm-4">                            
                            <select name="UnidadN" id="Unidad" class="form-control">
                               <option value="0">--Seleccione --</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT * FROM  tb_uniatn  GROUP BY F_NomCli;");
                                       
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

                        <div class="col-sm-2">
                            <button class="btn btn-block btn-info" name="Accion" value="btnUnidadSave" >Guardar&nbsp; <label class="glyphicon glyphicon-floppy-disk"></label></button>
                        </div>
                        <div class="col-sm-2">
                            
                            <button class="btn btn-block btn-info" name="Accion" value="btnUnidadregresa" >Regresar&nbsp; <label class="glyphicon glyphicon-arrow-left"></label></button>
                        </div>
                    </div>
                </div>
            </form>


         </div>
            
        <%@include file="/jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script>
            function capturar()
    {
        var FolioU1 = $('#FolioU').val();
        var UnidadA1=document.getElementsByName("UnidadA")[0].value;
        var ProyectoU1=document.getElementsByName("ProyectoU")[0].value;
        document.URL:"ModicarFolio";
    }
            
        </script>    
        
        
    </body>
</html>
