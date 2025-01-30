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

    String fol_gnkl = "", Unidad = "";
    int Sts = 0;
    try {
        fol_gnkl = request.getParameter("fol_gnkl");
        Unidad = request.getParameter("Unidad");

    } catch (Exception e) {

    }
    if (fol_gnkl == null) {
        fol_gnkl = "";

    }

    String UnidadSe = "";
    String juris = "";

    try {
        UnidadSe = request.getParameter("UnidadSe");
        juris = request.getParameter("juris");
    } catch (Exception e) {

    }

    if (UnidadSe == null) {
        UnidadSe = "";
    }
    if (juris == null) {
        juris = "";
    }
    String fechas = "", fechasCap="",fecha1="";
    String fechaCap1 = "";
    try {
        fechaCap1 = df2.format(df2.parse(request.getParameter("fechaCap1")));
    } catch (Exception e) {
        fechaCap1 = "";
    }
    String fechaCap2 = "";
    try {
        fechaCap2 = df2.format(df2.parse(request.getParameter("fechaCap2")));
        if(!fechaCap1.isEmpty()){
            fechasCap = "AND fecha >= '" + fechaCap1 + "' AND fecha <= '" + fechaCap2 + "' ";
        }
    } catch (Exception e) {
        fechaCap2 = "";
    }
    String fecha2 = "";
    try {
        fecha2 = df2.format(df2.parse(request.getParameter("fecha2")));
        if (!fecha1.isEmpty()) {
            fechas = "AND fecha_entrega >= '" + fecha1 + "' AND fecha_entrega <='" + fecha2 + "'";
        }
    } catch (Exception e) {
        fecha2 = "";
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"ReqFarmacia_Mult.xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <br />
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Unidad</td>
                                <td>Fecha de Entrega</td>
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
//                                        String query ="SELECT R.clave, IFNULL(M.F_DesPro,'**'), SUM(R.requerido) AS requerido FROM requerimiento_lodimed R LEFT JOIN tb_medica M ON R.clave COLLATE utf8_general_ci = M.F_ClaPro WHERE R.requerido > 0 AND R.folio = '" + request.getParameter("fol_gnkl") + "' AND R.clave_unidad = '" + Unidad + "' GROUP BY R.clave;";
                                        String query = "";
                                        if(!fechasCap.isEmpty()){
                                            query = "SELECT U.F_ClaCli, R.clave, IFNULL(M.F_DesPro,'**'), SUM(R.requerido) AS requerido, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega FROM requerimiento_lodimed R LEFT JOIN tb_medica M on M.F_ClaPro = R.clave COLLATE utf8_general_ci LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 AND U.F_StsCli = 'A' " + fechas + " " +fechasCap + "GROUP BY R.id order by R.fecha desc, u.F_ClaCli;"; 
                                        }
                                        if (!juris.isEmpty() && UnidadSe.isEmpty()) {
                                            query = "SELECT U.F_ClaCli, R.clave, IFNULL(M.F_DesPro,'**'), SUM(R.requerido) AS requerido, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega FROM requerimiento_lodimed R LEFT JOIN tb_medica M on M.F_ClaPro = R.clave COLLATE utf8_general_ci LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 AND U.F_ClaJur = '" + juris + "' AND U.F_StsCli = 'A' " + fechas + " " +fechasCap + " group by R.id order by R.fecha desc, u.F_ClaCli;;";
                                        }
                                        if (!UnidadSe.isEmpty()) {
                                            query = "SELECT U.F_ClaCli, R.clave, IFNULL(M.F_DesPro,'**'), SUM(R.requerido) AS requerido, IFNULL(DATE_FORMAT(re.fecha_entrega, '%d/%m/%Y'),'') AS fecha_entrega FROM requerimiento_lodimed R LEFT JOIN tb_medica M on M.F_ClaPro = R.clave COLLATE utf8_general_ci LEFT JOIN requerimiento_entrega re ON re.folio = R.folio AND re.clave_unidad = R.clave_unidad COLLATE utf8_general_ci INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte AND R.requerido > 0 AND R.clave_unidad = '" + UnidadSe + "' AND U.F_StsCli = 'A' " + fechas + " " +fechasCap + " group by R.id order by R.fecha desc, u.F_ClaCli;;";
                                        }
                                        System.out.println(query);
                                        ResultSet rset = con.consulta(query);
                                        while (rset.next()) {
                                            String des = rset.getString(2);
                                            if (des.compareTo("**") == 0) {
                                                des = "style='background-color: #F7412B;'";
                                            }
                            %>
                            <tr <%=des%>>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
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