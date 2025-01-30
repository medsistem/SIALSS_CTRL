package Develuciones;

import conn.ConectionDB;
import conn.ConectionDBTrans;
import in.co.sneh.model.Apartado;
import in.co.sneh.persistance.ApartadoDAOImpl;
import in.co.sneh.picking.service.PickingService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import sun.text.normalizer.UBiDiProps;

/**
 * Implementación class ConsultaDevoDao devoluciones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ConsultaDevoDaoImpl implements ConsultaDevoDao {

    public static String ConsultaRegistro = "SELECT COUNT(F_IdFact) FROM tb_devoglobalfact WHERE F_ClaDoc=?;";

    public static String ConsultaIndiceDev = "SELECT F_IndDev FROM tb_indice;";
    public final String ActualizaIndice = "UPDATE tb_indice SET F_IndDev=?";

    public static String ConsultaRegistroDev = "SELECT F_IdFact,F_CantDevo FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
    public static String ConsultaDatosFactura = "SELECT F_ClaCli, f.F_ClaPro, F_CantReq, F_CantSur, F_Costo, F_Iva, F_Monto, F_Lote, F_FecEnt, F_Ubicacion, f.F_Proyecto, F_Contrato, F_OC, F_Cause, l.F_Proyecto AS F_ProyectoLote,l.F_Origen FROM tb_factura f INNER JOIN tb_lote l ON f.F_ClaPro = l.F_ClaPro AND f.F_Lote = l.F_FolLot AND f.F_Ubicacion = l.F_Ubica WHERE F_IdFact = ? AND F_ClaDoc = ?;";
    public final String ActualizaFactura = "UPDATE tb_factura SET F_StsFact=?,F_Obs=?,F_DocAnt=? WHERE F_IdFact=? AND F_ClaDoc=?;";
    public final String ActualizaFacturaCant = "UPDATE tb_factura SET F_CantSur=?,F_Iva=?,F_Monto=? WHERE F_IdFact=? AND F_ClaDoc=?;";
    public static String BuscaDatosLote = "SELECT F_IdLote, F_ExiLot, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ?;";
    public static String BuscaRegistroLote = "SELECT F_IdLote, F_ExiLot, F_ClaLot, F_FecCad, F_FecFab, F_Cb, F_ClaMar, F_Origen, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ?;";
    public final String ActualizaExiLote = "UPDATE tb_lote SET F_ExiLot = ? WHERE F_IdLote = ? AND F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ? AND F_Proyecto = ?;";
    public final String InsertarLote = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,'131',?);";
    public final String InsertarMovimiento = "INSERT INTO tb_movinv VALUES(0,CURDATE(),?,?,?,?,?,?,'1',?,?,?,CURTIME(),?,'');";
    public final String InsertarFactura = "INSERT INTO tb_factura VALUES(0,?,?,'C',CURDATE(),?,'0',?,?,?,?,?,?,CURTIME(),?,?,?,?,?,?,?,?);";
    public final String EliminaRegDevGlobal = "DELETE FROM tb_devoglobalfact WHERE F_IdFact=? AND F_ClaDoc=?;";
    public static String SumaFactura = "SELECT SUM(F_CantSur) FROM tb_factura WHERE F_ClaDoc=? AND F_StsFact='A' AND F_Proyecto = ?;";
    public final String ActualizaStsDocantFact = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=? AND F_DocAnt !=1 AND F_Proyecto = ?;";
    public final String ActualizaStsDocant = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=? AND F_Proyecto = ?;";
    public final String InsertDevolucion = "INSERT INTO tb_devoluciones VALUES(CURRENT_TIMESTAMP(),?,?,?,?,?,0);";

//    public final String queryInserta = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0);";
    public final String queryElimina = "DELETE FROM tb_abastoweb WHERE F_Sts = 0 AND F_Proyecto = ? AND F_ClaDoc = ?;";
    public static String ValidaAbasto = "SELECT COUNT(*) FROM tb_abastoweb WHERE F_Sts = 1 AND F_Proyecto = ? AND F_ClaDoc = ?;";
    public final String queryInserta = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0,?);";
    public static String queryDatosCsV = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(), L.F_Origen AS ORIGEN, F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F.F_Proyecto = ? AND F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

    public static String getFactorEmpaque = "SELECT IFNULL(F_FactorEmpaque, 0) as factor FROM tb_compra where F_Lote = ? order by F_IdCom DESC";
//    public static String queryDatosCsV = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW() FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' AND F.F_Proyecto = ? GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";


    /*GASTOS CATASTROFICOS*/
    public static final String ACTUALIZA_GASTOS = "UPDATE tb_gasto_catastrofico SET Cant_Total = ? , Cant_Remi = ? WHERE  Id_Gastos = ? ;";

    public static final String BUSCA_GASTOS = "SELECT Id_Gastos, Cant_Total, Cant_Remi FROM tb_gasto_catastrofico WHERE Id_Lote = ? and No_clacli=?;";

    private final ConectionDBTrans con = new ConectionDBTrans();
    PreparedStatement ps;
    PreparedStatement psInsertarLote;
    PreparedStatement psActFact;
    PreparedStatement psActLote;
    PreparedStatement psInsertarMov;
    PreparedStatement psActFactCant;
    PreparedStatement psInsertarFact;
    PreparedStatement psActStsDocantFact;
    PreparedStatement psActStsDocant;
    PreparedStatement psInsertDevo;
    PreparedStatement psActIndice;
    PreparedStatement psAbastoInsert;
    PreparedStatement psActGCCant;
    PreparedStatement ps2;

    ResultSet rs;
    ResultSet rsD;
    ResultSet rs2;
    ResultSet rsGC;

    @Override
    public boolean ValidaDevolucionTran(String Folio, String Obs, String Usuario, String Proyectos) {

        boolean save = false;
        int IdFact = 0, CantDevo = 0, Lote = 0, CantReq = 0, CantSur = 0, DevoCant = 0, IdLote = 0, IdLote1 = 0, ExiLot = 0, Total = 0, Contar = 0;
//        int IndDev = 0, DocDev = 0, Proyecto = 0, ProyectoL = 0;
        int IndDev = 0, DocDev = 0, Proyecto = 0, ProyectoL = 0, factorEmpaque = 1;
        double Iva = 0.00, Monto = 0.00, Costo = 0.00, IvaDeVo = 0.00, IvaTot = 0.00, IvaTotDev = 0.00, MontoDev = 0.00;
        String ClaCli = "", ClaPro = "", FecEnt = "", Ubicacion = "", ClaLot = "", FecCad = "", FecFab = "", Cb = "", ClaMar = "", Origen = "", Concepto = "", DocAnt = "", UbicaDes = "", ClaOrg = "", Contrato = "", OC = "", Cause = "", OrigenGC = "";
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);

            psActFact = con.getConn().prepareStatement(ActualizaFactura);
            psActLote = con.getConn().prepareStatement(ActualizaExiLote);
            psInsertarLote = con.getConn().prepareStatement(InsertarLote);
            psInsertarMov = con.getConn().prepareStatement(InsertarMovimiento);
            psActFactCant = con.getConn().prepareStatement(ActualizaFacturaCant);
            psInsertarFact = con.getConn().prepareStatement(InsertarFactura);
            psActStsDocantFact = con.getConn().prepareStatement(ActualizaStsDocantFact);
            psActStsDocant = con.getConn().prepareStatement(ActualizaStsDocant);
            psInsertDevo = con.getConn().prepareStatement(InsertDevolucion);
            psActIndice = con.getConn().prepareStatement(ActualizaIndice);

            if (Usuario.equals("EduardoS")) {

                Concepto = "4";
                DocAnt = "1";
            } else {
                Concepto = "5";
                //DocAnt = "0";
                DocAnt = "1";
            }

//tablatemporal devoglobalfact
            ps = con.getConn().prepareStatement(ConsultaRegistro);
            ps.setString(1, Folio);
            rs = ps.executeQuery();
            if (rs.next()) {
                Contar = rs.getInt(1);
            }

            System.out.println("Folio: " + Folio + " Contar: " + Contar);

            if (Contar > 0) {

                ps2 = con.getConn().prepareStatement(ConsultaIndiceDev);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    IndDev = rs2.getInt(1);
                }
                ps2.clearParameters();
                ps.clearParameters();

                ps = con.getConn().prepareStatement(ConsultaRegistroDev);
                ps.setString(1, Folio);
                rsD = ps.executeQuery();
                while (rsD.next()) {
                    IdFact = rsD.getInt(1);
                    CantDevo = rsD.getInt(2);

                    ps2 = con.getConn().prepareStatement(ConsultaDatosFactura);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        ClaCli = rs2.getString(1);
                        ClaPro = rs2.getString(2);
                        CantReq = rs2.getInt(3);
                        CantSur = rs2.getInt(4);
                        Costo = rs2.getDouble(5);
                        Iva = rs2.getDouble(6);
                        Monto = rs2.getDouble(7);
                        Lote = rs2.getInt(8);
                        FecEnt = rs2.getString(9);
                        Ubicacion = rs2.getString(10);
                        Proyecto = rs2.getInt(11);
                        Contrato = rs2.getString(12);
                        OC = rs2.getString(13);
                        Cause = rs2.getString(14);
                        OrigenGC = rs2.getString(16);
                    }
                    System.out.println("OrigenGC: " + OrigenGC);

                    ps2.clearParameters();

                    if (Concepto.equals("4")) {
                        UbicaDes = Ubicacion;
                    } else {
                        UbicaDes = "NUEVA_DEVOLUCION";
                    }

                    boolean addMove = true;

                    DevoCant = CantSur - CantDevo;

                    System.out.println("Cantidad de devolucion: " + DevoCant);

/////devolver todo
                    if (DevoCant == 0) {

                        psActFact.setString(1, "C");
                        psActFact.setString(2, Obs);
                        psActFact.setString(3, DocAnt);
                        psActFact.setInt(4, IdFact);
                        psActFact.setString(5, Folio);
                        psActFact.addBatch();

                        if (Proyecto == 2) {

                            if (Ubicacion.equals("AF") || Ubicacion.equals("AF1N") || Ubicacion.equals("AFGC")) {
                                ps.clearParameters();
                                ps = con.getConn().prepareStatement(BuscaDatosLote);
                                ps.setString(1, ClaPro);
                                ps.setInt(2, Lote);
                                ps.setString(3, Ubicacion);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    IdLote = rs.getInt(1);
                                    ExiLot = rs.getInt(2);
                                    ClaOrg = rs.getString(3);
                                    ProyectoL = rs.getInt(4);
                                }
                                ps.clearParameters();

                                PickingService serv = new PickingService();
                                int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyecto);
                                boolean editable = (pickingStatus == -1) || (pickingStatus >= 5);
                                if (editable) {
                                    addMove = pickingStatus >= 5;
                                    ApartadoDAOImpl apartadoDao = new ApartadoDAOImpl(this.con.getConn());
                                    Apartado apartado = apartadoDao.getByIdLoteAndClaDoc(IdLote, Folio);
                                    if (apartado != null) {
                                        if (CantDevo == apartado.getCant()) {
                                            apartadoDao.delete(apartado.getId());
                                        } else {
                                            apartadoDao.updateCant(apartado.getId(), apartado.getCant() - CantDevo);
                                        }
                                        ///ACTUALIZA GASTOS CATASTROFICOS
                                        if (OrigenGC.equals("19")) {

                                            PreparedStatement psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                                            psActualizaGastos.setInt(1, Lote);
                                            psActualizaGastos.setString(2, ClaCli);

                                            Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                                            ResultSet rsetGastos = psActualizaGastos.executeQuery();
                                            while (rsetGastos.next()) {
                                                idGastos = rsetGastos.getInt(1);
                                                totalGastos = rsetGastos.getInt(2);
                                                remiGastos = rsetGastos.getInt(3);
                                            }
                                            System.out.println("BUSCA GASTOS TOTAL: " + psActualizaGastos);
                                            psActualizaGastos.execute();
                                            psActualizaGastos.clearParameters();
                                            psActualizaGastos.close();

                                            psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                                            psActualizaGastos.setInt(1, (totalGastos + CantDevo));
                                            psActualizaGastos.setInt(2, (remiGastos - CantDevo));
                                            psActualizaGastos.setInt(3, idGastos);
                                            psActualizaGastos.execute();
                                            System.out.println("UPDATE GASTOS TOTAL: " + psActualizaGastos);
                                            psActualizaGastos.clearParameters();
                                            psActualizaGastos.close();
                                        }
                                    }
                                } else {
                                    return false;
                                }
                            }
                        }//fin del if de proyecto

                        if (addMove) {
                            IdLote = 0;
                            ps.clearParameters();
                            ps = con.getConn().prepareStatement(BuscaDatosLote);
                            ps.setString(1, ClaPro);
                            ps.setInt(2, Lote);
                            ps.setString(3, UbicaDes);
                            System.out.println(ps);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                IdLote = rs.getInt(1);
                                ExiLot = rs.getInt(2);
                                ClaOrg = rs.getString(3);
                                ProyectoL = rs.getInt(4);
                            }
                            ps.clearParameters();

                            if (IdLote > 0) {
                                Total = ExiLot + CantDevo;

                                psActLote.setInt(1, Total);
                                psActLote.setInt(2, IdLote);
                                psActLote.setString(3, ClaPro);
                                psActLote.setInt(4, Lote);
                                psActLote.setString(5, UbicaDes);
                                psActLote.setInt(6, ProyectoL);
                                psActLote.addBatch();
                            } else {

                                ps2 = con.getConn().prepareStatement(BuscaRegistroLote);
                                ps2.setString(1, ClaPro);
                                ps2.setInt(2, Lote);
                                rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    IdLote1 = rs2.getInt(1);
                                    ExiLot = rs2.getInt(2);
                                    ClaLot = rs2.getString(3);
                                    FecCad = rs2.getString(4);
                                    FecFab = rs2.getString(5);
                                    Cb = rs2.getString(6);
                                    ClaMar = rs2.getString(7);
                                    Origen = rs2.getString(8);
                                    ClaOrg = rs2.getString(9);
                                    ProyectoL = rs2.getInt(10);
                                }
                                ps2.clearParameters();

                                if (IdLote1 > 0) {

                                    psInsertarLote.setString(1, ClaPro);
                                    psInsertarLote.setString(2, ClaLot);
                                    psInsertarLote.setString(3, FecCad);
                                    psInsertarLote.setInt(4, CantDevo);
                                    psInsertarLote.setInt(5, Lote);
                                    psInsertarLote.setString(6, ClaOrg);
                                    psInsertarLote.setString(7, UbicaDes);
                                    psInsertarLote.setString(8, FecFab);
                                    psInsertarLote.setString(9, Cb);
                                    psInsertarLote.setString(10, ClaMar);
                                    psInsertarLote.setString(11, Origen);
                                    psInsertarLote.setString(12, ClaOrg);
                                    psInsertarLote.setInt(13, ProyectoL);
                                    psInsertarLote.addBatch();

                                    IdLote1 = 0;
                                }
                            }
                            psInsertarMov.setInt(1, IndDev);
                            psInsertarMov.setString(2, Concepto);
                            psInsertarMov.setString(3, ClaPro);
                            psInsertarMov.setInt(4, CantDevo);
                            psInsertarMov.setDouble(5, Costo);
                            psInsertarMov.setDouble(6, Monto);
                            psInsertarMov.setInt(7, Lote);
                            psInsertarMov.setString(8, UbicaDes);
                            psInsertarMov.setString(9, ClaOrg);
                            psInsertarMov.setString(10, Usuario);
                            psInsertarMov.addBatch();

                            if (Proyecto == 2) {
                                if (!(Ubicacion.equals("AF") || Ubicacion.equals("AF1N") || Ubicacion.equals("AFGC")) && OrigenGC.equals("19")) {

                                    ///ACTUALIZA GASTOS CATASTROFICOS
                                    PreparedStatement psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                                    psActualizaGastos.setInt(1, Lote);
                                    psActualizaGastos.setString(2, ClaCli);

                                    Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                                    ResultSet rsetGastos = psActualizaGastos.executeQuery();
                                    while (rsetGastos.next()) {
                                        idGastos = rsetGastos.getInt(1);
                                        totalGastos = rsetGastos.getInt(2);
                                        remiGastos = rsetGastos.getInt(3);
                                    }
                                    System.out.println("BUSCA GASTOS TOTAL: " + psActualizaGastos);
                                    psActualizaGastos.execute();
                                    psActualizaGastos.clearParameters();
                                    psActualizaGastos.close();

                                    psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                                    psActualizaGastos.setInt(1, (totalGastos + CantDevo));
                                    psActualizaGastos.setInt(2, (remiGastos - CantDevo));
                                    psActualizaGastos.setInt(3, idGastos);
                                    psActualizaGastos.execute();
                                    System.out.println("UPDATE GASTOS TOTAL: " + psActualizaGastos);
                                    psActualizaGastos.clearParameters();
                                    psActualizaGastos.close();
                                }
                            }

                        }

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;

                        ///DEVOLUCION PARCIAL
                    } else if (DevoCant > 0) {

                        if (Iva > 0) {
                            IvaDeVo = 0.16;
                        } else {
                            IvaDeVo = 0.00;
                        }
                        IvaTot = ((DevoCant * Costo) * IvaDeVo);
                        Monto = (DevoCant * Costo) + IvaTot;

                        IvaTotDev = ((CantDevo * Costo) * IvaDeVo);
                        MontoDev = ((CantDevo * Costo) * IvaTotDev);

                        psActFactCant.setInt(1, DevoCant);
                        psActFactCant.setDouble(2, IvaTot);
                        psActFactCant.setDouble(3, Monto);
                        psActFactCant.setInt(4, IdFact);
                        psActFactCant.setString(5, Folio);
                        psActFactCant.addBatch();

                        psInsertarFact.setString(1, Folio);
                        psInsertarFact.setString(2, ClaCli);
                        psInsertarFact.setString(3, ClaPro);
                        psInsertarFact.setInt(4, CantDevo);
                        psInsertarFact.setDouble(5, Costo);
                        psInsertarFact.setDouble(6, IvaTotDev);
                        psInsertarFact.setDouble(7, MontoDev);
                        psInsertarFact.setInt(8, Lote);
                        psInsertarFact.setString(9, FecEnt);
                        psInsertarFact.setString(10, Usuario);
                        psInsertarFact.setString(11, Ubicacion);
                        psInsertarFact.setString(12, Obs);
                        psInsertarFact.setString(13, DocAnt);
                        psInsertarFact.setInt(14, Proyecto);
                        psInsertarFact.setString(15, Contrato);
                        psInsertarFact.setString(16, OC);
                        psInsertarFact.setString(17, Cause);
                        psInsertarFact.addBatch();
                        /*PROYECTO MICHOACAN*/
                        if (Proyecto == 2) {
                            if (Ubicacion.equals("AF1N") || Ubicacion.equals("AF") || Ubicacion.equals("AFGC")) {
                                ps2.clearParameters();
                                ps2 = con.getConn().prepareStatement(BuscaDatosLote);
                                ps2.setString(1, ClaPro);
                                ps2.setInt(2, Lote);
                                ps2.setString(3, Ubicacion);
                                rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    IdLote = rs2.getInt(1);
                                    ExiLot = rs2.getInt(2);
                                    ClaOrg = rs2.getString(3);
                                    ProyectoL = rs2.getInt(4);
                                }
                                ps2.clearParameters();
                                PickingService serv = new PickingService();
                                int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyecto);
                                boolean editable = (pickingStatus == -1) || (pickingStatus >= 5);
                                if (editable) {
                                    addMove = pickingStatus >= 5;
                                    ApartadoDAOImpl apartadoDao = new ApartadoDAOImpl(this.con.getConn());
                                    Apartado apartado = apartadoDao.getByIdLoteAndClaDoc(IdLote, Folio);
                                    if (apartado != null) {
                                        if (CantDevo == apartado.getCant()) {
                                            apartadoDao.delete(apartado.getId());
                                        } else {
                                            apartadoDao.updateCant(apartado.getId(), apartado.getCant() - CantDevo);
                                        }
                                        if (OrigenGC.equals("19")) {

                                            PreparedStatement psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                                            psActualizaGastos.setInt(1, Lote);
                                            psActualizaGastos.setString(2, ClaCli);

                                            Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                                            ResultSet rsetGastos = psActualizaGastos.executeQuery();
                                            while (rsetGastos.next()) {
                                                idGastos = rsetGastos.getInt(1);
                                                totalGastos = rsetGastos.getInt(2);
                                                remiGastos = rsetGastos.getInt(3);
                                            }
                                            System.out.println("BUSCA GASTOS TOTAL: " + psActualizaGastos);
                                            psActualizaGastos.execute();
                                            psActualizaGastos.clearParameters();
                                            psActualizaGastos.close();

                                            psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                                            psActualizaGastos.setInt(1, (totalGastos + CantDevo));
                                            psActualizaGastos.setInt(2, (remiGastos - CantDevo));
                                            psActualizaGastos.setInt(3, idGastos);
                                            psActualizaGastos.execute();
                                            System.out.println("UPDATE GASTOS TOTAL: " + psActualizaGastos);
                                            psActualizaGastos.clearParameters();
                                            psActualizaGastos.close();
                                        }
                                    }
                                } else {
                                    return false;
                                }
                            }
                        }//fin del if de proyecto

                        if (addMove) {
                            IdLote = 0;
                            ps.clearParameters();
                            ps = con.getConn().prepareStatement(BuscaDatosLote);
                            ps.setString(1, ClaPro);
                            ps.setInt(2, Lote);
                            ps.setString(3, UbicaDes);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                IdLote = rs.getInt(1);
                                ExiLot = rs.getInt(2);
                                ClaOrg = rs.getString(3);
                                ProyectoL = rs.getInt(4);
                            }

                            System.out.println("IdLote: " + IdLote);
                            if (IdLote > 0) {
                                Total = ExiLot + CantDevo;

                                psActLote.setInt(1, Total);
                                psActLote.setInt(2, IdLote);
                                psActLote.setString(3, ClaPro);
                                psActLote.setInt(4, Lote);
                                psActLote.setString(5, UbicaDes);
                                psActLote.setInt(6, ProyectoL);
                                psActLote.addBatch();

                            } else {

                                ps2 = con.getConn().prepareStatement(BuscaRegistroLote);
                                ps2.setString(1, ClaPro);
                                ps2.setInt(2, Lote);
                                rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    IdLote1 = rs2.getInt(1);
                                    ExiLot = rs2.getInt(2);
                                    ClaLot = rs2.getString(3);
                                    FecCad = rs2.getString(4);
                                    FecFab = rs2.getString(5);
                                    Cb = rs2.getString(6);
                                    ClaMar = rs2.getString(7);
                                    Origen = rs2.getString(8);
                                    ClaOrg = rs2.getString(9);
                                    ProyectoL = rs2.getInt(10);
                                }
                                ps2.clearParameters();

                                System.out.println("IdLote1 : " + IdLote1);
                                if (IdLote1 > 0) {

                                    psInsertarLote.setString(1, ClaPro);
                                    psInsertarLote.setString(2, ClaLot);
                                    psInsertarLote.setString(3, FecCad);
                                    psInsertarLote.setInt(4, CantDevo);
                                    psInsertarLote.setInt(5, Lote);
                                    psInsertarLote.setString(6, ClaOrg);
                                    psInsertarLote.setString(7, UbicaDes);
                                    psInsertarLote.setString(8, FecFab);
                                    psInsertarLote.setString(9, Cb);
                                    psInsertarLote.setString(10, ClaMar);
                                    psInsertarLote.setString(11, Origen);
                                    psInsertarLote.setString(12, ClaOrg);
                                    psInsertarLote.setInt(13, ProyectoL);
                                    psInsertarLote.addBatch();

                                    IdLote1 = 0;
                                }
                            }
                            ps.clearParameters();

                            psInsertarMov.setInt(1, IndDev);
                            psInsertarMov.setString(2, Concepto);
                            psInsertarMov.setString(3, ClaPro);
                            psInsertarMov.setInt(4, CantDevo);
                            psInsertarMov.setDouble(5, Costo);
                            psInsertarMov.setDouble(6, MontoDev);
                            psInsertarMov.setInt(7, Lote);
                            psInsertarMov.setString(8, UbicaDes);
                            psInsertarMov.setString(9, ClaOrg);
                            psInsertarMov.setString(10, Usuario);
                            psInsertarMov.addBatch();

                            System.out.println("OrigenGC parcial:" + OrigenGC);
                            if (Proyecto == 2) {
                                if (!(Ubicacion.equals("AF") || Ubicacion.equals("AF1N") || Ubicacion.equals("AFGC")) && OrigenGC.equals("19")) {
                                    ///ACTUALIZA GASTOS CATASTROFICOS
                                    PreparedStatement psActualizaGastos = con.getConn().prepareStatement(BUSCA_GASTOS);
                                    psActualizaGastos.setInt(1, Lote);
                                    psActualizaGastos.setString(2, ClaCli);

                                    Integer idGastos = 0, totalGastos = 0, remiGastos = 0;

                                    ResultSet rsetGastos = psActualizaGastos.executeQuery();
                                    while (rsetGastos.next()) {
                                        idGastos = rsetGastos.getInt(1);
                                        totalGastos = rsetGastos.getInt(2);
                                        remiGastos = rsetGastos.getInt(3);
                                    }
                                    System.out.println("BUSCA GASTOS PARCIAL: " + psActualizaGastos);
                                    psActualizaGastos.execute();
                                    psActualizaGastos.clearParameters();
                                    psActualizaGastos.close();

                                    psActualizaGastos = con.getConn().prepareStatement(ACTUALIZA_GASTOS);

                                    psActualizaGastos.setInt(1, (totalGastos + CantDevo));
                                    psActualizaGastos.setInt(2, (remiGastos - CantDevo));
                                    psActualizaGastos.setInt(3, idGastos);
                                    psActualizaGastos.execute();
                                    System.out.println("UPDATE GASTOS PARCIAL:  " + psActualizaGastos);
                                    psActualizaGastos.clearParameters();
                                    psActualizaGastos.close();

                                }
                            }

                        }

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;
                    }
                    ps2.clearParameters();
                    ps2 = con.getConn().prepareStatement(EliminaRegDevGlobal);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    ps2.execute();
                    ps2.clearParameters();
                    System.out.println("se elimina de tabla temporal");
                    int Existencia = 0;

                    ps = con.getConn().prepareStatement(SumaFactura);
                    ps.setString(1, Folio);
                    ps.setString(2, Proyectos);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Existencia = rs.getInt(1);
                    }
                    ps.clearParameters();

                    if (Existencia == 0) {
                        if (DocAnt.equals("0")) {
                            psActStsDocantFact.setString(1, "C");
                            psActStsDocantFact.setString(2, DocAnt);
                            psActStsDocantFact.setString(3, Folio);
                            psActStsDocantFact.setString(4, Proyectos);
                            psActStsDocantFact.addBatch();
                        } else {
                            psActStsDocant.setString(1, "C");
                            psActStsDocant.setString(2, DocAnt);
                            psActStsDocant.setString(3, Folio);
                            psActStsDocant.setString(4, Proyectos);
                            psActStsDocant.addBatch();
                        }

                    }
                    Existencia = 0;

                }
                PickingService serv = new PickingService();
                int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyecto);
                boolean editable = (pickingStatus == -1) || (pickingStatus < 5);
                if (!editable) {
                    System.out.println("entre a PickingService: " + editable);
                    psInsertDevo.setInt(1, IndDev);
                    psInsertDevo.setString(2, Folio);
                    psInsertDevo.setString(3, Obs);
                    psInsertDevo.setString(4, Usuario);
                    psInsertDevo.setInt(5, Proyecto);
                    psInsertDevo.addBatch();
                    IndDev = IndDev + 1;
                    psActIndice.setInt(1, IndDev);
                    psActIndice.addBatch();
                    System.out.println("IndDev: " + IndDev-- + "/" + IndDev);
                    psActFact.executeBatch();

                    psActLote.executeBatch();

                    psInsertarLote.executeBatch();

                    psInsertarMov.executeBatch();
                    psActFactCant.executeBatch();
                    psInsertarFact.executeBatch();
                    psActStsDocantFact.executeBatch();
                    psActStsDocant.executeBatch();
                    psInsertDevo.executeBatch();
                    psActIndice.executeBatch();
                    save = true;
                    con.getConn().commit();
                } else if (editable) {
                    System.out.println("No entre a PickingService ");

                    psActFact.executeBatch();
                    psActLote.executeBatch();

                    psInsertarLote.executeBatch();

                    psInsertarMov.executeBatch();
                    psActFactCant.executeBatch();
                    psInsertarFact.executeBatch();
                    psActStsDocantFact.executeBatch();
                    psActStsDocant.executeBatch();
//                    if (Proyecto == 2) {
//                        if (!(Ubicacion.equals("AF") || Ubicacion.equals("AF1N") || Ubicacion.equals("AFGC"))) {
                            psInsertDevo.setInt(1, IndDev);
                            psInsertDevo.setString(2, Folio);
                            psInsertDevo.setString(3, Obs);
                            psInsertDevo.setString(4, Usuario);
                            psInsertDevo.setInt(5, Proyecto);
                            psInsertDevo.addBatch();
                            psInsertDevo.executeBatch();

                            IndDev = IndDev + 1;
                            psActIndice.setInt(1, IndDev);
                            psActIndice.addBatch();

                            System.out.println("IndDev: "+IndDev--+"/"+IndDev++);

//                        }
//                    }

                    psActFact.executeBatch();
                    psActIndice.executeBatch();
                    save = true;
                    con.getConn().commit();
                } else {

                    save = false;
                    con.getConn().rollback();
                    con.cierraConexion();
                    return save;
                }
            }

            int Existencia = 0;
            ps = con.getConn().prepareStatement(SumaFactura);
            ps.setString(1, Folio);
            ps.setString(2, Proyectos);
            rs = ps.executeQuery();
            if (rs.next()) {
                Existencia = rs.getInt(1);
            }
            ps.clearParameters();

            if (Existencia == 0) {
                if (DocAnt.equals("0")) {
                    psActStsDocantFact.setString(1, "C");
                    psActStsDocantFact.setString(2, DocAnt);
                    psActStsDocantFact.setString(3, Folio);
                    psActStsDocantFact.setString(4, Proyectos);
                    psActStsDocantFact.addBatch();
                } else {
                    psActStsDocant.setString(1, "C");
                    psActStsDocant.setString(2, DocAnt);
                    psActStsDocant.setString(3, Folio);
                    psActStsDocant.setString(4, Proyectos);
                    psActStsDocant.addBatch();
                }
            }
            Existencia = 0;
            psActStsDocantFact.executeBatch();
            psActStsDocant.executeBatch();
            psActFact.clearParameters();
            int Cuenta = 0, Origen2 = 0;
            String Origenes2 = "";

            psActFact = con.getConn().prepareStatement(ValidaAbasto);
            psActFact.setString(1, Proyectos);
            psActFact.setString(2, Folio);
            rs2 = psActFact.executeQuery();

            if (rs2.next()) {
                Cuenta = rs2.getInt(1);
            }

            psActFact.clearParameters();

            if (Cuenta == 0) {
                psActFact = con.getConn().prepareStatement(queryElimina);
                psActFact.setString(1, Proyectos);
                psActFact.setString(2, Folio);
                psActFact.execute();

                psActFact.clearParameters();
                psActFact = con.getConn().prepareStatement(queryDatosCsV);
                psActFact.setString(1, Proyectos);
                psActFact.setString(2, Folio);
                rs2 = psActFact.executeQuery();
                while (rs2.next()) {
///devolucion origen
                    Origen2 = rs2.getInt(9);
//                    if ((Origen2 == 8) || (Origen2 == 1)) {
//                        Origenes2 = "1";
//                    } else {
//                        Origenes2 = "0";
//                    }
                    factorEmpaque = 1;
                    int folLot = rs2.getInt("LOTE");
                    PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                    psfe.setInt(1, folLot);
                    ResultSet rsfe = psfe.executeQuery();
                    if (rsfe.next()) {
                        factorEmpaque = rsfe.getInt("factor");
                    }
                    ps.clearParameters();

                    ps = con.getConn().prepareStatement(queryInserta);
                    ps.setString(1, rs2.getString(1));
                    ps.setString(2, rs2.getString(2));
                    ps.setString(3, rs2.getString(3));
                    ps.setString(4, rs2.getString(4));
                    ps.setString(5, rs2.getString(5));
                    ps.setString(6, rs2.getString(6));
                    ps.setString(7, rs2.getString(7));
                    ps.setString(8, rs2.getString(8));
                    ps.setInt(9, rs2.getInt(12));
                    ps.setString(10, rs2.getString(10));
                    ps.setString(11, Usuario);
                    ps.setInt(12, factorEmpaque);
                    System.out.println("fact factor de empaque" + ps);
                    ps.execute();

                }
            } else {

                psActFact = con.getConn().prepareStatement(queryElimina);
                psActFact.setString(1, Proyectos);
                psActFact.setString(2, Folio);
                psActFact.execute();

                psActFact.clearParameters();
                psActFact = con.getConn().prepareStatement(queryDatosCsV);
                psActFact.setString(1, Proyectos);
                psActFact.setString(2, Folio);
                rs2 = psActFact.executeQuery();
                while (rs2.next()) {

                    Origen2 = rs2.getInt(9);
//                    if ((Origen2 == 0) || (Origen2 != 8)) {
//                        Origenes2 = "0";
//                    }  if (Origen2 == 8) {
//                        Origenes2 = "1";
//                    }
                    factorEmpaque = 1;
                    int folLot = rs2.getInt("LOTE");
                    PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                    psfe.setInt(1, folLot);
                    ResultSet rsfe = psfe.executeQuery();
                    if (rsfe.next()) {
                        factorEmpaque = rsfe.getInt("factor");
                    }
                    ps.clearParameters();

                    ps = con.getConn().prepareStatement(queryInserta);
                    ps.setString(1, rs2.getString(1));
                    ps.setString(2, rs2.getString(2));
                    ps.setString(3, rs2.getString(3));
                    ps.setString(4, rs2.getString(4));
                    ps.setString(5, rs2.getString(5));
                    ps.setString(6, rs2.getString(6));
                    ps.setString(7, rs2.getString(7));
                    ps.setString(8, rs2.getString(8));
                    ps.setInt(9, rs2.getInt(12));
                    ps.setString(10, rs2.getString(10));
                    ps.setString(11, Usuario);
                    ps.setInt(12, factorEmpaque);
                    System.out.println("fact factor de empaque" + ps);
                    ps.execute();

                }

            }

            con.getConn().commit();

            psActFact.close();
            psActLote.close();
            psInsertarLote.close();
            psInsertarMov.close();
            psActFactCant.close();
            psInsertarFact.close();
            psActStsDocantFact.close();
            psActStsDocant.close();
            psInsertDevo.close();
            psActIndice.close();

            psActFact = null;
            psActLote = null;
            psInsertarLote = null;
            psInsertarMov = null;
            psActFactCant = null;
            psInsertarFact = null;
            psActStsDocantFact = null;
            psActStsDocant = null;
            psInsertDevo = null;
            psActIndice = null;
            con.cierraConexion();
            return save;
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
                con.cierraConexion();
                save = false;
                return save;
            } catch (SQLException ex1) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }
        return save;
    }



/*ONTENER REGISTRO*/
    @Override
    public JSONArray getRegistro(String Folio) {

        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;

        String query = "SELECT F_IdFact FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            rs = ps.executeQuery();

            while (rs.next()) {
                jsonObj = new JSONObject();
                jsonObj.put("IdReg", rs.getString(1));
                jsonArray.add(jsonObj);
            }

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }







/*PARA ELIMINAR REGISTRO*/
    @Override
    public JSONArray getEliminaRegistro(String Folio, String IdReg) {
        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        System.out.println("Eliminar Reg Folio:" + Folio + " Id: " + IdReg);
        String query = "DELETE FROM tb_devoglobalfact WHERE F_ClaDoc=? AND F_IdFact=?;";
        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            ps.setString(2, IdReg);
            ps.execute();

            jsonObj = new JSONObject();
            jsonObj.put("Procesado", "Terminado");
            jsonArray.add(jsonObj);

            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.cierraConexion();
            } catch (SQLException ex1) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }






    @Override
    public JSONArray getValidaDevolucion(String Folio, String Obs, String Usuario, String Proyectos) {
        int IdFact = 0, CantDevo = 0, Lote = 0, CantReq = 0, CantSur = 0, DevoCant = 0, IdLote = 0, IdLote1 = 0, ExiLot = 0, Total = 0, Contar = 0;
        int IndDev = 0, DocDev = 0, Proyecto = 0, ProyectoL = 0, factorEmpaque = 1, Cuenta = 0;
        double Iva = 0.00, Monto = 0.00, Costo = 0.00, IvaDeVo = 0.00, IvaTot = 0.00, IvaTotDev = 0.00, MontoDev = 0.00;
        String ClaCli = "", ClaPro = "", FecEnt = "", Ubicacion = "", ClaLot = "", FecCad = "", FecFab = "", Cb = "", ClaMar = "", Origen = "", Consulta = "", Consulta2 = "", Concepto = "", DocAnt = "", UbicaDes = "", ClaOrg = "", Contrato = "", OC = "", Cause = "";
        ConectionDB con = new ConectionDB();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        System.out.println("aqui ando en getValidaDevolucion");
        if ( Usuario.equals("EduardoS")) {

            Concepto = "4";
            DocAnt = "1";
        } else {
            Concepto = "5";
            DocAnt = "0";
        }

        String query = "SELECT COUNT(F_IdFact) FROM tb_devoglobalfact WHERE F_ClaDoc=?;";

        try {
            con.conectar();
            ps = con.getConn().prepareStatement(query);
            ps.setString(1, Folio);
            rs = ps.executeQuery();
            if (rs.next()) {
                Contar = rs.getInt(1);
            }
            if (Contar > 0) {
                Consulta2 = "SELECT F_IndDev FROM tb_indice;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    IndDev = rs2.getInt(1);
                }
                ps2.clearParameters();

                Consulta = "SELECT F_IdFact,F_CantDevo FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rsD = ps.executeQuery();
                while (rsD.next()) {
                    IdFact = rsD.getInt(1);
                    CantDevo = rsD.getInt(2);

                    Consulta2 = "SELECT F_ClaCli, F_ClaPro, F_CantReq, F_CantSur, F_Costo, F_Iva, F_Monto, F_Lote, F_FecEnt, F_Ubicacion, F_Proyecto, F_Contrato, F_OC, F_Cause FROM tb_factura WHERE F_IdFact = ? AND F_ClaDoc = ?;";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        ClaCli = rs2.getString(1);
                        ClaPro = rs2.getString(2);
                        CantReq = rs2.getInt(3);
                        CantSur = rs2.getInt(4);
                        Costo = rs2.getDouble(5);
                        Iva = rs2.getDouble(6);
                        Monto = rs2.getDouble(7);
                        Lote = rs2.getInt(8);
                        FecEnt = rs2.getString(9);
                        Ubicacion = rs2.getString(10);
                        Proyecto = rs2.getInt(11);
                        Contrato = rs2.getString(12);
                        OC = rs2.getString(13);
                        Cause = rs2.getString(14);
                    }

                    ps2.clearParameters();
                    if (Concepto.equals("4")) {
                        UbicaDes = Ubicacion;
                    } else {
                        UbicaDes = "NUEVA_DEVOLUCION";
                    }
                    DevoCant = CantSur - CantDevo;
                    if (DevoCant == 0) {

                        Consulta = "UPDATE tb_factura SET F_StsFact=?,F_Obs=?,F_DocAnt=? WHERE F_IdFact=? AND F_ClaDoc=?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, "C");
                        ps.setString(2, Obs);
                        ps.setString(3, DocAnt);
                        ps.setInt(4, IdFact);
                        ps.setString(5, Folio);
                        ps.execute();
                        ps.clearParameters();

                        Consulta = "SELECT F_IdLote, F_ExiLot, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, ClaPro);
                        ps.setInt(2, Lote);
                        ps.setString(3, UbicaDes);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            IdLote = rs.getInt(1);
                            ExiLot = rs.getInt(2);
                            ClaOrg = rs.getString(3);
                            ProyectoL = rs.getInt(4);
                        }
                        ps.clearParameters();

                        if (IdLote > 0) {

                            Total = ExiLot + CantDevo;
                            Consulta2 = "UPDATE tb_lote SET F_ExiLot = ? WHERE F_IdLote = ? AND F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ? AND F_Proyecto = ?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setInt(1, Total);
                            ps2.setInt(2, IdLote);
                            ps2.setString(3, ClaPro);
                            ps2.setInt(4, Lote);
                            ps2.setString(5, UbicaDes);
                            ps2.setInt(6, ProyectoL);
                            ps2.execute();
                            ps2.clearParameters();

                        } else {

                            Consulta = "SELECT F_IdLote, F_ExiLot, F_ClaLot, F_FecCad, F_FecFab, F_Cb, F_ClaMar, F_Origen, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, ClaPro);
                            ps.setInt(2, Lote);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                IdLote1 = rs.getInt(1);
                                ExiLot = rs.getInt(2);
                                ClaLot = rs.getString(3);
                                FecCad = rs.getString(4);
                                FecFab = rs.getString(5);
                                Cb = rs.getString(6);
                                ClaMar = rs.getString(7);
                                Origen = rs.getString(8);
                                ClaOrg = rs.getString(9);
                                ProyectoL = rs.getInt(10);
                            }
                            ps.clearParameters();

                            if (IdLote1 > 0) {
                                Consulta2 = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,'131',?);";
                                ps2 = con.getConn().prepareStatement(Consulta2);
                                ps2.setString(1, ClaPro);
                                ps2.setString(2, ClaLot);
                                ps2.setString(3, FecCad);
                                ps2.setInt(4, CantDevo);
                                ps2.setInt(5, Lote);
                                ps2.setString(6, ClaOrg);
                                ps2.setString(7, UbicaDes);
                                ps2.setString(8, FecFab);
                                ps2.setString(9, Cb);
                                ps2.setString(10, ClaMar);
                                ps2.setString(11, Origen);
                                ps2.setString(12, ClaOrg);
                                ps2.setInt(13, ProyectoL);
                                ps2.execute();
                                ps2.clearParameters();

                                IdLote1 = 0;
                            }

                        }

                        Consulta2 = "INSERT INTO tb_movinv VALUES(0,CURDATE(),?,?,?,?,?,?,'1',?,?,?,CURTIME(),?,'');";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setInt(1, IndDev);
                        ps2.setString(2, Concepto);
                        ps2.setString(3, ClaPro);
                        ps2.setInt(4, CantDevo);
                        ps2.setDouble(5, Costo);
                        ps2.setDouble(6, Monto);
                        ps2.setInt(7, Lote);
                        ps2.setString(8, UbicaDes);
                        ps2.setString(9, ClaOrg);
                        ps2.setString(10, Usuario);
                        ps2.execute();
                        ps2.clearParameters();

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;
                    } else if (DevoCant > 0) {

                        if (Iva > 0) {
                            IvaDeVo = 0.16;
                        } else {
                            IvaDeVo = 0.00;
                        }
                        IvaTot = ((DevoCant * Costo) * IvaDeVo);
                        Monto = (DevoCant * Costo) + IvaTot;

                        IvaTotDev = ((CantDevo * Costo) * IvaDeVo);
                        MontoDev = ((CantDevo * Costo) * IvaTotDev);

                        Consulta = "UPDATE tb_factura SET F_CantSur=?,F_Iva=?,F_Monto=? WHERE F_IdFact=? AND F_ClaDoc=?;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setInt(1, DevoCant);
                        ps.setDouble(2, IvaTot);
                        ps.setDouble(3, Monto);
                        ps.setInt(4, IdFact);
                        ps.setString(5, Folio);
                        ps.execute();
                        ps.clearParameters();

                        Consulta2 = "INSERT INTO tb_factura VALUES(0,?,?,'C',CURDATE(),?,'0',?,?,?,?,?,?,CURTIME(),?,?,?,?,?,?,?,?);";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, Folio);
                        ps2.setString(2, ClaCli);
                        ps2.setString(3, ClaPro);
                        ps2.setInt(4, CantDevo);
                        ps2.setDouble(5, Costo);
                        ps2.setDouble(6, IvaTotDev);
                        ps2.setDouble(7, MontoDev);
                        ps2.setInt(8, Lote);
                        ps2.setString(9, FecEnt);
                        ps2.setString(10, Usuario);
                        ps2.setString(11, Ubicacion);
                        ps2.setString(12, Obs);
                        ps2.setString(13, DocAnt);
                        ps2.setInt(14, Proyecto);
                        ps2.setString(15, Contrato);
                        ps2.setString(16, OC);
                        ps2.setString(17, Cause);
                        ps2.execute();
                        ps2.clearParameters();

                        Consulta2 = "SELECT F_IdLote, F_ExiLot, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ?;";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, ClaPro);
                        ps2.setInt(2, Lote);
                        ps2.setString(3, UbicaDes);
                        rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            IdLote = rs2.getInt(1);
                            ExiLot = rs2.getInt(2);
                            ClaOrg = rs2.getString(3);
                            ProyectoL = rs2.getInt(4);
                        }
                        ps2.clearParameters();

                        if (IdLote > 0) {
                            Total = ExiLot + CantDevo;
                            Consulta = "UPDATE tb_lote SET F_ExiLot = ? WHERE F_IdLote = ? AND F_ClaPro = ? AND F_FolLot = ? AND F_Ubica = ? AND F_Proyecto = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, Total);
                            ps.setInt(2, IdLote);
                            ps.setString(3, ClaPro);
                            ps.setInt(4, Lote);
                            ps.setString(5, UbicaDes);
                            ps.setInt(6, ProyectoL);
                            ps.execute();
                            ps.clearParameters();

                        } else {

                            Consulta2 = "SELECT F_IdLote, F_ExiLot, F_ClaLot, F_FecCad, F_FecFab, F_Cb, F_ClaMar, F_Origen, F_ClaOrg, F_Proyecto FROM tb_lote WHERE F_ClaPro = ? AND F_FolLot = ?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setString(1, ClaPro);
                            ps2.setInt(2, Lote);
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                IdLote1 = rs2.getInt(1);
                                ExiLot = rs2.getInt(2);
                                ClaLot = rs2.getString(3);
                                FecCad = rs2.getString(4);
                                FecFab = rs2.getString(5);
                                Cb = rs2.getString(6);
                                ClaMar = rs2.getString(7);
                                Origen = rs2.getString(8);
                                ClaOrg = rs2.getString(9);
                                ProyectoL = rs2.getInt(10);
                            }
                            ps2.clearParameters();

                            if (IdLote1 > 0) {
                                Consulta = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,'131',?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setString(1, ClaPro);
                                ps.setString(2, ClaLot);
                                ps.setString(3, FecCad);
                                ps.setInt(4, CantDevo);
                                ps.setInt(5, Lote);
                                ps.setString(6, ClaOrg);
                                ps.setString(7, UbicaDes);
                                ps.setString(8, FecFab);
                                ps.setString(9, Cb);
                                ps.setString(10, ClaMar);
                                ps.setString(11, Origen);
                                ps.setString(12, ClaOrg);
                                ps.setInt(13, ProyectoL);
                                ps.execute();
                                ps.clearParameters();

                                IdLote1 = 0;
                            }
                        }

                        Consulta = "INSERT INTO tb_movinv VALUES(0,CURDATE(),?,?,?,?,?,?,'1',?,?,?,CURTIME(),?,'');";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setInt(1, IndDev);
                        ps.setString(2, Concepto);
                        ps.setString(3, ClaPro);
                        ps.setInt(4, CantDevo);
                        ps.setDouble(5, Costo);
                        ps.setDouble(6, MontoDev);
                        ps.setInt(7, Lote);
                        ps.setString(8, UbicaDes);
                        ps.setString(9, ClaOrg);
                        ps.setString(10, Usuario);
                        ps.execute();
                        ps.clearParameters();

                        IdLote = 0;
                        Total = 0;
                        ExiLot = 0;
                        CantDevo = 0;
                    }

                    Consulta2 = "DELETE FROM tb_devoglobalfact WHERE F_IdFact=? AND F_ClaDoc=?;";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setInt(1, IdFact);
                    ps2.setString(2, Folio);
                    ps2.execute();
                    ps2.clearParameters();

                    int Existencia = 0;
                    Consulta = "SELECT SUM(F_CantSur) FROM tb_factura WHERE F_ClaDoc=? AND F_StsFact='A';";
                    ps = con.getConn().prepareStatement(Consulta);
                    ps.setString(1, Folio);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Existencia = rs.getInt(1);
                    }
                    ps.clearParameters();

                    if (Existencia == 0) {
                        if (DocAnt.equals("0")) {
                            Consulta2 = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=? AND F_DocAnt !=1;";
                        } else {
                            Consulta2 = "UPDATE tb_factura SET F_StsFact=?,F_DocAnt=? WHERE F_ClaDoc=?;";
                        }
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, "C");
                        ps2.setString(2, DocAnt);
                        ps2.setString(3, Folio);
                        ps2.execute();
                        ps2.clearParameters();
                    }
                    Existencia = 0;

                }

                Consulta = "INSERT INTO tb_devoluciones VALUES(CURRENT_TIMESTAMP(),?,?,?,?,?,0);";
                 System.out.println("insert devo" + Consulta);
                ps = con.getConn().prepareStatement(Consulta);
                ps.setInt(1, IndDev);
                ps.setString(2, Folio);
                ps.setString(3, Obs);
                ps.setString(4, Usuario);
                ps.setInt(5, Proyecto);
                ps.execute();
                ps.clearParameters();

                Consulta2 = "UPDATE tb_indice SET F_IndDev=?";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setInt(1, IndDev + 1);
                ps2.execute();
                ps2.clearParameters();
                System.out.println("actualizando indice");
                jsonObj = new JSONObject();
                jsonObj.put("Validado", "Validado");
                jsonArray.add(jsonObj);

//////abasto web
                psActFact = con.getConn().prepareStatement(ValidaAbasto);
                psActFact.setString(1, Proyectos);
                psActFact.setString(2, Folio);
                rs2 = psActFact.executeQuery();

                if (rs2.next()) {
                    Cuenta = rs2.getInt(1);
                }

                psActFact.clearParameters();

                if (Cuenta == 0) {
                    psActFact = con.getConn().prepareStatement(queryElimina);
                    psActFact.setString(1, Proyectos);
                    psActFact.setString(2, Folio);
                    psActFact.execute();

                    psActFact.clearParameters();
                    psActFact = con.getConn().prepareStatement(queryDatosCsV);
                    psActFact.setString(1, Proyectos);
                    psActFact.setString(2, Folio);
                    rs2 = psActFact.executeQuery();
                    while (rs2.next()) {

                        factorEmpaque = 1;
                        int folLot = rs2.getInt("LOTE");
                        PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                        psfe.setInt(1, folLot);
                        ResultSet rsfe = psfe.executeQuery();
                        if (rsfe.next()) {
                            factorEmpaque = rsfe.getInt("factor");
                        }
                        ps.clearParameters();

                        ps = con.getConn().prepareStatement(queryInserta);
                        ps.setString(1, rs2.getString(1));
                        ps.setString(2, rs2.getString(2));
                        ps.setString(3, rs2.getString(3));
                        ps.setString(4, rs2.getString(4));
                        ps.setString(5, rs2.getString(5));
                        ps.setString(6, rs2.getString(6));
                        ps.setString(7, rs2.getString(7));
                        ps.setString(8, rs2.getString(8));
                        ps.setInt(9, rs2.getInt(12));
                        ps.setString(10, rs2.getString(10));
                        ps.setString(11, Usuario);
                        ps.setInt(12, factorEmpaque);
                        System.out.println("fact factor de empaque" + ps);
                        ps.execute();

                    }
                } else {

                    psActFact = con.getConn().prepareStatement(queryElimina);
                    psActFact.setString(1, Proyectos);
                    psActFact.setString(2, Folio);
                    psActFact.execute();

                    psActFact.clearParameters();
                    psActFact = con.getConn().prepareStatement(queryDatosCsV);
                    psActFact.setString(1, Proyectos);
                    psActFact.setString(2, Folio);
                    rs2 = psActFact.executeQuery();
                    while (rs2.next()) {

                        factorEmpaque = 1;
                        int folLot = rs2.getInt("LOTE");
                        PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
                        psfe.setInt(1, folLot);
                        ResultSet rsfe = psfe.executeQuery();
                        if (rsfe.next()) {
                            factorEmpaque = rsfe.getInt("factor");
                        }
                        ps.clearParameters();

                        ps = con.getConn().prepareStatement(queryInserta);
                        ps.setString(1, rs2.getString(1));
                        ps.setString(2, rs2.getString(2));
                        ps.setString(3, rs2.getString(3));
                        ps.setString(4, rs2.getString(4));
                        ps.setString(5, rs2.getString(5));
                        ps.setString(6, rs2.getString(6));
                        ps.setString(7, rs2.getString(7));
                        ps.setString(8, rs2.getString(8));
                        ps.setInt(9, rs2.getInt(12));
                        ps.setString(10, rs2.getString(10));
                        ps.setString(11, Usuario);
                        ps.setInt(12, factorEmpaque);
                        System.out.println("fact factor de empaque" + ps);
                        ps.execute();

                    }
                }
            } else {
                jsonObj = new JSONObject();
                jsonObj.put("Validado", "NoValidado");
                jsonArray.add(jsonObj);
            }
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ConsultaDevoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonArray;
    }

}
