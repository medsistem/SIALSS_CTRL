package CatalogoISEM;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Proceso para leer archivo excel unidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class LeeExcelUnidades {

    private Vector vectorDataExcelXLSX = new Vector();
    ConectionDB con = new ConectionDB();

    public boolean obtieneArchivo(String path, String file) {
        String excelXLSXFileName = path + "/exceles/" + file;
        vectorDataExcelXLSX = readDataExcelXLSX(excelXLSXFileName);

        try {
            displayDataExcelXLSX(vectorDataExcelXLSX);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            actualizaUnidadesGNKL(vectorDataExcelXLSX);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void actualizaUnidadesGNKL(Vector vectorData) throws SQLException {
        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            con.conectar();
            try {
                int ClaGnkl = 1;
                for (int j = 0; j < vectorData.capacity(); j++) {
                    if (j >= 9) {
                        System.out.println(j);
                        String F_ClaGNkl = (vectorCellEachRowData.get(j).toString().replaceAll("\n", " ") + "");
                        if (!F_ClaGNkl.equals("") || !F_ClaGNkl.equals(" ")) {
                            try {
                                con.insertar("update tb_unidis set F_ClaInt" + ClaGnkl + "='" + F_ClaGNkl + "' where F_ClaUniIS = '" + (vectorCellEachRowData.get(0).toString().replaceAll("\n", " ") + "") + "' and F_JurUniIS = '" + (vectorCellEachRowData.get(4).toString().replaceAll("\n", " ") + "").trim() + "'  ");

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            if (ClaGnkl == 1) {
                                try {
                                    con.insertar("update tb_unidis set F_MedUniIS='" + F_ClaGNkl + "' where F_ClaUniIS = '" + (vectorCellEachRowData.get(0).toString().replaceAll("\n", " ") + "") + "' and F_JurUniIS = '" + (vectorCellEachRowData.get(4).toString().replaceAll("\n", " ") + "").trim() + "'  ");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            ClaGnkl++;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            con.cierraConexion();
        }
    }

    public void displayDataExcelXLSX(Vector vectorData) {
        // Looping every row data in vector

        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            // looping every cell in each row

            String F_ClaUniIS = (vectorCellEachRowData.get(0).toString().replaceAll("\n", " ") + "").trim();
            String F_DesUniIS = (vectorCellEachRowData.get(1).toString().replaceAll("\n", " ") + "").trim();
            String F_ClaLocIS = (vectorCellEachRowData.get(2).toString().replaceAll("\n", " ") + "").trim();
            String F_ClaMunIS = (vectorCellEachRowData.get(3).toString().replaceAll("\n", " ") + "").trim();
            String F_ClaJurIS = (vectorCellEachRowData.get(4).toString().replaceAll("\n", " ") + "").trim();
            String F_CveReg = (vectorCellEachRowData.get(5).toString().replaceAll("\n", " ") + "").trim();
            String F_CveNiv = (vectorCellEachRowData.get(6).toString().replaceAll("\n", " ") + "").trim();
            String F_CooUniIS = (vectorCellEachRowData.get(7).toString().replaceAll("\n", " ") + "").trim();
            String F_ClaSap = (vectorCellEachRowData.get(8).toString().replaceAll("\n", " ") + "").trim();

            try {
                con.conectar();
                try {
                    int banExistente = 0;
                    String F_Id = "";
                    ResultSet rset = con.consulta("select F_Id from tb_unidis where F_ClaUniIS = '" + F_ClaUniIS + "' and F_JurUniIS = '" + F_ClaJurIS + "'  ");
                    while (rset.next()) {
                        banExistente = 1;
                        F_Id = rset.getString("F_Id");
                    }
                    if (banExistente == 1) {
                        con.insertar("update tb_unidis set F_ClaUniIS = '" + F_ClaUniIS + "', F_DesUniIS = '" + F_DesUniIS + "', F_LocUniIS = '" + ((int) (Double.parseDouble(F_ClaLocIS))) + "', F_MunUniIS = '" + ((int) (Double.parseDouble(F_ClaMunIS))) + "', F_JurUniIS = '" + F_ClaJurIS + "', F_RegUniIS = '" + ((int) (Double.parseDouble((F_CveReg)))) + "', F_NivUniIS = '" + ((int) (Double.parseDouble(F_CveNiv))) + "', F_ClaLugEnt='" + F_ClaUniIS + "', F_CooUniIS='" + F_CooUniIS + "', F_ClaSap = '" + F_ClaSap + "' where F_Id = '" + F_Id + "' ");
                    } else {
                        con.insertar("insert into tb_unidis values ('" + F_ClaUniIS + "','" + F_DesUniIS + "','','','','','','','" + ((int) (Double.parseDouble(F_ClaLocIS))) + "','" + ((int) (Double.parseDouble(F_ClaMunIS))) + "','" + F_ClaJurIS + "','" + ((int) (Double.parseDouble(F_CveReg))) + "','" + ((int) (Double.parseDouble(F_CveNiv))) + "','','','','','','" + F_ClaUniIS + "','" + F_CooUniIS + "','" + F_ClaSap + "','0')");
                    }
                    //con.insertar(qry);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String fotmatoClaves(String clave) {
        String ClaPro = clave;
        DecimalFormat formatter = new DecimalFormat("0000.00");
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        custom.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(custom);
        ClaPro = formatter.format(Double.parseDouble(ClaPro));
        String[] punto = ClaPro.split("\\.");
        System.out.println(punto.length);
        if (punto.length > 1) {
            System.out.println(ClaPro + "***" + punto[0] + "////" + punto[1]);
            if (punto[1].equals("01")) {
                ClaPro = agrega(punto[0]) + ".01";
            } else if (punto[1].equals("02")) {
                ClaPro = agrega(punto[0]) + ".02";
            } else if (punto[1].equals("00")) {
                ClaPro = agrega(punto[0]);
            } else {
                ClaPro = agrega(punto[0]);
            }
            System.out.println(ClaPro);
        }
        return ClaPro;
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

    public String tipoMed(String F_ClaPro) {
        String tipoMed = "";
        if (Integer.parseInt(F_ClaPro) < 9999) {
            tipoMed = "2504";
        } else {
            tipoMed = "2505";
        }
        return tipoMed;
    }
}
