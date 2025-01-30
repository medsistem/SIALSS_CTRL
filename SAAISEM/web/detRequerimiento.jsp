<%-- 
    Document   : detRequerimiento
    Created on : 28-ene-2015, 13:14:30
    Author     : amerikillo
--%>


<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        Clave = request.getParameter("F_ClaUni");
    } catch (Exception e) {

    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body class="container">

        <h1>SIALSS_CTRL</h1>
        <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

        <h4>
            Unidad: 
            <%
                String F_NomCli = "", F_CantSol = "";
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select  F_NomCli from tb_uniatn where F_ClaCli = '" + Clave + "'");
                    while (rset.next()) {
                        F_NomCli = rset.getString("F_NomCli");
                    }

                    rset = con.consulta("SELECT M.F_ClaPro,M.F_DesPro,REQ.F_CajasReq, SUM(REQ.F_PiezasReq) as F_PiezasReq FROM tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro=M.F_ClaPro WHERE F_ClaUni='" + Clave + "' and F_Status =0 and F_PiezasReq != 0");
                    while (rset.next()) {
                        F_CantSol = rset.getString("F_PiezasReq");
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            %>
            <%=Clave%> | <%=F_NomCli%>
        </h4>
        <h4>Total de Piezas: <%=formatter.format(Integer.parseInt(F_CantSol))%></h4>
        <h4><%
            try {
            %>
            Fechas: <%=df3.format(df2.parse(request.getParameter("F_FecEnt")))%>
            <%
                } catch (Exception e) {

                }
            %>
        </h4>
        <form action="<%=request.getParameter("pagina")%>" method="post">
            <input type="text" class="hidden" id="F_Ruta" name="F_Ruta" value="<%=request.getParameter("F_Ruta")%>" />
            <input type="text" class="hidden" id="F_Mes" name="F_Mes" value="<%=request.getParameter("F_Mes")%>" />
            <button class="btn btn-default" type="submit">Regresar</button>
        </form>
        <form action="RequerimientosUnidades" method="post" >
            <input type="text" class="hidden" id="F_Ruta" name="F_Ruta" value="<%=request.getParameter("F_Ruta")%>" />
            <input type="text" class="hidden" id="F_Mes" name="F_Mes" value="<%=request.getParameter("F_Mes")%>" />
            <input name="F_ClaUni" class="hidden" value="<%=Clave%>" />
            <input name="pagina" class="hidden" value="<%=request.getParameter("pagina")%>" />
            <div class="row">
                <div class="col-sm-2 col-sm-offset-10">
                    <button class="btn btn-info btn-block" name="accion" value="actualizaRequerimiento">Actualizar</button>
                </div>
            </div>
            <br/>
            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed" id="datosProv">
                <tr>
                    <td>Clave</td>
                    <td>Descripción</td>
                    <td>Piezas Sol</td>
                    <td>Piezas Sur</td>
                    <td>Existencia</td>
                </tr>
                <%
                    try {
                        int ExiLot = 0, ExiSol=0, TotalSur=0;
                        con.conectar();
                        ResultSet rset = con.consulta("SELECT M.F_ClaPro,M.F_DesPro,REQ.F_CajasReq, REQ.F_PiezasReq FROM tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro=M.F_ClaPro WHERE F_ClaUni='" + Clave + "' and F_Status =0 and F_PiezasReq != 0");
                        while (rset.next()) {
                            
                            ResultSet rset2 = con.consulta("select sum(F_ExiLot) from tb_lote where F_ClaPro='" + rset.getString(1) + "'");
                            while (rset2.next()) {
                                ExiLot = rset2.getInt(1);
                            }
                %>
                <tr
                    <%
                        if (rset.getInt(4) > ExiLot) {
                            out.println("class='danger'");
                            ExiSol = ExiLot;
                        }else{
                            ExiSol = rset.getInt(4);
                        }
                        TotalSur = TotalSur + ExiSol;
                        
                    %>
                    >
                    <td><%=rset.getString(1)%></td>
                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getInt(4)%></td>
                    <td ><small><input name="<%=rset.getString(1).trim()%>" type="number" class="text-right form-control" value="<%=ExiSol%>" /></small></td>
                    <td class="text-right"><%=formatter.format(ExiLot)%></td>
                </tr>                 
                <%
                        }
                 %>
                 <h4>Total de Piezas a Facturar: <%=formatter.format(TotalSur)%></h4>
                 <%
                        con.cierraConexion();
                    } catch (Exception e) {
                    }
                %>
               
            </table>            
        </form>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
    </body>
</html>
