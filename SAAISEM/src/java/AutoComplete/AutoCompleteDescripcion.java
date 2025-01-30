/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Anibal MEDALFA
 */
@WebServlet(name = "AutoCompleteDescripcion", urlPatterns = {"/AutoCompleteDescripcion"})
public class AutoCompleteDescripcion extends HttpServlet {

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
                if (request.getParameter("accion").equals("BuscarDescripcion")) {
                    System.out.println("Requerimientos.AutoComplete.processRequest()");
                    con.conectar();
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = con.consulta("SELECT F_DesPro FROM tb_medica WHERE F_ProMichoacan = 1 AND F_DesPro like '%" + request.getParameter("descrip") + "%' limit 0,10");
                    while (rset.next()) {
                        json.put("DesPro", rset.getString(1).trim().replaceAll("\\n", ""));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);
                }

                if (request.getParameter("accion").equals("BuscarDescripcionCliente")) {
                    con.conectar();
                    String Campo = "", ProyectoCL = "";
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = null;
                    ProyectoCL = request.getParameter("ProyectoCL");
                    rset = con.consulta("SELECT GROUP_CONCAT(CONCAT(F_Campo,'= 1')) AS F_Campo FROM tb_proyectos WHERE F_Id IN (" + ProyectoCL + ");");
                    if (rset.next()) {
                        Campo = rset.getString(1);
                    }

                    Campo = Campo.replace(",", " OR ");
                    rset = con.consulta("SELECT F_DesPro FROM tb_medica WHERE (" + Campo + ") AND F_DesPro like '%" + request.getParameter("descrip") + "%' limit 0,10;");
                    while (rset.next()) {
                        json.put("DesPro", rset.getString(1).trim().replaceAll("\\n", ""));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } finally {
            out.close();
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
