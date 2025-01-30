/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.persistance;

import in.co.sneh.model.Compra;
import in.co.sneh.model.Lote;
import in.co.sneh.model.LoteCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class LoteDAOImpl {
    
    Connection con;
    SimpleDateFormat df;
    
    public LoteDAOImpl(Connection con){
        this.con=con;
        this.df = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public void create(Lote l){
        try{
            String qry= "INSERT INTO tb_lote VALUES (0,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps = this.con.prepareStatement(qry);
            ps.setString(1, l.getClaPro());
            ps.setString(2, l.getClaLot());
            ps.setString(3, df.format(l.getFecCadD()));
            ps.setInt(4, l.getExistencia());
            ps.setInt(5, l.getFolLot());
            ps.setInt(6, l.getClaProvee());
            ps.setString(7, l.getUbicacion());
            ps.setString(8, df.format(l.getFecFabD()));
            ps.setString(9, l.getCb());
            ps.setInt(10, l.getClaMar());
            ps.setInt(11, l.getOrigen());
            ps.setInt(12, l.getClaProvee());
            ps.setString(13, l.getUniMed());
            ps.setInt(14, l.getProyecto());
            
            System.out.println(ps);
            ps.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public LoteCompra findLote(Lote l){
        LoteCompra lc = new LoteCompra();
        PreparedStatement psFind= null;
        try {
            
            String qry= "SELECT l.*, c.F_Costo FROM tb_lote l inner join tb_compra c on l.F_FolLot = c.F_Lote where l.F_ClaPro = ? AND l.F_ClaLot = ? AND l.F_Proyecto = ? order by c.F_IdCom";
            psFind = this.con.prepareStatement(qry);
            psFind.setString(1, l.getClaPro());
            psFind.setString(2, l.getClaLot());
            psFind.setInt(3, l.getProyecto());
            ResultSet result= psFind.executeQuery();
            while(result.next()){
                Lote lote = new Lote();
                Compra compra = new Compra();
                lote.setIdLote(result.getInt("F_IdLote"));
                lote.setFolLot(result.getInt("F_FolLot"));
                lote.setFechaFab(result.getString("F_FecFab"));
                lote.setFecFabD(df.parse(lote.getFechaFab()));
                lote.setFecCad(result.getString("F_FecCad"));
                lote.setFecCadD(df.parse(lote.getFecCad()));
                lote.setClaProvee(result.getInt("F_ClaPrv"));
                lote.setClaMar(result.getInt("F_ClaMar"));
                compra.setCosto(result.getDouble("F_Costo"));
                lc.lote=lote;
                lc.compra= compra;
                return lc;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) { 
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public LoteCompra findLoteUbica(Lote l){
        LoteCompra lc = new LoteCompra();
        PreparedStatement psFind= null;
        try {
//            String qry= "SELECT l.*, c.F_Costo FROM tb_lote l inner join tb_compra c on l.F_FolLot = c.F_Lote where l.F_ClaPro = ? AND l.F_ClaLot = ? AND l.F_Ubica = ? AND l.F_Proyecto = ? order by c.F_IdCom";
            String qry= "SELECT l.*, c.F_Costo FROM tb_lote l inner join tb_compra c on l.F_FolLot = c.F_Lote where l.F_ClaPro = ? AND l.F_ClaLot = ? AND l.F_Ubica = ? AND l.F_Proyecto = ?, AND l.F_ClaPrv = ? AND l.F_ClaMar = ? order by c.F_IdCom";
            psFind = this.con.prepareStatement(qry);
            psFind.setString(1, l.getClaPro());
            psFind.setString(2, l.getClaLot());
            psFind.setString(3, l.getUbicacion());
            psFind.setInt(4, l.getProyecto());
            psFind.setInt(5, l.getClaProvee());
            psFind.setInt(6, l.getClaMar());
            System.out.println(psFind);
            ResultSet result= psFind.executeQuery();
            while(result.next()){
                Lote lote = new Lote();
                Compra compra = new Compra();
                lote.setIdLote(result.getInt("F_IdLote"));
                lote.setFolLot(result.getInt("F_FolLot"));
                lote.setUbicacion(result.getString("F_Ubica"));
                lote.setFecCad(result.getString("F_FecCad"));
                lote.setFechaFab(result.getString("F_FecFab"));
                lote.setClaProvee(result.getInt("F_ClaPrv"));
                compra.setCosto(result.getDouble("F_Costo"));
                lc.lote=lote;
                lc.compra= compra;
                return lc;
            }
            qry= "SELECT * FROM tb_lote l where l.F_ClaPro = ? AND l.F_ClaLot = ? AND l.F_Ubica = ? AND l.F_Proyecto = ?";
            psFind = this.con.prepareStatement(qry);
            psFind.setString(1, l.getClaPro());
            psFind.setString(2, l.getClaLot());
            psFind.setString(3, l.getUbicacion());
            psFind.setInt(4, l.getProyecto());
            System.out.println(psFind);
            result= psFind.executeQuery();
            while(result.next()){
                Lote lote = new Lote();
                Compra compra = new Compra();
                lote.setIdLote(result.getInt("F_IdLote"));
                lote.setFolLot(result.getInt("F_FolLot"));
                lote.setFecCad(result.getString("F_FecCad"));
                lote.setFechaFab(result.getString("F_FecFab"));
                lote.setClaProvee(result.getInt("F_ClaPrv"));
                lote.setUbicacion(result.getString("F_Ubica"));
                compra.setCosto(0d);
                lc.lote=lote;
                lc.compra= compra;
                return lc;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        return null;
    }
    
    public void acutualizaExistencia(Lote l, int existencia){
        PreparedStatement psUpdate= null;
        try {
            String qry= "UPDATE tb_lote SET F_ExiLot = ? where F_IdLote = ?";
            psUpdate = this.con.prepareStatement(qry);
            psUpdate.setInt(1, existencia);
            psUpdate.setInt(2, l.getIdLote());
            psUpdate.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }
    
    public int getFolioLote(){
        PreparedStatement psUpdate= null;
        try {
            String qry= "SELECT F_IndLote from tb_indice";
            psUpdate = this.con.prepareStatement(qry);
            ResultSet rsIndice = psUpdate.executeQuery();
            while(rsIndice.next()){
                return rsIndice.getInt("F_IndLote");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        return 0;
    }
    
    public void updateFolioLote(int folio){
        PreparedStatement psUpdate= null;
        try {
            String qry= "UPDATE tb_indice SET F_IndLote = ? where 1";
            psUpdate = this.con.prepareStatement(qry);
            psUpdate.setInt(1, folio);
            psUpdate.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }
    
    public Lote findById(Integer id){
        Lote l = null;
        
        String qry= "Select * from tb_lote where F_IdLote = "+id;
        try {
            ResultSet data = this.con.prepareStatement(qry).executeQuery();
            if(data.next())
             l = this.buildLote(data);
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }
    
    private Lote buildLote(ResultSet data){
        Lote l = new Lote();
        try {
            l.setIdLote(data.getInt("F_IdLote"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setClaLot(data.getString("F_ClaLot"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setClaMar(data.getInt("F_ClaMar"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setClaPro(data.getString("F_ClaPro"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setClaProvee(data.getInt("F_ClaProvee"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setExistencia(data.getInt("F_ExiLot"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setFecCadD(data.getDate("F_FecCad"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setFecFabD(data.getDate("F_FecFab"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setFolLot(data.getInt("F_FolLot"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setIdLote(data.getInt("F_IdLote"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setOrigen(data.getInt("F_Origen"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setProyecto(data.getInt("F_Proyecto"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            l.setUbicacion(data.getString("F_Ubica"));
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        l.setUniMed("131");
        return l;
    }
}
