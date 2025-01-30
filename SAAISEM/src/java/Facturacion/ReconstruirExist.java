/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
//import sun.org.mozilla.javascript.internal.ast.Loop;

/**
 * Reconstrucción de existencia
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ReconstruirExist extends HttpServlet {

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
        try {
            ResultSet Reconstruccion = null;

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            String Clave = request.getParameter("Clave");
            String F_ProMov = "", F_UbiMov = "";
            int F_LotMov = 0, F_CantMov = 0, Contar = 0;
            ResultSet DatosM = null;
            ResultSet DatosL = null;
            ResultSet DatosC = null;
            if (Clave.equals("--Selecciona--")) {
                Clave = "";
            }

            if (Clave != "") {
                con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_ClaPro='" + Clave + "';");
                DatosM = con.consulta("SELECT F_ProMov,F_LotMov,F_UbiMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv WHERE F_ProMov='" + Clave + "' GROUP BY F_ProMov,F_LotMov,F_UbiMov;");
                while (DatosM.next()) {
                    F_ProMov = DatosM.getString(1);
                    F_LotMov = DatosM.getInt(2);
                    F_UbiMov = DatosM.getString(3);
                    F_CantMov = DatosM.getInt(4);

                    DatosL = con.consulta("SELECT COUNT(F_ClaPro) FROM tb_lote WHERE F_ClaPro='" + F_ProMov + "' AND F_Ubica='" + F_UbiMov + "' AND F_FolLot='" + F_LotMov + "';");
                    if (DatosL.next()) {
                        Contar = DatosL.getInt(1);
                    }
                    if (Contar > 0) {
                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + F_CantMov + "' WHERE F_ClaPro='" + F_ProMov + "' AND F_Ubica='" + F_UbiMov + "' AND F_FolLot='" + F_LotMov + "' LIMIT 1;");
                        Contar = 0;
                    } else {
                        DatosC = con.consulta("SELECT F_ClaLot,F_FecCad,F_FecFab,F_ClaOrg,F_Cb,F_ClaMar,F_Origen,F_ClaPrv,F_UniMed FROM tb_lote WHERE F_ClaPro='" + F_ProMov + "' AND F_FolLot='" + F_LotMov + "';");
                        if (DatosC.next()) {
                            con.actualizar("INSERT INTO TB_LOTE VALUES (0,'" + F_ProMov + "','" + DatosC.getString(1) + "','" + DatosC.getString(2) + "','" + F_CantMov + "','" + F_LotMov + "','" + DatosC.getString(4) + "','" + F_UbiMov + "','" + DatosC.getString(3) + "','" + DatosC.getString(5) + "','" + DatosC.getString(6) + "','" + DatosC.getString(7) + "','" + DatosC.getString(8) + "','" + DatosC.getString(9) + "')");
                        }
                        Contar = 0;
                    }
                }
            } else {
                con.actualizar("UPDATE tb_lote SET F_ExiLot='0';");
                DatosM = con.consulta("SELECT F_ProMov,F_LotMov,F_UbiMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv GROUP BY F_ProMov,F_LotMov,F_UbiMov;");
                while (DatosM.next()) {
                    F_ProMov = DatosM.getString(1);
                    F_LotMov = DatosM.getInt(2);
                    F_UbiMov = DatosM.getString(3);
                    F_CantMov = DatosM.getInt(4);

                    if (F_CantMov > 0) {
                        DatosL = con.consulta("SELECT COUNT(F_ClaPro) FROM tb_lote WHERE F_ClaPro='" + F_ProMov + "' AND F_Ubica='" + F_UbiMov + "' AND F_FolLot='" + F_LotMov + "';");
                        if (DatosL.next()) {
                            Contar = DatosL.getInt(1);
                        }
                        if (Contar > 0) {
                            con.actualizar("UPDATE tb_lote SET F_ExiLot='" + F_CantMov + "' WHERE F_ClaPro='" + F_ProMov + "' AND F_Ubica='" + F_UbiMov + "' AND F_FolLot='" + F_LotMov + "' LIMIT 1;");
                            Contar = 0;
                        } else {
                            DatosC = con.consulta("SELECT F_ClaLot,F_FecCad,F_FecFab,F_ClaOrg,F_Cb,F_ClaMar,F_Origen,F_ClaPrv,F_UniMed FROM tb_lote WHERE F_ClaPro='" + F_ProMov + "' AND F_FolLot='" + F_LotMov + "';");
                            if (DatosC.next()) {
                                con.actualizar("INSERT INTO TB_LOTE VALUES (0,'" + F_ProMov + "','" + DatosC.getString(1) + "','" + DatosC.getString(2) + "','" + F_CantMov + "','" + F_LotMov + "','" + DatosC.getString(4) + "','" + F_UbiMov + "','" + DatosC.getString(3) + "','" + DatosC.getString(5) + "','" + DatosC.getString(6) + "','" + DatosC.getString(7) + "','" + DatosC.getString(8) + "','" + DatosC.getString(9) + "')");
                            }
                            Contar = 0;
                        }
                        F_CantMov = 0;
                    }
                    F_CantMov = 0;
                }
            }
            out.println("<script>alert('Reconstrucción Generado Correctamente')</script>");
            out.println("<script>window.location.href = 'Reconstruccion.jsp';</script>");

            con.cierraConexion();
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
