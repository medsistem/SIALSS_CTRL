/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

import com.gnk.model.FacturaProvisional;
import com.gnk.model.FacturaProvisionalDetail;
import conn.ConectionDBTrans;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class FacturaProvisionalDAO {

    private Connection con;
    private SimpleDateFormat df;

    public static String ubicacionesNoU013 = "'AF','ACOPIO', 'AH111PISO', 'MDI293', 'MDJ175', 'CROSSDOCKMORELIAl',"
        +"'MDQ184', 'MDK274', 'MDJ501', 'MDQ194', 'MDQ204', 'MDQ214', 'MDR184', "
        +"'MDQ224', 'MDQ234', 'MDQ244', 'MDR204', 'MDR194', 'MDS45', 'MDS55', "
        +"'MDS65', 'MDS75', 'MDS85', 'MDT45', 'MDT55', 'MDT65', 'MDT75', 'S11ACOPIO', "
        +"'SURTIDOACOPIO', 'CUARENTENA', 'APE', 'REDFRIA', 'CONTROLADO'";

    public FacturaProvisionalDAO(Connection con) {
        this.con = con;
        this.df = new SimpleDateFormat("yyyy-MM-dd");
    }

    public FacturaProvisional save(FacturaProvisional f) {
        try {
            String query = "INSERT INTO folio_provisional VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, f.getId());
            ps.setInt(2, f.getFolio());
            ps.setString(3, f.getClaCli());
            ps.setString(4, this.df.format(f.getFecEnt()));
            ps.setString(5, df.format(f.getFecApl()));
            ps.setString(6, f.getContrato());
            ps.setString(7, f.getOc());
            ps.setString(8, f.getCause());
            ps.setInt(9, f.getProyecto());
            ps.setInt(10, f.getStatus());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer idFactura = rs.getInt(1);
                f.setId(idFactura);
                for (FacturaProvisionalDetail d : f.getDetails()) {
                    d.setIdFactura(idFactura);
                    this.saveDetail(d);
                }
            }

            return f;
        } catch (SQLException ex) {
            Logger.getLogger(FacturaProvisionalDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public FacturaProvisionalDetail saveDetail(FacturaProvisionalDetail d) {
        try {
            String query = "INSERT INTO folio_provisional_detail VALUES (?,?,?,?,?,?,?,?,?,?,?,curtime())";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, d.getId());
            ps.setInt(2, d.getIdFactura());
            ps.setInt(3, d.getIdLote());
            ps.setString(4, this.df.format(d.getFecApl()));
            ps.setString(5, d.getUser());
            ps.setInt(6, d.getStatus());
            ps.setInt(7, d.getCantReq());
            ps.setInt(8, d.getCantSur());
            ps.setDouble(9, d.getCost());
            ps.setDouble(10, d.getIva());
            ps.setDouble(11, d.getMonto());
            ps.executeUpdate();
            return d;
        } catch (SQLException ex) {
            Logger.getLogger(FacturaProvisionalDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer getFolio() {
        try {
            String query = "SELECT F_IndFactProvisional2 from tb_indice";
            PreparedStatement ps = this.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("F_IndFactProvisional2");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturaProvisionalDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public Integer increaseFolio() {
        try {
            Integer folio = this.getFolio();
            String query = "UPDATE tb_indice SET F_IndFactProvisional2 = ? where 1";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, folio + 1);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(FacturaProvisionalDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
