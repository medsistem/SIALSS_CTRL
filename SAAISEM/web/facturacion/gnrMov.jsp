<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
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

    String fecha_ini = "", fecha_fin = "", clave = "", ClaCli = "", Proyecto = "", Origen = "";
    try {
        //if (request.getParameter("accion").equals("buscar")) {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        clave = request.getParameter("clave");
        ClaCli = request.getParameter("ClaCli");
        Proyecto = request.getParameter("Proyecto");
        Origen = request.getParameter("Origen");
        //}
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (clave == null) {
        clave = "";
    }
    if (ClaCli == null) {
        ClaCli = "";
    }

    if (Proyecto == null) {
        Proyecto = "0";
    }

    if (Origen == null) {
        Origen = "0";
    }
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Movimientos.xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <div class="panel panel-primary">
                <div class="panel-body">
                    <table>
                        <%
                            Date dNow = new Date();
                            DateFormat ft = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                            String fechaDia = ft.format(dNow);
                        %> 
                         <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                         <td colspan="4"><h4><%=fechaDia%></h4></td>
                        </tr>
                        <tr></tr>                           
                        <tr>
                            <th colspan="7"><h1>Consulta de Movimientos</h1></th>                            
                        </tr> <tr></tr>

                    </table>
                    <table class="table table-bordered table-striped" id="datosCompras" border="1">
                        <thead>
                            <tr>
                                <td>Proyecto</td>
                                <td>Fecha Mov.</td>
                                <td>No.Documento</td>
                                <td>Con/Mov</td>
                                <td>Des/Mov</td>
                                <td>Clave</td>
                                <td>Descripci&oacute;n</td>
                                <td>Lote</td>
                                <td>Caducidad</td>
                                <td>Cantidad</td>
                                <td>Origen</td>
                                <td>Proveedor</td>
                                <td>Marca</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        String FechaFol = "", Clave = "", Concep = "", Query = "", AND = "", ANDOrigen = "";
                                        int ban = 0, ban1 = 0, ban2 = 0;

                                        if (Origen.equals("0")) {
                                            ANDOrigen = "";
                                        } else {
                                            ANDOrigen = " AND l.F_Origen='" + Origen + "' ";
                                        }

                                        if (Proyecto.equals("0")) {
                                            AND = "";
                                        } else {
                                            AND = " AND l.F_Proyecto='" + Proyecto + "' ";
                                        }
                                        if (clave != "") {
                                            ban = 1;
                                            Clave = " m.F_ProMov='" + clave + "' ";
                                        }
                                        if (fecha_ini != "" && fecha_fin != "") {
                                            ban1 = 1;
                                            FechaFol = " m.F_FecMov between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                                        }
                                        if (ClaCli != "") {
                                            ban2 = 1;
                                            Concep = " m.F_ConMov IN (" + ClaCli + ") ";
                                        }
                                        if (ban == 1 && ban1 == 1 && ban2 == 1) {
                                            Query = Clave + " AND " + FechaFol + " AND " + Concep;
                                        } else if (ban == 1 && ban1 == 1) {
                                            Query = Clave + " AND " + FechaFol;
                                        } else if (ban == 1 && ban2 == 1) {
                                            Query = Clave + " AND " + Concep;
                                        } else if (ban1 == 1 && ban2 == 1) {
                                            Query = FechaFol + " AND " + Concep;
                                        } else if (ban == 1) {
                                            Query = Clave;
                                        } else if (ban1 == 1) {
                                            Query = FechaFol;
                                        } else if (ban2 == 1) {
                                            Query = Concep;
                                        }

                                        ResultSet rset = con.consulta("SELECT DATE_FORMAT(m.F_FecMov,'%d/%m/%Y') AS F_FecMov,F_DocMov,F_ConMov,C.F_DesCon,F_ProMov,MD.F_DesPro,l.F_ClaLot,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F_CantMov) AS F_CantMov, p.F_DesProy, O.F_DesOri, IFNULL(pr.F_NomPro,'') AS F_NomPro, IFNULL(mc.F_DesMar,'') AS F_DesMar FROM tb_movinv m INNER JOIN tb_lote l on m.F_ProMov=l.F_ClaPro AND m.F_LotMov=l.F_FolLot AND m.F_UbiMov=l.F_Ubica INNER JOIN tb_medica MD ON m.F_ProMov=MD.F_ClaPro INNER JOIN tb_coninv C ON m.F_ConMov=C.F_IdCon INNER JOIN tb_proyectos p ON l.F_Proyecto=p.F_Id INNER JOIN tb_origen O ON l.F_Origen=O.F_ClaOri LEFT JOIN tb_proveedor pr ON l.F_ClaPrv = pr.F_ClaProve LEFT JOIN tb_marca mc ON l.F_ClaMar = mc.F_ClaMar WHERE " + Query + " AND m.F_ConMov >= 1 " + AND + " " + ANDOrigen + " GROUP BY F_FecMov,F_DocMov,F_ConMov,F_ProMov,l.F_ClaLot,F_FecCad,l.F_Origen;");
                                        while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(10)%></td>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>
                                <td style="mso-number-format:'@';"><%=rset.getString(7)%></td>
                                <td><%=rset.getString(8)%></td>
                                <td><%=rset.getString(9)%></td>
                                <td><%=rset.getString(11)%></td>
                                <td><%=rset.getString(12)%></td>
                                <td><%=rset.getString(13)%></td>
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