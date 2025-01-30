/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventario;

import com.gnk.dao.RedistribucionDao;
import com.gnk.impl.RedistribucionDaoImpl;
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
 * Proceso de redistribución transaccional
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "RedistribucionTransaccional", urlPatterns = {"/RedistribucionTransaccional"})
public class RedistribucionTransaccional extends HttpServlet {

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
       // System.out.println("Usuario:" + Usuario);
        switch (accion) {
            case "Redistribuir":
                JSONObject json = new JSONObject();
                String Ubicacion = request.getParameter("Ubicacion");
                String Origen = request.getParameter("Origen");
                //System.out.println("Origen1: "+request.getParameter("Origen"));
                String IdLote = request.getParameter("IdLote");
                int CantMov = Integer.parseInt(request.getParameter("CantMov"));
                RedistribucionDao redistribucion = new RedistribucionDaoImpl();
                boolean insertRedistribucion = redistribucion.RedistribucionUbica(Ubicacion, IdLote, CantMov, Usuario,Origen);

                /*if (redistribucion.RedistribucionUbica(Ubicacion, IdLote, CantMov, Usuario)) {
                    json.put("msj", save);
                } else {
                    json.put("msj", true);
                }*/
                json.put("msj", insertRedistribucion);
                out.print(json);
                out.close();
                break;
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
