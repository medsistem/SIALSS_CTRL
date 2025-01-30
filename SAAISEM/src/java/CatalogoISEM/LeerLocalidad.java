/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CatalogoISEM;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Proceso para leer archivo excel localidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class LeerLocalidad {

    private Vector vectorDataExcelXLSX = new Vector();
    ConectionDB con = new ConectionDB();

    public boolean obtieneArchivo(String path, String file) {
        String excelXLSXFileName = path + "/CatalogoIsemArch/" + file;
        vectorDataExcelXLSX = readDataExcelXLSX(excelXLSXFileName);
        displayDataExcelXLSX(vectorDataExcelXLSX);
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

    public void displayDataExcelXLSX(Vector vectorData) {
        // Looping every row data in vector

        try {
            con.conectar();
            try {
                String qry2 = "delete from tb_locaistemp";
                con.insertar(qry2);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            con.cierraConexion();
        } catch (Exception e) {
        }

        for (int i = 0; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String qry = "insert into tb_locaistemp values (";
            // looping every cell in each row
            for (int j = 0; j < 4; j++) {

                if (j == 0) {
                    try {
                        String folio = (vectorCellEachRowData.get(j).toString() + "");
                        qry = qry + "'" + folio + "' , ";
                    } catch (Exception e) {
                    }
                } else if (j == 1) {
                    try {

                        String punto = (vectorCellEachRowData.get(j).toString() + "");
                        qry = qry + "'" + punto + "' , ";

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        String factura = (vectorCellEachRowData.get(j).toString() + "");
                        qry = qry + "'" + factura + "' ) ";
                    } catch (Exception e) {
                    }
                }
            }

            try {
                con.conectar();
                try {
                    con.insertar(qry);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
            } catch (Exception e) {
            }
        }
    }

}
