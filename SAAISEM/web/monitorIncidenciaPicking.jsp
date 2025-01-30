
<%@page import="conn.ConectionDB"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String fechaCap = "", fechaa = "" ;
    try {
        fechaCap = df2.format(df3.parse(request.getParameter("Fecha")));
        fechaa = request.getParameter("FechaEnt");
        System.out.println("fecha de entrega: " + fechaCap );
        System.out.println("fecha de entrega2: " + fechaa );
    } catch (Exception e) {

    }
    if (fechaCap == null) {
        fechaCap = "";
    }
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" >
        
        
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>           
            <%@include file="jspf/menuPrincipal.jspf" %>
            <div>
                <h3>Reporte  de incidencias en picking</h3>
               
            </div>
       
                    <div class="container-fluid">
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras">
                        <thead>
                            <tr>                                
                                <td class="text-center">Fecha de entrega</td>
                                <td class="text-center">Folio</td>
                                <td class="text-center">Unidad</td>
                                <td class="text-center">Clave</td>
                                <td class="text-center">Lote</td>
                                <td class="text-center">Cantidad</td>
                                <td class="text-center">Estatus</td>
                                <td class="text-center">Ubicación</td> 
                                
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = null;
                                   
                                    if ((fechaCap.equals(""))) {

                                        rset = con.consulta("SELECT F.F_IdFact, F.F_ClaDoc, F.F_ClaCli, F.F_StsFact, F.F_FecApl, F.F_ClaPro, L.F_ClaLot, F.F_CantReq, F.F_CantSur, F.F_Lote, F.F_FecEnt,  F.F_Hora, F.F_User, F.F_Ubicacion, A.F_Id, A.F_IdLote, A.F_Cant, A.F_Proyecto, A.F_Status, A.F_ClaDoc FROM tb_factura AS F INNER JOIN tb_lote AS L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica LEFT JOIN tb_apartado AS A ON A.F_IdLote = L.F_IdLote AND F.F_ClaDoc = A.F_ClaDoc WHERE F.F_CantSur > 0 AND F.F_Ubicacion IN ( 'AF', 'AF1N' ) AND F_Id IS NULL GROUP BY F.F_IdFact");
                                    
                                    } else {
                                       
                                        rset = con.consulta("SELECT F.F_IdFact, F.F_ClaDoc, F.F_ClaCli, F.F_StsFact, F.F_FecApl, F.F_ClaPro, L.F_ClaLot, F.F_CantReq, F.F_CantSur, F.F_Lote, F.F_FecEnt,  F.F_Hora, F.F_User, F.F_Ubicacion, A.F_Id, A.F_IdLote, A.F_Cant, A.F_Proyecto, A.F_Status, A.F_ClaDoc FROM tb_factura AS F INNER JOIN tb_lote AS L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica LEFT JOIN tb_apartado AS A ON A.F_IdLote = L.F_IdLote AND F.F_ClaDoc = A.F_ClaDoc WHERE F.F_CantSur > 0 AND F.F_Ubicacion IN ( 'AF', 'AF1N' ) AND F_Id IS NULL AND F.F_FecEnt = '" + fechaCap + "' GROUP BY F.F_IdFact");
                                    }
                                    
                                        while (rset.next()) {

                            %>
                            <tr>
                                <td><%=rset.getString("F_FecEnt")%></td>
                                <td><%=rset.getString("F_ClaDoc")%></td>
                                <td><%=rset.getString("F_ClaCli")%></td>
                                <td class="text-center"><%=rset.getString("F_ClaPro")%></td>
                                <td class="text-center"><%=rset.getString("F_ClaLot")%></td>
                                <td class="text-center"><%=rset.getString("F_CantSur")%></td>
                                <td class="text-center"><%=rset.getString("F_StsFact")%></td>
                                <td class="text-center"><%=rset.getString("F_Ubicacion")%></td>                                
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
                         </div>
        <br />
        
        <br><br><br>
    </body>
</html>
<%@include file="jspf/piePagina.jspf" %>

<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script>
                                $(document).ready(function () {
                                    $('#datosCompras').dataTable();
                                    
                                });
</script>