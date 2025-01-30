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
import javax.servlet.annotation.WebServlet;
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
@WebServlet(name = "AdministraRemisionesProvisionales", urlPatterns = {"/AdministraRemisionesProvisionales"})
public class AdministraRemisionesProvisionales extends HttpServlet {

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
                Consulta = "SELECT folio, f.ClaCli, u.F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(f.fecEnt, '%d/%m/%Y') AS F_FecEnt, '', u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact,'1' As Ban, f.id_folio  FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id WHERE fecEnt = CURDATE() GROUP BY folio, f.claCli, f.status, f.proyecto ORDER BY f.folio + 0;";
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
                    remisiones.setIdFolio(rs.getInt(13));
                    remisiones.setUsuario(usua);
                    remisiones.setControlado(remisiones.getUnidad().contains("CONTROLADO"));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("listaRemision", Listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_remisionesProvisionales.jsp").forward(request, response);
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
                ProyectoSelect = 2;
                if (ProyectoSelect == 0) {
                    AND = " AND f.F_proyecto != '2'";
                } else {
                    AND = " AND f.F_Proyecto = '" + ProyectoSelect + "'";
                }

                //Carga de folios solo de Michoacán
                if (ProyectoSelect == 0 || ProyectoSelect == 2) {
                    AND = " AND f.F_proyecto = '2'";
                    if ((FechaIni != "") && (FechaFin != "") && (Folio1 != "") && (Folio2 != "")) {
                        Consulta = "(SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','NORMAL' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica NOT IN ('CONTROLADO', 'REDFRIA') AND fecEnt between ? AND ? AND folio between ? AND ? group by folio) union (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','CONTROLADO' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'CONTROLADO' AND fecEnt between ? AND ? AND folio between ? AND ? group by folio) UNION (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','REDFRIA' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'REDFRIA' AND fecEnt between ? AND ? AND folio between ? AND ? group by folio) order by folio + 0;";
//                            Consulta = "(SELECT * from v_controlado_mich where Fecha BETWEEN ? AND ? AND F_ClaDoc BETWEEN ? AND ?) union (SELECT * FROM v_normal_mich where Fecha BETWEEN ? AND ? AND F_ClaDoc BETWEEN ? AND ?) UNION (SELECT * FROM v_redfria_mich where Fecha BETWEEN ? AND ? AND F_ClaDoc BETWEEN ? AND ?) UNION (SELECT * FROM v_ceros_mich where Fecha BETWEEN ? AND ? AND F_ClaDoc BETWEEN ? AND ?);";
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
                    }
                    if ((FechaIni != "") && (FechaFin != "")) {
                        Consulta = "(SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','NORMAL' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica NOT IN ('CONTROLADO', 'REDFRIA') AND fecEnt between ? AND ? group by folio) union (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','CONTROLADO' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'CONTROLADO' AND fecEnt between ? AND ? group by folio) UNION (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','REDFRIA' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'REDFRIA' AND fecEnt between ? AND ? group by folio) order by folio + 0;";
//                            Consulta = "(SELECT * from v_controlado_mich where Fecha BETWEEN ? AND ?) union (SELECT * FROM v_normal_mich where Fecha BETWEEN ? AND ?) UNION (SELECT * FROM v_redfria_mich where Fecha BETWEEN ? AND ? ) UNION (SELECT * FROM v_ceros_mich where Fecha BETWEEN ? AND ?);";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, FechaIni);
                        ps.setString(2, FechaFin);
                        ps.setString(3, FechaIni);
                        ps.setString(4, FechaFin);
                        ps.setString(5, FechaIni);
                        ps.setString(6, FechaFin);
                    }

                    if ((Folio1 != "") && (Folio2 != "")) {
                            Consulta = "(SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','NORMAL' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica NOT IN ('CONTROLADO', 'REDFRIA') AND folio between ? AND ? group by folio) union (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','CONTROLADO' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'CONTROLADO' AND folio between ? AND ? group by folio) UNION (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','REDFRIA' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'REDFRIA' AND folio between ? AND ? group by folio) order by folio + 0;";
//                            Consulta = "(SELECT * from v_controlado_mich where F_ClaDoc BETWEEN ? AND ?) union (SELECT * FROM v_normal_mich where F_ClaDoc BETWEEN ? AND ?) UNION (SELECT * FROM v_redfria_mich where AND F_ClaDoc BETWEEN ? AND ?) UNION (SELECT * FROM v_ceros_mich where  F_ClaDoc BETWEEN ? AND ?);";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Folio1);
                        ps.setString(2, Folio2);
                        ps.setString(3, Folio1);
                        ps.setString(4, Folio2);
                        ps.setString(5, Folio1);
                        ps.setString(6, Folio2);
                    }

                    if ((FechaIni == "") && (FechaFin == "") && (Folio1 == "") && (Folio2 == "")) {
                        Consulta = "(SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','NORMAL' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica NOT IN ('CONTROLADO', 'REDFRIA') group by folio) union (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','CONTROLADO' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'CONTROLADO' group by folio) UNION (SELECT f.folio, f.claCli, CONCAT(u.F_NomCli, ' - ','REDFRIA' ) AS F_NomCli, CASE when f.status = 1 THEN 'A' when f.status = 2 THEN 'C' END as F_StsFact, DATE_FORMAT(f.fecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(fecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '2' AS BAN, f.id_folio FROM folio_provisional f INNER JOIN tb_uniatn u ON f.claCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.folio = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.proyecto = ps.F_Id INNER JOIN folio_provisional_detail d on d.id_folio = f.id_folio inner join tb_lote l on l.F_IdLote = d.id_lote where l.F_Ubica = 'REDFRIA' group by folio) order by folio + 0;";
                        ps = con.getConn().prepareStatement(Consulta);
                    }
                    System.out.println(ps);
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
                        remisiones.setIdFolio(rs.getInt(13));
                        remisiones.setUsuario(usua);
                        remisiones.setControlado(remisiones.getUnidad().contains("CONTROLADO"));
                        Listaremisiones.add(remisiones);
                    }
                }
                request.setAttribute("proyecto", ProyectoSelect);
                request.setAttribute("listaRemision", Listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_remisionesProvisionales.jsp").forward(request, response);
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
            Logger.getLogger(AdministraRemisionesProvisionales.class.getName()).log(Level.SEVERE, null, e);
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
