/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModificarFolio;

import Develuciones.ConsultaDevoDaoImpl;
import conn.ConectionDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author MEDALFA
 */
public class ConsultaModificafolioDaoImpl implements ConsultaModificafolioDao {

    @Override
    public JSONArray getRegistro(String Folio) {

        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;

        String query = "SELECT F_IdFact FROM tb_modificacionfolio WHERE F_ClaDoc=?;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            rs = ps.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("IdReg", rs.getString(1));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray getEliminaRegistro(String Folio, String IdReg) {
        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        System.out.println("Eliminar Reg Folio:" + Folio + " Id: " + IdReg);
        String query = "DELETE FROM tb_modificacionfolio WHERE F_ClaDoc=? AND F_IdFact=?;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            ps.setString(2, IdReg);
            ps.execute();

            jsonObj = new JSONObject();
            jsonObj.put("Procesado", "Terminado");
            jsonArray.add(jsonObj);

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray getValidaDevolucion(String Folio, String Obs, String Usuario) {
        int IdFact = 0, CantDevo = 0, Lote = 0, CantReq = 0, CantSur = 0, DevoCant = 0, IdLote = 0, IdLote1 = 0, ExiLot = 0, Total = 0, Contar = 0;
        int IndDev = 0, DocDev = 0, Proyecto = 0;
        double Iva = 0.00, Monto = 0.00, Costo = 0.00, IvaDeVo = 0.00, IvaTot = 0.00, IvaTotDev = 0.00, MontoDev = 0.00;
        String ClaCli = "", ClaPro = "", FecEnt = "", Ubicacion = "", ClaLot = "", FecCad = "", FecFab = "", Cb = "", ClaMar = "", Origen = "";
        String Consulta = "", Consulta2 = "", Concepto = "", DocAnt = "", UbicaDes = "", ClaOrg = "";
        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;

       if (Usuario.equals("Facturacion")) {
        Concepto = "4";
            DocAnt = "1";
        } else {
            Concepto = "5";
            DocAnt = "0";
        }

        String query = "SELECT COUNT(F_IdFact) FROM tb_modificacionfolio WHERE F_ClaDoc=?;";
        PreparedStatement ps;
        ResultSet rs;
        ResultSet rsD;
        PreparedStatement ps2;
        ResultSet rs2;
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            rs = ps.executeQuery();
            if (rs.next()) {
                Contar = rs.getInt(1);
            }
            if (Contar > 0) {
                Consulta2 = "SELECT F_IndDev FROM tb_indice;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    IndDev = rs2.getInt(1);
                }
                ps2.clearParameters();

                Consulta = "SELECT F_IdFact,F_CantDevo FROM tb_modificacionfolio WHERE F_ClaDoc=?;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rsD = ps.executeQuery();
                while (rsD.next()) {
                    IdFact = rsD.getInt(1);
                    CantDevo = rsD.getInt(2);

                    Consulta2 = "SELECT F_ClaCli, F_ClaPro, F_CantReq, F_CantSur, F_Costo, F_Iva, F_Monto, F_Lote, F_FecEnt, F_Ubicacion, F_Proyecto FROM tb_factura WHERE F_IdFact = ? AND F_ClaDoc = ?;";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        ClaCli = rs2.getString(1);
                        ClaPro = rs2.getString(2);
                        CantReq = rs2.getInt(3);
                        CantSur = rs2.getInt(4);
                        Costo = rs2.getDouble(5);
                        Iva = rs2.getDouble(6);
                        Monto = rs2.getDouble(7);
                        Lote = rs2.getInt(8);
                        FecEnt = rs2.getString(9);
                        Ubicacion = rs2.getString(10);
                        Proyecto = rs2.getInt(11);
                    }
                    ps2.clearParameters();
                    if (Concepto.equals("4")) {
                        UbicaDes = Ubicacion;
                    } else {
                        UbicaDes = "NUEVA";
                    }
                    DevoCant = CantSur - CantDevo;
                    if (DevoCant == 0) {

                        Consulta = "UPDATE tb_factura SET F_StsFact=?,F_Obs=?,F_DocAnt=? WHERE F_IdFact=? AND F_ClaDoc=?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, "C");
                        ps.setString(2, Obs);
                        ps.setString(3, DocAnt);
                        ps.setInt(4, IdFact);
                        ps.setString(5, Folio);
                        ps.execute();
                        ps.clearParameters();

                        Consulta = "SELECT F_IdLote,F_ExiLot,F_ClaOrg FROM tb_lote WHERE F_ClaPro=? AND F_FolLot=? AND F_Ubica=? AND F_Proyecto=?;;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, ClaPro);
                        ps.setInt(2, Lote);
                        ps.setString(3, UbicaDes);
                        ps.setInt(4, Proyecto);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            IdLote = rs.getInt(1);
                            ExiLot = rs.getInt(2);
                            ClaOrg = rs.getString(3);
                        }
                        ps.clearParameters();

                        if (IdLote > 0) {

                            Total = ExiLot + CantDevo;
                            Consulta2 = "UPDATE tb_lote SET F_ExiLot=? WHERE F_IdLote=? AND F_ClaPro=? AND F_FolLot=? AND F_Ubica=? AND F_Proyecto=?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setInt(1, Total);
                            ps2.setInt(2, IdLote);
                            ps2.setString(3, ClaPro);
                            ps2.setInt(4, Lote);
                            ps2.setString(5, UbicaDes);
                            ps2.setInt(6, Proyecto);
                            ps2.execute();
                            ps2.clearParameters();

                        } else {

                            Consulta = "SELECT F_IdLote,F_ExiLot,F_ClaLot,F_FecCad,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_ClaOrg FROM tb_lote WHERE F_ClaPro=? AND F_FolLot=? AND F_Proyecto=?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, ClaPro);
                            ps.setInt(2, Lote);
                            ps.setInt(3, Proyecto);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                IdLote1 = rs.getInt(1);
                                ExiLot = rs.getInt(2);
                                ClaLot = rs.getString(3);
                                FecCad = rs.getString(4);
                                FecFab = rs.getString(5);
                                Cb = rs.getString(6);
                                ClaMar = rs.getString(7);
                                Origen = rs.getString(8);
                                ClaOrg = rs.getString(9);
                            }
                            ps.clearParameters();

                            if (IdLote1 > 0) {
                                Consulta2 = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,'131');";
                                ps2 = con.getConn().prepareStatement(Consulta2);
                                ps2.setString(1, ClaPro);
                                ps2.setString(2, ClaLot);
                                ps2.setString(3, FecCad);
                                ps2.setInt(4, CantDevo);
                                ps2.setInt(5, Lote);
                                ps2.setString(6, ClaOrg);
                                ps2.setString(7, UbicaDes);
                                ps2.setString(8, FecFab);
                                ps2.setString(9, Cb);
                                ps2.setString(10, ClaMar);
                                ps2.setString(11, Origen);
                                ps2.setString(12, ClaOrg);
                                ps2.execute();
                                ps2.clearParameters();

                                IdLote1 = 0;
                            }

                        }

                        Consulta2 = "INSERT INTO tb_movinv VALUES(0,CURDATE(),?,?,?,?,?,?,'1',?,?,?,CURTIME(),?,'');";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setInt(1, IndDev);
                        ps2.setString(2, Concepto);
                        ps2.setString(3, ClaPro);
                        ps2.setInt(4, CantDevo);
                        ps2.setDouble(5, Costo);
                        ps2.setDouble(6, Monto);
                        ps2.setInt(7, Lote);
                        ps2.setString(8, UbicaDes);
                        ps2.setString(9, ClaOrg);
                        ps2.setString(10, Usuario);
                        ps2.execute();
                        ps2.clearParameters();

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;
                    } else if (DevoCant > 0) {

                        if (Iva > 0) {
                            IvaDeVo = 0.16;
                        } else {
                            IvaDeVo = 0.00;
                        }
                        IvaTot = ((DevoCant * Costo) * IvaDeVo);
                        Monto = (DevoCant * Costo) + IvaTot;

                        IvaTotDev = ((CantDevo * Costo) * IvaDeVo);
                        MontoDev = ((CantDevo * Costo) * IvaTotDev);

                        Consulta = "UPDATE tb_factura SET F_CantSur=?,F_Iva=?,F_Monto=? WHERE F_IdFact=? AND F_ClaDoc=?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setInt(1, DevoCant);
                        ps.setDouble(2, IvaTot);
                        ps.setDouble(3, Monto);
                        ps.setInt(4, IdFact);
                        ps.setString(5, Folio);
                        ps.execute();
                        ps.clearParameters();

                        Consulta2 = "INSERT INTO tb_factura VALUES(0,?,?,'C',CURDATE(),?,'0',?,?,?,?,?,?,CURTIME(),?,?,?,?,?);";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, Folio);
                        ps2.setString(2, ClaCli);
                        ps2.setString(3, ClaPro);
                        ps2.setInt(4, CantDevo);
                        ps2.setDouble(5, Costo);
                        ps2.setDouble(6, IvaTotDev);
                        ps2.setDouble(7, MontoDev);
                        ps2.setInt(8, Lote);
                        ps2.setString(9, FecEnt);
                        ps2.setString(10, Usuario);
                        ps2.setString(11, Ubicacion);
                        ps2.setString(12, Obs);
                        ps2.setString(13, DocAnt);
                        ps2.setInt(14, Proyecto);
                        ps2.execute();
                        ps2.clearParameters();

                        Consulta2 = "SELECT F_IdLote,F_ExiLot,F_ClaOrg FROM tb_lote WHERE F_ClaPro=? AND F_FolLot=? AND F_Ubica=? AND F_Proyecto=?;";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, ClaPro);
                        ps2.setInt(2, Lote);
                        ps2.setString(3, UbicaDes);
                        ps2.setInt(4, Proyecto);
                        rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            IdLote = rs2.getInt(1);
                            ExiLot = rs2.getInt(2);
                            ClaOrg = rs2.getString(3);
                        }
                        ps2.clearParameters();

                        if (IdLote > 0) {
                            Total = ExiLot + CantDevo;
                            Consulta = "UPDATE tb_lote SET F_ExiLot=? WHERE F_IdLote=? AND F_ClaPro=? AND F_FolLot=? AND F_Ubica=? AND F_Proyecto=?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, Total);
                            ps.setInt(2, IdLote);
                            ps.setString(3, ClaPro);
                            ps.setInt(4, Lote);
                            ps.setString(5, UbicaDes);
                            ps.setInt(6, Proyecto);
                            ps.execute();
                            ps.clearParameters();

                        } else {

                            Consulta2 = "SELECT F_IdLote,F_ExiLot,F_ClaLot,F_FecCad,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_ClaOrg FROM tb_lote WHERE F_ClaPro=? AND F_FolLot=? AND F_Proyecto=?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setString(1, ClaPro);
                            ps2.setInt(2, Lote);
                            ps2.setInt(3, Proyecto);
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                IdLote1 = rs2.getInt(1);
                                ExiLot = rs2.getInt(2);
                                ClaLot = rs2.getString(3);
                                FecCad = rs2.getString(4);
                                FecFab = rs2.getString(5);
                                Cb = rs2.getString(6);
                                ClaMar = rs2.getString(7);
                                Origen = rs2.getString(8);
                                ClaOrg = rs2.getString(9);
                            }
                            ps2.clearParameters();

                            if (IdLote1 > 0) {
                                Consulta = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,'131');";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setString(1, ClaPro);
                                ps.setString(2, ClaLot);
                                ps.setString(3, FecCad);
                                ps.setInt(4, CantDevo);
                                ps.setInt(5, Lote);
                                ps.setString(6, ClaOrg);
                                ps.setString(7, UbicaDes);
                                ps.setString(8, FecFab);
                                ps.setString(9, Cb);
                                ps.setString(10, ClaMar);
                                ps.setString(11, Origen);
                                ps.setString(12, ClaOrg);
                                ps.execute();
                                ps.clearParameters();

                                IdLote1 = 0;
                            }
                        }

                        Consulta = "INSERT INTO tb_movinv VALUES(0,CURDATE(),?,?,?,?,?,?,'1',?,?,?,CURTIME(),?,'');";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setInt(1, IndDev);
                        ps.setString(2, Concepto);
                        ps.setString(3, ClaPro);
                        ps.setInt(4, CantDevo);
                        ps.setDouble(5, Costo);
                        ps.setDouble(6, MontoDev);
                        ps.setInt(7, Lote);
                        ps.setString(8, UbicaDes);
                        ps.setString(9, ClaOrg);
                        ps.setString(10, Usuario);
                        ps.execute();
                        ps.clearParameters();

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;
                    }

                    Consulta2 = "DELETE FROM tb_modificacionfolio WHERE F_IdFact=? AND F_ClaDoc=?;";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    ps2.execute();
                    ps2.clearParameters();

                    int Existencia = 0;
                    Consulta = "SELECT SUM(F_CantSur) FROM tb_factura WHERE F_ClaDoc=? AND F_StsFact='A';";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, Folio);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Existencia = rs.getInt(1);
                    }
                    ps.clearParameters();

                    if (Existencia == 0) {
                        if (DocAnt.equals("0")) {
                            Consulta2 = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=? AND F_DocAnt !=1;";
                        } else {
                            Consulta2 = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=?;";
                        }
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, "C");
                        ps2.setString(2, DocAnt);
                        ps2.setString(3, Folio);
                        ps2.execute();
                        ps2.clearParameters();
                    }
                    Existencia = 0;

                }

                Consulta = "INSERT INTO tb_devoluciones VALUES(CURRENT_TIMESTAMP(),?,?,?,?,0);";
                System.out.println("insert devo modifica" + Consulta);
                ps = con.getConn().prepareStatement(Consulta);
                ps.setInt(1, IndDev);
                ps.setString(2, Folio);
                ps.setString(3, Obs);
                ps.setString(4, Usuario);
                ps.execute();
                ps.clearParameters();

                Consulta2 = "UPDATE tb_indice SET F_IndDev=?";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setInt(1, IndDev + 1);
                ps2.execute();
                ps2.clearParameters();

                jsonObj = new JSONObject();
                jsonObj.put("Validado", "Validado");
                jsonArray.add(jsonObj);
            } else {
                jsonObj = new JSONObject();
                jsonObj.put("Validado", "NoValidado");
                jsonArray.add(jsonObj);
            }
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

}
