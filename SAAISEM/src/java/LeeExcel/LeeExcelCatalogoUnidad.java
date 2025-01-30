/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LeeExcel;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Anibal GNKL
 */
public class LeeExcelCatalogoUnidad {

    private Vector vectorDataExcelXLSX = new Vector();

    public boolean obtieneArchivo(String path, String file, String Usuario) {

        String excelXLSXFileName = path + "/exceles/" + file;
        vectorDataExcelXLSX = readDataExcelXLSX(excelXLSXFileName);
        displayDataExcelXLSX(vectorDataExcelXLSX, Usuario);
        return true;
    }

    public Vector readDataExcelXLSX(String fileName) {
        Vector vectorData = new Vector();

        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);

            XSSFWorkbook xssfWorkBook = new XSSFWorkbook(fileInputStream);

            // Read data at sheet 0
            XSSFSheet xssfSheet = xssfWorkBook.getSheetAt(0);

            Iterator rowIteration = xssfSheet.rowIterator();

            // Looping every row at sheet 0
            while (rowIteration.hasNext()) {
                XSSFRow xssfRow = (XSSFRow) rowIteration.next();
                Iterator cellIteration = xssfRow.cellIterator();

                Vector vectorCellEachRowData = new Vector();

                // Looping every cell in each row at sheet 0
                while (cellIteration.hasNext()) {
                    XSSFCell xssfCell = (XSSFCell) cellIteration.next();
                    vectorCellEachRowData.addElement(xssfCell);
                }

                vectorData.addElement(vectorCellEachRowData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return vectorData;
    }

    public void displayDataExcelXLSX(Vector vectorData, String User) {
        System.out.println("User: " + User);
        // Looping every row data in vector
        DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy");

        ConectionDB con = new ConectionDB();
        try {
            con.conectar();
            con.actualizar("DELETE FROM tb_cargacatunidad WHERE F_User='" + User + "'");
            System.out.println("Registros Eliminados");
            con.cierraConexion();
        } catch (Exception e) {

        }

        for (int i = 0; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String qry = "insert into tb_cargacatunidad values (";
            String qryElimina = "";
            // looping every cell in each row

            try {
                con.conectar();
                try {
                    String ClaPro = ((vectorCellEachRowData.get(1).toString()) + "");
                    DecimalFormat formatter = new DecimalFormat("0000.00");
                    if (ClaPro.equals("260.02") || ClaPro.equals("801.01") || ClaPro.equals("0260.02") || ClaPro.equals("0801.01")) {
                        formatter = new DecimalFormat("000.00");
                    }
                    DecimalFormatSymbols custom = new DecimalFormatSymbols();
                    custom.setDecimalSeparator('.');
                    custom.setGroupingSeparator(',');
                    formatter.setDecimalFormatSymbols(custom);
                    /*if (!(ClaPro.equals("5.0"))) {
                        ClaPro = formatter.format(Double.parseDouble(ClaPro));
                    }*/
                    String[] punto = ClaPro.split("\\.");
                    System.out.println(punto.length);
                    if (punto.length > 1) {
                        System.out.println(ClaPro + "***" + punto[0] + "////" + punto[1]);
                        if (punto[1].equals("01")) {
                            ClaPro = (punto[0]) + ".01";
                        } else if (punto[1].equals("02")) {
                            ClaPro = (punto[0]) + ".02";
                        } else if (punto[1].equals("10")) {
                            ClaPro = (punto[0]) + ".1";
                        } else if (punto[1].equals("20")) {
                            ClaPro = (punto[0]) + ".2";
                        } else if (punto[1].equals("30")) {
                            ClaPro = (punto[0]) + ".3";
                        } else if (punto[1].equals("40")) {
                            ClaPro = (punto[0]) + ".4";
                        } else if (punto[1].equals("50")) {
                            ClaPro = (punto[0]) + ".5";
                        } else if (punto[1].equals("03")) {
                            ClaPro = (punto[0]) + ".03";
                        } else if (punto[1].equals("04")) {
                            ClaPro = (punto[0]) + ".04";
                        } else if (punto[1].equals("05")) {
                            ClaPro = (punto[0]) + ".05";
                        } else if (punto[1].equals("00")) {
                            ClaPro = (punto[0]);
                        } else {
                            ClaPro = (punto[0]);
                        }
                        System.out.println(ClaPro);
                    }

                    // con.insertar("delete from tb_unireq where F_ClaUni = '" + (vectorCellEachRowData.get(0).toString() + "").trim() + "' and F_ClaPro = '" + agrega(ClaPro) + "' and F_Status=0;");
                } catch (Exception e) {
                }
                for (int j = 0; j < 2; j++) {

                    if (j == 0) {
                        try {
                            String Clave = (vectorCellEachRowData.get(j).toString() + "").trim();
                            /*NumberFormat formatter = new DecimalFormat("0000");
                             Clave = formatter.format(Double.parseDouble(Clave));*/
                            System.out.println(Clave);
                            Clave.replaceAll("^\\s*", "");
                            Clave.replaceAll(" ", "");
                            Clave.replaceAll("&nbsp;", "");
                            for (int x = 0; x < Clave.length(); x++) {
                                System.out.println(Clave.charAt(x) + " = " + Clave.codePointAt(x));
                            };
                            qry = qry + "'" + Clave + "', ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (j == 1) {
                        System.out.println("algo");
                        try {
                            String ClaPro = ((vectorCellEachRowData.get(j).toString()) + "");
                            DecimalFormat formatter = new DecimalFormat("0000.00");
                            if (ClaPro.equals("260.02") || ClaPro.equals("801.01") || ClaPro.equals("0260.02") || ClaPro.equals("0801.01")) {
                                formatter = new DecimalFormat("000.00");
                            }
                            DecimalFormatSymbols custom = new DecimalFormatSymbols();
                            custom.setDecimalSeparator('.');
                            custom.setGroupingSeparator(',');
                            formatter.setDecimalFormatSymbols(custom);
                            /*if (!(ClaPro.equals("5.0"))) {
                                ClaPro = formatter.format(Double.parseDouble(ClaPro));
                            }*/
                            String[] punto = ClaPro.split("\\.");
                            System.out.println(punto.length);
                            if (punto.length > 1) {
                                System.out.println(ClaPro + "***" + punto[0] + "////" + punto[1]);
                                if (punto[1].equals("01")) {
                                    ClaPro = (punto[0]) + ".01";
                                } else if (punto[1].equals("02")) {
                                    ClaPro = (punto[0]) + ".02";
                                } else if (punto[1].equals("10")) {
                                    ClaPro = (punto[0]) + ".1";
                                } else if (punto[1].equals("20")) {
                                    ClaPro = (punto[0]) + ".2";
                                } else if (punto[1].equals("30")) {
                                    ClaPro = (punto[0]) + ".3";
                                } else if (punto[1].equals("40")) {
                                    ClaPro = (punto[0]) + ".4";
                                } else if (punto[1].equals("50")) {
                                    ClaPro = (punto[0]) + ".5";
                                } else if (punto[1].equals("03")) {
                                    ClaPro = (punto[0]) + ".03";
                                } else if (punto[1].equals("04")) {
                                    ClaPro = (punto[0]) + ".04";
                                } else if (punto[1].equals("05")) {
                                    ClaPro = (punto[0]) + ".05";
                                } else if (punto[1].equals("00")) {
                                    ClaPro = (punto[0]);
                                } else {
                                    ClaPro = (punto[0]);
                                }
                                System.out.println(ClaPro);
                            }
                            if (ClaPro.equals("5")) {
                                qry = qry + "'" + ClaPro + "' , ";
                            } else {
                                qry = qry + "'" + agrega(ClaPro) + "' , ";
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                String Cpm = ((vectorCellEachRowData.get(2).toString()) + "");
                if ((Cpm.equals("CPM")) || (Cpm.equals("cpm"))) {
                    Cpm = "0";
                }

                String Cause = ((vectorCellEachRowData.get(3).toString()) + "");
                qry = qry + "curdate(), '" + Cpm + "','" + Cause + "','" + User + "',0,0,0)"; // agregar campos fuera del excel
                con.insertar(qry);
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        try {
            con.conectar();
            int ContarCat = 0;
            ResultSet Claves = null;
            ResultSet CatClaves = null;

            con.actualizar("DELETE FROM tb_cargacatunidad WHERE F_Cpm='CAUSE' AND F_User='" + User + "';");

            con.actualizar("UPDATE tb_cargacatunidad O INNER JOIN tb_uniatn U ON O.F_ClaUni = U.F_ClaCli SET O.F_ProblemaUni = 1 WHERE O.F_User='" + User + "';");

            con.actualizar("UPDATE tb_cargacatunidad O INNER JOIN tb_medica M ON O.F_ClaPro = M.F_ClaPro SET O.F_ProblemaPro = 1 WHERE O.F_User='" + User + "';");

            ResultSet Consulta = con.consulta("SELECT F_ClaUni, COUNT(*), SUM(F_ProblemaUni), SUM(F_ProblemaPro) FROM tb_cargacatunidad WHERE F_User = '" + User + "' GROUP BY F_ClaUni;");
            while (Consulta.next()) {
                String Unidad = Consulta.getString(1);
                int NoReg = Consulta.getInt(2);
                int NoUni = Consulta.getInt(3);
                int NoClave = Consulta.getInt(4);

                if ((NoReg == NoClave) && (NoReg == NoUni)) {

                    Claves = con.consulta("SELECT F_ClaUni, F_ClaPro, F_Cpm, U.F_Tipo, F_Cause FROM tb_cargacatunidad C INNER JOIN tb_uniatn U ON C.F_ClaUni = U.F_ClaCli WHERE F_User = '" + User + "' AND C.F_ClaUni = '" + Unidad + "' GROUP BY F_ClaPro;");
                    while (Claves.next()) {

                        con.actualizar("DELETE FROM tb_medicaunidad WHERE F_ClaUni = '" + Claves.getString(1) + "' AND F_ClaPro = '" + Claves.getString(2) + "';");

                        con.insertar("INSERT INTO tb_medicaunidad VALUES('" + Claves.getString(1) + "','" + Claves.getString(2) + "',1,'" + Claves.getString(3) + "','" + Claves.getString(4) + "',NOW(),'" + Claves.getString(5) + "',1,'" + User + "',0)");
                    }

                }
            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String agrega(String clave) {
        String clave2 = "";
        if (clave.length() < 4) {

            if (!clave.substring(0, 1).equals("0")) {
                //System.out.println(clave);
                if (clave.length() == 1) {
                    clave2 = ("000" + clave);
                }
                if (clave.length() == 2) {
                    clave2 = ("00" + clave);
                }
                if (clave.length() >= 3) {
                    clave2 = ("0" + clave);
                }

            }
        } else {
            clave2 = clave;
        }
        return clave2;
    }

}
