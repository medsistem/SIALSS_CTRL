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
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Anibal MEDALFA
 */
public class LeeExcelRecibo {

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

    @SuppressWarnings("empty-statement")
    public void displayDataExcelXLSX(Vector vectorData, String User) {
        // Looping every row data in vector
        DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy");

        ConectionDB con = new ConectionDB();
        try {
            con.conectar();
            con.actualizar("DELETE FROM tb_cargaoc WHERE F_Usu='" + User + "'");
            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger("LeeExcelRecibo").log(Level.SEVERE, null, e);
        }

        for (Object vectorData1 : vectorData) {
            Vector vectorCellEachRowData = (Vector) vectorData1;
            String qry = "INSERT INTO tb_cargaoc VALUES (";
            // looping every cell in each row
            try {

                if (vectorCellEachRowData.size() < 6) {
                    break;
                }
                con.conectar();
                for (int j = 0; j < 4; j++) {

                    switch (j) {
                        case 0:
                            try {
                            String NoOrden = (vectorCellEachRowData.get(j).toString().trim() + "");

                            NoOrden = NoOrden.replaceAll("^\\s*", "");
                            NoOrden = NoOrden.replaceAll(" ", "");
                            NoOrden = NoOrden.replaceAll("&nbsp;", "");

                            qry = qry + "'" + NoOrden + "', ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                        case 1:
                            try {
                            String Cliente = (vectorCellEachRowData.get(j).toString() + "");

                            Cliente = Cliente.replaceAll("^\\s*", "");
                            Cliente = Cliente.replaceAll(" ", "");
                            Cliente = Cliente.replaceAll("&nbsp;", "");

                            qry = qry + "'" + Cliente + "', ";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                        case 2:
                            try {
                            String Nombre = (vectorCellEachRowData.get(j).toString() + "").trim();
                            qry = qry + "'" + Nombre + "',";
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                        case 3:
                            try {
                            String ClaPro = ((vectorCellEachRowData.get(j).toString()) + "");
                            if (ClaPro.endsWith(".0")) {
                                ClaPro = ClaPro.replace(".0", "");
                            }
                                System.out.println("clave " + ClaPro);
                                ResultSet rsetcon = con.consulta("SELECT F_ClaPro, F_ClaProSS FROM tb_medica WHERE F_ClaPro = '" + ClaPro + "'");
                                while(rsetcon.next()){
                                    String claProSs = rsetcon.getString(2);
                            qry = qry + "'" + ClaPro + "', '" + claProSs + "', ";
                                }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                        

                        default:
                            break;
                    }
                }
                String Solicitado = "", Proyecto = "";
                    Solicitado = ((vectorCellEachRowData.get(4).toString()) + "");
                    if (Solicitado.equals("Cantidad")) {
                        Solicitado = "0";
                    }

                    Proyecto = ((vectorCellEachRowData.get(5).toString()) + "");
                    if (Proyecto.equals("proyecto")) {
                        Proyecto = "0";
                    }
                    Proyecto = Proyecto.replace(".0", "");

                    qry = qry + "'" + Solicitado + "','" + User + "','" + Proyecto + "',0,0,0,0)"; // agregar campos fuera del excel
                    System.out.println(qry);
                    con.insertar(qry);
                    con.cierraConexion();
                }catch (Exception e) {
                //System.out.println(qry);
                Logger.getLogger("LeeExcelRecibo").log(Level.SEVERE, null, e);
            }
            }

            try {
                con.conectar();

                con.actualizar("UPDATE tb_cargaoc O INNER JOIN tb_proveedor P ON O.F_Proveedor = P.F_ClaProve SET O.F_ProblemaProvee=1 WHERE O.F_UsU='" + User + "';");

                con.actualizar("UPDATE tb_cargaoc O INNER JOIN tb_medica M ON O.F_Clave = M.F_ClaPro AND O.F_Clavess = M.F_ClaProSS SET O.F_ProblemaClave=1 WHERE O.F_Usu='" + User + "';");
                con.actualizar("UPDATE tb_cargaoc O INNER JOIN tb_proyectos P ON O.F_Proyecto = P.F_Id SET O.F_ProblemaProyecto = 1 WHERE O.F_Usu='" + User + "';");

                ResultSet Consulta = con.consulta("SELECT F_NoOc, F_Proveedor, P.F_ClaProve, COUNT(*), SUM(F_ProblemaProvee), SUM(F_ProblemaClave),SUM(O.F_ProblemaProyecto),O.F_Proyecto FROM tb_cargaoc O INNER JOIN tb_proveedor P ON O.F_Proveedor = P.F_ClaProve INNER JOIN tb_proyectos PR ON O.F_Proyecto=PR.F_Id WHERE F_Usu = '" + User + "' GROUP BY F_NoOc, F_Proveedor,O.F_Proyecto;");
                while (Consulta.next()) {
                    int NoReg = Consulta.getInt(4);
                    int NoProvee = Consulta.getInt(5);
                    int NoClave = Consulta.getInt(6);
                    int NoProyecto = Consulta.getInt(7);

                    if ((NoReg == NoClave) && (NoReg == NoProvee) && (NoReg == NoProyecto)) {
                        con.actualizar("DELETE FROM tb_pedido_sialss WHERE F_NoCompra='" + Consulta.getString(1) + "' AND F_Provee='" + Consulta.getString(3) + "' AND F_Proyecto='" + Consulta.getString(8) + "';");

                        ResultSet Claves = con.consulta("SELECT F_NoOc, P.F_ClaProve, F_Clave, F_Clavess, SUM(F_Cant) AS F_Cant,F_Proyecto FROM tb_cargaoc O INNER JOIN tb_proveedor P ON O.F_Proveedor = P.F_ClaProve INNER JOIN tb_proyectos PR ON O.F_Proyecto=PR.F_Id WHERE F_Usu = '" + User + "' AND F_NoOc ='" + Consulta.getString(1) + "' AND P.F_ClaProve = '" + Consulta.getString(3) + "' AND F_Proyecto='" + Consulta.getString(8) + "' GROUP BY F_NoOc, P.F_ClaProve, F_Clave, F_Clavess,F_Proyecto;");
                        while (Claves.next()) {
                            con.insertar("INSERT INTO tb_pedido_sialss VALUES(0,'" + Claves.getString(1) + "','" + Claves.getString(2) + "','" + Claves.getString(3) + "','" + Claves.getString(4) + "','-','-','-',CURDATE(),'" + Claves.getString(5) + "','-',NOW(),CURDATE(),CURTIME(),'" + User + "',1,0,'" + Claves.getString(6) + "',DATE_ADD(CURDATE(),INTERVAL 30 DAY),0,0,'',0,0,0,0)");
                        }
                        con.actualizar("DELETE FROM tb_cargaoc WHERE F_Usu='" + User + "';");
                    } else {
                        System.out.println("error : revisar errores de carga");
                    }
                }

                con.cierraConexion();
            } catch (Exception e) {
                Logger.getLogger("LeeExcelRecibo").log(Level.SEVERE, null, e);
            }

        }
    }
