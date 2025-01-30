/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correo;

import conn.ConectionDB;
import java.util.List;
import java.util.Properties;
import javax.mail.Session;

/**
 *
 * @author HP-MEDALFA
 */
public abstract class CorreoHelper {

    private String from = "medalfawms2@gmail.com";
    private String nombre;
    private String comentario;
    private String qry;
    ConectionDB obj = new ConectionDB();
    List<String> receipts;

    private Properties props;
    private Session session;

    public CorreoHelper() {
        props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", "medalfawms2@gmail.com");
        props.setProperty("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(props);
    }

    public Session getSession() {
        return session;
    }
    
    public abstract void sendMail();
}
