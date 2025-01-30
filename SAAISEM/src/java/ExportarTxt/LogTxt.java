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
 * Creación de log de errores al generar los secuenciales
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class LogTxt {

    public String LogLimpiar() {
        String LogLimpiar = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            File archivo;
            archivo = new File("C:\\SecuencialesLOGTXT\\SecLog.txt");
            BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));

            fw.write("");

            fw.close();

            System.out.println("Se creo correctamente");
        } catch (Exception e) {
            System.err.println("No se pudo generar el archivo" + e);
        }
        return LogLimpiar;
    }

    public String LogErrorArticulo(String ClaPro) {
        String LogArticulo = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            File archivo;
            String Articulo = "";
            archivo = new File("C:\\SecuencialesLOGTXT\\SecLog.txt");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL","saa_sialss", "C3n+05@2605@2022");

            FileWriter fw = new FileWriter(archivo, true);
            fw.write("");
            String query = "SELECT F_ClaArtIS FROM tb_artiis WHERE F_ClaInt='" + ClaPro + "'";
            System.out.println(query);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Articulo = rs.getString(1);
            }
            System.out.println("arti:" + Articulo);
            if (Articulo.equals("")) {
                System.out.println("Entro");
                fw.write(ClaPro + " Articulo no Relacionado");
                fw.write("\r\n");
                fw.flush();
                fw.close();
            }

            conn.close();
            System.out.println("Se creo correctamente");
        } catch (Exception e) {
            System.err.println("No se pudo generar el archivo" + e);
        }
        return LogArticulo;
    }

    public String LogErrorUnidad(String F_Cliente) {
        String LogUnidad = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            File archivo;
            String Unidad = "";
            archivo = new File("C:\\SecuencialesLOGTXT\\SecLog.txt");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL", "saa_sialss", "C3n+05@2605@2022");
            //BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));
            FileWriter fw = new FileWriter(archivo, true);
            String query = "Select F_ClaUniIS FROM tb_unidis   WHERE F_ClaInt1 = '" + F_Cliente + "' OR  F_ClaInt2 = '" + F_Cliente + "' OR  F_ClaInt3 = '" + F_Cliente + "' "
                    + "OR  F_ClaInt4 = '" + F_Cliente + "' OR  F_ClaInt5 = '" + F_Cliente + "' OR  F_ClaInt6 = '" + F_Cliente + "' OR  F_ClaInt7 = '" + F_Cliente + "' OR  F_ClaInt8 = '" + F_Cliente + "' "
                    + "OR  F_ClaInt9 = '" + F_Cliente + "' OR  F_ClaInt10 = '" + F_Cliente + "'";
            System.out.println(query);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Unidad = rs.getString(1);
            }
            System.out.println("arti:" + Unidad);
            if (Unidad.equals("")) {
                System.out.println("Entro");
                fw.write(F_Cliente + " Unidad no Relacionada");
                fw.write("\r\n");
                fw.flush();
                fw.close();
            }

            conn.close();
            System.out.println("Se creo correctamente");
        } catch (Exception e) {
            System.err.println("No se pudo generar el archivo" + e);
        }
        return LogUnidad;
    }

}
