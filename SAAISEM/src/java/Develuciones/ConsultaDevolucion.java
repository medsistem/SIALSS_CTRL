package Develuciones;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Consulta de devoluciones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ConsultaDevolucion extends HttpServlet {

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
        JSONArray jsona;
        ServletContext conexto = request.getServletContext();
        HttpSession sesion = request.getSession(true);
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        String Folio = request.getParameter("foliosel");
        String IdReg = request.getParameter("idreg");
        String CB = request.getParameter("CB");
        String Proyecto = request.getParameter("Proyecto");
        String Usuario = (String) sesion.getAttribute("nombre");
        JSONObject json = new JSONObject();
        switch (accion) {
            case "obtenerIdReg":
                ConsultaDevoDaoImpl consultaDatos = new ConsultaDevoDaoImpl();
                jsona = consultaDatos.getRegistro(Folio);
                out.println(jsona);
                break;

            case "EliminarReg":
                ConsultaDevoDaoImpl consultaDatosE = new ConsultaDevoDaoImpl();
                jsona = consultaDatosE.getEliminaRegistro(Folio, IdReg);
                out.println(jsona);
                break;

            case "validaDevolucion":
                String Obs = "";
                byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                Obs = (new String(a, "UTF-8")).toUpperCase();
                ConsultaDevoDaoImpl consultaDatosV = new ConsultaDevoDaoImpl();
                jsona = consultaDatosV.getValidaDevolucion(Folio, Obs, Usuario, Proyecto);
                out.println(jsona);
                break;

            case "validaDevolucionTrans":
                json = new JSONObject();
                String ObsT = "";
                byte[] aT = request.getParameter("Obs").getBytes("ISO-8859-1");
                ObsT = (new String(aT, "UTF-8")).toUpperCase();
                ConsultaDevoDaoImpl consultaDatosT = new ConsultaDevoDaoImpl();
                boolean RegisDev = consultaDatosT.ValidaDevolucionTran(Folio, ObsT, Usuario, Proyecto);
                json.put("msj", RegisDev);
                out.print(json);
                out.close();
                break;
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
