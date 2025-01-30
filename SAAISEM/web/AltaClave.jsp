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
    String usua = "", Clave = "", DesTipo = "", F_DesPro = "", F_StsPro = "", F_Catipo = "", F_Costo = "";
    int tipo = 0, F_Origen = 0, F_IncMen = 0;
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
        <title>SIALSS_CTRL ALTA MEDICAMENTO</title>
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
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Clave" name="Clave" maxlength="60" placeholder="CLAVE" value="" required="" />
                                </div>
                                <label for="Clave" class="col-xs-1 control-label">CLAVE SS</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="ClaveSS" name="ClaveSS" maxlength="250" placeholder="CLAVE SS" value="" required="" />
                                </div>
                                <label for="Sts" class="col-xs-1 control-label">Status</label>
                                <div class="col-xs-2">                                                                        
                                    <input type="radio" name="radiosts" id="radiosts" checked="" value="A">Activo&nbsp;<input type="radio" name="radiosts" id="radiosts" value="S">Suspendido                                    
                                </div>
                            </div>
                            <div class="row">
                                <label for="Descripcion" class="col-xs-2 control-label">Descripción</label>
                                <div class="col-xs-8">
                                    <input type="text" class="form-control" id="Descripcion" maxlength="250" name="Descripcion" placeholder="Descripcion" required=""   />
                                </div>   
                            </div>
                            <br/>
                            
                            <div class="row">
                                <label for="Presentacion" class="col-xs-2 control-label">Presentacion</label>
                                <div class="col-xs-8">
                                    <input type="text" class="form-control" id="Descripcion" maxlength="60" name="Presentacion" placeholder="Presentacion" required=""   />
                                </div>   
                            </div>
                        </div>
                        <hr/>
                        
                        <div class="row">
                            <label for="list_medica" class="col-xs-1 control-label">ORIGEN</label>
                            <div class="col-sm-4"> 
                                 <select class="form-control" name="radiorigen" id="radiorigen">
                                        <option value="0">-Selec Origen-</option>
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
                                
                                <!--table  class="table" id="datosOrigen">     
                                    <tbody>
                                        <tr>    
                                            <td><input type="radio" name="radiorigen" id="radiorigen" checked="" value="1">MICH ABASTO REGULAR</td>
                                            <td><input type="radio" name="radiorigen" id="radiorigen" value="2">VENTA</td>
                                            <td><input type="radio" name="radiorigen" id="radiorigen" value="3">MICH COVID</td>
                                        </tr>
                                        <tr>
                                            <td><input type="radio" name="radiorigen" id="radiorigen" value="4">INSABI MICH ABASTO REGULAR </td>
                                            <td><input type="radio" name="radiorigen" id="radiorigen" value="5">INSABI MICH COVID</td>
                                            <td><input type="radio" name="radiorigen" id="radiorigen" value="6">DONACION</td>
                                        </tr>
                                    </tbody>
                                </table-->
                                <!--input type="radio" name="radiorigen" id="radiorigen" checked="" value="1">ADMÓN&nbsp;<input type="radio" name="radiorigen" id="radiorigen" value="2">VENTA-->                                
                            </div>                             
                            <!--label for="list_medica" class="col-xs-1 control-label">PARTIDA</label-->
                            <label for="list_medica" class="col-xs-1 control-label">Tipo: </label>
                            <div class="col-sm-5"> 
                                <table class="table"  id="datosTipo">     
                                    <tbody>
                                        <tr>
                                            <td><input type="radio" name="radiotipo" id="radiotipo" checked="" value="2504">Medicamento&nbsp;</td>
                                             <td><input type="radio" name="radiotipo" id="radiotipo" value="2505">Mat. Curación</td>
                                        </tr>
                                        
                                    </tbody>
                                </table>
                            </div>                            
                        </div>
                        <hr/>
                        <div class="row">                            
                            <label for="list_medica" class="col-xs-1 control-label">CATÁLOGO</label>
                            <div class="col-xs-11">
                                <table  class="table table-sm" id="datosnivel">
                            
                                <tbody>
                                <tr>
                                <td><small><input type="checkbox" name="cat1" id="cat1" value="1">(1)CSRD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat2" id="cat2" value="2">(2)CSU&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat3" id="cat3" value="3">(3)HOSPITAL MUNICIPAL&nbsp</small></td>
                                <td><small><input type="checkbox" name="cat4" id="cat4" value="4">(4)HOSPITAL GENERAL&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat5" id="cat5" value="5">(5)HOSPITAL MATERNO INFANTIL&nbsp;</small></td>
                                </tr><tr>
                                <td><small><input type="checkbox" name="cat6" id="cat6" value="6">(6)HOSPITAL MATERNO PERINATAL&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat7" id="cat7" value="7">(7)CENTRO MEDICO&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat8" id="cat8" value="8">(8)HOSPITAL PSIQUIATRICO&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat9" id="cat9" value="9">(9)SUEM&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat10" id="cat10" value="10">(10)PD&nbsp;</small></td>
                                </tr><tr>
                                <td><small><input type="checkbox" name="cat11" id="cat11" value="11">(11)PD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat12" id="cat12" value="12">(12)PD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat14" id="cat14" value="14">(14)CAD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat15" id="cat15" value="15">(15)MOD. ODONTOPEDIATRICO&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat16" id="cat16" value="16">(16)PD&nbsp;</small></td>
                                </tr><tr>
                                <td><small><input type="checkbox" name="cat216" id="cat216" value="216">(216)PD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat17" id="cat17" value="17">(17)GER&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat217" id="cat217" value="217">(217)CEAPS/GER&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat18" id="cat18" value="18">(18)PD&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat19" id="cat19" value="19">(19)HOSPITAL VISUAL&nbsp;</small></td>
                                <td><small></tr><tr>
                                <td><small><input type="checkbox" name="cat20" id="cat20" value="20">(20)DISPENSADORES&nbsp;</small></td>
                                <td><small><input type="checkbox" name="cat30" id="cat30" value="30">(30)SOLUCIONES&nbsp;</small></td>
                            </tr>
                            </tbody>
                            </table>                                            
                        </div>
                            </div>
                       <hr/>
                       
                        <div class="row">
                            <label for="Costo" class="col-xs-1 control-label">Precio</label>
                            <div class="col-xs-2">
                                <input type="text" class="form-control" id="Costo" name="Costo" placeholder="Costo" value="<%=F_Costo%>" required=""  />
                            </div>
                            <label for="Costo" class="col-xs-2 control-label">%Inc. Mensual</label>
                            <div class="col-xs-2">
                                <input type="text" class="form-control" id="Incmen" name="Incmen" placeholder="Inc Mensual" value="<%=F_IncMen%>" onKeyPress="return isNumberKey(event, this)" />
                            </div>
                            <label for="Costo" class="col-sm-1 control-label">Costo Nim</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="CostoNim" name="CostoNim" placeholder="Costo Nim" value="" required=""  />
                            </div>                                
                        </div>
                        <br/>
                        
                        <div class="row">                                
                            <label for="Costo" class="col-sm-1 control-label">Origen Nim</label>
                            <div class="col-sm-2">
                                <select name="origennim" id="origennim" class="form-control" required="">
                                    <option></option>
                                    <option value="LP">LP</option>
                                    <option value="CC">CC</option>
                                    <option value="CC/LP">CC/LP</option>
                                </select>
                            </div>
                            <label for="GrupoNim" class="col-sm-1 control-label">Grupo</label>
                            <div class="col-sm-2">
                                <!--input type="text" class="form-control" id="GrupoNim" name="GrupoNim" placeholder="Grupo Nim" value="" required=""/-->
                                <select class="form-control" name="GrupoNim" id="GrupoNim">
                                        <option value="0">-Selec GrupoNim-</option>
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
                            <label for="Costo" class="col-sm-1 control-label">Comentario Nim</label>
                            <div class="col-sm-4">                                
                                <select name="ComentarioNim" id="ComentarioNim" class="form-control" required="">
                                    <option></option>
                                    <option value="CLAVE HABILITADO 2018">CLAVE HABILITADO 2018</option>
                                    <option value="CLAVE HABILITADO 2018">CLAVE HABILITADO 2019</option>
                                     <option value="CLAVE HABILITADO 2019">CLAVE HABILITADO 2020</option>
                                    <option value="CLAVE INHABILITADO">CLAVE INHABILITADO</option>
                                    <option value="CLAVE SOLUCIONES">CLAVE SOLUCIONES</option>
                                    <option value="HABILITADO HASTA AGOTAR EXISTENCIAS">HABILITADO HASTA AGOTAR EXISTENCIAS</option>
                                </select>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <label for="Costo" class="col-sm-1 control-label">Catálogo Nim</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="CatalogoNim" name="CatalogoNim" placeholder="Catálogo Nim" value="" required=""  />
                            </div>
                            <label for="Costo" class="col-sm-1 control-label">Cant. Recibir</label>
                            <div class="col-sm-2">
                                <input type="number" min="0" class="form-control" id="CantRecibir" name="CantRecibir" placeholder="Cantidad Max. Recibir" value="" required=""  />
                            </div>
                            <div class="row">
                            <label for="Costo" class="col-sm-1 control-label">Prioridad</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="Prioridad" name="Prioridad" placeholder="Prioridad" value="" required=""  />
                            </div>
                            </div>
                        
                        </div>
                        
                        <hr/>
                        <div class="row">
                            <label for="list_medica" class="col-xs-1 control-label">PROYECTOS</label>
                            <div class="col-xs-11">
                                <table class="table table-striped" id="datosProyec">

                                    <tbody>
                                        <tr>
                                            <td><small><input type="checkbox" name="bProIsem" id="bProIsem" value="1"> (ISEM)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProMic" id="bProMic" value="1"> (MICHOACAN)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProIssemym" id="bProIssemym" value="1">(ISSEMYM)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProDDS" id="bProDDS" value="1"> (DISENO DE SALUD)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProEnseres" id="bProEnseres" value="1"> (ENSERES)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProAPM" id="bProIssemym" value="1"> (INVENTARIO CEDIS CENTRAL)&nbsp;</small></td>
                                        </tr>
                                        <tr>
                                            <td><small><input type="checkbox" name="bProI30" id="bProIssemym" value="1"> (ISEM 30)&nbsp;</small></td> 
                                            <td><small><input type="checkbox" name="bProEH-URU" id="bProIssemym" value="1"> (EH-URUAPAN)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProTHOMA" id="bProIssemym" value="1"> (THOMASANT)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProSAME" id="bProIssemym" value="1"> (SAMEDIC EEM)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProSinergo" id="bProSinergo" value="1"> (SINERGO)&nbsp;</small></td>
                                             <td><small><input type="checkbox" name="bProSonora" id="bProSonora" value="1"> (SONORA)&nbsp;</small></td>
                                        </tr><tr>
                                            <td><small><input type="checkbox" name="bProISOL" id="bProIssemym" value="1"> (ISEM SOL)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProEH-URU" id="bProIssemym" value="1"> (EH-CIVIL MORELIA)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProTHOMANES" id="bProIssemym" value="1"> (THOMASAN ISEM ANESTESIA)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProUPUEBLA" id="bProIssemym" value="1"> (UCIN PUEBLA)&nbsp;</small></td>
                                        </tr><tr>
                                            <td><small><input type="checkbox" name="bProIANES" id="bProIssemym" value="1"> (ISEM ANESTESIA)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProAcred" id="bProIssemym" value="1"> (ACREDITACIONES)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProTHOMAMER" id="bProIssemym" value="1"> (THOMASAN MERIDA)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProMinds" id="bProIssemym" value="1"> (Medical Minds)&nbsp;</small></td>
                                        </tr><tr>
                                            <td><small><input type="checkbox" name="bProIANUAL" id="bProIssemym" value="1"> (ISEM ANUAL)&nbsp;</small></td>
                                            <td><small><input type="checkbox" name="bProHEMO" id="bProIssemym" value="1"> (HEMODIALISIS)&nbsp;</small></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <hr/>
                        <div class="row">
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="Agregar">Agregar</button>
                            </div>
                            <div class="col-sm-6">
                                <a href="medicamento.jsp" class="btn btn-block btn-info" type="submit"  >Regresar</a>   
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
    </body>
</html>



