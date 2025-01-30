/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.marbetesDao;
import conn.ConectionDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class marbeteDaoImpl implements marbetesDao {

    ConectionDB con = new ConectionDB();
    private ResultSet rs;
    private PreparedStatement ps;
    private String query;

    @Override
    public String nombreUnidad(int folio, int Proyecto) {
        String nombreUnidad = "";
         int Contar = 0, Contar1 = 0;
        try {
            con.conectar();
//            int Contar = 0;
            query = "SELECT COUNT(R.F_ClaPro) FROM tb_redfria R INNER JOIN tb_factura F ON R.F_ClaPro = F.F_ClaPro WHERE F.F_ClaDoc = ? AND F.F_Proyecto = ? AND F.F_CantSur>0;";
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, folio);
            ps.setInt(2, Proyecto);
            rs = ps.executeQuery();
            while (rs.next()) {
                Contar = rs.getInt(1);
            }
            ps.clearParameters();
            
            query = "SELECT COUNT(C.F_ClaPro) FROM tb_controlados C INNER JOIN tb_factura F ON C.F_ClaPro = F.F_ClaPro WHERE F.F_ClaDoc = ? AND F.F_Proyecto = ? AND F.F_CantSur>0;";
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, folio);
            ps.setInt(2, Proyecto);
            rs = ps.executeQuery();
            while (rs.next()) {
                    Contar1 = rs.getInt(1);
            }
            ps.clearParameters();
            query = "SELECT u.F_NomCli FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli WHERE f.F_ClaDoc = ? AND f.F_Proyecto = ? GROUP BY f.F_ClaDoc;";
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, folio);
            ps.setInt(2, Proyecto);
            rs = ps.executeQuery();
            while (rs.next()) {
                if(Contar > 0){
                    nombreUnidad = Contar + "/" + rs.getString("F_NomCli") + "/" + "RF";  
                } else if(Contar1 > 0){
                    nombreUnidad = Contar1 + "/" + rs.getString("F_NomCli") + "/" +"Cont";    
                } else{
                    nombreUnidad =  "0/" + rs.getString("F_NomCli") + "";    
                }
            }
            
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nombreUnidad;
    }

    @Override
    public boolean insertar(String unidad, int folio, int marbetes, int Proyecto, String rutaParam) {
        boolean guardado = false;
        try {
            con.conectar();
            String Juris = "", Muni = "", TipoFactura = "", origen = "", ruta = "";
            query = "DELETE FROM tb_marbetes_cajas WHERE F_Folio=?;";
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, folio);
            ps.execute();
            ps.clearParameters();

              
            query = "SELECT F.F_ClaCli, U.F_NomCli, J.F_DesJurIS, M.F_DesMunIS, O.F_Tipo, extract(day from f.F_FecEnt) as F_FecEnt, ORI.F_DesOri FROM tb_factura F INNER JOIN tb_lote L ON L.F_FolLot = F.F_Lote AND L.F_Ubica = F.F_Ubicacion INNER JOIN tb_origen ORI ON L.F_Origen = ORI.F_ClaOri LEFT JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli LEFT JOIN tb_juriis J ON U.F_ClaJur = J.F_ClaJurIS LEFT JOIN tb_muniis M ON U.F_ClaMun = M.F_ClaMunIS LEFT JOIN tb_obserfact O ON F.F_ClaDoc = O.F_IdFact AND F.F_Proyecto = O.F_Proyecto AND U.F_ClaJur = M.F_JurMunIS WHERE F_ClaDoc = ? AND F.F_Proyecto = ? GROUP BY F.F_ClaCli;";
            ps = con.getConn().prepareStatement(query);
             ps.setInt(1, folio);
            ps.setInt(2, Proyecto);
            rs = ps.executeQuery();
            if (rs.next()) {
                Juris = rs.getString(3);
                Muni = rs.getString(4);
                TipoFactura = rs.getString(5);
                ruta = rs.getString(6);
                origen = rs.getString(7);
            }
            if(rutaParam == null || rutaParam.isEmpty()){
                rutaParam = ruta;
            }else {
                 rutaParam = rutaParam;
            }
            ps.clearParameters();
            query = "INSERT INTO tb_marbetes_cajas VALUES (?,?,?,?,DATE(NOW()),?,?,?,0,?,?)";
            for (int cajas = 1; cajas <= marbetes; cajas++) {

                ps = con.getConn().prepareStatement(query);
                ps.setString(1, unidad);
                ps.setInt(2, folio);
                ps.setInt(3, marbetes);
                ps.setInt(4, cajas);
                ps.setString(5, Juris);
                ps.setString(6, Muni);
                ps.setString(7, TipoFactura);
                ps.setString(8, rutaParam);
                ps.setString(9, origen);
                ps.execute();
            }

            guardado = true;
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return guardado;
    }
    
     @Override
    public boolean insertarSur(String unidad, int folio, int marbetes, int Proyecto, String rutaParam) {
        boolean guardado = false;
        try {
            con.conectar();
            String Juris = "", Muni = "", TipoFactura = "", origen = "", ruta = "";
            query = "DELETE FROM tb_marbetes_cajas WHERE F_Folio=?;";
            ps = con.getConn().prepareStatement(query);
            ps.setInt(1, folio);
            ps.execute();
            ps.clearParameters();
            
            System.out.println(unidad);
               
            query = "SELECT u.F_ClaCli, u.F_NomCli, m.F_DesMunIS, j.F_DesJurIS FROM tb_uniatn AS u INNER JOIN tb_muniis AS m ON u.F_ClaMun = m.F_ClaMunIS AND u.F_ClaJur = m.F_JurMunIS INNER JOIN tb_juriis AS j ON u.F_ClaJur = j.F_ClaJurIS AND m.F_JurMunIS = j.F_ClaJurIS WHERE u.F_ClaCli = ? GROUP BY u.F_ClaCli;";
            ps = con.getConn().prepareStatement(query);
             ps.setString(1, unidad);
           
            rs = ps.executeQuery();
            if (rs.next()) {
                Juris = rs.getString(4);
                Muni = rs.getString(3);
                unidad = rs.getString(2);
                //Muni = rs.getString(3);
            }
         
            query = "INSERT INTO tb_marbetes_cajas VALUES (?,?,?,?,DATE(NOW()),?,?,?,0,?,?)";
            for (int cajas = 1; cajas <= marbetes; cajas++) {

                ps = con.getConn().prepareStatement(query);
                ps.setString(1, unidad);
                ps.setInt(2, folio);
                ps.setInt(3, marbetes);
                ps.setInt(4, cajas);
                ps.setString(5, unidad);
                ps.setString(6, Muni);
                ps.setString(7, "Ordinario");
                ps.setString(8, rutaParam);
                ps.setString(9, "VENTA");
                ps.execute();
            }

            guardado = true;
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(marbeteDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return guardado;
    }
    
    
}
