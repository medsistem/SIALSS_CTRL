/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Develuciones;

import conn.ConectionDB;
import in.co.sneh.picking.service.PickingService;
//import java.awt.BorderLayout;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.DevolucionesFact;

/**
 * Proceso para las devoluciones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class DevolucionesFacturas extends HttpServlet {

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
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateador2 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSistema = formateador.format(fechaActual);
        String fechaSistema2 = formateador2.format(fechaActual);
        ConectionDB con = new ConectionDB();
        HttpSession sesion = request.getSession(true);
        List<DevolucionesFact> Listaremisiones = new ArrayList<DevolucionesFact>();
        DevolucionesFact remisiones;
        List<DevolucionesFact> DatosUnidad = new ArrayList<DevolucionesFact>();
        DevolucionesFact Unidad;
        List<DevolucionesFact> ListaDevolucion = new ArrayList<DevolucionesFact>();
        DevolucionesFact Devolucion;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        String Consulta = "", Consulta2 = "";
        String TipoFact = "", usua = "", Concepto = "";
int contarpicking = 0;
boolean editable = false;

        try {
            usua = (String) sesion.getAttribute("nombre");
            String Accion = request.getParameter("Accion");
            con.conectar();
            if (Accion.equals("ListaRemision")) {
                request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
            }

            if (Accion.equals("ListaDevolucion")) {

                sesion.setAttribute("fecha_ini", fechaSistema2);
                sesion.setAttribute("fecha_fin", fechaSistema2);
                System.out.println("Fecha" + fechaSistema2);
                Consulta = "SELECT DATE_FORMAT(D.F_FecDev, '%d/%m/%Y') AS F_FecDev, D.F_DocDev, F.F_ClaCli, U.F_NomCli, DATE_FORMAT(F.F_FecEnt, '%d/%m/%Y') AS F_FecEnt, D.F_DocRef, D.F_IdDev, D.F_Proyecto, p.F_DesProy  FROM tb_devoluciones D INNER JOIN tb_factura F ON D.F_DocRef = F.F_ClaDoc AND D.F_Proyecto = F.F_Proyecto INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN tb_proyectos p ON D.F_Proyecto = p.F_Id  WHERE D.F_FecDev BETWEEN ? AND ? GROUP BY D.F_DocDev ORDER BY D.F_DocDev;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, fechaSistema2 + " 00:00:00");
                ps.setString(2, fechaSistema2 + " 23:59:59");
                rs = ps.executeQuery();
                while (rs.next()) {
                    Devolucion = new DevolucionesFact();
                    Devolucion.setFechamov(rs.getString(1));
                    Devolucion.setDocmovimiento(rs.getString(2));
                    Devolucion.setClaveuni(rs.getString(3));
                    Devolucion.setUnidad(rs.getString(4));
                    Devolucion.setFechaentrega(rs.getString(5));
                    Devolucion.setDocreferencia(rs.getString(6));
                    Devolucion.setIddevolucion(rs.getString(7));
                    Devolucion.setProyecto(rs.getInt(8));
                    Devolucion.setDesproy(rs.getString(9));
                    ListaDevolucion.add(Devolucion);
                }
                request.setAttribute("ListaDevolucion", ListaDevolucion);
                request.getRequestDispatcher("/DevolucionesAdmin.jsp").forward(request, response);
            }

            if (Accion.equals("btnMostrarDev")) {
                String fecha_ini = request.getParameter("fecha_ini");
                String fecha_fin = request.getParameter("fecha_fin");
                sesion.setAttribute("fecha_ini", fecha_ini);
                sesion.setAttribute("fecha_fin", fecha_fin);
                System.out.println("fecha_ini:" + fecha_ini + " fecha_fin:" + fecha_fin);
                Consulta = "SELECT DATE_FORMAT(D.F_FecDev,'%d/%m/%Y') AS F_FecDev,D.F_DocDev,F.F_ClaCli,U.F_NomCli,DATE_FORMAT(F.F_FecEnt,'%d/%m/%Y') AS F_FecEnt,D.F_DocRef,D.F_IdDev, D.F_Proyecto, p.F_DesProy FROM tb_devoluciones D INNER JOIN tb_factura F ON D.F_DocRef=F.F_ClaDoc INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli INNER JOIN tb_proyectos p ON D.F_Proyecto = p.F_Id WHERE D.F_FecDev BETWEEN ? AND ? GROUP BY D.F_DocRef ,D.F_DocDev ORDER BY D.F_DocDev;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, fecha_ini + " 00:00:00");
                ps.setString(2, fecha_fin + " 23:59:59");
                rs = ps.executeQuery();
                while (rs.next()) {
                    Devolucion = new DevolucionesFact();
                    Devolucion.setFechamov(rs.getString(1));
                    Devolucion.setDocmovimiento(rs.getString(2));
                    Devolucion.setClaveuni(rs.getString(3));
                    Devolucion.setUnidad(rs.getString(4));
                    Devolucion.setFechaentrega(rs.getString(5));
                    Devolucion.setDocreferencia(rs.getString(6));
                    Devolucion.setIddevolucion(rs.getString(7));
                    Devolucion.setProyecto(rs.getInt(8));
                    Devolucion.setDesproy(rs.getString(9));
                    ListaDevolucion.add(Devolucion);
                }
                request.setAttribute("ListaDevolucion", ListaDevolucion);
                request.getRequestDispatcher("/DevolucionesAdmin.jsp").forward(request, response);
            }


//MOSTRAR TODO
            if (Accion.equals("btnMostrar")) {
                int Proyectos = 0;
                String Folio = request.getParameter("Folio");
                String Proyecto = request.getParameter("Nombre");
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Proyectos = Integer.parseInt(Proyecto);
                }

                Consulta2 = "DELETE FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                ps2.execute();

//                Consulta = "SELECT F.F_ClaPro,SUBSTR(M.F_DesPro,1,60) AS F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc=? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ? GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";
Consulta = "SELECT F.F_ClaPro,SUBSTR(M.F_DesPro,1,60) AS F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact, CASE WHEN F.F_Ubicacion not IN ('AF','AF1N','AFGC') THEN 0 ELSE 0 END sts FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli   WHERE F.F_ClaDoc=? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ? GROUP BY F.F_IdFact  ORDER BY F.F_IdFact+0;";              
  ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Consulta2 = "INSERT INTO tb_devoglobalfact VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setString(1, rs.getString(1));
                    ps2.setString(2, rs.getString(2));
                    ps2.setString(3, rs.getString(3));
                    ps2.setString(4, rs.getString(4));
                    ps2.setString(5, rs.getString(5));
                    ps2.setString(6, rs.getString(6));
                    ps2.setString(7, rs.getString(7));
                    ps2.setString(8, rs.getString(7));
                    ps2.setString(9, rs.getString(8));
                    ps2.setString(10, rs.getString(9));
                    ps2.setString(11, rs.getString(10));
                    ps2.setString(12, rs.getString(11));
                    ps2.execute();
                }
               
                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    Unidad.setProyecto(Proyectos);
                    DatosUnidad.add(Unidad);
                }

//                Consulta = "SELECT IFNULL(ff.`STATUS`,0) as sts FROM federated_folios ff where FOLIO = ?;";
//                ps3 = con.getConn().prepareStatement(Consulta);
//                ps3.setString(1, Folio);
//                rs3 = ps3.executeQuery();
//                while (rs3.next()) {
//                    if (rs3.wasNull()) {
//                        System.out.println("cuando es null");
                        contarpicking = 0;
//                    } else {
//                        contarpicking = rs3.getInt(1);
//                        System.out.println("Status: " + contarpicking);
//                    }
//                }

                if (contarpicking < 1 || contarpicking > 4) {
                    editable = true;
                    System.out.println("no picking" + editable);
                  
                } else {
                    editable = false;
                    System.out.println("es picking picking" + editable);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    remisiones.setProyecto(Proyectos);
                    Listaremisiones.add(remisiones);
                }


                //Verificar que el folio no haya sido enviado a picking
             /*   PickingService serv = new PickingService();
                int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyectos);
                editable = (pickingStatus == -1) || (pickingStatus >= 5);*/

                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                sesion.setAttribute("editable", editable);

                request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
            }




/*buscar clave para devolucion*/
            if (Accion.equals("btnClave")) {
                int Proyectos = 0, sts = 1;
                String Folio = request.getParameter("FolioC");
                String Proyecto = request.getParameter("ProyectoC");
                String Clave = request.getParameter("Clave");
                String ubicapicking = "";
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Proyectos = Integer.parseInt(Proyecto);
                }

                Consulta2 = "DELETE FROM tb_devoglobalfact WHERE F_ClaDoc=? AND F_ClaPro = ?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                ps2.setString(2, Clave);
                ps2.execute();

               // Consulta = "SELECT F.F_ClaPro,SUBSTR(M.F_DesPro,1,60) AS F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli WHERE F.F_ClaDoc=? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ? AND F.F_ClaPro = ? GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";
Consulta = "SELECT F.F_ClaPro,SUBSTR(M.F_DesPro,1,60) AS F_DesPro,L.F_ClaLot,F_FecCad,F.F_CantReq, F.F_Ubicacion,F.F_CantSur,F.F_Costo,F.F_Monto,F.F_ClaDoc, F.F_IdFact, CASE WHEN F.F_Ubicacion not IN ('AF','AF1N','AFGC') THEN 0 ELSE 0 END sts FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli   WHERE F.F_ClaDoc= ? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ? AND F.F_ClaPro = ? GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";               
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                ps.setString(3, Clave);
                rs = ps.executeQuery();

                while (rs.next()) {
                    ubicapicking = rs.getString(6);
                    sts = rs.getInt(12);
                    Consulta2 = "INSERT INTO tb_devoglobalfact VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
                    ps2 = con.getConn().prepareStatement(Consulta2);
                    ps2.setString(1, rs.getString(1));
                    ps2.setString(2, rs.getString(2));
                    ps2.setString(3, rs.getString(3));
                    ps2.setString(4, rs.getString(4));
                    ps2.setString(5, rs.getString(5));
                    ps2.setString(6, rs.getString(6));
                    ps2.setString(7, rs.getString(7));
                    ps2.setString(8, rs.getString(7));
                    ps2.setString(9, rs.getString(8));
                    ps2.setString(10, rs.getString(9));
                    ps2.setString(11, rs.getString(10));
                    ps2.setString(12, rs.getString(11));
                    ps2.execute();
                }

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    Unidad.setProyecto(Proyectos);
                    DatosUnidad.add(Unidad);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
String Clave12 = "";
                while (rs2.next()) {
Clave12 = rs2.getString(1);
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    remisiones.setProyecto(Proyectos);
                    Listaremisiones.add(remisiones);
                }

                if (!rs2.wasNull()) {
                if ((sts > 4 || sts < 1) ) {
                    System.out.println("esta clave puede ser editable: " + Clave12);                    
editable = true;
                } else {
                    System.out.println("clave no se puede editar: " + Clave12);
                out.print("<scrip>alert('esta clave esta siendo surtida o no tiene existencia') </scrip>");
                    editable = false;
                }
                }
                sesion.setAttribute("editable", editable);
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/DevolicionFacturaClave.jsp").forward(request, response);
            }
            
             if (Accion.equals("btnregresar")) {
                  request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
             }


//devolucion por clave
            if (Accion.equals("btnMostrarClave")) {
                int Proyectos = 0;
                String Folio = request.getParameter("Folio");
                String Proyecto = request.getParameter("Nombre");
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Proyectos = Integer.parseInt(Proyecto);
                }

                Consulta2 = "DELETE FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                ps2.execute();

               
                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();

                if (!rs.wasNull()) {
                    while (rs.next()) {
                        Unidad = new DevolucionesFact();
                        Unidad.setFolio(rs.getInt(1));
                        Unidad.setClaveuni(rs.getString(2));
                        Unidad.setUnidad(rs.getString(3));
                        Unidad.setFechaentrega(rs.getString(4));
                        Unidad.setUsuario(usua);
                        Unidad.setProyecto(Proyectos);
                        DatosUnidad.add(Unidad);
                    }

                    //Verificar que el folio no haya sido enviado a picking
                    PickingService serv = new PickingService();
                    int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyectos);
                    editable = (pickingStatus == -1) || (pickingStatus >= 5);
                    if (editable) {
                        out.println("<script>alert('Folio si! se puede editar')</script>");
                        System.out.println("'Folio si se puede editar'");
                        sesion.setAttribute("editable", editable);
                        request.setAttribute("DatosUnidad", DatosUnidad);
                        request.getRequestDispatcher("/DevolicionFacturaClave.jsp").forward(request, response);

                    } else{
                        sesion.setAttribute("editable", editable);
                        request.setAttribute("DatosUnidad", DatosUnidad);
                        request.getRequestDispatcher("/DevolicionFacturaClave.jsp").forward(request, response);
                    }

                } else {

                    if (Accion.equals("btnregresar")) {
                  request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
                    }
                }
            }



//MODIFICAR DEVOLUCION
            if (Accion.equals("btnModificar")) {
                int Cantidad = 0, Diferencia = 0;
                String Folio = request.getParameter("Folio");
                String IdFol = request.getParameter("Identi");
                String Devolver = request.getParameter("Devolver");
                String proyecto = request.getParameter("proyecto");
                int Surtida = Integer.parseInt(request.getParameter("Surtida"));
                System.out.println("Folio: " + Folio + " Id: " + IdFol);
                if (!(Devolver.equals(""))) {
                    Cantidad = Integer.parseInt(Devolver);
                    if (Cantidad == 0) {
                        out.println("<script>alert('Cantidad a Devolver debe ser Mayor a Cero')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {
                        Diferencia = Surtida - Cantidad;
                        if (Diferencia < 0) {
                            out.println("<script>alert('Cantidad a Devolver es Mayor a lo Surtido')</script>");
                            out.println("<script>window.history.back()</script>");
                        } else {
                            Consulta = "UPDATE tb_devoglobalfact SET F_CantDevo =? WHERE F_IdFact=? AND F_ClaDoc=?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Devolver);
                            ps.setString(2, IdFol);
                            ps.setString(3, Folio);
                            ps.execute();

                            Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc = ? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Folio);
                            ps.setString(2, proyecto);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                Unidad = new DevolucionesFact();
                                Unidad.setFolio(rs.getInt(1));
                                Unidad.setClaveuni(rs.getString(2));
                                Unidad.setUnidad(rs.getString(3));
                                Unidad.setFechaentrega(rs.getString(4));
                                Unidad.setUsuario(usua);
                                Unidad.setProyecto(Integer.parseInt(proyecto));
                                DatosUnidad.add(Unidad);
                            }

                            Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setString(1, Folio);
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                remisiones = new DevolucionesFact();
                                remisiones.setClavepro(rs2.getString(1));
                                remisiones.setDescripcion(rs2.getString(2));
                                remisiones.setLote(rs2.getString(3));
                                remisiones.setCaducidad(rs2.getString(4));
                                remisiones.setRequerido(rs2.getString(5));
                                remisiones.setUbicacion(rs2.getString(6));
                                remisiones.setSurtido(rs2.getString(7));
                                remisiones.setDevolucion(rs2.getString(8));
                                remisiones.setCosto(rs2.getString(9));
                                remisiones.setMonto(rs2.getString(10));
                                remisiones.setDocumento(rs2.getString(11));
                                remisiones.setIddocumento(rs2.getString(12));
                                remisiones.setProyecto(Integer.parseInt(proyecto));
                                Listaremisiones.add(remisiones);
                            }
                            request.setAttribute("DatosUnidad", DatosUnidad);
                            request.setAttribute("listaRemision", Listaremisiones);
                            request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);
                        }
                    }
                } else {
                    out.println("<script>alert('Ingrese Datos')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }


//MODIFICAR DEVOLUCION POR CLAVE
            if (Accion.equals("btnModificarClave")) {
                int Cantidad = 0, Diferencia = 0;
                String Folio = request.getParameter("Folio");
                String IdFol = request.getParameter("Identi");
                String Devolver = request.getParameter("Devolver");
                String proyecto = request.getParameter("proyecto");
                int Surtida = Integer.parseInt(request.getParameter("Surtida"));
                System.out.println("Folio: " + Folio + " Id: " + IdFol);
                if (!(Devolver == "")) {
                    Cantidad = Integer.parseInt(Devolver);
                    if (Cantidad == 0) {
                        out.println("<script>alert('Cantidad a Devolver debe ser Mayor a Cero')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {
                        Diferencia = Surtida - Cantidad;
                        if (Diferencia < 0) {
                            out.println("<script>alert('Cantidad a Devolver es Mayor a lo Surtido')</script>");
                            out.println("<script>window.history.back()</script>");
                        } else {
                            Consulta = "UPDATE tb_devoglobalfact SET F_CantDevo =? WHERE F_IdFact=? AND F_ClaDoc=?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Devolver);
                            ps.setString(2, IdFol);
                            ps.setString(3, Folio);
                            ps.execute();

                            Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc = ? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Folio);
                            ps.setString(2, proyecto);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                Unidad = new DevolucionesFact();
                                Unidad.setFolio(rs.getInt(1));
                                Unidad.setClaveuni(rs.getString(2));
                                Unidad.setUnidad(rs.getString(3));
                                Unidad.setFechaentrega(rs.getString(4));
                                Unidad.setUsuario(usua);
                                Unidad.setProyecto(Integer.parseInt(proyecto));
                                DatosUnidad.add(Unidad);
                            }

                            Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                            ps2 = con.getConn().prepareStatement(Consulta2);
                            ps2.setString(1, Folio);
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                remisiones = new DevolucionesFact();
                                remisiones.setClavepro(rs2.getString(1));
                                remisiones.setDescripcion(rs2.getString(2));
                                remisiones.setLote(rs2.getString(3));
                                remisiones.setCaducidad(rs2.getString(4));
                                remisiones.setRequerido(rs2.getString(5));
                                remisiones.setUbicacion(rs2.getString(6));
                                remisiones.setSurtido(rs2.getString(7));
                                remisiones.setDevolucion(rs2.getString(8));
                                remisiones.setCosto(rs2.getString(9));
                                remisiones.setMonto(rs2.getString(10));
                                remisiones.setDocumento(rs2.getString(11));
                                remisiones.setIddocumento(rs2.getString(12));
                                remisiones.setProyecto(Integer.parseInt(proyecto));
                                Listaremisiones.add(remisiones);
                            }
                            request.setAttribute("DatosUnidad", DatosUnidad);
                            request.setAttribute("listaRemision", Listaremisiones);
                            request.getRequestDispatcher("/DevolicionFacturaClave.jsp").forward(request, response);
                        }
                    }
                } else {
                    out.println("<script>alert('Ingrese Datos')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

            if (Accion.equals("btnEliminar")) {
                String Folio = request.getParameter("folio");
                String Proyecto = request.getParameter("Proyecto");

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc = ? and f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    Unidad.setProyecto(Integer.parseInt(Proyecto));
                    DatosUnidad.add(Unidad);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    remisiones.setProyecto(Integer.parseInt(Proyecto));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/DevolicionFactura.jsp").forward(request, response);

            }

            if (Accion.equals("btnEliminarClave")) {
                String Folio = request.getParameter("folio");
                String Proyecto = request.getParameter("Proyecto");

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc = ? and f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    Unidad.setProyecto(Integer.parseInt(Proyecto));
                    DatosUnidad.add(Unidad);
                }

                Consulta2 = "SELECT * FROM tb_devoglobalfact WHERE F_ClaDoc=?;";
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    remisiones = new DevolucionesFact();
                    remisiones.setClavepro(rs2.getString(1));
                    remisiones.setDescripcion(rs2.getString(2));
                    remisiones.setLote(rs2.getString(3));
                    remisiones.setCaducidad(rs2.getString(4));
                    remisiones.setRequerido(rs2.getString(5));
                    remisiones.setUbicacion(rs2.getString(6));
                    remisiones.setSurtido(rs2.getString(7));
                    remisiones.setDevolucion(rs2.getString(8));
                    remisiones.setCosto(rs2.getString(9));
                    remisiones.setMonto(rs2.getString(10));
                    remisiones.setDocumento(rs2.getString(11));
                    remisiones.setIddocumento(rs2.getString(12));
                    remisiones.setProyecto(Integer.parseInt(Proyecto));
                    Listaremisiones.add(remisiones);
                }
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/DevolicionFacturaClave.jsp").forward(request, response);

            }

            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(DevolucionesFacturas.class.getName()).log(Level.SEVERE, null, e);
        }

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
