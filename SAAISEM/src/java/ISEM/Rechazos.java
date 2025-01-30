/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISEM;

import Correo.*;
import conn.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Rechazo a proveedor ORI
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Rechazos extends HttpServlet {

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
//       ConectionDB_Nube con = new ConectionDB_Nube();
        ConectionDB con1 = new ConectionDB();
        DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        PrintWriter out = response.getWriter();

        HttpSession sesion = request.getSession(true);
        try {
            try {

                if (request.getParameter("accion").equals("Rechazar")) {
                    try {
                        CorreoRechaza correo = new CorreoRechaza();
                        con1.conectar();
                        String fechaA = "", horaA = "";
                        String[] claveschk = request.getParameterValues("chkCancela");
                        String claves = "";
                        if (claves != null && claveschk.length > 0) {
                            for (int i = 0; i < claveschk.length; i++) {
                                if (i == claveschk.length - 1) {
                                    claves = claves + claveschk[i];
                                } else {
                                    claves = claves + claveschk[i] + ",";
                                }
                            }
                        }
                        System.out.println(claves);
                        ResultSet rset = con1.consulta("select F_FecSur, F_HorSur from tb_pedidoisem where F_NoCompra = '" + request.getParameter("NoCompraRechazo") + "'");
                        while (rset.next()) {
                            fechaA = rset.getString(1);
                            horaA = rset.getString(2);
                        }
                        byte[] a = request.getParameter("rechazoObser").getBytes("ISO-8859-1");
                        String Observaciones = (new String(a, "UTF-8"));

                        con1.insertar("insert into tb_rechazos values (0,'" + request.getParameter("NoCompraRechazo") + "','" + Observaciones + "', NOW())");
                        con1.insertar("update tb_pedidoisem set F_FecSur = '" + request.getParameter("FechaOrden") + "' , F_HorSur = '" + request.getParameter("HoraOrden") + "' where F_NoCompra = '" + request.getParameter("NoCompraRechazo") + "' ");
                        //con1.insertar("update tb_pedidoisem set F_Recibido = '2' where F_NoCompra = '" + request.getParameter("NoCompraRechazo") + "' ");
                        //correo.enviaCorreo(request.getParameter("NoCompraRechazo"), horaA, fechaA, request.getParameter("correoProvee"), claves);
                        con1.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    response.sendRedirect("compraAuto2.jsp");
                }
                if (request.getParameter("accion").equals("Recalendarizar")) {

                    CorreoCambiaFecha correo = new CorreoCambiaFecha();
                    String folio = request.getParameter("NoRecalendarizar");
                    System.out.println(folio);

                    String fecha1 = request.getParameter("FechaOrden1_" + folio);
                    String fecha2 = request.getParameter("FechaOrden2_" + folio);
                    String hora1 = request.getParameter("HoraOrden1_" + folio);
                    String hora2 = request.getParameter("HoraOrden2_" + folio);
                    String fechaA1 = request.getParameter("FechaA1_" + folio);
                    String fechaA2 = request.getParameter("FechaA2_" + folio);
                    String horaA1 = request.getParameter("HoraA1_" + folio);
                    String horaA2 = request.getParameter("HoraA2_" + folio);
                    String bodega = request.getParameter("Bode_" + folio);
                    String email = request.getParameter("correoProvee_" + folio);

                    String obser = "";
                    byte[] a = request.getParameter("recaObser_" + folio).getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8"));
                    try {
//                        con.conectar();
                        try {
//                            ResultSet rset = con.consulta("select F_Obs from TB_FecEnt where F_Id = '" + folio + "' ");
//                            while (rset.next()) {
//                                obser = rset.getString(1);
//                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
//                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    obser = obser + "\n" + df2.format(new Date()) + ": " + Observaciones;

                    String query = "";
                    if (!fecha1.equals("") && fecha2.equals("")) {
                        query = "update TB_FecEnt set F_F1 = '" + df2.format(df.parse(fecha1)) + "', F_H1 = '" + hora1 + "', F_Obs = '" + obser + "', F_Bodega = '" + bodega + "' where F_Id = '" + folio + "' ;";
                    } else if (!fecha2.equals("") && fecha1.equals("")) {
                        query = "update TB_FecEnt set F_F2 = '" + df2.format(df.parse(fecha2)) + "', F_H2 = '" + hora2 + "', F_Obs = '" + obser + "', F_Bodega = '" + bodega + "' where F_Id = '" + folio + "' ;";
                    } else if (!fecha1.equals("") && fecha2.equals("")) {
                        query = "update TB_FecEnt set F_F1 = '" + df2.format(df.parse(fecha1)) + "', F_H1 = '" + hora1 + "', F_F2 = '" + df2.format(df.parse(fecha2)) + "', F_H2 = '" + hora2 + "', F_Obs = '" + obser + "', F_Bodega = '" + bodega + "' where F_Id = '" + folio + "' ;";
                    }
                    try {
//                        con.conectar();
                        try {
//                            con.insertar(query);
//                            con.insertar("insert into tb_regcambiofechas values (0,'" + sesion.getAttribute("nombre") + "',NOW(),'" + folio + "','" + fecha1 + "','" + hora1 + "','" + fecha2 + "','" + hora2 + "','" + obser + "')");
                            correo.enviaCorreo(folio, (String) sesion.getAttribute("nombre"), email, fechaA1, fechaA2, horaA1, horaA2);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
//                        con.cierraConexion();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    response.sendRedirect("Entrega.jsp");
                }
            } catch (Exception e) {
            }
        } finally {
            out.close();
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
