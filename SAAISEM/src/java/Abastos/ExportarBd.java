/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abastos;

import com.healthmarketscience.jackcess.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.lang.*;
import conn.ConectionDB;

/**
 * Proceso para generar abasto .SGW para los CEAPS
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ExportarBd {

    public String Exportar(String id_usu, String Folio) throws IOException, SQLException {
        String mensaje = "Se Genero Correctamente";
        String databaseName = "C:\\ABASTOSGW\\" + Folio + ".SGW";
        Database database = createDatabase(databaseName);
        String tableName = "TB_Factura"; // Creating table
        Table table = createTable(tableName)
                .addColumn(new ColumnBuilder("F_TipDoc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ClaDoc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_StsFac").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Cliente").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Ruta").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecApl").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Consig").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Produc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_UniVen").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_NumAlm").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Unidad").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_UniPaq").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaBru").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Descto").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaNet").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Costo").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Utilid").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Impto").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaTot").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Precio").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Lote").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DirRef").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecEnt").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_User").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ObsPar").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_ObsFac").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DocAnt").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_AM").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_UnidadReq").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DesPro").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ClaLot").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecCad").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_CosLot").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Obs").setSQLType(Types.VARCHAR).toColumn())
                .toTable(database);
        try {
            ConectionDB Conn = new ConectionDB();
            Conn.conectar();
            String F_ClaUni = "";
            int F_Unidad = 0;

            ResultSet Datos = Conn.consulta("SELECT T.F_ClaDoc,T.F_FecApl,T.F_ClaPro,T.F_CantSur,T.F_Costo,T.F_Lote,A.F_DesPro,L.F_ClaLot,L.F_FecCad,T.F_Monto,T.F_ClaCli "
                    + "FROM tb_transferencias T INNER JOIN tb_medica A ON T.F_ClaPro=A.F_ClaPro INNER JOIN tb_lote L ON T.F_Lote=L.F_FolLot AND T.F_Ubicacion=L.F_Ubica "
                    + "WHERE T.F_ClaDoc='" + Folio + "' ORDER BY T.F_ClaPro ASC");
            while (Datos.next()) {
                F_Unidad = Datos.getInt("T.F_ClaCli");
                if (F_Unidad == 75) {
                    F_ClaUni = "2200A";
                } else if (F_Unidad == 76) {
                    F_ClaUni = "8201A";
                } else if (F_Unidad == 77) {
                    F_ClaUni = "8200A";
                } else if (F_Unidad == 78) {
                    F_ClaUni = "4100A";
                } else if (F_Unidad == 79) {
                    F_ClaUni = "4101A";
                } else if (F_Unidad == 80) {
                    F_ClaUni = "4102A";
                } else if (F_Unidad == 81) {
                    F_ClaUni = "4103A";
                } else if (F_Unidad == 82) {
                    F_ClaUni = "7094A";
                }

                table.addRow("F", Datos.getString(1), "A", F_ClaUni, "", Datos.getString(2), "", Datos.getString(3), "0", "1", Datos.getString(4), Datos.getString(4), "0", "0", "0", Datos.getString(5), (Datos.getInt(5) * -1), "0", "0", "0", Datos.getString(6), "R", Datos.getString(2), "SGWARE", "0", "0", "", "A", Datos.getString(4), Datos.getString(7), Datos.getString(8), Datos.getString(9), Datos.getString(5), "");//Inserting values into the table
            }
            Conn.cierraConexion();
        } catch (Exception e) {

        }
        String tableName2 = "TB_Proceso"; // Creating table2
        Table table2 = createTable(tableName2)
                .addColumn(new ColumnBuilder("F_NomPro").setSQLType(Types.VARCHAR).toColumn())
                .toTable(database);
        table2.addRow("Abasto");//Inserting values into the table2

        return mensaje;
    }

    private static Database createDatabase(String databaseName) throws IOException {
        return Database.create(new File(databaseName));
    }

    private static TableBuilder createTable(String tableName) {
        return new TableBuilder(tableName);
    }

    public static void addColumn(Database database, TableBuilder tableName, String columnName, Types sqlType) throws SQLException, IOException {
        tableName.addColumn(new ColumnBuilder(columnName).setSQLType(Types.INTEGER).toColumn()).toTable(database);
    }

    public static void startDatabaseProcess() throws IOException, SQLException {
        //String databaseName = "C:\\ABASTOSGW\\employeedb.mdb"; // Creating an MS Access database
        //Database database = createDatabase(databaseName);

        /*String tableName = "TB_Factura"; // Creating table
        Table table = createTable(tableName)
                .addColumn(new ColumnBuilder("F_TipDoc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ClaDoc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_StsFac").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Cliente").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Ruta").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecApl").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Consig").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_Produc").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_UniVen").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_NumAlm").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Unidad").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_UniPaq").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaBru").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Descto").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaNet").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Costo").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Utilid").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Impto").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_VtaTot").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Precio").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Lote").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DirRef").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecEnt").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_User").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ObsPar").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_ObsFac").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DocAnt").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_AM").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_UnidadReq").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_DesPro").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_ClaLot").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_FecCad").setSQLType(Types.VARCHAR).toColumn())
                .addColumn(new ColumnBuilder("F_CosLot").setSQLType(Types.INTEGER).toColumn())
                .addColumn(new ColumnBuilder("F_Obs").setSQLType(Types.VARCHAR).toColumn())

                .toTable(database);*/
 /*try{
            ConectionDB Conn = new ConectionDB();
            Conn.conectar();
            String F_Unidad="";
            
            
            ResultSet Datos = Conn.consulta("SELECT T.F_ClaDoc,T.F_FecApl,T.F_ClaPro,T.F_CantSur,T.F_Costo,T.F_Lote,A.F_DesPro,L.F_ClaLot,L.F_FecCad,T.F_Monto,T.F_ClaCli "
                    + "FROM tb_transferencias T INNER JOIN tb_medica A ON T.F_ClaPro=A.F_ClaPro INNER JOIN tb_lote L ON T.F_Lote=L.F_FolLot AND T.F_Ubicacion=L.F_Ubica "
                    + "WHERE T.F_ClaDoc='57' ORDER BY T.F_ClaPro ASC");
            while(Datos.next()){
                table.addRow("F",Datos.getString(1),"A","2200A","",Datos.getString(2),"",Datos.getString(3),"0","1",Datos.getString(4),Datos.getString(4),"0","0","0",Datos.getString(5),(Datos.getInt(5)*-1),"0","0","0",Datos.getString(6),"R",Datos.getString(2),"SGWARE","0","0","","A",Datos.getString(4),Datos.getString(7),Datos.getString(8),Datos.getString(9),Datos.getString(5),"");//Inserting values into the table
            }
            Conn.cierraConexion();
        }catch(Exception e){
            
        }*/
        //table.addRow(122875, "Sarath Kumar Sivan","Infosys Limited.");//Inserting values into the table
        /*String tableName2 = "TB_Proceso"; // Creating table2
        Table table2 = createTable(tableName2)
                .addColumn(new ColumnBuilder("F_NomPro").setSQLType(Types.VARCHAR).toColumn())
                .toTable(database);
        table2.addRow("Abasto");//Inserting values into the table2
         */
    }

    public static void main(String[] args) throws IOException, SQLException {
        ExportarBd.startDatabaseProcess();
    }

}
