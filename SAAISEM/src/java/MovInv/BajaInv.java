package MovInv;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Baja de insumo y Registro de movimiento al inventario
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class BajaInv extends HttpServlet {

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
            String F_ProMov = "", F_UbiMov = "";
            int F_LotMov = 0, F_CantMov = 0, Contar = 0;
            ResultSet DatosM = null;
            ResultSet DatosL = null;

            int FolioMov = 0, FolioMovT = 0;
            con.actualizar("UPDATE tb_lote SET F_ExiLot='0';");

            DatosL = con.consulta("SELECT F_IndFolMov FROM tb_indice;");
            if (DatosL.next()) {
                FolioMov = DatosL.getInt(1);
            }

            FolioMovT = FolioMov + 1;

            con.actualizar("UPDATE tb_indice SET F_IndFolMov='" + FolioMovT + "';");

            DatosM = con.consulta("SELECT F_ProMov,F_LotMov,F_UbiMov,SUM(F_CantMov*F_SigMov) AS F_CantMov FROM tb_movinv GROUP BY F_ProMov,F_LotMov,F_UbiMov;");
            while (DatosM.next()) {
                F_ProMov = DatosM.getString(1);
                F_LotMov = DatosM.getInt(2);
                F_UbiMov = DatosM.getString(3);
                F_CantMov = DatosM.getInt(4);
                if (F_CantMov > 0) {
                    con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + FolioMov + "','62','" + F_ProMov + "','" + F_CantMov + "','0.00','0.00','-1','" + F_LotMov + "','" + F_UbiMov + "','3000',CURTIME(),'" + sesion.getAttribute("nombre") + "','');");
                }
            }

            out.println("<script>alert('La baja del Insumo Generado Correctamente')</script>");
            out.println("<script>window.location.href = 'BajaInv.jsp';</script>");

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
