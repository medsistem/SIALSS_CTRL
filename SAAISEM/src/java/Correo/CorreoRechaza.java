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
 * Envío de correo rechazo a proveedor
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CorreoRechaza extends HttpServlet {

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

    public void enviaCorreo(String folio, String Hora, String Fecha, String correoProvee, String claves) {
        try {
            System.out.println("hola");
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
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(correoProvee));
            message.setSubject("Se ha rechazado el folio " + folio + " / CEDIS CENTRAL");
            System.out.println("Se ha rechazado el folio " + folio + " / CEDIS CENTRAL");
            String mensaje = "Se ha rechazado el folio " + folio + "\n";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select F_Observaciones, F_Fecha from tb_rechazos where F_NoCompra = '" + folio + "'  ");
                while (rset.next()) {
                    mensaje = mensaje + "Fecha de rechazo: " + rset.getString(2) + "\n";
                    mensaje = mensaje + "Observaciones de rechazo: " + rset.getString(1) + "\n";
                }
                rset = obj.consulta("select p.F_FecSur, p.F_HorSur, pro.F_NomPro, u.F_Usuario from tb_pedidoisem p, tb_proveedor pro, tb_usuariosisem u where u.F_IdUsu = p.F_IdUsu and p.F_Provee = pro.F_ClaProve and  F_NoCompra = '" + folio + "' group by pro.F_NomPro ");
                while (rset.next()) {
                    mensaje = mensaje + "Proveedor: " + rset.getString(3) + "\n"
                            + "Fecha de entrega anterior: " + Fecha + " " + Hora + "\n"
                            + "Fecha de nueva entrega: " + rset.getString(1) + " " + rset.getString(2) + "\n"
                            + "Orden capurada por: " + rset.getString(4) + "\n";
                }
                obj.cierraConexion();
            } catch (Exception e) {
            }
            mensaje = mensaje + "Clave\t\t\tCantidad\n";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select F_Clave, F_Cant, F_Obser from tb_pedidoisem where F_NoCompra = '" + folio + "' and (F_Clave in (" + claves + ")) ");
                while (rset.next()) {
                    mensaje = mensaje + rset.getString(1) + "\t\t" + rset.getString(2) + "\t\t" + rset.getString(3) + "\n";
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
