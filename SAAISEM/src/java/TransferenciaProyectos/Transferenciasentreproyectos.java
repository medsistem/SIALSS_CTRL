/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TransferenciaProyectos;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.DevolucionesFact;
import modelos.TranferenciaProyecto;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "Transferenciasentreproyectos", urlPatterns = {"/Transferenciasentreproyectos"})
public class Transferenciasentreproyectos extends HttpServlet {

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
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateador2 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSistema = formateador.format(fechaActual);
        String fechaSistema2 = formateador2.format(fechaActual);
        ConectionDB con = new ConectionDB();
        HttpSession sesion = request.getSession(true);
        List<TranferenciaProyecto> Listaremisiones = new ArrayList<TranferenciaProyecto>();
        TranferenciaProyecto remisiones;
        List<TranferenciaProyecto> DatosUnidad = new ArrayList<TranferenciaProyecto>();
        TranferenciaProyecto Unidad;
        List<TranferenciaProyecto> ListaTranferencia = new ArrayList<TranferenciaProyecto>();
        TranferenciaProyecto Transferencia;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        String Consulta = "", Consulta2 = "";
        String TipoFact = "", usua = "", Concepto = "";

        try {
            usua = (String) sesion.getAttribute("nombre");
            String Accion = request.getParameter("Accion");
            con.conectar();

            if (Accion.equals("ListaTranferencia")) {

                sesion.setAttribute("fecha_ini", fechaSistema2);
                sesion.setAttribute("fecha_fin", fechaSistema2);
                Consulta = "SELECT DATE_FORMAT(T.F_Fecha, '%d/%m/%Y') AS F_Fecha, F_Documento, P1.F_DesProy, P2.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN ( SELECT P.F_Id, P.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN tb_proyectos P ON T.F_ProyectoIni = P.F_Id GROUP BY T.F_ProyectoIni ) AS P1 ON T.F_ProyectoIni = P1.F_Id INNER JOIN ( SELECT P.F_Id, P.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN tb_proyectos P ON T.F_ProyectoFin = P.F_Id GROUP BY T.F_ProyectoFin ) AS P2 ON T.F_ProyectoFin = P2.F_Id WHERE T.F_Fecha BETWEEN ? AND ? ORDER BY F_Documento+0;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, fechaSistema2 + " 00:00:00");
                ps.setString(2, fechaSistema2 + " 23:59:59");
                rs = ps.executeQuery();
                while (rs.next()) {
                    Transferencia = new TranferenciaProyecto();
                    Transferencia.setFecha(rs.getString(1));
                    Transferencia.setDocumento(rs.getString(2));
                    Transferencia.setProyectoini(rs.getString(3));
                    Transferencia.setProyectofin(rs.getString(4));
                    ListaTranferencia.add(Transferencia);
                }
                request.setAttribute("ListaTranferencia", ListaTranferencia);
                request.getRequestDispatcher("/TransferenciasAdmin.jsp").forward(request, response);
            }

            if (Accion.equals("btnMostrar")) {
                String fecha_ini = request.getParameter("fecha_ini");
                String fecha_fin = request.getParameter("fecha_fin");
                sesion.setAttribute("fecha_ini", fecha_ini);
                sesion.setAttribute("fecha_fin", fecha_fin);
                Consulta = "SELECT DATE_FORMAT(T.F_Fecha, '%d/%m/%Y') AS F_Fecha, F_Documento, P1.F_DesProy, P2.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN ( SELECT P.F_Id, P.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN tb_proyectos P ON T.F_ProyectoIni = P.F_Id GROUP BY T.F_ProyectoIni ) AS P1 ON T.F_ProyectoIni = P1.F_Id INNER JOIN ( SELECT P.F_Id, P.F_DesProy FROM tb_tranferenciaproyecto T INNER JOIN tb_proyectos P ON T.F_ProyectoFin = P.F_Id GROUP BY T.F_ProyectoFin ) AS P2 ON T.F_ProyectoFin = P2.F_Id WHERE T.F_Fecha BETWEEN ? AND ? ORDER BY F_Documento+0;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, fecha_ini + " 00:00:00");
                ps.setString(2, fecha_fin + " 23:59:59");
                rs = ps.executeQuery();
                while (rs.next()) {
                    Transferencia = new TranferenciaProyecto();
                    Transferencia.setFecha(rs.getString(1));
                    Transferencia.setDocumento(rs.getString(2));
                    Transferencia.setProyectoini(rs.getString(3));
                    Transferencia.setProyectofin(rs.getString(4));
                    ListaTranferencia.add(Transferencia);
                }
                request.setAttribute("ListaTranferencia", ListaTranferencia);
                request.getRequestDispatcher("/TransferenciasAdmin.jsp").forward(request, response);
            }

            /*if (Accion.equals("btnMostrar")) {
                String Folio = request.getParameter("Folio");

                Consulta2 = "DELETE FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                ps2.execute();

                Consulta = "SELECT F.F_ClaPro,SUBSTR(M.F_DesPro,1,60) AS F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc=? AND F_CantSur>0 AND F.F_StsFact='A' GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Consulta2 = "INSERT INTO tb_devoglobalfact VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setString(1, rs.getString(1));
                    ps2.setString(2, rs.getString(2));
                    ps2.setString(3, rs.getString(3));
                    ps2.setString(4, rs.getString(4));
                    ps2.setString(5, rs.getString(5));
                    ps2.setString(6, rs.getString(6));
                    ps2.setString(7, rs.getString(7));
                    ps2.setString(8, rs.getString(7));
                    ps2.setString(9, rs.getString(8));
                    ps2.setString(10, rs.getString(9));
                    ps2.setString(11, rs.getString(10));
                    ps2.setString(12, rs.getString(11));
                    ps2.execute();
                }

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    DatosUnidad.add(Unidad);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
            }
            if (Accion.equals("btnModificar")) {
                int Cantidad = 0, Diferencia = 0;
                String Folio = request.getParameter("Folio");
                String IdFol = request.getParameter("Identi");
                String Devolver = request.getParameter("Devolver");
                int Surtida = Integer.parseInt(request.getParameter("Surtida"));
                System.out.println("Folio: " + Folio + " Id: " + IdFol);
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
                            Consulta = "UPDATE tb_devoglobalfact SET F_CantDevo =? WHERE F_IdFact=? AND F_ClaDoc=?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Devolver);
                            ps.setString(2, IdFol);
                            ps.setString(3, Folio);
                            ps.execute();

                            Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Folio);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                Unidad = new DevolucionesFact();
                                Unidad.setFolio(rs.getInt(1));
                                Unidad.setClaveuni(rs.getString(2));
                                Unidad.setUnidad(rs.getString(3));
                                Unidad.setFechaentrega(rs.getString(4));
                                Unidad.setUsuario(usua);
                                DatosUnidad.add(Unidad);
                            }

                            Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setString(1, Folio);
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                remisiones = new DevolucionesFact();
                                remisiones.setClavepro(rs2.getString(1));
                                remisiones.setDescripcion(rs2.getString(2));
                                remisiones.setLote(rs2.getString(3));
                                remisiones.setCaducidad(rs2.getString(4));
                                remisiones.setRequerido(rs2.getString(5));
                                remisiones.setUbicacion(rs2.getString(6));
                                remisiones.setSurtido(rs2.getString(7));
                                remisiones.setDevolucion(rs2.getString(8));
                                remisiones.setCosto(rs2.getString(9));
                                remisiones.setMonto(rs2.getString(10));
                                remisiones.setDocumento(rs2.getString(11));
                                remisiones.setIddocumento(rs2.getString(12));
                                Listaremisiones.add(remisiones);
                            }
                            request.setAttribute("DatosUnidad", DatosUnidad);
                            request.setAttribute("listaRemision", Listaremisiones);
                            request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
                        }
                    }
                } else {
                    out.println("<script>alert('Ingrese Datos')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

            if (Accion.equals("btnEliminar")) {
                String Folio = request.getParameter("folio");

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    DatosUnidad.add(Unidad);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);

            }*/

            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(Transferenciasentreproyectos.class.getName()).log(Level.SEVERE, null, e);
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
