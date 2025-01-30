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
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/select2.css" rel="stylesheet">
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
            <%@include file="jspf/menuPrincipalCompra.jspf" %>
            <hr/>
        </div>
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catalogo de Unidades</h3>
                </div>
                <div class="panel-footer">
                    <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv">
                        <thead>
                            <tr>
                                <td>Clave</td>
                                <td>Nombre</td>
                                <td>Sts</td>
                                <td>Direcci&oacute;n</td>
                                <td>Tipo Unidad</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT u.F_ClaCli, u.F_NomCli, F_StsCli,u.F_Direc, tu.F_NomUni FROM tb_uniatn u inner join tb_tipunidad tu on u.F_Tipo = tu.F_idTipUni ORDER BY F_ClaCli ASC");
                                    while (rset.next()) {
                            %>
                            <tr class="odd gradeX">
                                <td class="Clave"><small><%=rset.getString(1)%></small></td>
                                <td class="Nombre"><small><%=rset.getString(2)%></small></td>
                                <td class="Sts"><small><%=rset.getString(3)%></small></td>
                                <td class="Direc"><small><%=rset.getString(4)%></small></td>
                                <td class="Tipo"><small><%=rset.getString(5)%></small></td>
                            </tr>
                            <%
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
        <div id="Devolucion" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>
                            Modificar Unidad
                        </h4>
                    </div>
                    <form name="AltaUni" action="AltaUnidad" method="Post">
                        <div class="modal-body">
                            <div class="row">
                                <h4 class="col-sm-2">Clave Unidad</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Clave1" id="Clave1" type="text" value="" readonly="" required="">
                                </div>                                
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Nombre Unidad</h4>
                                <div class="col-sm-10">
                                    <input class="form-control" name="Nombre1" id="Nombre1" type="text" value="" required="">
                                </div>                                
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Direcci&oacute;n</h4>
                                <div class="col-sm-10">
                                    <input class="form-control" name="Direc1" id="Direc1" type="text" value="" required="">
                                </div>                                
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Sts</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Sts1" id="Sts1" type="text" value="" required="">
                                </div>                                
                            </div>  
                            <div class="row">
                                <h4 class="col-sm-2">Tipo Unidad</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="TipoU" id="TipoU" type="text" value="" required="" readonly="">                                    
                                </div>
                                <div class="col-xs-2">
                                    <select name="tipou" id="tipou">
                                        <option value="">--Seleccione--</option>
                                        <option value="RURAL">RURAL</option>
                                        <option value="CEAPS">CEAPS</option>
                                        <option value="CSU">CSU</option>
                                        <option value="CAD">CAD</option>
                                        <option value="GEDIATRICAS">GERIATRICAS
                                        <option value="HG">HOSPITAL GENERAL</option>
                                        <option value="HMI">HOSPITAL MATERNO INFANTIL</option>
                                        <option value="HM">HOSPITAL MUNICIPAL</option>
                                        <option value="HPS">HOSPITAL PSIQUIATRICO</option>
                                        <option value="CISAME">CISAME</option>
                                        <option value="HV">HOSPITAL VISUAL</option>
                                        <option value="CM">CENTRO MEDICO</option>
                                        <option value="HMP">HOSPITAL MATERNO PERINATAL</option>
                                        <option value="JS">JURISDICCION SANITARIA</option>
                                        <option value="SOLUCIONES">SOLUCIONES</option>
                                        <option value="SUEM">SUEM</option>
                                        <option value="DISPENSADORES">DISPENSADORES</option>
                                        <option value="ODONTOPEDRIATICO">ODONTOPEDRIATICO</option>
                                    </select>
                                </div>
                            </div>  
                            <div class="row">
                                <h4 class="col-sm-2">Dispensador</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Dispensa" id="Dispensa" type="text" value="" required="" readonly="">
                                </div>
                                <div class="col-xs-2">
                                    <select name="dispensadoru" id="dispensadoru">
                                        <option value="">--Seleccione--</option>
                                        <option value="DIMESA">DIMESA</option>
                                        <option value="DISUR">DISUR</option>
                                        <option value="MEDALFA">MEDALFA</option>
                                        <option value="SOLUGLOB">SOLUGLOB</option>
                                        <option value="MOTION">MOTION</option>
                                        <option value="ISEM">ISEM</option>
                                    </select>
                                </div>
                            </div>  
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="Modificar">Guardar</button>
                            <button type="submit" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </div>
                    </form>
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
        <script src="js/select2.js"></script>
        <script>
                            $(document).ready(function () {
                                $('#datosProv').dataTable();
                                $('#juris').select2();
                            });
        </script>
        <script type="text/javascript">
            $(".rowButton").click(function () {
                var $row = $(this).closest("tr");
                var clave = $row.find("td.Clave").text();
                var nombre = $row.find("td.Nombre").text();
                var sts = $row.find("td.Sts").text();
                var dir = $row.find("td.Direc").text();
                var tipo = $row.find("td.Tipo").text();
                var dispen = $row.find("td.Dispen").text();

                $("#Clave1").val(clave);
                $("#Nombre1").val(nombre);
                $("#Sts1").val(sts);
                $("#Direc1").val(dir);
                $("#TipoU").val(tipo);
                $("#Dispensa").val(dispen);

            });
            $('#tipou').change(function () {
                var tipou = $('#tipou').val();
                if (tipou != '') {
                    $('#TipoU').val(tipou);
                }
            });

            $('#dispensadoru').change(function () {
                var dispenu = $('#dispensadoru').val();

                if (dispenu != '') {
                    $('#Dispensa').val(dispenu);
                }
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
                /*var Clave = document.formulario1.Clave.value;*/
                var Nombre = document.formulario1.Nombre.value;
                var Telefono = document.formulario1.Telefono.value;
                if (Nombre === "" || Telefono === "") {
                    alert("Tiene campos vacíos, verifique.");
                    return false;
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
                var key = window.event.keyCode; //codigo de tecla. 
                if (key < 48 || key > 57) {//si no es numero 
                    window.event.keyCode = 0; //anula la entrada de texto. 
                }
            }
            function anade(esto) {
                if (esto.value === "(55") {
                    if (esto.value.length === 0) {
                        if (esto.value.length === 0) {
                            esto.value += "(";
                        }
                    }
                    if (esto.value.length > otro) {
                        if (esto.value.length === 3) {
                            esto.value += ") ";
                        }
                    }
                    if (esto.value.length > otro) {
                        if (esto.value.length === 9) {
                            esto.value += "-";
                        }
                    }
                    if (esto.value.length < otro) {
                        if (esto.value.length === 4 || esto.value.length === 9) {
                            esto.value = esto.value.substring(0, esto.value.length - 1);
                        }
                    }
                } else {
                    if (esto.value.length === 0) {
                        if (esto.value.length === 0) {
                            esto.value += "(";
                        }
                    }
                    if (esto.value.length > otro) {
                        if (esto.value.length === 4) {
                            esto.value += ") ";
                        }
                    }
                    if (esto.value.length > otro) {
                        if (esto.value.length === 9) {
                            esto.value += "-";
                        }
                    }
                    if (esto.value.length < otro) {
                        if (esto.value.length === 4 || esto.value.length === 9) {
                            esto.value = esto.value.substring(0, esto.value.length - 1);
                        }
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

