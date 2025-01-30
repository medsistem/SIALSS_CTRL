package ReportesPuntos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Procesamiendo de bd sql e insertar información a bd mysql
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class DatabaseReseter extends HttpServlet {

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
            Statement stmt = null;
            Connection Ceaps;
            Class.forName("org.mariadb.jdbc.Driver");
            Ceaps = DriverManager.getConnection("jdbc:mariadb://localhost:3306/scr_ceaps", "root", "eve9397");
            stmt = Ceaps.createStatement();

            String s = new String();
            StringBuffer sb = new StringBuffer();

            try {
                FileReader fr = new FileReader(new File("C:\\Users\\Sistemas\\Desktop\\scr_ceaps20150623-1124.sql"));
                // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character 
                BufferedReader br = new BufferedReader(fr);

                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                br.close();

                String[] inst = sb.toString().split(";");
                Connection c = Database.getConnection();
                Statement st = c.createStatement();
                for (int i = 0; i < inst.length; i++) {
                    // we ensure that there is no spaces before or after the request string
                    // in order to not execute empty statements
                    if (!inst[i].trim().equals("")) {
                        st.executeUpdate(inst[i]);
                        System.out.println(">>" + inst[i]);
                    }
                }
            } catch (Exception e) {
                System.out.println("*** Error : " + e.toString());
                System.out.println("*** ");
                System.out.println("*** Error : ");
                e.printStackTrace();
                System.out.println("################################################");
                System.out.println(sb.toString());
            }
        } catch (Exception e) {

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
