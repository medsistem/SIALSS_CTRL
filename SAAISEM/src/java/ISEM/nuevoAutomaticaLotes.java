/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ISEM;

import Correo.CorreoConfirmaRemision;
import conn.ConectionDB;
//import conn.ConectionDB_Linux;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.gnk.impl.InterfaceSendetoDaoImpl;

public class nuevoAutomaticaLotes extends HttpServlet {

    ConectionDB con = new ConectionDB();

    CorreoConfirmaRemision correoConfirma = new CorreoConfirmaRemision();

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Accion:"+request.getParameter("accion"));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        HttpSession sesion = request.getSession(true);
        String usuarioVal= (String) request.getSession().getAttribute("nombre");
        //AbastoModula modula = new AbastoModula();
        String FolioCompra = "";
        int F_IndCom = 0;
        try {
            if (request.getParameter("accion").equals("EliminarVerifica")) {
                try {
                    con.conectar();
                    try {
                        String query = "delete from tb_compratemp where F_FolRemi = '" + request.getParameter("vRemi") + "' and F_OrdCom = '" + request.getParameter("vOrden") + "' ";
                        System.out.println(query);
                        con.insertar(query);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    con.cierraConexion();
                    sesion.setAttribute("vOrden", "");
                    sesion.setAttribute("vRemi", "");
                    response.sendRedirect("verificarCompraAuto.jsp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (request.getParameter("accion").equals("EliminarVerificaISEM")) {
                try {
                    con.conectar();
                    try {
                        con.insertar("delete from tb_compratempisem where F_FolRemi = '" + request.getParameter("vRemi") + "' and F_OrdCom = '" + request.getParameter("vOrden") + "' ");
                    } catch (Exception e) {
                    }
                    con.cierraConexion();
                    sesion.setAttribute("vOrden", "");
                    sesion.setAttribute("vRemi", "");
                    response.sendRedirect("verificarCompraAutoISEM.jsp");
                } catch (Exception e) {
                }
            }
            
           //////// ///VERIFICA TODA LA OC////////////////////////////////////////////////////////
            if (request.getParameter("accion").equals("GuardarAbiertaVerifica")) {
 String  Ori = "";
                try {
                    con.conectar();
                    System.out.println("Esta entrando a nuevoAtomaticalotes");
                      String Ubicacion = request.getParameter("UbicaN");
                     System.out.println("esto es la ubicacion:" + Ubicacion);
                    ResultSet rsetComTemp = con.consulta("select F_IdCom, F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca, F_Cb from tb_compratemp where F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' ");
                   
while (rsetComTemp.next()) {

                        Calendar c1 = GregorianCalendar.getInstance();
                        String F_FecCadAct = "", F_MarcaAct = "";
                        F_FecCadAct = request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom"));
                        F_MarcaAct = request.getParameter("F_Marca" + rsetComTemp.getString("F_IdCom"));
                        String Tipo = "";
                        //String cadu = df2.format(df3.parse(F_FecCadAct));
                        c1.setTime(df.parse(F_FecCadAct));

                        ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + rsetComTemp.getString("F_ClaPro") + "'");
                        while (rset_medica.next()) {
                            Tipo = rset_medica.getString("F_TipMed");
                            if (Tipo.equals("2504")) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                        }

                        String fecFab = (df.format(c1.getTime()));

                        ResultSet rset2 = con.consulta("select F_ClaMar from tb_marca where F_DesMar = '" + F_MarcaAct + "'");
                        while (rset2.next()) {
                            F_MarcaAct = rset2.getString("F_ClaMar");
                        }

                        //con.insertar("update tb_compratemp set F_Cb = '" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "', F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "'");
                        con.insertar("update tb_compratemp set F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "'");

                        con.insertar("insert into tb_cb values (0,'" + request.getParameter(rsetComTemp.getString("F_Cb") + "','"+ rsetComTemp.getString("F_IdCom")) + "','" + rsetComTemp.getString("F_ClaPro") + "','" + rsetComTemp.getString("F_Lote") + "','" + rsetComTemp.getString("F_FecCad") + "','" + rsetComTemp.getString("F_FecFab") + "','" + rsetComTemp.getString("F_Marca") + "')");
                    }
                    //ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
                    //conModula.conectar();
                    //consql.conectar();
                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Ori = "",F_CajasI="",F_TarimasI = "", F_Pza = "";
                    String F_FecApl="", F_Hora="",F_OrdenSuministro= "", tipo = "";
                    int ExiLot = 0, cantidad = 0, sumalote = 0, FolLot = 0, FolioLot = 0, F_IndComT = 0, F_Origen = 0, FolMov = 0, FolioMovi = 0, FolMovSQL = 0, FolioMoviSQL = 0;
                    double compraB = 0.0;

                    //VARIABLES SQL SERVER
                    String FolioLoteSQL = "", ExiLoteSQL = "", F_Numero = "", F_FecCadSQL = "", ExisMed = "",F_Proyectos="",F_FileFac="",F_FileRemi="",F_FolFac="" ;
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
//                    String querycomtemp = "SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Origen, F_Proyectos,F_FileFac,F_FileRemi,F_FolFac,F_TarimasI,F_CajasI, F_PzaCaja, F_FecApl FROM tb_compratemp WHERE F_OrdCom='" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "') ";
//                    String querycomtemp = "SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Origen, F_Proyectos,F_FileFac,F_FileRemi,F_FolFac,F_TarimasI,F_CajasI, F_PzaCaja, F_FecApl, F_FactorEmpaque, F_OrdenSuministro FROM tb_compratemp WHERE F_OrdCom='" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "') ";                   
                    String querycomtemp = ("SELECT C.F_Hora, C.F_ClaPro, C.F_Lote, C.F_FecCad, DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, C.F_FecFab, C.F_Marca, C.F_Provee, C.F_Cb, C.F_Tarimas, C.F_Cajas, C.F_Pz, C.F_Resto, C.F_Costo, C.F_ImpTo, C.F_ComTot, C.F_FolRemi, C.F_OrdCom, C.F_ClaOrg, C.F_User, C.F_Obser, C.F_Origen, C.F_Proyectos, C.F_FileFac, C.F_FileRemi, C.F_FolFac, C.F_TarimasI, C.F_CajasI, C.F_PzaCaja, C.F_FecApl, C.F_FactorEmpaque, C.F_OrdenSuministro, CASE WHEN C.F_ClaPro = CON.F_ClaPro THEN 'CONTROLADO' WHEN C.F_ClaPro = V.F_ClaPro THEN 'VACUNA' ELSE 'X' END 'Tipo' FROM tb_compratemp AS C LEFT JOIN tb_controlados AS CON ON C.F_ClaPro = CON.F_ClaPro LEFT JOIN tb_vacunas AS V ON C.F_ClaPro = V.F_ClaPro WHERE C.F_OrdCom = '" + request.getParameter("vOrden") + "' AND (C.F_FolRemi = '" + request.getParameter("vRemi") + "' OR C.F_FolFac = '" + request.getParameter("vRemi") + "')");                   
                    System.out.println(querycomtemp);
                    ResultSet rsetDatos = con.consulta(querycomtemp);
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
                        F_Piezas = rsetDatos.getString("F_PzaCaja");
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_Ori = rsetDatos.getString("F_Origen");
                        F_User = rsetDatos.getString("F_User");
                        F_Proyectos = rsetDatos.getString("F_Proyectos");
                        F_FileFac = rsetDatos.getString("F_FileFac");
                        F_FileRemi = rsetDatos.getString("F_FileRemi");
                        F_FolFac = rsetDatos.getString("F_FolFac");
                        F_TarimasI = rsetDatos.getString("F_TarimasI");
                        F_CajasI = rsetDatos.getString("F_CajasI");
                        F_Pza = rsetDatos.getString("F_Pz");
                        F_FecApl = rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        int factorEmpaque = rsetDatos.getInt("F_FactorEmpaque");
                        F_OrdenSuministro = rsetDatos.getString("F_OrdenSuministro");
                        tipo = rsetDatos.getString("Tipo");
                       
                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        Ori = F_Ori;
                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
                        
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_ClaOrg='" + F_Origen + "' and F_ClaMar='" + F_Marca + "' and F_Origen='" + F_Ori + "' and F_Proyecto= '"+F_Proyectos+"'");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }
                        if(tipo.equals("X")){
                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='" + request.getParameter("UbicaN") + "' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Pza);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='" + request.getParameter("UbicaN")+ "' and F_Proyecto= '"+F_Proyectos+"'");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (cantidad) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Pza + "','" + FolioLote + "','" + F_Origen + "','" + request.getParameter("UbicaN") + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "', '" + F_Ori + "','"+F_Origen+"','131',"+F_Proyectos+")");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Pza + "','" + FolioLote + "','" + F_Origen + "','" + request.getParameter("UbicaN") + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','" + F_Ori + "','"+F_Origen+"','131',"+F_Proyectos+")");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                                FolioLot = FolLot + 1;
                                con.actualizar("update tb_indice set F_IndLote='" + FolioLot + "'");
                            }

                        }
                        //FIN CONSULTA MYSQL

                        String F_ClaPrvSQL = "";
                        ResultSet rsetNomPro = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + F_Provee + "' ");

                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                        
                        String descripcion="",uMedida="",queryperdido="", plazo=""; 
                        queryperdido = "select F_Presentacion, F_UniMed, Plazo_Pago from tb_pedido_sialss where F_NoCompra = '" + F_OrdCom + "' and F_Clave = '" + F_ClaPro + "' ";
                        System.out.println(queryperdido);
                        FolioMov = con.consulta(queryperdido);
                        plazo="0";
                        while (FolioMov.next()) {
                            descripcion = FolioMov.getString("F_Presentacion");
                            uMedida = FolioMov.getString("F_UniMed");
                            plazo = FolioMov.getString("Plazo_Pago");
                        }
                        
                        //FIN CONSULTA MYSQL
                        String querymov="insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Pza + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "','" + request.getParameter("UbicaN") + "', '" + F_Provee + "',curtime(),'" + usuarioVal + "','') ";
//                        String querycom="insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(),'"+F_FecCad+"', DATE_ADD(curdate(), INTERVAL "+plazo+" DAY),'"+F_FileFac +"','"+F_FileRemi +"','"+F_FolFac +"','"+F_ClaPro +"','" + F_Pza + "', '" + F_Costo + "', '" + F_Tarimas + "', '" + F_Caja + "','"+F_Piezas+"', '" + F_TarimasI + "', '" + F_CajasI + "','" + F_Resto + "','" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_Marca + "','"+F_FolRemi+"' ,'" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0','"+FolioLote+"','"+F_Proyectos+"','"+descripcion+"','"+uMedida+"',"+F_Ori+", '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"')";
                        String querycom="insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(),'"+F_FecCad+"', DATE_ADD(curdate(), INTERVAL "+plazo+" DAY),'"+F_FileFac +"','"+F_FileRemi +"','"+F_FolFac +"','"+F_ClaPro +"','" + F_Pza + "', '" + F_Costo + "', '" + F_Tarimas + "', '" + F_Caja + "','"+F_Piezas+"', '" + F_TarimasI + "', '" + F_CajasI + "','" + F_Resto + "','" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_Marca + "','"+F_FolRemi+"' ,'" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0','"+FolioLote+"','"+F_Proyectos+"','"+descripcion+"','"+uMedida+"',"+F_Ori+", '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"', "+factorEmpaque+", '"+F_OrdenSuministro+"')";                        
                        String querycom2="INSERT INTO tb_compraregistro (`F_IdCom`, `F_FecApl`, `F_ClaPro`, `F_Lote`, `F_FecCad`, `F_FecFab`, `F_Marca`, `F_Provee`, `F_Cb`, `F_Tarimas`, `F_Cajas`, `F_Pz`, `F_TarimasI`, `F_CajasI`, `F_Resto`, `F_Costo`, `F_ImpTo`, `F_ComTot`, `F_Obser`, `F_FolRemi`, `F_OrdCom`, `F_ClaOrg`, `F_User`, `F_Proyecto`) VALUES ('0', curdate(), '"+ F_ClaPro +"', '" + FolioLote + "', '"+F_FecCad+"', '"+ F_FecFab +"', '"+ F_Marca +"', '" + F_Provee + "', '" + F_Cb + "', '" + F_Tarimas + "', '" + F_Caja + "', '"+F_Pza+"', '" + F_TarimasI + "', '" + F_CajasI + "', '" + F_Resto + "', '" + F_Costo + "', '" + F_ImpTo + "', '" + F_ComTot + "', '" + F_Obser + "', CONCAT( IFNULL('"+F_FolRemi+"', ''), IFNULL('"+F_FolFac +"', '')), '" + F_OrdCom + "', '" + F_Origen + "', '" + F_User + "', '"+F_Proyectos+"')";
                        System.out.println(querymov);
                        con.insertar(querymov);
                        System.out.println(querycom);
                        con.insertar(querycom);
                        System.out.println(querycom2);
                        con.insertar(querycom2);

                        FolioLote = "";
                        FolioLoteSQL = "";
                        String delQuery="delete from tb_compratemp where F_OrdCom = '" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "')";
                        con.actualizar("delete from tb_compratemp where F_OrdCom = '" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "')");
                    }
                        //Ingreso de Controlados y vacunas a la ubicacion NUEVATEMP
                        
                        else { 
                           // String ubicacion = "NUEVATMP";
                                if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='" + request.getParameter("UbicaN") + "' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Pza);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='" + request.getParameter("UbicaN") + "' and F_Proyecto= '"+F_Proyectos+"'");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (cantidad) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Pza + "','" + FolioLote + "','" + F_Origen + "','" + request.getParameter("UbicaN") + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "', '" + F_Ori + "','"+F_Origen+"','131',"+F_Proyectos+")");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Pza + "','" + FolioLote + "','" + F_Origen + "','" + request.getParameter("UbicaN") + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','" + F_Ori + "','"+F_Origen+"','131',"+F_Proyectos+")");
                                if (request.getParameter("UbicaN").equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                                FolioLot = FolLot + 1;
                                con.actualizar("update tb_indice set F_IndLote='" + FolioLot + "'");
                            }

                        }
                        //FIN CONSULTA MYSQL

                        String F_ClaPrvSQL = "";
                        ResultSet rsetNomPro = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + F_Provee + "' ");

                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                        
                        String descripcion="",uMedida="",queryperdido="", plazo=""; 
                        queryperdido = "select F_Presentacion, F_UniMed, Plazo_Pago from tb_pedido_sialss where F_NoCompra = '" + F_OrdCom + "' and F_Clave = '" + F_ClaPro + "' ";
                        System.out.println(queryperdido);
                        FolioMov = con.consulta(queryperdido);
                        plazo="0";
                        while (FolioMov.next()) {
                            descripcion = FolioMov.getString("F_Presentacion");
                            uMedida = FolioMov.getString("F_UniMed");
                            plazo = FolioMov.getString("Plazo_Pago");
                        }
                        
                        //FIN CONSULTA MYSQL
                        String querymov="insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Pza + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "','" + request.getParameter("UbicaN") + "', '" + F_Provee + "',curtime(),'" + F_User + "','') ";
//                        String querycom="insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(),'"+F_FecCad+"', DATE_ADD(curdate(), INTERVAL "+plazo+" DAY),'"+F_FileFac +"','"+F_FileRemi +"','"+F_FolFac +"','"+F_ClaPro +"','" + F_Pza + "', '" + F_Costo + "', '" + F_Tarimas + "', '" + F_Caja + "','"+F_Piezas+"', '" + F_TarimasI + "', '" + F_CajasI + "','" + F_Resto + "','" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_Marca + "','"+F_FolRemi+"' ,'" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0','"+FolioLote+"','"+F_Proyectos+"','"+descripcion+"','"+uMedida+"',"+F_Ori+", '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"')";
                        String querycom="insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(),'"+F_FecCad+"', DATE_ADD(curdate(), INTERVAL "+plazo+" DAY),'"+F_FileFac +"','"+F_FileRemi +"','"+F_FolFac +"','"+F_ClaPro +"','" + F_Pza + "', '" + F_Costo + "', '" + F_Tarimas + "', '" + F_Caja + "','"+F_Piezas+"', '" + F_TarimasI + "', '" + F_CajasI + "','" + F_Resto + "','" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_Marca + "','"+F_FolRemi+"' ,'" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0','"+FolioLote+"','"+F_Proyectos+"','"+descripcion+"','"+uMedida+"',"+F_Ori+", '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"', "+factorEmpaque+", '"+F_OrdenSuministro+"')";                        
                        String querycom2="INSERT INTO tb_compraregistro (`F_IdCom`, `F_FecApl`, `F_ClaPro`, `F_Lote`, `F_FecCad`, `F_FecFab`, `F_Marca`, `F_Provee`, `F_Cb`, `F_Tarimas`, `F_Cajas`, `F_Pz`, `F_TarimasI`, `F_CajasI`, `F_Resto`, `F_Costo`, `F_ImpTo`, `F_ComTot`, `F_Obser`, `F_FolRemi`, `F_OrdCom`, `F_ClaOrg`, `F_User`, `F_Proyecto`) VALUES ('0', curdate(), '"+ F_ClaPro +"', '" + FolioLote + "', '"+F_FecCad+"', '"+ F_FecFab +"', '"+ F_Marca +"', '" + F_Provee + "', '" + F_Cb + "', '" + F_Tarimas + "', '" + F_Caja + "', '"+F_Pza+"', '" + F_TarimasI + "', '" + F_CajasI + "', '" + F_Resto + "', '" + F_Costo + "', '" + F_ImpTo + "', '" + F_ComTot + "', '" + F_Obser + "', CONCAT( IFNULL('"+F_FolRemi+"', ''), IFNULL('"+F_FolFac +"', '')), '" + F_OrdCom + "', '" + F_Origen + "', '" + F_User + "', '"+F_Proyectos+"')";
                        System.out.println(querymov);
                        con.insertar(querymov);
                        System.out.println(querycom);
                        con.insertar(querycom);
                        System.out.println(querycom2);
                        con.insertar(querycom2);

                        FolioLote = "";
                        FolioLoteSQL = "";
                        String delQuery="delete from tb_compratemp where F_OrdCom = '" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "')";
                        con.actualizar("delete from tb_compratemp where F_OrdCom = '" + request.getParameter("vOrden") + "' and (F_FolRemi = '" + request.getParameter("vRemi") + "' or F_FolFac = '" + request.getParameter("vRemi") + "')");
                            }}
                    
            
                    //String queryValida = "select( (select sum(F_Cant) from `tb_pedido_sialss` where F_noCompra = '" + request.getParameter("vOrden")+"'  ) - (select sum(F_CanCom) from tb_compra where F_OrdCom='" + request.getParameter("vOrden")+"')) as res";
                    //System.out.println(queryValida);
                    //ResultSet rsetDV = con.consulta(queryValida);
                    int pivote = 0;
                   // ArrayList<String> listaVal= new ArrayList();
                   // if (rsetDV != null) {
                   // while (rsetDV.next()) {
                   //     listaVal.add(rsetDV.getString(1));
                  //      pivote = Integer.parseInt(listaVal.get(1));
                   // }
                    String queryBuscaOc = "SELECT c.F_ClaPro as clave, sum(c.F_CanCom) as totalCom, p17.F_Cant as solicitado, (p17.F_Cant - sum(c.F_CanCom )) AS Dif FROM tb_compra AS c INNER JOIN tb_pedido_sialss AS p17 ON c.F_OrdCom = p17.F_NoCompra AND c.F_ClaPro = p17.F_Clave WHERE c.F_ClaPro = '"+F_ClaPro+"' AND c.F_OrdCom = '"+ request.getParameter("vOrden")+"' GROUP BY c.F_ClaPro, c.F_OrdCom";
                    ResultSet rsetDV = con.consulta(queryBuscaOc);
                    while (rsetDV.next()) {
                        pivote = Integer.parseInt(rsetDV.getString(4));
                    }
                    if(pivote == 0){
                       con.actualizar("update `tb_pedido_sialss` set F_Recibido = 1 where F_noCompra = '" + request.getParameter("vOrden")+"' AND F_Clave = '"+F_ClaPro+"'");
                    }   
                   // }
                    
                    //PAra Enviar Correo
                    String BusquedaQFB = "SELECT c.F_ClaPro, c.F_CanCom FROM tb_compra as c INNER JOIN tb_controlados as ct on c.F_Clapro = ct.F_Clapro WHERE c.F_OrdCom = '" + request.getParameter("vOrden")+"' AND c.F_FolRemi = '" + request.getParameter("vRemi") + "' order by c.F_ClaPro;";
                    ResultSet rsetbusquedaQFB = con.consulta(BusquedaQFB);
                    String Bclave = "", ordenCompra = "",remision = "";
                    ordenCompra = request.getParameter("vOrden");
                    remision = request.getParameter("vRemi");
                    while (rsetbusquedaQFB.next()) {                        
                    Bclave = rsetbusquedaQFB.getString(1);    
                    }
                     if(!Bclave.equals("")){
                        correoConfirma.enviaCorreo(ordenCompra,remision);
                    }
                   
                    /*
                     *Para Abastecer modula
                     */
                    //modula.AbastoModula(F_OrdCom, F_FolRemi);
                    /*
                    
                     */
                  
                    con.cierraConexion();
             
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                sesion.setAttribute("vOrden", "");
                sesion.setAttribute("vRemi", "");
                out.println("<script>alert('Compra ABIERTA realizada, datos transferidos correctamente')</script>");

                if (Ori.equals("19")) {
                    out.println("<script>alert('Ingresar datos en WMS para registrar las cantidades de insumo asignado por unidad http://gnklbajio.local/')</script>");
              
                }
                out.println("<script>window.location='verificarCompraAuto.jsp'</script>");

            }
/*FIN DE INGRESO AUTOMATICO*/
            
            
            
            if (request.getParameter("accion").equals("GuardarAbiertaVerificaISEM")) {
                try {
                    con.conectar();

                    ResultSet rsetComTemp = con.consulta("select F_IdCom, F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca from tb_compratempisem where F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' ");
                    while (rsetComTemp.next()) {

                        Calendar c1 = GregorianCalendar.getInstance();
                        String F_FecCadAct = "", F_MarcaAct = "";
                        F_FecCadAct = request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom"));
                        F_MarcaAct = request.getParameter("F_Marca" + rsetComTemp.getString("F_IdCom"));
                        String Tipo = "";
                        //String cadu = df2.format(df3.parse(F_FecCadAct));
                        c1.setTime(df.parse(F_FecCadAct));

                        ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + rsetComTemp.getString("F_ClaPro") + "'");
                        while (rset_medica.next()) {
                            Tipo = rset_medica.getString("F_TipMed");
                            if (Tipo.equals("2504")) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                        }

                        String fecFab = (df.format(c1.getTime()));

                        ResultSet rset2 = con.consulta("select F_ClaMar from tb_marca where F_DesMar = '" + F_MarcaAct + "'");
                        while (rset2.next()) {
                            F_MarcaAct = rset2.getString("F_ClaMar");
                        }

                        con.insertar("update tb_compratempisem set F_Cb = '" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "', F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "'");

                        con.insertar("insert into tb_cb values (0,'" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "','" + rsetComTemp.getString("F_ClaPro") + "','" + rsetComTemp.getString("F_Lote") + "','" + rsetComTemp.getString("F_FecCad") + "','" + rsetComTemp.getString("F_FecFab") + "','" + rsetComTemp.getString("F_Marca") + "')");
                    }
                    //ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
                    //conModula.conectar();
                    //consql.conectar();
                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Ori = "", F_TarimasI = "", F_CajasI = "", F_RestoI = "", F_Proyectos="";
                    String F_FecApl="", F_Hora="";
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
                    ResultSet rsetDatos = con.consulta("SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Origen,F_TarimasI,F_CajasI, F_Estado, F_FecApl FROM tb_compratempisem WHERE F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' ");
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
                        F_RestoI = rsetDatos.getString("F_Resto");
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_Ori = rsetDatos.getString("F_Origen");
                        F_User = rsetDatos.getString("F_User");
                        F_TarimasI = rsetDatos.getString("F_TarimasI");
                        F_CajasI = rsetDatos.getString("F_CajasI");
                        F_Obser = rsetDatos.getString("F_Obser");
                        F_Proyectos = rsetDatos.getString("F_Estado");
                        F_FecApl= rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        String Ubicacion = "NUEVA";
                        con.insertar("insert into tb_compraregistro values(0,CURDATE(),'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_FecFab + "','" + F_Marca + "','" + F_Provee + "','" + F_Cb + "','" + F_Tarimas + "','" + F_Caja + "','" + F_Piezas + "','" + F_TarimasI + "','" + F_CajasI + "','" + F_RestoI + "','" + F_Costo + "','" + F_ImpTo + "','" + F_ComTot + "','" + F_Obser + "','" + F_FolRemi + "','" + F_OrdCom + "','" + F_Provee + "','" + sesion.getAttribute("nombre") + "')");
                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_ClaOrg='" + F_Origen + "' and F_ClaMar='" + F_Marca + "' and F_Origen='" + F_Ori + "' and F_Proyecto= '"+F_Proyectos+"");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }

                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Piezas);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (cantidad) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "', '" + F_Ori + "','"+F_Origen+"','131')");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','" + F_Ori + "','"+F_Origen+"','131')");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                                FolioLot = FolLot + 1;
                                con.actualizar("update tb_indice set F_IndLote='" + FolioLot + "'");
                            }

                        }
                        //FIN CONSULTA MYSQL

                        String F_ClaPrvSQL = "";
                        ResultSet rsetNomPro = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + F_Provee + "' ");

                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                        //FIN CONSULTA MYSQL
                        con.insertar("insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "', 'NUEVA', '" + F_Provee + "',curtime(),'" + F_User + "','') ");
//                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"') ");
                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"',1) ");
                        FolioLote = "";
                        FolioLoteSQL = "";
                    }

                    /*
                     *Para Abastecer modula
                     */
                    //modula.AbastoModula(F_OrdCom, F_FolRemi);
                    /*
                    
                     */
                    con.actualizar("delete from tb_compratempisem where F_OrdCom = '" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "'");
                    //con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "' and F_FolRemi = '" + request.getParameter("vRemi") + "'");
                    //conModula.cierraConexion();
                    con.cierraConexion();
                    //consql.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("vOrden", "");
                sesion.setAttribute("vRemi", "");

//                Lerma.conectar();
//                Lerma.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + request.getParameter("vOrden") + "';");
//                Lerma.cierraConexion();
                out.println("<script>alert('Compra ABIERTA realizada, datos transferidos correctamente')</script>");
                /* out.println("<script>window.open('reimpReporte.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 out.println("<script>window.open('reimp_marbete.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 //out.println("<script>window.open('reimpISEM.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");*/
                //correoConfirma.enviaCorreo(F_IndCom);
                out.println("<script>window.location='verificarCompraAutoISEM.jsp'</script>");

            }

            if (request.getParameter("accion").equals("VerificaRemi")) {
                try {
//ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
                    con.conectar();

                    ResultSet rsetComTemp = con.consulta("select F_IdCom, F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca from tb_compratemp where F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' ");
                    while (rsetComTemp.next()) {

                        Calendar c1 = GregorianCalendar.getInstance();
                        String F_FecCadAct = "", F_MarcaAct = "";
                        F_FecCadAct = request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom"));
                        F_MarcaAct = request.getParameter("F_Marca" + rsetComTemp.getString("F_IdCom"));
                        String Tipo = "";
                        //String cadu = df2.format(df3.parse(F_FecCadAct));
                        c1.setTime(df.parse(F_FecCadAct));

                        ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + rsetComTemp.getString("F_ClaPro") + "'");
                        while (rset_medica.next()) {
                            Tipo = rset_medica.getString("F_TipMed");
                            if (Tipo.equals("2504")) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                        }

                        String fecFab = (df.format(c1.getTime()));

                        ResultSet rset2 = con.consulta("select F_ClaMar from tb_marca where F_DesMar = '" + F_MarcaAct + "'");
                        while (rset2.next()) {
                            F_MarcaAct = rset2.getString("F_ClaMar");
                        }

                        con.insertar("update tb_compratemp set F_Estado='2', F_Cb = '" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "', F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "'");

                        con.insertar("insert into tb_cb values (0,'" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "','" + rsetComTemp.getString("F_ClaPro") + "','" + rsetComTemp.getString("F_Lote") + "','" + F_FecCadAct + "','" + fecFab + "','" + F_MarcaAct + "')");
                    }
                    con.cierraConexion();
                    out.println("<script>alert('RemisiÃ³n Validada Correctamente')</script>");

                } catch (Exception e) {
                    out.println(e);
                    out.println("<script>alert('" + e + "')</script>");
                }
                out.println("<script>window.location='compraAuto4.jsp'</script>");
            }
            
            if (request.getParameter("accion").equals("GuardarVerifica")) {
                try {
                    //ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
                    con.conectar();

                    ResultSet rsetComTemp = con.consulta("select F_IdCom, F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca,F_Pz,F_Costo from tb_compratemp where F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' ");
                    while (rsetComTemp.next()) {

                        Calendar c1 = GregorianCalendar.getInstance();
                        String F_FecCadAct = "", F_MarcaAct = "";
                        System.out.println("Id:" + request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom")));
                        F_FecCadAct = request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom"));
                        F_MarcaAct = request.getParameter("F_Marca" + rsetComTemp.getString("F_IdCom"));
                        String Tipo = "";
                        //String cadu = df2.format(df3.parse(F_FecCadAct));
                        c1.setTime(df.parse(F_FecCadAct));

                        ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + rsetComTemp.getString("F_ClaPro") + "'");
                        while (rset_medica.next()) {
                            Tipo = rset_medica.getString("F_TipMed");
                            if (Tipo.equals("2504")) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                        }

                        String fecFab = (df.format(c1.getTime()));

                        ResultSet rset2 = con.consulta("select F_ClaMar from tb_marca where F_DesMar = '" + F_MarcaAct + "'");
                        while (rset2.next()) {
                            F_MarcaAct = rset2.getString("F_ClaMar");
                        }

                        con.insertar("update tb_compratemp set F_Lote= '" + request.getParameter("F_Lote" + rsetComTemp.getString("F_IdCom")) + "',F_Pz= '" + request.getParameter("F_Cant" + rsetComTemp.getString("F_IdCom")) + "',F_Cb = '" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "', F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "'");

                        con.insertar("insert into tb_cb values (0,'" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "','" + rsetComTemp.getString("F_ClaPro") + "','" + request.getParameter("F_Lote" + rsetComTemp.getString("F_IdCom")) + "','" + F_FecCadAct + "','" + fecFab + "','" + F_MarcaAct + "')");
                    }
                    //conModula.conectar();
                    //consql.conectar();

                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Ori = "", F_Proyectos="";
                    String F_FecApl="", F_Hora="";
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
                    ResultSet rsetDatos = con.consulta("SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, SUM(F_Pz) AS F_Pz,SUM(F_Resto) AS F_Resto, F_Costo,SUM(F_ImpTo) AS F_ImpTo,SUM(F_ComTot) AS F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Origen, F_Estado, F_FecApl FROM tb_compratemp WHERE F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' GROUP BY F_ClaPro,F_Lote,F_FecCad,F_Origen ");
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
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_Ori = rsetDatos.getString("F_Origen");
                        F_User = rsetDatos.getString("F_User");
                        F_Proyectos = rsetDatos.getString("F_Estado");
                        F_FecApl = rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        String Ubicacion = "NUEVA";

                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_Origen = '" + F_Ori + "' and F_Proyecto= '"+F_Proyectos+"");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }

                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Piezas);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (cantidad) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','" + F_Ori + "','"+F_Origen+"','131')");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','','')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "', '" + F_Ori + "','"+F_Origen+"','131' )");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','','')");
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

                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");
                        //FIN CONSULTA MYSQL
                        con.insertar("insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "', 'NUEVA', '" + F_Provee + "',curtime(),'" + (String) sesion.getAttribute("nombre") + "','') ");
                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + (String) sesion.getAttribute("nombre") + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"') ");
                        FolioLote = "";
                        FolioLoteSQL = "";
                        con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "' and F_Clave = '" + F_ClaPro + "' ");
                    }

                    /*
                     * AquÃ­ se debe de mandar a MODULA
                     */
                    //modula.AbastoModula(F_OrdCom, F_FolRemi);

                    /*
                     *
                     */
                    con.actualizar("delete from tb_compratemp where F_OrdCom = '" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "'");
                    //conModula.cierraConexion();
                    con.cierraConexion();
                    //consql.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("vOrden", "");
                sesion.setAttribute("vRemi", "");
                out.println("<script>alert('Compra realizada, datos transferidos correctamente')</script>");/*
                 out.println("<script>window.open('reimpReporte.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 out.println("<script>window.open('reimp_marbete.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 //out.println("<script>window.open('reimpISEM.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");*/

                //correoConfirma.enviaCorreo(F_IndCom);

                out.println("<script>window.location='verificarCompraAuto.jsp'</script>");
            }
            
            

            if (request.getParameter("accion").equals("GuardarVerificaISEM")) {
                try {
                    //ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
                    con.conectar();

                    ResultSet rsetComTemp = con.consulta("select F_IdCom, F_ClaPro, F_Lote, F_FecCad, F_FecFab, F_Marca,F_Pz,F_Costo from tb_compratempisem where F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "';");
                    while (rsetComTemp.next()) {

                        Calendar c1 = GregorianCalendar.getInstance();
                        String F_FecCadAct = "", F_MarcaAct = "";
                        System.out.println("Id:" + request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom")));
                        F_FecCadAct = request.getParameter("F_FecCad" + rsetComTemp.getString("F_IdCom"));
                        F_MarcaAct = request.getParameter("F_Marca" + rsetComTemp.getString("F_IdCom"));
                        String Tipo = "";
                        //String cadu = df2.format(df3.parse(F_FecCadAct));
                        c1.setTime(df.parse(F_FecCadAct));

                        ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + rsetComTemp.getString("F_ClaPro") + "';");
                        while (rset_medica.next()) {
                            Tipo = rset_medica.getString("F_TipMed");
                            if (Tipo.equals("2504")) {
                                c1.add(Calendar.YEAR, -3);
                            } else {
                                c1.add(Calendar.YEAR, -5);
                            }
                        }

                        String fecFab = (df.format(c1.getTime()));

                        ResultSet rset2 = con.consulta("select F_ClaMar from tb_marca where F_DesMar = '" + F_MarcaAct + "';");
                        while (rset2.next()) {
                            F_MarcaAct = rset2.getString("F_ClaMar");
                        }

                        con.insertar("update tb_compratempisem set F_Lote= '" + request.getParameter("F_Lote" + rsetComTemp.getString("F_IdCom")) + "',F_Pz= '" + request.getParameter("F_Cant" + rsetComTemp.getString("F_IdCom")) + "',F_Cb = '" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "', F_FecCad = '" + F_FecCadAct + "', F_FecFab='" + fecFab + "', F_Marca = '" + F_MarcaAct + "' where F_IdCom='" + rsetComTemp.getString("F_IdCom") + "';");

                        con.insertar("insert into tb_cb values (0,'" + request.getParameter("F_Cb" + rsetComTemp.getString("F_IdCom")) + "','" + rsetComTemp.getString("F_ClaPro") + "','" + request.getParameter("F_Lote" + rsetComTemp.getString("F_IdCom")) + "','" + F_FecCadAct + "','" + fecFab + "','" + F_MarcaAct + "');");
                    }
                    //conModula.conectar();
                    //consql.conectar();

                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_RestoI = "", F_Piezas = "", F_Obser = "", F_Ori = "", F_TarimasI = "", F_CajasI = "", F_Proyectos="";
                    String F_FecApl="", F_Hora="";
                    int ExiLot = 0, cantidad = 0, sumalote = 0, FolLot = 0, FolioLot = 0, F_IndComT = 0, F_Origen = 0, FolMov = 0, FolioMovi = 0, FolMovSQL = 0, FolioMoviSQL = 0;
                    double compraB = 0.0;

                    //VARIABLES SQL SERVER
                    String FolioLoteSQL = "", ExiLoteSQL = "", F_Numero = "", F_FecCadSQL = "", ExisMed = "";
                    int sumaloteSQL = 0, ExiLotSQL = 0, cantidadSQL = 0, Contar = 0, FolLotSQL = 0, FolioLotSQL = 0, F_IndComTSQL = 0, F_IndComSQL = 0;
                    double cantidadTSQL = 0.0, TotalExi = 0.0, ExisMedTSQL = 0.0;
                    //CONSULTA MYSQL INDICE DE COMPRA
                    ResultSet rset_IndF = con.consulta("SELECT F_IndCom FROM tb_indice;");
                    while (rset_IndF.next()) {
                        F_IndCom = Integer.parseInt(rset_IndF.getString("F_IndCom"));
                    }
                    F_IndComT = F_IndCom + 1;
                    con.actualizar("update tb_indice set F_IndCom='" + F_IndComT + "';");
                    //FIN MYSQL

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
                    ResultSet rsetDatos = con.consulta("SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, SUM(F_Pz) AS F_Pz,SUM(F_Resto) AS F_Resto, F_Costo,SUM(F_ImpTo) AS F_ImpTo,SUM(F_ComTot) AS F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Origen,F_TarimasI,F_CajasI, F_Estado, F_FecApl FROM tb_compratempisem WHERE F_OrdCom='" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "' GROUP BY F_ClaPro,F_Lote,F_FecCad,F_Origen;");
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
                        F_RestoI = rsetDatos.getString("F_Resto");
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_Ori = rsetDatos.getString("F_Origen");
                        F_User = rsetDatos.getString("F_User");
                        F_TarimasI = rsetDatos.getString("F_TarimasI");
                        F_CajasI = rsetDatos.getString("F_CajasI");
                        F_Obser = rsetDatos.getString("F_Obser");
                        F_Proyectos = rsetDatos.getString("F_Estado");
                        F_FecApl = rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        String Ubicacion = "NUEVA";

                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_Origen = '" + F_Ori + "' and F_Proyecto= '"+F_Proyectos+";");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }

                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Piezas);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='" + Ubicacion + "' and F_Proyecto= '"+F_Proyectos+"'");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (cantidad) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','" + F_Ori + "','"+F_Origen+"','131');");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','','')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice;");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "', '" + F_Ori + "','"+F_Origen+"','131' );");
                                if (Ubicacion.equals("MODULA")) {
                                    //conModula.ejecutar("insert into IMP_AVVISIINGRESSO (RIG_OPERAZIONE, RIG_ARTICOLO, RIG_SUB1, RIG_SUB2, RIG_QTAR, RIG_DSCAD, RIG_REQ_NOTE, RIG_ATTR1, RIG_ERRORE, RIG_HOSTINF) values('A','" + F_ClaPro + "','" + F_Lote + "','1','" + (F_Piezas) + "','" + F_FecCad.replace("-", "") + "','" + F_Cb + "','','','" + df3.format(new Date()) + "')");
                                }
                                //con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','" + Ubicacion + "','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "','','')");
                                FolioLot = FolLot + 1;
                                con.actualizar("update tb_indice set F_IndLote='" + FolioLot + "';");
                            }

                        }
                        //FIN CONSULTA MYSQL

                        String F_ClaPrvSQL = "";
                        ResultSet rsetNomPro = con.consulta("select F_NomPro from tb_proveedor where F_ClaProve = '" + F_Provee + "';");
                        while (rsetNomPro.next()) {
                            /*ResultSet rsetProveeSQL = consql.consulta("select F_ClaPrv from TB_Provee where F_NomPrv = '" + rsetNomPro.getString(1) + "' ");
                             while (rsetProveeSQL.next()) {
                             F_ClaPrvSQL = rsetProveeSQL.getString(1);
                             }*/
                        }

                        //CONSULTA INDICE MOVIMIENTO MYSQL
                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice;");
                        while (FolioMov.next()) {
                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                        }
                        FolMov = FolioMovi + 1;
                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "';");
                        //FIN CONSULTA MYSQL
                        con.insertar("insert into tb_movinv values (0,curdate(),'" + F_IndCom + "','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_ComTot + "' ,'1', '" + FolioLote + "', 'NUEVA', '" + F_Provee + "',curtime(),'" + (String) sesion.getAttribute("nombre") + "','');");
                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + (String) sesion.getAttribute("nombre") + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"');");
                        FolioLote = "";
                        FolioLoteSQL = "";
                        con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "' and F_Clave = '" + F_ClaPro + "';");

                        con.insertar("insert into tb_compraregistro values(0,CURDATE(),'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_FecFab + "','" + F_Marca + "','" + F_Provee + "','" + F_Cb + "','" + F_Tarimas + "','" + F_Caja + "','" + F_Piezas + "','" + F_TarimasI + "','" + F_CajasI + "','" + F_RestoI + "','" + F_Costo + "','" + F_ImpTo + "','" + F_ComTot + "','" + F_Obser + "','" + F_FolRemi + "','" + F_OrdCom + "','" + F_Provee + "','" + sesion.getAttribute("nombre") + "')");

                    }

                    /*
                     * AquÃ­ se debe de mandar a MODULA
                     */
                    //modula.AbastoModula(F_OrdCom, F_FolRemi);

                    /*
                     *
                     */
                    con.actualizar("delete from tb_compratempisem where F_OrdCom = '" + request.getParameter("vOrden") + "' and F_FolRemi = '" + request.getParameter("vRemi") + "';");
                    con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "';");
                    //conModula.cierraConexion();
                    con.cierraConexion();
//                    Lerma.conectar();
//                    Lerma.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "';");
//                    Lerma.cierraConexion();
                    //consql.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                sesion.setAttribute("vOrden", "");
                sesion.setAttribute("vRemi", "");
                out.println("<script>alert('Compra realizada, datos transferidos correctamente')</script>");/*
                 out.println("<script>window.open('reimpReporte.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 out.println("<script>window.open('reimp_marbete.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
                 //out.println("<script>window.open('reimpISEM.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");*/

                //correoConfirma.enviaCorreo(F_IndCom);

                out.println("<script>window.location='verificarCompraAutoISEM.jsp'</script>");
            }

            
            
            if (request.getParameter("accion").equals("Eliminar")) {

                try {
                    con.conectar();
                    try {
                        con.insertar("delete from tb_compratemp where F_OrdCom = '" + request.getParameter("fol_gnkl") + "' ");
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                    request.getSession().setAttribute("folio", "");
                    request.getSession().setAttribute("fecha", "");
                    request.getSession().setAttribute("folio_remi", "");
                    request.getSession().setAttribute("orden", "");
                    request.getSession().setAttribute("provee", "");
                    request.getSession().setAttribute("recib", "");
                    request.getSession().setAttribute("entrega", "");
                    request.getSession().setAttribute("origen", "");
                    request.getSession().setAttribute("coincide", "");
                    request.getSession().setAttribute("observaciones", "");
                    request.getSession().setAttribute("clave", "");
                    request.getSession().setAttribute("descrip", "");
                } catch (Exception e) {
                }
                sesion.setAttribute("posClave", "0");
                sesion.setAttribute("NoCompra", "0");
                out.println("<script>alert('Compra cancelada')</script>");
                out.println("<script>window.location='hh/compraAuto3.jsp'</script>");
            }
            
            
            if (request.getParameter("accion").equals("EliminarISEM")) {

                try {
                    con.conectar();

                    try {
                        con.insertar("delete from tb_compratempisem where F_OrdCom = '" + request.getParameter("NoCompra") + "' ");
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                    request.getSession().setAttribute("folio", "");
                    request.getSession().setAttribute("fecha", "");
                    request.getSession().setAttribute("folio_remi", "");
                    request.getSession().setAttribute("orden", "");
                    request.getSession().setAttribute("provee", "");
                    request.getSession().setAttribute("recib", "");
                    request.getSession().setAttribute("entrega", "");
                    request.getSession().setAttribute("origen", "");
                    request.getSession().setAttribute("coincide", "");
                    request.getSession().setAttribute("observaciones", "");
                    request.getSession().setAttribute("clave", "");
                    request.getSession().setAttribute("descrip", "");
                } catch (Exception e) {
                }
                sesion.setAttribute("posClave", "0");
                sesion.setAttribute("NoCompra", "0");
                out.println("<script>alert('Compra cancelada')</script>");
                out.println("<script>window.location='compraAuto2.jsp'</script>");
            }
            
            
            if (request.getParameter("accion").equals("Guardar")) {
                try {
                    con.conectar();
                    //consql.conectar();
                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Proyectos="";
                    String F_FecApl="", F_Hora="";
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
                    ResultSet rsetDatos = con.consulta("SELECT F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Proyectos, F_FecApl FROM tb_compratemp WHERE F_OrdCom='" + request.getParameter("fol_gnkl") + "'");
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
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_User = rsetDatos.getString("F_User");
                        F_Proyectos = rsetDatos.getString("F_Proyectos");
                        F_FecApl = rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_ClaOrg='" + F_Origen + "' and F_ClaMar='" + F_Marca + "' and F_Proyecto= '"+F_Proyectos+"");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }

                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA' and F_Proyecto= '"+F_Proyectos+"'");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Piezas);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA' and F_Proyecto= '"+F_Proyectos+"'");
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                                con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
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
                         }
                         if (!(FolioLoteSQL.equals(""))) {//Lote existente
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
                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"') ");
                        //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioCompra + "','','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "','" + F_ImpTo + "', '" + F_ComTot + "' ,'1', '" + FolioLoteSQL + "', '" + FolioMovi + "','M', '0', '','','','" + F_ClaPrvSQL + "','" + F_User + "') ");
                        //consql.insertar("insert into TB_Compra values ('C','" + FolioCompra + "','" + F_ClaPrvSQL + "','A','CD',CONVERT(date,GETDATE()),'', '" + F_ClaPro + "','','','1', '" + F_Piezas + "', '1','" + F_ComTot + "','0','" + F_ComTot + "','" + F_ComTot + "','0', '" + F_ImpTo + "','" + F_ComTot + "', '" + F_Costo + "', '" + FolioLoteSQL + "','D',CONVERT(date,GETDATE()), '" + F_User + "','0','0','','" + F_FolRemi + "','' ) ");
                        //consql.insertar("insert into TB_Bitacora values ('" + F_User + "',CONVERT(date,GETDATE()),'COMPRA - MANUAL','REGISTRAR','" + FolioCompra + "','1','COMPRAS') ");

                        FolioLote = "";
                        FolioLoteSQL = "";
                    }

                    con.actualizar("delete from tb_compratemp where F_OrdCom = '" + F_OrdCom + "'");
                    con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "'");
                    con.cierraConexion();
                    //consql.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                request.getSession().setAttribute("folio", "");
                request.getSession().setAttribute("fecha", "");
                request.getSession().setAttribute("folio_remi", "");
                request.getSession().setAttribute("orden", "");
                request.getSession().setAttribute("provee", "");
                request.getSession().setAttribute("recib", "");
                request.getSession().setAttribute("entrega", "");
                request.getSession().setAttribute("origen", "");
                request.getSession().setAttribute("coincide", "");
                request.getSession().setAttribute("observaciones", "");
                request.getSession().setAttribute("clave", "");
                request.getSession().setAttribute("descrip", "");
                request.getSession().setAttribute("cuenta", "");
                request.getSession().setAttribute("cb", "");
                request.getSession().setAttribute("codbar2", "");
                request.getSession().setAttribute("Marca", "");
                request.getSession().setAttribute("PresPro", "");

                out.println("<script>alert('Compra realizada, datos transferidos correctamente')</script>");
            }
            
            
            
            if (request.getParameter("accion").equals("GuardarAbierta")) {
                try {
                    con.conectar();
                    //consql.conectar();
                    String F_ClaPro = "", F_Lote = "", F_FecCad = "", F_FecFab = "", F_Marca = "", F_Provee = "", F_Cb = "", F_Tarimas = "", F_Costo = "", F_ImpTo = "", F_ComTot = "", F_User = "", F_FolRemi = "", F_OrdCom = "";
                    String FolioLote = "", ExiLote = "", F_Caja = "", F_Resto = "", F_Piezas = "", F_Obser = "", F_Proyectos="";
                    String F_FecApl="", F_Hora="";
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
                    ResultSet rsetDatos = con.consulta("SELECT F_FecApl, F_Hora, F_ClaPro, F_Lote, F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS FECAD, F_FecFab, F_Marca, F_Provee, F_Cb, F_Tarimas, F_Cajas, F_Pz, F_Resto, F_Costo,F_ImpTo, F_ComTot, F_FolRemi, F_OrdCom, F_ClaOrg, F_User, F_Obser, F_Proyectos FROM tb_compratemp WHERE F_OrdCom='" + request.getParameter("fol_gnkl") + "'");
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
                        F_Resto = rsetDatos.getString("F_Resto");
                        F_Costo = rsetDatos.getString("F_Costo");
                        F_ImpTo = rsetDatos.getString("F_ImpTo");
                        F_ComTot = rsetDatos.getString("F_ComTot");
                        F_FolRemi = rsetDatos.getString("F_FolRemi");
                        F_OrdCom = rsetDatos.getString("F_OrdCom");
                        F_Origen = Integer.parseInt(rsetDatos.getString("F_ClaOrg"));
                        F_User = rsetDatos.getString("F_User");
                        F_Proyectos = rsetDatos.getString("F_Proyecto");
                        F_FecApl = rsetDatos.getString("F_FecApl");
                        F_Hora = rsetDatos.getString("F_Hora");
                        try {
                            byte[] a = rsetDatos.getString("F_Obser").getBytes("ISO-8859-1");
                            F_Obser = (new String(a, "UTF-8")).toUpperCase();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        // CONSULTA MYSQL
                        /*
                         *Se extrae fol_lote de F_FolLot para agregar o generar uno nuevo
                         */
//                        ResultSet rsetLote = con.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCad + "' and F_ClaOrg='" + F_Origen + "' and F_ClaMar='" + F_Marca + "' and F_Proyecto= '"+F_Proyectos+"'");
//                        while (rsetLote.next()) {
//                            //System.out.println(rset.getString("F_FolLot"));
//                            FolioLote = rsetLote.getString("F_FolLot");
//                        }

                        if (!(FolioLote.equals(""))) {//Lote existente
                            ResultSet rset_fol = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA' and F_Proyecto= '"+F_Proyectos+"");
                            while (rset_fol.next()) {
                                ExiLote = rset_fol.getString("F_ExiLot");
                            }
                            if (!(ExiLote.equals(""))) { //Lote con ubicacion
                                ExiLot = Integer.parseInt(ExiLote);
                                cantidad = Integer.parseInt(F_Piezas);
                                sumalote = ExiLot + cantidad;
                                con.actualizar("update tb_lote set F_ExiLot='" + sumalote + "' where F_FolLot='" + FolioLote + "' and F_Ubica='NUEVA' and F_Proyecto= '"+F_Proyectos+"");
                            } else { //Lote sin ubicacion
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
//                                con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
                            }
                        } else { //Lote Inexistente
                            ResultSet rset_Ind = con.consulta("SELECT F_IndLote FROM tb_indice");
                            while (rset_Ind.next()) {
                                FolioLote = rset_Ind.getString("F_IndLote");
                                FolLot = Integer.parseInt(rset_Ind.getString("F_IndLote"));
                                con.insertar("insert into tb_lote values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
//                                con.insertar("insert into tb_lote_repisem values (0,'" + F_ClaPro + "','" + F_Lote + "','" + F_FecCad + "','" + F_Piezas + "','" + FolioLote + "','" + F_Origen + "','NUEVA','" + F_FecFab + "','" + F_Cb + "','" + F_Marca + "')");
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
                        /*ResultSet rsetLoteSQL = consql.consulta("SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + F_ClaPro + "' and F_ClaLot='" + F_Lote + "' and F_FecCad='" + F_FecCadSQL + "'");
                         while (rsetLoteSQL.next()) {
                         FolioLoteSQL = rsetLoteSQL.getString("F_FolLot");
                         }
                         if (!(FolioLoteSQL.equals(""))) {//Lote existente
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
                        con.insertar("insert into tb_compra values (0,'" + F_IndCom + "','" + F_Provee + "','A',curdate(), '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "', '" + F_Caja + "', '0', '" + F_Tarimas + "', '" + F_ImpTo + "' ,'" + F_ComTot + "', '" + FolioLote + "', '" + F_FolRemi + "', '" + F_OrdCom + "', '" + F_Origen + "', '" + F_Cb + "', curtime(), '" + F_User + "','" + F_Obser + "','0', '"+sesion.getAttribute("nombre")+"', '"+F_FecApl+"', '"+F_Hora+"') ");
                        //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioCompra + "','','1', '" + F_ClaPro + "', '" + F_Piezas + "', '" + F_Costo + "','" + F_ImpTo + "', '" + F_ComTot + "' ,'1', '" + FolioLoteSQL + "', '" + FolioMovi + "','M', '0', '','','','" + F_ClaPrvSQL + "','" + F_User + "') ");
                        //consql.insertar("insert into TB_Compra values ('C','" + FolioCompra + "','" + F_ClaPrvSQL + "','A','CD',CONVERT(date,GETDATE()),'', '" + F_ClaPro + "','','','1', '" + F_Piezas + "', '1','" + F_ComTot + "','0','" + F_ComTot + "','" + F_ComTot + "','0', '" + F_ImpTo + "','" + F_ComTot + "', '" + F_Costo + "', '" + FolioLoteSQL + "','D',CONVERT(date,GETDATE()), '" + F_User + "','0','0','','" + F_FolRemi + "','' ) ");
                        //consql.insertar("insert into TB_Bitacora values ('" + F_User + "',CONVERT(date,GETDATE()),'COMPRA - MANUAL','REGISTRAR','" + FolioCompra + "','1','COMPRAS') ");

                        FolioLote = "";
                        FolioLoteSQL = "";
                    }

                    con.actualizar("delete from tb_compratemp where F_OrdCom = '" + F_OrdCom + "'");
                    //con.actualizar("update tb_pedidoisem set F_Recibido = '1' where F_NoCompra = '" + F_OrdCom + "'");
                    con.cierraConexion();
                    //consql.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                request.getSession().setAttribute("folio", "");
                request.getSession().setAttribute("fecha", "");
                request.getSession().setAttribute("folio_remi", "");
                request.getSession().setAttribute("orden", "");
                request.getSession().setAttribute("provee", "");
                request.getSession().setAttribute("recib", "");
                request.getSession().setAttribute("entrega", "");
                request.getSession().setAttribute("origen", "");
                request.getSession().setAttribute("coincide", "");
                request.getSession().setAttribute("observaciones", "");
                request.getSession().setAttribute("clave", "");
                request.getSession().setAttribute("descrip", "");
                request.getSession().setAttribute("cuenta", "");
                request.getSession().setAttribute("cb", "");
                request.getSession().setAttribute("codbar2", "");
                request.getSession().setAttribute("Marca", "");
                request.getSession().setAttribute("PresPro", "");

                out.println("<script>alert('Compra realizada, datos transferidos correctamente')</script>");
            }
        } catch (Exception e) {
        }
        sesion.setAttribute("posClave", "0");
        sesion.setAttribute("NoCompra", "0");
        out.println("<script>window.open('reimpReporte.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
        out.println("<script>window.open('reimp_marbete.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
        //out.println("<script>window.open('reimpISEM.jsp?fol_gnkl=" + F_IndCom + "','_blank')</script>");
        //correoConfirma.enviaCorreo(F_IndCom);
        out.println("<script>window.location='compraAuto2.jsp'</script>");

        // out.println("<script>window.location='<form action=reimpReporte.jsp target=_blank><input class=hidden name=fol_gnkl value=<%=F_IndCom%>></form>'</script>");
        //response.sendRedirect("captura.jsp");
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
        /*try {
         consql.conectar();
         try {
         ResultSet rset = consql.consulta("select F_IO from TB_Indice");
         while (rset.next()) {
         idIO = rset.getString("F_IO");
         consql.actualizar("update TB_Indice set F_IO = '" + (Integer.parseInt(idIO) + 1) + "' ");
         }
         } catch (Exception e) {
         }
         consql.cierraConexion();
         } catch (Exception e) {
         }*/
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
        try {
            /*consql.conectar();
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
             */
        } catch (Exception e) {
        }
        return idLote;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
