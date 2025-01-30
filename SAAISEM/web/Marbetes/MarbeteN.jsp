<%-- 
    Document   : Marbete
    Created on : 11/05/2016, 05:06:07 PM
    Author     : juan
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.Connection"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% /*Parametros para realizar la conexión*/

    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();

    if (sesion.getAttribute("nombre") != null) {
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        con.conectar();
        Connection conexion;
        con.conectar();
        conexion = con.getConn();
        String remis = request.getParameter("folio");
        int RF = Integer.parseInt(request.getParameter("RF"));
        int Ct = Integer.parseInt(request.getParameter("Ct"));
        int Proyecto = Integer.parseInt(request.getParameter("Proyecto"));

        String Ruta = "";
        /*if ((Proyecto == 6)) {
            Ruta = "/reportes/consultNormal.jasper";
        } else */
         if (RF > 0) {
            Ruta = "/reportes/consultRF.jasper";
         }else if (Ct > 0) {
           Ruta = "/reportes/consultCont.jasper";    
        } else {
            Ruta = "/reportes/consult.jasper";
        }

        String Imagen = "";
         if ((Proyecto == 8)) {
             Imagen = "image/LogoThomas.png";
        
         } else   if ((Proyecto == 9)) {
             Imagen = "image/bitalogo.png";
        
         } else   if ((Proyecto == 10)) {
             Imagen = "image/bitalogo.png";
        
         } else {
            
           Imagen = "image/sialss.png";
          
         }
           
         System.out.println("imagen: "+Imagen);

        File reportFile = new File(application.getRealPath(Ruta));

        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
         cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
        Map parameters = new HashMap();
        parameters.put("folio", remis);
        parameters.put("Imagen", Imagen);

        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
        ouputStream.close();

    } catch (Exception e) {
        Logger.getLogger("reimpFactura").log(Level.SEVERE, null, e);
    } finally {
        try {
            con.cierraConexion();
        } catch (Exception ex) {
            Logger.getLogger("reimpFactura").log(Level.SEVERE, null, ex);
        }
    }
%>
