/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Consultas.model.RemisionCostosView;
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
public class CostosRemisiones extends HttpServlet {

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
        List<RemisionCostosView> listaremisiones = new ArrayList<RemisionCostosView>();
        List<Proyectos> Listaproyecto = new ArrayList<Proyectos>();
        RemisionCostosView remision;
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

            ConsultaProyecto = "SELECT * FROM tb_proyectos;";
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
                if (tipo.equals("8")) {
                    Consulta = "SELECT f.F_ClaDoc, f.F_ClaCli, u.F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, IFNULL(CRL.CantSurCRL, 0) AS CantSurCRL, IFNULL(CR.CantSurCR, 0) AS CantSurCR, F_Obs FROM tb_factura f LEFT JOIN ( SELECT F_ClaCli, F_ClaDoc, SUM(F_CantSur) AS CantSurCR FROM tb_factura WHERE F_Ubicacion LIKE '%CROSS%' AND F_Proyecto = 2 AND F_StsFact = 'A' GROUP BY F_ClaDoc ) AS CR ON f.F_ClaDoc = CR.F_ClaDoc LEFT JOIN ( SELECT F_ClaCli, F_ClaDoc, SUM(F_CantSur) AS CantSurCRL FROM tb_factura WHERE F_Ubicacion NOT LIKE '%CROSS%' AND F_Proyecto = 2 AND F_StsFact = 'A' GROUP BY F_ClaDoc ) AS CRL ON f.F_ClaDoc = CRL.F_ClaDoc INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_Proyecto = 2 AND f.F_StsFact = 'A' AND F_FecEnt = CURDATE() GROUP BY f.F_ClaDoc, f.F_ClaCli, f.F_StsFact, f.F_Proyecto HAVING CantSurCRL = 0 AND CantSurCR > 0 ORDER BY f.F_ClaDoc + 0;";
                } else {
                    Consulta = "( SELECT f.F_ClaDoc, f.F_ClaCli, CONCAT( u.F_NomCli, ' - ', IFNULL(CONT.CONTROL, 'NORMAL')) AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '1' AS BAN FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id LEFT JOIN ( SELECT FTOTAL.F_ClaDoc, ( COUNT(*) - IFNULL(CONTAR.TOTAL2, 0)) AS DIF, CASE WHEN ( COUNT(*) - IFNULL(CONTAR.TOTAL2, 0)) = 0 THEN 'CONTROLADO' ELSE 'NORMAL' END AS CONTROL FROM tb_factura AS FTOTAL LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) TOTAL2 FROM tb_factura WHERE F_Ubicacion = 'CONTROLADO' GROUP BY F_ClaDoc ) AS CONTAR ON FTOTAL.F_ClaDoc = CONTAR.F_ClaDoc GROUP BY FTOTAL.F_ClaDoc ) AS CONT ON f.F_ClaDoc = CONT.F_ClaDoc WHERE F_FecEnt = CURDATE() AND F_CantSur > 0 GROUP BY f.F_ClaDoc, f.F_ClaCli, F_StsFact, f.F_Proyecto ORDER BY f.F_ClaDoc + 0 ) UNION ( SELECT F_ClaDoc, f.F_ClaCli, CONCAT(u.F_NomCli, ' - CERO') AS F_NomCli, F_StsFact, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, o.F_Req, u.F_Tipo, f.F_Proyecto, p.F_DesProy, ps.F_DesProy AS F_DesProyFact, '0' AS BAN FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON u.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE F_FecEnt = CURDATE() AND f.F_CantSur = 0 GROUP BY f.F_ClaDoc, f.F_ClaCli, F_StsFact, f.F_Proyecto ORDER BY F.F_ClaDoc + 0 );";
                }
                ps = con.getConn().prepareStatement(Consulta);
               
                request.setAttribute("listaRemision", listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_costos.jsp").forward(request, response);
            }
            if (Accion.equals("mostrar")) {
                int ProyectoSelect = 0;
                String AND = "";
                sesion.setAttribute("fecha_ini", FechaIni);
                sesion.setAttribute("fecha_fin", FechaFin);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                ProyectoSelect = Integer.parseInt(Proyecto);

                if (ProyectoSelect == 0) {
                    AND = "";
                } else {
                    AND = " AND f.F_Proyecto = '" + ProyectoSelect + "'";
                }

                if ((FechaIni != "") && (FechaFin != "") && (Folio1 != "") && (Folio2 != "")) {
                    Consulta = "SELECT F.F_proyecto, F.F_StsFact,DATE_FORMAT(F.F_FecEnt, '%d/%m/%Y') as fecEnt, p.F_DesProy, F.F_ClaDoc, u.F_NomCli, M.F_Costo, SUM(F.F_CantSur) as cantSur, COUNT(F.F_ClaPro) as Claves, M.F_Costo * F.F_CantReq as SUBTOTAL, ROUND(SUM((M.F_Costo * F.F_CantReq) /1000),2) as TOTAL FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_uniatn u ON F.F_ClaCli = u.F_ClaCli INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id WHERE F_FecEnt between ? AND ? AND F_ClaDoc between ? AND ? AND F_StsFact = 'A'  group by F_ClaDoc;";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, FechaIni);
                    ps.setString(2, FechaFin);
                    ps.setString(3, Folio1);
                    ps.setString(4, Folio2);
                }

                if ((FechaIni != "") && (FechaFin != "")) {
                    Consulta = "SELECT F.F_proyecto, F.F_StsFact,DATE_FORMAT(F.F_FecEnt, '%d/%m/%Y') as fecEnt, p.F_DesProy, F.F_ClaDoc, u.F_NomCli, M.F_Costo, SUM(F.F_CantSur) as cantSur, COUNT(F.F_ClaPro) as Claves, M.F_Costo * F.F_CantReq as SUBTOTAL, ROUND(SUM((M.F_Costo * F.F_CantReq) /1000),2) as TOTAL FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_uniatn u ON F.F_ClaCli = u.F_ClaCli INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id WHERE F_FecEnt between ? AND ? AND F_StsFact = 'A' group by F_ClaDoc;";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, FechaIni);
                    ps.setString(2, FechaFin);
                    
                }

                if ((Folio1 != "") && (Folio2 != "")) {
                    Consulta = "SELECT F.F_proyecto, F.F_StsFact,DATE_FORMAT(F.F_FecEnt, '%d/%m/%Y') as fecEnt, p.F_DesProy, F.F_ClaDoc, u.F_NomCli, M.F_Costo, SUM(F.F_CantSur) as cantSur, COUNT(F.F_ClaPro) as Claves, M.F_Costo * F.F_CantReq as SUBTOTAL, ROUND(SUM((M.F_Costo * F.F_CantReq) /1000),2) as TOTAL FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_uniatn u ON F.F_ClaCli = u.F_ClaCli INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id WHERE F_ClaDoc between ? AND ? AND F_StsFact = 'A' group by F_ClaDoc;";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, Folio1);
                    ps.setString(2, Folio2);
                }

                System.out.println(ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    remision = new RemisionCostosView(rs);
                    listaremisiones.add(remision);
                }
                request.setAttribute("listaRemision", listaremisiones);
                request.setAttribute("listaProyecto", Listaproyecto);
                request.getRequestDispatcher("/reimp_costos.jsp").forward(request, response);
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
                request.getRequestDispatcher("FacturacionManual?accion=ReenviarFactura&fol_gnkl=" + Folio + "").forward(request, response);
            }

            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(CostosRemisiones.class.getName()).log(Level.SEVERE, null, e);
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
