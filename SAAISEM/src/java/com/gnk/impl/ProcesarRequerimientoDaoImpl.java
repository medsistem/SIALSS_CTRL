/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.ProcesarRequerimientoDao;
import conn.ConectionDBTrans;
import in.co.sneh.model.RequerimientoEntrega;
import in.co.sneh.persistance.RequerimientoEntregaDAOImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anibal GNKL
 */
public class ProcesarRequerimientoDaoImpl implements ProcesarRequerimientoDao {

    public static String BUSCA_DATOSREQ = "SELECT U.F_ClaCli, R.clave, SUM(R.requerido) AS requerido, R.fecha, R.folio FROM requerimiento_lodimed R INNER JOIN tb_uniatn U ON R.clave_unidad = U.F_IdReporte COLLATE utf8_unicode_ci WHERE R.estatus = 'RECIBIDO' AND R.requerido > 0 AND R.folio = ? AND R.clave_unidad = ? GROUP BY clave_unidad, R.folio, R.clave;";

    public static final String INSERTA_REQUERIMIENTO = "INSERT INTO tb_unireq VALUES(?,?,0,?,curdate(),0,0,?,?,?);";

    public static final String ACTUALIZA_REQUERIMIENTO = "UPDATE tb_unireq SET F_Status = ? WHERE F_ClaUni = ? AND F_Status = ?;";

    public static final String ELIMINA_REQUERIMIENTO = "DELETE FROM tb_unireq WHERE F_ClaUni = ? AND F_Status = ?;";

    public static String CantRequerida = "SELECT SUM(R.requerido) AS requerido FROM requerimiento_lodimed R WHERE R.estatus = 'RECIBIDO' AND R.folio = ? AND R.clave_unidad = ?;";

    public static String CantRegistrada = "SELECT SUM(F_Solicitado) FROM tb_unireq WHERE F_ClaUni = ? AND F_Status = 0 AND F_Obs = ?;";

    public static final String ACTUALIZA_STS = "UPDATE requerimiento_lodimed SET estatus = 'PROCESADO' WHERE clave_unidad = ? AND folio = ?;";

    public static String updateCantidad = "UPDATE requerimiento_lodimed SET requerido = ? WHERE id= ?";

    private final ConectionDBTrans con = new ConectionDBTrans();
    private PreparedStatement psBuscaRequerimiento;
    private PreparedStatement PsInsertarReq;
    private PreparedStatement PsActualizaReq;
    private PreparedStatement PsDatos;
    private ResultSet rsRequerimiento;
    private ResultSet rsDatos;

    @Override
    public boolean ConfirmarRequerimiento(String Usuario, String Folio, String Unidad, String ClaCli) {
        boolean save = false;

        try {
            int CantidadReq = 0, CantidadReg = 0;
            con.conectar();
            con.getConn().setAutoCommit(false);

            PsActualizaReq = con.getConn().prepareStatement(ACTUALIZA_REQUERIMIENTO);
            PsActualizaReq.setInt(1, 1);
            PsActualizaReq.setString(2, ClaCli);
            PsActualizaReq.setInt(3, 0);
            PsActualizaReq.execute();

            PsInsertarReq = con.getConn().prepareStatement(INSERTA_REQUERIMIENTO);

            psBuscaRequerimiento = con.getConn().prepareStatement(BUSCA_DATOSREQ);
            psBuscaRequerimiento.setString(1, Folio);
            psBuscaRequerimiento.setString(2, Unidad);
            System.out.println(psBuscaRequerimiento);
            rsRequerimiento = psBuscaRequerimiento.executeQuery();
            while (rsRequerimiento.next()) {
                PsInsertarReq.setString(1, rsRequerimiento.getString(1));
                PsInsertarReq.setString(2, rsRequerimiento.getString(2));
                PsInsertarReq.setString(3, rsRequerimiento.getString(3));
                PsInsertarReq.setString(4, rsRequerimiento.getString(4));
                PsInsertarReq.setString(5, rsRequerimiento.getString(3));
                PsInsertarReq.setString(6, rsRequerimiento.getString(5));
//                System.out.println(PsInsertarReq);
                PsInsertarReq.addBatch();
            }

            PsInsertarReq.executeBatch();

            PsDatos = con.getConn().prepareStatement(CantRequerida);
            PsDatos.setString(1, Folio);
            PsDatos.setString(2, Unidad);
            rsDatos = PsDatos.executeQuery();
            while (rsDatos.next()) {
                CantidadReq = rsDatos.getInt(1);
            }
            PsDatos.clearParameters();

            PsDatos = con.getConn().prepareStatement(CantRegistrada);
            PsDatos.setString(1, ClaCli);
            PsDatos.setString(2, Folio);
            rsDatos = PsDatos.executeQuery();
            while (rsDatos.next()) {
                CantidadReg = rsDatos.getInt(1);
            }
            PsDatos.clearParameters();

            if (CantidadReg == CantidadReq) {

                PsActualizaReq.clearParameters();
                PsActualizaReq = con.getConn().prepareStatement(ACTUALIZA_STS);
                PsActualizaReq.setString(1, Unidad);
                PsActualizaReq.setString(2, Folio);
                PsActualizaReq.execute();
                save = true;
                con.getConn().commit();
            } else {
                PsActualizaReq.clearParameters();
                save = false;
                PsActualizaReq = con.getConn().prepareStatement(ELIMINA_REQUERIMIENTO);
                PsActualizaReq.setString(1, ClaCli);
                PsActualizaReq.setInt(2, 0);
                PsActualizaReq.execute();
            }
            PsInsertarReq.close();
            rsRequerimiento.close();
            psBuscaRequerimiento.close();
            PsActualizaReq.close();
            PsDatos.close();
            rsDatos.close();

        } catch (SQLException ex) {
            Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean actualizaRequerimiento(int id, int cantidad) {
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            PreparedStatement ps = con.getConn().prepareStatement(updateCantidad);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            ps.executeUpdate();
            con.getConn().commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean agregaFecha(int folio, String unidad, String fecha) {
        try {
            con.conectar();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            RequerimientoEntregaDAOImpl dao = new RequerimientoEntregaDAOImpl(con.getConn());
            
            RequerimientoEntrega r = dao.findByClaveUnidadAndFolio(unidad, folio);
            if(r== null){
                r= new RequerimientoEntrega();
                r.setId(0);
                r.setFechaEntrega(df.parse(fecha));
                r.setFolio(folio);
                r.setClaveUnidad(unidad);
                dao.guardar(r);
            }else{
                dao.updateById(r.getId(), fecha);
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ProcesarRequerimientoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
