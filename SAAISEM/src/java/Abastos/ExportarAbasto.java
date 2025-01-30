/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abastos;

import ISEM.ValidaReciboCC;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Proceso para generar abasto para las unidades medicas
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ExportarAbasto extends HttpServlet {

    ConectionDB con = new ConectionDB();

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
        PrintWriter out = response.getWriter();
        String Folio1 = "", Folio2 = "", Fecha1 = "", Fecha2 = "";
        HttpSession sesion = request.getSession(true);

        Folio1 = request.getParameter("folio1");
        Folio2 = request.getParameter("folio2");
        Fecha1 = request.getParameter("fecha_ini");
        Fecha2 = request.getParameter("fecha_fin");
        int Documento = 0;
        try {
            con.conectar();
            if (request.getParameter("accion").equals("mostrar")) {

                sesion.setAttribute("fecha_ini", Fecha1);
                sesion.setAttribute("fecha_fin", Fecha2);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                response.sendRedirect("reimp_factura.jsp");
            }

            if (request.getParameter("accion").equals("exportar")) {

                ResultSet Consulta = con.consulta("SELECT F.F_ClaDoc, F.F_Proyecto, F.F_ClaCli FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_StsFact='A' AND F_FecEnt BETWEEN '" + Fecha2 + "' AND '" + Fecha2 + "' GROUP BY F.F_ClaDoc, F.F_Proyecto;");
                while (Consulta.next()) {
                    Documento = Consulta.getInt(1);
                    out.println(" <script>window.open('facturacion/generaAbastoCSVFecha.jsp?F_ClaDoc=" + Documento + "&idProyecto=" + Consulta.getInt(2) + "&ConInv=" + Consulta.getString(3) + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    Documento = 0;
                }

                out.println("<script>window.history.back()</script>");
            }
            if (request.getParameter("accion").equals("exportarDispen")) {
                ResultSet Consulta = con.consulta("SELECT F.F_ClaDoc,U.F_ClaCli,U.F_NomCli,U.F_Dispen FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE U.F_Dispen ='DIMESA' AND F.F_StsFact='A' AND F_FecEnt BETWEEN '" + Fecha2 + "' AND '" + Fecha2 + "' GROUP BY F.F_ClaDoc");
                while (Consulta.next()) {
                    Documento = Consulta.getInt(1);
                    out.println(" <script>window.open('facturacion/generaFoliosFecha.jsp?F_ClaDoc=" + Documento + "&Unidad=" + Consulta.getString(2) + "&Nombre=" + Consulta.getString(3) + "&Tipo=" + Consulta.getString(4) + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    Documento = 0;
                }

                out.println("<script>window.history.back()</script>");
            }
        } catch (Exception e) {
            Logger.getLogger(ValidaReciboCC.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ValidaReciboCC.class.getName()).log(Level.SEVERE, null, ex);
            }
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
