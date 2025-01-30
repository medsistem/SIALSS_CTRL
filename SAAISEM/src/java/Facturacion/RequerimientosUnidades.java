/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Consulta de requerimiento
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "RequerimientosUnidades", urlPatterns = {"/RequerimientosUnidades"})
public class RequerimientosUnidades extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        try {
            try {
                if (request.getParameter("accion").equals("actualizaRequerimiento")) {
                    try {

                        con.conectar();
                        ResultSet rset = con.consulta("select F_ClaPro from tb_unireq where F_ClaUni = '" + request.getParameter("F_ClaUni") + "' and F_Status=0 and  F_PiezasReq != 0");
                        while (rset.next()) {
                            String ClaPro = rset.getString("F_ClaPro");
                            String F_NCant = request.getParameter(ClaPro.trim());
                            out.println(ClaPro);
                            out.println(F_NCant);
                            con.insertar("update tb_unireq set F_PiezasReq = '" + F_NCant + "' where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaUni = '" + request.getParameter("F_ClaUni") + "' and F_Status='0'");
                        }
                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    response.setContentType("text/html");
                    request.setAttribute("F_FecEnt", request.getParameter("F_FecEnt"));
                    request.setAttribute("F_ClaUni", request.getParameter("F_ClaUni"));
                    request.getRequestDispatcher("detRequerimiento.jsp").forward(request, response);
                }
            } catch (Exception e) {
            }
        } finally {
            out.close();
        }
    }

    public void actualizaReqs(String Unidades) {

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
