/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventario;

import conn.ConectionDB;
import conn.ConectionDB_SQLServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Redistribución de insumos
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Ubicaciones extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
//        Devoluciones objDev = new Devoluciones();
        HttpSession sesion = request.getSession(true);
        try {
            try {

                if (request.getParameter("accion").equals("Redistribucion")) {

                    if (validaUbicacion(request.getParameter("F_ClaUbi"))) {
                        String mensaje = Reubica(request.getParameter("F_IdLote"), request.getParameter("F_ClaUbi"), request.getParameter("CantMov"), (String) sesion.getAttribute("nombre"));

                        //ReubicaApartado(request.getParameter("F_IdLote"), nIdLote);
                        //response.sendRedirect("hh/insumoNuevoRedist.jsp");
                        out.println("<script>alert('" + mensaje + "')</script>");
                        out.println("<script>window.location='hh/insumoNuevoRedist.jsp'</script>");
                    } else {
                        out.println("<script>alert('Ubicación no válida')</script>");
                        out.println("<script>window.location='hh/insumoNuevoRedist.jsp'</script>");
                    }
                }
            } catch (Exception e) {
                out.println(e.getMessage());
                System.out.println(e.getMessage());
            }
        } finally {
            out.close();
        }
    }

    public boolean validaUbicacion(String F_ClaUbi) throws SQLException {
        ConectionDB con = new ConectionDB();
        int banUbica = 0;
        con.conectar();
        ResultSet rset = con.consulta("select F_ClaUbi from tb_ubica where F_ClaUbi = '" + F_ClaUbi + "';");
        while (rset.next()) {
            banUbica = 1;
        }
        con.cierraConexion();
        if (banUbica == 1) {
            return true;
        } else {
            return false;
        }
    }

    public String Reubica(String idLote, String CBUbica, String cantMov, String Nombre) throws SQLException, ParseException {
        int idLoteNuevo = 0;
        String Fecha = "", Time = "";
        String mensaje = "Reubicación correcta";
        Devoluciones objDev = new Devoluciones();
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
        //conModula.conectar();
        con.conectar();
        String UbicaMov = CBUbica;
        int CantMov = Integer.parseInt(cantMov);
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        String F_ClaPro = "", F_ClaLot = "", F_FecCad = "", F_FolLot = "", F_ClaOrg = "", F_Ubica = "", F_FecFab = "", F_Cb = "", F_ClaMar = "", F_Origen = "", F_UniMed = "";
        int F_ExiLot = 0, F_IdLote = 0, F_ExiLotDestino = 0;
        ResultSet rset = con.consulta("select * from tb_lote where F_IdLote = '" + idLote + "';");
        while (rset.next()) {
            F_ClaPro = rset.getString("F_ClaPro");
            F_ClaLot = rset.getString("F_ClaLot");
            F_FecCad = rset.getString("F_FecCad");
            F_FolLot = rset.getString("F_FolLot");
            F_ClaOrg = rset.getString("F_ClaOrg");
            F_Ubica = rset.getString("F_Ubica");
            F_FecFab = rset.getString("F_FecFab");
            F_Cb = rset.getString("F_Cb");
            F_ClaMar = rset.getString("F_ClaMar");
            F_ExiLot = rset.getInt("F_ExiLot");
            F_Origen = rset.getString("F_Origen");
            F_UniMed = rset.getString("F_UniMed");
        }

        if (F_ExiLot - CantMov >= 0) {
            if ((F_ExiLot - CantMov) >= 0) {
                rset = con.consulta("select F_ClaUbi from tb_ubica where F_Cb= '" + CBUbica + "';");
                while (rset.next()) {
                    UbicaMov = rset.getString("F_ClaUbi");
                }

                rset = con.consulta("select F_IdLote, F_ExiLot from tb_lote where F_ClaPro= '" + F_ClaPro + "' and F_ClaLot = '" + F_ClaLot + "' and F_FecCad = '" + F_FecCad + "' and F_Ubica = '" + UbicaMov + "' and F_Origen = '" + F_Origen + "' AND F_Cb='" + F_Cb + "' AND F_FolLot='" + F_FolLot + "';");
                while (rset.next()) {
                    F_ExiLotDestino = rset.getInt("F_ExiLot");
                    F_IdLote = rset.getInt("F_IdLote");
                }

                if (F_IdLote != 0) {//Ya existe insumo en el desitno
                    con.insertar("update tb_lote set F_ExiLot = '" + (F_ExiLotDestino + CantMov) + "' where F_IdLote='" + F_IdLote + "';");
                    if ((CBUbica.equals("MODULA")) || (CBUbica.equals("MODULA2"))) {
                        try {
                            System.out.println("Entra a Módula");
                            conModula.conectar();
                            //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_ClaLot + "','1','" + (CantMov) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df.format(new Date()) + "')");
                            ResultSet FechaM = con.consulta("SELECT CURDATE(),CURTIME();");
                            if (FechaM.next()) {
                                Fecha = FechaM.getString(1);
                                Time = FechaM.getString(2);
                            }
                            ////***** Reubica  a Módula *****/////                            
                            conModula.ejecutar("insert into IMP_ORDINI values ('R" + F_ClaPro + Time + "','A','','" + df.format(df3.parse(Fecha)) + "','V','','1','P');");
                            conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('R" + F_ClaPro + Time + "','','" + F_ClaPro + "','" + F_ClaLot + "','1','" + CantMov + "','" + F_Cb + "','" + df2.format(df3.parse(F_FecCad)) + "','');");
                            conModula.ejecutar("update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= 'R" + F_ClaPro + Time + "';");
                            ////***** Fin Reubica Módula *****/////

                            conModula.cierraConexion();
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println(e.getMessage());
                            mensaje = e.toString();
                        }
                    }
                } else {//No existe insumo en el destino
                    con.insertar("insert into tb_lote values(0,'" + F_ClaPro + "','" + F_ClaLot + "','" + F_FecCad + "','" + CantMov + "','" + F_FolLot + "','" + F_ClaOrg + "','" + UbicaMov + "','" + F_FecFab + "','" + F_Cb + "','" + F_ClaMar + "', '" + F_Origen + "','" + F_ClaOrg + "','" + F_UniMed + "');");
                    if ((CBUbica.equals("MODULA")) || (CBUbica.equals("MODULA2"))) {
                        try {
                            System.out.println("Entra a Módula");
                            conModula.conectar();
                            // conModula.ejecutar("insert into IMP_AVVISIINGRESSO  (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_ClaLot + "','1','" + (CantMov) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df.format(new Date()) + "')");
                            ResultSet FechaM = con.consulta("SELECT CURDATE(),CURTIME();");
                            if (FechaM.next()) {
                                Fecha = FechaM.getString(1);
                                Time = FechaM.getString(2);
                            }
                            ////***** Reubica  a Módula *****/////                            
                            conModula.ejecutar("insert into IMP_ORDINI values ('R" + F_ClaPro + Time + "','A','','" + df.format(df3.parse(Fecha)) + "','V','','1','P');");
                            conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('R" + F_ClaPro + Time + "','','" + F_ClaPro + "','" + F_ClaLot + "','1','" + CantMov + "','" + F_Cb + "','" + df2.format(df3.parse(F_FecCad)) + "','');");
                            conModula.ejecutar("update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= 'R" + F_ClaPro + Time + "';");
                            ////***** Fin Reubica Módula *****/////
                            conModula.cierraConexion();
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println(e.getMessage());
                            mensaje = e.toString();
                        }
                    }
                }
                con.insertar("update tb_lote set F_ExiLot = '" + (F_ExiLot - CantMov) + "' where F_IdLote = '" + idLote + "' ");

                con.insertar("insert into tb_movinv values (0,CURDATE(),'0','1000','" + F_ClaPro + "','" + CantMov + "','" + objDev.devuelveCosto(F_ClaPro) + "','" + objDev.devuelveImporte(F_ClaPro, CantMov) + "', '-1','" + F_FolLot + "','" + F_Ubica + "','" + F_ClaOrg + "',CURTIME(),'" + Nombre + "','');");
                con.insertar("insert into tb_movinv values (0,CURDATE(),'0','1000','" + F_ClaPro + "','" + CantMov + "','" + objDev.devuelveCosto(F_ClaPro) + "','" + objDev.devuelveImporte(F_ClaPro, CantMov) + "', '1','" + F_FolLot + "','" + UbicaMov + "','" + F_ClaOrg + "',CURTIME(),'" + Nombre + "','');");

            }
            rset = con.consulta("select F_IdLote from tb_lote where F_FolLot = '" + F_FolLot + "' and F_Ubica = '" + UbicaMov + "';");
            while (rset.next()) {
                idLoteNuevo = rset.getInt("F_IdLote");
            }
        }
        //conModula.cierraConexion();
        con.cierraConexion();
        return mensaje;
    }

    public void ReubicaApartado(String idLote, int nidLote) throws SQLException {
        ConectionDB con = new ConectionDB();
        con.conectar();
        ResultSet rset = con.consulta("select * from tb_facttemp where F_IdLot='" + idLote + "' and F_StsFact <5");
        while (rset.next()) {
            int cantFact = rset.getInt("F_Cant");
            int cantLote = 0;
            int cantApartada = 0;
            ResultSet rset2 = con.consulta("select F_ExiLot from tb_lote where F_IdLote = '" + idLote + "' ");
            while (rset2.next()) {
                cantLote = rset2.getInt(1);
            }
            rset2 = con.consulta("select sum(F_Cant) from tb_facttemp where F_IdLot = '" + idLote + "' and F_Id<'" + rset.getString("F_Id") + "' and F_StsFact<5 group by F_IdLot");
            while (rset2.next()) {
                cantApartada = rset2.getInt(1);
            }
            System.out.println(cantLote + "-" + cantApartada);
            cantLote = cantLote - cantApartada;
            System.out.println(cantFact + "-" + cantLote);
            int diferencia = cantFact - cantLote;
            System.out.println(diferencia + "/////Diferencia");

            if (diferencia > 0) {
                int F_Id = 0, CantAnt = 0;
                con.insertar("update tb_facttemp set F_Cant = '" + cantLote + "' where F_Id= '" + rset.getString("F_Id") + "' ");
                rset2 = con.consulta("select F_Id, F_Cant from tb_facttemp where F_IdLot = '" + nidLote + "' and F_IdFact = '" + rset.getString("F_IdFact") + "' ");
                while (rset2.next()) {
                    F_Id = rset2.getInt("F_Id");
                    CantAnt = rset2.getInt("F_Cant");
                }
                if (F_Id != 0) {
                    con.insertar("update tb_facttemp set F_Cant='" + (diferencia + CantAnt) + "' where F_Id='" + F_Id + "'");
                } else {
                    con.insertar("insert into tb_facttemp values ('" + rset.getString("F_IdFact") + "','" + rset.getString("F_ClaCli") + "','" + nidLote + "','" + diferencia + "','" + rset.getString("F_FecEnt") + "','" + rset.getString("F_StsFact") + "',0,'" + rset.getString("F_User") + "')");
                }
            }

        }
        con.cierraConexion();
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
