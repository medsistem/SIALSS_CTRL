/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CancelarSecuencial;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Proceso de cancelación de secuenciales global
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class CancelarSecuelcialTodo extends HttpServlet {

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
        try {
            String secuencial1 = request.getParameter("sec1");
            String secuencial2 = request.getParameter("sec2");
            String folio = request.getParameter("folio");
            String radio = request.getParameter("radio");

            out.println("sec1" + secuencial1 + " sec2" + secuencial2 + " folio" + folio + " rad" + radio);
            HttpSession sesion = request.getSession(true);

            con.conectar();
            if (radio.equals("si")) {
                con.actualizar("UPDATE tb_txtis SET F_Status='C' WHERE F_Secuencial BETWEEN '" + secuencial1 + "' AND '" + secuencial2 + "'");
            } else if (radio.equals("no")) {
                con.actualizar("UPDATE tb_txtis SET F_Status='C' WHERE F_FacGNKLAgr='" + folio + "'");
            }

            con.cierraConexion();
            sesion.setAttribute("radio", radio);
            sesion.setAttribute("secu1", secuencial1);
            sesion.setAttribute("secu2", secuencial2);
            sesion.setAttribute("folio", folio);
            response.sendRedirect("CancelarSec.jsp");
        } catch (Exception e) {
            out.println("<script>alert('No cancelo')</script>");
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
