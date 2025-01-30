/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LeeExcel;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Procesa archivo excel para carga de requerimiento unidades rurales
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaExcelReqRural {

    private Vector vectorDataExcelXLSX = new Vector();
    ConectionDB con = new ConectionDB();
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

    public boolean obtieneArchivo(String path, String file) {
        String excelXLSXFileName = path + "/exceles/" + file;
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
        String F_ClaCli = "", F_FecApl = "";
        int idFact = 0;
        try {
            con.conectar();
            for (int i = 0; i < vectorData.size(); i++) {
                Vector vectorCellEachRowData = (Vector) vectorData.get(i);
                String qry = "insert into tb_unireqrurales values (";
                // && !F_FecApl.equals(vectorCellEachRowData.get(1).toString())
                if (!F_ClaCli.equals(vectorCellEachRowData.get(0).toString())) {
                    try {
                        idFact = dameIdReqRural();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (F_ClaCli.equals(vectorCellEachRowData.get(0).toString()) && !F_FecApl.equals(vectorCellEachRowData.get(1).toString())) {
                    try {
                        idFact = dameIdReqRural();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                F_ClaCli = vectorCellEachRowData.get(0).toString();
                F_FecApl = vectorCellEachRowData.get(1).toString();
                // looping every cell in each row
                for (int j = 0; j < 7; j++) {
                    if (j == 1) {
                        try {

                            String Clave = (vectorCellEachRowData.get(j).toString());
                            qry = qry + "'" + df2.format(df.parse(Clave)) + "' , ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (j == 2) {
                        try {
                            String ClaPro = ((vectorCellEachRowData.get(j).toString()) + "");
                            NumberFormat formatter = new DecimalFormat("0000.00");
                            ClaPro = formatter.format(Double.parseDouble(ClaPro));
                            String[] punto = ClaPro.split("\\.");
                            if (punto.length > 1) {
                                if (punto[1].equals("01")) {
                                    ClaPro = agrega(punto[0]) + ".01";
                                } else if (punto[1].equals("02")) {
                                    ClaPro = agrega(punto[0]) + ".02";
                                } else if (punto[1].equals("00")) {
                                    ClaPro = agrega(punto[0]);
                                } else {
                                    ClaPro = agrega(punto[0]);
                                }
                            }
                            qry = qry + "'" + agrega(ClaPro) + "' , ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        try {
                            String Clave = ((vectorCellEachRowData.get(j).toString()) + "");
                            qry = qry + "'" + Clave + "' , ";
                        } catch (Exception e) {
                        }
                    }
                }
                String Solicitado = ((vectorCellEachRowData.get(3).toString()) + "");
                qry = qry + "curdate(),'" + idFact + "', 0, 0,'" + Solicitado + "')"; // agregar campos fuera del excel
                //System.out.println(qry);
                con.insertar(qry);
                

            }
            con.cierraConexion();
        } catch (Exception e) {
            e.printStackTrace();
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

    public int dameIdReqRural() throws SQLException {
        int idRural = 0;
        con.conectar();
        ResultSet rset = con.consulta("select F_IndReqRural from tb_indice");
        while (rset.next()) {
            idRural = rset.getInt(1);
        }
        con.insertar("update tb_indice set F_IndReqRural = '" + (idRural + 1) + "'");
        con.cierraConexion();
        return idRural;
    }
}
