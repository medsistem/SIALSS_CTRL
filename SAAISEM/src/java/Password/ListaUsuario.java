package Password;

import conn.ConectionDB;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.Usuarios;

/**
 * Muestra listado de usuarios de ingreso al sistema
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ListaUsuario extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        List<Usuarios> listaUsuario = new ArrayList<Usuarios>();
        Usuarios usuario;
        PreparedStatement ps;
        ResultSet rs;
        String Consulta;
        HttpSession sesion = request.getSession(true);
        String Accion = request.getParameter("Accion");

        try {
            con.conectar();
            if (Accion.equals("Listausuario")) {
                Consulta = "SELECT F_IdUsu,F_Usu,F_Nombre,F_Apellido,F_ApellidoM,F_Correo,F_Status FROM tb_usuario;";
                ps = con.getConn().prepareStatement(Consulta);
                rs = ps.executeQuery();
                while (rs.next()) {
                    usuario = new Usuarios();
                    usuario.setId(rs.getString(1));
                    usuario.setUsuario(rs.getString(2));
                    usuario.setNombre(rs.getString(3));
                    usuario.setApaterno(rs.getString(4));
                    usuario.setAmaterno(rs.getString(5));
                    usuario.setCorreo(rs.getString(6));
                    usuario.setSts(rs.getString(7));
                    listaUsuario.add(usuario);
                }
                request.setAttribute("listaUsuario", listaUsuario);
                request.getRequestDispatcher("/Password/ListaUsuario.jsp").forward(request, response);

            } else if (Accion.equals("ListaSuspendido")) {
                Consulta = "SELECT US.F_id, CONCAT( U.F_Nombre, ' ', U.F_Apellido, ' ', F_ApellidoM ) AS F_Nombre, DATE_FORMAT(US.F_FechaSusp, '%d/%m/%Y') AS F_FechaSusp, DATE_FORMAT(US.F_FechaAct, '%d/%m/%Y') AS F_FechaAct FROM tb_usuariosuspendidos US INNER JOIN tb_usuario U ON US.F_Usuario = U.F_Usu;";
                ps = con.getConn().prepareStatement(Consulta);
                rs = ps.executeQuery();
                while (rs.next()) {
                    usuario = new Usuarios();
                    usuario.setId(rs.getString(1));
                    usuario.setNombre(rs.getString(2));
                    usuario.setFechasusp(rs.getString(3));
                    usuario.setFechaact(rs.getString(4));
                    listaUsuario.add(usuario);
                }
                request.setAttribute("listaUsuario", listaUsuario);
                request.getRequestDispatcher("/Password/UsuarioSuspendido.jsp").forward(request, response);
            }
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ListaUsuario.class.getName()).log(Level.SEVERE, null, ex);
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
