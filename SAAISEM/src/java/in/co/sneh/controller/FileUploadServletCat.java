/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelCatalogoUnidad;
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
 * @author Anibal GNKL
 */
@WebServlet(name = "FileUploadServletCat", urlPatterns = {"/FileUploadServletCat"})
public class FileUploadServletCat extends HttpServlet {

    LeeExcelCatalogoUnidad lee = new LeeExcelCatalogoUnidad();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String Unidad = "";
        String Usuario = "";
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            ServletFileUpload upload = new ServletFileUpload();
            try {
                HttpSession sesion = request.getSession(true);
                try {
                    Usuario = (String) sesion.getAttribute("nombre");
                } catch (Exception e) {
                    System.out.println();
                }

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
                            //response.getWriter().println("file uploaded successfully");
                            if (lee.obtieneArchivo(path, item.getName(), Usuario)) {

                                try {
                                    con.conectar();
                                    String PedidoMal = "";
                                    int NoRegBien = 0, NoRegMal = 0;
                                    ResultSet Consulta = con.consulta("SELECT F_ClaUni, COUNT(*), SUM(F_ProblemaUni), SUM(F_ProblemaPro) FROM tb_cargacatunidad WHERE F_User = '" + Usuario + "' GROUP BY F_ClaUni;");
                                    while (Consulta.next()) {
                                        String UnidadR = Consulta.getString(1);
                                        int NoReg = Consulta.getInt(2);
                                        int NoUni = Consulta.getInt(3);
                                        int NoClave = Consulta.getInt(4);

                                        if ((NoReg == NoClave) && (NoReg == NoUni)) {
                                            NoRegBien++;
                                        } else {
                                            PedidoMal = PedidoMal + " / Unidad  " + UnidadR;
                                            NoRegMal++;
                                        }
                                    }
                                    if (NoRegMal > 0) {
                                        int BanClave = 0;
                                        String ClaveMal = "", Mensaje = "";
                                        out.println("<script>alert('Unidad no cargados =" + PedidoMal + "')</script>");
                                        ResultSet ConsultaC = con.consulta("SELECT GROUP_CONCAT(F_ClaPro) FROM tb_cargacatunidad WHERE F_ProblemaPro = 0 AND F_User = '" + Usuario + "';");
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
                                        out.println("<script>window.location='CatalogoUnidadCargar.jsp'</script>");
                                    }
                                    if (NoRegMal == 0) {
                                        out.println("<script>alert('Pedidos cargados Correctamente')</script>");
                                        out.println("<script>window.location='CatalogoUnidadCargar.jsp'</script>");
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    Logger.getLogger("LeeExcel").log(Level.SEVERE, null, e);
                                }
                            }
                            //response.sendRedirect("cargaFotosCensos.jsp");
                        } else {
                            //response.getWriter().println("file uploading falied");
                            //response.sendRedirect("cargaFotosCensos.jsp");
                        }
                    }
                }
            } catch (FileUploadException fue) {
                fue.printStackTrace();
            }
            //out.println("<script>alert('No se pudo cargar el Folio, verifique las celdas')</script>");
            //out.println("<script>window.location='CatalogoUnidadCargar.jsp'</script>");
            //response.sendRedirect("carga.jsp");
        }

        /*
         * Para insertar el excel en tablas
         */
    }
}
