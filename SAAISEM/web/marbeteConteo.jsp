<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="org.apache.commons.codec.Charsets"%>
<%@page import="in.co.sneh.model.Marbete"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.stream.Collectors"%>
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
    int Tarimas = 0, TTarimas = 0, TarimasI = 0, Piezas = 0, TPiezas = 0, Cajas = 0, TCajas = 0, CajasI = 0, Resto = 0, Restop = 0, PiezasT = 0, PiezasC = 0, PiezasTI = 0, TotalP = 0, Bandera = 0, Proyecto = 0;
    String Clave = "", Cb = "", Lote = "", Cadu = "", Descrip = "", Orden = "", F_OrdCom = "", FechaA = "", Imagen = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    String body= request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    body= new String(body.getBytes(), Charsets.UTF_8);
    Gson gson = new Gson();
    
    Marbete m = gson.fromJson(body, Marbete.class);
    String folio_gnk = request.getParameter("fol_gnkl");
    F_OrdCom = request.getParameter("F_OrdCom");
    Bandera = Integer.parseInt(request.getParameter("Ban"));

    try {
        con.conectar();

        con.insertar("delete from tb_cajasmarbetes where F_OrdCom='" + F_OrdCom + "'");
        ResultSet rset = con.consulta("SELECT C.F_ClaDoc, C.F_OrdCom, CONCAT(IFNULL(C.F_FolRemi, ''), IFNULL(C.F_FolFac, '')) as F_FolRemi, P.F_NomPro, MC.F_DesMar, PR.F_DesProy, C.F_ClaPro, SUBSTR(M.F_DesPro, 1, 150) AS F_DesPro, L.F_ClaLot, L.F_FecCad, L.F_Cb, C.F_CanCom, C.F_FecApl, (F_Cajas + F_CajasI) AS F_Cajas, F_Pz, F_Resto, CASE WHEN C.F_Proyecto = 3 THEN 'Y' ELSE SUBSTR(PR.F_DesProy, 1, 1) END AS SIGLAS, F_CajasI, F_Cajas AS Cajas, C.F_Proyecto FROM tb_compra C INNER JOIN ( SELECT F_ClaPro, F_ClaLot, F_FecCad, F_FolLot, F_Cb, F_ClaMar FROM tb_lote WHERE F_ClaLot != 'X' GROUP BY F_ClaPro, F_ClaLot, F_FecCad, F_FolLot ) L ON C.F_Lote = L.F_FolLot AND C.F_ClaPro = L.F_ClaPro INNER JOIN tb_medica M ON C.F_ClaPro = M.F_ClaPro INNER JOIN tb_proveedor P ON C.F_ProVee = P.F_ClaProve LEFT JOIN tb_marca MC ON L.F_ClaMar = MC.F_ClaMar INNER JOIN tb_proyectos PR ON C.F_Proyecto = PR.F_Id WHERE C.F_OrdCom='" + request.getParameter("F_OrdCom") + "' and (C.F_FolRemi = '" + request.getParameter("F_FolRemi") + "' or C.F_FolFac = '" + request.getParameter("F_FolRemi") + "') AND C.F_ClaDoc = '" + request.getParameter("fol_gnkl") + "';");
        while (rset.next()) {
            Resto = rset.getInt(16);
            Proyecto = rset.getInt(20);

            if (Resto > 0) {
                Cajas = rset.getInt(14) + 1;
            } else {
                Cajas = rset.getInt(14);
            }

            for (int x = 1; x <= rset.getInt(14); x++) {

                con.insertar("insert into tb_cajasmarbetes values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "', '" + rset.getString(7) + "', '" + rset.getString(8) + "', '" + rset.getString(9) + "','" + rset.getString(10) + "','" + rset.getString(11) + "','" + rset.getString(12) + "','" + rset.getString(13) + "','" + rset.getString(14) + "','" + rset.getString(15) + "','" + rset.getString(16) + "','" + x + " / " + Cajas + "','" + rset.getString(17) + "','0')");

            }

            if (rset.getInt(16) > 0) {

                con.insertar("insert into tb_cajasmarbetes values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "', '" + rset.getString(7) + "', '" + rset.getString(8) + "', '" + rset.getString(9) + "','" + rset.getString(10) + "','" + rset.getString(11) + "','" + rset.getString(12) + "','" + rset.getString(13) + "','" + rset.getString(14) + "','" + rset.getString(16) + "','" + rset.getString(16) + "','" + Cajas + " / " + Cajas + "','" + rset.getString(17) + "','0')");

            }

        }

        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    if ((Proyecto == 3) || (Proyecto == 9) || (Proyecto == 14) || (Proyecto == 15)) {
        Imagen = "LogoThomas.png";
    } else {
        Imagen = "SIALSS_CTRL.png";
    }
    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();
    /*Establecemos la ruta del reporte*/
    File reportFile = new File(application.getRealPath("/reportes/MarbeteCajasR.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("documento", folio_gnk);
    parameters.put("oc", request.getParameter("F_OrdCom"));
    parameters.put("remision", request.getParameter("F_FolRemi"));
    parameters.put("Imagen", Imagen);
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length);
    /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();
%>
