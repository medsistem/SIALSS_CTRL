<%-- 
    Document   : gnkKardexClave
    Created on : 22-oct-2014, 8:39:49
    Author     : amerikillo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    ConectionDB con = new ConectionDB();
    String Btn = request.getParameter("Btn");
    String ProyectoCL = request.getParameter("ProyectoCL");
    String Campo = "", ANDP="";

    if (!(ProyectoCL.equals(""))) {
        ANDP = " AND l.F_Proyecto IN (" + ProyectoCL + ") ";
        try {
            con.conectar();
            ResultSet rset = con.consulta("SELECT GROUP_CONCAT(CONCAT(F_Campo,'= 1')) AS F_Campo FROM tb_proyectos WHERE F_Id IN (" + ProyectoCL + ");");
            if (rset.next()) {
                Campo = rset.getString(1);
            }

            Campo = Campo.replace(",", " OR ");
            Campo = " AND ( " + Campo + " ) ";
            con.cierraConexion();
        } catch (Exception e) {

        }
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Trazabilidad_" + request.getParameter("Clave") + ".xls\"");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>-</title>
    </head>
    <body>
        <h2>Trazabilidad: <%=request.getParameter("Clave")%></h2>
        <h3>Lote: <%=request.getParameter("Lote")%></h3>
        <h3>Caducidad: <%=request.getParameter("Cadu")%></h3>
        <%
            try {
                con.conectar();
                ResultSet rset = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("Clave") + "' " + Campo + ";");
                while (rset.next()) {
                    out.println("<h3>" + rset.getString(1) + "</h3>");
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
                if (Btn.equals("Clave")) {
                    rset = con.consulta("select SUM(F_ExiLot) from tb_lote where F_ClaPro = '" + request.getParameter("Clave") + "' and F_ExiLot !=0 "+ANDP+";");
                } else {
                    rset = con.consulta("select SUM(F_ExiLot) from tb_lote where F_ClaPro = '" + request.getParameter("Clave") + "' and F_ClaLot ='" + request.getParameter("Lote") + "' and F_FecCad = STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y')  and F_ExiLot !=0 "+ANDP+";");
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
        <table border="1">
            
            <tr>
                <td>Clave</td>
                <td>Lote</td>
                <td>Caducidad</td>
                <td>Ubicacion</td>
                <td>Cantidad</td>
            </tr>
            <%
                try {
                    con.conectar();
                    ResultSet rset = null;
                    if (Btn.equals("Clave")) {
                        rset = con.consulta("select F_ClaPro, F_ClaLot, F_FecCad, F_Ubica, F_ExiLot from tb_lote where F_ClaPro = '" + request.getParameter("Clave") + "' and F_ExiLot !=0 "+ANDP+";");
                    } else {
                        rset = con.consulta("select F_ClaPro, F_ClaLot, F_FecCad, F_Ubica, F_ExiLot from tb_lote where F_ClaPro = '" + request.getParameter("Clave") + "' and F_ClaLot ='" + request.getParameter("Lote") + "' and F_FecCad = STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y')  and F_ExiLot !=0 "+ANDP+";");
                    }

                    while (rset.next()) {
            %>
            <tr>
                <td><%=rset.getString(1)%></td>
                <td><%=rset.getString(2)%></td>
                <td><%=rset.getString(3)%></td>
                <td><%=rset.getString(4)%></td>
                <td><%=rset.getString(5)%></td>
            </tr>
            <%
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            %>
        </table>
        <br/>

        <h4>Ingresos y Egresos</h4>
        <table border="1">
            <thead> 
                <tr>
                    <td>No. Mov</td>
                    <td>Usuario</td>
                    <td>Documento</td>
                    <td>Remisión</td>
                    <td>Proveedor</td>
                    <td>Factura</td>
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
                        if (Btn.equals("Clave")) {
                            rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, m.F_IdMov, m.F_LotMov, m.F_UbiMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' "+ANDP+" and m.F_ConMov!=1000 GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                        } else {
                            rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, m.F_IdMov, m.F_LotMov, m.F_UbiMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' and l.F_FecCad=STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y') "+ANDP+" and m.F_ConMov!=1000 GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
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
                                ResultSet rset2 = con.consulta("SELECT U.F_NomCli, F.F_ClaDoc FROM tb_movinv M INNER JOIN tb_factura F ON M.F_DocMov = F.F_ClaDoc AND M.F_ProMov = F.F_ClaPro AND M.F_LotMov = F.F_Lote AND M.F_UbiMov = F.F_Ubicacion INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F_DocMov = '" + rset.getString(12) + "' AND M.F_ProMov = '" + rset.getString(4) + "' AND M.F_LotMov = '" + rset.getString(14) + "' AND M.F_UbiMov = '" + rset.getString(15) + "' group by F_ClaDoc;");
                                while (rset2.next()) {
                                    Cliente = rset2.getString(1);
                                    FactRemi = rset2.getString(2);
                                }
                            }
                            if (rset.getString(2).equals("4")) {
                                /*ResultSet rset2 = con.consulta("select F_OrdCom, F_Provee from tb_compra where F_ClaDoc = '" + rset.getString(12) + "' group by F_ClaDoc ");
                                while (rset2.next()) {
                                    Documento = rset2.getString(1);
                                    ResultSet rset3 = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + rset2.getString(2) + "' ");
                                    while (rset3.next()) {
                                        Provoeedor = rset3.getString(1);
                                    }
                                }*/
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
                    <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(5)%></td>
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

        <br/>

        <h4>Redistribución en Almacén</h4>
        <table border="1">
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
                        if (Btn.equals("Clave")) {
                            rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, com.F_FolRemi, m.F_IdMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_compra com ON l.F_FolLot = com.F_Lote LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' "+ANDP+" and (m.F_ConMov=1000 or m.F_ConMov = 1) GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
                        } else {
                            rset = con.consulta("SELECT m.F_User, m.F_ConMov, c.F_DesCon, m.F_ProMov, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), (m.F_CantMov * m.F_SigMov), m.F_CostMov, u.F_DesUbi, DATE_FORMAT(m.F_FecMov, '%d/%m/%Y'), m.F_hora, m.F_DocMov, com.F_FolRemi, m.F_IdMov, l.F_Proyecto, p.F_DesProy FROM tb_movinv m INNER JOIN tb_coninv c ON m.F_ConMov = c.F_IdCon INNER JOIN tb_ubica u ON m.F_UbiMov = u.F_ClaUbi INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot AND m.F_ProMov = l.F_ClaPro AND m.F_UbiMov = l.F_Ubica INNER JOIN tb_compra com ON l.F_FolLot = com.F_Lote LEFT JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' and l.F_FecCad=STR_TO_Date('" + request.getParameter("Cadu") + "', '%d/%m/%Y') "+ANDP+" and (m.F_ConMov=1000 or m.F_ConMov = 1) GROUP BY m.F_IdMov ORDER BY m.F_IdMov;");
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
                    <td style="mso-number-format:'@';"><%=rset.getString(4)%></td>
                    <td style="mso-number-format:'@';"><%=rset.getString(5)%></td>
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
    </body>
</html>
