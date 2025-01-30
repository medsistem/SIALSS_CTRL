/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abastos;

import java.sql.ResultSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;

import java.io.File;

/**
 * Proceso para generar abasto .SGW
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class AbastoGNKLitExport {

    public String Exportar(String id_usu, String Folio) {

        String mensaje = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            double F_CosUni = 0.0, CosServSub = 0.0;
            int F_Cansur = 0;
            String F_CosVensIVA = "", F_IVAP = "", F_CosSersIVA = "", F_IVAS = "", F_Cveuni = "", Tipo = "";
            File archivo;

            archivo = new File("C:\\TXTISEM\\T_" + Folio + ".SGW");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL", "saa_sialss", "C3n+05@2605@2022");
            BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));

            String query = "SELECT F_ClaDoc, F_FecApl, M.F_ClaPro, F_CantSur, F_Costo, F_Lote, F_DesArtIS, F_ClaLot, F_Monto, F_FecCad "
                    + "FROM tb_transferencias M INNER JOIN tb_artiis A ON M.F_ClaPro = LTRIM(RTRIM(A.F_ClaInt)) INNER JOIN TB_Lote L ON M.F_Lote = L.F_FolLot "
                    + "WHERE F_ClaDoc = '76' "
                    + "ORDER BY F_FecApl, F_ClaDoc";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                fw.append(rs.getString(1) + ",");

                fw.newLine();
            }
            fw.flush();
            fw.close();
            conn.close();
            System.out.println("Se creo correctamente");
        } catch (Exception e) {
            System.err.println("No se pudo generar el archivo" + e);
        }
        return mensaje;
    }

}
