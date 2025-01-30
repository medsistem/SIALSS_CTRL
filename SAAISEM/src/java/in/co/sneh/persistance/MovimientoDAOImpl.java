/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class MovimientoDAOImpl {

    private Connection con;

    public MovimientoDAOImpl(Connection con) {
        this.con = con;
    }
    
    public void addMovimiento(Movimiento m){
        try {
            String qry = "INSERT INTO tb_movinv VALUES (?,curdate(),?,?,?,?,?,?,?,?,?,?,curtime(),?,?);";
            PreparedStatement psInsert = this.con.prepareStatement(qry);
            psInsert.setInt(1, 0);
            
            psInsert.setString(2, m.getDocMov());
            psInsert.setInt(3, m.getConMov());
            psInsert.setString(4, m.getProMov());
            psInsert.setInt(5, m.getCantMov());
            psInsert.setDouble(6, m.getCostMov());
            psInsert.setDouble(7, m.getTotalMov());
            psInsert.setInt(8, m.getSigMov());
            psInsert.setInt(9, m.getLotMov());
            psInsert.setString(10, m.getUbiMov());
            psInsert.setInt(11, m.getClaProve());
            
            psInsert.setString(12, m.getUser());
            psInsert.setString(13, m.getComentarios() == null ? "": m.getComentarios());
            psInsert.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
