/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import com.gnk.dao.FacturacionEnseresDao;
import com.gnk.impl.FacturacionEnseresDaoImp;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Anibal GNKL
 */
@WebServlet(name = "FacturacionEnseres", urlPatterns = {"/FacturacionEnseres"})
public class FacturacionEnseres extends HttpServlet {

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
        JSONArray jsona;
        JSONObject json = new JSONObject();
        HttpSession sesion = request.getSession();
        String accion = request.getParameter("accion");
        String Usuario = (String) sesion.getAttribute("nombre");
        String ClaUni = request.getParameter("Unidad");
        String IdReg = request.getParameter("idreg");
        String OrdenCompra = request.getParameter("OrdenCompra");
        String Proveedor = request.getParameter("Proveedor");
        String Cantidad = request.getParameter("Cantidad");
        String IdRegistro = request.getParameter("IdRegistro");
        int Folio = Integer.parseInt(request.getParameter("Folio"));
        String[] F_CLaCliArray = ClaUni.split(" - ");
        ClaUni = F_CLaCliArray[0];
        switch (accion) {
            case "MostrarRegistros":
                try (PrintWriter out = response.getWriter()) {
                    FacturacionEnseresDao consultaDatos = new FacturacionEnseresDaoImp();
                    jsona = consultaDatos.MostrarRegistros(Usuario, ClaUni, Folio);
                    out.println(jsona);
                }
                break;

            case "EliminarRegistro":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    FacturacionEnseresDao eliminaRegistroEnseres = new FacturacionEnseresDaoImp();
                    boolean EliminaRegistroEnseres = eliminaRegistroEnseres.EliminaRegistroEnseres(IdReg);
                    json.put("msj", EliminaRegistroEnseres);
                    out.print(json);
                    out.close();
                }
                break;

            case "TerminoCaptura":
                sesion.setAttribute("F_IndGlobal", null);
                response.sendRedirect("factManualEnseres.jsp");
                break;

            case "MostrarEnseres":
                try (PrintWriter out = response.getWriter()) {
                    FacturacionEnseresDao consultaDatos = new FacturacionEnseresDaoImp();
                    jsona = consultaDatos.MostrarEnseres();
                    out.println(jsona);
                }
                break;

            case "obtenerIdReg":
                try (PrintWriter out = response.getWriter()) {
                    FacturacionEnseresDao consultaDatos = new FacturacionEnseresDaoImp();
                    jsona = consultaDatos.Registro(OrdenCompra, Proveedor);
                    out.println(jsona);
                }
                break;

            case "ActualizarReq":
                try (PrintWriter out = response.getWriter()) {
                    FacturacionEnseresDao consultaDatos = new FacturacionEnseresDaoImp();
                    jsona = consultaDatos.ActualizaReq(OrdenCompra, Proveedor, Cantidad, IdRegistro);
                    out.println(jsona);
                }
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
        String accion = request.getParameter("accion");
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String Usuario = (String) sesion.getAttribute("nombre");
        String FechaEnt = request.getParameter("FechaEnt");
        String ClaUni = request.getParameter("Unidad");
        String[] F_CLaCliArray = ClaUni.split(" - ");
        ClaUni = F_CLaCliArray[0];
        String ClaPro = request.getParameter("ClaPro");
        String Proyecto = request.getParameter("Proyecto");
        int Cantidad = Integer.parseInt(request.getParameter("Cantidad"));
        int Folio = Integer.parseInt(request.getParameter("Folio"));
        JSONObject json = new JSONObject();
        switch (accion) {
            case "InsertaEnseres":
                json = new JSONObject();
                FacturacionEnseresDao registrarEnseres = new FacturacionEnseresDaoImp();
                boolean RegistrarEnseres = registrarEnseres.RegistrarEnseres(Usuario, ClaUni, ClaPro, FechaEnt, Cantidad, Folio);
                json.put("msj", RegistrarEnseres);
                out.print(json);
                out.close();
                break;

            case "EliminarCaptura":
                json = new JSONObject();
                FacturacionEnseresDao eliminarCapEnseres = new FacturacionEnseresDaoImp();
                boolean EliminarCapEnseres = eliminarCapEnseres.EliminarCapEnseres(Usuario, ClaUni, Folio);
                json.put("msj", EliminarCapEnseres);
                out.print(json);
                out.close();
                break;

            case "ConfirmarFactTempEnseres":
                json = new JSONObject();
                byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                FacturacionEnseresDao registrarFactTemp = new FacturacionEnseresDaoImp();
                boolean RegisFacTemp = registrarFactTemp.ConfirmarFactTempEnseres(Usuario, Observaciones, Proyecto);
                json.put("msj", RegisFacTemp);
                out.print(json);
                out.close();
                break;

            case "IngresarRemision":
                json = new JSONObject();
                String ordenCompra = request.getParameter("ordenCompra");
                String Proveedor = request.getParameter("Proveedor");
                FacturacionEnseresDao ingresar = new FacturacionEnseresDaoImp();
                boolean actualizado = false;
                if (ingresar.AutorizarEnseres(ordenCompra, Proveedor, (String) sesion.getAttribute("nombre"))) {
                    json.put("msj", true);
                } else {
                    json.put("msj", true);
                }

                sesion.setAttribute("vOrden", "");
                sesion.setAttribute("vRemi", "");
                out.print(json);
                out.close();
                break;
        }

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
