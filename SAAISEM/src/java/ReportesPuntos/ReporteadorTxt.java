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
 * Reporteado de los reportes txt facturación
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ReporteadorTxt extends HttpServlet {

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
            ResultSet Juris = null;
            ResultSet Iva = null;
            ResultSet Produc = null;
            ResultSet Unidades = null;
            ResultSet Contrato = null;
            String Fecha1 = "", Fecha2 = "", Sec1 = "", Sec2 = "";
            int Consulta = 0, Grupo1 = 0, Grupo2 = 0, Tipo = 0, Suministro = 0, Cobertura = 0, Surtido = 0, Status = 0;
            String ClaJur = "", Part1 = "", Part2 = "", Part3 = "", Part4 = "", Part5 = "", Part6 = "", Consultas = "", Group = "", Query = "";
            String Consultas2 = "", Query2 = "", Group2 = "";
            double IvaTotal = 0.0, IvaTotal2 = 0.0;
            int F_CReq = 0, F_CSur = 0, F_PzNSur = 0;
            double F_CTotal = 0.0, F_CSer = 0.0, F_CNSur = 0.0, F_Iva = 0.0;
            //  int Check1=0,Check2=0,Check3=0,Check4=0,Check5=0,Check6=0,Check7=0,Check8=0,Check9=0;
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);

            Fecha1 = request.getParameter("fecha_ini");
            Fecha2 = request.getParameter("fecha_fin");
            Consulta = Integer.parseInt(request.getParameter("radio"));
            Grupo1 = Integer.parseInt(request.getParameter("radio1"));
            Grupo2 = Integer.parseInt(request.getParameter("radio2"));
            /*Check1 = Integer.parseInt(request.getParameter("checkbox1"));
            Check2 = Integer.parseInt(request.getParameter("checkbox2"));
            Check3 = Integer.parseInt(request.getParameter("checkbox3"));
            Check4 = Integer.parseInt(request.getParameter("checkbox4"));
            Check5 = Integer.parseInt(request.getParameter("checkbox5"));
            Check6 = Integer.parseInt(request.getParameter("checkbox6"));
            Check7 = Integer.parseInt(request.getParameter("checkbox7"));
            Check8 = Integer.parseInt(request.getParameter("checkbox8"));
            Check9 = Integer.parseInt(request.getParameter("checkbox9"));*/
            Tipo = Integer.parseInt(request.getParameter("radio5"));
            Status = Integer.parseInt(request.getParameter("radio6"));
            Suministro = Integer.parseInt(request.getParameter("suministro"));
            Surtido = Integer.parseInt(request.getParameter("surtido"));
            Sec1 = request.getParameter("secuencial1");
            Sec2 = request.getParameter("secuencial2");
            Cobertura = Integer.parseInt(request.getParameter("cobertura"));

            out.println("r1 " + Grupo1 + " r2: " + Grupo2 + " Suministro: " + Suministro + " Surtido: " + Surtido + " Cobert: " + Cobertura);

            if (((Fecha1 != "") && (Fecha2 != "")) || ((Sec1 != "") && (Sec2 != ""))) { ///// VALIDA POR FECHAS 'O' SECUENCIALES /////
                con.actualizar("delete from tb_txtreporte where f_user='" + sesion.getAttribute("nombre") + "'");
                if (Surtido > 0) {
                    Part1 = " AND F_Idsur='" + Surtido + "' ";
                }
                if (Suministro > 0) {
                    Part2 = " AND F_TipMed='" + Suministro + "' ";
                }
                if (Cobertura < 2) {
                    Part3 = " AND F_IdePro='" + Cobertura + "' ";
                }
                if (Status == 1) {
                    Part4 = " AND (F_Status='' OR F_Status=NULL) ";
                } else {
                    Part4 = " AND F_Status='C' ";
                }
                if (Tipo == 1) {
                    Part5 = " AND F_FacGNKLAgr LIKE '%AG-0%' ";
                } else {
                    Part5 = " AND F_FacGNKLAgr LIKE '%AG-F%' ";
                }
                if (Consulta == 1) {
                    Part6 = " F_Fecsur BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' ";
                } else {
                    Part6 = " F_Secuencial BETWEEN '" + Sec1 + "' AND '" + Sec2 + "' ";
                }

                if ((Grupo1 == 1) && (Grupo2 == 1)) { ////    JURIS X JURIS //////
                    System.out.println("JURIS X JURIS");
                    Consultas = "SELECT U.F_JurUniIS,J.F_DesJurIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,"
                            + "SUM(F_CosUni*F_Cansur) AS F_CostTal ,SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,"
                            + "SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro"
                            + " WHERE ";
                    Group = " GROUP BY U.F_JurUniIS,J.F_DesJurIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Juris = con.consulta(Query);
                    while (Juris.next()) {
                        F_CReq = F_CReq + Juris.getInt(3);
                        F_CSur = F_CSur + Juris.getInt(4);
                        F_CTotal = F_CTotal + Juris.getDouble(5);
                        F_CSer = F_CSer + Juris.getDouble(6);
                        F_PzNSur = F_PzNSur + Juris.getInt(7);
                        F_CNSur = F_CNSur + Juris.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE  M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }

                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Juris.getString(1) + "','" + Juris.getString(2) + "','" + Juris.getString(3) + "',"
                                + "'" + Juris.getString(4) + "','" + Juris.getString(5) + "','" + Juris.getString(6) + "','" + Juris.getString(7) + "','" + Juris.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if ((Grupo1 == 1) && (Grupo2 == 2)) { ///// JURIS X INSUMO ////
                    System.out.println("JURIS X INSUMO");
                    Consultas = "SELECT U.F_JurUniIS,J.F_DesJurIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,"
                            + "SUM(F_CosUni*F_Cansur) AS F_CostTal ,SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,"
                            + "SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro"
                            + " WHERE ";
                    Group = " GROUP BY U.F_JurUniIS,J.F_DesJurIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Juris = con.consulta(Query);
                    while (Juris.next()) {

                        F_CReq = F_CReq + Juris.getInt(3);
                        F_CSur = F_CSur + Juris.getInt(4);
                        F_CTotal = F_CTotal + Juris.getDouble(5);
                        F_CSer = F_CSer + Juris.getDouble(6);
                        F_PzNSur = F_PzNSur + Juris.getInt(7);
                        F_CNSur = F_CNSur + Juris.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Juris.getString(1) + "','" + Juris.getString(2) + "','" + Juris.getString(3) + "',"
                                + "'" + Juris.getString(4) + "','" + Juris.getString(5) + "','" + Juris.getString(6) + "','" + Juris.getString(7) + "','" + Juris.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas2 = "SELECT F_Cveart,F_DesArtIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                                + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                + " WHERE U.F_JurUniIS='" + Juris.getString(1) + "' AND ";
                        Group2 = " GROUP BY F_Cveart,F_DesArtIS";
                        Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;

                        Produc = con.consulta(Query2);
                        while (Produc.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {

                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND T.F_Cveart='" + Produc.getString(1) + "' AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);

                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Produc.getString(1) + "','" + Produc.getString(2) + "','" + Produc.getString(3) + "','" + Produc.getString(4) + "','" + Produc.getString(5) + "',"
                                    + "'" + Produc.getString(6) + "','" + Produc.getString(7) + "','" + Produc.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                        }
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if ((Grupo1 == 1) && (Grupo2 == 3)) { ////JURIS X CONTRATO ////
                    System.out.println("JURIS X CONTRATO");
                    Consultas = "SELECT U.F_JurUniIS,J.F_DesJurIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,"
                            + "SUM(F_CosUni*F_Cansur) AS F_CostTal ,SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,"
                            + "SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro"
                            + " WHERE ";
                    Group = " GROUP BY U.F_JurUniIS,J.F_DesJurIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Juris = con.consulta(Query);
                    while (Juris.next()) {

                        F_CReq = F_CReq + Juris.getInt(3);
                        F_CSur = F_CSur + Juris.getInt(4);
                        F_CTotal = F_CTotal + Juris.getDouble(5);
                        F_CSer = F_CSer + Juris.getDouble(6);
                        F_PzNSur = F_PzNSur + Juris.getInt(7);
                        F_CNSur = F_CNSur + Juris.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Juris.getString(1) + "','" + Juris.getString(2) + "','" + Juris.getString(3) + "',"
                                + "'" + Juris.getString(4) + "','" + Juris.getString(5) + "','" + Juris.getString(6) + "','" + Juris.getString(7) + "','" + Juris.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas2 = "SELECT F_Contrato,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                                + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                + " WHERE U.F_JurUniIS='" + Juris.getString(1) + "' AND ";
                        Group2 = " GROUP BY F_Contrato";
                        Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;
                        Contrato = con.consulta(Query2);
                        while (Contrato.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {
                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND T.F_Contrato='" + Contrato.getString(1) + "' AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);
                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Contrato.getString(1) + "','" + Contrato.getString(1) + "','" + Contrato.getString(2) + "','" + Contrato.getString(3) + "','" + Contrato.getString(4) + "','" + Contrato.getString(5) + "',"
                                    + "'" + Contrato.getString(6) + "','" + Contrato.getString(7) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");

                        }

                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if (Grupo1 == 1 && Grupo2 == 4) { ///// JURIS X UNIDADES ////
                    System.out.println("JURIS X UNIDADES");
                    Consultas = "SELECT U.F_JurUniIS,J.F_DesJurIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,"
                            + "SUM(F_CosUni*F_Cansur) AS F_CostTal ,SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,"
                            + "SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro"
                            + " WHERE ";
                    Group = " GROUP BY U.F_JurUniIS,J.F_DesJurIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Juris = con.consulta(Query);
                    while (Juris.next()) {

                        F_CReq = F_CReq + Juris.getInt(3);
                        F_CSur = F_CSur + Juris.getInt(4);
                        F_CTotal = F_CTotal + Juris.getDouble(5);
                        F_CSer = F_CSer + Juris.getDouble(6);
                        F_PzNSur = F_PzNSur + Juris.getInt(7);
                        F_CNSur = F_CNSur + Juris.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Juris.getString(1) + "','" + Juris.getString(2) + "','" + Juris.getString(3) + "',"
                                + "'" + Juris.getString(4) + "','" + Juris.getString(5) + "','" + Juris.getString(6) + "','" + Juris.getString(7) + "','" + Juris.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas2 = "SELECT F_Cveuni,F_DesUniIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS "
                                + "WHERE U.F_JurUniIS='" + Juris.getString(1) + "'  AND ";
                        Group2 = " GROUP BY F_Cveuni,F_DesUniIS";
                        Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;

                        Unidades = con.consulta(Query2);
                        while (Unidades.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {

                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND U.F_JurUniIS='" + Juris.getString(1) + "' AND T.F_Cveart='" + Unidades.getString(1) + "' AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);

                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Unidades.getString(1) + "','" + Unidades.getString(2) + "','" + Unidades.getString(3) + "','" + Unidades.getString(4) + "','" + Unidades.getString(5) + "',"
                                    + "'" + Unidades.getString(6) + "','" + Unidades.getString(7) + "','" + Unidades.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                        }
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if ((Grupo1 == 2) && (Grupo2 == 2)) { /// INSUMO  X INSUMO ///
                    System.out.println("INSUMO X INSUMO");
                    Consultas2 = "SELECT F_Cveart,F_DesArtIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                            + " WHERE ";
                    Group2 = " GROUP BY F_Cveart,F_DesArtIS";
                    Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;

                    Produc = con.consulta(Query2);
                    while (Produc.next()) {

                        F_CReq = F_CReq + Produc.getInt(3);
                        F_CSur = F_CSur + Produc.getInt(4);
                        F_CTotal = F_CTotal + Produc.getDouble(5);
                        F_CSer = F_CSer + Produc.getDouble(6);
                        F_PzNSur = F_PzNSur + Produc.getInt(7);
                        F_CNSur = F_CNSur + Produc.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {

                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Cveart='" + Produc.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);

                            while (Iva.next()) {
                                IvaTotal2 = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }

                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Produc.getString(1) + "','" + Produc.getString(2) + "','" + Produc.getString(3) + "','" + Produc.getString(4) + "','" + Produc.getString(5) + "',"
                                + "'" + Produc.getString(6) + "','" + Produc.getString(7) + "','" + Produc.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                    }

                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if ((Grupo1 == 2) && (Grupo2 == 3)) { ////INSUMO X CONTRATO ////
                    System.out.println("INSUMO X CONTRATO");
                    Consultas2 = "SELECT F_Contrato,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                            + " WHERE ";
                    Group2 = " GROUP BY F_Contrato";
                    Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;
                    Contrato = con.consulta(Query2);
                    while (Contrato.next()) {
                        F_CReq = F_CReq + Contrato.getInt(2);
                        F_CSur = F_CSur + Contrato.getInt(3);
                        F_CTotal = F_CTotal + Contrato.getDouble(4);
                        F_CSer = F_CSer + Contrato.getDouble(5);
                        F_PzNSur = F_PzNSur + Contrato.getInt(6);
                        F_CNSur = F_CNSur + Contrato.getDouble(7);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Contrato='" + Contrato.getString(1) + "'  AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal2 = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }

                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Contrato.getString(1) + "','" + Contrato.getString(1) + "','" + Contrato.getString(2) + "','" + Contrato.getString(3) + "','" + Contrato.getString(4) + "','" + Contrato.getString(5) + "',"
                                + "'" + Contrato.getString(6) + "','" + Contrato.getString(7) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas = "SELECT F_Cveart,F_DesArtIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                                + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                + " WHERE F_Contrato='" + Contrato.getString(1) + "'  AND ";
                        Group = " GROUP BY F_Cveart,F_DesArtIS";
                        Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;

                        Produc = con.consulta(Query);
                        while (Produc.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {

                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND T.F_Cveart='" + Produc.getString(1) + "' AND F_Contrato='" + Contrato.getString(1) + "' AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);

                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Produc.getString(1) + "','" + Produc.getString(2) + "','" + Produc.getString(3) + "','" + Produc.getString(4) + "','" + Produc.getString(5) + "',"
                                    + "'" + Produc.getString(6) + "','" + Produc.getString(7) + "','" + Produc.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                        }

                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if (Grupo1 == 2 && Grupo2 == 4) { ///// UNIDADES X INSUMO ////
                    System.out.println("UNIDADES X INSUMO");
                    Consultas = "SELECT F_Cveuni,F_DesUniIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS"
                            + "WHERE ";
                    Group = " GROUP BY F_Cveuni,F_DesUniIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Unidades = con.consulta(Query);
                    while (Unidades.next()) {

                        F_CReq = F_CReq + Unidades.getInt(3);
                        F_CSur = F_CSur + Unidades.getInt(4);
                        F_CTotal = F_CTotal + Unidades.getDouble(5);
                        F_CSer = F_CSer + Unidades.getDouble(6);
                        F_PzNSur = F_PzNSur + Unidades.getInt(7);
                        F_CNSur = F_CNSur + Unidades.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Cveuni='" + Unidades.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Unidades.getString(1) + "','" + Unidades.getString(2) + "','" + Unidades.getString(3) + "',"
                                + "'" + Unidades.getString(4) + "','" + Unidades.getString(5) + "','" + Unidades.getString(6) + "','" + Unidades.getString(7) + "','" + Unidades.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas2 = "SELECT F_Cveart,F_DesArtIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                                + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                + " WHERE T.F_Cveuni='" + Unidades.getString(1) + "' AND ";
                        Group2 = " GROUP BY F_Cveart,F_DesArtIS";
                        Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;

                        Produc = con.consulta(Query2);
                        while (Produc.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {

                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND T.F_Cveuni='" + Unidades.getString(1) + "' AND T.F_Cveart='" + Produc.getString(1) + "' AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);

                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Produc.getString(1) + "','" + Produc.getString(2) + "','" + Produc.getString(3) + "','" + Produc.getString(4) + "','" + Produc.getString(5) + "',"
                                    + "'" + Produc.getString(6) + "','" + Produc.getString(7) + "','" + Produc.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                        }
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if ((Grupo1 == 3) && (Grupo2 == 3)) {/////CONTRATO X CONTRATO/////
                    System.out.println("CONTRATO X CONTRATO");
                    Consultas2 = "SELECT F_Contrato,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                            + " WHERE ";
                    Group2 = " GROUP BY F_Contrato";
                    Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;
                    Contrato = con.consulta(Query2);
                    while (Contrato.next()) {

                        F_CReq = F_CReq + Contrato.getInt(2);
                        F_CSur = F_CSur + Contrato.getInt(3);
                        F_CTotal = F_CTotal + Contrato.getDouble(4);
                        F_CSer = F_CSer + Contrato.getDouble(5);
                        F_PzNSur = F_PzNSur + Contrato.getInt(6);
                        F_CNSur = F_CNSur + Contrato.getDouble(7);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Contrato='" + Contrato.getString(1) + "'  AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal2 = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Contrato.getString(1) + "','" + Contrato.getString(1) + "','" + Contrato.getString(2) + "','" + Contrato.getString(3) + "','" + Contrato.getString(4) + "','" + Contrato.getString(5) + "',"
                                + "'" + Contrato.getString(6) + "','" + Contrato.getString(7) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;
                } else if (Grupo1 == 3 && Grupo2 == 4) { ///// CONTRATO X UNIDADES ////
                    System.out.println("CONTRATO X UNIDADES");
                    Consultas = "SELECT F_Contrato,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS INNER JOIN tb_juriis J ON U.F_JurUniIS=J.F_ClaJurIS "
                            + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                            + " WHERE ";
                    Group = " GROUP BY F_Contrato";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Contrato = con.consulta(Query);
                    while (Contrato.next()) {
                        F_CReq = F_CReq + Contrato.getInt(2);
                        F_CSur = F_CSur + Contrato.getInt(3);
                        F_CTotal = F_CTotal + Contrato.getDouble(4);
                        F_CSer = F_CSer + Contrato.getDouble(5);
                        F_PzNSur = F_PzNSur + Contrato.getInt(6);
                        F_CNSur = F_CNSur + Contrato.getDouble(7);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Contrato='" + Contrato.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal2 = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }

                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Contrato.getString(1) + "','" + Contrato.getString(1) + "','" + Contrato.getString(2) + "','" + Contrato.getString(3) + "','" + Contrato.getString(4) + "','" + Contrato.getString(5) + "',"
                                + "'" + Contrato.getString(6) + "','" + Contrato.getString(7) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");

                        Consultas2 = "SELECT F_Cveuni,F_DesUniIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                                + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                                + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS "
                                + "WHERE T.F_Contrato='" + Contrato.getString(1) + "' AND ";
                        Group2 = " GROUP BY F_Cveuni,F_DesUniIS";
                        Query2 = Consultas2 + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group2;

                        Unidades = con.consulta(Query2);
                        while (Unidades.next()) {

                            if ((Suministro == 0) || (Suministro == 2505)) {

                                Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                        + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                        + "WHERE M.F_TipMed='2505' AND T.F_Contrato='" + Contrato.getString(1) + "' AND T.F_Cveart='" + Unidades.getString(1) + "'  AND ";

                                Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                                Iva = con.consulta(Query2);

                                while (Iva.next()) {
                                    IvaTotal2 = Iva.getDouble(1);
                                }
                            }

                            con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Unidades.getString(1) + "','" + Unidades.getString(2) + "','" + Unidades.getString(3) + "','" + Unidades.getString(4) + "','" + Unidades.getString(5) + "',"
                                    + "'" + Unidades.getString(6) + "','" + Unidades.getString(7) + "','" + Unidades.getString(8) + "','" + IvaTotal2 + "','" + sesion.getAttribute("nombre") + "')");
                        }
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                } else if (Grupo1 == 4 && Grupo2 == 4) { ///// UNIDADES X UNIDADES ////
                    System.out.println("UNIDADES X UNIDADES");
                    Consultas = "SELECT F_Cveuni,F_DesUniIS,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CosUni*F_Cansur) AS F_CostTal ,"
                            + "SUM(F_CosServ) AS F_CosServ,SUM(F_Cansur-F_Canreq) AS F_PzNoSurt,SUM((F_Cansur-F_Canreq) * F_CosUni) AS F_CostNoSurt "
                            + "FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS AND T.F_CveJur=U.F_JurUniIS "
                            + "WHERE ";
                    Group = " GROUP BY F_Cveuni,F_DesUniIS";
                    Query = Consultas + Part6 + Part5 + Part1 + Part2 + Part3 + Part4 + Group;
                    Unidades = con.consulta(Query);
                    while (Unidades.next()) {

                        F_CReq = F_CReq + Unidades.getInt(3);
                        F_CSur = F_CSur + Unidades.getInt(4);
                        F_CTotal = F_CTotal + Unidades.getDouble(5);
                        F_CSer = F_CSer + Unidades.getDouble(6);
                        F_PzNSur = F_PzNSur + Unidades.getInt(7);
                        F_CNSur = F_CNSur + Unidades.getDouble(8);

                        if ((Suministro == 0) || (Suministro == 2505)) {
                            Consultas2 = "SELECT SUM((T.F_Cansur * T.F_CosUni) * 0.16) FROM tb_txtis T INNER JOIN tb_unidis U ON T.F_Cveuni=U.F_ClaUniIS "
                                    + "INNER JOIN tb_artiis A ON T.F_Cveart=A.F_ClaArtIS INNER JOIN tb_medica M ON A.F_ClaInt=M.F_ClaPro "
                                    + "WHERE M.F_TipMed='2505' AND T.F_Cveuni='" + Unidades.getString(1) + "' AND ";

                            Query2 = Consultas2 + Part6 + Part5 + Part1 + Part3 + Part4;
                            Iva = con.consulta(Query2);
                            while (Iva.next()) {
                                IvaTotal = Iva.getDouble(1);
                                F_Iva = F_Iva + Iva.getDouble(1);
                            }
                        }
                        con.insertar("INSERT INTO tb_txtreporte VALUES(0,'" + Unidades.getString(1) + "','" + Unidades.getString(2) + "','" + Unidades.getString(3) + "',"
                                + "'" + Unidades.getString(4) + "','" + Unidades.getString(5) + "','" + Unidades.getString(6) + "','" + Unidades.getString(7) + "','" + Unidades.getString(8) + "','" + IvaTotal + "','" + sesion.getAttribute("nombre") + "')");
                    }
                    con.insertar("INSERT INTO tb_txtreporte VALUES(0,'','Total','" + F_CReq + "','" + F_CSur + "','" + F_CTotal + "','" + F_CSer + "','" + F_PzNSur + "','" + F_CNSur + "','" + F_Iva + "','" + sesion.getAttribute("nombre") + "')");
                    F_CReq = 0;
                    F_CSur = 0;
                    F_CTotal = 0.0;
                    F_CSer = 0.0;
                    F_PzNSur = 0;
                    F_CNSur = 0.0;
                    F_Iva = 0.0;

                }

                out.println(" <script>window.open('ReportesPuntos/ReporteTxt.jsp?User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                out.println("<script>window.history.back()</script>");
            } else {
                out.println("<script>alert('Favor de Seleccionar CAMPOS VACÍOS')</script>");
                out.println("<script>window.history.back()</script>");
            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
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
