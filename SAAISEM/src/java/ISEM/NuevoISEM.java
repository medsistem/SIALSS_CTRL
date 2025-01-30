/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISEM;

import conn.ConectionDB;
import java.sql.ResultSet;

/**
 *
 * @author Americo
 */
public class NuevoISEM {

    ConectionDB con = new ConectionDB();
    //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();

    public int Guardar(String nombre) {
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String FolioCompra = "";
        int F_IndCom = 0;
        try {
            con.conectar();
            //consql.conectar();
            String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
            String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Proyectos="";
            String F_FecApl ="", F_Hora="";
            int ExiLot = 0, cantidad = 0, sumalote = 0, FolLot = 0, FolioLot = 0, F_IndComT = 0, F_Origen = 0, FolMov = 0, FolioMovi = 0, FolMovSQL = 0, FolioMoviSQL = 0;
            double compraB = 0.0;

            //VARIABLES SQL SERVER
            String FolioLoteSQL = "", ExiLoteSQL = "", F_Numero = "", F_FecCadSQL = "", ExisMed = "";
            int sumaloteSQL = 0, ExiLotSQL = 0, cantidadSQL = 0, Contar = 0, FolLotSQL = 0, FolioLotSQL = 0, F_IndComTSQL = 0, F_IndComSQL = 0;
            double cantidadTSQL = 0.0, TotalExi = 0.0, ExisMedTSQL = 0.0;
            //CONSULTA MYSQL INDICE DE COMPRA
            ResultSet rset_IndF = con.consulta("SELECT F_IndCom FROM tb_indice");
            while (rset_IndF.next()) {
                F_IndCom = Integer.parseInt(rset_IndF.getString("F_IndCom"));
            }
            F_IndComT = F_IndCom + 1;
            con.actualizar("update tb_indice set F_IndCom='" + F_IndComT + "'");
            //FIN MYSQL

            //CONSULTA SQL INDICE DE COMPRA
            /*ResultSet rset_IndFSQL = consql.consulta("SELECT F_IC FROM TB_Indice");
             while (rset_IndFSQL.next()) {
             F_Numero = rset_IndFSQL.getString("F_IC");
             F_IndComSQL = Integer.parseInt(rset_IndFSQL.getString("F_IC"));
             }
             F_IndComTSQL = F_IndComSQL + 1;
             consql.actualizar("update TB_Indice set F_IC='" + F_IndComTSQL + "'");
             */
            Contar = F_Numero.length();

            if (Contar == 1) {
                FolioCompra = "      " + F_Numero;
            } else if (Contar == 2) {
                FolioCompra = "     " + F_Numero;
            } else if (Contar == 3) {
                FolioCompra = "    " + F_Numero;
            } else if (Contar == 4) {
                FolioCompra = "   " + F_Numero;
            } else if (Contar == 5) {
                FolioCompra = "  " + F_Numero;
            } else if (Contar == 6) {
                FolioCompra = " " + F_Numero;
            } else if (Contar >= 7) {
                FolioCompra = F_Numero;
            }

            /*
             *Consulta a compra temporal (MySQL)
             *con base en fecha y usuario
             */
            ResultSet rsetDatos = con.consulta("SELECT F_FecApl, F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Proyectos FROM tb_compratemp WHERE F_User='" + nombre + "' AND F_FecApl=CURDATE()");
            while (rsetDatos.next()) {
                F_ClaPro = rsetDatos.getString("F_ClaPro");
                F_Lote = rsetDatos.getString("F_Lote").toUpperCase();
                F_FecCad = rsetDatos.getString("F_FecCad");
                F_FecCadSQL = rsetDatos.getString("FECAD");
                F_FecFab = rsetDatos.getString("F_FecFab");
                F_Marca = rsetDatos.getString("F_Marca");
                F_Provee = rsetDatos.getString("F_Provee");
                F_Cb = rsetDatos.getString("F_Cb");
                F_Tarimas = rsetDatos.getString("F_Tarimas");
                F_Caja = rsetDatos.getString("F_Cajas");
                F_Piezas = rsetDatos.getString("F_Pz");
                F_Resto = "0";
                F_Costo = rsetDatos.getString("F_Costo");
                F_ImpTo = rsetDatos.getString("F_ImpTo");
                F_ComTot = rsetDatos.getString("F_ComTot");
                F_FolRemi = rsetDatos.getString("F_FolRemi");
                F_OrdCom = rsetDatos.getString("F_OrdCom");
                F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                F_User = rsetDatos.getString("F_User");
                F_Proyectos = rsetDatos.getString("F_Proyectos");
                byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                F_Obser = (new String(a, "UTF-8")).toUpperCase();

                F_FecApl = rsetDatos.getString("F_FecApl");
                F_Hora = rsetDatos.getString("F_Hora");
                // CONSULTA MYSQL
                /*
                 *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                 */
                ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_ClaOrg='" + F_Origen + "' and F_ClaMar='" + F_Marca + "'");
                while (rsetLote.next()) {
                    //System.out.println(rset.getString("F_FolLot"));
                    FolioLote = rsetLote.getString("F_FolLot");
                }

                if (!(FolioLote.equals(""))) {//Lote existente
                    ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA'");
                    while (rset_fol.next()) {
                        ExiLote = rset_fol.getString("F_ExiLot");
                    }
                    if (!(ExiLote.equals(""))) { //Lote con ubicacion
                        ExiLot = Integer.parseInt(ExiLote);
                        cantidad = Integer.parseInt(F_Piezas);
                        sumalote = ExiLot + cantidad;
                        con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA' and F_Proyecto = '"+F_Proyectos+"'");
                    } else { //Lote sin ubicacion
                        con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                    }
                } else { //Lote Inexistente
                    ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                    while (rset_Ind.next()) {
                        FolioLote = rset_Ind.getString("F_IndLote");
                        FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                        con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                        FolioLot = FolLot + 1;
                        con.actualizar("update tb_indice set F_IndLote='" + FolioLot + "'");
                    }

                }
                //FIN CONSULTA MYSQL

                String F_ClaPrvSQL = "";
                ResultSet rsetNomPro = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + F_Provee + "' ");
                while (rsetNomPro.next()) {
                    /*ResultSet rsetProveeSQL = consql.consulta("select F_ClaPrv from TB_Provee where F_NomPrv = '" + rsetNomPro.getString(1) + "' ");
                     while (rsetProveeSQL.next()) {
                     F_ClaPrvSQL = rsetProveeSQL.getString(1);
                     }*/
                }

                //CONSULTA SQL SERVER
                //ResultSet rsetLoteSQL = consql.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCadSQL + "'");
                /*while (rsetLoteSQL.next()) {
                 FolioLoteSQL = rsetLoteSQL.getString("F_FolLot");
                 }*/
 /*if (!(FolioLoteSQL.equals(""))) {//Lote existente
                 ResultSet rset_folSQL = consql.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLoteSQL + "'");
                 while (rset_folSQL.next()) {
                 ExiLoteSQL = rset_folSQL.getString("F_ExiLot");
                 }
                 ExiLotSQL = (int) Double.parseDouble(ExiLoteSQL);
                 cantidadSQL = Integer.parseInt(F_Piezas);
                 sumaloteSQL = ExiLotSQL + cantidadSQL;
                 consql.actualizar("update tb_lote set F_ExiLot='" + sumaloteSQL + "' where F_FolLot='" + FolioLoteSQL + "'");
                 } else { // Lote inexistente
                 ResultSet rset_IndSQL = consql.consulta("SELECT F_IL FROM tb_indice");
                 while (rset_IndSQL.next()) {
                 FolioLoteSQL = rset_IndSQL.getString("F_IL");
                 FolLotSQL = Integer.parseInt(rset_IndSQL.getString("F_IL"));
                 consql.insertar("insert into tb_lote values ('" + F_Lote + "','" + F_ClaPro + "','" + F_FecCadSQL + "','" + F_Piezas + "','" + F_Costo + "','" + FolioLoteSQL + "','    1','','1','" + F_FecFab + "','0','" + F_Provee + "','0','" + F_Marca + "')");
                 FolioLotSQL = FolLotSQL + 1;
                 consql.actualizar("update tb_indice set F_IL='" + FolioLotSQL + "'");
                 }
                 }*/
                // FIN CONSULTA SQL SERVER
                //CONSULTA INDICE MOVIMIENTO MYSQL
                ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                while (FolioMov.next()) {
                    FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                }
                FolMov = FolioMovi + 1;
                con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                //FIN CONSULTA MYSQL

                // CONSULTA INDICE MOVIMIENTO SQL
                /*cantidadTSQL = Double.parseDouble(F_Piezas);
                 ResultSet FolioMovSQL = consql.consulta("select F_IM from TB_Indice");
                 while (FolioMovSQL.next()) {
                 FolioMoviSQL = Integer.parseInt(FolioMovSQL.getString("F_IM"));
                 }
                 FolMovSQL = FolioMoviSQL + 1;
                 consql.actualizar("update TB_Indice set F_IM='" + FolMovSQL + "'");
                 
                 ResultSet ExisMedSQL = consql.consulta("select F_Existen from TB_Medica where F_ClaPro='" + F_ClaPro + "'");
                 while (ExisMedSQL.next()) {
                 ExisMedTSQL = Double.parseDouble(ExisMedSQL.getString("F_Existen"));
                 }
                 TotalExi = ExisMedTSQL + cantidadTSQL;

                 consql.actualizar("update TB_Medica set F_Existen='" + TotalExi + "' where F_ClaPro='" + F_ClaPro + "'");
                 */
                // FIN CONSULTA SQL
                con.insertar("insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "', 'NUEVA', '" + F_Provee + "',curtime(),'" + F_User + "','') ");
                con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0', '"+nombre+"', '"+ F_FecApl + "', '"+F_Hora+"') ");
                //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioCompra + "','','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "','" + F_ImpTo + "', '" + F_ComTot + "' ,'1', '" + FolioLoteSQL + "', '" + FolioMovi + "','M', '0', '','','','" + F_ClaPrvSQL + "','" + F_User + "') ");
                //consql.insertar("insert into TB_Compra values ('C','" + FolioCompra + "','" + F_ClaPrvSQL + "','A','CD',CONVERT(date,GETDATE()),'', '" + F_ClaPro + "','','','1', '" + F_Piezas + "', '1','" + F_ComTot + "','0','" + F_ComTot + "','" + F_ComTot + "','0', '" + F_ImpTo + "','" + F_ComTot + "', '" + F_Costo + "', '" + FolioLoteSQL + "','D',CONVERT(date,GETDATE()), '" + F_User + "','0','0','','" + F_FolRemi + "','' ) ");
                //consql.insertar("insert into TB_Bitacora values ('" + F_User + "',CONVERT(date,GETDATE()),'COMPRA - MANUAL','REGISTRAR','" + FolioCompra + "','1','COMPRAS') ");

                FolioLote = "";
                FolioLoteSQL = "";
            }

            con.actualizar("delete from tb_compratemp where F_OrdCom = '" + F_OrdCom + "'");
            con.cierraConexion();
            //consql.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return F_IndCom;
    }

    public String insertaObservacionesCompra(String obser) {
        String id = dameIdObser();
        /*
         try {
         consql.conectar();
         try {
         consql.insertar("insert into TB_Obser values ('" + id + "', '" + obser + "')");
         } catch (Exception e) {
         }
         consql.cierraConexion();
         } catch (Exception e) {
         }*/
        return id;
    }

    public void insertaCompraBitacora(String usuario, String modulo, String boton, String folio, String concepto, String obser) {
        /*try {
         consql.conectar();
         try {
         consql.insertar(" INSERT INTO TB_Bitacora(F_BitUsu, F_BitFec, F_BitMod, F_BitAcc, F_BitFol, F_BitCon, F_BitObs) VALUES('" + usuario + "', CONVERT(date,GETDATE()), '" + modulo + "', '" + boton + "', '    " + folio + "', '" + concepto + "', '" + obser + "')");
         } catch (Exception e) {
         }
         consql.cierraConexion();
         } catch (Exception e) {
         }*/
    }

    public String dameIdObser() {
        String idIO = "";
        try {
            /*consql.conectar();
             try {
             ResultSet rset = consql.consulta("select F_IO from TB_Indice");
             while (rset.next()) {
             idIO = rset.getString("F_IO");
             consql.actualizar("update TB_Indice set F_IO = '" + (Integer.parseInt(idIO) + 1) + "' ");
             }
             } catch (Exception e) {
             }
             consql.cierraConexion();*/
        } catch (Exception e) {
        }
        return idIO;
    }

    public void sumaCompraInventario(String clave, String cant) {
        try {
            /*consql.conectar();
             try {
             ResultSet rset = consql.consulta("select F_ClaPro, F_Existen, F_Precio from TB_Medica where F_ClaPro = '" + clave + "' ");
             while (rset.next()) {
             double costo = Double.parseDouble(rset.getString("F_Precio"));
             String exsiten = rset.getString("F_Existen");
             int n_cant = Integer.parseInt(cant) + (int) Double.parseDouble(exsiten);
             double cos_pro = ((costo * n_cant) + (costo * Integer.parseInt(cant))) / (n_cant);
             consql.actualizar("update TB_Medica set F_Existen = '" + n_cant + "', F_CosPro = '" + cos_pro + "' where F_ClaPro = '" + clave + "' ");
             }
             } catch (Exception e) {
             }
             consql.cierraConexion();*/
        } catch (Exception e) {
        }
    }

    public void insertaMovimiento(String cladoc, String clapro, String cant, String costo, double cantcosto, String idLote, String observaciones, String codprov) {
        try {
            /*consql.conectar();
             try {
             consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()), '" + dame7car(cladoc) + "', '" + codprov + "', '1', '" + clapro + "', '" + cant + "', '" + costo + "', '" + costo + "', '" + cantcosto + "', '1', '" + idLote + "', '" + dameidMov() + "', 'M', '" + observaciones + "') ");
             } catch (Exception e) {
             }
             consql.cierraConexion();*/
        } catch (Exception e) {
        }
    }

    public String dameidMov() {
        String idMov = "";
        try {
            /*ResultSet rset = consql.consulta("select F_IM from TB_Indice");
             while (rset.next()) {
             idMov = rset.getString("F_IM");
             consql.actualizar("update TB_Indice set F_IM = '" + (Integer.parseInt(idMov) + 1) + "' ");
             }*/
        } catch (Exception e) {
        }
        return idMov;
    }

    public String dame7car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 7 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
    }

    public String dame5car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 5 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
    }

    public String idLote(String clave, String lote, String fec_cad, String cant, double costo, String origen, String fec_fab) {
        String idLote = "";
        int exi = 0;
        double cos = 0;
        int ban = 0;
        /*try {
         consql.conectar();
         try {
         ResultSet rset = consql.consulta("select F_FolLot, F_ExiLot, F_CosLot from tb_lote where F_ClaPro = '" + clave + "' and F_ClaLot = '" + lote + "' ");
         while (rset.next()) {
         idLote = rset.getString("F_FolLot");
         exi = rset.getInt("F_ExiLot");
         cos = rset.getDouble("F_CosLot");
         ban = 1;
         }
         } catch (SQLException e) {
         }
         if (ban == 0) {
         ResultSet rset = consql.consulta("select F_IL from TB_Indice ");
         while (rset.next()) {
         idLote = rset.getString("F_IL");
         consql.actualizar("update TB_Indice set F_IL = '" + (Integer.parseInt(idLote) + 1) + "' ");
         }
         consql.insertar("insert into tb_lote values ('" + lote + "', '" + clave + "', '" + fec_cad + "', '" + cant + "', '" + costo + "', '" + idLote + "', '" + origen + "', '0000', '" + fec_fab + "') ");
         } else {
         int texi = exi + Integer.parseInt(cant);
         double totcos = cos + costo;
         consql.actualizar("update tb_lote set F_ExiLot = '" + texi + "', F_CosLot = '" + totcos + "' where F_FolLot = '" + idLote + "' ");
         }
         consql.cierraConexion();
         } catch (SQLException e) {
         }*/
        return idLote;
    }
}
