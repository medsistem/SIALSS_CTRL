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
 * Consulta de  requerimiento procesado por unidad
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Requerimiento extends HttpServlet {

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
            ResultSet DatosU = null;
            ResultSet ClaUni = null;
            ResultSet Unidades = null;
            ResultSet Unidades2 = null;
            ResultSet Facturado = null;
            ResultSet DatosMed = null;

            String Fecha1 = "", Fecha2 = "", Impresora = "", NomUnidad = "", Fecha = "", F_ClaUni = "", F_CP7 = "", F_Ori = "", F_Tipo = "", F_Unidad = "", F_Fact = "";
            int Radio = 0, Radio2 = 0, F_Men = 0, F_Ent = 0;

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);

            Fecha1 = request.getParameter("fecha_ini");
            Fecha2 = request.getParameter("fecha_fin");
            Radio = Integer.parseInt(request.getParameter("radio1"));
            Radio2 = Integer.parseInt(request.getParameter("radio2"));
            Impresora = request.getParameter("impresora");
            NomUnidad = request.getParameter("NombreUnidad");
            Fecha = request.getParameter("Fecha");
            System.out.println("Fecha" + Fecha);
            int Cont = 0, F_CP1 = 0;
            if (request.getParameter("accion").equals("imprimir")) {
                if (Impresora != "") {
                    if (Radio2 == 1) {
                        con.actualizar("delete from tb_auxra");
                        con.actualizar("delete from tb_auxrai");

                        ClaUni = con.consulta("SELECT F_ClaUniIS FROM tb_unidis WHERE F_DesUniIS='" + NomUnidad + "'");
                        if (ClaUni.next()) {
                            F_ClaUni = ClaUni.getString(1);
                        }
                        DatosU = con.consulta("SELECT A.F_ClaInt,A.F_ClaArtIS, M.F_DesPro, (E.F_FacPro * 0.5), (E.F_FacPro), (E.F_FacPro * 1), M.F_Origen, M.F_CP7, M.F_TIPO, M.F_PesoGra, E.F_ClaUA, UNI.F_Ruta, U.F_ClaUniIS, "
                                + "U.F_DesUniIS, U.F_LocUniIS, L.F_DesLocIS, U.F_MunUniIS, MUN.F_DesMunIS, U.F_JurUniIS, J.F_DesJurIS "
                                + "FROM (((((((tb_artiis AS A  INNER JOIN tb_medica M    ON A.F_ClaInt = M.F_ClaPro)  INNER JOIN tb_exiua E    ON E.F_ClaMed = A.F_ClaInt ) "
                                + "INNER JOIN tb_unidis U    ON U.F_ClaInt1 = E.F_ClaUA     OR U.F_ClaInt2 = E.F_ClaUA     OR U.F_ClaInt3 = E.F_ClaUA     OR U.F_ClaInt4 = E.F_ClaUA     "
                                + "OR U.F_ClaInt5 = E.F_ClaUA     OR U.F_ClaInt6 = E.F_ClaUA     OR U.F_ClaInt7 = E.F_ClaUA     OR U.F_ClaInt8 = E.F_ClaUA     OR U.F_ClaInt9 = E.F_ClaUA     OR U.F_ClaInt10 = E.F_ClaUA )  "
                                + "INNER JOIN tb_juriis J    ON U.F_JurUniIS = J.F_ClaJurIS ) LEFT JOIN tb_locais L    ON U.F_LocUniIS = L.F_ClaLocIS     AND U.F_MunUniIS = L.F_MunLocIS ) "
                                + "INNER JOIN tb_muniis MUN    ON U.F_MunUniIS = MUN.F_ClaMunIS     AND U.F_JurUniIS = MUN.F_JurMunIS)  INNER JOIN tb_uniatn UNI    ON E.F_ClaUA = UNI.F_ClaCli)  "
                                + "WHERE M.F_StsPro = 'A' AND UNI.F_StsCli = 'A' AND U.F_ClaUniIS = '" + F_ClaUni + "' AND M.F_Catipo = '1'  "
                                + "ORDER BY U.F_ClaUniIS, E.F_ClaUA, A.F_ClaInt");
                        while (DatosU.next()) {

                            con.actualizar("INSERT INTO tb_auxra VALUES('" + DatosU.getString(1) + "','" + DatosU.getString(2) + "','" + DatosU.getString(3) + "','" + DatosU.getInt(4) + "','" + DatosU.getString(5) + "','" + DatosU.getString(6) + "','0','" + DatosU.getString(7) + "','" + DatosU.getString(8) + "',"
                                    + "'" + DatosU.getString(9) + "','" + DatosU.getString(10) + "','" + DatosU.getString(11) + "','" + DatosU.getString(12) + "','" + DatosU.getString(13) + "','" + DatosU.getString(14) + "','" + DatosU.getString(15) + "','" + DatosU.getString(16) + "','" + DatosU.getString(17) + "','" + DatosU.getString(18) + "','" + DatosU.getString(19) + "','" + DatosU.getString(20) + "',0)");
                        }
                        Unidades = con.consulta("SELECT F_ClaPro,F_Pro,F_UA FROM tb_auxra GROUP BY F_Pro,F_UA ORDER BY F_Pro,F_UA asc");
                        while (Unidades.next()) {
                            Facturado = con.consulta("SELECT SUM(F_CantSur) AS F_CantSur FROM tb_factura WHERE F_ClaCli='" + Unidades.getString(3) + "' AND F_FecEnt='" + Fecha + "' AND F_ClaPro='" + Unidades.getString(1) + "'");
                            if (Facturado.next()) {
                                F_Fact = Facturado.getString(1);
                                if (F_Fact == null) {
                                    F_Fact = "0";
                                }
                                con.actualizar("UPDATE tb_auxra SET F_Ent='" + F_Fact + "' WHERE F_UA='" + Unidades.getString(3) + "' AND F_ClaPro='" + Unidades.getString(1) + "'");
                            }
                        }
                        Unidades2 = con.consulta("SELECT F_UA FROM tb_auxra GROUP BY F_UA ORDER BY F_UA asc");
                        while (Unidades2.next()) {
                            F_Unidad = Unidades2.getString(1);
                            DatosMed = con.consulta("SELECT F_Pro,F_Des,F_Min,F_Men,F_Max,F_Ent,F_CP1,F_CP7 FROM tb_auxra WHERE F_UA='" + Unidades2.getString(1) + "'");
                            while (DatosMed.next()) {
                                Cont++;

                                F_CP1 = DatosMed.getInt(7);
                                F_CP7 = DatosMed.getString(8);
                                if (F_CP1 == 1) {
                                    F_Tipo = "ADM";
                                }
                                if (F_CP7.equals("EST")) {
                                    if (F_Ori.equals("ADM")) {
                                        F_Tipo = "A-O";
                                    } else {
                                        F_Tipo = "ODO";
                                    }
                                }
                                con.actualizar("INSERT INTO tb_auxrai VALUES('" + Cont + "','" + DatosMed.getString(1) + "','" + F_Tipo + "','" + DatosMed.getString(2) + "','" + DatosMed.getString(3) + "','" + DatosMed.getString(4) + "','" + DatosMed.getString(5) + "','" + DatosMed.getString(6) + "','" + Cont + "','" + Unidades2.getString(1) + "',0)");
                                F_Tipo = "";
                            }

                            Cont = 0;
                        }
                        out.println(" <script>window.open('ReportesPuntos/RequeImp.jsp?User=" + sesion.getAttribute("nombre") + "&Impresora=" + Impresora + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");

                    } else {

                    }
                    out.println("<script>window.history.back()</script>");
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }

            }
            if (request.getParameter("accion").equals("exportar")) {
                out.println("<script>alert('En proceso de desarrollo')</script>");
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
