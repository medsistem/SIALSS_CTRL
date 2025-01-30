/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

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
 *
 * @author Anibal MEDALFA
 */
@WebServlet(name = "AdministraRequrimiento", urlPatterns = {"/AdministraRequrimiento"})
public class AdministraRequrimiento extends HttpServlet {

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
        String TipoFact = "", usua = "";
        try {
            usua = (String) sesion.getAttribute("nombre");
            String Accion = request.getParameter("Accion");
            String FechaIni = request.getParameter("fecha_ini");
            String FechaFin = request.getParameter("fecha_fin");
            String Folio1 = request.getParameter("folio1");
            String Folio2 = request.getParameter("folio2");
            String Proyecto = request.getParameter("Proyecto");
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
                
                Consulta = "SELECT C.F_ClaUni, U.F_NomCli, DATE_FORMAT(C.F_FecCarg, '%d/%m/%Y') AS F_FecCarg, FORMAT(SUM(C.F_Solicitado), 0) FROM tb_cargareqcompra C INNER JOIN tb_uniatn U ON C.F_ClaUni = U.F_ClaCli WHERE F_ClaCli NOT IN ( SELECT GROUP_CONCAT(F_ClaUni) FROM tb_cargareqcompra WHERE F_ProblemaUnidad = 0 GROUP BY F_ClaUni ) AND F_ClaCli NOT IN ( SELECT GROUP_CONCAT(F_ClaUni) FROM tb_cargareqcompra CR WHERE CR.F_ProblemaProducto = 0 GROUP BY F_ClaUni ) GROUP BY C.F_ClaUni;";
                ps = con.getConn().prepareStatement(Consulta);
                rs = ps.executeQuery();
                while (rs.next()) {
                    remisiones = new Remisiones();
                    remisiones.setClaveuni(rs.getString(1));
                    remisiones.setUnidad(rs.getString(2));
                    remisiones.setFechaa(rs.getString(3));
                    remisiones.setSts(rs.getString(4));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/AdministrarRequerimiento.jsp").forward(request, response);
            }
            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(AdministraRequrimiento.class.getName()).log(Level.SEVERE, null, e);
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
