/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

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
public class CatalogosDAO {

    private Connection con;

    private static String isControladoQuery = "Select * from tb_controlados where F_ClaPro = ?";

    public CatalogosDAO(Connection con) {
        this.con = con;
    }

    public boolean esControlado(String claPro) {
        try {
            PreparedStatement ps = this.con.prepareStatement(isControladoQuery);
            ps.setString(1, claPro);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
            
        } catch (SQLException ex) {
            Logger.getLogger(CatalogosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
