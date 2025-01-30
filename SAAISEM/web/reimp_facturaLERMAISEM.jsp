<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB_Linux conLer = new ConectionDB_Linux();   
    ConectionDB con = new ConectionDB();   

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

            <div class="row">
                <h3 class="col-sm-4">Administrar OC</h3>
            </div>
            <form name="forma1" id="forma1" action="EnvioFolioLerma" method="post">
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>                                    
                                    <td>No. OC</td>
                                    <td>Nombre Proveedor</td>
                                    <!--td>Folio</td--> 
                                    <td>Enviar</td>                                                                        
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        conLer.conectar();
                                        String Folios="",Folio="";
                                        try {
                                            ResultSet RsetFol = con.consulta("SELECT F_NoCompra FROM tb_pedidoisem GROUP BY F_NoCompra;");
                                            while(RsetFol.next()){                                                
                                                
                                                Folio = Folio+RsetFol.getString(1)+"','";
                                            }
                                            Folios = Folio.substring(0,Folio.length()-3);
                                            
                                            //System.out.print("Folio:"+Folios);
                                            ResultSet rset = conLer.consulta("SELECT F_NoCompra,F_ClaProve,F_NomPro FROM tb_pedidoisem P INNER JOIN tb_proveedor PRO ON P.F_Provee=PRO.F_ClaProve WHERE F_Cedis='SENDERO' AND F_StsPed='1' AND F_NoCompra NOT IN ('"+Folios+"') GROUP BY F_NoCompra,F_ClaProve ORDER BY F_NoCompra+0;");
                                            while (rset.next()) {                                                
                                %>
                                <tr>
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <!--td>
                                        <a href="reimpFacturaLERMA.jsp?fol_gnkl=<%//=rset.getString(1)%>" target="_blank" class="btn btn-block btn-primary"><span class="glyphicon glyphicon-print"></span></a>                                        
                                    </td-->
                                    <td>
                                        <!--form class="form-horizontal" role="form" name="formulario_receta" id="formulario_receta" method="get" action="EnvioFolioLerma"-->   
                                        <form action="EnvioFolioLerma" method="post">
                                            <input class="hidden" id="FolioS" name="FolioS" value="<%=rset.getString(1)%>" />
                                            <input class="hidden" id="ClaProvee" name="ClaProvee" value="<%=rset.getString(2)%>" />
                                            <input class="hidden" id="Provee" name="Provee" value="<%=rset.getString(3)%>" />
                                            <button class="btn btn-block btn-info" id="accion" name="accion" value="EnvioIsem" onclick="return confirm('¿Esta Ud. Seguro de enviar la información?')"><span class="glyphicon glyphicon-ok"></span></button>
                                        </form>
                                        <!--/form-->
                                    </td>
                                    
                                    
                                </tr>
                                <%
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e);

                                        }
                                        con.cierraConexion();
                                        conLer.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
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
        <script>
                                                $(document).ready(function () {
                                                    $('#datosCompras').dataTable();
                                                });
        </script>
        <script>
            $(function () {
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });
             
            
        </script>
    </body>
</html>
