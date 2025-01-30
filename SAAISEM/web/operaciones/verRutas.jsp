<%-- 
    Document   : verRutas
    Created on : 18/12/2014, 03:33:29 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%    DecimalFormat formatter = new DecimalFormat("000");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
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
    String JurisUsu = "";
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
        F_Juris = formatter.format(Integer.parseInt(F_Juris));

    } catch (Exception e) {
    }

    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "' ");
        while (rset.next()) {
            JurisUsu = rset.getString("F_Juris");

        }
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

            <!--%@include file="../jspf/menuPrincipal.jspf" %-->
            <div class="row">
                <form action="verRutas.jsp" method="post">
                    <h4 class="col-sm-2">Seleccione Ruta:</h4>
                    <div class="col-sm-2">
                        <select name="F_Juris" class="form-control" onchange="SelectMuni(this.form);" required>
                            <option>Seleccione</option>
                            <%                            for (int i = 1; i <= 18; i++) {
                            %>
                            <option value="<%=i%>"><%=i%></option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <h4 class="col-sm-2">Seleccione Año:</h4>
                    <div class="col-sm-2">
                        <select name="F_Anio" class="form-control" required>
                            <option>Seleccione</option>
                            <%                            for (int i = 2014; i <= 2020; i++) {
                            %>
                            <option value="<%=i%>"><%=i%></option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <h4 class="col-sm-2">Seleccione el Mes:</h4>
                    <div class="col-sm-2">
                        <select name="F_Mes" class="form-control" required>
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
            <h2>Ruta <%=F_Juris%> - <%=MesLetra%> <%=F_Anio%> </h2>
            <%
                } catch (Exception e) {

                }
            %>
            <table class="table table-condensed table-striped table-bordered" id="tablaRuta">
                <thead>
                    <%
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
                            F_Juris = formatter.format(Integer.parseInt(F_Juris));
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
                        ResultSet rset = con.consulta("SELECT	u.F_ClaCli,	u.F_NomCli,	fr.F_LocPlano,	u.F_ClaJur,	fr.F_Ruta,	d.F_ModCli,	d.F_MunCli FROM	tb_uniatn u,	tb_detcli d,	tb_fecharuta fr WHERE u.F_ClaCli = d.F_ClaCli AND fr.F_ClaUni = d.F_ClaCli AND fr.F_Ruta LIKE '%" + F_Juris + "%' AND u.F_StsCli = 'A' and u.F_ClaJurNum in (" + JurisUsu + ") group by u.F_ClaCli ORDER BY	d.F_Ruta");
                        while (rset.next()) {
                            ResultSet rsetCab = con.consulta("select F_Ruta, F_LocPlano from tb_fecharuta where F_ClaUni = '" + rset.getString("F_ClaCli") + "' and MONTH(F_Fecha) = '" + (Integer.parseInt(F_Mes)+1) + "' and YEAR(F_Fecha) = '" + F_Anio + "' ");
                            String F_Ruta = "";
                            String F_LocPlano = "";
                            while (rsetCab.next()) {
                                F_Ruta = rsetCab.getString("F_Ruta");
                                F_LocPlano = rsetCab.getString("F_LocPlano");
                            }
                    %>
                    <tr>
                        <td><small><%=F_Ruta%></small></td>
                        <td><small><%=F_LocPlano%></small></td>
                        <td><small><%=rset.getString("F_ClaJur")%></small></td>
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
                                    if (enRuta.equals("X")) {
                                        out.println("X");
                                    }

                                %>
                                </form>
                            </div>
                        </td>
                        <%                                idReg++;
                            }
                        %>
                    </tr>
                    <%
                            }
                            con.cierraConexion();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
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
        </script> 
    </body>
</html>
