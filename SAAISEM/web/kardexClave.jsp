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
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
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

    String Clave = "", Proyecto = "", CB = "", Descripcion = "";
    try {
        Clave = request.getParameter("Clave");
        Descripcion = request.getParameter("Descrip");
        Proyecto = request.getParameter("Proyecto");
        CB = request.getParameter("CB");
    } catch (Exception e) {

    }
    if (Clave == null) {
        Clave = "";
    }
    if (Proyecto == null) {
        Proyecto = "";
    }
    if (CB == null) {
        CB = "";
    }
    if (Descripcion == null) {
        Descripcion = "";
    }
    String qryLote = "";
    try {
        System.out.println(request.getParameter("accion"));

        if (request.getParameter("accion").equals("CB")) {
            con.conectar();
            ResultSet rset = con.consulta("select F_ClaPro from tb_cb where F_Cb = '" + request.getParameter("CB") + "' group by F_ClaPro");
            while (rset.next()) {
                Clave = rset.getString(1);
            }
            con.cierraConexion();
            qryLote = ("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') from tb_lote where F_Cb = '" + request.getParameter("CB") + "' group by F_FecCad, F_ClaLot");
        }
        if (request.getParameter("accion").equals("Clave")) {
            con.conectar();
            if ((!(Descripcion.equals(""))) || (!(Descripcion.equals(null)))) {
                ResultSet rsetDesc = con.consulta("SELECT F_ClaPro FROM tb_medica m WHERE F_DesPro =  '" + Descripcion + "' group by F_ClaPro;");
            while (rsetDesc.next()) {
                Clave = rsetDesc.getString(1);
            }
                qryLote = ("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') from tb_lote where F_ClaPro = '" + Clave + "' group by F_FecCad, F_ClaLot");
                CB = "";
            } else {
                qryLote = ("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') from tb_lote where F_ClaPro = '" + request.getParameter("Clave") + "' group by F_FecCad, F_ClaLot");
                CB = "";
            }
             con.cierraConexion();
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
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
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <link href="css/select2.css" rel="stylesheet" type="text/css"/>
        <link href="css/sweetalert.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIALSS_CTRL</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>
            <%@include file="jspf/menuPrincipal.jspf" %>
        </div>
        <div style="width: 90%; margin: auto;">
            <h3>
                Kardex de Insumo
            </h3>

            <div class="panel panel-primary">
                <div class="panel-heading">
                    Criterios de Búsqueda
                </div>
                <div class="panel-body">
                    <form name="FormKardex" action="kardexClave.jsp" method="Post">
                        <div class="row">
                            <h4 class="col-sm-1">Descripción</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="Descrip" id="Descrip" type="text" value="<%=Descripcion%>"/>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary" name="accion" value="Clave">Por Descripción</button>
                            </div> 

                            <h4 class="col-sm-1">C.B.</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="CB" id="CB" type="text" value="<%=CB%>"/>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary" name="accion" value="CB">Por CB</button>
                            </div> 
                            <h4 class="col-sm-1">Clave</h4>
                            <div class="col-sm-2">
                                <input class="form-control" name="Clave" id="Clave" type="text" value="<%=Clave%>"/>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary" name="accion" value="Clave">Por Clave</button>
                            </div>
                        </div>
                        <div class="row">
                            <h4 class="col-sm-1">Lote</h4>
                            <div class="col-sm-2">
                                <select class="form-control" name="Lote" id="Lote" onchange="cambiaLoteCadu(this);">
                                    <option value="">-Lote-</option>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rsetLote = con.consulta(qryLote);
                                            while (rsetLote.next()) {
                                    %>
                                    <option><%=rsetLote.getString(2)%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                                </select>
                            </div>
                            <h4 class="col-sm-1">Caducidad</h4>
                            <div class="col-sm-2">
                                <select class="form-control" name="Cadu" id="Cadu">
                                    <option value="">-Caducidad-</option>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rsetLote = con.consulta(qryLote);
                                            while (rsetLote.next()) {
                                    %>
                                    <option><%=rsetLote.getString(3)%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                                </select>
                            </div>
                            <h4 class="col-sm-1">Origen</h4>
                            <div class="col-sm-2">
                                <select class="form-control" name="F_Origen" id="F_Origen">
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset3 = con.consulta("select F_ClaOri, F_DesOri from tb_origen");
                                            while (rset3.next()) {
                                    %>
                                    <option value="<%=rset3.getString("F_ClaOri")%>"><%=rset3.getString("F_DesOri")%></option>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    %>
                                </select>
                            </div>
                            <!--h4 class="col-sm-1">Proyecto</h4>
                            <div class="col-sm-2">
                                <select class="form-control" name="Proyecto" id="Proyecto">
                                    <option value="0">-- Seleccione --</option>
                            <%
                                /* try {
                                    con.conectar();
                                    ResultSet rset3 = con.consulta("SELECT F_Id, F_DesProy FROM tb_proyectos;");
                                    while (rset3.next()) {*/
                            %>
                            <option value="<%//=rset3.getString(1)%>"><%//=rset3.getString(2)%></option>
                            <%
                                /*}
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }*/
                            %>
                        </select>
                    </div-->
                        </div>
                        <div class="row">
                            <div class="col-sm-5">
                                <button class="btn btn-primary form-control" name="accion" value="Buscar">Buscar</button>
                            </div>
                            <div class="col-sm-5">
                                <a class="btn btn-primary form-control" a href="gnrKardexClave.jsp?Clave=<%=Clave%>&Lote=<%=request.getParameter("Lote")%>&Cadu=<%=request.getParameter("Cadu")%>&Btn=<%=request.getParameter("accion")%>&ProyectoCL=">Reporte de Trazabilidad</a>
                            </div>
                        </div>
                    </form>
                    <hr/>
                    <h3>Clave: <%=Clave%></h3>
                    <h4>Lote: 
                        <%
                            if (request.getParameter("Lote") != null) {
                                out.println(request.getParameter("Lote"));
                            }
                        %>
                    </h4>
                    <h4>Caducidad: 
                        <%
                            if (request.getParameter("Cadu") != null) {
                                out.println(request.getParameter("Cadu"));
                            }
                        %>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + Clave + "'");
                                while (rset.next()) {
                                    out.println("<h4>" + rset.getString(1) + "</h4>");
                                }
                                con.cierraConexion();
                            } catch (Exception e) {

                            }
                        %>
                        <br/>
                        <h4>Existencia Actual</h4>
                        <%
                            try {
                                con.conectar();
                                ResultSet rset = null;
                                String ProyectoExit = "";
                                if ((!(Proyecto.equals("0"))) || (!(Proyecto.equals("")))) {
                                    ProyectoExit = " AND F_Proyecto='" + Proyecto + "' ";
                                }
                                if (request.getParameter("accion").equals("Clave")) {
                                    rset = con.consulta("select SUM(F_ExiLot) from tb_lote where F_ClaPro = '" + Clave + "'  and F_ExiLot !=0;");
                                } else {
                                    rset = con.consulta("select SUM(F_ExiLot) from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot ='" + request.getParameter("Lote") + "' and F_FecCad = STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y') and F_Origen = '" + request.getParameter("F_Origen") + "'  and F_ExiLot !=0;");
                                }

                                while (rset.next()) {
                                    String Total = "0";
                                    Total = rset.getString(1);
                                    if (Total == null) {
                                        Total = "0";
                                    }
                        %>
                        <h4>Total: <%=formatter.format(Integer.parseInt(Total))%></h4>
                        <br/>
                        <%
                                }
                                con.cierraConexion();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        %>
                        <div style="width:100%; margin: auto" class="panel panel-info panel-body table-responsive">
                            <h3>Ingresos / Egresos</h3>
                            <table class="table table-bordered table-striped" width="100%" id="kardexTab2">
                                <thead> 
                                    <tr>
                                        <td>No. Mov</td>
                                        <td>Usuario</td>
                                        <td>Documento</td>
                                        <td>Remisión Proveedor</td>
                                        <td>Proveedor</td>
                                        <td>Folio Salida</td>
                                        <td>Punto de Entrega</td>
                                        <td>Concepto</td>
                                        <td>Clave</td>
                                        <td>Lote</td>
                                        <td>Caducidad</td>
                                        <td>Cantidad</td>
                                        <td>Ubicacion</td>
                                        <td>Proyecto</td>
                                        <td>Fecha</td>
                                        <td>Hora</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = null;
                                            String ProyectoMov = "";
                                            if (!(Proyecto.equals("0"))) {
                                                ProyectoMov = " AND l.F_Proyecto='" + Proyecto + "' ";
                                            }
                                            if (request.getParameter("accion").equals("Clave")) {
                                                rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, m.F_IdMov, m.F_LotMov, m.F_UbiMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + Clave + "' and m.F_ConMov!=1000 GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                                            } else {
                                                rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, m.F_IdMov, m.F_LotMov, m.F_UbiMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + Clave + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' and l.F_FecCad=STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y') and F_Origen = '" + request.getParameter("F_Origen") + "' and m.F_ConMov!=1000  GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                                            }

                                            while (rset.next()) {
                                                String Documento = "", Cliente = "", Provoeedor = "", FactRemi = "";
                                                if (rset.getString(2).equals("1")) {
                                                    ResultSet rset2 = con.consulta("select F_OrdCom, F_Provee from tb_compra where F_ClaDoc = '" + rset.getString(12) + "';");
                                                    while (rset2.next()) {
                                                        Documento = rset2.getString(1);
                                                        ResultSet rset3 = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + rset2.getString(2) + "' ");
                                                        while (rset3.next()) {
                                                            Provoeedor = rset3.getString(1);
                                                        }
                                                    }
                                                }
                                                if (rset.getString(2).equals("51")) {
                                                    ResultSet rset2 = con.consulta("SELECT U.F_NomCli, F.F_ClaDoc FROM tb_movinv M INNER JOIN tb_factura F ON M.F_DocMov = F.F_ClaDoc AND M.F_ProMov = F.F_ClaPro AND M.F_LotMov = F.F_Lote AND M.F_UbiMov = F.F_Ubicacion INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_DocMov = '" + rset.getString(12) + "' AND M.F_ProMov = '" + rset.getString(4) + "' AND M.F_LotMov = '" + rset.getString(14) + "' AND M.F_UbiMov = '" + rset.getString(15) + "' group by F_ClaDoc;");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                                if (rset.getString(2).equals("4")) {
                                                    ResultSet rset2 = con.consulta("SELECT U.F_NomCli, M.F_DocMov FROM tb_movinv M LEFT JOIN tb_devoluciones D ON M.F_DocMov = D.F_DocDev INNER JOIN tb_factura F ON D.F_DocRef = F.F_ClaDoc AND M.F_ProMov = F.F_ClaPro AND M.F_LotMov = F.F_Lote AND M.F_UbiMov = F.F_Ubicacion INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_DocMov = '" + rset.getString(12) + "' AND M.F_ProMov = '" + rset.getString(4) + "' AND M.F_LotMov = '" + rset.getString(14) + "' AND M.F_UbiMov = '" + rset.getString(15) + "' group by F_ClaDoc;");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                                if (rset.getString(2).equals("5")) {
                                                    ResultSet rset2 = con.consulta("SELECT U.F_NomCli, M.F_DocMov, D.F_DocRef, F_ProMov FROM tb_movinv M LEFT JOIN tb_devoluciones D ON M.F_DocMov = D.F_DocDev INNER JOIN tb_factura F ON D.F_DocRef = F.F_ClaDoc INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_DocMov = '" + rset.getString(12) + "' AND M.F_ProMov = '" + rset.getString(4) + "' AND M.F_LotMov = '" + rset.getString(14) + "' AND M.F_UbiMov = '" + rset.getString(15) + "' group by F_ClaDoc;");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                                if (rset.getString(2).equals("3")) {
                                                    ResultSet rset2 = con.consulta("SELECT U.F_NomCli, F.F_ClaDoc FROM tb_movinv M INNER JOIN tb_factura F ON M.F_DocMov = F.F_ClaDoc AND M.F_ProMov = F.F_ClaPro AND M.F_LotMov = F.F_Lote AND M.F_UbiMov = F.F_Ubicacion INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_DocMov = '" + rset.getString(12) + "' AND M.F_ProMov = '" + rset.getString(4) + "' AND M.F_LotMov = '" + rset.getString(14) + "' AND M.F_UbiMov = '" + rset.getString(15) + "' group by F_ClaDoc;");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                    %>
                                    <tr>
                                        <td><%=rset.getString("F_IdMov")%></td>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=Documento%></td>
                                        <td>
                                            <%
                                                if (!Documento.equals("")) {
                                                    ResultSet rset2 = con.consulta("select F_FolRemi from tb_compra where F_ClaPro = '" + rset.getString("F_ProMov") + "' and F_ClaDoc = '" + rset.getString("F_DocMov") + "' group by F_ClaDoc ");
                                                    while (rset2.next()) {
                                                        out.println(rset2.getString("F_FolRemi"));
                                                    }
                                                    //out.println(rset.getString("F_FolRemi"));
                                                }
                                            %>
                                        </td>
                                        <td><%=Provoeedor%></td>
                                        <td><%=FactRemi%></td>
                                        <td><%=Cliente%></td>
                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(4)%></td>
                                        <td><%=rset.getString(5)%></td>
                                        <td><%=rset.getString(6)%></td>
                                        <td><%=formatter.format(rset.getInt(7))%></td>
                                        <td><%=rset.getString(9)%></td>
                                        <td><%=rset.getString(17)%></td>
                                        <td><%=rset.getString(10)%></td>
                                        <td><%=rset.getString(11)%></td>
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


                        <div style="width:100%; margin: auto" class="panel panel-body panel-danger table-responsive">
                            <h3>Redistribución en Almacén</h3>
                            <table class="table table-bordered table-striped" width="100%" id="kardexTab">
                                <thead> 
                                    <tr>
                                        <td>No. Mov</td>
                                        <td>Usuario</td>

                                        <td>Concepto</td>
                                        <td>Clave</td>
                                        <td>Lote</td>
                                        <td>Caducidad</td>
                                        <td>Cantidad</td>
                                        <td>Ubicacion</td>
                                        <td>Proyecto</td>
                                        <td>Fecha</td>
                                        <td>Hora</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = null;
                                            String ProyectoMov = "";
                                            if (!(Proyecto.equals("0"))) {
                                                ProyectoMov = " AND l.F_Proyecto='" + Proyecto + "' ";
                                            }
                                            if (request.getParameter("accion").equals("Clave")) {
                                                rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, com.F_FolRemi, m.F_IdMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_compra com ON l.F_FolLot = com.F_Lote LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + Clave + "' and (m.F_ConMov=1000 or m.F_ConMov = 1) GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                                            } else {
                                                rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, com.F_FolRemi, m.F_IdMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_compra com ON l.F_FolLot = com.F_Lote LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + Clave + "' AND l.F_ClaLot = '" + request.getParameter("Lote") + "' AND l.F_FecCad = STR_TO_Date( '" + request.getParameter("Cadu") + "', '%d/%m/%Y' ) AND ( m.F_ConMov = 1000 OR m.F_ConMov = 1 ) AND F_Origen = '" + request.getParameter("F_Origen") + "' GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                                            }

                                            while (rset.next()) {
                                                String Documento = "", Cliente = "", Provoeedor = "", FactRemi = "";
                                                if (rset.getString(2).equals("1")) {
                                                    ResultSet rset2 = con.consulta("select F_OrdCom, F_Provee from tb_compra where F_ClaDoc = '" + rset.getString(12) + "' ");
                                                    while (rset2.next()) {
                                                        Documento = rset2.getString(1);
                                                        ResultSet rset3 = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + rset2.getString(2) + "' ");
                                                        while (rset3.next()) {
                                                            Provoeedor = rset3.getString(1);
                                                        }
                                                    }
                                                }
                                                if (rset.getString(2).equals("51")) {
                                                    ResultSet rset2 = con.consulta("select F_NomCli, F_ClaDoc from tb_factura f, tb_uniatn u where u.F_ClaCli = f.F_ClaCli and F_ClaDoc = '" + rset.getString(12) + "' ");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                                if (rset.getString(2).equals("3")) {
                                                    ResultSet rset2 = con.consulta("select F_NomCli, F_ClaDoc from tb_factura f, tb_uniatn u where u.F_ClaCli = f.F_ClaCli and F_ClaDoc = '" + rset.getString(12) + "' ");
                                                    while (rset2.next()) {
                                                        Cliente = rset2.getString(1);
                                                        FactRemi = rset2.getString(2);
                                                    }
                                                }
                                    %>
                                    <tr>
                                        <td><%=rset.getString("F_IdMov")%></td>
                                        <td><%=rset.getString(1)%></td>

                                        <td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(4)%></td>
                                        <td><%=rset.getString(5)%></td>
                                        <td><%=rset.getString(6)%></td>
                                        <td><%=formatter.format(rset.getInt(7))%></td>
                                        <td><%=rset.getString(9)%></td>
                                        <td><%=rset.getString(16)%></td>
                                        <td><%=rset.getString(10)%></td>
                                        <td><%=rset.getString(11)%></td>
                                    </tr>
                                    <%
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

        <%@include file="jspf/piePagina.jspf" %>
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
    <script src="js/select2.js" type="text/javascript"></script>
    <script src="js/sweetalert.min.js" type="text/javascript"></script>
    <script src="js/kardex/Kardex.js" type="text/javascript"></script>
    <script>
                                    $(document).ready(function () {
                                        $('#kardexTab').dataTable();
                                        $('#kardexTab2').dataTable();
                                    });

                                    function cambiaLoteCadu(elemento) {
                                        var indice = elemento.selectedIndex;
                                        document.getElementById('Cadu').selectedIndex = indice;
                                    }
    </script>
</html>

