/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelRutas;
import in.co.sneh.model.SubeRutas;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.commons.fileupload.FileItemIterator;
import javax.servlet.http.HttpSession;
import conn.*;

/**
 * Proceso de carga ruta unidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class SubeRutasServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String Unidad = "";
        LeeExcelRutas lee = new LeeExcelRutas();

        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            ServletFileUpload upload = new ServletFileUpload();
            try {
                HttpSession sesion = request.getSession(true);
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
                        if (SubeRutas.processFile(path, item)) {
                            //response.getWriter().println("file uploaded successfully");
                            String mensaje = "";
                            try {
                                mensaje = lee.obtieneArchivo(path, item.getName(), (String) sesion.getAttribute("nombre"));
                            } catch (Exception e) {
                                mensaje = e.getMessage();
                            }
                            out.println("<script>alert('" + mensaje + "')</script>");
                            //response.sendRedirect("cargaFotosCensos.jsp");
                        } else {
                            out.println("<script>alert('No se pudo cargar el Folio')</script>");
                        }
                    }
                }
            } catch (FileUploadException fue) {
                fue.printStackTrace();
            }
            out.println("<script>window.location='operaciones/subeRutas.jsp'</script>");
            //response.sendRedirect("carga.jsp");
        }

        /*
         * Para insertar el excel en tablas
         */
    }
}
