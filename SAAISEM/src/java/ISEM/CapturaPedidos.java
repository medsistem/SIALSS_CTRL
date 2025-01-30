/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISEM;

import Correo.*;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.Proyectos;

/**
 * Captura ORI por el cliente
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CapturaPedidos extends HttpServlet {
    
    ConectionDB con = new ConectionDB();
    DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    DateFormat df21 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat df31 = new SimpleDateFormat("dd/MM/yyyy");
    CancelaCompra correoCancela = new CancelaCompra();
    Correo correo = new Correo();

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
        HttpSession sesion = request.getSession(true);
        try {
            if (request.getParameter("accion").equals("recalendarizar")) {
                con.conectar();
                
                try {
                    
                    con.insertar("update tb_pedido_sialss set F_FecSur = '" + request.getParameter("F_FecEnt") + "', F_HorSur='" + request.getParameter("HoraN") + "' where F_NoCompra ='" + request.getParameter("NoCompra") + "';");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='verFoliosM.jsp'</script>");
                con.cierraConexion();
            }
            
            if (request.getParameter("accion").equals("vencimiento")) {
                con.conectar();
                
                try {
                    
                    con.insertar("update tb_pedido_sialss set Fecha_Vencimiento = '" + request.getParameter("F_FecVen") + "' where F_NoCompra ='" + request.getParameter("NoCompra") + "';");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='verFoliosM.jsp'</script>");
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("plazo")) {
                con.conectar();
                
                try {
                    
                    con.insertar("update tb_pedido_sialss set Plazo_Pago = '" + request.getParameter("plazo") + "' where F_NoCompra ='" + request.getParameter("NoCompra") + "';");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='verFoliosM.jsp'</script>");
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("verFolio")) {
                con.conectar();
                
                PreparedStatement ps = con.getConn().prepareStatement("SELECT F_ClaPro, F_DesPro FROM tb_medica where F_ClaPro = ?");
                ps.setString(1, request.getParameter("NoCompra"));
                ResultSet rset = ps.executeQuery();
                //ResultSet rset = con.consulta("SELECT o.F_NoCompra, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y'), F_HorSur, p.F_ClaProve FROM tb_pedido_sialss o, tb_proveedor p WHERE o.F_Provee = p.F_ClaProve AND F_NoCompra = '" + request.getParameter("NoCompra") + "'  GROUP BY o.F_NoCompra");
                while (rset.next()) {
                    sesion.setAttribute("NoOrdCompra", rset.getString(1));
                    sesion.setAttribute("proveedor", rset.getString(2));
                    sesion.setAttribute("fec_entrega", rset.getString(3));
                    sesion.setAttribute("hor_entrega", rset.getString(4));
                }
                response.sendRedirect("verFoliosIsem.jsp");
            }
            
            if (request.getParameter("accion").equals("eliminaClave")) {
                con.conectar();
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                String NomOC = request.getParameter("NomOC");
                String IdOC = request.getParameter("IdOC");
                String BanOC = request.getParameter("BanOC");
                //con.insertar("delete from tb_pedido_sialss where F_IdIsem = '" + request.getParameter("id") + "' ");
                PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_pedido_sialss WHERE F_IdIsem = ? ");
                ps.setString(1, request.getParameter("id"));
                ps.executeUpdate();
                con.cierraConexion();
                response.sendRedirect("capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "");
            }
            
            if (request.getParameter("accion").equals("eliminaClaveXtra")) {
                con.conectar();
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_capturaextra WHERE F_Id = ? ");
                ps.setString(1, request.getParameter("id"));
                ps.executeUpdate();
                con.cierraConexion();
                response.sendRedirect("capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "");
            }
            
            if (request.getParameter("accion").equals("Actualizar")) {
                sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));
                out.println("<script>window.location='capturaMedalfa.jsp'</script>");
            }
            
            if (request.getParameter("accion").equals("GeneraCodigo")) {
                String CodBar = "";
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT MAX(F_IdCb) AS F_IdCb FROM tb_gencb");
                    while (rset.next()) {
                        CodBar = rset.getString("F_IdCb");
                    }
                    System.out.println(CodBar);
                    Long CB = Long.parseLong(CodBar) + 1;
                    con.insertar("insert into tb_gencb values('" + CB + "','CEDIS CENTRAL')");
                    
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    sesion.setAttribute("CodBar", CodBar);
                    sesion.setAttribute("proveedor", request.getParameter("proveedor2"));
                    sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra2"));
                    
                    sesion.setAttribute("clave", request.getParameter("ClaPro"));
                    sesion.setAttribute("descripcion", request.getParameter("DesPro"));
                    response.sendRedirect("capturaExtraordinario.jsp?Proyecto=2&DesProyecto=MICHOACAN&Campo=F_ProMichoacan");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (request.getParameter("accion").equals("refresh")) {
                try {
                    sesion.setAttribute("CodBar", request.getParameter("CBPro"));
                    sesion.setAttribute("proveedor", request.getParameter("proveedor2"));
                    sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra2"));
                    
                    sesion.setAttribute("clave", request.getParameter("ClaPro"));
                    sesion.setAttribute("descripcion", request.getParameter("DesPro"));
                    response.sendRedirect("capturaExtraordinario.jsp?Proyecto=2&DesProyecto=MICHOACAN&Campo=F_ProMichoacan");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (request.getParameter("accion").equals("Clave")) {
                
                PreparedStatement ps;
                
                String NoCompra = request.getParameter("NoCompra");
                String BanOC = request.getParameter("BanOC");
                String Fecha = request.getParameter("Fecha");
                String Proveedor = request.getParameter("Proveedor");
                String fechaValidacion = request.getParameter("FechaVencimiento");
                String dias = request.getParameter("dias");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                if (Fecha == null) {
                    Fecha = "";
                }
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                String NomOC = request.getParameter("NomOC");
                String IdOC = request.getParameter("IdOC");
                int ban = 0;
                if (!(NoCompra.equals(""))) {
                    if (!(Proveedor.equals(""))) {
                        if (!(Fecha.equals(""))) {
                            con.conectar();
                            try {
                                
                                ps = con.getConn().prepareStatement("SELECT F_ClaPro, F_DesPro FROM tb_medica where F_ClaPro = ?");
                                ps.setString(1, request.getParameter("Clave"));
                                ResultSet rset = ps.executeQuery();

                                //ResultSet rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("Clave") + "'");
                                while (rset.next()) {
                                    ban = 1;
                                    sesion.setAttribute("clave", rset.getString(1));
                                    sesion.setAttribute("descripcion", rset.getString(2));
                                }
                                
                                ps = con.getConn().prepareStatement("SELECT o.F_DesOri FROM tb_origen AS o, tb_prodprov2017 AS pp WHERE o.F_ClaOri = pp.F_Origen AND pp.F_ClaPro=? AND pp.F_ClaProve=?");
                                ps.setString(1, request.getParameter("Clave"));
                                ps.setString(2, request.getParameter("Proveedor"));
                                rset = ps.executeQuery();
                                while (rset.next()) {
                                    sesion.setAttribute("origen", rset.getString(1));
                                }
                                sesion.setAttribute("proveedor", request.getParameter("Proveedor"));
                                sesion.setAttribute("fec_entrega", request.getParameter("Fecha"));
                                sesion.setAttribute("hor_entrega", request.getParameter("Hora"));
                                sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));
                                sesion.setAttribute("FechaVencimiento", request.getParameter("FechaVencimiento"));
                                sesion.setAttribute("dias", request.getParameter("dias"));
                                
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            con.cierraConexion();
                            
                            if (ban == 1) {
                                try {
                                    if (NoCompra.isEmpty()) {
                                        try {
                                            int ban2 = 0;
                                            con.conectar();
                                            ps = con.getConn().prepareStatement("select F_IdIsem from tb_pedido_sialss where F_NoCompra = ?");
                                            ps.setString(1, request.getParameter("NoCompra"));
                                            ResultSet rset = ps.executeQuery();
                                            //ResultSet rset = con.consulta("select F_IdIsem from tb_pedido_sialss where F_NoCompra = '" + request.getParameter("NoCompra") + "'");
                                            while (rset.next()) {
                                                ban2 = 1;
                                            }
                                            con.cierraConexion();
                                            if (ban2 == 1) {
                                                sesion.setAttribute("NoCompra", NoCompra);
                                                sesion.setAttribute("NoOrdCompra", "");
                                                sesion.setAttribute("clave", "");
                                                sesion.setAttribute("descripcion", "");
                                                out.println("<script>alert('Número de Compra ya utilizado')</script>");
                                                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                sesion.setAttribute("NoCompra", NoCompra);
                                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                            } else {
                                sesion.setAttribute("NoCompra", NoCompra);
                                sesion.setAttribute("clave", "");
                                sesion.setAttribute("descripcion", "");
                                out.println("<script>alert('Insumo Inexistente')</script>");
                                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                            }
                        } else {
                            sesion.setAttribute("NoCompra", NoCompra);
                            sesion.setAttribute("clave", "");
                            sesion.setAttribute("descripcion", "");
                            out.println("<script>alert('Favor de agregar Fecha de Entrega')</script>");
                            out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                        }
                    } else {
                        sesion.setAttribute("NoCompra", NoCompra);
                        sesion.setAttribute("clave", "");
                        sesion.setAttribute("descripcion", "");
                        out.println("<script>alert('Favor de seleccionar Proveedor')</script>");
                        out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                    }
                } else {
                    sesion.setAttribute("NoCompra", NoCompra);
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    out.println("<script>alert('Favor de agregar No OC')</script>");
                    out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
                }
                
            }
            
            if (request.getParameter("accion").equals("LimpiarOC")) {
                
                String NoCompra = "";
                String Fecha = request.getParameter("Fecha");
                String Proveedor = request.getParameter("Proveedor");
                String fechaValidacion = request.getParameter("FechaVencimiento");
                String dias = request.getParameter("dias");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                if (Fecha == null) {
                    Fecha = "";
                }
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                String NomOC = request.getParameter("NomOC");
                String IdOC = request.getParameter("IdOC");
                int ban = 0;
                
                sesion.setAttribute("NoCompra", NoCompra);
                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=0'</script>");
                
            }
            
            if (request.getParameter("accion").equals("ClaveExtra")) {
                
                PreparedStatement ps;
                
                String NoCompra = request.getParameter("NoCompra");
                String Proveedor = request.getParameter("Proveedor");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                int ban = 0;
                if (!(NoCompra.equals(""))) {
                    if (!(Proveedor.equals(""))) {
                        con.conectar();
                        try {
                            
                            ps = con.getConn().prepareStatement("SELECT F_ClaPro, F_DesPro FROM tb_medica where F_ClaPro = ?");
                            ps.setString(1, request.getParameter("Clave"));
                            ResultSet rset = ps.executeQuery();

                            //ResultSet rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("Clave") + "'");
                            while (rset.next()) {
                                ban = 1;
                                sesion.setAttribute("clave", rset.getString(1));
                                sesion.setAttribute("descripcion", rset.getString(2));
                            }
                            
                            ps = con.getConn().prepareStatement("SELECT o.F_DesOri FROM tb_origen AS o, tb_prodprov2017 AS pp WHERE o.F_ClaOri = pp.F_Origen AND pp.F_ClaPro=? AND pp.F_ClaProve=?");
                            ps.setString(1, request.getParameter("Clave"));
                            ps.setString(2, request.getParameter("Proveedor"));
                            rset = ps.executeQuery();
                            while (rset.next()) {
                                sesion.setAttribute("origen", rset.getString(1));
                            }
                            sesion.setAttribute("proveedor", request.getParameter("Proveedor"));
                            sesion.setAttribute("fec_entrega", request.getParameter("Fecha"));
                            sesion.setAttribute("hor_entrega", request.getParameter("Hora"));
                            sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));
                            
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        con.cierraConexion();
                        
                        if (ban == 1) {
                            try {
                                if (NoCompra.isEmpty()) {
                                    try {
                                        int ban2 = 0;
                                        con.conectar();
                                        ps = con.getConn().prepareStatement("select F_IdIsem from tb_pedido_sialss where F_NoCompra = ?");
                                        ps.setString(1, request.getParameter("NoCompra"));
                                        ResultSet rset = ps.executeQuery();
                                        //ResultSet rset = con.consulta("select F_IdIsem from tb_pedido_sialss where F_NoCompra = '" + request.getParameter("NoCompra") + "'");
                                        while (rset.next()) {
                                            ban2 = 1;
                                        }
                                        con.cierraConexion();
                                        if (ban2 == 1) {
                                            sesion.setAttribute("NoCompra", NoCompra);
                                            sesion.setAttribute("NoOrdCompra", "");
                                            sesion.setAttribute("clave", "");
                                            sesion.setAttribute("descripcion", "");
                                            out.println("<script>alert('Número de Compra ya utilizado')</script>");
                                            out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                                        }
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            sesion.setAttribute("NoCompra", NoCompra);
                            out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                        } else {
                            sesion.setAttribute("NoCompra", NoCompra);
                            sesion.setAttribute("clave", "");
                            sesion.setAttribute("descripcion", "");
                            out.println("<script>alert('Insumo Inexistente')</script>");
                            out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                        }
                    } else {
                        sesion.setAttribute("NoCompra", NoCompra);
                        sesion.setAttribute("clave", "");
                        sesion.setAttribute("descripcion", "");
                        out.println("<script>alert('Favor de seleccionar Proveedor')</script>");
                        out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                    }
                } else {
                    sesion.setAttribute("NoCompra", NoCompra);
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    out.println("<script>alert('Favor de agregar No OC')</script>");
                    out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                }
                
            }
            
            if (request.getParameter("accion").equals("Descripcion")) {
                String NoCompra = (String) sesion.getAttribute("NoCompra");
                int ban = 0;
                con.conectar();
                try {
                    PreparedStatement ps = con.getConn().prepareStatement("select F_ClaPro, F_DesPro from tb_medica where F_DesPro = ?");
                    ps.setString(1, request.getParameter("Descripcion"));
                    ResultSet rset = ps.executeQuery();
                    //ResultSet rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_DesPro = '" + request.getParameter("Descripcion") + "'");
                    while (rset.next()) {
                        ban = 1;
                        sesion.setAttribute("clave", rset.getString(1));
                        sesion.setAttribute("descripcion", rset.getString(2));
                    }
                    sesion.setAttribute("proveedor", request.getParameter("Proveedor"));
                    sesion.setAttribute("fec_entrega", request.getParameter("Fecha"));
                    sesion.setAttribute("hor_entrega", request.getParameter("Hora"));
                    sesion.setAttribute("FechaVencimiento", request.getParameter("FechaVencimiento"));
                    sesion.setAttribute("dias", request.getParameter("dias"));
                    sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
                
                if (ban == 1) {
                    try {
                        if (NoCompra.isEmpty()) {
                            try {
                                int ban2 = 0;
                                con.conectar();
                                //ResultSet rset = con.consulta("select F_IdIsem from tb_pedido_sialss where F_NoCompra = '" + request.getParameter("NoCompra") + "'");
                                PreparedStatement ps = con.getConn().prepareStatement("select F_IdIsem from tb_pedido_sialss where F_NoCompra = ?");
                                ps.setString(1, request.getParameter("NoCompra"));
                                ResultSet rset = ps.executeQuery();
                                while (rset.next()) {
                                    ban2 = 1;
                                }
                                con.cierraConexion();
                                if (ban2 == 1) {
                                    sesion.setAttribute("NoOrdCompra", "");
                                    sesion.setAttribute("clave", "");
                                    sesion.setAttribute("descripcion", "");
                                    sesion.setAttribute("FechaVencimiento", "");
                                    sesion.setAttribute("dias", "");
                                    out.println("<script>alert('Número de Compra ya utilizado')</script>");
                                    out.println("<script>window.location='capturaMedalfa.jsp'</script>");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    
                    out.println("<script>window.location='capturaMedalfa.jsp'</script>");
                } else {
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    out.println("<script>alert('Insumo Inexistente')</script>");
                    out.println("<script>window.location='capturaMedalfa.jsp'</script>");
                }
            }
            if (request.getParameter("accion").equals("capturar")) {
                String Proyecto = "", DesProyecto = "", Campo = "", NomOC = "", IdOC = "", BanOC = "", descuento="0", iva ="0";
                try {
                    con.conectar();
                    String ClaPro, Priori, Lote, Cadu, Cant, Observaciones, WHERE = "", ClaveSS = "", Precio = "";
                    ClaPro = request.getParameter("ClaPro");
                    Proyecto = request.getParameter("Proyecto");
                    Priori = request.getParameter("Prioridad");
                    Lote = request.getParameter("LotPro");
                    Cadu = request.getParameter("CadPro");
                    Cant = request.getParameter("CanPro");
                    DesProyecto = request.getParameter("DesProyecto");
                    NomOC = request.getParameter("NomOC");
                    IdOC = request.getParameter("IdOC");
                    Campo = request.getParameter("Campo");
                    Precio = request.getParameter("Precio");
                    descuento = request.getParameter("descuento");
                    iva = request.getParameter("iva");
                    BanOC = request.getParameter("BanOC");
                    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
                    Observaciones = (new String(a, "UTF-8")).toUpperCase();
                    if (Priori.isEmpty()) {
                        Priori = "-";
                    }
                    if (Lote.isEmpty()) {
                        Lote = "-";
                    }
                    if (Cadu.isEmpty()) {
                        Cadu = "00/00/0000";
                    }
                    
                    int i = 0, cantAnt = 0;
                    String F_IdIsem = "";
                    PreparedStatement ps = con.getConn().prepareStatement("select F_IdIsem, F_Cant from tb_pedido_sialss where F_NoCompra = ? and F_Clave = ? ");
                    ps.setString(1, (String) sesion.getAttribute("NoOrdCompra"));
                    ps.setString(2, ClaPro);
                    ResultSet rset = ps.executeQuery();
                    while (rset.next()) {
                        F_IdIsem = rset.getString("F_IdIsem");
                        i = 1;
                        cantAnt = rset.getInt("F_Cant");
                    }
                    if (i == 1) {
                        ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_Cant = ? WHERE F_IdIsem= ?");
                        ps.setInt(1, (cantAnt + Integer.parseInt(Cant)));
                        ps.setString(2, F_IdIsem);
                        ps.executeUpdate();
                    } else {
                        ps.clearParameters();
                        ps = con.getConn().prepareStatement("SELECT F_ClaProSS FROM tb_medica WHERE " + Campo + " = 1 AND F_ClaPro=?;");
                        ps.setString(1, ClaPro);
                        rset = ps.executeQuery();
                        rset.next();
                        ClaveSS = rset.getString(1);
                        ps.clearParameters();
                        ps = con.getConn().prepareStatement("INSERT INTO tb_pedido_sialss VALUES(0,?,?,?,?,'',?,?,?,?,?,CURRENT_TIMESTAMP(),?,?,?,'0','0',?,?,?,?,?,?,?,?,?)");
                        ps.setString(1, (String) sesion.getAttribute("NoOrdCompra"));
                        ps.setString(2, (String) sesion.getAttribute("proveedor"));
                        ps.setString(3, ClaPro);
                        ps.setString(4, ClaveSS);
                        ps.setString(5, Priori);
                        ps.setString(6, Lote);
                        ps.setString(7, df.format(df2.parse(Cadu)));
                        ps.setString(8, Cant);
                        ps.setString(9, Observaciones);
                        ps.setString(10, (String) sesion.getAttribute("fec_entrega"));
                        ps.setString(11, (String) sesion.getAttribute("hor_entrega"));
                        ps.setString(12, (String) sesion.getAttribute("IdUsu"));
                        ps.setString(13, Proyecto);
                        ps.setString(14, (String) sesion.getAttribute("FechaVencimiento"));
                        ps.setString(15, (String) sesion.getAttribute("dias"));
                        ps.setString(16, "0");
                        ps.setString(17, "");
                        ps.setString(18, "");
                        ps.setString(19, iva);
                        ps.setString(20, Precio);
                        ps.setString(21, descuento);
                        
                        ps.executeUpdate();
                        
                    }
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    sesion.setAttribute("origen", "");
                } catch (SQLException | UnsupportedEncodingException | NumberFormatException | ParseException e) {
                    System.out.println(e.getMessage());
                }
                
                con.cierraConexion();
                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&NomOC=" + NomOC + "&IdOC=" + IdOC + "&Campo=" + Campo + "&Ban=" + BanOC + "'</script>");
            }
            
            if (request.getParameter("accion").equals("capturarExtra")) {
                String Proyecto = "", DesProyecto = "", Campo = "";
                try {
                    con.conectar();
                    Calendar c1 = GregorianCalendar.getInstance();
                    String ClaPro, Priori, Lote, Cadu, Cant, Observaciones, WHERE = "", ClaveSS = "", Marca = "", CBPro = "", Tipo = "", NoCompra2 = "", proveedor2 = "", fechaCaducidad = "", descuento="0", iva ="0", Precio = "",factorEmpaque = "";
                    int dias = 0;
                    double IVA = 0.0;
                    ClaPro = request.getParameter("ClaPro");
                    NoCompra2 = request.getParameter("NoCompra2");
                    proveedor2 = request.getParameter("proveedor2");
                    Proyecto = request.getParameter("Proyecto");
                    Lote = request.getParameter("LotPro").toUpperCase();
                    Cadu = df21.format(df31.parse(request.getParameter("CadPro")));
                    Cant = request.getParameter("CanPro");
                    Marca = request.getParameter("list_marca");
                    CBPro = request.getParameter("CBPro");
                    DesProyecto = request.getParameter("DesProyecto");
                    Campo = request.getParameter("Campo");
                    c1.setTime(df31.parse(request.getParameter("CadPro")));
                    Precio = request.getParameter("Precio");
                    descuento = request.getParameter("descuento");
                    iva = request.getParameter("iva");
                    System.out.println("****   " + request.getParameter("fecVe"));
                    System.out.println("****   " + request.getParameter("dias"));
//                    String costo= request.getParameter("costo");
                    factorEmpaque = request.getParameter("factorEmpaque");
                    PreparedStatement ps = null;
                    ResultSet rset = null;
                    
                    
                    ps = con.getConn().prepareStatement("SELECT F_ClaProSS, F_TipMed FROM tb_medica WHERE " + Campo + " = 1 AND F_ClaPro=?;");
                    ps.setString(1, ClaPro);
                    rset = ps.executeQuery();
                    if (rset.next()) {
                        ClaveSS = rset.getString(1);
                        Tipo = rset.getString(2);
                        
                        if (Tipo.equals("2504")) {
                            c1.add(Calendar.YEAR, -3);
                            IVA = 0.0;
                        } else {
                            c1.add(Calendar.YEAR, -5);
                            IVA = 0.16;
                        }
                    }
                    
                    ps.clearParameters();
                    String fecFab = (df21.format(c1.getTime()));
                    String fechaInv = df21.format(df31.parse(request.getParameter("fecVe")));
                    /*String fechaVe = request.getParameter("fecVe").replaceAll("/", "-");
                    String [] arr = fechaVe.split("-");
                    String fechaInv = "";
                    for(int i = arr.length-1;i>=0;i--){
                        fechaInv += arr[i]+"-";
                    }*/
                    
                    Double costoUnitario =Double.parseDouble((String) request.getParameter("costoUnitario"));
                    Double total =Double.parseDouble((String) request.getParameter("total"));
                    Double impTot = total- (costoUnitario * Double.parseDouble(Cant));
                    
                    ps = con.getConn().prepareStatement("INSERT INTO tb_capturaextra VALUES (0,?,?,?,?,?,?,?,?,?,?,CURDATE(),CURTIME(),?,?,?,?,?,?, curtime(),?,?,?,?,'');");
                    ps.setString(1, NoCompra2);
                    ps.setString(2, proveedor2);
                    ps.setString(3, ClaPro);
                    ps.setString(4, ClaveSS);
                    ps.setString(5, Lote);
                    ps.setString(6, Cadu);
                    ps.setString(7, fecFab);
                    ps.setString(8, Marca);
                    ps.setString(9, CBPro);
                    ps.setString(10, Cant);
                    ps.setString(11, (String) sesion.getAttribute("IdUsu"));
                    ps.setString(12, "1");
                    ps.setString(13, Proyecto);
                    ps.setString(14, "2");
                    ps.setString(15, fechaInv);
                    ps.setString(16, request.getParameter("dias"));
                    ps.setDouble(17, costoUnitario);
                    ps.setDouble(18, impTot);
                    ps.setDouble(19, total);
                    ps.setString(20, factorEmpaque);
                    ps.execute();
                    
                    sesion.setAttribute("CodBar", "");
                    
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    
                } catch (SQLException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                } catch (ParseException ex) {
                    Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                con.cierraConexion();
                out.println("<script>window.location='capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
            }
            
            if (request.getParameter("accion").equals("cancelaOrden")) {
                con.conectar();
                try {
                    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
                    String Obser = (new String(a, "UTF-8")).toUpperCase();
                    try {
                        //con.insertar("update tb_pedido_sialss set F_StsPed = '2' where F_NoCompra = '" + request.getParameter("NoCompra") + "'  ");
                        PreparedStatement ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_StsPed = '2' WHERE F_NoCompra = ? ");
                        ps.setString(1, request.getParameter("NoCompra"));
                        ps.executeUpdate();
                    } catch (Exception e) {
                    }
                    try {
                        //con.insertar("INSERT INTO tb_obscancela VALUES('" + request.getParameter("NoCompra") + "','" + Obser + "')");
                        PreparedStatement ps = con.getConn().prepareStatement("INSERT INTO tb_obscancela VALUES(?,?)");
                        ps.setString(1, request.getParameter("NoCompra"));
                        ps.setString(2, Obser);
                        ps.executeUpdate();
                    } catch (Exception e) {
                    }
                    try {
                        correoCancela.cancelaCompra(request.getParameter("NoCompra"), (String) sesion.getAttribute("Usuario"));
                    } catch (Exception e) {
                    }
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    sesion.setAttribute("proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("hor_entrega", "");
                    sesion.setAttribute("NoOrdCompra", "");
                    sesion.setAttribute("FechaVencimiento", "");
                    sesion.setAttribute("dias", "");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
                response.sendRedirect("verFoliosM.jsp");
            }
            if (request.getParameter("accion").equals("cancelar")) {
                con.conectar();
                String Proyecto = "", DesProyecto = "", Campo = "";
                try {
                    Proyecto = request.getParameter("Proyecto");
                    DesProyecto = request.getParameter("DesProyecto");
                    Campo = request.getParameter("Campo");
                    //con.insertar("delete from tb_pedido_sialss where F_IdUsu = '" + (String) sesion.getAttribute("Usuario") + "'  ");
                    PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_pedido_sialss WHERE F_IdUsu = ? AND F_StsPed=0 ");
                    ps.setString(1, (String) sesion.getAttribute("IdUsu"));
                    ps.executeUpdate();
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    sesion.setAttribute("proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("hor_entrega", "");
                } catch (Exception e) {
                }
                con.cierraConexion();
                response.sendRedirect("crearOC.jsp");
            }
            
            if (request.getParameter("accion").equals("cancelarExtra")) {
                con.conectar();
                String Proyecto = "", DesProyecto = "", Campo = "";
                try {
                    Proyecto = request.getParameter("Proyecto");
                    DesProyecto = request.getParameter("DesProyecto");
                    Campo = request.getParameter("Campo");
                    PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_capturaextra WHERE F_IdUsu = ? AND F_StsPed=1 ");
                    ps.setString(1, (String) sesion.getAttribute("IdUsu"));
                    ps.executeUpdate();
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    sesion.setAttribute("proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("hor_entrega", "");
                    sesion.setAttribute("NoCompra", "");
                } catch (Exception e) {
                }
                con.cierraConexion();
                response.sendRedirect("capturaExtraordinario.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "");
            }
            
            if (request.getParameter("accion").equals("capturarEnseres")) {
                String costoUnitario="", descuento = "", iva= "", subtotal="", total="";
                String ClaPro, Cant;
                ClaPro = request.getParameter("ClaPro");
                Cant = request.getParameter("CanPro");
                String NoCompra = request.getParameter("NoCompra");
                String Fecha = request.getParameter("Fecha");
                String Proveedor = request.getParameter("Proveedor");
                costoUnitario = request.getParameter("costoUnitario");
                descuento = request.getParameter("descuento");
                iva = request.getParameter("iva");
                subtotal = request.getParameter("subtotal");
                total = request.getParameter("total");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                if (Fecha == null) {
                    Fecha = "";
                }
                if (!(NoCompra.equals(""))) {
                    if (!(Proveedor.equals(""))) {
                        if (!(Fecha.equals(""))) {
                            try {
                                con.conectar();
                                int i = 0, cantAnt = 0;
                                String F_IdIsem = "";
                                PreparedStatement ps = con.getConn().prepareStatement("SELECT F_Id, F_Cant FROM tb_enseresoc WHERE F_Oc = ? AND F_IdEnseres = ?;");
                                ps.setString(1, NoCompra);
                                ps.setString(2, ClaPro);
                                ResultSet rset = ps.executeQuery();
                                while (rset.next()) {
                                    F_IdIsem = rset.getString(1);
                                    i = 1;
                                    cantAnt = rset.getInt(2);
                                }
                                if (i == 1) {
                                    ps = con.getConn().prepareStatement("UPDATE tb_enseresoc SET F_Cant = ?, F_CantIngresar ? WHERE F_Id= ?");
                                    ps.setInt(1, (cantAnt + Integer.parseInt(Cant)));
                                    ps.setInt(2, (cantAnt + Integer.parseInt(Cant)));
                                    ps.setString(3, F_IdIsem);
                                    ps.executeUpdate();
                                } else {
                                    ps.clearParameters();
                                    ps = con.getConn().prepareStatement("INSERT INTO tb_enseresoc VALUES(0,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?)");
                                    ps.setString(1, ClaPro);
                                    ps.setString(2, Proveedor);
                                    ps.setString(3, Cant);
                                    ps.setString(4, Cant);
                                    ps.setString(5, Fecha);
                                    ps.setString(6, NoCompra);
                                    ps.setInt(7, 0);
                                    ps.setInt(8, 0);
                                    ps.setString(9, (String) sesion.getAttribute("IdUsu"));
                                    ps.setString(10, costoUnitario);
                                    ps.setString(11, descuento);
                                    ps.setString(12, iva);
                                    ps.setString(13, subtotal);
                                    ps.setString(14, total);
                                    ps.executeUpdate();
                                    
                                }
                                sesion.setAttribute("NoCompra", NoCompra);
                                sesion.setAttribute("Proveedor", Proveedor);
                                sesion.setAttribute("fec_entrega", Fecha);
                                con.cierraConexion();
                                out.println("<script>window.location='capturaEnseres.jsp'</script>");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            sesion.setAttribute("NoCompra", NoCompra);
                            sesion.setAttribute("Proveedor", Proveedor);
                            sesion.setAttribute("fec_entrega", Fecha);
                            out.println("<script>alert('Favor de agregar Fecha de Entrega')</script>");
                            out.println("<script>window.location='capturaEnseres.jsp'</script>");
                        }
                    } else {
                        sesion.setAttribute("NoCompra", NoCompra);
                        sesion.setAttribute("Proveedor", Proveedor);
                        sesion.setAttribute("fec_entrega", Fecha);
                        out.println("<script>alert('Favor de seleccionar Proveedor')</script>");
                        out.println("<script>window.location='capturaEnseres.jsp'</script>");
                    }
                } else {
                    sesion.setAttribute("NoCompra", NoCompra);
                    sesion.setAttribute("Proveedor", Proveedor);
                    sesion.setAttribute("fec_entrega", Fecha);
                    out.println("<script>alert('Favor de agregar No OC')</script>");
                    out.println("<script>window.location='capturaEnseres.jsp'</script>");
                }
                
            }
            
            if (request.getParameter("accion").equals("confirmarEnseres")) {
                con.conectar();
                try {
                    PreparedStatement ps = con.getConn().prepareStatement("UPDATE tb_enseresoc SET F_Sts = '1' WHERE F_Oc = ?  AND F_Usuario = ? ");
                    String noCompra;
                    noCompra = request.getParameter("NoCompra");
                    ps.setString(1, noCompra);
                    ps.setString(2, (String) sesion.getAttribute("IdUsu"));
                    ps.executeUpdate();
                    sesion.setAttribute("Proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("NoCompra", "");
                } catch (Exception e) {
                    Logger.getLogger(CapturaPedidos.class
                            .getName()).log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(CapturaPedidos.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                response.sendRedirect("capturaEnseres.jsp");
            }
            
            if (request.getParameter("accion").equals("cancelarEnseres")) {
                con.conectar();
                try {
                    PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_enseresoc WHERE F_Usuario = ? AND F_Sts = 0;");
                    ps.setString(1, (String) sesion.getAttribute("IdUsu"));
                    ps.executeUpdate();
                    sesion.setAttribute("Proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("NoCompra", "");
                } catch (Exception e) {
                }
                con.cierraConexion();
                response.sendRedirect("capturaEnseres.jsp");
            }
            
            if (request.getParameter("accion").equals("eliminaEnseres")) {
                con.conectar();
                PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_enseresoc WHERE F_Id = ? ");
                ps.setString(1, request.getParameter("id"));
                ps.executeUpdate();
                con.cierraConexion();
                response.sendRedirect("capturaEnseres.jsp");
            }
            
            if (request.getParameter("accion").equals("eliminarRemi")) {
                con.conectar();
                try {
                    //con.insertar("delete from tb_pedido_sialss where F_NoCompra = '" + request.getParameter("F_NoCompra") + "'");
                    PreparedStatement ps = con.getConn().prepareStatement("DELETE FROM tb_pedido_sialss WHERE F_NoCompra = ?");
                    ps.setString(1, request.getParameter("F_NoCompra"));
                    ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
                out.println("<script>alert('Se eliminó la orden " + request.getParameter("F_NoCompra") + " corrercetamente')</script>");
                out.println("<script>window.location='ordenesCompra.jsp'</script>");
            }
            if (request.getParameter("accion").equals("confirmarRemi")) {
                con.conectar();
                try {
                    //con.insertar("update tb_pedido_sialss set F_StsPed = '1' where F_NoCompra = '" + request.getParameter("F_NoCompra") + "'");
                    PreparedStatement ps = con.getConn().prepareStatement("update tb_pedido_sialss set F_StsPed = '1' where F_NoCompra = ?");
                    ps.setString(1, request.getParameter("F_NoCompra"));
                    ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
                out.println("<script>alert('Se validó la orden " + request.getParameter("F_NoCompra") + " corrercetamente')</script>");
                out.println("<script>window.location='ordenesCompra.jsp'</script>");
            }
            
            if (request.getParameter("accion").equals("confirmar")) {
                con.conectar();
                String Proyecto = "", DesProyecto = "", Campo = "", NomOC = "", noCompra, noCompra2 = "", BanOC = "";
                int IdOC = 0, ProyectoS = 0;
                try {
                    PreparedStatement ps = null;
                    ResultSet rset = null;
                    Proyecto = request.getParameter("Proyecto");
                    DesProyecto = request.getParameter("DesProyecto");
                    Campo = request.getParameter("Campo");
                    NomOC = request.getParameter("NomOC");
                    BanOC = request.getParameter("BanOC");
                    noCompra = request.getParameter("NoCompra");
                    //con.insertar("update tb_pedido_sialss set F_StsPed = '1' where F_NoCompra = '" + (String) sesion.getAttribute("NoOrdCompra") + "'  and F_IdUsu = '" + (String) sesion.getAttribute("Usuario") + "' ");
                    if ((BanOC.equals("1")) && (!(NomOC.equals("")))) {
                        ProyectoS = Integer.parseInt(Proyecto);
                        ps = con.getConn().prepareStatement("SELECT F_IdOC FROM tb_proyectos WHERE F_Id = ?;");
                        ps.setString(1, Proyecto);
                        rset = ps.executeQuery();
                        if (rset.next()) {
                            IdOC = rset.getInt(1);
                        }
                        ps.clearParameters();
                        
                        noCompra2 = NomOC + IdOC;
                        if ((ProyectoS == 9) || (ProyectoS == 14) || (ProyectoS == 15)) {
                            ps = con.getConn().prepareStatement("UPDATE tb_proyectos SET F_IdOC = ? WHERE F_DesProy LIKE '%THOMAS%';");
                            ps.setInt(1, IdOC + 1);
                            ps.execute();
                        } else {
                            ps = con.getConn().prepareStatement("UPDATE tb_proyectos SET F_IdOC = ? WHERE F_Id = ?;");
                            ps.setInt(1, IdOC + 1);
                            ps.setString(2, Proyecto);
                            ps.execute();
                            
                        }
                        ps.clearParameters();
                        ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_NoCompra = ?, F_StsPed = '1' WHERE F_NoCompra = ?  AND F_IdUsu = ? ");
                        ps.setString(1, noCompra2);
                        ps.setString(2, noCompra);
                        ps.setString(3, (String) sesion.getAttribute("IdUsu"));
                        ps.executeUpdate();
                    } else {
                        ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_StsPed = '1' WHERE F_NoCompra = ?  AND F_IdUsu = ? ");
                        ps.setString(1, noCompra);
                        ps.setString(2, (String) sesion.getAttribute("IdUsu"));
                        ps.executeUpdate();
                    }
                    
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    sesion.setAttribute("proveedor", "");
                    sesion.setAttribute("fec_entrega", "");
                    sesion.setAttribute("hor_entrega", "");
                    sesion.setAttribute("NoOrdCompra", "");
                    sesion.setAttribute("NoCompra", null);

                    //correo.enviaCorreo(noCompra);
                } catch (Exception e) {
                    Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, e);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                response.sendRedirect("crearOC.jsp");
            }
            if (request.getParameter("accion").equals("reactivar")) {
                con.conectar();
                try {
                    //con.insertar("update tb_pedido_sialss set F_Recibido='0' where F_NoCompra = '" + request.getParameter("NoCompra") + "'  ");
                    PreparedStatement ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_Recibido='0' WHERE F_NoCompra = ?  ");
                    ps.setString(1, request.getParameter("NoCompra"));
                    ps.executeUpdate();
                } catch (Exception e) {
                }
                con.cierraConexion();
                out.println("<script>alert('Se reactivo la orden " + request.getParameter("NoCompra") + " corrercetamente')</script>");
                out.println("<script>window.location='ordenesCompra.jsp'</script>");
            }
            if (request.getParameter("accion").equals("cerrar")) {
                con.conectar();
                try {
                    //con.insertar("update tb_pedido_sialss set F_Recibido='1' where F_NoCompra = '" + request.getParameter("NoCompra") + "'  ");
                    PreparedStatement ps = con.getConn().prepareStatement("UPDATE tb_pedido_sialss SET F_Recibido='1' WHERE F_NoCompra = ?  ");
                    ps.setString(1, request.getParameter("NoCompra"));
                    ps.executeUpdate();
                    ps.clearParameters();
                    //con.insertar("delete from tb_compratemp where F_OrdCom = '" + request.getParameter("NoCompra") + "'  ");
                    ps = con.getConn().prepareStatement("DELETE FROM tb_compratemp WHERE F_OrdCom = ?  ");
                    ps.setString(1, request.getParameter("NoCompra"));
                    ps.executeUpdate();
                } catch (Exception e) {
                }
                con.cierraConexion();
                out.println("<script>alert('Se cerró la orden " + request.getParameter("NoCompra") + " corrercetamente')</script>");
                out.println("<script>window.location='ordenesCompra.jsp'</script>");
            }
              if (request.getParameter("accion").equals("MostrarProvee")) {

                PreparedStatement ps;

                String NoCompra = request.getParameter("NoCompra");
                String Fecha = request.getParameter("Fecha");
                String Proveedor = request.getParameter("Proveedor");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                if (Fecha == null) {
                    Fecha = "";
                }
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                String TipoOC = request.getParameter("TipoOC");
                int ban = 0;

                sesion.setAttribute("proveedor", request.getParameter("Proveedor"));
                sesion.setAttribute("fec_entrega", request.getParameter("Fecha"));
                sesion.setAttribute("hor_entrega", request.getParameter("Hora"));
                sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));
                sesion.setAttribute("zona", request.getParameter("Zona"));

                sesion.setAttribute("NoCompra", NoCompra);
                out.println("<script>window.location='capturaMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "&TipoOC=" + TipoOC + "'</script>");
            }
              
              if (request.getParameter("accion").equals("ClaveAgregar")) {

                PreparedStatement ps;
                int ContarCompra = 0;
                String NoCompra = request.getParameter("NoCompra");
                String Fecha = request.getParameter("Fecha");
                String Proveedor = request.getParameter("Proveedor");
                String Zona = request.getParameter("Zona");
                String Obs = request.getParameter("Obs");
                String Tipo = request.getParameter("Tipo");
                if (NoCompra == null) {
                    NoCompra = "";
                }
                if (Fecha == null) {
                    Fecha = "";
                }
                String Proyecto = request.getParameter("Proyecto");
                String DesProyecto = request.getParameter("DesProyecto");
                String Campo = request.getParameter("Campo");
                int ban = 0;
                if (!(NoCompra.equals(""))) {

                    if (!(Proveedor.equals(""))) {
                        if (!(Fecha.equals(""))) {
                            con.conectar();
                            try {
                                ps = con.getConn().prepareStatement("SELECT F_ClaPro, F_DesPro FROM tb_medica where F_ClaPro = ?");
                                ps.setString(1, request.getParameter("Clave"));
                                ResultSet rset = ps.executeQuery();

                                //ResultSet rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("Clave") + "'");
                                while (rset.next()) {
                                    ban = 1;
                                    sesion.setAttribute("clave", rset.getString(1));
                                    sesion.setAttribute("descripcion", rset.getString(2));
                                }
                                sesion.setAttribute("proveedor", request.getParameter("Proveedor"));
                                sesion.setAttribute("fec_entrega", request.getParameter("Fecha"));
                                sesion.setAttribute("hor_entrega", request.getParameter("Hora"));
                                sesion.setAttribute("NoOrdCompra", request.getParameter("NoCompra"));

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            con.cierraConexion();

                            if (ban == 1) {

                                sesion.setAttribute("NoCompra", NoCompra);
                                out.println("<script>window.location='AgregarMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "&OC=" + NoCompra + "'</script>");
                            } else {
                                sesion.setAttribute("NoCompra", NoCompra);
                                sesion.setAttribute("clave", "");
                                sesion.setAttribute("descripcion", "");
                                out.println("<script>alert('Insumo Inexistente')</script>");
                                out.println("<script>window.location='AgregarMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                            }
                        } else {
                            sesion.setAttribute("NoCompra", NoCompra);
                            sesion.setAttribute("clave", "");
                            sesion.setAttribute("descripcion", "");
                            out.println("<script>alert('Favor de agregar Fecha de Entrega')</script>");
                            out.println("<script>window.location='AgregarMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                        }
                    } else {
                        sesion.setAttribute("NoCompra", NoCompra);
                        sesion.setAttribute("clave", "");
                        sesion.setAttribute("descripcion", "");
                        out.println("<script>alert('Favor de seleccionar Proveedor')</script>");
                        out.println("<script>window.location='AgregarMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                    }

                } else {
                    sesion.setAttribute("NoCompra", NoCompra);
                    sesion.setAttribute("clave", "");
                    sesion.setAttribute("descripcion", "");
                    out.println("<script>alert('Favor de agregar No OC')</script>");
                    out.println("<script>window.location='AgregarMedalfa.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                }

            }
            
        } catch (SQLException | IOException e) {
            Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String noCompra() {
        String indice = "0";
        try {
            con.conectar();
            ResultSet rset = con.consulta("select F_IndIsem from tb_indice");
            while (rset.next()) {
                indice = rset.getString(1);
                //con.insertar("update tb_indice set F_IndIsem = '" + (rset.getInt(1) + 1) + "'");
                PreparedStatement ps = con.getConn().prepareStatement("UPDATE tb_indice SET F_IndIsem = ?");
                ps.setInt(1, (rset.getInt(1) + 1));
                ps.executeUpdate();
                
            }
        } catch (Exception e) {
            Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(CapturaPedidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return indice;
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
