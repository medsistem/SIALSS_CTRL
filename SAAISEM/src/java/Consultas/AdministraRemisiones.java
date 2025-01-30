/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.Proyectos;
import modelos.Remisiones;


/**
 * Administración de los folios generados
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class AdministraRemisiones extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateador2 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSistema = formateador.format(fechaActual);
        String fechaSistema2 = formateador2.format(fechaActual);
        PrintWriter out = response.getWriter();
        ConectionDB con = new ConectionDB();
        HttpSession sesion = request.getSession(true);
        List<Remisiones> Listaremisiones = new ArrayList<Remisiones>();
        List<Proyectos> Listaproyecto = new ArrayList<Proyectos>();
        Remisiones remisiones;
        Proyectos proyecto;
        PreparedStatement ps = null;
        PreparedStatement psProyecto = null;
       // PreparedCallStatement psd = null;
        ResultSet rs = null;
        ResultSet rsProyecto = null;
        String Consulta = "", Condicion = "", Sts = "", ConsultaProyecto = "";
        String TipoFact = "", usua = "", tipo = "";
        try {
            usua = (String) sesion.getAttribute("nombre");
            String Accion = request.getParameter("Accion");
            String FechaIni = request.getParameter("fecha_ini");
            String FechaFin = request.getParameter("fecha_fin");
            String Folio1 = request.getParameter("folio1");
            String Folio2 = request.getParameter("folio2");
            String Proyecto = request.getParameter("Proyecto");
            tipo = (String) sesion.getAttribute("Tipo");
            con.conectar();

            ConsultaProyecto = "SELECT * FROM tb_proyectos Order By F_DesProy;";
            psProyecto = con.getConn().prepareStatement(ConsultaProyecto);
            rsProyecto = psProyecto.executeQuery();
            while (rsProyecto.next()) {
                proyecto = new Proyectos();
                proyecto.setIdproyecto(rsProyecto.getInt(1));
                proyecto.setDesproyecto(rsProyecto.getString(2));
                Listaproyecto.add(proyecto);
            }

            if (Accion.equals("ListaRemision")) {

                sesion.setAttribute("fecha_ini", fechaSistema2);
                sesion.setAttribute("fecha_fin", fechaSistema2);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                
                Consulta = "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - CONTROLADO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '4' AS BAN,'CONTROLADO' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' and f.F_Ubicacion LIKE '%VACUNA%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - REDFRIA') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN,'REDFRIA' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion RLIKE 'REDFRIA|RF' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - NORMAL') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN,'NORMAL' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN,'CERO' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id AND f.F_CantSur = 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - APE') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '3' AS BAN,'APE' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion LIKE '%APE%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) ORDER BY F_ClaDoc + 0";      
                
                
                ps = con.getConn().prepareStatement(Consulta);
                rs = ps.executeQuery();
                while (rs.next()) {
                    remisiones = new Remisiones();
                    Sts = rs.getString(4);
                    if (Sts.equals("A")) {
                        Date fechaDate1 = formateador.parse(rs.getString(5));
                        Date fechaDate2 = formateador.parse(fechaSistema);
                        if (fechaDate1.before(fechaDate2)) {
                            remisiones.setCancela("NO");
                        } else if (fechaDate2.before(fechaDate1)) {
                            remisiones.setCancela("NO");
                        } else {
                            remisiones.setCancela("SI");
                        }
                        remisiones.setVer("SI");
                    } else {
                        remisiones.setCancela("NO");
                        remisiones.setVer("NO");
                    }
                    remisiones.setFolio(rs.getInt(1));
                    remisiones.setClaveuni(rs.getString(2));
                    remisiones.setUnidad(rs.getString(3));
                    remisiones.setSts(rs.getString(4));
                    remisiones.setFechaa(rs.getString(5));
                    remisiones.setFechae(rs.getString(6));
                    TipoFact = rs.getString(7);
                    if (TipoFact == null) {
                        remisiones.setTipofact("M");
                    } else {
                        remisiones.setTipofact(rs.getString(7));
                    }
                    remisiones.setTipou(rs.getString(8));
                    remisiones.setIdproyecto(rs.getString(9));
                    remisiones.setProyecto(rs.getString(10));
                    remisiones.setProyectfactura(rs.getString(11));
                    remisiones.setBan(rs.getInt(12));
                    remisiones.setUbi(rs.getString(13));
                    remisiones.setUsuario(usua);
                    remisiones.setControlado(remisiones.getUnidad().contains("CONTROLADO"));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("listaRemision", Listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_remisiones.jsp").forward(request, response);
            }
            
            
            
            if (Accion.equals("mostrar")) {
                int ProyectoSelect = 0;
                String AND = "";
                sesion.setAttribute("fecha_ini", FechaIni);
                sesion.setAttribute("fecha_fin", FechaFin);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                ProyectoSelect = Integer.parseInt(Proyecto);
                sesion.setAttribute("proyecto", Proyecto);
                String  Fechapdf = "", foliopdf = "";
                sesion.setAttribute("fecha_ini", FechaIni);
                sesion.setAttribute("fecha_fin", FechaFin);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                ProyectoSelect = Integer.parseInt(Proyecto);
                    
                     if (ProyectoSelect == 0) {
                    AND = " ";
                } else {
                    AND = " AND f.F_Proyecto = '" + ProyectoSelect + "'";
                }

                if ((Folio1 != "") && (Folio2 != "")) {
                    foliopdf = " f.F_ClaDoc BETWEEN ? AND ? ";
                } else if ((FechaIni != "") && (FechaFin != "")) {
                    Fechapdf = " f.F_FecEnt  BETWEEN ? AND ? ";
                } else if ((FechaIni == "") && (FechaFin == "") && (Folio1 == "") && (Folio2 == "")) {
                    foliopdf = "f.F_FecEnt = CURDATE()";
                } else if ((FechaIni != "") && (FechaFin != "") && (Folio1 != "") && (Folio2 != "")) {
                    Fechapdf = " AND f.F_FecEnt  BETWEEN ? AND ? ";
                    foliopdf = " f.F_ClaDoc BETWEEN ? AND ? ";
                }

                    
                  Consulta = "SELECT f.F_ClaDoc, f.F_ClaCli,CASE WHEN (f.F_Ubicacion RLIKE 'ONCOAPE' && F_CantSur > 0 && onc.F_ClaPro is not null ) THEN CONCAT(u.F_NomCli, ' - ONCO-APE')"
                          + "WHEN (f.F_Ubicacion RLIKE 'ONCORF' && F_CantSur > 0 && onc.F_ClaPro is not null ) THEN CONCAT(u.F_NomCli, ' - ONCO-RF') "
                          + "WHEN (f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0 && a.F_ClaPro is null) THEN CONCAT(u.F_NomCli, ' - FONSABI') "
                          + "WHEN (f.F_Ubicacion RLIKE 'RFFO' && F_CantSur > 0) THEN CONCAT(u.F_NomCli, ' - RED-FONSABI') "
                          + "WHEN (f.F_Ubicacion RLIKE 'CTRFO' && F_CantSur > 0) THEN CONCAT(u.F_NomCli, ' - CONTROLADO-FONSABI') "
                          + "WHEN (f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0  && a.F_ClaPro is not null) THEN CONCAT(u.F_NomCli, ' - APE-FONSABI') "
                          + "WHEN (f.F_ClaPro = 9999 && F_CantSur > 0) THEN CONCAT(u.F_NomCli, ' - RECETAS') "
                          + "WHEN (f.F_ClaPro = 9995 && F_CantSur > 0) THEN CONCAT(u.F_NomCli, ' - COLECTIVO MED') "
                          + "WHEN (f.F_ClaPro = 9996 && F_CantSur > 0) THEN CONCAT(u.F_NomCli, ' - COLECTIVO MC') "
                          + "WHEN (f.F_Ubicacion RLIKE 'APE|(APE|ES)(1|2|3|PAL|URGENTE|CDist)' && F_CantSur > 0 && onc.F_ClaPro is null) THEN CONCAT(u.F_NomCli, ' - APE') "
                          + "WHEN (f.F_Ubicacion RLIKE 'REDFRIA|RF' && F_CantSur > 0 && onc.F_ClaPro is null) THEN CONCAT(u.F_NomCli, ' - REDFRIA') "
                          + "WHEN (f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' && F_CantSur > 0 ) THEN CONCAT(u.F_NomCli, ' - CONTROLADO') "
                          + "WHEN (F_CantSur > 0 &&  a.F_ClaPro is null ) THEN CONCAT(u.F_NomCli, ' - SECO') "
                          + "ELSE CONCAT(u.F_NomCli, ' - CERO')  END AS F_NomCli,F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact,"
                          + "CASE WHEN (f.F_Ubicacion RLIKE 'ONCOAPE' && F_CantSur > 0) THEN '7' "
                          + "WHEN (f.F_Ubicacion RLIKE 'ONCORF' && F_CantSur > 0) THEN '8' "
                          + "WHEN (f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0 &&  a.F_ClaPro is null) THEN '6' "
                          + "WHEN (f.F_Ubicacion RLIKE 'APE|(APE|ES)(1|2|3|PAL|URGENTE|CDist)' && F_CantSur > 0) THEN '3' "
                          + "WHEN (f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0  &&  a.F_ClaPro is not null) THEN '3' "
                          + "WHEN (f.F_Ubicacion RLIKE 'RFFO|REDFRIA' && F_CantSur > 0) THEN '2' "
                          + "WHEN (f.F_Ubicacion Rlike 'CTRFO|CONTROLADO' && F_CantSur > 0) THEN '4' "
                          + "WHEN (F_CantSur > 0) THEN '1' ELSE '0' END AS BAN,f.F_Ubicacion as ubi,CASE WHEN f.F_Ubicacion IN ('ONCOAPE','ONCORF') && F_CantSur > 0 THEN  '7' WHEN f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0 && a.F_ClaPro is null THEN  '6' WHEN f.F_Ubicacion RLIKE 'RECFO|FACFO' && F_CantSur > 0 && a.F_ClaPro is not null THEN  '6' WHEN f.F_Ubicacion RLIKE 'RFFO' && F_CantSur > 0 THEN '6' WHEN f.F_Ubicacion RLIKE 'CTRFO' && F_CantSur > 0 THEN  '6' WHEN f.F_ClaPro IN (9999,9995,9996) && F_CantSur > 0 THEN  '1' WHEN f.F_Ubicacion Rlike 'CONTROLADO|CTRFO' && F_CantSur > 0 THEN   '4' WHEN f.F_Ubicacion RLIKE 'APE|(APE|ES)(1|2|3|PAL|URGENTE|CDist)' && F_CantSur > 0 && onc.F_ClaPro is null THEN '3' WHEN f.F_Ubicacion RLIKE 'REDFRIA|RFFO' && F_CantSur > 0 && onc.F_ClaPro is null THEN '2' WHEN F_CantSur > 0 THEN '1' ELSE '0' END AS TipBan FROM tb_factura f LEFT JOIN tb_controlados ctr on f.F_ClaPro = ctr.F_ClaPro AND F_Ubicacion Rlike 'CTRFO|CONTROLADO' LEFT JOIN tb_ape a ON  f.F_ClaPro = a.F_ClaPro LEFT JOIN tb_redfria rf ON f.F_ClaPro =  rf.F_ClaPro LEFT JOIN tb_onco onc ON  f.F_ClaPro = onc.F_ClaPro AND f.F_Ubicacion IN ('ONCOAPE','ONCORF')  INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE " + foliopdf + Fechapdf + AND + " GROUP BY F_NomCli,BAN, f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0, F_NomCli;";                            
                    
                    if ((Folio1 != "") && (Folio2 != "")) {
                    
                            System.out.println("solo folio");
                              Consulta = "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'NORMAL') AS F_NomCli, F_StsFact,  DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_ClaDoc BETWEEN ? AND ? " + AND + " AND f.F_CantSur > 0 AND f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + " (SELECT  f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'CONTROLADO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '4' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_CantSur > 0 " + AND + "  AND f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' OR f.F_Ubicacion LIKE '%VACUNA%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto HAVING f.F_ClaDoc BETWEEN ? AND ? ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + " (SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'REDFRIA') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_ClaDoc BETWEEN ? AND ? " + AND + " AND f.F_CantSur > 0 AND f.F_Ubicacion RLIKE 'REDFRIA|RF' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + " (SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'APE') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '3' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_ClaDoc BETWEEN ? AND ? " + AND + " AND f.F_CantSur > 0 AND f.F_Ubicacion LIKE '%APE%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + " (SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_ClaDoc BETWEEN ? AND ? " + AND + " AND f.F_CantSur = 0  GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) ORDER BY F_ClaDoc + 0;";

                        
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Folio1);
                        ps.setString(2, Folio2);
                        ps.setString(3, Folio1);
                        ps.setString(4, Folio2);
                        ps.setString(5, Folio1);
                        ps.setString(6, Folio2);
                         ps.setString(7, Folio1);
                        ps.setString(8, Folio2);
                        ps.setString(9, Folio1);
                        ps.setString(10, Folio2);
                       System.out.println("CON FOLIO "+ps); 
                   
                    }else  if ((FechaIni != "") && (FechaFin != "")) {
                        
                            System.out.println("solo fecha");

                            Consulta = "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'NORMAL') AS F_NomCli, F_StsFact,  DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN,f.F_Ubicacion AS Ubi, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_FecEnt BETWEEN ? AND ?   "+AND+"  AND f.F_CantSur > 0 AND f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + "(SELECT  f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'CONTROLADO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '4' AS BAN,f.F_Ubicacion AS Ubi, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' OR f.F_Ubicacion LIKE '%VACUNA%' "+AND+"  GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto HAVING f.F_FecEnt BETWEEN ? AND ?  ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'REDFRIA') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN,f.F_Ubicacion AS Ubi, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_FecEnt BETWEEN ? AND ?   "+AND+"  AND f.F_CantSur > 0 AND f.F_Ubicacion RLIKE 'REDFRIA|RF' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION\n"
                                    + "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'APE') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '3' AS BAN,f.F_Ubicacion AS Ubi, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_FecEnt BETWEEN ? AND ?   "+AND+"  AND f.F_CantSur > 0 AND f.F_Ubicacion LIKE '%APE%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n"
                                    + "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ', 'CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN,f.F_Ubicacion AS Ubi, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_FecEnt BETWEEN ? AND ?   "+AND+"  AND f.F_CantSur = 0 GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) ORDER BY F_ClaDoc + 0;";
                        
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, FechaIni);
                        ps.setString(2, FechaFin);
                        ps.setString(3, FechaIni);
                        ps.setString(4, FechaFin);
                        ps.setString(5, FechaIni);
                        ps.setString(6, FechaFin);
                        ps.setString(7, FechaIni);
                        ps.setString(8, FechaFin);
                        ps.setString(9, FechaIni);
                        ps.setString(10, FechaFin);
                    System.out.println("CON FECHA "+ps);
                    }else
                    if ((FechaIni != "") && (FechaFin != "") && (Folio1 != "") && (Folio2 != "")) {
                        
                        System.out.println("todos");
                        
                        
                            Consulta = "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ',IFNULL(CONT.CONTROL,'CONTROLADO') ) AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '4' AS BAN,f.F_Ubicacion AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id LEFT JOIN ( SELECT FTOTAL.F_ClaDoc, ( COUNT(*) - IFNULL(CONTAR.TOTAL2, 0)) AS DIF, CASE WHEN (IFNULL(CONTAR.TOTAL2, 0)) > 0 THEN 'CONTROLADO' ELSE NULL END AS CONTROL FROM tb_factura AS FTOTAL LEFT JOIN ( SELECT ft.F_ClaDoc, COUNT(*) TOTAL2 FROM tb_factura as ft WHERE F_Ubicacion RLIKE 'CONTROLADO|CTRL' AND F_Ubicacion like '%VACUNA%' GROUP BY ft.F_ClaDoc ) AS CONTAR ON FTOTAL.F_ClaDoc = CONTAR.F_ClaDoc GROUP BY FTOTAL.F_ClaDoc ) AS CONT ON f.F_ClaDoc=CONT.F_ClaDoc WHERE F_FecEnt BETWEEN ? AND ?  AND f.F_ClaDoc BETWEEN ?  AND ?   "+AND+"  AND f.F_CantSur > 0 AND CONT.CONTROL IS NOT NULL GROUP BY f.F_ClaDoc,f.F_ClaCli,F_StsFact,f.F_Proyecto ORDER BY f.F_ClaDoc+0) UNION \n"
                                    + "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - ',IFNULL(CONT2.CONTROL,'NORMAL') ) AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN,f.F_Ubicacion AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id LEFT JOIN ( SELECT FTOTAL.F_ClaDoc, ( COUNT(*) - IFNULL(CONTAR.TOTAL2, 0)) AS DIF, CASE WHEN (IFNULL(CONTAR.TOTAL2, 0)) > 0 THEN 'NORMAL' ELSE NULL END AS CONTROL FROM tb_factura AS FTOTAL LEFT JOIN (SELECT fn.F_ClaDoc, COUNT(*) TOTAL2 FROM tb_factura as fn WHERE fn.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%'  GROUP BY fn.F_ClaDoc ) AS CONTAR ON FTOTAL.F_ClaDoc = CONTAR.F_ClaDoc GROUP BY FTOTAL.F_ClaDoc ) AS CONT2 ON f.F_ClaDoc=CONT2.F_ClaDoc WHERE f.F_FecEnt BETWEEN ?  AND ?  AND f.F_ClaDoc BETWEEN ?  AND ?   "+AND+"  AND f.F_CantSur > 0 AND CONT2.CONTROL IS NOT NULL GROUP BY f.F_ClaDoc,f.F_ClaCli,F_StsFact,f.F_Proyecto ORDER BY f.F_ClaDoc+0) UNION \n"
                                    + "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli,' - CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN,f.F_Ubicacion AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE F_FecEnt BETWEEN ?  AND ? AND f.F_ClaDoc BETWEEN ?  AND ?   "+AND+"  AND f.F_CantSur = 0 GROUP BY f.F_ClaDoc,f.F_ClaCli,F_StsFact,f.F_Proyecto ORDER BY F.F_ClaDoc+0) UNION  \n"
                                    + "(SELECT REDFRIA.F_ClaDoc, UNI.F_ClaCli, CONCAT(UNI.F_NomCli, '- REDFRIA') as F_NomCli, REDFRIA.F_StsFact,DATE_FORMAT(F_FecApl, '%d/%m/%Y') as F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, UNI.F_Tipo, REDFRIA.F_Proyecto, PRO.F_DesProy,PRO.F_DesProy as F_DesProyFact, '2' as BAN,REDFRIA.F_Ubicacion AS ubi FROM (SELECT ferd.F_FecEnt, ferd.F_FecApl, ferd.F_StsFact, ferd.F_ClaDoc, COUNT(*) TOTAL2, ferd.F_ClaCli, ferd.F_Proyecto,ferd.F_Ubicacion FROM tb_factura as ferd WHERE ferd.F_Ubicacion = '%REDFRIA%' AND ferd.F_CantSur > 0 AND ferd.F_FecEnt BETWEEN ?  AND ? AND ferd.F_ClaDoc BETWEEN ?  AND ? GROUP BY ferd.F_ClaDoc) AS REDFRIA inner JOIN tb_uniatn UNI ON REDFRIA.F_ClaCli = UNI.F_ClaCli INNER JOIN tb_proyectos PRO ON PRO.F_Id = REDFRIA.F_Proyecto LEFT JOIN tb_obserfact o ON REDFRIA.F_ClaDoc = o.F_IdFact) UNION \n"
                                    + "(SELECT APE.F_ClaDoc, UNI.F_ClaCli, CONCAT(UNI.F_NomCli, '- APE') as F_NomCli, APE.F_StsFact,DATE_FORMAT(F_FecApl, '%d/%m/%Y') as F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, UNI.F_Tipo, APE.F_Proyecto, PRO.F_DesProy,PRO.F_DesProy as F_DesProyFact, '3' as BAN,APE.F_Ubicacion AS ubi FROM (SELECT fape.F_FecEnt, fape.F_FecApl, fape.F_StsFact, fape.F_ClaDoc, COUNT(*) TOTAL2, fape.F_ClaCli, fape.F_Proyecto,fape.F_Ubicacion FROM tb_factura AS fape WHERE fape.F_Ubicacion = '%APE%' AND fape.F_CantSur > 0 AND fape.F_FecEnt BETWEEN ?  AND ? AND fape.F_ClaDoc BETWEEN ?  AND ? GROUP BY fape.F_ClaDoc) AS APE inner JOIN tb_uniatn UNI ON APE.F_ClaCli = UNI.F_ClaCli INNER JOIN tb_proyectos PRO ON PRO.F_Id = APE.F_Proyecto LEFT JOIN tb_obserfact o ON APE.F_ClaDoc = o.F_IdFact);";
                        
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, FechaIni);
                        ps.setString(2, FechaFin);
                        ps.setString(3, Folio1);
                        ps.setString(4, Folio2);
                        ps.setString(5, FechaIni);
                        ps.setString(6, FechaFin);
                        ps.setString(7, Folio1);
                        ps.setString(8, Folio2);
                        ps.setString(9, FechaIni);
                        ps.setString(10, FechaFin);
                        ps.setString(11, Folio1);
                        ps.setString(12, Folio2);
                        ps.setString(13, FechaIni);
                        ps.setString(14, FechaFin);
                        ps.setString(15, Folio1);
                        ps.setString(16, Folio2);
                         ps.setString(17, FechaIni);
                        ps.setString(18, FechaFin);
                        ps.setString(19, Folio1);
                        ps.setString(20, Folio2);
                        
                        System.out.println("cON TODO "+ps);
                    }

                   else 
                    if ((FechaIni == "") && (FechaFin == "") && (Folio1 == "") && (Folio2 == "")) {
                        
                        Consulta = "(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - CONTROLADO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '4' AS BAN,'CONTROLADO' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' and f.F_Ubicacion LIKE '%VACUNA%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - REDFRIA') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN,'REDFRIA' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion RLIKE 'REDFRIA|RF' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - NORMAL') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN,'NORMAL' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN,'CERO' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id AND f.F_CantSur = 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION \n" +
"(SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - APE') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt,IF(SUM(f.F_CantReq)>0,'A','M') AS F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '3' AS BAN,'APE' AS ubi FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Ubicacion LIKE '%APE%' AND f.F_CantSur > 0 AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) ORDER BY F_ClaDoc + 0";      
                
                        ps = con.getConn().prepareStatement(Consulta);
                      System.out.println("SIN FECHA NI FOLIO "+ps);
                    }
                    

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        remisiones = new Remisiones();
                        Sts = rs.getString(4);
                        if (Sts.equals("A")) {
                            Date fechaDate1 = formateador.parse(rs.getString(5));
                            Date fechaDate2 = formateador.parse(fechaSistema);
                            if (fechaDate1.before(fechaDate2)) {
                                remisiones.setCancela("NO");
                            } else if (fechaDate2.before(fechaDate1)) {
                                remisiones.setCancela("NO");
                            } else {
                                remisiones.setCancela("SI");
                            }
                            remisiones.setVer("SI");
                        } else {
                            remisiones.setCancela("NO");
                        }
                        remisiones.setFolio(rs.getInt(1));
                        remisiones.setClaveuni(rs.getString(2));
                        remisiones.setUnidad(rs.getString(3));
                        remisiones.setSts(rs.getString(4));
                        remisiones.setFechaa(rs.getString(5));
                        remisiones.setFechae(rs.getString(6));

                        TipoFact = rs.getString(7);
                        if (TipoFact == null) {
                            remisiones.setTipofact("M");
                        } else {
                            remisiones.setTipofact(rs.getString(7));
                        }
                        remisiones.setTipou(rs.getString(8));
                        remisiones.setIdproyecto(rs.getString(9));
                        remisiones.setProyecto(rs.getString(10));
                        remisiones.setProyectfactura(rs.getString(11));
                        remisiones.setBan(rs.getInt(12));
                        remisiones.setUbi(rs.getString(13));
                        remisiones.setUsuario(usua);
                        remisiones.setControlado(remisiones.getUnidad().contains("CONTROLADO"));
                        Listaremisiones.add(remisiones);
                    }
                
                request.setAttribute("proyecto", ProyectoSelect);
                request.setAttribute("listaRemision", Listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_remisiones.jsp").forward(request, response);
            }

            if (Accion.equals("exportar")) {
                request.getRequestDispatcher("ExportarAbasto?accion=exportar&fecha_fin=" + FechaFin + "").forward(request, response);
            }
            if (Accion.equals("exportarDispen")) {
                request.getRequestDispatcher("ExportarAbasto?accion=exportarDispen&fecha_fin=" + FechaFin + "").forward(request, response);
            }

            if (Accion.equals("DevoGlobal")) {
                String Folio = request.getParameter("fol_gnkl");
                request.getRequestDispatcher("DevolucionGlobal?accion=DevoGlobal&fol_gnkl=" + Folio + "").forward(request, response);
            }

            if (Accion.equals("ReenviarFactura")) {
                String Folio = request.getParameter("fol_gnkl");
                String idproyecto = request.getParameter("idproyecto");
                request.getRequestDispatcher("FacturacionManual?accion=ReenviarFactura&fol_gnkl=" + Folio + "&Proyecto=" + idproyecto + "").forward(request, response);
            }

            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(AdministraRemisiones.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
