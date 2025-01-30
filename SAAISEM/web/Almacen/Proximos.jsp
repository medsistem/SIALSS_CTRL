
<%@page import="java.util.Date"%>
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
    
    String Proyecto = "", DesProyecto = "";
    try {
        Proyecto = request.getParameter("Proyecto");
    } catch (Exception e) {
        
    }
    
    if (Proyecto == null) {
        Proyecto = "0";
    }
    int proy = Integer.parseInt(Proyecto);
     java.util.Date fecha = new Date();
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
          
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" >
    </head>
    <body>
        <div class="container" >
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="../jspf/menuPrincipal.jspf"%>
        </div> 
        <div class="container" >
            <div class="panel-danger" >
                <div class="panel-heading" >
                    <h4 class="text-center" > PROXIMOS A CADUCAR </h4>
                </div>
            </div>    
            <div class="panel panel-info">
                <form action="Proximos.jsp" id="formdownOc" method="post">
                    <div class="panel panel-body">
                        <label for="Proyecto" class="col-sm-2 control-label">Proyectos:</label>
                        <div class="col-sm-3">
                            <select name="Proyecto" id="Proyecto" class="form-control">
                                <option value="">--Seleccione--</option>
                                <%                                    
                                    try {
                                        con.conectar();
                                        ResultSet Unidad = con.consulta("SELECT  l.F_Proyecto, pry.F_DesProy FROM tb_lote AS l INNER JOIN tb_proyectos AS pry ON l.F_Proyecto = pry.F_Id WHERE l.F_ExiLot > 0 GROUP BY l.F_Proyecto ORDER BY   pry.F_DesProy");
                                        while (Unidad.next()) {
                                %>
                                <option value="<%=Unidad.getString(1)%>"><%=Unidad.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-info btn-block glyphicon glyphicon-search" name="BuscaOCCerrada0" value="BuscaOCCerrada0" onclick="return (SelectVacios());"> Buscar</button>
                        </div>
                        <div class="col-sm-2">
                            <a href="gnrProximos.jsp?&Proyecto=<%=Proyecto%>&DesProyecto=<%=DesProyecto%>" class="btn btn-primary btn-blbtn-primaryock ">Descargar<span class="glyphicon glyphicon-download-alt"/> </a>
                        </div>    
                    </div>
                </form>
              </div>
                <div class="panel-footer">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td><strong>Fecha Consulta:</strong></td>
                                <td><strong><%=fecha%></strong></td>
                            </tr>
                            <tr>
                                <td>Proyecto</td>
                                <td><%=DesProyecto%></td>
                            </tr>
                            
                        </thead>
                    </table>
                        <div class="table table-responsive" style="overflow:auto;">
                            <table class="table table-bordered table-striped" id="tbProx">
                                <thead>
                                <th>CLAVE</th>
                                <th>DESCRIPCION</th>
                                <th>LOTE</th>
                                <th>CADUCIDAD</th>
                                <th>PRESENTACION</th>
                                <th>EXISTENCIA</th>
                                <th>UBICACION</th>
                                <th>ORIGEN</th>
                                <th>RE-UBICAR</th>
                              
                               
                                </thead>
                                <tbody>
                                    <% try {
                                         
                                            con.conectar();
                                            try {
                                               ResultSet rs = null;
                                           
                                              // rs = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FecCad, m.F_PrePro, l.F_ExiLot, l.F_Ubica, o.F_DesOri, 'PROXACADUCAR' FROM tb_lote AS l INNER JOIN tb_medica AS m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen AS o ON l.F_Origen = o.F_ClaOri WHERE l.F_ExiLot > 0 AND l.F_Ubica NOT IN ('CADUCADOS', 'MERMA', 'CROSSDOCKMORELIA', 'INGRESOS_V', 'CUARENTENA', 'AT', 'A0T', 'AT2', 'AT3', 'AT4', 'ATI', 'duplicado', 'CADUCADOSFARMACIA', 'PROXACADUCAR') AND l.F_FecCad BETWEEN CURDATE() AND (SELECT DATE_ADD(CURDATE() , INTERVAL 3 MONTH)) AND l.F_Proyecto = '"+ Proyecto +"' ORDER BY l.F_Origen ASC, l.F_ClaPro ASC, l.F_ClaLot ASC, l.F_FecCad ASC, l.F_Ubica ASC");
                                                rs = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FecCad, m.F_PrePro, l.F_ExiLot, u.F_DesUbi, o.F_DesOri, CASE 	WHEN u.F_DesUbi NOT LIKE '%-%'  THEN 'PROXACADUCAR' ELSE CONCAT(u.F_DesUbi,'-','PROXACADUCAR') END  FROM tb_lote AS l INNER JOIN tb_medica AS m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen AS o ON l.F_Origen = o.F_ClaOri INNER JOIN tb_proyectos AS pry ON l.F_Proyecto = pry.F_Id INNER JOIN tb_ubica AS u ON l.F_Ubica = u.F_ClaUbi WHERE l.F_ExiLot > 0 AND l.F_Ubica NOT IN ('CADUCADOS', 'MERMA', 'CROSSDOCKMORELIA', 'INGRESOS_V', 'CUARENTENA', 'AT', 'A0T', 'AT2', 'AT3', 'AT4', 'ATI', 'duplicado', 'CADUCADOSFARMACIA') AND l.F_Ubica NOT LIKE '%PROXACADUCAR%' AND l.F_FecCad BETWEEN CURDATE() AND (SELECT DATE_ADD(CURDATE() , INTERVAL 3 MONTH)) AND l.F_Proyecto = '"+ Proyecto +"' ORDER BY l.F_Origen ASC,l.F_ClaPro ASC,l.F_ClaLot ASC,l.F_FecCad ASC,l.F_Ubica ASC");
                                               while (rs.next()) {
                                         
                                              
                                    %>
                                    <tr>    
                                        <td><%out.println(rs.getString(1)); %></td>
                                        <td><%out.println(rs.getString(2));%></td>
                                        <td><%out.println(rs.getString(3));%></td>
                                        <td><%out.println(rs.getString(4));%></td>
                                        <td><%out.println(rs.getString(5));%></td>
                                        <td><%out.println(rs.getString(6));%></td>
                                        <td><%out.println(rs.getString(7));%></td>
                                        <td><%out.println(rs.getString(8));%></td>
                                        <td><%out.println(rs.getString(9));%></td>
                                        
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
                  
                </div>
           
        </div>
        <div class="container" >
            <%@include file="../jspf/piePagina.jspf" %>
          
        </div>
        <script src="../js/jquery-1.9.1.js" type="text/javascript"></script>
     
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script>
            $(document).ready(function () {
                $('#tbProx').dataTable();
            }
    
     function SelectVacios() {
                        var missinginfo = "";
                        if ($("#Proyecto").val() == "") {
                            missinginfo += "\nTE HA FALTADO SELECCIONAR UN PROYECTO:";
                        }
                          if (missinginfo != "") {
                            missinginfo = "\n SELECCIONA UN PROYECTO:\n";
                            alert(missinginfo);
                            return false;
                        } else {
                            return true;
                        }
        }
        </script>                             
    </body>
</html>
