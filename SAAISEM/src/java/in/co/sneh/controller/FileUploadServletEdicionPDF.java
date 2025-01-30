/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

//import LeeExcel.LeeExcelRecibo;
import conn.ConectionDB;
import in.co.sneh.model.FileUpload;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
 * @author HOVA
 */
@WebServlet(name = "FileUploadServletEdicionPDF", urlPatterns = {"/FileUploadServletEdicionPDF"})
public class FileUploadServletEdicionPDF extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String id = "";
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);

        ConectionDB con = new ConectionDB();
        Map<String, String> map = new HashMap<String, String>();
        System.out.println(map.get("btnSave2"));
        String F_FileRemi="",F_FileRemiN="",F_FileFac="",F_FileFacN,F_FolRemi="",F_FolFac="",query="";
        String nomArchivo = "";
            ServletFileUpload upload = new ServletFileUpload();
            try {

                FileItemIterator itr = upload.getItemIterator(request);
                while (itr.hasNext()) {
                    FileItemStream item = itr.next();
                    if (item.isFormField()) {
                        String fielName = item.getFieldName();
                        InputStream is = item.openStream();
                        byte[] b = new byte[is.available()];
                        is.read(b);
                        String value = new String(b);
                        map.put(fielName, value);
                    } else {
                        try{
                            String path = getServletContext().getRealPath("/")+"pdf/";
                            System.out.println("NombreArchivo:"+item.getName());
                            nomArchivo = item.getName();
                            String archivo = FileUpload.processFileOC(path, item);
                            if(map.get("folioRemi") != null){
                                if(!map.get("folioRemi").equals("") ){
                                    if(F_FileRemi.equals("")){
                                        F_FileRemi = archivo.trim();
                                        F_FolRemi = map.get("folioRemi");
                                        System.out.println(F_FileRemi);
                                        System.out.println(F_FolRemi);
                                        
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                        System.out.println("Archivo cargado exitosamente");
                    }
                }
            } catch (FileUploadException e) {
                Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            
        //String nuevoArchivo="http://187.176.10.53:3000/bills/"+nomArchivo;
        String nuevoArchivo="http://127.0.0.1:8080/bills/"+nomArchivo;
        try {
            String pivote = "";
            con.conectar();
            ResultSet rset = con.consulta("select F_FolRemi,F_FolFac from tb_compratemp where F_IdCom="+map.get("idCo"));
            while (rset.next()) {
                if(!rset.getString(1).equals("")){
                pivote = "F_FileRemi";
                }
                else{
                pivote = "F_FileFac";
                }

            }
            con.actualizar("update tb_compratemp set "+pivote+"='"+nuevoArchivo+"' where F_IdCom = "+map.get("idCo"));
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(FileUploadServletEdicionPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
       response.sendRedirect("hh/compraAuto3.jsp");            
    }
   
}
