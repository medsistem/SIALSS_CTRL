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
 * Modificación de formatos iso por área
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ListaISOModifica extends HttpServlet {

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
        String Lista = request.getParameter("Lista");
        List<FormatoIso> listaIso = new ArrayList<FormatoIso>();
        FormatoIso formatos;
        PreparedStatement ps = null;
        ResultSet rs;
        String Consulta;
        HttpSession sesion = request.getSession(true);
        if (Lista.equals("formatos")) {
            try {
                con.conectar();
                Consulta = "SELECT F_IdFor,F_NoDoc,F_DesDoc,F_AreaF0,F_NomCom,F_Sts FROM tb_iso;";
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
                    formatos.setSts(rs.getString(6));
                    listaIso.add(formatos);
                }
                request.setAttribute("listaIso", listaIso);

                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(ListaISO.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getRequestDispatcher("/iso9001/ListaFormatoMod.jsp").forward(request, response);
        }

        if (Lista.equals("Modiformatos")) {
            try {
                con.conectar();
                String Formato = request.getParameter("Formato");
                Consulta = "SELECT F_IdFor,F_NoDoc,F_DesDoc,F_AreaF0,F_NomCom ,F_AreaF1,F_AreaF2,F_AreaF3,F_AreaF4,F_AreaF5,F_AreaF6,F_AreaF7,F_AreaF8,F_AreaF9,F_AreaF10,F_AreaF11,F_Sts FROM tb_iso WHERE F_IdFor=?;";
                System.out.println(Consulta);
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Formato);
                rs = ps.executeQuery();
                while (rs.next()) {
                    formatos = new FormatoIso();
                    formatos.setIdfor(rs.getInt(1));
                    formatos.setNodoc(rs.getString(2));
                    formatos.setDesdoc(rs.getString(3));
                    formatos.setAreaf(rs.getString(4));
                    formatos.setNomcom(rs.getString(5));
                    formatos.setAreaf1(rs.getInt(6));
                    formatos.setAreaf2(rs.getInt(7));
                    formatos.setAreaf3(rs.getInt(8));
                    formatos.setAreaf4(rs.getInt(9));
                    formatos.setAreaf5(rs.getInt(10));
                    formatos.setAreaf6(rs.getInt(11));
                    formatos.setAreaf7(rs.getInt(12));
                    formatos.setAreaf8(rs.getInt(13));
                    formatos.setAreaf9(rs.getInt(14));
                    formatos.setAreaf10(rs.getInt(15));
                    formatos.setAreaf11(rs.getInt(16));
                    formatos.setSts(rs.getString(17));
                    listaIso.add(formatos);
                }
                request.setAttribute("listaIso", listaIso);

                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(ListaISO.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getRequestDispatcher("/iso9001/FormatoModifica.jsp").forward(request, response);
        }

        if (Lista.equals("CargaArchivo")) {
            try {
                con.conectar();
                String Formato = request.getParameter("Formato");
                Consulta = "SELECT F_IdFor,F_NoDoc,F_DesDoc,F_AreaF0,F_NomCom FROM tb_iso WHERE F_IdFor=?;";
                System.out.println(Consulta);
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Formato);
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
            request.getRequestDispatcher("/iso9001/CargaArchivo.jsp").forward(request, response);
        }

        if (Lista.equals("AltaFormato")) {
            request.getRequestDispatcher("/iso9001/AltaFormato.jsp").forward(request, response);
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
