/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.CargaExcelReqRural;
import in.co.sneh.model.CargaExcelRuralModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
 * Proceso de carga el requerimiento de las unidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaExcelRural extends HttpServlet {

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
        try {
            CargaExcelReqRural lee = new CargaExcelReqRural();
            String Unidad = "";

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
                            if (CargaExcelRuralModel.processFile(path, item)) {
                                //response.getWriter().println("file uploaded successfully");
                                if (lee.obtieneArchivo(path, item.getName())) {
                                    out.println("<script>alert('Se cargó el Folio Correctamente')</script>");
                                    out.println("<script>window.location='facturacionRural/cargaRequerimento.jsp'</script>");
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
                out.println("<script>window.location='requerimiento.jsp'</script>");
                //response.sendRedirect("carga.jsp");
            }
        } finally {
            out.close();
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
