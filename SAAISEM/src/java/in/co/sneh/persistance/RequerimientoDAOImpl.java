/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Requerimiento;
import in.co.sneh.service.exception.RequerimentException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class RequerimientoDAOImpl {

    private final Connection con;
    private final Format formatter;
    private final String insert = "INSERT INTO tb_unireq VALUES(?,?,?,?,curdate(), 0, ?, ?, ?, ?)";

    public RequerimientoDAOImpl(Connection con) {
        this.con = con;
        this.formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void insert(Requerimiento r) throws RequerimentException {
        try {
            PreparedStatement st = con.prepareStatement(insert);
            st.setString(1, r.getClaUni());
            st.setString(2, r.getClaPro());
            st.setInt(3, r.getCajasReq());
            st.setInt(4, r.getPiezasReq());
            st.setInt(5, r.getStatus());
            st.setString(6, this.formatter.format(r.getFecha()));
            st.setInt(7, r.getSolicitado());
            st.setString(8, r.getObservaciones()== null?"":r.getObservaciones());
            st.executeUpdate();
        } catch (SQLException ex) {
            String error = ex.getMessage();
            Integer tipo = 0;
            if (error.contains("F_ClaCli")) {
                tipo = 1;
                error = r.getClaUni();
            }
            if (error.contains("F_ClaPro")) {
                tipo = 2;
                error = r.getClaPro();
            }
            Logger.getLogger(RequerimientoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new RequerimentException(tipo, error);
        }
    }

    public void insert(List<Requerimiento> list) throws RequerimentException {
        for (Requerimiento r : list) {
            this.insert(r);
        }
    }

    public void updateStatus(String unidad, Integer status) throws SQLException {
        String q = "UPDATE tb_unireq SET F_Status='1' WHERE F_ClaUni='" + unidad + "' and F_Status='" + status + "';";
        PreparedStatement st = con.prepareStatement(q);
        System.out.println(st);
        st.executeUpdate();
    }

    public void updateStatus(List<String> unidades, Integer status) throws SQLException {
        for (String unidad : unidades) {
            this.updateStatus(unidad, status);
        }
    }
}
