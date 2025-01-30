/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.CargaRequerimiento;
import in.co.sneh.service.RequerimientoService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class CargaReqDAOImpl {

    private final Connection con;
    private final String insertCargaReq = "INSERT INTO tb_cargareq VALUES(?,?,?,?,curdate(),0,?,?,?,?,now());";
    private final Format formatter;

    public CargaReqDAOImpl(Connection con) {
        this.formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.con = con;
    }

    public String insert(CargaRequerimiento cr) throws SQLException {
        String claPro = cr.getClaPro();
        PreparedStatement insertCargaPs = con.prepareStatement(insertCargaReq);
        insertCargaPs.setString(1, cr.getClaUni());
        insertCargaPs.setString(2, cr.getClaPro());
        insertCargaPs.setInt(3, cr.getCajasReq());
        insertCargaPs.setInt(4, cr.getPiezasReq());
//        insertCargaPs.setInt(5, cr.getStatus());
        insertCargaPs.setInt(5, 1);
        insertCargaPs.setString(6, formatter.format(cr.getFecha()));
        insertCargaPs.setInt(7, cr.getSolicitado());
        insertCargaPs.setString(8, cr.getUser());
        System.out.println(insertCargaPs);
        insertCargaPs.executeUpdate();
        return claPro;
    }

    public String insert(List<CargaRequerimiento> reqs) throws SQLException {
        String r = "";
        for (CargaRequerimiento cr : reqs) {
            try{
            r = this.insert(cr);
            }catch(Exception ex){
//                Logger.getLogger(CargaReqDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }

    public List<String> getClaUniByUser(String user) throws SQLException {
        List<String> unidades = new ArrayList<>();
        ResultSet rs = con.prepareStatement("SELECT F_ClaUni FROM tb_cargareq WHERE F_User='" + user + "' GROUP BY F_ClaUni;").executeQuery();
        while (rs.next()) {
            unidades.add(rs.getString(1));
        }
        return unidades;
    }

    public void deleteByUser(String user) throws SQLException {
        con.prepareStatement("DELETE FROM tb_cargareq WHERE F_User='" + user + "'").execute();
    }

}
