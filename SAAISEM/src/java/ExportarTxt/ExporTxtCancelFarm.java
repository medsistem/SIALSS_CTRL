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
 * Proceso para generar archivo txt facturación farmacia secuencial cancelado
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ExporTxtCancelFarm {

    public String Exportar(String cla_uni, String id_usu, String fecha1, String fecha2) {
        String mensaje = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            double F_CosUni = 0.0, CosServSub = 0.0;
            int F_Cansur = 0;
            String F_CosVensIVA = "", F_IVAP = "", F_CosSersIVA = "", F_IVAS = "", F_Cveuni = "";
            File archivo;

            archivo = new File("C:\\TXTISEM\\CF_" + fecha1 + "_al_" + fecha2 + ".txt");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL", "saa_sialss", "C3n+05@2605@2022");
            BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));

            String query = "SELECT F_Secuencial FROM tb_txtis where F_Fecsur between '" + fecha1 + "' and '" + fecha2 + "' and F_Status='C' AND F_FacGNKLAgr LIKE 'AG-F%'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                fw.append(Espacio20(rs.getString(1)));
                fw.append(Espacio10(""));
                fw.append(Espacio3(""));
                fw.append(Espacio4(""));
                fw.append(Espacio15(""));
                fw.append(Espacio15(""));
                fw.append(Espacio5(""));
                fw.append(Espacio5(""));
                fw.append(Espacio20D(""));
                fw.append(Espacio2("0"));
                fw.append(Espacio5(""));
                fw.append(Espacio20(""));
                fw.append(Espacio15(""));
                fw.append(Espacio50(""));
                fw.append(Espacio20("0.00"));
                fw.append(Espacio11("0"));
                fw.append(Espacio11("0"));
                fw.append(Espacio20("0.00"));
                fw.append(SinEspacio(""));
                fw.append(Espacio50(""));
                fw.append(Espacio50(""));
                fw.append(Espacio50(""));
                fw.append(Espacio3("0"));
                fw.append(Espacio2(""));
                fw.append(Espacio255(""));
                fw.append(Espacio2(""));
                fw.append(Espacio2(""));
                fw.append(Espacio100(""));
                fw.append(Espacio2("0"));
                fw.append(Espacio20(""));
                fw.append(SinEspacio(""));
                fw.append(Espacio2("0"));
                fw.append(Espacio2(""));
                fw.append(Espacio20("0.00"));
                fw.append(Espacio20(""));
                fw.append(Espacio20("0.00"));
                fw.append(Espacio20("0.00"));
                fw.append(Espacio20("0.00"));
                fw.append(Espacio20I("0.00"));

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

    public String Espacio255(String largo) {
        String espacios = "";
        String cadena = "";
        int leng = 0;
        leng = largo.length();
        cadena = largo;
        if (leng > 255) {
            cadena = cadena.substring(0, 255);
        }
        System.out.println(cadena);
        for (int x = 0; (x + leng) < 255; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + cadena + espacios + '"' + ',';
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

    public String Espacio50(String largo) {
        String espacios = "";
        String cadena = "";
        int leng = 0;
        leng = largo.length();
        cadena = largo;
        if (leng > 50) {
            cadena = cadena.substring(0, 50);
        }
        System.out.println(cadena);
        for (int x = 0; (x + leng) < 50; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + cadena + espacios + '"' + ',';
        return espacios;
    }

    public String Espacio20(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 20; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio20D(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 20; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + largo + espacios + '"' + ',';
        return espacios;
    }

    public String Espacio20I(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 20; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"';
        return espacios;
    }

    public String Espacio10(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 10; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio11(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 11; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
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

    public String Espacio5(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 5; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio3(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 3; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio4(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 4; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Espacio2(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 2; x++) {
            espacios = espacios + " ";
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String SinEspacio(String largo) {
        String espacios = "";

        espacios = '"' + largo + '"' + ',';
        return espacios;
    }

    public String Trescero(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 3; x++) {
            espacios = espacios + 0;
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String Cuatrocero(String largo) {
        String espacios = "";
        int leng = 0;
        leng = largo.length();
        for (int x = 0; (x + leng) < 4; x++) {
            espacios = espacios + 0;
        }
        espacios = '"' + espacios + largo + '"' + ',';
        return espacios;
    }

    public String ValidaCeaps(String F_Cveuni) {
        String Subtotal = "";
        if (F_Cveuni.equals("CSSA017114")) {
            //Subtotal="7.84";
            Subtotal = "0";
        } else if (F_Cveuni.equals("CSSA017153")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("CSSA017154")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("CSSA017109")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("CSSA017126")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("CSSA017151")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("MCSSA000982")) {
            Subtotal = "0";
        } else if (F_Cveuni.equals("CEAPSBEJ001")) {
            Subtotal = "0";
        } else {
            //Subtotal="4.74";
            Subtotal = "0";
        }
        return Subtotal;
    }
}
