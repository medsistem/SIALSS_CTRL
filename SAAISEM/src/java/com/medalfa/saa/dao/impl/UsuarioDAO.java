/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.dao.impl;

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
public class UsuarioDAO {
    
    
    Connection c;

    public UsuarioDAO(Connection c) {
        this.c = c;
    }
    
    public Integer getLastModified(Integer idUsuario){
        try {
            String queryString = "SELECT DATEDIFF(curdate(), F_FecMod)  from tb_usuario where F_IdUsu = ?";
            PreparedStatement ps = this.c.prepareStatement(queryString);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Integer getLastModifiedCompras(Integer idUsuario){
        try {
            String queryString = "SELECT DATEDIFF(curdate(), F_FecMod)  from tb_usuariocompra where F_IdUsu = ?";
            PreparedStatement ps = this.c.prepareStatement(queryString);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void updateLastModified(Integer idUsuario){
        try {
            String queryString = "UPDATE tb_usuario set F_FecMod = curdate() where F_IdUsu = ?";
            PreparedStatement ps = this.c.prepareStatement(queryString);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
