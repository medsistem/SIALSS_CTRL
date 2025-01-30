<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="conn.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% /*Parametros para realizar la conexión*/

    HttpSession sesion = request.getSession();
    ConectionDB_Linux con = new ConectionDB_Linux();
    int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0;
    double MontoMed = 0.0, MontoTMed = 0.0;
    String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", ubicacion = "";
    int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0;
    double MontoMat = 0.0, MontoTMat = 0.0;

    int TotalReq = 0, TotalSur = 0;
    double TotalMonto = 0.0;
    if (sesion.getAttribute("nombre") != null) {
    } else {
        response.sendRedirect("index.jsp");
    }
    try {
        con.conectar();
        Connection conexion;
        con.conectar();
        conexion = con.getConn();
        String remis = request.getParameter("fol_gnkl");
        con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + remis + "'");
        ResultSet ObsFact = con.consulta("SELECT F_Obser FROM tb_obserfact WHERE F_IdFact='" + remis + "' GROUP BY F_IdFact");
        while (ObsFact.next()) {
            F_Obs = ObsFact.getString(1);
        }
 

        ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli,U.F_NomCli,U.F_Direc,F.F_ClaDoc,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,"
                + "DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq,SUM(F.F_CantSur) AS F_CantSur,SUM(F.F_Costo) AS F_Costo,SUM(F.F_Monto) AS F_Monto,"
                + "DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl , F.F_Ubicacion "
                + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                + "INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro "
                + "WHERE F_ClaDoc='" + remis + "' and F_TipMed='2504' and F_CantSur>0 and F_StsFact='A' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad ORDER BY F.F_ClaPro +0");
        while (DatosFactMed.next()) {
            ubicacion = DatosFactMed.getString("F_Ubicacion");
            SumaMedReq = DatosFactMed.getInt("F_CantReq");
            SumaMedSur = DatosFactMed.getInt("F_CantSur");
            MontoMed = DatosFactMed.getDouble("F_Monto");
            SumaMedReqT = SumaMedReqT + SumaMedReq;
            SumaMedSurT = SumaMedSurT + SumaMedSur;

            Unidad = DatosFactMed.getString("F_NomCli");
            Direc = DatosFactMed.getString("F_Direc");
            Fecha = DatosFactMed.getString("F_FecEnt");
            F_FecApl = DatosFactMed.getString("F_FecApl");
            //F_Obs = DatosFactMed.getString("F_Obser");
            MontoTMed = MontoTMed + MontoMed;
            con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "',"
                    + "'" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + DatosFactMed.getString(12) + "','" + DatosFactMed.getString(13) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "',0,'" + ubicacion + "')");
        }
        if (SumaMedSurT > 0) {
            con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','SubTotal Medicamento (2504)','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + MontoTMed + "','','" + F_FecApl + "',0,'')");
        }

        ResultSet DatosFactMat = con.consulta("SELECT F.F_ClaCli,U.F_NomCli,U.F_Direc,F.F_ClaDoc,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,"
                + "DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq,SUM(F.F_CantSur) AS F_CantSur,SUM(F.F_Costo) AS F_Costo,SUM(F.F_Monto) AS F_Monto,"
                + "DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl, F.F_Ubicacion "
                + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                + "INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro "
                + "WHERE F_ClaDoc='" + remis + "' and F_TipMed='2505' and F_CantSur>0 and F_StsFact='A' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad ORDER BY F.F_ClaPro +0");
        while (DatosFactMat.next()) {
            ubicacion = DatosFactMat.getString("F_Ubicacion");
            SumaMatReq = DatosFactMat.getInt("F_CantReq");
            SumaMatSur = DatosFactMat.getInt("F_CantSur");
            MontoMat = DatosFactMat.getDouble("F_Monto");
            SumaMatReqT = SumaMatReqT + SumaMatReq;
            SumaMatSurT = SumaMatSurT + SumaMatSur;
            MontoTMat = MontoTMat + MontoMat;

            Unidad = DatosFactMat.getString("F_NomCli");
            Direc = DatosFactMat.getString("F_Direc");
            Fecha = DatosFactMat.getString("F_FecEnt");
            F_FecApl = DatosFactMat.getString("F_FecApl");
            //F_Obs = DatosFactMat.getString("F_Obser");

            con.insertar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMat.getString(1) + "','" + DatosFactMat.getString(2) + "','" + DatosFactMat.getString(3) + "','" + DatosFactMat.getString(4) + "','" + DatosFactMat.getString(5) + "','" + DatosFactMat.getString(6) + "',"
                    + "'" + DatosFactMat.getString(7) + "','" + DatosFactMat.getString(8) + "','" + DatosFactMat.getString(9) + "','" + DatosFactMat.getString(10) + "','" + DatosFactMat.getString(11) + "','" + DatosFactMat.getString(12) + "','" + DatosFactMat.getString(13) + "','" + F_Obs + "','" + DatosFactMat.getString(14) + "',0,'" + ubicacion + "')");
        }
        if (SumaMatSurT > 0) {
            con.insertar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','SubTotal Mat. Curación (2505)','','','" + SumaMatReqT + "','" + SumaMatSurT + "','','" + MontoTMat + "','','" + F_FecApl + "',0,'')");
        } else {
            /*for(int x=0; x<4; x++){
             con.actualizar("INSERT INTO tb_imprefolio VALUES('','','','"+remis+"','','','','','','','','','','',0)");   
             }*/
        }
        TotalReq = SumaMatReqT + SumaMedReqT;
        TotalSur = SumaMedSurT + SumaMatSurT;
        TotalMonto = MontoTMat + MontoTMed;

        con.insertar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + TotalMonto + "','','" + F_FecApl + "',0,'')");

        SumaMedReq = 0;
        SumaMedSur = 0;
        MontoMed = 0.0;
        SumaMedReqT = 0;
        SumaMedSurT = 0;
        MontoTMed = 0.0;

        SumaMatReq = 0;
        SumaMatSur = 0;
        MontoMat = 0.0;
        SumaMatReqT = 0;
        SumaMatSurT = 0;
        MontoTMat = 0.0;

        /*Establecemos la ruta del reporte*/
        File reportFile = new File(application.getRealPath("/reportes/ImprimeFoliosLERMA.jasper"));
        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
         cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
        Map parameters = new HashMap();
        parameters.put("Folfact", remis);
        parameters.put("F_Obs", F_Obs);
        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length); /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
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