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
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Reabastecer_Modula2.xls");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-condensed table-striped" id="tablaMovMod">
            <thead>
                <tr class="text-center">
                    <td>Clave</td>
                    <td>Descripci&oacute;n</td>
                    <td>Ubicaci&oacute;n</td>
                    <td>Min</td>
                    <td>Reorden</td>
                    <td>Max</td>
                    <td>Exist Modula</td>
                    <td>Falta Max</td>
                    <td>Reabastecer</td>
                </tr>
            </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                String F_ExiLot="";
                    int F_ExiLot1=0,F_Max=0,Diferencia=0,Reabastecer=0,F_Reo=0,F_Min=0;
                    //ResultSet rset = con.consulta("SELECT M.F_Clapro,MD.F_DesPro,FORMAT(M.F_Min,0) AS F_Min,FORMAT(M.F_Reo,0) AS F_Reo,FORMAT(M.F_Max,0) AS F_Max,FORMAT(SUM(F_ExiLot),0) AS F_ExiLot,FORMAT((F_Max - SUM(F_ExiLot)),0) AS F_FMax,CASE WHEN (M.F_Reo>=SUM(F_ExiLot)) THEN FORMAT((M.F_Max-M.F_Min),0) ELSE '0' END AS F_Resurtir FROM tb_lote L INNER JOIN tb_minmax M ON L.F_ClaPro=M.F_Clapro INNER JOIN tb_medica MD ON M.F_Clapro=MD.F_ClaPro WHERE L.F_ClaPro IN (SELECT M.F_Clapro FROM tb_medica MD WHERE MD.F_StsPro='A') AND L.F_Ubica='MODULA'");
                    ResultSet rset = con.consulta("SELECT M.F_Clapro,MD.F_DesPro,M.F_Min,M.F_Reo,M.F_Max,M.F_Ubi FROM tb_minmax M INNER JOIN tb_medica MD ON M.F_Clapro=MD.F_ClaPro WHERE M.F_Ubi NOT IN ('D','MODULA','AF','MATCUR','CADUCADOS','PROXACADUCAR','MERMA','CROSSDOCKMORELIA','INGRESOS_V','CUARENTENA','FALTANTE_ORIGEN','EXTRA_ORDINARIA') ORDER BY M.F_Clapro+0;");
                    while (rset.next()) {
                        F_Max = rset.getInt(5);
                        F_Reo = rset.getInt(4);
                        F_Min = rset.getInt(3);
                       ResultSet ExiMod = con.consulta("SELECT SUM(F_ExiLot) FROM tb_lote WHERE F_ClaPro='"+rset.getString(1)+"' AND F_Ubica='"+rset.getString(6)+"' AND F_Proyecto = 2;");
                       if(ExiMod.next()){
                           F_ExiLot = ExiMod.getString(1);
                       }
                       if (F_ExiLot != null){
                           F_ExiLot1 = Integer.parseInt(F_ExiLot);
                       }else{
                           F_ExiLot1=0;
                       }
                       if (F_ExiLot1 == 0){
                           Diferencia = F_Max;
                           Reabastecer = F_Max;
                       }else{
                           if(F_ExiLot1 >= F_Reo){
                               Diferencia = F_Max - F_ExiLot1;
                                if (Diferencia < 0){                                   
                                   Diferencia = 0;
                               }
                           }else{
                               Diferencia = F_Max - F_ExiLot1;
                               Reabastecer = F_Max - F_Min;
                           }
                           if (F_ExiLot1<= F_Reo){
                               Reabastecer = F_Max - F_Min;
                           }
                       }
                                              
                    %>
                    <tr>
                        <td style="mso-number-format:'@'"><%=rset.getString(1)%></td>
                    <td><%=rset.getString(2)%></td>
                    <td><%=rset.getString(6)%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=rset.getString(3)%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=rset.getString(4)%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=rset.getString(5)%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=F_ExiLot1%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=Diferencia%></td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=Reabastecer%></td>
                </tr>
                <%
                        int Exi=0,DifeLot=0,Existencia=0;
                if(Reabastecer > 0){
                    ResultSet Lote = con.consulta("SELECT F_ClaLot,F_Ubica,F_ExiLot,F_Origen,date_format(F_FecCad,'%d/%m/%Y') as F_FecCad1 FROM tb_lote WHERE F_ClaPro='"+rset.getString(1)+"' AND F_Ubica NOT IN('"+rset.getString(6)+"','MODULA','MODULA2','A0S','AF','DENTAL','RACKG','RACKH','RACKI','RACKJ','RACKK','APE','MATCUR','CADUCADOS','PROXACADUCAR','MERMA','CROSSDOCKMORELIA','INGRESOS_V','CUARENTENA','FALTANTE_ORIGEN','EXTRA_ORDINARIA') and F_ExiLot>0 AND F_Proyecto = 2 ORDER BY F_FecCad ASC");
                    while(Lote.next()){
                        Exi = Lote.getInt(3);                        
                        if (Exi >= Reabastecer && Reabastecer > 0){                            
                            Existencia = Reabastecer;
                            Reabastecer = 0;                            
                        }else{                            
                            if (Reabastecer > 0){
                                Existencia = Exi;
                                Reabastecer = Reabastecer - Existencia;
                            }
                        }
                       if (Existencia>0){    
                %>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>Lote[<%=Lote.getString(1)%>]</td>
                    <td>Cadu[<%=Lote.getString(5)%>]</td>
                    <td>Origen[<%=Lote.getString(4)%>]</td>
                    <td>Ubica[<%=Lote.getString(2)%>]</td>
                    <td class="text-center" style="mso-number-format:'#,##0'"><%=Existencia%></td>
                </tr>
                    <%
                       }
                       Existencia = 0;
                       Exi = 0;
                    }
                }
                        Diferencia = 0;
                        Reabastecer = 0;
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