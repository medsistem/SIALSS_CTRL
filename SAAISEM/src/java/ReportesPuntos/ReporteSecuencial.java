package ReportesPuntos;

import ExportarTxt.LogTxt;
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
 * Reporte secuencial facturación isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ReporteSecuencial extends HttpServlet {

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
            ResultSet ContratoCli = null;
            ResultSet FolioAgr = null;
            ResultSet DatosUni = null;
            ResultSet DatosFact = null;
            ResultSet UltSecu = null;
            ResultSet TxT = null;
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            String fecha_ini = request.getParameter("fecha_ini");
            String Dife = request.getParameter("radio");
            String fecha_fin = request.getParameter("fecha_fin");
            String F_Cliente = "", FolAgr = "", F_ClaDoc = "", F_ClaDoc2 = "", F_FecEnt = "", F_Fecsur = "", F_FacGNKLAgr = "", F_FolAgr = "", F_DesMedIS = "", F_Folios = "", vm_ClaArtSum = "", vm_ClaArtSum2 = "";
            String F_Origen = "", vp_AdmVen = "", F_IdOrg = "", F_SPArtIS = "", F_IdePro = "", F_Espacio = "", LogLimpiar = "", LogArticulo = "", LogUnidad = "";
            int CantidSum = 0, CantidReqSum = 0, F_Secuencial = 0, F_Long = 0, F_UltSecuencial = 0;
            String Contrato = "", CveCli = "";
            double F_PreVenIS = 0.0;
            LogTxt Log = new LogTxt();
            if ((fecha_ini != "") && (fecha_fin != "")) {
                try {

                    ContratoCli = con.consulta("SELECT F_Contrato,F_CvCliente FROM tb_contratocliente");
                    if (ContratoCli.next()) {
                        Contrato = ContratoCli.getString(1);
                        CveCli = ContratoCli.getString(2);
                    }

                    con.actualizar("Delete From tb_txtis where F_Fecsur >= '" + fecha_ini + "' AND F_FacGNKLAgr LIKE 'AG-0%'");  // Borra los registros de la tabla de acuerdo a la fecha                       

                    con.actualizar("Delete From tb_facagr");  // limpia tabla

                    // consulta tabla factura para obtener la fecha, folio y cliente
                    //FolFact = con.consulta("Select F_FecEnt, F_ClaDoc, F_ClaCli  FROM tb_factura WHERE F_FecEnt BETWEEN '"+df.format(df2.parse(fecha_ini))+"'  "
                    //+ "AND '"+df.format(df2.parse(fecha_fin))+"'  AND F_TipDoc <> 'D' AND F_StsFac <> 'C' GROUP BY F_FecEnt, F_ClaDoc, F_ClaCli  ORDER BY F_FecEnt, F_ClaDoc, F_ClaCli ");
                    FolFact = con.consulta("Select F_FecEnt, F_ClaDoc, F_ClaCli  FROM tb_factura WHERE F_FecEnt BETWEEN '" + fecha_ini + "'  "
                            + "AND '" + fecha_fin + "' AND F_StsFact <> 'C' GROUP BY F_FecEnt, F_ClaDoc, F_ClaCli  ORDER BY F_FecEnt, F_ClaDoc, F_ClaCli;");
                    while (FolFact.next()) {

                        con.actualizar("insert into tb_facagr values ('" + FolFact.getString(2) + "','" + FolFact.getString(3) + "','" + FolFact.getString(1) + "','')");

                    }
                    FolFact = con.consulta("SELECT F_Fol,F_Cli,F_FeE FROM tb_facagr ORDER BY F_FeE,F_Fol,F_Cli ASC");
                    while (FolFact.next()) {
                        F_Cliente = FolFact.getString(2);
                        F_ClaDoc = FolFact.getString(1);
                        F_FecEnt = FolFact.getString(3);

                        //obtiene los datos de la unidad
                        DatosUni = con.consulta("Select * FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                                + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                                + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'");
                        while (DatosUni.next()) {
                            F_Long = F_ClaDoc.length();
                            System.out.println("Longtud:" + F_Long);
                            for (int E = F_Long; E < 7; E++) {
                                F_Espacio = F_Espacio + "0";
                            }
                            //FolAgr = "AG-" + F_ClaDoc.replace(' ','0');
                            FolAgr = "AG-" + F_Espacio + F_ClaDoc;
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
                        DatosFact = con.consulta("Select f.F_ClaCli,f.F_ClaPro From tb_factura f, tb_facagr a WHERE f.F_ClaDoc = a.F_Fol AND F_FolAgr = '" + FolFact.getString(1) + "'");
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
                            DatosFact = con.consulta("Select tb_unidis.F_MunUniIS AS F_MunUniIS, tb_unidis.F_LocUniIS AS F_LocUniIS, tb_unidis.F_JurUniIS AS F_JurUniIS, "
                                    + "tb_unidis.F_ClaUniIS AS F_ClaUniIS, tb_unidis.F_MedUniIS AS F_MedUniIS, tb_artiis.F_SUMArtIS AS F_SUMArtIS, tb_artiis.F_ClaArtIS AS F_ClaArtIS, "
                                    + "tb_artiis.F_PreArtIS AS F_PreArtIS, tb_lote.F_Origen AS F_Origen, tb_artiis.F_PreVenIS AS F_PreVenIS, SUM(tb_factura.F_CantSur) AS F_Unidad, "
                                    + "SUM(tb_factura.F_CantReq) AS F_UnidadReq, tb_factura.F_FecEnt AS F_FecEnt, tb_artiis.F_DesArtIS AS F_DesArtIS, tb_mediis.F_DesMedIS AS F_DesMedIS, "
                                    + "tb_unidis.F_RegUniIS AS F_RegUniIS,tb_artiis.F_SPArtIS AS F_SPArtIS, tb_factura.F_ClaDoc AS F_ClaDoc "
                                    + "From tb_factura INNER JOIN tb_lote ON (tb_factura.F_Lote = tb_lote.F_FolLot AND tb_factura.F_Ubicacion=tb_lote.F_Ubica) Inner Join tb_artiis ON(tb_factura.F_ClaPro = tb_artiis.F_ClaInt) "
                                    + "Inner Join tb_unidis ON(tb_factura.F_ClaCli = tb_unidis.F_ClaInt1 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt2 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt3 "
                                    + "OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt4 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt5 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt6 "
                                    + "OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt7 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt8 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt9 "
                                    + "OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt10) Inner Join tb_facagr  ON(tb_factura.F_ClaDoc = tb_facagr.F_Fol) Inner Join tb_mediis ON (tb_unidis.F_MedUniIS = tb_mediis.F_ClaMedIs) "
                                    + "Where  F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CantSur > 0 AND F_StsFact='A' "
                                    + "GROUP BY tb_unidis.F_MunUniIS, tb_unidis.F_LocUniIS, tb_unidis.F_JurUniIS, tb_unidis.F_ClaUniIS, tb_unidis.F_MedUniIS, tb_artiis.F_SUMArtIS, tb_artiis.F_ClaArtIS, "
                                    + "tb_artiis.F_PreArtIS, tb_lote.F_Origen, tb_artiis.F_PreVenIS, tb_factura.F_FecEnt, tb_artiis.F_DesArtIS, tb_mediis.F_DesMedIS, tb_unidis.F_RegUniIS,tb_artiis.F_SPArtIS "
                                    + "ORDER BY F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                        } else {
                            //Secuencial Con Diferencias///
                            DatosFact = con.consulta("Select tb_unidis.F_MunUniIS AS F_MunUniIS, tb_unidis.F_LocUniIS AS F_LocUniIS, tb_unidis.F_JurUniIS AS F_JurUniIS, "
                                    + "tb_unidis.F_ClaUniIS AS F_ClaUniIS, tb_unidis.F_MedUniIS AS F_MedUniIS, tb_artiis.F_SUMArtIS AS F_SUMArtIS, tb_artiis.F_ClaArtIS AS F_ClaArtIS, "
                                    + "tb_artiis.F_PreArtIS AS F_PreArtIS, tb_lote.F_Origen AS F_Origen, tb_artiis.F_PreVenIS AS F_PreVenIS, SUM(tb_factura.F_CantSur) AS F_Unidad, "
                                    + "SUM(tb_factura.F_CantReq) AS F_UnidadReq, tb_factura.F_FecEnt AS F_FecEnt, tb_artiis.F_DesArtIS AS F_DesArtIS, tb_mediis.F_DesMedIS AS F_DesMedIS, "
                                    + "tb_unidis.F_RegUniIS AS F_RegUniIS,tb_artiis.F_SPArtIS AS F_SPArtIS, tb_factura.F_ClaDoc AS F_ClaDoc "
                                    + "From tb_factura INNER JOIN tb_lote ON (tb_factura.F_Lote = tb_lote.F_FolLot AND tb_factura.F_Ubicacion=tb_lote.F_Ubica) Inner Join tb_artiis ON(tb_factura.F_ClaPro = tb_artiis.F_ClaInt) "
                                    + "Inner Join tb_unidis ON(tb_factura.F_ClaCli = tb_unidis.F_ClaInt1 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt2 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt3 "
                                    + "OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt4 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt5 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt6 "
                                    + "OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt7 OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt8 OR tb_factura.F_ClaCli = tb_unidis.F_ClaInt9 "
                                    + "OR   tb_factura.F_ClaCli = tb_unidis.F_ClaInt10) Inner Join tb_facagr  ON(tb_factura.F_ClaDoc = tb_facagr.F_Fol) Inner Join tb_mediis ON (tb_unidis.F_MedUniIS = tb_mediis.F_ClaMedIs) "
                                    + "Where  F_ClaDoc IN (Select F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CantSur >= 0 AND F_StsFact='A' "
                                    + "GROUP BY tb_unidis.F_MunUniIS, tb_unidis.F_LocUniIS, tb_unidis.F_JurUniIS, tb_unidis.F_ClaUniIS, tb_unidis.F_MedUniIS, tb_artiis.F_SUMArtIS, tb_artiis.F_ClaArtIS, "
                                    + "tb_artiis.F_PreArtIS, tb_lote.F_Origen, tb_artiis.F_PreVenIS, tb_factura.F_FecEnt, tb_artiis.F_DesArtIS, tb_mediis.F_DesMedIS, tb_unidis.F_RegUniIS,tb_artiis.F_SPArtIS "
                                    + "ORDER BY F_Origen, F_PAArtIS, F_SPArtIS, F_SumArtIS, F_ClaArtIS, f_cladoc");
                        }
                        while (DatosFact.next()) {

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

                            F_Secuencial = F_Secuencial + 1;
                            if (Dife.equals("sin")) {
                                con.actualizar("INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','','" + DatosFact.getString("F_ClaArtIS") + "','" + DatosFact.getString("F_PreArtIS") + "','" + F_PreVenIS + "','" + DatosFact.getString("F_Unidad") + "','" + DatosFact.getString("F_Unidad") + "','0','" + DatosFact.getString("F_FecEnt") + "','','','','0','','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',curdate(),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + DatosFact.getString("F_ClaDoc") + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','','" + Contrato + "')");
                            } else {
                                con.actualizar("INSERT INTO tb_txtis VALUES ('" + F_Secuencial + "','" + CveCli + "','" + DatosFact.getString("F_MunUniIS") + "','" + DatosFact.getString("F_LocUniIS") + "','" + DatosFact.getString("F_JurUniIS") + "','" + DatosFact.getString("F_ClaUniIS") + "','9999','9999','" + DatosFact.getString("F_MedUniIS") + "','" + DatosFact.getString("F_SUMArtIS") + "','10','','" + DatosFact.getString("F_ClaArtIS") + "','" + DatosFact.getString("F_PreArtIS") + "','" + F_PreVenIS + "','" + DatosFact.getString("F_UnidadReq") + "','" + DatosFact.getString("F_Unidad") + "','0','" + DatosFact.getString("F_FecEnt") + "','','','','0','','" + DatosFact.getString("F_DesArtIS") + "','" + F_IdOrg + "','','" + DatosFact.getString("F_DesMedIS") + "','2','" + DatosFact.getString("F_ClaDoc") + "',curdate(),'" + F_IdePro + "','" + DatosFact.getString("F_RegUniIS") + "','0','" + DatosFact.getString("F_ClaDoc") + "','','','','','" + F_FolAgr + "','','','" + DatosFact.getString("F_Unidad") + "','','" + Contrato + "')");
                            }

                        }

                        FolFact = con.consulta("SELECT DISTINCT f_cladoc FROM tb_factura WHERE F_ClaDoc IN (SELECT F_Fol FROM tb_facagr WHERE F_FolAgr ='" + F_FolAgr + "') AND F_CantSur > 0");
                        while (FolFact.next()) {
                            F_ClaDoc = FolFact.getString(1);
                            F_Folios = F_Folios + F_ClaDoc.replace(" ", "") + ",";
                        }
                        con.actualizar("UPDATE tb_txtis SET F_Folios='" + F_Folios + "' WHERE F_FacGNKLAgr='" + F_FolAgr + "'");
                        F_Folios = "";
                        F_Secuencial = F_Secuencial;
                    }
                    F_Secuencial = 0;
                    out.println("<script>alert('Secuencial Generado Correctamente')</script>");
                    out.println("<script>window.location.href = 'Secuencial.jsp';</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                out.println("<script>window.history.back()</script>");
            }

            con.cierraConexion();

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
