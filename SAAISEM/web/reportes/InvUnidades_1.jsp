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
    ConectionInvUni con = new ConectionInvUni();

    
   
    
    
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
                        rset = con.consulta("SELECT count(I.F_ClaMod) FROM tb_inventarios I WHERE F_Fecha BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"';");
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
                                        <td>Fecha</td>
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
                                                String DesUni="",des_uni="";
                                                if(fecha_ini !="" && fecha_fin !=""){                        
                                                    rset = con.consulta("SELECT I.F_ClaMod,U.F_DesUni,DATE_FORMAT(I.F_Fecha,'%d/%m/%Y') AS F_Fecha,I.F_TipoInv,FORMAT(SUM(F_CantInv),0),SUM(F_CantInv) AS F_CantInv FROM tb_inventarios I INNER JOIN tb_detalleinv D ON I.F_IdInv=D.F_IdInv INNER JOIN tb_uni U ON I.F_ClaMod=U.F_ClaUni WHERE F_Fecha BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' GROUP BY I.F_ClaMod,U.F_DesUni,I.F_Fecha,I.F_TipoInv;");
                                                    while (rset.next()) {
                                                        Cant = rset.getInt(6);
                                                        Total = Total + Cant;
                                                        tipo_mod = rset.getInt(4);
                                                        
                                                        if(tipo_mod == 1){
                                                            DesUni=" (Dispensario)";
                                                        }else{
                                                            DesUni=" (Almacén)";
                                                        }
                                                      
                                                        des_uni = rset.getString(2)+DesUni;
                                                       
                                   %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=des_uni%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(5)%></td>                                        
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

