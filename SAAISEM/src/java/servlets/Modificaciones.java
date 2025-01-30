/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import conn.ConectionDB;
import java.sql.SQLException;
import java.text.*;

/**
 * Verifica y actualiza compra, eliminación de ori
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Modificaciones extends HttpServlet {

    ConectionDB con = new ConectionDB();
    //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();

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
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        try {
            if (request.getParameter("accion").equals("actualizarRemi")) {

                con.conectar();
                try {

                    String remi = request.getParameter("remiCorrecta");
                    String fecha = request.getParameter("fecRemision");
                    System.out.println(remi + "---" + fecha);

                    if (remi == null) {
                        remi = "";
                    }
                    if (fecha == null) {
                        fecha = "";
                    }
                    try {
                        if (!remi.equals("") && fecha.equals("")) {
                            con.insertar("update tb_compra set F_FolRemi = '" + remi + "' where F_FolRemi = '" + request.getParameter("remiIncorrecta") + "' and F_ClaDoc='" + request.getParameter("idRem") + "'");
                        }
                        if (remi.equals("") && !fecha.equals("")) {
                            con.insertar("update tb_compra set F_FecApl = '" + fecha + "' where F_FolRemi = '" + request.getParameter("remiIncorrecta") + "' and F_ClaDoc='" + request.getParameter("idRem") + "'");
                        }
                        if (!remi.equals("") && !fecha.equals("")) {
                            con.insertar("update tb_compra set F_FolRemi = '" + remi + "', F_FecApl='" + fecha + "' where F_FolRemi = '" + request.getParameter("remiIncorrecta") + "' and F_ClaDoc='" + request.getParameter("idRem") + "'");
                        }
                        out.println("<script>alert('Modificación Correcta!!')</script>");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("<script>alert('Modificación Incorrecta!!')</script>");
                }
                out.println("<script>window.location='reimpresion.jsp'</script>");
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("modificarVerifica")) {
                System.out.println("modificar");
                request.getSession().setAttribute("id", request.getParameter("id"));
                response.sendRedirect("editaClaveVerifica.jsp");
            }
            if (request.getParameter("accion").equals("eliminarVerifica")) {
                System.out.println("hola");
                try {
                    con.conectar();
                    con.insertar("delete from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                response.sendRedirect("verificarCompraAuto.jsp");
            }
            if (request.getParameter("accion").equals("verificarCompraAuto")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("update tb_compratemp set F_Estado = '2' where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();

                    sesion.setAttribute("CodBar", "");
                    sesion.setAttribute("Lote", "");
                    sesion.setAttribute("Cadu", "");
                    sesion.setAttribute("NoCompra", null);
                    response.sendRedirect("compraAuto2.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (request.getParameter("accion").equals("verificarCompraAutoISEM")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("update tb_compratempisem set F_Estado = '2' where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();

                    sesion.setAttribute("CodBar", "");
                    sesion.setAttribute("Lote", "");
                    sesion.setAttribute("Cadu", "");
                    sesion.setAttribute("NoCompra", null);
                    response.sendRedirect("compraAuto2.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (request.getParameter("accion").equals("verificarCompraManual")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("update tb_compratemp set F_Estado = '2' where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();

                    sesion.setAttribute("folio", "");
                    sesion.setAttribute("folio_remi", "");
                    sesion.setAttribute("orden", "");
                    sesion.setAttribute("provee", "");
                    response.sendRedirect("captura.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
             if (request.getParameter("accion").equals("verificarCompraManualProy")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("update tb_compratemp set F_Estado = '2' where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();

                    sesion.setAttribute("folio", "");
                    sesion.setAttribute("folio_remi", "");
                    sesion.setAttribute("orden", "");
                    sesion.setAttribute("provee", "");
                    response.sendRedirect("capturaProyecto.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (request.getParameter("accion").equals("verificarCompraManualCross")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("update tb_compratemp set F_Estado = '3' where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();

                    sesion.setAttribute("folio", "");
                    sesion.setAttribute("folio_remi", "");
                    sesion.setAttribute("orden", "");
                    sesion.setAttribute("provee", "");
                    response.sendRedirect("capturaCross.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            if (request.getParameter("accion").equals("eliminarCompraAuto")) {
                System.out.println("eliminar");
                try {
                    con.conectar();
                    con.borrar2("delete from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                response.sendRedirect("compraAuto2.jsp");
            }
            if (request.getParameter("accion").equals("eliminarCompraAutoISEM")) {
                System.out.println("eliminar");
                try {
                    con.conectar();
                    con.borrar2("delete from tb_compratempisem where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                response.sendRedirect("compraAuto2.jsp");
            }
            if (request.getParameter("accion").equals("modificarCompraAuto")) {
                System.out.println("modificar");
                request.getSession().setAttribute("id", request.getParameter("id"));
                response.sendRedirect("editaClaveCompraAuto.jsp");
            }
            if (request.getParameter("accion").equals("eliminar")) {
                System.out.println("eliminar");
                try {
                    con.conectar();
                    con.borrar2("delete from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                response.sendRedirect("captura.jsp");
            }
            
             if (request.getParameter("accion").equals("eliminarProy")) {
                System.out.println("eliminar");
                try {
                    con.conectar();
                    con.borrar2("delete from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                response.sendRedirect("capturaProyecto.jsp");
            }
             
            if (request.getParameter("accion").equals("eliminarCross")) {
                System.out.println("eliminar");
                try {
                    con.conectar();
                    con.borrar2("delete from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    con.cierraConexion();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                response.sendRedirect("capturaCross.jsp");
            }
            if (request.getParameter("accion").equals("modificar")) {
                System.out.println("modificar");
                request.getSession().setAttribute("id", request.getParameter("id"));
                response.sendRedirect("edita_clave.jsp");
            }
            if (request.getParameter("accion").equals("actualizar")) {
                System.out.println("actualizar");
                int cajas = Integer.parseInt((request.getParameter("Cajas")).replace(",", ""));
                int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));

                try {
                    con.conectar();
                    String idTemp = "";
                    ResultSet rsetId = con.consulta("select * from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    while (rsetId.next()) {
                        ResultSet rset = con.consulta("select F_IdCom from tb_compraregistro where F_ClaPro = '" + rsetId.getString("F_ClaPro") + "' and F_Lote = '" + rsetId.getString("F_Lote") + "' and F_FecCad = '" + rsetId.getString("F_FecCad") + "' and F_FecFab = '" + rsetId.getString("F_FecFab") + "' and F_Marca = '" + rsetId.getString("F_Marca") + "' and F_Cb = '" + rsetId.getString("F_Cb") + "' and F_Pz = '" + rsetId.getString("F_Pz") + "' and F_Resto = '" + rsetId.getString("F_Resto") + "' and F_ComTot = '" + rsetId.getString("F_ComTot") + "' and F_FolRemi = '" + rsetId.getString("F_FolRemi") + "' and F_OrdCom = '" + rsetId.getString("F_OrdCom") + "' ");
                        while (rset.next()) {
                            idTemp = rset.getString(1);
                        }
                    }

                    byte[] a = request.getParameter("pres").getBytes("ISO-8859-1");
                    String pres = new String(a, "UTF-8");
                    a = request.getParameter("Marca").getBytes("ISO-8859-1");
                    String marca = new String(a, "UTF-8");

                    con.actualizar("update tb_compratemp set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "'  where F_IdCom = '" + request.getParameter("id") + "' ");
                    con.actualizar("update tb_compraregistro set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_User = '" + sesion.getAttribute("nombre") + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "'  where F_IdCom = '" + idTemp + "' ");
                    con.cierraConexion();
                    out.println("<script>alert('Modificación Correcta')</script>");
                    out.println("<script>window.location='captura.jsp'</script>");
                } catch (Exception e) {
                    System.out.println("----" + e.getMessage());
                    out.println("<script>alert('Modificación incorrecta!!')</script>");
                    out.println("<script>window.location='edita_clave.jsp'</script>");
                }
            }

            if (request.getParameter("accion").equals("actualizarCompraAuto")) {
                System.out.println("actualizar");
                int cajas = Integer.parseInt((request.getParameter("Cajas")).replace(",", ""));
                int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));
                int piezasxCaja = Integer.parseInt((request.getParameter("PzsxCC")).replace(",",""));
                try {
                    con.conectar();
                    String idTemp = "";
                    ResultSet rsetId = con.consulta("select * from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    while (rsetId.next()) {
                        ResultSet rset = con.consulta("select F_IdCom from tb_compraregistro where F_ClaPro = '" + rsetId.getString("F_ClaPro") + "' and F_Lote = '" + rsetId.getString("F_Lote") + "' and F_FecCad = '" + rsetId.getString("F_FecCad") + "' and F_FecFab = '" + rsetId.getString("F_FecFab") + "' and F_Marca = '" + rsetId.getString("F_Marca") + "' and F_Cb = '" + rsetId.getString("F_Cb") + "' and F_Pz = '" + rsetId.getString("F_Pz") + "' and F_Resto = '" + rsetId.getString("F_Resto") + "' and F_ComTot = '" + rsetId.getString("F_ComTot") + "' and F_FolRemi = '" + rsetId.getString("F_FolRemi") + "' and F_OrdCom = '" + rsetId.getString("F_OrdCom") + "' ");
                        while (rset.next()) {
                            idTemp = rset.getString(1);
                        }
                    }

                    byte[] a = request.getParameter("pres").getBytes("ISO-8859-1");
                    String pres = new String(a, "UTF-8");
                    a = request.getParameter("Marca").getBytes("ISO-8859-1");
                    String marca = new String(a, "UTF-8");

                    con.actualizar("update tb_compratemp set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_PzaCaja = '"+piezasxCaja+"', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "'  where F_IdCom = '" + request.getParameter("id") + "' ");
                    con.actualizar("update tb_compraregistro set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_User = '" + sesion.getAttribute("nombre") + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "'  where F_IdCom = '" + idTemp + "' ");
                    con.cierraConexion();
                    out.println("<script>alert('Modificación Correcta')</script>");
                    out.println("<script>window.location='compraAuto2.jsp'</script>");
                } catch (Exception e) {
                    System.out.println("----" + e.getMessage());
                    out.println("<script>alert('Modificación incorrecta!!')</script>");
                    out.println("<script>window.location='editaClaveCompraAuto.jsp'</script>");
                }
            }

            if (request.getParameter("accion").equals("actualizarVerifica")) {
                System.out.println("actualizar");
                int cajas = Integer.parseInt((request.getParameter("Cajas")).replace(",", ""));
                int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));

                try {
                    con.conectar();
                    String idTemp = "";
                    ResultSet rsetId = con.consulta("select * from tb_compratemp where F_IdCom = '" + request.getParameter("id") + "'");
                    while (rsetId.next()) {
                        ResultSet rset = con.consulta("select F_IdCom from tb_compraregistro where F_ClaPro = '" + rsetId.getString("F_ClaPro") + "' and F_Lote = '" + rsetId.getString("F_Lote") + "' and F_FecCad = '" + rsetId.getString("F_FecCad") + "' and F_FecFab = '" + rsetId.getString("F_FecFab") + "' and F_Marca = '" + rsetId.getString("F_Marca") + "' and F_Cb = '" + rsetId.getString("F_Cb") + "' and F_Pz = '" + rsetId.getString("F_Pz") + "' and F_Resto = '" + rsetId.getString("F_Resto") + "' and F_ComTot = '" + rsetId.getString("F_ComTot") + "' and F_FolRemi = '" + rsetId.getString("F_FolRemi") + "' and F_OrdCom = '" + rsetId.getString("F_OrdCom") + "' ");
                        while (rset.next()) {
                            idTemp = rset.getString(1);
                        }
                    }

                    byte[] a = request.getParameter("pres").getBytes("ISO-8859-1");
                    String pres = new String(a, "UTF-8");
                    a = request.getParameter("Marca").getBytes("ISO-8859-1");
                    String marca = new String(a, "UTF-8");

                    con.actualizar("update tb_compratemp set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "' where F_IdCom = '" + request.getParameter("id") + "' ");
                    con.actualizar("update tb_compraregistro set F_Cb = '" + request.getParameter("cb").toUpperCase() + "', F_lote = '" + request.getParameter("Lote").toUpperCase() + "', F_FecCad = '" + df2.format(df3.parse(request.getParameter("Caducidad").toUpperCase())) + "', F_Cajas= '" + cajas + "', F_Pz = '" + piezas + "', F_Resto = '" + request.getParameter("Resto") + "', F_Tarimas='" + tarimas + "', F_TarimasI='" + request.getParameter("TarimasI") + "', F_CajasI = '" + request.getParameter("CajasxTI") + "', F_FecFab='" + df2.format(df3.parse(request.getParameter("FecFab").toUpperCase())) + "', F_User = '" + sesion.getAttribute("nombre") + "', F_Cb = '" + request.getParameter("cb") + "', F_FolRemi = '" + request.getParameter("folio_remi") + "'  where F_IdCom = '" + idTemp + "' ");
                    con.cierraConexion();
                    out.println("<script>alert('Modificación Correcta')</script>");
                    out.println("<script>window.location='verificarCompraAuto.jsp'</script>");
                } catch (Exception e) {
                    System.out.println("----" + e.getMessage());
                    out.println("<script>alert('Modificación incorrecta!!')</script>");
                    out.println("<script>window.location='editaClaveVerifica.jsp'</script>");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
