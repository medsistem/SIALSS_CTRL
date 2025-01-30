/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpSession;

/**
 * Validación y captura de registro del ingreso de las compras
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Altas extends HttpServlet {

    ConectionDB con = new ConectionDB();
    //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
    java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        String usua = (String) sesion.getAttribute("nombre");
        String clave = "", descr = "", cb = "", Cuenta = "", Marca = "", codbar2 = "", PresPro = "", tipo = "";
        int ban1 = 0, contador = 0, ban2 = 0, tipcap = 0;
        String boton = request.getParameter("accion");
        String ancla = "";
        try {
            if (request.getParameter("accion").equals("codigo")) {
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT F_Cb,COUNT(F_Cb) as cuenta FROM tb_cb WHERE F_Cb='" + request.getParameter("codigo") + "' GROUP BY F_Cb");
                    while (rset.next()) {
                        ban1 = 1;
                        cb = rset.getString("F_Cb");
                        Cuenta = rset.getString("cuenta");
                    }
                    if (Cuenta.equals("")) {
                        Cuenta = "0";
                        cb = request.getParameter("codigo");
                        ban1 = 2;
                    }
                    ancla = "#codigo";
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            if (request.getParameter("accion").equals("clave")) {
                try {
                    con.conectar();
                    tipcap = 1;
                    cb = request.getParameter("cb");

                    ResultSet rset = con.consulta("SELECT M.F_ClaPro, M.F_ClaProSS, M.F_DesPro, CASE WHEN M.F_ClaPro = C.F_ClaPro THEN '1' ELSE '0'END 'Controlado' FROM tb_medica AS M LEFT JOIN tb_controlados AS C ON C.F_ClaPro = M.F_ClaPro WHERE  M.F_StsPro = 'A'  AND M.F_ClaPro ='" + request.getParameter("clave") + "'");
                    while (rset.next()) {
                        tipo = rset.getString("Controlado");
                        System.out.println("tipo: "+tipo);
                        if (tipo.equals("1")) {
                            
                            ResultSet rsetUsuario = con.consulta("SELECT COUNT(pu.F_IdUsu) FROM tb_permisosusuario AS pu INNER JOIN tb_usuario AS u ON pu.F_IdUsu = u.F_IdUsu WHERE u.F_Nombre = '"+usua+"' AND pu.ingresoControlado = '1';");
                             while(rsetUsuario.next()){                                 
                            contador = rsetUsuario.getInt(1);                             
                            if (contador == 1) {
                                ban1 = 1;
                                 System.out.println("Es controlado");
                                clave = rset.getString("F_ClaPro");
                                descr = rset.getString("F_DesPro");
                                PresPro = rset.getString("F_PrePro");
                            } else {
                                ban1 = 3;
                            }}
                        } else {
                            System.out.println("no es controlado");
                            ban1 = 1;
                            clave = rset.getString("F_ClaPro");
                            descr = rset.getString("F_DesPro");
                            PresPro = rset.getString("F_PrePro");
                        }

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            
            /*Captura Resguardo*/
            if (request.getParameter("accion").equals("claveProy")) {
                try {
                    con.conectar();
                    tipcap = 2;
                    System.out.println("buscando clave por proyecto");
                    ResultSet rset = con.consulta("SELECT M.F_ClaPro, M.F_ClaProSS, M.F_DesPro, CASE WHEN M.F_ClaPro = C.F_ClaPro THEN '1' ELSE '0'END 'Controlado', CASE WHEN M.F_ProSonora = 1 THEN '1' ELSE '0' END 'ProyectoSts'  FROM tb_medica AS M LEFT JOIN tb_controlados AS C ON C.F_ClaPro = M.F_ClaPro WHERE  M.F_StsPro = 'A'   AND M.F_ClaPro ='" + request.getParameter("clave") + "'");
                    if (!rset.wasNull()) {
                        while (rset.next()) {
                            if (rset.getInt(5) == 1) {
                                tipo = rset.getString("Controlado");
                                System.out.println("tipo: " + tipo);
                                if (tipo.equals("1")) {
                                    ban2 = 1;
                                    System.out.println("Es controlado");
                                    clave = rset.getString("F_ClaPro");
                                    descr = rset.getString("F_DesPro");
                                    PresPro = rset.getString("F_PrePro");

                                } else {
                                    ban2 = 2;
                                    System.out.println("no es controlado");
                                    clave = rset.getString("F_ClaPro");
                                    descr = rset.getString("F_DesPro");
                                    PresPro = rset.getString("F_PrePro");
                                }
                            } else {
                                ban2 = 3;
                            }

                        }
                    } else {
                        ban2 = 0;
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }

            if (request.getParameter("accion").equals("CodBar")) {
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT MAX(F_IdCb) AS F_IdCb FROM tb_gencb");
                    while (rset.next()) {
                        ban1 = 1;
                        tipcap = 1;
//                        ban2 = 2;
                        codbar2 = rset.getString("F_IdCb");
                    }
                    System.out.println(codbar2);
                    Long CB = Long.parseLong(codbar2) + 1;
                    con.insertar("insert into tb_gencb values('" + CB + "','CEDIS CENTRAL')");
                    descr = request.getParameter("descripci");
                    clave = request.getParameter("clave1");
                    rset = con.consulta("select F_PrePro from tb_medica where F_ClaPro='" + clave + "'");
                    while (rset.next()) {
                        PresPro = rset.getString("F_PrePro");
                    }
                    Marca = request.getParameter("Marca");
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            if (request.getParameter("accion").equals("CodBarRes")) {
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT MAX(F_IdCb) AS F_IdCb FROM tb_gencb");
                    while (rset.next()) {
//                        ban1 = 1;
                        tipcap = 2;
                        ban2 = 2;
                        codbar2 = rset.getString("F_IdCb");
                    }
                    System.out.println(codbar2);
                    Long CB = Long.parseLong(codbar2) + 1;
                    con.insertar("insert into tb_gencb values('" + CB + "','CEDIS CENTRAL')");
                    descr = request.getParameter("descripci");
                    clave = request.getParameter("clave1");
                    rset = con.consulta("select F_PrePro from tb_medica where F_ClaPro='" + clave + "'");
                    while (rset.next()) {
                        PresPro = rset.getString("F_PrePro");
                    }
                    Marca = request.getParameter("Marca");
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            if (request.getParameter("accion").equals("descripcion")) {
                try {
                    con.conectar();
                    cb = request.getParameter("cb");
                    ResultSet rset = con.consulta("select F_ClaPro, F_DesPro, F_PrePro from tb_medica where F_DesPro='" + request.getParameter("descr") + "' ");
                    while (rset.next()) {
                        ban1 = 1;
                        clave = rset.getString("F_ClaPro");
                        descr = rset.getString("F_DesPro");
                        PresPro = rset.getString("F_PrePro");
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            
               if (request.getParameter("accion").equals("descripcionProy")) {
                try {
                    con.conectar();
                    cb = request.getParameter("cb");
                    ResultSet rset = con.consulta("select F_ClaPro, F_DesPro, F_PrePro from tb_medica where F_DesPro='" + request.getParameter("descr") + "' AND F_ProSonora = 1");
                    while (rset.next()) {
                        ban1 = 1;
                        clave = rset.getString("F_ClaPro");
                        descr = rset.getString("F_DesPro");
                        PresPro = rset.getString("F_PrePro");
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }
            }
            if (request.getParameter("accion").equals("refresh")) {
                try {
                    ban1 = 1;
                    descr = request.getParameter("descripci");
                    clave = request.getParameter("clave1");
                    PresPro = request.getParameter("Presentaci");
                } catch (Exception e) {

                }
            }

            ///Ingreso manual
            if (request.getParameter("accion").equals("capturar")) {
                ban1 = 1;
                tipcap = 1;
                System.out.println("Entrada manual capturalote");
                String cla_pro = request.getParameter("clave1");
                String Tipo = "", FechaC = "", FechaF = "", CostoCap = "", PzsxCC = "";
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                int fcdu = 0, anofec = 0;
                String lot_pro = request.getParameter("Lote").toUpperCase();
                String FeCad = request.getParameter("cad");

                // Se crea la fecha de fabricacion
                LocalDate localfecha = LocalDate.parse(FeCad);
                LocalDate fechaF = localfecha.minusYears(3);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String FeFab = fechaF.format(formatter);

                CostoCap = request.getParameter("F_Costo");
                CostoCap = CostoCap.replace(",", "");
                Costo = Double.parseDouble(CostoCap);
                try {
                    int cajas = Integer.parseInt((request.getParameter("Cajas")).replace(",", ""));
                    int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                    int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));

                    con.conectar();

                    ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + cla_pro + "'");
                    while (rset_medica.next()) {
                        Tipo = rset_medica.getString("F_TipMed");
                        //Costo = Double.parseDouble(rset_medica.getString("F_Costo"));
                        if (Tipo.equals("2504")) {
                            IVA = 0.0;
                        } else {
                            IVA = 0.16;
                        }
                    }

                    //String FeFab = anofec + "-" + cmm + "-" + cdd;
                    String CodBar = request.getParameter("cb");
                    String Tarimas = request.getParameter("TarimasC");
                    String claPro = request.getParameter("claPro");
                    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
                    String F_Obser = (new String(a, "UTF-8")).toUpperCase();

                    String TCajas = request.getParameter("TCajas");
                    TCajas = TCajas.replace(",", "");

                    if (CodBar.equals("")) {
                        ban1 = 2;
                        throw new Exception("Código de barrras vacío");
                    }
                    if (Tarimas.equals("")) {
                        Tarimas = "0";
                    }
                    Tarimas = Tarimas.replace(",", "");
                    String Cajas = request.getParameter("CajasxTC");
                    if (Cajas.equals("")) {
                        Cajas = "0";
                    }
                    Cajas = Cajas.replace(",", "");
                    String Piezas = request.getParameter("Piezas");
                    if (Piezas.equals("")) {
                        throw new Exception("Número de piezas en 0");
                    }
                    Piezas = Piezas.replace(",", "");
                    String TarimasI = request.getParameter("TarimasI");
                    if (TarimasI.equals("")) {
                        TarimasI = "0";
                    }
                    TarimasI = TarimasI.replace(",", "");
                    String CajasxTI = request.getParameter("CajasxTI");
                    if (CajasxTI.equals("")) {
                        CajasxTI = "0";
                    }
                    CajasxTI = CajasxTI.replace(",", "");
                    String Resto = request.getParameter("Resto");
                    if (Resto.equals("")) {
                        Resto = "0";
                    }
                    PzsxCC = request.getParameter("PzsxCC");
                    if (PzsxCC.equals("")) {
                        PzsxCC = "0";
                    }
                    String factorEmpaque = request.getParameter("factorEmpaque");
                    if (factorEmpaque.equals("")) {
                        factorEmpaque = "1";
                    }
                    String origen = request.getParameter("F_Origen");
                    System.out.println("origen " + origen);
                    String ordenSuministro = request.getParameter("ordenSuministro");
                    cajas = Integer.parseInt(Tarimas) * Integer.parseInt(Cajas);
                    PzsxCC = PzsxCC.replace(",", "");
                    Resto = Resto.replace(",", "");
                    IVAPro = (Double.parseDouble(Piezas) * Costo) * IVA;
                    Monto = Double.parseDouble(Piezas) * Costo;
                    MontoIva = Monto + IVAPro;
                    // con.insertar("insert into tb_compratemp values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + Cajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzsxCC + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "', '" + request.getParameter("F_Proyectos") + "','','','', curtime()) ");
                    con.insertar("insert into tb_compratemp values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + cajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzsxCC + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "', '" + request.getParameter("F_Proyectos") + "','','','', curtime(), " + factorEmpaque + ",'" + ordenSuministro + "') ");
                    //con.insertar("insert into tb_compraregistro values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "') ");
                    con.insertar("insert into tb_cb values(0,'" + request.getParameter("cb") + "','" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "', '" + request.getParameter("Marca") + "')");
                    // con.insertar("insert into tb_pzcaja values (0,'" + request.getParameter("provee") + "','" + Marca + "','" + request.getParameter("PzsxCC") + "','" + cla_pro.toUpperCase() + "','" + cla_pro.toUpperCase() + "')");
                    con.cierraConexion();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            /*capturar Proyecto*/
                ///Ingreso manual
            if (request.getParameter("accion").equals("capturarProyecto")) {
                ban2 = 2;
                tipcap = 2;
                System.out.println("Entrada manual captura por Proyecto");
                String cla_pro = request.getParameter("clave1");
                String Tipo = "", FechaC = "", FechaF = "", CostoCap = "", PzsxCC = "";
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                int fcdu = 0, anofec = 0;
                String lot_pro = request.getParameter("Lote").toUpperCase();
                String FeCad = request.getParameter("cad");

                // Se crea la fecha de fabricacion
                LocalDate localfecha = LocalDate.parse(FeCad);
                LocalDate fechaF = localfecha.minusYears(3);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String FeFab = fechaF.format(formatter);

                CostoCap = request.getParameter("F_Costo");
                CostoCap = CostoCap.replace(",", "");
                Costo = Double.parseDouble(CostoCap);
                try {
                    int cajas = Integer.parseInt((request.getParameter("TCajas")).replace(",", ""));
                    int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                    int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));

                    con.conectar();

                    ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + cla_pro + "'");
                    while (rset_medica.next()) {
                        Tipo = rset_medica.getString("F_TipMed");
                        
                        if (Tipo.equals("2504")) {
                            IVA = 0.0;
                        } else {
                            IVA = 0.16;
                        }
                    }

                   
                    String CodBar = request.getParameter("cb");
                    String Tarimas = request.getParameter("TarimasC");
                    String claPro = request.getParameter("claPro");
                    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
                    String F_Obser = (new String(a, "UTF-8")).toUpperCase();

                    String TCajas = request.getParameter("TCajas");
                    TCajas = TCajas.replace(",", "");
                    
                    
                    if (CodBar.equals("")) {
                      
                        throw new Exception("Código de barrras vacío");
                    }
                    if (Tarimas.equals("")) {
                        Tarimas = "0";
                    }
                    Tarimas = Tarimas.replace(",", "");
                    String Cajas = request.getParameter("CajasxTC");
                    if (Cajas.equals("")) {
                        Cajas = "0";
                    }
                    Cajas = Cajas.replace(",", "");
                    String Piezas = request.getParameter("Piezas");
                    if (Piezas.equals("")) {
                        throw new Exception("Número de piezas en 0");
                    }
                    Piezas = Piezas.replace(",", "");
//                    String TarimasI = request.getParameter("TarimasI");
//                    if (TarimasI.equals("")) {
                      String  TarimasI = "0";
//                    }
//                    TarimasI = TarimasI.replace(",", "");
//                    String CajasxTI = request.getParameter("CajasxTI");
//                    if (CajasxTI.equals("")) {
                      String  CajasxTI = "0";
//                    }
//                    CajasxTI = CajasxTI.replace(",", "");
//                    String Resto = request.getParameter("Resto");
//                    if (Resto.equals("")) {
                      String  Resto = "0";
//                    }
//                    PzsxCC = request.getParameter("PzsxCC");
//                    if (PzsxCC.equals("")) {
                        PzsxCC = "1";
//                    }
//                    String factorEmpaque = request.getParameter("factorEmpaque");
//                    if (factorEmpaque.equals("")) {
                     String   factorEmpaque = "1";
//                    }
                    String origen = request.getParameter("F_Origen");
                    System.out.println("origen " + origen);
                    String ordenSuministro = request.getParameter("ordenSuministro");
                    cajas = Integer.parseInt(Cajas);
                    PzsxCC = PzsxCC.replace(",", "");
                    Resto = Resto.replace(",", "");
                    IVAPro = (Double.parseDouble(Piezas) * Costo) * IVA;
                    Monto = Double.parseDouble(Piezas) * Costo;
                    MontoIva = Monto + IVAPro;
                     con.insertar("insert into tb_compratemp values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + cajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzsxCC + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("folio_remi") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "', '" + request.getParameter("F_Proyectos") + "','','','', curtime(), " + factorEmpaque + ",'" + ordenSuministro + "') ");
                    con.insertar("insert into tb_cb values(0,'" + request.getParameter("cb") + "','" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "', '" + request.getParameter("Marca") + "')");
                    con.cierraConexion();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            
            

            //cuando cuenta es  diferente de 0
            if (request.getParameter("accion").equals("capturarcb")) {
                System.out.println("Entrada manual2");
                ban1 = 1;
                String cla_pro = request.getParameter("clave1");
                String Tipo = "", FechaC = "", FechaF = "";
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                String lot_pro = request.getParameter("Lote");
                String FeCad = df3.format(df2.parse(request.getParameter("cdd")));
                String FeFab = df3.format(df2.parse(request.getParameter("fdd")));

                try {
                    int cajas = Integer.parseInt((request.getParameter("Cajas")).replace(",", ""));
                    int piezas = Integer.parseInt((request.getParameter("Piezas")).replace(",", ""));
                    int tarimas = Integer.parseInt((request.getParameter("Tarimas")).replace(",", ""));

                    con.conectar();

                    ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + cla_pro + "'");
                    while (rset_medica.next()) {
                        Tipo = rset_medica.getString("F_TipMed");
                        Costo = Double.parseDouble(rset_medica.getString("F_Costo"));
                        if (Tipo.equals("2504")) {
                            IVA = 0.0;
                        } else {
                            IVA = 0.16;
                        }
                    }

                    String Tarimas = request.getParameter("Tarimas");
                    byte[] a = request.getParameter("Observaciones").getBytes("ISO-8859-1");
                    String F_Obser = (new String(a, "UTF-8")).toUpperCase();

                    String TCajas = request.getParameter("TCajas");
                    TCajas = TCajas.replace(",", "");

                    if (Tarimas.equals("")) {
                        Tarimas = "0";
                    }
                    Tarimas = Tarimas.replace(",", "");
                    String Cajas = request.getParameter("Cajas");
                    if (Cajas.equals("")) {
                        Cajas = "0";
                    }
                    Cajas = Cajas.replace(",", "");

                    String Piezas = request.getParameter("Piezas");
                    if (Piezas.equals("")) {
                        Piezas = "0";
                    }
                    Piezas = Piezas.replace(",", "");

                    String TarimasI = request.getParameter("TarimasI");
                    if (TarimasI.equals("")) {
                        TarimasI = "0";
                    }
                    TarimasI = TarimasI.replace(",", "");
                    String CajasxTI = request.getParameter("CajasxTI");
                    if (CajasxTI.equals("")) {
                        CajasxTI = "0";
                    }
                    CajasxTI = CajasxTI.replace(",", "");
                    String Resto = request.getParameter("Resto");
                    if (Resto.equals("")) {
                        Resto = "0";
                    }
                    String ordenSuministro = request.getParameter("ordenSuministro");
                    String factorEmpaque = request.getParameter("factorEmpaque");
                    if (factorEmpaque.equals("")) {
                        factorEmpaque = "1";
                    }
                    Resto = Resto.replace(",", "");
                    IVAPro = (Double.parseDouble(Piezas) * Costo) * IVA;
                    Monto = Double.parseDouble(Piezas) * Costo;
                    MontoIva = Monto + IVAPro;
                    //con.insertar("insert into tb_compratemp values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "','','','', curtime()) ");
                    con.insertar("insert into tb_compratemp values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "','','','', curtime(), " + factorEmpaque + "),'" + ordenSuministro + "' ");
//                    con.insertar("insert into tb_compraregistro values (0,curdate(),'" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "','" + request.getParameter("Marca") + "','" + request.getParameter("provee") + "','" + request.getParameter("cb") + "', '" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "' , '" + request.getParameter("folio_remi") + "', '" + request.getParameter("orden") + "','" + request.getParameter("provee") + "' ,'" + sesion.getAttribute("nombre") + "') ");
                    con.insertar("insert into tb_cb values(0,'" + request.getParameter("cb") + "','" + cla_pro.toUpperCase() + "','" + lot_pro + "','" + FeCad + "','" + FeFab + "', '" + request.getParameter("Marca") + "')");
//                    con.insertar("insert into tb_pzcaja values (0,'" + request.getParameter("provee") + "','" + Marca + "','" + request.getParameter("PzsxCC") + "','" + cla_pro.toUpperCase() + "','" + cla_pro.toUpperCase() + "')");

                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        request.getSession().setAttribute("folio", request.getParameter("folio"));
        request.getSession().setAttribute("fecha", request.getParameter("fecha"));
        request.getSession().setAttribute("folio_remi", request.getParameter("folio_remi"));
        request.getSession().setAttribute("orden", request.getParameter("orden"));
        request.getSession().setAttribute("provee", request.getParameter("provee"));
        request.getSession().setAttribute("recib", request.getParameter("recib"));
        request.getSession().setAttribute("entrega", request.getParameter("entrega"));
        request.getSession().setAttribute("clave", clave);
        request.getSession().setAttribute("descrip", descr);
        request.getSession().setAttribute("cuenta", Cuenta);
        request.getSession().setAttribute("cb", cb);
        request.getSession().setAttribute("codbar2", codbar2);
        request.getSession().setAttribute("Marca", Marca);
        request.getSession().setAttribute("PresPro", PresPro);

//        if (tipcap == 1) {
            if (ban1 == 0) {
                out.println("<script>alert('Clave Inexistente')</script>");
                out.println("<script>window.location='captura.jsp'</script>");
            } else if (ban1 == 1) {
                out.println("<script>window.location='captura.jsp'</script>");
            } else if (ban1 == 2) {
                request.getSession().setAttribute("CBInex", "1");
                out.println("<script>alert('CB Inexistente, Favor de Llenar todos los Campos')</script>");
                out.println("<script>window.location='captura.jsp'</script>");
            } else if (ban1 == 3) {
                out.println("<script>alert('Usuario " + usua + " no tienes permisos para ingresar medicamento controlado ')</script>");
                out.println("<script>window.location='captura.jsp'</script>");
            }
//        } else {
//            if (ban2 == 0) {
//                out.println("<script>alert('Clave No Existe En Catalogo')</script>");
//                out.println("<script>window.location='capturaProyecto.jsp'</script>");
//            } else if (ban2 == 1) {
//                out.println("<script>alert('Clave Tipo Controlado')</script>");
//                out.println("<script>window.location='capturaProyecto.jsp'</script>");
//            } else if (ban2 == 2) {
//                out.println("<script>window.location='capturaProyecto.jsp'</script>");
//            } else if (ban2 == 3) {
//                out.println("<script>alert('Clave NO Habilitada Para Proyecto')</script>");
//                out.println("<script>window.location='capturaProyecto.jsp'</script>");
//            }
//
//        }
        
       
        //response.sendRedirect("captura.jsp");
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
