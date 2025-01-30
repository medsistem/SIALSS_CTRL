/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

import com.gnk.dao.ProcesarRequerimientoDao;
import com.gnk.impl.ProcesarRequerimientoDaoImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

/**
 *
 * @author Anibal GNKL
 */
@WebServlet(name = "ProcesarRequerimiento", urlPatterns = {"/ProcesarRequerimiento"})
public class ProcesarRequerimiento extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProcesarRequerimiento</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProcesarRequerimiento at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String accion = request.getParameter("accion");
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String Usuario = (String) sesion.getAttribute("nombre");
        String Folio = request.getParameter("Folio");
        String Unidad = request.getParameter("Unidad");
        String ClaCli = request.getParameter("ClaCli");
        JSONObject json = new JSONObject();

        switch (accion) {
            case "ProcesarRequerimiento":{
                json = new JSONObject();
                ProcesarRequerimientoDao registrarequerimiento = new ProcesarRequerimientoDaoImpl();
                boolean Registrarequerimiento = registrarequerimiento.ConfirmarRequerimiento(Usuario, Folio, Unidad, ClaCli);
                json.put("msj", Registrarequerimiento);
                out.print(json);
                out.close();
                break;
            }
            case "actualizaRequerimiento":{
                int id = Integer.parseInt(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                json = new JSONObject();
                ProcesarRequerimientoDao registrarequerimiento = new ProcesarRequerimientoDaoImpl();
                boolean Registrarequerimiento = registrarequerimiento.actualizaRequerimiento(id, cantidad);
                json.put("msj", Registrarequerimiento);
                out.print(json);
                out.close();
                break;
            }
            case "guardarFecha":{
                int folio = Integer.parseInt(request.getParameter("folio"));
                String fecha = request.getParameter("fecha");
                String unidad = request.getParameter("unidad");
                ProcesarRequerimientoDao registrarequerimiento = new ProcesarRequerimientoDaoImpl();
                boolean Registrarequerimiento = registrarequerimiento.agregaFecha(folio, unidad, fecha);
                json.put("msj", Registrarequerimiento);
                out.print(json);
                out.close();
                break;
            }
        }
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
