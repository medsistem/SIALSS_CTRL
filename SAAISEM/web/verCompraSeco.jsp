<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("F_FecApl");
        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
         
            <hr/>

            <div>
               <div class="panel panel-heading">
                    <h3>Ver Compras </h3>
                    <div class="nav navbar-nav navbar-right">                  
                     <% if (!tipo.equals("13")) { %>
                        <a href="reimpresionseco.jsp" class="btn btn-primary text-right">Regresar</a>
                        <% } else { %>
                        <a href="reimpresionCliente.jsp" class="btn btn-primary ">Regresar</a>
                        <% } %>
                    
                    </div>
                     <%
                        try {
                            con.conectar();
                            try {
                                ResultSet rset = con.consulta("SELECT P.F_NomPro, C.F_OrdCom ,C.F_FolRemi FROM  tb_compra C  INNER JOIN tb_proveedor P ON C.F_ProVee = P.F_ClaProve WHERE C.F_OrdCom = '" + request.getParameter("F_OrdCom") + "' AND (C.F_FolRemi = '" + request.getParameter("F_FolRemi") + "' || C.F_FolFac = '" + request.getParameter("F_FolRemi") + "')  GROUP BY P.F_NomPro;");
                                if (rset.next()) {
                    %>
                    <h4>Documento de Compra: <%=request.getParameter("fol_gnkl")%></h4>
                    <h4>Proveedor: <%=rset.getString(1)%></h4>
                    <h4>Remision: <%=rset.getString(3)%></h4>
                   
                    <%
                                }
                            } catch (Exception e) {

                            }
                            con.cierraConexion();
                        } catch (Exception e) {

                        }
                    %>
                    
                </div>
            
                <div class="panel panel-info">
                    <div class="panel-body">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <th>Clave</th>
                                    <th>Descripción</th>
                                    <th>Lote</th>
                                    <th>Caducidad</th>
                                    <th>Tarimas</th>
                                    <th>Cajas</th>
                                    <th>Presentación</th>
                                    <th class="text-center">Costo Unitario</th>
                                    <th>Importe</th>  
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            System.out.println(" fecha" + request.getParameter("F_FecApl"));
                                      //  ResultSet rset = con.consulta("SELECT P.F_NomPro, C.F_ClaDoc, C.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y')   F_FecCad, C.F_Resto, C.F_Cajas, C.F_Resto, C.F_CanCom, C.F_Costo, C.F_ComTot, (@csum:= @csum+F_ComTot) AS totales FROM (SELECT @csum := 0) r, tb_compra C INNER JOIN tb_lote L ON C.F_Lote = L.F_FolLot INNER JOIN tb_medica M ON C.F_ClaPro = M.F_ClaPro INNER JOIN tb_proveedor P ON C.F_ProVee = P.F_ClaProve WHERE C.F_OrdCom = '" + request.getParameter("F_OrdCom") + "' and C.F_FolRemi = '"+request.getParameter("F_FolRemi")+"' GROUP BY L.F_ClaLot, F_FecCad;");
                                          ResultSet rset = con.consulta("SELECT C.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y')   F_FecCad,  (C.F_Tarimas+c.F_TarimasI)as Tarimas,IF (C.F_Resto>0,(C.F_Cajas + C.F_CajasI)+1,(C.F_Cajas + C.F_CajasI)) AS F_Cajas, C.F_Pz, C.F_CanCom, C.F_Costo, C.F_ComTot, (@csum:= @csum+F_ComTot) AS totales FROM (SELECT @csum := 0) r, tb_compra C INNER JOIN tb_lote L ON C.F_Lote = L.F_FolLot INNER JOIN tb_medica M ON C.F_ClaPro = M.F_ClaPro INNER JOIN tb_proveedor P ON C.F_ProVee = P.F_ClaProve WHERE C.F_OrdCom = '" + request.getParameter("F_OrdCom") + "' and (C.F_FolRemi = '" + request.getParameter("F_FolRemi") + "' || C.F_FolFac = '" + request.getParameter("F_FolRemi") + "')  AND C.F_ClaDoc = '"+request.getParameter("fol_gnkl")+"' GROUP BY C.F_IdCom;");
                                           
                                          while (rset.next()) {
                                %>
                                <tr>
                                    <td class="text-center"><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%></td>
                                    <td class="text-center"><%=rset.getString(3)%></td>
                                    <td class="text-center"><%=rset.getString(4)%></td>
                                    <td class="text-center"><%=rset.getString(5)%></td>
                                    <td class="text-center"><%=rset.getString(6)%></td>
                                    <td class="text-center"><%=rset.getString(7)%></td>
                                    <td class="text-center"><%=rset.getString(9)%></td>
                                    <td class="text-center"><%=rset.getString(10)%></td>
                                    <td class="text-center"><%=rset.getString(11)%></td>
                                </tr>
                                <%
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
        </div>
        <%@include file="jspf/piePagina.jspf" %>
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
    $(document).ready(function() {
        $('#datosCompras').dataTable();
    });
</script>
<script>
    $(function() {
        $("#fecha").datepicker();
        $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
    });
</script>