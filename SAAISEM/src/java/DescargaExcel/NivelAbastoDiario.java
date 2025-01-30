/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DescargaExcel;

import conn.ConectionDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Anibal MEDALFA
 */
@WebServlet(name = "NivelAbastoDiario", urlPatterns = {"/NivelAbastoDiario"})
public class NivelAbastoDiario extends HttpServlet {

    private static final double LIMIT = 100000;

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
        request.setCharacterEncoding("UTF-8");
        String fecha_ini = request.getParameter("fecha_ini");
        String fecha_fin = request.getParameter("fecha_fin");
        ConectionDB con = new ConectionDB();
        PrintWriter pw = null;
        HttpSession sesion = request.getSession();
        String Nombre = "";
        try {
            fecha_ini = "2018-10-13";
            fecha_fin = "2018-10-13";
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
            pw = response.getWriter();

            String path = sesion.getServletContext().getRealPath("");
            String name = "Nivel_Abasto_Diario-%s.xlsx";

            if (fecha_ini != null) {
                name = String.format(name, Nombre.concat("-%s"));
            }
            name = String.format(name, Nombre);

            String filename = path + name;
            // Pestaña 1

            String sheetName = "Caratula";
            XSSFWorkbook wb = new XSSFWorkbook();

            XSSFSheet sheet = wb.createSheet(sheetName);
            int width = 20;
            sheet.setAutobreaks(true);
            sheet.setDefaultColumnWidth(width);

            List<String> unidades = new ArrayList<>();
            int totPiezas = 0, veces = 0;
            String qry, auxLimit;
            ResultSet rset;

            int index = 0;

            XSSFRow rowHeadInv = sheet.createRow(index);
            //rowHeadInv.createCell((int) 0).setCellValue(String.format("Total de existencias en %d unidades de atención: %s", unidades.size(), formatter.format(totPiezas)));

            index = 2;

            rowHeadInv = sheet.createRow(index);
            rowHeadInv.createCell((int) 0).setCellValue("Nivel");
            rowHeadInv.createCell((int) 1).setCellValue("No. Unidades");
            rowHeadInv.createCell((int) 2).setCellValue("Requerido");
            rowHeadInv.createCell((int) 3).setCellValue("Surtido");
            rowHeadInv.createCell((int) 4).setCellValue("No Susrtido");
            rowHeadInv.createCell((int) 5).setCellValue("Fill Ride");

            int aux;
            PreparedStatement ps;
            ResultSet rsTemp;
            int CantReq = 0, CantSur = 0, Unidad = 0, Diferencia = 0;

            qry = "SELECT U.F_Tipo, CONT.CONTAR, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO, ROUND((( SUM(F.F_CantSur) / (SUM(F.F_CantReq))) * 100 ), 2 ) AS Porcentaje FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN ( SELECT U.F_Tipo, COUNT(DISTINCT TIPO.F_ClaCli) AS CONTAR FROM tb_uniatn U INNER JOIN ( SELECT U.F_Tipo, U.F_ClaCli FROM tb_factura F LEFT JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_ClaCli ) AS TIPO ON U.F_Tipo = TIPO.F_Tipo GROUP BY U.F_Tipo ) AS CONT ON U.F_Tipo = CONT.F_Tipo WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_Tipo;;";

            aux = 0;

            ps = con.getConn().prepareStatement(qry);
            rsTemp = ps.executeQuery();

            while (rsTemp.next()) {
                aux++;
                index++;
                totPiezas += rsTemp.getInt(6);

                Unidad += rsTemp.getInt(2);
                CantReq += rsTemp.getInt(3);
                CantSur += rsTemp.getInt(4);
                Diferencia += rsTemp.getInt(5);

                XSSFRow row = sheet.createRow(index);
                row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                row.createCell((int) 1).setCellValue(formatter.format(rsTemp.getInt(2)));
                row.createCell((int) 2).setCellValue(formatter.format(rsTemp.getInt(3)));
                row.createCell((int) 3).setCellValue(formatter.format(rsTemp.getInt(4)));
                row.createCell((int) 4).setCellValue(formatter.format(rsTemp.getInt(5)));
                row.createCell((int) 5).setCellValue(rsTemp.getString(6));
            }
            XSSFRow row = sheet.createRow(index + 1);
            row.createCell((int) 0).setCellValue("Total");
            row.createCell((int) 1).setCellValue(formatter.format(Unidad));
            row.createCell((int) 2).setCellValue(formatter.format(CantReq));
            row.createCell((int) 3).setCellValue(formatter.format(CantSur));
            row.createCell((int) 4).setCellValue(formatter.format(Diferencia));
            row.createCell((int) 5).setCellValue(formatterDecimal.format(((float) CantSur / CantReq) * 100));
            rsTemp.close();
            ps.close();
            //}

            index = 0;

            rowHeadInv = sheet.getRow(index);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // Pestaña 2
            String sheetName2 = "Detalle";
            XSSFWorkbook wb2 = new XSSFWorkbook();

            XSSFSheet sheet2 = wb2.createSheet(sheetName2);
            int width2 = 20;
            sheet2.setAutobreaks(true);
            sheet2.setDefaultColumnWidth(width);

            String qry2, auxLimit2;

            int index2 = 0;

            XSSFRow rowHeadInv2 = sheet.createRow(index2);

            index2 = 2;

            rowHeadInv2 = sheet2.createRow(index);
            rowHeadInv2.createCell((int) 0).setCellValue("Nivel");
            rowHeadInv2.createCell((int) 1).setCellValue("No. Unidades");
            rowHeadInv2.createCell((int) 2).setCellValue("Requerido");
            rowHeadInv2.createCell((int) 3).setCellValue("Surtido");
            rowHeadInv2.createCell((int) 4).setCellValue("No Susrtido");
            rowHeadInv2.createCell((int) 5).setCellValue("Fill Ride");

            int aux2;
            PreparedStatement ps2;
            ResultSet rsTemp2;
            int CantReq2 = 0, CantSur2 = 0, Unidad2 = 0, Diferencia2 = 0;

            qry2 = "SELECT U.F_Tipo, CONT.CONTAR, SUM(F.F_CantReq) AS F_CantReq, SUM(F.F_CantSur) AS F_CantSur, SUM(F.F_CantReq) - SUM(F.F_CantSur) AS pzasNO, ROUND((( SUM(F.F_CantSur) / (SUM(F.F_CantReq))) * 100 ), 2 ) AS Porcentaje FROM tb_factura F INNER JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli INNER JOIN ( SELECT U.F_Tipo, COUNT(DISTINCT TIPO.F_ClaCli) AS CONTAR FROM tb_uniatn U INNER JOIN ( SELECT U.F_Tipo, U.F_ClaCli FROM tb_factura F LEFT JOIN tb_uniatn U ON F.F_ClaCli = U.F_ClaCli WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_ClaCli ) AS TIPO ON U.F_Tipo = TIPO.F_Tipo GROUP BY U.F_Tipo ) AS CONT ON U.F_Tipo = CONT.F_Tipo WHERE F.F_FecEnt BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' AND F.F_Proyecto = 2 AND F.F_StsFact = 'A' GROUP BY U.F_Tipo;;";

            aux2 = 0;

            ps2 = con.getConn().prepareStatement(qry2);
            rsTemp2 = ps2.executeQuery();

            while (rsTemp2.next()) {
                aux2++;
                index2++;

                Unidad2 += rsTemp2.getInt(2);
                CantReq2 += rsTemp2.getInt(3);
                CantSur2 += rsTemp2.getInt(4);
                Diferencia2 += rsTemp2.getInt(5);

                XSSFRow row2 = sheet.createRow(index2);
                row2.createCell((int) 0).setCellValue(rsTemp2.getString(1));
                row2.createCell((int) 1).setCellValue(formatter.format(rsTemp2.getInt(2)));
                row2.createCell((int) 2).setCellValue(formatter.format(rsTemp2.getInt(3)));
                row2.createCell((int) 3).setCellValue(formatter.format(rsTemp2.getInt(4)));
                row2.createCell((int) 4).setCellValue(formatter.format(rsTemp2.getInt(5)));
                row2.createCell((int) 5).setCellValue(rsTemp2.getString(6));
            }
            XSSFRow row2 = sheet.createRow(index2 + 1);
            row2.createCell((int) 0).setCellValue("Total");
            row2.createCell((int) 1).setCellValue(formatter.format(Unidad2));
            row2.createCell((int) 2).setCellValue(formatter.format(CantReq2));
            row2.createCell((int) 3).setCellValue(formatter.format(CantSur2));
            row2.createCell((int) 4).setCellValue(formatter.format(Diferencia2));
            row2.createCell((int) 5).setCellValue(formatterDecimal.format(((float) CantSur2 / CantReq2) * 100));
            rsTemp2.close();
            ps2.close();

            index2 = 0;

            rowHeadInv2 = sheet2.getRow(index2);
            sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // fin pestaña
            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                wb.write(fileOut);
            }
            wb.close();

            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                wb2.write(fileOut);
            }
            wb2.close();

            String disHeader = "Attachment;Filename=\"" + name + "\"";
            response.setHeader("Content-Disposition", disHeader);
            File desktopFile = new File(filename);
            System.out.println("Va comenzar escritura.");
            try (FileInputStream fileInputStream = new FileInputStream(desktopFile)) {
                int j;
                while ((j = fileInputStream.read()) != -1) {
                    pw.write(j);
                }
                fileInputStream.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(NivelAbastoDiario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                response.flushBuffer();
                pw.flush();

                pw.close();
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(NivelAbastoDiario.class.getName()).log(Level.SEVERE, null, ex);
            }
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
