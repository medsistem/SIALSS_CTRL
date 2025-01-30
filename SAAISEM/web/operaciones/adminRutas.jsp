<%-- 
    Document   : verRutas
    Created on : 18/12/2014, 03:33:29 PM
    Author     : Americo
--%>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String F_Anio = "";
    String F_Mes = "";
    String F_Juris = "";
    String F_Muni = "";
    try {
        F_Anio = request.getParameter("F_Anio");
    } catch (Exception e) {
    }
    try {
        F_Muni = request.getParameter("F_Muni");
    } catch (Exception e) {
    }
    try {
        F_Mes = request.getParameter("F_Mes");
    } catch (Exception e) {
    }
    try {
        F_Juris = request.getParameter("F_Juris");
    } catch (Exception e) {
    }

    try {
        con.conectar();

        con.cierraConexion();
    } catch (Exception e) {

    }
%>
<html>
    <head><!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>
            <div class="row">
                <form action="adminRutas.jsp" method="post">
                    <div class="row">
                        <h4 class="col-sm-2">Seleccione Juris</h4>
                        <div class="col-sm-2">
                            <select name="F_Juris" class="form-control" onchange="SelectMuni(this.form);" required="">
                                <option>Seleccione</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("select DISTINCT F_ClaJur from tb_uniatn; ");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString("F_ClaJur")%>">Juris <%=rset.getString("F_ClaJur")%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>
                            </select>
                        </div>

                        <h4 class="col-sm-2">Seleccione Muni</h4>
                        <div class="col-sm-2">
                            <select name="F_Muni" id="F_Muni" class="form-control" required="">
                                <option>Seleccione</option>
                                <option value="">Seleccione</option>
                            </select>
                        </div>

                    </div>
                    <div class="row">
                        <h4 class="col-sm-2">Seleccione Año</h4>
                        <div class="col-sm-2">
                            <select name="F_Anio" class="form-control" required="">
                                <option>Seleccione</option>
                                <%                            for (int i = 2010; i <= 2020; i++) {
                                %>
                                <option value="<%=i%>"><%=i%></option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        <h4 class="col-sm-2">Seleccione el Mes:</h4>
                        <div class="col-sm-2">
                            <select name="F_Mes" class="form-control" required="">
                                <option>Seleccione</option>
                                <option value="0">Enero</option>
                                <option value="1">Febrero</option>
                                <option value="2">Marzo</option>
                                <option value="3">Abril</option>
                                <option value="4">Mayo</option>
                                <option value="5">Junio</option>
                                <option value="6">Julio</option>
                                <option value="7">Agosto</option>
                                <option value="8">Septiembre</option>
                                <option value="9">Octubre</option>
                                <option value="10">Noviembre</option>
                                <option value="11">Diciembre</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-block btn-primary">Buscar</button>
                        </div>
                    </div>
                </form>
            </div>
            <hr/>
        </div>

        <div style="width: 1800px; margin: auto;">
            <%
                if (F_Juris != null && !F_Juris.equals("")) {
                    String MesLetra = "";
                    try {
                        switch (Integer.parseInt(F_Mes) + 1) {
                            case 1:
                                MesLetra = "Enero";
                                break;
                            case 2:
                                MesLetra = "Febrero";
                                break;
                            case 3:
                                MesLetra = "Marzo";
                                break;
                            case 4:
                                MesLetra = "Abril";
                                break;
                            case 5:
                                MesLetra = "Mayo";
                                break;
                            case 6:
                                MesLetra = "Junio";
                                break;
                            case 7:
                                MesLetra = "Julio";
                                break;
                            case 8:
                                MesLetra = "Agosto";
                                break;
                            case 9:
                                MesLetra = "Septiembre";
                                break;
                            case 10:
                                MesLetra = "Octubre";
                                break;
                            case 11:
                                MesLetra = "Noviembre";
                                break;
                            case 12:
                                MesLetra = "Diciembre";
                                break;
                        }
            %>
            <h2>Jurisdicción <%=F_Juris%> | Municipio: <%=F_Muni%> - <%=MesLetra%> <%=F_Anio%> </h2>
            <%
                } catch (Exception e) {

                }
            %>
            <table class="table table-condensed table-striped table-bordered" id="tablaRuta">
                <thead>
                    <%
                        try {
                            F_Muni = request.getParameter("F_Muni");
                        } catch (Exception e) {
                        }
                        try {
                            F_Anio = request.getParameter("F_Anio");
                        } catch (Exception e) {
                        }
                        try {
                            F_Mes = request.getParameter("F_Mes");
                        } catch (Exception e) {
                        }
                        try {
                            F_Juris = request.getParameter("F_Juris");
                        } catch (Exception e) {
                        }
                        try {
                            con.conectar();
                    %>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <%
                            Calendar calendar = Calendar.getInstance();
                            if (F_Anio != null || !F_Anio.equals("")) {
                                calendar.set(Calendar.YEAR, Integer.parseInt(F_Anio));
                            }
                            if (F_Mes != null || !F_Mes.equals("")) {
                                calendar.set(Calendar.MONTH, Integer.parseInt(F_Mes));
                            }
                            //int numDays = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                            int Mes = calendar.get(Calendar.MONTH) + 1;
                            int Anio = calendar.get(Calendar.YEAR);
                            int numDays = calendar.getActualMaximum(Calendar.DATE);
                            System.out.println(numDays);
                            if (F_Mes != null || !F_Mes.equals("")) {
                                calendar.set(Calendar.MONTH, Integer.parseInt(F_Mes));
                            }
                            numDays = calendar.getActualMaximum(Calendar.DATE);
                            Mes = calendar.get(Calendar.MONTH) + 1;
                            Anio = calendar.get(Calendar.YEAR);
                            System.out.println(numDays);
                            for (int i = 1; i <= numDays; i++) {
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.set(Anio, Integer.parseInt(F_Mes), i);
                                int ds = calendar2.get(Calendar.DAY_OF_WEEK);
                                String El_Dia = "";
                                switch (ds) {
                                    case 1:
                                        El_Dia = "D";
                                        break;
                                    case 2:
                                        El_Dia = "L";
                                        break;
                                    case 3:
                                        El_Dia = "M";
                                        break;
                                    case 4:
                                        El_Dia = "M";
                                        break;
                                    case 5:
                                        El_Dia = "J";
                                        break;
                                    case 6:
                                        El_Dia = "V";
                                        break;
                                    case 7:
                                        El_Dia = "S";
                                        break;
                                }
                        %>
                        <td class="
                            <%
                                if (El_Dia.equals("S") || El_Dia.equals("D")) {
                                    out.println("danger ");
                                }
                            %>
                            " ><%=El_Dia%></td>
                        <%
                            }
                        %>
                    </tr>
                    <tr>
                        <td width="100">Ruta</td>
                        <td>Loc Plano</td>
                        <td>Juris</td>
                        <td>Mod</td>
                        <td>Unidad</td>
                        <td>Municipio</td>
                        <%
                            /*if (F_Mes != null || !F_Mes.equals("")) {
                             calendar.set(Calendar.MONTH, Integer.parseInt(F_Mes));
                             }
                             numDays = calendar.getActualMaximum(Calendar.DATE);
                             Mes = calendar.get(Calendar.MONTH) + 1;
                             Anio = calendar.get(Calendar.YEAR);
                             System.out.println(numDays);*/
                            for (int i = 1; i <= numDays; i++) {
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.set(Anio, Integer.parseInt(F_Mes), i);
                                int ds = calendar2.get(Calendar.DAY_OF_WEEK);
                                String El_Dia = "";
                                switch (ds) {
                                    case 1:
                                        El_Dia = "D";
                                        break;
                                    case 2:
                                        El_Dia = "L";
                                        break;
                                    case 3:
                                        El_Dia = "M";
                                        break;
                                    case 4:
                                        El_Dia = "M";
                                        break;
                                    case 5:
                                        El_Dia = "J";
                                        break;
                                    case 6:
                                        El_Dia = "V";
                                        break;
                                    case 7:
                                        El_Dia = "S";
                                        break;
                                }
                        %>
                        <td class="
                            <%
                                if (El_Dia.equals("S") || El_Dia.equals("D")) {
                                    out.println("danger ");
                                }
                            %>
                            "><%=i%></td>
                        <%
                            }
                        %>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int idReg = 1;
                        ResultSet rset = con.consulta("select u.F_ClaCli, u.F_NomCli, d.F_LocPla, d.F_Juris, d.F_Ruta, d.F_ModCli, d.F_MunCli from tb_uniatn u, tb_detcli d where u.F_ClaCli=d.F_ClaCli and u.F_ClaJur = '" + F_Juris + "' and d.F_MunCli = '" + F_Muni + "'  and u.F_StsCli = 'A' order by d.F_Ruta");
                        while (rset.next()) {
                    %>
                    <tr>
                        <td><small><%=rset.getString("F_Ruta")%></small></td>
                        <td><small><%=rset.getString("F_LocPla")%></small></td>
                        <td><small><%=rset.getString("F_Juris")%></small></td>
                        <td><small><%=rset.getString("F_ModCli")%></small></td>
                        <td><small><%=rset.getString("F_ClaCli")%> - <%=rset.getString("F_NomCli")%></small></td>
                        <td><small><%=rset.getString("F_MunCli")%></small></td>
                        <%
                            for (int i = 1; i <= numDays; i++) {

                        %>
                        <td class="
                            <%                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.set(Anio, Integer.parseInt(F_Mes), i);
                                int ds = calendar2.get(Calendar.DAY_OF_WEEK);
                                String El_Dia = "";
                                switch (ds) {
                                    case 1:
                                        El_Dia = "D";
                                        break;
                                    case 2:
                                        El_Dia = "L";
                                        break;
                                    case 3:
                                        El_Dia = "M";
                                        break;
                                    case 4:
                                        El_Dia = "M";
                                        break;
                                    case 5:
                                        El_Dia = "J";
                                        break;
                                    case 6:
                                        El_Dia = "V";
                                        break;
                                    case 7:
                                        El_Dia = "S";
                                        break;
                                }
                                if (El_Dia.equals("S") || El_Dia.equals("D")) {
                                    out.println("danger ");
                                }
                            %>
                            " >
                            <div id="registroRuta<%=idReg%>">
                                <%
                                    ResultSet rset2 = con.consulta("select F_Id from tb_fecharuta where F_ClaUni = '" + rset.getString("F_ClaCli") + "' and F_Fecha = '" + Anio + "-" + Mes + "-" + i + "' ");
                                    String enRuta = "";
                                    while (rset2.next()) {
                                        enRuta = "X";
                                    }
                                %>
                                <form action="Rutas" method="post">
                                    <input class="hidden" name="F_ClaCli" value="<%=rset.getString("F_ClaCli")%>">
                                    <input class="hidden" name="F_Juris" value="<%=F_Juris%>">
                                    <input class="hidden" name="F_Anio" value="<%=F_Anio%>">
                                    <input class="hidden" name="F_Muni" value="<%=F_Muni%>">
                                    <input class="hidden" name="F_Mes" value="<%=F_Mes%>">
                                    <input class="hidden" name="idReg" value="<%=idReg%>">
                                    <input class="hidden" name="F_FecSur" value="<%=(Anio + "-" + Mes + "-" + i)%>">
                                    <%
                                        if (enRuta.equals("X")) {
                                    %>
                                    <input class="hidden" name="enRuta" value="X" />
                                    <%
                                    } else {
                                    %>
                                    <input class="hidden" name="enRuta" value="" />
                                    <%
                                        }
                                    %>
                                    <button class="btn btn-sm
                                            <%
                                                if (enRuta.equals("X")) {
                                                    out.println(" btn-info ");
                                                }
                                            %>
                                            " onclick="return agregaRuta(this.form)" name="accion" value="Agregar"><%=enRuta%></button>
                                </form>
                            </div>
                        </td>
                        <%
                                idReg++;
                            }
                        %>
                    </tr>
                    <%
                            }
                            con.cierraConexion();
                        } catch (Exception e) {

                        }
                    %>
                </tbody>
            </table>
            <%
                }
            %>
        </div>

        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/funcRutas.js"></script>
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
                                                $(document).ready(function() {
                                                    $('#tablaRuta').dataTable();
                                                });


                                                function SelectMuni(form) {
            <%
                try {
                    con.conectar();
                    ResultSet rset3 = con.consulta("select DISTINCT F_Juris from tb_detcli order by F_Juris");
                    while (rset3.next()) {
                        out.println("if (form.F_Juris.value == '" + rset3.getString(1) + "') {");
                        out.println("var select = document.getElementById('F_Muni');");
                        out.println("select.options.length = 0;");
                        ResultSet rset4 = con.consulta("select DISTINCT F_MunCli from tb_detcli where F_Juris = '" + rset3.getString(1) + "'");
                        while (rset4.next()) {
                            out.println("select.options[select.options.length] = new Option('" + rset4.getString(1) + "', '" + rset4.getString(1) + "');"
                            );
                        }
                        out.println("}");
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            %>
                                                }
        </script> 
    </body>
</html>
