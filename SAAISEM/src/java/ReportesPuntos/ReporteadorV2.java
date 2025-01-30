package ReportesPuntos;

import ReportesPuntos.cache.CacheQuery;
import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ReporteadorV2", urlPatterns = {"/entregas/v2"})
public class ReporteadorV2 extends HttpServlet {

    private final String[] PARAMETROS = new String[]{"unidad", "juris",
        "municipio", "nivel", "producto", "doct", "proyecto", "cause", "lote", "origen", "tipofact", "unireq", "unisur", "costo"};

    private final String[] PARAMETROS_BUSQUEDA = new String[]{"ListJuris", "ListMuni", "ListUni", "ListClave", "LisTipUni", "ListProyect"};

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
        String surtido, requerido, costo;
        String unidad, producto, jurisdicion, nivel, municipio, origen, requerimiento, folio, lote, proyecto, cause, Reporte;

        costo = request.getParameter("costo");
        surtido = request.getParameter("unisur");
        requerido = request.getParameter("unireq");

        if (costo == null && surtido == null && requerido == null) {
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
        cause = request.getParameter("cause");
        proyecto = request.getParameter("proyecto");
        origen = request.getParameter("origen");
        requerimiento = request.getParameter("tipofact");
        folio = request.getParameter("doct");
        lote = request.getParameter("lote");
        Reporte = request.getParameter("Reporte");

        if (unidad == null && producto == null && jurisdicion == null && nivel == null && municipio == null && origen == null && requerimiento == null && folio == null && lote == null && proyecto == null && cause == null) {

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
                response.sendRedirect(request.getContextPath() + "/reportes/ReporteadorConsulta.jsp");
            } else if (Reporte.equals("2")) {
                response.sendRedirect(request.getContextPath() + "/reportes/ReporteadorConsultaCompras.jsp");
            }
            else if (Reporte.equals("3")) {
                response.sendRedirect(request.getContextPath() + "/reportes/ReporteadorConsultaComprasM.jsp");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReporteadorV2.class.getName()).log(Level.SEVERE, null, ex);
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
                    campos.append(", j.F_DesJurIS AS Jurisdicion");
                    tablas.append(" INNER JOIN tb_juriis AS j ON u.F_ClaJur = j.F_ClaJurIS");
                    break;

                case "municipio":
                    campos.append(", m.F_DesMunIS AS Municipio");
                    tablas.append(" INNER JOIN tb_muniis AS m ON u.F_ClaMun = m.F_ClaMunIS");
                    break;

                case "nivel":
                    campos.append(",tu.F_NomUni AS nivel");
                    tablas.append(" INNER JOIN tb_tipunidad AS tu ON u.F_Tipo = tu.F_idTipUni ");
                    break;

                case "producto":
                    campos.append(",f.F_ClaPro AS clave, md.F_DesPro AS Descripcion_Clave");
                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    agrupamientos.append(",f.F_ClaPro");
                    break;

                case "doct":
                    campos.append(",f.F_FecEnt AS FecEnt,f.F_ClaDoc AS folio,IFNULL(ob.F_Tipo, '') AS tipo");
                    agrupamientos.append(",f.F_FecEnt,f.F_ClaDoc");
                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
                    }
                    break;

                case "proyecto":
                    ban = 1;
                    campos.append(",p.F_DesProy AS Proyecto");
                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id";
                    agrupamientos.append(",l.F_Proyecto");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id");
                    }
                    break;

                case "cause":
                    ban = 1;
                    campos.append(",tp.F_Cause AS Tipo_Cause");
                    join = " INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id";
                    agrupamientos.append(",tp.F_Cause");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id");
                    }
                    break;

                case "lote":

                    campos.append(",l.F_FecCad AS caducidad, l.F_ClaLot AS lote");
                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    if (ban == 0) {
                        ban = 1;
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }
                    agrupamientos.append(",f.F_Lote");
                    break;

                case "origen":

                    campos.append(",l.F_Origen AS origen");
                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    agrupamientos.append(",l.F_Origen");
                    if (ban == 0) {
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }

                    break;

                case "tipofact":
                    campos.append(",o.F_Tipo AS tipo_requerimiento");
                    tablas.append(" INNER JOIN tb_obserfact AS o ON o.F_IdFact = f.F_IdFact");
                    agrupamientos.append(",o.F_Tipo");
                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    break;

                case "costo":
                    campos.append(",Sum(f.F_Monto) AS costo");
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

                case "ListProyect":
                    busqueda = "AND f.F_Proyecto IN (%s) AND u.F_Proyecto NOT IN (5)";
                    break;

                case "LisTipUni":
                    tablas.append(" INNER JOIN tb_uniatn AS ut ON f.F_ClaCli = ut.F_ClaCli");
                    busqueda = "AND ut.F_Tipo IN (%s) ";
                    break;
            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura AS f %s  WHERE f.F_FecEnt BETWEEN '%s' AND '%s' %s AND f.F_StsFact='A' GROUP BY f.F_ClaCli %s ORDER BY NULL";

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
                    campos.append("j.F_DesJurIS AS Jurisdicion");
                    camposExternos.append("total.Jurisdicion");

                    tablas.append(" INNER JOIN tb_juriis AS j ON u.F_ClaJur = j.F_ClaJurIS");
                    agrupamientos.append("j.F_ClaJurIS");
                    agrupamientosExternos.append("total.Jurisdicion");

                    break;

                case "municipio":

                    if (campos.length() != 0) {
                        separator = ",";
                    }

                    tablas.append(" INNER JOIN tb_muniis AS m ON u.F_ClaMun = m.F_ClaMunIS");
                    campos.append(separator);
                    campos.append("m.F_DesMunIS AS Municipio");
                    agrupamientos.append(separator);
                    agrupamientos.append("m.F_ClaMunIS");

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
                    campos.append(",f.F_ClaPro AS clave, md.F_DesPro AS Descripcion_Clave");
                    camposExternos.append(",total.clave,total.Descripcion_Clave");
                    agrupamientos.append(",f.F_ClaPro");
                    agrupamientosExternos.append(",total.clave");
                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    break;

                case "doct":
                    campos.append(",f.F_FecEnt AS FecEnt,f.F_ClaDoc AS folio,IFNULL(ob.F_Tipo, '') AS tipo");
                    camposExternos.append(",total.FecEnt,total.folio");
                    agrupamientos.append(",f.F_FecEnt,f.F_ClaDoc");
                    agrupamientosExternos.append(",total.FecEnt,total.folio");
                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
                    }
                    break;

                case "lote":
                    campos.append(",l.F_FecCad AS caducidad, l.F_ClaLot AS lote");
                    camposExternos.append(",total.caducidad, total.lote");
                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    agrupamientos.append(",l.F_FolLot");
                    agrupamientosExternos.append(",total.lote, total.caducidad");
                    if (ban == 0) {
                        ban = 1;
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }
                    break;

                case "origen":
                    campos.append(",l.F_Origen AS origen");
                    camposExternos.append(",total.origen");
                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    if (ban == 0) {
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }

                    agrupamientos.append(",l.F_Origen");
                    agrupamientosExternos.append(",total.origen");

                    break;

                case "tipofact":
                    campos.append(",o.F_Tipo AS tipo_requerimiento");
                    camposExternos.append(",total.tipo_requerimiento");
                    tablas.append(" INNER JOIN tb_obserfact AS o ON o.F_IdFact = f.F_IdFact");
                    agrupamientos.append(",o.F_Tipo");
                    agrupamientosExternos.append(",total.tipo_requerimiento");
                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    camposExternos.append(",Sum(total.requerido) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    camposExternos.append(",Sum(total.surtido) AS surtido");
                    break;

                case "costo":
                    campos.append(",Sum(f.F_Monto) AS costo");
                    camposExternos.append(",Sum(total.costo) AS costo");
                    break;

                case "proyecto":
                    ban = 1;
                    campos.append(",p.F_DesProy AS Proyecto");
                    camposExternos.append(",total.Proyecto");
                    join = " INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id";
                    agrupamientos.append(",l.F_Proyecto");
                    agrupamientosExternos.append(",total.Proyecto");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id");
                    }
                    break;

                case "cause":
                    ban = 1;
                    campos.append(",tp.F_Cause AS Tipo_Cause");
                    camposExternos.append(",total.Tipo_Cause");
                    join = " INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id";
                    agrupamientos.append(",tp.F_Cause");
                    agrupamientosExternos.append(",total.Tipo_Cause");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id");
                    }
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

                case "LisTipUni":
                    tablas.append(" INNER JOIN tb_uniatn AS ut ON f.F_ClaCli = ut.F_ClaCli");
                    busqueda = "AND ut.F_Tipo IN (%s) ";
                    break;

                case "ListProyect":
                    busqueda = "AND f.F_Proyecto IN (%s) ";
                    break;
            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura AS f %s WHERE f.F_FecEnt BETWEEN '%s' AND '%s' %s AND f.F_StsFact='A' GROUP BY %s ORDER BY NULL";

        query = String.format(query, campos.toString(), tablas.toString(),
                request.getParameter("fecha_ini"), request.getParameter("fecha_fin"),
                parametrosBusqueda.toString(), agrupamientos.toString());

        query = String.format(query, "INNER JOIN tb_uniatn AS u ON f.F_ClaCli = u.F_ClaCli");

        String query_principal = "SELECT %s FROM (%s) AS total GROUP BY %s ORDER BY NULL;";
        query_principal = String.format(query_principal, camposExternos.toString(), query, agrupamientosExternos.toString());
        //String query_principal = String.format(camposExternos.toString(), query, agrupamientosExternos.toString());

        query_principal = query_principal.replace("%s", "");

        return query_principal;
    }

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

                    campos.append("f.F_ClaPro AS clave, md.F_DesPro AS Descripcion_Clave");
                    agrupamientos.append("f.F_ClaPro");
                    tablas.append(" INNER JOIN tb_medica AS md ON f.F_ClaPro = md.F_ClaPro");
                    break;

                case "doct":
                    campos.append(separador);
                    agrupamientos.append(separador);

                    campos.append("f.F_FecEnt AS FecEnt,f.F_ClaDoc AS folio,IFNULL(ob.F_Tipo, '') AS tipo");
                    agrupamientos.append("f.F_FecEnt,f.F_ClaDoc");
                    join = " LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ";
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" LEFT JOIN tb_obserfact ob ON f.F_ClaDoc = ob.F_IdFact AND f.F_Proyecto = ob.F_Proyecto ");
                    }
                    break;

                case "proyecto":
                    ban = 1;
                    campos.append(separador);
                    agrupamientos.append(separador);
                    campos.append("p.F_DesProy AS Proyecto");
                    join = "INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id";
                    agrupamientos.append("l.F_Proyecto");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_lote l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id");
                    }
                    break;

                case "cause":
                    ban = 1;
                    campos.append(separador);
                    agrupamientos.append(separador);
                    campos.append("tp.F_Cause AS Tipo_Cause");
                    join = " INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id";
                    agrupamientos.append("tp.F_Cause");
                    if (tablas.indexOf(join) == -1) {
                        tablas.append(" INNER JOIN tb_tipocause tp ON f.F_Cause=tp.F_Id");
                    }
                    break;

                case "lote":
                    campos.append(separador);
                    agrupamientos.append(separador);
                    campos.append("l.F_FecCad AS caducidad, l.F_ClaLot AS lote");
                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    if (ban == 0) {
                        ban = 1;
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }
                    agrupamientos.append("l.F_FolLot");
                    break;

                case "origen":
                    campos.append(separador);
                    agrupamientos.append(separador);
                    campos.append("l.F_Origen AS origen");
                    join = " INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica";
                    if (ban == 0) {
                        if (tablas.indexOf(join) == -1) {
                            tablas.append(" INNER JOIN tb_lote AS l ON f.F_Lote = l.F_FolLot AND f.F_ClaPro = l.F_ClaPro AND f.F_Ubicacion = l.F_Ubica");
                        }
                    }
                    agrupamientos.append("l.F_Origen");

                    break;

                case "tipofact":
                    campos.append(separador);
                    agrupamientos.append(separador);

                    campos.append("o.F_Tipo AS tipo_requerimiento");
                    tablas.append(" INNER JOIN tb_obserfact AS o ON o.F_IdFact = f.F_IdFact");
                    agrupamientos.append("o.F_Tipo");
                    break;

                case "unireq":
                    campos.append(",Sum(f.F_CantReq) AS requerido");
                    break;

                case "unisur":
                    campos.append(",Sum(f.F_CantSur) AS surtido");
                    break;

                case "costo":
                    campos.append(",Sum(f.F_Monto) AS costo");
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

                case "ListProyect":
                    busqueda = "AND f.F_Proyecto IN (%s) AND u.F_Proyecto NOT IN (5)";
                    break;

                case "LisTipUni":
                    tablas.append(" INNER JOIN tb_uniatn AS ut ON f.F_ClaCli = ut.F_ClaCli");
                    busqueda = "AND ut.F_Tipo IN (%s) ";
                    break;
            }

            busqueda = String.format(busqueda, valor);
            parametrosBusqueda.append(busqueda);

        }

        String query = "SELECT %s FROM tb_factura AS f %s WHERE f.F_FecEnt BETWEEN '%s' AND '%s' %s AND f.F_StsFact='A' GROUP BY %s ORDER BY NULL";

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
