
<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="com.medalfa.saa.utils.StaticText"%>
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
    String usua = "", Clave = "";
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
        <title>SIALSS_CTRL ALTA MEDICAMENTO FRIA</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Catalogo de Insumo para la Salud RED FRIA</h3>
                </div>
                <div class="panel-body ">
                 
                        <div class="row">
                            <div class="form-group">                                
                                <label for="Clave" class="col-xs-1 control-label">CLAVE</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="claveBusqueda" name="claveBusqueda" maxlength="60" placeholder="CLAVE" required="true" autocomplete="autocomplete-active"/>
                                </div>
                                <label for="Clave" class="col-xs-1 control-label">Descripcion</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="descripcionBusqueda" name="descripcionBusqueda" maxlength="250" placeholder="Descripcion"  required="true" />
                                </div>
                                <label for="Sts" class="col-xs-1 control-label">Tipo</label>
                                <div class="col-xs-2">                                                                        
                                    <select name="ClaveRF" id="ClaveRF" class="form-control">
                                        <option value="0">--Seleccione--</option>
                                        <option value="1">RED FRIA</option>
                                        <option value="2">CONTROLADO</option>
                                        <option value="3">APE</option>
                                    </select>                                    
                                </div>
                            </div>
                            
                        <hr/>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="AltaFria">Agregar</button>
                            </div>
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="BajaFria">Baja</button>
                            </div>
                            
                        </div>
                   
                </div>
                        <div class="footer">
                           
                            <div class="panel table table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="datosRedFria">

                                    <thead>
                                    <th>CLAVE</th>
                                    <th>DESCRIPCION</th>
                                    <th>TIPO</th>
                                    </thead>
                                    <tbody>
                                        <tr class="  "></tr>
                                        <tr class="  "></tr>
                                        <tr class="  "></tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <hr/>
                        
                    
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
                                        $('#datosRedFria').dataTable();
                                    });
        </script>
        <script type="text/javascript">
          $("#claveBusqueda").autocomplete({
                source: claves_productos,
                select: function (event, ui)
                {
                  
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_CLAVE%>", ui.item.value, fechaInicial, fechaFinal);
                }
            });
             $("#searchByClaveKardex").click(function ()
            {
                var clave = $("#claveBusqueda").val();
                var descripcion = $("#descripcionBusqueda").val();
            

                if (clave !== '')
                {
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_CLAVE%>", clave, fechaInicial, fechaFinal);
                } else if (descripcion !== '')
                {
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_DESCRIPCION%>", descripcion, fechaInicial, fechaFinal);
                }                
                else
                {
                    swal("Atención", "Favor de verificar criterios de búsqueda", "warning");
                }


            });

        </script>
        <script type="text/javascript">
          $("#descripcionBusqueda").autocomplete({
                source: descripcion_productos,
                select: function (event, ui)
                {
                  
                    dataSelectedAutocomplete(<%=StaticText.OBTENER_INFORMACION_CLAVE%>, "<%=StaticText.BUSCAR_DESCRIPCION%>", ui.item.value, fechaInicial, fechaFinal);
                }

            });
        </script>
        <script src="${generalContext}/js/kardex/kardexReload.js" ></script>
    </body>
</html>


