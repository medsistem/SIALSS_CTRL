/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.CargaExcelInvIni;
import conn.ConectionDB;
import in.co.sneh.model.FileUpload;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Proceso de carga de inventario
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaExcelInv extends HttpServlet {

    CargaExcelInvIni lee = new CargaExcelInvIni();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String Unidad = "";
        String Usuario = "";
        int SumaUbi = 0, SumaCla = 0, Total = 0;
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
                                    ResultSet ConsultaUbi = con.consulta("SELECT SUM(F_ValCla),SUM(F_ValUbi)FROM tb_cargainventario;");
                                    if (ConsultaUbi.next()) {
                                        SumaCla = ConsultaUbi.getInt(1);
                                        SumaUbi = ConsultaUbi.getInt(2);
                                    }

                                    Total = SumaCla + SumaUbi;

                                    if (Total > 0) {
                                        out.println("<script>alert('No cargó el Inventario Correctamente, Favor de Verificar Clave o Ubicación')</script>");
                                    } else {
                                        out.println("<script>alert('Se cargó el Inventario Correctamente')</script>");
                                    }
                                    out.println("<script>window.location='AltaInv.jsp'</script>");
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
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
            out.println("<script>alert('No se pudo cargar el Inventario, verifique las celdas')</script>");
            out.println("<script>window.location='AltaInv.jsp'</script>");
            //response.sendRedirect("carga.jsp");
        }

        /*
         * Para insertar el excel en tablas
         */
    }
}
