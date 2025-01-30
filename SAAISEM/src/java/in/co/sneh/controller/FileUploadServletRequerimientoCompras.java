/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelRequerimientoCompras;
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
@WebServlet(name = "FileUploadServletRequerimientoCompras", urlPatterns = {"/FileUploadServletRequerimientoCompras"})
public class FileUploadServletRequerimientoCompras extends HttpServlet {

    LeeExcelRequerimientoCompras lee = new LeeExcelRequerimientoCompras();

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
                            if (lee.obtieneArchivo(path, item.getName(), Usuario)) {

                                try {
                                    ConectionDB con = new ConectionDB();
                                    con.conectar();
                                    String PedidoMal = "";
                                    int NoRegBien = 0, NoRegMal = 0;
                                    ResultSet Consulta = con.consulta("SELECT F_ClaUni, F_ClaPro, COUNT(*), SUM(F_ProblemaProducto), SUM(F_ProblemaUnidad) FROM tb_cargareqcompra WHERE F_User = '" + Usuario + "' GROUP BY F_ClaUni;");
                                    while (Consulta.next()) {
                                        String ClaUni = Consulta.getString(1);
                                        int NoReg = Consulta.getInt(3);
                                        int NoProducto = Consulta.getInt(4);
                                        int NoUnidad = Consulta.getInt(5);

                                        if ((NoReg == NoProducto) && (NoReg == NoUnidad)) {
                                            NoRegBien++;
                                        } else {
                                            PedidoMal = PedidoMal + " / Unidad " + ClaUni;
                                            NoRegMal++;
                                        }
                                    }
                                    if (NoRegMal > 0) {
                                        int BanClave = 0;
                                        String ClaveMal = "", ProveeMal = "", ProyectoMal = "", Mensaje = "";
                                        out.println("<script>alert('Pedidos no cargados =" + PedidoMal + "')</script>");
                                        ResultSet ConsultaC = con.consulta("SELECT GROUP_CONCAT(F_ClaPro) FROM tb_cargareqcompra WHERE F_ProblemaProducto = 0 AND F_User='" + Usuario + "';");
                                        while (ConsultaC.next()) {
                                            ClaveMal = ConsultaC.getString(1);
                                            if (ClaveMal != null) {
                                                BanClave = 1;
                                            }
                                        }

                                        if (BanClave == 1) {
                                            Mensaje = Mensaje + " Claves no registradas = " + ClaveMal;
                                        }
                                        BanClave = 0;
                                        out.println("<script>alert('Problema de la Carga " + Mensaje + "')</script>");
                                        out.println("<script>window.location='requerimientoCompras.jsp'</script>");
                                    }
                                    if (NoRegMal == 0) {
                                        out.println("<script>alert('Pedidos cargados Correctamente')</script>");
                                        out.println("<script>window.location='requerimientoCompras.jsp'</script>");
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
            out.println("<script>window.location='requerimientoCompras.jsp'</script>");
        }

    }
}
