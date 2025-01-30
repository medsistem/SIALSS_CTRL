/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lerma;

import conn.ConectionDB;
//import conn.ConectionDB_Linux;
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
 * Envío de información de lerma a sendero
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class EnvioFolioLerma extends HttpServlet {

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
//        ConectionDB_Linux con = new ConectionDB_Linux();
        ConectionDB ConSendero = new ConectionDB();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        String Folio = "", Folio1 = "", Folio2 = "", Fecha1 = "", Fecha2 = "", Clave = "", Clave1 = "", Provee = "", ClaProvee = "";
        int Origen = 0, Origen1 = 0, Contar = 0, ContarProv = 0, ClaProve = 0;
        ResultSet Consulta = null;
        ResultSet ConsultaS = null;
        ResultSet ConsultaProv = null;
        try {
//            con.conectar();
            ConSendero.conectar();
            if (request.getParameter("accion").equals("Envio")) {
                Folio = request.getParameter("FolioS");

                ConsultaS = ConSendero.consulta("SELECT COUNT(F_OrdCom) FROM tb_compratempcc WHERE F_OrdCom='" + Folio + "' GROUP BY F_OrdCom;");
                if (ConsultaS.next()) {
                    Contar = ConsultaS.getInt(1);
                }
                if (Contar > 0) {
                    ConSendero.actualizar("DELETE FROM tb_compratempcc WHERE F_OrdCom='" + Folio + "';");

//                    Consulta = con.consulta("SELECT f.F_FecApl,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_FecFab,l.F_ClaMar,l.F_ClaOrg AS F_Provee,l.F_Cb,'1' AS F_Tarimas,'1' AS F_Cajas,"
//                            + "SUM(f.F_CantSur) AS F_Pz,'0' AS F_TarimasI,'0' AS F_CajasI,'0' AS F_Resto,f.F_Costo,SUM(f.F_Costo* f.F_CantSur) AS F_Impto,SUM(f.F_Costo* f.F_CantSur) AS F_Comtot,"
//                            + "'Se envía desde SAA CC' AS F_Obser,f.F_ClaDoc AS F_FolRemi,f.F_ClaDoc AS F_OrdCom,l.F_ClaOrg,'sistemas','2' AS F_Estado,l.F_Origen,SUM(f.F_CantSur) AS F_CantSur "
//                            + "FROM tb_factura f INNER JOIN tb_lote l ON f.F_ClaPro=l.F_ClaPro AND f.F_Lote=l.F_FolLot AND f.F_Ubicacion=l.F_Ubica WHERE f.F_ClaDoc='" + Folio + "' AND F_StsFact='A' AND f.F_CantSur>0 "
//                            + "GROUP BY f.F_ClaDoc,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_Origen;");
//                    while (Consulta.next()) {
//                        Origen = Consulta.getInt(24);
//                        Clave = Consulta.getString(2);
//                        if ((Origen == 100) || (Origen == 200)) {
//                            Origen1 = 2;
//                        } else {
//                            Origen1 = Origen;
//                        }
//
//                        if (Clave.equals("0801.01")) {
//                            Clave1 = "801.01";
//                        } else {
//                            Clave1 = Clave;
//                        }
//
//                        if (!(Clave1.equals("2111"))) {
//                            ConSendero.insertar("INSERT INTO tb_compratempcc VALUES(0,'" + Consulta.getString(1) + "','" + Clave1 + "','" + Consulta.getString(3) + "','" + Consulta.getString(4) + "','" + Consulta.getString(5) + "','" + Consulta.getString(6) + "','" + Consulta.getString(7) + "','" + Consulta.getString(8) + "','" + Consulta.getString(9) + "','" + Consulta.getString(10) + "',"
//                                    + "'" + Consulta.getString(11) + "','" + Consulta.getString(12) + "','" + Consulta.getString(13) + "','" + Consulta.getString(14) + "','" + Consulta.getString(15) + "','" + Consulta.getString(16) + "','" + Consulta.getString(17) + "','" + Consulta.getString(18) + "','" + Consulta.getString(19) + "','" + Consulta.getString(20) + "','" + Consulta.getString(21) + "','" + (String) sesion.getAttribute("nombre") + "'"
//                                    + ",'" + Consulta.getString(23) + "','" + Origen1 + "','" + Consulta.getString(25) + "', curtime());");
//                        }
//                        Origen1 = 0;
//                        Clave1 = "";
//                    }
                    out.println("<script>alert('La información se guardo correctamente')</script>");
                    out.println("<script>window.history.back()</script>");

                } else {
//                    Consulta = con.consulta("SELECT f.F_FecApl,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_FecFab,l.F_ClaMar,l.F_ClaOrg AS F_Provee,l.F_Cb,'1' AS F_Tarimas,'1' AS F_Cajas,"
//                            + "SUM(f.F_CantSur) AS F_Pz,'0' AS F_TarimasI,'0' AS F_CajasI,'0' AS F_Resto,f.F_Costo,SUM(f.F_Costo* f.F_CantSur) AS F_Impto,SUM(f.F_Costo* f.F_CantSur) AS F_Comtot,"
//                            + "'Se envía desde SAA CC' AS F_Obser,f.F_ClaDoc AS F_FolRemi,f.F_ClaDoc AS F_OrdCom,l.F_ClaOrg,'sistemas','2' AS F_Estado,l.F_Origen,SUM(f.F_CantSur) AS F_CantSur "
//                            + "FROM tb_factura f INNER JOIN tb_lote l ON f.F_ClaPro=l.F_ClaPro AND f.F_Lote=l.F_FolLot AND f.F_Ubicacion=l.F_Ubica WHERE f.F_ClaDoc='" + Folio + "' AND F_StsFact='A' "
//                            + "GROUP BY f.F_ClaDoc,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_Origen;");
//                    while (Consulta.next()) {
//                        Origen = Consulta.getInt(24);
//                        Clave = Consulta.getString(2);
//                        if ((Origen == 100) || (Origen == 200)) {
//                            Origen1 = 2;
//                        } else {
//                            Origen1 = Origen;
//                        }
//
//                        if (Clave.equals("0801.01")) {
//                            Clave1 = "801.01";
//                        } else {
//                            Clave1 = Clave;
//                        }
//
//                        if (!(Clave1.equals("2111"))) {
//                            ConSendero.insertar("INSERT INTO tb_compratempcc VALUES(0,'" + Consulta.getString(1) + "','" + Clave1 + "','" + Consulta.getString(3) + "','" + Consulta.getString(4) + "','" + Consulta.getString(5) + "','" + Consulta.getString(6) + "','" + Consulta.getString(7) + "','" + Consulta.getString(8) + "','" + Consulta.getString(9) + "','" + Consulta.getString(10) + "',"
//                                    + "'" + Consulta.getString(11) + "','" + Consulta.getString(12) + "','" + Consulta.getString(13) + "','" + Consulta.getString(14) + "','" + Consulta.getString(15) + "','" + Consulta.getString(16) + "','" + Consulta.getString(17) + "','" + Consulta.getString(18) + "','" + Consulta.getString(19) + "','" + Consulta.getString(20) + "','" + Consulta.getString(21) + "','" + (String) sesion.getAttribute("nombre") + "'"
//                                    + ",'" + Consulta.getString(23) + "','" + Origen1 + "','" + Consulta.getString(25) + "', curtime());");
//                            Origen1 = 0;
//                            Clave1 = "";
//                        }
//                    }
                    out.println("<script>alert('La información se guardo correctamente')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

            if (request.getParameter("accion").equals("EnvioIsem")) {
                Folio = request.getParameter("FolioS");
                Provee = request.getParameter("Provee");
                ClaProvee = request.getParameter("ClaProvee");

                ConsultaProv = ConSendero.consulta("SELECT COUNT(F_ClaProve),F_ClaProve  FROM tb_proveedor WHERE F_NomPro='" + Provee + "';");
                if (ConsultaProv.next()) {
                    ContarProv = ConsultaProv.getInt(1);
                    ClaProve = ConsultaProv.getInt(2);
                }

                if (ContarProv == 0) {
                    ConSendero.insertar("INSERT INTO tb_proveedor VALUES(0,'" + Provee + "','','','','0','','','','','');");

                    ConsultaProv = ConSendero.consulta("SELECT F_ClaProve  FROM tb_proveedor WHERE F_NomPro='" + Provee + "';");
                    if (ConsultaProv.next()) {
                        ClaProve = ConsultaProv.getInt(1);
                    }
                }

                ConsultaS = ConSendero.consulta("SELECT COUNT(F_NoCompra) FROM tb_pedidoisem WHERE F_NoCompra='" + Folio + "' GROUP BY F_NoCompra;");
                if (ConsultaS.next()) {
                    Contar = ConsultaS.getInt(1);
                }
                if (Contar > 0) {
                    ConSendero.actualizar("DELETE FROM tb_pedidoisem WHERE F_NoCompra='" + Folio + "';");
//
//                    Consulta = con.consulta("SELECT F_Clave,F_Cant,F_Obser,F_Fecha,F_FecSur,F_HorSur,F_IdUsu,F_StsPed,F_Recibido FROM tb_pedidoisem  WHERE F_Cedis='SENDERO' AND F_StsPed='1' AND F_NoCompra='" + Folio + "' AND F_Provee='" + ClaProvee + "';");
//                    while (Consulta.next()) {
//
//                        Clave = Consulta.getString(1);
//
//                        if (Clave.equals("0801.01")) {
//                            Clave1 = "801.01";
//                        } else if (Clave.equals("0005")) {
//                            Clave1 = "5";
//                        } else if (Clave.equals("0260.02")) {
//                            Clave1 = "260.02";
//                        } else {
//                            Clave1 = Clave;
//                        }
//
//                        if (!(Clave1.equals("2111"))) {
//                            ConSendero.insertar("INSERT INTO tb_pedidoisem VALUES(0,'" + Folio + "','" + ClaProve + "','" + Clave1 + "','','1-2016','-','0000-00-00','" + Consulta.getString(2) + "','" + Consulta.getString(3) + "','" + Consulta.getString(4) + "','" + Consulta.getString(5) + "','" + Consulta.getString(6) + "','" + (String) sesion.getAttribute("Usuario") + "','" + Consulta.getString(8) + "','0');");
//                        }
//                        Clave1 = "";
//                    }
                    out.println("<script>alert('La información se guardo correctamente')</script>");
                    out.println("<script>window.history.back()</script>");

                } else {
//                    Consulta = con.consulta("SELECT F_Clave,F_Cant,F_Obser,F_Fecha,F_FecSur,F_HorSur,F_IdUsu,F_StsPed,F_Recibido FROM tb_pedidoisem  WHERE F_Cedis='SENDERO' AND F_StsPed='1' AND F_NoCompra='" + Folio + "' AND F_Provee='" + ClaProvee + "';");
//                    while (Consulta.next()) {
//
//                        Clave = Consulta.getString(1);
//
//                        if (Clave.equals("0801.01")) {
//                            Clave1 = "801.01";
//                        } else if (Clave.equals("0005")) {
//                            Clave1 = "5";
//                        } else if (Clave.equals("0260.02")) {
//                            Clave1 = "260.02";
//                        } else {
//                            Clave1 = Clave;
//                        }
//
//                        if (!(Clave1.equals("2111"))) {
//                            ConSendero.insertar("INSERT INTO tb_pedidoisem VALUES(0,'" + Folio + "','" + ClaProve + "','" + Clave1 + "','','1-2016','-','0000-00-00','" + Consulta.getString(2) + "','" + Consulta.getString(3) + "','" + Consulta.getString(4) + "','" + Consulta.getString(5) + "','" + Consulta.getString(6) + "','" + (String) sesion.getAttribute("Usuario") + "','" + Consulta.getString(8) + "','0');");
//                        }
//                        Clave1 = "";
//                    }
                    out.println("<script>alert('La información se guardo correctamente')</script>");
                    out.println("<script>window.location.href = 'reimp_facturaLERMAISEM.jsp';</script>");
                }
            }

            if (request.getParameter("accion").equals("mostrar")) {

                Folio1 = request.getParameter("folio1");
                Folio2 = request.getParameter("folio2");
                Fecha1 = request.getParameter("fecha_ini");
                Fecha2 = request.getParameter("fecha_fin");

                sesion.setAttribute("fecha_ini", Fecha1);
                sesion.setAttribute("fecha_fin", Fecha2);
                sesion.setAttribute("folio1", Folio1);
                sesion.setAttribute("folio2", Folio2);
                response.sendRedirect("reimp_facturaLERMA.jsp");
            }

        } catch (Exception e) {
            Logger.getLogger(EnvioFolioLerma.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
//                con.cierraConexion();
                ConSendero.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(EnvioFolioLerma.class.getName()).log(Level.SEVERE, null, ex);
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
