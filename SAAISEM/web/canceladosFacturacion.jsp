<%-- 
    Document   : canceladosFacturacion
    Created on : 12/11/2021, 01:05:00 PM
    Author     : LUIS ANGEL
--%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSetMetaData"%>
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
    String usua = "",Clave="",Descripcion="", tipou = "", username="", Proyectos="";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        username= (String) sesion.getAttribute("Usuario");
        tipou = (String) sesion.getAttribute("Tipo");
        
    } else {
        response.sendRedirect("index.jsp");
    }
    
    Clave = request.getParameter("Clave");
    Descripcion = request.getParameter("Descripcion");
    Proyectos = request.getParameter("Proyectos");
    if(Clave == null){
        Clave = "";
    }
    if(Descripcion == null){
        Descripcion = "";
    }
    if(Proyectos == null){
        Proyectos = "";
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
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.11.3/date-1.1.1/datatables.min.css"/>
       
        <title>SIALSS_CTRL CANCELADOS</title>
    </head>
    <body>
            <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <div class="panel-heading">
                     <%     
                             String     folio = request.getParameter("F_ClaDoc");
                                String   idproy = request.getParameter("idProyecto");    
                                String   ubi = request.getParameter("ubi");  
                                %>
                    <h3 class="panel-title">Claves canceladas folio:<% out.print(folio);%></h3>
                </div>
            <div class="panel panel-primary">
                
               
                    <div class="panel-body table-responsive">
                      
                        <table id="tablax"  class="table table-bordered" style="width:100%" >
                            <thead>
                                <tr class="table-light">
                                    <th>Clave</th>
                                    <th>Lote</th>
                                    <th>Descripción</th> 
                                    <th>Caducidad</th>  
                                    <th>Ubicación</th>
                                    <th>Devuelto</th>
                                    <th>Observación</th>
                                </tr>
                            </thead>
                            <tbody>
                                
                                   
                                <%
                                   con.conectar();
                                    try{
                                 
                              if(ubi.equals("CONTROLADO") || ubi.equals("APE") || ubi.equals("REDFRIA")){
                                ResultSet lista = con.consulta("SELECT F.F_ClaCli, F.F_Proyecto, SUBSTRING(M.F_DesPro,1,100) AS descrip,LTRIM(RTRIM(F.F_ClaPro)), LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%Y/%m/%d') AS F_FecCad, f.F_CantSur, F.F_CantReq,F.F_Ubicacion,COUNT(F.F_ClaCli) AS t,F.F_Obs,f.F_User FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F_ClaDoc ='"+folio+"' AND F_StsFact = 'C' AND F.F_Proyecto = '"+idproy+"' AND F_Ubicacion = '"+ubi+"' GROUP BY L.F_FolLot,F.F_IdFact ORDER BY F.F_Ubicacion");
                                                while (lista.next()) {
                                                    String tipo = lista.getString(9);
                                                    
                                                %>
                                                
                                       <%if (tipo.equals("CONTROLADO")) {%>
                                         <tr class="table-success">
                                         <%}else if (tipo.equals("REDFRIA")) {%>
                                         <tr class="table-info">
                                         <%}else if (tipo.equals("APE")) {%>
                                         <tr class="table-warning">
                                         <%}else{%>
                                         <tr>
                                       <%}%>
                                    <td><%=lista.getString(4)%></td>
                                    <td><%=lista.getString(5)%></td>
                                    <td><%=lista.getString(3)%></td>
                                    <td><%=lista.getString(6)%></td> 
                                    <td><%=lista.getString(9)%></td>
                                    <td><%=lista.getString(7)%></td>
                                    <td><%=lista.getString(11)%></td>  
                                </tr>
                                <%
                                        }}
else{
ResultSet lista = con.consulta("SELECT F.F_ClaCli, F.F_Proyecto, SUBSTRING(M.F_DesPro,1,100) AS descrip,LTRIM(RTRIM(F.F_ClaPro)), LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%Y/%m/%d') AS F_FecCad, f.F_CantSur, F.F_CantReq,F.F_Ubicacion,COUNT(F.F_ClaCli) AS t,F.F_Obs,f.F_User FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F_ClaDoc ='"+folio+"' AND F_StsFact = 'C' AND F.F_Proyecto = '"+idproy+"' AND F_Ubicacion NOT IN ('CONTROLADO', 'REDFRIA','APE') GROUP BY L.F_FolLot,F.F_IdFact ORDER BY F.F_Ubicacion");
                                                while (lista.next()) {
                                                    String tipo = lista.getString(9);
                                                %>
                                                
                                       <%if (tipo.equals("CONTROLADO")) {%>
                                         <tr class="table-success">
                                         <%}else if (tipo.equals("REDFRIA")) {%>
                                         <tr class="table-info">
                                         <%}else if (tipo.equals("APE")) {%>
                                         <tr class="table-warning">
                                         <%}else{%>
                                         <tr>
                                       <%}%>
                                    <td><%=lista.getString(4)%></td>
                                    <td><%=lista.getString(5)%></td>
                                    <td><%=lista.getString(3)%></td>
                                    <td><%=lista.getString(6)%></td> 
                                    <td><%=lista.getString(9)%></td>
                                    <td><%=lista.getString(7)%></td>
                                    <td><%=lista.getString(11)%></td> 
                                </tr>
                                <%
                                        }
}
                                        con.cierraConexion();
}catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }

                                %>
                                 </tbody>
                        </table> 
                                <!-- JQUERY -->
    <script src="https://code.jquery.com/jquery-3.4.1.js"
        integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous">
        </script>
    <!-- DATATABLES -->
  <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.11.3/date-1.1.1/datatables.min.js"></script>
    </script>
    <script>
        $(document).ready(function () {
            $('#tablax').DataTable({
                language: {
                    processing: "Tratamiento en curso...",
                    search: "Buscar&nbsp;:",
                    lengthMenu: "_MENU_ Registros por pagina",
                    info: "Mostrando _START_ a _END_ de _TOTAL_ registros",
                    infoEmpty: "No existen datos.",
                    infoFiltered: "(filtrado de _MAX_ elementos en total)",
                    infoPostFix: "",
                    loadingRecords: "Cargando...",
                    zeroRecords: "No se encontraron datos con tu busqueda",
                    emptyTable: "No hay datos disponibles en la tabla.",
                    paginate: {
                        first: "Primero",
                        previous: "← Anterior",
                        next: "Siguiente →",
                        last: "Ultimo"
                    },
                    aria: {
                        sortAscending: ": active para ordenar la columna en orden ascendente",
                        sortDescending: ": active para ordenar la columna en orden descendente"
                    }
                },
                
                                lengthMenu: [ [10, 25, -1], [10, 25, "All"] ],
            });
        });
    </script>
                             
                                
                   
                    </div>         
                </div>
                
                
         
            </div>
        
         <%@include file="jspf/piePagina.jspf" %>
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
    </body>
</html>
