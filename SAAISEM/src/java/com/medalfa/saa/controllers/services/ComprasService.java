/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import com.medalfa.saa.querys.ComprasQuerys;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author IngMa
 */
public class ComprasService 
{
    public JSONArray existenciaPorProyecto(Connection conn, int proyecto)
    {
        JSONArray informacion = new JSONArray();
        JSONObject json;
        
        try (PreparedStatement ps = conn.prepareStatement(ComprasQuerys.OBTENER_EXISTENCIAS_POR_PROYECTO))
        {
            ps.setInt(1, proyecto);
            ps.setInt(2, proyecto);
            try (ResultSet rs = ps.executeQuery()) 
            {
                while(rs.next())
                {
                    json = new JSONObject();
                    json.put("clave", rs.getString("clave"));
                    json.put("descripcion", rs.getString("descripcion"));
                    json.put("letra", rs.getString("letra"));
                    json.put("existenciaDisponible", rs.getInt("existenciaDisponible"));
                    json.put("existenciaTemporal", rs.getInt("existenciaTemporal"));
                    json.put("existenciaTotal", rs.getInt("existenciaTotal"));
                    informacion.put(json);
                }
            }            
            
        } catch (SQLException ex) {
            Logger.getLogger(ComprasService.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return informacion;
        
    }
    
}
