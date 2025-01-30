
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<%@page import="conn.*"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../jspf/header.jspf" %>

<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    
    HttpSession sesion1 = request.getSession();
    String usu = "";
    String tipo1 = "";
    
    if (sesion.getAttribute("nombre") != null) {
        usu = (String) sesion1.getAttribute("nombre");
        tipo1 = (String) sesion1.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    
    String fecha_ini = "", fecha_fin = "", Proyecto = "", DesProyecto = "", ProyS="";
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        Proyecto = request.getParameter("Proyecto");
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
    int proy = Integer.parseInt(Proyecto);
     try {
        con.conectar();
        ResultSet rset = con.consulta("SELECT F_DesProy FROM tb_proyectos WHERE F_Id='" + Proyecto + "' ;");
        while (rset.next()) {
            DesProyecto = rset.getString(1);
        }
        con.cierraConexion();
    } catch (Exception e) {
        out.println(e.getMessage());
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <%@include file="../jspf/librerias_css.jspf" %>
        <!-- Estilos CSS -->
       
        <link href="../css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <link href="../css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>
        <!---->
    </head>
    <body>
        <div class="container" >
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="../jspf/menuPrincipal.jspf" %>
        </div> 
        <div class="container" >
            <div class="panel panel-primary" >
                <div class="panel-heading" >
                    <h4 class="text-center" > OC CERRADAS MULTIPROYECTOS  </h4>
                </div>
            </div>    
            <div class="panel panel-info" >
                <form action="BajaOrdenCerrada.jsp" id="formdownOc" method="post">
                    <div class="panel-body">
                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-3">
                            <select name="Proyecto" id="Proyecto" class="form-control">
                                <option value="0">--Seleccione--</option>
                                <%                                    
                                    try {
                                        con.conectar();
                                        ResultSet ProyectoS = con.consulta("SELECT * FROM tb_proyectos order by F_DesProy;");
                                        while (ProyectoS.next()) {
                                %>
                                <option value="<%=ProyectoS.getString(1)%>"><%=ProyectoS.getString(2)%></option>
                                ProyS = ProyectoS.getString(2);
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-primary btn-block " onclick="" name="BuscaOCCerrada0" value="BuscaOCCerrada0"> Buscar</button>
                        </div>
                        <div class="col-sm-2">
                            <a href="gnroccerrada.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>&Proyecto=<%=Proyecto%>&DesProyecto=<%=DesProyecto%>" class="btn btn-primary btn-block ">Descargar <span class="glyphicon glyphicon-download-alt"/> </a>
                        </div>    
                    </div>
                </form>
                <div class="panel-footer">
                    <form method="post" id="TbformdownOc">
                        <div class="table table-responsive" style="overflow:scroll;">
                            <table class="table table-striped table-bordered table-condensed"  id="datosOcCerrada">
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
                                     while (rs.next()) {
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
        </div>
        <div class="container" >
            <%@include file="../jspf/piePagina.jspf" %>
            <%@include file="../jspf/librerias_js.jspf" %>
        </div>
        <script src="../js/dataTables.bootstrap.js" type="text/javascript"></script>
        <script src="../js/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="../js/jquery.dataTables.js" type="text/javascript"></script>
        <script>
            $(document).ready(function () {
                $('#datosOcCerrada').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            }
        </script>                             
    </body>
</html>
