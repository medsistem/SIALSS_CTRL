/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.dao.impl;

import com.medalfa.saa.model.DetalleRequerimiento;
import com.medalfa.saa.model.RequerimientoFarmacia;
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
public class RequerimientoFarmaciaDAO {

    Connection c;

    public RequerimientoFarmaciaDAO(Connection c) {
        this.c = c;
    }

    public void guardar(RequerimientoFarmacia r) {
        boolean guardado = true;
        int count = 0;
        do {
            try {
                String stringQuery = "INSERT INTO fed_pharmacy_requirements VALUES(0,?,?,NOW(),?,?,?,NOW());";
                PreparedStatement ps = this.c.prepareStatement(stringQuery);
                ps.setInt(1, r.getProyecto());
                ps.setString(2, r.getClaCli());
//             ps.setString(3, r.getFecReq());
                ps.setInt(3, r.getStatus());
                ps.setInt(4, r.getFolio());
                ps.setInt(5, r.getPriority());
                System.out.println(ps);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                Long idRequerimiento = rs.getLong(1);
                r.setIdRequerimiento(idRequerimiento);
                stringQuery = "INSERT INTO fed_requirement_detail VALUES(0,?,?,?,?);";
                System.out.println("detalles: " + r.getDetalles().size());
                for (DetalleRequerimiento d : r.getDetalles()) {
                    if (d.getCantidad() > 0) {
                        ps = this.c.prepareStatement(stringQuery);
                        ps.setLong(1, idRequerimiento);
                        ps.setString(2, d.getClave());
                        ps.setInt(3, d.getCantidad());
                        ps.setString(4, d.getBatchCode());
                        System.out.println(ps);
                        ps.executeUpdate();
                    }
                }
                guardado = true;
            } catch (SQLException ex) {
                String message =ex.getMessage();
                if(message.contains("2006")){
                    guardado = false;
                }
                Logger.getLogger(RequerimientoFarmaciaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            count++;
        } while (!guardado && (count < 10));

    }

}
