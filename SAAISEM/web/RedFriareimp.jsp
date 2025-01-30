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

    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
    String usua = "";
    int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0;
    double MontoMed = 0.0, MontoTMed = 0.0;
    String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "";
    int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0;
    double MontoMat = 0.0, MontoTMat = 0.0;
    int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0;
    String DesV = "";
    double Hoja = 0.0, Hoja2 = 0.0;

    int TotalReq = 0, TotalSur = 0;
    double TotalMonto = 0.0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    String remis = request.getParameter("fol_gnkl");
    String Tempera = request.getParameter("temp");
    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();

    /*Establecemos la ruta del reporte*/
    
    double contar = 0.0, Div = 0.0;
    int contar2 = 0, Div2 = 0, Dife = 0;
    
    ResultSet Consulta = con.consulta("SELECT F_ClaDoc,COUNT(F_ClaDoc) AS CONTAR FROM tb_redfriaimp WHERE F_ClaDoc='" + remis + "';");
    while (Consulta.next()) {
        contar = Consulta.getDouble(2);
        contar2 = Consulta.getInt(2);
    }
    Div = contar / 12;
    Div2 = (int) Div;
    Dife = contar2 - (Div2 * 12);
    if ((Dife > 0) || (Dife <= 12)) {
        for (int x = Dife; x < 12; x++) {
            con.insertar("INSERT INTO tb_redfriaimp VALUES('','','" + remis + "','','','','',0);");
        }
    }
    Dife = 0;
    
    File reportFile = new File(application.getRealPath("/ReportesPuntos/ReporteRedFria.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("Documento", remis);
    parameters.put("Tempera", Tempera);
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length); /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();

%>
