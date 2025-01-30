<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="com.gnk.impl.FacturacionTranDaoImpl"%>
<%@page import="java.sql.Array"%>
<%@page import="com.gnk.dao.FacturaProvisionalDAO"%>
<%@page import="com.gnk.dao.FacturaProvisionalDAO"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatter2 = new DecimalFormat("000");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "", Desproyecto = "";
    String tipo = "", F_Ruta = "", F_FecEnt = "", Unidad1 = "", Unidad2 = "", Catalogo = "";
    int Proyecto = 0, UbicaModu = 0;

    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        Clave = (String) session.getAttribute("clave");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    if (Clave == null) {
        Clave = "";
    }
    ConectionDB con = new ConectionDB();
    String UsuaJuris = "";
    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_Juris from tb_usuario where F_Usu = '" + usua + "'");
        while (rset.next()) {
            UsuaJuris = rset.getString("F_Juris");
        }

        ResultSet UbiMod = con.consulta("SELECT PU.F_Id,P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE F_Usuario = '" + usua + "';");
        if (UbiMod.next()) {
            UbicaModu = UbiMod.getInt(1);
            Proyecto = UbiMod.getInt(2);
            Desproyecto = UbiMod.getString(3);
        }

        con.cierraConexion();
    } catch (Exception e) {

    }
    String where = " and (";
    String[] temp;
    temp = UsuaJuris.split(",");
    for (int i = 0; i < temp.length; i++) {
        where += "f.F_Ruta like 'R" + temp[i] + "%'";
        if (i != temp.length - 1) {
            where += " or ";
        }
    }
    where += ")";

    try {
        Unidad1 = request.getParameter("Unidad1");
        Unidad2 = request.getParameter("Unidad2");
        Catalogo = request.getParameter("Catalogo");
        if (Catalogo.equals("Seleccione")) {
            Catalogo = "";
        }

    } catch (Exception e) {
    }
    System.out.println("Unidad1" + Unidad1 + " Unidad2:" + Unidad2 + " Cata:" + Catalogo);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div class="container">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Facturación aútomatica Proyecto : <%=Desproyecto%>*</h3>
                </div>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="facturaAtomatica.jsp">
                        <label class="control-label col-sm-2" for="fecha_ini">Clave Unidad:</label>
                        <div class="col-sm-2">
                            <input name="Unidad1" id="Unidad1" type="text" class="form-control" placeholder="Unidad" value="" />                        
                        </div>
                        <div class="col-sm-2">
                            <input name="Unidad2" id="Unidad2" type="text" class="form-control" value="" placeholder="Unidad" />
                        </div>
                        <h4 class="col-sm-1">Catálogo:</h4>
                        <div class="col-sm-2">
                             <select name="Catalogo" id="Catalogo" class="form-control" required>
                                <option value="">Seleccione</option>
                                <%                                
                                    try {
                                        con.conectar();
                                        ResultSet rset = null;
                                        rset = con.consulta("SELECT tu.F_NivelUni,CONCAT(tu.F_NomeUnidad,' - ',tu.F_NomUni) as NombreUnidad FROM tb_tipunidad AS tu ORDER BY tu.F_NomUni ASC;");

                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("facturaAtomatica.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception ex) {
                                            Logger.getLogger("facturaAtomatica.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="form-group">
                            <!--label for="FecEnt" class="col-sm-2 control-label">Fecha de Entrega</label>
                            <div class="col-sm-2">
                                <input type="date" class="form-control" id="FecEnt" name="F_FecEnt" />
                            </div-->
                            <div class="col-lg-2">
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="consultar" onclick="return valida_clave();" > Consultar</button>
                            </div>
                        </div>
                    </form>
                    <%

                    %>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
                <div class="panel-footer">
                    <form action="Facturacion" method="post" onsubmit="muestraImagen()">
                        <%                            int banReq1 = 0, banReq = 0;
                            try {
                                con.conectar();
                                String F_NomCli = "", F_Fecha = "", UbicaDesc2 = "";
                                banReq = 0;
                                int F_PiezasReq = 0, TotalSur = 0, UbicaModu2 = 0;
                                if (!(Catalogo == "")) {

                                    ResultSet rset = con.consulta("select  F_ClaCli AS F_ClaUni, F_NomCli from tb_uniatn where F_ClaCli between  '" + Unidad1 + "' and '" + Unidad2 + "'  group by F_ClaCli;");
                                    while (rset.next()) {
                                        F_NomCli = rset.getString("F_NomCli");
                                        ResultSet rset3 = con.consulta("select F_ClaUni, sum(F_Solicitado) as F_PiezasReq,DATE_FORMAT(F_Fecha,'%d/%m/%Y') AS F_Fecha from tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro=M.F_ClaPro AND M.F_N" + Catalogo + "='1' where F_Status = '0' and F_ClaUni = '" + rset.getString(1) + "' group by F_ClaUni;");
                                        while (rset3.next()) {
                                            banReq = 1;
                                            banReq1 = 1;
                                            F_PiezasReq = (rset3.getInt("F_PiezasReq"));
                                            F_Fecha = rset3.getString("F_Fecha");
                                            if (F_PiezasReq == 0) {
                                                banReq1 = 0;
                                            }
                                        }
                                        if (F_PiezasReq != 0) {
                        %>
                        <div class="panel panel-default">
                            <div class="panel-heading">

                                <%
                                    if (banReq == 1) {
                                %>
                                <input type="checkbox" name="chkUniFact" id="chkUniFact" value="<%=rset.getString("F_ClaUni")%>" checked="">
                                <%
                                    }
                                %>
                                <a data-toggle="collapse" data-parent="#accordion" href="#111<%=rset.getString(1)%>" style="color:black" aria-expanded="true" aria-controls="collapseOne"><%=rset.getString(1)%> |  <%=F_NomCli%> | Fecha Req. <%=F_Fecha%> </a>


                                <%
                                    if (banReq == 1) {
                                %>
                                <input name="F_ClaUni" value="<%=rset.getString(1)%>" class="hidden" />
                                <!--input name="F_FecEnt" value="<%//=rset.getString("F_Fecha")%>" class="hidden" -->
                                <button class="btn btn-sm btn-warning" name="eliminar" value="<%=rset.getString(1)%>" onclick="eliminarReq(this)" type="button"><span class="glyphicon glyphicon-remove"></span></button>
                                    <%
                                        }
                                    %>
                            </div>
                            <div id="<%=rset.getString(1)%>" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                                <div class="panel-body">
                                    <div class="row">

                                        <!--div class="col-sm-2">
                                        <%
                                            if (banReq == 1) {
                                        %>
                                        <input name="pagina" class="hidden" value="factura.jsp">
                                        <input name="F_ClaUni" value="<%//=rset.getString(1)%>" class="hidden" />
                                        <input name="F_FecEnt" value="<%//=rset.getString("F_Fecha")%>" class="hidden" />
                                        <a class="btn btn-block btn-sm btn-primary" href="detRequerimiento.jsp?F_ClaUni=<%//=rset.getString(1)%>&F_Ruta=<%//=F_Ruta%>&F_Mes=<%//=request.getParameter("F_Mes")%>&pagina=factura.jsp" ><span class="glyphicon glyphicon-search"></span></a>
                                        <%
                                            }
                                        %>
                                    </div-->
                                        <div class="col-sm-2">
                                        </div>

                                    </div>
                                    <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed" id="datosProv">
                                        <tr>
                                            <td>Clave</td>
                                            <td>Descripción</td>
                                            <td>Piezas Sol</td>
                                            <td>Piezas Sur</td>
                                            <td>Existencia</td>
                                        </tr>
                                        <%
                                            try {
                                                String caducidad="";
                                                if(Proyecto == 9 || Proyecto == 14 || Proyecto == 15 || Proyecto == 23 || Proyecto == 27){
                                                    caducidad = " AND L.F_FecCad > CURDATE() ";
                                                }else{
                                                    caducidad = " AND L.F_FecCad > DATE_ADD(CURDATE(),INTERVAL 7 DAY)";
                                                }
                                                int ExiLot = 0, ExiSol = 0;
                                                String UbicaDesc = "",id = "";
                                                int Cata = Integer.parseInt(Catalogo);
                                                if (Cata > 0) {

                                                   String qryUbicaDesc = "SELECT UF.F_idUbicaFac, UF.F_UbicaSQL2 FROM tb_parametrousuario AS PU INNER JOIN tb_proyectos AS P ON PU.F_Proyecto = P.F_Id INNER JOIN tb_ubicafact AS UF ON PU.F_Id = UF.F_idUbicaFac WHERE PU.F_Usuario = '" + usua + "' ";
                                                   ResultSet rsetR2 = con.consulta(qryUbicaDesc);
                                                    while (rsetR2.next()) {
                                                         id = rsetR2.getString(1);
                                                        UbicaDesc = rsetR2.getString(2);
                                                    }
                                                
                                                }
                                                PreparedStatement ps = null;
                                                String qry = "";
                                                F_PiezasReq = 0;
                                               // String qry = "SELECT M.F_ClaPro,SUBSTR(M.F_DesPro,1,80) AS F_DesPro,REQ.F_CajasReq, REQ.F_Solicitado,REPLACE(M.F_ClaPro,'.',''),IFNULL(LOTE.F_ExiLot,0),IFNULL(MOVI.F_CantMov,0),IF(IFNULL(LOTE.F_ExiLot,0)>=IFNULL(MOVI.F_CantMov,0),IFNULL(MOVI.F_CantMov,0),IFNULL(LOTE.F_ExiLot,0)) AS EXITLOTE FROM tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT l.F_IdLote, l.F_ClaPro, SUM(l.F_ExiLot) AS F_ExiLot2, SUM(d.Disponible) as F_ExiLot FROM tb_lote l left join v_existencias d on l.F_IdLote = d.F_IdLote " + UbicaDesc + " AND l.F_Proyecto='" + Proyecto + "' GROUP BY F_ClaPro) AS LOTE ON REQ.F_ClaPro=LOTE.F_ClaPro LEFT JOIN (SELECT F_ProMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica " + UbicaDesc + " AND L.F_Proyecto='" + Proyecto + "' GROUP BY F_ProMov) AS MOVI ON REQ.F_ClaPro=MOVI.F_ProMov WHERE F_ClaUni='" + rset.getString("F_ClaUni") + "' and F_Status =0 and F_Solicitado != 0 AND F_N" + Catalogo + "='1' AND M.F_StsPro='A' group by F_IdReq order by M.F_ClaPro+0;";
                                              
                                               if( id.equals("13"))
                                               {
                                                  qry = "SELECT M.F_ClaPro, SUBSTR(M.F_DesPro, 1, 80) AS F_DesPro, REQ.F_CajasReq, REQ.F_Solicitado, REPLACE(M.F_ClaPro, '.', ''), IFNULL(LOTE.disponible, 0), IFNULL(LOTE.F_ExiLot, 0), IFNULL(LOTE.disponible, 0), IFNULL(LOTE.disponible, 0) AS EXITLOTE FROM tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro = M.F_ClaPro LEFT JOIN (SELECT l.*, SUM(l.F_ExiLot - IFNULL(a.apartado, 0)) AS disponible FROM tb_lote l INNER JOIN (SELECT * FROM tb_compra WHERE F_OrdCom LIKE '%U013' GROUP BY F_Lote) c ON l.F_FolLot = c.F_Lote LEFT JOIN (SELECT id_lote, SUM(cantSur) AS apartado FROM folio_provisional_detail fd WHERE fd.status = 1 GROUP BY id_lote) a ON a.id_lote = l.F_IdLote where l.F_Ubica IN ("+FacturaProvisionalDAO.ubicacionesNoU013+") group by l.F_ClaPro) LOTE ON REQ.F_ClaPro = LOTE.F_ClaPro WHERE F_ClaUni='" + rset.getString("F_ClaUni") + "' and F_Status =0 and F_Solicitado != 0 AND F_N" + Catalogo + "='1' AND M.F_StsPro='A' group by F_IdReq order by M.F_ClaPro+0;";
                                                   
                                               }else{
                                                   
                                                  qry = "SELECT M.F_ClaPro, SUBSTR(M.F_DesPro, 1, 80) AS F_DesPro, REQ.F_CajasReq, REQ.F_Solicitado, REPLACE(M.F_ClaPro, '.', ''), IFNULL(LOTE.F_ExiLot, 0) ,  IFNULL(MOVI.F_CantMov,0), IF(IFNULL(LOTE.F_ExiLot,0)>=IFNULL(MOVI.F_CantMov,0), IFNULL(MOVI.F_CantMov,0),IFNULL(LOTE.F_ExiLot,0)) AS EXITLOTE FROM tb_unireq REQ INNER JOIN tb_medica M ON REQ.F_ClaPro=M.F_ClaPro LEFT JOIN (SELECT l.F_IdLote, l.F_ClaPro, SUM(l.F_ExiLot) AS F_ExiLot2, SUM(d.Disponible) as F_ExiLot FROM tb_lote l left join v_existencias d on l.F_IdLote = d.F_IdLote " + UbicaDesc + " AND l.F_Proyecto='" + Proyecto + "' "+caducidad+ " GROUP BY F_ClaPro) AS LOTE ON REQ.F_ClaPro=LOTE.F_ClaPro LEFT JOIN (SELECT F_ProMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica " + UbicaDesc + " AND L.F_Proyecto='" + Proyecto + "' GROUP BY F_ProMov) AS MOVI ON REQ.F_ClaPro=MOVI.F_ProMov LEFT JOIN tb_medicaunidad MU on MU.F_ClaUni = REQ.F_ClaUni AND MU.F_ClaPro = REQ.F_ClaPro WHERE REQ.F_ClaUni='" + rset.getString("F_ClaUni") + "' and F_Status =0 and F_Solicitado != 0 AND F_N" + Catalogo + "='1' AND M.F_StsPro='A' group by F_IdReq order by M.F_ClaPro+0;";
                                                    }
                                               ps = con.getConn().prepareStatement(qry);
                                               System.out.println(ps);
                                               ResultSet rsetR1 = ps.executeQuery();
                                                
                                                while (rsetR1.next()) {
                                                    String claPro = rsetR1.getString("F_ClaPro");
                                                    if(!FacturacionTranDaoImpl.fkingNoRestrictionKeys().contains(claPro) && id.equals("13")){
                                                        continue;
                                                    }else if(id.equals("13")){
                                                        F_PiezasReq += rsetR1.getInt("F_Solicitado");
                                                    }
                                                    
                                                    ExiLot = rsetR1.getInt(8);
                                        %>
                                        <tr
                                            <%
                                                if (rsetR1.getInt(4) > ExiLot) {
                                                    out.println("class='danger'");
                                                    ExiSol = ExiLot;
                                                } else {
                                                    ExiSol = rsetR1.getInt(4);
                                                }
                                                if (ExiLot < 0) {
                                                    ExiLot = 0;
                                                    ExiSol = 0;
                                                }
                                                TotalSur = TotalSur + ExiSol;


                                            %>
                                            >
                                            <td><%=rsetR1.getString(1)%></td>
                                            <td><%=rsetR1.getString(2)%></td>
                                            <!--td><%//=rsetR1.getInt(4)%></td-->
                                            <td ><small><input name="CantidadReq_<%=rset.getString(1)%>_<%=rsetR1.getString(5).trim()%>" id="CantidadReq_<%=rset.getString(1)%>_<%=rsetR1.getString(5).trim()%>" type="number" min="0" class="text-right form-control" value="<%=rsetR1.getString(4)%>" data-behavior="only-num" /></small></td>
                                            <td ><small><input name="Cantidad_<%=rset.getString(1)%>_<%=rsetR1.getString(5).trim()%>" id="Cantidad_<%=rset.getString(1)%>_<%=rsetR1.getString(5).trim()%>" type="number" min="0" class="text-right form-control" value="<%=ExiSol%>" data-behavior="only-num" /></small></td>
                                            <td class="text-right"><%=formatter.format(ExiLot)%></td>
                                        </tr>                 
                                        <%
                                            }
                                        %>
                                        <%
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                out.println(e.getMessage());
                                            }
                                        %>

                                    </table> 
                                    <% if (Proyecto == 26) {%>
                                    <div class="row">
                                        <h4 class="col-sm-2">
                                            No. Procedimiento:
                                        </h4>
                                        <div class="col-sm-6">
                                            <input class="form-control" name="obs<%=rset.getString("F_ClaUni")%>" id="obs<%=rset.getString("F_ClaUni")%>" />
                                        </div>
                                    </div>
                                    <% } else {%>
                                    <div class="row">
                                        <h4 class="col-sm-2">
                                            Observaciones:
                                        </h4>
                                        <div class="col-sm-10">
                                            <textarea class="form-control" name="obs<%=rset.getString("F_ClaUni")%>" id="obs<%=rset.getString("F_ClaUni")%>" maxlength="50"></textarea>
                                        </div>
                                    </div>
                                    <% }%>

                                    <div class="row">


                                        <h4 class="col-sm-2">
                                            Piezas: <%=formatter.format(F_PiezasReq)%>
                                        </h4>

                                        <h4>Total de Piezas a Facturar: <%=formatter.format(TotalSur)%></h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%
                                            TotalSur = 0;
                                        }
                                    }
                                } else {
                                    out.println("<script>alert('Favor de Seleccionar Catálogo')</script>");
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                out.println(e.getMessage());
                            }
                        %>
                        <%
                            if (banReq1 == 1) {
                        %>
                        <div class="row">
                            <h4 class="col-sm-2">Fecha de Entrega:</h4>
                            <div class="col-sm-2">
                                <input type="date" name="F_FecEnt" id="FecEnt" class="form-control" value="" min="<%=df2.format(new Date())%>"  required onkeydown="return false" />
                            </div>
                            <h4 class="col-sm-1">Pedido</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="OC" id="OC" type="text" />
                            </div>
                            <h4 class="col-sm-1">Catálogo</h4>
                            <div class="col-sm-1">
                                <input type="text" name="Cata" id="Cata" class="form-control" readonly="" required="" value="<%=Catalogo%>" />
                            </div>
                            <h4 class="col-sm-1">Tipo</h4>
                            <div class="col-sm-2">
                               <select class="form-control" name="F_Tipo" id="F_Tipo">
                                     <%                                
                                    try {
                                        con.conectar();
                                        ResultSet rset = null;
                                        rset = con.consulta("SELECT * FROM tb_tiporequerimiento ORDER BY F_Id ASC;");

                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(2)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger("facturaAtomatica.jsp").log(Level.SEVERE, null, e);
                                    } finally {
                                        try {
                                            con.cierraConexion();
                                        } catch (Exception ex) {
                                            Logger.getLogger("facturaAtomatica.jsp").log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    %>
                                 
                                </select>
                            </div>
                        </div>
                        <br/>
                        <input type="hidden" readonly="" class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                        <input name="F_Juris" class="hidden" value="<%=UsuaJuris%>" />
                        <div class="row">
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-warning" type="submit" name="accion" value="cancelar" onclick="return validaRemision()">Cancelar Folio(s)</button> 
                            </div>
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-primary" type="button" name="accion" value="generarRemision" id="BtngenerarRemision">Generar Folio(s)</button>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </form>
                </div>
            </div>
        </div>
        <br><br><br>
        <%@include file="jspf/piePagina.jspf" %>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"></h4>
                    </div>
                    <div class="modal-body">
                        <div class="text-center" id="imagenCarga">
                            <img src="imagenes/ajax-loader-1.gif" alt="" />
                        </div>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
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
        <script src="js/facturajs/AutomaticaFacturacion.js"></script>
        <script src="js/jquery.alphanum.js" type="text/javascript"></script>
        <script src="js/sweetalert.min.js" type="text/javascript"></script>
        <script>
                                    $(document).ready(function () {
                                        $('#datosProv').dataTable();
                                    });
                                    function validaRemision() {
                                        var confirmacion = confirm('Seguro que desea generar los Folios');
                                        if (confirmacion === true) {
                                            $('#btnGeneraFolio').prop('disabled', true);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }

                                    function eliminarReq(e) {
                                        var confirma = confirm('Seguro que desea eliminar este requerimienro?');
                                        if (confirma) {
                                            var F_ClaUni = e.value;
                                            $.ajax({
                                                type: 'POST',
                                                url: 'Facturacion?eliminar=' + F_ClaUni,
                                                /*data: form.serialize(),*/
                                                success: function (data) {
                                                    recargarReqs(data);
                                                }
                                            });
                                            function recargarReqs(data) {
                                                location.reload();
                                            }
                                        }
                                    }
        </script>
        <script type="text/javascript">
            function muestraImagen() {
                $('#myModal').modal();
            }


            $("[data-behavior~=only-alphanum]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                allow: '.'
            });
            $("[data-behavior~=only-alphanum-caps]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true
            });
            $("[data-behavior~=only-alphanum-caps-15]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true,
                maxLength: 15
            });
            $("[data-behavior~=only-alphanum-white]").alphanum({
                allow: '.',
                disallow: "'",
                allowSpace: true
            });
            $("[data-behavior~=only-num]").numeric({
                allowMinus: false,
                allowThouSep: false
            });

            $("[data-behavior~=only-alpha]").alphanum({
                allowNumeric: false,
                allowSpace: false,
                allowOtherCharSets: true
            });


        </script> 

    </body>
</html>

