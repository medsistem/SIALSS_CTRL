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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Procesa archivo excel para carga de inventario
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaExcelInvIni {

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
            con.actualizar("DELETE FROM tb_cargainventario WHERE F_User='" + User + "'");
            System.out.println("Registros Eliminados");
            con.cierraConexion();
        } catch (Exception e) {

        }

        for (int i = 0; i < vectorData.size(); i++) {
            Vector vectorCellEachRowData = (Vector) vectorData.get(i);
            String qry = "insert into tb_cargainventario values (";
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
                    if (!(ClaPro.equals("5.0"))) {
                        ClaPro = formatter.format(Double.parseDouble(ClaPro));
                    }
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
                for (int j = 0; j < 1; j++) {

                    if (j == 0) {
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
                            if (!(ClaPro.equals("5.0"))) {
                                ClaPro = formatter.format(Double.parseDouble(ClaPro));
                            }
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

                String F_Fecha = "", Solicitado = "", Lote = "", Ubica = "", Clave = "";
                //Clave = ((vectorCellEachRowData.get(0).toString()) + "");
                Lote = ((vectorCellEachRowData.get(1).toString()) + "");
                try {
                    F_Fecha = (vectorCellEachRowData.get(2).toString());
                    F_Fecha = df2.format(df1.parse(F_Fecha));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    try {
                        F_Fecha = df2.format(df3.parse(F_Fecha));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                Ubica = ((vectorCellEachRowData.get(3).toString()) + "");
                Solicitado = ((vectorCellEachRowData.get(4).toString()) + "");
                //qry = qry + "'" + Clave + "','" + Lote + "','" + F_Fecha + "','" + Ubica + "','" + Solicitado + "','" + User + "','0','0',0)"; // agregar campos fuera del excel
                qry = qry + "'" + Lote + "','" + F_Fecha + "','" + Ubica + "','" + Solicitado + "','" + User + "','0','0',0)"; // agregar campos fuera del excel
                con.insertar(qry);
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        try {
            con.conectar();
            int ContarCla = 0, ContarUbi = 0, SumaUbi = 0, SumaCla = 0, Total = 0;
            ResultSet DatosCla = con.consulta("SELECT F_ClaPro FROM tb_cargainventario GROUP BY F_ClaPro ORDER BY F_ClaPro+0;");
            while (DatosCla.next()) {
                ResultSet ValidaCla = con.consulta("SELECT COUNT(F_ClaPro) FROM tb_medica WHERE F_ClaPro='" + DatosCla.getString(1) + "';");
                if (ValidaCla.next()) {
                    ContarCla = ValidaCla.getInt(1);
                }
                if (ContarCla == 0) {
                    con.actualizar("UPDATE tb_cargainventario SET F_ValCla='1' WHERE F_ClaPro='" + DatosCla.getString(1) + "';");
                }
                ContarCla = 0;
            }

            ResultSet DatosUbi = con.consulta("SELECT F_Ubica FROM tb_cargainventario GROUP BY F_Ubica ORDER BY F_Ubica ASC;");
            while (DatosUbi.next()) {
                ResultSet ValidaUbi = con.consulta("SELECT COUNT(F_ClaUbi) FROM tb_ubica WHERE F_ClaUbi='" + DatosUbi.getString(1) + "';");
                if (ValidaUbi.next()) {
                    ContarUbi = ValidaUbi.getInt(1);
                }
                if (ContarUbi == 0) {
                    con.actualizar("UPDATE tb_cargainventario SET F_ValUbi='1' WHERE F_Ubica='" + DatosUbi.getString(1) + "';");
                }
                ContarUbi = 0;
            }

            ResultSet ConsultaUbi = con.consulta("SELECT SUM(F_ValCla),SUM(F_ValUbi)FROM tb_cargainventario;");
            if (ConsultaUbi.next()) {
                SumaCla = ConsultaUbi.getInt(1);
                SumaUbi = ConsultaUbi.getInt(2);
            }

            Total = SumaCla + SumaUbi;

            if (Total == 0) {
                String F_ClaPro = "", F_ClaLot = "", F_FecCad = "", F_Ubica = "";
                String F_ClaOrg = "", F_FecFab = "", F_Cb = "", F_ClaMar = "", F_Origen = "", F_UniMed = "";
                int F_Cantidad = 0, F_FolLot = 0, F_TipMed = 0, Documento = 0;

                ResultSet Consulta = con.consulta("SELECT F_IndFolMov FROM tb_indice;");
                if (Consulta.next()) {
                    Documento = Consulta.getInt(1);
                }

                con.actualizar("UPDATE tb_indice SET F_IndFolMov='" + Documento + "'+1;");

                ResultSet Datos = con.consulta("SELECT F_ClaPro,F_ClaLot,F_FecCad,F_Ubica,SUM(F_Cantidad) FROM tb_cargainventario GROUP BY F_ClaPro,F_ClaLot,F_FecCad,F_Ubica ORDER BY F_ClaPro+0, F_ClaLot ASC ,F_FecCad ASC;");
                while (Datos.next()) {
                    F_ClaPro = Datos.getString(1);
                    F_ClaLot = Datos.getString(2);
                    F_FecCad = Datos.getString(3);
                    F_Ubica = Datos.getString(4);
                    F_Cantidad = Datos.getInt(5);
                    F_FolLot = 0;
                    ResultSet ConsulUbi = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' AND F_ClaLot='" + F_ClaLot + "' AND F_FecCad='" + F_FecCad + "' AND F_Ubica='" + F_Ubica + "' LIMIT 1;");
                    if (ConsulUbi.next()) {
                        F_FolLot = ConsulUbi.getInt(1);
                    }
                    if (F_FolLot > 0) {
                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + F_Cantidad + "' WHERE F_ClaPro='" + F_ClaPro + "' AND F_ClaLot='" + F_ClaLot + "' AND F_FecCad='" + F_FecCad + "' AND F_Ubica='" + F_Ubica + "' LIMIT 1;");
                        con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + Documento + "','11','" + F_ClaPro + "','" + F_Cantidad + "','0.00','0.00','1','" + F_FolLot + "','" + F_Ubica + "','3000',CURTIME(),'" + User + "','');");

                        F_FolLot = 0;
                        F_ClaOrg = "";
                        F_FecFab = "";
                        F_Cb = "";
                        F_ClaMar = "";
                        F_Origen = "";
                        F_UniMed = "";
                        F_ClaPro = "";
                        F_ClaLot = "";
                        F_FecCad = "";
                        F_Ubica = "";
                        F_Cantidad = 0;
                        F_TipMed = 0;
                    } else {
                        ConsulUbi = con.consulta("SELECT F_FolLot,F_ClaOrg,F_FecFab,F_Cb,F_ClaMar,F_Origen,F_UniMed FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' AND F_ClaLot='" + F_ClaLot + "' AND F_FecCad='" + F_FecCad + "' LIMIT 1;");
                        if (ConsulUbi.next()) {
                            F_FolLot = ConsulUbi.getInt(1);
                            F_ClaOrg = ConsulUbi.getString(2);
                            F_FecFab = ConsulUbi.getString(3);
                            F_Cb = ConsulUbi.getString(4);
                            F_ClaMar = ConsulUbi.getString(5);
                            F_Origen = ConsulUbi.getString(6);
                            F_UniMed = ConsulUbi.getString(7);
                        }
                        if (F_FolLot > 0) {
                            con.insertar("INSERT INTO tb_lote VALUES(0,'" + F_ClaPro + "','" + F_ClaLot + "','" + F_FecCad + "','" + F_Cantidad + "','" + F_FolLot + "','" + F_ClaOrg + "','" + F_Ubica + "','" + F_FecFab + "','" + F_Cb + "','" + F_ClaMar + "','" + F_Origen + "','" + F_ClaOrg + "','" + F_UniMed + "');");
                            con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + Documento + "','11','" + F_ClaPro + "','" + F_Cantidad + "','0.00','0.00','1','" + F_FolLot + "','" + F_Ubica + "','3000',CURTIME(),'" + User + "','');");
                            F_FolLot = 0;
                            F_ClaOrg = "";
                            F_FecFab = "";
                            F_Cb = "";
                            F_ClaMar = "";
                            F_Origen = "";
                            F_UniMed = "";
                            F_ClaPro = "";
                            F_ClaLot = "";
                            F_FecCad = "";
                            F_Ubica = "";
                            F_Cantidad = 0;
                            F_TipMed = 0;
                        } else {
                            int FolioF = 0, F_Origen1 = 0;
                            ResultSet ConsulFol = con.consulta("SELECT F_IndLote FROM tb_indice;");
                            if (ConsulFol.next()) {
                                F_FolLot = ConsulFol.getInt(1);
                            }
                            FolioF = F_FolLot + 1;
                            con.actualizar("UPDATE tb_indice SET F_IndLote='" + FolioF + "';");
                            Calendar c1 = GregorianCalendar.getInstance();
                            c1.setTime(df2.parse(F_FecCad));
                            ResultSet ClaMed = con.consulta("SELECT F_TipMed,F_Origen FROM tb_medica WHERE F_ClaPro='" + F_ClaPro + "';");
                            if (ClaMed.next()) {
                                F_TipMed = ClaMed.getInt(1);
                                F_Origen1 = ClaMed.getInt(2);
                            }
                            if (F_TipMed == 2504) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                            F_FecFab = (df2.format(c1.getTime()));
                            con.insertar("INSERT INTO tb_lote VALUES(0,'" + F_ClaPro + "','" + F_ClaLot + "','" + F_FecCad + "','" + F_Cantidad + "','" + F_FolLot + "','3000','" + F_Ubica + "','" + F_FecFab + "','0','329','" + F_Origen1 + "','3000','131');");
                            con.insertar("INSERT INTO tb_movinv VALUES(0,CURDATE(),'" + Documento + "','11','" + F_ClaPro + "','" + F_Cantidad + "','0.00','0.00','1','" + F_FolLot + "','" + F_Ubica + "','3000',CURTIME(),'" + User + "','');");
                            F_FolLot = 0;
                            F_Origen1 = 0;
                            F_ClaOrg = "";
                            F_FecFab = "";
                            F_Cb = "";
                            F_ClaMar = "";
                            F_Origen = "";
                            F_UniMed = "";
                            F_ClaPro = "";
                            F_ClaLot = "";
                            F_FecCad = "";
                            F_Ubica = "";
                            F_Cantidad = 0;
                            F_TipMed = 0;
                        }
                    }
                }
            }

            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*
        try {
            con.conectar();
            ResultSet Unidad = con.consulta("SELECT F_ClaUni FROM tb_cargareq WHERE F_User='" + User + "' GROUP BY F_ClaUni");
            while (Unidad.next()) {
                System.out.println("Eliminó");
                con.actualizar("UPDATE tb_unireq SET F_Status='1' WHERE F_ClaUni='" + Unidad.getString(1) + "' and F_Status='0'");
                ResultSet Claves = con.consulta("SELECT F_ClaUni,F_ClaPro,F_CajasReq,SUM(F_PiezasReq) AS F_PiezasReq,F_FecCarg,F_Status,F_Fecha,SUM(F_Solicitado) AS F_Solicitado "
                        + "FROM tb_cargareq WHERE F_User='" + User + "' AND F_ClaUni='" + Unidad.getString(1) + "' "
                        + "GROUP BY F_ClaUni,F_ClaPro,F_FecCarg,F_Status,F_Fecha");
                while (Claves.next()) {
                    System.out.println("Ingreso");
                    con.insertar("INSERT INTO tb_unireq VALUES('" + Claves.getString(1) + "','" + Claves.getString(2) + "','" + Claves.getString(3) + "','" + Claves.getString(4) + "','" + Claves.getString(5) + "',0,'" + Claves.getString(6) + "','" + Claves.getString(7) + "','" + Claves.getString(8) + "')");
                }
            }
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
         */

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
