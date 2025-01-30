/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

import conn.ConectionDB;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Anibal MEDALFA
 */
@WebServlet(name = "ImprimeRequerimientoC", urlPatterns = {"/ImprimeRequerimientoC"})
public class ImprimeRequerimientoC extends HttpServlet {

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
        HttpSession sesion = request.getSession(true);
        DecimalFormat df = new DecimalFormat("#,###.00");
        ServletContext context = request.getServletContext();
        String remis = request.getParameter("fol_gnkl");

        ConectionDB con = new ConectionDB();
        Connection conexion;
        try {
            conexion = con.getConn();

            File reportFile = new File(context.getRealPath("/reportes/ImprimeRequerimiento.jasper"));
            /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
            Map parameters = new HashMap();
            parameters.put("Folfact", remis);
            /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
            byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
            /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
            ouputStream.close();

        } catch (Exception e) {

        }
    }

    private static boolean band() {
        if (Math.random() > .5) {
            return true;
        } else {
            return false;
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
