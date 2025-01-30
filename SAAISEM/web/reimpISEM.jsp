<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="java.io.*" %> 
<%@ page import="java.sql.*" %> 
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<% /*Parametros para realizar la conexión*/

    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        //response.sendRedirect("index.jsp");
    }
    String folio_gnk = request.getParameter("idCom");
    String fecRecepcion = request.getParameter("fecRecepcion");
    String F_FolRemi = request.getParameter("F_FolRemi");
    String F_OrdCom = request.getParameter("F_OrdCom");
    String NoContrato = request.getParameter("NoContrato");
    String NoFolio = request.getParameter("NoFolio");
    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
    String Observaciones = (new String(a, "UTF-8")).toUpperCase();
    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();
    /*Establecemos la ruta del reporte*/
    File reportFile = new File(application.getRealPath("/reportes/RecepcionBienes2.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("folcom", folio_gnk);
    parameters.put("F_FolRemi", F_FolRemi);
    parameters.put("F_OrdCom", F_OrdCom);
    String fecRep = (df3.format(df2.parse(fecRecepcion)) + "").toString();
    parameters.put("fecRecep", fecRep);
    parameters.put("NoContrato", NoContrato);
    parameters.put("NoFolio", NoFolio);
    parameters.put("Observaciones", Observaciones);
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length); /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();
%>
