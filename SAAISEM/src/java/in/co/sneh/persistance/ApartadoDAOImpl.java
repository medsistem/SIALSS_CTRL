/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Apartado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class ApartadoDAOImpl {

    Connection con;
    SimpleDateFormat df;

    public ApartadoDAOImpl(Connection con) {
        this.con = con;
        this.df = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void guardar(Apartado a) {
        try {
            String query = "INSERT INTO tb_apartado VALUES(0,?,?,?,?,?);";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, a.getIdLote());
            ps.setInt(2, a.getCant());
            ps.setInt(3, a.getProyecto());
            ps.setInt(4, a.getStatus());
            ps.setString(5, a.getClaDoc());
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public List<Apartado> getByClaDocAndProyect(String claDoc, Integer proyect) {
        List<Apartado> lista = new ArrayList<Apartado>();
        try {
            String query = "SELECT * from tb_apartado where F_ClaDoc = ? AND F_Proyecto= ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, claDoc);
            ps.setInt(2, proyect);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(this.buildApartado(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public List<Apartado> getByClaDoc(String claDoc) {
        List<Apartado> lista = new ArrayList<Apartado>();
        try {
            String query = "SELECT * from tb_apartado where F_ClaDoc = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, claDoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(this.buildApartado(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public Apartado getByIdLoteAndClaDoc(Integer idLote, String claDoc) {
        try {
            String query = "SELECT * from tb_apartado where F_ClaDoc = ? AND F_IdLote = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, claDoc);
            ps.setInt(2, idLote);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return this.buildApartado(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Apartado buildApartado(ResultSet rs) {
        Apartado a = new Apartado();
        try {
            a.setId(rs.getInt("F_Id"));
        } catch (SQLException ex) {
        }
        try {
            a.setIdLote(rs.getInt(rs.getInt("F_IdLote")));
        } catch (SQLException ex) {
        }
        try {
            a.setCant(rs.getInt("F_Cant"));
        } catch (SQLException ex) {
        }
        try {
            a.setProyecto(rs.getInt("F_Proyecto"));
        } catch (SQLException ex) {
        }
        try {
            a.setStatus(rs.getInt("F_Status"));
        } catch (SQLException ex) {
        }
        try {
            a.setClaDoc(rs.getString("F_ClaDoc"));
        } catch (SQLException ex) {
        }
        return a;
    }

    public void delete(Integer id) {
        try {
            String query = "DELETE from tb_apartado where F_Id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateIdLote(Integer idApartado, Integer idLote){
        try {
            String query = "UPDATE tb_apartado SET F_IdLote = ? where F_Id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, idLote);
            ps.setInt(2, idApartado);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateCant(Integer idApartado, Integer cant){
        try {
            String query = "UPDATE tb_apartado SET F_Cant = ? where F_Id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, cant);
            ps.setInt(2, idApartado);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateStatus(Integer idApartado, Integer status){
        try {
            String query = "UPDATE tb_apartado SET F_Status = ? where F_Id = ?;";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, status);
            ps.setInt(2, idApartado);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
