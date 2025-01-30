package ReportesPuntos;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Relaciona factura txt isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class RelacionaFact extends HttpServlet {

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
        DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ConectionDB con = new ConectionDB();
        JSONObject json = new JSONObject();
        JSONArray jsona = new JSONArray();
        HttpSession sesion = request.getSession(true);
        String fecha1 = df.format(new Date());
        String fecha2 = "2030-01-01";
        String fecha_ini = "", fecha_fin = "", radio = "";
        try {
            fecha_ini = request.getParameter("fecha_ini");
            fecha_fin = request.getParameter("fecha_fin");
            radio = request.getParameter("radio");
        } catch (Exception ex) {
        }
        int F_Idsur = 0, F_IdePro = 0, F_Cvesum = 0, vp_FolCon = 0;
        System.out.println("hola");
        System.out.println("hola12" + fecha_ini + fecha_fin + radio);
        try {
            con.conectar();
            try {
                ResultSet rset = con.consulta("SELECT DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur, F_FacGNKLAgr, F_Idsur, F_IdePro, F_Cvesum, F_FacSAVI "
                        + "FROM (((((TB_TXTIS AS TXT  INNER JOIN TB_UnidIS AS UNI ON (TXT.F_Cveuni = UNI.F_ClaUniIS) AND (TXT.F_CveJur = UNI.F_JurUniIS)) "
                        + "LEFT JOIN TB_LocaIS as LOC ON (UNI.F_LocUniIS=LOC.F_ClaLocIS) AND UNI.F_MunUniIS=LOC.F_MunLocIS)  "
                        + "INNER JOIN TB_MuniIS as MUN ON (UNI.F_MunUniIS = MUN.F_ClaMunIS))  "
                        + "INNER JOIN TB_JuriIS as JUR ON (UNI.F_JurUniIS=JUR.F_ClaJurIS))  "
                        + "LEFT JOIN TB_CoorIS as Coo ON (UNI.F_CooUniIS=Coo.F_ClaCooIS)) "
                        + "WHERE TXT.F_Fecsur >= '" + df.format(df2.parse(fecha_ini)) + "' AND TXT.F_Fecsur <= '" + df.format(df2.parse(fecha_fin)) + "' "
                        + "AND (F_Status <> 'C' OR F_Status IS NULL)  AND F_FacSAVI = ''  AND F_FacGNKLAgr LIKE 'AG-0%'  "
                        + "GROUP BY F_Fecsur, F_FacGNKLAgr, F_IdSur, F_IdePro, F_Cvesum,  F_FacSAVI, F_Cveuni, F_JurUniIS, F_ClaCooIS  ;");
                while (rset.next()) {
                    F_Idsur = rset.getInt("F_Idsur");
                    F_IdePro = rset.getInt("F_IdePro");
                    F_Cvesum = rset.getInt("F_Cvesum");

                    if ((F_Idsur == 1) && (F_IdePro == 0) && (F_Cvesum == 1)) {
                        vp_FolCon = 1;
                    } else if ((F_Idsur == 1) && (F_IdePro == 1) && (F_Cvesum == 1)) {
                        vp_FolCon = 2;
                    } else if ((F_Idsur == 1) && (F_IdePro == 0) && (F_Cvesum == 2)) {
                        vp_FolCon = 3;
                    } else if ((F_Idsur == 1) && (F_IdePro == 1) && (F_Cvesum == 2)) {
                        vp_FolCon = 4;
                    } else if ((F_Idsur == 2) && (F_IdePro == 0) && (F_Cvesum == 1)) {
                        vp_FolCon = 5;
                    } else if ((F_Idsur == 2) && (F_IdePro == 1) && (F_Cvesum == 1)) {
                        vp_FolCon = 6;
                    } else if ((F_Idsur == 2) && (F_IdePro == 0) && (F_Cvesum == 2)) {
                        vp_FolCon = 7;
                    } else if ((F_Idsur == 2) && (F_IdePro == 1) && (F_Cvesum == 2)) {
                        vp_FolCon = 8;
                    }

                    json.put("F_Fecsur", rset.getString("F_Fecsur"));
                    json.put("F_FacGNKLAgr", rset.getString("F_FacGNKLAgr"));
                    json.put("F_FacSAVI", rset.getString("F_FacSAVI"));
                    json.put("vp_FolCon", vp_FolCon);

                    jsona.add(json);
                    json = new JSONObject();
                }
                out.println(jsona);
                System.out.println(jsona);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            con.cierraConexion();
        } catch (SQLException e) {
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
