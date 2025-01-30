package Parametros;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Modificación de parametros para la facturación
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ParametroCsrd extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        HttpSession sesion = request.getSession(true);
        /**
         * *PARAMERS DE FACTURACIÓN CSRD**
         */
        try {
            con.conectar();
//            if (request.getParameter("accion").equals("ActualizarP")) {
//                int radio = Integer.parseInt(request.getParameter("radio"));
//                String Datos = "";
//                if (radio == 1) {
//                    Datos = "MODULA,A0S,APE,DENTAL";
//                    con.insertar("UPDATE tb_parametro SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Date=NOW()");
//                } else if (radio == 2) {
//                    Datos = "MODULA2,A0S,APE,DENTAL";
//                    con.insertar("UPDATE tb_parametro SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Date=NOW()");
//                } else if (radio == 3) {
//                    Datos = "A0S,APE,DENTAL";
//                    con.insertar("UPDATE tb_parametro SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Date=NOW()");
//                } else {
//                    con.insertar("UPDATE tb_parametro SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Date=NOW()");
//                    Datos = "A0S,APE,DENTAL";
//                }
//                System.out.println("m: " + Datos);
//                Datos = "";
//
//                out.println("<script>alert('Datos Actualizados')</script>");
//                out.println("<script>window.location='Parametrofactura.jsp'</script>");
//
//            }

            if (request.getParameter("accion").equals("ActualizarU")) {
                int Contar = 0;
                String Datos = "";
                int radio = Integer.parseInt(request.getParameter("SltParametro"));
                int Proyecto = Integer.parseInt(request.getParameter("SltProyecto"));
                if (Proyecto > 0) {
                    ResultSet Consulta = con.consulta("SELECT COUNT(*) FROM tb_parametrousuario WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
                    if (Consulta.next()) {
                        Contar = Consulta.getInt(1);
                    }
                    if (Contar > 0) {
                         ResultSet Conp = con.consulta("Select * from tb_ubicafact where F_idUbicaFac = '"+radio+"'");
                        
                        if (Conp.next()) {
                            Datos = Conp.getString(2);
                            
                            con.insertar("UPDATE tb_parametrousuario SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Proyecto='" + Proyecto + "', F_Date=NOW() WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
                        } 
                        
//                        if (radio == 1) {
//                            Datos = "MODULA,A0S,APE,DENTAL";
//                            con.insertar("UPDATE tb_parametrousuario SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Proyecto='" + Proyecto + "', F_Date=NOW() WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
//                        } else if (radio == 2) {
//                            Datos = "MODULA2,A0S,APE,DENTAL";
//                            con.insertar("UPDATE tb_parametrousuario SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Proyecto='" + Proyecto + "', F_Date=NOW() WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
//                        } else if (radio == 3) {
//                            Datos = "A0S,APE,DENTAL";
//                            con.insertar("UPDATE tb_parametrousuario SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Proyecto='" + Proyecto + "', F_Date=NOW() WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
//                        } else {
//                            Datos = "MODULA,MODULA2";
//                            con.insertar("UPDATE tb_parametrousuario SET F_Id='" + radio + "', F_Parametro='" + Datos + "', F_Usuario='" + sesion.getAttribute("nombre") + "', F_Proyecto='" + Proyecto + "', F_Date=NOW() WHERE F_Usuario='" + sesion.getAttribute("nombre") + "';");
//                        }
                        System.out.println("m: " + Datos);
                        Datos = "";
                    } else {
                          ResultSet Conp = con.consulta("Select * from tb_ubicafact where F_idUbicaFac = '"+radio+"'");
                        
                        if (Conp.next()) {
                            Datos = Conp.getString(2);
                           con.insertar("INSERT INTO tb_parametrousuario VALUES( '" + radio + "', '" + Datos + "', '" + sesion.getAttribute("nombre") + "','" + Proyecto + "', NOW());");
                     } 
//                        if (radio == 1) {
//                            Datos = "MODULA,A0S,APE,DENTAL";
//                        } else if (radio == 2) {
//                            Datos = "MODULA2,A0S,APE,DENTAL";
//                        } else if (radio == 3) {
//                            Datos = "A0S,APE,DENTAL";
//                        } else {
//                            Datos = "MODULA,MODULA2";
//                        }
//                        con.insertar("INSERT INTO tb_parametrousuario VALUES( '" + radio + "', '" + Datos + "', '" + sesion.getAttribute("nombre") + "','" + Proyecto + "', NOW());");
                    }
                    sesion.setAttribute("F_IndGlobal", null);
                    out.println("<script>alert('Datos Actualizados')</script>");
                } else {
                    out.println("<script>alert('Seleccione Proyecto')</script>");
                }
                out.println("<script>window.location='ParametrofacturaU.jsp'</script>");

            }
            /**
             * *FIN PARAMERS DE FACTURACIÓN CSRD**
             */
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
