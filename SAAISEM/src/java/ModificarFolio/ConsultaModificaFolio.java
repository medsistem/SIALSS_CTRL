/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModificarFolio;

import static Develuciones.ConsultaDevoDaoImpl.getFactorEmpaque;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "ConsultaModificaFolio", urlPatterns = {"/ConsultaModificaFolio"})
public class ConsultaModificaFolio extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        JSONArray jsona;
        ServletContext conexto = request.getServletContext();
        HttpSession sesion = request.getSession(true);

        String accion = request.getParameter("accion");
        String Folio = request.getParameter("foliosel");
        String IdReg = request.getParameter("idreg");
        String CB = request.getParameter("CB");
        String Usuario = (String) sesion.getAttribute("nombre");
        switch (accion) {
//            case "obtenerIdReg":
//                try (PrintWriter out = response.getWriter()) {
//                    ConsultaModificafolioDaoImpl consultaDatos = new ConsultaModificafolioDaoImpl();
//                    jsona = consultaDatos.getRegistro(Folio);
//                    out.println(jsona);
//                }
//                break;

            case "EliminarReg":
                try (PrintWriter out = response.getWriter()) {
                    //System.out.println("Eliminar: " + IdReg + " Folio: " + Folio);
                    ConsultaModificafolioDaoImpl consultaDatos = new ConsultaModificafolioDaoImpl();
                    jsona = consultaDatos.getEliminaRegistro(Folio, IdReg);
                    out.println(jsona);
                }
                break;

            case "validaDevolucion":
                try (PrintWriter out = response.getWriter()) {
                    String Obs = "";
                    byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                    Obs = (new String(a, "UTF-8")).toUpperCase();
                    ConsultaModificafolioDaoImpl consultaDatos = new ConsultaModificafolioDaoImpl();
                    jsona = consultaDatos.getValidaDevolucion(Folio, Obs, Usuario);
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
