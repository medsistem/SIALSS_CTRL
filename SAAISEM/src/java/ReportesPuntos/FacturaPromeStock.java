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
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpSession;
//import sun.org.mozilla.javascript.internal.ast.Loop;

/**
 * Calcula facturación promedio
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class FacturaPromeStock extends HttpServlet {

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

            String Fecha1 = "", Fecha2 = "";
            String Dia = "", Dia1 = "", Mes = "", Mes1 = "";
            int Cont = 0, F_Dia = 0, F_Dia1 = 0, F_Mes = 0, F_Mes1 = 0, F_Ano = 0, F_Ano1 = 0, F_FacPro = 0;
            float F_FacPro1 = 0, NDias = 0;

            ResultSet Consulta = null;
            ResultSet Datos = null;
            ResultSet MaxMin = null;

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);

            Fecha1 = request.getParameter("fecha_ini");
            Fecha2 = request.getParameter("fecha_fin");
            if (request.getParameter("accion").equals("Generar")) {
                if ((Fecha1 != "") && (Fecha2 != "")) {

                    F_Dia = Integer.parseInt(Fecha2.substring(8, 10));
                    F_Mes = Integer.parseInt(Fecha2.substring(5, 7));
                    F_Ano = Integer.parseInt(Fecha2.substring(0, 4));

                    F_Dia1 = Integer.parseInt(Fecha1.substring(8, 10));
                    F_Mes1 = Integer.parseInt(Fecha1.substring(5, 7));
                    F_Ano1 = Integer.parseInt(Fecha1.substring(0, 4));

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
                    final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
                    Calendar FechaMenor1 = new GregorianCalendar(F_Ano1, F_Mes1, F_Dia1);
                    Calendar FechaMayor1 = new GregorianCalendar(F_Ano, F_Mes, F_Dia);

                    java.sql.Date FechaMenor = new java.sql.Date(FechaMenor1.getTimeInMillis());
                    java.sql.Date FechaMayor = new java.sql.Date(FechaMayor1.getTimeInMillis());

                    long diferencia = (FechaMayor.getTime() - FechaMenor.getTime()) / MILLSECS_PER_DAY;
                    NDias = (float) diferencia;
                    System.out.println("Dia" + NDias);

                    con.actualizar("UPDATE tb_exiua SET F_FacPro='0' WHERE F_CalSN = 'S'");

                    Consulta = con.consulta("SELECT F_ClaCli,LTRIM(rtrim(F_ClaPro)),SUM(F_CantSur) AS F_CantSur FROM tb_factura WHERE F_FecEnt BETWEEN '" + Fecha1 + "' AND '" + Fecha2 + "' AND F_StsFact='A' GROUP BY F_ClaCli,F_ClaPro");
                    while (Consulta.next()) {

                        Datos = con.consulta("SELECT * FROM tb_exiua Where F_ClaUA = '" + Consulta.getString(1) + "' AND F_ClaMed = '" + Consulta.getString(2) + "'");
                        while (Datos.next()) {
                            Cont++;
                        }
                        F_FacPro1 = Math.round((Consulta.getFloat(3) / NDias) * 30);
                        System.out.println(Consulta.getInt(3) + "/" + diferencia + "*" + 30 + " Factu: " + F_FacPro1);
                        F_FacPro = (int) F_FacPro1;
                        if (Cont > 0) {
                            con.actualizar("UPDATE tb_exiua  SET F_FacPro ='" + F_FacPro + "' Where F_ClaUA = '" + Consulta.getString(1) + "' AND F_ClaMed = '" + Consulta.getString(2) + "'");
                        } else {
                            con.insertar("INSERT INTO tb_exiua VALUES('" + Consulta.getString(1) + "','" + Consulta.getString(2) + "','0','0','" + F_FacPro + "','0','0','0','0',curdate(),'S','0','0','0','0','0',CURDATE(),0)");
                        }
                        F_FacPro1 = 0;
                        F_FacPro = 0;
                    }
                    out.println("<script>alert('Datos Generados Correctamente')</script>");
                    out.println("<script>window.location.href = 'ReportesPuntos/FactPromedio.jsp';</script>");

                } else {
                    out.println("<script>alert('Favor de Seleccionar Fechas')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }
            if (request.getParameter("accion").equals("Stock")) {
                int Maximo = 0, Minimo = 0, Punto = 0;
                float F_Min = 0, F_Max = 0, F_Punto = 0;
                MaxMin = con.consulta("SELECT F_Max,F_Min,F_Punto FROM tb_parametros");
                if (MaxMin.next()) {
                    F_Max = MaxMin.getFloat(1) / 100;
                    F_Min = MaxMin.getFloat(2) / 100;
                    F_Punto = MaxMin.getFloat(3) / 100;
                }

                Consulta = con.consulta("SELECT F_ClaUA,F_ClaMed,F_FacPro FROM tb_exiua");

                while (Consulta.next()) {
                    F_FacPro1 = Consulta.getFloat(3);
                    Maximo = Math.round((F_Max * F_FacPro1) + F_FacPro1);
                    Minimo = Math.round(F_FacPro1 - (F_Min * F_FacPro1));
                    Punto = Math.round((F_Punto * F_FacPro1) + F_FacPro1);

                    con.actualizar("UPDATE tb_exiua SET F_Maximo='" + Maximo + "', F_Minimo='" + Minimo + "', F_Punto='" + Punto + "' WHERE F_ClaUA='" + Consulta.getString(1) + "' and F_ClaMed='" + Consulta.getString(2) + "' and F_FacPro='" + Consulta.getString(3) + "'");
                }
                out.println("<script>alert('Datos Generados Correctamente')</script>");
                out.println("<script>window.location.href = 'ReportesPuntos/FactPromedio.jsp';</script>");

            }
            if (request.getParameter("accion").equals("1ERA")) {
                con.actualizar("delete from tb_exiua");
                String Tipo = "";
                int Cata = 0;
                Datos = con.consulta("SELECT F_ClaCli,F_Tipo FROM tb_uniatn GROUP BY F_ClaCli");
                while (Datos.next()) {
                    Tipo = Datos.getString(2);
                    if (Tipo.equals("RURAL")) {
                        Cata = 1;
                    } else if (Tipo.equals("CSU")) {
                        Cata = 2;
                    } else if (Tipo.equals("CEAPS")) {
                        Cata = 2;
                    } else if (Tipo.equals("CAD")) {
                        Cata = 14;
                    } else if (Tipo.equals("GEDIATRICAS")) {
                        Cata = 17;
                    } else if (Tipo.equals("CEAPSGER")) {
                        Cata = 217;
                    }

                    Consulta = con.consulta("SELECT F_ClaPro FROM tb_medica WHERE F_N" + Cata + "='1';");
                    while (Consulta.next()) {
                        con.insertar("INSERT INTO tb_exiua VALUES('" + Datos.getString(1) + "','" + Consulta.getString(1) + "','0','0','0','0','0','0','0',curdate(),'S','0','0','0','0','0',CURDATE(),0)");
                    }
                    Cata = 0;
                    Tipo = "";
                }
                out.println("<script>alert('Datos Generados Correctamente')</script>");
                out.println("<script>window.location.href = 'ReportesPuntos/FactPromedio.jsp';</script>");
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
