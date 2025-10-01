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
    
    public static final String ACTUALIZA_REQUERIMIENTO_CLAVE= "UPDATE tb_unireq SET F_Status = ? WHERE F_IdReq = ? ;";


    public static final String ELIMINA_REQUERIMIENTO = "DELETE FROM tb_unireq WHERE F_ClaUni = ? AND F_Status = ?;";

    public static String CantRequerida = "SELECT SUM(R.requerido) AS requerido FROM requerimiento_lodimed R WHERE R.estatus = 'RECIBIDO' AND R.folio = ? AND R.clave_unidad = ?;";

    public static String CantRegistrada = "SELECT SUM(F_Solicitado) FROM tb_unireq WHERE F_ClaUni = ? AND F_Status = 0 AND F_Obs = ?;";

    public static final String ACTUALIZA_STS = "UPDATE requerimiento_lodimed SET estatus = 'PROCESADO' WHERE clave_unidad = ? AND folio = ?;";

    public static String updateCantidad = "UPDATE requerimiento_lodimed SET requerido = ? WHERE id= ?";
    
    public static String Busca_clave = "SELECT ur.F_IdReq, ur.F_ClaUni, ur.F_ClaPro,ur.F_Status, CASE WHEN IFNULL( ctr.F_ClaPro, '' ) THEN '4' WHEN IFNULL( ape.F_ClaPro, '' ) THEN '3' WHEN IFNULL( rf.F_ClaPro, '' ) THEN '2' ELSE '1' END as TipoMed FROM tb_unireq ur INNER JOIN tb_uniatn ua ON ua.F_ClaCli = ur.F_ClaUni INNER JOIN tb_tipunidad tu ON ua.F_Tipo = tu.F_idTipUni LEFT JOIN tb_controlados ctr ON ur.F_ClaPro = ctr.F_ClaPro LEFT JOIN tb_ape ape ON ur.F_ClaPro = ape.F_ClaPro LEFT JOIN tb_redfria rf ON ur.F_ClaPro = rf.F_ClaPro WHERE ua.F_StsCli = 'A' AND ur.F_ClaUni = ? AND ur.F_Status = ? AND ur.F_Fecha = ?  GROUP BY TipoMed, F_ClaPro HAVING TipoMed = ? ORDER BY F_ClaPro";

    private final ConectionDBTrans con = new ConectionDBTrans();
    private PreparedStatement psBuscaRequerimiento;
    private PreparedStatement PsInsertarReq;
    private PreparedStatement PsActualizaReq;
    private PreparedStatement PsDatos;
    private PreparedStatement PsBusca_clave;
    
    private ResultSet rsRequerimiento;
    private ResultSet rsDatos;
    private ResultSet rsBusca_clave;

    @Override
    public boolean ConfirmarRequerimiento(String Usuario,  String Unidad, String ClaCli, int Tipo, String Fecha) {
        boolean save = false;
        int idfact = 0;

        try {
          
            con.conectar();
            con.getConn().setAutoCommit(false);

             System.out.println("Usuario: " + Usuario);
            System.out.println("Unidad: " + Unidad);
            System.out.println("ClaCli: " + ClaCli);
            System.out.println("Tipo: " + Tipo);
            System.out.println("Fecha: " + Fecha);
            
            PsBusca_clave = con.getConn().prepareStatement(Busca_clave);
            PsBusca_clave.setString(1, ClaCli);
            PsBusca_clave.setInt(2, 5);
            PsBusca_clave.setInt(4, Tipo);
            PsBusca_clave.setString(3, Fecha);
           rsBusca_clave = PsBusca_clave.executeQuery();
            
             while (rsBusca_clave.next()) {
                 
                 idfact = rsBusca_clave.getInt(1);
                 System.out.println("idfact" +idfact);
                 System.out.println(rsBusca_clave.getInt(1));
            PsActualizaReq = con.getConn().prepareStatement(ACTUALIZA_REQUERIMIENTO_CLAVE);
            PsActualizaReq.setInt(1, 0);
            PsActualizaReq.setInt(2, idfact);
            PsActualizaReq.executeUpdate();

             }
 System.out.println(" PsActualizaReq: " + PsActualizaReq);
            con.getConn().commit();
            save = true;
           

            PsActualizaReq.close();

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
