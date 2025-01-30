/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CancelarSecuencial;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Proceso de cancelación de secuenciales parcial
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CancelarSecuencialParcial extends HttpServlet {

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
        if (request.getParameter("accion").equals("actualizar")) {
            try {
                int CanSur = 0, CanReq = 0, DifeSur = 0, DifeReq = 0, Cancelar = 0;
                String F_LotCan = "", LoteModi = "", Datos = "";
                String fol_gnkl = request.getParameter("fol_gnkl");
                String Secuencial = request.getParameter("secuencial");
                String Lote = request.getParameter("lote");
                String Unidad = request.getParameter("unidad");
                String Folio = request.getParameter("folio");
                String secuencial1 = request.getParameter("sec1");
                String secuencial2 = request.getParameter("sec2");
                String folio = request.getParameter("folio2");
                String radio = request.getParameter("radio");

                Cancelar = Integer.parseInt(request.getParameter("cancelar"));
                out.println("Secu " + Secuencial + " folios " + Folio + " Unidad" + Unidad + " Lote" + Lote + " Cancelar " + Cancelar);
                Datos = Folio + "," + Lote + ";" + Cancelar + "|";
                HttpSession sesion = request.getSession(true);

                con.conectar();
                ResultSet Cantidad = con.consulta("SELECT F_Cansur,F_Canreq, F_LotCan FROM tb_txtis WHERE F_Secuencial='" + Secuencial + "'");
                if (Cantidad.next()) {
                    CanSur = Cantidad.getInt(1);
                    CanReq = Cantidad.getInt(2);
                    F_LotCan = Cantidad.getString(3);
                }

                DifeSur = CanSur - Cancelar;
                DifeReq = CanReq - Cancelar;
                LoteModi = F_LotCan + Datos;
                con.actualizar("UPDATE tb_txtis SET F_Cansur='" + DifeSur + "',F_Canreq='" + DifeReq + "',F_LotCan='" + LoteModi + "'  WHERE F_Secuencial = '" + Secuencial + "'");
                con.insertar("insert into tb_txtiscantparcial values('" + Secuencial + "','" + Unidad + "','" + Folio + "','" + Lote + "','" + Cancelar + "',curdate(),curtime(),'" + (String) sesion.getAttribute("nombre") + "',0)");
                sesion.setAttribute("SecuFol", Secuencial);
                out.println("<script>alert('Modificación Correcta')</script>");
                sesion.setAttribute("radio", radio);
                sesion.setAttribute("secu1", secuencial1);
                sesion.setAttribute("secu2", secuencial2);
                sesion.setAttribute("folio", folio);
                response.sendRedirect("CancelarP.jsp");
                con.cierraConexion();

            } catch (Exception e) {
                out.println("<script>alert('No cancelo el Secuencial')</script>");
            }
        }

        if (request.getParameter("accion").equals("regresar")) {

            String secuencial1 = request.getParameter("sec1");
            String secuencial2 = request.getParameter("sec2");
            String folio = request.getParameter("folio2");
            String radio = request.getParameter("radio");
            HttpSession sesion = request.getSession(true);

            sesion.setAttribute("radio", radio);
            sesion.setAttribute("secu1", secuencial1);
            sesion.setAttribute("secu2", secuencial2);
            sesion.setAttribute("folio", folio);
            response.sendRedirect("CancelarSec.jsp");
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
