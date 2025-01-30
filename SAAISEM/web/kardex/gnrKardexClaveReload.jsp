<%-- 
    Document   : gnkKardexClave
    Created on : 22-oct-2014, 8:39:49
    Author     : amerikillo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.medalfa.saa.db.ConnectionManager"%>
<%@page import="com.medalfa.saa.db.Source"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.medalfa.saa.querys.kardexQuerys"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../jspf/header.jspf" %>
<!DOCTYPE html>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    //HttpSession sesion = request.getSession();
    //String usua = "";
    ConectionDB con = new ConectionDB();
    String Btn = request.getParameter("Btn");
    String ProyectoCL = request.getParameter("ProyectoCL");
    String origen = request.getParameter("origen");
    String Campo = "", ANDP = "";

    String fechaInicial = request.getParameter("fechaInicial");
    String fechaFinal = request.getParameter("fechaFinal");

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
        } catch (Exception ex) {
            Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    String query_kardex = "";
    int banQuery = 0;
    String query_reubicacion = "";

    if (Btn.equals("Clave")) {

        if (!fechaInicial.equals("") && !fechaFinal.equals("")) 
        {
            query_kardex = String.format(kardexQuerys.OBTENER_KARDEX_POR_CLAVE, "AND m.F_FecMov BETWEEN ? AND ?");
            query_reubicacion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_CLAVE, "AND m.F_FecMov BETWEEN ? AND ?");
        } else {
            query_kardex = String.format(kardexQuerys.OBTENER_KARDEX_POR_CLAVE, "");
            query_reubicacion =  String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_CLAVE, "");
        }

        
    } else {
        banQuery = 1;
        
        if (!fechaInicial.equals("") && !fechaFinal.equals("")) 
        {
            query_kardex = String.format(kardexQuerys.OBTENER_KARDEX_POR_LOTE_CADUCIDAD, "AND m.F_FecMov BETWEEN ? AND ?");
            query_reubicacion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_LOTE_Y_CADUCIDAD, "AND m.F_FecMov BETWEEN ? AND ?");
        }
        else
        {        
        query_kardex = String.format(kardexQuerys.OBTENER_KARDEX_POR_LOTE_CADUCIDAD, "");
        query_reubicacion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_LOTE_Y_CADUCIDAD, "");
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
        <table>
            <tr valign="center">
                <td colspan="2"> <img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhWdMb9W8Mf4jHS3i4Ip6itWUjOl6PiZBN2kzsyp_T8txdQS2_4Ji1wlNJ5M7PiQjtkJSpcnGqaeJCsQgh6kBWMlFUtjIjc0fdgVgfhB8zqvidN0Jb22g6cku9pn4ZjODJzc93mvIi8y8fZcjjV2-RCj9NTMpPkOY7b-Pgs-3UNFiJASGLgUGn1viGx/s200/LOGO%20COLOR%20360%20400X400%20GNKL%20BAJIO.jpg" border="0" width="12%" height="5%" alt="" id="LogoMarbete"> </td>
                 <th colspan="3"><h1>Reporte de Trazabilidad</h1></th>
            </tr>
            <tr>
                <td colspan="3"><h2>Clave: <%=request.getParameter("Clave")%></h2></td>
                <td><%
                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("Clave") + "' " + Campo + ";");
                        while (rset.next()) {
                            out.println("<h3>" + rset.getString(1) + "</h3>");
                        }

                    } catch (Exception ex) {
                        Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                    } finally {
                        try {
                            con.cierraConexion();
                        } catch (Exception ex) {
                            Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                    %>  
            </tr>
            <tr valign="center">
                <td colspan="3"><h3>Lote: <%=request.getParameter("Lote")%></h3></td>
                <td colspan="3"><h3>Caducidad: <%=request.getParameter("Cadu")%></h3></td>
            </tr>
            <tr>
                <td colspan="3"><h4>Existencia Actual</h4></td>
            </tr>
            <%
                try {
                    con.conectar();
                    ResultSet rset = null;
                    ResultSet rsetApartado = null;
                      if (Btn.equals("Clave") && request.getParameter("Lote").equals("-Seleccione-")) 
                {
                    System.out.println("sin lote");
                    //rset = con.consulta("select SUM(F_CantMov * F_SigMov) from tb_movinv m inner join tb_lote l on l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov where F_ProMov = '" + request.getParameter("Clave") + "' " + ANDP + ";");
              rset = con.consulta("SELECT (SUM(m.F_CantMov*m.F_SigMov)) AS existencia FROM tb_movinv AS m  WHERE m.F_ProMov = '" + request.getParameter("Clave") + "';");
               
               rsetApartado = con.consulta("select IFNULL(sum(a.F_Cant),0) as apartado FROM tb_lote l LEFT JOIN tb_apartado AS a ON l.F_IdLote =a.F_IdLote and a.F_Status = 1 WHERE l.F_ClaPro ='" + request.getParameter("Clave") + "' " + ANDP + " AND l.F_ExiLot <> 0"); 
                } 
             //   else if (Btn.equals("Clave") && !fechaFinal.equals("")) 
             //   {
             //       rset = con.consulta("SELECT Mov.CantMov FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN( SELECT F_ProMov, SUM( F_CantMov * F_SigMov) AS CantMov FROM tb_movinv M WHERE M.F_ProMov = '" + request.getParameter("Clave") + "' AND M.F_ConMov != 1000 AND M.F_FecMov <= '" +fechaFinal+ "' GROUP BY F_ProMov HAVING CantMov > 0 ) Mov ON L.F_ClaPro = Mov.F_ProMov WHERE l.F_ClaPro ='" + request.getParameter("Clave") + "' GROUP BY L.F_ClaPro;");
            //        
            //    }
            //    else if(Btn.equals("")  && !fechaFinal.equals(""))
            //    {
            //        rset = con.consulta("SELECT SUM(m.F_CantMov * m.F_SigMov) AS existencia FROM tb_movinv m INNER JOIN( SELECT F_FolLot FROM tb_lote WHERE F_ClaPro ='" + request.getParameter("Clave") + "' AND F_ClaLot = '" + request.getParameter("Lote") + "' AND F_FecCad = '" + request.getParameter("Cadu") + "' GROUP BY F_FolLot) lote ON m.F_ProMov = '" + request.getParameter("Clave") + "' AND M.F_ConMov != 1000 AND M.F_FecMov <= '" +fechaFinal+ "' AND lote.F_FolLot = m.F_LotMov;");
             //   }
                else 
                {
                    System.out.println("con lote");
                     rset = con.consulta("SELECT (SUM(m.F_CantMov*m.F_SigMov)) AS existencia FROM tb_movinv AS m INNER JOIN (select l.F_ClaPro,l.F_ClaLot,l.F_Ubica, l.F_FolLot, m.F_DesPro FROM tb_lote l INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = l.F_Ubica INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro where l.F_ClaPro = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' and l.F_FecCad = '" + request.getParameter("Cadu") + "'and l.F_Origen = '"+request.getParameter("origen")+"' and l.F_ExiLot !=0 " + ANDP + " GROUP BY l.F_IdLote ) as lot on lot.F_ClaPro = m.F_ProMov and lot.F_FolLot = m.F_LotMov AND lot.F_Ubica = m.F_UbiMov;");
                    //rset = con.consulta("select SUM(F_CantMov * F_SigMov) from tb_movinv m inner join tb_lote as l on l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov where F_ClaPro = '" + request.getParameter("Clave") + "' and F_ClaLot ='" + request.getParameter("Lote") + "' and F_FecCad = '" + request.getParameter("Cadu") + "'and l.F_Origen = '"+request.getParameter("origen")+"' and F_ExiLot !=0 " + ANDP + ";");
                        rsetApartado = con.consulta("select IFNULL(sum(a.F_Cant),0) as apartado FROM tb_lote l LEFT JOIN tb_apartado AS a ON l.F_IdLote = a.F_IdLote AND a.F_Status = 1 INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = l.F_Ubica INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro where l.F_ClaPro = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' and l.F_FecCad = '" + request.getParameter("Cadu") + "'and l.F_Origen = '"+request.getParameter("origen")+"' and l.F_ExiLot !=0 " + ANDP + " GROUP BY l.F_IdLote;");
                }
                String Total = "0"; 
                   int  Apart = 0;
                    while (rset.next()) {
                       // String Total = "0";                        
                        Total = rset.getString(1);                        
                    }
                     while (rsetApartado.next()) {
                          Apart = rsetApartado.getInt(1);
                     }
                     if (Total == null) {
                            Total = "0";
                        }
                      Apart = Integer.parseInt(Total) -  Apart;
            %>
            <tr>
                <td colspan="2"><h4>Total: <%=formatter.format( Apart)%></h4></td>
                <%
                    Date dNow = new Date();
                    DateFormat ft = new SimpleDateFormat("dd/MM/yyyy'  'HH:mm:ss");
                    String fechaDia = ft.format(dNow);
                %>
                <td colspan="3"><%=fechaDia%></td>
            </tr>
        </table>
        
        <%
               

            } catch (Exception ex) {
                Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
            } finally {
                try {
                    con.cierraConexion();
                } catch (Exception ex) {
                    Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        %>
        <table border="1">
            <tr>
                <th class="text-center">Clave</th>
                <th class="text-center">Lote</th>
                <th class="text-center">Caducidad</th>
                <th class="text-center">Ubicacion</th>
                <th class="text-center">Cantidad</th>
                <th class="text-center">Apartado</th>
                <th class="text-center">Disponible</th>
                <th class="text-center">Ubicación temporal</th>
            </tr>
            <%
                try {
                    con.conectar();
                    ResultSet rset = null;
                    ResultSet rsetApartado = null;
                   if (Btn.equals("Clave") && request.getParameter("Lote").equals("-Seleccione-")) {
                        // rset = con.consulta("SELECT m.F_ProMov,lot.F_ClaLot, lot.F_FecCad, lot.F_Ubica, SUM(m.F_CantMov*m.F_SigMov), lot.esTemporal, lot.apartado FROM tb_movinv AS m INNER JOIN (select l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Ubica, l.F_ExiLot,IFNULL(sum(a.F_Cant),0), IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal, l.F_FolLot,IFNULL(sum(a.F_Cant),0) as apartado FROM tb_lote l LEFT JOIN tb_apartado AS a ON l.F_IdLote =a.F_IdLote AND a.F_Status = 1 INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = l.F_Ubica WHERE l.F_ClaPro = '" + request.getParameter("Clave") + "' AND l.F_ExiLot <> 0  " + ANDP +"  GROUP BY l.F_IdLote ) as lot on lot.F_ClaPro = m.F_ProMov and lot.F_FolLot = m.F_LotMov AND lot.F_Ubica = m.F_UbiMov WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' GROUP BY lot.F_ClaLot,lot.F_FecCad,lot.F_Ubica,m.F_LotMov ORDER BY lot.F_ClaLot,lot.F_FecCad,lot.F_Ubica;");
                        rset = con.consulta("SELECT m.F_ProMov AS clave, l.F_ClaLot, l.F_FecCad, m.F_UbiMov, (SUM(m.F_CantMov*m.F_SigMov)) AS existencia,IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal, l.F_IdLote FROM tb_movinv AS m INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = m.F_UbiMov INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot and m.F_UbiMov = l.F_Ubica and m.F_ProMov = l.F_ClaPro WHERE m.F_ProMov = '" + request.getParameter("Clave") + "'  GROUP BY m.F_LotMov, m.F_UbiMov HAVING existencia <> 0;");
                     
                        //rset = con.consulta("select l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Ubica, l.F_ExiLot, IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal FROM tb_lote l INNER JOIN tb_ubica u ON l.F_ClaPro = '" + request.getParameter("Clave") + "' AND l.F_ExiLot != 0 AND u.F_ClaUbi = l.F_Ubica " + ANDP + ";");
                       // } else if (Btn.equals("Clave") && !fechaFinal.equals("")) {
                      //      rset = con.consulta("SELECT movi.F_ProMov AS clave, l.F_ClaLot AS lote, l.F_FecCad AS caducidad, movi.F_UbiMov, movi.CantMov, IF( u.es_temporal = 1, 'SI', 'NO') AS temporal FROM ( SELECT M.F_ProMov, M.F_LotMov, M.F_UbiMov, SUM( F_CantMov * F_SigMov ) AS CantMov FROM tb_movinv M WHERE M.F_ProMov = '" + request.getParameter("Clave") + "' AND M.F_FecMov <= '" + fechaFinal + "' GROUP BY M.F_ProMov, M.F_LotMov, M.F_UbiMov HAVING CantMov > 0 ) movi INNER JOIN ( SELECT F_ClaLot, F_FecCad, F_FolLot FROM tb_lote WHERE F_ClaPro = '" + request.getParameter("Clave") + "' GROUP BY F_FolLot ) l ON movi.F_LotMov = l.F_FolLot INNER JOIN tb_ubica u ON u.F_ClaUbi = movi.F_UbiMov");
                    //    } else if (Btn.equals("") && !fechaFinal.equals("")) {
                     //       rset = con.consulta("SELECT m.F_ProMov, lote.F_FolLot, lote.F_FecCad, m.F_UbiMov, SUM( m.F_CantMov * m.F_SigMov) AS existencia, IF( u.es_temporal = 1, 'SI', 'NO' ) AS temporal FROM tb_movinv m INNER JOIN ( SELECT F_FolLot, F_ClaLot, F_FecCad, F_Ubica FROM tb_lote WHERE F_ClaPro = '" + request.getParameter("Clave") + "' AND F_ClaLot = '" + request.getParameter("Lote") + "' AND F_FecCad = '" + request.getParameter("Cadu") + "' GROUP BY F_FolLot ) lote ON m.F_ProMov = '" + request.getParameter("Clave") + "' AND M.F_FecMov <= '" + fechaFinal + "' AND lote.F_FolLot = m.F_LotMov INNER JOIN tb_ubica u ON u.F_ClaUbi = m.F_UbiMov GROUP BY m.F_ProMov, m.F_LotMov, m.F_UbiMov HAVING existencia > 0;");
                       } else {
                         //  rset = con.consulta("SELECT l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Ubica, l.F_ExiLot, IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal FROM tb_lote l INNER JOIN tb_ubica u ON l.F_ClaPro = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' AND l.F_FecCad = '" + request.getParameter("Cadu") + "' and l.F_Origen = '"+request.getParameter("origen")+"' AND l.F_ExiLot !=0 AND u.F_ClaUbi = l.F_Ubica " + ANDP + ";");
                     // rset = con.consulta("SELECT m.F_ProMov,lot.F_ClaLot, lot.F_FecCad, lot.F_Ubica, SUM(m.F_CantMov*m.F_SigMov), lot.esTemporal ,lot.apartado FROM tb_movinv AS m INNER JOIN (SELECT l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Ubica, l.F_ExiLot, IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal, l.F_FolLot,IFNULL(sum(a.F_Cant),0) as apartado FROM tb_lote l  LEFT JOIN tb_apartado AS a ON l.F_IdLote =a.F_IdLote AND a.F_Status = 1 INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = l.F_Ubica where l.F_ClaPro = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' AND l.F_FecCad = '" + request.getParameter("Cadu") + "' and l.F_Origen = '"+request.getParameter("origen")+"' AND l.F_ExiLot <> 0  " + ANDP +" GROUP BY l.F_IdLote) as lot on lot.F_ClaPro = m.F_ProMov and lot.F_FolLot = m.F_LotMov AND lot.F_Ubica = m.F_UbiMov WHERE m.F_ProMov = '" + request.getParameter("Clave") + "' GROUP BY lot.F_ClaLot,lot.F_FecCad,lot.F_Ubica,m.F_LotMov ORDER BY lot.F_ClaLot,lot.F_FecCad,lot.F_Ubica;");
                            rset = con.consulta("SELECT m.F_ProMov AS clave, l.F_ClaLot, l.F_FecCad, m.F_UbiMov, (SUM(m.F_CantMov*m.F_SigMov)) AS existencia,IF( u.es_temporal = 1, 'SI', 'NO') AS esTemporal, l.F_IdLote FROM tb_movinv AS m INNER JOIN tb_ubica AS u ON  u.F_ClaUbi = m.F_UbiMov INNER JOIN tb_lote l ON m.F_LotMov = l.F_FolLot and m.F_UbiMov = l.F_Ubica and m.F_ProMov = l.F_ClaPro and l.F_ClaPro = '" + request.getParameter("Clave") + "' and l.F_ClaLot ='" + request.getParameter("Lote") + "' AND l.F_FecCad = '" + request.getParameter("Cadu") + "' and l.F_Origen = '"+request.getParameter("origen")+"' AND l.F_ExiLot <> 0  " + ANDP +"  WHERE m.F_ProMov = '" + request.getParameter("Clave") + "'  GROUP BY m.F_LotMov, m.F_UbiMov HAVING existencia <> 0;");
                   }

                    while (rset.next()) {
                        rsetApartado = con.consulta("SELECT SUM(a.F_Cant) as apartado from tb_apartado a where a.F_IdLote = "+ rset.getString(7) +"  and a.F_Status = 1 GROUP BY a.F_IdLote;");
                    int apart = 0;
                    
            %>
            <tr>
                <td class="text-center" style="mso-number-format:'@';"><%=rset.getString(1)%></td>
                <td class="text-center" style="mso-number-format:'@';"><%=rset.getString(2)%></td>
                <td class="text-center"><%=rset.getString(3)%></td>
                <td class="text-center"><%=rset.getString(4)%></td>
                <td class="text-center"><%=rset.getString(5)%></td>
               <% 
                   int banap = 0 ;
                   while (rsetApartado.next()) { 
               banap = banap + 1 ;
                   if(rsetApartado.getString(1) != null && !rsetApartado.wasNull()){
                         apart = Integer.parseInt(rsetApartado.getString(1));
                   }else{
                       apart = 0;
                   }
                  %>
                <td class="text-center"><%= apart %></td>
           <% }
            if(banap == 0){ %>
                <td class="text-center">0</td>
            <% } %>
               <td class="text-center"><%=(Integer.parseInt(rset.getString(5)) -  apart) %></td>
                <td class="text-center"><%=rset.getString(6)%></td>
            </tr>
            <%
                    }

                } catch (Exception ex) {
                    Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (Exception ex) {
                        Logger.getLogger("gnrKardexClaveReload_1.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            %>
        </table>
        <br/>

        <h4>Ingresos y Egresos</h4>
        <table border="1">
            <thead> 
                <tr>
                    <th>No. Mov</th>
                    <th>Usuario</th>
                    <th>Usuario que captura</th>
                    <th>Usuario que libera</th>
                    <th>Orden Compra</th>
                    <th>Remisión</th>
                    <th>Proveedor</th>
                    <th>Folio o documento</th>
                    <th>Folio Referencia</th>
                    <th>Punto de Entrega</th>
                    <th>Concepto</th>
                    <th>Clave</th>
                    <th>Lote</th>
                    <th>Caducidad</th>
                    <th>Cantidad</th>
                    <th>Ubicacion</th>
                    <th>Origen</th>
                    <th>Proyecto</th>
                    <th>Fecha Aplicacion</th>
                    <th>Fecha Entrega</th>
                    <th>Hora</th>   
                    <th>Orden de suministro</th>   
                    <th>Observaciones</th>   
                </tr>
            </thead>
            <tbody>
                <%
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();
                            PreparedStatement ps = connection.prepareStatement(query_kardex)) {
                        if (banQuery == 0) {
                            ps.setString(1, request.getParameter("Clave"));
                            ps.setString(2, request.getParameter("Clave"));
                            ps.setString(3, request.getParameter("Clave"));
                            ps.setString(4, request.getParameter("Clave"));
                            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                                ps.setString(5, fechaInicial);
                                ps.setString(6, fechaFinal);
                            }
                        } else {
                            ps.setString(1, request.getParameter("Lote"));
                            ps.setString(2, request.getParameter("Cadu"));
                            ps.setInt(3, Integer.parseInt(origen));
                            ps.setString(4, request.getParameter("Clave"));
                            ps.setString(5, request.getParameter("Clave"));
                            ps.setString(6, request.getParameter("Clave"));
                            ps.setString(7, request.getParameter("Clave"));
                            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                                ps.setString(8, fechaInicial);
                                ps.setString(9, fechaFinal);
                            }
                        }

                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {


                %>
                <tr>
                    <td><%=rs.getInt("noMov")%></td>
                    <td><%=rs.getString("usuario")%></td>
                    <td><%=rs.getString("usuariocapt")%></td>
                    <td><%=rs.getString("usuariolib")%></td>
                    <td><%=rs.getString("ori")%></td>
                    <td><%=rs.getString("remision")%></td>
                    <td><%=rs.getString("proveedor")%></td>
                    <td><%=rs.getString("folioSalida")%></td>
                    <td><%=rs.getString("folioReferencia")%></td>
                    <td><%=rs.getString("puntoEntrega")%></td>
                    <td><%=rs.getString("concepto")%></td>
                    <td style="mso-number-format:'@';"><%=rs.getString("clave")%></td>
                    <td style="mso-number-format:'@';"><%=rs.getString("lote")%></td>
                    <td><%=rs.getString("caducidad")%></td>
                    <td><%=formatter.format(rs.getInt("cantidadMovimiento"))%></td>
                    <td><%=rs.getString("ubicacion")%></td>
                    <td><%=rs.getString("origen")%></td>
                    <td><%=rs.getString("proyecto")%></td>
                    <td><%=rs.getString("fechaMovimiento")%></td>
                    <td><%=rs.getString("fechaEnt")%></td>
                    <td><%=rs.getString("hora")%></td>
                    <td><%=rs.getString("ordSuministro")%></td>
                    <td><%=rs.getString("comentarios")%></td>
                </tr>
                <%
                            }
                        }
                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                    }
                %>
            </tbody>
        </table>

        <br/>
  <% if (ingreso.equals("normal")) {%>
  
        <h4>Redistribución en Almacén</h4>
        <table border="1">
            <thead> 
                <tr>
                    <th>No. Mov</th>
                    <th>Usuario</th>
                    <th>Documento</th>
                    <th>Remisión</th>
                    <th>Proveedor</th>
                    <th>Folio Salida</th>
                    <th>Folio Referencia</th>
                    <th>Punto de Entrega</th>
                    <th>Concepto</th>
                    <th>Clave</th>
                    <th>Lote</th>
                    <th>Caducidad</th>
                    <th>Cantidad</th>
                    <th>Ubicacion</th>
                    <th>Origen</th>
                    <th>Proyecto</th>
                    <th>Fecha Aplicacion</th>
                    <th>Fecha Entrega</th>
                    <th>Hora</th>
                    <th>Observaciones</th>
                </tr>
            </thead>
            <tbody>
                <%
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();
                            PreparedStatement ps = connection.prepareStatement(query_reubicacion)) {
                        if (banQuery == 0) {
                            ps.setString(1, request.getParameter("Clave"));
                            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                                ps.setString(2, fechaInicial);
                                ps.setString(3, fechaFinal);
                            }

                        } else {
                            ps.setString(1, request.getParameter("Clave"));
                            ps.setString(2, request.getParameter("Lote"));
                            ps.setString(3, request.getParameter("Cadu"));
                            ps.setInt(4, Integer.parseInt(origen));
                            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                                ps.setString(5, fechaInicial);
                                ps.setString(6, fechaFinal);
                            }

                        }

                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                %>
                <tr>
                    <td><%=rs.getInt("idMovimiento")%></td>
                    <td><%=rs.getString("usuario")%></td>
                    <td><%=rs.getString("documento")%></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td><%=rs.getString("concepto")%></td>
                    <td style="mso-number-format:'@';"><%=rs.getString("clave")%></td>
                    <td style="mso-number-format:'@';"><%=rs.getString("lote")%></td>
                    <td><%=rs.getString("caducidad")%></td>
                    <td><%=formatter.format(rs.getInt("cantidad"))%></td>
                    <td><%=rs.getString("ubicacion")%></td>
                    <td><%=rs.getString("origen")%></td>
                    <td><%=rs.getString("descProyecto")%></td>
                    <td><%=rs.getString("fechaMovimiento")%></td>
                    <td></td>
                    <td><%=rs.getString("hora")%></td>
                    <td><%=rs.getString("comentarios")%></td>
                </tr>
                <%
                            }
                        }
                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger("gnrKardexClaveReload.jsp").log(Level.SEVERE, ex.getMessage(), ex);
                    }
                %>
            </tbody>
        </table>
            <% } %>
    </body>
</html>
