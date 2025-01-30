<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Sistemas
--%>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    String Secuencial = "", FechaSe = "", Factura = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
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
            <form action="ProcesarRequeLocal.jsp" method="get">
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
                                                ResultSet RsetJur = con.consulta("SELECT US.F_Juris, US.F_JurisDes FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli = U.F_ClaCli INNER JOIN tb_usuariounidades US ON R.F_ClaCli = US.F_ClaCli AND U.F_ClaCli = US.F_ClaCli WHERE R.F_StsReq IN (3, 4, 6, 8, 10, 11) AND F_FecCap>='2017-12-31 0:00:00' AND F_Periodo !='DICIEMBRE-2017' GROUP BY US.F_Juris;");
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
                                    <option value="ENERO">ENERO <%=year%></option>
                                    <option value="FEBRERO">FEBRERO <%=year%></option>
                                    <option value="MARZO">MARZO <%=year%></option>
                                    <option value="ABRIL">ABRIL <%=year%></option>
                                    <option value="MAYO">MAYO <%=year%></option>
                                    <option value="JUNIO">JUNIO <%=year%></option>
                                    <option value="JULIO">JULIO <%=year%></option>
                                    <option value="AGOSTO">AGOSTO <%=year%></option>
                                    <option value="SEPTIEMBRE">SEPTIEMBRE <%=year%></option>
                                    <option value="OCTUBRE">OCTUBRE <%=year%></option>
                                    <option value="NOVIEMBRE">NOVIEMBRE <%=year%></option>
                                    <option value="DICIEMBRE">DICIEMBRE <%=year%></option>

                                    <%
                                        /*try {
                                            con.conectar();
                                            try {
                                                String F_Periodo = "";
                                                int dm = 0;
                                                ResultSet RsetMes = con.consulta("SELECT F_Periodo FROM tb_requerimientos WHERE F_Periodo !='' AND F_FecCap>='2017-12-31 0:00:00' AND F_Periodo !='DICIEMBRE-2017' GROUP BY F_Periodo;");
                                                while (RsetMes.next()) {
                                         */
                                    %>
                                    <!--option value="<%//=RsetMes.getString(1)%>"><%//=RsetMes.getString(1)%></option-->
                                    <%
/*
                                                }

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }

                                            con.cierraConexion();
                                        } catch (Exception e) {
                                        }*/
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
                                <td>Tipo Requerimiento</td>
                                <td>Clave Cliente</td>
                                <td>Nombre Cliente</td>
                                <td>No. Requerimiento</td>
                                <td>Sts</td>
                                <td>Cant. Sol</td>
                                <td>Exportar</td>
                                <td>Procesar</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%                                try {
                                    con.conectar();
                                    try {
                                        int F_StsReq = 0;
                                        String DeSts = "", F_Cant = "", MesD = "", MesM = "", Unidad = "", Mes1 = "", Mes2 = "", anno = "";
                                        int Largo = 0, Dife = 0, Posi = 0, Contar = 0;
                                        String Mess = request.getParameter("Mes");
                                        String Juris = request.getParameter("Juris");
                                        //System.out.println("Mess1 = "+request.getParameter("Mes"));
                                        //anno = Mess.substring(Posi + 1, Largo);

                                        if (Mess.equals("ENERO")) {
                                            MesD = "31";
                                            MesM = "01";
                                        } else if (Mess.equals("FEBRERO")) {
                                            MesD = "29";
                                            MesM = "02";
                                        } else if (Mess.equals("MARZO")) {
                                            MesD = "31";
                                            MesM = "03";
                                        } else if (Mess.equals("ABRIL")) {
                                            MesD = "30";
                                            MesM = "04";
                                        } else if (Mess.equals("MAYO")) {
                                            MesD = "31";
                                            MesM = "05";
                                        } else if (Mess.equals("JUNIO")) {
                                            MesD = "30";
                                            MesM = "06";
                                        } else if (Mess.equals("JULIO")) {
                                            MesD = "31";
                                            MesM = "07";
                                        } else if (Mess.equals("AGOSTO")) {
                                            MesD = "31";
                                            MesM = "08";
                                        } else if (Mess.equals("SEPTIEMBRE")) {
                                            MesD = "30";
                                            MesM = "09";
                                        } else if (Mess.equals("OCTUBRE")) {
                                            MesD = "31";
                                            MesM = "10";
                                        } else if (Mess.equals("NOVIEMBRE")) {
                                            MesD = "30";
                                            MesM = "11";
                                        } else if (Mess.equals("DIEMBRE")) {
                                            MesD = "31";
                                            MesM = "12";
                                        }

                                        Mes1 = year + "-" + MesM + "-01 00:00:00";
                                        Mes2 = year + "-" + MesM + "-" + MesD + " 23:59:59";

                                        //System.out.println("M1= "+Mes1+"- Mes2 = "+Mes2);
                                        ResultSet rset = con.consulta("( SELECT R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq, FORMAT(SUM(D.F_Cant), 0) AS F_Cant, 'Requerimiento_Autorización' AS F_TipoReq FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli = U.F_ClaCli LEFT JOIN tb_detrequerimiento D ON R.F_IdReq = D.F_IdReq LEFT JOIN tb_usuariounidades US ON R.F_ClaCli = US.F_ClaCli AND U.F_ClaCli = US.F_ClaCli WHERE R.F_StsReq IN (4) AND US.F_Juris = '" + Juris + "' AND F_FecCap BETWEEN '" + Mes1 + "' AND '" + Mes2 + "' GROUP BY R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq ) UNION ( SELECT R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq, FORMAT(SUM(D.F_Entrega), 0) AS F_Cant, 'Fuera_Catálogo' AS F_TipoReq FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli = U.F_ClaCli LEFT JOIN tb_detreqcatalogo D ON R.F_IdReq = D.F_IdReq LEFT JOIN tb_usuariounidades US ON R.F_ClaCli = US.F_ClaCli AND U.F_ClaCli = US.F_ClaCli WHERE R.F_StsReq IN (8) AND US.F_Juris = '" + Juris + "' AND F_FecCap BETWEEN '" + Mes1 + "' AND '" + Mes2 + "' GROUP BY R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq ) UNION ( SELECT R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq, FORMAT(SUM(D.F_Entrega), 0) AS F_Cant, 'Stock_Máximo' AS F_TipoReq FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli = U.F_ClaCli LEFT JOIN tb_detreqstock D ON R.F_IdReq = D.F_IdReq LEFT JOIN tb_usuariounidades US ON R.F_ClaCli = US.F_ClaCli AND U.F_ClaCli = US.F_ClaCli WHERE R.F_StsReq IN (11) AND US.F_Juris = '" + Juris + "' AND F_FecCap BETWEEN '" + Mes1 + "' AND '" + Mes2 + "' GROUP BY R.F_ClaCli, U.F_NomCli, R.F_IdReq, R.F_StsReq )");
                                        while (rset.next()) {
                                            F_StsReq = rset.getInt(4);
                                            F_Cant = rset.getString(5);
                                            Unidad = rset.getString(1);

                                            /*ResultSet Consulta = con.consulta("SELECT COUNT(F_ClaCli) FROM tb_factura f INNER JOIN tb_obserfact ob on f.F_ClaDoc=ob.F_IdFact WHERE F_FecEnt BETWEEN '" + Mes1 + "' AND '" + Mes2 + "' AND F_ClaCli='" + Unidad + "' AND F_StsFact='A' AND ob.F_Req='A' GROUP BY F_ClaCli;");
                                            if (Consulta.next()) {
                                                Contar = Consulta.getInt(1);
                                            }
                                            if (Contar == 0) {*/
                                            if (F_StsReq == 3) {
                                                DeSts = "Pendiente";
                                            } else if (F_StsReq == 6) {
                                                DeSts = "Pendiente Fuera de Catálogo";
                                            } else if (F_StsReq == 10) {
                                                DeSts = "Pendiente Stock Máximo";
                                            } else if (F_StsReq == 4) {
                                                DeSts = "Validado";
                                            } else if (F_StsReq == 8) {
                                                DeSts = "Validado Fuera de Catálogo";
                                            } else if (F_StsReq == 11) {
                                                DeSts = "Validado Stock Máximo";
                                            } else {
                                                DeSts = "";
                                            }

                            %>
                            <tr>
                                <td><%=rset.getString(6)%></td>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=DeSts%></td>
                                <td><%=F_Cant%></td>
                                <td>
                                    <a class="btn btn-block btn-primary" href="gnrReqLocalTodos.jsp?fol_gnkl=<%=rset.getString(3)%>&Sts=<%=rset.getString(4)%>"><span class="glyphicon glyphicon-save"></span></a>
                                </td>
                                <td>
                                    <% if((F_StsReq == 6) || (F_StsReq == 8)){%>
                                    <%}else{%>
                                    <form action="../ProcesaRequerimientoLocal" method="post" onsubmit="muestraImagen()">
                                        <input class="hidden" name="fol_gnkl" value="<%=rset.getString(3)%>">
                                        <input class="hidden" name="Unidad" value="<%=rset.getString(1)%>">
                                        <input class="hidden" name="Sts" value="<%=rset.getString(4)%>">
                                        <button class="btn btn-block btn-info" name="accion" type="submit" id="accion" value="EnviarRequeFact"><span class="glyphicon glyphicon-ok"></span></button>
                                    </form>
                                        <%}%>
                                </td>
                            </tr>
                            <%
                                            //}
                                            Contar = 0;
                                            F_StsReq = 0;
                                            F_Cant = "";
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

