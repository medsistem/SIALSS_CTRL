/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.gnk.impl.FacturacionTranDaoImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 * Proceso de la facturación automática Imprimir multiples folios de entrega,
 * listado de folios y modificación de fechas entrega
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Facturacion extends HttpServlet {

    java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        String clave = "", descr = "";
        int ban1 = 0;
        String remis = request.getParameter("fol_gnkl");

        ConectionDB con = new ConectionDB();
        //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
        try {
            if (request.getParameter("eliminar") != null) {

                con.conectar();
                con.insertar("update tb_unireq set F_Status = '1' where F_ClaUni = '" + request.getParameter("eliminar") + "' and F_Status=0");
                con.cierraConexion();
                //out.println("<script>alert('Eliminación Correcta')</script>");
                //out.println("<script>window.location='factura.jsp'</script>");
            }
        } catch (Exception e) {
        }
        try {

            ////IMPRESIONES MULTIPLES//////
            ///////////////////////////////
            if (request.getParameter("accion").equals("impRemisMultples")) {
                con.conectar();
                String Impresora = request.getParameter("impresora");
                String Copy = request.getParameter("Copy");
                String[] claveschk = request.getParameterValues("checkRemis");
                String remisionesReImp = "";
                String TipoInsumo = request.getParameter("TipoInsumo");
                System.out.println("bandera de tipo: "+TipoInsumo);
                
                System.out.println("Impresora:" + Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if (!(Impresora.equals(""))) {
                    if (!(Copy.equals(""))) {
                        
                        con.actualizar("DELETE FROM tb_folioimp WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                        for (int i = 0; i < claveschk.length; i++) {
                            System.out.println("claveschk: " + claveschk[i]);
                            int posi = claveschk[i].indexOf('-');
                            String Folio = claveschk[i].substring(0, posi);
                            String Proyecto = claveschk[i].substring(posi + 1, claveschk[i].length());
                            con.actualizar("INSERT INTO tb_folioimp VALUES('" + Folio + "','" + Copy + "','" + sesion.getAttribute("nombre") + "','" + Proyecto + "',0);");
                            //out.println(" <script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "&Impresora="+Impresora+"&Copy="+Copy+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                            //out.println("<script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "', '_blank')</script>");
                            if (i == (claveschk.length - 1)) {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                                out.println("remisionesReImp:" + remisionesReImp);
                            } else {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                                out.println("remisionesReImp:" + remisionesReImp);
                            }
                        }
                        out.println(" <script>window.open('reportes/multiplesRemis.jsp?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "&Tipo="+TipoInsumo+" ', '','width=500,height=500,left=50,top=50,toolbar=no'); </script>");
                        //out.println(" <script>window.open('ImprimeFolioMultiple?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                        out.println("<script>window.history.back()</script>");
                        out.println("remisionesReImp:" + remisionesReImp);
                    } else {
                        out.println("<script>alert('Favor de Seleccionar No Copias')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            
            ////IMPRESION ENSERES//////
            ///////////////////////////////
            if (request.getParameter("accion").equals("impRemisMultplesEnseres")) {
                con.conectar();
                String Impresora = request.getParameter("impresora");
                String Copy = request.getParameter("Copy");
                String[] claveschk = request.getParameterValues("checkRemis");
                String remisionesReImp = "";
                System.out.println("Impresora:" + Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if (!(Impresora.equals(""))) {
                    if (!(Copy.equals(""))) {
                        con.actualizar("DELETE FROM tb_folioimp WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                        for (int i = 0; i < claveschk.length; i++) {
                            System.out.println("claveschk: " + claveschk);
                            int posi = claveschk[i].indexOf('-');
                            String Folio = claveschk[i].substring(0, posi);
                            String Proyecto = claveschk[i].substring(posi + 1, claveschk[i].length());
                            con.actualizar("INSERT INTO tb_folioimp VALUES('" + Folio + "','" + Copy + "','" + sesion.getAttribute("nombre") + "','" + Proyecto + "',0);");
                            //out.println(" <script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "&Impresora="+Impresora+"&Copy="+Copy+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                            //out.println("<script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "', '_blank')</script>");
                            if (i == (claveschk.length - 1)) {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                                out.println("remisionesReImp:" + remisionesReImp);
                            } else {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                                out.println("remisionesReImp:" + remisionesReImp);
                            }
                        }
                        out.println(" <script>window.open('reportes/multiplesRemisEnseres.jsp?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        //out.println(" <script>window.open('ImprimeFolioMultiple?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                        out.println("<script>window.history.back()</script>");
                        out.println("remisionesReImp:" + remisionesReImp);
                    } else {
                        out.println("<script>alert('Favor de Seleccionar No Copias')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            
            ////PARA IMPRIMIR  VARIOS FOLIOS A LA VEZ DE SURTIDO///
            if (request.getParameter("accion").equals("impRemisMultplesMich")) {
                con.conectar();
                String Impresora = request.getParameter("impresora");
                String Copy = request.getParameter("Copy");
                String[] claveschk = request.getParameterValues("checkRemis");
                String remisionesReImp = "";
                String TipoInsumo = request.getParameter("TipoInsumo");
                System.out.println("bandera de tipo: "+TipoInsumo);
                System.out.println("Impresora:" + Impresora);
                if (!(Impresora.equals(""))) {
                    if (!(Copy.equals(""))) {
                        ban1=1;
                        con.actualizar("DELETE FROM tb_folioimp WHERE F_User='" + sesion.getAttribute("nombre") + "';");
                        for (int i = 0; i < claveschk.length; i++) {
                            System.out.println("claveschk: " + claveschk);
                            int posi = claveschk[i].indexOf('-');
                            String Folio = claveschk[i].substring(0, posi);
                            String Proyecto = claveschk[i].substring(posi + 1, claveschk[i].length());
                            con.actualizar("INSERT INTO tb_folioimp VALUES('" + Folio + "','" + Copy + "','" + sesion.getAttribute("nombre") + "','" + Proyecto + "',0);");
                            if (i == (claveschk.length - 1)) {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                                out.println("remisionesReImp:" + remisionesReImp);
                            } else {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                                out.println("remisionesReImp:" + remisionesReImp);
                            }
                        }
                        out.println(" <script>window.open('reportes/multiplesRemisMich.jsp?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "&Tipo="+TipoInsumo+" ', '','width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        out.println("<script>window.history.back()</script>");
                        out.println("remisionesReImp:" + remisionesReImp);
                    } else {
                        out.println("<script>alert('Favor de Seleccionar No Copias')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }

//            if (request.getParameter("accion").equals("impRemisMultplesSurtido")) {
//                con.conectar();
//                String Impresora = request.getParameter("impresora");
//                String Copy = request.getParameter("Copy");
//                String[] claveschk = request.getParameterValues("checkRemis");
//                String remisionesReImp = "";
//                System.out.println("Impresora:" + Impresora);
//                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
//                if (!(Impresora.equals(""))) {
//                    if (!(Copy.equals(""))) {
//                        con.actualizar("DELETE FROM tb_folioimp WHERE F_User='" + sesion.getAttribute("nombre") + "'");
//                        for (int i = 0; i < claveschk.length; i++) {
//                            System.out.println("claveschk: " + claveschk);
//                            int posi = claveschk[i].indexOf('-');
//                            String Folio = claveschk[i].substring(0, posi);
//                            String Proyecto = claveschk[i].substring(posi + 1, claveschk[i].length());
//
//                            con.actualizar("INSERT INTO tb_folioimp VALUES('" + Folio + "','" + Copy + "','" + sesion.getAttribute("nombre") + "','" + Proyecto + "',0)");
//                            //out.println(" <script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "&Impresora="+Impresora+"&Copy="+Copy+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
//                            //out.println("<script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "', '_blank')</script>");
//                            if (i == (claveschk.length - 1)) {
//                                remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
//                                out.println("remisionesReImp:" + remisionesReImp);
//                            } else {
//                                remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
//                                out.println("remisionesReImp:" + remisionesReImp);
//                            }
//                        }
//                        out.println(" <script>window.open('reportes/multiplesRemisSurtido.jsp?Impresora=" + Impresora + "&User=" + sesion.getAttribute("nombre") + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
//                        //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
//                        out.println("<script>window.history.back()</script>");
//                        out.println("remisionesReImp:" + remisionesReImp);
//                    } else {
//                        out.println("<script>alert('Favor de Seleccionar No Copias')</script>");
//                        out.println("<script>window.history.back()</script>");
//                    }
//                } else {
//                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
//                    out.println("<script>window.history.back()</script>");
//                }
//                con.cierraConexion();
//            }
            if (request.getParameter("accion").equals("ImpRelacion")) {
                con.conectar();
                int LargoF = 0;
                String FechaFol = "", ContFolio = "", MuestraFolio = "", FecMin = "", FecMax = "", MuestraProyecto = "", ContarProyecto = "";
                String Radio = request.getParameter("radio1");
                String Folio1 = request.getParameter("folio11");
                String unidad = request.getParameter("unidad1");
                String Folio2 = request.getParameter("folio21");
                String Fecha1 = request.getParameter("fecha_ini1");
                String Fecha2 = request.getParameter("fecha_fin1");
                String Impresora = request.getParameter("impresora");
                String Proyecto = request.getParameter("Proyecto1");

                String QUni = "", QFolio = "", QFecha = "", Query = "", AND = "";
                int ban = 0, ban2 = 0, ban3 = 0;
                if (Proyecto.equals("0")) {
                    AND = "";
                } else {
                    AND = " AND f.F_Proyecto='" + Proyecto + "' ";
                }
                if (unidad != "") {
                    ban = 1;
                }
                if (Folio1 != "" && Folio2 != "") {
                    ban2 = 1;
                }
                if (Fecha1 != "" && Fecha2 != "") {
                    ban3 = 1;
                }
                if (ban == 1) {
                    QUni = " WHERE f.F_ClaCli = '" + unidad + "' ";
                }
                if (ban2 == 1) {
                    if (ban == 0) {
                        QFolio = " WHERE F_ClaDoc between '" + Folio1 + "' and '" + Folio2 + "' ";
                    } else {
                        QFolio = " AND F_ClaDoc between '" + Folio1 + "' and '" + Folio2 + "' ";
                    }
                }

                if (ban3 == 1) {
                    if (ban == 0 && ban2 == 0) {
                        QFecha = " WHERE F_FecEnt between '" + Fecha1 + "' and '" + Fecha2 + "' ";
                    } else {
                        QFecha = " AND F_FecEnt between '" + Fecha1 + "' and '" + Fecha2 + "' ";
                    }
                }

                Query = QUni + QFolio + QFecha;

                String remisionesReImp = "";
                System.out.println("Impresora:" + Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if (!(Impresora.equals(""))) {

                    con.actualizar("delete from tb_imprelacion where F_User='" + sesion.getAttribute("nombre") + "';");
                    ResultSet Folios = con.consulta("SELECT F_ClaDoc, f.F_Proyecto, f.F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id " + Query + " " + AND + " GROUP BY F_ClaDoc, f.F_Proyecto;");
                    while (Folios.next()) {
                        ContFolio = Folios.getString(1);
                        con.insertar("INSERT INTO tb_imprelacion VALUES (0,'" + Folios.getString(1) + "','" + Folios.getString(2) + "','" + sesion.getAttribute("nombre") + "','" + Folios.getString(3) + "','','');");
                    }
                    con.actualizar("UPDATE tb_imprelacion AS t1 INNER JOIN ( SELECT CONCAT( '[', MIN( DATE_FORMAT(F_FechaE, '%d/%m/%Y')), ']' ) AS FECMIN, CONCAT( '[', MAX( DATE_FORMAT(F_FechaE, '%d/%m/%Y')), ']' ) AS FECMAX FROM tb_imprelacion WHERE F_User = '" + sesion.getAttribute("nombre") + "' ) AS t2 SET t1.F_Min = t2.FECMIN, t1.F_Max = t2.FECMAX WHERE t1.F_User = '" + sesion.getAttribute("nombre") + "';");

                    //out.println(" <script>window.open('reportes/ImprimeRelacion.jsp?FecMin=" + FecMin + "&FecMax=" + FecMax + "&Impresora=" + Impresora + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    out.println(" <script>window.open('reportes/ImprimeRelacion.jsp?Impresora=" + Impresora + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                    out.println("<script>window.history.back()</script>");
                    MuestraFolio = "";
                } else {
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("recalendarizarRemis")) {
                con.conectar();
                try {
                    String[] claveschk = request.getParameterValues("checkRemis");
                    String remisionesReCal = "";
                    String remisionesReImp = "";
                    
                    for (int i = 0; i < claveschk.length; i++) {
                            System.out.println("claveschk: " + claveschk);
                            int posi = claveschk[i].indexOf('-');
                            String Folio = claveschk[i].substring(0, posi);
                            String Proyecto = claveschk[i].substring(posi + 1, claveschk[i].length());
                            con.insertar("update tb_factura set F_FecEnt = '" + request.getParameter("F_FecEnt") + "' where F_ClaDoc ='" + Folio + "' AND F_Proyecto = '"+Proyecto+"';");
                            if (i == (claveschk.length - 1)) {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                                //out.println("remisionesReImp:" + remisionesReImp);
                            } else {
                                remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                                //out.println("remisionesReImp:" + remisionesReImp);
                            }
                        }
                    
                    /*
                    for (int i = 0; i < claveschk.length; i++) {
                        if (i == (claveschk.length - 1)) {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "'";
                        } else {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "',";
                        }
                    }
                    out.println(remisionesReCal);*/

                    //con.insertar("update tb_factura set F_FecEnt = '" + request.getParameter("F_FecEnt") + "' where F_ClaDoc in (" + remisionesReCal + ")");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("validarVariasAuditor")) {
                con.conectar();
                String[] claveschk = request.getParameterValues("chkId");
                for (int i = 0; i < claveschk.length; i++) {
                    con.insertar("update tb_facttemp set F_StsFact='2' WHERE F_Id= '" + claveschk[i] + "'");
                    con.insertar("insert into tb_regvalida values ('" + claveschk[i] + "','" + sesion.getAttribute("nombre") + "',0)");
                }
                con.cierraConexion();

                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('Claves Validadas Correctamente')</script>");
                out.println("<script>window.location='validacionAuditores.jsp'</script>");
            }
            if (request.getParameter("accion").equals("validarVariasSurtido")) {
                con.conectar();
                String[] claveschk = request.getParameterValues("chkId");
                for (int i = 0; i < claveschk.length; i++) {
                    con.insertar("update tb_facttemp set F_StsFact='4', F_User='" + sesion.getAttribute("nombre") + "' WHERE F_Id= '" + claveschk[i] + "'");
                    con.insertar("insert into tb_regvalida values ('" + claveschk[i] + "','" + sesion.getAttribute("nombre") + "',0)");
                }
                con.cierraConexion();

                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('Claves Validadas Correctamente')</script>");
                out.println("<script>window.location='remisionarCamion.jsp'</script>");
            }
            if (request.getParameter("accion").equals("actualizarCBAuditor")) {
                try {
                    con.conectar();
                    con.insertar("update tb_lote set F_Cb='" + request.getParameter("F_Cb") + "' WHERE F_FolLot= '" + request.getParameter("F_FolLot") + "'");
                    con.insertar("update tb_compra set F_Cb='" + request.getParameter("F_Cb") + "' WHERE F_FolLot= '" + request.getParameter("F_FolLot") + "'");
                    con.cierraConexion();
                } catch (Exception e) {

                }
                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('CB actualizado Correctamente, ingrese el CB')</script>");
                out.println("<script>alert('Reimprima el Marbete Correcto')</script>");
                out.println("<script>window.location='validacionAuditores.jsp'</script>");
            }
            if (request.getParameter("accion").equals("actualizarCB")) {
                try {
                    con.conectar();
                    con.insertar("update tb_lote set F_Cb='" + request.getParameter("F_Cb") + "' WHERE F_FolLot= '" + request.getParameter("F_FolLot") + "'");
                    con.insertar("update tb_compra set F_Cb='" + request.getParameter("F_Cb") + "' WHERE F_FolLot= '" + request.getParameter("F_FolLot") + "'");
                    con.cierraConexion();
                } catch (Exception e) {

                }
                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('CB actualizado Correctamente, ingrese el CB')</script>");
                out.println("<script>alert('Reimprima el Marbete Correcto')</script>");
                out.println("<script>window.location='validacionSurtido.jsp'</script>");
            }
            if (request.getParameter("accion").equals("validaAuditor")) {
                try {
                    con.conectar();
                    con.insertar("update tb_facttemp set F_StsFact='2' WHERE F_Id= '" + request.getParameter("folio") + "'");
                    con.insertar("insert into tb_regvalida values ('" + request.getParameter("folio") + "','" + sesion.getAttribute("nombre") + "',0)");
                    con.cierraConexion();
                } catch (Exception e) {

                }
                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('Clave Validada Correctamente')</script>");
                out.println("<script>window.location='validacionAuditores.jsp'</script>");
            }
            if (request.getParameter("accion").equals("validaRegistro")) {
                try {
                    con.conectar();
                    con.insertar("update tb_facttemp set F_StsFact='4', F_User='" + sesion.getAttribute("nombre") + "' WHERE F_Id= '" + request.getParameter("folio") + "'");
                    con.insertar("insert into tb_regvalida values ('" + request.getParameter("folio") + "','" + sesion.getAttribute("nombre") + "',0)");
                    con.cierraConexion();
                } catch (Exception e) {

                }
                sesion.setAttribute("Nombre", request.getParameter("Nombre"));
                out.println("<script>alert('Clave Validada Correctamente')</script>");
                out.println("<script>window.location='remisionarCamion.jsp'</script>");
            }
            if (request.getParameter("accion").equals("EliminaConcentrado")) {
                try {
                    con.conectar();
                    sesion.setAttribute("F_IndGlobal", null);

                    ResultSet rset = con.consulta("select * from tb_facttemp where F_IdFact = '" + request.getParameter("fol_gnkl") + "'");
                    while (rset.next()) {
                        con.insertar("insert into tb_facttemp_elim values ('" + rset.getString(1) + "','" + rset.getString(2) + "','" + rset.getString(3) + "','" + rset.getString(4) + "','" + rset.getString(5) + "','" + rset.getString(6) + "','" + rset.getString(7) + "', '" + (String) sesion.getAttribute("nombre") + "', NOW())");
                    }
                    con.insertar("delete from tb_facttemp WHERE F_IdFact = '" + request.getParameter("fol_gnkl") + "'");

                    con.cierraConexion();
                } catch (Exception e) {

                }
                response.sendRedirect("reimpConcentrado.jsp");
            }
            if (request.getParameter("accion").equals("consultarAuto")) {
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT * FROM tb_unireq WHERE F_ClaUni = '" + request.getParameter("Nombre") + "' GROUP BY F_ClaUni");
                    while (rset.next()) {
                        ban1 = 1;
                        clave = rset.getString("F_ClaUni");
                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }

                out.println("<script>window.location='facturaAuto.jsp'</script>");
            }

            if (request.getParameter("accion").equals("cancelar")) {
                try {

                    ban1 = 1;
                    String ClaUni = request.getParameter("Nombre");
                    String FechaE = request.getParameter("FecFab");
                    String Clave = "", FolioLote = "";
                    int piezas = 0, existencia = 0, diferencia = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, Org = 0, piezasDif = 0;

                    String[] claveschk = request.getParameterValues("chkUniFact");
                    String Unidades = "";
                    for (int i = 0; i < claveschk.length; i++) {
                        if (i == (claveschk.length - 1)) {
                            Unidades = Unidades + "'" + claveschk[i] + "'";
                        } else {
                            Unidades = Unidades + "'" + claveschk[i] + "',";
                        }
                    }
                    out.println(Unidades);
                    con.conectar();
                    ban1 = 1;
                    con.insertar("update tb_unireq set F_Status = '1' where F_ClaUni in (" + Unidades + ") and F_Status=0 ");
                    con.cierraConexion();
                } catch (Exception e) {

                }

                response.setContentType("text/html");
                request.setAttribute("F_FecEnt", request.getParameter("F_FecEnt"));
                request.getRequestDispatcher("facturaAtomatica.jsp").forward(request, response);
            }
            if (request.getParameter("accion").equals("cancelarCause")) {
                try {

                    ban1 = 1;
                    String ClaUni = request.getParameter("Nombre");
                    String FechaE = request.getParameter("FecFab");
                    String Clave = "", FolioLote = "";
                    int piezas = 0, existencia = 0, diferencia = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, Org = 0, piezasDif = 0;

                    String[] claveschk = request.getParameterValues("chkUniFact");
                    String Unidades = "";
                    for (int i = 0; i < claveschk.length; i++) {
                        if (i == (claveschk.length - 1)) {
                            Unidades = Unidades + "'" + claveschk[i] + "'";
                        } else {
                            Unidades = Unidades + "'" + claveschk[i] + "',";
                        }
                    }
                    out.println(Unidades);
                    con.conectar();
                    ban1 = 1;
                    con.insertar("update tb_unireq set F_Status = '1' where F_ClaUni in (" + Unidades + ") and F_Status=0 ");
                    con.cierraConexion();
                } catch (Exception e) {

                }

                response.setContentType("text/html");
                request.setAttribute("F_FecEnt", request.getParameter("F_FecEnt"));
                request.getRequestDispatcher("facturaAtomaticaCause.jsp").forward(request, response);
            }
            //-------------------------------------------------------------------------------------------------
            /*if (request.getParameter("accion").equals("guardarGlobalAuto")) {

                ban1 = 1;
                String ClaUni = request.getParameter("Nombre");
                String FechaE = request.getParameter("F_FecEnt");
                String Clave = "", FolioLote = "";
                int piezas = 0, existencia = 0, diferencia = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, Org = 0, piezasDif = 0;

                try {

                    con.conectar();

                    con.insertar("DROP TABLE IF EXISTS tb_lotetemp" + (String) sesion.getAttribute("nombre"));
                    con.insertar("create table tb_lotetemp" + (String) sesion.getAttribute("nombre") + " select * from tb_lote");
                    ResultSet FolioFact = con.consulta("SELECT F_IndGlobal FROM tb_indice");
                    while (FolioFact.next()) {
                        FolioFactura = Integer.parseInt(FolioFact.getString("F_IndGlobal"));
                    }
                    FolFact = FolioFactura + 1;
                    con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");

                    ResultSet rset_cantidad = con.consulta("SELECT F_ClaPro,SUM(F_CajasReq) as cajas, SUM(F_PiezasReq) as piezas, F_IdReq FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_Status='0'  GROUP BY F_ClaPro");
                    while (rset_cantidad.next()) {
                        Clave = rset_cantidad.getString("F_ClaPro");
                        int cajasReq = Integer.parseInt(rset_cantidad.getString("cajas"));
                        int piezasReq = Integer.parseInt(rset_cantidad.getString("piezas"));
                        int pzxCaja = 0;
                        ResultSet rsetCP = con.consulta("select F_Pzs from tb_pzxcaja where F_ClaPro = '" + Clave + "' ");
                        while (rsetCP.next()) {
                            pzxCaja = rsetCP.getInt(1);
                        }
                        piezas = (pzxCaja * cajasReq) + piezasReq;

                        String IdLote = "";
                        ResultSet r_Org = con.consulta("SELECT F_ClaOrg FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " WHERE F_ClaPro='" + Clave + "' GROUP BY F_ClaPro");
                        while (r_Org.next()) {
                            Org = Integer.parseInt(r_Org.getString("F_ClaOrg"));

                            if (Org == 1) {
                                ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,(L.F_ExiLot) AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot,F_IdLote FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' and L.F_Ubica !='REJA_DEVOL'  GROUP BY L.F_IdLote ORDER BY L.F_Origen, L.F_FecCad,L.F_IdLote ASC");
                                while (FechaLote.next()) {
                                    FolioLote = FechaLote.getString("F_FolLot");
                                    IdLote = FechaLote.getString("F_IdLote");
                                    existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                    ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_facttemp where F_IdLot = '" + IdLote + "' and F_StsFact!=5");
                                    while (rset2.next()) {
                                        existencia = existencia - rset2.getInt(1);
                                    }
                                    Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                    if (existencia > 0) {
                                        if (piezas > existencia) {
                                            diferencia = piezas - existencia;
                                            con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");
                                            con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + existencia + "','" + FechaE + "','0','0','','" + existencia + "','0')");

                                            piezasDif = 0;
                                            piezas = diferencia;

                                        } else {
                                            diferencia = existencia - piezas;
                                            if (diferencia > 0) {
                                                con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                if (piezas > 0) {
                                                    con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + piezas + "','" + FechaE + "','0','0','','" + piezas + "','0')");
                                                    con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                }
                                            }
                                            piezasDif = diferencia;
                                            piezas = 0;
                                        }
                                    }
                                }
                            } else {
                                ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,(L.F_ExiLot) AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot,F_IdLote FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' AND L.F_Ubica !='REJA_DEVOL'  GROUP BY L.F_IdLote ORDER BY L.F_Origen, L.F_IdLote,L.F_FecCad ASC");
                                while (FechaLote.next()) {
                                    FolioLote = FechaLote.getString("F_FolLot");
                                    IdLote = FechaLote.getString("F_IdLote");
                                    existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                    ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_facttemp where F_IdLot = '" + IdLote + "' and F_StsFact!=5");
                                    while (rset2.next()) {
                                        existencia = existencia - rset2.getInt(1);
                                    }
                                    Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                    if (existencia > 0) {
                                        if (piezas > existencia) {
                                            diferencia = piezas - existencia;
                                            con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");

                                            con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + existencia + "','" + FechaE + "','0','0','','" + existencia + "','0')");
                                            piezasDif = 0;
                                            piezas = diferencia;
                                        } else {
                                            diferencia = existencia - piezas;
                                            if (diferencia > 0) {
                                                con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");

                                                if (piezas >= 1) {
                                                    con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + piezas + "','" + FechaE + "','0','0','','" + piezas + "','0')");
                                                    con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                }
                                            }
                                            piezasDif = diferencia;
                                            piezas = 0;
                                        }
                                    }
                                }
                            }
                            if (diferencia > 0 && piezasDif == 0) {
                                con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','0','" + FechaE + "','0','0','','" + diferencia + "','0')");
                                diferencia = 0;
                                piezasDif = 0;
                            }
                        }
                        con.actualizar("update tb_unireq set F_Status='2' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");
                    }
                    RequerimientoModula reqMod = new RequerimientoModula();
                    reqMod.enviaRequerimiento(FolFact + "");
                    response.sendRedirect("reimpConcentrado.jsp");
                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getLocalizedMessage());
                }
                out.println("<script>window.open('reimpGlobalReq.jsp?fol_gnkl=" + FolFact + "','_blank')</script>");
                out.println("<script>window.open('reimpGlobalMarbetes.jsp?fol_gnkl=" + FolFact + "','_blank')</script>");
            }*/
            //-------------------------------------------------------------------------------------------------
            if (request.getParameter("accion").equals("guardarGlobal")) {

                ban1 = 1;
                String ClaUni = request.getParameter("Nombre");
                String FechaE = request.getParameter("FecFab");
                String Clave = "", FolioLote = "";
                int piezas = 0, existencia = 0, diferencia = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, Org = 0, piezasDif = 0;

                String[] claveschk = request.getParameterValues("chkUniFact");
                String Unidades = "";
                for (int i = 0; i < claveschk.length; i++) {
                    if (i == (claveschk.length - 1)) {
                        Unidades = Unidades + "'" + claveschk[i] + "'";
                    } else {
                        Unidades = Unidades + "'" + claveschk[i] + "',";
                    }
                }
                out.println(Unidades);

                try {
                    con.conectar();
                    ResultSet rset = con.consulta("select F_ClaPro, F_ClaUni from tb_unireq where F_ClaUni in( " + Unidades + ") and F_Status=0 and  F_PiezasReq != 0");
                    while (rset.next()) {
                        String ClaPro = rset.getString("F_ClaPro");
                        String F_NCant = request.getParameter(rset.getString("F_ClaUni") + "_" + ClaPro.trim());
                        con.insertar("update tb_unireq set F_PiezasReq = '" + F_NCant + "' where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaUni = '" + rset.getString("F_ClaUni") + "' and F_Status='0'");
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {

                    con.conectar();
                    //consql.conectar();

                    con.insertar("DROP TABLE IF EXISTS tb_lotetemp" + (String) sesion.getAttribute("nombre"));
                    con.insertar("create table tb_lotetemp" + (String) sesion.getAttribute("nombre") + " select * from tb_lote");
                    /*ResultSet Fechaa = con.consulta("SELECT STR_TO_DATE(" + FechaE + ", '%d/%m/%Y')");
                     while (Fechaa.next()) {
                     FechaE = Fechaa.getString("STR_TO_DATE(" + FechaE + ", '%d/%m/%Y')");
                     }*/

                    ResultSet rset = con.consulta("select f.F_ClaUni from tb_fecharuta f, tb_uniatn u where f.F_ClaUni = u.F_ClaCli and f.F_ClaUni in (" + Unidades + ") group by f.F_ClaUni ");
                    while (rset.next()) {
                        ResultSet FolioFact = con.consulta("SELECT F_IndGlobal FROM tb_indice");
                        while (FolioFact.next()) {
                            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndGlobal"));
                        }
                        FolFact = FolioFactura + 1;
                        con.actualizar("update tb_indice set F_IndGlobal='" + FolFact + "'");
                        ClaUni = rset.getString("F_ClaUni");
                        FechaE = request.getParameter("F_FecEnt");
                        /*
                         *Abre Ciclo ClaUni
                         */
                        ResultSet rset_cantidad = con.consulta("SELECT F_ClaPro,SUM(F_CajasReq) as cajas, SUM(F_PiezasReq) as piezas, F_IdReq FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_Status='0' and F_PiezasReq!=0 GROUP BY F_ClaPro");
                        while (rset_cantidad.next()) {
                            Clave = rset_cantidad.getString("F_ClaPro");
                            int cajasReq = Integer.parseInt(rset_cantidad.getString("cajas"));
                            int piezasReq = Integer.parseInt(rset_cantidad.getString("piezas"));
                            int pzxCaja = 0;
                            ResultSet rsetCP = con.consulta("select F_Pzs from tb_pzxcaja where F_ClaPro = '" + Clave + "' ");
                            while (rsetCP.next()) {
                                pzxCaja = rsetCP.getInt(1);
                            }
                            piezas = (pzxCaja * cajasReq) + piezasReq;
                            //piezas = Integer.parseInt(rset_cantidad.getString("CANTIDAD"));

                            String IdLote = "";
                            //INICIO DE CONSULTA MYSQL
                            ResultSet r_Org = con.consulta("SELECT F_ClaOrg FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " WHERE F_ClaPro='" + Clave + "' GROUP BY F_ClaPro");
                            while (r_Org.next()) {
                                Org = Integer.parseInt(r_Org.getString("F_ClaOrg"));

                                if (Org == 1) {
                                    ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,(L.F_ExiLot) AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot,F_IdLote FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' and L.F_Ubica !='REJA_DEVOL'  GROUP BY L.F_IdLote ORDER BY L.F_Origen, L.F_FecCad,L.F_IdLote ASC");
                                    while (FechaLote.next()) {
                                        FolioLote = FechaLote.getString("F_FolLot");
                                        IdLote = FechaLote.getString("F_IdLote");
                                        existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                        ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_facttemp where F_IdLot = '" + IdLote + "' and F_StsFact!=5");
                                        while (rset2.next()) {
                                            existencia = existencia - rset2.getInt(1);
                                        }
                                        Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                        if (existencia > 0) {
                                            if (piezas > existencia) {
                                                diferencia = piezas - existencia;
                                                con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");
                                                con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + existencia + "','" + FechaE + "','0','0','','" + existencia + "','0')");

                                                piezasDif = 0;
                                                piezas = diferencia;

                                            } else {
                                                diferencia = existencia - piezas;

                                                if (diferencia > 0) {
                                                    con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                    if (piezas > 0) {
                                                        con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + piezas + "','" + FechaE + "','0','0','','" + piezas + "','0')");
                                                        con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                    }
                                                }
                                                piezasDif = diferencia;
                                                piezas = 0;
                                            }
                                        }
                                    }
                                } else {
                                    ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,(L.F_ExiLot) AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot,F_IdLote FROM tb_lotetemp" + (String) sesion.getAttribute("nombre") + " L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' AND L.F_Ubica !='REJA_DEVOL'  GROUP BY L.F_IdLote ORDER BY L.F_Origen, L.F_IdLote,L.F_FecCad ASC");
                                    while (FechaLote.next()) {
                                        FolioLote = FechaLote.getString("F_FolLot");
                                        IdLote = FechaLote.getString("F_IdLote");
                                        existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                        ResultSet rset2 = con.consulta("select sum(F_Cant) from tb_facttemp where F_IdLot = '" + IdLote + "' and F_StsFact!=5");
                                        while (rset2.next()) {
                                            existencia = existencia - rset2.getInt(1);
                                        }
                                        Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                        if (existencia > 0) {
                                            if (piezas > existencia) {
                                                diferencia = piezas - existencia;
                                                con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");

                                                con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + existencia + "','" + FechaE + "','0','0','','" + existencia + "','0')");
                                                piezasDif = 0;
                                                piezas = diferencia;
                                            } else {
                                                diferencia = existencia - piezas;
                                                if (diferencia > 0) {
                                                    con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");

                                                    if (piezas >= 1) {
                                                        con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','" + piezas + "','" + FechaE + "','0','0','','" + piezas + "','0')");
                                                        con.actualizar("UPDATE tb_lotetemp" + (String) sesion.getAttribute("nombre") + " SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                                    }
                                                }
                                                piezasDif = diferencia;
                                                piezas = 0;
                                            }
                                        }
                                    }
                                }
                                /**/
                                if (diferencia > 0 && piezasDif == 0) {
                                    con.insertar("insert into tb_facttemp values('" + FolFact + "','" + ClaUni + "','" + IdLote + "','0','" + FechaE + "','0','0','','" + diferencia + "','0')");
                                    diferencia = 0;
                                    piezasDif = 0;
                                }
                            }
                            con.actualizar("update tb_unireq set F_Status='2' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");

                        }
                        con.actualizar("update tb_unireq set F_Status='2' where F_ClaUni='" + ClaUni + "' and F_Status='0' ");
                        try {
                            /*RequerimientoModula reqMod = new RequerimientoModula();
                             reqMod.enviaRequerimiento(FolFact + "");*/
                        } catch (Exception e) {
                            out.println("<script>alert('Error conexión MODULA')</script>");
                        }
                        //response.sendRedirect("reimpConcentrado.jsp");
                        /*
                         * Cierra Ciclo
                         */
                    }
                    //con.actualizar("delete * FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_FecCarg = CURDATE()");
                    con.cierraConexion();
                    //consql.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getLocalizedMessage());
                }
                out.println("<script>window.location='reimpConcentrado.jsp'</script>");
                //out.println("<script>window.open('reimpGlobalReq.jsp?fol_gnkl=" + FolFact + "','_blank')</script>");
                //out.println("<script>window.open('reimpGlobalMarbetes.jsp?fol_gnkl=" + FolFact + "','_blank')</script>");
            }
            //--------------------------------------------------------------------------------------------------------------------------------------
            if (request.getParameter("accion").equals("generarRemision")) {

                ban1 = 1;
                String ClaUni = request.getParameter("Nombre");
                String F_Tipo = request.getParameter("F_Tipo");
                String FechaE = request.getParameter("F_FecEnt");
                int Catalogo = Integer.parseInt(request.getParameter("Cata"));

                String Clave = "", Caducidad = "", FolioLote = "", Lote = "", Ubicacion = "", UbicaDesc = "", UbicaDescMov = "", Desproyecto = "";
                int piezas = 0, existencia = 0, diferencia = 0, ContaLot = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, ClaProve = 0, Org = 0, FolMov = 0, FolioMovi = 0, piezasDif = 0, UbicaModu = 0, Proyecto = 0;
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                int Movimiento = 0, ContarV = 0;;
                String[] claveschk = request.getParameterValues("chkUniFact");
                String Unidades = "";
                for (int i = 0; i < claveschk.length; i++) {
                    if (i == (claveschk.length - 1)) {
                        Unidades = Unidades + "'" + claveschk[i] + "'";
                    } else {
                        Unidades = Unidades + "'" + claveschk[i] + "',";
                    }
                }
                System.out.println(Unidades);

                try {
                    con.conectar();

                    if (Catalogo == 1) {
                        ResultSet UbiMod = con.consulta("SELECT PU.F_Id,P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE F_Usuario = '" + sesion.getAttribute("nombre") + "';");
                        if (UbiMod.next()) {
                            UbicaModu = UbiMod.getInt(1);
                            Proyecto = UbiMod.getInt(2);
                            Desproyecto = UbiMod.getString(3);
                        }
                        if (UbicaModu == 1) {
                            UbicaDesc = " WHERE F_Ubica IN ('MODULA','A0S','APE','DENTAL','REDFRIALERMA')";
                            UbicaDescMov = " F_UbiMov IN ('MODULA','A0S','APE','DENTAL','REDFRIALERMA')";
                        } else if (UbicaModu == 2) {
                            UbicaDesc = " WHERE F_Ubica IN ('MODULA2','A0S','APE','DENTAL','REDFRIALERMA')";
                            UbicaDescMov = " F_UbiMov IN ('MODULA2','A0S','APE','DENTAL','REDFRIALERMA')";
                        } else if (UbicaModu == 3) {
                            UbicaDesc = " WHERE F_Ubica IN ('A0S','APE','DENTAL','REDFRIALERMA')";
                            UbicaDescMov = " F_UbiMov IN ('A0S','APE','DENTAL','REDFRIALERMA')";
                        } else {
                            UbicaDesc = " WHERE F_Ubica NOT IN ('MODULA','MODULA2','CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA')";
                            UbicaDescMov = " F_UbiMov NOT IN ('MODULA','MODULA2','CADUCADOS','PROXACADUCAR','MERMA','EXTRA_ORDINARIA')";
                        }
                    } else if (Catalogo == 30) {
                        ResultSet UbiSolu = con.consulta("SELECT * FROM tb_ubicasoluciones;");
                        if (UbiSolu.next()) {
                            UbicaDesc = " WHERE F_Ubica IN (" + UbiSolu.getString(1) + ")";
                            UbicaDescMov = " F_UbiMov IN (" + UbiSolu.getString(1) + ")";
                        }
                    } else {
                        UbicaDesc = " WHERE F_Ubica IN ('AF','MATCUR','APE','DENTAL','REDFRIALERMA')";
                        UbicaDescMov = " F_UbiMov IN ('AF','MATCUR','APE','DENTAL','REDFRIALERMA')";
                    }

                    /*else if ((Catalogo == 2) || (Catalogo == 14) || (Catalogo == 16) || (Catalogo == 216) || (Catalogo == 17) || (Catalogo == 217)) {
                        UbicaDesc = "'AF','APE','DENTAL'";
                    } else if (Catalogo == 3) {
                        UbicaDesc = "'AF','APE','DENTAL'";
                    }*/
                    ResultSet rset = con.consulta("select U.F_ClaPro, F_ClaUni from tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro where F_ClaUni in( " + Unidades + ") and F_Status=0 and  F_Solicitado != 0 AND M.F_StsPro='A' AND F_N" + Catalogo + "='1';");
                    while (rset.next()) {
                        String ClaPro = rset.getString("U.F_ClaPro");
                        String F_NCant = request.getParameter(rset.getString("F_ClaUni") + "_" + ClaPro.trim());
                        con.insertar("update tb_unireq set F_PiezasReq = '" + F_NCant + "' where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaUni = '" + rset.getString("F_ClaUni") + "' and F_Status='0'");
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {

                    con.conectar();
                    //consql.conectar();

                    ResultSet rset = con.consulta("select f.F_ClaUni from tb_fecharuta f, tb_uniatn u where f.F_ClaUni = u.F_ClaCli and  f.F_ClaUni in (" + Unidades + ") group by f.F_ClaUni;");
                    while (rset.next()) {
                        ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice");
                        while (FolioFact.next()) {
                            FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFact"));
                        }
                        FolFact = FolioFactura + 1;
                        con.actualizar("update tb_indice set F_IndFact='" + FolFact + "'");
                        ClaUni = rset.getString("F_ClaUni");
                        FechaE = request.getParameter("F_FecEnt");
                        /*
                         *Abre Ciclo ClaUni
                         */

                        ResultSet rset_cantidad = con.consulta("SELECT U.F_ClaPro,SUM(F_CajasReq) as cajas, SUM(F_PiezasReq) as piezas, F_IdReq,SUM(F_Solicitado) as F_Solicitado FROM tb_unireq U INNER JOIN tb_medica M ON U.F_ClaPro=M.F_ClaPro WHERE F_ClaUni='" + ClaUni + "' and F_Status='0' and F_Solicitado!=0 AND M.F_StsPro='A' AND F_N" + Catalogo + "='1' GROUP BY F_ClaPro order by U.F_ClaPro+0");
                        while (rset_cantidad.next()) {
                            Clave = rset_cantidad.getString("F_ClaPro");
                            int cajasReq = Integer.parseInt(rset_cantidad.getString("cajas"));
                            int piezasReq = Integer.parseInt(rset_cantidad.getString("piezas"));
                            int F_Solicitado = Integer.parseInt(rset_cantidad.getString("F_Solicitado"));
                            int pzxCaja = 0;
                            int Facturado = 0;
                            /*ResultSet rsetCP = con.consulta("select F_Pzs from tb_pzxcaja where F_ClaPro = '" + Clave + "' ");
                             while (rsetCP.next()) {
                             pzxCaja = rsetCP.getInt(1);
                             }*/
                            //piezas = (pzxCaja * cajasReq) + piezasReq;
                            piezas = piezasReq;
                            //piezas = Integer.parseInt(rset_cantidad.getString("CANTIDAD"));

                            //INICIO DE CONSULTA MYSQL
                            ///// *** Consulta a Módula O A0S O APE*** /////
                            if (piezas > 0) {

                                ResultSet Ubica = null;
                                int F_IdLote = 0, F_ExiLot = 0, CanSur = 0, F_FolLot = 0, Existencia = 0, ExistLote = 0, Contar = 0, DifExi = 0, ExiMov = 0;

                                ResultSet Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov = L.F_ClaPro AND M.F_LotMov = L.F_FolLot AND M.F_UbiMov = L.F_Ubica where F_ProMov='" + Clave + "' AND " + UbicaDescMov + " AND L.F_Proyecto='" + Proyecto + "' ;");
                                if (Exitotal.next()) {
                                    ExiMov = Exitotal.getInt(1);
                                }
                                if (ExiMov < 0) {
                                    ExiMov = 0;
                                }
                                if (ExiMov > 0) {

                                    ResultSet ExiLote = con.consulta("SELECT SUM(L.F_ExiLot) AS F_ExiLot,COUNT(F_Ubica) AS Contar FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro "
                                            + " " + UbicaDesc + " AND L.F_ExiLot>0 AND M.F_N" + Catalogo + "='1' AND L.F_Proyecto='" + Proyecto + "' AND M.F_StsPro='A' AND L.F_ClaPro='" + Clave + "';");
                                    while (ExiLote.next()) {
                                        ExistLote = ExiLote.getInt(1);
                                        Contar = ExiLote.getInt(2);
                                    }
                                    if (ExistLote >= ExiMov) {
                                        Existencia = ExiMov;
                                    } else {
                                        Existencia = ExistLote;
                                    }
                                    ContarV = Contar;

                                    if ((Existencia >= piezas) && (piezas > 0)) {

                                        Existencia = 0;
                                        Ubica = con.consulta("SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro "
                                                + "" + UbicaDesc + " AND L.F_ExiLot>0 AND M.F_N" + Catalogo + "='1' AND L.F_Proyecto='" + Proyecto + "' AND M.F_StsPro='A' AND L.F_ClaPro='" + Clave + "' ORDER BY L.F_Origen,L.F_FecCad,L.F_ClaLot ASC ");
                                        while (Ubica.next()) {
                                            F_IdLote = Ubica.getInt(1);
                                            Existencia = Ubica.getInt(2);
                                            F_FolLot = Ubica.getInt(3);
                                            Tipo = Ubica.getInt(4);
                                            Costo = Ubica.getDouble(5);
                                            Ubicacion = Ubica.getString(6);
                                            if (Tipo == 2504) {
                                                IVA = 0.0;
                                            } else {
                                                IVA = 0.16;
                                            }

                                            Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv  M INNER JOIN tb_lote L ON M.F_ProMov = L.F_ClaPro AND M.F_LotMov = L.F_FolLot AND M.F_UbiMov = L.F_Ubica where F_ProMov='" + Clave + "' AND " + UbicaDescMov + " AND F_LotMov='" + F_FolLot + "' AND L.F_Proyecto='" + Proyecto + "';");
                                            if (Exitotal.next()) {
                                                ExiMov = Exitotal.getInt(1);
                                            }
                                            if (Existencia >= ExiMov) {
                                                F_ExiLot = ExiMov;
                                            } else {
                                                F_ExiLot = Existencia;
                                            }

                                            if (F_ExiLot < 0) {
                                                F_ExiLot = 0;
                                            }

                                            if ((F_ExiLot >= piezas) && (piezas > 0)) {
                                                diferencia = F_ExiLot - piezas;
                                                CanSur = piezas;
                                                con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + F_IdLote + "';");
                                                /*
                                                Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv where F_ProMov='" + Clave + "' AND F_UbiMov IN (" + UbicaDesc + ") AND F_LotMov='" + F_FolLot + "';");
                                                if (Exitotal.next()) {
                                                    ExiMov = Exitotal.getInt(1);
                                                }
                                                if (ExiMov >= piezas) {
                                                    CanSur = piezas;
                                                } else if (ExiMov > 0) {
                                                    CanSur = ExiMov;
                                                } else {
                                                    CanSur = 0;
                                                }*/

                                                IVAPro = (CanSur * Costo) * IVA;
                                                Monto = CanSur * Costo;
                                                MontoIva = Monto + IVAPro;

                                                if (CanSur > 0) {
                                                    Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + CanSur + " ");
                                                    con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + CanSur + "','" + Costo + "','" + MontoIva + "','-1','" + F_FolLot + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                                    if (ContarV > 1) {
                                                        Facturado = piezas - CanSur;
                                                        if (Facturado == 0) {
                                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                        } else {
                                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + CanSur + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                        }
                                                        Facturado = 0;
                                                        F_Solicitado = F_Solicitado - CanSur;
                                                    } else {
                                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                    }
                                                    ContarV = ContarV - 1;
                                                    Facturado = 0;
                                                } else {
                                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezas + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                }

                                                piezasDif = 0;
                                                piezas = 0;
                                                Facturado = 0;

                                            } else if ((piezas > 0) && (F_ExiLot > 0)) {
                                                diferencia = piezas - F_ExiLot;
                                                CanSur = F_ExiLot;
                                                con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + F_IdLote + "'");
                                                /*
                                                Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv where F_ProMov='" + Clave + "';");
                                                if (Exitotal.next()) {
                                                    ExiMov = Exitotal.getInt(1);
                                                }
                                                if (ExiMov >= F_ExiLot) {
                                                    CanSur = F_ExiLot;
                                                } else if (ExiMov > 0) {
                                                    CanSur = ExiMov;
                                                } else {
                                                    CanSur = 0;
                                                }
                                                 */
                                                IVAPro = (CanSur * Costo) * IVA;
                                                Monto = CanSur * Costo;
                                                MontoIva = Monto + IVAPro;
                                                if (CanSur > 0) {
                                                    Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + CanSur + " ");
                                                    con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + CanSur + "','" + Costo + "','" + MontoIva + "','-1','" + F_FolLot + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                                    if (ContarV > 1) {
                                                        Facturado = piezas - CanSur;
                                                        if (Facturado == 0) {
                                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                        } else {
                                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + CanSur + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                        }
                                                        Facturado = 0;
                                                        F_Solicitado = F_Solicitado - CanSur;
                                                    } else {
                                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                    }
                                                    Facturado = 0;
                                                    piezas = piezas - CanSur;
                                                    ContarV = ContarV - 1;
                                                } else {
                                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezas + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                    piezas = 0;
                                                }

                                                piezasDif = 0;
                                                Facturado = 0;

                                            }
                                        }
                                    } else if (Existencia > 0) {
                                        DifExi = piezas;
                                        int x = 1;
                                        Ubica = con.consulta("SELECT L.F_IdLote,L.F_ExiLot,L.F_FolLot,M.F_TipMed,M.F_Costo,L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro "
                                                + "" + UbicaDesc + " AND L.F_ExiLot>0 AND M.F_N" + Catalogo + "='1' AND L.F_Proyecto='" + Proyecto + "' AND M.F_StsPro='A' AND L.F_ClaPro='" + Clave + "' ORDER BY L.F_Origen,L.F_FecCad,L.F_ClaLot ASC ");
                                        while (Ubica.next()) {
                                            F_IdLote = Ubica.getInt(1);
                                            Existencia = Ubica.getInt(2);
                                            F_FolLot = Ubica.getInt(3);
                                            Tipo = Ubica.getInt(4);
                                            Costo = Ubica.getDouble(5);
                                            Ubicacion = Ubica.getString(6);
                                            if (Tipo == 2504) {
                                                IVA = 0.0;
                                            } else {
                                                IVA = 0.16;
                                            }

                                            con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + F_IdLote + "'");

                                            Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv M INNER JOIN tb_lote L ON M.F_ProMov = L.F_ClaPro AND M.F_LotMov = L.F_FolLot AND M.F_UbiMov = L.F_Ubica where F_ProMov='" + Clave + "' AND " + UbicaDescMov + " AND F_LotMov='" + F_FolLot + "' AND L.F_Proyecto = '" + Proyecto + "';");
                                            if (Exitotal.next()) {
                                                ExiMov = Exitotal.getInt(1);
                                            }
                                            if (Existencia >= ExiMov) {
                                                F_ExiLot = ExiMov;
                                            } else {
                                                F_ExiLot = Existencia;
                                            }

                                            if (F_ExiLot < 0) {
                                                F_ExiLot = 0;
                                            }

                                            diferencia = piezas - F_ExiLot;
                                            CanSur = F_ExiLot;
                                            /*        
                                            Exitotal = con.consulta("select sum(F_CantMov*F_SigMov) from tb_movinv where F_ProMov='" + Clave + "';");
                                            if (Exitotal.next()) {
                                                ExiMov = Exitotal.getInt(1);
                                            }
                                            if (ExiMov >= F_ExiLot) {
                                                CanSur = F_ExiLot;
                                            } else if (ExiMov > 0) {
                                                CanSur = ExiMov;
                                            } else {
                                                CanSur = 0;
                                            }
                                             */

                                            IVAPro = (CanSur * Costo) * IVA;
                                            Monto = CanSur * Costo;
                                            MontoIva = Monto + IVAPro;
                                            if (CanSur > 0) {
                                                Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + CanSur + " ");
                                                con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + CanSur + "','" + Costo + "','" + MontoIva + "','-1','" + F_FolLot + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                            }

                                            if (x == Contar) {
                                                DifExi = DifExi;
                                                con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + DifExi + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                            } else {
                                                if (CanSur > 0) {
                                                    Facturado = piezas - CanSur;
                                                    if (Facturado == 0) {
                                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezas + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                    } else {
                                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + CanSur + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                    }
                                                    Facturado = 0;
                                                    piezas = piezas - CanSur;
                                                } else {
                                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + DifExi + "','" + CanSur + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_FolLot + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                                }
                                                Facturado = 0;
                                                piezasDif = 0;
                                                //piezas = diferencia;
                                                x = x + 1;
                                                DifExi = DifExi - CanSur;
                                            }
                                            F_IdLote = 0;
                                            //x=1;
                                        }
                                    } else {
                                        int FolioL = 0, IndiceLote = 0;
                                        ResultSet FolLote = con.consulta("SELECT F_FolLot,F_Ubica,F_Costo FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro WHERE L.F_ClaPro='" + Clave + "' ORDER BY F_FolLot DESC");
                                        if (FolLote.next()) {
                                            FolioL = FolLote.getInt(1);
                                            Ubicacion = FolLote.getString(2);
                                            Costo = FolLote.getDouble(3);
                                        }
                                        if (FolioL == 0) {
                                            ResultSet IndLote = con.consulta("SELECT F_IndLote FROM tb_indice");
                                            if (IndLote.next()) {
                                                FolioL = IndLote.getInt(1);
                                            }
                                            IndiceLote = FolioL + 1;
                                            con.actualizar("update tb_indice set F_IndLote='" + IndiceLote + "'");
                                            con.insertar("INSERT INTO tb_lote VALUES(0,'" + Clave + "','X','2015-01-01','0','" + FolioL + "','900000000','NUEVA','2013-01-01','111','14','2','900000000','131')");
                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','0','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','NUEVA','','" + existencia + "')");
                                        } else {
                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','" + Costo + "','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                        }

                                    }

                                } else {
                                    int FolioL = 0, IndiceLote = 0;
                                    ResultSet FolLote = con.consulta("SELECT F_FolLot,F_Ubica,F_Costo FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro WHERE L.F_ClaPro='" + Clave + "' ORDER BY F_FolLot DESC");
                                    if (FolLote.next()) {
                                        FolioL = FolLote.getInt(1);
                                        Ubicacion = FolLote.getString(2);
                                        Costo = FolLote.getDouble(3);
                                    }
                                    if (FolioL == 0) {
                                        ResultSet IndLote = con.consulta("SELECT F_IndLote FROM tb_indice");
                                        if (IndLote.next()) {
                                            FolioL = IndLote.getInt(1);
                                        }
                                        IndiceLote = FolioL + 1;
                                        con.actualizar("update tb_indice set F_IndLote='" + IndiceLote + "'");
                                        con.insertar("INSERT INTO tb_lote VALUES(0,'" + Clave + "','X','2015-01-01','0','" + FolioL + "','900000000','NUEVA','2013-01-01','111','14','2','900000000','131')");
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','0','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','NUEVA','','" + existencia + "')");
                                    } else {
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','" + Costo + "','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                    }
                                }

                                System.out.println("**************************" + piezas + "******************************");

                                con.actualizar("update tb_unireq set F_Status='2' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");
                            } else {

                                int FolioL = 0, IndiceLote = 0;
                                ResultSet FolLote = con.consulta("SELECT F_FolLot,F_Ubica,F_Costo FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro WHERE L.F_ClaPro='" + Clave + "' ORDER BY F_FolLot DESC");
                                if (FolLote.next()) {
                                    FolioL = FolLote.getInt(1);
                                    Ubicacion = FolLote.getString(2);
                                    Costo = FolLote.getDouble(3);
                                }
                                if (FolioL == 0) {
                                    ResultSet IndLote = con.consulta("SELECT F_IndLote FROM tb_indice");
                                    if (IndLote.next()) {
                                        FolioL = IndLote.getInt(1);
                                    }
                                    IndiceLote = FolioL + 1;
                                    con.actualizar("update tb_indice set F_IndLote='" + IndiceLote + "'");
                                    con.insertar("INSERT INTO tb_lote VALUES(0,'" + Clave + "','X','2015-01-01','0','" + FolioL + "','900000000','NUEVA','2013-01-01','111','14','2','900000000','131')");
                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','0','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','NUEVA','','" + existencia + "')");
                                } else {
                                    con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + F_Solicitado + "','0','" + Costo + "','0','0','" + FolioL + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','','" + existencia + "')");
                                }

                                con.actualizar("update tb_unireq set F_Status='2' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");
                            }
                        }
                        con.actualizar("update tb_unireq set F_Status='2' where F_ClaUni='" + ClaUni + "' and F_Status='0' ");
                        byte[] a = request.getParameter("obs" + ClaUni).getBytes("ISO-8859-1");
                        String Observaciones = (new String(a, "UTF-8")).toUpperCase();
                        con.insertar("insert into tb_obserfact values ('" + FolioFactura + "','" + Observaciones + "',0,'A', '" + F_Tipo + "')");
                        //out.println("<script>window.open('reimpFactura.jsp?fol_gnkl=" + FolioFactura + "','_blank')</script>");
                        /*
                         * Cierra Ciclo
                         */
                    }

                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getLocalizedMessage());
                }
                //out.println("<script>window.location='reimpConcentrado.jsp'</script>");

                ////----------------------------------------------------------------------------------------------------
                ////----------------------------------------------------------------------------------------------------
            }
            //-----------------------------------------------------------------------------------------------------------
            if (request.getParameter("accion").equals("guardar")) {

                ban1 = 1;
                String ClaUni = request.getParameter("Nombre");
                String FechaE = request.getParameter("FecFab");
                String Clave = "", Caducidad = "", FolioLote = "", Lote = "", Ubicacion = "";
                int piezas = 0, existencia = 0, diferencia = 0, ContaLot = 0, X = 0, FolioFactura = 0, FolFact = 0, Tipo = 0, ClaProve = 0, Org = 0, FolMov = 0, FolioMovi = 0;
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;

                try {

                    con.conectar();
                    //consql.conectar();

                    ResultSet Fechaa = con.consulta("SELECT STR_TO_DATE('" + FechaE + "', '%d/%m/%Y')");
                    while (Fechaa.next()) {
                        FechaE = Fechaa.getString("STR_TO_DATE('" + FechaE + "', '%d/%m/%Y')");
                    }
                    ResultSet FolioFact = con.consulta("SELECT F_IndFact FROM tb_indice");
                    while (FolioFact.next()) {
                        FolioFactura = Integer.parseInt(FolioFact.getString("F_IndFact"));
                    }
                    FolFact = FolioFactura + 1;
                    con.actualizar("update tb_indice set F_IndFact='" + FolFact + "'");

                    ResultSet rset_cantidad = con.consulta("SELECT F_ClaPro,SUM(F_CantReq) AS CANTIDAD, F_IdReq FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_Status='0' and F_FecCarg = CURDATE() GROUP BY F_ClaPro");
                    while (rset_cantidad.next()) {
                        Clave = rset_cantidad.getString("F_ClaPro");
                        piezas = Integer.parseInt(rset_cantidad.getString("CANTIDAD"));
                        String piezasOri = rset_cantidad.getString("CANTIDAD");

                        //INICIO DE CONSULTA MYSQL
                        ResultSet r_Org = con.consulta("SELECT F_ClaOrg FROM tb_lote WHERE F_ClaPro='" + Clave + "' GROUP BY F_ClaOrg ORDER BY F_ClaOrg+0");
                        while (r_Org.next()) {
                            Org = Integer.parseInt(r_Org.getString("F_ClaOrg"));

                            if (Org == 1) {
                                ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,L.F_ExiLot AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot, L.F_IdLote FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' AND L.F_ClaOrg='" + Org + "' ORDER BY L.F_Origen, L.F_FecCad ASC");
                                while (FechaLote.next()) {
                                    Caducidad = FechaLote.getString("F_FecCad");
                                    FolioLote = FechaLote.getString("F_FolLot");
                                    String IdLote = FechaLote.getString("F_IdLote");
                                    String ClaLot = FechaLote.getString("F_ClaLot");
                                    existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                    Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                    Costo = Double.parseDouble(FechaLote.getString("F_Costo"));
                                    Ubicacion = FechaLote.getString("F_Ubica");
                                    ClaProve = Integer.parseInt(FechaLote.getString("F_ProVee"));
                                    if (Tipo == 2504) {
                                        IVA = 0.0;
                                    } else {
                                        IVA = 0.16;
                                    }

                                    if (piezas > existencia) {
                                        diferencia = piezas - existencia;
                                        /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + dame5car("1") + "' ");
                                         String loteSQL = "";
                                         while (rsetLoteSQL.next()) {
                                         loteSQL = rsetLoteSQL.getString("lote");
                                         }
                                         consql.actualizar("UPDATE TB_lote SET F_ExiLot='0' WHERE F_FolLot='" + loteSQL + "'");*/
                                        con.actualizar("UPDATE tb_lote SET F_ExiLot='0' WHERE F_IdLote='" + IdLote + "'");

                                        IVAPro = (existencia * Costo) * IVA;
                                        Monto = existencia * Costo;
                                        MontoIva = Monto + IVAPro;

                                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                        while (FolioMov.next()) {
                                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                        }
                                        FolMov = FolioMovi + 1;
                                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");

                                        Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + existencia + " ");
                                        con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + existencia + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezasOri + "','" + existencia + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                        //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + existencia + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + FolioLote + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                        //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + piezasOri + "','" + existencia + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + FolioLote + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + existencia + "','" + Ubicacion + "') ");
                                        piezas = diferencia;
                                    } else {
                                        diferencia = existencia - piezas;
                                        /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + dame5car("1") + "' ");
                                         String loteSQL = "";
                                         while (rsetLoteSQL.next()) {
                                         loteSQL = rsetLoteSQL.getString("lote");
                                         }*/
                                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                        //consql.actualizar("UPDATE TB_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + loteSQL + "'");

                                        IVAPro = (piezas * Costo) * IVA;
                                        Monto = piezas * Costo;
                                        MontoIva = Monto + IVAPro;

                                        if (piezas > 0) {
                                            ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                            while (FolioMov.next()) {
                                                FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                            }
                                            FolMov = FolioMovi + 1;
                                            con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");

                                            Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + piezas + " ");
                                            con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + piezas + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezasOri + "','" + piezas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                            con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                            //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + piezas + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + FolioLote + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                            //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + piezasOri + "','" + piezas + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + FolioLote + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + piezas + "','" + Ubicacion + "') ");
                                        }
                                        piezas = 0;
                                    }
                                }
                            } else {
                                ResultSet FechaLote = con.consulta("SELECT L.F_FecCad AS F_FecCad,L.F_FolLot AS F_FolLot,L.F_ExiLot AS F_ExiLot, M.F_TipMed AS F_TipMed, M.F_Costo AS F_Costo, L.F_Ubica AS F_Ubica, C.F_ProVee AS F_ProVee, F_ClaLot,F_IdLote FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro INNER JOIN tb_compra C ON L.F_FolLot=C.F_Lote WHERE L.F_ClaPro='" + Clave + "' AND L.F_ExiLot>'0' ORDER BY L.F_Origen, L.F_FecCad ASC");
                                while (FechaLote.next()) {
                                    Caducidad = FechaLote.getString("F_FecCad");
                                    FolioLote = FechaLote.getString("F_FolLot");
                                    String IdLote = FechaLote.getString("F_IdLote");
                                    String ClaLot = FechaLote.getString("F_ClaLot");
                                    existencia = Integer.parseInt(FechaLote.getString("F_ExiLot"));
                                    Tipo = Integer.parseInt(FechaLote.getString("F_TipMed"));
                                    Costo = Double.parseDouble(FechaLote.getString("F_Costo"));
                                    Ubicacion = FechaLote.getString("F_Ubica");
                                    ClaProve = Integer.parseInt(FechaLote.getString("F_ProVee"));
                                    if (Tipo == 2504) {
                                        IVA = 0.0;
                                    } else {
                                        IVA = 0.16;
                                    }
                                    /* ResultSet CantidadLote = con.consulta("SELECT F_ExiLot FROM tb_lote WHERE F_FolLot='"+FolioLote+"'");
                                     while(CantidadLote.next()){
                                     existencia = Integer.parseInt(CantidadLote.getString("F_ExiLot"));
                                     }*/
                                    if (piezas > existencia) {
                                        diferencia = piezas - existencia;
                                        /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + dame5car("1") + "' ");
                                         String loteSQL = "";
                                         while (rsetLoteSQL.next()) {
                                         loteSQL = rsetLoteSQL.getString("lote");
                                         }
                                         consql.actualizar("UPDATE TB_lote SET F_ExiLot='0' WHERE F_FolLot='" + loteSQL + "'");*/
                                        con.actualizar("UPDATE tb_lote SET F_IdLote='0' WHERE F_FolLot='" + IdLote + "'");

                                        IVAPro = (existencia * Costo) * IVA;
                                        Monto = existencia * Costo;
                                        MontoIva = Monto + IVAPro;

                                        ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                        while (FolioMov.next()) {
                                            FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                        }
                                        FolMov = FolioMovi + 1;
                                        con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");

                                        Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + existencia + " ");
                                        con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + existencia + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                        con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezasOri + "','" + existencia + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                        //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + existencia + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + FolioLote + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                        //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + piezasOri + "','" + existencia + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + FolioLote + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + existencia + "','" + Ubicacion + "') ");

                                        piezas = diferencia;
                                    } else {
                                        diferencia = existencia - piezas;
                                        /*ResultSet rsetLoteSQL = consql.consulta("select F_FolLot as lote from tb_lote where F_ClaPro = '" + Clave + "' and F_ClaLot = '" + ClaLot + "' and F_FecCad = '" + df2.format(df3.parse(Caducidad)) + "'  and F_Origen = '" + dame5car("1") + "' ");
                                         String loteSQL = "";
                                         while (rsetLoteSQL.next()) {
                                         loteSQL = rsetLoteSQL.getString("lote");
                                         }*/
                                        con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                        //consql.actualizar("UPDATE TB_lote SET F_ExiLot='" + diferencia + "' WHERE F_FolLot='" + loteSQL + "'");

                                        IVAPro = (piezas * Costo) * IVA;
                                        Monto = piezas * Costo;
                                        MontoIva = Monto + IVAPro;

                                        if (piezas >= 1) {
                                            ResultSet FolioMov = con.consulta("SELECT F_IndMov FROM tb_indice");
                                            while (FolioMov.next()) {
                                                FolioMovi = Integer.parseInt(FolioMov.getString("F_IndMov"));
                                            }
                                            FolMov = FolioMovi + 1;
                                            con.actualizar("update tb_indice set F_IndMov='" + FolMov + "'");

                                            Logger.getLogger(Facturacion.class.getName()).log(Level.INFO, null, "Movimiento tipo 51: { usuario: " + sesion.getAttribute("nombre") + ", clave = " + Clave + ", folio = " + FolioFactura + ", ubicacion = " + Ubicacion + ", piezas = " + piezas + " ");
                                            con.insertar("insert into tb_movinv values(0,curdate(),'" + FolioFactura + "','51','" + Clave + "','" + piezas + "','" + Costo + "','" + MontoIva + "','-1','" + FolioLote + "','" + Ubicacion + "','" + ClaProve + "',curtime(),'" + sesion.getAttribute("nombre") + "','')");
                                            con.insertar("insert into tb_factura values(0,'" + FolioFactura + "','" + ClaUni + "','A',curdate(),'" + Clave + "','" + piezasOri + "','" + piezas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + FolioLote + "','" + FechaE + "',curtime(),'" + sesion.getAttribute("nombre") + "','" + Ubicacion + "','')");
                                            con.actualizar("UPDATE tb_lote SET F_ExiLot='" + diferencia + "' WHERE F_IdLote='" + IdLote + "'");
                                            //consql.insertar("insert into TB_MovInv values (CONVERT(date,GETDATE()),'" + FolioFactura + "','','51', '" + Clave + "', '" + piezas + "', '" + Costo + "','" + IVAPro + "', '" + MontoIva + "' ,'-1', '" + FolioLote + "', '" + FolioMovi + "','A', '0', '','','','" + ClaProve + "','" + sesion.getAttribute("nombre") + "') ");
                                            //consql.insertar("insert into TB_Factura values ('F','" + FolioFactura + "','" + dame5car(ClaUni) + "','A','',CONVERT(date,GETDATE()),'','" + Clave + "', '','1','" + piezasOri + "','" + piezas + "', '" + Monto + "','0', '" + Monto + "','" + Monto + "','" + Monto + "','" + IVAPro + "', '" + MontoIva + "','" + Costo + "' ,'" + FolioLote + "','R','" + df2.format(df3.parse(FechaE)) + "','" + sesion.getAttribute("nombre") + "','0','0','','A','" + piezas + "','" + Ubicacion + "') ");
                                        }
                                        piezas = 0;
                                    }
                                }
                            }
                        }
                        //FIN CONSULTA MYSQL
                        /*ResultSet existSql = consql.consulta("select F_Existen from TB_Medica where F_ClaPro = '" + Clave + "' ");
                         while (existSql.next()) {
                         int difTotal = existSql.getInt("F_Existen") - rset_cantidad.getInt("CANTIDAD");
                         if (difTotal < 0) {
                         difTotal = 0;
                         }
                         consql.actualizar("update TB_Medica set F_Existen = '" + difTotal + "' where F_ClaPro = '" + Clave + "' ");
                         }*/
                        con.actualizar("update tb_unireq set F_Status='1' where F_IdReq='" + rset_cantidad.getString("F_IdReq") + "'");
                    }
                    con.actualizar("delete * FROM tb_unireq WHERE F_ClaUni='" + ClaUni + "' and F_FecCarg = CURDATE()");
                    con.cierraConexion();
                    //consql.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                //out.println("<script>window.open('reimpFactura.jsp?fol_gnkl=" + FolioFactura + "','_blank')</script>");

            }

        } catch (Exception e) {
            e.printStackTrace();
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

        //String original = "hello world";
        //byte[] utf8Bytes = original.getBytes("UTF8");
        //String value = new String(utf8Bytes, "UTF-8"); 
        //out.println(value);
        if (ban1 == 0) {
            out.println("<script>alert('Clave Inexistente')</script>");
            out.println("<script>window.history.back();</script>");
        } else {
            out.println("<script>window.location='main_menu.jsp'</script>");
        }
        //response.sendRedirect("captura.jsp");
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
