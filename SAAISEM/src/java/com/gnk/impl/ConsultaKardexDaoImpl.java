/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.ConsultaKardexDao;
import static com.gnk.impl.ExistenciaProyectoDaoImpl.BuscaProyecto;
import conn.ConectionDBTrans;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author MEDALFA
 */
public class ConsultaKardexDaoImpl implements ConsultaKardexDao {

    public static String BuscaClave = "SELECT F_ClaPro,CONCAT(F_ClaPro,' - ',F_DesPro) AS F_DesPro FROM tb_medica GROUP BY F_ClaPro;";
    
    private final ConectionDBTrans con = new ConectionDBTrans();
    private PreparedStatement psConsulta;
    private ResultSet rs;

    @Override
    public JSONArray ObtenerClave() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        try {
            con.conectar();
            psConsulta = con.getConn().prepareStatement(BuscaClave);
            rs = psConsulta.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Clave", rs.getString(1));
                jsonObj.put("Descripcion", rs.getString(2));
                jsonArray.add(jsonObj);
            }
            //psBuscaProyecto.close();
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaKardexDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaKardexDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

}
