/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.gnk.dao.InterfaceSenderoDao;
import com.gnk.impl.InterfaceSendetoDaoImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Recepción transaccional del ingreso de las compras
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "recepcionTransaccional", urlPatterns = {"/recepcionTransaccional"})
public class recepcionTransaccional extends HttpServlet {

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

        String accion = request.getParameter("accion");
        HttpSession sesion = request.getSession(true);
        JSONArray jsona;
        ServletContext conexto = request.getServletContext();
        String vOrden = request.getParameter("vOrden");
        String vRemi = request.getParameter("vRemi");
        String vFac = request.getParameter("vFac");

        switch (accion) {
            case "obtenerIdReg":
                try (PrintWriter out = response.getWriter()) {
                    InterfaceSenderoDao consultaDatos = new InterfaceSendetoDaoImpl();
                    jsona = consultaDatos.getRegistro(vOrden, vRemi);
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

        switch (accion) {
            case "Editar":
                int id = Integer.parseInt(request.getParameter("id"));
                JSONObject json = new JSONObject();
                InterfaceSenderoDao editar = new InterfaceSendetoDaoImpl();
                json = editar.datosAditar(id);
                out.print(json);
                out.close();

                break;
           
            case "EditarLotes":
                json = new JSONObject();
                int tarimas = 0,
                 tarimasI = 0,
                 cajas = 0,
                 pzacaja = 0,
                 cajasi = 0,
                 resto = 0;
                id = Integer.parseInt(request.getParameter("id"));
                String lote = request.getParameter("lote");
                String caducidad = request.getParameter("caducidad");
                String tarimas1 = request.getParameter("tarimas");
                String cajas1 = request.getParameter("cajas");
                String pzacaja1 = request.getParameter("pzacaja");
                String cajasi1 = request.getParameter("cajasi");
                String resto1 = request.getParameter("resto");
                String costo1 = request.getParameter("costo");
                String factorEmpaque = request.getParameter("factorEmpaque");

                if ((tarimas1.equals("")) || (tarimas1 == null)) {
                    tarimas1 = "0";
                }
                tarimas1 = tarimas1.replace(",", "");
                if ((cajas1.equals("")) || (cajas1 == null)) {
                    cajas1 = "0";
                }
                cajas1 = cajas1.replace(",", "");
                if ((costo1.equals("")) || (costo1 == null)) {
                    costo1 = "0";
                }
                costo1 = costo1.replace(",", "");
                if ((pzacaja1.equals("")) || (pzacaja1 == null)) {
                    pzacaja1 = "0";
                }
                pzacaja1 = pzacaja1.replace(",", "");
                if ((cajasi1.equals("")) || (cajasi1 == null)) {
                    cajasi1 = "0";
                }
                cajasi1 = cajasi1.replace(",", "");
                if ((resto1.equals("")) || (resto1 == null)) {
                    resto1 = "0";
                }
                resto1 = resto1.replace(",", "");
                tarimas = Integer.parseInt(tarimas1);
                cajas = Integer.parseInt(cajas1);
                pzacaja = Integer.parseInt(pzacaja1);
                cajasi = Integer.parseInt(cajasi1);
                resto = Integer.parseInt(resto1);

                //int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                int cantidad = ((((tarimas * cajas) + cajasi) * pzacaja) + resto);

                if (cajasi > 0) {
                    tarimasI = 1;
                }

                String cb = request.getParameter("cb");
                String marca = request.getParameter("marca");
                String usuario = request.getParameter("usuario");
                InterfaceSenderoDao editarUpdate = new InterfaceSendetoDaoImpl();
               // boolean save = editarUpdate.ActualizarDatos(usuario, lote, caducidad, cantidad, cb, marca, id, tarimas, cajas, pzacaja, cajasi, resto, tarimasI, costo1);
                
                boolean save = editarUpdate.ActualizarDatos(usuario, lote, caducidad, cantidad, cb, marca, id, tarimas, cajas, pzacaja, cajasi, resto, tarimasI, costo1, Integer.parseInt(factorEmpaque));
                json.put("msj", save);
                out.print(json);
                out.close();
                break;
              
                //////editar´para reguardo
                 case "EditarLotesRes":
                json = new JSONObject();
         
                 cajas = 0;
                id = Integer.parseInt(request.getParameter("id"));
                lote = request.getParameter("lote");
                caducidad = request.getParameter("caducidad");
                tarimas1 = request.getParameter("tarimas");
                cajas1 = request.getParameter("cajas");
                pzacaja1 = request.getParameter("pzacaja");
                cajasi1 = request.getParameter("cajasi");
                resto1 = request.getParameter("resto");
                costo1 = request.getParameter("costo");
                factorEmpaque = request.getParameter("factorEmpaque");

                if ((tarimas1.equals("")) || (tarimas1 == null)) {
                    tarimas1 = "0";
                }
                tarimas1 = tarimas1.replace(",", "");
                if ((cajas1.equals("")) || (cajas1 == null)) {
                    cajas1 = "0";
                }
                cajas1 = cajas1.replace(",", "");
                if ((costo1.equals("")) || (costo1 == null)) {
                    costo1 = "0";
                }
                costo1 = costo1.replace(",", "");
                tarimas = Integer.parseInt(cajas1);
                cajas = Integer.parseInt(cajas1);
                pzacaja = Integer.parseInt(cajas1);
                cantidad = cajas;
                cb = request.getParameter("cb");
                marca = request.getParameter("marca");
                usuario = request.getParameter("usuario");
                InterfaceSenderoDao editarUpdateRes = new InterfaceSendetoDaoImpl();
                 
                boolean save2 = editarUpdateRes.ActualizarDatosRes(usuario, lote, caducidad, cantidad, cb, marca, id, tarimas, cajas, costo1);
                json.put("msj", save2);
                out.print(json);
                out.close();
                break;
                
                
                
                                //liberacion de OC
            case "IngresarRemision":{
                json = new JSONObject();
                String ordenCompra = request.getParameter("ordenCompra");
                String remision = request.getParameter("remision");
                String usuarioVal= (String) request.getSession().getAttribute("nombre");
                String UbicaN = request.getParameter("UbicaN");
                InterfaceSenderoDao ingresarLermaSendero = new InterfaceSendetoDaoImpl();
                boolean actualizado = false;
                if (ingresarLermaSendero.Actualizarlerma(ordenCompra, remision, usuarioVal, UbicaN)) {
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
            case "IngresarRemisionExtra":{
                json = new JSONObject();
                String IdUsu = request.getParameter("IdUsu");
                InterfaceSenderoDao ingresarremisionExtra = new InterfaceSendetoDaoImpl();
                boolean actualizadoExtra = false;
                String usuarioVal= (String) request.getSession().getAttribute("nombre");
                if (ingresarremisionExtra.IngresarExtra(IdUsu, usuarioVal)) {
                    sesion.setAttribute("NoCompra", "");
                    sesion.setAttribute("proveedor", "");
                    json.put("msj", true);
                } else {
                    json.put("msj", true);
                }

                out.print(json);
                out.close();
                break;
            }
            case "IngresarRemisionCross":{
                json = new JSONObject();
                String ordenCompraCross = request.getParameter("ordenCompra");
                String remisionCross = request.getParameter("remision");
                String usuarioVal= (String) request.getSession().getAttribute("nombre");
                InterfaceSenderoDao ingresarLermaSenderoCross = new InterfaceSendetoDaoImpl();
                boolean actualizadoCross = false;
                if (ingresarLermaSenderoCross.ActualizarlermaCross(ordenCompraCross, remisionCross, usuarioVal)) {
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
            case "RegistrarDatosParcial":
                json = new JSONObject();
                String IdReg = request.getParameter("IdReg");
                String ordenCompraP = request.getParameter("vOrden");
                String remisionP = request.getParameter("vRemi");
                String UbicaN = request.getParameter("UbicaN");
                InterfaceSenderoDao ingresarParcial = new InterfaceSendetoDaoImpl();
                String usuarioVal= (String) request.getSession().getAttribute("nombre");
                boolean IngParcial = ingresarParcial.IngresoParcial(IdReg, ordenCompraP, remisionP, usuarioVal,UbicaN);
                json.put("msj", IngParcial);

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
