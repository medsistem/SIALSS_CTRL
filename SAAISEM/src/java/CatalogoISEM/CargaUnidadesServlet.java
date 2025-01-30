/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CatalogoISEM;

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
 * Servlet cargar unidades medicas
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaUnidadesServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String Unidad = "";

        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {

            LeeExcelUnidades lee = new LeeExcelUnidades();
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
                        if (CargaUnidades.processFile(path, item)) {
                            //response.getWriter().println("file uploaded successfully");
                            if (lee.obtieneArchivo(path, item.getName())) {
                                out.println("<script>alert('Se cargó el Folio Correctamente')</script>");
                                out.println("<script>window.location='catalogoUniDis.jsp'</script>");
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
            out.println("<script>alert('No se pudo cargar el Folio, verifique las celdas')</script>");
            out.println("<script>window.location='catalogoUniDis.jsp'</script>");
            //response.sendRedirect("carga.jsp");
        }

        /*
         * Para insertar el excel en tablas
         */
    }
}
