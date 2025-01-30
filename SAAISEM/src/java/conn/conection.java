/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conexión a la Base de Datos isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class conection {

    //Varibles para la conexión
    private String UsuarioBD;
    private String Password;
    private String Url;
    private String DriverClassName;
    private Connection Conn = null;
    private Statement estancia;
    private Object Obj;

    public conection(String Usuario, String Password, String Url, String DriverClassName) {
        this.UsuarioBD = Usuario;
        this.Password = Password;
        this.Url = Url;
        this.DriverClassName = DriverClassName;
    }

    public conection() {
        
        this.UsuarioBD = "sialss_ctrl";
        this.Password = "51lssCt234@!iofn";
        
        this.Url = "jdbc:mariadb://127.0.0.1:3306/SIALSS_CTRL";
//          this.Url = "jdbc:mariadb://192.168.0.180:3306/SIALSS_CTRL";
//      this.Url = "jdbc:mariadb://192.168.0.184:3306/SIALSS_CTRL?autoReconnect=true"; 
        this.DriverClassName = "org.mariadb.jdbc.Driver";            
        
    }

    // Metodo de Recuperación los datos de Conexión
    public String getUsurioBD() {
        return UsuarioBD;
    }

    public String getPassword() {
        return Password;
    }

    public String getUrl() {
        return Url;
    }

    public Connection getConn() {
        return Conn;
    }

    public String getDriverClassName() {
        return DriverClassName;
    }

    // Metodo Establecer los valores de Conexión
    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setUsuarioBD(String UsuarioBD) {
        this.UsuarioBD = UsuarioBD;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setConn(Connection Conn) {
        this.Conn = Conn;
    }

    //Conexion
    public void conectar() throws SQLException {

        try {
            Class.forName(this.DriverClassName).newInstance();
            this.Conn = DriverManager.getConnection(this.Url, this.UsuarioBD, this.Password);
            System.out.println("Conexión Exitosa mysql gnklmex_consolidada");

        } catch (Exception err) {
            System.out.println("Error" + err.getMessage());
        }
    }

    // cierre de conexión
    public void CierreConn() throws SQLException {
        this.Conn.close();
    }

    //METODOS PARA TRABAJAR CON LA BASE DE DATOS
    public ResultSet consulta(String consulta) throws SQLException {
        System.out.println(consulta);
        this.estancia = (Statement) Conn.createStatement();
        return this.estancia.executeQuery(consulta);
    }

    // -------------------
    public void actualizar(String actualiza) throws SQLException {
        this.estancia = (Statement) Conn.createStatement();
        System.out.println(actualiza);
        estancia.executeUpdate(actualiza);
        //this.conn.commit();
    }

    public ResultSet borrar(String borra) throws SQLException {
        Statement st = (Statement) this.Conn.createStatement();
        return (ResultSet) st.executeQuery(borra);
    }

    public void borrar2(String borra) throws SQLException {
        this.estancia = (Statement) Conn.createStatement();
        estancia.executeUpdate(borra);
    }

    public int insertar(String inserta) throws SQLException {
        Statement st = (Statement) this.Conn.createStatement();
        System.out.println(inserta);
        return st.executeUpdate(inserta);
    }
}
