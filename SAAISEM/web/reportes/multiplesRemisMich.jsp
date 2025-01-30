<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="Impresiones.ImprimeFolio"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="NumeroLetra.Numero_a_Letra"%>
<%@page import="net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter"%>
<%@page import="net.sf.jasperreports.engine.export.JRPrintServiceExporter"%>
<%@page import="javax.print.attribute.standard.Copies"%>
<%@page import="javax.print.attribute.standard.MediaSizeName"%>
<%@page import="javax.print.attribute.standard.MediaSize"%>
<%@page import="javax.print.attribute.standard.MediaPrintableArea"%>
<%@page import="javax.print.attribute.PrintRequestAttributeSet"%>
<%@page import="javax.print.attribute.HashPrintRequestAttributeSet"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
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
        response.sendRedirect("index.jsp");
    }
    ImprimeFolio imp = new ImprimeFolio();
    int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0;
    String DesV = "", remis = "", ProyectoF = "", ProyectoFactura = "", Nomenclatura = "", TipMed = "", Encabezado = "";
    double Hoja = 0.0, Hoja2 = 0.0, MTotalMonto = 0.0, Iva = 0.0;
    String User = request.getParameter("User");
    String Impresora = request.getParameter("Impresora");
    String TipoInsumo = request.getParameter("Tipo");
    DecimalFormat df = new DecimalFormat("#,###.00");
    int Copy = 0;
    String imgOrigen = "";

    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();
    int count = 0, Epson = 0, Impre = 0;
    String Nom = "";
    PrintService[] impresoras = PrintServiceLookup.lookupPrintServices(null, null);
    PrintService imprePredet = PrintServiceLookup.lookupDefaultPrintService();
    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
    MediaSizeName mediaSizeName = MediaSize.findMedia(4, 4, MediaPrintableArea.INCH);
    printRequestAttributeSet.add(mediaSizeName);
    printRequestAttributeSet.add(new Copies(1));

    for (PrintService printService : impresoras) {
        Nom = printService.getName();
        System.out.println("impresora" + Nom);
        if (Nom.contains(Impresora)) {
            Epson = count;
        } else {
            Impre = count;
        }
        count++;
    }
    ResultSet FoliosC = con.consulta("SELECT F_Folio, F_Copy, U.F_Proyecto, F.F_Proyecto AS F_ProyectoF FROM tb_folioimp F INNER JOIN tb_factura FT ON F.F_Folio = FT.F_ClaDoc AND F.F_Proyecto = FT.F_Proyecto INNER JOIN tb_uniatn U ON FT.F_ClaCli = U.F_ClaCli WHERE F.F_User = '" + User + "' GROUP BY F.F_Folio, F.F_Proyecto ORDER BY F_Folio + 0;");
//    ResultSet FoliosC = con.consulta("SELECT F_Folio, F_Copy, U.F_Proyecto, F.F_Proyecto AS F_ProyectoF FROM tb_folioimp F INNER JOIN tb_factura FT ON F.F_Folio = FT.F_ClaDoc AND F.F_Proyecto = FT.F_Proyecto INNER JOIN tb_uniatn U ON FT.F_ClaCli = U.F_ClaCli WHERE F.F_User = '" + User + "' GROUP BY F.F_Folio, F.F_Proyecto ORDER BY F_Folio + 0;");
    while (FoliosC.next()) {
        remis = FoliosC.getString(1);
        Copy = FoliosC.getInt(2);
        ProyectoF = FoliosC.getString(3);
        ProyectoFactura = FoliosC.getString(4);
System.out.println("ProyectoF SURTIDO: "+ProyectoF);
System.out.println("ProyectoFactura SURTIDO: "+ProyectoFactura);

        ResultSet RsetNomenc = con.consulta("SELECT F_Nomenclatura FROM tb_proyectos WHERE F_Id='" + ProyectoFactura + "';");
        while (RsetNomenc.next()) {
            Nomenclatura = RsetNomenc.getString(1);
        }

        con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + User + "';");
        int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0, Origen = 0, SumaRedFria = 0, SumaMedRedFT = 0, SumaReFSur = 0, SumaReFSurT = 0;
        int SumaMedReqA = 0, SumaMedSurA = 0, SumaMedReqTA = 0, SumaMedSurTA = 0, OrigenA = 0;
        int SumaMedReqR = 0, SumaMedSurR = 0, SumaMedReqTR = 0, SumaMedSurTR = 0, OrigenR = 0;
        double MontoMed = 0.0, MontoTMed = 0.0, Costo = 0.0;
        double MontoReF = 0.0, MontoTRedF = 0.0;
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", Razon = "", Proyecto = "", Letra = "", Jurisdiccion = "", Municipio = "", RedFria = "", Imgape = "", NoImgApe = "", ImagenControlado = "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0, ContarControlado = 0, Contarape = 0, ContarRedF = 0, StsRF = 0, Ban2 = 0;
        double MontoMat = 0.0, MontoTMat = 0.0;
        
        int TotalReq = 0, TotalSur = 0;
        double TotalMonto = 0.0;

        ResultSet ObsFact = con.consulta("SELECT F_Obser FROM tb_obserfact WHERE F_IdFact='" + remis + "' AND F_Proyecto = '" + ProyectoFactura + "' GROUP BY F_IdFact;");
        while (ObsFact.next()) {
            F_Obs = ObsFact.getString(1);
        }

        ResultSet RsetControlado = con.consulta("SELECT F.F_ClaDoc, COUNT(*) AS CONTAR, IFNULL(FC.CONTARCONTROLADO, 0) AS CONTARCONTROLADO, COUNT(*) - IFNULL(FC.CONTARCONTROLADO, 0) AS DIF FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARCONTROLADO FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = '" + ProyectoFactura + "' AND F_Ubicacion  RLIKE 'CONTROLADO|CTRL' ) FC ON F.F_ClaDoc = FC.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "'AND F_Proyecto = '" + ProyectoFactura + "' GROUP BY F.F_ClaDoc;");
        if (RsetControlado.next()) {
            ContarControlado = RsetControlado.getInt(4);
        }
        if (ContarControlado == 0) {
            ImagenControlado = "image/Controlado.jpg";

        } else {
            ImagenControlado = "image/Nored_fria.jpg";
        }

        ResultSet DatosRedF = con.consulta("SELECT COUNT(*) FROM tb_redfria r INNER JOIN tb_factura f ON r.F_ClaPro = f.F_ClaPro AND  f.F_Ubicacion RLIKE 'REDFRIA|RF' WHERE F_StsFact = 'A' AND f.F_ClaDoc = '" + remis + "' AND f.F_CantSur > 0 AND f.F_Proyecto = '" + ProyectoFactura + "';");
        if (DatosRedF.next()) {
            ContarRedF = DatosRedF.getInt(1);
            System.out.println(ContarRedF);
        }
        if (ContarRedF > 0) {

            RedFria = "image/red_fria.jpg";
            StsRF = 1;
        } else {
            RedFria = "image/Nored_fria.jpg";
            StsRF = 2;
        }

        ResultSet DatosAPE = con.consulta("SELECT COUNT(*) FROM tb_ape ap INNER JOIN tb_factura f ON ap.F_ClaPro = f.F_ClaPro AND  f.F_Ubicacion LIKE '%APE%' WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + ProyectoFactura + "';");
        if (DatosAPE.next()) {
            Contarape = DatosAPE.getInt(1);
        }
        NoImgApe = "image/Nored_fria.jpg";
        if (Contarape > 0) {
            Imgape = "image/imgape.png";
            Ban2 = 3;
        } else {
            Imgape = NoImgApe;
        }
        imgOrigen = "image/Nored_fria.jpg"; 
        System.out.println("bandera de tipo: " + TipoInsumo);
        
        
        
    
        /////////////////////////////////////
        /////////proyectos//////////////
        ///////////////////////////////////
        
          if (TipoInsumo.equals("0") || TipoInsumo.equals("1")) {
                System.out.println("tipo de insumo seco");
                ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, M.F_DesPro AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon,  L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'image/red_fria.jpg' ELSE 'image/Nored_fria.jpg' END AS REDFRI,L.F_Origen "
                        + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro and F.F_Ubicacion RLIKE 'REDFRIA|RF' WHERE F_ClaDoc='" + remis + "' and F_CantSur > 0 and F_DocAnt !='1' AND F.F_Proyecto = '" + ProyectoFactura + "'  AND RF.F_ClaPro IS  NULL AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad ORDER BY M.F_TipMed asc, REDFRI ASC, F.F_ClaPro + 0;");

              

                while (DatosFactMed.next()) {
                    SumaMedReq = DatosFactMed.getInt("F_CantReq");
                    SumaMedSur = DatosFactMed.getInt("F_CantSur");
                    Origen = DatosFactMed.getInt("F_Origen");
                    if (Origen == 1) {
                        MontoMed = 0;
                        Costo = 0;
                    } else {
                        MontoMed = 0;
                        Costo = 0;
                    }
                     if (ProyectoFactura.equals("7")) {
                       
                        if (Origen == 27  || Origen == 26) {
                            imgOrigen = "image/almacen.png";
                        }else{
                            imgOrigen = "image/Nored_fria.jpg"; 
                        }
                    }
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
                    MontoTMed = MontoTMed + MontoMed;

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + MontoMed + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + User + "','" + DatosFactMed.getString(17) + "','','','','','','','','','" + ProyectoFactura + "','" + DatosFactMed.getString(18) + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','','" + remis + "',0)");
                }

                Ban = 1;

                if (Ban == 1) {

                    for (int x = 0; x < Copy; x++) {
                        JRPrintServiceExporter exporter = this.imprimeReporte("/reportes/ImprimeFoliosMicSurt.jasper", Nomenclatura, remis, User, F_Obs, RedFria, Imgape, ImagenControlado, imgOrigen, application, con.getConn(), impresoras[Epson]);
                        try {
                            exporter.exportReport();
                        } catch (Exception ex) {

                           ex.printStackTrace();

                        }
                    }
                }

            }

            /////////////////
            /////CONTROLADO//
            //////////////
            if (TipoInsumo.equals("0") || TipoInsumo.equals("4")) {
                System.out.println("Tipo de insumo controlado");

                con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + User + "';");

                 ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, M.F_DesPro AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon,  L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'image/red_fria.jpg' ELSE 'image/Nored_fria.jpg' END AS REDFRI,L.F_Origen FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro and F.F_Ubicacion RLIKE 'REDFRIA|RF'  WHERE F_ClaDoc='" + remis + "' and F_CantSur>0 and F.F_Ubicacion RLIKE 'CONTROLADO|CTRL' and F_DocAnt !='1' AND F.F_Proyecto = '" + ProyectoFactura + "'  AND RF.F_ClaPro IS  NULL AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE 'APE%' AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad ORDER BY M.F_TipMed asc, REDFRI ASC, F.F_ClaPro + 0;");

                RedFria = "image/Nored_fria.jpg";
                Imgape = "image/Nored_fria.jpg";

                while (DatosFactMed.next()) {
                    SumaMedReq = DatosFactMed.getInt("F_CantReq");
                    SumaMedSur = DatosFactMed.getInt("F_CantSur");
                  Origen = DatosFactMed.getInt("F_Origen");
                    if (Origen == 1) {
                        MontoMed = 0;
                        Costo = 0;
                    } else {
                        MontoMed = 0;
                        Costo = 0;
                    }
                    SumaMedReqT = SumaMedReqT + SumaMedReq;
                    SumaMedSurT = SumaMedSurT + SumaMedSur;
                    if (ProyectoFactura.equals("7")) {
                        Origen = DatosFactMed.getInt("F_Origen");
                        if (Origen == 27  || Origen == 26) {
                            imgOrigen = "image/almacen.png";
                        }else{
                            imgOrigen = "image/Nored_fria.jpg"; 
                        }
                    }
                    Unidad = DatosFactMed.getString("F_NomCli");
                    Direc = DatosFactMed.getString("F_Direc");
                    Fecha = DatosFactMed.getString("F_FecEnt");
                    F_FecApl = DatosFactMed.getString("F_FecApl");
                    Razon = DatosFactMed.getString(15);
                    Proyecto = DatosFactMed.getString(18);
                    Jurisdiccion = DatosFactMed.getString(19);
                    Municipio = DatosFactMed.getString(20);
                    MontoTMed = MontoTMed + MontoMed;

                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + MontoMed + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + User + "','" + DatosFactMed.getString(17) + "','','','','','','','','','" + ProyectoFactura + "','" + DatosFactMed.getString(18) + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','','" + remis + "',0)");
                }

                Ban = 1;

                if (Ban == 1) {

                    for (int x = 0; x < Copy; x++) {
                        JRPrintServiceExporter exporter = this.imprimeReporte("/reportes/ImprimeFoliosMicSurt.jasper", Nomenclatura, remis, User, F_Obs, RedFria, Imgape, ImagenControlado, imgOrigen, application, con.getConn(), impresoras[Epson]);
                        try {
                            exporter.exportReport();
                        } catch (Exception ex) {

                               ex.printStackTrace();

                        }
                    }
                }

            }

            ////////////////
            ///////ape//////
            if (TipoInsumo.equals("0") || TipoInsumo.equals("3")) {
                 System.out.println("Tipo de insumo ape");
                con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + User + "';");

                RedFria = "image/Nored_fria.jpg";
                Imgape = "image/imgape.png";

                ResultSet DatosFactMedAPE = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN '(RED FRÍA)' ELSE '' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur>0 and F_DocAnt !='1' AND F.F_Proyecto = '" + ProyectoFactura + "' AND F.F_Ubicacion like '%APE%' AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY F.F_ClaPro + 0;");

                while (DatosFactMedAPE.next()) {
                    SumaMedReqA = DatosFactMedAPE.getInt("F_CantReq");
                    SumaMedSurA = DatosFactMedAPE.getInt("F_CantSur");
                    Origen = DatosFactMedAPE.getInt("F_Origen");
                    if (Origen == 1) {
                        MontoMed = 0;
                        Costo = 0;
                    } else {
                        MontoMed = 0;
                        Costo = 0;
                    }
                     if (ProyectoFactura.equals("7")) {
                    
                        if (Origen == 27  || Origen == 26) {
                            imgOrigen = "image/almacen.png";
                        }else{
                            imgOrigen = "image/Nored_fria.jpg"; 
                        }
                    }
                    SumaMedReqTA = SumaMedReqTA + SumaMedReqA;
                    SumaMedSurTA = SumaMedSurTA + SumaMedSurA;

                    Unidad = DatosFactMedAPE.getString("F_NomCli");
                    Direc = DatosFactMedAPE.getString("F_Direc");
                    Fecha = DatosFactMedAPE.getString("F_FecEnt");
                    F_FecApl = DatosFactMedAPE.getString("F_FecApl");
                    Razon = DatosFactMedAPE.getString(15);
                    Proyecto = DatosFactMedAPE.getString(18);
                    Jurisdiccion = DatosFactMedAPE.getString(19);
                    Municipio = DatosFactMedAPE.getString(20);
                    MontoTMed = MontoTMed + MontoMed;
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMedAPE.getString(1) + "','" + DatosFactMedAPE.getString(2) + "','" + DatosFactMedAPE.getString(3) + "','" + Nomenclatura + "" + DatosFactMedAPE.getString(4) + "','" + DatosFactMedAPE.getString(5) + "','" + DatosFactMedAPE.getString(6) + "','" + DatosFactMedAPE.getString(7) + "','" + DatosFactMedAPE.getString(8) + "','" + DatosFactMedAPE.getString(9) + "','" + DatosFactMedAPE.getString(10) + "','" + DatosFactMedAPE.getString(11) + "','" + Costo + "','" + MontoMed + "','" + F_Obs + "','" + DatosFactMedAPE.getString(14) + "','" + DatosFactMedAPE.getString(15) + "','" + User + "','" + DatosFactMedAPE.getString(18) + "','','','','','','','','','" + ProyectoFactura + "','" + DatosFactMedAPE.getString(19) + "','" + DatosFactMedAPE.getString(20) + "','" + DatosFactMedAPE.getString(21) + "','','" + remis + "',0)");
                }

                TotalReq = SumaMatReqT + SumaMedReqT + SumaMedReqTA + SumaMedReqTR;
                TotalSur = SumaMedSurT + SumaMatSurT + SumaMedSurTA + SumaMedSurTR;
                TotalMonto = MontoTMat + MontoTMed;

                 MTotalMonto = TotalMonto + Iva;
                boolean band = true;
                Numero_a_Letra NumLetra = new Numero_a_Letra();
                String numero = String.valueOf(String.format("%.2f", MTotalMonto));
                Letra = NumLetra.Convertir(numero, band);
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

                Ban = 1;

                if (Ban == 1) {
                    for (int x = 0; x < Copy; x++) {
                        JRPrintServiceExporter exporter = this.imprimeReporte("/reportes/ImprimeFoliosMicSurt.jasper", Nomenclatura, remis, User, F_Obs, RedFria, Imgape, ImagenControlado, imgOrigen, application, con.getConn(), impresoras[Epson]);
                        try {
                            exporter.exportReport();
                        } catch (Exception ex) {

                              ex.printStackTrace();

                        }
                    }
                }

            }
            ///////////////////////////
            /// //Impresión de Red Fría///
            
             
            if (TipoInsumo.equals("0") || TipoInsumo.equals("2")) {
                 System.out.println("Tipo de insumo red fria");
                
                con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + Nomenclatura + "" + remis + "' AND F_User='" + User + "';");
                ResultSet DatosFactMedRED = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon, L.F_Origen, L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN '(RED FRÍA)' ELSE '' END AS REDFRI FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro WHERE F_ClaDoc='" + remis + "' and F_CantSur>0 and F_DocAnt !='1' AND F.F_Proyecto = '" + ProyectoFactura + "' AND F.F_Ubicacion RLIKE 'REDFRIA|RF' AND F.F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen ORDER BY F.F_ClaPro + 0;");

                Imgape = "image/Nored_fria.jpg";
                RedFria = "image/red_fria.jpg";

                while (DatosFactMedRED.next()) {
                    SumaMedReqR = DatosFactMedRED.getInt("F_CantReq");
                    SumaMedSurR = DatosFactMedRED.getInt("F_CantSur");
                    Origen = DatosFactMedRED.getInt("F_Origen");
                    if (Origen == 1) {
                        MontoMed = 0;
                        Costo = 0;
                    } else {
                        MontoMed = 0;
                        Costo = 0;
                    }
                     if (ProyectoFactura.equals("7")) {
                       
                        if (Origen == 27  || Origen == 26) {
                            imgOrigen = "image/almacen.png";
                        }else{
                            imgOrigen = "image/Nored_fria.jpg"; 
                        }
                    }
                    SumaMedReqTR = SumaMedReqTR + SumaMedReqR;
                    SumaMedSurTR = SumaMedSurTR + SumaMedSurR;

                    Unidad = DatosFactMedRED.getString("F_NomCli");
                    Direc = DatosFactMedRED.getString("F_Direc");
                    Fecha = DatosFactMedRED.getString("F_FecEnt");
                    F_FecApl = DatosFactMedRED.getString("F_FecApl");
                    Razon = DatosFactMedRED.getString(15);
                    Proyecto = DatosFactMedRED.getString(18);
                    Jurisdiccion = DatosFactMedRED.getString(19);
                    Municipio = DatosFactMedRED.getString(20);
                    MontoTMed = MontoTMed + MontoMed;
                  
                    
                    con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMedRED.getString(1) + "','" + DatosFactMedRED.getString(2) + "','" + DatosFactMedRED.getString(3) + "','" + Nomenclatura + "" + DatosFactMedRED.getString(4) + "','" + DatosFactMedRED.getString(5) + "','" + DatosFactMedRED.getString(6) + "','" + DatosFactMedRED.getString(7) + "','" + DatosFactMedRED.getString(8) + "','" + DatosFactMedRED.getString(9) + "','" + DatosFactMedRED.getString(10) + "','" + DatosFactMedRED.getString(11) + "','" + Costo + "','" + MontoMed + "','" + F_Obs + "','" + DatosFactMedRED.getString(14) + "','" + DatosFactMedRED.getString(15) + "','" + User + "','" + DatosFactMedRED.getString(18) + "','','','','','','','','','" + ProyectoFactura + "','" + DatosFactMedRED.getString(19) + "','" + DatosFactMedRED.getString(20) + "','" + DatosFactMedRED.getString(21) + "','','" + remis + "',0)");
                }

                TotalReq = SumaMatReqT + SumaMedReqT + SumaMedReqTA + SumaMedReqTR;
                TotalSur = SumaMedSurT + SumaMatSurT + SumaMedSurTA + SumaMedSurTR;
                TotalMonto = MontoTMat + MontoTMed;

                MTotalMonto = TotalMonto + Iva;
             
                boolean band = true;
                Numero_a_Letra NumLetra = new Numero_a_Letra();
                String numero = String.valueOf(String.format("%.2f", MTotalMonto));
                Letra = NumLetra.Convertir(numero, band);

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

                Ban = 1;

                if (Ban == 1) {
                    for (int x = 0; x < Copy; x++) {
                        JRPrintServiceExporter exporter = this.imprimeReporte("/reportes/ImprimeFoliosMicSurt.jasper", Nomenclatura, remis, User, F_Obs, RedFria, Imgape, ImagenControlado,imgOrigen, application, con.getConn(), impresoras[Epson]);
                        try {
                            exporter.exportReport();
                        } catch (Exception ex) {

                              ex.printStackTrace();

                        }
                    }
                }
            }

      
    }
        
%>
<%!
    public JRPrintServiceExporter imprimeReporte(String jasperName, String nomenclatura, String remis,
            String usuario, String obs, String RedFria, String Imgape, String ImagenControlado, String imgOrigen, ServletContext application, Connection conexion, PrintService impresora) {
        File reportFile = new File(application.getRealPath(jasperName));
        Map parameters = new HashMap();
        //parameters.put("Folfact", remis);
        parameters.put("Folfact", nomenclatura + remis);        
        parameters.put("Usuario", usuario);
        parameters.put("F_Obs", obs);
        parameters.put("RedFria", RedFria);
        parameters.put("Imgape", Imgape);
        parameters.put("ImagenControlado", ImagenControlado);
        parameters.put("ImgOrigen", imgOrigen);

        System.out.println(remis);
        System.out.println("ImgOrigen: " + imgOrigen);
        System.out.println("RedFria: "+ RedFria);
        System.out.println("Imgape: "+ Imgape);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), parameters, conexion);
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, impresora.getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            return exporter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
%>
<script type="text/javascript">

    var ventana = window.self;
    ventana.opener = window.self;
    setTimeout("window.close()", 5000);

</script>
