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
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <div class="row">
                <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
                <hr/>
                <h3 class="col-sm-10">Capturar Claves</h3>
                <a href="nuevoInventario.jsp" class="col-sm-2 btn btn-default">Regresar</a>
            </div>

            <form id="formBusca" method="post">
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Búsqueda:</h4>
                    </div>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="Ingresa CB Ubi" id="buscarUbi" name="buscarUbi"/>
                    </div>
                    <!--div class="col-sm-2">
                        <input class="form-control" placeholder="Ingresa CB Med" id="buscarMed" name="F_CBMed"/>
                    </div>
                    <div class="col-sm-4">
                        <input class="form-control" placeholder="Ingrese Descripción" id="buscarDescrip" name="F_DesMed"/>
                    </div-->
                    <div class="col-sm-2">
                        <button class="btn btn-block btn-primary" id="btnBuscar">Buscar</button>
                    </div>
                </div>
            </form>
            <hr/>
            <form name="formIngresa" id="formIngresa" method="post">
                <div class="row">
                    <h4 class="col-sm-1 text-right">Clave</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_ClaPro" name="F_ClaPro"/>
                    </div>
                    <div class="col-sm-2">
                        <select class="form-control" id="selectClave" onchange="cambiaLoteCadu(this)">
                            <option value="">----</option>
                        </select>
                    </div>
                    <h4 class="col-sm-1 text-right" >Lote</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_ClaLot" name="F_ClaLot"/>
                    </div>
                    <div class="col-sm-2">
                        <select class="form-control" id="selectLote">
                            <option value="">----</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <h4 class="col-sm-1 text-right" >Cadu</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_FecCad" name="F_FecCad" onKeyPress="
                                return LP_data(event, this);
                                anade(this, event);
                                return tabular(event, this);
                               " maxlength="10" />
                    </div>
                    <div class="col-sm-2">
                        <select class="form-control" id="selectCadu">
                            <option value="">----</option>
                        </select>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <h4 class="col-sm-1 text-right" >Ubicación</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_ClaUbi" name="F_ClaUbi"/>
                    </div>
                    <h4 class="col-sm-1 text-right" >Present</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_Presentacion" name="F_Presentacion" />
                    </div><div class="col-sm-2">
                        <select class="form-control" id="selectResto">
                            <option value="">Presentación</option>
                            <option value="Resto">Resto</option>
                        </select>
                    </div>
                </div>
                <hr/>
                <div class="row">
                    <h4 class="col-sm-2 text-right col-sm-offset-1" >Total Existencias</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="" id="F_Total" name="F_Total" />
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-block btn-primary" id="btnGuardar">Guardar</button>
                    </div>
                    <div class="col-sm-2">
                        <a class="btn btn-block btn-info" href="Comparativo.jsp">Comparativo</a>
                    </div>
                </div>
            </form>
            <hr/>
            <div class="table-responsive" id="tbInsumo">
                <table class="table table-striped table-bordered table-condensed">
                    <tr>
                        <td>Clave</td>
                        <td>Lote</td>
                        <td>Caducidad</td>
                        <td>Ubicación</td>
                        <td>Cantidad</td>
                        <td></td>
                    </tr>
                    <%
                        try {
                            con.conectar();
                            ResultSet rset = con.consulta("select * from tb_loteinv where F_ExiLot!=0 and F_Ubica='" + request.getParameter("F_Ubica") + "'");
                            while (rset.next()) {
                    %>
                    <tr>
                        <td><%=rset.getString("F_ClaPro")%></td>
                        <td><%=rset.getString("F_ClaLot")%></td>
                        <td><%=rset.getString("F_FecCad")%></td>
                        <td><%=rset.getString("F_Ubica")%></td>
                        <td><%=formatter.format(rset.getInt("F_ExiLot"))%></td>
                        <td><button class="btn btn-sm btn-info"><span class="glyphicon glyphicon-remove"></span></button></td>
                    </tr>
                    <%
                            }
                            con.cierraConexion();
                        } catch (Exception e) {

                        }
                    %>
                </table>
            </div>
            <br><br><br>
            <div class="navbar  navbar-inverse">
                <div class="text-center text-muted">
                    Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                    Todos los Derechos Reservados
                </div>
            </div>
    </body>
    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="../js/jquery-1.9.1.js"></script>
    <script src="../js/bootstrap.js"></script>
    <script src="../js/funcInvCiclico.js"></script>
    <script src="../js/jquery-ui.js"></script>
    <script>
                            otro = 0;
                            function LP_data(e, esto) {
                                var key = (document.all) ? e.keyCode : e.which; //codigo de tecla. 
                                if (key < 48 || key > 57)//si no es numero 
                                    return false; //anula la entrada de texto.
                                else
                                    anade(esto);
                            }
                            function tabular(e, obj) {
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
                            function anade(esto) {
                                if (esto.value.length > otro) {
                                    if (esto.value.length === 2) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length > otro) {
                                    if (esto.value.length === 5) {
                                        esto.value += "/";
                                    }
                                }
                                if (esto.value.length < otro) {
                                    if (esto.value.length === 2 || esto.value.length === 5) {
                                        esto.value = esto.value.substring(0, esto.value.length - 1);
                                    }
                                }
                                otro = esto.value.length;
                            }
    </script>
</html>

