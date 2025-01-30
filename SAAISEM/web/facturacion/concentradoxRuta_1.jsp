<%-- 
    Document   : concentradoxRuta
    Created on : 27/03/2015, 09:19:56 AM
    Author     : Americo
--%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <!-- Estilos CSS -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <%@include file="../jspf/menuPrincipal.jspf" %>

        <h4>Concentrados por Ruta</h4>
        
        <hr/>        
        <div class="row" id="divImagen">
            <div class="text-center">
                <img src="../imagenes/ajax-loader-1.gif" width="100" />
            </div>
        </div>
        <table class="table table-condensed table-striped table-bordered" id="tbConcentrados">
            <thead>
                <tr>
                    <td>Fecha</td>
                    <td>Imprimir Global</td>
                    <!--td>Enviar Módula</td-->
                    <td>Enviar Requerimientos Rurales</td>
                    <!--td>Enviar Requerimientos CSU y CEAPS</td-->
                    <td>Exportar CSR</td>
                    <td>Concentrado CSU</td>
                    <td>Concentrado CEAPS</td>
                    <td>Exportar Global</td>
                </tr>
            </thead>
            <tbody>
                <%                    
                    try {
                        con.conectar();
                        int CSU=0,CEAPS=0,CSR=0;
                        ResultSet rset = con.consulta("select F_FecEnt, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') as F_Fecha,F_FecEnt from tb_factura WHERE F_StsFact='A' group by F_Fecent, F_StsFact");
                        while (rset.next()) {
                            ResultSet ContarCSR = con.consulta("SELECT COUNT(F.F_ClaCli) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt='"+rset.getString(3)+"' AND U.F_Tipo='RURAL'");
                            if(ContarCSR.next()){
                                CSR = ContarCSR.getInt(1);
                            }
                            ResultSet ContarCSU = con.consulta("SELECT COUNT(F.F_ClaCli) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt='"+rset.getString(3)+"' AND U.F_Tipo='CSU'");
                            if(ContarCSU.next()){
                                CSU = ContarCSU.getInt(1);
                            }
                            ResultSet ContarCSC = con.consulta("SELECT COUNT(F.F_ClaCli) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt='"+rset.getString(3)+"' AND U.F_Tipo='CEAPS'");
                            if(ContarCSC.next()){
                                CEAPS = ContarCSC.getInt(1);
                            }
                %>
                <tr>
                    <td><h5>Concentrado del <%=rset.getString("F_Fecha")%></h5></td>
                    <td><a class="btn btn-block btn-primary btn-sm" target="_blank" href="../reimpRutaConcentrado.jsp?F_FecSur=<%//=rset.getString("F_FecEnt")%>"><span class="glyphicon glyphicon-print"></span></a></td>
                    <!--td><button class="btn btn-block btn-info btn-sm"><span class="glyphicon glyphicon-upload"></span></button></td-->
                    
                    <!--td>
                        <form action="../FacturacionManual" method="post" onsubmit="muestraImagen()">
                            <input class="hidden" name="F_FecEnt" value="<%//=rset.getString("F_FecEnt")%>">
                            <button id="btnConct" class="btn btn-block btn-info btn-sm" name="accion" value="ReenviarConcentradoRuta" onclick="return confirm('Seguro de Reenviar este concentrado de Rurales?'); muestraImagen()"><span class="glyphicon glyphicon-upload"></span></button>

                        </form>
                    </td-->                    
                    <td>
                        <%
                        if(CSR>0){
                        %>
                        <form action="../FacturacionManual" method="post" onsubmit="muestraImagen()">
                            <input class="hidden" name="F_FecEnt" value="<%//=rset.getString("F_FecEnt")%>">
                            <button id="btnFacts" class="btn btn-block btn-info btn-sm" name="accion" value="ReenviarConcentradoRequerimientos" onclick="return confirm('Seguro de Reenviar estas remisiones RURALES?'); muestraImagen()"><span class="glyphicon glyphicon-upload"></span></button>

                        </form>
                            <%}%>
                    </td>
                    <!--td>
                        <form action="../FacturacionManual" method="post" onsubmit="muestraImagen()">
                            <input class="hidden" name="F_FecEnt" value="<%//=rset.getString("F_FecEnt")%>">
                            <button id="btnFacts" class="btn btn-block btn-info btn-sm" name="accion" value="ReenviarConcentradoRequerimientosCSU" onclick="return confirm('Seguro de Reenviar estas remisiones CSU y CEAPS?'); muestraImagen()"><span class="glyphicon glyphicon-upload"></span></button>
                        </form>
                    </td-->
                    <td>
                        <%
                        if(CSR>0){
                        %>
                        <a href="ExporConcentrado.jsp?Fecha=<%//=rset.getString("F_FecEnt")%>&Ban=1">
                           <button id="btnFacts" class="btn btn-block btn-primary btn-sm" ><span class="glyphicon glyphicon-export"></span></button> 
                        </a>
                           <%}%>
                    </td>
                    <td>
                        <%
                        if(CSU>0){
                        %>
                        <a href="ExporConcentrado.jsp?Fecha=<%//=rset.getString("F_FecEnt")%>&Ban=2">
                           <button id="btnFacts" class="btn btn-block btn-primary btn-sm" ><span class="glyphicon glyphicon-export"></span></button> 
                        </a>
                           <%}%>
                    </td>
                    <td>
                        <%
                        if(CEAPS>0){
                        %>
                        <a href="ExporConcentrado.jsp?Fecha=<%//=rset.getString("F_FecEnt")%>&Ban=3">
                           <button id="btnFacts" class="btn btn-block btn-primary btn-sm" ><span class="glyphicon glyphicon-export"></span></button> 
                        </a>
                           <%}%>
                    </td>
                    <td>                        
                        <a href="ExporConcentrado.jsp?Fecha=<%//=rset.getString("F_FecEnt")%>&Ban=0">
                           <button id="btnFacts" class="btn btn-block btn-info btn-sm" ><span class="glyphicon glyphicon-export"></span></button> 
                        </a>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                %>
            </tbody>
        </table>

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->

        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
                                $(document).ready(function () {
                                    $('#tbConcentrados').dataTable();
                                });
                                
                                $('#divImagen').toggle();
                                
                                function muestraImagen(){
                                    //$('#btnFacts').attr('disabled', true);
                                    //$('#btnConct').attr('disabled', true);
                                    $('#divImagen').toggle();
                                }
        </script>
    </body>
</html>
