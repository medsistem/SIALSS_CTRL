/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers;

import com.medalfa.saa.controllers.services.CatalogoService;
import com.medalfa.saa.db.ConnectionManager;
import com.medalfa.saa.db.Source;
import static com.medalfa.saa.utils.StaticText.BUSCAR_INFORMACION_FOLLOT;
import static com.medalfa.saa.utils.StaticText.OBTENER_CATALOGO_CLAVE;
import static com.medalfa.saa.utils.StaticText.OBTENER_INFORMACION_CLAVE;
import static com.medalfa.saa.utils.StaticText.OBTENER_INFORMACION_LOTE_CADUCIAD_ORIGEN;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author IngMa
 */
@WebServlet(name = "CatalogoController", urlPatterns = {"/CatalogoController"})
public class CatalogoController extends HttpServlet {

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
            out.println("<title>Servlet CatalogoController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CatalogoController at " + request.getContextPath() + "</h1>");
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

        int accion = Integer.parseInt(request.getParameter("accion"));
        JSONObject json;
        JSONArray jsona = new JSONArray();
        
        CatalogoService catalogoServicio = new CatalogoService();
          
        
        switch (accion) {
            case OBTENER_CATALOGO_CLAVE:

                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {

                      
                        jsona = catalogoServicio.autocompleteClaveDescripcion(connection);
                        
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(CatalogoController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                break;
            case OBTENER_INFORMACION_CLAVE:
                
                String cricterio = request.getParameter("cricterio");
                String item = request.getParameter("item");
                String fechaFinal = request.getParameter("fechaFinal");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) 
                    {
                        json = new JSONObject();
                        
                        json = catalogoServicio.informacionClave(connection, cricterio, item,fechaFinal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);
                        

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(CatalogoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case BUSCAR_INFORMACION_FOLLOT:
                
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) 
                    {
                         json = new JSONObject();
                         json = catalogoServicio.informacionLote(connection, Integer.parseInt(request.getParameter("folLot")));
                         response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);
                    
                        
                    }catch (SQLException | NamingException ex) {
                        Logger.getLogger(CatalogoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                break;
            case OBTENER_INFORMACION_LOTE_CADUCIAD_ORIGEN:
                
                String clave = request.getParameter("clave");
                String lote = request.getParameter("lote");
                String caducidad = request.getParameter("caducidad");
                fechaFinal = request.getParameter("fechaFinal");
                int origen = Integer.parseInt(request.getParameter("origen"));
                
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) 
                    {
                        json = new JSONObject();
                        json = catalogoServicio.informacionLote(connection, clave, lote, caducidad, origen,fechaFinal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);
                    }
                        
                    }catch (SQLException | NamingException ex) {
                        Logger.getLogger(CatalogoController.class.getName()).log(Level.SEVERE, null, ex);
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
