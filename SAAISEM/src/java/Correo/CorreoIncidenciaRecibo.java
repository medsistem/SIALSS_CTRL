/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correo;

import conn.ConectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author SISTEMAS
 */
public class CorreoIncidenciaRecibo extends HttpServlet{
    
    ConectionDB obj = new ConectionDB();
    
    public void enviarCorreoInc() throws AddressException, MessagingException{
        
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
            
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jmondragon@medalfa.mx"));
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("nidia.leon@medalfa.mx"));
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("tester.desarrollo@medalfa.mx"));
//              message.addRecipient(Message.RecipientType.BCC, new InternetAddress("ivigueras@medalfa.mx"));
              message.addRecipient(Message.RecipientType.BCC, new InternetAddress("mdotor@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("hmorales@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("norma.hernandez@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("luis.mendoza@medalfa.mx"));
////            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("supervisor.recibo@medalfa.mx"));
            message.setSubject("¡¡INCIDENCIA EN RECIBO!! / MICHOACAN");
    
            
            String mensaje = "", documento = "", clave = "" , lote = "", caducidad = "" , folLote = "",  remision = "", ordenCompra = "", usuario = "", fecha = "", hora = "", body = "", recepcion = "";
            try {
                obj.conectar();
                ResultSet rset = obj.consulta("SELECT l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_FolLot, c.F_ClaDoc, c.F_OrdCom, c.F_FolRemi, c.F_UserIngreso, c.F_FecCaptura, c.F_HoraCaptura, CONCAT( l.F_FolLot, l.F_Ubica ), Count(*) FROM tb_lote AS l INNER JOIN tb_compra AS c ON c.F_Lote = l.F_FolLot WHERE l.F_Ubica LIKE '%NUEVA%' AND l.F_ClaLot <> 'x' AND l.F_FolLot NOT IN ('11629', '20948') GROUP BY l.F_FolLot, l.F_Ubica, l.F_ClaPro, l.F_ClaLot HAVING COUNT( * ) > 1;");
                while(rset.next()){
                    clave = rset.getString("F_ClaPro");
                    lote = rset.getString("F_ClaLot");
                    caducidad = rset.getString("F_FecCad");
                    folLote = rset.getString("F_FolLot");
                    documento = rset.getString("F_ClaDoc");
                    remision = rset.getString("F_FolRemi");
                    ordenCompra = rset.getString("F_OrdCom");
                    usuario = rset.getString("F_UserIngreso");
                    fecha = rset.getString("F_FecCaptura");
                    hora = rset.getString("F_HoraCaptura");
                    
                    mensaje += "<tr><td>" +  documento + "</td><td>" +  clave + "</td><td>" + lote + "</td><td>" + caducidad + "</td><td>" + folLote + "</td><td>" + remision + "</td><td>" + ordenCompra + "</td><td>" + usuario + "</td><td>" + fecha + "</td><td>" + hora + "</td></tr>";
                    
                }
                              
                                  
               

                obj.cierraConexion();

            } catch (SQLException e) {
                e.getMessage();
            }
            
               //Cuerpo del correo
            body = "FOLIOS CON INCIDENCIA DE INGRESO" + "\n";
            
            body +=  "<head>\n"
                + "<style>\n"
                + "table {\n"
                + "  border-collapse: collapse;\n"
                + "  width: 100%;\n"
                + "}\n"
                + "\n"
                + "th, td {\n"
                + "  text-align: left;\n"
                + "  padding: 8px;\n"
                + "  border: 1px solid black;\n"
                + "}\n"
                + "\n"
                + "tr:nth-child(even){background-color: #263692}\n"
                + "\n"
                + "th {\n"
                + "  background-color: #263692;\n"
                + "  color: white;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n";
            body += "<table><thead><tr><th>Documento</th><th>Clave</th><th>Lote</th><th>Caducidad</th><th>FolLote</th><th>Remisión</th><th>Orden de Compra</th><th>Usuario</th><th>Fecha de ingreso</th><th>Hora de ingreso</th></thead><tbody>";
            body += mensaje;
            body += "</tbody></table>";
            
            message.setContent(body, "text/html; charset=utf-8");

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("medalfawms2@gmail.com", "ezdwaltbtfwcfzef");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

}
    

