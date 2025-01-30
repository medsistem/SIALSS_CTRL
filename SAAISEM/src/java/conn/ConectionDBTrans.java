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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Conexión a la Base de Datos isem transaccional
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ConectionDBTrans {

//variables miembro
    private String usuario;
    private String clave;
    private String url;
    private String driverClassName;
    private Connection conn = null;
    private Statement estancia;
    public Object objeto;

    public String id_medico;

//CONSTRUCTORES
    //Constructor que toma los datos de conexion por medio de parametros
    public ConectionDBTrans(String usuario, String clave, String url, String driverClassName) {
        this.usuario = usuario;
        this.clave = clave;
        this.url = url;
        this.driverClassName = driverClassName;
    }

//    Constructor que crea la conexion sin parametros con unos definidos en la clase
    //(meter los datos correspondientes)
    public ConectionDBTrans() {

        this.usuario = "sialss_ctrl";
        this.clave = "51lssCt234@!iofn";
        
        this.url = "jdbc:mariadb://127.0.0.1:3306/SIALSS_CTRL?autoReconnect=true";
//      this.url = "jdbc:mariadb://192.168.0.180:3306/SIALSS_CTRL?autoReconnect=true";
//      this.url = "jdbc:mariadb://192.168.0.184:3306/SIALSS_CTRL?autoReconnect=true"; 
        this.driverClassName = "org.mariadb.jdbc.Driver";


    }

    //metodos para recuperar los datos de conexion
    public String getClave() {
        return clave;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public Connection getConn() {
        return conn;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    //metodos para establecer los valores de conexion
    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsuario(String usuario) throws SQLException {
        this.usuario = usuario;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

//la conexion propiamente dicha
    public void conectar() throws SQLException {
        try {
            Class.forName(this.driverClassName).newInstance();
            this.conn = DriverManager.getConnection(this.url, this.usuario, this.clave);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            Logger.getLogger(ConectionDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void cierraConexion() throws SQLException {
        this.conn.close();
    }

//METODOS PARA TRABAJAR CON LA BASE DE DATOS
    public ResultSet consulta(String consulta) throws SQLException {
        this.estancia = (Statement) conn.createStatement();
        return this.estancia.executeQuery(consulta);
    }

    public void actualizar(String actualiza) throws SQLException {
        this.estancia = (Statement) conn.createStatement();
        estancia.executeUpdate(actualiza);
    }

    public ResultSet borrar(String borra) throws SQLException {
        Statement st = (Statement) this.conn.createStatement();
        return (ResultSet) st.executeQuery(borra);
    }

    public void borrar2(String borra) throws SQLException {
        this.estancia = (Statement) conn.createStatement();
        estancia.executeUpdate(borra);
    }

    public int insertar(String inserta) throws SQLException {
        Statement st = (Statement) this.conn.createStatement();
        return st.executeUpdate(inserta);
    }
}
