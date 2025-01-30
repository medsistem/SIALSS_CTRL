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
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        //response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String Fecha = "";
    String fechaCap = "";
    String Proveedor = "";
    try {
        fechaCap = df2.format(df3.parse(request.getParameter("Fecha")));
        Fecha = request.getParameter("Fecha");
    } catch (Exception e) {

    }
    if(fechaCap==null){
        fechaCap="";
    }
    try {
        Proveedor = request.getParameter("Proveedor");
    } catch (Exception e) {

    }
    if(Proveedor==null){
        Proveedor="";
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
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>           

            <div>
                <h3>Reporte por Fecha Proveedor</h3>
                <div class="row">
                    <form action="ReporteF.jsp" method="post">
                        <h4 class="col-sm-1">Proveedor</h4>
                        <div class="col-sm-5">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="this.form.submit();">
                                <option value="">--Proveedor--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("SELECT F_ClaProve,F_NomPro FROM tb_proveedor ORDER BY F_NomPro ASC");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>"><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>

                            </select>
                        </div>
                        <h4 class="col-sm-2">Fecha de Recibo</h4>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha"  onchange="this.form.submit();" />
                        </div>
                        <a class="btn btn-primary" href="ReporteF.jsp">Todo</a>
                        <a class="btn btn-primary" href="ReporteF_gnr.jsp?provee=<%=Proveedor%>&fecha=<%=fechaCap%>">Descargar&nbsp;<label class="glyphicon glyphicon-download-alt"></label></a>
                    </form>
                </div>
            </div>
        </div>
        <br />
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>                                
                                <td class="text-center">Proveedor</td>
                                <td class="text-center">Clave</td>
                                <td class="text-center">Lote</td>
                                <td class="text-center">Piezas</td>
                                <td class="text-center">Fecha</td>
                                <td class="text-center">OC</td> 
                                <td class="text-center">No. Ingreso</td> 
                                <td class="text-center">Remisión</td> 
                                <td class="text-center">Usuario</td> 
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                    if ((Proveedor.equals("")) && (fechaCap.equals(""))){
                                        
                                    rset = con.consulta("SELECT p.F_NomPro,c.F_ClaPro,l.F_ClaLot,SUM(F_CanCom),DATE_FORMAT(F_FecApl,'%d/%m/%Y'),c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User FROM tb_compra c INNER JOIN tb_proveedor p on c.F_ClaOrg=p.F_ClaProve INNER JOIN (SELECT F_FolLot,F_ClaLot FROM tb_lote GROUP BY F_FolLot) l on c.F_Lote=l.F_FolLot GROUP BY p.F_NomPro,c.F_ClaPro,l.F_ClaLot,c.F_FecApl,c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User ");
                                    }else if (!(Proveedor.equals("")) && (fechaCap.equals(""))){
                                        
                                    rset = con.consulta("SELECT p.F_NomPro,c.F_ClaPro,l.F_ClaLot,SUM(F_CanCom),DATE_FORMAT(F_FecApl,'%d/%m/%Y'),c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User FROM tb_compra c INNER JOIN tb_proveedor p on c.F_ClaOrg=p.F_ClaProve INNER JOIN (SELECT F_FolLot,F_ClaLot FROM tb_lote GROUP BY F_FolLot) l on c.F_Lote=l.F_FolLot where p.F_ClaProve='"+Proveedor+"' GROUP BY p.F_NomPro,c.F_ClaPro,l.F_ClaLot,c.F_FecApl,c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User");                                        
                                    }else{                     
                                        
                                    rset = con.consulta("SELECT p.F_NomPro,c.F_ClaPro,l.F_ClaLot,SUM(F_CanCom),DATE_FORMAT(F_FecApl,'%d/%m/%Y'),c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User FROM tb_compra c INNER JOIN tb_proveedor p on c.F_ClaOrg=p.F_ClaProve INNER JOIN (SELECT F_FolLot,F_ClaLot FROM tb_lote GROUP BY F_FolLot) l on c.F_Lote=l.F_FolLot where c.F_FecApl='"+fechaCap+"' GROUP BY p.F_NomPro,c.F_ClaPro,l.F_ClaLot,c.F_FecApl,c.F_OrdCom,c.F_ClaDoc,c.F_FolRemi,c.F_User ");
                                    }
                                    while (rset.next()) {
                                       
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td class="text-center"><%=rset.getString(2)%></td>
                                <td class="text-center"><%=rset.getString(3)%></td>
                                <td class="text-center"><%=formatter.format(rset.getInt(4))%></td>
                                <td class="text-center"><%=rset.getString(5)%></td>
                                <td class="text-center"><%=rset.getString(6)%></td>
                                <td class="text-center"><%=rset.getString(7)%></td>
                                <td class="text-center"><%=rset.getString(8)%></td>
                                <td class="text-center"><%=rset.getString(9)%></td>
                            </tr>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            %>
                        </tbody>
                    </table>
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
        $("#Fecha").datepicker();
        $("#Fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
    });
</script>