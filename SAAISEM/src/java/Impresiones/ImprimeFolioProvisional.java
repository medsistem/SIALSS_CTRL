/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresiones;

import NumeroLetra.Numero_a_Letra;
import conn.ConectionDB;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "ImprimeFolioProvisional", urlPatterns = {"/ImprimeFolioProvisional"})
public class ImprimeFolioProvisional extends HttpServlet {

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
        HttpSession sesion = request.getSession(true);
        DecimalFormat df = new DecimalFormat("#,###.00");
        ServletContext context = request.getServletContext();
        String usua = "";
        int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0, SumaRedFria = 0, SumaMedRedFT = 0, SumaReFSur = 0, SumaReFSurT = 0;
        double MontoMed = 0.0, MontoTMed = 0.0, Costo = 0.0, MontoReF = 0.0, MontoTRedF = 0.0;
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0;
        double MontoMat = 0.0, MontoTMat = 0.0;
        int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, Origen = 0, ContarRedF = 0;
        String DesV = "", Letra = "", Contrato = "", OC = "", Nomenclatura = "", Encabezado = "", RedFria = "";
        double Hoja = 0.0, Hoja2 = 0.0;
        String claCli = "";
        int TotalReq = 0, TotalSur = 0;
        double TotalMonto = 0.0, MTotalMonto = 0.0, Iva = 0.0;
        String remis = request.getParameter("fol_gnkl");
        String ProyectoF = request.getParameter("Proyecto");
        String IdProyecto = request.getParameter("idProyecto");
        String idFolio = request.getParameter("idFolio");
        usua = (String) sesion.getAttribute("nombre");
        ConectionDB con = new ConectionDB();
        Connection conexion;
        try {
            conexion = con.getConn();

            ResultSet RsetNomenc = con.consulta("SELECT F_Nomenclatura, F_Encabezado FROM tb_proyectos WHERE F_Id='" + IdProyecto + "';");
            while (RsetNomenc.next()) {
                Nomenclatura = "PRV-";
                Encabezado = RsetNomenc.getString(2);
            }

            //Eliminar registros de folios anteriores para la misma unidad médica
            ResultSet claCliente = con.consulta("SELECT F_ClaCli, F_ClaDoc FROM tb_factura WHERE F_ClaDoc = '" + remis + "'");
            while (claCliente.next()) {
                String doc = claCliente.getString(2);
//                String clave = claCliente.getString(1);
                con.actualizar("DELETE FROM tb_imprefolio WHERE F_Folio='" + remis + "' ;");
                //con.actualizar("DELETE FROM tb_imprefolio WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                break;
            }

//            con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "';");
            ResultSet ObsFact = con.consulta("SELECT F_Obser FROM tb_obserfact WHERE F_IdFact='" + remis + "' AND F_Proyecto = '" + IdProyecto + "' GROUP BY F_IdFact;");
            while (ObsFact.next()) {
                F_Obs = ObsFact.getString(1);
            }

            ResultSet DatosRedF = con.consulta("SELECT COUNT(*) FROM tb_redfria r INNER JOIN tb_lote l on l.F_ClaPro= r.f_ClaPro INNER JOIN folio_provisional_detail d ON l.F_IdLote = d.id_lote WHERE d.status = 1 AND d.id_folio = '" + idFolio + "' AND cantSur > 0 AND l.F_Ubica = 'REDFRIA';");
            if (DatosRedF.next()) {
                ContarRedF = DatosRedF.getInt(1);
            }
            if (ContarRedF > 0) {
                RedFria = "red_fria.jpg";
            } else {
                RedFria = "Nored_fria.jpg";
            }
            //////////////PROYECTO MICHOACAN//////////////
            ////////////////////////////////////////// 
            if (ProyectoF.equals("2")) {
                int BanDato = Integer.parseInt(request.getParameter("BanDato"));
                if (BanDato == 1) {
//                    RedFria = "Nored_fria.jpg";
//                    String query = "SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, 'Nored_fria.jpg' AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS WHERE F_ClaDoc='" + remis + "'  and F_CantSur>0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY M.F_TipMed asc, F.F_ClaPro + 0;";
//                    if (request.getParameter("controlado").compareTo("true") == 0) {
//                        query = "SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, 'Nored_fria.jpg' AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS WHERE F_ClaDoc='" + remis + "' and F_CantSur>0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY  M.F_TipMed asc, F.F_ClaPro + 0;";
//                    }
                    String query = this.queryConsulta;
                    System.out.println(query);
                    PreparedStatement ps = con.getConn().prepareStatement(query);
                    ps.setString(1, idFolio);
                    ResultSet DatosFactMed = ps.executeQuery();
                    while (DatosFactMed.next()) {
                        SumaMedReq = DatosFactMed.getInt("F_CantReq");
                        SumaMedSur = DatosFactMed.getInt("F_CantSur");
                        SumaMedReqT = SumaMedReqT + SumaMedReq;
                        SumaMedSurT = SumaMedSurT + SumaMedSur;

                        Unidad = DatosFactMed.getString("F_NomCli");
                        Direc = DatosFactMed.getString("F_Direc");
                        Fecha = DatosFactMed.getString("F_FecEnt");
                        F_FecApl = DatosFactMed.getString("F_FecApl");
                        Razon = DatosFactMed.getString(15);
                        Proyecto = DatosFactMed.getString(18);
                        Jurisdiccion = DatosFactMed.getString(19);
                        Municipio = DatosFactMed.getString(20);
                        //F_Obs = DatosFactMed.getString("F_Obser");
                        MontoTMed = MontoTMed + MontoMed;
                        claCli = DatosFactMed.getString(1);
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + usua + "','" + DatosFactMed.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','" + DatosFactMed.getString(21) + "','" + Encabezado + "','" + remis + "',0);");
                    }
                    if (SumaMedSurT > 0) {
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Administración','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + df.format(MontoTMed) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");
                    }

                    SumaMatReqT = SumaMatReqT + SumaMatReq;
                    SumaMatSurT = SumaMatSurT + SumaMatSur;
                    MontoTMat = MontoTMat + MontoMat;

                    TotalReq = SumaMatReqT + SumaMedReqT + SumaMedRedFT;
                    TotalSur = SumaMedSurT + SumaMatSurT + SumaReFSurT;
                    TotalMonto = MontoTMat + MontoTMed + MontoTRedF;

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + df.format(TotalMonto) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");

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

                    ResultSet Contare = con.consulta("SELECT COUNT(F_ClaDoc),F_Obs FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "' AND F_ProyectoF = '" + IdProyecto + "';");
                    if (Contare.next()) {
                        RegistroC = Contare.getInt(1);
                    }

                    Hoja = RegistroC * 1.0 / 34;
                    Hoja2 = RegistroC / 34;
                    HojasC = (int) Hoja2 * 34;

                    HojasR = RegistroC - HojasC;
                    Ban = 1;
                    System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                    Hoja = 0;

                    /*Establecemos la ruta del reporte*/
                    if (Ban == 1) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosMic.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
         cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }
                }
                if (BanDato == 2) {

                    PreparedStatement ps = con.getConn().prepareStatement(this.queryConsultaRedFria);
                    ps.setString(1, idFolio);
                    System.out.println(ps);
                    ResultSet DatosFactRedFria = ps.executeQuery();
                            //con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur>0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' AND RF.F_ClaPro IS NOT NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY F.F_ClaPro + 0;");
                    while (DatosFactRedFria.next()) {
                        SumaRedFria = DatosFactRedFria.getInt("F_CantReq");
                        SumaReFSur = DatosFactRedFria.getInt("F_CantSur");
                        SumaMedRedFT = SumaMedRedFT + SumaRedFria;
                        SumaReFSurT = SumaReFSurT + SumaReFSur;
                        MontoTRedF = MontoTRedF + MontoReF;

                        Unidad = DatosFactRedFria.getString("F_NomCli");
                        Direc = DatosFactRedFria.getString("F_Direc");
                        Fecha = DatosFactRedFria.getString("F_FecEnt");
                        F_FecApl = DatosFactRedFria.getString("F_FecApl");
                        Razon = DatosFactRedFria.getString(15);
                        Proyecto = DatosFactRedFria.getString(18);
                        Jurisdiccion = DatosFactRedFria.getString(19);
                        Municipio = DatosFactRedFria.getString(20);
                        claCli = DatosFactRedFria.getString(1);
                        //F_Obs = DatosFactRedFria.getString("F_Obser");

                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactRedFria.getString(1) + "','" + DatosFactRedFria.getString(2) + "','" + DatosFactRedFria.getString(3) + "','" + Nomenclatura + "" + DatosFactRedFria.getString(4) + "','" + DatosFactRedFria.getString(5) + "','" + DatosFactRedFria.getString(6) + "','" + DatosFactRedFria.getString(7) + "','" + DatosFactRedFria.getString(8) + "','" + DatosFactRedFria.getString(9) + "','" + DatosFactRedFria.getString(10) + "','" + DatosFactRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoReF) + "','" + F_Obs + "','" + DatosFactRedFria.getString(14) + "','" + DatosFactRedFria.getString(15) + "','" + usua + "','" + DatosFactRedFria.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactRedFria.getString(19) + "','" + DatosFactRedFria.getString(20) + "','" + DatosFactRedFria.getString(21) + "','" + Encabezado + "','" + remis + "',0);");
                    }
                    if (SumaReFSurT > 0) {
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Red Fría','','','" + SumaMedRedFT + "','" + SumaReFSurT + "','','" + df.format(MontoTRedF) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");
                    }

                    TotalReq = SumaMatReqT + SumaMedReqT + SumaMedRedFT;
                    TotalSur = SumaMedSurT + SumaMatSurT + SumaReFSurT;
                    TotalMonto = MontoTMat + MontoTMed + MontoTRedF;

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + df.format(TotalMonto) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");

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

                    ResultSet Contare = con.consulta("SELECT COUNT(F_ClaDoc),F_Obs FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "' AND F_ProyectoF = '" + IdProyecto + "';");
                    if (Contare.next()) {
                        RegistroC = Contare.getInt(1);
                    }

                    Hoja = RegistroC * 1.0 / 34;
                    Hoja2 = RegistroC / 34;
                    HojasC = (int) Hoja2 * 34;

                    HojasR = RegistroC - HojasC;

                    Ban = 1;
                    System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                    Hoja = 0;

                    /*Establecemos la ruta del reporte*/
                    if (Ban == 1) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosMic.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
         cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }
                }
                if (BanDato == 0) {
//                    RedFria = "Nored_fria.jpg";
                    ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur=0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' AND RF.F_ClaPro IS NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY F.F_ClaPro + 0;");
                    while (DatosFactMed.next()) {
                        SumaMedReq = DatosFactMed.getInt("F_CantReq");
                        SumaMedSur = DatosFactMed.getInt("F_CantSur");
                        SumaMedReqT = SumaMedReqT + SumaMedReq;
                        SumaMedSurT = SumaMedSurT + SumaMedSur;

                        Unidad = DatosFactMed.getString("F_NomCli");
                        Direc = DatosFactMed.getString("F_Direc");
                        Fecha = DatosFactMed.getString("F_FecEnt");
                        F_FecApl = DatosFactMed.getString("F_FecApl");
                        Razon = DatosFactMed.getString(15);
                        Proyecto = DatosFactMed.getString(18);
                        Jurisdiccion = DatosFactMed.getString(19);
                        Municipio = DatosFactMed.getString(20);
                        claCli = DatosFactMed.getString(1);
                        //F_Obs = DatosFactMed.getString("F_Obser");
                        MontoTMed = MontoTMed + MontoMed;
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + usua + "','" + DatosFactMed.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','" + DatosFactMed.getString(21) + "','" + Encabezado + "','" + remis + "',0);");
                    }
                    if (SumaMedSurT > 0) {
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Administración','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + df.format(MontoTMed) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");
                    }

                    ResultSet DatosFactRedFria = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur=0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' AND RF.F_ClaPro IS NOT NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY F.F_ClaPro + 0;");
                    while (DatosFactRedFria.next()) {
                        SumaRedFria = DatosFactRedFria.getInt("F_CantReq");
                        SumaReFSur = DatosFactRedFria.getInt("F_CantSur");
                        SumaMedRedFT = SumaMedRedFT + SumaRedFria;
                        SumaReFSurT = SumaReFSurT + SumaReFSur;
                        MontoTRedF = MontoTRedF + MontoReF;

                        Unidad = DatosFactRedFria.getString("F_NomCli");
                        Direc = DatosFactRedFria.getString("F_Direc");
                        Fecha = DatosFactRedFria.getString("F_FecEnt");
                        F_FecApl = DatosFactRedFria.getString("F_FecApl");
                        Razon = DatosFactRedFria.getString(15);
                        Proyecto = DatosFactRedFria.getString(18);
                        Jurisdiccion = DatosFactRedFria.getString(19);
                        Municipio = DatosFactRedFria.getString(20);
                        claCli = DatosFactRedFria.getString(1);
                        //F_Obs = DatosFactRedFria.getString("F_Obser");
                        RedFria = "red_fria.jpg";
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactRedFria.getString(1) + "','" + DatosFactRedFria.getString(2) + "','" + DatosFactRedFria.getString(3) + "','" + Nomenclatura + "" + DatosFactRedFria.getString(4) + "','" + DatosFactRedFria.getString(5) + "','" + DatosFactRedFria.getString(6) + "','" + DatosFactRedFria.getString(7) + "','" + DatosFactRedFria.getString(8) + "','" + DatosFactRedFria.getString(9) + "','" + DatosFactRedFria.getString(10) + "','" + DatosFactRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoReF) + "','" + F_Obs + "','" + DatosFactRedFria.getString(14) + "','" + DatosFactRedFria.getString(15) + "','" + usua + "','" + DatosFactRedFria.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactRedFria.getString(19) + "','" + DatosFactRedFria.getString(20) + "','" + DatosFactRedFria.getString(21) + "','" + Encabezado + "','" + remis + "',0);");
                    }
                    if (SumaReFSurT > 0) {
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Red Fría','','','" + SumaMedRedFT + "','" + SumaReFSurT + "','','" + df.format(MontoTRedF) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");
                    }

                    TotalReq = SumaMatReqT + SumaMedReqT + SumaMedRedFT;
                    TotalSur = SumaMedSurT + SumaMatSurT + SumaReFSurT;
                    TotalMonto = MontoTMat + MontoTMed + MontoTRedF;

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + claCli + "','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + df.format(TotalMonto) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");

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

                    ResultSet Contare = con.consulta("SELECT COUNT(F_ClaDoc),F_Obs FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "' AND F_ProyectoF = '" + IdProyecto + "';");
                    if (Contare.next()) {
                        RegistroC = Contare.getInt(1);
                    }

                    Hoja = RegistroC * 1.0 / 34;
                    Hoja2 = RegistroC / 34;
                    HojasC = (int) Hoja2 * 34;

                    HojasR = RegistroC - HojasC;

                    Ban = 1;
                    System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                    Hoja = 0;

                    /*Establecemos la ruta del reporte*/
                    if (Ban == 1) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosMicCeros.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
         cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }
                }

            } 
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean band() {
        if (Math.random() > .5) {
            return true;
        } else {
            return false;
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

    String queryConsulta = "SELECT "
            + "    F.claCli AS F_ClaCli, "
            + "    U.F_NomCli, "
            + "    U.F_Direc, "
            + "    F.folio AS F_ClaDoc, "
            + "    DATE_FORMAT(F.fecEnt, '%d/%m/%Y') AS F_FecEnt, "
            + "    L.F_ClaPro, "
            + "    SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, "
            + "    L.F_ClaLot, "
            + "    DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, "
            + "    SUM(D.cantReq) AS F_CantReq, "
            + "    SUM(D.cantSur) AS F_CantSur, "
            + "    SUM(D.costo) AS F_Costo, "
            + "    SUM(D.monto) AS F_Monto, "
            + "    DATE_FORMAT(F.fecApl, '%d/%m/%Y') AS F_FecApl, "
            + "    U.F_Razon, "
            + "    L.F_Origen, "
            + "    L.F_Proyecto, "
            + "    P.F_DesProy, "
            + "    J.F_DesJurIS, "
            + "    MU.F_DesMunIS, "
            + "    'Nored_fria.jpg' AS REDFRI "
            + "FROM"
            + "    folio_provisional F "
            + "        INNER JOIN"
            + "    tb_uniatn U ON F.claCli = U.F_ClaCli "
            + "        INNER JOIN "
            + "    folio_provisional_detail D ON D.id_folio = F.id_folio "
            + "        INNER JOIN "
            + "    tb_lote L ON D.id_lote = L.F_IdLote "
            + "        INNER JOIN "
            + "    tb_proyectos P ON F.proyecto = P.F_Id "
            + "        INNER JOIN "
            + "    tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS "
            + "        INNER JOIN "
            + "    tb_muniis MU ON MU.F_ClaMunIS = U.F_ClaMun "
            + "        INNER JOIN "
            + "    tb_medica M ON M.F_ClaPro = L.F_ClaPro "
            + "WHERE "
            + "    F.id_folio = ? "
            + "GROUP BY L.F_ClaPro , L.F_ClaLot , L.F_FecCad , L.F_Origen "
            + "ORDER BY M.F_TipMed ASC , L.F_ClaPro + 0;";
    
    String queryConsultaRedFria = "SELECT "
            + "    F.claCli AS F_ClaCli, "
            + "    U.F_NomCli, "
            + "    U.F_Direc, "
            + "    F.folio AS F_ClaDoc, "
            + "    DATE_FORMAT(F.fecEnt, '%d/%m/%Y') AS F_FecEnt, "
            + "    L.F_ClaPro, "
            + "    SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, "
            + "    L.F_ClaLot, "
            + "    DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, "
            + "    SUM(D.cantReq) AS F_CantReq, "
            + "    SUM(D.cantSur) AS F_CantSur, "
            + "    SUM(D.costo) AS F_Costo, "
            + "    SUM(D.monto) AS F_Monto, "
            + "    DATE_FORMAT(F.fecApl, '%d/%m/%Y') AS F_FecApl, "
            + "    U.F_Razon, "
            + "    L.F_Origen, "
            + "    L.F_Proyecto, "
            + "    P.F_DesProy, "
            + "    J.F_DesJurIS, "
            + "    MU.F_DesMunIS, "
            + "    'Nored_fria.jpg' AS REDFRI "
            + "FROM"
            + "    folio_provisional F "
            + "        INNER JOIN"
            + "    tb_uniatn U ON F.claCli = U.F_ClaCli "
            + "        INNER JOIN "
            + "    folio_provisional_detail D ON D.id_folio = F.id_folio "
            + "        INNER JOIN "
            + "    tb_lote L ON D.id_lote = L.F_IdLote "
            + "        INNER JOIN "
            + "    tb_proyectos P ON F.proyecto = P.F_Id "
            + "        INNER JOIN "
            + "    tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS "
            + "        INNER JOIN "
            + "    tb_muniis MU ON MU.F_ClaMunIS = U.F_ClaMun "
            + "        INNER JOIN "
            + "    tb_medica M ON M.F_ClaPro = L.F_ClaPro "
            + "        INNER JOIN "
            + "    tb_redfria RF ON RF.F_ClaPro = L.F_ClaPro "
            + "WHERE "
            + "    F.id_folio = ? "
            + "GROUP BY L.F_ClaPro , L.F_ClaLot , L.F_FecCad , L.F_Origen "
            + "ORDER BY M.F_TipMed ASC , L.F_ClaPro + 0;";
}
