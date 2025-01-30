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
 * @author Anibal GNKL
 */
@WebServlet(name = "ExcelExistenciaDisponible", urlPatterns = {"/ExcelExistenciaDisponible"})
public class ExcelExistenciaDisponible extends HttpServlet {

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
        String Proyecto = request.getParameter("Proyecto");
        String ProyectoL = request.getParameter("ProyectoL");
        ConectionDB con = new ConectionDB();
        PrintWriter pw = null;
        HttpSession sesion = request.getSession();
        String Nombre = "";
        ResultSet rsetDatos = null;
        PreparedStatement psDatos = null;
        try {
            //con = ConectionDB.instancias.get(0);
            con.conectar();
            pw = response.getWriter();
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String QueryProyecto = "SELECT F_DesProy FROM tb_proyectos WHERE F_Id = ?;";
            if (Proyecto.equals("0")) {
                Nombre = "Todos";
            } else {
                psDatos = con.getConn().prepareStatement(QueryProyecto);
                psDatos.setString(1, Proyecto);
                rsetDatos = psDatos.executeQuery();
                if (rsetDatos.next()) {
                    Nombre = rsetDatos.getString(1);
                }
            }

            String path = sesion.getServletContext().getRealPath("");
            String name = "Inventario-Disponible-%s.xlsx";

            if (Proyecto != null) {
                name = String.format(name, Nombre.concat("-%s"));
            }
            name = String.format(name, Nombre);

            String filename = path + name;
            String sheetName = "Detalles";
            XSSFWorkbook wb = new XSSFWorkbook();

            XSSFSheet sheet = wb.createSheet(sheetName);
            int width = 20;
            sheet.setAutobreaks(true);
            sheet.setDefaultColumnWidth(width);

            List<String> unidades = new ArrayList<>();
            int totPiezas = 0, veces = 0;
            String qry = null, auxLimit;
            ResultSet rset;

            int index = 0;

            XSSFRow rowHeadInv = sheet.createRow(index);
            //rowHeadInv.createCell((int) 0).setCellValue(String.format("Total de existencias en %d unidades de atención: %s", unidades.size(), formatter.format(totPiezas)));

            index = 2;

            rowHeadInv = sheet.createRow(index);

            rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
            rowHeadInv.createCell((int) 1).setCellValue("Clave");
            rowHeadInv.createCell((int) 2).setCellValue("Descripción");
            rowHeadInv.createCell((int) 3).setCellValue("Presentación");
            rowHeadInv.createCell((int) 4).setCellValue("Lote");
            rowHeadInv.createCell((int) 5).setCellValue("Caducidad");
            rowHeadInv.createCell((int) 6).setCellValue("Cantidad");

            int aux;
            PreparedStatement ps;
            ResultSet rsTemp;
            //for (int i = 0; i < veces; i++) {
            String AND = "";

            if (Proyecto.equals("0")) {
                qry = "SELECT p.F_DesProy AS F_DesProy, l.F_ClaPro AS F_ClaPro, substr(m.F_DesPro, 1, 40) AS F_DesPro, l.F_ClaLot AS F_ClaLot, date_format(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, sum(l.F_ExiLot) AS F_ExiLot, o.F_DesOri AS F_DesOri, l.F_Ubica AS F_Ubica, l.F_Proyecto AS F_Proyecto FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE l.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ( 'AT10', 'AT11', 'AT12', 'AT13', 'AT14', 'AT15', 'AT16', 'AT17', 'AT18', 'AT19', 'AT2', 'AT20', 'AT21', 'AT22', 'AT23', 'AT24', 'AT25', 'AT26', 'AT27', 'AT28', 'AT29', 'AT3', 'AT30', 'AT31', 'AT32', 'AT4', 'AT5', 'AT6', 'AT7', 'AT8', 'AT9', 'ATI', 'CADUCADOS', 'MERMA', 'EXTRA_ORDINARIA', 'NUEVA' ) GROUP BY l.F_ClaPro, l.F_Proyecto;";
                Proyecto = ProyectoL;
            } else {
                qry = "SELECT p.F_DesProy AS F_DesProy, l.F_ClaPro AS F_ClaPro, substr(m.F_DesPro, 1, 40) AS F_DesPro, l.F_ClaLot AS F_ClaLot, date_format(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, sum(l.F_ExiLot) AS F_ExiLot, o.F_DesOri AS F_DesOri, l.F_Ubica AS F_Ubica, l.F_Proyecto AS F_Proyecto, substr(m.F_PrePro, 1, 40) AS F_PrePro FROM tb_lote l INNER JOIN tb_medica m ON l.F_ClaPro = m.F_ClaPro INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id WHERE l.F_Proyecto = ? AND l.F_ExiLot > 0 AND l.F_Ubica NOT IN ( 'AT10', 'AT11', 'AT12', 'AT13', 'AT14', 'AT15', 'AT16', 'AT17', 'AT18', 'AT19', 'AT2', 'AT20', 'AT21', 'AT22', 'AT23', 'AT24', 'AT25', 'AT26', 'AT27', 'AT28', 'AT29', 'AT3', 'AT30', 'AT31', 'AT32', 'AT4', 'AT5', 'AT6', 'AT7', 'AT8', 'AT9', 'ATI', 'CADUCADOS', 'MERMA', 'EXTRA_ORDINARIA', 'NUEVA' ) GROUP BY l.F_ClaPro, l.F_ClaLot, l.F_FecCad, l.F_Proyecto HAVING F_ExiLot > 0;";
            }
            aux = 0;
            ps = con.getConn().prepareStatement(qry);
            ps.setString(1, Proyecto);
            rsTemp = ps.executeQuery();
            while (rsTemp.next()) {
                aux++;
                index++;
                totPiezas += rsTemp.getInt(6);
                XSSFRow row = sheet.createRow(index);
                row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                row.createCell((int) 3).setCellValue(rsTemp.getString(10));
                row.createCell((int) 4).setCellValue(rsTemp.getString(4));
                row.createCell((int) 5).setCellValue(rsTemp.getString(5));
                row.createCell((int) 6).setCellValue(formatter.format(rsTemp.getInt(6)));
            }
            rsTemp.close();
            ps.close();
            //}

            index = 0;

            rowHeadInv = sheet.getRow(index);
            //rowHeadInv.getCell((int) 0).setCellValue(String.format("Total de existencias en %d unidades de atención: %s", unidades.size(), formatter.format(totPiezas)));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                wb.write(fileOut);
            }
            wb.close();
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
            Logger.getLogger(ExcelExistenciaDisponible.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                response.flushBuffer();
                pw.flush();

                pw.close();
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExcelExistenciaDisponible.class.getName()).log(Level.SEVERE, null, ex);
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
