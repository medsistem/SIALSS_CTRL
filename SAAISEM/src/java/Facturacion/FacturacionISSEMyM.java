/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 * Facturación issemym
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class FacturacionISSEMyM extends HttpServlet {

    ConectionDB con = new ConectionDB();
//    ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
    java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        String clave = "", descr = "";
        int ban1 = 0;
        try {
            if (request.getParameter("accion").equals("consultar")) {
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT * FROM tb_unireq WHERE F_ClaUni = '" + request.getParameter("Nombre") + "' GROUP BY F_ClaUni");
                    while (rset.next()) {
                        ban1 = 1;
                        clave = rset.getString("F_ClaUni");
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }

            if (request.getParameter("accion").equals("cancelar")) {
                try {
                    con.conectar();
                    ban1 = 1;
                    con.insertar("update tb_unireq set F_Status = '1' where F_ClaUni = '" + request.getParameter("Nombre") + "' ");
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }

            if (request.getParameter("accion").equals("guardar")) {
                ban1 = 1;
                String ClaUni = request.getParameter("Nombre");
                String FechaE = request.getParameter("FecFab");
                String Clave = "", Caducidad = "", FolioLote = "", Lote = "", Ubicacion = "";
                int piezas = 0, existencia = 0, diferencia = 0, ContaLot = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, ClaProve = 0, Org = 0, FolMov = 0, FolioMovi = 0;
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;

                try {

                    con.conectar();
                    //consql.conectar();
                    ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice");
                    while (FolioFact.next()) {
                        FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFact"));
                    }
                    FolFact = FolioFactura + 1;
                    con.actualizar("update tb_indice set F_IndFact='" + FolFact + "'");

                    ResultSet rset_cantidad = con.consulta("SELECT F_ClaPro,SUM(F_PiezasReq) AS CANTIDAD, F_IdReq FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_Status='0' and F_FecCarg = CURDATE() GROUP BY F_ClaPro");
                    while (rset_cantidad.next()) {
                        Clave = rset_cantidad.getString("F_ClaPro");
                        piezas = Integer.parseInt(rset_cantidad.getString("CANTIDAD"));

                        //INICIO DE CONSULTA MYSQL
                        ResultSet r_Org = con.consulta("SELECT F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + Clave + "' GROUP BY F_ClaOrg ORDER BY F_ClaOrg+0");
                        while (r_Org.next()) {
                            Org = Integer.parseInt(r_Org.getString("F_ClaOrg"));
                        }
                        if (Org == 1) {
                            ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,L.F_ExiLot AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' AND L.F_ClaOrg='" + Org + "' ORDER BY L.F_FecCad ASC");
                            while (FechaLote.next()) {
                                Caducidad = FechaLote.getString("F_FecCad");
                                FolioLote = FechaLote.getString("F_FolLot");
                                String ClaLot = FechaLote.getString("F_ClaLot");
                                existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                Costo = Double.parseDouble(FechaLote.getString("F_Costo"));
                                Ubicacion = FechaLote.getString("F_Ubica");
                                ClaProve = Integer.parseInt(FechaLote.getString("F_ProVee"));
                                if (Tipo == 2504) {
                                    IVA = 0.0;
                                } else {
                                    IVA = 0.16;
                                }

                                if (piezas > existencia) {
                                    diferencia = piezas - existencia;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_FolLot='" + FolioLote + "'");

                                    IVAPro = (existencia * Costo) * IVA;
                                    Monto = existencia * Costo;
                                    MontoIva = Monto + IVAPro;

                                    ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                    while (FolioMov.next()) {
                                        FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                    }
                                    FolMov = FolioMovi + 1;
                                    con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                    Logger.getLogger(FacturacionISSEMyM.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + existencia + " ");
                                    con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + existencia + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + existencia + "','" + existencia + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                    piezas = diferencia;
                                } else {
                                    diferencia = existencia - piezas;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + FolioLote + "'");

                                    IVAPro = (piezas * Costo) * IVA;
                                    Monto = piezas * Costo;
                                    MontoIva = Monto + IVAPro;

                                    if (piezas > 0) {
                                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                        while (FolioMov.next()) {
                                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                        }
                                        FolMov = FolioMovi + 1;
                                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                        Logger.getLogger(FacturacionISSEMyM.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + piezas + " ");
                                        con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + piezas + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezas + "','" + piezas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + FolioLote + "'");
                                    }
                                    piezas = 0;
                                }
                            }
                        } else {
                            ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,L.F_ExiLot AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' ORDER BY L.F_FecCad ASC");
                            while (FechaLote.next()) {
                                Caducidad = FechaLote.getString("F_FecCad");
                                FolioLote = FechaLote.getString("F_FolLot");
                                String ClaLot = FechaLote.getString("F_ClaLot");
                                existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                Costo = Double.parseDouble(FechaLote.getString("F_Costo"));
                                Ubicacion = FechaLote.getString("F_Ubica");
                                ClaProve = Integer.parseInt(FechaLote.getString("F_ProVee"));
                                if (Tipo == 2504) {
                                    IVA = 0.0;
                                } else {
                                    IVA = 0.16;
                                }
                                if (piezas > existencia) {
                                    diferencia = piezas - existencia;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_FolLot='" + FolioLote + "'");

                                    IVAPro = (existencia * Costo) * IVA;
                                    Monto = existencia * Costo;
                                    MontoIva = Monto + IVAPro;

                                    ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                    while (FolioMov.next()) {
                                        FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                    }
                                    FolMov = FolioMovi + 1;
                                    con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                    Logger.getLogger(FacturacionISSEMyM.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + existencia + " ");
                                    con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + existencia + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + existencia + "','" + existencia + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");

                                    piezas = diferencia;
                                } else {
                                    diferencia = existencia - piezas;
                                    con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + FolioLote + "'");

                                    IVAPro = (piezas * Costo) * IVA;
                                    Monto = piezas * Costo;
                                    MontoIva = Monto + IVAPro;

                                    if (piezas >= 1) {
                                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                        while (FolioMov.next()) {
                                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                        }
                                        FolMov = FolioMovi + 1;
                                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                                        Logger.getLogger(FacturacionISSEMyM.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioLote + ", ubicacion = " + Ubicacion + ", piezas = " + piezas + " ");
                                        con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + piezas + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezas + "','" + piezas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + FolioLote + "'");
                                    }
                                    piezas = 0;
                                }
                            }
                        }

                        con.actualizar("update tb_unireq set F_Status='1' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");
                    }
                    byte[] a = request.getParameter("Obs").getBytes("ISO-8859-1");
                    String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                    con.insertar("insert into tb_obserfact values ('" + FolioFactura + "','" + Observaciones + "',0,'" + request.getParameter("F_Req").toUpperCase() + "')");
                    con.actualizar("delete * FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_FecCarg = CURDATE()");
                    con.cierraConexion();
                    //consql.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                out.println("<script>window.open('reimpFactura.jsp?fol_gnkl=" + FolioFactura + "','_blank')</script>");
            }

        } catch (Exception e) {
        }
        request.getSession().setAttribute("folio", request.getParameter("folio"));
        request.getSession().setAttribute("fecha", request.getParameter("fecha"));
        request.getSession().setAttribute("folio_remi", request.getParameter("folio_remi"));
        request.getSession().setAttribute("orden", request.getParameter("orden"));
        request.getSession().setAttribute("provee", request.getParameter("provee"));
        request.getSession().setAttribute("recib", request.getParameter("recib"));
        request.getSession().setAttribute("entrega", request.getParameter("entrega"));
        request.getSession().setAttribute("clave", clave);
        request.getSession().setAttribute("descrip", descr);

        //String original = "hello world";
        //byte[] utf8Bytes = original.getBytes("UTF8");
        //String value = new String(utf8Bytes, "UTF-8"); 
        //out.println(value);
        if (ban1 == 0) {
            out.println("<script>alert('Clave Inexistente')</script>");
            out.println("<script>window.location='factura.jsp'</script>");
        } else {
            out.println("<script>window.location='factura.jsp'</script>");
        }
        //response.sendRedirect("captura.jsp");
    }

    public String dame7car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 7 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
    }

    public String dame5car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 5 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
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
