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
    String usua = "";
    String tipo = "", fol_gnkl = "", Tempera="";

    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
        fol_gnkl = (String) sesion.getAttribute("Folio");
    } else {
        response.sendRedirect("index.jsp");
    }

    ConectionDB con = new ConectionDB();

    System.out.println("Hola" + fol_gnkl);

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
            <div>
                <h3>Modificar Reporte Red Fría</h3>
                <h4>Folio de Factura: <%=fol_gnkl%></h4>
                <%
                    try {
                        con.conectar();
                        try {
                            ResultSet Consulta = con.consulta("SELECT * FROM tb_temperatura;");
                            if(Consulta.next()){
                                Tempera = Consulta.getString(1);
                            }
                            ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantReq) as requerido,SUM(F.F_CantSur) as surtido,F.F_Costo,SUM(F.F_Monto) as importe, F.F_Ubicacion FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + fol_gnkl + "' GROUP BY F.F_ClaDoc");
                            while (rset.next()) {


                %>
                <h4>Cliente: <%=rset.getString(1)%></h4>
                <h4>Fecha de Entrega: <%=rset.getString(2)%></h4>
                <h4>Factura: <%=rset.getString(3)%></h4>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <a href="RedFria.jsp" class="btn btn-block btn-info">Regresar</a>
                        </div>
                        <div class="col-sm-6">
                            <a href="RedFriareimp.jsp?fol_gnkl=<%=fol_gnkl%>&temp=<%=Tempera%>" target="_blank" class="btn btn-block btn-info">Imprimir Reporte</a>                    
                        </div>
                    </div>
                </div>
                <%
                            }
                        } catch (Exception e) {

                        }
                        con.cierraConexion();
                    } catch (Exception e) {

                    }
                %>
                <br />
                <div class="panel panel-primary">
                    <div class="panel-body">
                        <table class="table table-bordered table-striped" id="datosCompras">
                            <thead>
                                <tr>
                                    <td>Clave</td>
                                    <td>Lote</td>
                                    <td>Caducidad</td>                                    
                                    <td>Surtido</td>
                                    <td>Eliminar</td>
                                    <td></td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            ResultSet rset = con.consulta("SELECT F_ClaPro,F_ClaLot,Caducidad,CantSur,F_Id FROM tb_redfriaimp WHERE F_ClaDoc='" + fol_gnkl + "' AND F_DesJurIS !='';");
                                            while (rset.next()) {

                                %>
                                <tr>
                                    <td class="Clave"><%=rset.getString(1)%></td>
                                    <td class="Lote"><%=rset.getString(2)%></td>
                                    <td class="Caducidad"><%=rset.getString(3)%></td>
                                    <td class="Cantidad"><%=rset.getString(4)%></td>                                    
                                    <td>
                                        <form action="RedFriaReporte" method="post">
                                            <input class="hidden" name="IdFol" value="<%=rset.getString(5)%>">
                                            <input class="hidden" name="fol_gnkl" value="<%=fol_gnkl%>">
                                            <button class="btn btn-block btn-info" name="accion" value="Eliminar"><span class="glyphicon glyphicon-trash"></span></button>                                        
                                        </form>
                                    </td>
                                    <td class="Identificador"><%=rset.getString(5)%></td>
                                </tr>
                                <%
                                            }
                                        } catch (Exception e) {

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
        </div>
        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>
    </body>
</html>


<!-- 
================================================== -->
<!-- Se coloca al final del documento para que cargue mas rapido -->
<!-- Se debe de seguir ese orden al momento de llamar los JS -->
<script src="js/jquery-1.9.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
    $(document).ready(function() {
        $('#datosCompras').dataTable();
    });
</script>
<script>
    $(function() {
        $("#fecha").datepicker();
        $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
    });
</script>
<script type="text/javascript">
    function validaRemision() {
        var confirmacion = confirm('Seguro que desea generar la Devolución');
        if (confirmacion === true) {
            //$('#btnGeneraFolio').prop('disabled', true);
            return true;
        } else {
            return false;
        }
    }

</script>