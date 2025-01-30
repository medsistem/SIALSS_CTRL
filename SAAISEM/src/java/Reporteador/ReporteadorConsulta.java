/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reporteador;

import com.gnk.impl.ReporteadorDaoImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;

/**
 *
 * @author Anibal MEDALFA
 */
public class ReporteadorConsulta extends HttpServlet {

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
        JSONArray jsona;
        String accion = request.getParameter("accion");
        String Jurisdiccion = request.getParameter("Jurisdiccion");
        String Municipio = request.getParameter("Municipio");
        switch (accion) {
            case "obtenerJurisdicciones":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerJurisdicciones();
                    out.println(jsona);
                }
                break;

            case "obtenerMunicipio":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerMunicipio(Jurisdiccion);
                    out.println(jsona);
                }
                break;

            case "obtenerUnidad":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerUnidad(Jurisdiccion, Municipio);
                    out.println(jsona);
                }
                break;

            case "obtenerUnidades":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerUnidades();
                    out.println(jsona);
                }
                break;

            case "obtenerTipoUnidad":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerTipoUnidad();
                    out.println(jsona);
                }
                break;

            ////Auditoria///
            case "obtenerJurisdiccionesAud":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerJurisdiccionesAud();
                    out.println(jsona);
                }
                break;

            case "obtenerMunicipioAud":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerMunicipioAud(Jurisdiccion);
                    out.println(jsona);
                }
                break;

            case "obtenerUnidadAud":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerUnidadAud(Jurisdiccion, Municipio);
                    out.println(jsona);
                }
                break;

            case "obtenerUnidadesAud":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerUnidadesAud();
                    out.println(jsona);
                }
                break;

            case "obtenerTipoUnidadAud":
                try (PrintWriter out = response.getWriter()) {
                    ReporteadorDaoImpl consultaDatos = new ReporteadorDaoImpl();
                    jsona = consultaDatos.ObtenerTipoUnidadAud();
                    out.println(jsona);
                }
                break;
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
