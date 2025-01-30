/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import conn.*;
import Inventario.Devoluciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servlets.Facturacion;
import ISEM.*;

/**
 * Aplicación de devoluciones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class DevolucionGlobal extends HttpServlet {

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
//        ConectionDB_SQLServer Modula = new ConectionDB_SQLServer();
        ConectionDB con = new ConectionDB();
//        Devoluciones dev = new Devoluciones();
        //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
        Facturacion fact = new Facturacion();
        NuevoISEM objSql = new NuevoISEM();
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);

        try {
            if (request.getParameter("accion").equals("DevoGlobal")) {
                String F_ClaDoc = request.getParameter("fol_gnkl");
                try {
                    con.conectar();
                    con.actualizar("DELETE FROM tb_devoglobal WHERE F_ClaDoc='" + request.getParameter("fol_gnkl") + "';");
                    ResultSet rset1 = con.consulta("SELECT F.F_ClaPro,M.F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc='" + request.getParameter("fol_gnkl") + "' AND F_CantSur>0 AND F.F_StsFact='A' GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;");
                    while (rset1.next()) {
                        con.insertar("INSERT INTO tb_devoglobal VALUES('" + rset1.getString(1) + "','" + rset1.getString(2) + "','" + rset1.getString(3) + "','" + rset1.getString(4) + "','" + rset1.getString(5) + "','" + rset1.getString(6) + "','" + rset1.getString(7) + "','" + rset1.getString(7) + "','" + rset1.getString(8) + "','" + rset1.getString(9) + "','" + rset1.getString(10) + "','" + rset1.getString(11) + "');");
                    }
                    sesion.setAttribute("Folio", request.getParameter("fol_gnkl"));
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    response.sendRedirect("devolucionesFacturasGlobal.jsp");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Eliminar")) {
                String IdFol = request.getParameter("IdFol");
                String fol_gnkl = request.getParameter("fol_gnkl");
                try {
                    con.conectar();
                    con.actualizar("DELETE FROM tb_devoglobal WHERE F_ClaDoc='" + fol_gnkl + "' AND F_IdFact='" + IdFol + "';");

                    sesion.setAttribute("Folio", request.getParameter("fol_gnkl"));
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    response.sendRedirect("devolucionesFacturasGlobal.jsp");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Guardar")) {
                String fol_gnkl = request.getParameter("fol_gnkl");

                int IdFact = 0, CantDevo = 0, Lote = 0, CantReq = 0, CantSur = 0, DevoCant = 0, IdLote = 0, IdLote1 = 0, ExiLot = 0, Total = 0, Contar = 0, proyecto=0;
                double Iva = 0.00, Monto = 0.00, Costo = 0.00, IvaDeVo = 0.00, IvaTot = 0.00, IvaTotDev = 0.00, MontoDev = 0.00;
                String ClaCli = "", ClaPro = "", FecEnt = "", Ubicacion = "", Obs = "", ClaLot = "", FecCad = "", FecFab = "", Cb = "", ClaMar = "", Origen = "", ClaOrg = "";

                byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                Obs = (new String(a, "UTF-8")).toUpperCase();
                try {
                    con.conectar();
                    ResultSet rsetContar = con.consulta("SELECT COUNT(F_IdFact) FROM tb_devoglobal WHERE F_ClaDoc='" + fol_gnkl + "';");
                    if (rsetContar.next()) {
                        Contar = rsetContar.getInt(1);
                    }
                    if (Contar > 0) {
                        ResultSet rset = con.consulta("SELECT F_IdFact,F_CantDevo FROM tb_devoglobal WHERE F_ClaDoc='" + fol_gnkl + "';");
                        while (rset.next()) {
                            IdFact = rset.getInt(1);
                            CantDevo = rset.getInt(2);

                            ResultSet RFactura = con.consulta("SELECT F_ClaCli,F_ClaPro,F_CantReq,F_CantSur,F_Costo,F_Iva,F_Monto,F_Lote,F_FecEnt,F_Ubicacion, F_Proyecto FROM tb_factura WHERE F_IdFact='" + IdFact + "' AND F_ClaDoc='" + fol_gnkl + "';");
                            while (RFactura.next()) {
                                ClaCli = RFactura.getString(1);
                                ClaPro = RFactura.getString(2);
                                CantReq = RFactura.getInt(3);
                                CantSur = RFactura.getInt(4);
                                Costo = RFactura.getDouble(5);
                                Iva = RFactura.getDouble(6);
                                Monto = RFactura.getDouble(7);
                                Lote = RFactura.getInt(8);
                                FecEnt = RFactura.getString(9);
                                Ubicacion = RFactura.getString(10);
                                proyecto = RFactura.getInt(11);
                            }
                            DevoCant = CantSur - CantDevo;

                            if (DevoCant == 0) {
                                ResultSet LoteDev = con.consulta("SELECT F_IdLote,F_ExiLot,F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Ubica='" + Ubicacion + "' AND F_Proyecto = '" + proyecto + "';");
                                if (LoteDev.next()) {
                                    IdLote = LoteDev.getInt(1);
                                    ExiLot = LoteDev.getInt(2);
                                    ClaOrg = LoteDev.getString(3);
                                }

                                con.actualizar("UPDATE tb_factura SET F_StsFact='C',F_Obs='" + Obs + "',F_DocAnt='1' WHERE F_IdFact='" + IdFact + "' AND F_ClaDoc='" + fol_gnkl + "';");
                                con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + fol_gnkl + "','4','" + ClaPro + "','" + CantDevo + "','" + Costo + "','" + Monto + "','1','" + Lote + "','" + Ubicacion + "','" + ClaOrg + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','');");

                                if (IdLote > 0) {
                                    Total = ExiLot + CantDevo;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Total + "' WHERE F_IdLote='" + IdLote + "' AND F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Ubica='" + Ubicacion + "';");
                                    IdLote = 0;
                                    Total = 0;
                                    ExiLot = 0;
                                    CantDevo = 0;
                                } else {
                                    ResultSet RLote = con.consulta("SELECT F_IdLote,F_ExiLot,F_ClaLot,F_FecCad,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Proyecto= '" + proyecto + "';");
                                    if (RLote.next()) {
                                        IdLote1 = RLote.getInt(1);
                                        ExiLot = RLote.getInt(2);
                                        ClaLot = RLote.getString(3);
                                        FecCad = RLote.getString(4);
                                        FecFab = RLote.getString(5);
                                        Cb = RLote.getString(6);
                                        ClaMar = RLote.getString(7);
                                        Origen = RLote.getString(8);
                                        ClaOrg = RLote.getString(9);
                                    }
                                    if (IdLote1 > 0) {
                                        con.insertar("INSERT INTO tb_lote VALUES(0,'" + ClaPro + "','" + ClaLot + "','" + FecCad + "','" + CantDevo + "','" + Lote + "','" + ClaOrg + "','" + Ubicacion + "','" + FecFab + "','" + Cb + "','" + ClaMar + "','" + Origen + "','" + ClaOrg + "','131');");
                                        IdLote1 = 0;
                                    }
                                }

                            } else if (DevoCant > 0) {
                                if (Iva > 0) {
                                    IvaDeVo = 0.16;
                                } else {
                                    IvaDeVo = 0.00;
                                }
                                IvaTot = ((DevoCant * Costo) * IvaDeVo);
                                Monto = (DevoCant * Costo) + IvaTot;

                                IvaTotDev = ((CantDevo * Costo) * IvaDeVo);
                                MontoDev = ((CantDevo * Costo) * IvaTotDev);

                                ResultSet LoteDev = con.consulta("SELECT F_IdLote,F_ExiLot,F_ClaLot,F_FecCad,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Ubica='" + Ubicacion + "' AND F_Proyecto= '" + proyecto + "';");
                                if (LoteDev.next()) {
                                    IdLote = LoteDev.getInt(1);
                                    ExiLot = LoteDev.getInt(2);
                                    ClaOrg = LoteDev.getString(9);
                                }

                                con.actualizar("UPDATE tb_factura SET F_CantSur='" + DevoCant + "',F_Iva='" + IvaTot + "',F_Monto='" + Monto + "' WHERE F_IdFact='" + IdFact + "' AND F_ClaDoc='" + fol_gnkl + "';");
                                con.insertar("INSERT INTO tb_factura VALUES(0,'" + fol_gnkl + "','" + ClaCli + "','C',CURDATE(),'" + ClaPro + "','0','" + CantDevo + "','" + Costo + "','" + IvaTotDev + "','" + MontoDev + "','" + Lote + "','" + FecEnt + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','" + Obs + "','1');");
                                con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + fol_gnkl + "','4','" + ClaPro + "','" + CantDevo + "','" + Costo + "','" + MontoDev + "','1','" + Lote + "','" + Ubicacion + "','" + ClaOrg + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','');");

                                if (IdLote > 0) {
                                    Total = ExiLot + CantDevo;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Total + "' WHERE F_IdLote='" + IdLote + "' AND F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Ubica='" + Ubicacion + "';");
                                    IdLote = 0;
                                    Total = 0;
                                    ExiLot = 0;
                                    CantDevo = 0;
                                } else {
                                    ResultSet RLote = con.consulta("SELECT F_IdLote,F_ExiLot,F_ClaLot,F_FecCad,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + ClaPro + "' AND F_FolLot='" + Lote + "' AND F_Proyecto= '" + proyecto + "';");
                                    if (RLote.next()) {
                                        IdLote1 = RLote.getInt(1);
                                        ExiLot = RLote.getInt(2);
                                        ClaLot = RLote.getString(3);
                                        FecCad = RLote.getString(4);
                                        FecFab = RLote.getString(5);
                                        Cb = RLote.getString(6);
                                        ClaMar = RLote.getString(7);
                                        Origen = RLote.getString(8);
                                        ClaOrg = RLote.getString(9);
                                    }
                                    if (IdLote1 > 0) {
                                        con.insertar("INSERT INTO tb_lote VALUES(0,'" + ClaPro + "','" + ClaLot + "','" + FecCad + "','" + CantDevo + "','" + Lote + "','" + ClaOrg + "','" + Ubicacion + "','" + FecFab + "','" + Cb + "','" + ClaMar + "','" + Origen + "','" + ClaOrg + "','131');");
                                        IdLote1 = 0;
                                    }
                                }
                            }

                            con.actualizar("DELETE FROM tb_devoglobal WHERE F_IdFact='" + IdFact + "' AND F_ClaDoc='" + fol_gnkl + "';");
                            int Existencia = 0;
                            ResultSet Consulta = con.consulta("SELECT SUM(F_CantSur) FROM tb_factura WHERE F_ClaDoc='" + fol_gnkl + "' AND F_StsFact='A';");
                            if (Consulta.next()) {
                                Existencia = Consulta.getInt(1);
                            }
                            if (Existencia == 0) {
                                con.actualizar("UPDATE tb_factura SET F_StsFact='C' WHERE F_ClaDoc='" + fol_gnkl + "';");
                            }
                            Existencia = 0;
                        }
                        sesion.setAttribute("Folio", request.getParameter("fol_gnkl"));
                        //Aqui tenemos que poner en nulo la variable de folio de dactura
                        response.sendRedirect("devolucionesFacturasGlobal.jsp");
                    } else {
                        out.println("<script>alert('No existe datos a Devolver')</script>");
                        out.println("<script>window.history.back()</script>");
                    }

                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Modificar")) {
                String Devolver = request.getParameter("Devolver");
                int Surtida = Integer.parseInt(request.getParameter("Surtida"));
                int Identi = Integer.parseInt(request.getParameter("Identi"));
                String Folio = request.getParameter("Folio");
                int Cantidad = 0, Diferencia = 0;
                try {
                    con.conectar();
                    if (!(Devolver == "")) {
                        Cantidad = Integer.parseInt(Devolver);
                        if (Cantidad == 0) {
                            out.println("<script>alert('Cantidad a Devolver debe ser Mayor a Cero')</script>");
                            out.println("<script>window.history.back()</script>");
                        } else {
                            Diferencia = Surtida - Cantidad;
                            if (Diferencia < 0) {
                                out.println("<script>alert('Cantidad a Devolver es Mayor a lo Surtido')</script>");
                                out.println("<script>window.history.back()</script>");
                            } else {
                                con.actualizar("UPDATE tb_devoglobal SET F_CantDevo = '" + Cantidad + "' WHERE F_IdFact='" + Identi + "' AND F_ClaDoc='" + Folio + "';");
                                sesion.setAttribute("Folio", Folio);
                                response.sendRedirect("devolucionesFacturasGlobal.jsp");
                            }

                        }

                    } else {
                        out.println("<script>alert('Ingrese Datos')</script>");
                        out.println("<script>window.history.back()</script>");
                    }

                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (Exception e) {
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
