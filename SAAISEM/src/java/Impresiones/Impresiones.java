/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impresiones;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;

/**
 * Impresión de folios
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Impresiones extends HttpServlet {

    java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        HttpSession sesion = request.getSession(true);
        String clave = "", descr = "";
        int ban1 = 0;
        int SumaMedReq = 0, SumaMedSur = 0, SumaMedReqT = 0, SumaMedSurT = 0;
        double MontoMed = 0.0, MontoTMed = 0.0, MontoMat = 0.0, MontoTMat = 0.0, Hoja = 0.0, Hoja2 = 0.0, TotalMonto = 0.0;
        String Unidad = "", Fecha = "", Direc = "", F_FecApl = "", F_Obs = "", F_Obs2 = "", DesV = "";
        int SumaMatReq = 0, SumaMatSur = 0, SumaMatReqT = 0, SumaMatSurT = 0, RegistroC = 0, Ban = 0, HojasC = 0, HojasR = 0, TotalReq = 0, TotalSur = 0, BanFolio = 0;

        ConectionDB con = new ConectionDB();
        //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();

        try {

            if (request.getParameter("accion").equals("impRemisMultples")) {
                con.conectar();

                String Copy = request.getParameter("Copy");
                String[] claveschk = request.getParameterValues("checkRemis");
                String remisionesReImp = "";

                if (!(Copy.equals(""))) {
                    //con.actualizar("DELETE FROM tb_folioimp WHERE F_User='" + sesion.getAttribute("nombre") + "'");
                    //con.actualizar("DELETE FROM tb_imprefolio WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                    for (int i = 0; i < claveschk.length; i++) {
                        System.out.println("claveschk: " + claveschk);

                        con.actualizar("DELETE FROM tb_imprefolio WHERE F_ClaDoc='" + claveschk[i] + "';");

                        ResultSet ObsFact = con.consulta("SELECT F_Obser FROM tb_obserfact WHERE F_IdFact='" + claveschk[i] + "' GROUP BY F_IdFact");
                        while (ObsFact.next()) {
                            F_Obs = ObsFact.getString(1);
                        }

                        ResultSet DatosFactMed = con.consulta("SELECT F.F_ClaCli,U.F_NomCli,U.F_Direc,F.F_ClaDoc,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,"
                                + "DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq,SUM(F.F_CantSur) AS F_CantSur,SUM(F.F_Costo) AS F_Costo,SUM(F.F_Monto) AS F_Monto,"
                                + "DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl "
                                + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                + "INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro "
                                + "WHERE F_ClaDoc='" + claveschk[i] + "' and F_TipMed='2504' and F_CantSur>0 and F_StsFact='A' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad ORDER BY F.F_ClaPro +0");
                        while (DatosFactMed.next()) {
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
                                    + "'" + DatosFactMed.getString(7) + "','" + DatosFactMed.getString(8) + "','" + DatosFactMed.getString(9) + "','" + DatosFactMed.getString(10) + "','" + DatosFactMed.getString(11) + "','" + DatosFactMed.getString(12) + "','" + DatosFactMed.getString(13) + "','" + F_Obs + "','" + DatosFactMed.getString(14) + "','0','" + sesion.getAttribute("nombre") + "','" + Copy + "',0)");
                        }
                        if (SumaMedSurT > 0) {
                            con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + claveschk[i] + "','" + Fecha + "','','SubTotal Medicamento (2504)','','','" + SumaMedReqT + "','" + SumaMedSurT + "','','" + MontoTMed + "','','" + F_FecApl + "','0','" + sesion.getAttribute("nombre") + "','" + Copy + "',0)");
                        }

                        ResultSet DatosFactMat = con.consulta("SELECT F.F_ClaCli,U.F_NomCli,U.F_Direc,F.F_ClaDoc,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt,F.F_ClaPro,M.F_DesPro,L.F_ClaLot,"
                                + "DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad, SUM(F.F_CantReq) AS F_CantReq,SUM(F.F_CantSur) AS F_CantSur,SUM(F.F_Costo) AS F_Costo,SUM(F.F_Monto) AS F_Monto,"
                                + "DATE_FORMAT(F_FecApl,'%d/%m/%Y') AS F_FecApl "
                                + "FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_Ubicacion=L.F_Ubica "
                                + "INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro "
                                + "WHERE F_ClaDoc='" + claveschk[i] + "' and F_TipMed='2505' and F_CantSur>0 and F_StsFact='A' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad ORDER BY F.F_ClaPro +0");
                        while (DatosFactMat.next()) {
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

                            con.actualizar("INSERT INTO tb_imprefolio VALUES('" + DatosFactMat.getString(1) + "','" + DatosFactMat.getString(2) + "','" + DatosFactMat.getString(3) + "','" + DatosFactMat.getString(4) + "','" + DatosFactMat.getString(5) + "','" + DatosFactMat.getString(6) + "',"
                                    + "'" + DatosFactMat.getString(7) + "','" + DatosFactMat.getString(8) + "','" + DatosFactMat.getString(9) + "','" + DatosFactMat.getString(10) + "','" + DatosFactMat.getString(11) + "','" + DatosFactMat.getString(12) + "','" + DatosFactMat.getString(13) + "','" + F_Obs + "','" + DatosFactMat.getString(14) + "','0','" + sesion.getAttribute("nombre") + "','" + Copy + "',0)");
                        }
                        if (SumaMatSurT > 0) {
                            con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + claveschk[i] + "','" + Fecha + "','','SubTotal Mat. Curación (2505)','','','" + SumaMatReqT + "','" + SumaMatSurT + "','','" + MontoTMat + "','','" + F_FecApl + "','0','" + sesion.getAttribute("nombre") + "','" + Copy + "',0)");
                        } else {
                            /*for(int x=0; x<4; x++){
                             con.actualizar("INSERT INTO tb_imprefolio VALUES('','','','"+claveschk[i]+"','','','','','','','','','','',0)");   
                             }*/
                        }
                        TotalReq = SumaMatReqT + SumaMedReqT;
                        TotalSur = SumaMedSurT + SumaMatSurT;
                        TotalMonto = MontoTMat + MontoTMed;

                        con.actualizar("INSERT INTO tb_imprefolio VALUES('','" + Unidad + "','" + Direc + "','" + claveschk[i] + "','" + Fecha + "','','TOTAL','','','" + TotalReq + "','" + TotalSur + "','','" + TotalMonto + "','','" + F_FecApl + "','0','" + sesion.getAttribute("nombre") + "','" + Copy + "',0)");

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

                        ResultSet Contare = con.consulta("SELECT COUNT(F_ClaDoc),F_Obs FROM tb_imprefolio WHERE F_ClaDoc='" + claveschk[i] + "'");
                        if (Contare.next()) {
                            RegistroC = Contare.getInt(1);
                        }

                        Hoja = RegistroC * 1.0 / 35;
                        Hoja2 = RegistroC / 35;
                        HojasC = (int) Hoja2 * 35;

                        HojasR = RegistroC - HojasC;

                        if ((HojasR > 0) && (HojasR <= 22)) {
                            Ban = 1;
                        } else {
                            Ban = 2;
                        }
                        con.actualizar("UPDATE tb_imprefolio SET F_Ban='" + Ban + "' WHERE F_ClaDoc='" + claveschk[i] + "';");

                        System.out.println("Re: " + RegistroC + " Ban: " + Ban + " Hoja2 " + Hoja2 + " HojaC " + HojasC + " HohasR " + HojasR);
                        Hoja = 0;

                        if (i == (claveschk.length - 1)) {
                            remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                            out.println("remisionesReImp:" + remisionesReImp);
                        } else {
                            remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                            out.println("remisionesReImp:" + remisionesReImp);
                        }
                    }
                    //out.println(" <script>window.open('reportes/multiplesRemis.jsp?User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    //out.println("<script>window.history.back()</script>");
                    ResultSet FolioBan = con.consulta("SELECT F_ClaDoc,F_Ban,F_Copy FROM tb_imprefolio WHERE F_User='" + sesion.getAttribute("nombre") + "' GROUP BY F_ClaDoc,F_Ban ORDER BY F_Ban+0,F_ClaDoc+0;");
                    while (FolioBan.next()) {
                        BanFolio = FolioBan.getInt(2);
                        if (BanFolio == 1) {
                            out.println(" <script>window.open('reportes/multiplesRemis1.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        } else if (BanFolio == 2) {
                            out.println(" <script>window.open('reportes/multiplesRemis12.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        }
                        BanFolio = 0;
                    }
                    out.println("Flios Imprime:" + remisionesReImp);
                } else {
                    out.println("<script>alert('Favor de Seleccionar No Copias')</script>");
                    out.println("<script>window.history.back()</script>");
                }

                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("ImpRelacion")) {
                con.conectar();
                int LargoF = 0;
                String FechaFol = "", ContFolio = "", MuestraFolio = "", FecMin = "", FecMax = "";
                String Radio = request.getParameter("radio1");
                String Folio1 = request.getParameter("folio11");
                String unidad = request.getParameter("unidad1");
                String unidad2 = request.getParameter("unidad2");
                String Folio2 = request.getParameter("folio21");
                String Fecha1 = request.getParameter("fecha_ini1");
                String Fecha2 = request.getParameter("fecha_fin1");
                String Impresora = request.getParameter("impresora");

                String QUni = "", QFolio = "", QFecha = "", Query = "";
                int ban = 0, ban2 = 0, ban3 = 0;
                if (unidad != "" && unidad2 != "") {
                    ban = 1;
                }
                if (Folio1 != "" && Folio2 != "") {
                    ban2 = 1;
                }
                if (Fecha1 != "" && Fecha2 != "") {
                    ban3 = 1;
                }
                if (ban == 1) {
                    QUni = " WHERE F_ClaCli BETWEEN '" + unidad + "' AND '" + unidad2 + "' ";
                }
                if (ban2 == 1) {
                    if (ban == 0) {
                        QFolio = " WHERE F_ClaDoc between '" + Folio1 + "' and '" + Folio2 + "' ";
                    } else {
                        QFolio = " AND F_ClaDoc between '" + Folio1 + "' and '" + Folio2 + "' ";
                    }
                }

                if (ban3 == 1) {
                    if (ban == 0 && ban2 == 0) {
                        QFecha = " WHERE F_FecEnt between '" + Fecha1 + "' and '" + Fecha2 + "' ";
                    } else {
                        QFecha = " AND F_FecEnt between '" + Fecha1 + "' and '" + Fecha2 + "' ";
                    }
                }

                Query = QUni + QFolio + QFecha;

                String remisionesReImp = "";
                System.out.println("Impresora:" + Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if (!(Impresora.equals(""))) {

                    con.actualizar("delete from tb_imprelacion where F_User='" + sesion.getAttribute("nombre") + "'");
                    ResultSet Folios = con.consulta("SELECT F_ClaDoc FROM tb_factura " + Query + " GROUP BY F_ClaDoc");
                    //ResultSet Folios = con.consulta("SELECT MIN(F_ClaDoc),MAX(F_ClaDoc) FROM tb_factura WHERE "+FechaFol+"");
                    while (Folios.next()) {
                        ContFolio = Folios.getString(1);
                        //Folio1 = Folios.getString(1);
                        //Folio2 = Folios.getString(2);
                        con.insertar("INSERT INTO tb_imprelacion VALUES (0,'" + Folios.getString(1) + "','" + sesion.getAttribute("nombre") + "')");
                        MuestraFolio = MuestraFolio + ContFolio + ",";
                    }
                    LargoF = MuestraFolio.length();
                    MuestraFolio = MuestraFolio.substring(0, LargoF - 1);
                    ResultSet Fechas = con.consulta("SELECT DATE_FORMAT(MIN(F_FecEnt),'%d/%m/%Y'),DATE_FORMAT(MAX(F_FecEnt),'%d/%m/%Y') FROM tb_factura WHERE F_ClaDoc IN ('" + MuestraFolio + "')");
                    if (Fechas.next()) {
                        FecMin = Fechas.getString(1);
                        FecMax = Fechas.getString(2);
                    }

                    out.println(" <script>window.open('reportes/ImprimeRelacion.jsp?FecMin=" + FecMin + "&FecMax=" + FecMax + "&Impresora=" + Impresora + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                    out.println("<script>window.history.back()</script>");
                    MuestraFolio = "";
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("recalendarizarRemis")) {
                con.conectar();
                try {
                    String[] claveschk = request.getParameterValues("checkRemis");
                    String remisionesReCal = "";
                    for (int i = 0; i < claveschk.length; i++) {
                        if (i == (claveschk.length - 1)) {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "'";
                        } else {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "',";
                        }
                    }
                    out.println(remisionesReCal);

                    con.insertar("update tb_factura set F_FecEnt = '" + request.getParameter("F_FecEnt") + "' where F_ClaDoc in (" + remisionesReCal + ")");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                con.cierraConexion();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
