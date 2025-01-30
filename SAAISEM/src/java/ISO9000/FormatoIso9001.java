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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Proceso para ver los formatos iso por área
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class FormatoIso9001 extends HttpServlet {

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
        HttpSession sesison = request.getSession(true);
        ConectionDB con = new ConectionDB();
        String Consulta, Consulta2;
        PreparedStatement ps = null, ps2 = null;
        ResultSet rs;
        try {
            String Id = request.getParameter("Id");

            if (request.getParameter("accion").equals("ModiFormato")) {
                System.out.println("Modifica Formato y áreas");
                String formato = request.getParameter("formato");
                String Sts = request.getParameter("stadoSwitch");
                //String descrip = request.getParameter("descrip");
                String descrip = new String(request.getParameter("descrip").getBytes("ISO-8859-1"), "UTF-8");
                String areaf1 = request.getParameter("areaf1");
                String areaf2 = request.getParameter("areaf2");
                String areaf3 = request.getParameter("areaf3");
                String areaf4 = request.getParameter("areaf4");
                String areaf5 = request.getParameter("areaf5");
                String areaf6 = request.getParameter("areaf6");
                String areaf7 = request.getParameter("areaf7");
                String areaf8 = request.getParameter("areaf8");
                String areaf9 = request.getParameter("areaf9");
                String areaf10 = request.getParameter("areaf10");
                String areaf11 = request.getParameter("areaf11");

                if (areaf1 == null) {
                    areaf1 = "0";
                }
                if (areaf2 == null) {
                    areaf2 = "0";
                }
                if (areaf3 == null) {
                    areaf3 = "0";
                }
                if (areaf4 == null) {
                    areaf4 = "0";
                }
                if (areaf5 == null) {
                    areaf5 = "0";
                }
                if (areaf6 == null) {
                    areaf6 = "0";
                }
                if (areaf7 == null) {
                    areaf7 = "0";
                }
                if (areaf8 == null) {
                    areaf8 = "0";
                }
                if (areaf9 == null) {
                    areaf9 = "0";
                }
                if (areaf10 == null) {
                    areaf10 = "0";
                }
                if (areaf11 == null) {
                    areaf11 = "0";
                }
                if (Sts == null) {
                    Sts = "off";
                }

                if (areaf1.equals("on")) {
                    areaf1 = "1";
                }
                if (areaf2.equals("on")) {
                    areaf2 = "1";
                }
                if (areaf3.equals("on")) {
                    areaf3 = "1";
                }
                if (areaf4.equals("on")) {
                    areaf4 = "1";
                }
                if (areaf5.equals("on")) {
                    areaf5 = "1";
                }
                if (areaf6.equals("on")) {
                    areaf6 = "1";
                }
                if (areaf7.equals("on")) {
                    areaf7 = "1";
                }
                if (areaf8.equals("on")) {
                    areaf8 = "1";
                }
                if (areaf9.equals("on")) {
                    areaf9 = "1";
                }
                if (areaf10.equals("on")) {
                    areaf10 = "1";
                }
                if (areaf11.equals("on")) {
                    areaf11 = "1";
                }

                System.out.println("areaf5" + areaf5);
                if ((Id != "") && (formato != "") && (descrip != "")) {
                    Consulta = "UPDATE tb_iso SET F_NoDoc=?, F_DesDoc=?,F_AreaF1=?,F_AreaF2=?,F_AreaF3=?,F_AreaF4=?,F_AreaF5=?,F_AreaF6=?,F_AreaF7=?,F_AreaF8=?,F_AreaF9=?,F_AreaF10=?,F_AreaF11=?,F_Sts=? WHERE F_IdFor=?";
                    Consulta2 = "UPDATE tb_iso SET F_NoDoc='" + formato + "', F_DesDoc='" + descrip + "',F_AreaF1='" + areaf1 + "',F_AreaF2='" + areaf2 + "',F_AreaF3='" + areaf3 + "',F_AreaF4='" + areaf4 + "',F_AreaF5='" + areaf5 + "',F_AreaF6='" + areaf6 + "',F_AreaF7='" + areaf7 + "',F_AreaF8='" + areaf8 + "',F_AreaF9='" + areaf9 + "',F_AreaF10='" + areaf10 + "',F_AreaF11='" + areaf11 + "' WHERE F_IdFor='" + Id + "'";
                    System.out.println(Consulta2);
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, formato);
                    ps.setString(2, descrip);
                    ps.setString(3, areaf1);
                    ps.setString(4, areaf2);
                    ps.setString(5, areaf3);
                    ps.setString(6, areaf4);
                    ps.setString(7, areaf5);
                    ps.setString(8, areaf6);
                    ps.setString(9, areaf7);
                    ps.setString(10, areaf8);
                    ps.setString(11, areaf9);
                    ps.setString(12, areaf10);
                    ps.setString(13, areaf11);
                    ps.setString(14, (Sts.equals("on")) ? "A" : "S");
                    ps.setString(15, Id);
                    ps.execute();
                    request.getRequestDispatcher("/ListaISOModifica?Lista=formatos").forward(request, response);
                } else {
                    request.getRequestDispatcher("/iso9001/FormatoModifica.jsp").forward(request, response);
                }
            }

            if (request.getParameter("accion").equals("Guardar")) {

                String formato = request.getParameter("formato");
                String descrip = new String(request.getParameter("descrip").getBytes("ISO-8859-1"), "UTF-8");
                String areaf1 = request.getParameter("areaf1");
                String areaf2 = request.getParameter("areaf2");
                String areaf3 = request.getParameter("areaf3");
                String areaf4 = request.getParameter("areaf4");
                String areaf5 = request.getParameter("areaf5");
                String areaf6 = request.getParameter("areaf6");
                String areaf7 = request.getParameter("areaf7");
                String areaf8 = request.getParameter("areaf8");
                String areaf9 = request.getParameter("areaf9");
                String areaf10 = request.getParameter("areaf10");
                String areaf11 = request.getParameter("areaf11");

                if (areaf1 == null) {
                    areaf1 = "0";
                }
                if (areaf2 == null) {
                    areaf2 = "0";
                }
                if (areaf3 == null) {
                    areaf3 = "0";
                }
                if (areaf4 == null) {
                    areaf4 = "0";
                }
                if (areaf5 == null) {
                    areaf5 = "0";
                }
                if (areaf6 == null) {
                    areaf6 = "0";
                }
                if (areaf7 == null) {
                    areaf7 = "0";
                }
                if (areaf8 == null) {
                    areaf8 = "0";
                }
                if (areaf9 == null) {
                    areaf9 = "0";
                }
                if (areaf10 == null) {
                    areaf10 = "0";
                }
                if (areaf11 == null) {
                    areaf11 = "0";
                }

                if (areaf1.equals("on")) {
                    areaf1 = "1";
                }
                if (areaf2.equals("on")) {
                    areaf2 = "1";
                }
                if (areaf3.equals("on")) {
                    areaf3 = "1";
                }
                if (areaf4.equals("on")) {
                    areaf4 = "1";
                }
                if (areaf5.equals("on")) {
                    areaf5 = "1";
                }
                if (areaf6.equals("on")) {
                    areaf6 = "1";
                }
                if (areaf7.equals("on")) {
                    areaf7 = "1";
                }
                if (areaf8.equals("on")) {
                    areaf8 = "1";
                }
                if (areaf9.equals("on")) {
                    areaf9 = "1";
                }
                if (areaf10.equals("on")) {
                    areaf10 = "1";
                }
                if (areaf11.equals("on")) {
                    areaf11 = "1";
                }
                int Contar = 0;
                Consulta2 = "SELECT COUNT(F_NoDoc) FROM tb_iso WHERE F_NoDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, formato);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    Contar = rs.getInt(1);
                }

                if (Contar == 0) {
                    Consulta = "INSERT INTO tb_iso VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setInt(1, 0);
                    ps.setString(2, formato);
                    ps.setString(3, descrip);
                    ps.setString(4, "A");
                    ps.setInt(5, 0);
                    ps.setString(6, areaf1);
                    ps.setString(7, areaf2);
                    ps.setString(8, areaf3);
                    ps.setString(9, areaf4);
                    ps.setString(10, areaf5);
                    ps.setString(11, areaf6);
                    ps.setString(12, areaf7);
                    ps.setString(13, areaf8);
                    ps.setString(14, areaf9);
                    ps.setString(15, areaf10);
                    ps.setString(16, areaf11);
                    ps.setInt(17, 0);
                    ps.setString(18, "");
                    ps.execute();
                    request.getRequestDispatcher("/ListaISOModifica?Lista=formatos").forward(request, response);
                } else {
                    out.println("<script>alert('Formato Existente')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
