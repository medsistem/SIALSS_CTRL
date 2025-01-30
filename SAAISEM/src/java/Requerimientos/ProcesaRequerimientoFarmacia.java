/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Procesamiento del requerimiento de la unidad por farmacia nube
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "ProcesaRequerimientoFarmacia", urlPatterns = {"/ProcesaRequerimientoFarmacia"})
public class ProcesaRequerimientoFarmacia extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        ResultSet ConsultaReq = null;
        ResultSet ConsultaU = null;
        ResultSet Consulta = null;
        ResultSet ConsultaInv = null;
        String Unidad = "", Fecha = "", IdInv = "", DFecha = "", CantInv = "", Inv = "";
        int Contar = 0;
        try {

            con.conectar();

            if (request.getParameter("accion").equals("EnviarRequeFact")) {

                con.actualizar("UPDATE tb_requerimientofarmacia SET F_Cantprocesado = F_Cantidad WHERE F_NoReq ='" + request.getParameter("fol_gnkl") + "' AND F_ClaCli = '" + request.getParameter("Unidad") + "';");
                /*
                ConsultaReq = con.consulta("SELECT F_ClaCli,F_ClaPro,F_Cantidad,F_Visita FROM tb_requerimientonivel WHERE F_ClaCli='" + request.getParameter("Unidad") + "' AND F_NoReq='" + request.getParameter("fol_gnkl") + "';");
                while (ConsultaReq.next()) {
                    con.insertar("INSERT INTO tb_requerimientoprocesarnivel VALUES('" + ConsultaReq.getString(1) + "','" + ConsultaReq.getString(2) + "','" + ConsultaReq.getString(3) + "','" + request.getParameter("fol_gnkl") + "','" + ConsultaReq.getString(4) + "',0);");
                }*/

                out.println(" <script>window.open('Requerimientos/reimpresionFarmacia.jsp?F_ClaDoc=" + request.getParameter("fol_gnkl") + "&ClaCli=" + request.getParameter("Unidad") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");

                out.println("<script>window.history.back()</script>");
            }

            if (request.getParameter("accion").equals("Actualizar")) {
                int IdReg = 0, Req = 0, CantVal = 0;
                String ClaCli = "";
                IdReg = Integer.parseInt(request.getParameter("idreg"));
                Req = Integer.parseInt(request.getParameter("Req"));
                CantVal = Integer.parseInt(request.getParameter("CantV"));

                ConsultaReq = con.consulta("SELECT F_ClaCli FROM tb_requerimientofarmacia WHERE F_Id='" + IdReg + "';");
                while (ConsultaReq.next()) {
                    ClaCli = ConsultaReq.getString(1);
                }

                con.actualizar("UPDATE tb_requerimientofarmacia SET F_Cantprocesado='" + CantVal + "' WHERE F_Id='" + IdReg + "';");
                response.sendRedirect("Requerimientos/reimpresionFarmacia.jsp?F_ClaDoc=" + Req + "&ClaCli=" + ClaCli + "");

            }

            if (request.getParameter("accion").equals("procesareque")) {

                String FechaE = "";

                ConsultaU = con.consulta("SELECT COUNT(F_ClaUni) FROM tb_unireq WHERE F_ClaUni='" + request.getParameter("Unidad") + "' AND F_Status='0' GROUP BY F_Status;");
                if (ConsultaU.next()) {
                    Contar = ConsultaU.getInt(1);
                }

                if (Contar > 0) {
                    con.actualizar("UPDATE tb_unireq SET F_Status='1' WHERE F_ClaUni='" + request.getParameter("Unidad") + "';");
                }

                Contar = 0;

                FechaE = request.getParameter("fecha_fin");

                if ((FechaE != "")) {

                    ConsultaReq = con.consulta("SELECT F_ClaCli, F_ClaPro, F_Cantprocesado FROM tb_requerimientofarmacia WHERE F_NoReq = '" + request.getParameter("reque") + "' AND F_Cantprocesado > 0 AND F_ClaCli = '" + request.getParameter("Unidad") + "';");
                    while (ConsultaReq.next()) {
                        con.insertar("INSERT INTO tb_unireq VALUES('" + ConsultaReq.getString(1) + "','" + ConsultaReq.getString(2) + "','0','" + ConsultaReq.getString(3) + "',CURDATE(),0,'0','" + FechaE + "','" + ConsultaReq.getString(3) + "','');");
                    }
                    con.actualizar("UPDATE tb_requerimientofarmacia SET F_Sts=2 WHERE F_NoReq='" + request.getParameter("reque") + "' AND F_ClaCli = '" + request.getParameter("Unidad") + "';");

                    out.println("<script>var ventana = window.self;\n"
                            + "                                            ventana.opener = window.self;\n"
                            + "                                            setTimeout(\"window.close()\", 0);</script>");
                } else {
                    out.println("<script>alert('Seleccionar Fecha Entrega')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(ProcesaRequerimientoFarmacia.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ProcesaRequerimientoFarmacia.class.getName()).log(Level.SEVERE, null, ex);
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
