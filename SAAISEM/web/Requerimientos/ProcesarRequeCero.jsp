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
    String Secuencial = "", FechaSe = "", Factura = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }

    ConectionDB conlocal = new ConectionDB();

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
        <link href="../css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../css/topPadding.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>


            <div class="col-sm-12">
                <h4>Generar Requerimientos Electr&oacute;nicos en Ceros</h4>
            </div>
            <br />
            <hr/>

            <div class="panel panel-primary">
                <div class="panel-body table-responsive">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td>Clave Cliente</td>
                                <td>Nombre Cliente</td>
                                <td>Generar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%                                try {

                                    conlocal.conectar();
                                    try {

                                        String Meses = "", MesD = "", MesM = "", Unidad = "", Mes1 = "", Mes2 = "", anno = "";

                                        ResultSet Consulta = conlocal.consulta("SELECT F_ClaCli,F_NomCli FROM tb_uniatn WHERE F_StsCli='A' AND F_Tipo='RURAL';");
                                        while (Consulta.next()) {

                                            if (Meses.equals("ENERO")) {
                                                MesD = "31";
                                                MesM = "01";
                                            } else if (Meses.equals("FEBRERO")) {
                                                MesD = "29";
                                                MesM = "02";
                                            } else if (Meses.equals("MARZO")) {
                                                MesD = "31";
                                                MesM = "03";
                                            } else if (Meses.equals("ABRIL")) {
                                                MesD = "30";
                                                MesM = "04";
                                            } else if (Meses.equals("MAYO")) {
                                                MesD = "31";
                                                MesM = "05";
                                            } else if (Meses.equals("JUNIO")) {
                                                MesD = "30";
                                                MesM = "06";
                                            } else if (Meses.equals("JULIO")) {
                                                MesD = "31";
                                                MesM = "07";
                                            } else if (Meses.equals("AGOSTO")) {
                                                MesD = "31";
                                                MesM = "08";
                                            } else if (Meses.equals("SEPTIEMBRE")) {
                                                MesD = "30";
                                                MesM = "09";
                                            } else if (Meses.equals("OCTUBRE")) {
                                                MesD = "31";
                                                MesM = "10";
                                            } else if (Meses.equals("NOVIEMBRE")) {
                                                MesD = "30";
                                                MesM = "11";
                                            } else if (Meses.equals("DIEMBRE")) {
                                                MesD = "31";
                                                MesM = "12";
                                            }
                                            Mes1 = anno + "-" + MesM + "-01";
                                            Mes2 = anno + "-" + MesM + "-" + MesD;

                                            System.out.println("Mes" + Meses + " D: " + MesD + " M: " + MesM + " M1:" + Mes1 + " M2:" + Mes2);


                            %>
                            <tr>

                                <td><%=Consulta.getString(1)%></td>
                                <td><%=Consulta.getString(2)%></td>
                                <td>                                        
                                    <form action="reimpRequecero.jsp" target="_blank">
                                        <input class="hidden" name="Unidad" value="<%=Consulta.getString(1)%>">
                                        <button class="btn btn-block btn-info"><span class="glyphicon glyphicon-print"></span></button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                        }

                                    } catch (Exception e) {

                                    }

                                    conlocal.cierraConexion();
                                } catch (Exception e) {

                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>


        <div style="display: none;" class="text-center" id="Loader">
            <img src="../imagenes/ajax-loader-1.gif" height="150" />
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
                $(document).ready(function () {
                    $('#datosCompras').dataTable();
                });
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

                if (FI == "" && FF == "") {
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

