package Modula;

import conn.ConectionDB;
import conn.ConectionDB_SQLServer;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Proceso para guardar en la bd sql del modula
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class AbastoModula {

    public void AbastoModula(String F_OrdCom, String F_FolRemi) {

        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df4 = new SimpleDateFormat("yyyyMMddhhmmss");
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();

        try {
            con.conectar();
            conModula.conectar();
            try {
                conModula.ejecutar("delete from IMP_ORDINI_RIGHE where RIG_ORDINE='" + F_OrdCom + "-" + F_FolRemi + "'");
                conModula.ejecutar("delete from IMP_ORDINI where ORD_ORDINE='" + F_OrdCom + "-" + F_FolRemi + "'");
                ResultSet rset = con.consulta("select F_FecApl from tb_compratemp where F_OrdCom = '" + F_OrdCom + "' and F_FolRemi = '" + F_FolRemi + "' group by F_OrdCom, F_FolRemi");
                while (rset.next()) {
                    conModula.ejecutar("insert into IMP_ORDINI values ('" + F_OrdCom + "-" + F_FolRemi + "','A','','" + df4.format(df3.parse(rset.getString("F_FecApl"))) + "','V','','1')");
                }
                rset = con.consulta("select F_FecApl, F_FolRemi, F_OrdCom, F_ClaPro, F_Lote, F_FecCad, F_Cb, F_Pz from tb_compratemp where F_OrdCom = '" + F_OrdCom + "' and F_FolRemi = '" + F_FolRemi + "'");
                while (rset.next()) {
                    /*
                     * La 'A' es de inserción
                     */
                    conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('" + F_OrdCom + "-" + F_FolRemi + "','','" + rset.getString("F_ClaPro") + "','" + rset.getString("F_Lote") + "','1','" + rset.getString("F_Pz") + "','" + rset.getString("F_Cb") + "','" + df.format(df3.parse(rset.getString("F_FecCad"))) + "','')");
                    //con.insertar("update tb_factttemp set F_StsFact='0' where F_Id='" + rset.getString("F_Id") + "'");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            conModula.cierraConexion();
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void enviaMultRemis(String F_FecEnt) {
        /*
         * Método para mandar todos los folios de una ruta
         */
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df4 = new SimpleDateFormat("yyyyMMddhhmmss");
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();

        try {
            con.conectar();
            conModula.conectar();
            ResultSet rsetFacts = con.consulta("SELECT F_ClaDoc, F.F_Proyecto FROM tb_factura F INNER JOIN tb_uniatn U ON U.F_ClaCli = F.F_ClaCli WHERE F.F_ClaDoc IN ('RURAL', 'CSU') AND F_FecEnt = '" + F_FecEnt + "' AND F_StsFact='A' GROUP BY F_ClaDoc, F.F_Proyecto;");
            while (rsetFacts.next()) {

                String F_IdFact = rsetFacts.getString("F_ClaDoc");
                String idproyecto = rsetFacts.getString(2);

                try {
                    conModula.ejecutar("delete from IMP_ORDINI_RIGHE where RIG_ORDINE='" + F_IdFact + "';");
                    conModula.ejecutar("delete from IMP_ORDINI where ORD_ORDINE='" + F_IdFact + "';");
                    System.out.println("Este si es el Bueno");
                    ResultSet rset = con.consulta("select F_ClaCli, F_FecEnt, F_ClaDoc, F_ClaPro, F_ClaLot, F_FecCad, F_Cb, SUM(F_Cant) AS F_Cant, F_Id from v_folioremisiones where F_ClaDoc = '" + F_IdFact + "' AND F_Proyecto = '" + idproyecto + "' GROUP BY F_ClaCli, F_FecEnt, F_ClaDoc, F_ClaPro, F_ClaLot, F_FecCad, F_Cb;");
                    while (rset.next()) {
                        /*
                         * La 'A' es de inserción, La 'I' inserta y update
                         */
                        conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('" + rset.getString("F_ClaDoc") + "','','" + rset.getString("F_ClaPro") + "','" + rset.getString("F_ClaLot") + "','1','" + rset.getString("F_Cant") + "','" + rset.getString("F_Cb") + "','" + df.format(df3.parse(rset.getString("F_FecCad"))) + "','')");
                        //con.insertar("update tb_factttemp set F_StsFact='0' where F_Id='" + rset.getString("F_Id") + "'");
                    }
                    rset = con.consulta("select F_ClaCli, F_FecEnt, F_ClaDoc from v_folioremisiones where F_ClaDoc = '" + F_IdFact + "' AND F_Proyecto = '" + idproyecto + "' group by F_ClaDoc");
                    while (rset.next()) {
                        //conModula.ejecutar("insert into IMP_ORDINI values ('" + rset.getString("F_IdFact") + "','A','','" + df.format(df3.parse(rset.getString("F_FecEnt"))) + "','P','" + rset.getString("F_ClaCli") + "','1','P')");
                        conModula.ejecutar("insert into IMP_ORDINI values ('" + rset.getString("F_ClaDoc") + "','I','','" + df.format(df3.parse(rset.getString("F_FecEnt"))) + "','P','" + rset.getString("F_ClaCli") + "','1','P')");
                    }

                    conModula.ejecutar("update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= '" + F_IdFact + "'");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            /* Elimina los pedidos ya surtidos */
            conModula.ejecutar("DELETE OD from IMP_ORDINI_RIGHE OD JOIN SYSTOREDB.dbo.DAT_ORDINI_RIGHE SDO ON OD.RIG_ORDINE = SDO.RIG_ORDINE COLLATE Latin1_General_CI_AS AND OD.RIG_ARTICOLO = SDO.RIG_ARTICOLO  COLLATE Latin1_General_CI_AS WHERE SDO.RIG_STARIORD IN('I', 'C')");

            conModula.cierraConexion();
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void enviaMultRemisCSU(String F_FecEnt) {
        /*
         * Método para mandar todos los folios de una ruta
         */
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df4 = new SimpleDateFormat("yyyyMMddhhmmss");
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();

        try {
            con.conectar();
            conModula.conectar();
            ResultSet rsetFacts = con.consulta("SELECT F_ClaDoc FROM tb_uniatn U INNER JOIN tb_factura F ON U.F_ClaCli=F.F_ClaCli WHERE F_Tipo IN ('CEAPS','CSU') AND F_FecEnt = '" + F_FecEnt + "' group by F_ClaDoc;");
            while (rsetFacts.next()) {
                System.out.println("este es el bueno");
                String F_IdFact = rsetFacts.getString("F_ClaDoc");
                try {
                    conModula.ejecutar("delete from IMP_ORDINI_RIGHE where RIG_ORDINE='" + F_IdFact + "'");
                    conModula.ejecutar("delete from IMP_ORDINI where ORD_ORDINE='" + F_IdFact + "'");
                    ResultSet rset = con.consulta("select F_ClaCli, F_FecEnt, F_ClaDoc from v_folioremisiones where F_ClaDoc = '" + F_IdFact + "' group by F_ClaDoc");
                    while (rset.next()) {
                        conModula.ejecutar("insert into IMP_ORDINI values ('" + rset.getString("F_ClaDoc") + "','A','','" + df.format(df3.parse(rset.getString("F_FecEnt"))) + "','P','" + rset.getString("F_ClaCli") + "','1','P')");
                    }
                    rset = con.consulta("select F_ClaCli, F_FecEnt, F_ClaDoc, F_ClaPro, F_ClaLot, F_FecCad, F_Cb, SUM(F_Cant) AS F_Cant, F_Id from v_folioremisiones where F_ClaDoc = '" + F_IdFact + "' GROUP BY F_ClaCli, F_FecEnt, F_ClaDoc, F_ClaPro, F_ClaLot, F_FecCad, F_Cb");
                    while (rset.next()) {
                        /*
                         * La 'A' es de inserción
                         */
                        conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('" + rset.getString("F_ClaDoc") + "','','" + rset.getString("F_ClaPro") + "','" + rset.getString("F_ClaLot") + "','1','" + rset.getString("F_Cant") + "','" + rset.getString("F_Cb") + "','" + df.format(df3.parse(rset.getString("F_FecCad"))) + "','')");
                        //con.insertar("update tb_factttemp set F_StsFact='0' where F_Id='" + rset.getString("F_Id") + "'");
                    }
                    conModula.ejecutar("update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= '" + F_IdFact + "'");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            conModula.cierraConexion();
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void enviaRuta(String F_FecEnt) {
        /*
         * Método para mandar Concentrados por ruta
         */
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df4 = new SimpleDateFormat("yyyyMMddhhmmss");
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();

        try {
            con.conectar();
            conModula.conectar();
            try {
                System.out.println("Entre en donde");
                conModula.ejecutar("delete from IMP_ORDINI_RIGHE where RIG_ORDINE='R" + F_FecEnt + "'");
                conModula.ejecutar("delete from IMP_ORDINI where ORD_ORDINE='R" + F_FecEnt + "'");
                ResultSet rset = con.consulta("select F_FecEnt from tb_factura where F_FecEnt = '" + F_FecEnt + "' group by F_FecEnt");
                while (rset.next()) {
                    conModula.ejecutar("insert into IMP_ORDINI values ('R" + F_FecEnt + "','A','','" + df4.format(df3.parse(rset.getString("F_FecEnt"))) + "','V','','1','P')");
                }
                rset = con.consulta("select F_FecEnt, F_ClaPro, F_ClaLot, F_FecCad, F_Cb, SUM(F_Cant) as F_Cant, F_Id from v_folioremisiones where F_FecEnt = '" + F_FecEnt + "' group by F_ClaPro, F_ClaLot, F_FecCad");
                while (rset.next()) {
                    /*
                     * La 'A' es de inserción
                     */
                    conModula.ejecutar("insert into IMP_ORDINI_RIGHE values('R" + F_FecEnt + "','','" + rset.getString("F_ClaPro") + "','" + rset.getString("F_ClaLot") + "','1','" + rset.getString("F_Cant") + "','" + rset.getString("F_Cb") + "','" + df.format(df3.parse(rset.getString("F_FecCad"))) + "','')");
                    con.insertar("update tb_facttemp set F_StsMod='1' where F_Id='" + rset.getString("F_Id") + "'");
                }
                conModula.ejecutar("update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= 'R" + F_FecEnt + "'");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            conModula.cierraConexion();
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
