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
 * Proceso para generar archivo txt facturación farmacia isem
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ExporTxtFarm {

    public String Exportar(String cla_uni, String id_usu, String fecha1, String fecha2) {
        String mensaje = "Se Genero Correctamente";
        DecimalFormat f = new DecimalFormat("##0.00");
        try {
            double F_CosUni = 0.0, CosServSub = 0.0;
            int F_Cansur = 0;
            String F_CosVensIVA = "", F_IVAP = "", F_CosSersIVA = "", F_IVAS = "", F_Cveuni = "", Tipo = "";
            File archivo;

            archivo = new File("C:\\TXTISEM\\TF_" + fecha1 + "_al_" + fecha2 + ".txt");
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/SIALSS_CTRL","saa_sialss", "C3n+05@2605@2022");
            BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));

            String query = "SELECT F_Secuencial,F_Cvepro,F_Cvemun,F_CveLoc,F_CveJur,F_Cveuni,F_CveCie,F_CveCie2,F_Cvemed,"
                    + "F_Cvesum,F_Cveser,F_Cvepac,F_Cveart,F_Presen,F_CosUni,F_Canreq,F_Cansur,F_Saldo,DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur,"
                    + "F_NomPac,F_ApePatP,F_ApeMatP,F_Edad,F_GeneroP,F_DesGen,F_Idsur,F_CveCon,F_NomMed,F_Tipreq,F_Folrec,DATE_FORMAT(F_Fecexp,'%d/%m/%Y') AS F_Fecexp,"
                    + "F_IdePro,F_Region,F_CosServ,F_FacSAVI,(F_Cansur * F_CosUni) AS F_CosVensIVA,((F_Cansur * F_CosUni) * 0.16) AS F_IVAP,"
                    + "(4.74 * F_Cansur) AS F_CosSersIVA,((4.74 * F_Cansur) * 0.16) AS F_IVAS "
                    + "FROM tb_txtis where F_Fecsur between '" + fecha1 + "' and '" + fecha2 + "' AND F_FacGNKLAgr LIKE 'AG-F%' AND F_Status !='C'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                F_Cansur = rs.getInt("F_Cansur");
                F_CosUni = rs.getDouble("F_CosUni");
                F_Cveuni = ValidaCeaps(rs.getString("F_Cveuni"));
                CosServSub = Double.parseDouble(F_Cveuni);
                F_CosVensIVA = (f.format(F_Cansur * F_CosUni));
                F_IVAP = (f.format(((F_Cansur * F_CosUni) * 0.16)));
                F_CosSersIVA = (f.format(CosServSub * F_Cansur));
                F_IVAS = (f.format(((CosServSub * F_Cansur) * 0.16)));
                Tipo = rs.getString("F_IdePro");
                if (Tipo.equals("1")) {
                    Tipo = "2";
                } else {
                    Tipo = "1";
                }

                fw.append(Espacio20(rs.getString(1)));
                fw.append(Espacio10(rs.getString(2)));
                fw.append(Trescero(rs.getString(3)));
                fw.append(Cuatrocero(rs.getString(4)));
                fw.append(Espacio15(rs.getString(5)));
                fw.append(Espacio15(rs.getString(6)));
                fw.append(Espacio5(rs.getString(7)));
                fw.append(Espacio5(rs.getString(8)));
                fw.append(Espacio20D(rs.getString(9)));
                fw.append(Espacio2(rs.getString(10)));
                fw.append(Espacio5(rs.getString(11)));
                fw.append(Espacio20(rs.getString(12)));
                fw.append(Espacio15(rs.getString(13)));
                fw.append(Espacio50(rs.getString(14)));
                fw.append(Espacio20(rs.getString(15)));
                fw.append(Espacio11(rs.getString(16)));
                fw.append(Espacio11(rs.getString(17)));
                fw.append(Espacio20(rs.getString(18)));
                fw.append(SinEspacio(rs.getString(19)));
                fw.append(Espacio50(rs.getString(20)));
                fw.append(Espacio50(rs.getString(21)));
                fw.append(Espacio50(rs.getString(22)));
                fw.append(Espacio3(rs.getString(23)));
                fw.append(Espacio2(rs.getString(24)));
                fw.append(Espacio255(rs.getString(25)));
                fw.append(Espacio2(rs.getString(26)));
                fw.append(Espacio2(rs.getString(27)));
                fw.append(Espacio100(rs.getString(28)));
                fw.append(Espacio2(rs.getString(29)));
                fw.append(Espacio20(rs.getString(30)));
                fw.append(SinEspacio(rs.getString(31)));
                fw.append(Espacio2(Tipo));
                fw.append(Espacio2(rs.getString(33)));
                fw.append(Espacio20(rs.getString(34)));
                fw.append(Espacio20(rs.getString(35)));
                fw.append(Espacio20(F_CosVensIVA));
                fw.append(Espacio20(F_IVAP));
                fw.append(Espacio20(F_CosSersIVA));
                fw.append(Espacio20I(F_IVAS));

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
