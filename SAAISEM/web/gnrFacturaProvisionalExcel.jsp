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
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fol_gnkl = "", fol_remi = "", orden_compra = "", fecha = "";
    try {
        if (request.getParameter("accion").equals("buscar")) {
            fol_gnkl = request.getParameter("fol_gnkl");
            fol_remi = request.getParameter("fol_remi");
            
            orden_compra = request.getParameter("orden_compra");
            String IdProyecto = request.getParameter("idProyecto");
            fecha = request.getParameter("fecha");
        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";
        fol_remi = "";
        orden_compra = "";
        fecha = "";
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Factura_" + request.getParameter("fol_gnkl") + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <h4>Folio de Factura: <%=request.getParameter("fol_gnkl")%></h4>
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.fecEnt,'%d/%m/%Y') AS F_FecEnt,F.folio as F_ClaDoc,L.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(D.cantReq) as requerido,SUM(D.cantSur) as surtido,D.costo,SUM(D.monto) as importe, L.F_Ubica FROM folio_provisional F INNER JOIN folio_provisional_detail D ON d.id_folio = F.id_folio INNER JOIN tb_lote L ON D.id_lote=L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_uniatn U ON F.claCli=U.F_ClaCli WHERE F.id_folio='" + request.getParameter("idFolio") + "' AND F.proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.folio");
                        while (rset.next()) {


            %>
            <h4>Cliente: <%=rset.getString(1)%></h4>
            <h4>Fecha de Entrega: <%=rset.getString(2)%></h4>
            <h4>Factura: <%=rset.getString(3)%></h4>
            <%
                int req = 0, sur = 0;
                Double imp = 0.0;
                ResultSet rset2 = con.consulta("SELECT (D.cantSur) as surtido,(D.cantReq) as requerido,(D.monto) as importe FROM folio_provisional F INNER JOIN folio_provisional_detail D ON d.id_folio = F.id_folio INNER JOIN tb_lote L ON D.id_lote=L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_uniatn U ON F.claCli=U.F_ClaCli WHERE F.id_folio='" + request.getParameter("idFolio") + "' AND F.proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY d.id_detail");
                while (rset2.next()) {
                    req = req + rset2.getInt("requerido");
                    sur = sur + rset2.getInt("surtido");
                    imp = imp + rset2.getDouble("importe");
                    System.out.println(req);
                }
            %>

            <div class="row">
                <h5 class="col-sm-3">Total Solicitado: <%=formatter.format(req)%></h5>
                <h5 class="col-sm-3">Total Surtido: <%=formatter.format(sur)%></h5>
                <h5 class="col-sm-3">Total Importe: $ <%=formatterDecimal.format(imp)%></h5>
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
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Clave</td>
                                <td>Descripción</td>
                                <td>Origen</td>
                                <td>Lote</td>
                                <td>Caducidad</td>
                                <td>Req.</td>
                                <td>Ubicación</td>
                                <td>Ent.</td>
                                <td>Costo U</td>
                                <td>Importe</td>
                                <td>Fec Fab</td>
                                <td>Marca</td>
                                <td>Status</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.fecEnt,'%d/%m/%Y') AS F_FecEnt,F.folio,L.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,D.cantReq,D.cantSur,D.costo,D.monto, L.F_Ubica, DATE_FORMAT(L.F_FecFab, '%d/%m/%Y') AS F_FecFab, Mar.F_DesMar, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, L.F_Origen FROM folio_provisional F INNER JOIN folio_provisional_detail D ON d.id_folio = F.id_folio INNER JOIN tb_lote L ON D.id_lote=L.F_IdLote INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_uniatn U ON F.claCli=U.F_ClaCli  INNER JOIN tb_marca Mar ON Mar.F_ClaMar = L.F_ClaMar WHERE F.id_folio='" + request.getParameter("idFolio") + "' AND F.proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY D.id_detail");
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString("F_Origen")%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(6)%></td>
                                <td><%=rset.getString(7)%></td>
                                <td><%=rset.getString(8)%></td>
                                <td><%=rset.getString(12)%></td>
                                <td><%=rset.getString(9)%></td>
                                <td><%=rset.getString(10)%></td>
                                <td><%=rset.getString(11)%></td>
                                <td><%=rset.getString("F_FecFab")%></td>
                                <td><%=rset.getString("F_DesMar")%></td>
                                <td><%=rset.getString("F_StsFact")%></td>
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
    </body>
</html>