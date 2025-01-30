/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import ISEM.ValidaReciboCC;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Reporte de red fría
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class RedFriaReporte extends HttpServlet {

    ConectionDB con = new ConectionDB();

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
        String Folio1 = "", Folio2 = "", Fecha1 = "", Fecha2 = "";
        HttpSession sesion = request.getSession(true);
        Fecha1 = request.getParameter("fecha_ini");
        Fecha2 = request.getParameter("fecha_fin");
        int Documento = 0;
        try {
            con.conectar();
            if (request.getParameter("accion").equals("mostrar")) {
                sesion.setAttribute("fecha_ini", Fecha1);
                sesion.setAttribute("fecha_fin", Fecha2);
                response.sendRedirect("RedFria.jsp");
            }

            if (request.getParameter("accion").equals("ModiRed")) {
                String F_ClaDoc = request.getParameter("fol_gnkl");

                con.actualizar("DELETE FROM tb_redfriaimp;");

                ResultSet ConsulDato = con.consulta("SELECT U.F_DesUniIS, J.F_DesJurIS, F.F_ClaDoc, F.F_ClaPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') as Caducidad, sum(F.F_CantSur) as CantSur FROM tb_redfria RF INNER JOIN tb_factura F on RF.F_ClaPro=F.F_ClaPro INNER JOIN tb_lote L on F.F_Lote=L.F_FolLot and F.F_ClaPro=L.F_ClaPro and F.F_Ubicacion=L.F_Ubica INNER JOIN tb_unidis U on F.F_ClaCli=U.F_ClaInt1 or F.F_ClaCli=U.F_ClaInt2 or F.F_ClaCli=U.F_ClaInt3 or F.F_ClaCli=U.F_ClaInt4 or F.F_ClaCli=U.F_ClaInt5 INNER JOIN tb_juriis J on U.F_JurUniIS=J.F_ClaJurIS where F_CantSur > 0 and F_StsFact='A' AND F_ClaDoc='" + F_ClaDoc + "' GROUP BY F_ClaDoc, F_ClaPro, F_ClaLot, F_FecCad;");
                while (ConsulDato.next()) {
                    con.insertar("INSERT INTO tb_redfriaimp VALUES('" + ConsulDato.getString(1) + "','" + ConsulDato.getString(2) + "','" + ConsulDato.getString(3) + "','" + ConsulDato.getString(4) + "','" + ConsulDato.getString(5) + "','" + ConsulDato.getString(6) + "','" + ConsulDato.getString(7) + "',0);");
                }

                sesion.setAttribute("Folio", request.getParameter("fol_gnkl"));
                response.sendRedirect("RedFriaModi.jsp");
            }

            if (request.getParameter("accion").equals("Eliminar")) {
                String IdFol = request.getParameter("IdFol");
                String fol_gnkl = request.getParameter("fol_gnkl");
                try {
                    con.conectar();
                    con.actualizar("DELETE FROM tb_redfriaimp WHERE F_ClaDoc='" + fol_gnkl + "' AND F_Id='" + IdFol + "';");

                    sesion.setAttribute("Folio", request.getParameter("fol_gnkl"));
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    response.sendRedirect("RedFriaModi.jsp");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("ModificarTemp")) {
                String Tempera = request.getParameter("tempe");
                try {
                    con.conectar();
                    con.actualizar("UPDATE tb_temperatura SET F_Temperatura='" + Tempera + "';");

                    out.println("<script>window.history.back()</script>");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
             if (request.getParameter("accion").equals("AltaFria")) {
                String Tempera = request.getParameter("tempe");
                try {
                    con.conectar();
                    con.actualizar(";");

                    out.println("<script>window.history.back()</script>");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
             
              if (request.getParameter("accion").equals("BajaFria")) {
                String Tempera = request.getParameter("tempe");
                try {
                    con.conectar();
                    con.actualizar(";");

                    out.println("<script>window.history.back()</script>");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            Logger.getLogger(ValidaReciboCC.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ValidaReciboCC.class.getName()).log(Level.SEVERE, null, ex);
            }
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
