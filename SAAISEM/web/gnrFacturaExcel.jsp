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
                        ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantReq) as requerido,SUM(F.F_CantSur) as surtido,F.F_Costo,SUM(F.F_Monto) as importe, F.F_Ubicacion,DATE_FORMAT(F.F_FecApl,'%d/%m/%Y') AS elaboracion,tb_obserfact.F_Obser FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli LEFT JOIN tb_obserfact ON F.F_ClaDoc = tb_obserfact.F_IdFact AND F.F_Proyecto = tb_obserfact.F_Proyecto WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaDoc");
                        
                        while (rset.next()) {


            %>
            <h4>Cliente: <%=rset.getString(1)%></h4>
            <h4>Fecha de Entrega: <%=rset.getString(2)%></h4>
            <h4>Fecha de Elaboración: <%=rset.getString(13)%></h4>
            <h4>Observación: <%=rset.getString(14)%></h4>
            <h4>Factura: <%=rset.getString(3)%></h4>
            <%
                int req = 0, sur = 0;
                Double imp = 0.0;
                ResultSet rset2 = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,(F.F_CantSur) as surtido,(F.F_CantReq) as requerido,F.F_Costo,(F.F_CantSur * M.F_Costo) as importe, F.F_Ubicacion, DATE_FORMAT(L.F_FecFab, '%d/%m/%Y') AS F_FecFab, f.F_StsFact as sts FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_IdFact");
                while (rset2.next()) {
                    req = req + rset2.getInt("requerido");
                    if (rset2.getString("sts").equals("A")) {
                          sur = sur + rset2.getInt("surtido");   
                        }
                   
                    imp = imp + rset2.getDouble("importe");
                   
                }
                 System.out.println("Requerido: "+req);
                 System.out.println("Surtido: "+sur);
            %>

            <div class="row">
                <h5 class="col-sm-3">Total Solicitado: <%=formatter.format(req)%></h5>
                <h5 class="col-sm-3">Total Surtido: <%=formatter.format(sur)%></h5>
                <!--h5 class="col-sm-3">Total Importe: $ <%=formatterDecimal.format(imp)%></h5-->
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
                                <th>Clave</th>
                                <th>Descripción</th>
                                <th>Origen</th>
                                <th>Lote</th>
                                <th>Caducidad</th>
                                <th>Req.</th>
                                <th>Ubicación</th>
                                <th>Ent.</th>
                                <th>Costo U</th>
                                <th>Importe</th>
                                <th>Fec Fab</th>
                                <th>Marca</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        ResultSet rset = con.consulta("SELECT U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaDoc,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,F.F_CantReq,F.F_CantSur,M.F_Costo,(F.F_CantSur * M.F_Costo) as F_Monto, F.F_Ubicacion, DATE_FORMAT(L.F_FecFab, '%d/%m/%Y') AS F_FecFab, Mar.F_DesMar, F_StsFact, L.F_Origen , F.F_Proyecto FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli  INNER JOIN tb_marca Mar ON Mar.F_ClaMar = L.F_ClaMar WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_IdFact");
                                       
                                        while (rset.next()) {
                                        int Proy = Integer.parseInt(rset.getString("F_Proyecto"));
                                        System.out.println("Proyecto: "+Proy);
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
                                <%if(Proy == 26) {%>
                                <td><%=rset.getString(10)%></td>
                                <td><%=rset.getString(11)%></td>     
                                 <%   }else { %>
                                <td>0</td>
                                <td>0</td>  
                                 <%  
                                     }
                                %>
                               
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