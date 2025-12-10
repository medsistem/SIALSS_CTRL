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
    String usua = "", DesProyecto = "", Proyecto = "", clave="";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        DesProyecto = request.getParameter("DesProyecto");
        Proyecto = request.getParameter("Proyecto");
    } catch (Exception e) {

    }

    if (DesProyecto == null) {
        DesProyecto = "";
    }
    if (Proyecto == null) {
        Proyecto = "";
    }
     clave = (String) sesion.getAttribute("ClaProFM");
     System.out.println("clave: "+clave);
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" >
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIALSS IMSS</title>
    </head>
    <body onload="muestraCalendario()" >
        <div class="container">
            <h1>SIALSS IMSS</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Alta Lote</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="../FacturacionManual">
                        <div class="form-group">
                            <div class="form-group">
                                <label for="Clave" class="col-xs-2 control-label">Clave*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="clave" name="clave" placeholder="Clave" value="<%=clave%>" onKeyPress="return tabular(event, this)" readonly >
                               
                                   <!--select id="clave" name="clave" >
                                       <option value="< %=clave%>" disabled>< %=clave%></option>                                    
                                        < %
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT F_ClaPro FROM tb_medica;");
                                                while (rset.next()) {
                                        %>
                                        <option value="< %=rset.getString(1)%>">< %=rset.getString(1)%></option>                                        
                                        < %
                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {
                                                out.println(e.getMessage());
                                            }
                                        %>
                                    </select-->
                                </div>
                                <label for="Lote" class="col-xs-1 control-label">Lote*</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="lote" name="lote" maxlength="30" placeholder="Lote" onKeyPress="return tabular(event, this)" />
                                </div>                                               
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">

                                <label for="Caducidad" class="col-xs-2 control-label">Caducidad*</label>
                                <div class="col-xs-2">
                                    <input type="text" data-date-format="dd/mm/yyyy" class="form-control" id="caducidad" name="caducidad" oncopy="return false" onpaste="return false" onkeypress="return false;" maxlength="10" autocomplete="off" placeholder="dd/mm/aaaa" />
                                </div>
                                <label for="Fec_Fac" class="col-xs-1 control-label">Fabricacion*</label>
                                <div class="col-xs-2">
                                    <input type="text" data-date-format="dd/mm/yyyy" class="form-control" id="Fec_Fac" name="Fec_Fac" oncopy="return false" onpaste="return false" onkeypress="return false;" maxlength="10"  autocomplete="off" placeholder="dd/mm/aaaa"" />                            
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="form-group">
                                <label for="Origen" class="col-xs-2 control-label">Origen*</label>
                                <div class="col-xs-2">
                                    <select id="origen" name="origen">
                                        <option selected disabled>-Origen-</option>                                    
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT F_ClaOri,F_DesOri FROM tb_origen;");
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


                                <label for="Marca" class="col-xs-1 control-label">Marca*</label>
                                <div class="col-xs-2">
                                    <select id="Marca" name="Marca">
                                        <option selected disabled >-Marca-</option>                                     
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT F_ClaMar,F_DesMar FROM tb_Marca;");
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

                            </div>
                        </div>

                        <div class="form-group">
                            <div class="form-group">
                                <label for="Proveedor" class="col-xs-2 control-label">Proveedor*</label>
                                <div class="col-xs-2">
                                    <select id="proveedor" name="proveedor">
                                        <option selected disabled>-Proveedor-</option>   
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT F_ClaProve,F_NomPro FROM tb_proveedor;");
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
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="form-group">
                                <label for="cb" class="col-sm-2 control-label">CB*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="cb" name="cb"  placeholder="CB" onKeyPress="return isNumberKey(event, this)" max="13" maxlength="13" min="3" minlength="3" />
                                </div>
                                <label for="Ubicacion" class="col-sm-1 control-label">Ubicación</label>
                                <div class="col-xs-2">
                                    <select id="UbicaNueva" class="form-control" name="UbicaNueva">
                                        <option selected disabled>-Ubicación-</option>      
                                        <%
                                            try {
                                                con.conectar();
                                                ResultSet rset = con.consulta("SELECT * FROM tb_ubicanueva;");
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
                                <label for="Proyecto" class="col-sm-1 control-label">Proyecto</label>
                                <div class="col-xs-2">
                                    <input type="text" readonly="" class="form-control" name="DesProyecto" id="DesProyecto" value="<%=DesProyecto%>"/>

                                </div>

                            </div>
                        </div>
                        <input type="hidden" readonly class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                      
                               
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="NuevoLote" onclick="return valida_alta();"> Guardar</button> 
                        <a class="btn btn-block btn-info" onclick="window.close();">Salir</a>
                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
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
<script src="../js/jquery-1.9.1.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/jquery-ui-1.10.3.custom.js"></script>
<script src="../js/jquery.dataTables.js"></script>
<script src="../js/dataTables.bootstrap.js"></script>
<script src="../js/select2.js" type="text/javascript"></script>

<script>
                            $("#proveedor").select2();
                            $("#origen").select2();
                            $("#Marca").select2();
                            $("#UbicaNueva").select2();
                           /* $("#clave").select2();*/
                            
                            $(document).ready(function () {
                            $('#datosProv').dataTable();
                            });
                            
                            function muestraCalendario() {
                            $("#caducidad").datepicker({
                            dateFormat: 'dd/mm/yy',
                                    minDate: 1,
                                    changeMonth: true,
                                    changeYear: true
                            });
                            $("#Fec_Fac").datepicker({
                            dateFormat: 'dd/mm/yy',
                                    maxDate: - 3,
                                    changeMonth: true,
                                    changeYear: true
                            });
                            }
</script>

<script>
     function valida_alta(){
   var clave = document.formulario1.clave.value.trim();
    if ((clave === "-Clave-") || (origen === "")) {
    alert("Selecciona Clave, verifique.");
    return false;
    }
    
     var lote = document.formulario1.lote.value.trim();
    if (lote === "") {
    alert("Ingresa un Lote, verifique.");
    return false;
    }
    
    var caducidad = document.formulario1.caducidad.value.trim();
    if (caducidad === "") {
    alert("Tiene campos vacíos, verifique.");
    return false;
    } 
    
    var Fec_Fac = document.formulario1.Fec_Fac.value.trim();
    if (Fec_Fac === "") {
    alert("Agregue fecha de fabricacion, verifique.");
    return false;
    } 

    var origen = document.formulario1.origen.value.trim();
    if ((origen === "-Origen-") || (origen === "")) {
    alert("Seleccione Origen, verifique.");
    return false;
    }
    
    var marca = document.formulario1.Marca.value.trim();
    if ((marca === "-Marca-") || (marca === "")) {
    alert("Agregue Marca, verifique.");
    return false;
    }
    
    var proveedor = document.formulario1.proveedor.value.trim();
    if ((proveedor === "-Proveedor-") || (proveedor === "")) {
    alert("Seleccione Proveedor, verifique.");
    return false;
    }
    
    var cb = document.formulario1.cb.value.trim();
    if ((cb === "") || (cb === "0")) {
    alert("Agregue CB, verifique.");
    return false;
    }
    
    var ubica = document.formulario1.UbicaNueva.value.trim();
    if ((ubica === "-Ubicación-") || (ubica === "")) {
    alert("Seleccione Ubicación, verifique.");
    return false;
    }

    }
    
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
            i = - 1;
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
    var key = window.event.keyCode; //codigo de tecla. 
    if (key < 48 || key > 57) {//si no es numero 
    window.event.keyCode = 0; //anula la e        ntrada        de texto. 
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
            i = - 1;
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
