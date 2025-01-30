/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ExportarTxt;

import java.sql.ResultSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Exportar médico
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ExportMedico {

    public String ExporMed(String cla_uni, String id_usu) {
        String mensaje = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            File archivo;
            archivo = new File("C:\\TXTISEM\\T_Medicos.txt");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL", "saa_sialss", "C3n+05@2605@2022");
            BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));

            String query = "Select F_JurUniIs, F_ClaUniIs, F_ClaMedis, F_DesMedis "
                    + "from TB_UnidIS inner join TB_MediIS on TB_MediIS.F_ClaMedis=TB_UnidIS.F_MedUniIS "
                    + "where F_MedUniIS <>'' order by F_JurUniIs, F_ClaUniIs";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                fw.append(Espacio15(rs.getString(1)));
                fw.append(Espacio15(rs.getString(2)));
                fw.append(Espacio18("0"));
                fw.append(Espacio20(rs.getString(3)));
                fw.append(Espacio100(rs.getString(4)));
                fw.append(SinEspacio("1"));
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

    public String Espacio15(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 15; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + largo + espacios + '"' + ',';
        return espacios;
    }

    public String Espacio18(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 18; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio20(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 20; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + largo + espacios + '"' + ',';
        return espacios;
    }

    public String Espacio100(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 100; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + largo + espacios + '"' + ',';
        return espacios;
    }

    public String SinEspacio(String largo) {
        String espacios = "";

        espacios = '"' + largo + '"';
        return espacios;
    }
}
