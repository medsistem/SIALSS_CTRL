package ReportesPuntos;

import ExportarTxt.LogTxt;
import conn.ConectionDB;
//import conn.ConnectionBDCeaps;
//import conn.ConnectionSQLIsem;
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
 * Reporte secuencial por farmacia facturación isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ReporteSecuencialFarm extends HttpServlet {

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
            ResultSet FolFact = null;
            ResultSet ConCeaps = null;
            ResultSet ContratoCli = null;
            ResultSet FolioAgr = null;
            ResultSet DatosUni = null;
            ResultSet DatosFact = null;
            ResultSet UltSecu = null;
            ResultSet TxT = null;
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
//            ConnectionBDCeaps ceaps = new ConnectionBDCeaps();
//            ConnectionSQLIsem ISEM = new ConnectionSQLIsem();
            con.conectar();
//            ceaps.conectar();
//            ISEM.conectar();
            HttpSession sesion = request.getSession(true);
            String fecha_ini = request.getParameter("fecha_ini");
            String Dife = request.getParameter("radio");
            String fecha_fin = request.getParameter("fecha_fin");
            String BaseDatos = request.getParameter("radioB");
            String Sistemas = request.getParameter("radioS");
            String F_Cliente = "", FolAgr = "", F_ClaDoc = "", F_ClaDoc2 = "", F_FecEnt = "", F_Fecsur = "", F_FacGNKLAgr = "", F_FolAgr = "", F_DesMedIS = "", F_Folios = "", vm_ClaArtSum = "", vm_ClaArtSum2 = "";
            String F_Origen = "", vp_AdmVen = "", F_IdOrg = "", F_SPArtIS = "", F_IdePro = "", F_Espacio = "", LogLimpiar = "", LogArticulo = "", LogUnidad = "";
            int CantidSum = 0, CantidReqSum = 0, F_Secuencial = 0, F_Long = 0, F_UltSecuencial = 0, F_GNKLAgr = 0, F_GNKLAgr2 = 0, ContPres = 0;
            String Contrato = "", CveCli = "", F_Agr = "", F_LAgr = "", Fecha = "", QuerySql = "", Presentacion = "";
            double F_PreVenIS = 0.0;
            LogTxt Log = new LogTxt();
            if ((fecha_ini != "") && (fecha_fin != "")) {
                try {
                    // con.actualizar("Delete From tb_txtis where F_Fecsur >= '"+fecha_ini+"' AND F_FacGNKLAgr LIKE 'AG-F%'");  // Borra los registros de la tabla de acuerdo a la fecha                       
                    if (BaseDatos.equals("SQL")) {/*BASE DE DATOS SQL*/

                        if (Sistemas.equals("gnklite")) {/*SISTEMA GNKLITE SQL*/
//                            ContratoCli = ISEM.consulta("select F_Contrato,F_Cvepro from TB_TXTIS group by f_contrato,F_Cvepro");
                            if (ContratoCli.next()) {
                                Contrato = ContratoCli.getString(1);
                                CveCli = ContratoCli.getString(2);
                            }
                            //con.actualizar("Delete From tb_txtfarmacia");
                            con.actualizar("Delete From tb_facagr");  // limpia tabla

                            FolFact = con.consulta("SELECT F_FecEnt,F_ClaDoc,F_ClaCli FROM tb_txtfarmacia WHERE F_FecEnt BETWEEN '" + fecha_ini + "'  "
                                    + "AND '" + fecha_fin + "' GROUP BY F_FecEnt,F_ClaDoc,F_ClaCli  ORDER BY F_FecEnt,F_ClaDoc,F_ClaCli ");
                            while (FolFact.next()) {

                                con.actualizar("insert into tb_facagr values ('" + FolFact.getString(2) + "','" + FolFact.getString(3) + "','" + FolFact.getString(1) + "','')");

                            }
                            //TxT = ISEM.consulta("SELECT F_FacGNKLAgr FROM tb_txtis LIMIT 0,1");
//                            TxT = ISEM.consulta("select top(1) F_Secuencial,F_FacGNKLAgr from TB_TXTIS order by F_Secuencial desc");
                            while (TxT.next()) {
                                F_Agr = TxT.getString("F_FacGNKLAgr");
                            }
                            F_LAgr = F_Agr.substring(0, 4);
                            if (F_LAgr.equals("AG-0")) {
                                F_Agr = F_Agr.substring(3, 10);
                            } else if (F_LAgr.equals("AG-F")) {
                                F_Agr = F_Agr.substring(4, 11);
                            }
                            F_GNKLAgr = Integer.parseInt(F_Agr);
                            F_GNKLAgr2 = F_GNKLAgr;
                            FolFact = con.consulta("SELECT F_Fol,F_Cli,F_FeE FROM tb_facagr group by F_FeE ORDER BY F_FeE,F_Fol,F_Cli ASC");
                            while (FolFact.next()) {
                                F_Cliente = FolFact.getString(2);
                                F_ClaDoc = FolFact.getString(1);
                                F_FecEnt = FolFact.getString(3);
                                F_GNKLAgr = F_GNKLAgr + 1;
                                //obtiene los datos de la unidad
                                DatosUni = con.consulta("Select * FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                                        + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                                        + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'");
                                while (DatosUni.next()) {
                                    while (F_GNKLAgr2 > 0) {
                                        F_GNKLAgr2 = F_GNKLAgr2 / 10;
                                        F_Long += 1;
                                    }
                                    //F_Long = F_GNKLAgr;
                                    System.out.println("Longtud:" + F_Long);
                                    for (int E = F_Long; E < 7; E++) {
                                        F_Espacio = F_Espacio + "0";
                                    }
                                    //FolAgr = "AG-" + F_ClaDoc.replace(' ','0');
                                    FolAgr = "AG-F" + F_Espacio + F_GNKLAgr;
                                    System.out.println("FolAgr:" + FolAgr);
                                    // actualiza el campo de folio ag y médico
                                    con.actualizar("update tb_facagr set F_FolAgr='" + FolAgr + "' where F_FeE = '" + F_FecEnt + "' "
                                            + "and F_Cli in ('" + DatosUni.getString(3) + "','" + DatosUni.getString(4) + "','" + DatosUni.getString(5) + "','" + DatosUni.getString(6) + "','" + DatosUni.getString(7) + "',"
                                            + "'" + DatosUni.getString(14) + "','" + DatosUni.getString(15) + "','" + DatosUni.getString(16) + "','" + DatosUni.getString(17) + "','" + DatosUni.getString(18) + "') and F_FolAgr=''");
                                    F_Espacio = "";
                                }

                            }
                            // obtiene el último secuencial registrado

//                            UltSecu = ISEM.consulta("Select top 1 F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC");
                            if (UltSecu.next()) {
                                F_UltSecuencial = UltSecu.getInt(1);
                                F_Fecsur = UltSecu.getString(2);
                                F_FacGNKLAgr = UltSecu.getString(3);
                            }
                            F_Secuencial = F_UltSecuencial;
                            ///----------------//////
//                            TxT = ISEM.consulta("SELECT TOP(1) * FROM tb_txtis");

                            // Valida datos de la factura
                            LogLimpiar = Log.LogLimpiar();
                            FolFact = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr");
                            while (FolFact.next()) {
                                DatosFact = con.consulta("Select F_ClaCli,F_ClaPro From tb_txtfarmacia f, tb_facagr a WHERE f.F_ClaDoc = a.F_Fol AND F_FolAgr = '" + FolFact.getString(1) + "'");
                                while (DatosFact.next()) {
                                    LogArticulo = Log.LogErrorArticulo(DatosFact.getString(2));
                                    LogUnidad = Log.LogErrorUnidad(DatosFact.getString(1));
                                }
                            }
                            ////------------------//////
                            ///-------------Obtiene el folio AGR ----

                            FolioAgr = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr ASC");
                            while (FolioAgr.next()) {
                                F_FolAgr = FolioAgr.getString(1);

                                if (Dife.equals("sin")) {
                                    //Secuencial Sin Diferencias///
                                    DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                            + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                            + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                            + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                            + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                            + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                            + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                            + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                            + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                            + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur > 0 "
                                            + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                            + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                            + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                                } else {
                                    //Secuencial Con Diferencias///
                                    DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                            + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                            + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                            + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                            + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                            + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                            + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                            + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                            + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                            + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur >= 0 "
                                            + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                            + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                            + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                                }
                                while (DatosFact.next()) {
                                    Presentacion = DatosFact.getString("F_PreArtIS");
                                    ContPres = Presentacion.length();
                                    if (ContPres > 50) {
                                        Presentacion = Presentacion.substring(0, 49);
                                    }
                                    F_Origen = DatosFact.getString("F_Origen");
                                    F_SPArtIS = DatosFact.getString("F_SPArtIS");
                                    if (F_Origen.equals("1")) {
                                        F_IdOrg = "1";
                                        F_PreVenIS = 0;
                                    } else {
                                        F_IdOrg = "2";
                                        F_PreVenIS = DatosFact.getDouble("F_PreVenIS");
                                    }
                                    if (F_SPArtIS.equals("1")) {
                                        F_IdePro = "1";
                                    } else {
                                        F_IdePro = "0";
                                    }

                                    F_LAgr = F_FolAgr.substring(0, 4);
                                    if (F_LAgr.equals("AG-0")) {
                                        F_Agr = F_FolAgr.substring(3, 10);
                                    } else if (F_LAgr.equals("AG-F")) {
                                        F_Agr = F_FolAgr.substring(4, 11);
                                    }
                                    F_GNKLAgr = Integer.parseInt(F_Agr);
                                    Fecha = df2.format(df.parse(DatosFact.getString("F_FecEnt")));
                                    F_Secuencial = F_Secuencial + 1;

                                    /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                                    //con.actualizar("INSERT INTO tb_txtis VALUES ('"+F_Secuencial+"','"+CveCli+"','"+DatosFact.getString("F_MunUniIS")+"','"+DatosFact.getString("F_LocUniIS")+"','"+DatosFact.getString("F_JurUniIS")+"','"+DatosFact.getString("F_ClaUniIS")+"','9999','9999','"+DatosFact.getString("F_MedUniIS")+"','"+DatosFact.getString("F_SUMArtIS")+"','10','"+DatosFact.getString("F_ClaPac")+"','"+DatosFact.getString("F_ClaArtIS")+"','"+DatosFact.getString("F_PreArtIS")+"','"+F_PreVenIS+"','"+DatosFact.getString("F_UnidadReq")+"','"+DatosFact.getString("F_Unidad")+"','0','"+DatosFact.getString("F_FecEnt")+"','"+DatosFact.getString("F_NomPac")+"','"+DatosFact.getString("F_APat")+"','"+DatosFact.getString("F_AMat")+"','"+DatosFact.getString("F_Edad")+"','"+DatosFact.getString("F_Sexo")+"','"+DatosFact.getString("F_DesArtIS")+"','"+F_IdOrg+"','','"+DatosFact.getString("F_DesMedIS")+"','2','"+DatosFact.getString("F_ClaDoc")+"',curdate(),'"+F_IdePro+"','"+DatosFact.getString("F_RegUniIS")+"','0','"+F_GNKLAgr+"','','','','','"+F_FolAgr+"','','','"+DatosFact.getString("F_Unidad")+"','"+DatosFact.getString("F_ClaLot")+"','"+Contrato+"')");
                                    QuerySql = "INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','" + DatosFact.getString("F_ClaPac") + "','" + DatosFact.getString("F_ClaArtIS") + "','" + Presentacion + "','" + F_PreVenIS + "','" + DatosFact.getString("F_UnidadReq") + "','" + DatosFact.getString("F_Unidad") + "','0','" + Fecha + "','" + DatosFact.getString("F_NomPac") + "','" + DatosFact.getString("F_APat") + "','" + DatosFact.getString("F_AMat") + "','" + DatosFact.getString("F_Edad") + "','" + DatosFact.getString("F_Sexo") + "','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',CONVERT (char(10), getdate(), 103),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + F_GNKLAgr + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','" + DatosFact.getString("F_ClaLot") + "','" + Contrato + "')";
                                    System.out.println(QuerySql);
//                                    ISEM.actualizar(QuerySql);

                                    // *** FIN *** //
                                }

                                FolFact = con.consulta("SELECT DISTINCT F_ClaDoc FROM tb_txtfarmacia WHERE F_ClaDoc IN (SELECT F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CanSur > 0");
                                while (FolFact.next()) {
                                    F_ClaDoc = FolFact.getString(1);
                                    F_Folios = F_Folios + F_ClaDoc.replace(" ", "") + ",";
                                }

                                /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                                //con.actualizar("UPDATE tb_txtis SET F_Folios='"+F_Folios+"' WHERE F_FacGNKLAgr='"+F_FolAgr+"'");
//                                ISEM.actualizar("UPDATE tb_txtis SET F_Folios='" + F_Folios + "' WHERE F_FacGNKLAgr='" + F_FolAgr + "'");
                                F_Folios = "";
                                F_Secuencial = F_Secuencial;

                                // *** FIN *** //
                            }
                            F_Secuencial = 0;
                            out.println("<script>alert('Secuencial Generado Correctamente')</script>");
                            out.println("<script>window.location.href = 'SecuencialFarm.jsp';</script>");

                            /**
                             * ****************************************FIN
                             * SISTEMA GNKLITE
                             * SQL*****************************************
                             */
                        } else {/*SISTEMA SCR SIALSS_CTRL SQL*/

//                            ContratoCli = ISEM.consulta("select F_Contrato,F_Cvepro from TB_TXTIS group by f_contrato,F_Cvepro");
                            if (ContratoCli.next()) {
                                Contrato = ContratoCli.getString(1);
                                CveCli = ContratoCli.getString(2);
                            }
                            con.actualizar("Delete From tb_txtfarmacia");
                            con.actualizar("Delete From tb_facagr");  // limpia tabla

//                            ConCeaps = ceaps.consulta("SELECT * FROM facturacion_txt WHERE fec_sur BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "'");
//                            while (ConCeaps.next()) {
//
//                                con.insertar("insert into tb_txtfarmacia values(0,'" + ConCeaps.getString(1) + "','" + ConCeaps.getString(2) + "','" + ConCeaps.getString(3) + "','" + ConCeaps.getString(4) + "','" + ConCeaps.getString(5) + "',"
//                                        + "'" + ConCeaps.getString(6) + "','" + ConCeaps.getString(7) + "','" + ConCeaps.getString(8) + "','" + ConCeaps.getString(9) + "','" + ConCeaps.getString(10) + "','" + ConCeaps.getString(11) + "','" + ConCeaps.getString(12) + "',"
//                                        + "'" + ConCeaps.getString(13) + "','" + ConCeaps.getString(14) + "','" + ConCeaps.getString(15) + "','" + ConCeaps.getString(16) + "','" + ConCeaps.getString(17) + "')");
//                            }

                            // consulta tabla factura para obtener la fecha, folio y cliente
                            //FolFact = con.consulta("Select F_FecEnt, F_ClaDoc, F_ClaCli  FROM tb_factura WHERE F_FecEnt BETWEEN '"+df.format(df2.parse(fecha_ini))+"'  "
                            //+ "AND '"+df.format(df2.parse(fecha_fin))+"'  AND F_TipDoc <> 'D' AND F_StsFac <> 'C' GROUP BY F_FecEnt, F_ClaDoc, F_ClaCli  ORDER BY F_FecEnt, F_ClaDoc, F_ClaCli ");
                            FolFact = con.consulta("SELECT F_FecEnt,F_ClaDoc,F_ClaCli FROM tb_txtfarmacia WHERE F_FecEnt BETWEEN '" + fecha_ini + "'  "
                                    + "AND '" + fecha_fin + "' GROUP BY F_FecEnt,F_ClaDoc,F_ClaCli  ORDER BY F_FecEnt,F_ClaDoc,F_ClaCli ");
                            while (FolFact.next()) {

                                con.actualizar("insert into tb_facagr values ('" + FolFact.getString(2) + "','" + FolFact.getString(3) + "','" + FolFact.getString(1) + "','')");

                            }
                            //TxT = con.consulta("SELECT F_FacGNKLAgr FROM tb_txtis LIMIT 0,1");
//                            TxT = ISEM.consulta("select top(1) F_Secuencial,F_FacGNKLAgr from TB_TXTIS order by F_Secuencial desc");
                            while (TxT.next()) {
                                F_Agr = TxT.getString("F_FacGNKLAgr");
                            }
                            F_LAgr = F_Agr.substring(0, 4);
                            if (F_LAgr.equals("AG-0")) {
                                F_Agr = F_Agr.substring(3, 10);
                            } else if (F_LAgr.equals("AG-F")) {
                                F_Agr = F_Agr.substring(4, 11);
                            }
                            F_GNKLAgr = Integer.parseInt(F_Agr);
                            F_GNKLAgr2 = F_GNKLAgr;
                            FolFact = con.consulta("SELECT F_Fol,F_Cli,F_FeE FROM tb_facagr group by F_FeE ORDER BY F_FeE,F_Fol,F_Cli ASC");
                            while (FolFact.next()) {
                                F_Cliente = FolFact.getString(2);
                                F_ClaDoc = FolFact.getString(1);
                                F_FecEnt = FolFact.getString(3);
                                F_GNKLAgr = F_GNKLAgr + 1;
                                //obtiene los datos de la unidad
                                DatosUni = con.consulta("Select * FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                                        + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                                        + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'");
                                while (DatosUni.next()) {
                                    while (F_GNKLAgr2 > 0) {
                                        F_GNKLAgr2 = F_GNKLAgr2 / 10;
                                        F_Long += 1;
                                    }
                                    //F_Long = F_GNKLAgr;
                                    System.out.println("Longtud:" + F_Long);
                                    for (int E = F_Long; E < 7; E++) {
                                        F_Espacio = F_Espacio + "0";
                                    }
                                    //FolAgr = "AG-" + F_ClaDoc.replace(' ','0');
                                    FolAgr = "AG-F" + F_Espacio + F_GNKLAgr;
                                    System.out.println("FolAgr:" + FolAgr);
                                    // actualiza el campo de folio ag y médico
                                    con.actualizar("update tb_facagr set F_FolAgr='" + FolAgr + "' where F_FeE = '" + F_FecEnt + "' "
                                            + "and F_Cli in ('" + DatosUni.getString(3) + "','" + DatosUni.getString(4) + "','" + DatosUni.getString(5) + "','" + DatosUni.getString(6) + "','" + DatosUni.getString(7) + "',"
                                            + "'" + DatosUni.getString(14) + "','" + DatosUni.getString(15) + "','" + DatosUni.getString(16) + "','" + DatosUni.getString(17) + "','" + DatosUni.getString(18) + "') and F_FolAgr=''");
                                    F_Espacio = "";
                                }

                            }
                            // obtiene el último secuencial registrado
                            //UltSecu = con.consulta("Select F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC LIMIT 0,1");
//                            UltSecu = ISEM.consulta("Select top 1 F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC");
                            if (UltSecu.next()) {
                                F_UltSecuencial = UltSecu.getInt(1);
                                F_Fecsur = UltSecu.getString(2);
                                F_FacGNKLAgr = UltSecu.getString(3);
                            }
                            F_Secuencial = F_UltSecuencial;
                            ///----------------//////
//                            TxT = ISEM.consulta("SELECT TOP(1) * FROM tb_txtis");

                            // Valida datos de la factura
                            LogLimpiar = Log.LogLimpiar();
                            FolFact = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr");
                            while (FolFact.next()) {
                                DatosFact = con.consulta("Select F_ClaCli,F_ClaPro From tb_txtfarmacia f, tb_facagr a WHERE f.F_ClaDoc = a.F_Fol AND F_FolAgr = '" + FolFact.getString(1) + "'");
                                while (DatosFact.next()) {
                                    LogArticulo = Log.LogErrorArticulo(DatosFact.getString(2));
                                    LogUnidad = Log.LogErrorUnidad(DatosFact.getString(1));
                                }
                            }
                            ////------------------//////
                            ///-------------Obtiene el folio AGR ----

                            FolioAgr = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr ASC");
                            while (FolioAgr.next()) {
                                F_FolAgr = FolioAgr.getString(1);

                                if (Dife.equals("sin")) {
                                    //Secuencial Sin Diferencias///
                                    DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                            + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                            + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                            + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                            + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                            + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                            + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                            + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                            + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                            + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur > 0 "
                                            + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                            + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                            + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                                } else {
                                    //Secuencial Con Diferencias///
                                    DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                            + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                            + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                            + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                            + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                            + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                            + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                            + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                            + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                            + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur >= 0 "
                                            + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                            + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                            + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                                }
                                while (DatosFact.next()) {
                                    Presentacion = DatosFact.getString("F_PreArtIS");
                                    ContPres = Presentacion.length();
                                    if (ContPres > 50) {
                                        Presentacion = Presentacion.substring(0, 49);
                                    }
                                    F_Origen = DatosFact.getString("F_Origen");
                                    F_SPArtIS = DatosFact.getString("F_SPArtIS");
                                    if (F_Origen.equals("1")) {
                                        F_IdOrg = "1";
                                        F_PreVenIS = 0;
                                    } else {
                                        F_IdOrg = "2";
                                        F_PreVenIS = DatosFact.getDouble("F_PreVenIS");
                                    }
                                    if (F_SPArtIS.equals("1")) {
                                        F_IdePro = "1";
                                    } else {
                                        F_IdePro = "0";
                                    }

                                    F_LAgr = F_FolAgr.substring(0, 4);
                                    if (F_LAgr.equals("AG-0")) {
                                        F_Agr = F_FolAgr.substring(3, 10);
                                    } else if (F_LAgr.equals("AG-F")) {
                                        F_Agr = F_FolAgr.substring(4, 11);
                                    }
                                    F_GNKLAgr = Integer.parseInt(F_Agr);
                                    Fecha = df2.format(df.parse(DatosFact.getString("F_FecEnt")));
                                    F_Secuencial = F_Secuencial + 1;

                                    /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                                    //ISEM.actualizar("INSERT INTO tb_txtis VALUES ('"+F_Secuencial+"','"+CveCli+"','"+DatosFact.getString("F_MunUniIS")+"','"+DatosFact.getString("F_LocUniIS")+"','"+DatosFact.getString("F_JurUniIS")+"','"+DatosFact.getString("F_ClaUniIS")+"','9999','9999','"+DatosFact.getString("F_MedUniIS")+"','"+DatosFact.getString("F_SUMArtIS")+"','10','"+DatosFact.getString("F_ClaPac")+"','"+DatosFact.getString("F_ClaArtIS")+"','"+DatosFact.getString("F_PreArtIS")+"','"+F_PreVenIS+"','"+DatosFact.getString("F_UnidadReq")+"','"+DatosFact.getString("F_Unidad")+"','0','"+DatosFact.getString("F_FecEnt")+"','"+DatosFact.getString("F_NomPac")+"','"+DatosFact.getString("F_APat")+"','"+DatosFact.getString("F_AMat")+"','"+DatosFact.getString("F_Edad")+"','"+DatosFact.getString("F_Sexo")+"','"+DatosFact.getString("F_DesArtIS")+"','"+F_IdOrg+"','','"+DatosFact.getString("F_DesMedIS")+"','2','"+DatosFact.getString("F_ClaDoc")+"',curdate(),'"+F_IdePro+"','"+DatosFact.getString("F_RegUniIS")+"','0','"+F_GNKLAgr+"','','','','','"+F_FolAgr+"','','','"+DatosFact.getString("F_Unidad")+"','"+DatosFact.getString("F_ClaLot")+"','"+Contrato+"')");
                                    QuerySql = "INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','" + DatosFact.getString("F_ClaPac") + "','" + DatosFact.getString("F_ClaArtIS") + "','" + Presentacion + "','" + F_PreVenIS + "','" + DatosFact.getString("F_UnidadReq") + "','" + DatosFact.getString("F_Unidad") + "','0','" + Fecha + "','" + DatosFact.getString("F_NomPac") + "','" + DatosFact.getString("F_APat") + "','" + DatosFact.getString("F_AMat") + "','" + DatosFact.getString("F_Edad") + "','" + DatosFact.getString("F_Sexo") + "','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',CONVERT (char(10), getdate(), 103),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + F_GNKLAgr + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','" + DatosFact.getString("F_ClaLot") + "','" + Contrato + "')";
                                    System.out.println(QuerySql);
//                                    ISEM.actualizar(QuerySql);

                                    // *** FIN *** //
                                }

                                FolFact = con.consulta("SELECT DISTINCT F_ClaDoc FROM tb_txtfarmacia WHERE F_ClaDoc IN (SELECT F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CanSur > 0");
                                while (FolFact.next()) {
                                    F_ClaDoc = FolFact.getString(1);
                                    F_Folios = F_Folios + F_ClaDoc.replace(" ", "") + ",";
                                }

                                /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
//                                ISEM.actualizar("UPDATE tb_txtis SET F_Folios='" + F_Folios + "' WHERE F_FacGNKLAgr='" + F_FolAgr + "'");
                                //ISEM.actualizar("UPDATE tb_txtis SET F_Folios='"+F_Folios+"' WHERE F_FacGNKLAgr='"+F_FolAgr+"'");
                                F_Folios = "";
                                F_Secuencial = F_Secuencial;

                                // *** FIN *** //
                            }
                            F_Secuencial = 0;
                            out.println("<script>alert('Secuencial Generado Correctamente')</script>");
                            out.println("<script>window.location.href = 'SecuencialFarm.jsp';</script>");

                            /**
                             * ****************************************FIN
                             * SISTEMA SCR SIALSS_CTRL
                             * SQL*****************************************
                             */
                        }
                        /**
                         * ****************************************FIN BASE DE
                         * DATOS SQL*****************************************
                         */
                    } else/*BASE DE DATOS MYSQL*/ if (Sistemas.equals("gnklite")) {/*SISTEMA GNKLITE MYSQL*/
                        ContratoCli = con.consulta("SELECT F_Contrato,F_CvCliente FROM tb_contratocliente");
                        if (ContratoCli.next()) {
                            Contrato = ContratoCli.getString(1);
                            CveCli = ContratoCli.getString(2);
                        }
                        //con.actualizar("Delete From tb_txtfarmacia");
                        con.actualizar("Delete From tb_facagr");  // limpia tabla

                        /*ConCeaps = ceaps.consulta("SELECT * FROM facturacion_txt WHERE fec_sur BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"'");
                                while(ConCeaps.next()){

                                    con.insertar("insert into tb_txtfarmacia values(0,'"+ConCeaps.getString(1)+"','"+ConCeaps.getString(2)+"','"+ConCeaps.getString(3)+"','"+ConCeaps.getString(4)+"','"+ConCeaps.getString(5)+"',"
                                            + "'"+ConCeaps.getString(6)+"','"+ConCeaps.getString(7)+"','"+ConCeaps.getString(8)+"','"+ConCeaps.getString(9)+"','"+ConCeaps.getString(10)+"','"+ConCeaps.getString(11)+"','"+ConCeaps.getString(12)+"',"
                                            + "'"+ConCeaps.getString(13)+"','"+ConCeaps.getString(14)+"','"+ConCeaps.getString(15)+"','"+ConCeaps.getString(16)+"','"+ConCeaps.getString(17)+"')");
                                }*/
                        // consulta tabla factura para obtener la fecha, folio y cliente
                        //FolFact = con.consulta("Select F_FecEnt, F_ClaDoc, F_ClaCli  FROM tb_factura WHERE F_FecEnt BETWEEN '"+df.format(df2.parse(fecha_ini))+"'  "
                        //+ "AND '"+df.format(df2.parse(fecha_fin))+"'  AND F_TipDoc <> 'D' AND F_StsFac <> 'C' GROUP BY F_FecEnt, F_ClaDoc, F_ClaCli  ORDER BY F_FecEnt, F_ClaDoc, F_ClaCli ");
                        FolFact = con.consulta("SELECT F_FecEnt,F_ClaDoc,F_ClaCli FROM tb_txtfarmacia WHERE F_FecEnt BETWEEN '" + fecha_ini + "'  "
                                + "AND '" + fecha_fin + "' GROUP BY F_FecEnt,F_ClaDoc,F_ClaCli  ORDER BY F_FecEnt,F_ClaDoc,F_ClaCli ");
                        while (FolFact.next()) {

                            con.actualizar("insert into tb_facagr values ('" + FolFact.getString(2) + "','" + FolFact.getString(3) + "','" + FolFact.getString(1) + "','')");

                        }
                        TxT = con.consulta("SELECT F_FacGNKLAgr FROM tb_txtis LIMIT 0,1");
                        //TxT = ISEM.consulta("select top(1) F_Secuencial,F_FacGNKLAgr from TB_TXTIS order by F_Secuencial desc");
                        while (TxT.next()) {
                            F_Agr = TxT.getString("F_FacGNKLAgr");
                        }
                        F_LAgr = F_Agr.substring(0, 4);
                        if (F_LAgr.equals("AG-0")) {
                            F_Agr = F_Agr.substring(3, 10);
                        } else if (F_LAgr.equals("AG-F")) {
                            F_Agr = F_Agr.substring(4, 11);
                        }
                        F_GNKLAgr = Integer.parseInt(F_Agr);
                        F_GNKLAgr2 = F_GNKLAgr;
                        FolFact = con.consulta("SELECT F_Fol,F_Cli,F_FeE FROM tb_facagr group by F_FeE ORDER BY F_FeE,F_Fol,F_Cli ASC");
                        while (FolFact.next()) {
                            F_Cliente = FolFact.getString(2);
                            F_ClaDoc = FolFact.getString(1);
                            F_FecEnt = FolFact.getString(3);
                            F_GNKLAgr = F_GNKLAgr + 1;
                            //obtiene los datos de la unidad
                            DatosUni = con.consulta("Select * FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                                    + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                                    + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'");
                            while (DatosUni.next()) {
                                while (F_GNKLAgr2 > 0) {
                                    F_GNKLAgr2 = F_GNKLAgr2 / 10;
                                    F_Long += 1;
                                }
                                //F_Long = F_GNKLAgr;
                                System.out.println("Longtud:" + F_Long);
                                for (int E = F_Long; E < 7; E++) {
                                    F_Espacio = F_Espacio + "0";
                                }
                                //FolAgr = "AG-" + F_ClaDoc.replace(' ','0');
                                FolAgr = "AG-F" + F_Espacio + F_GNKLAgr;
                                System.out.println("FolAgr:" + FolAgr);
                                // actualiza el campo de folio ag y médico
                                con.actualizar("update tb_facagr set F_FolAgr='" + FolAgr + "' where F_FeE = '" + F_FecEnt + "' "
                                        + "and F_Cli in ('" + DatosUni.getString(3) + "','" + DatosUni.getString(4) + "','" + DatosUni.getString(5) + "','" + DatosUni.getString(6) + "','" + DatosUni.getString(7) + "',"
                                        + "'" + DatosUni.getString(14) + "','" + DatosUni.getString(15) + "','" + DatosUni.getString(16) + "','" + DatosUni.getString(17) + "','" + DatosUni.getString(18) + "') and F_FolAgr=''");
                                F_Espacio = "";
                            }

                        }
                        // obtiene el último secuencial registrado
                        UltSecu = con.consulta("Select F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC LIMIT 0,1");
                        //UltSecu = ISEM.consulta("Select top 1 F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC");
                        if (UltSecu.next()) {
                            F_UltSecuencial = UltSecu.getInt(1);
                            F_Fecsur = UltSecu.getString(2);
                            F_FacGNKLAgr = UltSecu.getString(3);
                        }
                        F_Secuencial = F_UltSecuencial;
                        ///----------------//////
                        TxT = con.consulta("SELECT * FROM tb_txtis LIMIT 0,1");

                        // Valida datos de la factura
                        LogLimpiar = Log.LogLimpiar();
                        FolFact = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr");
                        while (FolFact.next()) {
                            DatosFact = con.consulta("Select F_ClaCli,F_ClaPro From tb_txtfarmacia f, tb_facagr a WHERE f.F_ClaDoc = a.F_Fol AND F_FolAgr = '" + FolFact.getString(1) + "'");
                            while (DatosFact.next()) {
                                LogArticulo = Log.LogErrorArticulo(DatosFact.getString(2));
                                LogUnidad = Log.LogErrorUnidad(DatosFact.getString(1));
                            }
                        }
                        ////------------------//////
                        ///-------------Obtiene el folio AGR ----

                        FolioAgr = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr ASC");
                        while (FolioAgr.next()) {
                            F_FolAgr = FolioAgr.getString(1);

                            if (Dife.equals("sin")) {
                                //Secuencial Sin Diferencias///
                                DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                        + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                        + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                        + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                        + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                        + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                        + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                        + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                        + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                        + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur > 0 "
                                        + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                        + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                        + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                            } else {
                                //Secuencial Con Diferencias///
                                DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                        + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                        + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                        + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                        + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                        + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                        + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                        + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                        + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                        + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur >= 0 "
                                        + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                        + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                        + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                            }
                            while (DatosFact.next()) {
                                Presentacion = DatosFact.getString("F_PreArtIS");
                                ContPres = Presentacion.length();
                                if (ContPres > 50) {
                                    Presentacion = Presentacion.substring(0, 49);
                                }
                                F_Origen = DatosFact.getString("F_Origen");
                                F_SPArtIS = DatosFact.getString("F_SPArtIS");
                                if (F_Origen.equals("1")) {
                                    F_IdOrg = "1";
                                    F_PreVenIS = 0;
                                } else {
                                    F_IdOrg = "2";
                                    F_PreVenIS = DatosFact.getDouble("F_PreVenIS");
                                }
                                if (F_SPArtIS.equals("1")) {
                                    F_IdePro = "1";
                                } else {
                                    F_IdePro = "0";
                                }

                                F_LAgr = F_FolAgr.substring(0, 4);
                                if (F_LAgr.equals("AG-0")) {
                                    F_Agr = F_FolAgr.substring(3, 10);
                                } else if (F_LAgr.equals("AG-F")) {
                                    F_Agr = F_FolAgr.substring(4, 11);
                                }
                                F_GNKLAgr = Integer.parseInt(F_Agr);
                                Fecha = df2.format(df.parse(DatosFact.getString("F_FecEnt")));
                                F_Secuencial = F_Secuencial + 1;

                                /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                                con.actualizar("INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','" + DatosFact.getString("F_ClaPac") + "','" + DatosFact.getString("F_ClaArtIS") + "','" + DatosFact.getString("F_PreArtIS") + "','" + F_PreVenIS + "','" + DatosFact.getString("F_UnidadReq") + "','" + DatosFact.getString("F_Unidad") + "','0','" + DatosFact.getString("F_FecEnt") + "','" + DatosFact.getString("F_NomPac") + "','" + DatosFact.getString("F_APat") + "','" + DatosFact.getString("F_AMat") + "','" + DatosFact.getString("F_Edad") + "','" + DatosFact.getString("F_Sexo") + "','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',curdate(),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + F_GNKLAgr + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','" + DatosFact.getString("F_ClaLot") + "','" + Contrato + "')");
                                /*QuerySql = "INSERT INTO tb_txtis VALUES ('"+F_Secuencial+"','"+CveCli+"','"+DatosFact.getString("F_MunUniIS")+"','"+DatosFact.getString("F_LocUniIS")+"','"+DatosFact.getString("F_JurUniIS")+"','"+DatosFact.getString("F_ClaUniIS")+"','9999','9999','"+DatosFact.getString("F_MedUniIS")+"','"+DatosFact.getString("F_SUMArtIS")+"','10','"+DatosFact.getString("F_ClaPac")+"','"+DatosFact.getString("F_ClaArtIS")+"','"+Presentacion+"','"+F_PreVenIS+"','"+DatosFact.getString("F_UnidadReq")+"','"+DatosFact.getString("F_Unidad")+"','0','"+Fecha+"','"+DatosFact.getString("F_NomPac")+"','"+DatosFact.getString("F_APat")+"','"+DatosFact.getString("F_AMat")+"','"+DatosFact.getString("F_Edad")+"','"+DatosFact.getString("F_Sexo")+"','"+DatosFact.getString("F_DesArtIS")+"','"+F_IdOrg+"','','"+DatosFact.getString("F_DesMedIS")+"','2','"+DatosFact.getString("F_ClaDoc")+"',CONVERT (char(10), getdate(), 103),'"+F_IdePro+"','"+DatosFact.getString("F_RegUniIS")+"','0','"+F_GNKLAgr+"','','','','','"+F_FolAgr+"','','','"+DatosFact.getString("F_Unidad")+"','"+DatosFact.getString("F_ClaLot")+"','"+Contrato+"')";
                                        System.out.println(QuerySql);
                                        ISEM.actualizar(QuerySql);*/

                                // *** FIN *** //
                            }

                            FolFact = con.consulta("SELECT DISTINCT F_ClaDoc FROM tb_txtfarmacia WHERE F_ClaDoc IN (SELECT F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CanSur > 0");
                            while (FolFact.next()) {
                                F_ClaDoc = FolFact.getString(1);
                                F_Folios = F_Folios + F_ClaDoc.replace(" ", "") + ",";
                            }

                            /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                            con.actualizar("UPDATE tb_txtis SET F_Folios='" + F_Folios + "' WHERE F_FacGNKLAgr='" + F_FolAgr + "'");
                            //ISEM.actualizar("UPDATE tb_txtis SET F_Folios='"+F_Folios+"' WHERE F_FacGNKLAgr='"+F_FolAgr+"'");
                            F_Folios = "";
                            F_Secuencial = F_Secuencial;

                            // *** FIN *** //
                        }
                        F_Secuencial = 0;
                        out.println("<script>alert('Secuencial Generado Correctamente')</script>");
                        out.println("<script>window.location.href = 'SecuencialFarm.jsp';</script>");

                        /**
                         * ****************************************FIN SISTEMA
                         * GNKLITE
                         * MYSQL*****************************************
                         */
                    } else {/*SISTEMA SCR SIALSS_CTRL MYSQL*/
                        ContratoCli = con.consulta("SELECT F_Contrato,F_CvCliente FROM tb_contratocliente");
                        if (ContratoCli.next()) {
                            Contrato = ContratoCli.getString(1);
                            CveCli = ContratoCli.getString(2);
                        }
                        con.actualizar("Delete From tb_txtfarmacia");
                        con.actualizar("Delete From tb_facagr");  // limpia tabla

//                        ConCeaps = ceaps.consulta("SELECT * FROM facturacion_txt WHERE fec_sur BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "'");
//                        while (ConCeaps.next()) {
//
//                            con.insertar("insert into tb_txtfarmacia values(0,'" + ConCeaps.getString(1) + "','" + ConCeaps.getString(2) + "','" + ConCeaps.getString(3) + "','" + ConCeaps.getString(4) + "','" + ConCeaps.getString(5) + "',"
//                                    + "'" + ConCeaps.getString(6) + "','" + ConCeaps.getString(7) + "','" + ConCeaps.getString(8) + "','" + ConCeaps.getString(9) + "','" + ConCeaps.getString(10) + "','" + ConCeaps.getString(11) + "','" + ConCeaps.getString(12) + "',"
//                                    + "'" + ConCeaps.getString(13) + "','" + ConCeaps.getString(14) + "','" + ConCeaps.getString(15) + "','" + ConCeaps.getString(16) + "','" + ConCeaps.getString(17) + "')");
//                        }

                        // consulta tabla factura para obtener la fecha, folio y cliente
                        //FolFact = con.consulta("Select F_FecEnt, F_ClaDoc, F_ClaCli  FROM tb_factura WHERE F_FecEnt BETWEEN '"+df.format(df2.parse(fecha_ini))+"'  "
                        //+ "AND '"+df.format(df2.parse(fecha_fin))+"'  AND F_TipDoc <> 'D' AND F_StsFac <> 'C' GROUP BY F_FecEnt, F_ClaDoc, F_ClaCli  ORDER BY F_FecEnt, F_ClaDoc, F_ClaCli ");
                        FolFact = con.consulta("SELECT F_FecEnt,F_ClaDoc,F_ClaCli FROM tb_txtfarmacia WHERE F_FecEnt BETWEEN '" + fecha_ini + "'  "
                                + "AND '" + fecha_fin + "' GROUP BY F_FecEnt,F_ClaDoc,F_ClaCli  ORDER BY F_FecEnt,F_ClaDoc,F_ClaCli ");
                        while (FolFact.next()) {

                            con.actualizar("insert into tb_facagr values ('" + FolFact.getString(2) + "','" + FolFact.getString(3) + "','" + FolFact.getString(1) + "','')");

                        }
                        TxT = con.consulta("SELECT F_FacGNKLAgr FROM tb_txtis LIMIT 0,1");
                        //TxT = ISEM.consulta("select top(1) F_Secuencial,F_FacGNKLAgr from TB_TXTIS order by F_Secuencial desc");
                        while (TxT.next()) {
                            F_Agr = TxT.getString("F_FacGNKLAgr");
                        }
                        F_LAgr = F_Agr.substring(0, 4);
                        if (F_LAgr.equals("AG-0")) {
                            F_Agr = F_Agr.substring(3, 10);
                        } else if (F_LAgr.equals("AG-F")) {
                            F_Agr = F_Agr.substring(4, 11);
                        }
                        F_GNKLAgr = Integer.parseInt(F_Agr);
                        F_GNKLAgr2 = F_GNKLAgr;
                        FolFact = con.consulta("SELECT F_Fol,F_Cli,F_FeE FROM tb_facagr group by F_FeE ORDER BY F_FeE,F_Fol,F_Cli ASC");
                        while (FolFact.next()) {
                            F_Cliente = FolFact.getString(2);
                            F_ClaDoc = FolFact.getString(1);
                            F_FecEnt = FolFact.getString(3);
                            F_GNKLAgr = F_GNKLAgr + 1;
                            //obtiene los datos de la unidad
                            DatosUni = con.consulta("Select * FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                                    + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                                    + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'");
                            while (DatosUni.next()) {
                                while (F_GNKLAgr2 > 0) {
                                    F_GNKLAgr2 = F_GNKLAgr2 / 10;
                                    F_Long += 1;
                                }
                                //F_Long = F_GNKLAgr;
                                System.out.println("Longtud:" + F_Long);
                                for (int E = F_Long; E < 7; E++) {
                                    F_Espacio = F_Espacio + "0";
                                }
                                //FolAgr = "AG-" + F_ClaDoc.replace(' ','0');
                                FolAgr = "AG-F" + F_Espacio + F_GNKLAgr;
                                System.out.println("FolAgr:" + FolAgr);
                                // actualiza el campo de folio ag y médico
                                con.actualizar("update tb_facagr set F_FolAgr='" + FolAgr + "' where F_FeE = '" + F_FecEnt + "' "
                                        + "and F_Cli in ('" + DatosUni.getString(3) + "','" + DatosUni.getString(4) + "','" + DatosUni.getString(5) + "','" + DatosUni.getString(6) + "','" + DatosUni.getString(7) + "',"
                                        + "'" + DatosUni.getString(14) + "','" + DatosUni.getString(15) + "','" + DatosUni.getString(16) + "','" + DatosUni.getString(17) + "','" + DatosUni.getString(18) + "') and F_FolAgr=''");
                                F_Espacio = "";
                            }

                        }
                        // obtiene el último secuencial registrado
                        UltSecu = con.consulta("Select F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC LIMIT 0,1");
                        //UltSecu = ISEM.consulta("Select top 1 F_Secuencial, F_Fecsur, F_FacGNKLAgr From tb_txtis ORDER BY F_Secuencial DESC");
                        if (UltSecu.next()) {
                            F_UltSecuencial = UltSecu.getInt(1);
                            F_Fecsur = UltSecu.getString(2);
                            F_FacGNKLAgr = UltSecu.getString(3);
                        }
                        F_Secuencial = F_UltSecuencial;
                        ///----------------//////
                        TxT = con.consulta("SELECT * FROM tb_txtis LIMIT 0,1");

                        // Valida datos de la factura
                        LogLimpiar = Log.LogLimpiar();
                        FolFact = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr");
                        while (FolFact.next()) {
                            DatosFact = con.consulta("Select F_ClaCli,F_ClaPro From tb_txtfarmacia f, tb_facagr a WHERE f.F_ClaDoc = a.F_Fol AND F_FolAgr = '" + FolFact.getString(1) + "'");
                            while (DatosFact.next()) {
                                LogArticulo = Log.LogErrorArticulo(DatosFact.getString(2));
                                LogUnidad = Log.LogErrorUnidad(DatosFact.getString(1));
                            }
                        }
                        ////------------------//////
                        ///-------------Obtiene el folio AGR ----

                        FolioAgr = con.consulta("SELECT F_FolAgr FROM tb_facagr GROUP BY F_FolAgr ORDER BY F_FolAgr ASC");
                        while (FolioAgr.next()) {
                            F_FolAgr = FolioAgr.getString(1);

                            if (Dife.equals("sin")) {
                                //Secuencial Sin Diferencias///
                                DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                        + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                        + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                        + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                        + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                        + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                        + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                        + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                        + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                        + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur > 0 "
                                        + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                        + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                        + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                            } else {
                                //Secuencial Con Diferencias///
                                DatosFact = con.consulta("SELECT U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,U.F_MedUniIS,A.F_SUMArtIS,A.F_ClaArtIS,"
                                        + "A.F_PreArtIS,A.F_PreVenIS,SUM(T.F_CanSur) AS F_Unidad,SUM(T.F_CanSol) AS F_UnidadReq,T.F_FecEnt,"
                                        + "A.F_DesArtIS,M.F_DesMedIS,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc,T.F_FolRec,T.F_ClaPac,T.F_NomPac,T.F_APat,T.F_AMat,"
                                        + "T.F_Edad,T.F_Sexo,T.F_ClaLot,ME.F_Origen AS F_Origen,T.F_Origen AS ORI "
                                        + "FROM tb_txtfarmacia T INNER JOIN tb_artiis A ON T.F_ClaPro=A.F_ClaInt INNER JOIN tb_medica ME ON A.F_ClaInt=ME.F_ClaPro "
                                        + "INNER JOIN tb_unidis U ON (T.F_ClaCli = U.F_ClaInt1 OR T.F_ClaCli = U.F_ClaInt2 OR T.F_ClaCli = U.F_ClaInt3 "
                                        + "OR T.F_ClaCli = U.F_ClaInt4 OR T.F_ClaCli = U.F_ClaInt5 OR T.F_ClaCli = U.F_ClaInt6 OR T.F_ClaCli = U.F_ClaInt7 "
                                        + "OR T.F_ClaCli = U.F_ClaInt8 OR T.F_ClaCli = U.F_ClaInt9 OR T.F_ClaCli = U.F_ClaInt10) "
                                        + "INNER JOIN tb_facagr F ON T.F_ClaDoc=F.F_Fol Inner Join tb_mediis M ON U.F_MedUniIS = M.F_ClaMedIs "
                                        + "WHERE T.F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND T.F_CanSur >= 0 "
                                        + "GROUP BY U.F_MunUniIS,U.F_LocUniIS,U.F_JurUniIS,U.F_ClaUniIS,A.F_SUMArtIS,A.F_ClaArtIS,A.F_PreArtIS,T.F_Origen,"
                                        + "A.F_PreVenIS,T.F_FecEnt,A.F_DesArtIS,T.F_NomMed,U.F_RegUniIS,A.F_SPArtIS,T.F_ClaDoc "
                                        + "ORDER BY T.F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                            }
                            while (DatosFact.next()) {
                                Presentacion = DatosFact.getString("F_PreArtIS");
                                ContPres = Presentacion.length();
                                if (ContPres > 50) {
                                    Presentacion = Presentacion.substring(0, 49);
                                }
                                F_Origen = DatosFact.getString("F_Origen");
                                F_SPArtIS = DatosFact.getString("F_SPArtIS");
                                if (F_Origen.equals("1")) {
                                    F_IdOrg = "1";
                                    F_PreVenIS = 0;
                                } else {
                                    F_IdOrg = "2";
                                    F_PreVenIS = DatosFact.getDouble("F_PreVenIS");
                                }
                                if (F_SPArtIS.equals("1")) {
                                    F_IdePro = "1";
                                } else {
                                    F_IdePro = "0";
                                }

                                F_LAgr = F_FolAgr.substring(0, 4);
                                if (F_LAgr.equals("AG-0")) {
                                    F_Agr = F_FolAgr.substring(3, 10);
                                } else if (F_LAgr.equals("AG-F")) {
                                    F_Agr = F_FolAgr.substring(4, 11);
                                }
                                F_GNKLAgr = Integer.parseInt(F_Agr);
                                Fecha = df2.format(df.parse(DatosFact.getString("F_FecEnt")));
                                F_Secuencial = F_Secuencial + 1;

                                /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                                con.actualizar("INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','" + DatosFact.getString("F_ClaPac") + "','" + DatosFact.getString("F_ClaArtIS") + "','" + DatosFact.getString("F_PreArtIS") + "','" + F_PreVenIS + "','" + DatosFact.getString("F_UnidadReq") + "','" + DatosFact.getString("F_Unidad") + "','0','" + DatosFact.getString("F_FecEnt") + "','" + DatosFact.getString("F_NomPac") + "','" + DatosFact.getString("F_APat") + "','" + DatosFact.getString("F_AMat") + "','" + DatosFact.getString("F_Edad") + "','" + DatosFact.getString("F_Sexo") + "','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',curdate(),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + F_GNKLAgr + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','" + DatosFact.getString("F_ClaLot") + "','" + Contrato + "')");
                                /*QuerySql = "INSERT INTO tb_txtis VALUES ('"+F_Secuencial+"','"+CveCli+"','"+DatosFact.getString("F_MunUniIS")+"','"+DatosFact.getString("F_LocUniIS")+"','"+DatosFact.getString("F_JurUniIS")+"','"+DatosFact.getString("F_ClaUniIS")+"','9999','9999','"+DatosFact.getString("F_MedUniIS")+"','"+DatosFact.getString("F_SUMArtIS")+"','10','"+DatosFact.getString("F_ClaPac")+"','"+DatosFact.getString("F_ClaArtIS")+"','"+Presentacion+"','"+F_PreVenIS+"','"+DatosFact.getString("F_UnidadReq")+"','"+DatosFact.getString("F_Unidad")+"','0','"+Fecha+"','"+DatosFact.getString("F_NomPac")+"','"+DatosFact.getString("F_APat")+"','"+DatosFact.getString("F_AMat")+"','"+DatosFact.getString("F_Edad")+"','"+DatosFact.getString("F_Sexo")+"','"+DatosFact.getString("F_DesArtIS")+"','"+F_IdOrg+"','','"+DatosFact.getString("F_DesMedIS")+"','2','"+DatosFact.getString("F_ClaDoc")+"',CONVERT (char(10), getdate(), 103),'"+F_IdePro+"','"+DatosFact.getString("F_RegUniIS")+"','0','"+F_GNKLAgr+"','','','','','"+F_FolAgr+"','','','"+DatosFact.getString("F_Unidad")+"','"+DatosFact.getString("F_ClaLot")+"','"+Contrato+"')";
                                        System.out.println(QuerySql);
                                        ISEM.actualizar(QuerySql);*/

                                // *** FIN *** //
                            }

                            FolFact = con.consulta("SELECT DISTINCT F_ClaDoc FROM tb_txtfarmacia WHERE F_ClaDoc IN (SELECT F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CanSur > 0");
                            while (FolFact.next()) {
                                F_ClaDoc = FolFact.getString(1);
                                F_Folios = F_Folios + F_ClaDoc.replace(" ", "") + ",";
                            }

                            /////// ***** INSERTA A LA TABLA DE TXT MySQL O QL ***** ///////
                            con.actualizar("UPDATE tb_txtis SET F_Folios='" + F_Folios + "' WHERE F_FacGNKLAgr='" + F_FolAgr + "'");
                            //ISEM.actualizar("UPDATE tb_txtis SET F_Folios='"+F_Folios+"' WHERE F_FacGNKLAgr='"+F_FolAgr+"'");
                            F_Folios = "";
                            F_Secuencial = F_Secuencial;

                            // *** FIN *** //
                        }
                        F_Secuencial = 0;
                        out.println("<script>alert('Secuencial Generado Correctamente')</script>");
                        out.println("<script>window.location.href = 'SecuencialFarm.jsp';</script>");
                    }
                    /**
                     * ****************************************FIN SISTEMA SCR
                     * SIALSS_CTRL MYSQL*****************************************
                     */
                    /**
                     * ****************************************FIN BASE DE
                     * DATOS MYSQL*****************************************
                     */

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                out.println("<script>window.history.back()</script>");
            }

            con.cierraConexion();
//            ceaps.cierraConexion();
            //ISEM.CierreConn();

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
