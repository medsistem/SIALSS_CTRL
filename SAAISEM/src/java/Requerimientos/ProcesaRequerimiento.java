/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

import conn.*;
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
 * Procesamiento del requerimiento de la unidad nube
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ProcesaRequerimiento extends HttpServlet {

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
//        ConRequerimiento req = new ConRequerimiento();
        ConectionDB con = new ConectionDB();
//        ConectionDB_Inventarios conInv = new ConectionDB_Inventarios();
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
//            req.conectar();
            con.conectar();
//            conInv.conectar();

            if (request.getParameter("accion").equals("EnviarRequeFact")) {

                con.actualizar("DELETE FROM tb_requerimientoprocesar WHERE F_IdReq ='" + request.getParameter("fol_gnkl") + "';");

//                ConsultaReq = req.consulta("SELECT R.F_ClaCli,D.F_ClaPro,D.F_Cant,DATE_FORMAT(R.F_FecCap,'%d/%m/%Y') AS F_FecCap FROM tb_detrequerimiento D INNER JOIN tb_requerimientos R ON D.F_IdReq=R.F_IdReq WHERE D.F_IdReq='" + request.getParameter("fol_gnkl") + "' AND F_Cant>0;");
                while (ConsultaReq.next()) {
                    Unidad = ConsultaReq.getString(1);
                    con.insertar("INSERT INTO tb_requerimientoprocesar VALUES('" + ConsultaReq.getString(1) + "','" + ConsultaReq.getString(2) + "','" + ConsultaReq.getString(3) + "','" + request.getParameter("fol_gnkl") + "','" + ConsultaReq.getString(4) + "','0',0);");
                }
                /*
                ConsultaU = conInv.consulta("select  DATE_FORMAT(MAX(hora_ini), '%d/%m/%Y') as hora_ini,MAX(_id) from inventarios where cla_mod = '" + Unidad + "';");
                if (ConsultaU.next()) {
                    Fecha = ConsultaU.getString(1);
                    IdInv = ConsultaU.getString(2);
                }
                if ((Fecha == "") || (Fecha == null)) {
                    DFecha = "No tiene Inventario";
                } else {
                    DFecha = Fecha;
                }
                con.actualizar("UPDATE tb_requerimientoprocesar SET F_Fecha='" + DFecha + "' WHERE F_IdReq='" + request.getParameter("fol_gnkl") + "';");

                if ((!(Fecha == null))) {
                    System.out.println("Entro con datos" + Fecha);

                    Consulta = con.consulta("SELECT R.F_ClaPro FROM tb_requerimientoprocesar R WHERE F_IdReq='" + request.getParameter("fol_gnkl") + "' AND F_Cant>0");
                    while (Consulta.next()) {
                        ConsultaInv = conInv.consulta("select SUM(cant) AS cant from v_inventarios where idInv = '" + IdInv + "' and cla_mod = '" + Unidad + "' AND cla_prod='" + Consulta.getString(1) + "';");
                        if (ConsultaInv.next()) {
                            CantInv = ConsultaInv.getString(1);
                        }
                        if ((CantInv == "") || (CantInv == null)) {
                            Inv = "0";
                        } else {
                            Inv = CantInv;
                        }
                        con.actualizar("UPDATE tb_requerimientoprocesar SET F_CantInv='" + Inv + "' WHERE F_IdReq='" + request.getParameter("fol_gnkl") + "' AND F_ClaPro='" + Consulta.getString(1) + "';");
                        Inv = "";
                        CantInv = "";
                    }
                }
                 */

                out.println(" <script>window.open('Requerimientos/reimpresion.jsp?F_ClaDoc=" + request.getParameter("fol_gnkl") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");

                out.println("<script>window.history.back()</script>");
            }

            if (request.getParameter("accion").equals("Actualizar")) {
                int IdReg = 0, Req = 0, CantVal = 0;

                IdReg = Integer.parseInt(request.getParameter("idreg"));
                Req = Integer.parseInt(request.getParameter("Req"));
                CantVal = Integer.parseInt(request.getParameter("CantV"));

                con.actualizar("UPDATE tb_requerimientoprocesar SET F_Cant='" + CantVal + "' WHERE F_Id='" + IdReg + "';");
                response.sendRedirect("Requerimientos/reimpresion.jsp?F_ClaDoc=" + Req + "");

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

                    ConsultaReq = con.consulta("SELECT F_ClaCli,P.F_ClaPro,F_Cant FROM tb_requerimientoprocesar P INNER JOIN tb_medica M ON P.F_ClaPro=M.F_ClaPro WHERE F_IdReq='" + request.getParameter("reque") + "' AND F_Cant>0 AND F_StsPro='A';");
                    while (ConsultaReq.next()) {
                        con.insertar("INSERT INTO tb_unireq VALUES('" + ConsultaReq.getString(1) + "','" + ConsultaReq.getString(2) + "','0','" + ConsultaReq.getString(3) + "',CURDATE(),0,'0','" + FechaE + "','" + ConsultaReq.getString(3) + "','');");
                    }
                    out.println("<script>var ventana = window.self;\n"
                            + "                                            ventana.opener = window.self;\n"
                            + "                                            setTimeout(\"window.close()\", 0);</script>");
                } else {
                    out.println("<script>alert('Seleccionar Fecha Entrega')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(ProcesaRequerimiento.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
//                req.cierraConexion();
//                conInv.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ProcesaRequerimiento.class.getName()).log(Level.SEVERE, null, ex);
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
