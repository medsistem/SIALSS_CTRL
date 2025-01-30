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
 * Reporteador txt isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Reporteador extends HttpServlet {

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

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            ResultSet Datos = null;
            String Unidad = "", Insumo = "", juris = "", origen = "", municipio = "", tipdoc = "", doct = "", lote = "", origenl = "", tipofact = "";
            String unisur = "", unireq = "", costo = "", ListJuris = "", ListMuni = "", ListUni = "", ListClave = "", fecha_ini = "", fecha_fin = "";
            String Query = "", Query2 = "", QueryJ = "", QueryM = "", QueryU = "", QueryC = "", QueryT = "";
            String Select = "", SelectU = "", SelectI = "", SelectJ = "", SelectO = "", SelectM = "", SelectT = "", SelectD = "", SelectL = "";
            String SelectTF = "", SelectN = "", SelectTU = "";
            String Cifras = "", CifrasR = "", CifrasS = "", CifrasC = "", LisTipUni = "", Nivel = "";
            String Group = "";
            int banc = 0, bans = 0, ban = 0, ban1 = 0, ban2 = 0, ban3 = 0, ban4 = 0, ban5 = 0, ban6 = 0, ban7 = 0, ban8 = 0, ban9 = 0, ban10 = 0;
            int ban11 = 0, ban12 = 0;

            if (request.getParameter("accion").equals("Generar")) {

                /**
                 * ******* FILTROS CONSULTAS *******
                 */
                ListJuris = request.getParameter("ListJuris");
                ListMuni = request.getParameter("ListMuni");
                ListUni = request.getParameter("ListUni");
                ListClave = request.getParameter("ListClave");
                fecha_ini = request.getParameter("fecha_ini");
                fecha_fin = request.getParameter("fecha_fin");
                LisTipUni = request.getParameter("LisTipUni");

                if (ListJuris == null) {
                    ListJuris = "";
                }
                if (ListMuni == null) {
                    ListMuni = "";
                }
                if (ListUni == null) {
                    ListUni = "";
                }
                if (ListClave == null) {
                    ListClave = "";
                }
                if (LisTipUni == null) {
                    LisTipUni = "";
                }

                if (!(ListJuris == "")) {
                    QueryJ = " AND F_ClaJurIS IN (" + ListJuris + ")";
                }
                if (!(ListMuni == "")) {
                    QueryM = " AND F_ClaMunIS IN (" + ListMuni + ")";
                }
                if (!(ListUni == "")) {
                    QueryU = " AND F_ClaCli IN (" + ListUni + ")";
                }
                if (!(ListClave == "")) {
                    QueryC = " AND F_ClaPro IN (" + ListClave + ")";
                }
                if (!(LisTipUni == "")) {
                    QueryT = " AND F_TIPO IN (" + LisTipUni + ")";
                }

                Query = QueryJ + QueryM + QueryU + QueryC + QueryT;
                System.out.println(Query);

                /**
                 * ******* FIN FILTROS CONSULTAS *******
                 */
                /**
                 * ******* DATOS GRUPOS *******
                 */
                Unidad = request.getParameter("Unidad");
                Insumo = request.getParameter("Insumo");
                juris = request.getParameter("juris");
                Nivel = request.getParameter("nivel");
                origen = request.getParameter("origen");
                tipofact = request.getParameter("tipofact");
                municipio = request.getParameter("municipio");
                //tipdoc = request.getParameter("tipdoc");
                doct = request.getParameter("doct");
                lote = request.getParameter("lote");
                //origenl = request.getParameter("origenl");

                if (Unidad == null) {
                    Unidad = "";
                }
                if (Insumo == null) {
                    Insumo = "";
                }
                if (juris == null) {
                    juris = "";
                }
                if (Nivel == null) {
                    Nivel = "";
                }
                if (origen == null) {
                    origen = "";
                }
                if (municipio == null) {
                    municipio = "";
                }
                if (tipdoc == null) {
                    tipdoc = "";
                }
                if (doct == null) {
                    doct = "";
                }
                if (lote == null) {
                    lote = "";
                }
                if (origenl == null) {
                    origenl = "";
                }
                if (tipofact == null) {
                    tipofact = "";
                }

                if (!(juris == "")) {
                    SelectJ = juris;
                    ban1 = 2;
                }
                if (!(Nivel == "")) {
                    SelectN = Nivel;
                    ban11 = 1;
                }
                if (!(municipio == "")) {
                    SelectM = municipio;
                    ban2 = 2;
                }
                if (!(Unidad == "")) {
                    SelectU = Unidad;
                    ban3 = 2;
                }
                if (!(Insumo == "")) {
                    SelectI = Insumo;
                    ban4 = 2;
                }
                if (!(lote == "")) {
                    SelectL = lote;
                    ban5 = 2;
                }
                if (!(origen == "")) {
                    SelectO = origen;
                    ban6 = 1;
                }
                if (!(doct == "")) {
                    SelectD = doct;
                    ban7 = 1;
                }
                if (!(tipofact == "")) {
                    SelectTF = tipofact;
                    ban12 = 1;
                }
                //if(!(tipdoc == "")){SelectT = tipdoc;}

                if (LisTipUni != "") {
                    SelectTU = "F_TIPO,";
                    bans = ban1 + ban2 + ban3 + ban4 + ban5 + ban6 + ban7 + ban11 + ban12;
                    Select = SelectJ + SelectM + SelectU + SelectI + SelectL + SelectO + SelectD + SelectN + SelectTF;
                } else {
                    bans = ban1 + ban2 + ban3 + ban4 + ban5 + ban6 + ban7 + ban11 + ban12;
                    Select = SelectJ + SelectM + SelectU + SelectI + SelectL + SelectO + SelectD + SelectN + SelectTF;
                }
                SelectTU = "";

                if (bans > 0) {
                    Group = Select.substring(0, Select.length() - 1);
                }
                /**
                 * ******* FIN DATOS GRUPOS *******
                 */

                /**
                 * ******** DATOS CIFRAS ******
                 */
                unisur = request.getParameter("unisur");
                unireq = request.getParameter("unireq");
                costo = request.getParameter("costo");

                if (unisur == null) {
                    unisur = "";
                }
                if (unireq == null) {
                    unireq = "";
                }
                if (costo == null) {
                    costo = "";
                }

                if (!(unisur == "")) {
                    CifrasS = unisur;
                    ban8 = 1;
                }
                if (!(unireq == "")) {
                    CifrasR = unireq;
                    ban9 = 1;
                }
                if (!(costo == "")) {
                    CifrasC = costo;
                    ban10 = 1;
                }
                banc = ban8 + ban9 + ban10;
                Cifras = CifrasR + CifrasS + CifrasC;
                if (banc > 0) {
                    Cifras = Cifras.substring(0, Cifras.length() - 1);
                }
                /**
                 * ******* FIN DATOS CIFRAS *******
                 */
                ban = bans + banc;
                Query2 = Select + Cifras;
                System.out.println("Campos-->" + Query2);

                System.out.println("Ban-->" + ban);
                ResultSet SRLotes = null;
                ResultSet Consulta = null;

                if ((ban8 == 1) && (ban9 == 1)) {
                    Query = Query + " AND F_User='" + sesion.getAttribute("nombre") + "' ";
                } else if (ban8 == 1) {
                    Query = Query + " AND F_CantSur>0 AND F_User='" + sesion.getAttribute("nombre") + "' ";
                } else {
                    Query = Query + " AND F_User='" + sesion.getAttribute("nombre") + "' ";
                }

                if (!(fecha_ini == "") && (!(fecha_fin == ""))) {
                    con.actualizar("delete from tb_reporteador where F_User='" + sesion.getAttribute("nombre") + "';");
                    con.actualizar("delete from tb_reporteadordatos where F_User='" + sesion.getAttribute("nombre") + "';");
                    if (bans > 0) {
                        if (!(unisur == "") || !(unireq == "") || !(costo == "")) {
                            if ((!(lote == "")) || (!(origenl == "")) || (!(origen == ""))) {

                                if ((ban8 == 1) && (ban9 == 1)) {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT '' AS F_ClaJurIS,'' AS F_DesJurIS,'0' AS F_ClaMunIS,'' AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,'' AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,md.F_DesPro AS F_DesPro,l.F_ClaLot AS F_ClaLot,date_format(l.F_FecCad,'%d/%m/%Y') AS F_FecCad,l.F_Origen AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum((f.F_CantSur * f.F_Costo)) AS F_Monto,u.F_Tipo AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User  FROM tb_factura F INNER JOIN tb_lote l on f.F_ClaPro = l.F_ClaPro and f.F_Lote = l.F_FolLot and f.F_Ubicacion = l.F_Ubica INNER JOIN tb_medica md on f.F_ClaPro = md.F_ClaPro INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_StsFact='A' group by f.F_ClaCli,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_Origen;");
                                } else if (ban8 == 1) {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT '' AS F_ClaJurIS,'' AS F_DesJurIS,'0' AS F_ClaMunIS,'' AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,'' AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,md.F_DesPro AS F_DesPro,l.F_ClaLot AS F_ClaLot,date_format(l.F_FecCad,'%d/%m/%Y') AS F_FecCad,l.F_Origen AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum((f.F_CantSur * f.F_Costo)) AS F_Monto,u.F_Tipo AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User  FROM tb_factura F INNER JOIN tb_lote l on f.F_ClaPro = l.F_ClaPro and f.F_Lote = l.F_FolLot and f.F_Ubicacion = l.F_Ubica INNER JOIN tb_medica md on f.F_ClaPro = md.F_ClaPro INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_StsFact='A' AND F_CantSur>0 group by f.F_ClaCli,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_Origen;");
                                } else {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT '' AS F_ClaJurIS,'' AS F_DesJurIS,'0' AS F_ClaMunIS,'' AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,'' AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,md.F_DesPro AS F_DesPro,l.F_ClaLot AS F_ClaLot,date_format(l.F_FecCad,'%d/%m/%Y') AS F_FecCad,l.F_Origen AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum((f.F_CantSur * f.F_Costo)) AS F_Monto,u.F_Tipo AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User  FROM tb_factura F INNER JOIN tb_lote l on f.F_ClaPro = l.F_ClaPro and f.F_Lote = l.F_FolLot and f.F_Ubicacion = l.F_Ubica INNER JOIN tb_medica md on f.F_ClaPro = md.F_ClaPro INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_StsFact='A' group by f.F_ClaCli,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro,l.F_ClaLot,l.F_FecCad,l.F_Origen;");
                                }

                                /*if (!(LisTipUni == "")) {

                                    con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F.F_ClaCli,U.F_Tipo FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F_TIPO IN (" + LisTipUni + ") GROUP BY F.F_ClaCli) AS t2 SET t1.F_Tipo=t2.F_Tipo WHERE t1.F_ClaCli=t2.F_ClaCli;");

                                    con.actualizar("UPDATE tb_reporteadordatos AS T1, (SELECT J.F_ClaJurIS AS F_ClaJurIS,J.F_DesJurIS AS F_DesJurIS,M.F_ClaMunIS AS F_ClaMunIS,M.F_DesMunIS AS F_DesMunIS,R.F_ClaCli AS F_ClaCli,U.F_DesUniIS AS F_DesUniIS FROM tb_reporteadordatos R INNER JOIN tb_unidis U ON R.F_ClaCli=U.F_ClaInt1 OR R.F_ClaCli=U.F_ClaInt2 OR R.F_ClaCli=U.F_ClaInt3 OR R.F_ClaCli=U.F_ClaInt4 OR R.F_ClaCli=U.F_ClaInt5 INNER JOIN tb_muniis M on U.F_MunUniIS = M.F_ClaMunIS and U.F_JurUniIS = M.F_JurMunIS INNER JOIN tb_juriis J on U.F_JurUniIS = J.F_ClaJurIS WHERE R.F_Tipo IN (" + LisTipUni + ") AND  F_User='" + sesion.getAttribute("nombre") + "' GROUP BY R.F_ClaCli) AS T2 SET T1.F_ClaJurIS=T2.F_ClaJurIS,T1.F_DesJurIS=T2.F_DesJurIS,T1.F_ClaMunIS=T2.F_ClaMunIS,T1.F_DesMunIS=T2.F_DesMunIS,T1.F_DesUniIS=T2.F_DesUniIS WHERE T1.F_ClaCli=T2.F_ClaCli;");

                                } else {
                                    con.actualizar("UPDATE tb_reporteadordatos AS T1, (SELECT J.F_ClaJurIS AS F_ClaJurIS,J.F_DesJurIS AS F_DesJurIS,M.F_ClaMunIS AS F_ClaMunIS,M.F_DesMunIS AS F_DesMunIS,R.F_ClaCli AS F_ClaCli,U.F_DesUniIS AS F_DesUniIS FROM tb_reporteadordatos R INNER JOIN tb_unidis U ON R.F_ClaCli=U.F_ClaInt1 OR R.F_ClaCli=U.F_ClaInt2 OR R.F_ClaCli=U.F_ClaInt3 OR R.F_ClaCli=U.F_ClaInt4 OR R.F_ClaCli=U.F_ClaInt5 INNER JOIN tb_muniis M on U.F_MunUniIS = M.F_ClaMunIS and U.F_JurUniIS = M.F_JurMunIS INNER JOIN tb_juriis J on U.F_JurUniIS = J.F_ClaJurIS WHERE F_User='" + sesion.getAttribute("nombre") + "' GROUP BY R.F_ClaCli) AS T2 SET T1.F_ClaJurIS=T2.F_ClaJurIS,T1.F_DesJurIS=T2.F_DesJurIS,T1.F_ClaMunIS=T2.F_ClaMunIS,T1.F_DesMunIS=T2.F_DesMunIS,T1.F_DesUniIS=T2.F_DesUniIS WHERE T1.F_ClaCli=T2.F_ClaCli;");
                                }*/
                                con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F.F_ClaCli,U.F_Tipo FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' GROUP BY F.F_ClaCli) AS t2 SET t1.F_Tipo=t2.F_Tipo WHERE t1.F_ClaCli=t2.F_ClaCli;");
                                con.actualizar("UPDATE tb_reporteadordatos AS T1, (SELECT J.F_ClaJurIS AS F_ClaJurIS,J.F_DesJurIS AS F_DesJurIS,M.F_ClaMunIS AS F_ClaMunIS,M.F_DesMunIS AS F_DesMunIS,R.F_ClaCli AS F_ClaCli,U.F_DesUniIS AS F_DesUniIS FROM tb_reporteadordatos R INNER JOIN tb_unidis U ON R.F_ClaCli=U.F_ClaInt1 OR R.F_ClaCli=U.F_ClaInt2 OR R.F_ClaCli=U.F_ClaInt3 OR R.F_ClaCli=U.F_ClaInt4 OR R.F_ClaCli=U.F_ClaInt5 INNER JOIN tb_muniis M on U.F_MunUniIS = M.F_ClaMunIS and U.F_JurUniIS = M.F_JurMunIS INNER JOIN tb_juriis J on U.F_JurUniIS = J.F_ClaJurIS WHERE F_User='" + sesion.getAttribute("nombre") + "' GROUP BY R.F_ClaCli) AS T2 SET T1.F_ClaJurIS=T2.F_ClaJurIS,T1.F_DesJurIS=T2.F_DesJurIS,T1.F_ClaMunIS=T2.F_ClaMunIS,T1.F_DesMunIS=T2.F_DesMunIS,T1.F_DesUniIS=T2.F_DesUniIS WHERE T1.F_ClaCli=T2.F_ClaCli;");

                                //con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F_ClaPro,F_DesPro FROM tb_medica) AS t2 SET t1.F_DesPro=t2.F_DesPro WHERE t1.F_ClaPro=t2.F_ClaPro AND t1.F_User='" + sesion.getAttribute("nombre") + "';");
                                SRLotes = con.consulta("SELECT " + Query2 + " FROM tb_reporteadordatos WHERE F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' " + Query + " GROUP BY " + Group + "");

                                while (SRLotes.next()) {
                                    if (ban == 1) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','','','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 2) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 3) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 4) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 5) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 6) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 7) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 8) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 9) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 10) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 11) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 12) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 13) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 14) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','" + SRLotes.getString(14) + "','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 15) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','" + SRLotes.getString(14) + "','" + SRLotes.getString(15) + "','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 16) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','" + SRLotes.getString(14) + "','" + SRLotes.getString(15) + "','" + SRLotes.getString(16) + "','" + sesion.getAttribute("nombre") + "')");
                                    }

                                }
                            } else {
                                if ((ban8 == 1) && (ban9 == 1)) {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT j.F_ClaJurIS AS F_ClaJurIS,j.F_DesJurIS AS F_DesJurIS,m.F_ClaMunIS AS F_ClaMunIS,m.F_DesMunIS AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,u.F_DesUniIS AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,'' AS F_DesPro,'' AS F_ClaLot,'' AS F_FecCad,'' AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum(f.F_Monto) AS F_Monto,'' AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User FROM tb_factura f INNER JOIN tb_unidis u on f.F_ClaCli = u.F_ClaInt1 or f.F_ClaCli = u.F_ClaInt2 or f.F_ClaCli = u.F_ClaInt3 or f.F_ClaCli = u.F_ClaInt4 or f.F_ClaCli = u.F_ClaInt5 or f.F_ClaCli = u.F_ClaInt6 or f.F_ClaCli = u.F_ClaInt7 or f.F_ClaCli = u.F_ClaInt8 or f.F_ClaCli = u.F_ClaInt9 or f.F_ClaCli = u.F_ClaInt10 join tb_muniis m on u.F_MunUniIS = m.F_ClaMunIS and u.F_JurUniIS = m.F_JurMunIS join tb_juriis j on u.F_JurUniIS = j.F_ClaJurIS LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact where f.F_StsFact = 'A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' group by j.F_ClaJurIS,j.F_DesJurIS,m.F_ClaMunIS,m.F_DesMunIS,f.F_ClaCli,u.F_DesUniIS,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro;");
                                } else if (ban8 == 1) {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT j.F_ClaJurIS AS F_ClaJurIS,j.F_DesJurIS AS F_DesJurIS,m.F_ClaMunIS AS F_ClaMunIS,m.F_DesMunIS AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,u.F_DesUniIS AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,'' AS F_DesPro,'' AS F_ClaLot,'' AS F_FecCad,'' AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum(f.F_Monto) AS F_Monto,'' AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User FROM tb_factura f INNER JOIN tb_unidis u on f.F_ClaCli = u.F_ClaInt1 or f.F_ClaCli = u.F_ClaInt2 or f.F_ClaCli = u.F_ClaInt3 or f.F_ClaCli = u.F_ClaInt4 or f.F_ClaCli = u.F_ClaInt5 or f.F_ClaCli = u.F_ClaInt6 or f.F_ClaCli = u.F_ClaInt7 or f.F_ClaCli = u.F_ClaInt8 or f.F_ClaCli = u.F_ClaInt9 or f.F_ClaCli = u.F_ClaInt10 join tb_muniis m on u.F_MunUniIS = m.F_ClaMunIS and u.F_JurUniIS = m.F_JurMunIS join tb_juriis j on u.F_JurUniIS = j.F_ClaJurIS LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact where f.F_StsFact = 'A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F_CantSur>0 group by j.F_ClaJurIS,j.F_DesJurIS,m.F_ClaMunIS,m.F_DesMunIS,f.F_ClaCli,u.F_DesUniIS,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro;");
                                } else {
                                    con.insertar("INSERT INTO tb_reporteadordatos (F_ClaJurIS,F_DesJurIS,F_ClaMunIS,F_DesMunIS,F_ClaCli,F_DesUniIS,F_ClaDoc,F_FecEnt,F_ClaPro,F_DesPro,F_ClaLot,F_FecCad,F_Origen,F_CantReq,F_CantSur,F_Costo,F_Monto,F_Tipo,F_TipoFact,F_User) SELECT j.F_ClaJurIS AS F_ClaJurIS,j.F_DesJurIS AS F_DesJurIS,m.F_ClaMunIS AS F_ClaMunIS,m.F_DesMunIS AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,u.F_DesUniIS AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,'' AS F_DesPro,'' AS F_ClaLot,'' AS F_FecCad,'' AS F_Origen,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum(f.F_Monto) AS F_Monto,'' AS F_Tipo,OFAC.F_Tipo AS F_TipoFact,'" + sesion.getAttribute("nombre") + "' AS F_User FROM tb_factura f INNER JOIN tb_unidis u on f.F_ClaCli = u.F_ClaInt1 or f.F_ClaCli = u.F_ClaInt2 or f.F_ClaCli = u.F_ClaInt3 or f.F_ClaCli = u.F_ClaInt4 or f.F_ClaCli = u.F_ClaInt5 or f.F_ClaCli = u.F_ClaInt6 or f.F_ClaCli = u.F_ClaInt7 or f.F_ClaCli = u.F_ClaInt8 or f.F_ClaCli = u.F_ClaInt9 or f.F_ClaCli = u.F_ClaInt10 join tb_muniis m on u.F_MunUniIS = m.F_ClaMunIS and u.F_JurUniIS = m.F_JurMunIS join tb_juriis j on u.F_JurUniIS = j.F_ClaJurIS LEFT JOIN tb_obserfact OFAC ON F.F_ClaDoc=OFAC.F_IdFact where f.F_StsFact = 'A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' group by j.F_ClaJurIS,j.F_DesJurIS,m.F_ClaMunIS,m.F_DesMunIS,f.F_ClaCli,u.F_DesUniIS,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro;");
                                }

                                con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F_ClaPro,F_DesPro FROM tb_medica) AS t2 SET t1.F_DesPro=t2.F_DesPro WHERE t1.F_ClaPro=t2.F_ClaPro AND t1.F_User='" + sesion.getAttribute("nombre") + "';");

                                /*if (!(LisTipUni == "")) {
                                    con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F.F_ClaCli,U.F_Tipo FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F_TIPO IN (" + LisTipUni + ") GROUP BY F.F_ClaCli) AS t2 SET t1.F_Tipo=t2.F_Tipo WHERE t1.F_ClaCli=t2.F_ClaCli;");
                                }*/
                                con.actualizar("UPDATE tb_reporteadordatos AS t1,(SELECT F.F_ClaCli,U.F_Tipo FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_StsFact='A' AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' GROUP BY F.F_ClaCli) AS t2 SET t1.F_Tipo=t2.F_Tipo WHERE t1.F_ClaCli=t2.F_ClaCli;");

                                /*Consulta = con.consulta("SELECT j.F_ClaJurIS AS F_ClaJurIS,j.F_DesJurIS AS F_DesJurIS,m.F_ClaMunIS AS F_ClaMunIS,m.F_DesMunIS AS F_DesMunIS,f.F_ClaCli AS F_ClaCli,u.F_DesUniIS AS F_DesUniIS,f.F_ClaDoc AS F_ClaDoc,f.F_FecEnt AS F_FecEnt,f.F_ClaPro AS F_ClaPro,md.F_DesPro AS F_DesPro,sum(f.F_CantReq) AS F_CantReq,sum(f.F_CantSur) AS F_CantSur,f.F_Costo AS F_Costo,sum(f.F_Monto) AS F_Monto FROM tb_factura f INNER JOIN tb_unidis u on (f.F_ClaCli = u.F_ClaInt1) or (f.F_ClaCli = u.F_ClaInt2) or (f.F_ClaCli = u.F_ClaInt3) or (f.F_ClaCli = u.F_ClaInt4) or (f.F_ClaCli = u.F_ClaInt5) or (f.F_ClaCli = u.F_ClaInt6) or (f.F_ClaCli = u.F_ClaInt7) or (f.F_ClaCli = u.F_ClaInt8) or (f.F_ClaCli = u.F_ClaInt9) or (f.F_ClaCli = u.F_ClaInt10) join tb_muniis m on u.F_MunUniIS = m.F_ClaMunIS and u.F_JurUniIS = m.F_JurMunIS join tb_juriis j on u.F_JurUniIS = j.F_ClaJurIS join tb_medica md on f.F_ClaPro = md.F_ClaPro where (f.F_StsFact = 'A') AND F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' group by j.F_ClaJurIS,j.F_DesJurIS,m.F_ClaMunIS,m.F_DesMunIS,f.F_ClaCli,u.F_DesUniIS,f.F_ClaDoc,f.F_FecEnt,f.F_ClaPro,md.F_DesPro;");
                                 while(Consulta.next()){
                                 con.insertar("INSERT INTO tb_reporteadordatos VALUES('"+Consulta.getString(1)+"','"+Consulta.getString(2)+"','"+Consulta.getString(3)+"','"+Consulta.getString(4)+"','"+Consulta.getString(5)+"','"+Consulta.getString(6)+"','"+Consulta.getString(7)+"','"+Consulta.getString(8)+"','"+Consulta.getString(9)+"','"+Consulta.getString(10)+"','"+Consulta.getString(11)+"','"+Consulta.getString(12)+"','"+Consulta.getString(13)+"','"+Consulta.getString(14)+"','','" + sesion.getAttribute("nombre") + "');");
                                 }*/
                                SRLotes = con.consulta("SELECT " + Query2 + " FROM tb_reporteadordatos WHERE F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' " + Query + " GROUP BY " + Group + "");
                                while (SRLotes.next()) {
                                    if (ban == 1) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','','','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 2) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 3) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 4) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 5) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 6) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 7) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 8) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 9) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 10) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 11) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 12) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 13) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','','','','" + sesion.getAttribute("nombre") + "')");
                                    } else if (ban == 14) {
                                        con.insertar("INSERT INTO tb_reporteador VALUES('" + SRLotes.getString(1) + "','" + SRLotes.getString(2) + "','" + SRLotes.getString(3) + "','" + SRLotes.getString(4) + "','" + SRLotes.getString(5) + "','" + SRLotes.getString(6) + "','" + SRLotes.getString(7) + "','" + SRLotes.getString(8) + "','" + SRLotes.getString(9) + "','" + SRLotes.getString(10) + "','" + SRLotes.getString(11) + "','" + SRLotes.getString(12) + "','" + SRLotes.getString(13) + "','" + SRLotes.getString(14) + "','','','" + sesion.getAttribute("nombre") + "')");
                                    }

                                }
                            }

                            out.println(" <script>window.open('reportes/ReporteadorConsulta.jsp?User=" + sesion.getAttribute("nombre") + "&ban=" + ban + "&f1=" + fecha_ini + "&f2=" + fecha_fin + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                            out.println("<script>window.history.back()</script>");
                        } else {
                            out.println("<script>alert('Favor de seleccionar cifras')</script>");
                            out.println("<script>window.history.back()</script>");
                        }
                    } else {
                        out.println("<script>alert('Favor de seleccionar Datos')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else {
                    out.println("<script>alert('Favor de seleccionar Fechas')</script>");
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
