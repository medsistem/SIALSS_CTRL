package ReportesPuntos;

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
 * Actualizacón de archivo txt
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ActualizaTxt extends HttpServlet {

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

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            ResultSet Datos = null;

            if (request.getParameter("accion").equals("CambiOrCost")) {

                String Fecha1 = request.getParameter("Fecha1");
                String Fecha2 = request.getParameter("Fecha2");
                String Radio = request.getParameter("Radio");
                String Unidad1 = request.getParameter("Unidad1");
                String Clave1 = request.getParameter("Clave1");
                String Costo = request.getParameter("Costo");
                String Origen = request.getParameter("Origen");
                String Unidad = request.getParameter("Unidad");

                if (!(Costo == "")) {
                    con.actualizar("UPDATE tb_txtis set F_CosUni='" + Costo + "' WHERE F_Fecsur BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' AND LTRIM(F_Cveart)=LTRIM('" + Clave1 + "')");
                }
                if (!(Origen == "")) {
                    con.actualizar("UPDATE tb_txtis set F_Idsur='" + Origen + "' WHERE F_Fecsur BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' AND LTRIM(F_Cveart)=LTRIM('" + Clave1 + "')");
                }

                sesion.setAttribute("Fecha11", Fecha1);
                sesion.setAttribute("Fecha22", Fecha2);
                sesion.setAttribute("Radio11", Radio);
                sesion.setAttribute("Unidad11", Unidad1);
                sesion.setAttribute("Clave11", Clave1);

                response.sendRedirect("CatalogoIsem/ActualizaTxt.jsp");

                //out.println("<script>window.location='CatalogoIsem/ActualizaTxt.jsp'</script>");
            }

            if (request.getParameter("accion").equals("CambioUnidad")) {
                String F_MunUniIS = "", F_LocUniIS = "", F_JurUniIS = "", F_MedUniIS = "";
                String Fecha1 = request.getParameter("Fecha1");
                String Fecha2 = request.getParameter("Fecha2");
                String Radio = request.getParameter("Radio");
                String Unidad1 = request.getParameter("Unidad1");
                String Clave1 = request.getParameter("Clave1");
                String Unidad = request.getParameter("Unidad");

                ResultSet DatosU = con.consulta("SELECT F_MunUniIS,F_LocUniIS,F_JurUniIS,F_MedUniIS FROM tb_unidis WHERE F_ClaUniIS='" + Unidad + "';");
                if (DatosU.next()) {
                    F_MunUniIS = DatosU.getString(1);
                    F_LocUniIS = DatosU.getString(2);
                    F_JurUniIS = DatosU.getString(3);
                    F_MedUniIS = DatosU.getString(4);
                }
                con.actualizar("UPDATE tb_txtis set F_Cveuni='" + Unidad + "',F_Cvemun='" + F_MunUniIS + "',F_CveLoc='" + F_LocUniIS + "',F_CveJur='" + F_JurUniIS + "',F_Cvemed='" + F_MedUniIS + "' WHERE F_Fecsur BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Cveuni='" + Unidad1 + "'");

                sesion.setAttribute("Fecha11", Fecha1);
                sesion.setAttribute("Fecha22", Fecha2);
                sesion.setAttribute("Radio11", Radio);
                sesion.setAttribute("Unidad11", Unidad1);
                sesion.setAttribute("Clave11", Clave1);

                response.sendRedirect("CatalogoIsem/ActualizaTxt.jsp");

            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*String folio_sp = request.getParameter("sp_pac");
         System.out.println(folio_sp);
         out.println(folio_sp);*/
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
