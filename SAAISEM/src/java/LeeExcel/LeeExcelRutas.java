package LeeExcel;

import conn.ConectionDB;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Procesa archivo excel para creación de rutas
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class LeeExcelRutas {

    private Vector vectorDataExcelXLSX = new Vector();
    ConectionDB con = new ConectionDB();

    public String obtieneArchivo(String path, String file, String usua) {
        String excelXLSXFileName = path + "/exceles/" + file;
        vectorDataExcelXLSX = readDataExcelXLSX(excelXLSXFileName);
        String mensaje = displayDataExcelXLSX(vectorDataExcelXLSX, usua);
        return mensaje;
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

    public String displayDataExcelXLSX(Vector vectorData, String usua) {
        String mensaje = "Se cargó correctamente";
        // Looping every row data in vector

        DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            if (!vectorCellEachRowData.get(0).toString().equals("")) {
                try {
                    con.conectar();
                    int banExiste = 0;
                    String F_Id = "";
                    String F_ClaUni = "";
                    F_ClaUni = (vectorCellEachRowData.get(2).toString() + "").trim();

                    String F_Fecha = "";
                    F_Fecha = df2.format((df1.parse((vectorCellEachRowData.get(6).toString() + "").trim())));

                    String F_Ruta = "";
                    F_Ruta = (vectorCellEachRowData.get(4).toString() + "").trim();

                    String F_LocPlano = "";
                    F_LocPlano = (vectorCellEachRowData.get(5).toString() + "").trim();

                    ResultSet rset = con.consulta("select F_Id from tb_fecharuta where F_ClaUni = '" + F_ClaUni + "' and YEAR(F_Fecha) = YEAR('" + F_Fecha + "') and MONTH(F_Fecha) = MONTH('" + F_Fecha + "') ");
                    while (rset.next()) {
                        F_Id = rset.getString("F_Id");
                        banExiste = 1;
                    }
                    String qry = "";
                    if (banExiste == 0) {
                        qry = "insert into tb_fecharuta values ('" + F_ClaUni + "','" + F_Fecha + "','" + F_Ruta + "','" + F_LocPlano + "','0')";
                    } else {
                        qry = "update tb_fecharuta set F_Ruta = '" + F_Ruta + "', F_LocPlano = '" + F_LocPlano + "', F_Fecha='" + F_Fecha + "' where F_Id = '" + F_Id + "'";
                    }

                    try {
                        con.insertar(qry);
                    } catch (Exception e) {
                        mensaje = e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    mensaje = e.getMessage();
                    System.out.println(e.getMessage());
                }
            }
        }
        return mensaje;
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
