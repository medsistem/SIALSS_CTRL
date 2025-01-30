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
    ConRequerimiento con = new ConRequerimiento();
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
                <h4>Procesar Requerimientos Electr&oacute;nicos</h4>
            </div>
            <br />
            <hr/>
            <form action="ProcesarReque.jsp" method="get">
                <div class="row">


                    <div class="panel-footer">
                        <div class="row">
                            <label class="control-label col-lg-2" for="jurisdiccion">Jurisdicción</label>
                            <div class="col-lg-5">
                                <select class="form-control" name="Juris" id="Juris" >
                                    <option id="op">--Jurisdicción--</option>
                                    <%                                                try {
                                            con.conectar();
                                            try {
                                                ResultSet RsetJur = con.consulta("SELECT J.F_ClaJur,J.F_DesJur FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli=U.F_ClaCli INNER JOIN tb_jurisdiccion J ON U.F_Juris=J.F_ClaJur WHERE R.F_StsReq in (3,4) and F_Periodo !='' AND F_FecCap>='2015-12-01 0:00:00' AND F_Periodo !='DICIEMBRE-2015' GROUP BY J.F_ClaJur,J.F_DesJur");
                                                while (RsetJur.next()) {
                                    %>
                                    <option value="<%=RsetJur.getString(1)%>"><%=RsetJur.getString(2)%></option>
                                    <%

                                                }

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }

                                            con.cierraConexion();
                                        } catch (Exception e) {
                                        }
                                    %>
                                </select>
                            </div>
                            <label class="control-label col-lg-2" for="mess">Período</label>
                            <div class="col-lg-3">
                                <select class="form-control" name="Mes" id="Mes" >
                                    <option id="op">--Período--</option>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                String F_Periodo = "";
                                                int dm = 0;
                                                ResultSet RsetMes = con.consulta("SELECT F_Periodo FROM tb_requerimientos WHERE F_Periodo !='' AND F_FecCap>='2015-12-01 0:00:00' AND F_Periodo !='DICIEMBRE-2015' GROUP BY F_Periodo");
                                                while (RsetMes.next()) {


                                    %>
                                    <option value="<%=RsetMes.getString(1)%>"><%=RsetMes.getString(1)%></option>
                                    <%

                                                }

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }

                                            con.cierraConexion();
                                        } catch (Exception e) {
                                        }
                                    %>
                                </select>
                            </div>


                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <button class="btn btn-block btn-primary" type="submit" id="btn_capturar" name="btn_capturar" onclick="return valida_alta()">Mostrar</button>
                                    </div>
                                </div>
                            </div>   
                        </div> 
                    </div>
                </div>
            </form>
            <div class="panel panel-primary">
                <div class="panel-body table-responsive">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td>Clave Cliente</td>
                                <td>Nombre Cliente</td>
                                <td>No. Requerimiento</td>
                                <td>Sts</td>
                                <td>Cant. Sol</td>                                    
                                <td>Procesar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    conlocal.conectar();
                                    try {
                                        int F_StsReq = 0;
                                        String DeSts = "", F_Cant = "", Meses = "", MesD = "", MesM = "", Unidad = "", Mes1 = "", Mes2 = "", anno = "";
                                        int Largo = 0, Dife = 0, Posi = 0, Contar = 0;
                                        String Mess = request.getParameter("Mes");
                                        String Juris = request.getParameter("Juris");

                                        Largo = Mess.length();
                                        Dife = Largo - 5;

                                        Posi = Mess.lastIndexOf('-');
                                        anno = Mess.substring(Posi + 1, Largo);

                                        Meses = Mess.substring(0, Dife);

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

                                        ResultSet rset = con.consulta("SELECT R.F_ClaCli,U.F_NomCli,R.F_IdReq,R.F_StsReq,FORMAT(SUM(D.F_Cant),0) AS F_Cant FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli=U.F_ClaCli INNER JOIN tb_detrequerimiento D ON R.F_IdReq=D.F_IdReq WHERE R.F_StsReq in (3,4) AND U.F_Juris='" + Juris + "' AND R.F_Periodo= '" + Mess + "' GROUP BY R.F_ClaCli,U.F_NomCli,R.F_IdReq,R.F_StsReq;");
                                        while (rset.next()) {
                                            F_StsReq = rset.getInt(4);
                                            F_Cant = rset.getString(5);
                                            Unidad = rset.getString(1);

                                            ResultSet Consulta = conlocal.consulta("SELECT COUNT(F_ClaCli) FROM tb_factura f INNER JOIN tb_obserfact ob on f.F_ClaDoc=ob.F_IdFact WHERE F_FecEnt BETWEEN '" + Mes1 + "' AND '" + Mes2 + "' AND F_ClaCli='" + Unidad + "' AND F_StsFact='A' AND ob.F_Req='A' GROUP BY F_ClaCli;");
                                            if (Consulta.next()) {
                                                Contar = Consulta.getInt(1);
                                            }
                                            if (Contar == 0) {
                                                if (F_StsReq == 3) {
                                                    DeSts = "Pendiente";
                                                } else if (F_StsReq == 4) {
                                                    DeSts = "Validado";
                                                } else {
                                                    DeSts = "";
                                                }

                            %>
                            <tr>

                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=DeSts%></td>
                                <td><%=F_Cant%></td>                                    
                                <td>                                        
                                    <form action="../ProcesaRequerimiento" method="post" onsubmit="muestraImagen()">
                                        <input class="hidden" name="fol_gnkl" value="<%=rset.getString(3)%>">
                                        <input class="hidden" name="Unidad" value="<%=rset.getString(1)%>">
                                        <button class="btn btn-block btn-info" name="accion" type="submit" id="accion" value="EnviarRequeFact"><span class="glyphicon glyphicon-ok"></span></button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                            }
                                            Contar = 0;
                                            F_StsReq = 0;
                                            F_Cant = "";
                                        }
                                    } catch (Exception e) {

                                    }
                                    con.cierraConexion();
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

