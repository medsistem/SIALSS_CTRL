/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.picking.persistance;

import in.co.sneh.persistance.ApartadoDAOImpl;
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
public class FolioDAOImpl {
    Connection con;

    public FolioDAOImpl(Connection con) {
        this.con = con;
    }
    
    public Integer getFolioStatus(Integer folio, Integer proyecto){
        try {
            String query = "SELECT STATUS FROM federated_folios where ID_CLIENT = ? AND FOLIO = ?";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, proyecto);
            ps.setInt(2, folio);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return -1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ApartadoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
