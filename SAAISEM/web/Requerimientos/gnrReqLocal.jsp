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

        }
    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";

    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Requerimiento_" + request.getParameter("fol_gnkl") + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <h4>Folio del Requerimiento: <%=request.getParameter("fol_gnkl")%></h4>
            <%
                try {
                    con.conectar();
                    try {
                        ResultSet rset = con.consulta("SELECT US.F_JurisDes, US.F_NomCli FROM tb_requerimientos R INNER JOIN tb_uniatn U ON R.F_ClaCli = U.F_ClaCli INNER JOIN tb_usuariounidades US ON R.F_ClaCli = US.F_ClaCli AND U.F_ClaCli = US.F_ClaCli WHERE F_IdReq = '" + request.getParameter("fol_gnkl") + "' GROUP BY US.F_JurisDes, US.F_NomCli;");
                        while (rset.next()) {


            %>
            <h4>Jurisdiccion: <%=rset.getString(1)%></h4>
            <h4>Nombre Unidad: <%=rset.getString(2)%></h4>    
            <%
                int req = 0, sur = 0;
                Double imp = 0.0;
                ResultSet rset2 = con.consulta("SELECT SUM(F_Cant) FROM tb_detrequerimiento WHERE F_IdReq='" + request.getParameter("fol_gnkl") + "'");
                while (rset2.next()) {
                    req = rset2.getInt(1);

                }
            %>

            <div class="row">
                <h5 class="col-sm-3">Total Solicitado: <%=formatter.format(req)%></h5>

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
                                <td>Descripcion</td>                        
                                <td>Cant. Req.</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        ResultSet rset = con.consulta("SELECT D.F_ClaPro,M.F_DesPro,D.F_Cant FROM tb_detrequerimiento D INNER JOIN tb_medica M ON D.F_ClaPro=M.F_ClaPro WHERE F_IdReq='" + request.getParameter("fol_gnkl") + "' AND F_Cant>0");
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td style='mso-number-format:"@"'><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>                        
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
                <br />
                <br />
                <br />
                <!--div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosfirmas" border="0">
                        <tr>
                            <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                            <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><h5>RESPONSABLE MEDICO</h5></td>
                            <td colspan="3"><h5>COORDINADOR O ADMINISTRADOR MUNICIPAL</h5></td>
                        </tr>
                    </table>
                </div>
                </div-->


            </div>
        </div>
    </body>
</html>