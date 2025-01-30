/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISO9000;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.FormatoIso;

/**
 * Muestra el listado de formatos iso por área
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ListaISO extends HttpServlet {

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
        List<FormatoIso> listaIso = new ArrayList<FormatoIso>();
        FormatoIso formatos;
        PreparedStatement ps = null;
        ResultSet rs;
        String Consulta;
        HttpSession sesion = request.getSession(true);
        try {
            con.conectar();

            Consulta = "SELECT F_IdFor,F_NoDoc,F_DesDoc,F_AreaF0,F_NomCom FROM tb_iso WHERE F_AreaF" + (String) sesion.getAttribute("Area") + "=1 AND F_Sts='A';";
            System.out.println(Consulta);
            ps = con.getConn().prepareStatement(Consulta);
            rs = ps.executeQuery();
            while (rs.next()) {
                formatos = new FormatoIso();
                formatos.setIdfor(rs.getInt(1));
                formatos.setNodoc(rs.getString(2));
                formatos.setDesdoc(rs.getString(3));
                formatos.setAreaf(rs.getString(4));
                formatos.setNomcom(rs.getString(5));
                listaIso.add(formatos);
            }
            request.setAttribute("listaIso", listaIso);
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ListaISO.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("/iso9001/ListaFormato.jsp").forward(request, response);
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
