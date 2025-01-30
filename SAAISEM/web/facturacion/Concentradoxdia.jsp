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

     String fecha_ini="";
    try {
        fecha_ini = request.getParameter("fecha_ini");
    } catch (Exception e) {

    }
    if(fecha_ini==null){
        fecha_ini="";
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
                <h3 class="panel-title">Concentrado por Día Rurales</h3>
            </div>
            <form action="Concentradoxdia.jsp" method="post">
            <div class="panel-footer">
                <div class="row">
                    <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                    </div>
                              <div class="col-sm-4">
                            <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>         
                </div>   
            </div>
            </form>
            <%
            int Contar=0;
            try {
                con.conectar();
                try {
                    ResultSet rset = null;
                    if(fecha_ini !=""){                        
                            rset = con.consulta("SELECT ur.F_ClaPro,SUM(ur.F_PiezasReq) AS F_PiezasReq FROM tb_unireq ur INNER JOIN tb_uniatn un on ur.F_ClaUni=un.F_ClaCli WHERE F_Fecha='"+fecha_ini+"' AND F_Status='0' AND un.F_Tipo='3';");
                        
                        if (rset.next()) {
                            Contar = rset.getInt(2);
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
                    <a class="btn btn-block btn-info" href="gnrConcentrado.jsp?fecha_ini=<%=fecha_ini%>">Exportar<span class="glyphicon glyphicon-save"></span></a>
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
                                        <td>Descripción</td>
                                        <td>Cantidad Req.</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            try {
                                                ResultSet rset = null;
                                                int CantReq=0,CantSur=0;
                                                if(fecha_ini !=""){
                                                    rset = con.consulta("SELECT ur.F_ClaPro,m.F_DesPro,SUM(ur.F_PiezasReq) FROM tb_unireq ur INNER JOIN tb_uniatn un on ur.F_ClaUni=un.F_ClaCli INNER JOIN tb_medica m on ur.F_ClaPro=m.F_ClaPro WHERE F_Fecha='"+fecha_ini+"' AND F_Status='0' AND un.F_Tipo='3' GROUP BY ur.F_ClaPro;");
                                                    while (rset.next()) {                                                    
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%></td>
                                        <td><%=rset.getString(3)%></td>
                                    </tr>
                                    <%
                                                CantReq =CantReq + rset.getInt(3);
                                                }
                                     %>
                                     <tr>
                                        <td></td>
                                        <td>Total</td>
                                        <td><%=format.format(CantReq)%></td>
                                    </tr>
                                     <%
                                                    CantReq=0;
                                                    CantSur=0;
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
    <script type="text/javascript">
          $(function() {
               var availableTags = [
          <%
            try {
                con.conectar();
                try {
                    ResultSet rset = con.consulta("SELECT F_NomCli FROM tb_uniatn");
                    while (rset.next()) {
                        out.println("'" + rset.getString(1) + "',");
                    }
                } catch (Exception e) {

                }
                con.cierraConexion();
            } catch (Exception e) {

            }
        %>
               ];
               $("#NombreUnidad").autocomplete({
                   source: availableTags
               });
          });
        </script>        
        <script>
            $(document).ready(function(){
                $('#selectjur').change(function(){
                    var Juris = $('#juris').val();
                     var valor = $('#selectjur').val();
                     if(Juris !=""){
                     $('#juris').val(Juris+","+valor);
                     }else{
                         $('#juris').val(valor);
                     }
                 });
            });
       </script>
    </body>
</html>

