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

    String fol_sialss = "", Unidad = "", Fecha = "";
    int Sts = 0;
    try {
        fol_sialss = request.getParameter("fol_sialss");
        Unidad = request.getParameter("Unidad");
        Fecha = request.getParameter("Fecha");

    } catch (Exception e) {

    }
    if (fol_sialss == null) {
        fol_sialss = "";

    }

    System.out.println("Fecha de descarga: ");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"ReqFarmacia_" + request.getParameter("fol_sialss") + "-"+Fecha+".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <div>
            <h4>Clave Unidad: <%=request.getParameter("fol_sialss")%></h4>
            <%
                try {
                    con.conectar();

                    //ResultSet rset = con.consulta("SELECT U.F_NomCli, SUM(R.requerido) AS requerido FROM requerimiento_lodimed R INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte WHERE R.clave_unidad = '" + Unidad + "' AND R.folio = '" + request.getParameter("fol_sialss") + "';");
                   ResultSet rset = con.consulta("SELECT  ua.F_NomCli, SUM(ur.F_Solicitado) FROM tb_unireq ur INNER JOIN tb_uniatn ua ON ua.F_ClaCli = ur.F_ClaUni WHERE ur.F_ClaUni = '" + Unidad + "' and ur.F_Status in (0,5) GROUP BY ur.F_ClaUni ORDER BY ur.F_FecCarg, ur.F_ClaUni; ");
                   
                    while (rset.next()) {
            %>
            <h4>Nombre Unidad: <%=rset.getString(1)%></h4>
            <div class="row">
                <h5 class="col-sm-3">Total Solicitado: <%=formatter.format(rset.getInt(2))%></h5>
            </div>
            <%
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
                                <th>Clave Unidad</th>
                                <th>Nombre Unidad</th>
                                <th>Fecha Cargo</th>
                                <th>Clave</th>
                                <th>Solicitado</th>
                                <th>Fecha Entrega</th>

                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    con.conectar();
                                    try {
                                        String query ="SELECT  ur.F_ClaUni, ua.F_NomCli,ur.F_FecCarg,ur.F_ClaPro,ur.F_Solicitado,ur.F_Fecha FROM tb_unireq ur INNER JOIN tb_uniatn ua ON ua.F_ClaCli = ur.F_ClaUni WHERE ur.F_ClaUni = '" + Unidad + "' and ur.F_Status in (0,5) AND ur.F_Fecha = '"+Fecha+"'  ORDER BY ur.F_FecCarg, ur.F_ClaUni; ";
                                      ResultSet rset = con.consulta(query);
                                        while (rset.next()) {
                                            String des = rset.getString(2);
                                          //  if(des.compareTo("**")==0){
                                           //     des= "style='background-color: #F7412B;'";
                                          //  }
                            %>
                            <tr >
                                <td style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td><%=rset.getString(6)%></td>
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