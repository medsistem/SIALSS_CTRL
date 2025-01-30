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
    String usua = "", tipou = "";
    tipou = (String) sesion.getAttribute("Tipo");
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        
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
        </div>
        
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catalogo de Proveedores</h3>
                </div>
                
                <div class="panel-body ">
                     <% if(tipou.equals("1") || tipou.equals("11") || tipou.equals("13")){ %>
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="Proveedores">
                        <div class="form-group">
                            <div class="form-group">
                                <label for="Nombre" class="col-xs-1 control-label">Nombre*</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Nombre" name="Nombre" maxlength="150" placeholder="Nombre" onKeyPress="return tabular(event, this)" />
                                </div>
                                <label for="Telefono" class="col-xs-1 control-label">Teléfono* </label>
                                <div class="col-xs-2">
                                    <input name="Telefono" type="text" class="form-control" id="Telefono" placeholder="Telefono" onKeyPress="LP_data();
                                            anade(this);
                                            return isNumberKey(event, this);" maxlength="14" /><h6>(XXX) XXX-XXXX</h6></div>
                                </div>

                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label for="Direccion" class="col-xs-1 control-label">Dirección*</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Direccion" maxlength="200" name="Direccion" placeholder="Direccion" onKeyPress="return tabular(event, this)" />
                                </div>
                                <label for="Colonia" class="col-xs-1 control-label">Colonia*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Colonia" name="Colonia" maxlength="150" placeholder="Colonia" onKeyPress="return tabular(event, this)" />
                                </div>
                                <label for="CP" class="col-xs-2 control-label">C.P.*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="CP" name="CP" placeholder="CP" maxlength="5" onKeyPress="return isNumberKey(event, this);" />
                                </div>
                            </div>

                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label for="RFC" class="col-xs-1 control-label">RFC*</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="RFC" name="RFC" maxlength="15" placeholder="RFC" onKeyPress="return tabular(event, this)" />
                                </div>      
                                <label for="FAX" class="col-xs-1 control-label">FAX*</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="FAX" name="FAX" placeholder="FAX" maxlength="14" onKeyPress="LP_data();
                                            anade(this);
                                            return isNumberKey(event, this);" />
                                </div>
                                <label for="Mail" class="col-xs-1 control-label">Mail*</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Mail" name="Mail" placeholder="Mail" maxlength="60" onKeyPress="return tabular(event, this)" />
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-block btn-primary" type="submit" name="accion" value="guardar" onclick="return valida_alta();"> Guardar</button> 

                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                    <% }%> 
                </div>
               
                <div class="panel-footer">
                    <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv">
                        <thead>
                            <tr>
                                <td>Clave</td>
                                <td>Nombre</td>
                                <td>Dirección</td>
                                <td>Teléfono</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT * FROM tb_proveedor GROUP BY F_NomPro ORDER BY F_NomPro ASC");
                                    while (rset.next()) {
                            %>
                            <tr class="odd gradeX">
                                <td><small><%=rset.getString(1)%></small></td>
                                <td><small><%=rset.getString(2)%></small></td>
                                <td><small><%=rset.getString(3)%></small></td>
                                <td><small><%=rset.getString(6)%></small></td>                            

                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                }
                            %>
                        </tbody>
                    </table>
                          <hr/>
                        <div> 
                                <div class="col-lg-2"><a class="btn btn-block btn-info" onclick="window.close();">Salir</a></div>
                    </div>      
                </div>
            </div>
        </div>
        
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
