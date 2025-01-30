/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.service;

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
public class AbastoService {

    private final String consultaAbasto = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), "
            + "DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(), "
            + "L.F_Origen AS ORIGEN, "
            + "F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot "
            + "AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro "
            + "WHERE F.F_Proyecto = ? AND F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

    private final String queryEliminaAbasto = "DELETE FROM tb_abastoweb WHERE F_Sts = 0 AND F_Proyecto = ? AND F_ClaDoc = ?;";

    private final String queryInserta = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0,?);";

    private final String getFactorEmpaque = "SELECT IFNULL(F_FactorEmpaque, 0) as factor FROM tb_compra where F_Lote = ? order by F_IdCom DESC";

    private Connection con;

    public AbastoService(Connection con) {
        this.con = con;
    }

    public boolean crearAbastoWeb(Integer folio, Integer proyecto, String usuario) {
        try {
            PreparedStatement ps = this.con.prepareStatement(queryEliminaAbasto);
            ps.setInt(1, proyecto);
            ps.setInt(2, folio);
            ps.executeUpdate();

            ps = this.con.prepareStatement(consultaAbasto);
            ps.setInt(1, proyecto);
            ps.setInt(2, folio);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int factorEmpaque = 1;
                int folLot = rs.getInt("LOTE");
                PreparedStatement psfe = con.prepareStatement(getFactorEmpaque);
                psfe.setInt(1, folLot);
                ResultSet rsfe = psfe.executeQuery();
                if (rsfe.next()) {
                    factorEmpaque = rsfe.getInt("factor");
                }

                ps = con.prepareStatement(queryInserta);

                ps.setString(1, rs.getString(1));
                ps.setString(2, rs.getString(2));
                ps.setString(3, rs.getString(3));
                ps.setString(4, rs.getString(4));
                ps.setString(5, rs.getString(5));
                ps.setString(6, rs.getString(6));
                ps.setString(7, rs.getString(7));
                ps.setString(8, rs.getString(8));
                ps.setString(9, rs.getString("ORIGEN"));
                ps.setString(10, rs.getString(10));
                ps.setString(11, usuario);
                ps.setInt(12, factorEmpaque);
                ps.execute();
            }

        } catch (SQLException ex) {
            Logger.getLogger(AbastoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
