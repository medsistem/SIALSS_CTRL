/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModificarFolio;

//import static Develuciones.ConsultaDevoDaoImpl.getFactorEmpaque;
import conn.ConectionDB;
import in.co.sneh.model.Apartado;
import in.co.sneh.persistance.ApartadoDAOImpl;
import in.co.sneh.picking.service.PickingService;
import in.co.sneh.service.FacturaService;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.DevolucionesFact;

/**
 *
 * @author MEDALFA
 */
@WebServlet(name = "ModificarFolio", urlPatterns = {"/ModificarFolio"})
public class ModificarFolio extends HttpServlet {

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
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        String Consulta = "", Consulta2 = "", Conedit = "";
        String TipoFact = "", usua = "", Concepto = "";
        int edit = 0;
        boolean editable = false;
        try {
            usua = (String) sesion.getAttribute("nombre");
            String Accion = request.getParameter("Accion");

            con.conectar();

            if (Accion.equals("ListaRemision")) {
                request.getRequestDispatcher("/ModificarFolio.jsp").forward(request, response);
            }

            if (Accion.equals("btnMostrar")) {
                int Proyectos = 0;
                String Folio = request.getParameter("Folio");
                String Proyecto = request.getParameter("Nombre");
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Proyectos = Integer.parseInt(Proyecto);
                }

               
                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto=? GROUP BY F_ClaDoc,f.F_ClaCli;";
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
                    Unidad.setProyectoM(Proyectos);
                    DatosUnidad.add(Unidad);
                }

                
                //Consulta2 = "SELECT F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 60) AS F_DesPro, L.F_ClaLot, F_FecCad, F.F_CantReq, F.F_Ubicacion, F.F_CantSur, F.F_Costo, F.F_Monto, F.F_ClaDoc, L.F_Origen, L.F_Proyecto, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica WHERE F.F_ClaDoc = ? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto=? GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";
                Consulta2 = "SELECT F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 60) AS F_DesPro, L.F_ClaLot, F_FecCad, F.F_CantReq, F.F_Ubicacion, F.F_CantSur, F.F_Costo, F.F_Monto, F.F_ClaDoc, L.F_Origen, L.F_Proyecto, F.F_IdFact, CASE WHEN F.F_Ubicacion not IN ('AF','AF1N','AFGC') THEN 0 ELSE 0 END sts FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli   WHERE F.F_ClaDoc= ? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ?  GROUP BY F.F_IdFact HAVING sts > 4 OR sts < 1 ORDER BY F.F_IdFact+0;";                
                ps2 = con.getConn().prepareStatement(Consulta2);
                ps2.setString(1, Folio);
                ps2.setString(2, Proyecto);
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
                    remisiones.setDevolucion(rs2.getString(7));
                    remisiones.setCosto(rs2.getString(8));
                    remisiones.setMonto(rs2.getString(9));
                    remisiones.setDocumento(rs2.getString(10));
                    remisiones.setOrigen(rs2.getInt(11));
                    remisiones.setProyecto(rs2.getInt(12));
                    remisiones.setIddocumento(rs2.getString(13));
                    remisiones.setProyectoM(Proyectos);
                    Listaremisiones.add(remisiones);
                }

                Conedit = "SELECT COUNT(*) AS CONTAR FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot INNER JOIN tb_uniatn U ON F.F_ClaCli=U.F_ClaCli  WHERE F.F_ClaDoc= ? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ?  GROUP BY F.F_IdFact  ORDER BY F.F_IdFact+0;";
                ps3 = con.getConn().prepareStatement(Conedit);
                ps3.setString(1, Folio);
                ps3.setString(2, Proyecto);
                rs3 = ps3.executeQuery();

                while (rs3.next()) {

                    if (rs3.wasNull()) {

                        edit = 0;
                    } else {
                        System.out.println("edit 1: "+edit);
                        edit = rs3.getInt(1);
                    }

                }
                if (edit > 0) {
                    editable = true;
                } else {
                    editable = false;
                }

                request.setAttribute("DatosUnidad", DatosUnidad);
                request.setAttribute("listaRemision", Listaremisiones);
                sesion.setAttribute("editable", editable);

                request.getRequestDispatcher("/ModificarFolio.jsp").forward(request, response);
            }
            

            if (Accion.equals("btnAgregarFolio")) {
                String Folio = request.getParameter("foliod");
                String claveuni = request.getParameter("claveuni");
                String unidad = request.getParameter("unidad");
                String fechaentrega = request.getParameter("fechaentrega");
                int Proyectos = 0;
                 String Proyecto = request.getParameter("Nombre");
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Proyectos = Integer.parseInt(Proyecto);
                }


                PickingService serv = new PickingService();
                int pickingStatus = serv.isFolioEditable(Integer.parseInt(Folio), Proyectos);
                editable = (pickingStatus == -1) || (pickingStatus >= 5);


                sesion.setAttribute("editable", editable);
                sesion.setAttribute("ClaCliFM", request.getParameter("claveuni"));
                sesion.setAttribute("FechaEntFM", request.getParameter("fechaentrega"));
                sesion.setAttribute("ClaProFM", "");
                sesion.setAttribute("DesProFM", "");
                sesion.setAttribute("F_IndGlobal", request.getParameter("foliod"));
                request.getRequestDispatcher("facturacionManualFolio.jsp").forward(request, response);

            }

            //PARA Buscar UNIDAD//
            if (Accion.equals("btnUnidadFolio")) {
                int Proyectos = 0;

                String Folio = request.getParameter("Folio");
                String Proyecto = request.getParameter("Nombre");
                Proyectos = Integer.parseInt(Proyecto);
                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setString(2, Proyecto);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    DatosUnidad.add(Unidad);
                }
                System.out.println(Folio);

                sesion.setAttribute("ProyectoU", "");
                request.setAttribute("DatosUnidad", DatosUnidad);
                request.getRequestDispatcher("/facturacion/ModificarUnidadFolio.jsp").forward(request, response);

            }

            //PARA MODIFICAR UNIDAD//
            if (Accion.equals("btnUnidadSave")) {
                int Proyectos = 0;
                String Folio = request.getParameter("FolioU");
                String Proyecto = request.getParameter("ProyectoU");
                String UnidadS = request.getParameter("UnidadN");

                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Consulta2 = "select F_Id from tb_proyectos where F_DesProy = ? ";
                    ps = con.getConn().prepareStatement(Consulta2);
                    ps.setString(1, Proyecto);
                    rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        Proyectos = rs2.getInt(1);
                    }
                }
                System.out.println("antes del cambio");
                System.out.println(Folio + '-' + UnidadS + '-' + Proyectos + '-' + Proyecto);
                Consulta = "UPDATE tb_factura SET F_ClaCli = ? WHERE F_ClaDoc= ? AND F_Proyecto = ?;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, UnidadS);
                ps.setString(2, Folio);
                ps.setInt(3, Proyectos);
                ps.execute();

                if (!ps.equals("")) {
                    out.println("<script>alert('Unidad cambiada existosamente')</script>");

                }
                //Mostrar los datos despues de actualizar
                ps.clearParameters();
                if ((Proyecto == null) || (Proyecto.equals("")) || (Proyecto.equals("--Selecciona Proyecto--"))) {
                    Proyectos = 0;
                } else {
                    Consulta2 = "select F_Id from tb_proyectos where F_DesProy = ? ";
                    ps = con.getConn().prepareStatement(Consulta2);
                    ps.setString(1, Proyecto);
                    rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        Proyectos = rs2.getInt(1);
                    }
                }
                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                ps.setInt(2, Proyectos);
                rs = ps.executeQuery();
                System.out.println("antes de la busqueda");
                System.out.println(Folio + '-' + Proyectos);
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    DatosUnidad.add(Unidad);
                }
                sesion.setAttribute("ProyectoU", "");
                request.setAttribute("DatosUnidad", DatosUnidad);

                request.getRequestDispatcher("/facturacion/ModificarUnidadFolio.jsp").forward(request, response);
            }

            if (Accion.equals("btnUnidadregresa")) {

                request.getRequestDispatcher("/ModificarFolio.jsp").forward(request, response);

            }

            //FIN MODIFICAR UNIDAD//
            if (Accion.equals("btnEliminar")) {
                String Folio = request.getParameter("folio");

                Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? GROUP BY F_ClaDoc,f.F_ClaCli;";
                ps = con.getConn().prepareStatement(Consulta);
                ps.setString(1, Folio);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Unidad = new DevolucionesFact();
                    Unidad.setFolio(rs.getInt(1));
                    Unidad.setClaveuni(rs.getString(2));
                    Unidad.setUnidad(rs.getString(3));
                    Unidad.setFechaentrega(rs.getString(4));
                    Unidad.setUsuario(usua);
                    DatosUnidad.add(Unidad);
                }

//                Consulta2 = "SELECT * FROM tb_modificacionfolio WHERE F_ClaDoc=?;";
//                ps2 = con.getConn().prepareStatement(Consulta2);
//                ps2.setString(1, Folio);
//                rs2 = ps2.executeQuery();
//                while (rs2.next()) {
//                    remisiones = new DevolucionesFact();
//                    remisiones.setClavepro(rs2.getString(1));
//                    remisiones.setDescripcion(rs2.getString(2));
//                    remisiones.setLote(rs2.getString(3));
//                    remisiones.setCaducidad(rs2.getString(4));
//                    remisiones.setRequerido(rs2.getString(5));
//                    remisiones.setUbicacion(rs2.getString(6));
//                    remisiones.setSurtido(rs2.getString(7));
//                    remisiones.setDevolucion(rs2.getString(8));
//                    remisiones.setCosto(rs2.getString(9));
//                    remisiones.setMonto(rs2.getString(10));
//                    remisiones.setDocumento(rs2.getString(11));
//                    remisiones.setIddocumento(rs2.getString(15));
//                    Listaremisiones.add(remisiones);
//                }
                request.setAttribute("DatosUnidad", DatosUnidad);
//                request.setAttribute("listaRemision", Listaremisiones);
                request.getRequestDispatcher("/ModificarFolio.jsp").forward(request, response);

            }

            if (Accion.equals("btnModificar")) {
                int Cantidad = 0, Diferencia = 0, ExiLot = 0, IndLote = 0;
                String ubicacion = "", FolLot = "", ClaOrg = "", FecFab = "", Cb = "", ClaMar = "", ClaPrv = "", FolLot1 = "", ClaOrg1 = "", FecFab1 = "", Cb1 = "", ClaMar1 = "", ClaPrv1 = "", FolLot2 = "";
                String Folio = request.getParameter("Folio");
                String IdFol = request.getParameter("Identi");
                String Clave = request.getParameter("Clave1");
                String Lote = request.getParameter("Lote1");
                String Caducidad = request.getParameter("Caducidad1");
                String Lote2 = request.getParameter("Lote2");
                String Caducidad2 = request.getParameter("Caducidad2");
                String Origen = request.getParameter("Origen");
                String Proyecto = request.getParameter("Proyecto");
                String Ubicacion1 = request.getParameter("Ubicacion1");
                Integer idLote = 0, idLote1 = 0;
                int Surtida = Integer.parseInt(request.getParameter("Surtida"));
                String ProyectoM = request.getParameter("proyectoM");
                System.out.println("Folio: " + Folio + " Id: " + IdFol);
                if ((Lote2 != "") || (Caducidad2 != "")) {
                    if ((Lote.equals(Lote2)) && (Caducidad.equals(Caducidad2))) {
                        out.println("<script>alert('Datos Iguales agregar lote o caducidad diferentes')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {
                        Consulta = "SELECT F_FolLot,F_ClaOrg,F_FecFab,F_Cb,F_ClaMar,F_ClaPrv, F_IdLote FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Proyecto = ? AND F_Origen = ? LIMIT 1;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Clave);
                        ps.setString(2, Lote);
                        ps.setString(3, Caducidad);
                        ps.setString(4, Proyecto);
                        ps.setString(5, Origen);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            FolLot = rs.getString(1);
                            ClaOrg = rs.getString(2);
                            FecFab = rs.getString(3);
                            Cb = rs.getString(4);
                            ClaMar = rs.getString(5);
                            ClaPrv = rs.getString(6);
                            idLote1 = rs.getInt(7);
                        }
                        ps.clearParameters();

                        Consulta = "SELECT F_FolLot,F_ClaOrg,F_FecFab,F_Cb,F_ClaMar,F_ClaPrv, F_IdLote FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Proyecto = ? AND F_Origen = ? LIMIT 1;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Clave);
                        ps.setString(2, Lote2);
                        ps.setString(3, Caducidad2);
                        ps.setString(4, Proyecto);
                        ps.setString(5, Origen);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            FolLot1 = rs.getString(1);
                            ClaOrg1 = rs.getString(2);
                            FecFab1 = rs.getString(3);
                            Cb1 = rs.getString(4);
                            ClaMar1 = rs.getString(5);
                            ClaPrv1 = rs.getString(6);
                            idLote = rs.getInt(7);
                        }

                        if (FolLot1 != "") {
                            ps.clearParameters();
                            Consulta = "SELECT F_FolLot,F_ExiLot, F_IdLote FROM tb_lote WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Proyecto = ? AND F_Origen = ? AND F_FolLot = ? AND F_Ubica = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, Clave);
                            ps.setString(2, Lote2);
                            ps.setString(3, Caducidad2);
                            ps.setString(4, Proyecto);
                            ps.setString(5, Origen);
                            ps.setString(6, FolLot1);
                            ps.setString(7, Ubicacion1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                FolLot2 = rs.getString(1);
                                ExiLot = rs.getInt(2);

                            }
                            if (FolLot2 != "") {

                                if ((Ubicacion1.equals("AF") || Ubicacion1.equals("AF1N")) && ProyectoM.equals("2")) {
                                    FacturaService fs = new FacturaService();
                                    fs.cambiarLote(Integer.parseInt(FolLot1), Integer.parseInt(FolLot2), idLote, idLote1, Integer.parseInt(Folio));
                                }
                                /*Cantidad = ExiLot + Surtida;
                                Consulta = "UPDATE tb_lote SET F_ExiLot = ? WHERE F_ClaPro = ? AND F_ClaLot = ? AND F_FecCad = ? AND F_Proyecto = ? AND F_Origen = ? AND F_FolLot = ? AND F_Ubica = ?;";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, Surtida);
                                ps.setString(2, Clave);
                                ps.setString(3, Lote2);
                                ps.setString(4, Caducidad2);
                                ps.setString(5, Proyecto);
                                ps.setString(6, Origen);
                                ps.setString(7, FolLot1);
                                ps.setString(8, Ubicacion1);
                                ps.execute();*/
                            } else {
                                ps.clearParameters();

                                if (Ubicacion1.equals("NUEVA")) {
                                    Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                    ps = con.getConn().prepareStatement(Consulta);
                                    ps.setInt(1, 0);
                                    ps.setString(2, Clave);
                                    ps.setString(3, Lote2);
                                    ps.setString(4, Caducidad2);
                                    ps.setInt(5, 0);
                                    ps.setString(6, FolLot1);
                                    ps.setString(7, ClaOrg1);
                                    ps.setString(8, Ubicacion1);
                                    ps.setString(9, FecFab1);
                                    ps.setString(10, Cb1);
                                    ps.setString(11, ClaMar1);
                                    ps.setString(12, Origen);
                                    ps.setString(13, ClaPrv1);
                                    ps.setInt(14, 131);
                                    ps.setString(15, Proyecto);
                                    ps.execute();
                                } else {

                                    Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                    ps = con.getConn().prepareStatement(Consulta);
                                    ps.setInt(1, 0);
                                    ps.setString(2, Clave);
                                    ps.setString(3, Lote2);
                                    ps.setString(4, Caducidad2);
                                    ps.setInt(5, 0);
                                    ps.setString(6, FolLot1);
                                    ps.setString(7, ClaOrg1);
                                    ps.setString(8, "NUEVA");
                                    ps.setString(9, FecFab1);
                                    ps.setString(10, Cb1);
                                    ps.setString(11, ClaMar1);
                                    ps.setString(12, Origen);
                                    ps.setString(13, ClaPrv1);
                                    ps.setInt(14, 131);
                                    ps.setString(15, Proyecto);
                                    ps.execute();
                                    ps.clearParameters();

                                    Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                    ps = con.getConn().prepareStatement(Consulta);
                                    ps.setInt(1, 0);
                                    ps.setString(2, Clave);
                                    ps.setString(3, Lote2);
                                    ps.setString(4, Caducidad2);
                                    ps.setInt(5, 0);
                                    ps.setString(6, FolLot1);
                                    ps.setString(7, ClaOrg1);
                                    ps.setString(8, Ubicacion1);
                                    ps.setString(9, FecFab1);
                                    ps.setString(10, Cb1);
                                    ps.setString(11, ClaMar1);
                                    ps.setString(12, Origen);
                                    ps.setString(13, ClaPrv1);
                                    ps.setInt(14, 131);
                                    ps.setString(15, Proyecto);
                                    ps.execute();
                                }
                            }

                            if (!((Ubicacion1.equals("AF") || Ubicacion1.equals("AF1N") || Ubicacion1.equals("AFGC")) && ProyectoM.equals("2"))) {
                                ps.clearParameters();
                                Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Folio);
                                ps.setString(3, "7");
                                ps.setString(4, Clave);
                                ps.setInt(5, Surtida);
                                ps.setString(6, "0.00");
                                ps.setString(7, "0.00");
                                ps.setString(8, "1");
                                ps.setString(9, FolLot);
                                ps.setString(10, Ubicacion1);
                                ps.setString(11, ClaPrv);
                                ps.setString(12, usua);
                                ps.execute();

                                ps.clearParameters();

                                Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Folio);
                                ps.setString(3, "55");
                                ps.setString(4, Clave);
                                ps.setInt(5, Surtida);
                                ps.setString(6, "0.00");
                                ps.setString(7, "0.00");
                                ps.setString(8, "-1");
                                ps.setString(9, FolLot);
                                ps.setString(10, Ubicacion1);
                                ps.setString(11, ClaPrv);
                                ps.setString(12, usua);
                                ps.execute();

                                ps.clearParameters();

                                Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Folio);
                                ps.setString(3, "8");
                                ps.setString(4, Clave);
                                ps.setInt(5, Surtida);
                                ps.setString(6, "0.00");
                                ps.setString(7, "0.00");
                                ps.setString(8, "1");
                                ps.setString(9, FolLot1);
                                ps.setString(10, Ubicacion1);
                                ps.setString(11, ClaPrv);
                                ps.setString(12, usua);
                                ps.execute();

                                ps.clearParameters();

                                Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Folio);
                                ps.setString(3, "51");
                                ps.setString(4, Clave);
                                ps.setInt(5, Surtida);
                                ps.setString(6, "0.00");
                                ps.setString(7, "0.00");
                                ps.setString(8, "-1");
                                ps.setString(9, FolLot1);
                                ps.setString(10, Ubicacion1);
                                ps.setString(11, ClaPrv);
                                ps.setString(12, usua);
                                ps.execute();
                            } else {
                                ApartadoDAOImpl dao = new ApartadoDAOImpl(con.getConn());
                                Apartado a = new Apartado();
                                a.setCant(Surtida);
                                a.setClaDoc(Folio);
                                a.setIdLote(idLote);
                                a.setProyecto(Integer.parseInt(ProyectoM));
                                a.setStatus(1);
                                dao.guardar(a);
                                dao.delete(idLote1);
                            }
                            ps.clearParameters();
                            Consulta = "UPDATE tb_factura SET F_Lote = ? WHERE F_IdFact = ? AND F_ClaDoc = ? AND F_ClaPro = ? AND F_Lote = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setString(1, FolLot1);
                            ps.setString(2, IdFol);
                            ps.setString(3, Folio);
                            ps.setString(4, Clave);
                            ps.setString(5, FolLot);
                            ps.execute();

                        } else {
                            ps.clearParameters();
                            Consulta = "SELECT F_IndLote FROM tb_indice;";
                            ps = con.getConn().prepareStatement(Consulta);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                IndLote = rs.getInt(1);
                            }

                            ps.clearParameters();
                            Consulta = "UPDATE tb_indice SET F_IndLote = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, IndLote + 1);
                            ps.execute();

                            ps.clearParameters();

                            if (Ubicacion1.equals("NUEVA")) {
                                Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Clave);
                                ps.setString(3, Lote2);
                                ps.setString(4, Caducidad2);
                                ps.setInt(5, 0);
                                ps.setInt(6, IndLote);
                                ps.setString(7, ClaOrg);
                                ps.setString(8, Ubicacion1);
                                ps.setString(9, FecFab);
                                ps.setString(10, Cb);
                                ps.setString(11, ClaMar);
                                ps.setString(12, Origen);
                                ps.setString(13, ClaPrv);
                                ps.setInt(14, 131);
                                ps.setString(15, Proyecto);
                                ps.execute();

                            } else {

                                Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Clave);
                                ps.setString(3, Lote2);
                                ps.setString(4, Caducidad2);
                                ps.setInt(5, 0);
                                ps.setInt(6, IndLote);
                                ps.setString(7, ClaOrg);
                                ps.setString(8, "NUEVA");
                                ps.setString(9, FecFab);
                                ps.setString(10, Cb);
                                ps.setString(11, ClaMar);
                                ps.setString(12, Origen);
                                ps.setString(13, ClaPrv);
                                ps.setInt(14, 131);
                                ps.setString(15, Proyecto);
                                ps.execute();

                                Consulta = "INSERT tb_lote VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                                ps = con.getConn().prepareStatement(Consulta);
                                ps.setInt(1, 0);
                                ps.setString(2, Clave);
                                ps.setString(3, Lote2);
                                ps.setString(4, Caducidad2);
                                ps.setInt(5, 0);
                                ps.setInt(6, IndLote);
                                ps.setString(7, ClaOrg);
                                ps.setString(8, Ubicacion1);
                                ps.setString(9, FecFab);
                                ps.setString(10, Cb);
                                ps.setString(11, ClaMar);
                                ps.setString(12, Origen);
                                ps.setString(13, ClaPrv);
                                ps.setInt(14, 131);
                                ps.setString(15, Proyecto);
                                ps.execute();
                            }

                            ps.clearParameters();
                            Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?,'');";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, 0);
                            ps.setString(2, Folio);
                            ps.setString(3, "7");
                            ps.setString(4, Clave);
                            ps.setInt(5, Surtida);
                            ps.setString(6, "0.00");
                            ps.setString(7, "0.00");
                            ps.setString(8, "1");
                            ps.setString(9, FolLot);
                            ps.setString(10, Ubicacion1);
                            ps.setString(11, ClaPrv);
                            ps.setString(12, usua);
                            ps.execute();

                            ps.clearParameters();

                            Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?,'');";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, 0);
                            ps.setString(2, Folio);
                            ps.setString(3, "55");
                            ps.setString(4, Clave);
                            ps.setInt(5, Surtida);
                            ps.setString(6, "0.00");
                            ps.setString(7, "0.00");
                            ps.setString(8, "-1");
                            ps.setString(9, FolLot);
                            ps.setString(10, Ubicacion1);
                            ps.setString(11, ClaPrv);
                            ps.setString(12, usua);
                            ps.execute();

                            ps.clearParameters();

                            Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?,'');";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, 0);
                            ps.setString(2, Folio);
                            ps.setString(3, "8");
                            ps.setString(4, Clave);
                            ps.setInt(5, Surtida);
                            ps.setString(6, "0.00");
                            ps.setString(7, "0.00");
                            ps.setString(8, "1");
                            ps.setInt(9, IndLote);
                            ps.setString(10, Ubicacion1);
                            ps.setString(11, ClaPrv);
                            ps.setString(12, usua);
                            ps.execute();

                            ps.clearParameters();

                            Consulta = "INSERT tb_movinv VALUES (?,CURDATE(),?,?,?,?,?,?,?,?,?,?,CURTIME(),?,'');";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, 0);
                            ps.setString(2, Folio);
                            ps.setString(3, "51");
                            ps.setString(4, Clave);
                            ps.setInt(5, Surtida);
                            ps.setString(6, "0.00");
                            ps.setString(7, "0.00");
                            ps.setString(8, "-1");
                            ps.setInt(9, IndLote);
                            ps.setString(10, Ubicacion1);
                            ps.setString(11, ClaPrv);
                            ps.setString(12, usua);
                            ps.execute();

                            ps.clearParameters();
                            Consulta = "UPDATE tb_factura SET F_Lote = ? WHERE F_IdFact = ? AND F_ClaDoc = ? AND F_ClaPro = ? AND F_Lote = ?;";
                            ps = con.getConn().prepareStatement(Consulta);
                            ps.setInt(1, IndLote);
                            ps.setString(2, IdFol);
                            ps.setString(3, Folio);
                            ps.setString(4, Clave);
                            ps.setString(5, FolLot);
                            ps.execute();

                        }

                        ps.clearParameters();

                        Consulta = "SELECT F_ClaDoc,f.F_ClaCli,u.F_NomCli,DATE_FORMAT(F_FecEnt,'%d/%m/%Y') AS F_FecEnt FROM tb_factura f INNER JOIN tb_uniatn u on f.F_ClaCli=u.F_ClaCli WHERE F_ClaDoc=? AND f.F_Proyecto = ? GROUP BY F_ClaDoc,f.F_ClaCli;";
                        ps = con.getConn().prepareStatement(Consulta);
                        ps.setString(1, Folio);
                        ps.setString(2, ProyectoM);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            Unidad = new DevolucionesFact();
                            Unidad.setFolio(rs.getInt(1));
                            Unidad.setClaveuni(rs.getString(2));
                            Unidad.setUnidad(rs.getString(3));
                            Unidad.setFechaentrega(rs.getString(4));
                            Unidad.setUsuario(usua);
                            Unidad.setProyectoM(Integer.parseInt(ProyectoM));
                            DatosUnidad.add(Unidad);
                        }

                        Consulta2 = "SELECT F.F_ClaPro, SUBSTR(M.F_DesPro, 1, 60) AS F_DesPro, L.F_ClaLot, F_FecCad, F.F_CantReq, F.F_Ubicacion, F.F_CantSur, F.F_Costo, F.F_Monto, F.F_ClaDoc, L.F_Origen, L.F_Proyecto, F.F_IdFact FROM tb_factura F INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica WHERE F.F_ClaDoc = ? AND F_CantSur>0 AND F.F_StsFact='A' AND F.F_Proyecto = ? GROUP BY F.F_IdFact ORDER BY F.F_IdFact+0;";
                        ps2 = con.getConn().prepareStatement(Consulta2);
                        ps2.setString(1, Folio);
                        ps2.setString(2, ProyectoM);
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
                            remisiones.setDevolucion(rs2.getString(7));
                            remisiones.setCosto(rs2.getString(8));
                            remisiones.setMonto(rs2.getString(9));
                            remisiones.setDocumento(rs2.getString(10));
                            remisiones.setOrigen(rs2.getInt(11));
                            remisiones.setProyecto(rs2.getInt(12));
                            remisiones.setIddocumento(rs2.getString(13));
                            remisiones.setProyectoM(Integer.parseInt(ProyectoM));
                            Listaremisiones.add(remisiones);
                        }

                        String queryElimina = "DELETE FROM tb_abastoweb WHERE F_Sts = 0 AND F_Proyecto = ? AND F_ClaDoc = ?;";

                        String ValidaAbasto = "SELECT COUNT(*) FROM tb_abastoweb WHERE F_Sts = 1 AND F_Proyecto = ? AND F_ClaDoc = ?;";

                        String queryInserta = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0,?);";

                        String queryDatosCsV = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen AS ORIGEN, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(),F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F_ClaDoc = ? AND F_CantSur > 0 AND F_StsFact = 'A' AND F.F_Proyecto = ? GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

                        String getFactorEmpaque = "SELECT IFNULL(F_FactorEmpaque, 0) as factor FROM tb_compra where F_Lote = ? order by F_IdCom DESC";

                        ps2.clearParameters();
                        int Cuenta = 0, Origen2 = 0;
//                        String Origenes2 = "";
                        ps2 = con.getConn().prepareStatement(ValidaAbasto);
                        ps2.setString(1, ProyectoM);
                        ps2.setString(2, Folio);
                        rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            Cuenta = rs2.getInt(1);
                        }
                        ps2.clearParameters();
                        if (Cuenta == 0) {
                            ps2 = con.getConn().prepareStatement(queryElimina);
                            ps2.setString(1, ProyectoM);
                            ps2.setString(2, Folio);
                            ps2.execute();

                            ps2.clearParameters();
                            ps2 = con.getConn().prepareStatement(queryDatosCsV);
                            ps2.setString(1, Folio);
                            ps2.setString(2, ProyectoM);
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {

//                                Origen2 = rs2.getInt(9);
//                                if ((Origen2 == 0) || (Origen2 == 1)) {
//                                    Origenes2 = "0";
//                                } else {
//                                    Origenes2 = "0";
//                                }
                                int factorEmpaque = 1;
                                int folLot = rs2.getInt("LOTE");
                                ps3 = con.getConn().prepareStatement(getFactorEmpaque);
                                ps3.setInt(1, folLot);
                                rs3 = ps3.executeQuery();
                                if (rs3.next()) {
                                    factorEmpaque = rs3.getInt("factor");
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
                                ps.setInt(9, rs2.getInt(9));
                                ps.setString(10, rs2.getString(10));
                                ps.setString(11, usua);
                                ps.setInt(12, factorEmpaque);
                                ps.execute();

                            }
                        }

                        request.setAttribute("DatosUnidad", DatosUnidad);
                        request.setAttribute("listaRemision", Listaremisiones);
                        request.getRequestDispatcher("/ModificarFolio.jsp").forward(request, response);
                    }

                } else {
                    out.println("<script>alert('Ingrese Datos')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            }

            con.cierraConexion();
        } catch (Exception e) {
            Logger.getLogger(ModificarFolio.class.getName()).log(Level.SEVERE, null, e);
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
