/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers;

import com.medalfa.saa.controllers.services.ComprasService;
import com.medalfa.saa.db.ConnectionManager;
import com.medalfa.saa.db.Source;
import com.medalfa.saa.querys.ComprasQuerys;
import static com.medalfa.saa.utils.StaticText.OBTENER_INFORMACION_POR_PROYECTO;
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
@WebServlet(name = "ComprasController", urlPatterns = {"/ComprasController"})
public class ComprasController extends HttpServlet {

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
            out.println("<title>Servlet ComprasController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ComprasController at " + request.getContextPath() + "</h1>");
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
        request.setCharacterEncoding("UTF-8");
        try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {
            
            processGroupDetail(connection, request, response);

        } catch (SQLException | NamingException ex) {
            Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     private void processGroupDetail(Connection con, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException 
     {
        String proyectoDescripcion = request.getParameter("descripcionProyecto");
        String filename = "existenciasDisponibles_"+proyectoDescripcion+".xlsx";
        int proyecto = Integer.parseInt(request.getParameter("proyecto"));
        String SAVE_DIR = "ExistenciasDescarga";
        JSONObject settings = ExcelExporter.getDefaultSettings(filename, 10000, 30,
                "Existencias", "Existencias disponibles");
        String path = request.getServletContext().getRealPath("");
        String rutaCarpeta = path + File.separatorChar + SAVE_DIR;
        try (PreparedStatement ps = con.prepareStatement(ComprasQuerys.OBTENER_EXISTENCIAS_POR_PROYECTO)) 
        {
            ps.setInt(1, proyecto);
            ps.setInt(2, proyecto);
            
             List<SheetInformation> sheets = new ArrayList<>();
            SheetInformation sheet = new SheetInformation();
            sheet.setSheetName("Detalle");
            sheet.setPreparedStatement(ps);
            sheet.setHeaders(new String[]{"Clave", "Descripción", "Ubicaciones Temporales",
                "Existencia Disponible", "Existencia en temporal", "Existencia Total"});
            sheet.setInitialRow(5);
            sheets.add(sheet);

            JSONObject data = new JSONObject();
            data.put(SETTINGS_FIELD, settings);

            //Se construye el excel.
            data = ExcelExporter.preparedReport(data, sheets);
            data.put(RESPONSE_HTTP_FIELD, response);
            data.put(DIRECTORY_SAVE, rutaCarpeta);
            //se añade encabezado
            //Exporta el excel
            ExcelExporter.exportInResponseHTTP(data);
            //ExcelExporter.exportInDirectory(data);
        }
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

        ComprasService comprasService = new ComprasService();

        switch (accion) {
            case OBTENER_INFORMACION_POR_PROYECTO:

                int proyecto = Integer.parseInt(request.getParameter("proyecto"));

                try (PrintWriter out = response.getWriter()) {
                    try (Connection connection = ConnectionManager.getManager(Source.SAA_WAREHOUSE).getConnection();) {
                        jsona = comprasService.existenciaPorProyecto(connection, proyecto);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.println(jsona);
                    } catch (SQLException | NamingException ex) {
                        Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
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
