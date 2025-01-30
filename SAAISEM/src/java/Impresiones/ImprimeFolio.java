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
import java.sql.SQLException;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "ImprimeFolio", urlPatterns = {"/ImprimeFolio"})
public class ImprimeFolio extends HttpServlet {

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
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "", TipMed= "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0;
        double MontoMat = 0.0, MontoTMat = 0.0;
        int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, Origen = 0, ContarRedF = 0, ContarControlado = 0, Contarape = 0, Contarvac = 0, ContarCont = 0, contarGC = 0;
       // String DesV = "", Letra = "", Contrato = "", OC = "", Nomenclatura = "", Encabezado = "", RedFria = "";
        String DesV = "", Letra = "", Contrato = "", OC = "", Nomenclatura = "", Encabezado = "", RedFria = "", Imgape = "", NoImgApe = "", ImagenControlado = "", CargoResponsable = "", NombreResponsable = "", NoImgGC = "", ImgGC = "";
       String imgOrigen = "";
       double Hoja = 0.0, Hoja2 = 0.0;
        String claCli = "";
        int TotalReq = 0, TotalSur = 0;
        double TotalMonto = 0.0, MTotalMonto = 0.0, Iva = 0.0;
        
        String remis = request.getParameter("fol_gnkl");
        String ProyectoF = request.getParameter("Proyecto");
        String IdProyecto = request.getParameter("idProyecto");
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
///////////busca claves red fria y ape

            ResultSet RsetControlado = con.consulta("SELECT F.F_ClaDoc, COUNT(*) AS CONTAR, IFNULL(FC.CONTARCONTROLADO, 0) AS CONTARCONTROLADO, COUNT(*) - IFNULL(FC.CONTARCONTROLADO, 0) AS DIF FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARCONTROLADO FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = '" + IdProyecto + "' AND F_Ubicacion RLIKE 'CONTROLADO|CTRL' AND F_CantSur > 0) FC ON F.F_ClaDoc = FC.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "'AND F_Proyecto = '" + IdProyecto + "' AND F_CantSur > 0 GROUP BY F.F_ClaDoc;");
            if (RsetControlado.next()) {
                ContarCont = RsetControlado.getInt(3);
                ContarControlado = RsetControlado.getInt(4);
            }
            if (ContarControlado == 0) {
                ImagenControlado = "image/Controlado.jpg";
                ResultSet RsetUsuCargo = con.consulta("SELECT uc.Usuario_Nombre, uc.Cargo, uc.Nomeclatura_Usu FROM tb_usuariocargo AS uc WHERE uc.`Status` = 1 AND uc.IdTipoUsu = 16;");
                if (RsetUsuCargo.next()) {

                    CargoResponsable = RsetUsuCargo.getString(2);
                    NombreResponsable = RsetUsuCargo.getString(3) + ' ' + RsetUsuCargo.getString(1);
                }
                System.out.println("NombreResponsable: " + NombreResponsable);

            } else {
                ImagenControlado = "image/NoControlado.jpg";
                CargoResponsable = " ";
                NombreResponsable = " ";
            }

            ResultSet DatosRedF = con.consulta("SELECT COUNT(*) FROM tb_redfria r INNER JOIN tb_factura f ON r.F_ClaPro = f.F_ClaPro WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + IdProyecto + "' AND F_Ubicacion Rlike 'REDFRIA1|RF';");
            if (DatosRedF.next()) {
                ContarRedF = DatosRedF.getInt(1);
            }
            if (ContarRedF > 0) {
                RedFria = "image/red_fria.jpg";
            } else {
                RedFria = "image/Nored_fria.jpg";
            }

            ResultSet DatosAPE = con.consulta("SELECT COUNT(*) FROM tb_ape ap INNER JOIN tb_factura f ON ap.F_ClaPro = f.F_ClaPro WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + IdProyecto + "' AND F_Ubicacion like '%APE%';");
            if (DatosAPE.next()) {
                Contarape = DatosAPE.getInt(1);
            }
            NoImgApe = "image/Nored_fria.jpg";
            if (Contarape > 0) {
                Imgape = "image/imgape.png";
            } else {
                Imgape = NoImgApe;
            }
            ResultSet DatosGastos = con.consulta("SELECT COUNT(*) FROM tb_factura AS F INNER JOIN tb_lote AS L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica WHERE F.F_StsFact = 'A' AND F.F_ClaDoc = '" + remis + "' AND F.F_CantSur > 0 AND F.F_Proyecto = '" + IdProyecto + "' AND L.F_Origen = '19';");
            if (DatosGastos.next()) {
                contarGC = DatosGastos.getInt(1);
            }
            NoImgGC = "image/Nored_fria.jpg";
            if (contarGC > 0) {
                ImgGC = "image/ImgGC.png";
            } else {
                ImgGC = NoImgGC;
            }

             ResultSet DatosVac = con.consulta("SELECT IFNULL(COUNT(*), 0) AS CONTAR FROM tb_vacunas ap INNER JOIN tb_factura f ON ap.F_ClaPro = f.F_ClaPro WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + IdProyecto + "' AND F_Ubicacion like '%VACUNA%';");
            if (DatosVac.next()) {
                Contarvac = DatosVac.getInt(1);
            }
            if (Contarvac > 0 || ContarCont > 0) {
                System.out.println("si estoy a vacunas: "+Contarvac);
                //F_Obs += "(Folio no valido)";
                System.out.println("Observaciones :"+F_Obs);
            }
            imgOrigen = "image/Nored_fria.jpg"; 
            int BanDato = Integer.parseInt(request.getParameter("BanDato"));
            System.out.println("BanDato: " + BanDato);
         
            
            
               System.out.println("entro  a todos los proyectos");
                switch (BanDato) {
                    case 0:
                    {
                        RedFria = "Nored_fria.jpg";
                    ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur=0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' AND RF.F_ClaPro IS NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY  F.F_ClaPro + 0,L.F_Origen;");
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

                    ResultSet DatosFactRedFria = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur=0 and F_DocAnt !='1'  AND F.F_Proyecto = '" + IdProyecto + "' AND RF.F_ClaPro IS NOT NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY  F.F_ClaPro + 0,L.F_Origen;");
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
 
                        con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactRedFria.getString(1) + "','" + DatosFactRedFria.getString(2) + "','" + DatosFactRedFria.getString(3) + "','" + Nomenclatura + "" + DatosFactRedFria.getString(4) + "','" + DatosFactRedFria.getString(5) + "','" + DatosFactRedFria.getString(6) + "','" + DatosFactRedFria.getString(7) + "','" + DatosFactRedFria.getString(8) + "','" + DatosFactRedFria.getString(9) + "','" + DatosFactRedFria.getString(10) + "','" + DatosFactRedFria.getString(11) + "','" + Costo + "','" + df.format(MontoReF) + "','" + F_Obs + "','" + DatosFactRedFria.getString(14) + "','" + DatosFactRedFria.getString(15) + "','" + usua + "','" + DatosFactRedFria.getString(18) + "','','','','','','','','','" + IdProyecto + "','" + DatosFactRedFria.getString(19) + "','" + DatosFactRedFria.getString(20) + "','" + DatosFactRedFria.getString(21) + "','" + Encabezado + "','" + remis + "',0);");
                    }

                    
                    TotalReq = SumaMatReqT + SumaMedReqT + SumaMedRedFT;
                    TotalSur = SumaMedSurT + SumaMatSurT + SumaReFSurT;
                    TotalMonto = MontoTMat + MontoTMed + MontoTRedF;

                    
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

                    
                    Ban = 1;
                    
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
                    
                    break;
                    case 1:
                    {
                        
                        imgOrigen = "image/Nored_fria.jpg";
                        Ban = 1;
                    }
                        break;
                    case 2:
                        {
                        
                        RedFria = "image/red_fria.jpg";
                        Imgape = "image/Nored_fria.jpg";
                        imgOrigen = "image/Nored_fria.jpg";

                            Ban = 1;
                        break;
                        }
                    case 3:
                        {
                        RedFria = "image/Nored_fria.jpg";
                        Imgape = "image/imgape.png";
                        imgOrigen = "image/Nored_fria.jpg";

                            Ban = 1;
                            break;
                        }
                      case 4:
                    {
                        RedFria = "image/Nored_fria.jpg";
                        Imgape = "image/Nored_fria.jpg";
                        imgOrigen = "image/Nored_fria.jpg";
                         Ban = 1;
                        break;
                    }
                    default:
                        RedFria = "image/Nored_fria.jpg";
                        Imgape = "image/Nored_fria.jpg";
                        imgOrigen = "image/Nored_fria.jpg";
                         Ban = 1;
                        break;
                }


                if (Ban == 1) {
                    File reportFile;
                    
                    Map parameters = new HashMap();
                   
                    parameters.put("Usuario", usua);
                    parameters.put("F_Obs", F_Obs);        
                    parameters.put("TipoInsumo", BanDato);
                    parameters.put("Proyecto", ProyectoF);
                    
                  
                    System.out.println("F_Obs: "+ F_Obs);
                    System.out.println("Origen imagen: "+imgOrigen);
                    System.out.println("Origen: "+Origen);
                    System.out.println("Proyectof: "+ProyectoF);
                    
            switch (ProyectoF) {
                case "8":
                    System.out.println("proyecto merida");
                    parameters.put("Folfact", remis);
                            
                        switch (BanDato) {
                            case 1:
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case 2:
                                parameters.put("RedFria", RedFria);
                                 parameters.put("Imgape", Imgape);
                                break;
                            case 3:
                                 parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            default:
                                System.out.println("proyecto merida no hay controlados");
                                break;
                        }
//                        System.out.println("RedFria: " +RedFria);
//                        System.out.println("Imgape: " +Imgape);
                            reportFile = new File(context.getRealPath("/reportes/ImprimeFolios15.jasper"));
                            break;

                case "9":
                     parameters.put("Folfact", remis);
                            System.out.println("proyecto BITA HEMODIALISIS");
                        switch (BanDato) {
                            case 1:
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case 2:
                                parameters.put("RedFria", RedFria);
                                 parameters.put("Imgape", Imgape);
                                break;
                            case 3:
                                 parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            default:
                                parameters.put("ImagenControlado", ImagenControlado);
                                parameters.put("CargoResponsable", CargoResponsable);
                                parameters.put("NombreResponsable", NombreResponsable);
                                break;
                        }
                            reportFile = new File(context.getRealPath("/reportes/ImprimeFolios30.jasper"));
                            break;

                case "10":
                     parameters.put("Folfact", remis);
                            System.out.println("proyecto bita");
                        switch (BanDato) {
                            case 1:
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case 2:
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case 3:
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            default:
                                parameters.put("ImagenControlado", ImagenControlado);
                                parameters.put("CargoResponsable", CargoResponsable);
                                parameters.put("NombreResponsable", NombreResponsable);
                                break;
                        }
                            reportFile = new File(context.getRealPath("/reportes/ImprimeFolios30.jasper"));
                  break;

                default:
                     parameters.put("Folfact", remis);//Nomenclatura + 
                    System.out.println("proyectos gnk");
                    
                        parameters.put("ImgOrigen", imgOrigen);
                        parameters.put("RedFria", RedFria);
                        parameters.put("Imgape", Imgape);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                        
                        reportFile = new File(context.getRealPath("/reportes/ImprimeFolios.jasper")); 
                       
                    break;
            }
            
                         System.out.println("Folio: "+remis);
                         System.out.println("imgOrigen: " +imgOrigen);
                         System.out.println("RedFria: "+ RedFria);
                         System.out.println("Imgape: " +Imgape);
                         System.out.println("ImagenControlado: " +ImagenControlado);
                         System.out.println("CargoResponsable: " + CargoResponsable);
                         System.out.println("NombreResponsable: " +NombreResponsable);
                         
            
                    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
                    response.setContentType("application/pdf");
                    response.setContentLength(bytes.length);
                    ServletOutputStream ouputStream = response.getOutputStream();
                    ouputStream.write(bytes, 0, bytes.length);
                    ouputStream.flush();
                    ouputStream.close();
                }
//            }
            conexion.close();
        } catch (IOException | NumberFormatException | SQLException | JRException e) {
        }
    }

   public static boolean band() {
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
