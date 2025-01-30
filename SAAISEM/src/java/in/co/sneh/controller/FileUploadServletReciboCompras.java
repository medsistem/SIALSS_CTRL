/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelReciboCompras;
import conn.ConectionDB;
import in.co.sneh.model.FileUpload;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "FileUploadServletReciboCompras", urlPatterns = {"/FileUploadServletReciboCompras"})
public class FileUploadServletReciboCompras extends HttpServlet {

    LeeExcelReciboCompras lee = new LeeExcelReciboCompras();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String Usuario;

        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            ServletFileUpload upload = new ServletFileUpload();
            String Proyecto = "", DesProyecto = "", Campo = "";
            try {

                HttpSession sesion = request.getSession(true);
                Usuario = (String) sesion.getAttribute("nombre");
                Proyecto = (String) sesion.getAttribute("ProyectoSel");
                DesProyecto = (String) sesion.getAttribute("DesProyectoSel");
                Campo = (String) sesion.getAttribute("CampoSel");

                FileItemIterator itr = upload.getItemIterator(request);
                while (itr.hasNext()) {
                    FileItemStream item = itr.next();
                    if (item.isFormField()) {
                        String fielName = item.getFieldName();
                        InputStream is = item.openStream();
                        byte[] b = new byte[is.available()];
                        is.read(b);
                        String value = new String(b);
                        response.getWriter().println(fielName + ":" + value + "<br/>");
                    } else {
                        String path = getServletContext().getRealPath("/");
                        if (FileUpload.processFile(path, item)) {
                            if (lee.obtieneArchivo(path, item.getName(), Usuario, Proyecto, Campo)) {

                                try {
                                    ConectionDB con = new ConectionDB();
                                    con.conectar();
                                    String PedidoMal = "";
                                    int NoRegBien = 0, NoRegMal = 0;
                                    ResultSet Consulta = con.consulta("SELECT F_NoOc, F_Proveedor, F_Proyecto, COUNT(*), SUM(F_ProblemaClave), SUM(F_ProblemaProvee), SUM(F_ProblemaProyecto) FROM tb_cargaoc WHERE F_Usu = '" + Usuario + "' GROUP BY F_NoOc, F_Proveedor, F_Proyecto;");
                                    while (Consulta.next()) {
                                        String Oc = Consulta.getString(1);
                                        String Provee = Consulta.getString(2);
                                        String ProyectoC = Consulta.getString(3);
                                        int NoReg = Consulta.getInt(4);
                                        int NoClave = Consulta.getInt(5);
                                        int NoProvee = Consulta.getInt(6);
                                        int NoProyecto = Consulta.getInt(7);

                                        if ((NoReg == NoClave) && (NoReg == NoProvee) && (NoReg == NoProyecto)) {
                                            NoRegBien++;
                                        } else {
                                            PedidoMal = PedidoMal + " / No OC " + Oc + " / Proveedor " + Provee + " / Proyecto " + ProyectoC;
                                            NoRegMal++;
                                        }
                                    }
                                    if (NoRegMal > 0) {
                                        int BanClave = 0, BanProvee = 0, BanProyecto = 0;
                                        String ClaveMal = "", ProveeMal = "", ProyectoMal = "", Mensaje = "";
                                        out.println("<script>alert('Pedidos no cargados =" + PedidoMal + "')</script>");
                                        ResultSet ConsultaC = con.consulta("SELECT GROUP_CONCAT(F_Clave) FROM tb_cargaoc WHERE F_ProblemaClave = 0 AND F_Usu='" + Usuario + "';");
                                        while (ConsultaC.next()) {
                                            ClaveMal = ConsultaC.getString(1);
                                            if (ClaveMal != null) {
                                                BanClave = 1;
                                            }
                                        }

                                        ResultSet ConsultaP = con.consulta("SELECT GROUP_CONCAT(F_Proveedor) FROM tb_cargaoc WHERE F_ProblemaProvee = 0 AND F_Usu='" + Usuario + "';");
                                        while (ConsultaP.next()) {
                                            ProveeMal = ConsultaP.getString(1);
                                            if (ProveeMal != null) {
                                                BanProvee = 1;
                                            }
                                        }

                                        ResultSet ConsultaR = con.consulta("SELECT GROUP_CONCAT(F_Proyecto) FROM tb_cargaoc WHERE F_ProblemaProyecto = 0 AND F_Usu='" + Usuario + "';");
                                        while (ConsultaR.next()) {
                                            ProyectoMal = ConsultaR.getString(1);
                                            if (ProyectoMal != "") {
                                                BanProyecto = 1;
                                            }
                                        }

                                        if (BanProvee == 1) {
                                            Mensaje = " Proveedor no registrado = " + ProveeMal;
                                        } else if (BanClave == 1) {
                                            Mensaje = Mensaje + " Claves no registradas = " + ClaveMal;
                                        } else if (BanProyecto == 1) {
                                            Mensaje = Mensaje + " Proyecto no registrado = " + ProyectoMal;
                                        }
                                        BanClave = 0;
                                        BanProvee = 0;
                                        BanProyecto = 0;
                                        out.println("<script>alert('Problema de la Carga " + Mensaje + "')</script>");
                                        out.println("<script>window.location='cargareciboOC.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
                                    }
                                    if (NoRegMal == 0) {
                                        out.println("<script>alert('Pedidos cargados Correctamente')</script>");
                                        out.println("<script>window.location='cargarOC.jsp'</script>");
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("LeeExcel").log(Level.SEVERE, null, e);
                                }
                                //out.println("<script>window.location='cargarOC.jsp'</script>");
                            }
                        }
                    }
                }
            } catch (FileUploadException e) {
                Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            out.println("<script>alert('No se pudo cargar, verifique las celdas')</script>");
            out.println("<script>window.location='cargareciboOC.jsp?Proyecto=" + Proyecto + "&DesProyecto=" + DesProyecto + "&Campo=" + Campo + "'</script>");
        }

    }
}
