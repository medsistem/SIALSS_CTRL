/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers;

import com.medalfa.saa.controllers.services.KardexService;
import com.medalfa.saa.db.ConnectionManager;
import com.medalfa.saa.db.Source;
import static com.medalfa.saa.utils.StaticText.KARDEX_POR_CLAVE;
import static com.medalfa.saa.utils.StaticText.KARDEX_POR_FECHA;
import static com.medalfa.saa.utils.StaticText.KARDEX_POR_LOTE;
import exportExcel.ExcelExporter;
import static exportExcel.ExcelExporter.DIRECTORY_SAVE;
import static exportExcel.ExcelExporter.RESPONSE_HTTP_FIELD;
import static exportExcel.ExcelExporter.SETTINGS_FIELD;
import exportExcel.SheetInformation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "KardexController", urlPatterns = {"/KardexController"})
public class KardexController extends HttpServlet {

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
            out.println("<title>Servlet KardexController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet KardexController at " + request.getContextPath() + "</h1>");
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

        KardexService kardexService = new KardexService();

        switch (accion) {
            case KARDEX_POR_CLAVE:

                String clave = request.getParameter("clave");
                String fechaInicial = request.getParameter("fechaInicial");
                String fechaFinal = request.getParameter("fechaFinal");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {

                        json = kardexService.kardexInformation(connection, clave, fechaInicial, fechaFinal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);

                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case KARDEX_POR_LOTE:

                clave = request.getParameter("clave");
                String lote = request.getParameter("lote");
                String caducidad = request.getParameter("caducidad");
                int origen = Integer.parseInt(request.getParameter("origen"));
                fechaInicial = request.getParameter("fechaInicial");
                fechaFinal = request.getParameter("fechaFinal");

                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {
                        json = kardexService.kardexInformation(connection, clave, lote, caducidad, origen, fechaInicial, fechaFinal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);
                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(KardexController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case KARDEX_POR_FECHA:
                
                String dia = request.getParameter("fecha");
                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {

                        json = kardexService.kardexInformation(connection, dia);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(json);

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
