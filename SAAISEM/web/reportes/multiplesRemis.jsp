<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="Impresiones.ImprimeFolio"%>
<%@page import="javax.print.attribute.PrintServiceAttributeSet"%>
<%@page import="javax.print.attribute.standard.Sides"%>
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
<%@page import="java.io.File"%>
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
        // response.sendRedirect("index.jsp");
    }

    ImprimeFolio imp = new ImprimeFolio();

    int RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, ContarRedF = 0, Ban2 = 0;
    String DesV = "", remis = "", ProyectoF = "", ProyectoFactura = "", TipMed = "", Nomenclatura = "", Encabezado = "", RedFria = "", Letra = "", Imgape = "", NoImgApe = "";
    double Hoja = 0.0, Hoja2 = 0.0, MTotalMonto = 0.0, Iva = 0.0;
    String imgOrigen = "";
    String User = request.getParameter("User");
    String Impresora = request.getParameter("Impresora");
    DecimalFormat df = new DecimalFormat("#,###.00");
    String TipoInsumo = request.getParameter("Tipo");
    int Copy = 0;

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

    ResultSet FoliosC = con.consulta("SELECT F_Folio, F_Copy, U.F_Proyecto, F.F_Proyecto AS F_ProyectoF  FROM tb_folioimp F INNER JOIN tb_factura FT ON F.F_Folio = FT.F_ClaDoc AND F.F_Proyecto = FT.F_Proyecto INNER JOIN tb_uniatn U ON FT.F_ClaCli = U.F_ClaCli WHERE F.F_User = '" + User + "' GROUP BY F.F_Folio, F.F_Proyecto ORDER BY F_Folio + 0;");
    while (FoliosC.next()) {
        remis = FoliosC.getString(1);
        Copy = FoliosC.getInt(2);
        ProyectoF = FoliosC.getString(3);
        ProyectoFactura = FoliosC.getString(4);
        Ban2 = 3;
        System.out.println("ProyectoF: " + ProyectoF);
        System.out.println("ProyectoFactura: " + ProyectoFactura);

        ///////////**comienza co encabezado dependiendo el proyecto
        ResultSet RsetNomenc = con.consulta("SELECT F_Nomenclatura, F_Encabezado FROM tb_proyectos WHERE F_Id='" + ProyectoFactura + "';");
        while (RsetNomenc.next()) {
            Nomenclatura = RsetNomenc.getString(1);
            Encabezado = RsetNomenc.getString(2);
        }

        con.actualizar("DELETE FROM tb_imprefolio WHERE F_Folio='" + remis + "';");

        int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0, Origen = 0, SumaRedFria = 0, SumaMedRedFT = 0, SumaReFSur = 0, SumaReFSurT = 0, contarGC = 0;
        double MontoMed = 0.0, MontoTMed = 0.0, Costo = 0.0, MontoReF = 0.0, MontoTRedF = 0.0;
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "", ImagenControlado = "", CargoResponsable = "", NombreResponsable = "", NoImgGC = "", ImgGC = "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0, ContarControlado = 0, Contarape = 0, Contarvac = 0, ContarCont = 0;
        double MontoMat = 0.0, MontoTMat = 0.0;
        int TotalReq = 0, TotalSur = 0;
        double TotalMonto = 0.0;
        int StsRF = 0;

        ResultSet ObsFact = con.consulta("SELECT F_Obser FROM tb_obserfact WHERE F_IdFact='" + remis + "' AND F_Proyecto = '" + ProyectoFactura + "' GROUP BY F_IdFact;");
        while (ObsFact.next()) {
            F_Obs = ObsFact.getString(1);
        }

        ResultSet RsetControlado = con.consulta("SELECT F.F_ClaDoc, COUNT(*) AS CONTAR, IFNULL(FC.CONTARCONTROLADO, 0) AS CONTARCONTROLADO, COUNT(*) - IFNULL(FC.CONTARCONTROLADO, 0) AS DIF FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARCONTROLADO FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = '" + ProyectoFactura + "' AND F_Ubicacion  RLIKE 'CONTROLADO|CTRL' ) FC ON F.F_ClaDoc = FC.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "'AND F_Proyecto = '" + ProyectoFactura + "' GROUP BY F.F_ClaDoc;");
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
                 System.out.println("NombreResponsable: " + NombreResponsable);
            }
        } else {
            System.out.println("no exite usurio controlado");
            ImagenControlado = "image/NoControlado.jpg";
            CargoResponsable = " ";
            NombreResponsable = " ";
            // System.out.println("NombreResponsable: " + NombreResponsable);
        }

        ResultSet DatosRedF = con.consulta("SELECT COUNT(*) FROM tb_redfria r INNER JOIN tb_factura f ON r.F_ClaPro = f.F_ClaPro AND  f.F_Ubicacion Rlike 'REDFRIA1|RF' WHERE F_StsFact = 'A' AND f.F_ClaDoc = '" + remis + "' AND f.F_CantSur > 0 AND f.F_Proyecto = '" + ProyectoFactura + "';");
        if (DatosRedF.next()) {
            ContarRedF = DatosRedF.getInt(1);
            System.out.println(ContarRedF);
        }
        if (ContarRedF > 0) {

            RedFria = "image/red_fria.jpg";
            StsRF = 1;
        } else {
            RedFria = "image/Nored_fria.jpg";
            StsRF = 1;
        }

        ResultSet DatosAPE = con.consulta("SELECT COUNT(*) FROM tb_ape ap INNER JOIN tb_factura f ON ap.F_ClaPro = f.F_ClaPro AND  f.F_Ubicacion LIKE '%APE%' WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + ProyectoFactura + "';");
        if (DatosAPE.next()) {
            Contarape = DatosAPE.getInt(1);
        }

        if (Contarape > 0) {
            Imgape = "image/imgape.png";
            Ban2 = 3;
        } else {
            Imgape = "image/Nored_fria.jpg";
        }

        ResultSet DatosGastos = con.consulta("SELECT COUNT(*) FROM tb_factura AS F INNER JOIN tb_lote AS L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica WHERE F.F_StsFact = 'A' AND F.F_ClaDoc = '" + remis + "' AND F.F_CantSur > 0 AND F.F_Proyecto = '" + ProyectoFactura + "' AND L.F_Origen = '19';");
        if (DatosGastos.next()) {
            contarGC = DatosGastos.getInt(1);
        }
        NoImgGC = "image/Nored_fria.jpg";
        if (contarGC > 0) {
            ImgGC = "image/ImgGC.png";
        } else {
            ImgGC = NoImgGC;
        }
        imgOrigen = "image/Nored_fria.jpg";

        //ResultSet DatosVac = con.consulta(" SELECT  IFNULL(COUNT(*), 0) AS CONTAR FROM tb_factura F LEFT JOIN ( SELECT F_ClaDoc, COUNT(*) AS CONTARCONTROLADO FROM tb_factura WHERE F_ClaDoc = '" + remis + "' AND F_Proyecto = '" + ProyectoFactura + "' AND F_Ubicacion LIKE '%CONTROLADO%' OR F_Ubicacion LIKE '%VACUNA%' AND F_CantSur > 0) FC ON F.F_ClaDoc = FC.F_ClaDoc WHERE F.F_ClaDoc = '" + remis + "'AND F_Proyecto = '" + ProyectoFactura + "' AND F_CantSur > 0 GROUP BY F.F_ClaDoc;");
        ResultSet DatosVac = con.consulta("SELECT IFNULL(COUNT(*), 0) AS CONTAR FROM tb_vacunas ap INNER JOIN tb_factura f ON ap.F_ClaPro = f.F_ClaPro WHERE F_StsFact = 'A' AND F_ClaDoc = '" + remis + "' AND F_CantSur > 0 AND F_Proyecto = '" + ProyectoFactura + "' AND F_Ubicacion like '%VACUNA%';");
        if (DatosVac.next()) {
            Contarvac = DatosVac.getInt(1);
        }
        if (Contarvac > 0 || ContarCont > 0) {
            //   F_Obs += " (Folio no valido) ";
        }

        System.out.println("bandera de tipo: " + TipoInsumo);

        /*para bita*/
        //////////////otros proyectos tomasant//////////////////
        //////////////////////////////////////////////////
        System.out.println("Todos los proyectos");
        ResultSet DatosFactMed = null;

        if (TipoInsumo.equals("0")) {
            System.out.println("Tipo de insumo cero");
            DatosFactMed = con.consulta("SELECT F.F_ClaCli, U.F_NomCli, U.F_Direc, F.F_ClaDoc, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F.F_ClaPro, M.F_DesPro AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_Costo) AS F_Costo, SUM(F.F_Monto) AS F_Monto, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, U.F_Razon,  L.F_Proyecto, P.F_DesProy, J.F_DesJurIS, MU.F_DesMunIS, CASE WHEN RF.F_ClaPro IS NOT NULL THEN 'red_fria.jpg' ELSE 'Nored_fria.jpg' END AS REDFRI, M.F_TipMed, M.F_Costo as Costom, F_OC,L.F_Origen FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_Ubicacion = L.F_Ubica AND F.F_ClaPro = L.F_ClaPro INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_proyectos P ON F.F_Proyecto=P.F_Id LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis MU ON U.F_ClaMun = MU.F_ClaMunIS AND U.F_ClaJur = MU.F_JurMunIS LEFT JOIN tb_redfria RF ON F.F_ClaPro = RF.F_ClaPro and F.F_Ubicacion in ('REDFRIA') WHERE F_ClaDoc='" + remis + "'  and F_CantSur>0 and F_DocAnt !='1' AND F.F_Proyecto = '" + ProyectoFactura + "' AND RF.F_ClaPro IS NULL GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad  ORDER BY REDFRI ASC, F.F_ClaPro + 0;");

            while (DatosFactMed.next()) {
                SumaMedReq = DatosFactMed.getInt("F_CantReq");
                SumaMedSur = DatosFactMed.getInt("F_CantSur");
                Origen = DatosFactMed.getInt("F_Origen");
                SumaMedReqT = SumaMedReqT + SumaMedReq;
                SumaMedSurT = SumaMedSurT + SumaMedSur;

                Unidad = DatosFactMed.getString("F_NomCli");
                Direc = DatosFactMed.getString("F_Direc");
                Fecha = DatosFactMed.getString("F_FecEnt");
                F_FecApl = DatosFactMed.getString("F_FecApl");
                Razon = DatosFactMed.getString(15);
                Proyecto = DatosFactMed.getString(17);
                Jurisdiccion = DatosFactMed.getString(18);
                Municipio = DatosFactMed.getString(19);

                imgOrigen = "image/Nored_fria.jpg";

                MontoTMed = MontoTMed + MontoMed + Iva;
                System.out.println("Medicamento: " + MontoTMed);

                con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMed.getString(1) + "','" + DatosFactMed.getString(2) + "','" + DatosFactMed.getString(3) + "','" + Nomenclatura + "" + DatosFactMed.getString(4) + "','" + DatosFactMed.getString(5) + "','" + DatosFactMed.getString(6) + "','" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + Costo + "','" + df.format(MontoMed) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','" + DatosFactMed.getString(15) + "','" + User + "','" + DatosFactMed.getString(17) + "','','','','','','','','','" + ProyectoFactura + "','" + DatosFactMed.getString(18) + "','" + DatosFactMed.getString(19) + "','" + DatosFactMed.getString(20) + "','" + Encabezado + "','" + remis + "',0)");
                System.out.println(SumaMedSurT);

            }
            SumaMatReqT = SumaMatReqT + SumaMatReq;
            SumaMatSurT = SumaMatSurT + SumaMatSur;
            MontoTMat = MontoTMat + MontoMat;
            Ban = 1;
        }

        if (TipoInsumo.equals("1")) {

            imgOrigen = "image/Nored_fria.jpg";

            Ban = 1;

        }
        if (TipoInsumo.equals("4")) {

            RedFria = "image/Nored_fria.jpg";
            Imgape = "image/Nored_fria.jpg";
            imgOrigen = "image/Nored_fria.jpg";

            Ban = 1;

        } ////////    RED FRIA /////////////
        else if (TipoInsumo.equals("2")) {
           // System.out.println("Tipo de insumo red fria");
            imgOrigen = "image/Nored_fria.jpg";
            RedFria = "image/red_fria.jpg";
            Imgape = "image/Nored_fria.jpg";
            Ban = 1;
        } ////////   APE //////////////    
        else if (TipoInsumo.equals("3")) {

          //  System.out.println("Tipo de insumo ape");
            RedFria = "image/Nored_fria.jpg";
            Imgape = "image/imgape.png";
            imgOrigen = "image/Nored_fria.jpg";
            Ban = 1;
        }

        if (Ban == 1) {
            for (int x = 0; x < Copy; x++) {
                System.out.println("remis-> " + remis);
                
                File reportFile = null;
                Map parameters = new HashMap();

                parameters.put("Folfact", remis);
                parameters.put("TipoInsumo", TipoInsumo);
                parameters.put("Proyecto", ProyectoFactura);

                System.out.println("ProyectoFactura: "+ProyectoFactura);
                if (ProyectoFactura.equals("8")) {
                         System.out.println("proyecto merida");
                        parameters.put("Folfact", remis);
                        switch (TipoInsumo) {
                            case "1":
                                System.out.println("proyecto merida seco");
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                
                                break;
                            case "2":
                                 System.out.println("proyecto merida rf");
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                              
                                break;
                            case "3":
                                 System.out.println("proyecto merida ape");
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                               
                                break;
                            default:
                                System.out.println("proyecto merida no hay controlados");
                                break;
                        }
                     reportFile = new File(application.getRealPath("/reportes/ImprimeFolios15.jasper"));
                        
                    } else if (ProyectoFactura.equals("9")) {
                         System.out.println("proyecto hemodialisis");
                        switch (TipoInsumo) {
                            
                            case "1":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "2":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "3":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            default:
                                System.out.println("proyecto hemo no hay controlados");
                                break;
                        }
                       reportFile = new File(application.getRealPath("/reportes/ImprimeFolios30.jasper"));
                    }else if (ProyectoFactura.equals("10")) {
                          
                         System.out.println("proyecto anestesia");
                        switch (TipoInsumo) {
                            case "1":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "2":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "3":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            default:
                                System.out.println("proyecto anestesia no hay controlados");
                                break;
                        }
                       reportFile = new File(application.getRealPath("/reportes/ImprimeFolios30.jasper"));
                  
                    }else if (ProyectoFactura.equals("11")) {
                          System.out.println("proyecto medica renacer");
                        switch (TipoInsumo) {
                            case "1":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "2":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "3":
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                break;
                            case "4":
                                parameters.put("ImgOrigen", imgOrigen);
                                parameters.put("RedFria", RedFria);
                                parameters.put("Imgape", Imgape);
                                parameters.put("ImagenControlado", ImagenControlado);
                                parameters.put("CargoResponsable", CargoResponsable);
                                parameters.put("NombreResponsable", NombreResponsable);
                                break;
                        }
                       reportFile = new File(application.getRealPath("/reportes/ImprimeFolios.jasper"));
                    } else {
                          System.out.println("proyecto NA");
                        parameters.put("ImgOrigen", imgOrigen);
                        parameters.put("RedFria", RedFria);
                        parameters.put("Imgape", Imgape);
                        parameters.put("ImagenControlado", ImagenControlado);
                        parameters.put("CargoResponsable", CargoResponsable);
                        parameters.put("NombreResponsable", NombreResponsable);
                       reportFile = new File(application.getRealPath("/reportes/ImprimeFolios.jasper"));
                    }
              
                

                parameters.put("Usuario", User);
                parameters.put("F_Obs", F_Obs);
                parameters.put("RedFria", RedFria);
                parameters.put("Imgape", Imgape);
                parameters.put("ImgOrigen", imgOrigen);

                System.out.println("Usuario: " + User);
                System.out.println("F_Obs: " + F_Obs);
                System.out.println("Origen imagen: " + imgOrigen);
                System.out.println("Origen: " + Origen);
                System.out.println("Proyectof: " + ProyectoFactura);
                System.out.println("RedFria" + RedFria);
                System.out.println("Imgape" + Imgape);

                JasperPrint jasperPrint = JasperFillManager.fillReport( reportFile.getPath(), parameters, con.getConn());
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                PrintRequestAttributeSet printRequestAttributes = new HashPrintRequestAttributeSet();
                // printRequestAttributes.add(Sides.ONE_SIDED);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
                 printRequestAttributes);

                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, impresoras[Epson].getAttributes());
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

                try {
                    exporter.exportReport();
                } catch (Exception ex) {

                    System.out.println("Error-> " + ex);

                }
            }
        }
    }
    

    conexion.close();

%>
<script type="text/javascript">

    var ventana = window.self;
    ventana.opener = window.self;
    setTimeout("window.close()", 5000);

</script>
