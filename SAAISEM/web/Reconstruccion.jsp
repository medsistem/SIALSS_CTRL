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
                    <h4>Reconstrucción de Existencias</h4>
                </div>
            <br />
            <hr/>
            <form action="ReconstruirExist" method="get" name="formulario" id="formulario">
                <div class="row">                    
                    
                    <div class="panel-footer">
                            <div class="row">
                            <h5>IMPORTANTE! Este proceso solo debe EJECUTARSE en una sola Computadora a la Vez</h5><label class="control-label col-lg-2" for="fecha_ini">Clave Insumo</label>
                            <div class="col-lg-2">
                                <select name="Clave" id="Clave">
                                    <option>--Selecciona--</option>
                                    <%
                                    try{
                                        con.conectar();
                                        
                                        ResultSet DatosC = con.consulta("SELECT F_ClaPro,CONCAT(F_DesPro,' [',F_ClaPro,']') AS F_DesPro FROM tb_medica WHERE F_StsPro='A'");
                                        while(DatosC.next()){
                                    %>
                                    <option value="<%=DatosC.getString(1)%>"><%=DatosC.getString(2)%></option>
                                    <%
                                        }
                                        con.cierraConexion();

                                    }catch (Exception e) {System.out.println(e.getMessage());}
                                    %>
                                    %>
                                </select>
                                </div>
                                    <br/>
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <button class="btn btn-block btn-info" type="submit" id="btn_capturar" name="btn_capturar" onclick="">Reconstruir</button>
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
           
            $(document).ready(function() {
                $("#formulario").submit(function (){                    
                    var missinginfo = "";
                    var botonEnviar = document.getElementById('btn_capturar');
                       var valida = confirm('Seguro que desea Reconstruir Existencia?');
                        if (valida) {
                            $('#formCambio').submit();
                            botonEnviar.disabled = true;
                            document.getElementById('Loader').style.display = 'block';
                            return true;
                        }else{
                            return false;
                        }
                   
                });
            });
        </script>
       
        <script>
       
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

