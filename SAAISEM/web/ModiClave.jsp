<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "", DesTipo = "", F_DesPro = "",F_PrePro ="", F_StsPro = "", F_Catipo = "", F_Costo = "";
    String OriNim = "", CostoNim = "", Grupo = "", Comentario = "", Catalogo = "",  F_DesOri = "";
    int tipo = 0, F_Origen = 0, F_IncMen = 0, CantMax = 0;
    int ban1 = 0, ban2 = 0, ban3 = 0, ban4 = 0, ban5 = 0, ban6 = 0, ban7 = 0, ban8 = 0, ban9 = 0, ban10 = 0, ban11 = 0, ban12 = 0, ban14 = 0, ban15 = 0, ban16 = 0, ban216 = 0, ban17 = 0, ban217 = 0, ban18 = 0, ban19 = 0, ban20 = 0, ban30 = 0;
    int bProIsem = 0, bProMic = 0, bProIsemym = 0 ,  bProSAME = 0, bProTHOMA = 0, bProUPUEBLA = 0, bProEHCM = 0, bProEHURU = 0,  bProMinds = 0,bProTHOMANES = 0, bProTHOMAMER = 0,bProAcre = 0, bProHEMO = 0,bProDDS = 0, bProEnseres = 0, bProAPM = 0,bProSonora = 0,bProSinergo = 0 ;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        Clave = request.getParameter("Clave");
    } catch (Exception e) {

    }

    ConectionDB con = new ConectionDB();

    try {
        con.conectar();

        ResultSet rset = con.consulta("SELECT F_ClaPro,F_DesPro,F_StsPro,F_TipMed,F_Costo,F_Origen,F_Catipo,F_IncMen,F_N1,F_N2,F_N3,F_N4,F_N5,F_N6,F_N7,F_N8,F_N9,F_N10,F_N11,F_N12,F_N14,F_N15,F_N16,F_N216,F_N17,F_N217,F_N18,F_N19,F_N20,F_N30,F_OriNim,F_CostoNim,F_Grupo,F_Comentario,F_Catalogo,F_CantMax,F_ProIsem,F_ProMichoacan,F_ProIssemym, F_ProSamedic,F_ProThomasant,F_ProUcinpuebla,F_proCivilMor,F_ProEHUruapan,F_ProMinds,F_ProThomaIsem,F_ProThomaMerida,F_ProMichAcre,F_ProMichHemodi,F_ProDDS,F_ProEnseres,F_ProAPM,F_PrePro,F_DesOri,F_ProSonora  FROM tb_medica m INNER JOIN tb_origen as o ON   F_Origen = F_ClaOri where F_ClaPro = '" + Clave + "' ORDER BY F_ClaPro+0 ASC;");
        while (rset.next()) {
            F_DesPro = rset.getString(2);
            tipo = Integer.parseInt(rset.getString(4));
            F_StsPro = rset.getString(3);
            F_PrePro = rset.getString(53);
            F_Origen = rset.getInt(6);
            F_DesOri = rset.getString(54);
            F_Catipo = rset.getString(7);
            F_Costo = rset.getString(5);
            F_IncMen = rset.getInt(8);
            ban1 = rset.getInt(9);
            ban2 = rset.getInt(10);
            ban3 = rset.getInt(11);
            ban4 = rset.getInt(12);
            ban5 = rset.getInt(13);
            ban6 = rset.getInt(14);
            ban7 = rset.getInt(15);
            ban8 = rset.getInt(16);
            ban9 = rset.getInt(17);
            ban10 = rset.getInt(18);
            ban11 = rset.getInt(19);
            ban12 = rset.getInt(20);
            ban14 = rset.getInt(21);
            ban15 = rset.getInt(22);
            ban16 = rset.getInt(23);
            ban216 = rset.getInt(24);
            ban17 = rset.getInt(25);
            ban217 = rset.getInt(26);
            ban18 = rset.getInt(27);
            ban19 = rset.getInt(28);
            ban20 = rset.getInt(29);
            ban30 = rset.getInt(30);
            OriNim = rset.getString(31);
            CostoNim = rset.getString(32);
            Grupo = rset.getString(33);
            Comentario = rset.getString(34);
            Catalogo = rset.getString(35);
            CantMax = rset.getInt(36);
            bProIsem = rset.getInt(37);
            bProMic = rset.getInt(38);
            bProIsemym = rset.getInt(39);
            bProSAME = rset.getInt(40);
            bProTHOMA = rset.getInt(41);
            bProUPUEBLA = rset.getInt(42);
            bProEHCM = rset.getInt(43);
            bProEHURU = rset.getInt(44);
            bProMinds = rset.getInt(45);
            bProTHOMANES = rset.getInt(46);
            bProTHOMAMER = rset.getInt(47);
            bProAcre = rset.getInt(48);
            bProHEMO = rset.getInt(49);
            bProDDS = rset.getInt(50);
            bProEnseres  = rset.getInt(51);
            bProAPM = rset.getInt(52);
            bProSonora =  rset.getInt(55);
            bProSinergo =  rset.getInt(56);
        }
        con.cierraConexion();
    } catch (Exception e) {

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
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catalogo de Insumo para la Salud</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="Medicamentos">
                        <div class="row">
                            <div class="form-group">                                
                                <label for="Clave" class="col-xs-1 control-label">CLAVE</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Clave" name="Clave" maxlength="60" placeholder="CLAVE" value="<%=Clave%>" readonly="" />
                                </div>
                               
                            
                                <label for="Sts" class="col-xs-1 control-label">Status</label>
                                <div class="col-xs-2">                                    
                                    <%if (F_StsPro.equals("A")) {%>
                                    <input type="radio" name="radiosts" id="radiosts" checked="" value="A">Activo&nbsp;<input type="radio" name="radiosts" id="radiosts" value="S">Suspendido
                                    <%} else {%>
                                    <input type="radio" name="radiosts" id="radiosts" value="A">Activo&nbsp;<input type="radio" name="radiosts" id="radiosts" checked="" value="S">Suspendido
                                    <%}%>                                    
                                </div>
                                
                                 <label for="list_medica" class="col-xs-1 control-label">PARTIDA</label>
                            <div class="col-xs-3">                                    
                                <%if (tipo == 2504) {%>
                                <input type="radio" name="radiotipo" id="radiotipo" checked="" value="2504">Medicamento&nbsp;
                                <input type="radio" name="radiotipo" id="radiotipo" value="2505">Mat. Curación
                                <%} else {%>
                                <input type="radio" name="radiotipo" id="radiotipo" value="2504">Medicamento&nbsp;
                                <input type="radio" name="radiotipo" id="radiotipo" checked="" value="2505">Mat. Curación
                                <%}%>
                            </div>
                            </div>
                                
                           <div class="row">
                                <label for="Descripcion" class="col-xs-2 control-label">Descripción</label>
                                <div class="col-xs-8">
                                    <input type="text" class="form-control" id="Descripcion" maxlength="250" name="Descripcion" placeholder="Descripcion" required=""  value="<%=F_DesPro%>"  />
                                </div>
                                
                            </div>
                            <br/>
                            <div class="row">
                                <label for="Presentacion" class="col-xs-2 control-label">Presentación</label>
                                <div class="col-xs-8">
                                    <input type="text" class="form-control" id="Descripcion" maxlength="60" name="Presentacion" placeholder="Presentacion" required=""  value="<%=F_PrePro%>"  />
                                </div>
                               
                            </div>  
                        </div>
                                <hr/>
                       
                        <br/>
                        <div class="row">
                            <div class="col-xs-11">
                                <table  class="table table-sm" id="datosnivel">
                                <tbody>
                                <tr>
                            <label for="list_medica" class="col-xs-1 control-label">NIVEL</label>
                            <div class="col-xs-11">                                    
                                <%if (ban1 == 1) {%>
                                <td><small><input type="checkbox" name="cat1" id="cat1" checked="" value="1">(1)CSRD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat1" id="cat1" value="1">(1)CSRD&nbsp;</small></td>
                               
                                <%}
                                    if (ban2 == 1) {%>
                                <td><small><input type="checkbox" name="cat2" id="cat2" checked="" value="2">(2)CSU&nbsp;</small></td>
                               
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat2" id="cat2" value="2">(2)CSU&nbsp;</small></td>
                                <%}
                                    if (ban3 == 1) {%>
                                <td><small><input type="checkbox" name="cat3" id="cat3" checked="" value="3">(3)HOSPITAL MUNICIPAL&nbsp</small></td>
                                <%} else {%>
                                 <td><small><input type="checkbox" name="cat3" id="cat3" value="3">(3)HOSPITAL MUNICIPAL&nbsp</small></td>
                                                              
                                <%}
                                    if (ban4 == 1) {%>
                               <td><small><input type="checkbox" name="cat4" id="cat4" checked="" value="4">(4)HOSPITAL GENERAL&nbsp;</small></td>
                                <%} else {%>
                               <td><small><input type="checkbox" name="cat4" id="cat4" value="4">(4)HOSPITAL GENERAL&nbsp;</small></td>
                                
                                <%}
                                    if (ban5 == 1) {%>
                                <td><small><input type="checkbox" name="cat5" id="cat5" checked="" value="5">(5)HOSPITAL MATERNO INFANTIL&nbsp;</small></td> 
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat5" id="cat5" value="5">(5)HOSPITAL MATERNO INFANTIL&nbsp;</small></td> 
                                <%}%>
                                </tr><tr>
                                    <%if (ban6 == 1) {%>
                                <td><small><input type="checkbox" name="cat6" id="cat6" checked="" value="6">(6)HOSPITAL MATERNO PERINATAL&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat6" id="cat6" value="6">(6)HOSPITAL MATERNO PERINATAL&nbsp;</small></td>
                                
                                <%}
                                    if (ban7 == 1) {%>
                                <td><small><input type="checkbox" name="cat7" id="cat7" checked="" value="7">(7)CENTRO MEDICO&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat7" id="cat7" value="7">(7)CENTRO MEDICO&nbsp;</small></td>
                                
                                <%}
                                    if (ban8 == 1) {%>
                                 <td><small><input type="checkbox" name="cat8" id="cat8" checked="" value="8">(8)HOSPITAL PSIQUIATRICO&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat8" id="cat8" value="8">(8)HOSPITAL PSIQUIATRICO&nbsp;</small></td>
                                
                                <%}
                                    if (ban9 == 1) {%>
                                <td><small><input type="checkbox" name="cat9" id="cat9" checked="" value="9">(9)SUEM&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat9" id="cat9" value="9">(9)SUEM&nbsp;</small></td>
                                
                                <%}
                                    if (ban10 == 1) {%>
                                <td><small><input type="checkbox" name="cat10" id="cat10" checked=""  value="10">(10)PD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat10" id="cat10" value="10">(10)PD&nbsp;</small></td>
                                <%}%>
                                </tr><tr>
                                    <%if (ban11 == 1) {%>
                                <td><small><input type="checkbox" name="cat11" id="cat11" checked="" value="11">(11)PD&nbsp;</small></td>
                                <%} else {%>
                               <td><small><input type="checkbox" name="cat11" id="cat11" value="11">(11)PD&nbsp;</small></td>
                                
                                <%}
                                    if (ban12 == 1) {%>
                               <td><small><input type="checkbox" name="cat12" id="cat12" checked="" value="12">(12)PD&nbsp;</small></td>
                                <%} else {%>
                               <td><small><input type="checkbox" name="cat12" id="cat12" value="12">(12)PD&nbsp;</small></td>
                                                          
                                <%}
                                    if (ban14 == 1) {%>
                                <td><small><input type="checkbox" name="cat14" id="cat14" checked="" value="14">(14)CAD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat14" id="cat14" value="14">(14)CAD&nbsp;</small></td>
                                   
                                <%}
                                    if (ban15 == 1) {%>
                                 <td><small><input type="checkbox" name="cat15" id="cat15" checked="" value="15">(15)MOD. ODONTOPEDIATRICO&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat15" id="cat15" value="15">(15)MOD. ODONTOPEDIATRICO&nbsp;</small></td>
                                
                                <%}
                                    if (ban16 == 1) {%>
                                <td><small><input type="checkbox" name="cat16" id="cat16" checked="" value="16">(16)PD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat16" id="cat16" value="16">(16)PD&nbsp;</small></td>
                                <%}%>
                                </tr><tr>
                                    <%if (ban216 == 1) {%>
                                <td><small><input type="checkbox" name="cat216" id="cat216" checked="" value="216">(216)PD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat216" id="cat216" value="216">(216)PD&nbsp;</small></td>
                               
                                <%}
                                    if (ban17 == 1) {%>
                                <td><small><input type="checkbox" name="cat17" id="cat17" checked="" value="17">(17)GER&nbsp;</small></td>
                                <%} else {%>
                                 <td><small><input type="checkbox" name="cat17" id="cat17" value="17">(17)GER&nbsp;</small></td>
                                
                                <%}
                                    if (ban217 == 1) {%>
                                <td><small><input type="checkbox" name="cat217" id="cat217" checked="" value="217">(217)CEAPS/GER&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat217" id="cat217" value="217">(217)CEAPS/GER&nbsp;</small></td>
                                                          
                                <%}
                                    if (ban18 == 1) {%>
                               <td><small><input type="checkbox" name="cat18" id="cat18" checked="" value="18">(18)PD&nbsp;</small></td>
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat18" id="cat18" value="18">(18)PD&nbsp;</small></td>
                                
                                <%}
                                    if (ban19 == 1) {%>
                               <td><small><input type="checkbox" name="cat19" id="cat19" checked="" value="19">(19)HOSPITAL VISUAL&nbsp;</small></td>  
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat19" id="cat19" value="19">(19)HOSPITAL VISUAL&nbsp;</small></td>
                                <%}%>
                                </tr><tr>
                                    <%   if (ban20 == 1) {%>
                                <td><small><input type="checkbox" name="cat20" id="cat20" checked="" value="20">(20)DISPENSADORES&nbsp;</small></td>
                               
                                <%} else {%>
                                <td><small><input type="checkbox" name="cat20" id="cat20" value="20">(20)DISPENSADORES&nbsp;</small></td>
                                <%}if (ban30 == 1) {%>
                                 <td><small><input type="checkbox" name="cat30" id="cat30" checked="" value="30">(30)SOLUCIONES&nbsp;</small></td>
                                <%} else {%>
                                 <td><small><input type="checkbox" name="cat30" id="cat30" value="30">(30)SOLUCIONES&nbsp;</small></td>
                                <%}%>
                                </tr>
                            </tbody>
                            </table>                                            
                        </div>
                            </div>                                                      
                        </div>
                        <hr/>
                        <div class="row">
                            <label for="Costo" class="col-xs-1 control-label">Precio</label>
                            <div class="col-xs-2">
                                <input type="text" class="form-control" id="Costo" name="Costo" placeholder="Costo" value="<%=F_Costo%>"  />
                            </div>
                            <label for="Costo" class="col-xs-2 control-label">%Inc. Mensual</label>
                            <div class="col-xs-2">
                                <input type="text" class="form-control" id="Incmen" name="Incmen" placeholder="Inc Mensual" value="<%=F_IncMen%>" onKeyPress="return isNumberKey(event, this)" />
                            </div>
                            <label for="Costo" class="col-sm-1 control-label">Costo Nim</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="CostoNim" name="CostoNim" placeholder="Costo Nim" value="<%=CostoNim%>" required=""  />
                            </div> 
                        </div>
                        <br />
                        <div class="row">  
                            <label for="selectorigen" class="col-xs-1 control-label">ORIGEN</label>
                            <div class="col-xs-3">                                    
                                <select class="form-control" name="selectorigen" id="radiotipo">
                                        
                                    <option value="<%=F_Origen%>"><%=F_DesOri%></option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT * FROM tb_origen;");
                                                while (rset.next()) {
                                        %>
                                        <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                out.println(e.getMessage());
                                            }
                                        %>
                                    </select>                                   
                            </div>  
                            
                            
                            <label for="Costo" class="col-sm-1 control-label">Origen Nim</label>
                            <div class="col-sm-2">
                                <select name="origennim" id="origennim" class="form-control" required="">
                                    <option value="<%=OriNim%>"><%=OriNim%></option>
                                    <option value="LP">LP</option>
                                    <option value="CC">CC</option>
                                </select>
                            </div>
                            <label for="GrupoNim" class="col-sm-1 control-label">Grupo</label>
                            <div class="col-sm-2">
                                <!--input type="text" class="form-control" id="GrupoNim" name="GrupoNim" placeholder="Grupo Nim" value="" required=""/-->
                                <select class="form-control" name="GrupoNim" id="GrupoNim">
                                        <option value="0"><%=Grupo%></option>
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT * FROM tb_gruptera;");
                                                while (rset.next()) {
                                        %>
                                        <option value="<%=rset.getString(2)%>"><%=rset.getString(2)%></option>
                                        <%
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                out.println(e.getMessage());
                                            }
                                        %>
                                    </select>
                            </div>
                                      </div>
                        <br/>
                        <div class="row">
                            <label for="Costo" class="col-sm-1 control-label">Comentario Nim</label>
                           
                            <div class="col-sm-4">                                
                                <select name="ComentarioNimSelect" id="ComentarioNimSelect" class="form-control" onchange="selecOp()">
                                    <option><%=Comentario%></option>
                                    <option value="CLAVE HABILITADO 2018">CLAVE HABILITADO 2018</option>
                                    <option value="CLAVE HABILITADO 2018/SOLUCIONES">CLAVE HABILITADO 2018/SOLUCIONES</option>
                                    <option value="CLAVE INHABILITADO">CLAVE INHABILITADO</option>
                                    <option value="CLAVE SOLUCIONES">CLAVE SOLUCIONES</option>
                                    <option value="HABILITADO HASTA AGOTAR EXISTENCIAS">HABILITADO HASTA AGOTAR EXISTENCIAS</option>
                                </select>
                            </div>
                      
                             <label for="CatalogoNim" class="col-sm-1 control-label">CatalogoNim</label>
                            <div class="col-sm-2">
                                <select name="CatalogoNim" id="origennim" class="form-control" required="">
                                    <option value="0">-select año-</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                </select>
                            </div>
                            <label for="Costo" class="col-sm-1 control-label">Cant. Recibir</label>
                            <div class="col-sm-2">
                                <input type="number" min="0" class="form-control" id="CantRecibir" name="CantRecibir" placeholder="Cantidad Max. Recibir" value="<%=CantMax%>" required=""  />
                            </div>
                            </div>
                            <hr/>
                                <div class="row">
                                    <label for="list_medica" class="col-xs-1 control-label">PROYECTOS</label>
                                    
                                    <div class="col-xs-11">
                                <table class="table table-striped" id="datosProyec">

                                    <tbody>
                                        <tr>
                                                    <% if (bProIsem == 1) {%>  
                                                    <td><small><input type="checkbox" name="bProIsem" id="bProIsem" checked="" value="1">(ISEM)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProIsem" id="bProIsem" value="1">(ISEM)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProMic == 1) {%>
                                                    <td><small><input type="checkbox" name="bProMic" id="bProMic" checked="" value="1">(MICHOACAN)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProMic" id="bProMic" value="1">(MICHOACAN)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProIsemym == 1) {%>
                                                    <td><small><input type="checkbox" name="bProIssemym" id="bProIssemym" checked="" value="1">(ISSEMYM)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProIssemym" id="bProIssemym" value="1">(ISSEMYM)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProIsem == 1) {%>
                                                    <td><small><input type="checkbox" name="bProI30" id="bProIssemym" checked="" value="1">(ISEM 30)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProI30" id="bProIssemym" value="1">(ISEM 30)&nbsp;</small></td>
                                                    <% } %>
                                                     <% if (bProSonora == 1) {%>
                                                    <td><small><input type="checkbox" name="bProSonora" id="bProSonora" checked="" value="1"> (SONORA)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProSonora" id="bProSonora" value="1"> (SONORA)&nbsp;</small></td>
                                                    <% } %>
                                                </tr><tr> 
                                                    <% if (bProIsem == 1) {%>
                                                    <td><small><input type="checkbox" name="bProISOL" id="bProIssemym" checked="" value="1">(ISEM SOL)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProISOL" id="bProIssemym" value="1">(ISEM SOL)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProIsem == 1) {%>
                                                    <td><small><input type="checkbox" name="bProIANES" id="bProIssemym" checked="" value="1">(ISEM ANESTESIA)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProIANES" id="bProIssemym"  value="1">(ISEM ANESTESIA)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProIsem == 1) {%>
                                                    <td><small><input type="checkbox" name="bProIANUAL" id="bProIssemym" checked="" value="1">(ISEM ANUAL)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProIANUAL" id="bProIssemym"  value="1">(ISEM ANUAL)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProSAME == 1) {%>
                                                    <td><small><input type="checkbox" name="bProSAME" id="bProIssemym" checked="" value="1">(SAMEDIC EEM)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProSAME" id="bProIssemym" value="1">(SAMEDIC EEM)&nbsp;</small></td>
                                                    <% } %>
                                                     <% if (bProSinergo == 1) {%>
                                                    <td><small><input type="checkbox" name="bProSinergo" id="bProSinergo" checked="" value="1"> (SINERGO)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProSinergo" id="bProSinergo" value="1"> (SINERGO)&nbsp;</small></td>
                                                    <% } %>
                                                </tr><tr>
                                                    <% if (bProTHOMA == 1) {%>
                                                    <td><small><input type="checkbox" name="bProTHOMA" id="bProIssemym" checked="" value="1">(THOMASANT)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProTHOMA" id="bProIssemym"  value="1">(THOMASANT)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProUPUEBLA == 1) {%>
                                                    <td><small><input type="checkbox" name="bProUPUEBLA" id="bProIssemym" checked="" value="1">(UCIN PUEBLA)&nbsp;</small></td>
                                                    <% } else {%>
                                                     <td><small><input type="checkbox" name="bProUPUEBLA" id="bProIssemym" value="1">(UCIN PUEBLA)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProEHCM == 1) {%>
                                                    <td><small><input type="checkbox" name="bProEH-CM" id="bProIssemym" checked="" value="1">(EH-CIVIL MORELIA)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProEH-CM" id="bProIssemym" value="1">(EH-CIVIL MORELIA)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProEHURU == 1) {%>
                                                    <td><small><input type="checkbox" name="bProEH-URU" id="bProIssemym" checked="" value="1">(EH-URUAPAN)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProEH-URU" id="bProIssemym" value="1">(EH-URUAPAN)&nbsp;</small></td>
                                                    <% } %>
                                                </tr><tr>
                                                    <% if (bProMinds == 1) {%>
                                                    <td><small><input type="checkbox" name="bProMinds" id="bProIssemym" checked="" value="1">(Medical Minds)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProMinds" id="bProIssemym" value="1">(Medical Minds)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProTHOMANES == 1) {%>
                                                    <td><small><input type="checkbox" name="bProTHOMANES" id="bProIssemym" checked="" value="1">(THOMASANISEMANESTESIA)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProTHOMANES" id="bProIssemym" value="1">(THOMASANISEMANESTESIA)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProTHOMAMER == 1) {%>
                                                    <td><small><input type="checkbox" name="bProTHOMAMER" id="bProIssemym" checked="" value="1">(THOMASANMERIDA)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProTHOMAMER" id="bProIssemym" value="1">(THOMASANMERIDA)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProAcre == 1) {%>
                                                    <td><small><input type="checkbox" name="bProAcred" id="bProIssemym" checked="" value="1">(ACREDITACIONES)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProAcred" id="bProIssemym" value="1">(ACREDITACIONES)&nbsp;</small></td>
                                                    <% } %>
                                                </tr><tr>
                                                    <% if (bProHEMO == 1) {%>
                                                    <td><small><input type="checkbox" name="bProHEMO" id="bProIssemym" checked="" value="1">(HEMODIALISIS)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProHEMO" id="bProIssemym" value="1">(HEMODIALISIS)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProDDS == 1) {%>
                                                    <td><small><input type="checkbox" name="bProDDS" id="bProIssemym" checked="" value="1">(DISENO DE SALUD)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProDDS" id="bProIssemym" value="1">(DISENO DE SALUD)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProEnseres == 1) {%>
                                                    <td><small><input type="checkbox" name="bProEnseres" id="bProIssemym" checked="" value="1">(ENSERES)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProEnseres" id="bProIssemym" value="1">(ENSERES)&nbsp;</small></td>
                                                    <% } %>
                                                    <% if (bProAPM == 1) {%>
                                                    <td><small><input type="checkbox" name="bProAPM" id="bProIssemym" checked="" value="1">(INVENTARIO CEDIS CENTRAL)&nbsp;</small></td>
                                                    <% } else {%>
                                                    <td><small><input type="checkbox" name="bProAPM" id="bProIssemym" value="1">(INVENTARIO CEDIS CENTRAL)&nbsp;</small></td>
                                                    <% } %>
                                                    
                                                </tr> 

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                        <hr/>
                        <div class="row">
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="Modificar">Modificar</button>   
                            </div>
                            <div class="col-sm-6">
                                <a href="medicamento.jsp" class="btn btn-block btn-info" type="submit" value="Regresar">Regresar</a>   
                            </div>
                        </div>
                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>

            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
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
                                    $(document).ready(function () {
                                        $('#datosProyec').dataTable();
                                         $('#datosnivel').dataTable();
                                    });
        </script>
        <script>


            function isNumberKey(evt, obj)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode;
                if (charCode === 13 || charCode > 31 && (charCode < 48 || charCode > 57)) {
                    if (charCode === 13) {
                        frm = obj.form;
                        for (i = 0; i < frm.elements.length; i++)
                            if (frm.elements[i] === obj)
                            {
                                if (i === frm.elements.length - 1)
                                    i = -1;
                                break
                            }
                        /*ACA ESTA EL CAMBIO*/
                        if (frm.elements[i + 1].disabled === true)
                            tabular(e, frm.elements[i + 1]);
                        else
                            frm.elements[i + 1].focus();
                        return false;
                    }
                    return false;
                }
                return true;

            }


            function valida_alta() {
                var missinginfo = "";
                var radiotipo = ($("#radiotipo1")).val();
                alert(radiotipo);
                if ($("#Clave").val() == "") {
                    missinginfo += "\n El campo Clave no debe de estar vacío";
                }
                if ($("#Descripcion").val() == "") {
                    missinginfo += "\n El campo Descripcion no debe de estar vacío";
                }
                if ($("#radiosts").val() == "") {
                    missinginfo += "\n El campo Descripcion no debe de estar vacío";
                }
                if ($("#Costo").val() == "") {
                    missinginfo += "\n El campo Costo no debe de estar vacío";
                }
                if (missinginfo != "") {
                    missinginfo = "\n TE HA FALTADO INTRODUCIR LOS SIGUIENTES DATOS PARA ENVIAR PETICIÓN DE SOPORTE:\n" + missinginfo + "\n\n ¡INGRESA LOS DATOS FALTANTES Y TRATA OTRA VEZ!\n";
                    alert(missinginfo);

                    return false;
                } else {

                    return true;
                }

            }
        </script>
        <script language="javascript">
            function justNumbers(e)
            {
                var keynum = window.event ? window.event.keyCode : e.which;
                if ((keynum == 8) || (keynum == 46))
                    return true;

                return /\d/.test(String.fromCharCode(keynum));
            }
            otro = 0;
            function LP_data() {
                var key = window.event.keyCode;//codigo de tecla. 
                if (key < 48 || key > 57) {//si no es numero 
                    window.event.keyCode = 0;//anula la entrada de texto. 
                }
            }
            function anade(esto) {
                if (esto.value.length === 0) {
                    if (esto.value.length == 0) {
                        esto.value += "(";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 4) {
                        esto.value += ") ";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 9) {
                        esto.value += "-";
                    }
                }
                if (esto.value.length < otro) {
                    if (esto.value.length == 4 || esto.value.length == 9) {
                        esto.value = esto.value.substring(0, esto.value.length - 1);
                    }
                }
                otro = esto.value.length
            }


            function tabular(e, obj)
            {
                tecla = (document.all) ? e.keyCode : e.which;
                if (tecla != 13)
                    return;
                frm = obj.form;
                for (i = 0; i < frm.elements.length; i++)
                    if (frm.elements[i] == obj)
                    {
                        if (i == frm.elements.length - 1)
                            i = -1;
                        break
                    }
                /*ACA ESTA EL CAMBIO*/
                if (frm.elements[i + 1].disabled == true)
                    tabular(e, frm.elements[i + 1]);
                else
                    frm.elements[i + 1].focus();
                return false;
            }

        </script> 
        <script type="text/javascript">
            function selecOp()
            {
                var select = $("#ComentarioNimSelect").val();

                $("#ComentarioNim").val(select);


            }
        </script> 
    </body>
</html>



