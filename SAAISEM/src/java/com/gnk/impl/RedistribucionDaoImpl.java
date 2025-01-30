/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.impl;

import com.gnk.dao.RedistribucionDao;
import com.gnk.model.RedistribucionModel;
import conn.ConectionDBTrans;
import conn.ConectionDB_SQLServer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Correo.CorreoConfirmaAT;
//import Correo.CorreoConfirmaRemision;

/**
 * Class Implementación de RedistribucionDao (Redistribución ubicaciones
 * transaccional)
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class RedistribucionDaoImpl implements RedistribucionDao {

    DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
    DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
    DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");

    public static String VALIDA_UBICACION = "SELECT COUNT(F_ClaUbi) FROM tb_ubica WHERE F_ClaUbi = ?;";
    public static final String INSERTAR_KARDEX = "INSERT INTO tb_movinv VALUES(0,CURDATE(),0,1000,?,?,'0.00','0.00',?,?,?,?,CURTIME(),?,'');";
    public static String BUSCA_LOTE = "SELECT * FROM tb_lote AS l INNER JOIN tb_proyectos AS p ON l.F_Proyecto = p.F_Id WHERE F_IdLote = ?;";
    public static final String INSERTAR_LOTE = "INSERT INTO tb_lote VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
   // public static String BUSCA_DATOLOTE = "SELECT F_ExiLot, F_FolLot, p.F_DesProy FROM tb_lote WHERE F_FolLot =? AND F_Ubica =? AND F_ClaPrv =? AND F_Proyecto =?;";
    public static String BUSCA_DATOLOTE = "SELECT l.F_ExiLot, l.F_FolLot, p.F_DesProy, l.F_ClaPro FROM tb_lote AS l INNER JOIN tb_proyectos AS p ON l.F_Proyecto = p.F_Id WHERE l.F_FolLot =? AND l.F_Ubica =? AND l.F_ClaPrv =? AND l.F_Proyecto =?;";
   
    
    private static final String ACTUALIZAR_TB_LOTE = "UPDATE tb_lote SET F_ExiLot =? WHERE F_FolLot =? AND F_Ubica =? AND F_ClaPrv =? AND F_Proyecto =?;";
    private static final String IMP_ORDINI = "insert into IMP_ORDINI values (?,'A','',?,'V','','1','P');";
    private static final String IMP_ORDINI_RIGHE = "insert into IMP_ORDINI_RIGHE values(?,'',?,?,'1',?,?,?,'');";
    private static final String ACTUALIZA_ORDINI = "update IMP_ORDINI set ord_p='' where ord_p='P' and ord_ordine= ?;";
  //  private static final String BusquedaAT = "";
    
    
    private final ConectionDBTrans con = new ConectionDBTrans();
    private final ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
    
    CorreoConfirmaAT correoConfirmaAT = new CorreoConfirmaAT();
    private ResultSet rs;
    private PreparedStatement psValidaUbica;
    private PreparedStatement psBuscaLote;
    private PreparedStatement psImpOrdini;
    private PreparedStatement psImpOrdiniRig;
    private PreparedStatement psActImpOrdini;
    private PreparedStatement psBuscaDatoLote;
    private PreparedStatement psINSERKARDEX;
    private PreparedStatement psINSERKARDEX2;
    private PreparedStatement psACTUALIZALOTE;
    private PreparedStatement psACTUALIZALOTE2;
    private String query;
//    private String claveAT; 

    @Override
    public boolean RedistribucionUbica(String Ubicacion, String IdLote, int CantMov, String Usuario, String Origen) {

        boolean save = false;
        int ContarUbi = 0;
        int Diferencias = 0;
        int ExiLot = 0;
        int TotalExi = 0;
        int  CantMovat = 0;
        String FolLot = "", Time = "", Fecha = "", Dato1 = "", Dato2 = "",  claveAT = "", Proy = "", LoteDesc = "", UbicaAn= "", Cadu="";
        String AOrigen = "" ;       
        int hora, minutos, segundos;

        Date FechaCad = null;
        Date fecha = new Date();
        Fecha = new SimpleDateFormat("yyyyMMdd").format(fecha);

        Calendar calendario = new GregorianCalendar();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        Time = hora + ":" + minutos + ":" + segundos;

        List<RedistribucionModel> Redist = new ArrayList<>();
        try {
            con.conectar();
            con.getConn().setAutoCommit(false);
            //System.out.println("Origen tran: "+Origen);

            System.out.println("Usuarioif: "+Usuario);
            if (!Usuario.equals("ICuellarB") && !Usuario.equals("RCuellarB")) {
                if (Ubicacion.equals("NUEVATMP") || Ubicacion.equals("CONTROLADOTMP") || Ubicacion.equals("VACUNASTMP") || Ubicacion.equals("GNKVACUNAS") || Ubicacion.equals("GNKCONTROLADO") || Ubicacion.equals("MERE-CTRL") || Ubicacion.equals("NUEVAMERE-CTRL") || Ubicacion.equals("LOGISTICA_CTRL")) {
                    ContarUbi = 0;
                } else {
                psValidaUbica = con.getConn().prepareStatement(VALIDA_UBICACION);
                psValidaUbica.setString(1, Ubicacion);
                rs = psValidaUbica.executeQuery();

                    if (rs.next()) {
                        ContarUbi = rs.getInt(1);
                    }
                }
            } else 
            //            if (Ubicacion.equals("AF1N") || Ubicacion.equals("APE1N") || Ubicacion.equals("REDFRIA1N") || Ubicacion.equals("CONTROLADO1N")) {
            //                ContarUbi = 0;
            //            } else
            {
                psValidaUbica = con.getConn().prepareStatement(VALIDA_UBICACION);
                psValidaUbica.setString(1, Ubicacion);
                rs = psValidaUbica.executeQuery();

                if (rs.next()) {
                    ContarUbi = rs.getInt(1);
                }
            }
            System.out.println("Ubicacion: " + Ubicacion);
            System.out.println("ContarUbi: " + ContarUbi);

            if (ContarUbi > 0) {
                psBuscaLote = con.getConn().prepareStatement(BUSCA_LOTE);
                psBuscaLote.setString(1, IdLote);
                rs = psBuscaLote.executeQuery();
                while (rs.next()) {
                    RedistribucionModel redistribucion = new RedistribucionModel();
                    redistribucion.setF_ClaPro(rs.getString(2));
                    redistribucion.setF_ClaLot(rs.getString(3));
                    redistribucion.setF_FecCad(rs.getString(4));
                    redistribucion.setF_ExiLot(rs.getInt(5));
                    redistribucion.setF_FolLot(rs.getInt(6));
                    redistribucion.setF_ClaOrg(rs.getString(7));
                    redistribucion.setF_Ubica(rs.getString(8));
                    redistribucion.setF_FecFab(rs.getString(9));
                    redistribucion.setF_Cb(rs.getString(10));
                    redistribucion.setF_ClaMar(rs.getString(11));
                    redistribucion.setF_Origen(rs.getString(12));
                    redistribucion.setF_ClaPrv(rs.getString(13));
                    redistribucion.setF_UniMed(rs.getString(14));
                    redistribucion.setF_Proyecto(rs.getInt(15));
                    Diferencias = rs.getInt(5) - CantMov;
                    Redist.add(redistribucion);
                    claveAT =  rs.getString(2);
                      LoteDesc = rs.getString(3);
                      Proy = rs.getString("F_DesProy");
                      Cadu = rs.getString(4);
                      Origen = rs.getString(12);
                }
               
                
                psValidaUbica.clearParameters();
                psValidaUbica.close();
                
                if(Diferencias >= 0){
            

                for (RedistribucionModel r : Redist) {
                    psINSERKARDEX = con.getConn().prepareStatement(INSERTAR_KARDEX);
                    psINSERKARDEX.setString(1, r.getF_ClaPro());
                    psINSERKARDEX.setInt(2, CantMov);
                    psINSERKARDEX.setInt(3, -1);
                    psINSERKARDEX.setInt(4, r.getF_FolLot());
                    psINSERKARDEX.setString(5, r.getF_Ubica());
                    psINSERKARDEX.setString(6, r.getF_ClaOrg());
                    psINSERKARDEX.setString(7, Usuario);
                      UbicaAn = r.getF_Ubica();
                      
                    psINSERKARDEX2 = con.getConn().prepareStatement(INSERTAR_KARDEX);
                    psINSERKARDEX2.setString(1, r.getF_ClaPro());
                    psINSERKARDEX2.setInt(2, CantMov);
                    psINSERKARDEX2.setInt(3, 1);
                    psINSERKARDEX2.setInt(4, r.getF_FolLot());
                    psINSERKARDEX2.setString(5, Ubicacion);
                    psINSERKARDEX2.setString(6, r.getF_ClaOrg());
                    psINSERKARDEX2.setString(7, Usuario);

                    psINSERKARDEX.execute();
                    psINSERKARDEX.clearParameters();
                    psINSERKARDEX2.execute();
                    psINSERKARDEX2.clearParameters();

                    psBuscaDatoLote = con.getConn().prepareStatement(BUSCA_DATOLOTE);
                    psBuscaDatoLote.setInt(1, r.getF_FolLot());
                    psBuscaDatoLote.setString(2, Ubicacion);
                    psBuscaDatoLote.setString(3, r.getF_ClaPrv());
                    psBuscaDatoLote.setInt(4, r.getF_Proyecto());
                    rs = psBuscaDatoLote.executeQuery();
                    if (rs.next()) {
                        ExiLot = rs.getInt(1);
                        FolLot = rs.getString(2);
//                        Proy = rs.getString(3);
//                        claveAT = rs.getString(4);
                    }
                    System.out.println(Proy);
                    if (FolLot != "") {
                        TotalExi = ExiLot + CantMov;

                        psACTUALIZALOTE2 = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                        psACTUALIZALOTE2.setInt(1, TotalExi);
                        psACTUALIZALOTE2.setInt(2, r.getF_FolLot());
                        psACTUALIZALOTE2.setString(3, Ubicacion);
                        psACTUALIZALOTE2.setString(4, r.getF_ClaPrv());
                        psACTUALIZALOTE2.setInt(5, r.getF_Proyecto());
                    } else {
                        psACTUALIZALOTE2 = con.getConn().prepareStatement(INSERTAR_LOTE);
                        psACTUALIZALOTE2.setString(1, r.getF_ClaPro());
                        psACTUALIZALOTE2.setString(2, r.getF_ClaLot());
                        psACTUALIZALOTE2.setString(3, r.getF_FecCad());
                        psACTUALIZALOTE2.setInt(4, CantMov);
                        psACTUALIZALOTE2.setInt(5, r.getF_FolLot());
                        psACTUALIZALOTE2.setString(6, r.getF_ClaOrg());
                        psACTUALIZALOTE2.setString(7, Ubicacion);
                        psACTUALIZALOTE2.setString(8, r.getF_FecFab());
                        psACTUALIZALOTE2.setString(9, r.getF_Cb());
                        psACTUALIZALOTE2.setString(10, r.getF_ClaMar());
                        psACTUALIZALOTE2.setString(11, r.getF_Origen());
                        psACTUALIZALOTE2.setString(12, r.getF_ClaPrv());
                        psACTUALIZALOTE2.setString(13, r.getF_UniMed());
                        psACTUALIZALOTE2.setInt(14, r.getF_Proyecto());
                    }
                    
                   

                    psACTUALIZALOTE2.execute();
                    psACTUALIZALOTE2.clearParameters();

                    psACTUALIZALOTE = con.getConn().prepareStatement(ACTUALIZAR_TB_LOTE);
                    psACTUALIZALOTE.setInt(1, Diferencias);
                    psACTUALIZALOTE.setInt(2, r.getF_FolLot());
                    psACTUALIZALOTE.setString(3, r.getF_Ubica());
                    psACTUALIZALOTE.setString(4, r.getF_ClaPrv());
                    psACTUALIZALOTE.setInt(5, r.getF_Proyecto());

                    psACTUALIZALOTE.execute();
                    psACTUALIZALOTE.clearParameters();

                    if ((Ubicacion.equals("MODULA")) || (Ubicacion.equals("MODULA2"))) {
                        try {
                            conModula.conectar();
                            conModula.getConn().setAutoCommit(false);

                            Dato1 = "R" + r.getF_ClaPro() + Time;

                            try {
                                Dato2 = df2.format(df3.parse(r.getF_FecCad()));
                            } catch (ParseException ex) {
                                Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            System.out.println("Dat1:" + Dato1);
                            System.out.println("Dat2:" + Dato2);
                            System.out.println("fECHA:" + Fecha);

                            psImpOrdini = conModula.getConn().prepareStatement(IMP_ORDINI);
                            psImpOrdini.setString(1, Dato1);
                            psImpOrdini.setString(2, Fecha);
                            System.out.println("psImpOrdini: " + psImpOrdini);

                            psImpOrdiniRig = conModula.getConn().prepareStatement(IMP_ORDINI_RIGHE);
                            psImpOrdiniRig.setString(1, Dato1);
                            psImpOrdiniRig.setString(2, r.getF_ClaPro());
                            psImpOrdiniRig.setString(3, r.getF_ClaLot());
                            psImpOrdiniRig.setInt(4, CantMov);
                            psImpOrdiniRig.setString(5, r.getF_Cb());
                            psImpOrdiniRig.setString(6, Dato2);
                            System.out.println("psImpOrdiniRig: " + psImpOrdiniRig);

                            psActImpOrdini = conModula.getConn().prepareStatement(ACTUALIZA_ORDINI);
                            psActImpOrdini.setString(1, Dato1);
                            System.out.println("psActImpOrdini: " + psActImpOrdini);

                            psImpOrdini.execute();
                            psImpOrdini.clearParameters();
                            psImpOrdiniRig.execute();
                            psImpOrdiniRig.clearParameters();
                            psActImpOrdini.execute();
                            psActImpOrdini.clearParameters();

                            conModula.getConn().commit();

                            conModula.cierraConexion();
                        } catch (SQLException ex) {
                            Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
                            try {
                                conModula.getConn().rollback();
                            } catch (SQLException ex1) {
                                Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        } finally {
                            try {
                                conModula.cierraConexion();
                            } catch (Exception ex) {
                                Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
                            }
                        }
                    }
                    CantMovat = CantMov;
                    TotalExi = 0;
                    ExiLot = 0;
                    CantMov = 0;
                    FolLot = "";

                }//for

                }//Diferetente

                save = true;
                con.getConn().commit();
             
           
//                  if(Ubicacion.contains("AT-acopi-controlado-merma-ape-redfria")){                                                                                                               /*Ubicacion de donde sale*/        
                  if(Ubicacion.equals("AT") || Ubicacion.equals("A0T") || Ubicacion.equals("ACOPIO") || Ubicacion.equals("ONCO") || Ubicacion.equals("REDFRIA") || Ubicacion.equals("CONTROLADO") || Ubicacion.equals("APE") || Ubicacion.equals("MERMA") || Ubicacion.equals("CADUCADOS")  || UbicaAn.equals("CADUCADOS")  || UbicaAn.equals("AT") || UbicaAn.equals("A0T") || UbicaAn.equals("ACOPIO") || UbicaAn.equals("ONCO") || UbicaAn.equals("REDFRIA")|| UbicaAn.equals("CONTROLADO") || UbicaAn.equals("APE") || UbicaAn.equals("MERMA")){ 
//                       correoConfirmaAT.enviaCorreoAT(claveAT,CantMovat,Ubicacion,Usuario,Proy,LoteDesc,UbicaAn); 
                         correoConfirmaAT.enviaCorreoAT(claveAT,CantMovat,Ubicacion,Usuario,Proy,LoteDesc,UbicaAn, Cadu); 
                    }
                
                return save;
            }
            ContarUbi = 0;
            con.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex.getSQLState()), ex);
            try {
                con.getConn().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(RedistribucionDaoImpl.class.getName()).log(Level.SEVERE, String.format("m: %s, sql: %s", ex.getMessage(), ex), ex);
            }
        }

        return save;

    }

}
