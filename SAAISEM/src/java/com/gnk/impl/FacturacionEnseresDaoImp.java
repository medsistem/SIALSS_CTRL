/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.FacturacionEnseresDao;
import conn.ConectionDBTrans;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Anibal GNKL
 */
public class FacturacionEnseresDaoImp implements FacturacionEnseresDao {

    public static final String InsertarEnseres = "INSERT INTO tb_enserestemp SET F_Usuario = ?, F_ClaCli = ?, F_ClaEnseres = ?, F_Fecha = ?, F_Cantidad = ?, F_Folio = ?, F_Sts = ?;";

    public static String ValidaExistencia = "SELECT F_Existencia, IFNULL(T.F_Cantidad, 0), (F_Existencia - IFNULL(T.F_Cantidad, 0)) AS DIF FROM tb_enseres E LEFT JOIN ( SELECT F_ClaEnseres, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserestemp WHERE F_ClaEnseres = ? AND F_Sts < 5 ) AS T ON E.F_Id = T.F_ClaEnseres WHERE E.F_Id = ?;";

    public static String ConsultaExistencia = "SELECT F_Existencia FROM tb_enseres E WHERE E.F_Id = ?;";

    public static String ConsultaEnseres = "SELECT E.F_Insumos, E.F_UM, R.F_Cantidad, R.F_Id FROM tb_enserestemp R INNER JOIN tb_enseres E ON R.F_ClaEnseres = E.F_Id WHERE R.F_Usuario = ? AND R.F_Folio = ? AND R.F_ClaCli = ?;";

    public static final String EliminarCapturaEnseres = "DELETE FROM tb_enserestemp WHERE F_Usuario = ? AND F_Folio = ? AND F_ClaCli = ?;";

    public static String ValidarCapturaEnseres = "SELECT COUNT(*) FROM tb_enserestemp WHERE F_Usuario = ? AND F_Folio = ? AND F_ClaCli = ?;";

    public static final String EliminaRegistroEnseres = "DELETE FROM tb_enserestemp WHERE F_Id = ?;";

    public static String ValidarRegistroEnseres = "SELECT COUNT(*) FROM tb_enserestemp WHERE F_Id = ?;";

    public static String ConsultaIndice = "SELECT %s FROM tb_indice;";

    public static String ActualizaIndice = "UPDATE tb_indice SET %s = ?;";

    public static String CapturaFacturar = "SELECT F_ClaCli, F_ClaEnseres, F_Fecha, SUM(F_Cantidad) AS F_Cantidad FROM tb_enserestemp WHERE F_Usuario = ? AND F_Sts = 3 GROUP BY F_ClaCli, F_ClaEnseres;";

    public static final String InsertarEntregaEnseres = "INSERT INTO tb_enseresfactura VALUES (0, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?);";

    public static final String EliminarEnseres = "DELETE FROM tb_enserestemp WHERE F_Usuario = ?;";

    public static String CatalogoEnseres = "SELECT F_Id, CONCAT( F_Insumos, ' UM: ', F_UM, ' Existencia: ', F_Existencia ) FROM tb_enseres WHERE F_Existencia > 0;";

    public static String Registro = "SELECT F_Id FROM tb_enseresoc WHERE F_Oc = ? AND F_IdProveedor = ? AND F_Sts = 1;";

    public static final String ActualizaReq = "UPDATE tb_enseresoc SET F_CantIngresar = ? WHERE F_Oc = ? AND F_IdProveedor = ? AND F_Id = ?;";

    private static String INDICE_COMPRA = "SELECT F_IdCompraEnseres FROM tb_indice;";

    private static String ACTUALIZAINDICE_COMPRA = "UPDATE tb_indice SET F_IdCompraEnseres = ?;";

    private static String BUSCADATOSOC = "SELECT F_IdEnseres, F_CantIngresar, E.F_Existencia, ( O.F_CantIngresar + E.F_Existencia ) AS EXISTENCIA, E.F_Id FROM tb_enseresoc O LEFT JOIN tb_enseres E ON O.F_IdEnseres = E.F_Id WHERE O.F_Oc = ? AND O.F_IdProveedor = ? AND O.F_Sts = 1 AND F_Recibido = 0 AND F_CantIngresar > 0;";

    private static final String INSERTARCOMPRA = "INSERT INTO tb_enserescompra VALUES (0, NOW() , ?, ?, ?, ?, ?, ?);";

    private static final String ACTUALIZAEXISTENCIA = "UPDATE tb_enseres SET F_Existencia = ? WHERE F_Id = ?;";

    private static final String INSERTAMOVIMIENTO = "INSERT INTO tb_enseresmovimiento VALUES (0, NOW() , ?, ?, ?, ?, ?, ?);";

    private final ConectionDBTrans con = new ConectionDBTrans();

    private PreparedStatement psInsertar;
    private PreparedStatement psConsulta;
    private PreparedStatement psIndice;
    private PreparedStatement psElimina;
    private PreparedStatement ps;
    private PreparedStatement psCompra;
    private PreparedStatement psExistencia;
    private PreparedStatement psMovimiento;
    private ResultSet rs;
    private ResultSet rsExistencia;

    @Override
    public boolean RegistrarEnseres(String Usuario, String Unidad, String ClaProE, String FechaEntE, int CantidadE, int Folio) {

        boolean save = false;
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            int Existencia = 0;
            psConsulta = con.getConn().prepareStatement(ValidaExistencia);
            psConsulta.setString(1, ClaProE);
            psConsulta.setString(2, ClaProE);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                Existencia = rs.getInt(3);
            }

            if (Existencia >= CantidadE) {

                psInsertar = con.getConn().prepareStatement(String.format(InsertarEnseres), PreparedStatement.RETURN_GENERATED_KEYS);
                psInsertar.setString(1, Usuario);
                psInsertar.setString(2, Unidad);
                psInsertar.setString(3, ClaProE);
                psInsertar.setString(4, FechaEntE);
                psInsertar.setInt(5, CantidadE);
                psInsertar.setInt(6, Folio);
                psInsertar.setInt(7, 3);
                System.out.println("com.gnk.impl.FacturacionTranDaoImpl.RegistrarEnseres()" + psInsertar);
                boolean ok = psInsertar.executeUpdate() == 1;
                if (!ok) {
                    psInsertar.close();
                    save = false;
                    String mensajeError = String.format("NO creo el registro");
                    throw new SQLException(mensajeError);
                } else {
                    rs = psInsertar.getGeneratedKeys();
                    rs.next();
                    int id = rs.getInt(1);
                    psInsertar.close();
                    psInsertar = null;
                    rs.close();
                    rs = null;
                    save = true;
                    con.getConn().commit();
                    return save;
                }
            } else {
                save = false;
                String mensajeError = String.format("Existencia Menor a lo facturado");
                throw new SQLException(mensajeError);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public JSONArray MostrarRegistros(String Usuario, String Unidad, int Folio) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(ConsultaEnseres);
            psConsulta.setString(1, Usuario);
            psConsulta.setInt(2, Folio);
            psConsulta.setString(3, Unidad);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                Contar++;
                CantidadT = CantidadT + rs.getInt(3);
                jsonObj = new JSONObject();
                jsonObj.put("Descripcion", rs.getString(1));
                jsonObj.put("UM", rs.getString(2));
                jsonObj.put("Cantidad", rs.getString(3));
                jsonObj.put("Id", rs.getString(4));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public boolean EliminarCapEnseres(String Usuario, String Unidad, int Folio) {

        boolean save = false;
        int Contar = 0;
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            psElimina = con.getConn().prepareStatement(EliminarCapturaEnseres);
            psElimina.setString(1, Usuario);
            psElimina.setInt(2, Folio);
            psElimina.setString(3, Unidad);
            psElimina.addBatch();
            psElimina.executeBatch();

            psConsulta = con.getConn().prepareStatement(ValidarCapturaEnseres);
            psConsulta.setString(1, Usuario);
            psConsulta.setInt(2, Folio);
            psConsulta.setString(3, Unidad);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                Contar = rs.getInt(1);
            }

            if (Contar > 0) {
                psConsulta.close();
                psElimina.close();
                psConsulta = null;
                psElimina = null;
                rs.close();
                rs = null;
                save = false;
                String mensajeError = String.format("NO se cancelo el folio");
                throw new SQLException(mensajeError);
            } else {

                con.getConn().commit();
                save = true;
                psConsulta.close();
                psElimina.close();
                psConsulta = null;
                psElimina = null;
                rs.close();
                rs = null;
                return save;
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean EliminaRegistroEnseres(String IdReg) {

        boolean save = false;
        int Contar = 0;
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            psElimina = con.getConn().prepareStatement(EliminaRegistroEnseres);
            psElimina.setString(1, IdReg);
            psElimina.addBatch();
            psElimina.executeBatch();

            psConsulta = con.getConn().prepareStatement(ValidarRegistroEnseres);
            psConsulta.setString(1, IdReg);
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                Contar = rs.getInt(1);
            }

            if (Contar > 0) {
                psConsulta.close();
                psElimina.close();
                psConsulta = null;
                psElimina = null;
                rs.close();
                rs = null;
                save = false;
                String mensajeError = String.format("NO se Elimino el Registro");
                throw new SQLException(mensajeError);
            } else {

                con.getConn().commit();
                save = true;
                psConsulta.close();
                psElimina.close();
                psConsulta = null;
                psElimina = null;
                rs.close();
                rs = null;
                return save;
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public boolean ConfirmarFactTempEnseres(String Usuario, String Observaciones, String Proyecto) {

        boolean save = false;
        int Existencia = 0;
        String CampoI = "";
        int ProyectoU = Integer.parseInt(Proyecto);
        if (ProyectoU == 2) {
            CampoI = "F_IndFactPEMICH";
        } else if ((ProyectoU == 1) || (ProyectoU == 4) || (ProyectoU == 5) || (ProyectoU == 6) || (ProyectoU == 7)) {
            CampoI = "F_IndFactPEISEM";
        }
        if ((ProyectoU == 17)){
            CampoI = "F_IndFactPEHEMO";
        }
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            int Factura = 0;
            psConsulta = con.getConn().prepareStatement(String.format(ConsultaIndice, CampoI));
            rs = psConsulta.executeQuery();
            if (rs.next()) {
                Factura = rs.getInt(1);
            }

            psConsulta.clearParameters();

            psIndice = con.getConn().prepareStatement(String.format(ActualizaIndice,CampoI));
            psIndice.setInt(1, Factura + 1);
            psIndice.addBatch();

            psConsulta = con.getConn().prepareStatement(CapturaFacturar);
            psConsulta.setString(1, Usuario);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                psInsertar = con.getConn().prepareStatement(InsertarEntregaEnseres);
                psInsertar.setString(1, Usuario);
                psInsertar.setString(2, rs.getString(1));
                psInsertar.setString(3, rs.getString(2));
                psInsertar.setString(4, rs.getString(3));
                psInsertar.setString(5, rs.getString(4));
                psInsertar.setInt(6, Factura);
                psInsertar.setString(7, "A");
                psInsertar.setString(8, Observaciones);
                psInsertar.setInt(9, ProyectoU);
                psInsertar.addBatch();
                psInsertar.executeBatch();

                psMovimiento = con.getConn().prepareStatement(INSERTAMOVIMIENTO);
                psMovimiento.setInt(1, Factura);
                psMovimiento.setInt(2, 51);
                psMovimiento.setInt(3, rs.getInt(2));
                psMovimiento.setInt(4, rs.getInt(4));
                psMovimiento.setInt(5, -1);
                psMovimiento.setString(6, Usuario);
                psMovimiento.addBatch();
                psMovimiento.executeBatch();

                ps = con.getConn().prepareStatement(ConsultaExistencia);
                ps.setInt(1, rs.getInt(2));
                rsExistencia = ps.executeQuery();
                if (rsExistencia.next()) {
                    Existencia = rsExistencia.getInt(1);
                }

                Existencia = Existencia - rs.getInt(4);

                if (Existencia >= 0) {
                    psExistencia = con.getConn().prepareStatement(ACTUALIZAEXISTENCIA);
                    psExistencia.setInt(1, Existencia);
                    psExistencia.setInt(2, rs.getInt(2));
                    psExistencia.addBatch();
                    psExistencia.executeBatch();
                } else {
                    save = false;
                    return save;
                }
            }

            psIndice.executeBatch();

            psElimina = con.getConn().prepareStatement(EliminarEnseres);
            psElimina.setString(1, Usuario);
            psElimina.execute();

            con.getConn().commit();
            save = true;

        } catch (SQLException ex) {
            Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(FacturacionEnseresDaoImp.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }

    @Override
    public JSONArray MostrarEnseres() {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(CatalogoEnseres);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonObj.put("Descripcion", rs.getString(2));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray Registro(String OrdenCompra, String Proveedor) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(Registro);
            psConsulta.setString(1, OrdenCompra);
            psConsulta.setString(2, Proveedor);
            rs = psConsulta.executeQuery();
            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("Id", rs.getString(1));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray ActualizaReq(String OrdenCompra, String Proveedor, String Cantidad, String IdRegistro) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String ConsultaExi = "";
        int Contar = 0, CantidadT = 0, ContarClave = 0;
        try {
            con.conectar();

            psConsulta = con.getConn().prepareStatement(ActualizaReq);
            psConsulta.setString(1, Cantidad);
            psConsulta.setString(2, OrdenCompra);
            psConsulta.setString(3, Proveedor);
            psConsulta.setString(4, IdRegistro);
            psConsulta.execute();

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public boolean AutorizarEnseres(String ordenCompra, String Proveedor, String Usuario) {
        boolean save = false;

        int indiceCompra = 0;

        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            ps = con.getConn().prepareStatement(INDICE_COMPRA);
            rs = ps.executeQuery();
            while (rs.next()) {
                indiceCompra = rs.getInt(1);
            }
            ps = con.getConn().prepareStatement(BUSCADATOSOC);
            ps.setString(1, ordenCompra);
            ps.setString(2, Proveedor);
            rs = ps.executeQuery();
            while (rs.next()) {
                psCompra = con.getConn().prepareStatement(INSERTARCOMPRA);
                psCompra.setInt(1, indiceCompra);
                psCompra.setString(2, ordenCompra);
                psCompra.setString(3, Proveedor);
                psCompra.setString(4, Usuario);
                psCompra.setInt(5, rs.getInt(1));
                psCompra.setInt(6, rs.getInt(2));
                psCompra.addBatch();
                psCompra.executeBatch();

                psExistencia = con.getConn().prepareStatement(ACTUALIZAEXISTENCIA);
                psExistencia.setInt(1, rs.getInt(4));
                psExistencia.setInt(2, rs.getInt(1));
                psExistencia.addBatch();
                psExistencia.executeBatch();

                psMovimiento = con.getConn().prepareStatement(INSERTAMOVIMIENTO);
                psMovimiento.setInt(1, indiceCompra);
                psMovimiento.setInt(2, 1);
                psMovimiento.setInt(3, rs.getInt(1));
                psMovimiento.setInt(4, rs.getInt(2));
                psMovimiento.setInt(5, 1);
                psMovimiento.setString(6, Usuario);
                psMovimiento.addBatch();
                psMovimiento.executeBatch();

            }

            ps.clearParameters();
            ps = con.getConn().prepareStatement(ACTUALIZAINDICE_COMPRA);
            ps.setInt(1, indiceCompra + 1);
            ps.execute();

            con.getConn().commit();
            save = true;

            psCompra.close();
            psExistencia.close();
            psMovimiento.close();

        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                save = false;
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExistenciaProyectoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }
}
