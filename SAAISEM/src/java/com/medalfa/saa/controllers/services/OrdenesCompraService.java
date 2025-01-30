/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import com.medalfa.saa.querys.OrdenesCompraQuerys;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author IngMa
 */
public class OrdenesCompraService {

    public JSONArray ordenesDeCompraNoCompra(Connection conn, String noOrden) {
        JSONArray information = new JSONArray();
        JSONObject json;

        try (PreparedStatement ps = conn.prepareStatement(OrdenesCompraQuerys.OBTENER_POR_NO_ORDEN)) {
            ps.setString(1, noOrden);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noCompra", rs.getString("F_NoCompra"));
                    json.put("fecha", rs.getString("F_FecSur"));
                    json.put("proveedor", rs.getString("F_NomPro"));
                    information.put(json);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrdenesCompraService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return information;

    }

    public JSONArray ordenesDeCompraByProvider(Connection conn, String proveedor) {
        JSONArray information = new JSONArray();
        JSONObject json;
        
         try (PreparedStatement ps = conn.prepareStatement(OrdenesCompraQuerys.OBTENER_POR_PROVEEDOR)) {
            ps.setString(1, proveedor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noCompra", rs.getString("F_NoCompra"));
                    json.put("fecha", rs.getString("F_FecSur"));
                    json.put("proveedor", rs.getString("F_NomPro"));
                    information.put(json);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrdenesCompraService.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return information;
    }
    
    public JSONObject insertOrden(Connection conn, String noOrden, String idUsu)
    {
        JSONObject result = new JSONObject();
        try (PreparedStatement ps = conn.prepareStatement(OrdenesCompraQuerys.INSERT_ESTATUS_ORDEN)) {
            ps.setString(1, noOrden);
            ps.setString(2, idUsu);
            ps.executeUpdate();
            result.put("msj", "true");
                
            

        } catch (SQLException ex) {
            result.put("msj", "false");
            Logger.getLogger(OrdenesCompraService.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        return result;
    }
    public JSONArray ordenesCompraCerradas(Connection conn)
    {
        JSONArray information = new JSONArray();
        JSONObject json;
         try (PreparedStatement ps = conn.prepareStatement(OrdenesCompraQuerys.OBTENER_REPORTE_ORDEN_CERRADAS)) {
          
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noCompra", rs.getString("noOrden"));
                    json.put("fecha", rs.getString("fechaCerrado"));
                    json.put("usuario", rs.getString("usuario"));
                    information.put(json);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrdenesCompraService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return information;
    }

}
