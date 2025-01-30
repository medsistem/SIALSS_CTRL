/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.medalfa.saa.dao.impl.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;
import java.sql.*;
import conn.*;
import in.co.sneh.model.Usuario;
import in.co.sneh.picking.service.SessionManagerService;

/**
 * Validación de usuarios para el ingreso al sistema SAA
 *
 * @author MEDALFA SOFTWARE
 * @param usuario
 * @param paswword
 * @version 1.40
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {

    ConectionDB con = new ConectionDB();

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
        HttpSession sesion = request.getSession(true);
        try {
            /* TODO output your page here. You may use following sample code. */
            try {
                con.conectar();
                UsuarioDAO dao = new UsuarioDAO(con.getConn());
                try {
                    String F_Usu = "", F_nombre = "", F_TipUsu = "", F_IdUsu = "", F_Area = "", F_Proyecto = "";
                    int ban = 0, Contar = 0;
                    Usuario usuario = null;
                    ResultSet rset = con.consulta("SELECT F_Usu, F_nombre, F_Status, F_TipUsu, F_IdUsu, F_Area, F_Proyecto FROM tb_usuario where F_Usu = '" + request.getParameter("nombre") + "' and F_Pass = MD5('" + request.getParameter("pass") + "') AND F_Status = 'A'; ");
                    while (rset.next()) {
                        ban = 1;
                        usuario = this.getUsuario(rset);
                    }
                    if (ban == 1) {//----------------------EL USUARIO ES VÁLIDO
                        boolean otherSession = SessionManagerService.closeSessionId(usuario.getId()+"");
                        Integer daysAgo = dao.getLastModified(usuario.getId());
                        this.llenarSesion(request, sesion, usuario);
                        if (daysAgo < 90) {
                            if (otherSession) {
                                sesion.setAttribute("mensaje", "Se cerró otra sesión que estaba activa");
                            }
                            response.sendRedirect("main_menu.jsp");
                        } else {
                            response.sendRedirect("Password/EditaPasswordUsuario.jsp");
                        }

                    } 
                    else {//--------------------------EL USUARIO NO ES VÁLIDO
                        //out.println("hola");
                        con.insertar("INSERT INTO tb_registroentradas VALUES ('" + request.getParameter("nombre") + "',NOW(),0,0)");
                        ResultSet rsetContar = con.consulta("SELECT COUNT(F_Usu) FROM tb_registroentradas WHERE F_Usu = '" + request.getParameter("nombre") + "' AND F_Correcto = 0;");
                        if (rsetContar.next()) {
                            Contar = rsetContar.getInt(1);
                        }
                        if (Contar >= 4) {
                            con.insertar("UPDATE tb_usuario SET F_Status = 'S' WHERE F_Usu = '" + request.getParameter("nombre") + "';");
                            Contar = 0;
                            rsetContar = con.consulta("SELECT COUNT(F_id) FROM tb_usuariosuspendidos WHERE F_FechaAct = '0000-00-00' AND F_Usuario = '" + request.getParameter("nombre") + "';");
                            if (rsetContar.next()) {
                                Contar = rsetContar.getInt(1);
                            }
                            if (Contar == 0) {
                                con.insertar("INSERT INTO tb_usuariosuspendidos VALUES(0,'" + request.getParameter("nombre") + "',CURDATE(),'0000-00-00');");
                            }

                            sesion.setAttribute("mensaje", "Usuario Suspendido, favor de contactar al departamento de sistemas.");
                        } else {
                            sesion.setAttribute("mensaje", "Datos incorrectos, Intente de nuevo...");
                        }

                        response.sendRedirect("index.jsp");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } finally {
            out.close();
        }
    }
    
    private void suspenderUsuario(HttpServletRequest request) throws SQLException {
        con.insertar("UPDATE tb_usuario SET F_Status = 'S' WHERE F_Usu = '" + request.getParameter("nombre") + "';");
        ResultSet rsetContar = con.consulta("SELECT F_id FROM tb_usuariosuspendidos WHERE F_FechaAct = '0000-00-00' AND F_Usuario = '" + request.getParameter("nombre") + "';");
        if (!rsetContar.next()) {
            con.insertar("INSERT INTO tb_usuariosuspendidos VALUES(0,'" + request.getParameter("nombre") + "',CURDATE(),'0000-00-00');");
        }
    }
    
    private void llenarSesion(HttpServletRequest request, HttpSession sesion, Usuario usuario) throws SQLException {
        sesion.setAttribute("Usuario", usuario.getUsuario());
        sesion.setAttribute("nombre", usuario.getNombre());
        sesion.setAttribute("Tipo", usuario.getTipo());
        sesion.setAttribute("IdUsu", usuario.getId() + "");
        sesion.setAttribute("Area", usuario.getArea());
        sesion.setAttribute("ProyectoCL", usuario.getProyecto());
        sesion.setAttribute("posClave", "0");
        sesion.setAttribute("tipoIngreso", "normal");
        con.insertar("INSERT INTO tb_registroentradas VALUES ('" + request.getParameter("nombre") + "',NOW(),1,0)");
        con.insertar("DELETE FROM tb_registroentradas WHERE F_Usu = '" + request.getParameter("nombre") + "' AND F_Correcto = 0;");

    }

    private Usuario getUsuario(ResultSet rset) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setUsuario(rset.getString("F_Usu"));
        usuario.setNombre(rset.getString("F_nombre"));
        usuario.setTipo(rset.getString("F_TipUsu"));
        usuario.setId(rset.getInt("F_IdUsu"));
        usuario.setArea(rset.getString("F_Area"));
        usuario.setProyecto(rset.getString("F_Proyecto"));
        return usuario;
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

}// end of the Class
