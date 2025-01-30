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
    String tipo = "";
    String secuencial1="",secuencial2="",folio="",radio="",Folio1="",radio1="",Secu1="",Secu2="";
    String LoteC="",Clave="",Folio="",secuencial="",SecuFol="", FolDesc="",LoteDesc="",UniDesc="";
    int F_Idsur=0,F_IdePro=0,F_Cvesum=0,F_Punto=0,F_Con=0,CantDesc=0,Existen=0,Diferencia=0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo"); 
        SecuFol = (String) sesion.getAttribute("SecuFol");
        radio1 = (String) sesion.getAttribute("radio");       
        Folio1 = (String) sesion.getAttribute("folio");       
        Secu1 = (String) sesion.getAttribute("secu1");       
        Secu2 = (String) sesion.getAttribute("secu2");
    } else {
        response.sendRedirect("index.jsp");
    }
    System.out.println("SEc1"+Secu1+" Sec2"+Secu2+" Folio"+Folio1+" Radio"+radio1);
    try {
        secuencial = request.getParameter("fol_gnkl");        
        secuencial1=request.getParameter("sec1");
        secuencial2=request.getParameter("sec2");
        folio=request.getParameter("folio");
        radio=request.getParameter("radio");  
    } catch (Exception e) {

    }
    
    if (secuencial == null){
        secuencial = SecuFol;
    }
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
    if(folio==null){
        if(Folio1 == null){
        folio = "";
        }else{
        folio = Folio1;
        }        
    }
    if(radio==null){
        if(radio1 == null){
        radio = "";
        }else{
        radio= radio1;
        }        
    }
    System.out.println("SEc1"+secuencial1+" Sec2"+secuencial2+" Folio2"+folio+" Radio"+radio);
    
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
                    <h3 class="panel-title">
                        <form action="CancelarSecuencialParcial" method="post">
                        Cancelar Secuencial Parcial = <%=secuencial%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                        <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                        <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                        <input type="text" class="hidden" readonly="" name="folio2" value="<%=folio%>"/>
                        <button class="btn btn-info" name="accion" value="regresar">Regresar<span class="glyphicon glyphicon-hand-left"></span></button>                                                                        
                        </form>
                    </h3>
                    
                </div>
                
                
                
                <br />

                <div class="panel-footer">
                            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv">
                                <thead>
                                    <tr>
                                        <td>U.A. MEDALFA</td>
                                        <td>Folio</td> 
                                        <td>Lote</td>                                
                                        <td>Cantidad</td>
                                        <td>Cancelada</td>
                                        <td>Saldo</td>
                                        <td>Cantidad Cancelar</td>
                                        <td>Modificar</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                try {
                                    ResultSet rset = null;
                                    ResultSet RFolios = null;
                                    ResultSet RDescuento = null;
                                    
                                    con.conectar();
                                        rset = con.consulta("SELECT F_Folios,F_ClaInt,F_LotCan FROM tb_txtis T INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS WHERE F_Secuencial='"+secuencial+"'");                                       
                                        if (rset.next()){
                                            Folio = rset.getString(1);
                                            Clave = rset.getString(2);
                                            LoteC = rset.getString(3);                                        
                                         
                                        int fol = Folio.length();
                                        Folio = Folio.substring(0,fol-1);
                                        System.out.println(Folio);
                                        RFolios = con.consulta("SELECT F.F_ClaCli,F.F_ClaDoc,L.F_ClaLot,SUM(F.F_CantSur) FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica WHERE F.F_ClaDoc IN ("+Folio+") AND F.F_ClaPro='"+Clave+"' GROUP BY F.F_ClaDoc,F.F_Lote");
                                        while (RFolios.next()) {                                        
                                        Existen= RFolios.getInt(4);
                                        RDescuento = con.consulta("SELECT SUM(F_Can) AS F_Can FROM tb_txtiscantparcial WHERE F_Folio='"+RFolios.getString(2)+"' AND F_Secuencial='"+secuencial+"' AND F_ClaUni='"+RFolios.getString(1)+"' AND F_Lote='"+RFolios.getString(3)+"'");
                                        if(RDescuento.next()){
                                        CantDesc = RDescuento.getInt(1);
                                        }
                                        Diferencia = Existen - CantDesc;
                            %>

                            <tr>
                                <form action="CancelarSecuencialParcial" method="post">
                                <td><%=RFolios.getString(1)%></td>
                                <td><%=RFolios.getString(2)%></td>                                
                                <td><%=RFolios.getString(3)%></td>                        
                                <td><%=RFolios.getString(4)%></td>
                                <td><%=CantDesc%></td>
                                <td><%=Diferencia%></td>
                                
                                <td><input type="number" min="1" max="<%=Diferencia%>" class="text-right" name="cancelar" value="0"  /></td>
                                
                                <td><%if(Diferencia > 0){%>
                                    <input type="text" class="hidden" readonly="" name="radio" value="<%=radio%>"/>
                                    <input type="text" class="hidden" readonly="" name="sec1" value="<%=secuencial1%>"/>
                                    <input type="text" class="hidden" readonly="" name="sec2" value="<%=secuencial2%>"/>
                                    <input type="text" class="hidden" readonly="" name="folio2" value="<%=folio%>"/>
                                    <input type="text" class="text-right hidden" readonly="" name="unidad" value="<%=RFolios.getString(1)%>"/>
                                    <input type="text" class="text-right hidden" readonly="" name="folio" value="<%=RFolios.getString(2)%>"/>
                                    <input type="text" class="text-right hidden" readonly="" name="lote" value="<%=RFolios.getString(3)%>"/>
                                    <input type="text" class="text-right hidden" readonly="" name="secuencial" value="<%=secuencial%>"/>
                                    <input class="hidden" name="fol_gnkl" value="<%=RFolios.getString("F.F_ClaDoc")%>:<%=RFolios.getString("L.F_ClaLot")%>/<%=RFolios.getString("F.F_ClaCli")%>">
                                    <button class="btn btn-block btn-primary" name="accion" value="actualizar" onclick="return confirm('¿Seguro que desea CANCELAR Secuenial Parcialmente?');"><span class="glyphicon glyphicon-refresh"></span></button>                                                                        
                                    <%}%>
                                </td>
                                </form>
                            </tr>
                            
                            <%
                                    }
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
   
    </script>
    

