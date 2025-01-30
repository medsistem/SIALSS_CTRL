/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISO9000;

import conn.ConectionDB;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Proceso para poder ver los archvios iso pdf
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class VerPDF extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConectionDB con = new ConectionDB();
        response.setContentType("text/html;charset=UTF-8");

        ServletOutputStream outs = response.getOutputStream();
//---------------------------------------------------------------
// Set the output data's mime type
//---------------------------------------------------------------
        response.setContentType("application/pdf");  // MIME type for pdf doc
//---------------------------------------------------------------
// create an input stream from fileURL
//---------------------------------------------------------------

        try {
            File file;
            String OC = "";
            con.conectar();
            String path = getServletContext().getRealPath("/");
            OC = request.getParameter("id");
            ResultSet Consulta = con.consulta("SELECT F_NomCom FROM tb_iso WHERE F_IdFor='" + OC + "';");
            if (Consulta.next()) {
                file = new File(path + "\\iso9001\\" + Consulta.getString(1) + "");

//------------------------------------------------------------
// Content-disposition header - don't open in browser and
// set the "Save As..." filename.
// *There is reportedly a bug in IE4.0 which  ignores this...
//------------------------------------------------------------
                response.setHeader("Content-disposition", "inline; filename=" + "Example.pdf");

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {

                    InputStream isr = new FileInputStream(file);
                    bis = new BufferedInputStream(isr);
                    bos = new BufferedOutputStream(outs);
                    byte[] buff = new byte[2048];
                    int bytesRead;
                    // Simple read/write loop.
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                } catch (Exception e) {
                    System.out.println("Exception ----- Message ---" + e);
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                }
            }
            con.cierraConexion();
        } catch (Exception e) {
        }
    }
}
