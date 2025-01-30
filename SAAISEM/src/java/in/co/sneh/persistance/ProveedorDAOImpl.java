/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HP-MEDALFA
 */
public class ProveedorDAOImpl {
    private Connection con;
    
    public ProveedorDAOImpl(Connection con){
        this.con=con;
    }
    
    public Proveedor findByName(String name){
        try{
        String sql = "SELECT F_ClaProve, F_NomPro from tb_proveedor where F_NomPro= ?";
        PreparedStatement ps= this.con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Proveedor p = new Proveedor();
            p.setClaProvee(rs.getInt("F_ClaProve"));
            p.setNombre(rs.getString("F_NomPro"));
            return p;
        }
        
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
