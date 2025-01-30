
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
public class CorreoConfirmaAT extends HttpServlet {

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

    public void enviaCorreoAT(String claveAT, int CantMov, String Ubicacion, String Usuario,String Proy ,String LoteDesc, String UbicaAn, String Cadu) {
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
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("mdotor@medalfa.mx"));
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jmondragon@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("gerente.operaciones@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("coordinador.operaciones@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("auditor11@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("jefe.almacen@medalfa.mx"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("bodegaxona@medalfa.mx"));
//          message.setSubject("Redistribucion AT / CEDIS CENTRAL MultiProyectos");
            
             String mensaje = "" ,mensaje2 ="",mensaje3 ="",clave="", cantidad="", Tipo="", Desc = "", Fecha = "", Costo = "" , Lote = "", UbicaAnt = "", Ubi = "", Cadu1 = "";
          try {
                obj.conectar();
//                 ResultSet rset2 = obj.consulta("SELECT m.F_ClaPro, m.F_DesPro, m.F_Costo, CASE  WHEN  r.F_ClaPro = m.F_ClaPro  THEN 'RedFria' WHEN  c.F_ClaPro = m.F_ClaPro  THEN 'Controlado' WHEN  a.F_ClaPro = m.F_ClaPro  THEN 'Ape' else 'Normal' END as StatuAT, CURDATE() FROM tb_medica AS m LEFT JOIN tb_redfria AS r ON  m.F_ClaPro = r.F_ClaPro LEFT JOIN tb_controlados AS c ON  m.F_ClaPro = c.F_ClaPro  LEFT JOIN tb_ape AS a ON  m.F_ClaPro = a.F_ClaPro WHERE m.F_ClaPro = '"+ claveAT +"' LIMIT 1;");
                    ResultSet rset2 = obj.consulta("SELECT m.F_ClaPro, m.F_DesPro, m.F_Costo, CASE  WHEN  r.F_ClaPro = m.F_ClaPro  THEN 'RedFria' WHEN  c.F_ClaPro = m.F_ClaPro  THEN 'Controlado' WHEN  a.F_ClaPro = m.F_ClaPro  THEN 'Ape' WHEN o.F_ClaPro = m.F_ClaPro THEN 'Oncología' else 'Normal' END as StatuAT, CURDATE() FROM tb_medica AS m LEFT JOIN tb_redfria AS r ON  m.F_ClaPro = r.F_ClaPro LEFT JOIN tb_controlados AS c ON  m.F_ClaPro = c.F_ClaPro  LEFT JOIN tb_ape AS a ON  m.F_ClaPro = a.F_ClaPro LEFT JOIN tb_onco AS o ON m.F_ClaPro = o.F_ClaPro WHERE m.F_ClaPro = '"+ claveAT +"' LIMIT 1;");
                             
            System.out.println("PARAMETROS: "+"/"+ claveAT +"/"+ CantMov +"/"+Ubicacion+"/"+ Usuario +"/"+Proy +"/"+LoteDesc );
                while (rset2.next()) {
                    System.out.println("Comienza el ciclo de claves");
                    clave = rset2.getString(1);
                    Desc = rset2.getString(2);
                    Costo = rset2.getString(3);
                    cantidad = Integer.toString(CantMov);
                    Tipo = rset2.getString(4);
                    Fecha = rset2.getString(5);
                    Lote = LoteDesc;
                    UbicaAnt = UbicaAn;
                    Ubi = Ubicacion;
                    Cadu1 = Cadu;
                }   
                if(Tipo.equals("RedFria") || Tipo.equals("Controlado")|| Tipo.equals("Ape") || Tipo.equals("Oncología")){
                     message.setSubject("URGENTE "+Tipo+" Redistribucion "+Ubicacion+" / CEDIS CENTRAL  "+Proy+"");
                }else{
                     message.setSubject("Redistribucion "+Ubicacion+" / CEDIS CENTRAL "+Proy+"");
                }
                mensaje2 =""+ clave+"\t\t"+ Lote +"\t\t"+ Cadu1 +"\t\t"+ cantidad +"\t\t"+Tipo+"\t\t"+Costo+"\t\t"+UbicaAnt+"\t\t"+Fecha+"\n" ;
                mensaje3 = "\n"+ Desc +"\n";
                 obj.cierraConexion();
        
            } catch (Exception e) {
                e.getMessage();
            }
         mensaje = "El Usuario: "+Usuario+" redistribuyo en el Proyecto: "+Proy+" la siguiente clave a la ubicacion "+Ubicacion+" \n\nClave\t\tLote\t\tCaducidad\t\tCantidad\t\tTipo\t\tCosto\t\tUbicacion\t\tFecha\n\n"+mensaje2+"\nDescripcion\n"+mensaje3;
//          mensaje = "prueba Usuario: "Sistemas Dev" redistribuyo en el Proyecto: "+Proy+" la siguiente clave a la ubicacion "+Ubicacion+" \n\nClave\t\tLote\t\tCaducidad\t\tCantidad\t\tTipo\t\tCosto\t\tUbicacion\t\tFecha\n\n"+mensaje2+"\nDescripcion\n"+mensaje3;
              
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