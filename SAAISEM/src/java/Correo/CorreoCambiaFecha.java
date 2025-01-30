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
 * Envío de correo cambio de fecha de recepción
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CorreoCambiaFecha extends HttpServlet {

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

    public void enviaCorreo(String folio, String usuario, String email, String FA1, String FA2, String HA1, String HA2) {
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
            message.setFrom(new InternetAddress("medalfawms2@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("sistemasmedalfa@outlook.com"));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress("mdotor@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            message.setSubject("Cambio de Fecha de Entrega / CEDIS CENTRAL");
            System.out.println("Cambio de Fecha de Entrega / CEDIS CENTRAL");
            String mensaje = "Se acaba de recalendarizar la siguiente orden de compra: \n";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select * from TB_FecEnt where F_Id = '" + folio + "'  ");
                while (rset.next()) {
                    mensaje = mensaje + "Recalendarizó: " + usuario + "\n"
                            + "Proveedor: " + rset.getString("F_Provee") + "\n\n\n"
                            + "Previas Fechas de entrega\n"
                            + "Primera Fecha de Entrega: " + FA1 + " " + HA1 + "\n"
                            + "Segunda Fecha de Entrega: " + FA2 + " " + HA2 + "\n\n\n"
                            + "Nuevas Fechas de entrega\n"
                            + "Primera Fecha de Entrega: " + rset.getString("F_F1") + " " + rset.getString("F_H1") + "\n"
                            + "Segunda Fecha de Entrega: " + rset.getString("F_F2") + " " + rset.getString("F_H2") + "\n\n"
                            + "Observaciones: " + rset.getString("F_Obs") + "\n"
                            + "Bodega: " + rset.getString("F_Bodega") + "\n";
                }
                obj.cierraConexion();
            } catch (Exception e) {
            }

            System.out.println(mensaje);
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
