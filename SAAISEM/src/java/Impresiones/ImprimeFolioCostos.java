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
@WebServlet(name = "ImprimeFolioCostos", urlPatterns = {"/ImprimeFolioCostos"})
public class ImprimeFolioCostos extends HttpServlet {

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
        int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0, Diferencia = 0;;
        double MontoMed = 0.0, MontoTMed = 0.0, Costo = 0.0;
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0;
        double MontoMat = 0.0, MontoTMat = 0.0;
        int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, Origen = 0, ContarControlado = 0;
        String DesV = "", Letra = "", Contrato = "", OC = "", Nomenclatura = "", Encabezado = "", RedFria = "", ImagenControlado = "", CargoResponsable = "", NombreResponsable = "";
        double Hoja = 0.0, Hoja2 = 0.0;

        int TotalReq = 0, TotalSur = 0, ContarRedF = 0;
        double TotalMonto = 0.0, MTotalMonto = 0.0, Iva = 0.0;
        String remis = request.getParameter("fol_gnkl");
        String ProyectoF = request.getParameter("Proyecto");
        String IdProyecto = request.getParameter("idProyecto");
        String clave = "";
        usua = (String) sesion.getAttribute("nombre");
        ConectionDB con = new ConectionDB();
        Connection conexion;
        try {
            conexion = con.getConn();

            ResultSet RsetNomenc = con.consulta("SELECT F_Nomenclatura, F_Encabezado FROM tb_proyectos WHERE F_Id='" + IdProyecto + "';");
            while (RsetNomenc.next()) {
                Nomenclatura = RsetNomenc.getString(1);
                Encabezado = RsetNomenc.getString(2);
            }
            
            //Eliminar registros de folios anteriores para la misma unidad médica
            ResultSet claCliente= con.consulta("SELECT F_ClaCli FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = "+ProyectoF);
            while(claCliente.next()){
                clave = claCliente.getString(1);
                con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaCli='"+ clave +"';");
                //con.actualizar("DELETE FROM tb_imprefolio WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                break;
            }

//            con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "';");

            ResultSet ObsFact = con.consulta("SELECT CONCAT(F_Obser, ' - ', F_Tipo) AS F_Obser FROM tb_obserfact WHERE F_IdFact='" + remis + "' AND F_Proyecto = '" + IdProyecto + "' GROUP BY F_IdFact;");
            while (ObsFact.next()) {
                F_Obs = ObsFact.getString(1);
            }

            ResultSet RsetControlado = con.consulta("SELECT F.F_ClaDoc, COUNT(*) AS CONTAR, IFNULL(FC.CONTARCONTROLADO, 0) AS CONTARCONTROLADO, COUNT(*) - IFNULL(FC.CONTARCONTROLADO, 0) AS DIF FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARCONTROLADO FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = '" + IdProyecto + "' AND F_Ubicacion = 'CONTROLADO' ) FC ON F.F_ClaDoc = FC.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "'AND F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaDoc;");
            if (RsetControlado.next()) {
                ContarControlado = RsetControlado.getInt(4);
            }
            if (ContarControlado == 0) {
                ImagenControlado = "Controlado.jpg";
                CargoResponsable = "RESPONSABLE SANITARIO CEDIS CENTRAL";
                NombreResponsable = "Q.F.B. ANA MARGARITA REYES NAVA";
            } else {
                ImagenControlado = "NoControlado.jpg";
                CargoResponsable = "RESP. DE ALMACEN CEDIS CENTRAL";
                NombreResponsable = "JORGE HERNÁNDEZ";
            }

            ResultSet DatosRedF = con.consulta("SELECT COUNT(*) FROM tb_redfria r INNER JOIN tb_factura f ON r.F_ClaPro = f.F_ClaPro WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + IdProyecto + "';");
            if (DatosRedF.next()) {
                ContarRedF = DatosRedF.getInt(1);
            }
            if (ContarRedF > 0 ) {
                RedFria = "red_fria.jpg";
            } else {
                RedFria = "Nored_fria.jpg";
            }

            if ((ProyectoF.equals("1")) || (ProyectoF.equals("4")) || (ProyectoF.equals("5")) || (ProyectoF.equals("6")) || (ProyectoF.equals("7"))) {

                    ResultSet ContarDatos = con.consulta("SELECT F.F_ClaDoc, COUNT(*) AS CONTARFACT, IFNULL(C.CONTARC, 0) AS CONTARREG, ( COUNT(*) - IFNULL(C.CONTARC, 0)) AS DIF FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARC FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_ClaPro = 9999 ) AS C ON F.F_ClaDoc = C.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "';");
                    if (ContarDatos.next()) {
                        Diferencia = ContarDatos.getInt(4);
                    }

                    ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo,  ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F.F_Ubicacion !='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                    while (DatosFactMed.next()) {
                        SumaMedReq = DatosFactMed.getInt("F_CantReq");
                        SumaMedSur = DatosFactMed.getInt("F_CantSur");
                        Origen = DatosFactMed.getInt("F_Origen");
                        MontoMed = DatosFactMed.getDouble("F_Monto");
                        Costo = DatosFactMed.getDouble("F_Costo");

                        SumaMedReqT = SumaMedReqT + SumaMedReq;
                        SumaMedSurT = SumaMedSurT + SumaMedSur;
                        Unidad = DatosFactMed.getString("F_NomCli");
                        Direc = DatosFactMed.getString("F_Direc");
                        Fecha = DatosFactMed.getString("F_FecEnt");
                        F_FecApl = DatosFactMed.getString("F_FecApl");
                        Razon = DatosFactMed.getString(15);
                        Proyecto = DatosFactMed.getString(22);
                        //F_Obs = DatosFactMed.getString("F_Obser");
                        MontoTMed = MontoTMed + MontoMed;
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + usua + "','" + DatosFactMed.getString(22) + "','','','','','','" + DatosFactMed.getString(19) + "' ,'" + DatosFactMed.getString(20) + "' ,'" + DatosFactMed.getString(21) + "','" + IdProyecto + "','" + DatosFactMed.getString(23) + "','" + DatosFactMed.getString(24) + "','" + DatosFactMed.getString(25) + "','" + Encabezado + "','" + DatosFactMed.getString(27) + "','" + remis + "',0);");
                    }
                    if (SumaMedSurT > 0) {
                        //con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','SubTotal Medicamento (2504)','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + MontoTMed + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','',0);");
                    }

                    ResultSet DatosFactMat = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2505' AND F_DocAnt != '1'  AND F.F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                    while (DatosFactMat.next()) {
                        SumaMatReq = DatosFactMat.getInt("F_CantReq");
                        SumaMatSur = DatosFactMat.getInt("F_CantSur");
                        Origen = DatosFactMat.getInt("F_Origen");
                        MontoMat = DatosFactMat.getDouble("F_Monto");
                        Costo = DatosFactMat.getDouble("F_Costo");

                        /*if (ProyectoF.equals("6")) {
                        if (DatosFactMat.getInt(26) == 5) {
                            MontoMat = 0;
                            Costo = 0;
                        }
                    }*/
                        //System.out.println("MatCur=" + MontoMat);
                        SumaMatReqT = SumaMatReqT + SumaMatReq;
                        SumaMatSurT = SumaMatSurT + SumaMatSur;
                        MontoTMat = MontoTMat + MontoMat;
                        //System.out.println("MatCur2=" + MontoTMat);

                        Unidad = DatosFactMat.getString("F_NomCli");
                        Direc = DatosFactMat.getString("F_Direc");
                        Fecha = DatosFactMat.getString("F_FecEnt");
                        F_FecApl = DatosFactMat.getString("F_FecApl");
                        Razon = DatosFactMat.getString(15);
                        Proyecto = DatosFactMat.getString(22);
                        //F_Obs = DatosFactMat.getString("F_Obser");

                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMat.getString(1) + "','" + DatosFactMat.getString(2) + "','" + DatosFactMat.getString(3) + "','" + Nomenclatura + "" + DatosFactMat.getString(4) + "','" + DatosFactMat.getString(5) + "','" + DatosFactMat.getString(6) + "','" + DatosFactMat.getString(7) + "','" + DatosFactMat.getString(8) + "','" + DatosFactMat.getString(9) + "','" + DatosFactMat.getString(10) + "','" + DatosFactMat.getString(11) + "','" + Costo + "','" + df.format(MontoMat) + "','" + F_Obs + "','" + DatosFactMat.getString(14) + "','" + DatosFactMat.getString(15) + "','" + usua + "','" + DatosFactMat.getString(22) + "','','','','','','" + DatosFactMat.getString(19) + "','" + DatosFactMat.getString(20) + "','" + DatosFactMat.getString(21) + "','" + IdProyecto + "','" + DatosFactMat.getString(23) + "','" + DatosFactMat.getString(24) + "','" + DatosFactMat.getString(25) + "','" + Encabezado + "','" + DatosFactMat.getString(27) + "','" + remis + "',0);");
                    }
                    ResultSet DatosRedFria = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(F.F_CantSur * M.F_Costo),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F_CantSur > 0 AND F.F_Ubicacion='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                    while (DatosRedFria.next()) {
                        String redFria= DatosRedFria.getString("REDFRI");
//                        if(redFria.toUpperCase().contains("NORED")){
                            SumaMedReq = DatosRedFria.getInt("F_CantReq");
                            SumaMedSur = DatosRedFria.getInt("F_CantSur");
                            Origen = DatosRedFria.getInt("F_Origen");
                            MontoMed = DatosRedFria.getDouble("F_Monto");
                            Costo = DatosRedFria.getDouble("F_Costo");
                            /*if (ProyectoF.equals("6")) {
                            if (DatosFactMed.getInt(26) == 5) {
                                MontoMed = 0;
                                Costo = 0;
                            }
                        }*/

                            SumaMedReqT = SumaMedReqT + SumaMedReq;
                            SumaMedSurT = SumaMedSurT + SumaMedSur;
                            Unidad = DatosRedFria.getString("F_NomCli");
                            Direc = DatosRedFria.getString("F_Direc");
                            Fecha = DatosRedFria.getString("F_FecEnt");
                            F_FecApl = DatosRedFria.getString("F_FecApl");
                            Razon = DatosRedFria.getString(15);
                            Proyecto = DatosRedFria.getString(22);
                            //F_Obs = DatosFactMed.getString("F_Obser");
                            MontoTMed = MontoTMed + MontoMed;
                            con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosRedFria.getString(1) + "','" + DatosRedFria.getString(2) + "','" + DatosRedFria.getString(3) + "','" + Nomenclatura + "" + DatosRedFria.getString(4) + "','" + DatosRedFria.getString(5) + "','" + DatosRedFria.getString(6) + "','" + DatosRedFria.getString(7) + "','" + DatosRedFria.getString(8) + "','" + DatosRedFria.getString(9) + "','" + DatosRedFria.getString(10) + "','" + DatosRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosRedFria.getString(14) + "','" + DatosRedFria.getString(15) + "','" + usua + "','" + DatosRedFria.getString(22) + "','','','','','','" + DatosRedFria.getString(19) + "' ,'" + DatosRedFria.getString(20) + "' ,'" + DatosRedFria.getString(21) + "','" + IdProyecto + "','" + DatosRedFria.getString(23) + "','" + DatosRedFria.getString(24) + "','" + DatosRedFria.getString(25) + "','" + Encabezado + "','" + DatosRedFria.getString(27) + "','" + remis + "',0);");
//                        }
                    }
                    if (SumaMatSurT > 0) {
                        //con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','SubTotal Mat. Curación (2505)','','','" + SumaMatReqT + "','" + SumaMatSurT + "','','" + MontoTMat + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','',0);");
                    }
                    TotalReq = SumaMatReqT + SumaMedReqT;
                    TotalSur = SumaMedSurT + SumaMatSurT;
                    TotalMonto = MontoTMat + MontoTMed;
                    Iva = MontoTMat * 0.16;

                    MTotalMonto = TotalMonto + Iva;
                    Numero_a_Letra NumLetra = new Numero_a_Letra();
                    String numero = String.valueOf(String.format("%.2f", MTotalMonto));
                    Letra = NumLetra.Convertir(numero, band());

                    con.actualizar("UPDATE tb_imprefolio SET F_Piezas='" + TotalSur + "',F_Subtotal='" + df.format(TotalMonto) + "',F_MontoT='" + df.format(MTotalMonto) + "',F_Iva='" + df.format(Iva) + "',F_Letra='" + Letra + "' WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + usua + "' AND F_ProyectoF = '" + IdProyecto + "';");

                    //con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + TotalMonto + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','" + Iva + "','Letra',0);");
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

                    if (Diferencia == 0) {
                        Hoja = RegistroC * 1.0 / 32;
                        Hoja2 = RegistroC / 32;
                        HojasC = (int) Hoja2 * 32;

                        HojasR = RegistroC - HojasC;

                        if ((HojasR > 0) && (HojasR <= 15)) {
                            Ban = 3;
                        } else {
                            Ban = 4;
                        }
                    } else {
                        Hoja = RegistroC * 1.0 / 8;
                        Hoja2 = RegistroC / 8;
                        HojasC = (int) Hoja2 * 8;

                        HojasR = RegistroC - HojasC;

                        if ((HojasR > 0) && (HojasR <= 4)) {
                            Ban = 1;
                        } else {
                            Ban = 2;
                        }
                    }
                    
                    Hoja = 0;

                    /*Establecemos la ruta del reporte*/
                    if (Ban == 1) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosIsemCostos.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    } else if (Ban == 2) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosIsemCostos2.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }else if (Ban == 3) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosIsemReceta.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }else if (Ban == 4) {
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosIsemReceta2.jasper"));
                        /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
                        Map parameters = new HashMap();
                        parameters.put("Folfact", Nomenclatura + remis);
                        parameters.put("Usuario", usua);
                        parameters.put("F_Obs", F_Obs);
                        parameters.put("RedFria", RedFria);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                        /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                        /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
                        ouputStream.close();
                    }

            } else if (ProyectoF.equals("2")) {

                ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo,  ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F.F_Ubicacion !='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosFactMed.next()) {
                    SumaMedReq = DatosFactMed.getInt("F_CantReq");
                    SumaMedSur = DatosFactMed.getInt("F_CantSur");
                    Origen = DatosFactMed.getInt("F_Origen");
                    MontoMed = DatosFactMed.getDouble("F_Monto");
                    Costo = DatosFactMed.getDouble("F_Costo");
                    SumaMedReqT = SumaMedReqT + SumaMedReq;
                    SumaMedSurT = SumaMedSurT + SumaMedSur;

                    Unidad = DatosFactMed.getString("F_NomCli");
                    Direc = DatosFactMed.getString("F_Direc");
                    Fecha = DatosFactMed.getString("F_FecEnt");
                    F_FecApl = DatosFactMed.getString("F_FecApl");
                    Razon = DatosFactMed.getString(15);
                    Proyecto = DatosFactMed.getString(18);
                    Jurisdiccion = DatosFactMed.getString(19);
                    String redFria= DatosFactMed.getString(21);
                    Municipio = DatosFactMed.getString(20);
                    //F_Obs = DatosFactMed.getString("F_Obser");
                    MontoTMed = MontoTMed + MontoMed;
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + usua + "','" + DatosFactMed.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");
                }
                if (SumaMedSurT > 0) {
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Administración','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + df.format(MontoTMed) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");
                }

                ResultSet DatosFactMat = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2505' AND F_DocAnt != '1'  AND F.F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosFactMat.next()) {
                    SumaMatReq = DatosFactMat.getInt("F_CantReq");
                    SumaMatSur = DatosFactMat.getInt("F_CantSur");
                    Origen = DatosFactMat.getInt("F_Origen");
                    if (Origen == 1) {
                        MontoMat = 0;
                        Costo = 0;
                    } else {
                        // todo costo a cero por eso se comenta sólo para michoacán
                        //MontoMat = DatosFactMat.getDouble("F_Monto");
                        //Costo = DatosFactMat.getDouble("F_Costo");

                        MontoMat = 0;
                        Costo = 0;
                    }
                    SumaMatReqT = SumaMatReqT + SumaMatReq;
                    SumaMatSurT = SumaMatSurT + SumaMatSur;
                    MontoTMat = MontoTMat + MontoMat;

                    Unidad = DatosFactMat.getString("F_NomCli");
                    Direc = DatosFactMat.getString("F_Direc");
                    Fecha = DatosFactMat.getString("F_FecEnt");
                    F_FecApl = DatosFactMat.getString("F_FecApl");
                    Razon = DatosFactMat.getString(15);
                    Proyecto = DatosFactMat.getString(18);
                    Jurisdiccion = DatosFactMat.getString(19);
                    Municipio = DatosFactMat.getString(20);
                    //F_Obs = DatosFactMat.getString("F_Obser");

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMat.getString(1) + "','" + DatosFactMat.getString(2) + "','" + DatosFactMat.getString(3) + "','" + Nomenclatura + "" + DatosFactMat.getString(4) + "','" + DatosFactMat.getString(5) + "','" + DatosFactMat.getString(6) + "','" + DatosFactMat.getString(7) + "','" + DatosFactMat.getString(8) + "','" + DatosFactMat.getString(9) + "','" + DatosFactMat.getString(10) + "','" + DatosFactMat.getString(11) + "','" + Costo + "','" + df.format(MontoMat) + "','" + F_Obs + "','" + DatosFactMat.getString(14) + "','" + DatosFactMat.getString(15) + "','" + usua + "','" + DatosFactMat.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMat.getString(19) + "','" + DatosFactMat.getString(20) + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");
                }
                if (SumaMatSurT > 0) {
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Venta','','','" + SumaMatReqT + "','" + SumaMatSurT + "','','" + df.format(MontoTMat) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");
                } else {
                    /*for(int x=0; x<4; x++){
             con.actualizar("INSERT INTO tb_imprefolio VALUES('','','','"+remis+"','','','','','','','','','','',0)");   
                }*/
                }
                ResultSet DatosRedFria = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(F.F_CantSur * M.F_Costo),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F_CantSur > 0 AND F.F_Ubicacion='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosRedFria.next()) {
                    String redFria= DatosRedFria.getString("REDFRI");
//                        if(redFria.toUpperCase().contains("NORED")){
                        SumaMedReq = DatosRedFria.getInt("F_CantReq");
                        SumaMedSur = DatosRedFria.getInt("F_CantSur");
                        Origen = DatosRedFria.getInt("F_Origen");
                        MontoMed = DatosRedFria.getDouble("F_Monto");
                        Costo = DatosRedFria.getDouble("F_Costo");
                        /*if (ProyectoF.equals("6")) {
                        if (DatosFactMed.getInt(26) == 5) {
                            MontoMed = 0;
                            Costo = 0;
                        }
                    }*/

                        SumaMedReqT = SumaMedReqT + SumaMedReq;
                        SumaMedSurT = SumaMedSurT + SumaMedSur;
                        Unidad = DatosRedFria.getString("F_NomCli");
                        Direc = DatosRedFria.getString("F_Direc");
                        Fecha = DatosRedFria.getString("F_FecEnt");
                        F_FecApl = DatosRedFria.getString("F_FecApl");
                        Razon = DatosRedFria.getString(15);
                        Proyecto = DatosRedFria.getString(22);
                        //F_Obs = DatosFactMed.getString("F_Obser");
                        MontoTMed = MontoTMed + MontoMed;
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosRedFria.getString(1) + "','" + DatosRedFria.getString(2) + "','" + DatosRedFria.getString(3) + "','" + Nomenclatura + "" + DatosRedFria.getString(4) + "','" + DatosRedFria.getString(5) + "','" + DatosRedFria.getString(6) + "','" + DatosRedFria.getString(7) + "','" + DatosRedFria.getString(8) + "','" + DatosRedFria.getString(9) + "','" + DatosRedFria.getString(10) + "','" + DatosRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosRedFria.getString(14) + "','" + DatosRedFria.getString(15) + "','" + usua + "','" + DatosRedFria.getString(22) + "','','','','','','" + DatosRedFria.getString(19) + "' ,'" + DatosRedFria.getString(20) + "' ,'" + DatosRedFria.getString(21) + "','" + IdProyecto + "','" + DatosRedFria.getString(23) + "','" + DatosRedFria.getString(24) + "','" + DatosRedFria.getString(25) + "','" + Encabezado + "','" + remis + "',0);");
//                        }
                }
                
                TotalReq = SumaMatReqT + SumaMedReqT;
                TotalSur = SumaMedSurT + SumaMatSurT;
                TotalMonto = MontoTMat + MontoTMed;

                con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + df.format(TotalMonto) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "','" + remis + "',0);");

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

                if ((HojasR > 0) && (HojasR <= 21)) {
                    Ban = 1;
                } else {
                    Ban = 2;
                }
                System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                Hoja = 0;

                /*Establecemos la ruta del reporte*/
                if (Ban == 1) {
                    File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosMicCostos.jasper"));
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
                } else {
                    File reportFile = new File(context.getRealPath("/reportes/ImprimeFolios2MicCostos.jasper"));
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
            } else {
                ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo,  ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F.F_Ubicacion !='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosFactMed.next()) {
                    SumaMedReq = DatosFactMed.getInt("F_CantReq");
                    SumaMedSur = DatosFactMed.getInt("F_CantSur");
                    Origen = DatosFactMed.getInt("F_Origen");
                    MontoMed = DatosFactMed.getDouble("F_Monto");
                    Costo = DatosFactMed.getDouble("F_Costo");
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
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + usua + "','" + DatosFactMed.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','" + DatosFactMed.getString(21) + "','" + Encabezado + "', '"+remis+"',0);");
                }
                if (SumaMedSurT > 0) {
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Medicamento (2504)','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + df.format(MontoTMed) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','','" + Encabezado + "','" + remis + "',0);");
                }

                ResultSet DatosFactMat = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(M.F_Costo * F.F_CantReq),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2505' AND F_DocAnt != '1'  AND F.F_Proyecto = '" + IdProyecto + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosFactMat.next()) {
                    SumaMatReq = DatosFactMat.getInt("F_CantReq");
                    SumaMatSur = DatosFactMat.getInt("F_CantSur");
                    Origen = DatosFactMat.getInt("F_Origen");
                    MontoMat = DatosFactMat.getDouble("F_Monto");
                    Costo = DatosFactMat.getDouble("F_Costo");
                    SumaMatReqT = SumaMatReqT + SumaMatReq;
                    SumaMatSurT = SumaMatSurT + SumaMatSur;
                    MontoTMat = MontoTMat + MontoMat;

                    Unidad = DatosFactMat.getString("F_NomCli");
                    Direc = DatosFactMat.getString("F_Direc");
                    Fecha = DatosFactMat.getString("F_FecEnt");
                    F_FecApl = DatosFactMat.getString("F_FecApl");
                    Razon = DatosFactMat.getString(15);
                    Proyecto = DatosFactMat.getString(18);
                    Jurisdiccion = DatosFactMat.getString(19);
                    Municipio = DatosFactMat.getString(20);
                    //F_Obs = DatosFactMat.getString("F_Obser");

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMat.getString(1) + "','" + DatosFactMat.getString(2) + "','" + DatosFactMat.getString(3) + "','" + Nomenclatura + "" + DatosFactMat.getString(4) + "','" + DatosFactMat.getString(5) + "','" + DatosFactMat.getString(6) + "','" + DatosFactMat.getString(7) + "','" + DatosFactMat.getString(8) + "','" + DatosFactMat.getString(9) + "','" + DatosFactMat.getString(10) + "','" + DatosFactMat.getString(11) + "','" + Costo + "','" + df.format(MontoMat) + "','" + F_Obs + "','" + DatosFactMat.getString(14) + "','" + DatosFactMat.getString(15) + "','" + usua + "','" + DatosFactMat.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactMat.getString(19) + "','" + DatosFactMat.getString(20) + "','" + DatosFactMat.getString(21) + "','" + Encabezado + "', '"+remis+"',0);");
                }
                ResultSet DatosRedFria = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR( IFNULL( REPLACE (CPR.F_DesPro, '\n', ' '), M.F_DesPro ), 1, 333 ) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(M.F_Costo) AS F_Costo, ROUND(SUM(F.F_CantSur * M.F_Costo),2) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy,IFNULL(CPR.F_Presentacion, M.F_PrePro) AS Presentacion, F.F_Contrato, F.F_OC, PS.F_DesProy AS ProyectoFact, IFNULL(J.F_DesJurIS, '') AS F_DesJurIS, IFNULL(MU.F_DesMunIS, '') AS F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, F.F_Cause, U.F_Clues FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON U.F_Proyecto = P.F_Id LEFT JOIN ( SELECT F_ClaPro, F_DesPro, F_Costo, F_Proyecto, F_Presentacion FROM tb_catalogoprecios ) AS CPR ON F.F_ClaPro = CPR.F_ClaPro AND L.F_Proyecto = CPR.F_Proyecto INNER JOIN tb_proyectos PS ON F.F_Proyecto = PS.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc = '" + remis + "' AND F_TipMed = '2504' AND F_DocAnt != '1' AND F.F_Proyecto = '" + IdProyecto + "' AND F_CantSur > 0 AND F.F_Ubicacion='REDFRIA' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY REDFRI ASC, F.F_ClaPro + 0, L.F_ClaLot;");
                while (DatosRedFria.next()) {
                    String redFria= DatosRedFria.getString("REDFRI");
//                        if(redFria.toUpperCase().contains("NORED")){
                        SumaMedReq = DatosRedFria.getInt("F_CantReq");
                        SumaMedSur = DatosRedFria.getInt("F_CantSur");
                        Origen = DatosRedFria.getInt("F_Origen");
                        MontoMed = DatosRedFria.getDouble("F_Monto");
                        Costo = DatosRedFria.getDouble("F_Costo");
                        /*if (ProyectoF.equals("6")) {
                        if (DatosFactMed.getInt(26) == 5) {
                            MontoMed = 0;
                            Costo = 0;
                        }
                    }*/

                        SumaMedReqT = SumaMedReqT + SumaMedReq;
                        SumaMedSurT = SumaMedSurT + SumaMedSur;
                        Unidad = DatosRedFria.getString("F_NomCli");
                        Direc = DatosRedFria.getString("F_Direc");
                        Fecha = DatosRedFria.getString("F_FecEnt");
                        F_FecApl = DatosRedFria.getString("F_FecApl");
                        Razon = DatosRedFria.getString(15);
                        Proyecto = DatosRedFria.getString(22);
                        //F_Obs = DatosFactMed.getString("F_Obser");
                        MontoTMed = MontoTMed + MontoMed;
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosRedFria.getString(1) + "','" + DatosRedFria.getString(2) + "','" + DatosRedFria.getString(3) + "','" + Nomenclatura + "" + DatosRedFria.getString(4) + "','" + DatosRedFria.getString(5) + "','" + DatosRedFria.getString(6) + "','" + DatosRedFria.getString(7) + "','" + DatosRedFria.getString(8) + "','" + DatosRedFria.getString(9) + "','" + DatosRedFria.getString(10) + "','" + DatosRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosRedFria.getString(14) + "','" + DatosRedFria.getString(15) + "','" + usua + "','" + DatosRedFria.getString(22) + "','','','','','','" + DatosRedFria.getString(19) + "' ,'" + DatosRedFria.getString(20) + "' ,'" + DatosRedFria.getString(21) + "','" + IdProyecto + "','" + DatosRedFria.getString(23) + "','" + DatosRedFria.getString(24) + "','" + DatosRedFria.getString(25) + "','" + Encabezado + "', '" + remis + "',0);");
//                        }
                }
                if (SumaMatSurT > 0) {
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','SubTotal Mat. Curación (2505)','','','" + SumaMatReqT + "','" + SumaMatSurT + "','','" + df.format(MontoTMat) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");
                } else {
                    /*for(int x=0; x<4; x++){
             con.actualizar("INSERT INTO tb_imprefolio VALUES('','','','"+remis+"','','','','','','','','','','',0)");   
                }*/
                }
                TotalReq = SumaMatReqT + SumaMedReqT;
                TotalSur = SumaMedSurT + SumaMatSurT;
                TotalMonto = MontoTMat + MontoTMed;

                con.actualizar("INSERT INTO tb_imprefolio VALUES('"+clave+"','" + Unidad + "','" + Direc + "','" + Nomenclatura + "" + remis + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + df.format(TotalMonto) + "','','" + F_FecApl + "','" + Razon + "','" + usua + "','" + Proyecto + "','','','','','','','','','" + IdProyecto + "','" + Jurisdiccion + "','" + Municipio + "','Nored_fria.jpg','" + Encabezado + "', '"+remis+"',0);");

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

                Hoja = RegistroC * 1.0 / 15;
                Hoja2 = RegistroC / 15;
                HojasC = (int) Hoja2 * 15;

                HojasR = RegistroC - HojasC;

                if ((HojasR > 0) && (HojasR <= 10)) {
                    Ban = 1;
                } else {
                    Ban = 2;
                }
                System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                Hoja = 0;

                /*Establecemos la ruta del reporte*/
                if (Ban == 1) {
                    File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosCostos.jasper"));
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
                } else {
                    File reportFile = new File(context.getRealPath("/reportes/ImprimeFolios2Costos.jasper"));
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

}
