package ReportesPuntos;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
//import sun.org.mozilla.javascript.internal.ast.Loop;

/**
 * Generación de reporte global por farmacia facturación txt isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ReporteGlobalFarm extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        try {
            ResultSet FoliosFact = null;
            ResultSet FoliosFactura = null;
            ResultSet FactAgr = null;
            ResultSet Txt = null;
            ResultSet TxtRegion = null;
            ResultSet TxtDatos = null;
            ResultSet TxtSecuenc = null;
            String Fecha1 = "", Fecha2 = "";
            int Folio = 0;

            String F_FacGNKLAgr = "";
            String F_Region = "", F_CveJur = "", F_Cvemun = "", F_CveLoc = "", F_CveUni = "", F_Cvepro = "", F_Fecsur = "";
            String F_FecIni = "", F_FecFin = "", F_FolCon = "", F_Title = "", F_Surti = "", F_Cober = "", F_Sumi = "";
            String F_DesSur = "", F_Descob = "", F_DesSum = "", F_Dia2 = "", Folio1 = "", Folio2 = "", Radio = "", FechaFol = "", Mes = "";
            int F_Dia = 0, F_Dia1 = 0, F_Mes = 0, F_Mes1 = 0, F_Ano = 0, F_Ano1 = 0, NumReg = 0;
            int F_SecIni = 0, F_SecFin = 0, F_SecIniC = 0, F_SecFinC = 0, PzsReq = 0, PzsSur = 0, PzsNOSur = 0, vp_Reporte = 0, F_Idsur = 0, F_IdePro = 0, F_Cvesum = 0;
            double F_Cosmed = 0.0, CosServ = 0.0, CosServSub = 0.0, F_CosServ = 0.0, F_IVA = 0.0, F_Total = 0.0, IVA = 0.0, Total = 0.0, CosNOSur = 0.0;
            int PzsReqC = 0, PzsSurC = 0, PzsNOSurC = 0;
            double CosNOSurC = 0.0, TotalC = 0.0, F_CosServC = 0.0, F_CosmedC = 0.0, IVAC = 0.0, CosMed = 0.0;
            String F_DesRegion = "", F_DesJur = "", F_DesMun1 = "", F_DesLoc = "", F_DesUni = "", F_Contrato = "", Impresora = "";
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            Fecha1 = request.getParameter("fecha_ini");
            Fecha2 = request.getParameter("fecha_fin");
            F_CveUni = request.getParameter("Unidad");

            FechaFol = " F_Fecsur between '" + Fecha1 + "' and '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND F_Cveuni='" + F_CveUni + "' ";

            if (((Fecha1 != "") && (Fecha2 != ""))) {
                FoliosFactura = con.consulta("SELECT COUNT(F_Secuencial) FROM tb_txtis WHERE " + FechaFol + " ");
                while (FoliosFactura.next()) {
                    Folio = FoliosFactura.getInt(1);

                    if (Folio == 0) {
                        out.println("<script>alert('Unidad no Registrada Con Secuenciales')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {

                        /*con.actualizar("delete from tb_imprepconfarm where F_Usuario='"+sesion.getAttribute("nombre")+"'");
                con.actualizar("delete from tb_imprepvalfarm where F_Usuario='"+sesion.getAttribute("nombre")+"'");
                con.actualizar("delete from tb_imprepreqfarm where F_Usuario='"+sesion.getAttribute("nombre")+"'");*/
                        con.actualizar("delete from tb_imprepconfarm");
                        con.actualizar("delete from tb_imprepvalfarm");
                        con.actualizar("delete from tb_imprepreqfarm");
                        Txt = con.consulta("SELECT * FROM tb_txtis WHERE " + FechaFol + " AND (F_Status <> 'C' OR F_Status='') LIMIT 1,1");
                        while (Txt.next()) {
                            F_Region = Txt.getString("F_Region");
                            F_CveJur = Txt.getString("F_CveJur");
                            F_Cvemun = Txt.getString("F_Cvemun");
                            F_CveLoc = Txt.getString("F_CveLoc");
                            F_CveUni = Txt.getString("F_CveUni");
                            F_Cvepro = Txt.getString("F_Cvepro");
                            F_Fecsur = Txt.getString("F_Fecsur");
                            F_Contrato = Txt.getString("F_Contrato");
                        }
                        if (F_Contrato.equals("")) {
                            F_Contrato = ".";
                        }

                        TxtRegion = con.consulta("SELECT F_DesRegIS FROM tb_regiis WHERE F_ClaRegIS='" + F_Region + "'");
                        if (TxtRegion.next()) {
                            F_DesRegion = F_Region + " " + TxtRegion.getString(1);
                        }
                        TxtDatos = con.consulta("SELECT U.F_DesUniIS,J.F_DesJurIS,M.F_DesMunIS,L.F_DesLocIS "
                                + "FROM tb_unidis U INNER JOIN tb_locais L ON U.F_LocUniIS=L.F_ClaLocIS AND U.F_MunUniIS=L.F_MunLocIS "
                                + "INNER JOIN tb_muniis M ON L.F_MunLocIS=M.F_ClaMunIS INNER JOIN tb_juriis J ON M.F_JurMunIS=J.F_ClaJurIS "
                                + "WHERE U.F_ClaUniIS='" + F_CveUni + "' AND U.F_JurUniIS='" + F_CveJur + "' "
                                + "AND U.F_LocUniIS='" + F_CveLoc + "' AND U.F_MunUniIS='" + F_Cvemun + "' ");
                        if (TxtDatos.next()) {
                            F_DesUni = F_CveUni + " " + TxtDatos.getString(1);
                            F_DesJur = F_CveJur + " " + TxtDatos.getString(2);
                            //F_DesMun = F_Cvemun +""+ TxtDatos.getString(3);
                            F_DesMun1 = F_Cvemun + " " + TxtDatos.getString(3);
                            F_DesLoc = F_CveLoc + " " + TxtDatos.getString(4);

                        }

                        F_Dia = Integer.parseInt(F_Fecsur.substring(8, 10));
                        F_Mes = Integer.parseInt(F_Fecsur.substring(5, 7));
                        F_Ano = Integer.parseInt(F_Fecsur.substring(0, 4));
                        if (F_Dia <= 15) {
                            F_Dia2 = "01";
                            F_Dia1 = 15;
                        } else {
                            F_Dia2 = "16";
                            if ((F_Mes == 2 && F_Ano == 2016) || (F_Mes == 2 && F_Ano == 2020) || (F_Mes == 2 && F_Ano == 2024)) {
                                F_Dia1 = 29;
                            }
                            if (F_Mes == 1) {
                                F_Dia1 = 31;
                            } else if ((F_Mes == 2 && F_Ano != 2016) || (F_Mes == 2 && F_Ano != 2020) || (F_Mes == 2 && F_Ano != 2024)) {
                                F_Dia1 = 28;
                            } else if (F_Mes == 3) {
                                F_Dia1 = 31;
                            } else if (F_Mes == 4) {
                                F_Dia1 = 30;
                            } else if (F_Mes == 5) {
                                F_Dia1 = 31;
                            } else if (F_Mes == 6) {
                                F_Dia1 = 30;
                            } else if (F_Mes == 7) {
                                F_Dia1 = 31;
                            } else if (F_Mes == 8) {
                                F_Dia1 = 31;
                            } else if (F_Mes == 9) {
                                F_Dia1 = 30;
                            } else if (F_Mes == 10) {
                                F_Dia1 = 31;
                            } else if (F_Mes == 11) {
                                F_Dia1 = 30;
                            } else if (F_Mes == 12) {
                                F_Dia1 = 31;
                            }
                        }
                        if (F_Mes < 10) {
                            Mes = "0" + F_Mes;
                        } else {
                            Mes = "" + F_Mes;
                        }
                        F_FecIni = F_Dia2 + "/" + Mes + "/" + F_Ano;
                        F_FecFin = F_Dia1 + "/" + Mes + "/" + F_Ano;

                        for (int Reporte = 1; Reporte <= 8; Reporte++) {

                            if (Reporte == 1) {
                                F_Idsur = 1;
                                F_IdePro = 0;
                                F_Cvesum = 1;
                            } else if (Reporte == 2) {
                                F_Idsur = 1;
                                F_IdePro = 1;
                                F_Cvesum = 1;
                            } else if (Reporte == 3) {
                                F_Idsur = 1;
                                F_IdePro = 0;
                                F_Cvesum = 2;
                            } else if (Reporte == 4) {
                                F_Idsur = 1;
                                F_IdePro = 1;
                                F_Cvesum = 2;
                            } else if (Reporte == 5) {
                                F_Idsur = 2;
                                F_IdePro = 0;
                                F_Cvesum = 1;
                            } else if (Reporte == 6) {
                                F_Idsur = 2;
                                F_IdePro = 1;
                                F_Cvesum = 1;
                            } else if (Reporte == 7) {
                                F_Idsur = 2;
                                F_IdePro = 0;
                                F_Cvesum = 2;
                            } else if (Reporte == 8) {
                                F_Idsur = 2;
                                F_IdePro = 1;
                                F_Cvesum = 2;
                            }
                            F_FolCon = "R-" + F_Dia1 + Mes + F_Ano + "." + Reporte;
                            con.actualizar("UPDATE tb_txtis SET F_FolCon='' WHERE F_FolCon='" + F_FolCon + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND F_Cveuni='" + F_CveUni + "'");
                            F_FacGNKLAgr = "R-" + F_Dia1 + Mes + F_Ano;
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis "
                                    + "WHERE F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' "
                                    + "AND F_Cvesum = '" + F_Cvesum + "' AND (F_Status <> 'C' OR F_Status='') and " + FechaFol + " "
                                    + "ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(2);
                            }
                            if (F_SecIni > 0) {

                                TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis "
                                        + "WHERE " + FechaFol + " AND (F_Status <> 'C' OR F_Status='')  "
                                        + "ORDER BY F_Secuencial");
                                while (TxtSecuenc.next()) {
                                    F_SecIni = TxtSecuenc.getInt(1);
                                    F_SecFin = TxtSecuenc.getInt(2);
                                }

                                con.actualizar("UPDATE tb_txtis SET F_FolCon = '" + F_FolCon + "' WHERE " + FechaFol + " AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "'");
                                TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni "
                                        + "FROM  tb_txtis WHERE F_FolCon='" + F_FolCon + "' AND  F_Cveuni='" + F_CveUni + "' GROUP BY F_Cveart ORDER BY F_Cveart ASC ");
                                while (TxtSecuenc.next()) {
                                    PzsReq = PzsReq + TxtSecuenc.getInt("F_Canreq");
                                    PzsSur = PzsSur + TxtSecuenc.getInt("F_Cansur");

                                    if (TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur") <= 0) {
                                        PzsNOSur = PzsNOSur + 0;
                                        CosNOSur = CosNOSur + 0;
                                    } else {
                                        PzsNOSur = PzsNOSur + (TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur"));
                                        CosNOSur = CosNOSur + ((TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur")) * TxtSecuenc.getDouble("F_CosUni"));
                                    }
                                    F_Cosmed = TxtSecuenc.getInt("F_Cansur") * TxtSecuenc.getDouble("F_CosUni");

                                    //valida ceaps
                                    if ((F_CveUni.equals("CSSA017114")) || (F_CveUni.equals("CSSA017153")) || (F_CveUni.equals("CSSA017154")) || (F_CveUni.equals("CSSA017109")) || (F_CveUni.equals("CSSA017126")) || (F_CveUni.equals("CSSA017151")) || (F_CveUni.equals("MCSSA000982")) || (F_CveUni.equals("CEAPSBEJ001"))) {
                                        //if (F_Surti.equals("001 ADMINISTRACION")){
                                        CosServ = 0;
                                        CosServSub = 0;
                                        /*}else{
                                    CosServ = 9.1;
                                    CosServSub = 7.84;
                                }*/
                                    } else {
                                        //if (F_Surti.equals("001 ADMINISTRACION")){
                                        CosServ = 0;
                                        CosServSub = 0;
                                        /*}else{
                                    CosServ = 4.74;
                                    CosServSub = 5.5;
                                }*/
                                    }

                                    F_CosServ = TxtSecuenc.getInt("F_Cansur") * CosServSub;
                                    if ((F_Idsur == 2) && (F_Cvesum == 2)) {
                                        F_IVA = (F_CosServ + F_Cosmed) * 0.16;
                                    } else {
                                        F_IVA = F_CosServ * 0.16;
                                    }

                                    F_Total = F_Cosmed + F_CosServ + F_IVA;

                                    if (F_Idsur == 1) {
                                        F_Surti = "001 ADMINISTRACION";
                                        F_DesSur = "ADM.";
                                    } else {
                                        F_Surti = "002 VENTA";
                                        F_DesSur = "VTA.";
                                    }
                                    if (F_IdePro == 0) {
                                        F_Cober = "001 POBLACION ABIERTA";
                                        F_Descob = "P.A.";
                                    } else {
                                        F_Cober = "002 SEGURO POPULAR";
                                        F_Descob = "S.P.";
                                    }
                                    if (F_Cvesum == 1) {
                                        F_Sumi = "001 MEDICAMENTO";
                                        F_DesSum = "MED.";
                                    } else {
                                        F_Sumi = "002 CURACION";
                                        F_DesSum = "CUR.";
                                    }

                                    con.actualizar("INSERT INTO tb_imprepconfarm VALUES ('" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Total + "','','','','','','','','','','','0','0','','0','','','" + F_DesUni + "','" + F_DesRegion + "','" + F_DesJur + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }

                                con.actualizar("INSERT INTO tb_imprepconfarm VALUES ('','','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + IVA + "','0','" + Total + "','','','','','','','','','','','0','0','','0','','','" + F_DesUni + "','" + F_DesRegion + "','" + F_DesJur + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                //out.println(" <script>window.open('ReportesPuntos/ReporteVerticalImp.jsp?FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteVerticalfarm.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                con.actualizar("INSERT INTO tb_imprepvalfarm VALUES (0,'" + F_DesSur + "','" + F_Descob + "','" + F_DesSum + "','ABIERTO','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + (CosMed + F_CosServ) + "','" + IVA + "','" + (CosMed + F_CosServ + IVA) + "','" + PzsNOSur + "','" + CosNOSur + "','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");

                                PzsReqC = PzsReqC + PzsReq;
                                PzsSurC = PzsSurC + PzsSur;
                                PzsNOSurC = PzsNOSurC + PzsNOSur;
                                CosNOSurC = CosNOSurC + CosNOSur;
                                TotalC = TotalC + CosMed + F_CosServ;
                                F_CosServ = F_CosServC + F_CosServ;
                                F_CosmedC = F_CosmedC + CosMed;
                                IVAC = IVAC + IVA;
                                PzsReq = 0;
                                PzsSur = 0;
                                PzsNOSur = 0;
                                CosNOSur = 0;
                                Total = 0;
                                F_CosServ = 0;
                                F_Cosmed = 0;
                                Total = 0;
                                IVA = 0;
                                CosMed = 0;
                                //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?Mpio="+F_DesMun1+"'</script>");
                                //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"'</script>");

                                //out.println("<script>window.open='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'</script>");
                            }
                        }

                        con.actualizar("INSERT INTO tb_imprepvalfarm VALUES (0,'','','','','" + PzsReqC + "','" + PzsSurC + "','" + F_CosmedC + "','" + F_CosServC + "','" + TotalC + "','" + IVAC + "','" + (TotalC + IVAC) + "','" + PzsNOSurC + "','" + CosNOSurC + "','','','','','','" + F_FolCon + "','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                        PzsReqC = 0;
                        PzsSurC = 0;
                        F_CosmedC = 0;
                        F_CosServC = 0;
                        TotalC = 0;
                        IVAC = 0;
                        PzsNOSurC = 0;
                        CosNOSurC = 0;

                        // REPORTE CONCENTRADO
                        TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE " + FechaFol + " AND (F_Status <> 'C' OR F_Status='') ORDER BY F_Secuencial");
                        while (TxtSecuenc.next()) {
                            F_SecIniC = TxtSecuenc.getInt(1);
                            F_SecFinC = TxtSecuenc.getInt(2);
                        }
                        //F_Title = "REPORTE VALIDACION No "+F_FacGNKLAgr;

                        //out.println(" <script>window.open('ReportesPuntos/ReporteConcentradoImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIniC+"&F_SecFin="+F_SecFinC+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FacGNKLAgr+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        out.println(" <script>window.open('ReportesPuntos/ReporteConcentradofarm.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FacGNKLAgr + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        F_SecIniC = 0;
                        F_SecFinC = 0;

                        // DATOS REPORTE DE REQUERIMIENTOS
                        for (int Reporteq = 1; Reporteq <= 8; Reporteq++) {

                            if (Reporteq == 1) {
                                F_Idsur = 1;
                                F_IdePro = 0;
                                F_Cvesum = 1;
                            } else if (Reporteq == 2) {
                                F_Idsur = 1;
                                F_IdePro = 1;
                                F_Cvesum = 1;
                            } else if (Reporteq == 3) {
                                F_Idsur = 1;
                                F_IdePro = 0;
                                F_Cvesum = 2;
                            } else if (Reporteq == 4) {
                                F_Idsur = 1;
                                F_IdePro = 1;
                                F_Cvesum = 2;
                            } else if (Reporteq == 5) {
                                F_Idsur = 2;
                                F_IdePro = 0;
                                F_Cvesum = 1;
                            } else if (Reporteq == 6) {
                                F_Idsur = 2;
                                F_IdePro = 1;
                                F_Cvesum = 1;
                            } else if (Reporteq == 7) {
                                F_Idsur = 2;
                                F_IdePro = 0;
                                F_Cvesum = 2;
                            } else if (Reporteq == 8) {
                                F_Idsur = 2;
                                F_IdePro = 1;
                                F_Cvesum = 2;
                            }
                            /*if (Reporteq > 1){
                            con.actualizar("delete from tb_imprepreq");
                            }*/
                            // con.actualizar("delete from tb_imprepval");
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE " + FechaFol + " AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND (F_Status <> 'C' OR F_Status='') ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(2);
                            }
                            if (F_SecIni > 0) {
                                TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE " + FechaFol + " AND (F_Status <> 'C' OR F_Status='') ORDER BY F_Secuencial");
                                while (TxtSecuenc.next()) {
                                    F_SecIni = TxtSecuenc.getInt(1);
                                    F_SecFin = TxtSecuenc.getInt(2);
                                }
                                F_FolCon = "R-" + F_Dia1 + Mes + F_Ano + "." + Reporteq;

                                con.actualizar("UPDATE tb_txtis SET F_FolCon = '" + F_FolCon + "' WHERE " + FechaFol + " AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "'");
                                TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni "
                                        + "FROM  tb_txtis WHERE F_FolCon='" + F_FolCon + "' AND  F_Cveuni='" + F_CveUni + "' GROUP BY F_Cveart ORDER BY F_Cveart ASC");
                                while (TxtSecuenc.next()) {
                                    PzsReq = PzsReq + TxtSecuenc.getInt("F_Canreq");
                                    PzsSur = PzsSur + TxtSecuenc.getInt("F_Cansur");

                                    if (TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur") <= 0) {
                                        PzsNOSur = PzsNOSur + 0;
                                        CosNOSur = CosNOSur + 0;
                                    } else {
                                        PzsNOSur = PzsNOSur + (TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur"));
                                        CosNOSur = CosNOSur + ((TxtSecuenc.getInt("F_Canreq") - TxtSecuenc.getInt("F_Cansur")) * TxtSecuenc.getInt("F_CosUni"));
                                    }
                                    F_Cosmed = TxtSecuenc.getInt("F_Cansur") * TxtSecuenc.getDouble("F_CosUni");

                                    //valida ceaps
                                    if ((F_CveUni.equals("CSSA017114")) || (F_CveUni.equals("CSSA017153")) || (F_CveUni.equals("CSSA017154")) || (F_CveUni.equals("CSSA017109")) || (F_CveUni.equals("CSSA017126")) || (F_CveUni.equals("CSSA017151")) || (F_CveUni.equals("MCSSA000982")) || (F_CveUni.equals("CEAPSBEJ001"))) {
                                        //if (F_Surti.equals("001 ADMINISTRACION")){
                                        CosServ = 0;
                                        CosServSub = 0;
                                        /*}else{
                                    CosServ = 9.1;
                                    CosServSub = 7.84;
                                }*/
                                    } else {
                                        //if (F_Surti.equals("001 ADMINISTRACION")){
                                        CosServ = 0;
                                        CosServSub = 0;
                                        /*}else{
                                    CosServ = 4.74;
                                    CosServSub = 5.5;
                                }*/
                                    }

                                    F_CosServ = TxtSecuenc.getInt("F_Cansur") * CosServSub;
                                    if ((F_Idsur == 2) && (F_Cvesum == 2)) {
                                        F_IVA = (F_CosServ + F_Cosmed) * 0.16;
                                    } else {
                                        F_IVA = F_CosServ * 0.16;
                                    }

                                    F_Total = F_Cosmed + F_CosServ + F_IVA;

                                    NumReg = NumReg + 1;

                                    if (F_Idsur == 1) {
                                        F_Surti = "001 ADMINISTRACION";
                                        F_DesSur = "ADM.";
                                    } else {
                                        F_Surti = "002 VENTA";
                                        F_DesSur = "VTA.";
                                    }
                                    if (F_IdePro == 0) {
                                        F_Cober = "001 POBLACION ABIERTA";
                                        F_Descob = "P.A.";
                                    } else {
                                        F_Cober = "002 SEGURO POPULAR";
                                        F_Descob = "S.P.";
                                    }
                                    if (F_Cvesum == 1) {
                                        F_Sumi = "001 MEDICAMENTO";
                                        F_DesSum = "MED.";
                                    } else {
                                        F_Sumi = "002 CURACION";
                                        F_DesSum = "CUR.";
                                    }

                                    con.actualizar("INSERT INTO tb_imprepreqfarm VALUES (0,'" + NumReg + "','" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + PzsNOSur + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + F_Total + "','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }
                                con.actualizar("INSERT INTO tb_imprepreqfarm VALUES (0,'','','','" + PzsReq + "','" + PzsSur + "','" + PzsNOSur + "','0','" + CosMed + "','" + F_CosServ + "','" + IVA + "','" + Total + "','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");

                                //out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalfarm.jsp?Use=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");

                                PzsReq = 0;
                                PzsSur = 0;
                                PzsNOSur = 0;
                                CosNOSur = 0;
                                Total = 0;
                                F_CosServ = 0;
                                F_Cosmed = 0;
                                Total = 0;
                                IVA = 0;
                                NumReg = 0;
                                CosMed = 0;
                            }
                        }
                        out.println("<script>window.print('ReportesPuntos/ReporteHorizontalfarm.jsp?FolCon=" + F_FolCon + ")</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                }

            } else {
                out.println("<script>alert('Favor de ingresar FECHA')</script>");
                out.println("<script>window.history.back()</script>");
            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*String folio_sp = request.getParameter("sp_pac");
         System.out.println(folio_sp);
         out.println(folio_sp);*/
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
