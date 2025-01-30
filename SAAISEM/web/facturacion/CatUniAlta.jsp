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
    String usua = "",Id="",DesTipo = "",F_Juris="",F_StsPro="",F_Catipo="",F_Costo="";
    String tipo = "";
    int F_Origen=0,F_IncMen=0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    try{
        Id = request.getParameter("Id");
    }catch(Exception e){
        
    }
    
    ConectionDB con = new ConectionDB();
    
    
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>

        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>            
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <%@include file="../jspf/menuPrincipal.jspf" %>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catálogo de Unidades (Alta)</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="../Medicamentos">
                        <div class="row">                                                          
                                <label for="Clave" class="col-xs-1 control-label">Clave</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Clave" name="Clave" maxlength="60" placeholder="CLUES" />
                                </div>                                
                                <label for="Descripcion" class="col-xs-1 control-label">Nombre:</label>
                                <div class="col-xs-5">
                                    <input type="text" class="form-control" id="Nombre" maxlength="40" name="Nombre" placeholder="Nombre Unidad" />
                                </div>
                        </div>
                                <br />                                
                        <div class="row">
                            <label for="Descripcion" class="col-xs-1 control-label">Jurisdicción</label>
                                <div class="col-xs-5">
                                    <select class="form-control input-sm" id="selectJuris" name="selectJuris">
                                        <option>--Seleccione--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaJurIS,F_DesJurIS FROM tb_juriis;");
                                        while(ConJuris.next()){
                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                        
                                    </select>                                    
                                </div>
                                <label for="Costo" class="col-xs-1 control-label">Municipio</label>
                                <div class="col-xs-5">
                                    <select class="form-control input-sm" id="selectMuni" name="selectMuni">
                                        <option>--Seleccione--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaMunIS,CONCAT(F_DesMunIS,' - ',F_DesJurIS),F_DesMunIS,J.F_DesJurIS FROM tb_muniis M INNER JOIN tb_juriis J ON M.F_JurMunIS=J.F_ClaJurIS;");
                                        while(ConJuris.next()){
                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>                                                               
                            
                        </div>
                                <br />
                                <div class="row">
                                    <label for="Costo" class="col-xs-1 control-label">Localidad</label>
                                <div class="col-xs-5">
                                    <select class="form-control input-sm" id="selectLoc" name="selectLoc" >
                                        <option>--Seleccione--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaLocIS,CONCAT(F_DesLocIS,' - ',F_DesMunIS,' - ',F_DesJurIS) FROM tb_locais L INNER JOIN tb_muniis M ON L.F_MunLocIS=M.F_ClaMunIS INNER JOIN tb_juriis J ON M.F_JurMunIS=J.F_ClaJurIS;");
                                        while(ConJuris.next()){
                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>   
                                <label for="Descripcion" class="col-xs-1 control-label">Coordinación</label>
                                <div class="col-xs-5">
                                    <select class="form-control input-sm" id="selectCoo" name="selectCoo">
                                        <option>--Seleccione--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCooIS,F_DesCooIS FROM tb_cooris;");
                                        while(ConJuris.next()){
                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>                                                                
                        </div>
                                <br />
                                <div class="row">                                                                  
                                <label for="Costo" class="col-xs-1 control-label">Región</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="region" name="region" placeholder="Region" onkeypress="return isNumberKey(event, this);" />
                                </div>
                                <label for="Costo" class="col-xs-1 control-label">Nivel</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="nivel" name="nivel" onkeypress="return isNumberKey(event, this);" placeholder="Nivel" />
                                </div>
                                <label for="Costo" class="col-xs-1 control-label">Doctor</label>                                
                                    <div class="col-xs-5">
                                    <select class="form-control input-sm" id="selectDoct" name="selectDoct" >
                                        <option>--Seleccione--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaMedIs,CONCAT(F_ClaMedIs,' - ',F_DesMedIS) FROM tb_mediis;");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>   
                        </div>
                                <br />
                        <div class="row">
                            <div class="form-group">                                
                                <label for="Clave" class="col-xs-1 control-label">Módulos</label>
                                <div class="col-xs-2">
                                    <select class="form-control input-sm" id="selectM1" name="selectM1" >
                                        <option>--Seleccione M1--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' - ',F_NomCli) FROM tb_uniatn WHERE F_ClaCli LIKE '%A%' AND F_StsCli='A';");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>
                                    <div class="col-xs-2">
                                    <select class="form-control input-sm" id="selectM2" name="selectM2" >
                                        <option>--Seleccione M2--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' - ',F_NomCli) FROM tb_uniatn WHERE F_ClaCli LIKE '%B%' AND F_StsCli='A';");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div> 
                                <div class="col-xs-2">
                                    <select class="form-control input-sm" id="selectM3" name="selectM3" >
                                        <option>--Seleccione M3--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' - ',F_NomCli) FROM tb_uniatn WHERE F_ClaCli LIKE '%C%' AND F_StsCli='A';");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>
                                <div class="col-xs-2">
                                    <select class="form-control input-sm" id="selectM4" name="selectM4" >
                                        <option>--Seleccione M4--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' - ',F_NomCli) FROM tb_uniatn WHERE F_ClaCli LIKE '%D%' AND F_StsCli='A';");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>
                                <div class="col-xs-2">
                                    <select class="form-control input-sm" id="selectM5" name="selectM5" >
                                        <option>--Seleccione M5--</option>
                                        <%try{
                                        con.conectar();
                                        ResultSet ConJuris = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' - ',F_NomCli) FROM tb_uniatn WHERE F_ClaCli LIKE '%E%' AND F_StsCli='A';");
                                        while(ConJuris.next()){                                            
                                        %>
                                        <option value="<%=ConJuris.getString(1)%>"><%=ConJuris.getString(2)%></option>
                                        <%
                                            }
                                        con.cierraConexion();
                                        }catch(Exception e){       
                                        }%>
                                    </select>
                                </div>
                            </div>
                        </div>                                              
                        <br/>                        
                        <div class="row">
                            <div class="col-sm-6">
                              <button class="btn btn-block btn-primary" type="submit" name="accion" value="RegistrarUnidis">Registrar</button>   
                            </div>
                            <div class="col-sm-6">
                                <a href="catalogoUniDis.jsp" class="btn btn-block btn-info" type="submit" name="accion" value="Regresar">Regresar</a>   
                            </div>
                        </div>
                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
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
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/select2.js" type="text/javascript"></script>

        <script>
                            
            $("#selectJuris").select2();
            $("#selectMuni").select2();
            $("#selectLoc").select2();
            $("#selectCoo").select2();
            $("#selectDoct").select2();
            $("#selectM1").select2();
            $("#selectM2").select2();
            $("#selectM3").select2();
            $("#selectM4").select2();
            $("#selectM5").select2();
            $(document).ready(function() {
                $('#datosProv').dataTable();                
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
    </body>
</html>



