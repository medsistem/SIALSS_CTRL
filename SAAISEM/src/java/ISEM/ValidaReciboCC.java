/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISEM;

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
 * valida de lo ingresado por recibo compras
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ValidaReciboCC extends HttpServlet {

    ConectionDB con = new ConectionDB();
    //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();

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
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        HttpSession sesion = request.getSession(true);

        int Folio = 0, IdReg = 0, CantSur = 0, CantVal = 0, Diferencia = 0, Tipo = 0, Marca = 0;
        double Costo = 0.0, Importe = 0.0, Total = 0.0, Iva = 0.0;
        Folio = Integer.parseInt(request.getParameter("folio"));

        try {
            con.conectar();

            if (request.getParameter("accion").equals("Eliminar")) {
                IdReg = Integer.parseInt(request.getParameter("idreg"));
                ResultSet Consulta = con.consulta("SELECT F_Pz,F_CantSur FROM tb_compratempcc WHERE F_IdCom='" + IdReg + "' AND F_OrdCom='" + Folio + "';");
                if (Consulta.next()) {
                    CantSur = Consulta.getInt(1);
                    CantVal = Consulta.getInt(2);
                }
                Diferencia = CantSur - CantVal;
                ResultSet Datos = con.consulta("SELECT F_FecApl,C.F_ClaPro,F_Lote,F_FecCad,F_FecFab,F_Marca,F_Provee,F_Cb,F_Tarimas,F_Cajas,F_CantSur,F_TarimasI,F_CajasI,F_Resto,C.F_Costo,F_Obser,F_FolRemi,F_OrdCom,F_ClaOrg,F_User,F_Estado,C.F_Origen,M.F_TipMed FROM tb_compratempcc C INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro WHERE F_IdCom='" + IdReg + "' AND F_OrdCom='" + Folio + "';");
                while (Datos.next()) {
                    CantSur = Datos.getInt(11);
                    Costo = Datos.getDouble(15);
                    Tipo = Datos.getInt(23);

                    Importe = CantSur * Costo;
                    if (Tipo == 2505) {
                        Iva = Importe * 0.16;
                    } else {
                        Iva = 0.0;
                    }

                    Total = Importe + Iva;

                    con.insertar("INSERT INTO tb_compratemp VALUES(0,'" + Datos.getString(1) + "','" + Datos.getString(2) + "','" + Datos.getString(3) + "','" + Datos.getString(4) + "','" + Datos.getString(5) + "','" + Datos.getString(6) + "','3','" + Datos.getString(8) + "','" + Datos.getString(9) + "','" + Datos.getString(10) + "','" + Datos.getString(11) + "','" + Datos.getString(12) + "','" + Datos.getString(13) + "','" + Datos.getString(14) + "','" + Datos.getString(15) + "','" + Importe + "','" + Total + "','" + Datos.getString(16) + "','" + Datos.getString(17) + "','" + Datos.getString(18) + "','3','" + (String) sesion.getAttribute("nombre") + "','" + Datos.getString(21) + "','" + Datos.getString(22) + "', curtime())");

                }

                //con.insertar("INSERT INTO tb_compratemp (F_FecApl,F_ClaPro,F_Lote,F_FecCad,F_FecFab,F_Marca,F_Provee,F_Cb,F_Tarimas,F_Cajas,F_Pz,F_TarimasI,F_CajasI,F_Resto,F_Costo,F_ImpTo,F_ComTot,F_Obser,F_FolRemi,F_OrdCom,F_ClaOrg,F_User,F_Estado,F_Origen )  ");
                if (Diferencia == 0) {
                    con.actualizar("DELETE FROM tb_compratempcc WHERE F_IdCom='" + IdReg + "' AND F_OrdCom='" + Folio + "';");
                } else {
                    con.actualizar("UPDATE tb_compratempcc SET F_Pz='" + Diferencia + "',F_CantSur='" + Diferencia + "' WHERE F_IdCom='" + IdReg + "' AND F_OrdCom='" + Folio + "'; ");
                }

                response.sendRedirect("verificarRecibocc.jsp");
            }

            if (request.getParameter("accion").equals("Actualizar")) {
                IdReg = Integer.parseInt(request.getParameter("idreg"));
                CantSur = Integer.parseInt(request.getParameter("Cantidad"));
                CantVal = Integer.parseInt(request.getParameter("CantV"));
                if (CantVal > 0) {
                    if (CantVal > CantSur) {
                        out.println("<script>alert('La cantidar a Validar es Mayor a Cantidad a Recibir')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {
                        //Diferencia = CantSur - CantVal;
                        con.actualizar("UPDATE tb_compratempcc SET F_CantSur='" + CantVal + "' WHERE F_IdCom='" + IdReg + "' AND F_OrdCom='" + Folio + "'; ");
                        response.sendRedirect("verificarRecibocc.jsp");
                    }
                } else {
                    out.println("<script>alert('Agregar Cantidad Mayor a Cero')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

            if (request.getParameter("accion").equals("Limpiar")) {
                con.actualizar("DELETE FROM tb_compratempcc WHERE F_OrdCom='" + Folio + "';");
                response.sendRedirect("verificarRecibocc.jsp");
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
