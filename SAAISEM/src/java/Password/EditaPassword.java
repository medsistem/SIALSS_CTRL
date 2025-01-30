package Password;

import Correo.CorreoModiPass;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Usuarios;

/**
 * Modifica Password usuarios
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class EditaPassword extends HttpServlet {

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
        Usuarios usuario = new Usuarios();
        CorreoModiPass correo = new CorreoModiPass();
        PreparedStatement ps;
        ResultSet rs = null;
        String Consulta = "", Destino = "", accion = "", IdUsuario = "", Sts = "", Usuarios = "", StsActual = "";
        int Contar = 0;
        Destino = "/Password/Editapassword.jsp";
        accion = request.getParameter("action");
        if (accion == null) {
            accion = "";
        }
        try {
            con.conectar();
            if (accion.equals("Modifica")) {
                IdUsuario = request.getParameter("Id");
                Consulta = "SELECT F_IdUsu,F_Usu,F_Nombre,F_Apellido,F_ApellidoM,F_Correo,F_Status FROM tb_usuario WHERE F_IdUsu=?;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, IdUsuario);
                rs = ps.executeQuery();
                while (rs.next()) {
                    usuario.setId(rs.getString(1));
                    usuario.setUsuario(rs.getString(2));
                    usuario.setNombre(rs.getString(3));
                    usuario.setApaterno(rs.getString(4));
                    usuario.setAmaterno(rs.getString(5));
                    usuario.setCorreo(rs.getString(6));
                    usuario.setSts((!rs.getString(7).equals("S")) ? "A" : rs.getString(7));
                }

                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher(Destino).forward(request, response);
            }

            if (accion.equals("ModificaSts")) {
                IdUsuario = request.getParameter("Id");
                Usuarios = request.getParameter("Usuarios");
                Sts = request.getParameter("stadoSwitch");
                StsActual = request.getParameter("StsActual");
                if (Sts == null) {
                    Sts = "off";
                }
                //System.out.println("stsff"+Sts);
                Consulta = "UPDATE tb_usuario SET F_Status=? WHERE F_IdUsu=?;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, (Sts.equals("on")) ? "A" : "S");
                ps.setString(2, IdUsuario);
                ps.execute();
                ps.clearParameters();
                if ((StsActual.equals("S")) && (Sts.equals("on"))) {
                    Consulta = "";
                    Consulta = "SELECT COUNT(F_id) FROM tb_usuariosuspendidos WHERE F_FechaAct = '0000-00-00' AND F_Usuario = ?;";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, Usuarios);
                    //System.out.println("ContarUsuario" + ps);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Contar = rs.getInt(1);
                    }

                    if (Contar > 0) {
                        ps.clearParameters();
                        Consulta = "";
                        Consulta = "UPDATE tb_usuariosuspendidos SET F_FechaAct = CURDATE() WHERE F_FechaAct = '0000-00-00' AND F_Usuario = ?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Usuarios);
                        //System.out.println("Actualizafechasusp" + ps);
                        ps.execute();
                        ps.clearParameters();
                        Consulta = "";
                        Consulta = "DELETE FROM tb_registroentradas WHERE F_Usu = ? AND F_Correcto=0;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Usuarios);
                        //System.out.println("eliminareg" + ps);
                        ps.execute();

                    }
                }

                request.setAttribute("modificadoOk", "Usuario Modificado Estatus");
                request.getRequestDispatcher("/ListaUsuario?Accion=Listausuario").forward(request, response);
            }

            if (accion.equals("Actualizar")) {
                String Id = request.getParameter("Id");
                String Usu = request.getParameter("Usuarios");
                String Nombre = request.getParameter("Nombre");
                String ApellidoP = request.getParameter("ApellidoP");
                String ApellidoM = request.getParameter("ApellidoM");
                String Correo = request.getParameter("Correo");
                String pswd = request.getParameter("pswd");
                Consulta = "UPDATE tb_usuario SET F_Pass=MD5(?),F_ApellidoM=?,F_Correo=? WHERE F_IdUsu=? AND F_Usu=?";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, pswd);
                ps.setString(2, ApellidoM);
                ps.setString(3, Correo);
                ps.setString(4, Id);
                ps.setString(5, Usu);
                System.out.println("Password.EditaPassword.processRequest()"+ps);
                ps.execute();
                //con.actualizar("UPDATE tb_usuario SET F_Pass=MD5('" + pswd + "'),F_ApellidoM='" + ApellidoM + "',F_Correo='" + Correo + "' WHERE F_IdUsu='" + Id + "' AND F_Usu='" + Usu + "';");
                //DESCOMENTAR CUANDO SE VALIDE LA CUENTA DE CORREO
//                correo.enviaCorreo(Correo, Usu, pswd);
                request.setAttribute("modificadoOk", "Usuario Modificado Password");
                request.getRequestDispatcher("/ListaUsuario?Accion=Listausuario").forward(request, response);
            }

            if (accion.equals("ActualizarPass")) {
                String Id = request.getParameter("Id");
                String pswd = request.getParameter("pswd");
                String Correo = request.getParameter("Correo");
                String Usu = request.getParameter("Usuarios");
                Consulta = "SELECT MD5(?), F_Pass from tb_usuario where F_IdUsu = ?";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, pswd);
                ps.setString(2, Id);
                ResultSet comparaPassRs = ps.executeQuery();
                comparaPassRs.next();
                String oldPass = comparaPassRs.getString(1);
                String newPass = comparaPassRs.getString(2);
                if (oldPass.equals(newPass)) {
                    response.sendRedirect("Password/EditaPasswordUsuario.jsp?error=samePass");
                } else {
                    Consulta = "UPDATE tb_usuario SET F_Pass=MD5(?), F_FecMod = curdate() WHERE F_IdUsu=?";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, pswd);
                    ps.setString(2, Id);
                    System.out.println("Password.EditaPassword.processRequest()" + ps);
                    ps.execute();
                    //con.actualizar("UPDATE tb_usuario SET F_Pass=MD5('" + pswd + "'),F_ApellidoM='" + ApellidoM + "',F_Correo='" + Correo + "' WHERE F_IdUsu='" + Id + "' AND F_Usu='" + Usu + "';");
                    correo.enviaCorreo(Correo, Usu, pswd);
                    request.setAttribute("modificadoOk", "Usuario Modificado Password");
                    request.getRequestDispatcher("/").forward(request, response);
                }
            }
            
            con.cierraConexion();

        } catch (SQLException ex) {
            Logger.getLogger(EditaPassword.class.getName()).log(Level.SEVERE, null, ex);
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
