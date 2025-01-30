/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.FolioStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class FolioStatusDAOImpl {
    
    Connection con;
    
    public FolioStatusDAOImpl(Connection con){
        this.con=con;
    }
    
    public boolean guardar(FolioStatus status){
        
        try {
            String qry = "INSERT INTO tb_facturastatus VALUES(0,?,?,?);";
            PreparedStatement ps = this.con.prepareStatement(qry);
            ps.setInt(1, status.getClaDoc());
            ps.setInt(2, status.getProyecto());
            ps.setInt(3, status.getStatus());
            return ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(FolioStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void updateStatus(Integer cladoc, Integer proyecto, Integer status){
        try {
            String qry = "UPDATE tb_facturastatus SET F_Status = ? where F_ClaDoc = ? AND F_Proyecto = ?;";
            PreparedStatement ps = this.con.prepareStatement(qry);
            ps.setInt(1, status);
            ps.setInt(2, proyecto);
            ps.setInt(3, cladoc);
        } catch (SQLException ex) {
            Logger.getLogger(FolioStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
