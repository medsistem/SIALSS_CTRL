/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Marbetes;

import com.gnk.dao.marbetesDao;
import com.gnk.impl.marbeteDaoImpl;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author juan
 */
@WebServlet(name = "marbetesGenerar", urlPatterns = {"/marbetesGenerar"})
public class marbetesGenerar extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet marbetesGenerar</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet marbetesGenerar at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

        int accion = Integer.parseInt(request.getParameter("ban"));
        switch (accion) {
            case 0:
                try {
                int folio = Integer.parseInt(request.getParameter("folio"));
                int Proyecto = Integer.parseInt(request.getParameter("Proyecto"));
                Map marbeteMap = new LinkedHashMap();
                marbetesDao marbete = new marbeteDaoImpl();
                String nombreUnidad = marbete.nombreUnidad(folio, Proyecto);
                int posi = nombreUnidad.indexOf('/');
                String Cantidad = nombreUnidad.substring(0, posi);
                nombreUnidad = nombreUnidad.substring(posi + 1, nombreUnidad.length());
//                    marbeteMap.put("unidad", nombreUnidad);
//                    marbeteMap.put("RF", Cantidad);

                if (nombreUnidad.contains("/RF")) {
                    marbeteMap.put("unidad", nombreUnidad);
                    marbeteMap.put("RF", Cantidad);
                    marbeteMap.put("Ct", 0);
                } else if (nombreUnidad.contains("/Cont")) {
                    marbeteMap.put("unidad", nombreUnidad);
                    marbeteMap.put("Ct", Cantidad);
                    marbeteMap.put("RF", 0);
                } else {
                    marbeteMap.put("unidad", nombreUnidad);
                    marbeteMap.put("RF", 0);
                    marbeteMap.put("Ct", 0);
                }

                String json1;
                json1 = new Gson().toJson(marbeteMap);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json1);

            } catch (NumberFormatException | IOException e) {
                Logger.getLogger(marbetesGenerar.class.getName()).log(Level.SEVERE, null, e);
            }
            break;
            case 1:
                try {
                int folio = Integer.parseInt(request.getParameter("folio"));
                int marbeteCantidad = Integer.parseInt(request.getParameter("marbetes"));
                String unidad = request.getParameter("unidad");
                int Proyecto = Integer.parseInt(request.getParameter("Proyecto"));
                String rutaParam = request.getParameter("ruta");
                Map marbeteMap = new LinkedHashMap();
                marbetesDao marbete = new marbeteDaoImpl();
                    System.out.println("rutaParam: "+rutaParam);
                boolean guardado = marbete.insertar(unidad, folio, marbeteCantidad, Proyecto, rutaParam);
                if (guardado) {
                    marbeteMap.put("msj", "realizado");
                } else {
                    marbeteMap.put("msj", "fallo");
                }
                String json1;
                json1 = new Gson().toJson(marbeteMap);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json1);

            } catch (NumberFormatException | IOException e) {
                Logger.getLogger(marbetesGenerar.class.getName()).log(Level.SEVERE, null, e);
            }
            break;

            case 2:
                 try {
                int folio = Integer.parseInt(request.getParameter("folio"));
                //int folio2 = Integer.parseInt(request.getParameter("folio2"));
                int marbeteCantidad = Integer.parseInt(request.getParameter("marbetes"));
                String unidad = request.getParameter("unidad");
                int Proyecto = Integer.parseInt(request.getParameter("Proyecto"));
                String rutaParam = request.getParameter("ruta");
                Map marbeteMap = new LinkedHashMap();
                marbetesDao marbete = new marbeteDaoImpl();
                boolean guardado = marbete.insertarSur(unidad, folio , marbeteCantidad, Proyecto, rutaParam);
                if (guardado) {
                    marbeteMap.put("msj", "realizado");
                } else {
                    marbeteMap.put("msj", "fallo");
                }
                String json1;
                json1 = new Gson().toJson(marbeteMap);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json1);

            } catch (NumberFormatException | IOException e) {
                Logger.getLogger(marbetesGenerar.class.getName()).log(Level.SEVERE, null, e);
            }
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
