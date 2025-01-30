/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correo;

import com.gnk.model.DetalleFactura;
import com.medalfa.saa.model.NotificacionFoliosUrgentes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author HP-MEDALFA
 */
public class CorreoFoliosUrgentes extends CorreoHelper {

    private NotificacionFoliosUrgentes notificacion;

    public CorreoFoliosUrgentes(NotificacionFoliosUrgentes notificacion) {
        this.receipts = new ArrayList<String>();
        this.receipts.add("ivigueras@medalfa.mx");
        this.receipts.add("jmondragon@medalfa.mx");
        this.notificacion = notificacion;
    }

    @Override
    public void sendMail() {
        try {
            MimeMessage message = new MimeMessage(this.getSession());
            message.setFrom(new InternetAddress("medalfawms2@gmail.com"));
            for (String receipt : this.receipts) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipt));
            }
            message.setSubject("Nuevo Folio Urgente de " + this.notificacion.getUnidad());
            String body = "<p>Se ha recibido un nuevo requerimiento <b>URGENTE</b> para la unidad <b>" + this.notificacion.getUnidad() + "</b> con el folio "
                    + "<b>" + this.notificacion.getFolio() + ".</b></p><br>";

            body += "<table><thead><tr><th>Clave</th><th>Descripción</th><th>Cantidad</th><th>Lote</th></thead><tbody>";
            for (DetalleFactura detail : notificacion.getDetails()) {
                body += "<tr><td>" + detail.getClave() + "</td><td>" + detail.getObservaciones() + "</td><td>" + detail.getPiezas() +"</td><td>"+ detail.getOc()==null?"": detail.getOc()+ "</td></tr>";
            }
            body += "</tbody></table>";
            body += "<style>\n"
                    + "table, td, th {\n"
                    + "  border: 1px solid black;\n"
                    + "}\n"
                    + "\n"
                    + "table {\n"
                    + "  width: 100%;\n"
                    + "  border-collapse: collapse;\n"
                    + "}\n"
                    + "</style>";
            message.setContent(body, "text/html; charset=utf-8");
            Transport t = this.getSession().getTransport("smtp");
//            t.connect("medalfawms2@gmail.com", "M3d&&Wms2020");
//            t.sendMessage(message, message.getAllRecipients());

            t.close();

        } catch (AddressException ex) {
            Logger.getLogger(CorreoFoliosUrgentes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(CorreoFoliosUrgentes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
