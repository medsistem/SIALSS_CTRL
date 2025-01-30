/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.gnk.impl.ExistenciaProyectoDaoImpl;
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
 * @author CEDIS TOLUCA3
 */
@WebServlet(name = "ExistenciaProyecto", urlPatterns = {"/ExistenciaProyecto"})
public class ExistenciaProyecto extends HttpServlet {

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
        JSONArray jsona;
        String accion = request.getParameter("accion");
        String Proyecto = request.getParameter("Proyecto");
        String ProyectoCL = request.getParameter("ProyectoCL");
        String Tipo = request.getParameter("Tipo");

        switch (accion) {

            case "obtenerProyectos":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.ObtenerProyectos();
                    out.println(jsona);
                }
                break;

            case "obtenerProyectosCliente":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.ObtenerProyectosClientes(ProyectoCL);
                    out.println(jsona);
                }
                break;

            case "obtenerProyectosConsultas":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.ObtenerProyectosConsulta(Proyecto);
                    out.println(jsona);
                }
                break;
             case "obtenerProyectosCompras":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.ObtenerProyectosCompras();
                    out.println(jsona);
                }
                break;    

            case "MostrarRegistros":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarRegistros(Proyecto, Tipo);
                    out.println(jsona);
                }
                break;

            case "MostrarRegistrosCompra":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarRegistrosCompra(Proyecto, Tipo);
                    out.println(jsona);
                }
                break;

            case "MostrarTodos":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarTodos(Tipo);
                    out.println(jsona);
                }
                break;

            case "MostrarTodosClientes":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarTodosClientes(ProyectoCL);
                    out.println(jsona);
                }
                break;

            case "MostrarTodosCompra":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarTodosCompra(Tipo);
                    out.println(jsona);
                }
                break;

            case "MostrarTodosCompraDisp":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarTodosCompraDisp(ProyectoCL);
                    out.println(jsona);
                }
                break;

            case "MostrarTodosConsulta":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarTodosConsulta(Proyecto);
                    out.println(jsona);
                }
                break;

            case "MostrarRegistrosCompraDisp":
                try (PrintWriter out = response.getWriter()) {
                    ExistenciaProyectoDaoImpl consultaDatos = new ExistenciaProyectoDaoImpl();
                    jsona = consultaDatos.MostrarRegistrosCompraDisp(Proyecto);
                    out.println(jsona);
                }
                break;
            /*
            case "ValidarCB":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    FacturacionTranDao validar = new FacturacionTranDaoImpl();
                    boolean validarcb = validar.ValidarCB(Usuario, ClaUni, CB);
                    json.put("msj", validarcb);
                    out.print(json);
                    out.close();
                }
                break;

            

            case "EliminarRegistro":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    FacturacionTranDao Eliminar = new FacturacionTranDaoImpl();
                    boolean Eliminarcb = Eliminar.EliminaRegistro(ClaUni, CB);
                    json.put("msj", Eliminarcb);
                    out.print(json);
                    out.close();
                }
                break;

            case "ConformarFactTemp":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                    FacturacionTranDao registrarFactTemp = new FacturacionTranDaoImpl();
                    boolean RegisFacTemp = registrarFactTemp.ConfirmarFactTemp(Usuario, Observaciones, Tipo, ClaUni);
                    json.put("msj", RegisFacTemp);
                    out.print(json);
                    out.close();
                }
                break;

            case "obtenerIdReg":
                try (PrintWriter out = response.getWriter()) {
                    FacturacionTranDaoImpl consultaDatos = new FacturacionTranDaoImpl();
                    jsona = consultaDatos.getRegistro(Clapro);
                    out.println(jsona);
                }
                break;

            case "obtenerIdRegFact":
                try (PrintWriter out = response.getWriter()) {
                    int Catalogo = Integer.parseInt(request.getParameter("Catalogo"));
                    FacturacionTranDaoImpl consultaDatos = new FacturacionTranDaoImpl();
                    jsona = consultaDatos.getRegistroFact(ClaUni, Catalogo);
                    out.println(jsona);
                }
                break;

            case "obtenerIdRegFactAuto":
                try (PrintWriter out = response.getWriter()) {
                    String Catalogo = request.getParameter("Catalogo");
                    FacturacionTranDaoImpl consultaDatos = new FacturacionTranDaoImpl();
                    jsona = consultaDatos.getRegistroFactAuto(ClaUni, Catalogo);
                    out.println(jsona);
                }
                break;

            case "RegresarCaptura":
                sesion.setAttribute("ClaProFM", "");
                sesion.setAttribute("DesProFM", "");
                response.sendRedirect("facturacionManual.jsp");
                break;

            case "TerminoCaptura":
                sesion.setAttribute("ClaCliFM", "");
                sesion.setAttribute("FechaEntFM", "");
                sesion.setAttribute("ClaProFM", "");
                sesion.setAttribute("DesProFM", "");
                sesion.setAttribute("F_IndGlobal", null);
                response.sendRedirect("facturacionManual.jsp");
                break;

            case "ActualizaReqId":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    int Catalogo = Integer.parseInt(request.getParameter("Catalogo"));
                    int Cantidad = Integer.parseInt(request.getParameter("Cantidad"));
                    int IdREg = Integer.parseInt(request.getParameter("IdRegistro"));
                    byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                    System.out.println("Obs=" + Observaciones);
                    FacturacionTranDao Requerimiento = new FacturacionTranDaoImpl();
                    //System.out.println("ClaUni=" + ClaUnidad + " ClaPro=" + ClaPro + " Cantidad=" + Cantidad + " Catalogo=" + Catalogo);
                    boolean ActRequerimiento = Requerimiento.ActualizaREQ(ClaUnidad, ClaPro, Cantidad, Catalogo, IdREg, Observaciones);
                    json.put("msj", ActRequerimiento);
                    out.print(json);
                    out.close();
                }
                break;
            case "GeneraFolio":
                try (PrintWriter out = response.getWriter()) {
                    json = new JSONObject();
                    int Catalogo = Integer.parseInt(request.getParameter("Catalogo"));
                    FacturacionTranDao RegistroFolio = new FacturacionTranDaoImpl();
                    //System.out.println("ClaUni=" + ClaUnidad + " ClaPro=" + ClaPro + " Cantidad=" + Cantidad + " Catalogo=" + Catalogo);
                    if (Obs == null) {
                        Obs = "";
                    }
                    boolean RegistrarFolio = RegistroFolio.RegistrarFolios(ClaUnidad, Catalogo, Tipo, FecEnt, Usuario, Obs);
                    json.put("msj", RegistrarFolio);
                    out.print(json);
                    out.close();
                }
                break;
             */
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
        String FolioFact = (String) sesion.getAttribute("F_IndGlobal");
        String FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        String ClaUni = (String) sesion.getAttribute("ClaCliFM");
        String ClaUnidad = request.getParameter("ClaUni");
        String ClaPro = request.getParameter("ClaPro");

        String[] F_CLaCliArray = ClaUni.split(" - ");
        ClaUni = F_CLaCliArray[0];
        JSONObject json = new JSONObject();

        switch (accion) {
            /*
            case "RegistrarDatos":
                json = new JSONObject();
                String IdLote = request.getParameter("IdLote");
                int CantMov = Integer.parseInt(request.getParameter("CantMov"));
                FacturacionTranDao facturacion = new FacturacionTranDaoImpl();
                boolean insertFacTemp = facturacion.RegistraDatosFactTemp(FolioFact, ClaUni, IdLote, CantMov, FechaEnt, Usuario);
                json.put("msj", insertFacTemp);
                out.print(json);
                out.close();
                break;

            case "ActualizaReq":
                json = new JSONObject();
                int Catalogo = Integer.parseInt(request.getParameter("Catalogo"));
                int Cantidad = Integer.parseInt(request.getParameter("Cantidad"));
                FacturacionTranDao Requerimiento = new FacturacionTranDaoImpl();
                System.out.println("ClaUni=" + ClaUnidad + " ClaPro=" + ClaPro + " Cantidad=" + Cantidad + " Catalogo=" + Catalogo);
                //boolean ActRequerimiento = Requerimiento.ActualizaREQ(ClaUnidad, ClaPro, Cantidad, Catalogo);
                //json.put("msj", ActRequerimiento);
                out.print(json);
                out.close();
                break;
             */
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
