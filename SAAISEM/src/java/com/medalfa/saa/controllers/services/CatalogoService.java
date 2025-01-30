/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import com.medalfa.saa.querys.CatalogoQuerys;
import static com.medalfa.saa.utils.StaticText.BUSCAR_CLAVE;
import static com.medalfa.saa.utils.StaticText.BUSCAR_DESCRIPCION;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Esta clase maneja las operaciones con el catálogo
 *
 * @author Cisneros
 */
public class CatalogoService {

    /**
     * Método para consultar las claves y descripciones dentro del catálogo
     *
     * @param Connection Desde el controller
     * @return JSONArray
     * @throws SQLException Si crear una nueva conexión.
     * @throws NamingException Si encuentra el recurso.
     */
    public JSONArray autocompleteClaveDescripcion(Connection conn) {

        JSONArray catalogo = new JSONArray();
        JSONObject json;
        try (PreparedStatement ps = conn.prepareStatement(CatalogoQuerys.OBTENER_CLAVE_DESCRIPCION)) {
            try (ResultSet rset = ps.executeQuery()) {
                while (rset.next()) {
                    json = new JSONObject();
                    json.put("clave", rset.getString("clave"));
                    json.put("descripcion", rset.getString("descripcion"));
                    catalogo.put(json);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return catalogo;

    }

    /**
     * Método para consultar información de la clave o descripción enviada,
     * retorna lotes y caducidades que ha tenido y su existencia actual
     *
     *
     * @param Connection Desde el controller
     * @return JSONObject
     * @throws SQLException Si crear una nueva conexión.
     * @throws NamingException Si encuentra el recurso.
     */
    public JSONObject informacionClave(Connection conn, String cricterio, String item, String fechaFinal) {
        JSONObject informacion = new JSONObject();
        JSONObject json;
        JSONObject headerClave = new JSONObject();
        JSONArray lotesCaducidad = new JSONArray();

        String consulta = "";
        
        switch (cricterio) {

            case BUSCAR_CLAVE:
                consulta = CatalogoQuerys.OBTENER_ENCABEZADO_CLAVE;
                break;

            case BUSCAR_DESCRIPCION:

                consulta = CatalogoQuerys.OBTENER_ENCABEZADO_DESCRIPCION;

                break;

        }

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, item);
            
            try (ResultSet rset = ps.executeQuery()) {
                while (rset.next()) {
                    headerClave.put("clave", rset.getString("clave"));
                    headerClave.put("descripcion", rset.getString("descripcion"));
                    headerClave.put("existencia", rset.getInt("existencia"));
                }
            }

            if (!fechaFinal.equals("")) {
                int ExistenciaPorFechas = 0;
                PreparedStatement psFechasExistencia = conn.prepareStatement(CatalogoQuerys.OBTENER_ENCABEZADO_CLAVE_FECHAS);
                psFechasExistencia.setString(1, headerClave.getString("clave"));
                psFechasExistencia.setString(2, fechaFinal);
                psFechasExistencia.setString(3, headerClave.getString("clave"));
                try (ResultSet rset = psFechasExistencia.executeQuery()) {
                    while (rset.next()) {
                       
                        ExistenciaPorFechas = rset.getInt("CantMov");
                    }
                }
                headerClave.remove("existencia");
                headerClave.put("existencia", ExistenciaPorFechas);

            }

            PreparedStatement psBody = conn.prepareStatement(CatalogoQuerys.OBTENER_LOTES_POR_CLAVE);
            psBody.setString(1, headerClave.getString("clave"));

            try (ResultSet rsetBody = psBody.executeQuery()) {
                while (rsetBody.next()) {
                    json = new JSONObject();
                    json.put("lote", rsetBody.getString("lote"));
                    json.put("caducidad", rsetBody.getString("caducidad"));
                    json.put("origen", rsetBody.getString("origen"));
                    json.put("idLote", rsetBody.getInt("idLote"));
                    json.put("idOrigen", rsetBody.getInt("idOrigen"));
                    lotesCaducidad.put(json);
                }
            }

            informacion.put("header", headerClave);
            informacion.put("body", lotesCaducidad);

        } catch (SQLException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return informacion;

    }

    public JSONObject informacionLote(Connection conn, int folLot) {
        JSONObject informacion = new JSONObject();

        try (PreparedStatement ps = conn.prepareStatement(CatalogoQuerys.OBTENER_INFORMACION_POR_FOLLOT)) {
            ps.setInt(1, folLot);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    informacion.put("origen", rs.getString("descripcionOrigen"));
                    informacion.put("idOrigen", rs.getInt("idOrigen"));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return informacion;
    }

    public JSONObject informacionLote(Connection conn, String clave, String lote, String caducidad, int origen, String fechaFinal) {
        JSONObject informacion = new JSONObject();

        try (PreparedStatement ps = conn.prepareStatement(CatalogoQuerys.OBTENER_INFORMACION_POR_LOTE_CADUCIDAD)) {
            ps.setString(1, clave);
            ps.setString(2, lote);
            ps.setString(3, caducidad);
            ps.setInt(4, origen);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    informacion.put("clave", rs.getString("clave"));
                    informacion.put("descripcion", rs.getString("descripcion"));
                    informacion.put("existencia", rs.getInt("existencia"));
                }
            }
            
            if (!fechaFinal.equals("")) 
            {
                int ExistenciaPorFechas = 0;
                PreparedStatement psFechasExistencia = conn.prepareStatement(CatalogoQuerys.OBTENER_ENCABEZADO_CLAVE_LOTE_CADUCIDAD_FECHAS);
                psFechasExistencia.setString(1, clave);
                psFechasExistencia.setString(2, lote);
                psFechasExistencia.setString(3, caducidad);
                psFechasExistencia.setInt(4, origen);
                psFechasExistencia.setString(5, clave);
                psFechasExistencia.setString(6, fechaFinal);
                
                 try (ResultSet rset = psFechasExistencia.executeQuery()) {
                    while (rset.next()) {
                       
                        ExistenciaPorFechas = rset.getInt("existencia");
                    }
                }
                 
                 informacion.remove("existencia");
                 informacion.put("existencia", ExistenciaPorFechas);                
            }         
            

        } catch (SQLException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return informacion;
    }

}
