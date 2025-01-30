/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelRecibo;
import in.co.sneh.model.FileUpload;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
 * @author Anibal MEDALFA
 */
@WebServlet(name = "FileUploadServletRecibo", urlPatterns = {"/FileUploadServletRecibo"})
public class FileUploadServletRecibo extends HttpServlet {

    LeeExcelRecibo lee = new LeeExcelRecibo();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String Usuario;
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            ServletFileUpload upload = new ServletFileUpload();
            try {
                HttpSession sesion = request.getSession(true);
                Usuario = (String) sesion.getAttribute("IdUsu");

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
                                out.println("<script>alert('El archivo se cargo correctamente')</script>");
                                out.println("<script>window.location='cargarecibo.jsp'</script>");
                            }else{
                                out.println("<script>alert('No se pudo cargar, verifique las celdas')</script>");
                                out.println("<script>window.location='cargarecibo.jsp'</script>");
                            }
                        }
                    }
                }
            } catch (FileUploadException e) {
                Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            out.println("<script>alert('No se pudo cargar, verifique las celdas')</script>");
            out.println("<script>window.location='requerimiento.jsp'</script>");
        }

    }
}
