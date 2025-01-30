<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

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
    String tipo = "",F_Status="",Secu1="",Secu2="";
    String fecha_ini="", fecha_fin="",radio="",F_FolCon="",Folio="",secuencial1="",secuencial2="",Folio1="",radio1="";
    int F_Idsur=0,F_IdePro=0,F_Cvesum=0,F_Punto=0,F_Con=0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        radio1 = (String) sesion.getAttribute("radio");       
        Folio1 = (String) sesion.getAttribute("folio");       
        Secu1 = (String) sesion.getAttribute("secu1");       
        Secu2 = (String) sesion.getAttribute("secu2");       
    } else {
        response.sendRedirect("index.jsp");
    }
    System.out.println("s1="+Secu1+" s6="+Secu2+" rd="+radio1+" foli="+Folio1);
    try {
        secuencial1 = request.getParameter("secuencial1");        
        secuencial2 = request.getParameter("secuencial2");    
        radio = request.getParameter("radio");       
        Folio = request.getParameter("folios");       
    } catch (Exception e) {

    }
System.out.println("SEc1"+secuencial1+" Sec2"+secuencial2+" Folio"+Folio);
    if(secuencial1==null){
        if(Secu1 == null){
        secuencial1 = "";
        }else{
        secuencial1 = Secu1;
        }
    }
    if(secuencial2==null){
        if(Secu2 == null){
        secuencial2 = "";
        }else{        
        secuencial2 = Secu2;
        }
    }
    if(Folio==null){
        if(Folio1 == null){
        Folio = "";
        }else{
        Folio = Folio1;
        }        
    }
    if(radio==null){
        if(radio1 == null){
        radio = "";
        }else{
        radio= radio1;
        }        
    }    
    
    
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
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Cancelar Secuenciales</h3>
                </div>
                
                <form action="CancelarSec.jsp" method="post">
                        <div class="panel-footer">
                            <div class="row">                                    
                                                                                      
                                <label class="control-label col-lg-2" for="fecha_ini"><input type="radio" id="radio" name="radio" value="si" onchange="habilitar(this.value);" checked >Por Secuencial</label>
                                <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_ini" name="fecha_ini" data-date-format="DD/MM/YYYY"  value="" /-->
                                    <input class="form-control" id="secuencial1" name="secuencial1" type="text" value="<%=secuencial1%>" />
                                </div>
                                <div class="col-lg-2">
                                    <!--input class="form-control" id="fecha_fin" name="fecha_fin" data-date-format="dd/mm/yyyy"  value="" /-->
                                    <input class="form-control" id="secuencial2" name="secuencial2" type="text" value="<%=secuencial2%>" />
                                </div>
                            </div>
                            <div class="row">                                    
                                <label class="control-label col-lg-2" for="fecha_ini"><input type="radio" id="radio" name="radio" value="no" onchange="habilitar(this.value);" >Facturas</label>
                                <div class="col-sm-3">                            
                                    <select class="form-control" name="folios" id="folios">
                                        <option value="">--Seleccione--</option>
                                        <%
                                        try {
                                            ResultSet rsetfact = null;

                                            con.conectar();

                                                rsetfact = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratula GROUP BY F_FacGNKLAgr");                                                                              


                                            while (rsetfact.next()) {

                                            %>
                                            <option value="<%=rsetfact.getString(1)%>"><%=rsetfact.getString(1)%></option>
                                            <%}
                                             con.cierraConexion();
                                                } catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                }
                                            %>                                            
                                    </select>
                                    
                                </div>                                
                            </div> 
                        </div>     
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6 text-center">
                                    <button class="btn btn-block btn-primary" id="btn_capturar">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>
                                </div>                                 
                            </div>
                        </div>                        
                    </form>
                
                <br />
                <div class="col-sm-6 text-center">
                    <form action="CancelarSecuelcialTodo" method="post">
                    <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                    <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                    <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                    <input type="text" class="hidden" readonly="" name="folio" value="<%=Folio%>"/>
                    <button class="btn btn-block btn-info" id="btn_cancelar">Cancelar TODO&nbsp;<label class="glyphicon glyphicon-trash"></label></button>
                    </form>
                </div> 
                <div class="panel-footer">
                            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv">
                                <thead>
                                    <tr>
                                        <td>Sec.</td>
                                        <td>Cve. Art.</td> 
                                        <!--td>Descripción</td-->                                
                                        <td>Pz Req.</td>
                                        <td>Pz Sur.</td>
                                        <td>Costo</td>
                                        <td>Total</td>
                                        <td>Sts.</td>
                                        <td>Factura Agr.</td>
                                        <td>Cve. Uni.</td>
                                        <td>Surtido</td>
                                        <td>Cobertura</td>
                                        <td>Suministro</td>
                                        <td>cancelar Secuencial</td>
                                        <td>cancelar Parcial</td>
                                        <td>Reactivar</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                try {
                                    ResultSet rset = null;
                                    
                                    con.conectar();
                                    
                                        if (radio.equals("si")){
                                        rset = con.consulta("SELECT F_Secuencial,F_Cveart,F_DesGen,F_Canreq,F_Cansur,F_CosUni,ROUND((F_Cansur*F_CosUni),2) AS TOTAL, F_Status,F_FacGNKLAgr,F_Cveuni,CASE WHEN F_Idsur= 1  THEN 'ADM' ELSE 'VEN' END AS F_Idsur,CASE WHEN F_IdePro = 0 THEN 'P.A.' ELSE 'S.P.' END AS F_IdePro,CASE WHEN F_Cvesum = 1 THEN 'MED' ELSE 'CUR' END AS F_Cvesum FROM tb_txtis WHERE F_Secuencial between '"+secuencial1+"' and '"+secuencial2+"' ORDER BY F_Secuencial+0");
                                        }else{
                                        
                                        rset = con.consulta("SELECT F_Secuencial,F_Cveart,F_DesGen,F_Canreq,F_Cansur,F_CosUni,ROUND((F_Cansur*F_CosUni),2) AS TOTAL, F_Status,F_FacGNKLAgr,F_Cveuni,CASE WHEN F_Idsur= 1  THEN 'ADM' ELSE 'VEN' END AS F_Idsur,CASE WHEN F_IdePro = 0 THEN 'P.A.' ELSE 'S.P.' END AS F_IdePro,CASE WHEN F_Cvesum = 1 THEN 'MED' ELSE 'CUR' END AS F_Cvesum FROM tb_txtis WHERE F_FacGNKLAgr='"+Folio+"' ORDER BY F_Secuencial+0");                                       
                                        }
                                        
                                    
                                   
                                    while (rset.next()) {
                                        F_Status = rset.getString("F_Status");
                            %>

                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>                                
                                <!--td><%=rset.getString(3)%></td-->                        
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>                                
                                <td><%=rset.getString(7)%></td>                        
                                <td><%=rset.getString(8)%></td>
                                <td><%=rset.getString(9)%></td>
                                <td><%=rset.getString(10)%></td>                                
                                <td><%=rset.getString(11)%></td>                        
                                <td><%=rset.getString(12)%></td>
                                <td><%=rset.getString(13)%></td>
                                <%if (!(F_Status.equals("C"))){%>
                                <td>
                                    <form action="CancelarSecuencial" method="post">                                        
                                            <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                                            <input type="text" class="hidden" readonly="" name="folio" value="<%=Folio%>"/>
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-primary" onclick="return confirm('¿Seguro que desea CANCELAR secuenial?');"><span class="glyphicon glyphicon-remove"></span></button>
                                    </form>                                    
                                </td>
                                <td>                                    
                                    <form action="CancelarP.jsp" method="post">                                            
                                            <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                                            <input type="text" class="hidden" readonly="" name="folio" value="<%=Folio%>"/>
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-primary"><span class="glyphicon glyphicon-refresh"></span></button>
                                    </form>
                                </td>
                                <td></td>
                                <%}else{%>
                                <td></td>
                                <td></td>
                                <td>
                                    <form action="ReactivarSecuencial" method="post">                                        
                                            <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                                            <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                                            <input type="text" class="hidden" readonly="" name="folio" value="<%=Folio%>"/>
                                            <input class="hidden" name="fol_gnkl" value="<%=rset.getString(1)%>">
                                            <button class="btn btn-block btn-primary" onclick="return confirm('¿Seguro que desea Reactivar secuenial?');"><span class="glyphicon glyphicon-refresh"></span></button>
                                    </form>  
                                 </td>
                                <%}%>
                            </tr>
                            
                            <%
                                    }
                                    
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            %>
                                </tbody>
                            </table>
                        </div> 
            </div>
        </div>
        <br><br><br>
        <%@include file="../jspf/piePagina.jspf" %>
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
    //$("#fecha_ini").datepicker();
    //$("#fecha_fin").datepicker();
    function habilitar(value){
        
        if(value=="si"){
            document.getElementById("folios").disabled=true;
            document.getElementById("secuencial1").disabled=false;
            document.getElementById("secuencial2").disabled=false;

        }else if(value=="no"){
            document.getElementById("secuencial1").disabled=true;
            document.getElementById("secuencial2").disabled=true;
            document.getElementById("secuencial1").value="";
            document.getElementById("secuencial2").value="";
            document.getElementById("folios").disabled=false;
             
        }else{
            document.getElementById("secuencial1").disabled=false;
            document.getElementById("secuencial2").disabled=false;
            document.getElementById("EliminaSec").disabled=false;
            document.getElementById("folios").disabled=false;
            document.getElementById("EliminaFol").disabled=false;
        }
    }
    </script>
    <script>
    $(document).ready(function() {
        $('#datosProv').dataTable();
    });
     function valida_alta() {
                var Nombre = document.getElementById('file1').value;

                if (Nombre === "") {
                    alert("Tiene campos vacíos, verifique.");
                    return false;
                }         
                document.getElementById('Loader').style.display = 'block';
            }
</script>

