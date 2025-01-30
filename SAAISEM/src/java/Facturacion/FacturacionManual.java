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
import Modula.AbastoModula;
import Modula.RequerimientoModula;
import in.co.sneh.model.Apartado;
import in.co.sneh.persistance.ApartadoDAOImpl;
import in.co.sneh.model.FolioStatus;
import in.co.sneh.persistance.FolioStatusDAOImpl;
import in.co.sneh.picking.service.PickingService;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proceso de facturacón manual btnClaveTMov
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class FacturacionManual extends HttpServlet {

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
        Devoluciones dev = new Devoluciones();
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
            if (!request.getParameter("accionEliminar").equals("")) {

                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminar") + "'");
                while (rset.next()) {
                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                }
                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminar") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='facturacionManual.jsp'</script>");
            }
        } catch (Exception e) {
        }

        try {
            if (!request.getParameter("accionEliminarExtra").equals("")) {

                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarExtra") + "'");
                while (rset.next()) {
                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                }
                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarExtra") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='facturacionManualExtra.jsp'</script>");
            }
        } catch (Exception e) {
        }

//        try {
//            if (!request.getParameter("accionEliminarSemiCause").equals("")) {
//
//                //sesion.setAttribute("F_IndGlobal", null);
//                con.conectar();
//                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarSemiCause") + "'");
//                while (rset.next()) {
//                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                }
//                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarSemiCause") + "' ");
//                con.cierraConexion();
//                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
//                out.println("<script>window.location='facturacionSemiManual.jsp'</script>");
//            }
//        } catch (Exception e) {
//        }
//        try {
//            if (!request.getParameter("accionEliminarCause").equals("")) {
//
//                //sesion.setAttribute("F_IndGlobal", null);
//                con.conectar();
//                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarCause") + "'");
//                while (rset.next()) {
//                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                }
//                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarCause") + "' ");
//                con.cierraConexion();
//                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
//                out.println("<script>window.location='facturacionManualCause.jsp'</script>");
//            }
//        } catch (Exception e) {
//        }
        try {
            if (!request.getParameter("accionEliminarFOLIO").equals("")) {

                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarFOLIO") + "'");
//                while (rset.next()) {
//                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                }
                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarFOLIO") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='facturacionManualFolio.jsp'</script>");
            }
        } catch (Exception e) {
        }

        try {
            if (!request.getParameter("accionEliminarProducproyecto").equals("")) {

                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarProducproyecto") + "'");
//                while (rset.next()) {
//                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                }
                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarProducproyecto") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='productotransferencia.jsp'</script>");
            }
        } catch (Exception e) {
        }

        try {
            if (!request.getParameter("accionEliminarTrans").equals("")) {
                System.out.println("Eliminar Transfer Clave");
                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarTrans") + "'");
                while (rset.next()) {
                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                }
                con.insertar("delete from tb_facttemp where F_Id = '" + request.getParameter("accionEliminarTrans") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='facturacion/generaTransferencias.jsp'</script>");
            }
        } catch (Exception e) {
        }
        try {
            if (!request.getParameter("accionEliminarMov").equals("")) {
                System.out.println("Eliminar Movimiento Clave");
                //sesion.setAttribute("F_IndGlobal", null);
                con.conectar();
                ResultSet rset = con.consulta("select * from tb_capmovtemp where F_Id = '" + request.getParameter("accionEliminarMov") + "'");
                while (rset.next()) {
                    con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                }
                con.insertar("delete from tb_capmovtemp where F_Id = '" + request.getParameter("accionEliminarMov") + "' ");
                con.cierraConexion();
                out.println("<script>alert('Clave Eliminada Correctamente')</script>");
                out.println("<script>window.location='facturacion/CapturaMovi.jsp'</script>");
            }
        } catch (Exception e) {
            
        }
        
        try {
            if (request.getParameter("accion").equals("reintegrarInsumo")) {
                String F_ClaDoc = request.getParameter("F_ClaDoc");
                try {
                    con.conectar();
                    //consql.conectar();
                    String ClaPro = "", CantSur = "", ClaDoc = "", Proveedor = "", Monto = "", Costo = "", FolLote = "", F_IdFact;
                    int FolioMovi = 0, FolMov, proyecto = 0;
                    ResultSet rset = con.consulta("select f.F_ClaCli, f.F_IdFact, f.F_StsFact, f.F_ClaPro, l.F_ClaLot, l.F_FecCad, DATE_FORMAT(l.F_FecCad,'%d/%m/%Y') AS FECAD, f.F_CantSur, f.F_FecEnt, f.F_Ubicacion, f.F_ClaDoc, l.F_ClaOrg, l.F_FecFab, DATE_FORMAT(l.F_FecFab,'%d/%m/%Y') AS FEFAB, l.F_Cb, l.F_ClaMar, f.F_Monto, f.F_Costo, f.F_Lote, f.F_Iva,l.F_Origen,l.F_UniMed  from tb_factdevol f, tb_lote l where f.F_Lote = l.F_FolLot and f.F_ClaDoc='" + F_ClaDoc + "' and f.F_FactSts = 0 group by f.F_IdFact");
                    while (rset.next()) {
                        CantSur = rset.getString("F_CantSur");
                        ClaDoc = rset.getString("F_ClaDoc");
                        Proveedor = rset.getString("F_ClaOrg");
                        Monto = rset.getString("F_Monto");
                        Costo = rset.getString("F_Costo");
                        FolLote = rset.getString("F_Lote");
                        F_IdFact = rset.getString("F_IdFact");
                        ClaPro = rset.getString("F_ClaPro");
                        proyecto = rset.getInt("F_Proyecto");
                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");

                        /*
                         * Insercion a lotes
                         */
                        String idLote = "";
                        int CantLote = 0;
                        ResultSet rsetLote = con.consulta("select F_IdLote, SUM(F_ExiLot) from tb_lote where F_FolLot ='" + FolLote + "' AND F_Ubica = 'NUEVA' and F_Proyecto = '" + proyecto + "' group by F_IdLote");
                        while (rsetLote.next()) {
                            idLote = rsetLote.getString("F_IdLote");
                            CantLote = rsetLote.getInt(2);
                        }
                        if (idLote != "") {
                            con.insertar("update tb_lote set F_ExiLot ='" + (Integer.parseInt(CantSur) + CantLote) + "' where F_IdLote = '" + idLote + "'");
                        } else {
                            con.insertar("INSERT INTO tb_lote VALUES(0,'" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(8) + "','" + FolLote + "','" + rset.getString(12) + "','NUEVA','" + rset.getString(13) + "','" + rset.getString(15) + "','" + rset.getString(16) + "','" + rset.getString(21) + "','" + rset.getString(12) + "','" + rset.getString(22) + "')");

                        }

                        //Insercion de movimientos
                        con.insertar("insert into tb_movinv values (0,curdate(),'" + ClaDoc + "','3', '" + ClaPro + "', '" + CantSur + "', '" + Costo + "', '" + Monto + "' ,'1', '" + FolLote + "', 'NUEVA', '" + Proveedor + "',curtime(),'" + (String) sesion.getAttribute("nombre") + "','') ");

                        con.insertar("update tb_factdevol set F_FactSts=1 where F_IdFact = '" + F_IdFact + "'");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                sesion.setAttribute("F_IndGlobal", null);
                response.sendRedirect("reimp_factura.jsp");
            }
            if (request.getParameter("accion").equals("devolucion")) {
                String idFact = request.getParameter("IdFact");
                try {
                    con.conectar();
                    int cantDevol = Integer.parseInt(request.getParameter("CantDevolver"));
                    //consql.conectar();
                    String ClaCli = "", StsFact = "", ClaPro = "", ClaLot = "", FecCad = "", CantSur = "", FecEnt = "", Ubicacion = "", ClaDoc = "", Proveedor = "", FecFab = "", CB = "", Marca = "", Monto = "", FecCadSQL = "", FecFabSQL = "", Costo = "", FolLote = "", IVA = "", F_Hora = "", F_User = "";
                    String FolioLoteSQL = "";
                    int FolioMovi = 0, FolMov, FolioMoviSQL = 0, FolMovSQL;
                    ResultSet rset = con.consulta("select f.F_ClaCli, f.F_StsFact, f.F_ClaPro, l.F_ClaLot, l.F_FecCad, DATE_FORMAT(l.F_FecCad,'%d/%m/%Y') AS FECAD, f.F_CantSur, f.F_FecEnt, f.F_Ubicacion, f.F_ClaDoc, l.F_ClaOrg, l.F_FecFab, DATE_FORMAT(l.F_FecFab,'%d/%m/%Y') AS FEFAB, l.F_Cb, l.F_ClaMar, f.F_Monto, f.F_Costo, f.F_Lote, f.F_Iva, f.F_Hora, f.F_User from tb_factura f, tb_lote l where f.F_Lote = l.F_FolLot and f.F_IdFact='" + idFact + "'");
                    while (rset.next()) {
                        ClaCli = rset.getString("F_ClaCli");
                        StsFact = rset.getString("F_StsFact");
                        ClaPro = rset.getString("F_ClaPro");
                        ClaLot = rset.getString("F_ClaLot");
                        FecCad = rset.getString("F_FecCad");
                        CantSur = rset.getString("F_CantSur");
                        FecEnt = rset.getString("F_FecEnt");
                        Ubicacion = rset.getString("F_Ubicacion");
                        ClaDoc = rset.getString("F_ClaDoc");
                        Proveedor = rset.getString("F_ClaOrg");
                        FecFab = rset.getString("F_FecFab");
                        CB = rset.getString("F_Cb");
                        Marca = rset.getString("F_ClaMar");
                        Monto = rset.getString("F_Monto");
                        FecCadSQL = rset.getString("FECAD");
                        FecFabSQL = rset.getString("FEFAB");
                        Costo = rset.getString("F_Costo");
                        FolLote = rset.getString("F_Lote");
                        IVA = rset.getString("F_Iva");
                        F_Hora = rset.getString("F_Hora");
                        F_User = rset.getString("F_User");
                    }
                    byte[] a = request.getParameter("Obser").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();

                    if (Integer.parseInt(CantSur) - cantDevol == 0) {
                        con.insertar("update tb_factura set F_StsFact = 'C', F_Obs='" + Observaciones + "' where F_IdFact = '" + idFact + "'");

                        ResultSet rsetfact = con.consulta("select * from tb_factura where F_IdFact = '" + idFact + "' ");
                        while (rsetfact.next()) {
                            con.insertar("insert into tb_factdevol values ('" + rsetfact.getString(1) + "','" + rsetfact.getString(2) + "','" + rsetfact.getString(3) + "','" + rsetfact.getString(4) + "','" + rsetfact.getString(5) + "','" + rsetfact.getString(6) + "','" + rsetfact.getString(7) + "','" + rsetfact.getString(8) + "','" + rsetfact.getString(9) + "','" + rsetfact.getString(10) + "','" + rsetfact.getString(11) + "','" + rsetfact.getString(12) + "','" + rsetfact.getString(13) + "','" + rsetfact.getString(14) + "','" + rsetfact.getString(15) + "','" + rsetfact.getString(16) + "','" + rsetfact.getString(17) + "',0) ");
                        }

                    } else {
                        con.insertar("update tb_factura set F_CantSur = '" + (Integer.parseInt(CantSur) - cantDevol) + "', F_Iva='" + dev.devuelveIVA(ClaPro, Integer.parseInt(CantSur) - cantDevol) + "', F_Monto = '" + dev.devuelveImporte(ClaPro, Integer.parseInt(CantSur) - cantDevol) + "' where F_IdFact = '" + idFact + "'");
                        con.insertar("insert into tb_factura values(0,'" + ClaDoc + "','" + ClaCli + "','C',CURDATE(),'" + ClaPro + "','" + cantDevol + "','" + cantDevol + "','" + Costo + "','" + dev.devuelveIVA(ClaPro, cantDevol) + "','" + dev.devuelveImporte(ClaPro, cantDevol) + "','" + FolLote + "','" + FecEnt + "','" + F_Hora + "','" + F_User + "','" + Ubicacion + "','" + Observaciones + "',0)");

                        ResultSet rsetfact = con.consulta("select * from tb_factura where F_Obs = '" + Observaciones + "' and F_ClaDoc='" + ClaDoc + "' and F_ClaPro='" + ClaPro + "' and F_Lote='" + FolLote + "' ");
                        while (rsetfact.next()) {
                            con.insertar("insert into tb_factdevol values ('" + rsetfact.getString(1) + "','" + rsetfact.getString(2) + "','" + rsetfact.getString(3) + "','" + rsetfact.getString(4) + "','" + rsetfact.getString(5) + "','" + rsetfact.getString(6) + "','" + rsetfact.getString(7) + "','" + rsetfact.getString(8) + "','" + rsetfact.getString(9) + "','" + rsetfact.getString(10) + "','" + rsetfact.getString(11) + "','" + rsetfact.getString(12) + "','" + rsetfact.getString(13) + "','" + rsetfact.getString(14) + "','" + rsetfact.getString(15) + "','" + rsetfact.getString(16) + "','" + rsetfact.getString(17) + "',0) ");
                        }
                    }

                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                sesion.setAttribute("F_IndGlobal", null);
                response.sendRedirect("reimp_factura.jsp");
            }


////////CANCELAR FOLIO EN ADMIN REMISIONES
            if (request.getParameter("accion").equals("CancelarFolio")) {
                String Folio = request.getParameter("IdFact");
                String IdProyecto = request.getParameter("IdProyecto");
                try {
                    con.conectar();
                    ResultSet FolioModula = null ,GastosC = null,FolioMySQL = null, FolioMySQL2 = null, ExiUbi = null;

                    int F_Lote = 0, F_CantSur = 0, F_TipMed = 0, F_IdFact2 = 0, F_IdFact = 0, F_ExiLot = 0, F_TotalExi = 0, F_Origen = 0, F_Proyecto = 0;
                    String F_Ubicacion = "", F_ClaPro = "", F_ClaPrv = "", F_FecEnt = "", F_Exi = "", F_ClaLot = "", F_FecCad = "", F_FecFab = "", F_ClaCli = "";
                    String F_Cb = "", F_ClaMar = "", F_UniMed = "";
                    double F_Costo = 0.0, F_Iva = 0.0, F_Total = 0.0, F_TotalIva = 0.0, F_Monto = 0.0;
                    Date fechaActual = new Date();
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaSistema = formateador.format(fechaActual);
                    byte[] a = request.getParameter("Obser").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();

                    //FolioModula = Modula.consulta("");
                    FolioMySQL = con.consulta("SELECT F.F_ClaPro, F_Lote, F_Ubicacion, F_CantSur, M.F_Costo, M.F_TipMed, L.F_ClaPrv, F.F_IdFact, DATE_FORMAT( DATE_ADD(F.F_FecEnt, INTERVAL - 1 DAY), '%d/%m/%Y' ) AS F_FecEnt, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_FecFab, L.F_Cb, L.F_ClaMar, L.F_UniMed, L.F_Proyecto, F.F_ClaCli FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro WHERE F.F_ClaDoc='" + Folio + "' AND F.F_StsFact='A' AND F.F_Proyecto = '" + IdProyecto + "' AND F_CantSur>0; ");
                    while (FolioMySQL.next()) {
                        F_ClaPro = FolioMySQL.getString(1);
                        F_Lote = FolioMySQL.getInt(2);
                        F_Ubicacion = FolioMySQL.getString(3);
                        F_CantSur = FolioMySQL.getInt(4);
                        F_Costo = FolioMySQL.getDouble(5);
                        F_TipMed = FolioMySQL.getInt(6);
                        F_ClaPrv = FolioMySQL.getString(7);
                        F_IdFact = FolioMySQL.getInt(8);
                        F_FecEnt = FolioMySQL.getString(9);
                        F_ClaLot = FolioMySQL.getString(10);
                        F_FecCad = FolioMySQL.getString(11);
                        F_Origen = FolioMySQL.getInt(12);
                        F_FecFab = FolioMySQL.getString(13);
                        F_Cb = FolioMySQL.getString(14);
                        F_ClaMar = FolioMySQL.getString(15);
                        F_UniMed = FolioMySQL.getString(16);
                        F_Proyecto = FolioMySQL.getInt(17);
F_ClaCli = FolioMySQL.getString(18);

                        if (F_TipMed == 2504) {
                            F_Iva = 0.0;
                        } else {
                            F_Iva = 0.16;
                        }
                        F_Monto = F_Costo * F_CantSur;
                        F_TotalIva = F_Monto * F_Iva;
                        F_Total = F_Monto + F_TotalIva;

                        Date fechaDate1 = formateador.parse(F_FecEnt);
                        Date fechaDate2 = formateador.parse(fechaSistema);
                        System.out.println("FecSist: " + fechaDate2 + " FecEnt: " + fechaDate1);
                        if (fechaDate1.before(fechaDate2)) {
                            F_Ubicacion = F_Ubicacion;
                            System.out.println("La Fecha 1 es menor " + F_Ubicacion);
                        } else if (fechaDate2.before(fechaDate1)) {
                            System.out.println("La Fecha 1 es Mayor " + F_Ubicacion);
                        } else {
                            System.out.println("Las Fechas Son iguales " + F_Ubicacion);
                        }

                        if (IdProyecto.equals("2") && (F_Ubicacion.equals("AF")  || F_Ubicacion.equals("AF1N") || F_Ubicacion.equals("AFGC"))) {
                            ApartadoDAOImpl dao = new ApartadoDAOImpl(con.getConn());
                            List<Apartado> apartados =dao.getByClaDocAndProyect(Folio, Integer.parseInt(IdProyecto));
                            for(Apartado ap: apartados){
                                dao.updateStatus(ap.getId(), 3);
                            }
                        } else {
                            ExiUbi = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + F_Lote + "' AND F_Ubica='" + F_Ubicacion + "' AND F_Proyecto='" + F_Proyecto + "' AND F_ClaPro='" + F_ClaPro + "';");
                            if (ExiUbi.next()) {
                                F_Exi = ExiUbi.getString(1);
                            }

                            if (F_Exi != "") {
                                F_ExiLot = Integer.parseInt(F_Exi);
                                F_TotalExi = F_ExiLot + F_CantSur;
                                con.actualizar("update tb_lote set F_ExiLot='" + F_TotalExi + "' WHERE F_FolLot='" + F_Lote + "' AND F_Ubica='" + F_Ubicacion + "' AND F_Proyecto='" + F_Proyecto + "' AND F_ClaPro='" + F_ClaPro + "';");
                            } else {
                                F_TotalExi = F_CantSur;
                                con.insertar("INSERT INTO tb_lote VALUES(0,'" + F_ClaPro + "','" + F_ClaLot + "','" + F_FecCad + "','" + F_TotalExi + "','" + F_Lote + "','" + F_ClaPrv + "','" + F_Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_ClaMar + "','" + F_Origen + "','" + F_ClaPrv + "','" + F_UniMed + "','" + F_Proyecto + "');");
                            }
                            con.insertar("INSERT INTO TB_MOVINV VALUES (0,CURDATE(),'" + Folio + "','3','" + F_ClaPro + "','" + F_CantSur + "','" + F_Costo + "','" + F_Total + "','1','" + F_Lote + "','" + F_Ubicacion + "','" + F_ClaPrv + "',CURTIME(),'" + (String) sesion.getAttribute("nombre") + "','');");
                            con.actualizar("update tb_factura set F_StsFact='C' where F_ClaDoc='" + Folio + "' AND F_IdFact='" + F_IdFact + "';");
                            con.actualizar("DELETE FROM tb_abastoweb WHERE  F_Proyecto = '" + F_Proyecto + "' AND F_ClaDoc ='" + Folio + "';");
                            
                        }
///GASTOS CATASTROFICOS
                        GastosC = con.consulta("SELECT Id_Gastos, Cant_Total, Cant_Remi FROM tb_gasto_catastrofico WHERE Id_Lote = " + F_Lote + " and No_clacli=" + F_ClaCli + ";");
                        Integer idGastos = 0, totalGastos = 0, remiGastos = 0;
                        while (GastosC.next()) {
                            idGastos = GastosC.getInt(1);
                            totalGastos = GastosC.getInt(2);
                            remiGastos = GastosC.getInt(3);

                            con.actualizar("UPDATE tb_gasto_catastrofico SET Cant_Total = " + totalGastos + "  Cant_Remi = " + remiGastos + " WHERE  Id_Gastos = " + idGastos + " ;");
                        }


                    }

                    FolioMySQL2 = con.consulta("SELECT F.F_ClaPro,F_Lote,F_Ubicacion,F_CantSur,M.F_Costo,M.F_TipMed,L.F_ClaPrv,F.F_IdFact,DATE_FORMAT(DATE_ADD(F.F_FecEnt,INTERVAL -1 DAY),'%d/%m/%Y') AS F_FecEnt,L.F_ClaLot,L.F_FecCad,L.F_Origen,L.F_FecFab,L.F_Cb,L.F_ClaMar,L.F_UniMed FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica WHERE F.F_ClaDoc='" + Folio + "' AND F.F_StsFact='A' AND F.F_Proyecto = '" + IdProyecto + "';");
                    while (FolioMySQL2.next()) {
                        F_IdFact2 = FolioMySQL2.getInt(8);
                        con.actualizar("update tb_factura set F_StsFact='C' where F_ClaDoc='" + Folio + "' AND F_IdFact='" + F_IdFact2 + "';");
                    }

                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                sesion.setAttribute("F_IndGlobal", null);
                response.sendRedirect("AdministraRemisiones?Accion=ListaRemision");
            }



            if (request.getParameter("accion").equals("CancelarFactura")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Factura Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacionManual.jsp'</script>");
                } catch (Exception e) {
                }
            }
            //Extra
            if (request.getParameter("accion").equals("CancelarFacturaextra")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Factura Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacionManualExtra.jsp'</script>");
                } catch (Exception e) {
                }
            }

            if (request.getParameter("accion").equals("CancelarFacturaSemiCause")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Factura Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacionSemiManual.jsp'</script>");
                } catch (Exception e) {
                }
            }
            if (request.getParameter("accion").equals("CancelarFacturaCause")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Factura Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacionManualCause.jsp'</script>");
                } catch (Exception e) {
                }
            }
            if (request.getParameter("accion").equals("CancelarFacturaFOLIO")) {
                String F_ClaCli = "";
                String Folio = (String) sesion.getAttribute("F_IndGlobal");
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
//                    while (rset.next()) {
//                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Factura Eliminada Correctamente')</script>");
                    out.println("<script>window.location='ModificarFolio?Accion=btnMostrar&Folio=" + Folio + "'</script>");
                } catch (Exception e) {
                }
            }
            if (request.getParameter("accion").equals("CancelarTranferProductoProyecto")) {
                String F_ClaCli = "";
                String Folio = (String) sesion.getAttribute("F_IndGlobal");
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
//                    while (rset.next()) {
//                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
//                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Transferencia Eliminada Correctamente')</script>");
                    out.println("<script>window.location='productotransferencia.jsp'</script>");
                } catch (Exception e) {
                }
            }
            if (request.getParameter("accion").equals("CancelarTransferencia")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_facttemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Transferencia Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacion/generaTransferencias.jsp'</script>");
                } catch (Exception e) {
                }
            }
            if (request.getParameter("accion").equals("remisionCamion")) {

                try {
                    String IdProyecto = request.getParameter("IdProyecto");
                    String[] claveschk = request.getParameterValues("chkSeleccciona");
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
                    con.conectar();
                    //consql.conectar();
                    int FolioFactura = 0;
                    String FechaE = request.getParameter("Fecha");
                    ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice");
                    while (FolioFact.next()) {
                        FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFact"));
                    }
                    int FolFact = FolioFactura + 1;
                    con.actualizar("update tb_indice set F_IndFact='" + FolFact + "'");
                    byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                    String req = request.getParameter("F_Req").toUpperCase();
                    if (req.equals("")) {
                        req = "00000";
                    }
                    String idFact = "";
                    String qryFact = "select f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol  from tb_facttemp f, tb_lote l, tb_medica m, tb_proveedor p where f.F_IdLot = l.F_IdLote AND l.F_ClaPro = m.F_ClaPro AND l.F_ClaOrg = p.F_ClaProve and f.F_IdFact = '" + request.getParameter("Nombre") + "' and f.F_StsFact=4 AND (f.F_Id IN (" + claves + ")) AND l.F_Proyecto = '" + IdProyecto + "'";
                    ResultSet rset = con.consulta(qryFact);
                    while (rset.next()) {
                        idFact = rset.getString("F_IdFact");
                        String ClaUni = rset.getString("F_ClaCli");
                        String Clave = rset.getString("F_ClaPro");
                        String Caducidad = rset.getString("F_FecCad");
                        String FolioLote = rset.getString("F_FolLot");
                        String IdLote = rset.getString("F_IdLote");
                        String ClaLot = rset.getString("F_ClaLot");
                        String Ubicacion = rset.getString("F_Ubica");
                        String ClaProve = rset.getString("F_ClaProve");
                        String F_Id = rset.getString("F_Id");
                        String F_CantSol = rset.getString("F_CantSol");
                        FechaE = rset.getString("F_FecEnt");
                        int existencia = rset.getInt("F_ExiLot");
                        int cantidad = rset.getInt("F_Cant");
                        int Tipo = rset.getInt("F_TipMed");
                        int FolioMovi = 0, FolMov = 0;
                        double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                        if (Tipo == 2504) {
                            IVA = 0.0;
                        } else {
                            IVA = 0.16;
                        }

                        Costo = rset.getDouble("F_Costo");

                        int Diferencia = existencia - cantidad;

                        //Actualizacion de TB Lote
                        /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + fact.dame5car("1") + "' ");
                         String loteSQL = "";
                         while (rsetLoteSQL.next()) {
                         loteSQL = rsetLoteSQL.getString("lote");
                         }*/
                        if (Diferencia >= 0) {
                            if (Diferencia == 0) {
                                con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");
                                //consql.actualizar("UPDATE TB_lote SET F_ExiLot='0' WHERE F_FolLot='" + loteSQL + "'");
                            } else {
                                con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                //consql.actualizar("UPDATE TB_lote SET F_ExiLot='" + Diferencia + "' WHERE F_FolLot='" + loteSQL + "'");
                            }
                            IVAPro = (cantidad * Costo) * IVA;
                            Monto = cantidad * Costo;
                            MontoIva = Monto + IVAPro;
                            //Obtencion de indice de movimiento

                            ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                            while (FolioMov.next()) {
                                FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                            }
                            FolMov = FolioMovi + 1;
                            con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                            //Inserciones
                            Logger.getLogger(FacturacionManual.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + cantidad + " ");
                            con.insertar("insert into tb_movinv values(0,curdate(),'" + idFact + "','51','" + Clave + "','" + cantidad + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                            con.insertar("insert into tb_factura values(0,'" + idFact + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_CantSol + "','" + cantidad + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','',0)");
                            //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + cantidad + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + loteSQL + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                            //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + fact.dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + cantidad + "','" + cantidad + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + loteSQL + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + cantidad + "','" + Ubicacion + "') ");

                            /*ResultSet existSql = consql.consulta("select F_Existen from TB_Medica where F_ClaPro = '" + Clave + "' ");
                             while (existSql.next()) {
                             int difTotal = existSql.getInt("F_Existen") - cantidad;
                             if (difTotal < 0) {
                             difTotal = 0;
                             }
                             consql.actualizar("update TB_Medica set F_Existen = '" + difTotal + "' where F_ClaPro = '" + Clave + "' ");
                             }*/
                            con.actualizar("update tb_facttemp set F_StsFact='5' where F_Id='" + F_Id + "'");
                        } else {
                            out.println("<script>alert('Error al Generar la remisión, Contacte con el área de Sistemas')</script>");
                            out.println("<script>window.location='remisionarCamion.jsp'</script>");
                        }

                    }

                    con.insertar("insert into tb_obserfact values ('" + idFact + "','" + Observaciones + "',0,'" + request.getParameter("F_Req").toUpperCase() + "', '" + request.getParameter("F_Tipo") + "')");
                    //Finaliza
                    //consql.cierraConexion();
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", "");
                    sesion.setAttribute("FechaEntFM", "");
                    sesion.setAttribute("ClaProFM", "");
                    sesion.setAttribute("DesProFM", "");
                    sesion.setAttribute("F_IndGlobal", null);
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    out.println("<script>window.open('reimpFactura.jsp?fol_gnkl=" + idFact + "','_blank')</script>");
                    out.println("<script>window.location='remisionarCamion.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (request.getParameter("accion").equals("ConfirmarTransferencia")) {

                /* try {
                 con.conectar();
                 RequerimientoModula reqMod = new RequerimientoModula();
                 reqMod.enviaRequerimiento((String) sesion.getAttribute("F_IndGlobal"));
                 //con.insertar("update tb_facttemp set F_StsFact = '0' where F_IdFact = '" + (String) sesion.getAttribute("F_IndGlobal") + "' ");
                 con.cierraConexion();
                 /*
                 sesion.setAttribute("F_IndGlobal", null);
                 sesion.setAttribute("ClaCliFM", "");
                 sesion.setAttribute("FechaEntFM", "");
                 sesion.setAttribute("ClaProFM", "");
                 sesion.setAttribute("DesProFM", "");
                 response.sendRedirect("facturacionManual.jsp");
                 */
 /*} catch (Exception e) {
                 System.out.println(e.getMessage());
                 }*/
                ////----------------------------------------------------------------------------------------------------
                ////----------------------------------------------------------------------------------------------------
                int FolioFactura = 0, FolFact = 0;
                int Signo = 0, ConInv = 0, Diferencia = 0;
                String FechaE = "", Obs = "";

                try {

                    con.conectar();
                    Obs = request.getParameter("obs");
                    ResultSet rsetFactTemp = con.consulta("select F_Id, F_IdFact, F_ClaCli from tb_facttemp where F_StsFact=3 AND F_User='" + sesion.getAttribute("nombre") + "' group by F_IdFact");
//consql.conectar();

                    while (rsetFactTemp.next()) {
                        String IdProyecto = request.getParameter("IdProyecto");
                        FolioFactura = 0;
                        String idFact = rsetFactTemp.getString("F_IdFact");
                        String F_ClaCli = rsetFactTemp.getString("F_ClaCli");
                        FechaE = request.getParameter("Fecha");
                        ResultSet FolioFact = con.consulta("SELECT F_IndTransfer FROM tb_indice");
                        while (FolioFact.next()) {
                            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndTransfer"));
                        }
                        FolFact = FolioFactura + 1;
                        con.actualizar("update tb_indice set F_IndTransfer='" + FolFact + "'");
                        //byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                        String Observaciones = "";//(new String(a, "UTF-8")).toUpperCase();
                        String req = "";//request.getParameter("F_Req").toUpperCase();
                        if (req.equals("")) {
                            req = "00000";
                        }
                        String qryFact = "select f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol  from tb_facttemp f, tb_lote l, tb_medica m, tb_proveedor p where f.F_IdLot = l.F_IdLote AND l.F_ClaPro = m.F_ClaPro AND l.F_ClaOrg = p.F_ClaProve and f.F_IdFact = '" + idFact + "' and f.F_StsFact<5 AND f.F_ClaCli = '" + F_ClaCli + "' AND f.F_User='" + sesion.getAttribute("nombre") + "' AND l.F_Proyecto = '" + IdProyecto + "'";
                        ResultSet rset = con.consulta(qryFact);
                        while (rset.next()) {
                            idFact = rset.getString("F_IdFact");
                            String ClaUni = rset.getString("F_ClaCli");
                            String Clave = rset.getString("F_ClaPro");
                            String Caducidad = rset.getString("F_FecCad");
                            String FolioLote = rset.getString("F_FolLot");
                            String IdLote = rset.getString("F_IdLote");
                            String ClaLot = rset.getString("F_ClaLot");
                            String Ubicacion = rset.getString("F_Ubica");
                            String ClaProve = rset.getString("F_ClaProve");
                            String F_Id = rset.getString("F_Id");
                            String F_CantSol = rset.getString("F_CantSol");
                            FechaE = rset.getString("F_FecEnt");
                            int existencia = rset.getInt("F_ExiLot");
                            int cantidad = rset.getInt("F_Cant");
                            int Tipo = rset.getInt("F_TipMed");
                            int FolioMovi = 0, FolMov = 0;
                            double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                            if (Tipo == 2504) {
                                IVA = 0.0;
                            } else {
                                IVA = 0.16;
                            }

                            Costo = rset.getDouble("F_Costo");
                            ConInv = Integer.parseInt(ClaUni);

                            if (ConInv <= 50) {
                                Diferencia = existencia + cantidad;
                                Signo = 1;
                            } else {
                                Diferencia = existencia - cantidad;
                                Signo = -1;
                            }

                            //Actualizacion de TB Lote
                            /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + fact.dame5car("1") + "' ");
                             String loteSQL = "";
                             while (rsetLoteSQL.next()) {
                             loteSQL = rsetLoteSQL.getString("lote");
                             }*/
                            if (Diferencia >= 0) {
                                if (Diferencia == 0) {
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");
                                    //consql.actualizar("UPDATE TB_lote SET F_ExiLot='0' WHERE F_FolLot='" + loteSQL + "'");
                                } else {
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                    //consql.actualizar("UPDATE TB_lote SET F_ExiLot='" + Diferencia + "' WHERE F_FolLot='" + loteSQL + "'");
                                }
                                IVAPro = (cantidad * Costo) * IVA;
                                Monto = cantidad * Costo;
                                MontoIva = Monto + IVAPro;
                                //Obtencion de indice de movimiento

                                ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                while (FolioMov.next()) {
                                    FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                }
                                FolMov = FolioMovi + 1;
                                con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                //Inserciones
                                Logger.getLogger(FacturacionManual.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + cantidad + " ");
                                con.insertar("insert into tb_movinv values(0,curdate(),'" + idFact + "','" + ClaUni + "','" + Clave + "','" + cantidad + "','" + Costo + "','" + MontoIva + "','" + Signo + "','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                con.insertar("insert into tb_transferencias values(0,'" + idFact + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_CantSol + "','" + cantidad + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','',0)");
                                //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + cantidad + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + loteSQL + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + fact.dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + cantidad + "','" + cantidad + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + loteSQL + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + cantidad + "','" + Ubicacion + "') ");

                                /*ResultSet existSql = consql.consulta("select F_Existen from TB_Medica where F_ClaPro = '" + Clave + "' ");
                                 while (existSql.next()) {
                                 int difTotal = existSql.getInt("F_Existen") - cantidad;
                                 if (difTotal < 0) {
                                 difTotal = 0;
                                 }
                                 consql.actualizar("update TB_Medica set F_Existen = '" + difTotal + "' where F_ClaPro = '" + Clave + "' ");
                                 }*/
                                con.actualizar("update tb_facttemp set F_StsFact='5' where F_Id='" + F_Id + "'");
                            } else {
                                out.println("<script>alert('Error al Generar la remisión, Contacte con el área de Sistemas')</script>");
                                out.println("<script>window.location='remisionarCamion.jsp'</script>");
                            }
                        }

                        con.insertar("insert into tb_obsertrans values ('" + idFact + "','" + Obs + "',0,'', 'ORDINARIO')");
                        out.println("<script>window.open('reportes/reimpTransferencia.jsp?fol_gnkl=" + idFact + "','_blank')</script>");
                        //Finaliza
                        //consql.cierraConexion();
                    }
                    // con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", "");
                    sesion.setAttribute("FechaEntFM", "");
                    sesion.setAttribute("ClaProFM", "");
                    sesion.setAttribute("DesProFM", "");
                    sesion.setAttribute("F_IndTransfer", null);
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    out.println("<script>window.location='facturacion/generaTransferencias.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (request.getParameter("accion").equals("ConfirmarFactura")) {
               

                ////----------------------------------------------------------------------------------------------------
                ////----------------------------------------------------------------------------------------------------
                int FolioFactura = 0, FolFact = 0;
                String FechaE = "";
                try {
                    String IdProyecto = request.getParameter("IdProyecto");
                    con.conectar();
                    System.out.println("entre en factura manual");
                    ResultSet rsetFactTemp = con.consulta("select F_Id, F_IdFact, F_ClaCli from tb_facttemp where F_StsFact=3 and F_User = '" + (String) sesion.getAttribute("nombre") + "' group by F_IdFact  ");
                    //consql.conectar();

                    while (rsetFactTemp.next()) {
                        boolean hasAF = false;
                        FolioFactura = 0;
                        String idFact = rsetFactTemp.getString("F_IdFact");
                        String F_ClaCli = rsetFactTemp.getString("F_ClaCli");
                        FechaE = request.getParameter("Fecha");
                        ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice");
                        while (FolioFact.next()) {
                            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFact"));
                        }
                        FolFact = FolioFactura + 1;
                        con.actualizar("update tb_indice set F_IndFact='" + FolFact + "'");
                        byte[] a = request.getParameter("obs").getBytes("ISO-8859-1");
                        String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                        String tipo = request.getParameter("F_Tipo");
                        String req = "";//request.getParameter("F_Req").toUpperCase();
                        if (req.equals("")) {
                            req = "00000";
                        }
                        String qryFact = "select f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol  from tb_facttemp f, tb_lote l, tb_medica m, tb_proveedor p where f.F_IdLot = l.F_IdLote AND l.F_ClaPro = m.F_ClaPro AND l.F_ClaOrg = p.F_ClaProve and f.F_IdFact = '" + idFact + "' and f.F_StsFact<5 AND f.F_ClaCli = '" + F_ClaCli + "' ";
                        ResultSet rset = con.consulta(qryFact);
                        ApartadoDAOImpl apartadoDAO = new ApartadoDAOImpl(con.getConn());
                        while (rset.next()) {
                            idFact = rset.getString("F_IdFact");
                            String ClaUni = rset.getString("F_ClaCli");
                            String Clave = rset.getString("F_ClaPro");
                            String Caducidad = rset.getString("F_FecCad");
                            String FolioLote = rset.getString("F_FolLot");
                            String IdLote = rset.getString("F_IdLote");
                            String ClaLot = rset.getString("F_ClaLot");
                            String Ubicacion = rset.getString("F_Ubica");
                            String ClaProve = rset.getString("F_ClaProve");
                            String F_Id = rset.getString("F_Id");
                            String F_CantSol = rset.getString("F_CantSol");
                            FechaE = rset.getString("F_FecEnt");
                            int existencia = rset.getInt("F_ExiLot");
                            int cantidad = rset.getInt("F_Cant");
                            int Tipo = rset.getInt("F_TipMed");
                            int FolioMovi = 0, FolMov = 0;
                            double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                            if (Tipo == 2504) {
                                IVA = 0.0;
                            } else {
                                IVA = 0.16;
                            }

                            Costo = rset.getDouble("F_Costo");

                            int Diferencia = existencia - cantidad;

                            //Actualizacion de TB Lote
                            /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + fact.dame5car("1") + "' ");
                             String loteSQL = "";
                             while (rsetLoteSQL.next()) {
                             loteSQL = rsetLoteSQL.getString("lote");
                             }*/
                            if (Diferencia >= 0) {
                                int status = 1;
                                if (Ubicacion.compareTo("AF") != 0 || Ubicacion.compareTo("AF1N") != 0) {
                                    status = 2;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                } else {
                                    hasAF = true;
                                }
                                Apartado apartado = new Apartado();
                                apartado.setId(0);
                                apartado.setIdLote(Integer.parseInt(IdLote));
                                apartado.setCant(cantidad);
                                apartado.setStatus(status);
                                apartado.setClaDoc(FolioFactura + "");
                                apartadoDAO.guardar(apartado);
                                IVAPro = (cantidad * Costo) * IVA;
                                Monto = cantidad * Costo;
                                MontoIva = Monto + IVAPro;
                                //Obtencion de indice de movimiento

                                ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                while (FolioMov.next()) {
                                    FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                }
                                FolMov = FolioMovi + 1;
                                con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                //Inserciones
                                Logger.getLogger(FacturacionManual.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + cantidad + " ");
                                con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + cantidad + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_CantSol + "','" + cantidad + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','',0)");
                                //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + cantidad + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + loteSQL + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + fact.dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + cantidad + "','" + cantidad + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + loteSQL + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + cantidad + "','" + Ubicacion + "') ");

                                /*ResultSet existSql = consql.consulta("select F_Existen from TB_Medica where F_ClaPro = '" + Clave + "' ");
                                 while (existSql.next()) {
                                 int difTotal = existSql.getInt("F_Existen") - cantidad;
                                 if (difTotal < 0) {
                                 difTotal = 0;
                                 }
                                 consql.actualizar("update TB_Medica set F_Existen = '" + difTotal + "' where F_ClaPro = '" + Clave + "' ");
                                 }*/
                                con.actualizar("update tb_facttemp set F_StsFact='5' where F_Id='" + F_Id + "'");
                            } else {
                                out.println("<script>alert('Error al Generar la remisión, Contacte con el área de Sistemas')</script>");
                                out.println("<script>window.location='facturacionManual.jsp'</script>");
                            }
                        }
                        FolioStatusDAOImpl fsdao = new FolioStatusDAOImpl(con.getConn());
                        FolioStatus fs = new FolioStatus();
                        fs.setClaDoc(FolFact);
                        fs.setProyecto(Integer.parseInt(IdProyecto));
                        fs.setStatus(hasAF ? 1 : 2);
//                        fsdao.guardar(fs);

                        con.insertar("insert into tb_obserfact values ('" + idFact + "','" + Observaciones + "',0,'M', '" + tipo + "')");
                        //out.println("<script>window.open('reimpFactura.jsp?fol_gnkl=" + idFact + "','_blank')</script>");
                        //Finaliza
                        //consql.cierraConexion();
                    }
                    // con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", "");
                    sesion.setAttribute("FechaEntFM", "");
                    sesion.setAttribute("ClaProFM", "");
                    sesion.setAttribute("DesProFM", "");
                    sesion.setAttribute("F_IndGlobal", null);
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    out.println("<script>window.location='facturacionManual.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (request.getParameter("accion").equals("ReenviarFactura")) {
                RequerimientoModula reqMod = new RequerimientoModula();
                reqMod.enviaRequerimiento(request.getParameter("fol_gnkl"), request.getParameter("idproyecto"));
                request.getRequestDispatcher("/AdministraRemisiones?Accion=ListaRemision").forward(request, response);
                //response.sendRedirect("reimp_factura.jsp");
            }

            if (request.getParameter("accion").equals("ReenviarConcentradoRuta")) {
                try {
                    AbastoModula reqMod = new AbastoModula();
                    reqMod.enviaRuta(request.getParameter("F_FecEnt"));
                    response.sendRedirect("facturacion/concentradoxRuta.jsp");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            if (request.getParameter("accion").equals("ReenviarConcentradoRequerimientos")) {
                try {
                    AbastoModula reqMod = new AbastoModula();
                    reqMod.enviaMultRemis(request.getParameter("F_FecEnt"));
                    response.sendRedirect("facturacion/concentradoxRuta.jsp");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            if (request.getParameter("accion").equals("ReenviarConcentradoRequerimientosCSU")) {
                try {
                    AbastoModula reqMod = new AbastoModula();
                    reqMod.enviaMultRemisCSU(request.getParameter("F_FecEnt"));
                    response.sendRedirect("facturacion/concentradoxRuta.jsp");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

//            if (request.getParameter("accion").equals("AgregarClave")) {
//                String F_CLaCli = (String) sesion.getAttribute("ClaCliFM");
//                String[] F_CLaCliArray = F_CLaCli.split(" - ");
//                F_CLaCli = F_CLaCliArray[0];
//                int Contar = 0, Contar2 = 0, cant = 0;
//                cant = Integer.parseInt(request.getParameter("Cant"));
//                try {
//                    con.conectar();
//                    System.out.println("ando en factura manual");
//                    ResultSet rset = con.consulta("select count(*) as NoLote  from tb_facttemp where F_IdLot =  '" + request.getParameter("IdLot") + "' and (F_StsFact = '0' or F_StsFact = '3');");
//                    out.println("query de folio"+rset);
//                    while (rset.next()){
//                    Contar  = rset.getInt(1);
//                        out.println("contar = "+Contar);
//                    }
//                    ResultSet rsetapaex = con.consulta("SELECT disponible FROM v_existencias  where  F_IdLote  =  '" + request.getParameter("IdLot") + "' LIMIT 0, 1000 ;");
//                    out.println("query de folio"+rsetapaex);
//                    while (rsetapaex.next()){
//                    Contar2  = rset.getInt(1);
//                      out.println("contar = "+Contar2);
//                    }
//                    if (Contar == 0) {
//                        if (cant <= Contar2) {
//                            cant = Contar2;
//                        }
//
//                      con.insertar("insert into tb_facttemp values('" + (String) sesion.getAttribute("F_IndGlobal") + "','" + F_CLaCli + "','" + request.getParameter("IdLot") + "','" + cant + "','" + (String) sesion.getAttribute("FechaEntFM") + "','3','0','" + (String) sesion.getAttribute("nombre") + "','" + request.getParameter("Cant") + "','0')");
//                       con.cierraConexion();
//                    
//                    response.sendRedirect("facturacionManual.jsp");
//                    }else{
//                     
//                    response.sendRedirect("facturacionManualSelectLote.jsp");
//                        con.cierraConexion();
//                    out.println("<script>alert('Lote ya Existe')</script>");
//                    }
//                    //con.insertar("insert into tb_facttemp values('" + (String) sesion.getAttribute("F_IndGlobal") + "','" + F_CLaCli + "','" + request.getParameter("IdLot") + "','" + request.getParameter("Cant") + "','" + (String) sesion.getAttribute("FechaEntFM") + "','3','0','" + (String) sesion.getAttribute("nombre") + "','" + request.getParameter("Cant") + "','0')");
//                    
//                    sesion.setAttribute("ClaProFM", "");
//                    sesion.setAttribute("DesProFM", "");
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }

            if (request.getParameter("accion").equals("AgregarClaveFOLIO")) {
                String F_CLaCli = (String) sesion.getAttribute("ClaCliFM");
                String[] F_CLaCliArray = F_CLaCli.split(" - ");
                F_CLaCli = F_CLaCliArray[0];
                try {
                    con.conectar();
                    con.insertar("insert into tb_facttemp values('" + (String) sesion.getAttribute("F_IndGlobal") + "','" + F_CLaCli + "','" + request.getParameter("IdLot") + "','" + request.getParameter("Cant") + "','" + (String) sesion.getAttribute("FechaEntFM") + "','3','0','" + (String) sesion.getAttribute("nombre") + "','" + request.getParameter("Cant") + "','0')");
                    con.cierraConexion();
                    sesion.setAttribute("ClaProFM", "");
                    sesion.setAttribute("DesProFM", "");
                    response.sendRedirect("facturacionManualFolio.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

//            if (request.getParameter("accion").equals("AgregarClaveT")) {
//                try {
//                    con.conectar();
//                    con.insertar("insert into tb_facttemp values('" + (String) sesion.getAttribute("F_IndTransfer") + "','" + (String) sesion.getAttribute("ClaCliFM") + "','" + request.getParameter("IdLot") + "','" + request.getParameter("Cant") + "','" + (String) sesion.getAttribute("FechaEntFM") + "','3','0','" + (String) sesion.getAttribute("nombre") + "','" + request.getParameter("Cant") + "','0')");
//                    con.cierraConexion();
//                    sesion.setAttribute("ClaProFM", "");
//                    response.sendRedirect("facturacion/generaTransferencias.jsp");
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }

//            if (request.getParameter("accion").equals("SeleccionaLoteT")) {
//                System.out.println("Cant: " + request.getParameter("Cantidad"));
//                System.out.println("Mov: " + request.getParameter("ClaCli"));
//                response.setContentType("text/html");
//                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
//                request.setAttribute("Mov", request.getParameter("ClaCli"));
//                request.getRequestDispatcher("facturacion/generaTransManualSelecLote.jsp").forward(request, response);
//            }

            if (request.getParameter("accion").equals("SeleccionaLote")) {
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacionManualSelecLote.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("SeleccionaLotextra")) {
                System.out.println(request.getParameter("Cantidad"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacionManualSelecLotextra.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("SeleccionaLoteSemiCause")) {
                System.out.println(request.getParameter("Cantidad"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacionSemiManualSelecLoteCause.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("SeleccionaLoteCause")) {
                System.out.println(request.getParameter("Cantidad"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacionManualSelecLoteCause.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("SeleccionaLoteFOLIO")) {
                System.out.println(request.getParameter("Cantidad"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacionManualSelecLoteFOLIO.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("SeleccionaLoteTProyecto")) {
                System.out.println(request.getParameter("Cantidad"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("productotransferenciaSelecLote.jsp").forward(request, response);
            }

/*BUSCAR CLAVE EN FACTURA MANUAL*/
            if (request.getParameter("accion").equals("btnClaveT")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();
                    ResultSet rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Ubica NOT IN ('MODULA','A0S') group by m.F_ClaPro;");
                    while (rset.next()) {
                        banInsumo = 1;
                        sesion.setAttribute("DesProFM", rset.getString(2));
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                    if (banInsumo == 0) {
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    }
                    out.println("<script>window.location='facturacion/generaTransferencias.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("btnClave")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    String Tipo = (String) sesion.getAttribute("Tipo");
                    String ssmcli = request.getParameter("ClaCli");
                    int banInsumo = 0;
                    String DesProFM = "", Ubicaciones = "";
                    ResultSet rset = null;
                    ResultSet rsetUbica = null;
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();

                    rsetUbica = con.consulta("SELECT F_Ubi FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        Ubicaciones = rsetUbica.getString(1);
                    }
                    if (Tipo.equals("8")) {

                        rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='2' AND l.F_Ubica IN (" + Ubicaciones + ",'CROSSDOCKMORELIA') group by m.F_ClaPro;");
                    } else {
                        rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND l.F_Ubica NOT IN (" + Ubicaciones + ") group by m.F_ClaPro;");
                    }
                    while (rset.next()) {
                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    System.out.println("clave unidad: "+request.getParameter("ClaCli") + " mas ssm:"+ ssmcli);
           
                 
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));

                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                        sesion.setAttribute("DesProFM", DesProFM);
                    }
                    out.println("<script>window.location='facturacionManual.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            //buscar clave extra
            if (request.getParameter("accion").equals("btnClavextra")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    String Tipo = (String) sesion.getAttribute("Tipo");
                    int banInsumo = 0;
                    String DesProFM = "", Ubicaciones = "";
                    ResultSet rset = null;
                    ResultSet rsetUbica = null;
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();

                    rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND l.F_Ubica IN ('EXTRA_ORDINARIA', 'VIRTUAL-MCH-EXTRAORD')group by m.F_ClaPro;");

                    while (rset.next()) {
                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                        sesion.setAttribute("DesProFM", DesProFM);
                    }
                    out.println("<script>window.location='facturacionManualExtra.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("btnClaveSemiCause")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    String DesProFM = "";
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();

                    String Ubicaciones = "";
                    ResultSet rsetUbica = rsetUbica = con.consulta("SELECT F_Ubi FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        Ubicaciones = rsetUbica.getString(1);
                    }

                    ResultSet rset = con.consulta("SELECT m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), SUM(F_ExiLot) FROM tb_medica m INNER JOIN tb_lote l ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_catalogoprecios c ON l.F_ClaPro = c.F_ClaPro AND l.F_Proyecto = c.F_Proyecto WHERE m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND l.F_Ubica NOT IN (" + Ubicaciones + ") group by m.F_ClaPro;");
                    while (rset.next()) {
                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                        sesion.setAttribute("DesProFM", DesProFM);
                    }
                    out.println("<script>window.location='facturacionSemiManual.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("btnClaveCause")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    String DesProFM = "";
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();
                    String Ubicaciones = "";
                    ResultSet rsetUbica = con.consulta("SELECT F_Ubi FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        Ubicaciones = rsetUbica.getString(1);
                    }

                    ResultSet rset = con.consulta("SELECT m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), SUM(F_ExiLot) FROM tb_medica m INNER JOIN tb_lote l ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_catalogoprecios c ON l.F_Proyecto = c.F_Proyecto AND l.F_ClaPro = c.F_ClaPro WHERE m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND c.F_Cause = '" + request.getParameter("Cause") + "' AND l.F_Ubica NOT IN (" + Ubicaciones + ")  group by m.F_ClaPro;");
                    while (rset.next()) {

                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("CauseFM", request.getParameter("Cause"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                        sesion.setAttribute("DesProFM", DesProFM);
                    }
                    out.println("<script>window.location='facturacionManualCause.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("btnClaveFOLIO")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    String DesProFM = "",clavedit = "";
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();
                    String Ubicaciones = "";
                    ResultSet rsetUbica = con.consulta("SELECT F_Ubi FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        Ubicaciones = rsetUbica.getString(1);
                    }

                    int Proyectos = 0;
                    ResultSet RsEdit = con.consulta("SELECT f.F_Proyecto from tb_factura f WHERE  f.F_ClaDoc = '" + F_IndGlobal + "' GROUP BY f.F_ClaDoc;");
                    if (RsEdit.next()) {
                        Proyectos = RsEdit.getInt(1);
                    }

                    ResultSet rset = null;
                    boolean editable = false;
                    PickingService serv = new PickingService();
                    int pickingStatus = serv.isFolioEditable(Integer.parseInt(F_IndGlobal), Proyectos);
                    editable = (pickingStatus == -1) || (pickingStatus >= 5);
                    if (editable) {
                        System.out.println("todos las claves");
                        rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND l.F_Ubica NOT IN (" + Ubicaciones + ") group by m.F_ClaPro;");
                    } else {
                        System.out.println("solo claves especiales");
                        out.println("<script>alert('Seleccionar Insumo !NO SECO!)</script>");
                        rset = con.consulta("SELECT  CASE WHEN a.F_ClaPro = l.F_ClaPro THEN a.F_ClaPro WHEN c.F_ClaPro = l.F_ClaPro THEN c.F_ClaPro ELSE r.F_ClaPro END F_ClaPro,CASE WHEN a.F_ClaPro then a.F_DesPro WHEN c.F_ClaPro then c.F_DesPro WHEN r.F_ClaPro then r.F_DesPro END DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_lote l LEFT JOIN tb_ape a ON a.F_ClaPro = l.F_ClaPro LEFT JOIN tb_controlados c ON c.F_ClaPro = l.F_ClaPro LEFT JOIN tb_redfria r ON r.F_ClaPro = l.F_ClaPro where l.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' AND l.F_Ubica NOT IN (" + Ubicaciones + ") group by l.F_ClaPro;");

                    }
                    while (rset.next()) {
                        clavedit = rset.getString(1);
                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        System.out.println("clave: "+ clavedit);
                        if (clavedit == null) {
                             out.println("<script>alert('Este insumo es seco no se puede agregar')</script>");
                            sesion.setAttribute("ClaProFM", "");
                            sesion.setAttribute("DesProFM", "");
                        } else {
                           
sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                            sesion.setAttribute("DesProFM", DesProFM);
                        }
                    }
                    out.println("<script>window.location='facturacionManualFolio.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("btnClaveTProyecto")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    String DesProFM = "";
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();
                    ResultSet rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'),SUM(F_ExiLot) from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Proyecto='" + request.getParameter("Proyecto") + "' group by m.F_ClaPro;");
                    while (rset.next()) {
                        banInsumo = rset.getInt(6);
                        DesProFM = rset.getString(2);
                    }
                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    if (banInsumo == 0) {
                        sesion.setAttribute("ClaProFM", "");
                        sesion.setAttribute("DesProFM", "");
                        out.println("<script>alert('Insumo sin Existencias')</script>");
                    } else {
                        sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));
                        sesion.setAttribute("DesProFM", DesProFM);
                    }
                    out.println("<script>window.location='productotransferencia.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String[] quitar = request.getParameter("accion").split(",");
            if (quitar[0].equals("quitarInsumo")) {
                System.out.println(request.getParameter("Nombre") + "*****");
                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                con.conectar();
                con.insertar("update tb_facttemp set F_StsFact = '2' where F_Id = '" + quitar[1] + "' ");
                con.cierraConexion();
                out.println("<script>window.location='remisionarCamion.jsp'</script>");
            }

            // Botones captura movimientos
            if (request.getParameter("accion").equals("btnClaveTMov")) {
                try {
                    String F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    int banInsumo = 0;
                    if (F_IndGlobal == null) {
                        sesion.setAttribute("F_IndGlobal", dameIndGlobal() + "");
                        F_IndGlobal = (String) sesion.getAttribute("F_IndGlobal");
                    }
                    con.conectar();
                    System.out.println("Aqui esta el query de busqueda");
                    ResultSet rset1 = con.consulta("select m.F_ClaPro, m.F_DesPro from tb_medica m where  m.F_ClaPro ='" + request.getParameter("ClaPro") + "' group by m.F_ClaPro;");

                    while (rset1.next()) {
                        banInsumo = 2;
                        sesion.setAttribute("DesProFM", rset1.getString(2));

                        if (banInsumo == 2) {
                            ResultSet rset = con.consulta("select m.F_ClaPro, m.F_DesPro, l.F_ClaLot, l.F_FolLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') from tb_medica m, tb_lote l where m.F_ClaPro = l.F_ClaPro and m.F_ClaPro = '" + request.getParameter("ClaPro") + "' AND l.F_Ubica NOT IN ('MODULA','A0S') group by m.F_ClaPro;");
                            while (rset.next()) {
                                banInsumo = 1;
                            }
                        }
                    }

                    con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", request.getParameter("ClaCli"));
                    sesion.setAttribute("FechaEntFM", request.getParameter("FechaEnt"));
                    sesion.setAttribute("ClaProFM", request.getParameter("ClaPro"));

                    if (banInsumo == 0) {

                        out.println("<script>alert('Insumo sin existecia')</script>");

                        sesion.setAttribute("DesProFM", request.getParameter(""));
                        sesion.setAttribute("ClaProFM", request.getParameter(""));
                    }

                    out.println("<script>window.location='facturacion/CapturaMovi.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            ////SELECCIONA LOTES PARA MOVIMIENTO//
            if (request.getParameter("accion").equals("SeleccionaLoteTMovi")) {
                System.out.println("Cant: " + request.getParameter("Cantidad"));
                System.out.println("Mov: " + request.getParameter("ClaCli"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("Mov", request.getParameter("ClaCli"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacion/generaTransManualSelecLoteMovi.jsp").forward(request, response);
            }

            if (request.getParameter("accion").equals("AgregarClaveTMov")) {
                try {
                    con.conectar();
                    con.insertar("insert into tb_capmovtemp values('" + (String) sesion.getAttribute("F_IndFolMov") + "','" + (String) sesion.getAttribute("ClaCliFM") + "','" + request.getParameter("IdLot") + "','" + request.getParameter("Cant") + "','" + (String) sesion.getAttribute("FechaEntFM") + "','3','0','" + (String) sesion.getAttribute("nombre") + "','" + request.getParameter("Cant") + "','0','')");
                    con.cierraConexion();
                    sesion.setAttribute("ClaProFM", "");
                    response.sendRedirect("facturacion/CapturaMovi.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("ConfirmarMovimiento")) {

                ////----------------------------------------------------------------------------------------------------
                ////----------------------------------------------------------------------------------------------------
                int FolioFactura = 0, FolFact = 0;
                int Signo = 0, ConInv = 0, Diferencia = 0, existencia = 0;
                String FechaE = "", Obs = "";
                try {

                    con.conectar();
                    Obs = request.getParameter("obs");
                    String proyecto = request.getParameter("Proyecto");
                    ResultSet rsetFactTemp = con.consulta("select F_Id, F_IdFact, F_ClaCli from tb_capmovtemp where F_StsFact=3 AND F_User='" + sesion.getAttribute("nombre") + "' group by F_IdFact");
//consql.conectar();

                    while (rsetFactTemp.next()) {
                        FolioFactura = 0;
                        String idFact = rsetFactTemp.getString("F_IdFact");
                        String F_ClaCli = rsetFactTemp.getString("F_ClaCli");
                        FechaE = request.getParameter("Fecha");
                        ResultSet FolioFact = con.consulta("SELECT F_IndFolMov FROM tb_indice");
                        while (FolioFact.next()) {
                            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFolMov"));
                        }
                        FolFact = FolioFactura + 1;
                        con.actualizar("update tb_indice set F_IndFolMov='" + FolFact + "'");
                        //byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                        String Observaciones = "";//(new String(a, "UTF-8")).toUpperCase();
                        String req = "";//request.getParameter("F_Req").toUpperCase();
                        if (req.equals("")) {
                            req = "00000";
                        }
                        String qryFact = "select f.F_ClaCli, l.F_FolLot, l.F_IdLote, l.F_ClaPro, l.F_ClaLot, l.F_FecCad, m.F_TipMed, m.F_Costo, p.F_ClaProve, f.F_Cant, l.F_ExiLot, l.F_Ubica, f.F_IdFact, f.F_Id, f.F_FecEnt, f.F_CantSol  from tb_capmovtemp f, tb_lote l, tb_medica m, tb_proveedor p where f.F_IdLot = l.F_IdLote AND l.F_ClaPro = m.F_ClaPro AND l.F_ClaOrg = p.F_ClaProve and f.F_IdFact = '" + idFact + "' and f.F_StsFact<5 AND f.F_ClaCli = '" + F_ClaCli + "' AND f.F_User='" + sesion.getAttribute("nombre") + "' ";
                        ResultSet rset = con.consulta(qryFact);
                        while (rset.next()) {
                            idFact = rset.getString("F_IdFact");
                            String ClaUni = rset.getString("F_ClaCli");
                            String Clave = rset.getString("F_ClaPro");
                            String Caducidad = rset.getString("F_FecCad");
                            String FolioLote = rset.getString("F_FolLot");
                            String IdLote = rset.getString("F_IdLote");
                            String ClaLot = rset.getString("F_ClaLot");
                            String Ubicacion = rset.getString("F_Ubica");
                            String ClaProve = rset.getString("F_ClaProve");
                            String F_Id = rset.getString("F_Id");
                            String F_CantSol = rset.getString("F_CantSol");
                            FechaE = rset.getString("F_FecEnt");
                            // int existencia = rset.getInt("F_ExiLot");
                            int cantidad = rset.getInt("F_Cant");
                            int Tipo = rset.getInt("F_TipMed");
                            int FolioMovi = 0, FolMov = 0;
                            double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                            if (Tipo == 2504) {
                                IVA = 0.0;
                            } else {
                                IVA = 0.16;
                            }

                            String qryLote = "SELECT F_ExiLot FROM tb_lote WHERE F_ClaPro='" + Clave + "' AND F_IdLote='" + IdLote + "' AND F_FolLot='" + FolioLote + "' AND F_Ubica='" + Ubicacion + "'; ";
                            ResultSet rsetL = con.consulta(qryLote);
                            if (rsetL.next()) {
                                existencia = rsetL.getInt("F_ExiLot");
                            }

                            Costo = rset.getDouble("F_Costo");
                            ConInv = Integer.parseInt(ClaUni);

                            if (ConInv <= 50) {
                                Diferencia = existencia + cantidad;
                                Signo = 1;
                            } else {
                                Diferencia = existencia - cantidad;
                                Signo = -1;
                            }

                            //Actualizacion de TB Lote
                            /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + fact.dame5car("1") + "' ");
                             String loteSQL = "";
                             while (rsetLoteSQL.next()) {
                             loteSQL = rsetLoteSQL.getString("lote");
                             }*/
                            if (Diferencia >= 0) {
                                if (Diferencia == 0) {
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");
                                    //consql.actualizar("UPDATE TB_lote SET F_ExiLot='0' WHERE F_FolLot='" + loteSQL + "'");
                                } else {
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + Diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                    //consql.actualizar("UPDATE TB_lote SET F_ExiLot='" + Diferencia + "' WHERE F_FolLot='" + loteSQL + "'");
                                }
                                IVAPro = (cantidad * Costo) * IVA;
                                Monto = cantidad * Costo;
                                MontoIva = Monto + IVAPro;
                                //Obtencion de indice de movimiento

                                ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                while (FolioMov.next()) {
                                    FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                }
                                FolMov = FolioMovi + 1;
                                con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                //Inserciones
                                Logger.getLogger(FacturacionManual.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + cantidad + " ");
                                con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','" + ClaUni + "','" + Clave + "','" + cantidad + "','" + Costo + "','" + MontoIva + "','" + Signo + "','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                //con.insertar("insert into tb_transferencias values(0,'" + idFact + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_CantSol + "','" + cantidad + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','',0)");
                                //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + cantidad + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + loteSQL + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + fact.dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + cantidad + "','" + cantidad + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + loteSQL + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + cantidad + "','" + Ubicacion + "') ");

                                /*ResultSet existSql = consql.consulta("select F_Existen from TB_Medica where F_ClaPro = '" + Clave + "' ");
                                 while (existSql.next()) {
                                 int difTotal = existSql.getInt("F_Existen") - cantidad;
                                 if (difTotal < 0) {
                                 difTotal = 0;
                                 }
                                 consql.actualizar("update TB_Medica set F_Existen = '" + difTotal + "' where F_ClaPro = '" + Clave + "' ");
                                 }*/
                                existencia = 0;
                                con.actualizar("update tb_capmovtemp set F_Obs='" + Obs + "' where F_Id='" + F_Id + "'");
                                con.actualizar("update tb_capmovtemp set F_StsFact='5' where F_Id='" + F_Id + "'");
                            } else {
                                existencia = 0;
                                out.println("<script>alert('Error al Generar la remisión, Contacte con el área de Sistemas')</script>");
                                out.println("<script>window.location='remisionarCamion.jsp'</script>");
                            }
                        }

                        con.insertar("insert into tb_obsmov values ('" + idFact + "','" + Obs + "',0,'', 'ORDINARIO')");
                        out.println("<script>window.open('reportes/reimpMovimiento.jsp?fol_gnkl=" + idFact + "&Mov=" + F_ClaCli + "&Proyecto=" + proyecto + "','_blank')</script>");
                        //Finaliza
                        //consql.cierraConexion();
                    }
                    // con.cierraConexion();
                    sesion.setAttribute("ClaCliFM", "");
                    sesion.setAttribute("FechaEntFM", "");
                    sesion.setAttribute("ClaProFM", "");
                    sesion.setAttribute("DesProFM", "");
                    sesion.setAttribute("F_IndFolMov", null);
                    //Aqui tenemos que poner en nulo la variable de folio de dactura
                    out.println("<script>window.location='facturacion/CapturaMovi.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (request.getParameter("accion").equals("CancelarMovimiento")) {
                String F_ClaCli = "";
                try {

                    String F_ClaCliFM = (String) sesion.getAttribute("ClaCliFM");
                    F_ClaCli = F_ClaCliFM;
                    String[] F_ClaCliFMArray = F_ClaCliFM.split(" - ");
                    if (F_ClaCliFMArray.length > 0) {
                        F_ClaCli = F_ClaCliFMArray[0];
                    }
                } catch (Exception e) {
                }
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_capmovtemp where F_StsFact='3' and F_ClaCLi = '" + F_ClaCli + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_capmovtemp where F_ClaCLi = '" + F_ClaCli + "' ");
                    con.cierraConexion();
                    sesion.setAttribute("F_IndFolMov", null);
                    out.println("<script>alert('Movimiento Eliminada Correctamente')</script>");
                    out.println("<script>window.location='facturacion/CapturaMovi.jsp'</script>");
                } catch (Exception e) {
                }
            }

            if (request.getParameter("accion").equals("NuevoLote")) {
                try {
                    con.conectar();
                    String Clave = "", Lote = "", Caducidad = "", Origen = "", Proveedor = "", Cb = "", Proyecto = "", DesProyecto = "";
                    int F_FolLot = 0, F_FolLotU = 0, FolioLot = 0, FolioLote = 0;
                    Clave = request.getParameter("clave");
                    Lote = request.getParameter("lote");
                    Caducidad = request.getParameter("caducidad");
                    Origen = request.getParameter("origen");
                    Proveedor = request.getParameter("proveedor");
                    Proyecto = request.getParameter("Proyecto");
                    DesProyecto = request.getParameter("DesProyecto");
                    Cb = request.getParameter("cb");

                    ResultSet rset = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro = '" + Clave + "' AND F_ClaLot = '" + Lote + "' AND F_FecCad = '" + Caducidad + "' AND F_Origen = '" + Origen + "' AND F_ClaOrg = '" + Proveedor + "' AND F_Cb = '" + Cb + "' AND F_Proyecto = '" + Proyecto + "';");
                    if (rset.next()) {
                        F_FolLot = rset.getInt(1);
                    }

                    if (F_FolLot > 0) {
                        ResultSet rsetUbica = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_Ubica='NUEVA' AND F_FolLot='" + F_FolLot + "'");
                        if (rsetUbica.next()) {
                            F_FolLotU = rsetUbica.getInt(1);
                            System.out.println(F_FolLotU + "foloubi");
                        }
                        if (F_FolLotU > 0) {
                            out.println("<script>alert ('La clave:  " +  Clave + "  \\nlote:  " + Lote + " \\n\\n\\tDato existente')</script>");
                            out.println("<script>window.history.back()</script>");
                        }
//                      else {
//                            ResultSet rsetDatos = con.consulta("SELECT F_ClaOrg,F_FecFab,F_Cb,F_ClaMar,F_ClaPrv FROM tb_lote WHERE F_FolLot='" + F_FolLot + "'");
//                            if (rsetDatos.next()) {
//                                con.actualizar("INSERT INTO TB_LOTE VALUES (0,'" + Clave + "','" + Lote + "','" + Caducidad + "','0','" + F_FolLot + "','" + rsetDatos.getString(1) + "','NUEVA','" + rsetDatos.getString(2) + "','" + rsetDatos.getString(3) + "','" + rsetDatos.getString(4) + "','" + Origen + "','" + rsetDatos.getString(5) + "','131','" + Proyecto + "')");
//                            }
//                            out.println("<script>window.location='facturacion/AltaLote.jsp?DesProyecto=" + DesProyecto + "&Proyecto=" + Proyecto + "'</script>");
//                       }
                    } else {

                        ResultSet Folio = con.consulta("SELECT F_IndLote FROM tb_indice");
                        if (Folio.next()) {
                            FolioLot = Folio.getInt(1);
                        }
                        FolioLote = FolioLot;
                        FolioLot = FolioLot + 1;
                        con.actualizar("UPDATE tb_indice SET F_IndLote='" + FolioLot + "'");

                        con.actualizar("INSERT INTO TB_LOTE VALUES (0,'" + Clave + "','" + Lote + "','" + Caducidad + "','0','" + FolioLote + "','" + Proveedor + "','NUEVA','" + Caducidad + "','" + Cb + "','10372','" + Origen + "','" + Proveedor + "','131','" + Proyecto + "')");
                        out.println("<script>alert ('La clave:  " +  Clave + "  \\nlote:  " + Lote + " \\n\\n\\tFue registrada con existo')</script>");                           
                        out.println("<script>window.location='facturacion/AltaLote.jsp?DesProyecto=" + DesProyecto + "&Proyecto=" + Proyecto + "'</script>");
                    }

                    con.cierraConexion();

                } catch (Exception e) {
                }
            }

            if (request.getParameter("accion").equals("Refresh")) {
                System.out.println("Cant: " + request.getParameter("Cantidad"));
                System.out.println("Mov: " + request.getParameter("Mov"));
                response.setContentType("text/html");
                request.setAttribute("Cantidad", request.getParameter("Cantidad"));
                request.setAttribute("Mov", request.getParameter("Mov"));
                request.setAttribute("DesProyecto", request.getParameter("DesProyecto"));
                request.setAttribute("Proyecto", request.getParameter("Proyecto"));
                request.getRequestDispatcher("facturacion/generaTransManualSelecLoteMovi.jsp").forward(request, response);
            }

            // botones captura movimientos
        } catch (Exception e) {
        }
    }

    public int dameIndGlobal() throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioFactura = 0, FolFact = 0;
        ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice;");
        while (FolioFact.next()) {
            FolioFactura = Integer.parseInt(FolioFact.getString(1));
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioFactura;
    }

    public int dameIndGlobalProyectos(int proyectos) throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioFactura = 0, FolFact = 0;
        ResultSet FolioFact = con.consulta("SELECT F_IndFactP" + proyectos + " FROM tb_indice;");
        while (FolioFact.next()) {
            FolioFactura = Integer.parseInt(FolioFact.getString(1));
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioFactura;
    }

    public int dameIndGlobalEnseres(String proyecto) throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioFactura = 0;
        ResultSet FolioFact = null;
        int Proyecto = Integer.parseInt(proyecto);
        if (Proyecto == 2) {
            FolioFact = con.consulta("SELECT F_IndFactPEMICH FROM tb_indice;");
        } else if ((Proyecto == 1) || (Proyecto == 4) || (Proyecto == 5) || (Proyecto == 6) || (Proyecto == 7)) {
            FolioFact = con.consulta("SELECT F_IndFactPEISEM FROM tb_indice;");
        } else if (Proyecto == 17) {
            FolioFact = con.consulta("SELECT F_IndFactPEHEMO FROM tb_indice;");
        }
        while (FolioFact.next()) {
            FolioFactura = Integer.parseInt(FolioFact.getString(1));
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioFactura;
    }

    public int dameIndGlobalTranferencia() throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioFactura = 0, FolFact = 0;
        ResultSet FolioFact = con.consulta("SELECT F_IndTProducto FROM tb_indice;");
        while (FolioFact.next()) {
            FolioFactura = Integer.parseInt(FolioFact.getString(1));
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioFactura;
    }

    //Número de folio transferencias
    public int dameIndTranfer() throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioFactura = 0, FolFact = 0;
        ResultSet FolioFact = con.consulta("SELECT F_IndTransfer FROM tb_indice");
        while (FolioFact.next()) {

            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndTransfer"));
            //FolioFactura = FolioFactura + 1;
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioFactura;
    }

    //Número de folio movimiento
    public int dameIndMov() throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        int FolioMov = 0, FolFact = 0;
        ResultSet FolioFact = con.consulta("SELECT F_IndFolMov FROM tb_indice");
        while (FolioFact.next()) {
            FolioMov = Integer.parseInt(FolioFact.getString("F_IndFolMov"));
            //FolioFactura = FolioFactura + 1;
        }
        //FolFact = FolioFactura + 1;
        //con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
        con.cierraConexion();
        return FolioMov;
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
