/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Volumetria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class VolumetriaDAOImpl {

    Connection con;

    public VolumetriaDAOImpl(Connection con) {
        this.con = con;
    }

    public int guardar(Volumetria v){
        try {
            String query = "INSERT INTO volumetria VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setDouble(1, v.getPesoPieza());
            ps.setString(2, v.getUnidadPesoPieza());
            ps.setDouble(3, v.getPesoCaja());
            ps.setString(4, v.getUnidadPesoCaja());
            ps.setDouble(5, v.getPesoConcentrada());
            ps.setString(6, v.getUnidadPesoConcentrada());
            ps.setDouble(7, v.getPesoTarima());
            ps.setString(8, v.getUnidadPesoTarima());
            ps.setDouble(9, v.getAltoPieza());
            ps.setDouble(10, v.getAnchoPieza());
            ps.setDouble(11, v.getLargoPieza());
            ps.setString(12, v.getUnidadVolPieza());
            ps.setDouble(13, v.getAltoCaja());
            ps.setDouble(14, v.getAnchoCaja());
            ps.setDouble(15, v.getLargoCaja());
            ps.setString(16, v.getUnidadVolCaja());
            ps.setDouble(17, v.getAltoConcentrada());
            ps.setDouble(18, v.getAnchoConcentrada());
            ps.setDouble(19, v.getLargoConcentrada());
            ps.setString(20, v.getUnidadVolConcentrada());
            ps.setDouble(21, v.getAltoTarima());
            ps.setDouble(22, v.getAnchoTarima());
            ps.setDouble(23, v.getLargoTarima());
            ps.setString(24, v.getUnidadVolTarima());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    
    
    
    private Volumetria buildVolumetria(ResultSet rs){
        try {
            Volumetria v = new Volumetria();
            v.setId(rs.getLong(1));
            v.setPesoPieza(rs.getDouble(2));
            v.setUnidadPesoPieza(rs.getString(3));
            v.setPesoCaja(rs.getDouble(4));
            v.setUnidadPesoCaja(rs.getString(5));
            v.setPesoConcentrada(rs.getDouble(6));
            v.setUnidadPesoConcentrada(rs.getString(7));
            v.setPesoTarima(rs.getDouble(8));
            v.setUnidadPesoTarima(rs.getString(9));
            v.setAltoPieza(rs.getDouble(10));
            v.setAnchoPieza(rs.getDouble(11));
            v.setLargoPieza(rs.getDouble(12));
            v.setUnidadVolPieza(rs.getString(13));
            v.setAltoCaja(rs.getDouble(14));
            v.setAnchoCaja(rs.getDouble(15));
            v.setLargoCaja(rs.getDouble(16));
            v.setUnidadVolCaja(rs.getString(17));
            v.setAltoConcentrada(rs.getDouble(18));
            v.setAnchoConcentrada(rs.getDouble(19));
            v.setLargoConcentrada(rs.getDouble(20));
            v.setUnidadVolConcentrada(rs.getString(21));
            v.setAltoTarima(rs.getDouble(22));
            v.setAnchoTarima(rs.getDouble(23));
            v.setLargoTarima(rs.getDouble(24));
            v.setUnidadVolTarima(rs.getString(25));
            
            return v;
        } catch (SQLException ex) {
            Logger.getLogger(VolumetriaDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
