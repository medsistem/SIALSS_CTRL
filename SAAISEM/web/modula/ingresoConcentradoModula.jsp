<%-- 
    Document   : ingresoConcentradoModula
    Created on : 27/03/2015, 04:30:53 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd"); %>
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
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String F_FecEnt = "";
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
    <body class="container">
        <h1>SIALSS_CTRL</h1>
        <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
        <hr/>
        <h4>Concentrado - Modula</h4>
        <div class="row">
            <H4 class="col-sm-3">Seleccione Concentrado:</H4>
            <div class="col-sm-6">
                <form action="ingresoConcentradoModula.jsp" method="post">
                    <select class="form-control" onchange="this.form.submit()" name="F_FecEnt">
                        <option>Seleccione Fecha</option>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_FecEnt, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') as F_Fecha from tb_facttemp where F_StsFact!=5 group by F_FecEnt");
                                while (rset.next()) {
                        %>
                        <option value="<%=rset.getString("F_FecEnt")%>"><%=rset.getString("F_Fecha")%></option>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                            }
                        %>
                    </select>
                </form>
            </div>
        </div>
        <%
            try {
                con.conectar();
                if (request.getParameter("F_FecEnt") != null) {
                    F_FecEnt = request.getParameter("F_FecEnt");
        %>
        <div id="tablaConcentrado">
            <h3>Concentrado de la fecha: <%=df3.format(df2.parse(F_FecEnt))%></h3>
            <form method="post">
                <table class="table table-bordered table-condensed table-striped" id="tablaConcenModula">
                    <thead>
                        <tr>
                            <td>Clave</td>
                            <td>Descripción</td>
                            <td>Lote</td>
                            <td>Caducidad</td>
                            <td>Cantidad</td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ResultSet rset = con.consulta("select F_Id, F_IdLote, F_FecEnt, v.F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad, F_Cb, SUM(F_Cant) as F_Cant, F_Ubica, F_StsMod, F_DesPro from v_folioremisiones v, tb_medica m where v.F_ClaPro = m.F_ClaPro and v.F_StsMod='1' group by m.F_ClaPro, v.F_ClaLot, v.F_FecCad order by v.F_ClaPro+0");
                            while (rset.next()) {

                        %>
                        <tr>
                            <td><%=rset.getString("F_ClaPro")%></td>
                            <td><%=rset.getString("F_DesPro")%></td>
                            <td><%=rset.getString("F_ClaLot")%></td>
                            <td><%=rset.getString("F_FecCad")%></td>
                            <td><%=rset.getString("F_Cant")%></td>
                            <td id="Clave<%=rset.getString("F_ClaPro")%>">
                                <button type="button" class="btn btn-primary btn-sm" onclick="validaClave(this)" value="<%=rset.getString("F_ClaPro")%>"><span class="glyphicon glyphicon-ok"></span></button>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </form>
        </div>
        <%                }
                con.cierraConexion();
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        %>


        <!-- ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
                                    $(document).ready(function() {
                                        $('#tablaConcenModula').dataTable();
                                    });

                                    function validaClave(e) {
                                        var clave = e.value;
                                        //alert(clave);
                                        //Se quitará el registro
                                        var dir = '../AbasteceConcentrado?accion=validaClaveConcen&F_ClaPro=' + clave + '&F_FecEnt=<%=F_FecEnt%>';
                                        var form = $('#formIngresa');
                                        $.ajax({
                                            type: form.attr('method'),
                                            url: dir,
                                            data: form.serialize(),
                                            success: function(data) {
                                                recargaTabla();
                                            },
                                            error: function(data) {
                                                alert('Error al insertar el registro');
                                            }
                                        });

                                        function recargaTabla() {
                                            $('#Clave' + clave).load('ingresoConcentradoModula.jsp?F_FecEnt=<%=F_FecEnt%> #Clave' + clave);
                                        }
                                    }
        </script>
    </body>
</html>
