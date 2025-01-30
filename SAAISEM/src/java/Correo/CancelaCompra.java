/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correo;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import conn.*;
import java.sql.ResultSet;

/**
 * Envío de correo cancelación de ordenes de compras
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CancelaCompra {

    private String cuenta_correo;
    private String nombre;
    private String comentario;
    private String qry;
    ConectionDB obj = new ConectionDB();

    public void cancelaCompra(String folio, String usuario) {
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
            message.setSubject("Cancelación de Orden de Compra / CEDIS CENTRAL");
            System.out.println("Cancelación de Orden de Compra / CEDIS CENTRAL");
            String user = "";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select F_Usuario from tb_usuariosisem where F_IdUsu = '" + usuario + "' ");
                while (rset.next()) {
                    user = rset.getString(1);
                }
                obj.cierraConexion();
            } catch (Exception e) {
            }
            String mensaje = "CANCELACIÓN\nSe acaba de cancelar la siguiente orden de compra: " + folio + " por el Usuario: " + user + "\n";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select F_Observaciones from tb_obscancela where F_NoCompra = '" + folio + "' ");
                while (rset.next()) {
                    mensaje = mensaje + "Observaciones: " + rset.getString(1) + "\n";
                }
                obj.cierraConexion();
            } catch (Exception e) {
            }
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select p.F_FecSur, p.F_HorSur, pro.F_NomPro, u.F_Usuario from tb_pedidoisem p, tb_proveedor pro, tb_usuariosisem u where u.F_IdUsu = p.F_IdUsu and p.F_Provee = pro.F_ClaProve and  F_NoCompra = '" + folio + "' group by pro.F_NomPro ");
                while (rset.next()) {
                    mensaje = mensaje + "Proveedor: " + rset.getString(3) + "\n"
                            + "Fecha de Entrega: " + rset.getString(1) + " " + rset.getString(2) + "\n"
                            + "Orden capurada por: " + rset.getString(4) + "\n";
                }
                obj.cierraConexion();
            } catch (Exception e) {
            }
            mensaje = mensaje + "Clave\t\t\tCantidad\n";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("select F_Clave, F_Cant, F_Obser from tb_pedidoisem where F_NoCompra = '" + folio + "' ");
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
//            t.connect("lodimed2018@gmail.com", "MedalfaHH2018");
            t.connect("medalfawms2@gmail.com", "M3d&&Wms2020");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
