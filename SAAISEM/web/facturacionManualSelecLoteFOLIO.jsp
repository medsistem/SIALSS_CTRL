<%--   
        Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
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

    String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "", Cantidad = "", DesProyecto="", Proyecto="";

    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
        Cantidad = (String) request.getAttribute("Cantidad");
        DesProyecto = (String) request.getAttribute("DesProyecto");
        Proyecto = (String) request.getAttribute("Proyecto");
    } catch (Exception e) {

    }
    if (ClaCli == null) {
        ClaCli = "";
    }
    if (FechaEnt == null) {
        FechaEnt = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
    }
    if (DesPro == null) {
        DesPro = "";
    }
    if (Cantidad == null) {
        Cantidad = "";
    }

    if(DesProyecto==null){
        DesProyecto="";
    }

    if(Proyecto==null){
        Proyecto="";
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
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="jspf/menuPrincipal.jspf" %>
            <div class="row">
                <div class="col-sm-12">
                    <h2>Agregar Lote a Modificar folio</h2>
                </div>
            </div>
            <hr/>
            <form action="FacturacionManual" method="post">
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Unidad:</h4>
                    </div>
                    <div class="col-sm-4">
                        <input value="<%=ClaCli%>"  class="form-control" name="ClaCli" id="ClaCli" readonly="" />
                        <!--select class="form-control" name="ClaCli" id="ClaCli">
                            <option value="">-Seleccione Unidad-</option>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn");
                                while (rset.next()) {
                        %>
                        <option value="<%=rset.getString(1)%>"
                        <%
                            if (rset.getString(1).equals(ClaCli)) {
                                out.println("selected");
                            }
                        %>
                        ><%=rset.getString(2)%></option>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>
                    </select-->
                    </div>
                    <div class="col-sm-2">
                        <h4>Fecha de Entrega</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" name="FechaEnt" id="FechaEnt" min="<%=df2.format(new Date())%>" value="<%=FechaEnt%>"/>
                    </div>
                    <div class="col-sm-1">
                        <h4>Proyecto</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="text" readonly="" class="form-control" name="DesProyecto" id="DesProyecto" value="<%=DesProyecto%>"/>
                        <input type="hidden" readonly="" class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">

                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-1">
                                <h4>CLAVE:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" readonly="" id="ClaPro" name="ClaPro" value="<%=ClaPro%>"/>
                            </div>
                            <div class="col-sm-2">
                                <h4>Descripción:</h4>
                            </div>
                            <div class="col-sm-7">
                                <textarea class="form-control" readonly=""><%=DesPro%></textarea>
                            </div>
                        </div>
                        <br/>

                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Cantidad a Facturar:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="Cantidad" id="Cantidad" value="<%=Cantidad%>"/>
                            </div>
                            <div class="col-sm-2 col-sm-offset-6">
                                <a class="btn btn-block btn-default" href="facturacionManualFolio.jsp">Regresar</a>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
            <table class="table table-condensed table-striped table-bordered table-responsive">
                <%  try {
                        con.conectar();
                         String Ubicaciones = "", UbicaNofacturar = "", id = "";
                        String qryUbicaDesc = "SELECT UF.F_idUbicaFac, UF.F_UbicaSQL2 FROM tb_parametrousuario AS PU INNER JOIN tb_proyectos AS P ON PU.F_Proyecto = P.F_Id INNER JOIN tb_ubicafact AS UF ON PU.F_Id = UF.F_idUbicaFac WHERE PU.F_Usuario = '" + usua + "' ";
                                                   ResultSet rsetR2 = con.consulta(qryUbicaDesc);
                                                    while (rsetR2.next()) {
                                                        id = rsetR2.getString(1);
                                                        Ubicaciones = rsetR2.getString(2);
                                                    } %>
                <tr>
                    <td>CLAVE:</td>
                   
                    <td>Lote</td>
                    <td>Caducidad</td>
                    <td>Ubicación</td>
                    <% if (id.equals("11")) { %>
                    <th class="text-center">Disponible para unidad</th>
                    <th class="text-center">Apartado en Remision</th>
                    <th class="text-center">Cantidad en ubicación remisionable</th>
                     <th class="text-center">Cantidad Permitida para unidad</th>
                    <% }else { %>
                     <th class="text-center">Disponible</th>
                    <th class="text-center">Apartado en Remision</th>
                     <% } %>
                    <th class="text-center">Marca</th>
                    <th class="text-center">Origen</th>
                    <th class="text-center">Proyecto</th>
                    <th class="text-center">Seleccionar</th>
                </tr>
                <%
                    
                                                    
                        String filtroOrigen="",filtroRural = "",tipoUnidad = "", query ="";;
                        int nivel = 0, Id_Uni = 0;
                        
                        ResultSet rset = con.consulta("Select F_Nivel, F_Tipo from tb_uniatn where F_ClaCli = '"+ ClaCli+"'");
                        while(rset.next()){
                            nivel = rset.getInt(1);
                            tipoUnidad = rset.getString(2);
                        }
                        
                         ResultSet rsetN3;
                            if (id.equals("11")) {
                                rsetN3 = con.consulta("Select * from tb_unidgastoscata ugc where ugc.ClaCli = '" + ClaCli + "' and ugc.Sts_Cliente = '1' ");

                                while (rsetN3.next()) {
                                    Id_Uni = rsetN3.getInt(2);
                                }

                            }
                        if(Proyecto.equals("2")){
                            
                     
                          switch (id) {
                                    case "6":
                                        if (nivel == 1) {
                                            if (tipoUnidad.equals("1")) {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
                                            } else {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                            }
                                        //query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                           query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                        } else {
                                            out.println("<script>alert('Validar Parametro de Facturacion para 1er nivel')</script>");
                                        }
                                    break;
                                    case "7":
                                                
                                        if (nivel == 1) {
                                            filtroRural = " AND l.F_FecCad >= CURDATE() ";
                                          //  query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                         query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                   
                                        } else {
                                            out.println("<script>alert('Validar Parametro de Facturacion para 1er nivel')</script>");
                                        }
                                        break;
                                    case "8":
                                        if (nivel == 1) {
                                            
                                               if (tipoUnidad.equals("1")) {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
                                            } else {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                            }
                                        //    query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                         query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                   
                                        } else {
                                             filtroOrigen = "AND l.F_Origen != 8 ";
                                              filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                        //     query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                    query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                   
                                        }
                                        
                                        break;
                                    case "9":
                                                
                                        if (!tipoUnidad.equals("1")) {
                                            filtroOrigen = "AND l.F_Origen != 8 ";
                                            filtroRural = " AND l.F_FecCad >= CURDATE() ";
                                          //  query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                        query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                   
                                        } else {
                                            out.println("<script>alert('Validar Parametro de Facturacion para 2 y 3 nivel')</script>");
                                        }
                                    break;
                                    case "11":
                                      if (nivel == 1) {
                                            if (tipoUnidad.equals("1")) {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ";
                                            } else {
                                                filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                            }
                                             filtroOrigen = " AND l.F_Origen = 19 ";
                                      // query = "";
                                         query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy,CASE WHEN IFNULL(fol.F_Cant,0) > 0 THEN  (gc.Cant_Asig - IFNULL(fol.F_Cant,0)) ELSE (gc.Cant_Total - IFNULL(fol.F_Cant,0)) END as disponiblegc,gc.Cant_Total FROM v_existencias AS l INNER JOIN tb_gasto_catastrofico AS gc ON l.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND l.F_FolLot = gc.Id_Lote LEFT JOIN  (SELECT IFNULL(a.F_Cant,0) as F_Cant, f.F_Lote,f.F_ClaCli,f.F_ClaDoc FROM tb_apartado AS a INNER JOIN tb_factura AS f ON f.F_ClaDoc = a.F_ClaDoc WHERE  a.F_Status = 1 GROUP BY a.F_Id) fol On fol.F_Lote = l.F_IdLote and fol.F_ClaCli ="+Id_Uni+"  " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + "AND gc.No_ClaCli = "+Id_Uni+"  HAVING disponiblegc > 0 ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                               
                                        } else {
                                            filtroOrigen = " AND l.F_Origen = 19 ";
                                            filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                           // query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy,(gc.Cant_Total - l.apartado) as disponiblegc,gc.Cant_Total FROM v_existencias AS l INNER JOIN tb_gasto_catastrofico AS gc ON l.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND l.F_FolLot = gc.Id_Lote " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + "AND gc.No_ClaCli = "+Id_Uni+"  HAVING disponiblegc > 0 ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                                       query = "SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy,CASE WHEN IFNULL(fol.F_Cant,0) > 0 THEN  (gc.Cant_Asig - IFNULL(fol.F_Cant,0)) ELSE (gc.Cant_Total - IFNULL(fol.F_Cant,0)) END as disponiblegc,gc.Cant_Total FROM v_existencias AS l INNER JOIN tb_gasto_catastrofico AS gc ON l.F_ClaPro = gc.ClaPro COLLATE utf8_unicode_ci AND l.F_FolLot = gc.Id_Lote LEFT JOIN  (SELECT IFNULL(a.F_Cant,0) as F_Cant, f.F_Lote,f.F_ClaCli,f.F_ClaDoc FROM tb_apartado AS a INNER JOIN tb_factura AS f ON f.F_ClaDoc = a.F_ClaDoc WHERE  a.F_Status = 1 GROUP BY a.F_Id) fol On fol.F_Lote = l.F_IdLote and fol.F_ClaCli ="+Id_Uni+"  " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + "AND gc.No_ClaCli = "+Id_Uni+"  HAVING disponiblegc > 0 ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;";
                               
                                      }
                                      System.out.println("query de Gastos: " +  query);
                                    break;
                                     case "34":
                                     case "35":
                                    case "36":
                                    case "37":
                                    case "38": 
                                    filtroRural = " AND l.F_FecCad < CURDATE()";
                                     rset = con.consulta("SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;");
                        
                               break;
                               case "12":
                            System.out.println("MODIFICAR FOLIO de SSM: " );

                                rset = con.consulta("SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;");
                        
                               break;
                               
                               case "39":
                               rset = con.consulta("SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;");
                        
                               break;
                                    default:
                                       
                                        if (!tipoUnidad.equals("1")) {
                                            filtroOrigen = "AND l.F_Origen != 8 ";
                                            filtroRural = " AND l.F_FecCad >= DATE_ADD(CURDATE(),INTERVAL 7 DAY) ";
                                        //   query = "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;";
                                  rset = con.consulta("SELECT l.F_ClaPro,l.F_ClaLot,DATE_FORMAT( l.F_FecCad, '%d/%m/%Y' ),l.F_Ubica,l.disponible,l.F_IdLote,l.F_FolLot,l.F_DesMar,l.F_DesOri,l.F_DesProy FROM v_existencias AS l " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "'  AND l.disponible > 0 AND l.F_Proyecto = '" + Proyecto + "' " + filtroOrigen + filtroRural + " ORDER BY l.F_FecCad ASC,l.F_ClaLot ASC, l.F_DesOri ASC;");
                                   
                                                } else {
                                            out.println("<script>alert('Validar Parametro de Facturacion para 2 y 3 nivel')</script>");
                                        }
                            }
                             
                          }else{ 
                                
                          switch (id) {
                                    case "39":
                            rset = con.consulta( "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy,d.F_DesOri FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;");
                         
                           break;
                                    default:
                            filtroOrigen = "AND l.F_Origen != 8 ";
                            filtroRural = " AND l.F_FecCad >=  DATE_ADD(CURDATE(),INTERVAL 7 DAY)  ";
                           rset = con.consulta( "SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), l.F_Ubica, (d.disponible) as F_ExiLot, l.F_IdLote, l.F_FolLot, m.F_DesMar, l.F_Origen, p.F_DesProy,d.F_DesOri FROM tb_lote l LEFT JOIN v_existencias d ON l.F_IdLote = d.F_IdLote INNER JOIN tb_marca m ON l.F_ClaMar = m.F_ClaMar INNER JOIN tb_medica me ON me.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id " + Ubicaciones + " AND l.F_ClaPro = '" + ClaPro + "' AND l.F_ExiLot != 0 AND l.F_Proyecto = '" + Proyecto + "' "+ filtroOrigen + filtroRural + " ORDER BY l.F_Origen, l.F_FecCad ASC;");
                                        
                        }
                   
                    }
                    
                   System.out.println("query : "+query );
                          
                        while (rset.next()) {
                            
                            int cant = 0, cantTemp = 0;
                             int cantLot = 0;
                             if (rset.getRow() == 0) {
                                cantLot = 0;
                            } else {
                                cantLot = rset.getInt(5);
                            }
                            System.out.println("cantLot : " + cantLot + " " + cantTemp);

                           // int cantLot = rset.getInt(5);
                            ResultSet rset2 = con.consulta("select SUM(F_Cant) from tb_facttemp where F_IdLot = '" + rset.getString("F_IdLote") + "' and (F_StsFact = '0' or F_StsFact = '3')  ");
                            while (rset2.next()) {
                                if (rset2.getRow() == 0) {
                                    cantTemp = 0;
                                } else {
                                    cantTemp = rset2.getInt(1);
                                }
                            }
                          
               
                  
                        
                       

                            cant = cantLot - cantTemp;
                %>
                <tr>
                      <td><%=rset.getString(1)%></td>

                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getString(3)%></td>
                    <td><%=rset.getString(4)%></td>
                    <% if (id.equals("11")) { %>
                    <td class="text-center"><%=rset.getString(11)%></td>
                    <td class="text-center"><%=cantTemp%></td>
                     <td class="text-center"><%=rset.getString(5)%></td>
                    <td class="text-center"><%=rset.getString(12)%></td>
                    <td><%=rset.getString("F_DesMar")%></td>
                    <td style="align-content: center"><%=rset.getString("F_DesOri")%></td>
                    <td><%=rset.getString(10)%></td>
                    <% }else { %>
                    
                    <td class="text-center"><%=cant%></td>
                    <td class="text-center"><%=cantTemp%></td>
                    <td><%=rset.getString("F_DesMar")%></td>
                    <td><%=rset.getString("F_DesOri")%></td>
                    <td><%=rset.getString(10)%></td>
                    <% }%>
                    <td>
                        <form action="FacturacionManual" method="post">
                            <input name="FolLot" value="<%=rset.getString(7)%>" class="hidden" readonly=""/>
                            <input name="IdLot" value="<%=rset.getString(6)%>" class="hidden" readonly=""/>
                            <input class="hidden" name="Cant" id="Cant<%=rset.getString(6)%>" value=""/>
                             <% if (id.equals("11")) {%>
                            <input class="hidden" name="Cant2" id="Cant2<%=rset.getString(11)%>" value=""/>
                            <% } %>
                            <input class="hidden" name="CantAlm_<%=rset.getString(6)%>" id="CantAlm_<%=rset.getString(6)%>" value="<%=cant%>"/>
                            <button name="accion" type="submit" value="AgregarClaveFOLIO" id="BtnAgregar_<%=rset.getString(6)%>" class="btn btn-block btn-success" onclick="return validaCantidad(<%=rset.getString(6)%>);"><span class="glyphicon glyphicon-ok"></span></button>
                            <!--button name="accion" value="AgregarClave" id="BtnAgregar_<%=rset.getString(6)%>" class="btn btn-block btn-success" onclick="return validaCantidad(this.id);"><span class="glyphicon glyphicon-ok"></span></button-->
                        </form>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                %>
            </table>

        </div>
        <%@include file="../jspf/piePagina.jspf" %>
    </body>
    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script src="js/bootstrap-datepicker.js"></script>
    <script src="js/jquery.alphanum.js" type="text/javascript"></script>
    <script src="js/facturajs/FacturacionjsFolio.js"></script>
    <script src="js/sweetalert.min.js" type="text/javascript"></script>    
</html>

