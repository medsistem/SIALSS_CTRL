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
 * Generación de reporte global facturación txt isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */


public class ReporteGlobal extends HttpServlet {

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
            ResultSet TxtFolios = null;
            ResultSet TxtLote = null;
            ResultSet TxtLoteCont = null;
            ResultSet TxtLoteDesc = null;
            ResultSet TxtSec1 = null;
            ResultSet TxtSec2 = null;
            String Fecha1 = "", Fecha2 = "", FoliosTxt = "", F_ClaInt = "", F_ClaDyn = "", DesLoteTxt = "", LoteTxt = "", Caduci = "";
            int Folio = 0, CantiLote = 0, CantDesc = 0, TotaLote = 0, LargoCa = 0, LoteCont = 0;

            String F_FacGNKLAgr = "", Periodo = "", Secu1 = "", Secu2 = "", Secuencial = "", FolioCon = "", F_Cveuni = "";
            String F_Region = "", F_CveJur = "", F_Cvemun = "", F_CveLoc = "", F_CveUni = "", F_Cvepro = "", F_Fecsur = "";
            String F_FecIni = "", F_FecFin = "", F_FolCon = "", F_FolCon2 = "", F_Title = "", F_Surti = "", F_Cober = "", F_Sumi = "";
            String F_DesSur = "", F_Descob = "", F_DesSum = "", F_Dia2 = "";
            int F_Dia = 0, F_Dia1 = 0, F_Mes = 0, F_Mes1 = 0, F_Ano = 0, F_Ano1 = 0, NumReg = 0;
            int F_SecIni = 0, F_SecFin = 0, F_SecIniC = 0, F_SecFinC = 0, PzsReq = 0, PzsSur = 0, PzsNOSur = 0, vp_Reporte = 0, F_Idsur = 0, F_IdePro = 0, F_Cvesum = 0;
            double F_Cosmed = 0.0, CosServ = 0.0, CosServSub = 0.0, F_CosServ = 0.0, F_IVA = 0.0, F_Total = 0.0, IVA = 0.0, Total = 0.0, CosNOSur = 0.0;
            int PzsReqC = 0, PzsSurC = 0, PzsNOSurC = 0, Radio = 0, Radio2 = 0;
            double CosNOSurC = 0.0, TotalC = 0.0, F_CosServC = 0.0, F_CosmedC = 0.0, IVAC = 0.0, CosMed = 0.0;
            String F_DesRegion = "", F_DesJur = "", F_DesMun1 = "", F_DesLoc = "", F_DesUni = "", F_Contrato = "";
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);

            Fecha1 = request.getParameter("fecha_ini");
            Fecha2 = request.getParameter("fecha_fin");
            Radio = Integer.parseInt(request.getParameter("radio"));
            Radio2 = Integer.parseInt(request.getParameter("radio2"));

            if (Radio2 == 1) {////////////  REPORTE GLOBAL RURALES ///////////

                if (Radio == 1) {//////////  REPORTE GLOBAL  RURALES //////////

                    if ((Fecha1 != "") && (Fecha2 != "")) {
                        con.actualizar("delete from tb_imprepconglobal");
                        con.actualizar("delete from tb_imprepvalglobal");
                        con.actualizar("delete from tb_imprepreqglobal");

                        Txt = con.consulta("SELECT F_Contrato,F_Cvepro FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Contrato,F_Cvepro");
                        while (Txt.next()) {
                            F_Contrato = Txt.getString("F_Contrato");
                            F_Cvepro = Txt.getString("F_Cvepro");
                        }
                        if (F_Contrato.equals("")) {
                            F_Contrato = ".";
                        }

                        F_DesRegion = "";
                        F_DesUni = "";
                        F_DesJur = "";
                        //F_DesMun = F_Cvemun +""+ TxtDatos.getString(3);
                        F_DesMun1 = "";
                        F_DesLoc = "";

                        F_Dia = Integer.parseInt(Fecha2.substring(8, 10));
                        F_Mes = Integer.parseInt(Fecha2.substring(5, 7));
                        F_Ano = Integer.parseInt(Fecha2.substring(0, 4));

                        F_Dia1 = Integer.parseInt(Fecha1.substring(8, 10));
                        F_Mes1 = Integer.parseInt(Fecha1.substring(5, 7));
                        F_Ano1 = Integer.parseInt(Fecha1.substring(0, 4));

                        String Dia = "", Dia1 = "", Mes = "", Mes1 = "";
                        if (F_Dia < 10) {
                            Dia = "0" + F_Dia;
                        } else {
                            Dia = "" + F_Dia;
                        }

                        if (F_Dia1 < 10) {
                            Dia1 = "0" + F_Dia1;
                        } else {
                            Dia1 = "" + F_Dia1;
                        }
                        if (F_Mes < 10) {
                            Mes = "0" + F_Mes;
                        } else {
                            Mes = "" + F_Mes;
                        }
                        if (F_Mes1 < 10) {
                            Mes1 = "0" + F_Mes1;
                        } else {
                            Mes1 = "" + F_Mes1;
                        }

                        F_FecIni = Dia1 + "/" + Mes1 + "/" + F_Ano1;
                        F_FecFin = Dia + "/" + Mes + "/" + F_Ano;

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
                            F_FolCon = "R-" + F_Dia + Mes + F_Ano + "." + Reporte;
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(3);
                            }

                            TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni  FROM  tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Cveart,F_DesGen,F_CosUni");
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

                                if (PzsSur > 0) {
                                    con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Total + "','','','','','','','','','','','0','0','','0','','','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','0','0','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }
                            }

                            if (PzsSur > 0) {
                                con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + IVA + "','0','" + Total + "','','','','','','','','','','','0','0','','0','','','','','','','','" + F_FolCon + "','','','','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                //out.println(" <script>window.open('ReportesPuntos/ReporteVerticalImp.jsp?FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteVerticalGlobal.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                F_FacGNKLAgr = "R-" + Dia + Mes + F_Ano;
                                con.actualizar("INSERT INTO tb_imprepvalglobal VALUES (0,'" + F_DesSur + "','" + F_Descob + "','" + F_DesSum + "','ABIERTO','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + (CosMed + F_CosServ) + "','" + IVA + "','" + (CosMed + F_CosServ + IVA) + "','" + PzsNOSur + "','" + CosNOSur + "','" + F_DesUni + "','" + F_DesJur + "','" + F_Region + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");

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
                            }
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?Mpio="+F_DesMun1+"'</script>");
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"'</script>");

                            //out.println("<script>window.open='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'</script>");
                        }
                        if (PzsSurC > 0) {
                            con.actualizar("INSERT INTO tb_imprepvalglobal VALUES (0,'','','','','" + PzsReqC + "','" + PzsSurC + "','" + F_CosmedC + "','" + F_CosServC + "','" + TotalC + "','" + IVAC + "','" + (TotalC + IVAC) + "','" + PzsNOSurC + "','" + CosNOSurC + "','','','','','','" + F_FolCon + "','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                            PzsReqC = 0;
                            PzsSurC = 0;
                            F_CosmedC = 0;
                            F_CosServC = 0;
                            TotalC = 0;
                            IVAC = 0;
                            PzsNOSurC = 0;
                            CosNOSurC = 0;
                        }
                        // REPORTE CONCENTRADO

                        TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial) FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') ORDER BY F_Secuencial");
                        while (TxtSecuenc.next()) {
                            F_SecIniC = TxtSecuenc.getInt(1);
                            F_SecFinC = TxtSecuenc.getInt(2);
                        }
                        //F_Title = "REPORTE VALIDACION No "+F_FacGNKLAgr;

                        //out.println(" <script>window.open('ReportesPuntos/ReporteConcentradoImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIniC+"&F_SecFin="+F_SecFinC+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FacGNKLAgr+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        out.println(" <script>window.open('ReportesPuntos/ReporteConcentradoGlobal.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FacGNKLAgr + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
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

                            F_FolCon = "R-" + F_Dia + Mes + F_Ano + "." + Reporteq;
                            /*if (Reporteq > 1){
                                    con.actualizar("delete from tb_imprepreq");
                                    }*/
                            // con.actualizar("delete from tb_imprepval");
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial) FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(2);
                            }

                            TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni FROM  tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') group by F_Cveart,F_DesGen,F_CosUni");
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

                                if (PzsSur > 0) {
                                    con.actualizar("INSERT INTO tb_imprepreqglobal VALUES (0,'" + NumReg + "','" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + PzsNOSur + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + F_Total + "','" + F_DesUni + "','" + F_DesJur + "','" + F_Region + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','0','0','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate(),'')");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }
                            }
                            if (PzsSur > 0) {
                                con.actualizar("INSERT INTO tb_imprepreqglobal VALUES (0,'','','','" + PzsReq + "','" + PzsSur + "','" + PzsNOSur + "','0','" + CosMed + "','" + F_CosServ + "','" + IVA + "','" + Total + "','','','','','','" + F_FolCon + "','','','','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate(),'')");

                                //out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalGlobal.jsp?Use=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");

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
                        out.println("<script>window.print('ReportesPuntos/ReporteHorizontalGlobal.jsp)</script>");
                        out.println("<script>window.history.back()</script>");

                    } else {
                        out.println("<script>alert('Favor de Seleccionar Fechas')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else if (Radio == 2) {////// REPORTE GLOBAL LOTE RURALES ///////////

                    if ((Fecha1 != "") && (Fecha2 != "")) {
                        con.actualizar("delete from tb_imprepconglobal");
                        con.actualizar("delete from tb_imprepvalglobal");
                        con.actualizar("delete from tb_imprepreqglobal");

                        Txt = con.consulta("SELECT F_Contrato,F_Cvepro FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Contrato");
                        while (Txt.next()) {
                            F_Contrato = Txt.getString("F_Contrato");
                            F_Cvepro = Txt.getString("F_Cvepro");
                        }
                        if (F_Contrato.equals("")) {
                            F_Contrato = ".";
                        }

                        F_DesRegion = "";
                        F_DesUni = "";
                        F_DesJur = "";
                        //F_DesMun = F_Cvemun +""+ TxtDatos.getString(3);
                        F_DesMun1 = "";
                        F_DesLoc = "";

                        F_Dia = Integer.parseInt(Fecha2.substring(8, 10));
                        F_Mes = Integer.parseInt(Fecha2.substring(5, 7));
                        F_Ano = Integer.parseInt(Fecha2.substring(0, 4));

                        F_Dia1 = Integer.parseInt(Fecha1.substring(8, 10));
                        F_Mes1 = Integer.parseInt(Fecha1.substring(5, 7));
                        F_Ano1 = Integer.parseInt(Fecha1.substring(0, 4));

                        String Dia = "", Dia1 = "", Mes = "", Mes1 = "";
                        if (F_Dia < 10) {
                            Dia = "0" + F_Dia;
                        } else {
                            Dia = "" + F_Dia;
                        }
                        if (F_Dia1 < 10) {
                            Dia1 = "0" + F_Dia1;
                        } else {
                            Dia1 = "" + F_Dia1;
                        }
                        if (F_Mes < 10) {
                            Mes = "0" + F_Mes;
                        } else {
                            Mes = "" + F_Mes;
                        }
                        if (F_Mes1 < 10) {
                            Mes1 = "0" + F_Mes1;
                        } else {
                            Mes1 = "" + F_Mes1;
                        }

                        F_FecIni = Dia1 + "/" + Mes1 + "/" + F_Ano1;
                        F_FecFin = Dia + "/" + Mes + "/" + F_Ano;

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
                            F_FolCon = "R-" + F_Dia + Mes + F_Ano + "." + Reporte;
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(3);
                            }

                            TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni,F_ClaDyn  FROM  tb_txtis T INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Cveart,F_DesGen,F_CosUni");
                            while (TxtSecuenc.next()) {
                                PzsReq = PzsReq + TxtSecuenc.getInt("F_Canreq");
                                PzsSur = PzsSur + TxtSecuenc.getInt("F_Cansur");
                                F_ClaDyn = TxtSecuenc.getString("F_ClaDyn");

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

                                if (PzsSur > 0) {
                                    con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Total + "','','','','','','','','','','','0','0','','0','','" + F_ClaDyn + "','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','0','" + F_SecFin + "','0','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;

                                    TxtFolios = con.consulta("SELECT F_FacGNKL,F_Folios,F_Idsur,F_ClaInt,F_ClaDyn FROM tb_txtis T INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS WHERE F_Cveart='" + TxtSecuenc.getString("F_Cveart") + "' AND F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='')");
                                    while (TxtFolios.next()) {
                                        FoliosTxt = FoliosTxt + TxtFolios.getString(2);
                                        F_ClaInt = TxtFolios.getString(4);

                                    }
                                    LargoCa = FoliosTxt.length();
                                    FoliosTxt = FoliosTxt.substring(0, LargoCa - 1);
                                    TxtLoteCont = con.consulta("SELECT F.F_Lote,F.F_ClaPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur) AS F_CantSur "
                                            + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                            + "WHERE F.F_ClaDoc IN (" + FoliosTxt + ") AND F.F_ClaPro='" + F_ClaInt + "' AND F.F_StsFact <> 'C' AND L.F_Origen='" + F_Idsur + "' "
                                            + "GROUP BY F.F_Lote,F.F_ClaPro,L.F_ClaLot,L.F_FecCad ");
                                    while (TxtLoteCont.next()) {
                                        LoteCont++;
                                    }

                                    TxtLote = con.consulta("SELECT F.F_Lote,F.F_ClaPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur) AS F_CantSur "
                                            + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                            + "WHERE F.F_ClaDoc IN (" + FoliosTxt + ") AND F.F_ClaPro='" + F_ClaInt + "' AND F.F_StsFact <> 'C' AND L.F_Origen='" + F_Idsur + "' "
                                            + "GROUP BY F.F_Lote,F.F_ClaPro,L.F_ClaLot,L.F_FecCad ");
                                    while (TxtLote.next()) {
                                        LoteTxt = TxtLote.getString(3);
                                        CantiLote = TxtLote.getInt(5);
                                        Caduci = TxtLote.getString(4);
                                        DesLoteTxt = "Lote[" + LoteTxt + "]" + " Cad[" + Caduci + "]";
                                        LoteCont = LoteCont - 1;
                                        /*TxtLoteDesc = con.consulta("SELECT SUM(F_Can) FROM tb_txtiscantparcial WHERE F_Folio IN ("+FoliosTxt+") AND F_Lote='"+LoteTxt+"'");
                                     while(TxtLoteDesc.next()){
                                       CantDesc = TxtLoteDesc.getInt(1);
                                     }*/
                                        TotaLote = CantiLote - CantDesc;
                                        //con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','"+DesLoteTxt+"','','"+TotaLote+"','','','','','','','','','','','','','','','','0','0','','0','','','','','','','','"+F_FolCon+"','','','','','"+F_Cvepro+"','','','','','','"+sesion.getAttribute("nombre")+"',curdate())");
                                        //con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','"+DesLoteTxt+"','0','"+TotaLote+"','0','0','0','0','0','','','','','','','','','','','0','0','','0','','','','','','','','"+F_FolCon+"','','','','','"+F_Cvepro+"','','','','','','"+sesion.getAttribute("nombre")+"',curdate())");
                                        con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','" + DesLoteTxt + "','','" + TotaLote + "','','','','','','','','','','','','','','','','','','','','','','','','','','','" + F_FolCon + "','','','','','" + F_Cvepro + "','','','','','','" + sesion.getAttribute("nombre") + "',curdate())");
                                        if (LoteCont == 0) {
                                            FoliosTxt = "";
                                        }
                                    }

                                }
                            }

                            if (PzsSur > 0) {
                                con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + IVA + "','0','" + Total + "','','','','','','','','','','','0','0','','0','','','','','','','','" + F_FolCon + "','','','','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                //out.println(" <script>window.open('ReportesPuntos/ReporteVerticalImp.jsp?FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteVerticalGlobal.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                con.actualizar("INSERT INTO tb_imprepvalglobal VALUES (0,'" + F_DesSur + "','" + F_Descob + "','" + F_DesSum + "','ABIERTO','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + (CosMed + F_CosServ) + "','" + IVA + "','" + (CosMed + F_CosServ + IVA) + "','" + PzsNOSur + "','" + CosNOSur + "','" + F_DesUni + "','" + F_DesJur + "','" + F_Region + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");

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
                            }
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?Mpio="+F_DesMun1+"'</script>");
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"'</script>");

                            //out.println("<script>window.open='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'</script>");
                        }

                        out.println("<script>window.history.back()</script>");

                    } else {
                        out.println("<script>alert('Favor de Seleccionar Fechas')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else if (Radio == 3) {/////// EXPORTAR LOTE RURALES /////////

                    if ((Fecha1 != "") && (Fecha2 != "")) {
                        con.actualizar("delete from tb_imprepconglobalote");

                        F_Dia = Integer.parseInt(Fecha2.substring(8, 10));
                        F_Mes = Integer.parseInt(Fecha2.substring(5, 7));
                        F_Ano = Integer.parseInt(Fecha2.substring(0, 4));

                        F_Dia1 = Integer.parseInt(Fecha1.substring(8, 10));
                        F_Mes1 = Integer.parseInt(Fecha1.substring(5, 7));
                        F_Ano1 = Integer.parseInt(Fecha1.substring(0, 4));
                        String Dia1 = "", Dia = "", Mes1 = "", Mes = "";
                        if (F_Dia1 < 10) {
                            Dia1 = "0" + F_Dia1;
                        } else {
                            Dia1 = "" + F_Dia1;
                        }
                        if (F_Dia < 10) {
                            Dia = "0" + F_Dia;
                        } else {
                            Dia = "" + F_Dia;
                        }
                        if (F_Mes1 < 10) {
                            Mes1 = "0" + F_Mes1;
                        } else {
                            Mes1 = "" + F_Mes1;
                        }
                        if (F_Mes < 10) {
                            Mes = "0" + F_Mes;
                        } else {
                            Mes = "" + F_Mes;
                        }

                        F_FecIni = Dia1 + "/" + Mes1 + "/" + F_Ano1;
                        F_FecFin = Dia + "/" + Mes + "/" + F_Ano;
                        Periodo = F_FecIni + " al " + F_FecFin;

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

                            F_FolCon = "R-" + F_Dia + F_Mes + F_Ano + "." + Reporte;
                            TxtSecuenc = con.consulta("SELECT F_Cveart,DATE_FORMAT(F_Fecsur,'%d.%m.%Y') AS F_Fecsur,F_ClaDyn,F_ClaInt,F_FolCon FROM tb_txtis T LEFT JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS  WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Cveart,F_ClaDyn,F_ClaInt");
                            while (TxtSecuenc.next()) {
                                F_ClaInt = TxtSecuenc.getString("F_ClaInt");
                                F_ClaDyn = TxtSecuenc.getString("F_ClaDyn");
                                F_FolCon2 = TxtSecuenc.getString("F_FolCon");
                                F_Fecsur = TxtSecuenc.getString("F_Fecsur");

                                if (F_Idsur == 1) {
                                    F_Surti = "ADMINISTRACION";
                                    F_DesSur = "ADM.";
                                } else {
                                    F_Surti = "VENTA";
                                    F_DesSur = "VTA.";
                                }
                                if (F_IdePro == 0) {
                                    F_Cober = "POBLACION ABIERTA";
                                    F_Descob = "P.A.";
                                } else {
                                    F_Cober = "SEGURO POPULAR";
                                    F_Descob = "S.P.";
                                }
                                if (F_Cvesum == 1) {
                                    F_Sumi = "MEDICAMENTO";
                                    F_DesSum = "MED.";
                                } else {
                                    F_Sumi = "MAT. CURACION";
                                    F_DesSum = "CUR.";
                                }

                                TxtFolios = con.consulta("SELECT F_Folios FROM tb_txtis WHERE F_Cveart='" + TxtSecuenc.getString("F_Cveart") + "' AND F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='')");
                                while (TxtFolios.next()) {
                                    FoliosTxt = FoliosTxt + TxtFolios.getString(1);
                                }
                                LargoCa = FoliosTxt.length();
                                FoliosTxt = FoliosTxt.substring(0, LargoCa - 1);
                                TxtSec1 = con.consulta("SELECT MIN(F_Secuencial),Max(F_Secuencial),F_FolCon,F_Cveuni FROM tb_txtis WHERE F_Cveart='" + TxtSecuenc.getString("F_Cveart") + "' AND F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-0%' AND (F_Status <> 'C' OR F_Status='')");
                                if (TxtSec1.next()) {
                                    Secu1 = TxtSec1.getString(1);
                                    Secu2 = TxtSec1.getString(2);
                                    FolioCon = TxtSec1.getString(3);
                                    F_Cveuni = TxtSec1.getString(4);

                                }
                                Secuencial = Secu1 + "-" + Secu2;
                                TxtLoteCont = con.consulta("SELECT F.F_Lote,F.F_ClaPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur) AS F_CantSur "
                                        + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                        + "WHERE F.F_ClaDoc IN (" + FoliosTxt + ") AND F.F_ClaPro='" + F_ClaInt + "' AND F.F_StsFact <> 'C' AND L.F_Origen='" + F_Idsur + "' "
                                        + "GROUP BY F.F_Lote,F.F_ClaPro,L.F_ClaLot,L.F_FecCad ");
                                while (TxtLoteCont.next()) {
                                    LoteCont++;
                                }
                                TxtLote = con.consulta("SELECT F.F_Lote,F.F_ClaPro,L.F_ClaLot,DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur) AS F_CantSur "
                                        + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                        + "WHERE F.F_ClaDoc IN (" + FoliosTxt + ") AND F.F_ClaPro='" + F_ClaInt + "' AND F.F_StsFact <> 'C' AND L.F_Origen='" + F_Idsur + "' "
                                        + "GROUP BY F.F_Lote,F.F_ClaPro,L.F_ClaLot,L.F_FecCad ");
                                while (TxtLote.next()) {
                                    LoteTxt = TxtLote.getString(3);
                                    CantiLote = TxtLote.getInt(5);
                                    Caduci = TxtLote.getString(4);
                                    DesLoteTxt = "Lote[" + LoteTxt + "]" + " FecCad[" + Caduci + "]";
                                    LoteCont = LoteCont - 1;
                                    /*TxtLoteDesc = con.consulta("SELECT SUM(F_Can) FROM tb_txtiscantparcial WHERE F_Folio IN ("+FoliosTxt+") AND F_Lote='"+LoteTxt+"'");
                                     while(TxtLoteDesc.next()){
                                       CantDesc = TxtLoteDesc.getInt(1);
                                     }*/
                                    TotaLote = CantiLote - CantDesc;
                                    con.actualizar("INSERT INTO tb_imprepconglobalote VALUES (0,'" + F_Fecsur + "','" + F_Fecsur + "','" + F_ClaDyn + "','" + TotaLote + "','" + LoteTxt + "','" + Periodo + "','" + Secuencial + "','" + FolioCon + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Cveuni + "','" + F_ClaInt + "','" + F_FolCon + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    if (LoteCont == 0) {
                                        FoliosTxt = "";
                                    }
                                    TotaLote = 0;
                                }
                            }
                        }
                        FoliosFact = con.consulta("SELECT F_Reporte FROM tb_imprepconglobalote WHERE F_Usuario='" + sesion.getAttribute("nombre") + "' AND F_Date=CURDATE() GROUP BY F_Reporte");
                        while (FoliosFact.next()) {
                            System.out.println("Reporte:" + FoliosFact.getString(1));
                            out.println(" <script>window.open('ReportesPuntos/ExportarGlobal.jsp?Folio=" + FoliosFact.getString(1) + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        }
                        out.println("<script>window.history.back()</script>");

                    } else {
                        out.println("<script>alert('Favor de Seleccionar Fechas')</script>");
                        out.println("<script>window.history.back()</script>");
                    }

                }

                ///////////////////////// REPORTE FARMACIA ///////////////////////
            } else if (Radio2 == 2) {

                if (Radio == 1) { ///// REPORTE GLOBAL FARMACIA /////////
                    if ((Fecha1 != "") && (Fecha2 != "")) {
                        con.actualizar("delete from tb_imprepconglobal");
                        con.actualizar("delete from tb_imprepvalglobal");
                        con.actualizar("delete from tb_imprepreqglobal");

                        Txt = con.consulta("SELECT F_Contrato FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Contrato");
                        while (Txt.next()) {
                            F_Contrato = Txt.getString("F_Contrato");
                        }
                        if (F_Contrato.equals("")) {
                            F_Contrato = ".";
                        }

                        F_DesRegion = "";
                        F_DesUni = "";
                        F_DesJur = "";
                        //F_DesMun = F_Cvemun +""+ TxtDatos.getString(3);
                        F_DesMun1 = "";
                        F_DesLoc = "";

                        F_Dia = Integer.parseInt(Fecha2.substring(8, 10));
                        F_Mes = Integer.parseInt(Fecha2.substring(5, 7));
                        F_Ano = Integer.parseInt(Fecha2.substring(0, 4));

                        F_Dia1 = Integer.parseInt(Fecha1.substring(8, 10));
                        F_Mes1 = Integer.parseInt(Fecha1.substring(5, 7));
                        F_Ano1 = Integer.parseInt(Fecha1.substring(0, 4));

                        String Dia = "", Dia1 = "", Mes = "", Mes1 = "";
                        if (F_Dia < 10) {
                            Dia = "0" + F_Dia;
                        } else {
                            Dia = "" + F_Dia;
                        }

                        if (F_Dia1 < 10) {
                            Dia1 = "0" + F_Dia1;
                        } else {
                            Dia1 = "" + F_Dia1;
                        }
                        if (F_Mes < 10) {
                            Mes = "0" + F_Mes;
                        } else {
                            Mes = "" + F_Mes;
                        }
                        if (F_Mes1 < 10) {
                            Mes1 = "0" + F_Mes1;
                        } else {
                            Mes1 = "" + F_Mes1;
                        }

                        F_FecIni = Dia1 + "/" + Mes1 + "/" + F_Ano1;
                        F_FecFin = Dia + "/" + Mes + "/" + F_Ano;

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
                            F_FolCon = "R-" + F_Dia + F_Mes + F_Ano + "." + Reporte;
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(3);
                            }

                            TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni  FROM  tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND (F_Status <> 'C' OR F_Status='') GROUP BY F_Cveart,F_DesGen,F_CosUni");
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

                                if (PzsSur > 0) {
                                    con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Total + "','','','','','','','','','','','0','0','','0','','','" + F_DesUni + "','" + F_DesJur + "','" + F_DesRegion + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }
                            }

                            if (PzsSur > 0) {
                                con.actualizar("INSERT INTO tb_imprepconglobal VALUES ('','','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + IVA + "','0','" + Total + "','','','','','','','','','','','0','0','','0','','','','','','','','" + F_FolCon + "','','','','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                                //out.println(" <script>window.open('ReportesPuntos/ReporteVerticalImp.jsp?FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                out.println(" <script>window.open('ReportesPuntos/ReporteVerticalGlobal.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FolCon + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                F_FacGNKLAgr = "R-" + Dia + Mes + F_Ano;
                                con.actualizar("INSERT INTO tb_imprepvalglobal VALUES (0,'" + F_DesSur + "','" + F_Descob + "','" + F_DesSum + "','ABIERTO','" + PzsReq + "','" + PzsSur + "','" + CosMed + "','" + F_CosServ + "','" + (CosMed + F_CosServ) + "','" + IVA + "','" + (CosMed + F_CosServ + IVA) + "','" + PzsNOSur + "','" + CosNOSur + "','" + F_DesUni + "','" + F_DesJur + "','" + F_Region + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");

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
                            }
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?Mpio="+F_DesMun1+"'</script>");
                            //out.println("<script>window.location='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"'</script>");

                            //out.println("<script>window.open='ReportesPuntos/ReporteVertical.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'</script>");
                        }
                        if (PzsSurC > 0) {
                            con.actualizar("INSERT INTO tb_imprepvalglobal VALUES (0,'','','','','" + PzsReqC + "','" + PzsSurC + "','" + F_CosmedC + "','" + F_CosServC + "','" + TotalC + "','" + IVAC + "','" + (TotalC + IVAC) + "','" + PzsNOSurC + "','" + CosNOSurC + "','','','','','','" + F_FolCon + "','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate())");
                            PzsReqC = 0;
                            PzsSurC = 0;
                            F_CosmedC = 0;
                            F_CosServC = 0;
                            TotalC = 0;
                            IVAC = 0;
                            PzsNOSurC = 0;
                            CosNOSurC = 0;
                        }
                        // REPORTE CONCENTRADO

                        TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' ORDER BY F_Secuencial");
                        while (TxtSecuenc.next()) {
                            F_SecIniC = TxtSecuenc.getInt(1);
                            F_SecFinC = TxtSecuenc.getInt(3);
                        }
                        //F_Title = "REPORTE VALIDACION No "+F_FacGNKLAgr;

                        //out.println(" <script>window.open('ReportesPuntos/ReporteConcentradoImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIniC+"&F_SecFin="+F_SecFinC+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FacGNKLAgr+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        out.println(" <script>window.open('ReportesPuntos/ReporteConcentradoGlobal.jsp?User=" + sesion.getAttribute("nombre") + "&FolCon=" + F_FacGNKLAgr + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
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

                            F_FolCon = "R-" + F_Dia + F_Mes + F_Ano + "." + Reporteq;
                            /*if (Reporteq > 1){
                                    con.actualizar("delete from tb_imprepreq");
                                    }*/
                            // con.actualizar("delete from tb_imprepval");
                            TxtSecuenc = con.consulta("SELECT MIN(F_Secuencial),MAX(F_Secuencial),((MIN(F_Secuencial)+COUNT(F_Secuencial))-1) AS F_Final FROM tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' ORDER BY F_Secuencial");
                            while (TxtSecuenc.next()) {
                                F_SecIni = TxtSecuenc.getInt(1);
                                F_SecFin = TxtSecuenc.getInt(3);
                            }

                            TxtSecuenc = con.consulta("SELECT F_Cveart,F_DesGen,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,F_CosUni FROM  tb_txtis WHERE F_fecsur between '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_Idsur = '" + F_Idsur + "' AND F_IdePro = '" + F_IdePro + "' AND F_Cvesum = '" + F_Cvesum + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND (F_Status <> 'C' OR F_Status='') group by F_Cveart,F_DesGen,F_CosUni");
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

                                if (PzsSur > 0) {
                                    con.actualizar("INSERT INTO tb_imprepreqglobal VALUES (0,'" + NumReg + "','" + TxtSecuenc.getString("F_Cveart") + "','" + TxtSecuenc.getString("F_DesGen") + "','" + TxtSecuenc.getString("F_Canreq") + "','" + TxtSecuenc.getString("F_Cansur") + "','" + PzsNOSur + "','" + TxtSecuenc.getString("F_CosUni") + "','" + F_Cosmed + "','" + F_CosServ + "','" + F_IVA + "','" + F_Total + "','" + F_DesUni + "','" + F_DesJur + "','" + F_Region + "','" + F_DesMun1 + "','" + F_DesLoc + "','" + F_FolCon + "','" + F_FecIni + "','" + F_FecFin + "','" + F_SecIni + "','" + F_SecFin + "','" + F_Cvepro + "','" + F_Surti + "','" + F_Cober + "','" + F_Sumi + "','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate(),'')");
                                    IVA = IVA + F_IVA;
                                    Total = Total + F_Total;
                                    CosServ = CosServ + F_CosServ;
                                    CosMed = CosMed + F_Cosmed;
                                }
                            }
                            if (PzsSur > 0) {
                                con.actualizar("INSERT INTO tb_imprepreqglobal VALUES (0,'','','','" + PzsReq + "','" + PzsSur + "','" + PzsNOSur + "','0','" + CosMed + "','" + F_CosServ + "','" + IVA + "','" + Total + "','','','','','','" + F_FolCon + "','','','','','','','','','" + F_Contrato + "','" + F_FacGNKLAgr + "','" + sesion.getAttribute("nombre") + "',curdate(),'')");

                                //out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalImp.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                                //aquí out.println(" <script>window.open('ReportesPuntos/ReporteHorizontalGlobal.jsp?Use="+sesion.getAttribute("nombre")+"&FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
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
                        out.println("<script>window.print('ReportesPuntos/ReporteHorizontalGlobal.jsp)</script>");
                        out.println("<script>window.history.back()</script>");

                    } else {
                        out.println("<script>alert('Favor de Seleccionar Fechas')</script>");
                        out.println("<script>window.history.back()</script>");
                    }

                } else if (Radio == 2) {////// REPORTE GLOBAL LOTE FARMACIA ///////////
                    out.println("<script>window.history.back()</script>");

                } else if (Radio == 3) {
                    out.println("<script>window.history.back()</script>");
                }
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
