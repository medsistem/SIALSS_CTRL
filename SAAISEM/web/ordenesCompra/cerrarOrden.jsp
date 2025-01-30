<%-- 
    Document   : cerrarOrden
    Created on : 31/07/2019, 10:51:53 PM
    Author     : IngMa
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.medalfa.saa.querys.OrdenesCompraQuerys"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.medalfa.saa.db.Source"%>
<%@page import="com.medalfa.saa.db.ConnectionManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.medalfa.saa.utils.StaticText" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../jspf/header.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title>SIALSS_CTRL MULTI</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <%@include file="../jspf/librerias_css.jspf" %>
        
    </head>
    <body>
        <div class="container" >
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="../jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container" >
            <h4 class="text-center" > Cerrar Ordenes De Compra </h4>
            <br/>
            <br/>
            <br/>
            <div class="panel panel-primary" >
                <div class="panel-heading" >
                    <h4 class="text-center" > Cricterios de búsqueda </h4>
                </div>
                <div class="panel-body" >
                    <div class="row" >
                        <h4 class="col-sm-2" >Ordenes de compra</h4>
                        <div class="col-sm-3" >
                            <select class="form-control" id="selectOrdenCompra" >
                                <option value="" >---- Seleccione ----</option>
                                <%
                                    try{
                                        Connection con = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();
                                        PreparedStatement ps = con.prepareStatement(OrdenesCompraQuerys.OBTENER_ORDENES_COMPRA); 
                                        ResultSet rset = ps.executeQuery();
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString("F_NoCompra")%>" ><%=rset.getString("F_NoCompra")%></option>
                                <%    }
                                    } catch (SQLException ex) {
                                        Logger.getLogger("cerrarOrden.jsp").log(Level.SEVERE, String.format("m: '%s', sql: '%s'", ex.getMessage(), ex.getSQLState()), ex);
                                    } catch (NamingException ex) {
                                        Logger.getLogger("cerrarOrden.jsp").log(Level.SEVERE, null, ex);
                                    }
                                %>
                            </select>

                        </div>
                        <div class="col-sm-1" >
                            <button class="btn btn-block btn-info" type="button" id="searchByNoOrdenCOmpra" ><span class="glyphicon glyphicon-search" ></span></button>
                        </div>
                        <h4 class="col-sm-1" >Proveedor</h4>
                        <div class="col-sm-4" >
                            <select class="form-control" id="selectProveedor" >
                                <option value="" >---- Seleccione ----</option>
                                <%
                                    try{
                                        Connection con = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();
                                        PreparedStatement ps = con.prepareStatement(OrdenesCompraQuerys.OBTENER_PROVEEDORES_PEDIDOS);
                                        ResultSet rset = ps.executeQuery();
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString("F_Provee")%>" ><%=rset.getString("F_NomPro")%></option>
                                <%    }
                                    } catch (SQLException ex) {
                                        Logger.getLogger("cerrarOrden.jsp").log(Level.SEVERE, String.format("m: '%s', sql: '%s'", ex.getMessage(), ex.getSQLState()), ex);
                                    } catch (NamingException ex) {
                                        Logger.getLogger("cerrarOrden.jsp").log(Level.SEVERE, null, ex);
                                    }
                                %>
                            </select>

                        </div>
                        <div class="col-sm-1" >
                            <button class="btn btn-block btn-info" type="button" id="searchByProvider" ><span class="glyphicon glyphicon-search" ></span></button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-info" >
                 <div class="panel-heading" >
                   <h4 class="text-center" >Ordenes de compra</h4>
               </div>
                <div class="panel-body table-responsive" >
                    <div id="dynamic"></div>
                </div>
            </div>  
                            
           <div class="panel panel-info" >
               <div class="panel-heading" >
                   <h4 class="text-center" >Ordenes cerradas</h4>
               </div>
                <div class="panel-body table-responsive" >
                    <div id="dynamiCerradas"></div>
                </div>
            </div>                            


            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"></h4>
                        </div>
                        <div class="modal-body">
                            <div class="text-center" id="imagenCarga">
                                <img src="${generalContext}/imagenes/ajax-loader-1.gif" alt="" />
                            </div>
                        </div>
                        <div class="modal-footer">
                        </div>
                    </div>
                </div>
            </div>
        </div>  

        <%@include file="../jspf/piePagina.jspf" %>
        <%@include file="../jspf/librerias_js.jspf" %>
        <script>
            var porNoOrden = <%=StaticText.OBTENER_POR_ORDEN_DE_COMPRA%>
            var porProveedor = <%=StaticText.OBTENER_POR_PROVEEDOR%>
            var closeOrder = <%=StaticText.CERRAR_ORDEN%>
            var reporteOrdenes = <%=StaticText.REPORTE_ORDENES_CERRADAS%>
        </script>
        <script src="${generalContext}/js/ordenesCompra/cerrarOrdenCompra.js" ></script>
    </body>
</html>
