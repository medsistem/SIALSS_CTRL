<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%DecimalFormat format = new DecimalFormat("####,###");%>
<%

    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
String ubicaciones = "";
 int Parametro = 0;
    String fecha_ini = "", fecha_fin = "";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
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

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="panel-heading">
                <h3 class="panel-title">Parametro facturacón para Usuarios SIALSS_CTRL</h3>
            </div>
            <form action="ParametroCsrd" method="post">
             
                
            <div class="panel-body">  
                <div class="container">
                    <div class="row col-sm-12">
                        <%
                           

                              try {
                                con.conectar();
                             Parametro = 0;
                                String Proyecto = "", Parametro1 = "";
                                ResultSet Consulta = con.consulta("SELECT PU.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto, PU.F_Parametro FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE PU.F_Usuario = '" + usua + "';");
                                
                                if (Consulta.next()) {
                                    Parametro = Consulta.getInt(1);
                                    Parametro1 = Consulta.getString(3);
                                    Proyecto = Consulta.getString(2);
                                }
                        %>  
                        <div class="row">
                        <div class="col-sm-1">
                            <label>Parametro:</label>
                        </div>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" readonly="" id="Proyecto" name="Proyecto" value="<%=Parametro1%>" />
                        </div>
                        <div class="col-sm-4">
                            <select name="SltParametro" id="SltParametro" class="form-control">
                                <option value="0">-- Seleccione --</option>
                                <%
                                    Consulta = con.consulta("SELECT * FROM tb_ubicafact;");
                                    while (Consulta.next()) {
                                %>
                                <option value="<%=Consulta.getString(1)%>"><%=Consulta.getString(2)%></option>
                                <%
                                   
                                    }
                                                                     %>
                            </select>
                        </div>   
                        </div>    
                       
                         <br/>
                        <div class="row">
                        <div class="col-sm-1">
                            <label>Proyecto:</label>
                        </div>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" readonly="" id="Proyecto" name="Proyecto" value="<%=Proyecto%>" />
                        </div>
                        <div class="col-sm-4">
                            <select name="SltProyecto" id="SltProyecto" class="form-control">
                                <option value="0">-- Seleccione --</option>
                                <%
                                    Consulta = con.consulta("SELECT * FROM tb_proyectos;");
                                    while (Consulta.next()) {
                                %>
                                <option value="<%=Consulta.getString(1)%>"><%=Consulta.getString(2)%></option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        </div>    
                        <%
                                con.cierraConexion();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }%>
                    </div>
                </div>
                      </div>  
                <br />
                <div class="panel-footer">
                    <div class="row center-block">
                        <div class="col-sm-6">
                            <button class="btn btn-block btn-primary" id="accion" name="accion" value="ActualizarU" onclick="return confirma();">ACTUALIZAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                        </div>                           
                    </div>
                </div>  
              
                 <br/>
                <div class="panel panel-info">
                    <div class="table table-info table-responsive">
                        
                        
                        
                        <table class="table table-responsive table-bordered" id="tableparametrou">
                            <thead class="thead-dark">
                            <th scope="col">#</th>
                            <th scope="col">Ubicación</th> 
                            <th scope="col">Ubica Descripción </th>
                            <th scope="col">Codigo de Barras</th>
                            
                            </thead>
                            <tbody>
                                <% try {
                                con.conectar();
                                
                                ResultSet Consul0 = con.consulta("SELECT F_UbicaSQL FROM tb_ubicafact WHERE F_idUbicaFac = '"+  Parametro +"';");
                                int listado = 1;
                                while (Consul0.next()) {                                        
                                   ubicaciones = Consul0.getString(1);
                                   System.out.println("Ubicaciones:"+ubicaciones);
                                      }    
                                 ResultSet Consul = con.consulta("SELECT F_Ubica,u1.F_DesUbi, u1.F_Cb  FROM tb_lote l INNER JOIN tb_ubica u1 ON F_Ubica = F_ClaUbi  "+ ubicaciones +"  GROUP BY F_Ubica;");
                           
                               while (Consul.next()) {        
                          %>          
                                        

                                <tr>
                                    <td scope="row"><%= listado %></td>
                                    <td><%=Consul.getString(1)%></td>
                                    <td><%=Consul.getString(2)%></td>
                                    <td><%=Consul.getString(3)%></td>
                                </tr>

                            <%    listado = listado + 1;   
                                } 

                            } catch (Exception e) {
                                e.getMessage();
                            }
                        %>
                            </tbody>
                        </table>
                                                  
                    </div>
                </div>
                

                
                
                
            </form>
        </div>


        <%@include file="jspf/piePagina.jspf" %>
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
       
    </body>
</html>

