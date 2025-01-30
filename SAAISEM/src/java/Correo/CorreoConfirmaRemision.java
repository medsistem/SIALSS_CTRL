/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;

/**
 * Envío de correo confirmación de registro de ordenes de compras
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CorreoConfirmaRemision extends HttpServlet {

    private String cuenta_correo;
    private String nombre;
    private String comentario;
    private String qry;
    ConectionDB obj = new ConectionDB();

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

    }

    public void enviaCorreo(String remision, String oc) {
        try {
            /* TODO output your page here. You may use following sample code. */
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "medalfawms2@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

//            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("medalfawms2@gmail.com"));
//            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("sistemasmedalfa@outlook.com"));
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("amreyes@medalfa.mx"));
//          message.addRecipient(Message.RecipientType.BCC, new InternetAddress("mesadeayuda3@medalfa.mx"));
//          message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jmondragon@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jefe.almacen@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jefe.Recibo@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("dhernandez@medalfa.mx"));
            message.setSubject("Remision Confirmada / CEDIS CENTRAL MultiProyectos");
            
             String mensaje = "" ,mensaje2 ="", mensaje3="",mensaje4="",mensaje5="", clave="", cantidad="", clave2="", cantidad2="",clave3="", cantidad3="", Tipo="",Tipo1="", Tipo2="";
          try {
                obj.conectar();
                 ResultSet rset2 = obj.consulta("SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_controlados as ct on c.F_Clapro = ct.F_Clapro WHERE c.F_OrdCom = '" + oc + "' AND c.F_FolRemi = '"+ remision +"'  order by c.F_ClaPro;");
                ResultSet rset3 = obj.consulta("SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_redfria AS r ON c.F_ClaPro = r.F_ClaPro WHERE c.F_OrdCom = '" + oc + "' AND c.F_FolRemi = '"+ remision +"'  order by c.F_ClaPro;");
                ResultSet rset4 = obj.consulta("SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_ape AS a ON c.F_ClaPro = a.F_ClaPro WHERE c.F_OrdCom = '" + oc + "' AND c.F_FolRemi = '"+ remision +"'  order by c.F_ClaPro;");
               
                while (rset2.next()) {
                    System.out.println("Comienza el ciclo Controlado");
                    clave = rset2.getString("c.F_ClaPro");
                    cantidad = rset2.getString("c.F_CanCom");
                    Tipo = "Controlado";
                    System.out.println(clave+cantidad);
                }
                mensaje3 = clave+"\t\t"+ cantidad +"\t\t"+ Tipo +"\n" ;
                 while (rset3.next()) {
                    System.out.println("Comienza el ciclo Red FRia");
                    clave2 = rset3.getString("c.F_ClaPro");
                    cantidad2 = rset3.getString("c.F_CanCom");
                    Tipo1 = "Red FRia";
                    System.out.println(clave+cantidad);
                }
                mensaje4 = clave2+"\t\t"+ cantidad2 +"\t\t"+ Tipo1 +"\n" ;
                 while (rset4.next()) {
                    System.out.println("Comienza el ciclo Ape");
                    clave3 = rset4.getString("c.F_ClaPro");
                    cantidad3 = rset4.getString("c.F_CanCom");
                    Tipo2 = "APE";
                    System.out.println(clave+cantidad);
                } 
                  mensaje5 = clave3+"\t\t"+ cantidad3 +"\t\t"+ Tipo2 +"\n" ;
                  
                  
                 obj.cierraConexion();
                 
//                System.out.println("SELECT c.F_OrdCom, c.F_FolRemi, p.F_NomPro, c.F_User, c.F_FecApl FROM tb_compra AS c INNER JOIN tb_proveedor AS p ON c.F_ProVee = p.F_ClaProve WHERE c.F_OrdCom ='"+oc+"' AND c.F_FolRemi ='"+remision+"'GROUP BY c.F_FolRemi;");
//                System.out.println("SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_controlados as ct on c.F_Clapro = ct.F_Clapro WHERE c.F_OrdCom = '" + oc + "' AND c.F_FolRemi = '"+ remision +"'  order by c.F_ClaPro;");
//                
//                System.out.println(mensaje2);
//                 System.out.println(mensaje3);
            } catch (Exception e) {
                e.getMessage();
            }
          mensaje = "Se Ingreso en ISEM la siguiente orden de compra: " + oc + " con la remision: " + remision + "\n"+mensaje2+"\nClave\t\tCantidad\t\tTipo\n"+mensaje3+"\n"+mensaje4+"\n"+mensaje5+"";
//            mensaje = "Pruebas Ingreso en ISEM la siguiente orden de compra: " + oc + " con la remision: " + remision + "\n"+mensaje2+"\nClave\t\tCantidad\t\tTipo\n"+mensaje3+"\n"+mensaje4+"\n"+mensaje5+"";
           
            message.setText(mensaje);
            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("medalfawms2@gmail.com", "M3d&&Wms2020");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
