<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
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
    String fecha_ini="", fecha_fin="",radio="",radio2="",F_FolCon="";
    int F_Idsur=0,F_IdePro=0,F_Cvesum=0,F_Punto=0,F_Con=0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    
    try {
        fecha_ini = request.getParameter("fecha_ini");        
        fecha_fin = request.getParameter("fecha_fin");    
        radio = request.getParameter("radio");
        radio2 = request.getParameter("radio2");
    } catch (Exception e) {

    }
    if(fecha_ini==null){
        fecha_ini="";
    }
    if(fecha_fin==null){
        fecha_fin="";
    }
    
    ConectionDB con = new ConectionDB();
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
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Carga de Facturas Savi</h3>
                </div>
                <div class="panel-body ">
                    <form method="post" action="FileUploadFactura" enctype="multipart/form-data" name="form1">
                        <div class="form-group">
                            <div class="form-group">
                                <div class="col-lg-4 text-success">
                                    <h4>Seleccione el Excel a Cargar</h4>
                                </div>
                                <!--label for="Clave" class="col-xs-2 control-label">Clave*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Clave" name="Clave" placeholder="Clave" onKeyPress="return tabular(event, this)" autofocus >
                                </div-->
                                <label for="Nombre" class="col-xs-2 control-label">Nombre Archivo*</label>
                                <div class="col-sm-5">
                                    <input class="form-control" type="file" name="file1" id="file1" accept=".xlsx"/>                                    
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="guardar" onclick="return valida_alta();"> Cargar Archivo</button>
                    </form>
                    <div style="display: none;" class="text-center" id="Loader">
                        <img src="imagenes/ajax-loader-1.gif" height="150" />
                    </div>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
                <form action="RelacionarF.jsp" method="post">
                        <div class="panel-footer">
                            <div class="row">                                    
                                <label class="control-label col-sm-1" for="fecha_ini">Fecha Inicio</label>
                                <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_ini" name="fecha_ini" data-date-format="DD/MM/YYYY"  value="" /-->
                                    <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" />
                                </div>
                                <label class="control-label col-sm-1" for="fecha_fin">Fecha Fin</label>
                                <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_fin" name="fecha_fin" data-date-format="dd/mm/yyyy"  value="" /-->
                                    <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" />
                                </div>
                                <label class="control-label col-sm-1" for="fecha_fin">Relacionadas</label>
                                <div class="col-sm-2">
                                    <input type="radio" id="radio" name="radio" value="si" > Si
                                    <input type="radio" id="radio" name="radio" checked="true" value="no"> No
                                </div>
                                <label class="control-label col-sm-1" for="fecha_fin">Partida</label>
                                <div class="col-sm-2">
                                    <input type="radio" id="radio2" name="radio2" checked="true" value="si" > Rurales
                                    <input type="radio" id="radio2" name="radio2" value="no"> Farmacia
                                </div>
                            </div>                            
                        </div>     
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-10 text-center">
                                    <button class="btn btn-block btn-info" id="btn_capturar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                                </div>
                                <div>
                                    <label class="control-label col-lg-2" for="exportar"><a href="txtfact_gnr.jsp?f1=<%=fecha_ini%>&f2=<%=fecha_fin%>&ra=<%=radio%>&ra2=<%=radio2%>">Exportar<label class="glyphicon glyphicon-export"></label></a></label>
                                </div>
                            </div>
                        </div>                        
                    </form>
                <form class="form-horizontal" role="form" name="formulario_receta" id="formulario_receta" method="get" action="Caratula">   
                <div class="panel-footer">
                    <h4 class="col-sm-1">Facturas</h4>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                            <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />
                            <select class="form-control" name="folios" id="folios">
                                <option value="">--Seleccione--</option>
                                <%
                                try {
                                    ResultSet rsetfact = null;

                                    con.conectar();
                                    if (fecha_ini == "" && fecha_fin == ""){
                                        rsetfact = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratula WHERE F_FacGNKLAgr='1' GROUP BY F_FacGNKLAgr");                                   
                                    }else{
                                        if(radio2.equals("si")){
                                                rsetfact = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratula WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' AND F_FacGNKLAgr LIKE 'AG-0%'  GROUP BY F_FacGNKLAgr");                                                                              
                                            }else{
                                                rsetfact = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratula WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' AND F_FacGNKLAgr LIKE 'AG-F%'  GROUP BY F_FacGNKLAgr");                                       
                                            }

                                        
                                    }

                                    while (rsetfact.next()) {

                                    %>
                                    <option value="<%=rsetfact.getString(1)%>"><%=rsetfact.getString(1)%></option>
                                    <%}
                                     con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                            </select>
                        </div>
                     <!--label class="control-label col-sm-1" for="imprera">Impresora</label>
                            <div class="col-sm-2 col-sm-2">                       
                                <select id="impresora" name="impresora">
                                    <option value="">--Seleccione Impresora--</option>
                                    <%
                                    /*String Nom = "";
                                    PrintService[] impresoras = PrintServiceLookup.lookupPrintServices(null, null);
                                    for (PrintService printService : impresoras) {
                                        Nom = printService.getName();*/
                                        //System.out.println("impresora" + Nom);                            
                                    %>
                                    <option value="<%//=Nom%>"><%//=Nom%></option>                            
                                    <%//}%>
                                </select>                        
                            </div-->       
                    <div class="col-sm-2 col-sm-offset-2">
                        <button class="btn btn-block btn-info" id="btn_capturar" name="ban" value="1" >GENERAR 1x1&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                    </div>
                    <div class="col-sm-2">
                    <button class="btn btn-block btn-info" id="btn_capturar"  name="ban" value="2">GENERAR TODOS&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                    </div>
                </div>
                </form>
                <br />
                
                <div class="panel-footer">
                    <div style="width:100%; height:400px; overflow:auto;">
                            <!--table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv"-->
                            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered">
                                <thead>
                                    <tr>
                                        <td>Fecha Surtido</td>
                                        <td>Factura MEDALFA.</td> 
                                        <td>Fol. Conc.</td>                                
                                        <td>Fact. SAVI</td>                                                           
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                try {
                                    ResultSet rset = null;
                                    
                                    con.conectar();
                                    if (fecha_ini == "" && fecha_fin == ""){
                                        rset = con.consulta("SELECT date_format(F_Fecsur,'%d/%m/%Y') as F_Fecsur,F_FacGNKLAgr,F_Idsur,F_IdePro,F_Cvesum,F_FacSAVI FROM tb_facturas where F_FacGNKLAgr=1");                                   
                                    }else{
                                        if (radio.equals("si")){
                                            if(radio2.equals("si")){
                                                rset = con.consulta("SELECT date_format(F_Fecsur,'%d/%m/%Y') as F_Fecsur,F_FacGNKLAgr,F_Idsur,F_IdePro,F_Cvesum,F_FacSAVI FROM tb_facturas WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' and F_FacSAVI !='' AND F_FacGNKLAgr LIKE 'AG-0%'");                                       
                                            }else{
                                                rset = con.consulta("SELECT date_format(F_Fecsur,'%d/%m/%Y') as F_Fecsur,F_FacGNKLAgr,F_Idsur,F_IdePro,F_Cvesum,F_FacSAVI FROM tb_facturas WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' and F_FacSAVI !='' AND F_FacGNKLAgr LIKE 'AG-F%'");                                       
                                            }
                                        }else{
                                            if(radio2.equals("si")){
                                                rset = con.consulta("SELECT date_format(F_Fecsur,'%d/%m/%Y') as F_Fecsur,F_FacGNKLAgr,F_Idsur,F_IdePro,F_Cvesum,F_FacSAVI FROM tb_facturas WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' and F_FacSAVI ='' AND F_FacGNKLAgr LIKE 'AG-0%'");                                       
                                            }else{
                                                rset = con.consulta("SELECT date_format(F_Fecsur,'%d/%m/%Y') as F_Fecsur,F_FacGNKLAgr,F_Idsur,F_IdePro,F_Cvesum,F_FacSAVI FROM tb_facturas WHERE F_Fecsur >= '"+fecha_ini+"' and F_Fecsur <= '"+fecha_fin+"' and F_FacSAVI ='' AND F_FacGNKLAgr LIKE 'AG-F%'");                                       
                                            }
                                        
                                        }
                                        
                                    }
                                   
                                    while (rset.next()) {
                                        F_Con ++;
                                        F_Idsur = rset.getInt(3);
                                        F_IdePro = rset.getInt(4);
                                        F_Cvesum = rset.getInt(5);
                                        
                                        if ((F_Idsur == 1) && (F_IdePro == 0) && (F_Cvesum == 1)){
                                            F_Punto = 1;
                                        }else if ((F_Idsur == 1) && (F_IdePro == 1) && (F_Cvesum == 1)){
                                            F_Punto = 2;                                                    
                                        }else if ((F_Idsur == 1) && (F_IdePro == 0) && (F_Cvesum == 2)){
                                            F_Punto = 3;                                                    
                                        }else if ((F_Idsur == 1) && (F_IdePro == 1) && (F_Cvesum == 2)){
                                            F_Punto = 4;                                                    
                                        }else if ((F_Idsur == 2) && (F_IdePro == 0) && (F_Cvesum == 1)){
                                            F_Punto = 5;                                                    
                                        }else if ((F_Idsur == 2) && (F_IdePro == 1) && (F_Cvesum == 1)){
                                            F_Punto = 6;                                                    
                                        }else if ((F_Idsur == 2) && (F_IdePro == 0) && (F_Cvesum == 2)){
                                            F_Punto = 7;                                                    
                                        }else if ((F_Idsur == 2) && (F_IdePro == 1) && (F_Cvesum == 2)){
                                            F_Punto = 8;                                                    
                                        }
                                        F_FolCon = rset.getString(2)+"_"+F_Punto;
                            %>
 
                         


                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>                                
                                <td><%=F_Punto%></td>                                  
                                <td><%=rset.getString(6)%></td>
                                                                 
                            </tr>
                            
                            <%
                                    }
                                    if(F_Con == 0){
                                        if (radio.equals("si")){
                            %>
                                <script>
                                    alert("No Existen Datos Relacionados");
                                </script>
                            <%                                    
                                    }else{
                            %>
                                <script>
                                    alert("No Existen Datos No Relacionados");
                                </script>
                            <%                 
                                        }
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            %>
                                </tbody>
                            </table>
                    </div>
                        </div> 
            </div>
        </div>
        <br><br><br>
        <%@include file="../jspf/piePagina.jspf" %>
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
    //$("#fecha_ini").datepicker();
    //$("#fecha_fin").datepicker();
    
    </script>
    <script>
    $(document).ready(function() {
        $('#datosProv').dataTable();
    });
     function valida_alta() {
                var Nombre = document.getElementById('file1').value;

                if (Nombre === "") {
                    alert("Tiene campos vacíos, verifique.");
                    return false;
                }         
                document.getElementById('Loader').style.display = 'block';
            }
</script>

