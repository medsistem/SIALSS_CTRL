/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abastos;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 * Proceso para generar abasto SGW
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class AbastoGNKLite extends HttpServlet {

    AbastoGNKLitExport Abasto = new AbastoGNKLitExport();
    ExportarBd bd = new ExportarBd();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        String mensaje = "";
        String Folio = request.getParameter("accion");
        System.out.println("Folio--> " + Folio);
        try {
            mensaje = bd.Exportar((String) sesion.getAttribute("id_usu"), (String) Folio);
        } catch (SQLException ex) {
            Logger.getLogger(AbastoGNKLite.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println("<script>alert('" + mensaje + "')</script>");
        out.println("<script>window.location='facturacion/reimp_transferencia.jsp'</script>");
    }

}
