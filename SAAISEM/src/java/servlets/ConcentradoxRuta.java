/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import conn.ConectionDB;
import java.io.IOException;
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
import modelos.ConcentradoFechas;

/**
 * Proceso para generar el concentrado por ruta de entrega
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ConcentradoxRuta extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        List<ConcentradoFechas> listaFechas = new ArrayList<ConcentradoFechas>();
        ConcentradoFechas concentradoF;
        PreparedStatement ps, psceaps, pscsu, pscsrd;
        ResultSet rs, rsceaps, rscsu, rscsrd;
        String consulta, conceaps, concsu, concsrd;
        int contceaps = 0, contcsu = 0, contcsrd = 0;
        try {
            con.conectar();
            consulta = "SELECT F.F_FecEnt, DATE_FORMAT(F.F_FecEnt, '%d/%m/%Y') FROM tb_factura F WHERE F.F_StsFact = 'A' AND F.F_FecEnt BETWEEN CURDATE() AND CURDATE() + INTERVAL 7 DAY GROUP BY F_FecEnt ORDER BY F_FecEnt DESC;";
            ps = con.getConn().prepareStatement(consulta);
            rs = ps.executeQuery();
            while (rs.next()) {
                concentradoF = new ConcentradoFechas();
                concentradoF.setFechas(rs.getString(1));
                concentradoF.setFechaconvert(rs.getString(2));

                conceaps = "SELECT COUNT(F.F_ClaDoc) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt = '" + rs.getString(1) + "' AND U.F_Tipo='CEAPS';";
                psceaps = con.getConn().prepareStatement(conceaps);
                rsceaps = psceaps.executeQuery();
                if (rsceaps.next()) {
                    contceaps = rsceaps.getInt(1);
                }

                concsu = "SELECT COUNT(F.F_ClaDoc) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt = '" + rs.getString(1) + "' AND U.F_Tipo='CSU';";
                pscsu = con.getConn().prepareStatement(concsu);
                rscsu = pscsu.executeQuery();
                if (rscsu.next()) {
                    contcsu = rscsu.getInt(1);
                }

                concsrd = "SELECT COUNT(F.F_ClaDoc) FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F.F_FecEnt = '" + rs.getString(1) + "' AND U.F_Tipo IN ('RURAL','CSU');";
                pscsrd = con.getConn().prepareStatement(concsrd);
                rscsrd = pscsrd.executeQuery();
                if (rscsrd.next()) {
                    contcsrd = rscsrd.getInt(1);
                }

                concentradoF.setCeaps(contceaps);
                concentradoF.setCsu(contcsu);
                concentradoF.setCsrd(contcsrd);

                listaFechas.add(concentradoF);

                contceaps = 0;
                contcsu = 0;
                contcsrd = 0;
            }
            request.setAttribute("concentrado", listaFechas);
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConcentradoxRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("/facturacion/concentradoxRuta.jsp").forward(request, response);
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
