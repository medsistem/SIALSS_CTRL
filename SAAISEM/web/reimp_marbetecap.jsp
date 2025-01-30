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
<% /*Parametros para realizar la conexión*/


    /**
     * Reimpresion de marbete
     */
    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
    String usua = "";
    int Tarimas = 0, TTarimas = 0, TarimasI = 0, Piezas = 0, TPiezas = 0, Cajas = 0, TCajas = 0, CajasI = 0, Resto = 0, Restop = 0, PiezasT = 0, PiezasC = 0, PiezasTI = 0, TotalP = 0;
    String Clave = "", Cb = "", Lote = "", Cadu = "", Descrip = "", Orden = "", Ban = "";

    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }

    try {
        Ban = request.getParameter("ban");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    Connection conn;
    con.conectar();
    conn = con.getConn();
    /*Establecemos la ruta del reporte*/
    File reportFile;
    if (Ban.equals("1")) {
        reportFile = new File(application.getRealPath("/reportes/Marbetecap.jasper"));
    } else {
        reportFile = new File(application.getRealPath("/reportes/MarbetecapMedia.jasper"));
    }
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("folmar", "hola");
    //parameters.put("F_OrdCom", request.getParameter("F_OrdCom"));
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length);
    /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();
    con.cierraConexion();
%>
