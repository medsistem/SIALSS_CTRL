<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "" ,Proyecto = "", DesProyecto = "";
    try {
       
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        Proyecto = request.getParameter("Proyecto");
        DesProyecto = request.getParameter("DesProyecto");
        
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    

    if (Proyecto == null) {
        Proyecto = "0";
    }

  
    if(Proyecto.equals("0")){
        DesProyecto = "TODOS";
    }else {
        DesProyecto = DesProyecto;
    }
    
   
    
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"OC_cerradas_"+DesProyecto+".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <div class="panel panel-info">
                <div class="panel-heading">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Fecha Consulta:</td>
                                <td colspan="2"><%=fecha_ini%> AL <%=fecha_fin%></td>
                            </tr>
                            <tr>
                                <td>Proyecto</td>
                                <td><%=DesProyecto%></td>
                            </tr>
                            
                        </thead>
                    </table>
                </div>
            </div>
            <br />
           
            <div class="panel panel-info">
                 <div class="panel-footer">
                    <form method="post" id="TbformdownOc">
                        <div class="table table-responsive" style=" overflow:auto;">
                            <table class="table table-bordered table-striped" id="datosOcCerrada">
                                <thead>
                                <th>Fecha</th>
                                <th>Claves</th>
                                <th>Ingresos</th>
                                <th>Orden de Compra</th>
                                <th>Solicitado</th>
                                <th>Faltante/sobrante</th>
                                <th>Status OC</th>
                                <th>Provedor</th>
                                <th>Orden de suministro</th>
                                <th>Proyecto</th>
                                </thead>
                                <tbody>
                                    <% try {
                                            
                                        
                                            con.conectar();
                                            try {
                                                ResultSet rs = null;
                                               
                                                rs = con.consulta("call sae_oc_Multi('" + fecha_ini + "','" + fecha_fin + "'," + Proyecto + ")");
                                            
                                        while(rs.next()){
                                           
                                            String fecha1 = rs.getString(1);
                                             
                                             
                                    %>
                                    <tr>    
                                        <td><%out.println(fecha1); %></td>
                                        <td><%out.println(rs.getString(2));%></td>
                                        <td><%out.println(rs.getString(3));%></td>
                                        <td><%out.println(rs.getString(4));%></td>
                                        <td><%out.println(rs.getString(5));%></td>
                                        <td><%out.println(rs.getString(6));%></td>
                                        <td><%out.println(rs.getString(7));%></td>
                                        <td><%out.println(rs.getString(8));%></td>
                                        <td><%out.println(rs.getString(10));%></td>
                                         <td><%out.println(rs.getString(11));%></td>
                                    </tr>
                                    <% 
                                            }//fin del while
                                            } catch (Exception e) {
                                                e.getMessage();
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </form>
                </div>
        </div>
       
    </body>
</html>