<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
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
    int Total = 0,TotalMes = 0,TotalIni = 0,TotalComp = 0,TotalDev = 0,TotalOMov = 0,TotalFact = 0,TotalSMov = 0, Cant = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    String MesT = "", FechaI = "", FechaF = "", MesD = "";
    int mes = 0, anno = 0, dia = 0;
    try {
        mes = Integer.parseInt(request.getParameter("Mess"));
        anno = Integer.parseInt(request.getParameter("Anno"));
    } catch (Exception e) {
    }

    if (mes < 10) {
        MesT = "0" + mes;
    } else {
        MesT = "" + mes;
    }
    if (mes == 1) {
        dia = 31;
        MesD = "ENERO";
    } else if (mes == 2) {
        if ((anno == 2016) || (anno == 2020) || (anno == 2024)) {
            dia = 29;
        } else {
            dia = 28;
        }
        MesD = "FEBRERO";
    } else if (mes == 3) {
        dia = 31;
        MesD = "MARZO";
    } else if (mes == 4) {
        dia = 30;
        MesD = "ABRIL";
    } else if (mes == 5) {
        dia = 31;
        MesD = "MAYO";
    } else if (mes == 6) {
        dia = 30;
        MesD = "JUNIO";
    } else if (mes == 7) {
        dia = 31;
        MesD = "JULIO";
    } else if (mes == 8) {
        dia = 31;
        MesD = "AGOSTO";
    } else if (mes == 9) {
        dia = 30;
        MesD = "SEPTIEMBRE";
    } else if (mes == 10) {
        dia = 31;
        MesD = "OCTUBRE";
    } else if (mes == 11) {
        dia = 30;
        MesD = "NOVIEMBRE";
    } else if (mes == 12) {
        dia = 31;
        MesD = "DICIEMBRE";
    }
    FechaI = anno + "-" + MesT + "-01";
    FechaF = anno + "-" + MesT + "-" + dia;

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Existencias_Cedis_Mes_Detalle_" + MesD + "-" + anno + ".xls\"");
    try {
        con.conectar();
        ResultSet Consulta = null;
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table>
                <tr>
                <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                </tr><tr></tr>
                <tr>
                    <th colspan="7"><h1>Reporte de Existencia en Cedis por Mes</h1></th>
                </tr>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaDia = ft.format(dNow);
                %>
                <tr>
                    <td colspan="7"><%=fechaDia%></td>
                </tr>                
            </table>
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <th style="text-align: center">Clave</th>
                        <th style="text-align: center">Descripci&oacute;n</th>
                        <th style="text-align: center">Lote</th>
                        <th style="text-align: center">Caducidad</th>
                        <th style="text-align: center">cantidad</th>
                        <th style="text-align: center">Movimientos</th>
                        <th style="text-align: center">Concepto</th>
                        
                    </tr>            
                </thead>
               <tbody>
                   
                    
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(M.F_CantMov*M.F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro WHERE F_ConMov='11' AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalIni = TotalIni + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Entrada Por Inv. Inicial</td>
                        <td></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center">SubTotal Entrada Por Inv. Inicial</td>
                        <td><%=formatter.format(TotalIni)%></td>
                        <td>Entrada Por Inv. Inicial</td>
                        <td></td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(M.F_CantMov*M.F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro WHERE F_ConMov='1' AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalComp = TotalComp + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Entrada Por Compra</td>
                        <td></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center">SubTotal Entrada Por Compra</td>
                        <td><%=formatter.format(TotalComp)%></td>
                        <td>Entrada Por Compra</td>
                        <td></td>
                    </tr>
                     <tr></tr>
                    <tr></tr> 
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(M.F_CantMov*M.F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro WHERE F_ConMov in (3,4,5,6,20,26,27) AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalDev = TotalDev + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Entrada Por Devoluci&oacute;n</td>
                        <td></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center">SubTotal Entrada Por Devoluci&oacute;n</td>
                        <td><%=formatter.format(TotalDev)%></td>
                        <td>Entrada Por Devoluci&oacute;n</td>
                        <td></td>
                    </tr>
                     <tr></tr>
                    <tr></tr> 
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(M.F_CantMov*M.F_SigMov) AS F_CantMov,C.F_DesCon FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro INNER JOIN tb_coninv C ON C.F_IdCon=M.F_ConMov WHERE M.F_ConMov in (2,7,8,9,10,13,16,18,19,22,23,24,25,26,27,28,30) AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad,M.F_ConMov;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalOMov = TotalOMov + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Entrada Por Otros Movimientos</td>
                        <td><%=Consulta.getString(6)%></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center">SubTotal Entrada Por Otros Movimientos</td>
                        <td><%=formatter.format(TotalOMov)%></td>
                        <td>Entrada Por Otros Movimientos</td>
                        <td></td>
                    </tr>
                     <tr></tr>
                    <tr></tr> 
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,(SUM(M.F_CantMov*M.F_SigMov)* -1) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro WHERE F_ConMov = '51' AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalFact = TotalFact + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Salida Por Facturaci&oacute;n</td>
                        <td></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4"  style="text-align: center">SubTotal Salida Por Facturaci&oacute;n</td>
                        <td><%=formatter.format(TotalFact)%></td>
                        <td>Salida Por Facturaci&oacute;n</td>
                        <td></td>
                    </tr>
                     <tr></tr>
                    <tr></tr> 
                    
                    <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,(SUM(M.F_CantMov*M.F_SigMov)* -1) AS F_CantMov,C.F_DesCon FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro INNER JOIN tb_coninv C ON C.F_IdCon=M.F_ConMov WHERE F_ConMov > '51' AND F_ConMov < '1000' AND F_FecMov BETWEEN '" + FechaI + "' AND '" + FechaF + "' AND F_ProMov <> '9999' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad,M.F_ConMov;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalSMov = TotalSMov + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Salida Por Otros Movimientos</td>
                        <td><%=Consulta.getString(6)%></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                        Total = (TotalIni+TotalMes+TotalComp+TotalDev+TotalOMov)-(TotalFact+TotalSMov); 
                    %>
                    <tr>
                        <td colspan="4" style="text-align: right">SubTotal Salida Por Otros Movimientos</td>
                        <td><%=formatter.format(TotalSMov)%></td>
                        <td>Salida Por Otros Movimientos</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4" style="text-align: center">Total Inv. Final Mes</td>
                        <td><%=formatter.format(Total)%></td>
                        <td></td>
                    </tr>
                     <tr></tr>
                    <tr></tr>
                    
                     <%
                        Consulta = con.consulta("SELECT L.F_ClaPro,MD.F_DesPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(M.F_CantMov*M.F_SigMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov=L.F_ClaPro AND M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_medica MD ON L.F_ClaPro=MD.F_ClaPro WHERE F_FecMov < '" + FechaI + "' AND F_ProMov <> '9999' and M.F_ConMov < '1000' GROUP BY L.F_ClaPro,L.F_ClaLot,L.F_FecCad HAVING F_CantMov > 0;");
                        while (Consulta.next()) {
                            Cant = Consulta.getInt(5);
                            TotalMes = TotalMes + Cant;
                            if (Cant > 0) {
                    %>
                    <tr>
                        <td><%=Consulta.getString(1)%></td>
                        <td><%=Consulta.getString(2)%></td>
                        <td style='mso-number-format:"@"'><%=Consulta.getString(3)%></td>
                        <td><%=Consulta.getString(4)%></td>
                        <td><%=formatter.format(Cant)%></td>
                        <td>Inv. Inicio del Mes</td>
                        <td></td>
                    </tr>
                    <%
                            }
                            Cant = 0;
                        }
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center">SubTotal Inv. Inicio del Mes</td>
                        <td><%=formatter.format(TotalMes)%></td>
                        <td>Inv. Inicio del Mes</td>
                        <td></td>
                    </tr>
                    
                   
                </tbody>
            </table>
        </div>
    </div>
</div>
<%
        con.cierraConexion();
    } catch (Exception e) {

    }


%>