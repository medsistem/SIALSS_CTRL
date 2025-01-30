/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import conn.ConectionDB;
import exportExcel.ExcelExporter;
import static exportExcel.ExcelExporter.DIRECTORY_SAVE;
import static exportExcel.ExcelExporter.RESPONSE_HTTP_FIELD;
import static exportExcel.ExcelExporter.SETTINGS_FIELD;
import exportExcel.SheetInformation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author IngMa
 */
@WebServlet(name = "ReporteASFReloaded", urlPatterns = {"/ReporteASFReloaded"})
public class ReporteASFReloaded extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReporteASFReloaded</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReporteASFReloaded at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        request.setCharacterEncoding("UTF-8");
        ConectionDB con = new ConectionDB();
        try {
            

            con.conectar();

            processGroupDetail(con.getConn(), request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.WARNING, null, ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            try {
                con.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processGroupDetail(Connection con, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException {

        String filename = "ASF.xlsx";
        String Proyecto = request.getParameter("proyecto");
        String AND = "";
        String SAVE_DIR = "ASFExcel";
        org.json.JSONObject settings = ExcelExporter.getDefaultSettings(filename, 10000, 30,
                "ASF", "AUTO SUFICIENCIA");

        int meses = Integer.parseInt(request.getParameter("meses"));
        int prioridad = Integer.parseInt(request.getParameter("prioridad"));
        String qry;

        String path = request.getServletContext().getRealPath("");
        String rutaCarpeta = path + File.separatorChar + SAVE_DIR;

        switch (Proyecto) {
            case "1":
                AND = "AND M.F_ProIsem = 1 ";
                break;
            case "2":
                AND = "AND M.F_ProMichoacan = 1";
                break;
            case "3":
                AND = "AND M.F_ProIssemym = 1 ";
                break;
            default:
                break;
        }

        //WHERE asf.NIM = ? ORDER BY asf.CantReq DESC
        if (Proyecto.equals("2")) {
            //qry = "SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE,(IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF ( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, IFNULL(SSM.F_Cantidad, 0) AS CantidadSSM, ROUND( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(SSM.F_Cantidad, 0), 2 ) AS NIMSSM, tm.F_DesMed AS `tipo_medicamento` FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, ( SUM(L.F_ExiLot) - IFNULL(CADU.CADUCID, 0) ) AS F_ExiLot FROM tb_lote L LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS CADUCID FROM tb_lote WHERE F_FecCad <= CURDATE() AND F_ExiLot > 0 AND F_Proyecto = ? GROUP BY F_ClaPro ) AS CADU ON L.F_ClaPro = CADU.F_ClaPro WHERE L.F_Proyecto = ? GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%d/%m/%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%d/%m/%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%d/%m/%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave LEFT JOIN ( SELECT F_Clave, F_ClaveSS, F_Cantidad FROM tb_ssm ) AS SSM ON M.F_ClaPro = SSM.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' "+AND+" GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC;";
            qry = "SELECT asf.prioridad AS prioridad,asf.F_ClaPro AS clave, asf.F_DesPro AS descripcion, asf.F_OriNim AS oriNim, asf.PROVEE AS proveedor, asf.tipo_medicamento AS tipoMedicamentoTexto, asf.ExiLot AS existencia, asf.CantidadSSM AS cantidadSSM, asf.NIMSSM AS nimssm, asf.CantReq AS cantidadRequerida, asf.CantSur AS cantidadSurtida, asf.CPDREQ AS cpdreq, asf.CPDSUR AS cpdsur, asf.INV AS inv, asf.MESDIA AS mesdia, asf.F_CostoNim AS costoNim, asf.MONTO AS monto, asf.FECHACOMPRA AS fechaCompra, asf.FECHAORI AS fechaOrdenDeCompra, asf.CantOri AS cantidadOrdenDeCompra, asf.F_CantMax AS cantidadMaxima, asf.CanCom AS cantidadCompra, asf.PORABASTO AS porabasto, asf.F_Grupo AS grupo, asf.F_Comentario AS comentario FROM( SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE, (IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF ( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, IFNULL(SSM.F_Cantidad, 0) AS CantidadSSM, ROUND( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(SSM.F_Cantidad, 0), 2 ) AS NIMSSM, tm.F_DesMed AS `tipo_medicamento`, M.F_Prioridad AS prioridad FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, SUM(L.F_ExiLot) AS F_ExiLot FROM tb_lote L WHERE L.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ('INGRESOS_V', 'MERMA', 'CUARENTENA', 'FALTANTE_ORIGEN','CADUCADOS') GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%%d/%%m/%%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%%d/%%m/%%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%%d/%%m/%%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave LEFT JOIN ( SELECT F_Clave, F_ClaveSS, F_Cantidad FROM tb_ssm ) AS SSM ON M.F_ClaPro = SSM.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' " + AND + " GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC ) AS asf %s";

        } else {
            qry = "SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE, IFNULL(LOTE.F_ExiLot, 0) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, tm.F_DesMed AS `tipo_medicamento` FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, ( SUM(L.F_ExiLot) - IFNULL(CADU.CADUCID, 0) ) AS F_ExiLot FROM tb_lote L LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS CADUCID FROM tb_lote WHERE F_FecCad <= CURDATE() AND F_ExiLot > 0 AND F_Proyecto = ? GROUP BY F_ClaPro ) AS CADU ON L.F_ClaPro = CADU.F_ClaPro WHERE L.F_Proyecto = ? GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%d/%m/%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%d/%m/%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%d/%m/%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' " + AND + " GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC;";
        }

        if (meses == -1 && prioridad == -1) {
            qry = String.format(qry, "GROUP BY asf.F_ClaPro HAVING cantidadRequerida >0");
        } else {
            qry = String.format(qry, "WHERE asf.NIM BETWEEN ? AND ? AND asf.prioridad = ? GROUP BY asf.F_ClaPro HAVING cantidadRequerida >0 ORDER BY asf.CantReq DESC");
        }

        try (PreparedStatement ps = con.prepareStatement(qry)) {
            ps.setString(1, Proyecto);
            ps.setString(2, Proyecto);
            ps.setString(3, Proyecto);
            ps.setString(4, Proyecto);
            if (meses != -1) {
                Double nim = (double) meses;
                ps.setDouble(5, nim);
                ps.setDouble(6, (nim + .99));
                ps.setInt(7, prioridad);
            }

            List<SheetInformation> sheets = new ArrayList<>();
            SheetInformation sheet = new SheetInformation();
            sheet.setSheetName("Detalle");
            sheet.setPreparedStatement(ps);
            sheet.setHeaders(new String[]{"Prioridad", "Clave", "Descripcion",
                "Origen", "Proveedor", "Tipo", "Inventario", "CPM MORELIA", "ASF CPM", "Requerido",
                "Surtido", "CPD Requerido", "CPD Surtido", "Sobre Inventario", "NIM", "C.U.", "Importe", "Última recepción",
                "Fecha ORI", "Cantidad Ori", "Cantidad Max.", "Cantidad Reg.", "% Abasto", "Grupo terapeútico", "Comentarios"});
            sheet.setInitialRow(5);
            sheets.add(sheet);

            org.json.JSONObject data = new org.json.JSONObject();
            data.put(SETTINGS_FIELD, settings);

            //Se construye el excel.
            data = ExcelExporter.preparedReport(data, sheets);
            data.put(RESPONSE_HTTP_FIELD, response);
            data.put(DIRECTORY_SAVE, rutaCarpeta);
            //se añade encabezado
            //Exporta el excel
            ExcelExporter.exportInResponseHTTP(data);
            //ExcelExporter.exportInDirectory(data);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        JSONArray json;
        JSONArray jsona = new JSONArray();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        DecimalFormat dosDecimas = new DecimalFormat("###,###,###.##");
        ConectionDB con = new ConectionDB();
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        custom.setGroupingSeparator(',');
        dosDecimas.setDecimalFormatSymbols(custom);
        formatter.setDecimalFormatSymbols(custom);

        String accion, Proyecto, AND = "";

        Proyecto = request.getParameter("proyecto");
        accion = request.getParameter("accion");
        switch (accion) {
            case "tablaASF":

                try (PrintWriter out = response.getWriter()) {
                    int meses = Integer.parseInt(request.getParameter("meses"));
                    int prioridad = Integer.parseInt(request.getParameter("prioridad"));
                    int noLinea = 1;
                    String qry;

                    switch (Proyecto) {
                        case "1":
                            AND = "AND M.F_ProIsem = 1 ";
                            break;
                        case "2":
                            AND = "AND M.F_ProMichoacan = 1";
                            break;
                        case "3":
                            AND = "AND M.F_ProIssemym = 1 ";
                            break;
                        default:
                            break;
                    }

                    //WHERE asf.NIM = ? ORDER BY asf.CantReq DESC
                    if (Proyecto.equals("2")) {
                        //qry = "SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE,(IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF ( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, IFNULL(SSM.F_Cantidad, 0) AS CantidadSSM, ROUND( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(SSM.F_Cantidad, 0), 2 ) AS NIMSSM, tm.F_DesMed AS `tipo_medicamento` FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, ( SUM(L.F_ExiLot) - IFNULL(CADU.CADUCID, 0) ) AS F_ExiLot FROM tb_lote L LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS CADUCID FROM tb_lote WHERE F_FecCad <= CURDATE() AND F_ExiLot > 0 AND F_Proyecto = ? GROUP BY F_ClaPro ) AS CADU ON L.F_ClaPro = CADU.F_ClaPro WHERE L.F_Proyecto = ? GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%d/%m/%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%d/%m/%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%d/%m/%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave LEFT JOIN ( SELECT F_Clave, F_ClaveSS, F_Cantidad FROM tb_ssm ) AS SSM ON M.F_ClaPro = SSM.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' "+AND+" GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC;";
                        qry = "SELECT asf.F_Catalogo AS catalogo, asf.F_ClaPro AS clave, asf.F_DesPro AS descripcion, asf.F_OriNim AS oriNim, asf.PROVEE AS proveedor, asf.ExiLot AS existencia, asf.CantReq AS cantidadRequerida, asf.CantSur AS cantidadSurtida, asf.CPDREQ AS cpdreq, asf.CPDSUR AS cpdsur, asf.INV AS inv, asf.F_CostoNim AS costoNim, asf.MONTO AS monto, asf.FECHACOMPRA AS fechaCompra, asf.FECHA AS fechaFactura, asf.F_Grupo AS grupo, asf.F_Comentario AS comentario, asf.F_TipMed AS tipoMedicamento, asf.FECHAORI AS fechaOrdenDeCompra, asf.CantOri AS cantidadOrdenDeCompra, asf.NIM AS nim, asf.F_CantMax AS cantidadMaxima, asf.CanCom AS cantidadCompra, asf.PORABASTO AS porabasto, asf.MESDIA AS mesdia, asf.CantidadSSM AS cantidadSSM, asf.NIMSSM AS nimssm, asf.tipo_medicamento AS tipoMedicamentoTexto, asf.prioridad AS prioridad FROM( SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE, (IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF ( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, IFNULL(SSM.F_Cantidad, 0) AS CantidadSSM, ROUND( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(SSM.F_Cantidad, 0), 2 ) AS NIMSSM, tm.F_DesMed AS `tipo_medicamento`, M.F_Prioridad AS prioridad FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, SUM(L.F_ExiLot) AS F_ExiLot FROM tb_lote L WHERE L.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_Ubica AND l.F_Ubica NOT IN ('INGRESOS_V', 'MERMA', 'CUARENTENA', 'FALTANTE_ORIGEN','CADUCADOS') GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%%d/%%m/%%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%%d/%%m/%%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%%d/%%m/%%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave LEFT JOIN ( SELECT F_Clave, F_ClaveSS, F_Cantidad FROM tb_ssm ) AS SSM ON M.F_ClaPro = SSM.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' " + AND + " GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC ) AS asf %s";

                    } else {
                        qry = "SELECT M.F_Catalogo, M.F_ClaPro, SUBSTRING(M.F_DesPro, 1, 60) AS F_DesPro, M.F_OriNim, IFNULL(PROV.F_NomPro, '') AS PROVEE, IFNULL(LOTE.F_ExiLot, 0) AS ExiLot, IFNULL(FACT.CantReq, 0) AS CantReq, IFNULL(FACT.CantSur, 0) AS CantSur, IFNULL(FACT.CPDREQ, 0) AS CPDREQ, IFNULL(FACT.CPDSUR, 0) AS CPDSUR, IF( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO) < 0, 0, IFNULL( ( IFNULL(LOTE.F_ExiLot, 0) - FACT.CONSUMO ), 0 ) ) AS INV, M.F_CostoNim, IFNULL(LOTE.F_ExiLot, 0) * M.F_CostoNim AS MONTO, IFNULL(FECHACOMP.F_FecCom, 'NA') AS FECHACOMPRA, IFNULL(FACT.FECHA, 'NA') AS FECHA, M.F_Grupo, M.F_Comentario, M.F_TipMed, IFNULL(ORI.FECHAORI, 'NA') AS FECHAORI, IFNULL(ORI.CantOri, 0) AS CantOri, IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) AS NIM, IFNULL(M.F_CantMax, 0) AS F_CantMax, IFNULL(FECHACOMP.F_CanCom, 0) AS CanCom, ROUND( IFNULL( ( ( IFNULL(FECHACOMP.F_CanCom, 0) * 100 ) / IFNULL(M.F_CantMax, 0) ), 0 ), 2 ) AS PORABASTO, CONCAT( REPLACE ( FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) + ROUND( ( ( ( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) - FLOOR( IFNULL( ROUND( ( IFNULL(LOTE.F_ExiLot, 0) / IFNULL(FACT.CPDREQDia, 0) ) / 30, 2 ), IFNULL(LOTE.F_ExiLot, 0) ) ) ) / 100 ) * 30 ), 2 ), '.', ' Mes(es) ' ), ' Día(s)' ) AS MESDIA, tm.F_DesMed AS `tipo_medicamento` FROM tb_medica M LEFT JOIN ( SELECT F_ClaPro, GROUP_CONCAT(tp.F_NomPro) AS F_NomPro FROM tb_prodprov tpp INNER JOIN tb_proveedor tp ON tpp.F_ClaProve = tp.F_ClaProve WHERE tp.F_ClaProve NOT IN (169, 900000000) AND F_Proyecto = ? GROUP BY tpp.F_ClaPro ) AS PROV ON M.F_ClaPro = PROV.F_ClaPro LEFT JOIN ( SELECT L.F_ClaPro, ( SUM(L.F_ExiLot) - IFNULL(CADU.CADUCID, 0) ) AS F_ExiLot FROM tb_lote L LEFT JOIN ( SELECT F_ClaPro, SUM(F_ExiLot) AS CADUCID FROM tb_lote WHERE F_FecCad <= CURDATE() AND F_ExiLot > 0 AND F_Proyecto = ? GROUP BY F_ClaPro ) AS CADU ON L.F_ClaPro = CADU.F_ClaPro WHERE L.F_Proyecto = ? GROUP BY L.F_ClaPro ) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro LEFT JOIN ( SELECT F.F_ClaPro, SUM(F.F_CantReq) AS CantReq, SUM(F.F_CantSur) AS CantSur, ROUND( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQ, ROUND( (SUM(F.F_CantSur)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDSUR, ROUND( ( ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) * DATEDIFF('2018-12-31', CURDATE()) ) ) AS CONSUMO, IF ( (SUM(F.F_CantSur)) > 0, DATE_FORMAT(MAX(F.F_FecEnt), '%d/%m/%Y'), 'NA' ) AS FECHA, ( (SUM(F.F_CantReq)) / ( DATEDIFF(CURDATE(), '2018-01-01') + 1 ) ) AS CPDREQDia FROM tb_factura F WHERE F_StsFact = 'A' AND F_FecEnt >= '2018-01-01' AND F_Proyecto = ? GROUP BY F.F_ClaPro ) AS FACT ON M.F_ClaPro = FACT.F_ClaPro LEFT JOIN ( SELECT F_ClaPro, DATE_FORMAT(MAX(F_FecApl), '%d/%m/%Y') AS F_FecCom, SUM(F_CanCom) AS F_CanCom FROM tb_compra WHERE F_FecApl >= '2018-01-01' AND F_Proyecto = ? GROUP BY F_ClaPro ) AS FECHACOMP ON M.F_ClaPro = FECHACOMP.F_ClaPro LEFT JOIN ( SELECT F_Clave, GROUP_CONCAT( DATE_FORMAT(F_FecSur, '%d/%m/%Y') ) AS FECHAORI, GROUP_CONCAT(F_Cant) AS CantOri FROM tb_pedido_sialss WHERE F_StsPed = 1 AND F_Recibido = 0 GROUP BY F_Clave ) AS ORI ON M.F_ClaPro = ORI.F_Clave INNER JOIN tb_tipmed AS tm ON M.F_TipMed = tm.F_TipMed WHERE M.F_StsPro = 'A' " + AND + " GROUP BY M.F_ClaPro ORDER BY M.F_ClaPro ASC;";
                    }

                    if (meses == -1 && prioridad == -1) {
                        qry = String.format(qry, "GROUP BY asf.F_ClaPro HAVING cantidadRequerida >0");
                    } else {
                        qry = String.format(qry, "WHERE asf.NIM BETWEEN ? AND ? AND asf.prioridad = ?  GROUP BY asf.F_ClaPro HAVING cantidadRequerida >0 ORDER BY asf.CantReq DESC");
                    }

                    PreparedStatement ps = con.getConn().prepareStatement(qry);
                    int Cero = 0, Existencia = 0, ExistenciaT = 0, ComentInha = 0, Coment = 0, ContarSO = 0, Med = 0, Mat = 0, Tipo = 0, Agotar = 0;
                    String Comentario;

                    ps.setString(1, Proyecto);
                    ps.setString(2, Proyecto);
                    ps.setString(3, Proyecto);
                    ps.setString(4, Proyecto);
                    if (meses != -1) {
                        Double nim = (double) meses;
                        ps.setDouble(5, nim);
                        ps.setDouble(6, (nim + .99));
                        ps.setInt(7, prioridad);
                    }
                    try (ResultSet rset = ps.executeQuery()) {
                        while (rset.next()) {

                            ExistenciaT = rset.getInt("existencia");
                            Comentario = rset.getString("comentario");
                            Tipo = rset.getInt("tipoMedicamento");
                            Existencia = Existencia + ExistenciaT;

                            if (Comentario.equals("CLAVE INHABILITADO")) {
                                if (ExistenciaT > 0) {
                                    ComentInha++;
                                }
                            } else if (!(Comentario.equals("HABILITADO HASTA AGOTAR EXISTENCIAS"))) {
                                if (ExistenciaT == 0) {
                                    Cero++;
                                }
                                Coment++;

                                if (Tipo == 2504) {
                                    Med++;
                                } else {
                                    Mat++;
                                }
                            }

                            if ((Comentario.equals("HABILITADO HASTA AGOTAR EXISTENCIAS")) && (ExistenciaT > 0)) {
                                Agotar++;
                            }

                            json = new JSONArray();
                            json.add(noLinea);
                            json.add(rset.getString("prioridad"));
                            json.add(rset.getString("clave"));
                            json.add(rset.getString("descripcion"));
                            json.add(rset.getString("oriNim"));
                            json.add(rset.getString("proveedor"));
                            json.add(rset.getString("tipoMedicamentoTexto"));
                            json.add(formatter.format(rset.getInt("existencia")));
                            json.add(formatter.format(rset.getInt("cantidadSSM")));
                            json.add((rset.getString("nimssm")));
                            json.add(dosDecimas.format(rset.getInt("cantidadRequerida")));
                            json.add(dosDecimas.format(rset.getInt("cantidadSurtida")));
                            json.add(dosDecimas.format(rset.getInt("cpdreq")));
                            json.add(dosDecimas.format(rset.getDouble("cpdsur")));
                            json.add(formatter.format(rset.getDouble("inv")));
                            json.add(rset.getString("mesdia"));
                            json.add(formatter.format(rset.getInt("costoNim")));
                            json.add(dosDecimas.format(rset.getInt("monto")));
                            json.add(rset.getString("fechaCompra"));
                            json.add(rset.getString("fechaOrdenDeCompra"));
                            json.add(rset.getString("cantidadOrdenDeCompra"));
                            json.add(formatter.format(rset.getInt("cantidadMaxima")));
                            json.add(formatter.format(rset.getInt("cantidadCompra")));
                            json.add(rset.getString("porabasto"));
                            json.add(rset.getString("grupo"));
                            json.add(rset.getString("comentario"));

                            jsona.add(json);

                            noLinea++;
                        }
                    }

                    out.println(jsona);

                } catch (SQLException ex) {
                    Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "totales":

                try (PrintWriter out = response.getWriter()) {
                    String qry;

                    qry = "SELECT COUNT(F_ClaPro) AS clavesAutorizadas,(SELECT COUNT(F_ClaPro) FROM tb_medica WHERE F_TipMed='2504' AND F_ProMichoacan=1) AS medicamento, (SELECT COUNT(F_ClaPro) FROM tb_medica WHERE F_TipMed='2505' AND F_ProMichoacan=1) AS materialCuracion, (SELECT COUNT(F_OriNim) FROM tb_medica M WHERE F_OriNim LIKE '%SO%' AND F_StsPro='A' AND F_ProMichoacan=1) AS soluciones, (SELECT sum(CASE WHEN existencias.ExiLot = 0 THEN 1 ELSE 0 END) AS clavesEnCero FROM( SELECT M.F_ClaPro,(IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot FROM tb_medica M LEFT JOIN ( SELECT L.F_ClaPro, SUM(L.F_ExiLot) AS F_ExiLot FROM tb_lote L WHERE L.F_Proyecto = 2 AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ('INGRESOS_V', 'MERMA', 'CUARENTENA', 'FALTANTE_ORIGEN','CADUCADOS') GROUP BY L.F_ClaPro) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro WHERE M.F_StsPro = 'A' AND M.F_ProMichoacan = 1 GROUP BY M.F_ClaPro) AS existencias ) AS clavesEnCero, (SELECT sum(existencias.ExiLot) AS totalInventario FROM( SELECT M.F_ClaPro,(IFNULL(LOTE.F_ExiLot, 0)) AS ExiLot FROM tb_medica M LEFT JOIN ( SELECT L.F_ClaPro, SUM(L.F_ExiLot) AS F_ExiLot FROM tb_lote L WHERE L.F_Proyecto = 2 AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ('INGRESOS_V', 'MERMA', 'CUARENTENA', 'FALTANTE_ORIGEN','CADUCADOS')  GROUP BY L.F_ClaPro) AS LOTE ON M.F_ClaPro = LOTE.F_ClaPro WHERE M.F_StsPro = 'A' AND M.F_ProMichoacan = 1 GROUP BY M.F_ClaPro ) AS existencias ) AS existenciasTotales FROM tb_medica WHERE F_ProMichoacan = 1";
                    JSONObject totales = new JSONObject();
                    PreparedStatement ps = con.getConn().prepareStatement(qry);

                    try (ResultSet rset = ps.executeQuery()) {
                        while (rset.next()) {
                            totales.put("clavesAutorizadas", formatter.format(rset.getInt("clavesAutorizadas")));
                            totales.put("medicamento", formatter.format(rset.getInt("medicamento")));
                            totales.put("materialCuracion", formatter.format(rset.getInt("materialCuracion")));
                            totales.put("soluciones", formatter.format(rset.getInt("soluciones")));
                            totales.put("clavesEnCero", formatter.format(rset.getInt("clavesEnCero")));
                            totales.put("existenciasTotales", formatter.format(rset.getInt("existenciasTotales")));
                        }
                    }

                    out.println(totales);

                } catch (SQLException ex) {
                    Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        con.cierraConexion();
                    } catch (SQLException ex) {
                        Logger.getLogger(ReporteASFReloaded.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }
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
