package CatalogoISEM;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Proceso para leer archivo excel medicamentos
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class LeeExcelInsumo {

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
            actualizaClaveGNKL(vectorDataExcelXLSX);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            actualizaClaveSAP(vectorDataExcelXLSX);
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

    public void actualizaClaveSAP(Vector vectorData) {
        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String F_ClaArtIS = ((int) Double.parseDouble(vectorCellEachRowData.get(0).toString()) + "");
            String F_ClaSap = (vectorCellEachRowData.get(9).toString().replaceAll("\n", " ") + "");
            try {
                con.conectar();
                if (!F_ClaSap.equals("")) {
                    con.insertar("update tb_artiis set F_ClaDyn = '" + F_ClaSap + "' where F_ClaArtIS = '" + F_ClaArtIS + "' ");
                    con.insertar("update tb_medica set F_ClaSap = '" + F_ClaSap + "' where F_ClaPro = '" + fotmatoClaves(F_ClaArtIS) + "' ");
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void actualizaClaveGNKL(Vector vectorData) {
        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String F_ClaArtIS = ((int) Double.parseDouble(vectorCellEachRowData.get(0).toString()) + "");
            String F_DesPro = (vectorCellEachRowData.get(11).toString().replaceAll("\n", " ") + "");
            String F_PreArtIS = (vectorCellEachRowData.get(2).toString().replaceAll("\n", " ") + "");
            String F_SUMArtIS = (vectorCellEachRowData.get(3).toString().replaceAll("\n", " ") + "");
            String F_PAArtIS = (vectorCellEachRowData.get(4).toString().replaceAll("\n", " ") + "");
            String F_SPArtIS = (vectorCellEachRowData.get(5).toString().replaceAll("\n", " ") + "");
            String F_CauArtIS = (vectorCellEachRowData.get(6).toString().replaceAll("\n", " ") + "");
            String F_PreVenIS = (vectorCellEachRowData.get(7).toString().replaceAll("\n", " ") + "");
            String F_CPPArtIS = (vectorCellEachRowData.get(8).toString().replaceAll("\n", " ") + "");
            String F_ClaSap = (vectorCellEachRowData.get(9).toString().replaceAll("\n", " ") + "");
            try {
                con.conectar();
                ResultSet rset = con.consulta("select F_ClaInt from tb_artiis where F_ClaArtIS = '" + F_ClaArtIS + "'");
                while (rset.next()) {
                    int banClave = 0;
                    ResultSet rset2 = con.consulta("select F_ClaPro from tb_medica where F_ClaPro = '" + rset.getString("F_ClaInt") + "'");
                    while (rset2.next()) {
                        banClave = 1;
                    }
                    if (banClave == 1) {
                        con.insertar("update tb_medica set F_DesPro='" + F_DesPro + "', F_StsPro='A', F_TipMed='" + tipoMed(rset.getString("F_ClaInt")) + "', F_Costo = '" + F_PreVenIS + "', F_PrePro = '" + F_PreArtIS + "', F_ClaSap = '" + F_ClaSap + "' where F_ClaPro = '" + rset.getString("F_ClaInt") + "' ");
                    } else {
                        con.insertar("insert into tb_medica values ('" + rset.getString("F_ClaInt") + "','" + F_DesPro + "','A','" + tipoMed(rset.getString("F_ClaInt")) + "','" + F_PreVenIS + "','" + F_PreArtIS + "','" + ((int) (Double.parseDouble(F_SUMArtIS))) + "','" + F_ClaSap + "')");
                    }
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void displayDataExcelXLSX(Vector vectorData) {
        // Looping every row data in vector

        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            // looping every cell in each row

            String F_ClaArtIS = ((int) Double.parseDouble(vectorCellEachRowData.get(0).toString()) + "");
            String F_DesArtIS = (vectorCellEachRowData.get(1).toString().replaceAll("\n", " ") + "");
            String F_PreArtIS = (vectorCellEachRowData.get(2).toString().replaceAll("\n", " ") + "");
            String F_SUMArtIS = (vectorCellEachRowData.get(3).toString().replaceAll("\n", " ") + "");
            String F_PAArtIS = (vectorCellEachRowData.get(4).toString().replaceAll("\n", " ") + "");
            String F_SPArtIS = (vectorCellEachRowData.get(5).toString().replaceAll("\n", " ") + "");
            String F_CauArtIS = (vectorCellEachRowData.get(6).toString().replaceAll("\n", " ") + "");
            String F_PreVenIS = (vectorCellEachRowData.get(7).toString().replaceAll("\n", " ") + "");
            String F_CPPArtIS = (vectorCellEachRowData.get(8).toString().replaceAll("\n", " ") + "");

            try {
                con.conectar();
                try {
                    int banExistente = 0;
                    ResultSet rset = con.consulta("select F_ClaArtIS from tb_artiis where F_ClaArtIS = '" + F_ClaArtIS + "' ");
                    while (rset.next()) {
                        banExistente = 1;
                    }
                    if (banExistente == 1) {
                        con.insertar("update tb_artiis set F_DesArtIS = '" + F_DesArtIS + "', F_PreArtIS = '" + F_PreArtIS + "', F_SUMArtIS = '" + ((int) (Double.parseDouble(F_SUMArtIS)) + "") + "', F_PAArtIS = '" + ((int) (Double.parseDouble(F_PAArtIS))) + "', F_SPArtIS = '" + ((int) (Double.parseDouble(F_SPArtIS))) + "', F_CauArtIS = '" + ((int) (Double.parseDouble(F_CauArtIS))) + "', F_PreVenIS = '" + F_PreVenIS + "', F_CPPArtIS = '" + ((int) (Double.parseDouble(F_CPPArtIS))) + "' where F_ClaArtIS = '" + F_ClaArtIS + "' ");
                    } else {
                        con.insertar("insert into tb_artiis values ('" + F_ClaArtIS + "','" + F_DesArtIS + "','','" + F_PreVenIS + "','" + ((int) (Double.parseDouble(F_CPPArtIS))) + "','" + F_PreArtIS + "','" + ((int) (Double.parseDouble(F_SUMArtIS))) + "','" + ((int) (Double.parseDouble(F_PAArtIS))) + "','" + ((int) (Double.parseDouble(F_SPArtIS))) + "','" + ((int) (Double.parseDouble(F_CauArtIS))) + "','')");
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
