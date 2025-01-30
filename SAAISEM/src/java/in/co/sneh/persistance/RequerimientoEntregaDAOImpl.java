/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.RequerimientoEntrega;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Israel
 */
public class RequerimientoEntregaDAOImpl {
    Connection con;
    SimpleDateFormat df;
    
    public RequerimientoEntregaDAOImpl(Connection con){
        this.con=con;
        this.df = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    /**
     * Inserta en requerimiento_entrega
     * 
     * @param r Datos a insertar
     */
    public void guardar(RequerimientoEntrega r){
        try {
            String query = "INSERT INTO requerimiento_entrega VALUES(0,?,?,?);";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, r.getClaveUnidad());
            ps.setString(2, df.format(r.getFechaEntrega()));
            ps.setInt(3, r.getFolio());
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public RequerimientoEntrega findById(Integer id){
        try{
            String query = "SELECT * from requerimiento_entrega where id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return this.buildRequerimientoEntrega(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public RequerimientoEntrega updateById(Integer id, String fecha){
        try{
            String query = "Update requerimiento_entrega set fecha_entrega = ? where id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, fecha);
            ps.setInt(2, id);
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public RequerimientoEntrega findByClaveUnidadAndFolio(String claveUnidad, Integer folio){
        try{
            String query = "SELECT * from requerimiento_entrega where clave_unidad = ? AND folio = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, claveUnidad);
            ps.setInt(2, folio);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return this.buildRequerimientoEntrega(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private RequerimientoEntrega buildRequerimientoEntrega(ResultSet rs){
        RequerimientoEntrega r = new RequerimientoEntrega();
        try {
            r.setId(rs.getInt("id"));
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            r.setClaveUnidad(rs.getString("clave_unidad"));
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            r.setFechaEntrega(rs.getDate("fecha_entrega"));
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            r.setFolio(rs.getInt("folio"));
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
