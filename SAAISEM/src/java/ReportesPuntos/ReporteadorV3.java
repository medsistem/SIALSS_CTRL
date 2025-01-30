package ReportesPuntos;

import ReportesPuntos.cache.CacheQuery;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Reporteado de los reportes txt facturación
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "ReporteadorV3", urlPatterns = {"/entregas/v3"})
public class ReporteadorV3 extends HttpServlet {

    private final String[] PARAMETROS = new String[]{"unidad", "producto", "juris", "municipio", "nivel",  "doct","lote",  "unireq", "unisur"};

    private final String[] PARAMETROS_BUSQUEDA = new String[]{"ListJuris", "ListMuni", "ListUni", "ListClave"};

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
        HttpSession sesion = request.getSession();

        if (sesion == null) {
            sesion = request.getSession(true);
        }

        String inicio, fin;
        String surtido, requerido;
        String unidad, producto, jurisdicion, nivel, municipio,  requerimiento, folio, lote, proyecto,  Reporte;

//        costo = request.getParameter("costo");
        surtido = request.getParameter("unisur");
        requerido = request.getParameter("unireq");

        if ( surtido == null && requerido == null) {
            out.println("<script>alert('Favor de seleccionar cifras')</script>");
            out.println("<script>window.history.back()</script>");
            out.close();
            return;
        }

        unidad = request.getParameter("unidad");
        producto = request.getParameter("producto");
        jurisdicion = request.getParameter("juris");
        nivel = request.getParameter("nivel");
        municipio = request.getParameter("municipio");
        
        proyecto = request.getParameter("2");
//        origen = request.getParameter("origen");
        requerimiento = request.getParameter("tipofact");
        folio = request.getParameter("doct");
        lote = request.getParameter("lote");
        Reporte = request.getParameter("Reporte");

        if (unidad == null && producto == null && jurisdicion == null && nivel == null && municipio == null &&  requerimiento == null && folio == null && lote == null ) {

            out.println("<script>alert('Favor de seleccionar al menos otra categaria además de \"Cifras\"')</script>");
            out.println("<script>window.history.back()</script>");
            out.close();
            return;
        }

        inicio = request.getParameter("fecha_ini");
        fin = request.getParameter("fecha_fin");

        if (inicio.isEmpty() && fin.isEmpty()) {
            out.println("<script>alert('Favor de seleccionar Fechas')</script>");
            out.println("<script>window.history.back()</script>");
            out.close();
            return;
        }

        try {

            ConectionDB con = new ConectionDB();
            con.conectar();

            ResultSet rs;
            String query;

            if (unidad != null) {
                query = porUnidad(request);
            } else if (jurisdicion != null
                    || nivel != null
                    || municipio != null) {
                query = porJuris(request);
            } else {
                query = paraResto(request);
            }

            rs = con.consulta(query);

            ArrayList<String> columnas = new ArrayList<>();
            columnas.add("id");
            int total_columnas = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= total_columnas; i++) {
                columnas.add(rs.getMetaData().getColumnLabel(i));
            }

            String path = sesion.getServletContext().getRealPath("/queries/");

            CacheQuery cache = new CacheQuery(path);
            cache.writeJSON(sesion.getAttribute("IdUsu").toString(),
                    sesion.getId(), "entregas", rs);

            sesion.setAttribute("ultima_columnas", columnas);
            sesion.setAttribute("ultimo_inicio", inicio);
            sesion.setAttribute("ultima_fin", fin);

            rs.close();
            con.estancia.close();
            con.cierraConexion();
           if (Reporte.equals("1")) {
//                response.sendRedirect(request.getContextPath() + "/reportes/ReporteadorComprasAuditoria.jsp");/reportes/ReporteadorConsultaCompras.jsp
          response.sendRedirect(request.getContextPath() + "/reportes/ReporteadorConsultaComprasAud.jsp");
           }

        } catch (SQLException ex) {
            Logger.getLogger(ReporteadorV3.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String porUnidad(HttpServletRequest request) {

        StringBuilder campos = new StringBuilder();
        StringBuilder agrupamientos = new StringBuilder();
        StringBuilder tablas = new StringBuilder();
        tablas.append("%s ");
        String join = "", valor;
        int ban = 0;
        for (String clave : PARAMETROS) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }

            switch (valor) {

                case "unidad":
                    campos.append("f.F_ClaCli AS Clave_Unidad, u.F_NomCli AS Unidad");
                    break;

                case "juris":
                    campos.append(", f.F_Juris AS Jurisdicion");
//                    tablas.append(" INNER JOIN tb_juriis AS j ON u.F_ClaJur = j.F_ClaJurIS");
                    break;

                case "municipio":
                    campos.append(", f.F_Munis AS Municipio");
//                    tablas.append(" INNER JOIN tb_muniis AS m ON u.F_ClaMun = m.F_ClaMunIS");
                    break;

//                case "nivel":
//                    campos.append(",u.F_Tipo AS nivel");
//                    break;

                case "producto":
                    campos.append(",f.F_ClaPro AS clave, f.F_Descripcion AS Descripcion_Clave");
//                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    agrupamientos.append(",f.F_ClaPro");
                    break;

                case "doct":
                    campos.append(",f.F_FechaEnt AS FecEnt,f.F_Folio AS folio");
                    agrupamientos.append(",f.F_FechaEnt,f.F_Folio");
//                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
//                    }
                    break;

//                case "proyecto":
//                    ban = 1;
//                    campos.append(",p.F_DesProy AS Proyecto");
//                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id";
//                    agrupamientos.append(",l.F_Proyecto");
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id");
//                    }
//                    break;

//                case "cause":
//                    ban = 1;
//                    campos.append(",tp.F_Cause AS Tipo_Cause");
//                    join = " INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id";
//                    agrupamientos.append(",tp.F_Cause");
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id");
//                    }
//                    break;

                case "lote":

                    campos.append(",f.F_Caducidad AS caducidad, f.F_Lote AS lote");
//                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
//                    if (ban == 0) {
//                        ban = 1;
//                        if (tablas.indexOf(join) == -1) {
//                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
//                        }
//                    }
                    agrupamientos.append(",f.F_Lote");
                    break;

//                case "origen":
//
//                    campos.append(",l.F_Origen AS origen");
//                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
//                    agrupamientos.append(",l.F_Origen");
//                    if (ban == 0) {
//                        if (tablas.indexOf(join) == -1) {
//                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
//                        }
//                    }

//                    break;

//                case "tipofact":
//                    campos.append(",o.F_Tipo AS tipo_requerimiento");
//                    tablas.append(" INNER JOIN tb_obserfact AS o ON o.F_IdFact = f.F_IdFact");
//                    agrupamientos.append(",o.F_Tipo");
//                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    break;

//                case "costo":
//                    campos.append(",Sum(f.F_Monto) AS costo");
//                    break;
            }
        }

        StringBuilder parametrosBusqueda = new StringBuilder();
        for (String clave : PARAMETROS_BUSQUEDA) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }

            if (valor.isEmpty()) {
                continue;
            }

            String busqueda = "%s";
            switch (clave) {

                case "ListJuris":
                    busqueda = "AND u.F_ClaJur IN (%s) ";
                    break;

                case "ListMuni":
                    busqueda = "AND u.F_ClaMun IN (%s) ";
                    break;

                case "ListUni":
                    busqueda = "AND f.F_ClaCli IN (%s) ";
                    break;

                case "ListClave":
                    busqueda = "AND f.F_ClaPro IN (%s) ";
                    break;

//                case "ListProyect":
//                    busqueda = "AND u.F_Proyecto IN (%s) ";
//                    break;

                case "LisTipUni":
                    tablas.append(" INNER JOIN tb_uniatn AS ut ON f.F_ClaCli = ut.F_ClaCli");
                    busqueda = "AND ut.F_Tipo IN (%s) ";
                    break;
            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura_plain AS f %s  WHERE f.F_FechaEnt BETWEEN '%s' AND '%s' %s  GROUP BY f.F_ClaCli %s ORDER BY NULL";

        query = String.format(query, campos.toString(), tablas.toString(),
                request.getParameter("fecha_ini"), request.getParameter("fecha_fin"),
                parametrosBusqueda.toString(), agrupamientos.toString());

        query = String.format(query, "INNER JOIN tb_uniatn AS u ON f.F_ClaCli = u.F_ClaCli");

        //String query_principal = "(%s) UNION (%s)";
        //String query_principal = "";
        //query_principal = String.format(query_principal, query);
        String query_principal = String.format(query);

        query_principal = query_principal.replace("%s", "");
        
        return query_principal;
    }

    
    private String porJuris(HttpServletRequest request) {

        StringBuilder campos = new StringBuilder();
        StringBuilder camposExternos = new StringBuilder();
        StringBuilder agrupamientos = new StringBuilder();
        StringBuilder agrupamientosExternos = new StringBuilder();
        StringBuilder tablas = new StringBuilder();
        tablas.append("%s ");

        String valor, join = "";
        int ban = 0;
        for (String clave : PARAMETROS) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }
            String separator = "";

            switch (valor) {

                case "juris":
                    campos.append("f.F_Juris AS Jurisdicion");
                    camposExternos.append("total.Jurisdicion");

//                    tablas.append(" INNER JOIN tb_juriis AS j ON u.F_ClaJur = j.F_ClaJurIS");
                    agrupamientos.append("f.F_Juris");
                    agrupamientosExternos.append("total.Jurisdicion");

                    break;

                case "municipio":

                    if (campos.length() != 0) {
                        separator = ",";
                    }

//                    tablas.append(" INNER JOIN tb_munis AS m ON u.F_ClaMun = m.F_ClaMunIS");
                    campos.append(separator);
                    campos.append("f.F_Munis AS Municipio");
                    agrupamientos.append(separator);
                    agrupamientos.append("f.F_Munis");

                    camposExternos.append(separator);
                    camposExternos.append("total.Municipio");
                    agrupamientosExternos.append(separator);
                    agrupamientosExternos.append("total.Municipio");

                    break;

                case "nivel":

                    if (campos.length() != 0) {
                        separator = ",";
                    }

                    campos.append(separator);
                    campos.append("u.F_Tipo AS nivel");
                    agrupamientos.append(separator);
                    agrupamientos.append("u.F_Tipo");

                    camposExternos.append(separator);
                    camposExternos.append("total.nivel");
                    agrupamientosExternos.append(separator);
                    agrupamientosExternos.append("total.nivel");

                    break;

                case "producto":
                    campos.append(",f.F_ClaPro AS clave, f.F_Descripcion AS Descripcion_Clave");
                    camposExternos.append(",total.clave,total.Descripcion_Clave");
                    agrupamientos.append(",f.F_ClaPro");
                    agrupamientosExternos.append(",total.clave");
//                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    break;

                case "doct":
                    campos.append(",f.F_FechaEnt AS FecEnt,f.F_Folio AS folio");
                    camposExternos.append(",total.FecEnt,total.folio");
                    agrupamientos.append(",f.F_FechaEnt,f.F_F_Folio");
                    agrupamientosExternos.append(",total.FecEnt,total.folio");
//                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
//                    }
                    break;

                case "lote":
                    campos.append(",f.F_Caducidad AS caducidad, l.F_Lote AS lote");
                    camposExternos.append(",total.caducidad, total.lote");
//                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    agrupamientos.append(",f.F_Lote");
                    agrupamientosExternos.append(",total.lote, total.caducidad");
//                    if (ban == 0) {
//                        ban = 1;
//                        if (tablas.indexOf(join) == -1) {
//                            tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
//                        }
//                    }
                    break;

//                case "origen":
//                    campos.append(",l.F_Origen AS origen");
//                    camposExternos.append(",total.origen");
//                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
//                    if (ban == 0) {
//                        if (tablas.indexOf(join) == -1) {
//                            tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
//                        }
//                    }
//
//                    agrupamientos.append(",l.F_Origen");
//                    agrupamientosExternos.append(",total.origen");
//
//                    break;

//                case "tipofact":
//                    campos.append(",o.F_Tipo AS tipo_requerimiento");
//                    camposExternos.append(",total.tipo_requerimiento");
//                    tablas.append(" INNER JOIN tb_obserfact AS o ON o.F_IdFact = f.F_IdFact");
//                    agrupamientos.append(",o.F_Tipo");
//                    agrupamientosExternos.append(",total.tipo_requerimiento");
//                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    camposExternos.append(",Sum(total.requerido) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    camposExternos.append(",Sum(total.surtido) AS surtido");
                    break;

//                case "costo":
//                    campos.append(",Sum(f.F_Monto) AS costo");
//                    camposExternos.append(",Sum(total.costo) AS costo");
//                    break;
//
//                case "proyecto":
//                    ban = 1;
//                    campos.append(",p.F_DesProy AS Proyecto");
//                    camposExternos.append(",total.Proyecto");
//                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id";
//                    agrupamientos.append(",l.F_Proyecto");
//                    agrupamientosExternos.append(",total.Proyecto");
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id");
//                    }
//                    break;

//                case "cause":
//                    ban = 1;
//                    campos.append(",tp.F_Cause AS Tipo_Cause");
//                    camposExternos.append(",total.Tipo_Cause");
//                    join = " INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id";
//                    agrupamientos.append(",tp.F_Cause");
//                    agrupamientosExternos.append(",total.Tipo_Cause");
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id");
//                    }
//                    break;
            }
        }
        

        StringBuilder parametrosBusqueda = new StringBuilder();
        for (String clave : PARAMETROS_BUSQUEDA) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }

            if (valor.isEmpty()) {
                continue;
            }

            String busqueda = "%s";
            switch (clave) {

                case "ListJuris":
                    busqueda = "AND u.F_ClaJur IN (%s) ";
                    break;

                case "ListMuni":
                    busqueda = "AND u.F_ClaMun IN (%s) ";
                    break;

                case "ListUni":
                    busqueda = "AND f.F_ClaCli IN (%s) ";
                    break;

                case "ListClave":
                    busqueda = "AND f.F_ClaPro IN (%s) ";
                    break;

            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura_plain AS f %s WHERE f.F_FechaEnt BETWEEN '%s' AND '%s' %s  GROUP BY %s ORDER BY NULL";

        query = String.format(query, campos.toString(), tablas.toString(),
                request.getParameter("fecha_ini"), request.getParameter("fecha_fin"),
                parametrosBusqueda.toString(), agrupamientos.toString());

        query = String.format(query, "INNER JOIN tb_uniatn AS u ON f.F_ClaCli = u.F_ClaCli");
        System.out.println("Query sin lista"+query);
        String query_principal = "SELECT %s FROM (%s) AS total GROUP BY %s ORDER BY NULL;";
        query_principal = String.format(query_principal, camposExternos.toString(), query, agrupamientosExternos.toString());
       
        query_principal = query_principal.replace("%s", "");
        System.out.println("Query Principal"+query_principal);
        return query_principal;
        
    }

    ////////////*********************///////////
    
    private String paraResto(HttpServletRequest request) {

        StringBuilder campos = new StringBuilder();
        StringBuilder agrupamientos = new StringBuilder();
        StringBuilder tablas = new StringBuilder();
        tablas.append("%s ");

        String valor;
        String join = "", separador;
        int ban = 0;
        for (String clave : PARAMETROS) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }

            separador = ",";
            if (campos.length() == 0) {
                separador = "";
            }

            switch (valor) {

                case "producto":
                    campos.append(separador);
                    agrupamientos.append(separador);

                    campos.append("f.F_ClaPro AS clave, f.F_Descripcion AS Descripcion_Clave");
                    agrupamientos.append("f.F_ClaPro");
//                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    break;

                case "doct":
                    campos.append(separador);
                    agrupamientos.append(separador);

                    campos.append("f.F_FechaEnt AS FecEnt,f.F_Folio AS folio");
                    agrupamientos.append(",f.F_FechaEnt,f.F_Folio");
//                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
//                    if (tablas.indexOf(join) == -1) {
//                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
//                    }
                    break;

                case "lote":
                    campos.append(separador);
                    agrupamientos.append(separador);
                    campos.append("f.F_Caducidad AS caducidad, f.F_Lote AS lote");
//                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
//                    if (ban == 0) {
//                        ban = 1;
//                        if (tablas.indexOf(join) == -1) {
//                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
//                        }
//                    }
                    agrupamientos.append("f.F_Lote");
                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    break;

            }
        }

        StringBuilder parametrosBusqueda = new StringBuilder();
        for (String clave : PARAMETROS_BUSQUEDA) {

            valor = request.getParameter(clave);
            if (valor == null) {
                continue;
            }

            if (valor.isEmpty()) {
                continue;
            }

            String busqueda = "%s";
            switch (clave) {

                case "ListJuris":
                    busqueda = "AND u.F_ClaJur IN (%s) ";
                    break;

                case "ListMuni":
                    busqueda = "AND u.F_ClaMun IN (%s) ";
                    break;

                case "ListUni":
                    busqueda = "AND f.F_ClaCli IN (%s) ";
                    break;

                case "ListClave":
                    busqueda = "AND f.F_ClaPro IN (%s) ";
                    break;

//                case "ListProyect":
//                    busqueda = "AND u.F_Proyecto IN (%s) ";
//                    break;

                case "LisTipUni":
                    tablas.append(" INNER JOIN tb_uniatn AS ut ON f.F_ClaCli = ut.F_ClaCli");
                    busqueda = "AND ut.F_Tipo IN (%s) ";
                    break;
            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura_plain AS f %s WHERE f.FechaEnt BETWEEN '%s' AND '%s' %s  GROUP BY %s ORDER BY NULL";

        query = String.format(query, campos.toString(), tablas.toString(),
                request.getParameter("fecha_ini"), request.getParameter("fecha_fin"),
                parametrosBusqueda.toString(), agrupamientos.toString());

        query = String.format(query, "INNER JOIN tb_uniatn AS u ON f.F_ClaCli = u.F_ClaCli");

        query = query.replace("%s", "");

        return query;
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
