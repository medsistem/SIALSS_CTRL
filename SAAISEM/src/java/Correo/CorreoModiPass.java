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

/**
 * Envío de correo modificación de password
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CorreoModiPass extends HttpServlet {

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

    public void enviaCorreo(String correo, String usuario, String password) {
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

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            //Correo de donde se enviará el mensaje
            message.setFrom(new InternetAddress("medalfawms2@gmail.com"));
            //Direcciones de correo a enviar
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
            //message.addRecipient(Message.RecipientType.BCC, new InternetAddress("lodimed2018@gmail.com"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("sistemasmedalfa@outlook.com"));

            // Cuerpo del mensaje
            message.setSubject("Se Modificó su Password en el sistema SAA ");
            String mensaje = "Se ha modificado su password para el ingreso al sistema SAA:  " + "\n";

            mensaje = mensaje + "Usuario: " + usuario + "\n"
                    + "Password: " + password + "\n";

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

    public void NuevoUsuario(String correo, String usuario, String password) {
        try {
            /* TODO output your page here. You may use following sample code. */
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "lodimed2018@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            //Correo de donde se enviará el mensaje
            message.setFrom(new InternetAddress("sistemasmedalfa@outlook.com"));
            //Direcciones de correo a enviar
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));//Aqui se pone la direccion a donde se enviara el correo
            //message.addRecipient(Message.RecipientType.BCC, new InternetAddress("lodimed2018@gmail.com"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("med_sistem@outlook.com"));

            // Cuerpo del mensaje
            message.setSubject("Nuevo Usuario creado sistema SAA ");
            String mensaje = "Se ha creado nuevo usuario en el sistema SAA:  " + "\n";

            mensaje = mensaje + "Usuario: " + usuario + "\n"
                    + "Password: " + password + "\n";

            message.setText(mensaje);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("sistemasmedalfa@outlook.com", "Med_2019B");
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
