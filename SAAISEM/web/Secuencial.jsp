<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Sistemas
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    String Secuencial="", FechaSe="", Factura="";
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
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
    } catch (Exception e) {

    }

    if (ClaCli == null) {
        ClaCli = "";
    }
    if (FechaEnt == null) {
        FechaEnt = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
    }
    if (DesPro == null) {
        DesPro = "";
    }

try {
con.conectar();
try {
            ResultSet rset_Fecha = con.consulta("SELECT DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur FROM tb_txtis WHERE F_FacGNKLAgr LIKE 'AG-0%' ORDER BY F_Secuencial DESC LIMIT 0,1");
            while (rset_Fecha.next()) {
                FechaSe = rset_Fecha.getString("F_Fecsur");                
            }
            ResultSet rset_Secu = con.consulta("SELECT F_Secuencial,DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur,F_FacGNKLAgr FROM tb_txtis ORDER BY F_Secuencial DESC LIMIT 0,1");
            while (rset_Secu.next()) {
                Secuencial = rset_Secu.getString("F_Secuencial");
                Factura = rset_Secu.getString("F_FacGNKLAgr");
            }
            if(FechaSe.equals("")){
                FechaSe = "00/00/0000";
            }
            if (Secuencial.equals("")){
                Secuencial = "0";                
                Factura = "0";
            }
            
        } catch (Exception e) {
            e.getMessage();
        } 

 con.cierraConexion();
    } catch (Exception e) {
    }  

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="css/topPadding.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>

            
                <div class="col-sm-12">
                    <h4>Generación Secuencial (Rurales)</h4>
                </div>
            <br />
            <hr/>
            <form action="ReporteSecuencial" method="get">
                <div class="row">
                    <div class="panel-footer">
                        <div class="row">
                            <h5>Descripción del Proceso</h5>
                            <h6>
                                a) A partir de la Fecha de Inicio, se eliminarán los Secuenciales Registrados <br />
                                b) Se Generarán los Secuenciales del Rango de Fechas <br />
                                c) Se seleccionan todas las Facturas, sin las Canceladas y sin las Devoluciones <br />
                                
                            </h6>
                        </div>                            
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <h5>Último Secuencial Generado</h5>
                            <h6>
                                Secuencial:&nbsp;&nbsp;<%=Secuencial%> &nbsp;&nbsp;&nbsp; Fecha Surtido:&nbsp;&nbsp;<%=FechaSe%> &nbsp;&nbsp;&nbsp; Factura (MEDALFA):&nbsp;&nbsp;<%=Factura%>
                            </h6>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <h5>IMPORTANTE! Este proceso solo debe EJECUTARSE en una sola Computadora a la Vez</h5><label class="control-label col-lg-2" for="fecha_ini">Fecha Inicio</label>
                            <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_ini" name="fecha_ini" data-date-format="dd/mm/yyyy"  value="" readonly /-->
                                    <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" />
                                </div>
                            <label class="control-label col-lg-2" for="fecha_fin">Fecha Fin</label>
                            <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_fin" name="fecha_fin" data-date-format="dd/mm/yyyy"  value="" readonly /-->
                                    <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" />
                                </div>
                            <label class="control-label col-lg-2" for="fecha_fin">Secuenciales</label>
                            <div class="col-lg-4">
                                <input type="radio" id="radio" name="radio" checked="true" value="sin" > Sin Diferencias
                                <input type="radio" id="radio" name="radio" value="con"> Con Diferencias
                            </div>
                            
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <button class="btn btn-block btn-primary" type="submit" id="btn_capturar" name="btn_capturar" onclick="return valida_alta()">Generar</button>
                                    </div>
                                </div>
                            </div>   
                        </div> 
                    </div>
                </div> 
            </form>
            <div style="display: none;" class="text-center" id="Loader">
                <img src="imagenes/ajax-loader-1.gif" height="150" />
            </div>  
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
    ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        
        <script>
       // $("#fecha_ini").datepicker({});
       // $("#fecha_fin").datepicker({});
        </script>
        <script>
       /* $(document).ready(function() {
            $("#btn_capturar").click(function() {
                var FI = $("#fecha_ini").val();
                var FF = $("#fecha_ini").val();

                if(FI =="" && FF ==""){
                    alert("Favor de Seleccionar Fechas");
                }
            });
        });*/
        function valida_alta() {
                /*var Clave = document.formulario1.Clave.value;*/
                var FI = $("#fecha_ini").val();
                var FF = $("#fecha_ini").val();

                if(FI =="" && FF ==""){
                    alert("Favor de Seleccionar Fechas");
                    return false;
                }/*else{
                    return confirm('¿Esta Ud. Seguro de Iniciar proceso de Generación?')
                }   */             
                document.getElementById('Loader').style.display = 'block';
            }
    </script>
    </body>

</html>

