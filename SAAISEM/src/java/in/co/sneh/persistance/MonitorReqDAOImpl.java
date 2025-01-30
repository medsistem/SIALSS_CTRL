/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.MonitorReq;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class MonitorReqDAOImpl {

    private final Connection con;

    private final String insertMonitor = "INSERT INTO tb_monitor_req (F_Fecha, F_ClaUni, F_Creacion) VALUES (?, ?, NOW())";
    private final String findMonitor = "SELECT c.F_Fecha, c.F_ClaUni from tb_cargareq c where F_User = ? GROUP BY F_Fecha, F_ClaUni ;";
    private final Format formatter;

    public MonitorReqDAOImpl(Connection con) {
        this.formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.con = con;
    }

    public void insertar(MonitorReq m) throws SQLException {
        PreparedStatement ps = con.prepareStatement(insertMonitor);
        ps.setString(1, this.formatter.format(m.getFecha()));
        ps.setString(2, m.getClaUni());
        ps.executeUpdate();
    }
    
    public void insertar(List<MonitorReq> list) throws SQLException{
        for(MonitorReq m: list){
            this.insertar(m);
        }
    }
    
    public List<MonitorReq> findDataByUser(String user) throws SQLException{
        List<MonitorReq> result = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement(findMonitor);
        ps.setString(1, user);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            MonitorReq m = new MonitorReq();
            m.setFecha(rs.getDate(1));
            m.setClaUni(rs.getString(2));
            result.add(m);
        }
        return result;
    }
}
