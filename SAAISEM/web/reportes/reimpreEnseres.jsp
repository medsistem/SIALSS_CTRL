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
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        //response.sendRedirect("index.jsp");
    }

    String folio_gnk = request.getParameter("fol_gnkl");
    String proyecto = request.getParameter("proyecto");
    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();
    int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, SumaMedSur = 0, SumaMedSurT = 0, Copy = 0;
    String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", Folio = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "", Clues = "", Nomenclatura = "", Encabezado = "";
    double Hoja = 0.0, Hoja2 = 0.0, MTotalMonto = 0.0, Iva = 0.0;
    ResultSet RsetNomenc = con.consulta("SELECT F_Nomenclatura, F_Encabezado FROM tb_proyectos WHERE F_Id='" + proyecto + "';");
    while (RsetNomenc.next()) {
        Nomenclatura = RsetNomenc.getString(1);
        Encabezado = RsetNomenc.getString(2);
    }

    con.actualizar("DELETE FROM tb_enseresimprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + folio_gnk + "' AND F_User='" + usua + "';");

    ResultSet DatosFactMed = con.consulta("SELECT E.F_ClaEnseres, CONCAT(P.F_Nomenclatura, E.F_Folio) AS F_ClaDoc, E.F_ClaCli, U.F_NomCli, U.F_Direc, U.F_Razon, DATE_FORMAT( DATE(F_FechaCaptura), '%d/%m/%Y' ) AS F_Elabo, DATE_FORMAT( E.F_FechaEntrega, '%d/%m/%Y' ) AS F_FecEnt, CE.F_Insumos AS F_DesPro, CE.F_UM, SUM(E.F_Cantidad) AS F_Cantidad, J.F_DesJurIS AS F_Juris, IFNULL(M.F_DesMunIS, '') AS F_Muni, U.F_Clues, F_Obs, P.F_DesProy FROM tb_enseresfactura E INNER JOIN tb_uniatn U ON E.F_ClaCli = U.F_ClaCli INNER JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS INNER JOIN tb_enseres CE ON E.F_ClaEnseres = CE.F_Id LEFT JOIN tb_muniis M ON U.F_ClaMun = M.F_ClaMunIS AND U.F_ClaJur = M.F_JurMunIS LEFT JOIN tb_proyectos P ON E.F_Proyecto = P.F_Id WHERE E.F_Folio = '" + folio_gnk + "' AND E.F_Sts = 'A' AND E.F_Proyecto = '" + proyecto + "' GROUP BY E.F_ClaCli, F_Folio, E.F_ClaEnseres;");
    while (DatosFactMed.next()) {
        SumaMedSur = DatosFactMed.getInt(11);

        SumaMedSurT = SumaMedSurT + SumaMedSur;

        Folio = DatosFactMed.getString(2);
        Unidad = DatosFactMed.getString(4);
        Direc = DatosFactMed.getString(5);
        Fecha = DatosFactMed.getString(7);
        F_FecApl = DatosFactMed.getString(8);
        Razon = DatosFactMed.getString(6);
        Jurisdiccion = DatosFactMed.getString(12);
        Municipio = DatosFactMed.getString(13);
        Clues = DatosFactMed.getString(14);
        F_Obs = DatosFactMed.getString(15);
        Proyecto = DatosFactMed.getString(16);

        con.actualizar("INSERT INTO tb_enseresimprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + DatosFactMed.getString(12) + "','" + DatosFactMed.getString(13) + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + DatosFactMed.getString(16) + "','" + usua + "','" + folio_gnk + "',0);");
    }
    if (SumaMedSurT > 0) {
        con.actualizar("INSERT INTO tb_enseresimprefolio VALUES('','" + Folio + "','','" + Unidad + "','" + Direc + "','" + Razon + "','" + Fecha + "','" + F_FecApl + "','','Total Enseres','" + SumaMedSurT + "','" + Jurisdiccion + "','" + Municipio + "','" + Clues + "','" + F_Obs + "','" + Proyecto + "','" + usua + "','" + folio_gnk + "',0);");
    }

    SumaMedSur = 0;
    SumaMedSurT = 0;

    ResultSet Contare = con.consulta("SELECT COUNT(F_ClaDoc),F_Obs FROM tb_enseresimprefolio WHERE F_ClaDoc = '" + Folio + "' AND F_User = '" + usua + "';");
    if (Contare.next()) {
        RegistroC = Contare.getInt(1);
    }

    Hoja = RegistroC * 1.0 / 52;
    Hoja2 = RegistroC / 52;
    HojasC = (int) Hoja2 * 52;
    HojasR = RegistroC - HojasC;

    if ((HojasR > 0) && (HojasR <= 34)) {
        Ban = 1;
    } else {
        Ban = 2;
    }

    System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
    Hoja = 0;

    /*Establecemos la ruta del reporte*/
    if (Ban == 1) {
        File reportFile = new File(application.getRealPath("/reportes/EnseresFolio.jasper"));
        Map parameters = new HashMap();
        parameters.put("Folfact", Folio);
        parameters.put("Usuario", usua);
        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();
    } else {

        File reportFile = new File(application.getRealPath("/reportes/EnseresFolio2.jasper"));
        Map parameters = new HashMap();
        parameters.put("Folfact", Folio);
        parameters.put("Usuario", usua);
        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();
    }


%>
