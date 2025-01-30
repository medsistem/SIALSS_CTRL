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
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();

    
   
    
    
    String fecha_ini="",fecha_fin="";
    try {
        fecha_ini = request.getParameter("fecha_ini");        
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }
    if(fecha_ini==null){
        fecha_ini="";
    }
    if(fecha_fin==null){
        fecha_fin="";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>
            
            <div class="panel-heading">
                <h3 class="panel-title">Inventarios Unidades</h3>
            </div>
            <form action="InvUnidades.jsp" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                    </div>                      
                </div>   
            </div>
                
                <div class="panel-body">
                    <div class="row">
                            <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
            <%
            int Contar=0;
            try {
                con.conectar();
                try {
                    ResultSet rset = null;
                    if(fecha_ini !="" && fecha_fin !=""){
                        fecha_ini= fecha_ini+" 00:00:00";
                        fecha_fin= fecha_fin+" 23:59:59";
                        rset = con.consulta("select COUNT(cla_mod) FROM inventarios WHERE hora_ini BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"';");
                        if (rset.next()) {
                            Contar = rset.getInt(1);
                        }    
                    }else{
                        Contar = 0;
                    }
                    
                } catch (Exception e) {

                }
                con.cierraConexion();
            } catch (Exception e) {

            }

            %>
            
                <%
            System.out.println("Contar: "+Contar);
                if(Contar > 0){
                %>
                <div class="row">
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />                    
                    <a class="btn btn-block btn-info" href="gnrReportInvUni.jsp?fecha_ini=<%=fecha_ini%>&fecha_fin=<%=fecha_fin%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
                    <br />                    
                </div>
                    <%}%>
                    
                    
                <div>
                    
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                    <tr>                                        
                                        <td>Clave</td>
                                        <td>Nombre</td>
                                        <td>Cantidad</td>                                        
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                ResultSet rset = null;
                                                ResultSet RsetDatos = null;
                                                int Cant=0,tipo_mod=0,Total=0;
                                                String DesUni="",des_uni="",cla_mod="";
                                                if(fecha_ini !="" && fecha_fin !=""){   
                                                    fecha_ini= fecha_ini+" 00:00:00";
                                                    fecha_fin= fecha_fin+" 23:59:59";
                                                    rset = con.consulta("SELECT cla_mod,tipo_mod FROM inventarios WHERE hora_ini BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' GROUP BY cla_mod,tipo_mod;");
                                                    while (rset.next()) {
                                                        cla_mod = rset.getString(1);
                                                        tipo_mod = rset.getInt(2);
                                                        
                                                        if(tipo_mod == 1){
                                                            DesUni=" (Dispensario)";
                                                        }else{
                                                            DesUni=" (Almacén)";
                                                        }
                                                      ResultSet DatosU = con.consulta("SELECT des_uni FROM unidades WHERE cla_mod='"+rset.getString(1)+"';");
                                                      if(DatosU.next()){
                                                          des_uni = DatosU.getString(1);
                                                      }
                                                        des_uni = des_uni+" "+DesUni;
                                                     
                                                     ResultSet DatosI = con.consulta("SELECT SUM(cant) FROM inventarios i INNER JOIN det_inv d ON i._id=d.id_inv WHERE hora_ini BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND i.cla_mod='"+rset.getString(1)+"' AND tipo_mod='"+rset.getString(2)+"';");
                                                     if(DatosI.next()){
                                                         Total = DatosI.getInt(1);
                                                     }
                                                     
                                   %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=des_uni%></td>
                                        <td><%=Total%></td>                                        
                                    </tr>
                                    <%                                                    
                                               
                                                }
                                     %>
                                     
                                     <%
                                                  Total=0;
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
            
        </div>

        
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function() {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });            
        </script>
        <script>
    
    </script>
          
        
    </body>
</html>

