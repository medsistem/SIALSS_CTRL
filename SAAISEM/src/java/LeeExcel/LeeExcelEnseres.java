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
public class LeeExcelEnseres {

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
            con.actualizar("DELETE FROM tb_enserescargareq WHERE F_User='" + User + "'");
            System.out.println("Registros Eliminados");
            con.cierraConexion();
        } catch (Exception e) {

        }

        for (int i = 0; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String qry = "insert into tb_enserescargareq values (";
            String qryElimina = "";
            // looping every cell in each row

            try {
                con.conectar();
                for (int j = 0; j < 3; j++) {
                    if (j == 0) {
                        try {
                            String Unidad = (vectorCellEachRowData.get(j).toString() + "").trim();
                            qry = qry + "'" + Unidad + "', ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (j == 1) {
                        try {
                            String ClaEnseres = ((vectorCellEachRowData.get(j).toString()) + "");
                            qry = qry + "'" + ClaEnseres + "' , ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (j == 2) {
                        String Cantidad = ((vectorCellEachRowData.get(j).toString()) + "");
                        qry = qry + "'" + Cantidad + "' , ";
                    }
                }
                String Solicitado = ((vectorCellEachRowData.get(2).toString()) + "");
                qry = qry + "NOW(), '" + Solicitado + "','" + User + "',0,0,0)"; // agregar campos fuera del excel
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

            con.actualizar("UPDATE tb_enserescargareq O INNER JOIN tb_uniatn U ON O.F_ClaUni = U.F_ClaCli SET O.F_ValUnidad = 1 WHERE O.F_User = '" + User + "';");

            con.actualizar("UPDATE tb_enserescargareq O INNER JOIN tb_enseres E ON O.F_ClaPro = E.F_Id SET O.F_ValEnseres = 1 WHERE O.F_User = '" + User + "';");

            ResultSet Consulta = con.consulta("SELECT COUNT(*), SUM(F_ValUnidad), SUM(F_ValEnseres) FROM tb_enserescargareq O WHERE F_User = '" + User + "';");
            while (Consulta.next()) {
                int NoReg = Consulta.getInt(1);
                int ValUnidad = Consulta.getInt(2);
                int ValEnseres = Consulta.getInt(3);

                if ((NoReg == ValUnidad) && (NoReg == ValEnseres)) {

                    ResultSet Unidad = con.consulta("SELECT F_ClaUni FROM tb_enserescargareq WHERE F_User = '" + User + "' GROUP BY F_ClaUni;");
                    while (Unidad.next()) {
                        con.actualizar("UPDATE tb_enseresunireq SET F_Status='1' WHERE F_ClaUni='" + Unidad.getString(1) + "' and F_Status='0';");

                        Claves = con.consulta("SELECT F_ClaUni, F_ClaPro, SUM(F_PiezasReq) AS F_PiezasReq, SUM(F_Solicitado) AS F_Solicitado FROM tb_enserescargareq C WHERE F_User = '" + User + "' AND F_ClaUni = '" + Unidad.getString(1) + "' GROUP BY F_ClaUni, F_ClaPro;");

                        while (Claves.next()) {
                            con.insertar("INSERT INTO tb_enseresunireq VALUES('" + Claves.getString(1) + "','" + Claves.getString(2) + "','" + Claves.getString(3) + "',NOW(),0,'" + Claves.getString(4) + "','" + User + "','',0);");
                        }
                    }
                }
            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
