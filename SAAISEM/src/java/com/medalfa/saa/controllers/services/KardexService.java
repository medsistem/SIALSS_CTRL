/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import com.medalfa.saa.querys.kardexQuerys;
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
public class KardexService {

    public JSONObject kardexInformation(Connection conn, String clave, String fechaInicial, String fechaFinal) {
        JSONArray information = new JSONArray();
        JSONArray informationRedistribucion = new JSONArray();
        JSONObject json;
        JSONObject result = new JSONObject();
        String query = "";
        String queryRedistribucion = "";

        if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
            query = String.format(kardexQuerys.OBTENER_KARDEX_POR_CLAVE, "AND m.F_FecMov BETWEEN ? AND ?");
            queryRedistribucion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_CLAVE, "AND m.F_FecMov BETWEEN ? AND ?");
        } else {
            query = String.format(kardexQuerys.OBTENER_KARDEX_POR_CLAVE, "");
            queryRedistribucion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_CLAVE, "");
        }

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clave);
            ps.setString(2, clave);
            ps.setString(3, clave);
            ps.setString(4, clave);
            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                ps.setString(5, fechaInicial);
                ps.setString(6, fechaFinal);
            }
            System.out.println(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noMov", rs.getInt("noMov"));
                    json.put("usuario", rs.getString("usuario"));
                    json.put("usuariocaptura", rs.getString("usuariocapt"));
                    json.put("usuariolibera", rs.getString("usuariolib"));
                    json.put("documentoCompra", rs.getString("documentoCompra"));
                    json.put("ori", rs.getString("ori"));
                    json.put("remision", rs.getString("remision"));
                    json.put("proveedor", rs.getString("proveedor"));
                    json.put("folioSalida", rs.getString("folioSalida"));
                    json.put("puntoEntrega", rs.getString("puntoEntrega"));
                    json.put("concepto", rs.getString("concepto"));
                    json.put("clave", rs.getString("clave"));
                    json.put("lote", rs.getString("lote"));
                    json.put("caducidad", rs.getString("caducidad"));
                    json.put("cantidadMovimiento", rs.getInt("cantidadMovimiento"));
                    json.put("ubicacion", rs.getString("ubicacion"));
                    json.put("origen", rs.getString("origen"));
                    json.put("proyecto", rs.getString("proyecto"));
                    json.put("fechaMovimiento", rs.getString("fechaMovimiento"));
                    json.put("hora", rs.getString("hora"));
                    json.put("comentarios", rs.getString("comentarios"));
                    json.put("folioReferencia", rs.getString("folioReferencia"));
                    json.put("fechaEnt", rs.getString("fechaEnt"));
                    json.put("ordSuministro", rs.getString("ordSuministro"));
                    information.put(json);

                }
            }

            PreparedStatement psRedistribucion = conn.prepareStatement(queryRedistribucion);

            psRedistribucion.setString(1, clave);
            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                psRedistribucion.setString(2, fechaInicial);
                psRedistribucion.setString(3, fechaFinal);
            }

            try (ResultSet rsRedistribucion = psRedistribucion.executeQuery()) {
                while (rsRedistribucion.next()) {
                    json = new JSONObject();
                    json.put("noMov", rsRedistribucion.getInt("idMovimiento"));
                    json.put("usuario", rsRedistribucion.getString("usuario"));
                    json.put("concepto", rsRedistribucion.getString("concepto"));
                    json.put("clave", rsRedistribucion.getString("clave"));
                    json.put("lote", rsRedistribucion.getString("lote"));
                    json.put("caducidad", rsRedistribucion.getString("caducidad"));
                    json.put("cantidadMovimiento", rsRedistribucion.getInt("cantidad"));
                    json.put("ubicacion", rsRedistribucion.getString("ubicacion"));
                    json.put("origen", rsRedistribucion.getString("origen"));
                    json.put("proyecto", rsRedistribucion.getString("descProyecto"));
                    json.put("fechaMovimiento", rsRedistribucion.getString("fechaMovimiento"));
                    json.put("hora", rsRedistribucion.getString("hora"));
                    json.put("comentarios", rsRedistribucion.getString("comentarios"));
                    informationRedistribucion.put(json);
                }
            }

            result.put("movimeintos", information);
            result.put("reabastecimiento", informationRedistribucion);

        } catch (SQLException ex) {
            Logger.getLogger(KardexService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }

    public JSONObject kardexInformation(Connection conn, String clave, String lote, String caducidad, int origen, String fechaInicial, String fechaFinal) {
        JSONArray information = new JSONArray();
        JSONArray informationRedistribucion = new JSONArray();
        JSONObject json;
        JSONObject result = new JSONObject();
        String query = "";
        String queryRedistribucion = "";
        if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
            query = String.format(kardexQuerys.OBTENER_KARDEX_POR_LOTE_CADUCIDAD, "AND m.F_FecMov BETWEEN ? AND ?");
            queryRedistribucion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_LOTE_Y_CADUCIDAD, "AND m.F_FecMov BETWEEN ? AND ?");
        } else {
            query = String.format(kardexQuerys.OBTENER_KARDEX_POR_LOTE_CADUCIDAD, "");
            queryRedistribucion = String.format(kardexQuerys.OBTENER_KARDEX_REUBICACIONES_POR_LOTE_Y_CADUCIDAD, "");
        }

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, lote);
            ps.setString(2, caducidad);
            ps.setInt(3, origen);
            ps.setString(4, clave);
            ps.setString(5, clave);
            ps.setString(6, clave);
            ps.setString(7, clave);
            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                ps.setString(8, fechaInicial);
                ps.setString(9, fechaFinal);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noMov", rs.getInt("noMov"));
                    json.put("usuario", rs.getString("usuario"));
                    json.put("usuariocaptura", rs.getString("usuariocapt"));
                    json.put("usuariolibera", rs.getString("usuariolib"));
                    json.put("documentoCompra", rs.getString("documentoCompra"));
                    json.put("ori", rs.getString("ori"));
                    json.put("remision", rs.getString("remision"));
                    json.put("proveedor", rs.getString("proveedor"));
                    json.put("folioSalida", rs.getString("folioSalida"));
                    json.put("puntoEntrega", rs.getString("puntoEntrega"));
                    json.put("concepto", rs.getString("concepto"));
                    json.put("clave", rs.getString("clave"));
                    json.put("lote", rs.getString("lote"));
                    json.put("caducidad", rs.getString("caducidad"));
                    json.put("cantidadMovimiento", rs.getInt("cantidadMovimiento"));
                    json.put("ubicacion", rs.getString("ubicacion"));
                    json.put("origen", rs.getString("origen"));
                    json.put("proyecto", rs.getString("proyecto"));
                    json.put("fechaMovimiento", rs.getString("fechaMovimiento"));
                    json.put("hora", rs.getString("hora"));
                    json.put("comentarios", rs.getString("comentarios"));
                    json.put("folioReferencia", rs.getString("folioReferencia"));
                    json.put("fechaEnt", rs.getString("fechaEnt"));
                    json.put("ordSuministro", rs.getString("ordSuministro"));
                    information.put(json);

                }
            }
            PreparedStatement psRedistribucion = conn.prepareStatement(queryRedistribucion);

            psRedistribucion.setString(1, clave);
            psRedistribucion.setString(2, lote);
            psRedistribucion.setString(3, caducidad);
            psRedistribucion.setInt(4, origen);
            if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
                psRedistribucion.setString(5, fechaInicial);
                psRedistribucion.setString(6, fechaFinal);
            }

            try (ResultSet rsRedistribucion = psRedistribucion.executeQuery()) {
                while (rsRedistribucion.next()) {
                    json = new JSONObject();
                    json.put("noMov", rsRedistribucion.getInt("idMovimiento"));
                    json.put("usuario", rsRedistribucion.getString("usuario"));
                    json.put("concepto", rsRedistribucion.getString("concepto"));
                    json.put("clave", rsRedistribucion.getString("clave"));
                    json.put("lote", rsRedistribucion.getString("lote"));
                    json.put("caducidad", rsRedistribucion.getString("caducidad"));
                    json.put("cantidadMovimiento", rsRedistribucion.getInt("cantidad"));
                    json.put("ubicacion", rsRedistribucion.getString("ubicacion"));
                    json.put("origen", rsRedistribucion.getString("origen"));
                    json.put("proyecto", rsRedistribucion.getString("descProyecto"));
                    json.put("fechaMovimiento", rsRedistribucion.getString("fechaMovimiento"));
                    json.put("hora", rsRedistribucion.getString("hora"));
                    json.put("comentarios", rsRedistribucion.getString("comentarios"));
                    informationRedistribucion.put(json);
                }
            }

            result.put("movimeintos", information);
            result.put("reabastecimiento", informationRedistribucion);

        } catch (SQLException ex) {
            Logger.getLogger(KardexService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }

///////////kardex por fecha
    public JSONObject kardexInformation(Connection conn, String fecha) {
        JSONArray information = new JSONArray();
        JSONArray informationRedistribucion = new JSONArray();
        JSONObject json;
        JSONObject result = new JSONObject();

        try (PreparedStatement ps = conn.prepareStatement(kardexQuerys.OBTENER_KARDEX_POR_FECHA)) {

            ps.setString(1, fecha);
            ps.setString(2, fecha);
            ps.setString(3, fecha);
            ps.setString(4, fecha);
            ps.setString(5, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    json = new JSONObject();
                    json.put("noMov", rs.getInt("noMov"));
                    json.put("usuario", rs.getString("usuario"));
                    json.put("usuariocaptura", rs.getString("usuariocapt"));
                    json.put("usuariolibera", rs.getString("usuariolib"));
                    json.put("documentoCompra", rs.getString("documentoCompra"));
                    json.put("ori", rs.getString("ori"));
                    json.put("remision", rs.getString("remision"));
                    json.put("proveedor", rs.getString("proveedor"));
                    json.put("folioSalida", rs.getString("folioSalida"));
                    json.put("puntoEntrega", rs.getString("puntoEntrega"));
                    json.put("concepto", rs.getString("concepto"));
                    json.put("clave", rs.getString("clave"));
                    json.put("lote", rs.getString("lote"));
                    json.put("caducidad", rs.getString("caducidad"));
                    json.put("cantidadMovimiento", rs.getInt("cantidadMovimiento"));
                    json.put("ubicacion", rs.getString("ubicacion"));
                    json.put("origen", rs.getString("origen"));
                    json.put("proyecto", rs.getString("proyecto"));
                    json.put("fechaMovimiento", rs.getString("fechaMovimiento"));
                    json.put("hora", rs.getString("hora"));
                    json.put("comentarios", rs.getString("comentarios"));
                    json.put("folioReferencia", rs.getString("folioReferencia"));
                    json.put("fechaEnt", rs.getString("fechaEnt"));
                    json.put("ordSuministro", rs.getString("ordSuministro"));
                    information.put(json);

                }
            }

            PreparedStatement psRedistribucion = conn.prepareStatement(kardexQuerys.OBTENER_KARDEX_REDISTRIBUCION_POR_FECHA);
            psRedistribucion.setString(1, fecha);

            try (ResultSet rsRedistribucion = psRedistribucion.executeQuery()) {
                while (rsRedistribucion.next()) {
                    json = new JSONObject();
                    json.put("noMov", rsRedistribucion.getInt("idMovimiento"));
                    json.put("usuario", rsRedistribucion.getString("usuario"));
                    json.put("concepto", rsRedistribucion.getString("concepto"));
                    json.put("clave", rsRedistribucion.getString("clave"));
                    json.put("lote", rsRedistribucion.getString("lote"));
                    json.put("caducidad", rsRedistribucion.getString("caducidad"));
                    json.put("cantidadMovimiento", rsRedistribucion.getInt("cantidad"));
                    json.put("ubicacion", rsRedistribucion.getString("ubicacion"));
                    json.put("origen", rsRedistribucion.getString("origen"));
                    json.put("proyecto", rsRedistribucion.getString("descProyecto"));
                    json.put("fechaMovimiento", rsRedistribucion.getString("fechaMovimiento"));
                    json.put("hora", rsRedistribucion.getString("hora"));
                    json.put("comentarios", rsRedistribucion.getString("comentarios"));
                    informationRedistribucion.put(json);
                }
            }

            result.put("movimeintos", information);
            result.put("reabastecimiento", informationRedistribucion);

        } catch (SQLException ex) {
            Logger.getLogger(KardexService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
