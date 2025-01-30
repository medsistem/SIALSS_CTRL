/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Factura;
import in.co.sneh.model.Lote;
import in.co.sneh.model.Medica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP-MEDALFA
 */
public class FacturaDAOImpl {

    private final Connection con;

    public FacturaDAOImpl(Connection con) {
        this.con = con;
    }

    public List<Factura> findByFolioAndProyecto(Integer folio, Integer proyecto) throws SQLException {
        PreparedStatement st = this.con.prepareStatement("Select * from tb_factura where F_ClaDoc = ? AND F_Proyecto = ? AND F_CantSur > 0");
        st.setInt(1, folio);
        st.setInt(2, proyecto);
        List<Factura> result = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            result.add(this.buildFactura(rs));
        }
        return result;
    }
    
    public List<Factura> findByFolioAndProyectoFull(Integer folio, Integer proyecto) throws SQLException {
        List<Factura> list = this.findByFolioAndProyecto(folio, proyecto);
        for(Factura f : list){
            f.setLoteData(this.findLote(f));
            f.setMedicaLote(this.findMedica(f));
        }
        return list;
    }

    private Factura buildFactura(ResultSet rs) throws SQLException {
        Factura f = new Factura(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                 rs.getDate(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getDouble(10),
                 rs.getDouble(11), rs.getInt(12), rs.getDate(13), rs.getTime(14), rs.getString(15), rs.getString(16),
                 rs.getString(17), rs.getString(18), rs.getInt(19), rs.getString(20), rs.getString(21), rs.getInt(22));
        return f;
    }
    
    private Lote findLote(Factura f) throws SQLException{
        PreparedStatement st = this.con.prepareStatement("Select * from tb_lote where F_FolLot = ?");
        st.setInt(1, f.getLote());
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            Lote l = new Lote();
            l.setClaLot(rs.getString("F_ClaLot"));
            l.setFecCadD(rs.getDate("F_FecCad"));
            return l;
        }
        return null;
    }
    
    private Medica findMedica(Factura f) throws SQLException{
        PreparedStatement st = this.con.prepareStatement("Select * from tb_medica where F_ClaPro = ?");
        st.setString(1, f.getClaPro());
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            Medica m = new Medica();
            m.setDescription(rs.getString("F_DesPro"));
            m.setKey(rs.getString("F_ClaPro"));
            return m;
        }
        return null;
    }
}
