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
    
    Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema=formateador.format(fechaActual);
    int dia=0,mes=0,ano=0;
    String Fecha1="",Fecha2="",Fecha11="",Fecha22="",dia1="",mes1="";
    String Folio1="",Folio2="";
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB_Linux con = new ConectionDB_Linux();   

    try {

        con.conectar();
        
        
        ResultSet RFecha = con.consulta("SELECT DAY(CURDATE()) as dia,MONTH(CURDATE()) AS mes,YEAR(CURDATE()) AS ano,CURDATE() AS fecha;");
        if(RFecha.next()){
            /*dia = RFecha.getInt(1);
            mes = RFecha.getInt(2);
            ano = RFecha.getInt(3);*/
            Fecha22= RFecha.getString(4);
            /*if(mes<10){
                mes1 = "0"+mes;
            }else{
                mes1 = ""+mes;
            }
            if(dia>1){
                dia1 = "01";
            }else{
                dia1 = "01";
            }*/
            //Fecha11 = ano+"-"+mes1+"-"+dia1;
            
            Fecha11 = RFecha.getString(4);
                    
        }
        try{
            Fecha1 = (String) sesion.getAttribute("fecha_ini");
            Fecha2 = (String) sesion.getAttribute("fecha_fin");
            Folio1 = (String) sesion.getAttribute("folio1");
            Folio2 = (String) sesion.getAttribute("folio2");            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        /*
        Fecha1 = request.getParameter("fecha_ini");
        Fecha2 = request.getParameter("fecha_fin");        
        Folio1 = request.getParameter("folio1");
        Folio2 = request.getParameter("folio2");
        */
        if(Folio1 == null){Folio1="";}
        if(Folio2 == null){Folio2="";}
        if((Fecha1 =="") || (Fecha1 == null)){
            Fecha1 = Fecha11;
        }else{
            Fecha1 = Fecha1;
        }

        if((Fecha2 =="") || (Fecha2 == null)){
            Fecha2 = Fecha22;
        }else{
            Fecha2 = Fecha2;
        }
        
        con.cierraConexion();
    } catch (Exception e) {

    }
    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            orden_compra = request.getParameter("orden_compra");
            fecha = request.getParameter("fecha");
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

            <%@include file="jspf/menuPrincipal.jspf" %>

            <div class="row">
                <h3 class="col-sm-4">Administrar Remisiones LERMA</h3>
            </div>
            <form name="forma1" id="forma1" action="EnvioFolioLerma" method="post">
            <div class="panel-footer">
                <div class="row">                    
                    <label class="control-label col-sm-1" for="fecha_ini">Folios</label>
                    <div class="col-lg-1">
                        <input class="form-control" id="folio1" name="folio1" type="text" value="" onchange="habilitar(this.value);" />
                    </div>
                    <div class="col-lg-1">
                        <input class="form-control" id="folio2" name="folio2" type="text" value="" onchange="habilitar(this.value);"/>
                    </div>
                                                   
                    <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_ini" name="fecha_ini" value="<%=Fecha1%>" type="date" onchange="habilitar(this.value);"/>
                    </div>
                    <div class="col-sm-2">
                        <input class="form-control" id="fecha_fin" name="fecha_fin" value="<%=Fecha2%>" type="date" onchange="habilitar(this.value);"/>
                    </div>                    
                </div>   
            </div>
                    
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-8">
                            <button class="btn btn-block btn-primary" name="accion" value="mostrar" >MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                        </div>      
                    </div>
                </div>  
            <div>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>                                    
                                    <td>No. Folio</td>
                                    <td>Punto de Entrega</td>
                                    <td>Estatus</td>
                                    <td>Fec Ent</td>
                                    <td>Folio</td> 
                                    <td>Enviar</td>                                                                        
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        String F_StsFact="",F_FecEnt="",F_FecApl="",Query="",TipoU="";                                        
                                        try {
                                            System.out.println("F1"+Folio1+" F2:"+Folio2);
                                            if((Folio1 !="") && (Folio2 !="")){
                                                System.out.println("Entro Datos");
                                                Query = " AND F_ClaDoc BETWEEN '"+Folio1+"' AND '"+Folio2+"' " ;
                                            }else{
                                                System.out.println("Entro vacío");
                                                Query = "";
                                            }
                                            
                                            
                                            ResultSet rset = con.consulta("SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,F_StsFact,DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,o.F_Req,u.F_Tipo FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc=o.F_IdFact WHERE F_FecEnt BETWEEN '"+Fecha1+"' AND '"+Fecha2+"' "+Query+" AND f.F_ClaCli IN ('5000','S001','M001') GROUP BY F_ClaDoc,f.F_ClaCli,F_StsFact ORDER BY f.F_ClaDoc+0;");
                                            while (rset.next()) {
                                                F_StsFact = rset.getString("F_StsFact");
                                                F_FecApl = rset.getString("F_FecApl");
                                                String F_Req1 = rset.getString("F_Req");
                                                Date fechaDate1 = formateador.parse(F_FecApl);
                                                Date fechaDate2 = formateador.parse(fechaSistema);
                                                TipoU = rset.getString(8);
                                %>
                                <tr>
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%> - <%=rset.getString(3)%></td>
                                    <td><%=rset.getString("F_StsFact")%></td>
                                    <td><%=rset.getString("F_FecEnt")%></td>
                                    <td>
                                        <a href="reimpFacturaLERMA.jsp?fol_gnkl=<%=rset.getString(1)%>" target="_blank" class="btn btn-block btn-primary"><span class="glyphicon glyphicon-print"></span></a>                                        
                                    </td>
                                    <td>
                                        <!--form class="form-horizontal" role="form" name="formulario_receta" id="formulario_receta" method="get" action="EnvioFolioLerma"-->   
                                        <form action="EnvioFolioLerma" method="post">
                                            <input class="hidden" id="FolioS" name="FolioS" value="<%=rset.getString(1)%>" />
                                            <button class="btn btn-block btn-info" id="accion" name="accion" value="Envio" onclick="return confirm('¿Esta Ud. Seguro de enviar la información?')"><span class="glyphicon glyphicon-ok"></span></button>
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
