/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers;

import com.medalfa.saa.controllers.services.OrdenesCompraService;
import com.medalfa.saa.db.ConnectionManager;
import com.medalfa.saa.db.Source;
import static com.medalfa.saa.utils.StaticText.CERRAR_ORDEN;
import static com.medalfa.saa.utils.StaticText.OBTENER_POR_ORDEN_DE_COMPRA;
import static com.medalfa.saa.utils.StaticText.OBTENER_POR_PROVEEDOR;
import static com.medalfa.saa.utils.StaticText.REPORTE_ORDENES_CERRADAS;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author IngMa
 */
@WebServlet(name = "OrdenesCompraController", urlPatterns = {"/OrdenesCompraController"})
public class OrdenesCompraController extends HttpServlet {

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
            out.println("<title>Servlet OrdenesCompraController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrdenesCompraController at " + request.getContextPath() + "</h1>");
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
        JSONObject json = new JSONObject();
        JSONArray jsona = new JSONArray();
        int accion = Integer.parseInt(request.getParameter("accion"));
        HttpSession sesion = request.getSession(true);
        String idUsu = (String) sesion.getAttribute("IdUsu");

        OrdenesCompraService oriService = new OrdenesCompraService();
        switch (accion) {
            case OBTENER_POR_ORDEN_DE_COMPRA:

                String noOrden = request.getParameter("noOrdenCompra");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();) {

                        jsona = oriService.ordenesDeCompraNoCompra(connection, noOrden);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case OBTENER_POR_PROVEEDOR:

                String proveedor = request.getParameter("proveedor");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();) {

                        jsona = oriService.ordenesDeCompraByProvider(connection, proveedor);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case CERRAR_ORDEN:
                noOrden = request.getParameter("noOrdenCompra");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();) {

                        json = oriService.insertOrden(connection, noOrden, idUsu);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case  REPORTE_ORDENES_CERRADAS:
                
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_AUDIT).getConnection();) {

                        jsona = oriService.ordenesCompraCerradas(connection);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
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
