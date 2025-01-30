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
import java.text.SimpleDateFormat;
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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author CEDIS TOLUCA3
 */
@WebServlet(name = "ExcelExistenciaProyecto", urlPatterns = {"/ExcelExistenciaProyecto"})
public class ExcelExistenciaProyecto extends HttpServlet {

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
        String Tipo = request.getParameter("Tipo");
        String Consulta = request.getParameter("Consulta");
         String Nombre = "";
         int tipusu = 0;
         
        ConectionDB con = new ConectionDB();
        
        PrintWriter pw = null;
        HttpSession sesion = request.getSession();       
        ResultSet rsetDatos = null;
        PreparedStatement psDatos = null;
        
        try {
        
            con.conectar();
            pw = response.getWriter();
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyy");
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
            String name = "Inventario-Proyecto-%s.xlsx";

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

            //Formato de la celdas<
            XSSFCellStyle style = wb.createCellStyle();
            XSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor((new XSSFColor(new java.awt.Color(0, 0, 153))));
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            XSSFCellStyle styleBord = wb.createCellStyle();
            styleBord.setBorderTop(BorderStyle.MEDIUM);
            styleBord.setBorderBottom(BorderStyle.MEDIUM);
            styleBord.setBorderLeft(BorderStyle.MEDIUM);
            styleBord.setBorderRight(BorderStyle.MEDIUM);

            CreationHelper crearteHelper = wb.getCreationHelper();
            CellStyle datestyle = wb.createCellStyle();
            datestyle.setDataFormat(crearteHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            datestyle.setBorderTop(BorderStyle.MEDIUM);
            datestyle.setBorderBottom(BorderStyle.MEDIUM);
            datestyle.setBorderLeft(BorderStyle.MEDIUM);
            datestyle.setBorderRight(BorderStyle.MEDIUM);

            List<String> unidades = new ArrayList<>();
            int totPiezas = 0, veces = 0;
            String qry = null, auxLimit;
            ResultSet rset;

            int index = 0;

            XSSFRow rowHeadInv = sheet.createRow(index);
            //rowHeadInv.createCell((int) 0).setCellValue(String.format("Total de existencias en %d unidades de atención: %s", unidades.size(), formatter.format(totPiezas)));

            index = 2;
            rowHeadInv = sheet.createRow(index);
            
            if (Tipo.equals("Compras")) {
             tipusu = 2;
             Consulta = "6";
            }else{
                tipusu = 1;
            }
            
            System.out.println("Consulta:"+ Consulta);
            System.out.println("Proyecto:"+ Proyecto);
  
           
            if ((Proyecto.equals("8")) || (Proyecto.equals("9")) || (Proyecto.equals("10")) || (Proyecto.equals("11"))){
               
                switch (Consulta) {
                    
                    case "1":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 4).setCellValue("Cantidad");
                        for (int j = 0; j <= 4; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                    case "2":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 4).setCellValue("Lote");
                        rowHeadInv.createCell((int) 5).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 6).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 7).setCellValue("Costo");
                        rowHeadInv.createCell((int) 8).setCellValue("Monto");
                        rowHeadInv.createCell((int) 9).setCellValue("Origen");
                        rowHeadInv.createCell((int) 10).setCellValue("Proveedor");
                        for (int j = 0; j <= 10; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                    case "3":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Lote");
                        rowHeadInv.createCell((int) 4).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 5).setCellValue("Fecha movimiento");
                        rowHeadInv.createCell((int) 6).setCellValue("Fecha ingreso");
                        rowHeadInv.createCell((int) 7).setCellValue("Ubicación");
                        rowHeadInv.createCell((int) 8).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 9).setCellValue("Origen");
                        rowHeadInv.createCell((int) 10).setCellValue("No. documento");
                        rowHeadInv.createCell((int) 11).setCellValue("Orden de compra");
                        rowHeadInv.createCell((int) 12).setCellValue("Remision");
                        rowHeadInv.createCell((int) 13).setCellValue("Orden de suministro");
                        rowHeadInv.createCell((int) 14).setCellValue("Bodega");
                        for (int j = 0; j <= 14; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                        
                    case "4":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Lote");
                        rowHeadInv.createCell((int) 3).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 4).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 5).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 6).setCellValue("Cantidad");
                        break;
                    case "5":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 4).setCellValue("Lote");
                        rowHeadInv.createCell((int) 5).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 6).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 7).setCellValue("Costo");
                        rowHeadInv.createCell((int) 8).setCellValue("Monto");
                        rowHeadInv.createCell((int) 9).setCellValue("Origen");
                        break;

                    default:
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 4).setCellValue("Cantidad");
                        break;
                }

            } else {
                switch (Consulta) {
                    case "1":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Cantidad");

                        for (int j = 0; j <= 3; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;

                    case "2":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Lote");
                        rowHeadInv.createCell((int) 4).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 5).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 6).setCellValue("Origen");
                        rowHeadInv.createCell((int) 7).setCellValue("Proveedor");

                        for (int j = 0; j <= 7; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                    case "3":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Lote");
                        rowHeadInv.createCell((int) 4).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 5).setCellValue("Fecha movimiento");
                        rowHeadInv.createCell((int) 6).setCellValue("Fecha ingreso");
                        rowHeadInv.createCell((int) 7).setCellValue("Ubicación");
                        rowHeadInv.createCell((int) 8).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 9).setCellValue("Origen");
                        rowHeadInv.createCell((int) 10).setCellValue("No. documento");
                        rowHeadInv.createCell((int) 11).setCellValue("Orden de compra");
                        rowHeadInv.createCell((int) 12).setCellValue("Remisión");
                        rowHeadInv.createCell((int) 13).setCellValue("Orden de suministro");
                        rowHeadInv.createCell((int) 14).setCellValue("Bodega");

                        for (int j = 0; j <= 14; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                    case "4":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Lote");
                        rowHeadInv.createCell((int) 3).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 4).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 5).setCellValue("Presentación");
                        rowHeadInv.createCell((int) 6).setCellValue("Cantidad");

                        break;
                    case "5":
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Lote");
                        rowHeadInv.createCell((int) 4).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 5).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 6).setCellValue("Origen");
                        rowHeadInv.createCell((int) 7).setCellValue("Orden Suministro");
                        break;
                    default:
                        rowHeadInv.createCell((int) 0).setCellValue("Proyecto");
                        rowHeadInv.createCell((int) 1).setCellValue("Clave");
                        rowHeadInv.createCell((int) 2).setCellValue("Descripción");
                        rowHeadInv.createCell((int) 3).setCellValue("Lote");
                        rowHeadInv.createCell((int) 4).setCellValue("Caducidad");
                        rowHeadInv.createCell((int) 5).setCellValue("Fecha movimiento");
                        rowHeadInv.createCell((int) 6).setCellValue("Fecha ingreso");
                        rowHeadInv.createCell((int) 7).setCellValue("Ubicación");
                        rowHeadInv.createCell((int) 8).setCellValue("Cantidad");
                        rowHeadInv.createCell((int) 9).setCellValue("Origen");
                        rowHeadInv.createCell((int) 10).setCellValue("Bodega");
                        for (int j = 0; j <= 10; j++) {
                            rowHeadInv.getCell(j).setCellStyle(style);
                        }
                        break;
                }

            }
            int aux;
            PreparedStatement ps;
            ResultSet rsTemp;
            //for (int i = 0; i < veces; i++) {
            String AND = "";

            if (Tipo.equals("Compras")) {
                AND = " AND L.F_Ubica NOT IN ('CADUCADOS','MERMA','EXTRA_ORDINARIA') ";
            } else {
                AND = "";
            }
            if (Proyecto.equals("0")) {
                switch (Consulta) {
                    case "1":
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot > 0 " + AND + " GROUP BY L.F_ClaPro, L.F_Proyecto;";
                        break;
                    case "2":
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,tb_proveedor.F_NomPro,case when (`l`.`F_Ubica` like 'ST%' or `l`.`F_Ubica` like '%SANTIN%' or `l`.`F_Ubica` like 'PS%') then 'Bajio' when (`l`.`F_Ubica` like 'MD%' or `l`.`F_Ubica` like '%1N%' or `l`.`F_Ubica` like '%AERO%') then 'Bajio' when (`l`.`F_Ubica` like 'MICH%' or `l`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot > 0 " + AND + " GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;
                    case "3":
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,tb_proveedor.F_NomPro,case when (`l`.`F_Ubica` like 'ST%' or `l`.`F_Ubica` like '%SANTIN%' or `l`.`F_Ubica` like 'PS%') then 'Bajio' when (`l`.`F_Ubica` like 'MD%' or `l`.`F_Ubica` like '%1N%' or `l`.`F_Ubica` like '%AERO%') then 'Bajio' when (`l`.`F_Ubica` like 'MICH%' or `l`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, lo.fechaMov AS fechaMov, lot.fechaIng AS fechaIng  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, Max(m.F_FecMov) AS fechaMov FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov GROUP BY l.F_IdLote ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, MIN(m.F_FecMov) AS fechaIng FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov  GROUP BY l.F_IdLote) AS lot ON L.F_FolLot = lot.F_FolLot AND L.F_Ubica = lot.F_Ubica INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot > 0 " + AND + " GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";
                        break;
                    case "4":
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot > 0 " + AND + " AND L.F_Origen = 8 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;
                    case "5":
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_ExiLot > 0 " + AND + " AND L.F_Origen = 7 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;
                    default:
                        System.out.println("aa");
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica,tb_proveedor.F_NomPro,case when (`L`.`F_Ubica` like 'ST%' or `L`.`F_Ubica` like '%SANTIN%' or `L`.`F_Ubica` like 'PS%') then 'Bajio' when (`L`.`F_Ubica` like 'MD%' or `L`.`F_Ubica` like '%1N%' or `L`.`F_Ubica` like '%AERO%') then 'Bajio' when (`L`.`F_Ubica` like 'MICH%' or `L`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, lo.fechaMov AS fechaMov , lot.fechaIng AS fechaIng  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, Max(m.F_FecMov) AS fechaMov FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov GROUP BY l.F_IdLote ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, MIN(m.F_FecMov) AS fechaIng FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov  GROUP BY l.F_IdLote) AS lot ON L.F_FolLot = lot.F_FolLot AND L.F_Ubica = lot.F_Ubica INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id  INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot>0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";
                        break;
                }

            } else {
                switch (Consulta) {
                    case "1":
                        System.out.println("1");
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = '" + Proyecto + "' AND L.F_ExiLot>0 " + AND + " GROUP BY L.F_ClaPro, L.F_Proyecto;";
                        break;

                    case "2":
                        System.out.println("2");
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,tb_proveedor.F_NomPro,case when (`l`.`F_Ubica` like 'ST%' or `l`.`F_Ubica` like '%SANTIN%' or `l`.`F_Ubica` like 'PS%') then 'Bajio' when (`l`.`F_Ubica` like 'MD%' or `l`.`F_Ubica` like '%1N%' or `l`.`F_Ubica` like '%AERO%') then 'Bajio' when (`l`.`F_Ubica` like 'MICH%' or `l`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = '" + Proyecto + "' AND L.F_ExiLot>0 " + AND + " GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;

                    case "3":
                        System.out.println("consulta");
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR( M.F_DesPro, 1, 40 ) AS F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad,	SUM( L.F_ExiLot - IFNULL( A.F_Cant, 0 ) ) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND( ( SUM( L.F_ExiLot ) * M.F_Costo ), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, tb_proveedor.F_NomPro, CASE WHEN ( `L`.`F_Ubica` LIKE 'ST%' OR `L`.`F_Ubica` LIKE '%SANTIN%' OR `L`.`F_Ubica` LIKE 'PS%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MD%' OR `L`.`F_Ubica` LIKE '%1N%' OR `L`.`F_Ubica` LIKE '%AERO%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MICH%' OR `L`.`F_Ubica` LIKE 'CROSS%' ) THEN'Bajio' ELSE 'Bajio' END AS `lugar`, fm.fechaMov AS fechaMov, fi.fechaIng AS fechaIng, IF( lo.documento='null', '',lo.documento ) AS documento, IF( lo.OC='null', '',lo.OC ) AS OC, IF( lo.remision='null', '',lo.remision ) AS remision, IF( lo.OrdSuministro='null', '',lo.OrdSuministro ) ordSuministro FROM tb_lote AS L INNER JOIN tb_medica AS M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen AS O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos AS P ON L.F_Proyecto = P.F_Id LEFT JOIN ( SELECT SUM( IFNULL( F_Cant, 0 ) ) AS F_Cant, F_Status, F_IdLote FROM tb_apartado WHERE F_Status = 1 GROUP BY F_IdLote ) AS A ON L.F_IdLote = A.F_IdLote AND A.F_Status = 1 LEFT JOIN ( SELECT l.F_FolLot,l.F_Ubica, MAX( m.F_FecMov ) AS fechaMov, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov <> 51 GROUP BY l.F_IdLote) AS fm ON L.F_FolLot = fm.F_FolLot AND L.F_Ubica = fm.F_Ubica AND fm.F_ClaPro = L.F_ClaPro AND fm.F_origen = L.F_Origen LEFT JOIN (SELECT l.F_FolLot, l.F_Ubica, MIN( m.F_FecMov ) AS fechaIng, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov BETWEEN 1 AND 50 GROUP BY l.F_IdLote) AS fi ON L.F_FolLot = fi.F_FolLot AND L.F_Ubica = fi.F_Ubica AND L.F_ClaPro = fi.F_ClaPro AND L.F_Origen = fi.F_Origen LEFT JOIN (SELECT l.F_FolLot, l.F_Ubica, c.F_ClaDoc AS documento, c.F_FolRemi AS remision, c.F_OrdCom AS OC, c.F_OrdenSuministro AS OrdSuministro, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov INNER JOIN tb_compra AS c ON c.F_Lote = l.F_FolLot AND c.F_ClaDoc = m.F_DocMov GROUP BY l.F_IdLote, c.F_IdCom ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica AND lo.F_ClaPro = L.F_ClaPro AND lo.F_Origen = L.F_Origen INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = '" + Proyecto + "' AND L.F_ExiLot > 0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica, L.F_IdLote;";
                        break;
                    case "4":
                        System.out.println("4");
                        qry = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro,os as F_OrdenSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (select c.F_Lote,GROUP_CONCAT(IF(c.F_OrdenSuministro = NULL,'',c.F_OrdenSuministro)SEPARATOR' ') as os FROM tb_compra as c GROUP BY c.F_Lote) as c ON L.F_FolLot =  c.F_Lote WHERE L.F_ExiLot > 0 AND L.F_Origen = 8 and L.F_Proyecto='" + Proyecto + "' GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;
                    case "5":
                        System.out.println("5");
//                        qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = '" + Proyecto + "' AND L.F_ExiLot>0 " + AND + " AND L.F_Origen = 7 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        qry = " SELECT P.F_DesProy, L.F_ClaPro, SUBSTR(M.F_DesPro,1,40) AS F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, L.F_Ubica,os as F_OrdenSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id LEFT JOIN (select c.F_Lote,GROUP_CONCAT(IF(c.F_OrdenSuministro = NULL,'',c.F_OrdenSuministro)SEPARATOR' ') as os FROM tb_compra as c GROUP BY c.F_Lote) as c ON L.F_FolLot =  c.F_Lote WHERE L.F_ExiLot > 0 AND L.F_Origen = 7 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto;";
                        break;
                    default:
                        System.out.println("prrf");
                         qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id WHERE L.F_Proyecto = '" + Proyecto + "' AND L.F_ExiLot>0 " + AND + " GROUP BY L.F_ClaPro, L.F_Proyecto;";
                      
                       // qry = "SELECT P.F_DesProy, L.F_ClaPro, M.F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM(L.F_ExiLot) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND((SUM(L.F_ExiLot) * M.F_Costo), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica,tb_proveedor.F_NomPro,case when (`l`.`F_Ubica` like 'ST%' or `l`.`F_Ubica` like '%SANTIN%' or `l`.`F_Ubica` like 'PS%') then 'Bajio' when (`l`.`F_Ubica` like 'MD%' or `l`.`F_Ubica` like '%1N%' or `l`.`F_Ubica` like '%AERO%') then 'Bajio' when (`l`.`F_Ubica` like 'MICH%' or `l`.`F_Ubica` like 'CROSS%') then 'Bajio' else 'Bajio' end AS `lugar`, lo.fechaMov AS fechaMov, lot.fechaIng AS fechaIng  FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, Max(m.F_FecMov) AS fechaMov FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov GROUP BY l.F_IdLote ) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica LEFT JOIN ( SELECT l.F_FolLot, l.F_Ubica, MIN(m.F_FecMov) AS fechaIng FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov  GROUP BY l.F_IdLote) AS lot ON L.F_FolLot = lot.F_FolLot INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_ExiLot > 0 " + AND + " and L.F_Proyecto = '" + Proyecto + "' GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen, L.F_Proyecto, L.F_Ubica;";
                        break;
                }

            }

            aux = 0;
            System.out.println(qry);
            ps = con.getConn().prepareStatement(qry);

            rsTemp = ps.executeQuery();

            while (rsTemp.next()) {
                aux++;
                index++;
                totPiezas += rsTemp.getInt(6);

                XSSFRow row = sheet.createRow(index);
                if ((Proyecto.equals("8")) || (Proyecto.equals("9")) || (Proyecto.equals("10")) || (Proyecto.equals("11")) ) {
                    switch (Consulta) {
                        case "1":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 4).setCellValue(formatter.format(rsTemp.getInt(6)));
                            break;
                        case "2":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 5).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 6).setCellValue(formatter.format(rsTemp.getInt(6)));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(9));
                            row.createCell((int) 8).setCellValue(rsTemp.getString(10));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(13));
                            break;
                        case "3":
                                                        
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getString(15));
                            row.createCell((int) 6).setCellValue(rsTemp.getString(16));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(12));
                            row.createCell((int) 8).setCellValue(formatter.format(rsTemp.getInt(6)));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(17));
                            row.createCell((int) 11).setCellValue(rsTemp.getString(18));
                            row.createCell((int) 12).setCellValue(rsTemp.getString(19));
                            row.createCell((int) 13).setCellValue(rsTemp.getString(20));
                            row.createCell((int) 14).setCellValue(rsTemp.getString(14));
                            break;
                        case "4":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 5).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 6).setCellValue(formatter.format(rsTemp.getInt(6)));
                            break;
                        case "5":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 5).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 6).setCellValue(formatter.format(rsTemp.getInt(6)));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(9));
                            row.createCell((int) 8).setCellValue(rsTemp.getString(10));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            break;

                        default:
                            
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 4).setCellValue(formatter.format(rsTemp.getInt(6)));
                            break;
                    }

                } else if (Proyecto.equals("0")) {
                    switch (Consulta) {
                        case "1":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getInt(6));

                            for (int j = 0; j <= 3; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            break;
                        case "2":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getInt(6));
                            row.createCell((int) 6).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(13));

                            for (int j = 0; j <= 7; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            row.getCell(4).setCellStyle(datestyle);
                            break;
                        case "3":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getDate(15));
                            row.createCell((int) 6).setCellValue(rsTemp.getDate(16));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(12));
                            row.createCell((int) 8).setCellValue(rsTemp.getInt(6));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(14));

                            for (int j = 0; j <= 10; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            for(int h = 4; h <= 6; h++){
                                row.getCell(h).setCellStyle(datestyle);
                            }
                            break;
                        default:
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getDate(11));
                            row.createCell((int) 6).setCellValue(rsTemp.getDate(12));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 8).setCellValue(rsTemp.getInt(6));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(10));

                            for (int j = 0; j <= 10; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            for (int h = 4; h <= 6; h++){
                                row.getCell(h).setCellStyle(datestyle);
                            }
                            break;
                    }

                } else {
                    switch (Consulta) {
                        case "1":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getInt(6));

                            for (int j = 0; j <= 3; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            break;
                        case "2":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getInt(6));
                            row.createCell((int) 6).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(13));

                            for (int j = 0; j <= 7; j++) {
                                row.getCell(j).setCellStyle(styleBord);
                            }
                            row.getCell(4).setCellStyle(datestyle);
                            break;
                        case "3":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getDate(15));
                            row.createCell((int) 6).setCellValue(rsTemp.getDate(16));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(12));
                            row.createCell((int) 8).setCellValue(rsTemp.getInt(6));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(17));
                            row.createCell((int) 11).setCellValue(rsTemp.getString(18));
                            row.createCell((int) 12).setCellValue(rsTemp.getString(19));
                            row.createCell((int) 13).setCellValue(rsTemp.getString(20));
                            row.createCell((int) 14).setCellValue(rsTemp.getString(14));

                            
                            
                                for (int j = 0; j <= 14; j++) {
                                    row.getCell(j).setCellStyle(styleBord);
                                }
                                for (int h = 4; h <= 6; h++) {
                                 row.getCell(h).setCellStyle(datestyle);
                            }
                               

                            break;
                        case "4":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 5).setCellValue(rsTemp.getString(8));
                            row.createCell((int) 6).setCellValue(formatter.format(rsTemp.getInt(6)));
                            break;
                        case "5":
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getString(5));
                            row.createCell((int) 5).setCellValue(formatter.format(rsTemp.getInt(6)));
                            row.createCell((int) 6).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(9));
                            break;

                        default:
                            row.createCell((int) 0).setCellValue(rsTemp.getString(1));
                            row.createCell((int) 1).setCellValue(rsTemp.getString(2));
                            row.createCell((int) 2).setCellValue(rsTemp.getString(3));
                            row.createCell((int) 3).setCellValue(rsTemp.getString(4));
                            row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
                            row.createCell((int) 5).setCellValue(rsTemp.getDate(15));
                            row.createCell((int) 6).setCellValue(rsTemp.getDate(16));
                            row.createCell((int) 7).setCellValue(rsTemp.getString(12));
                            row.createCell((int) 8).setCellValue((rsTemp.getInt(6)));
                            row.createCell((int) 9).setCellValue(rsTemp.getString(7));
                            row.createCell((int) 10).setCellValue(rsTemp.getString(14));

                            for (int j = 0; j <= 10; j++) {
                                row.getCell(j).setCellStyle(styleBord);                                
                            }
                            for (int h = 4; h <= 6; h++){
                                row.getCell(h).setCellStyle(datestyle);
                            }
                            break;
                    }

                }
            }
            rsTemp.close();
            ps.close();
            //}
//            if (Proyecto.equals("2") && Consulta.equals("3")) {
//                qry = "SELECT P.F_DesProy, L.F_ClaPro, SUBSTR( M.F_DesPro, 1, 40 ) AS F_DesPro, L.F_ClaLot, L.F_FecCad AS F_FecCad, SUM( IFNULL( A.F_Cant, 0 ) ) AS F_ExiLot, O.F_DesOri, M.F_PrePro, M.F_Costo, FORMAT( ROUND( ( SUM( L.F_ExiLot ) * M.F_Costo ), 2 ), 2 ) AS Monto, P.F_Id, L.F_Ubica, tb_proveedor.F_NomPro, CASE WHEN ( `L`.`F_Ubica` LIKE 'ST%' OR `L`.`F_Ubica` LIKE '%SANTIN%' OR `L`.`F_Ubica` LIKE 'PS%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MD%' OR `L`.`F_Ubica` LIKE '%1N%' OR `L`.`F_Ubica` LIKE '%AERO%' ) THEN 'Bajio' WHEN ( `L`.`F_Ubica` LIKE 'MICH%' OR `L`.`F_Ubica` LIKE 'CROSS%' ) THEN 'Bajio' ELSE 'Bajio' END AS `lugar`, fm.fechaMov AS fechaMov, fi.fechaIng AS fechaIng, IFNULL( lo.documento, '' ) AS documento, IFNULL( lo.OC, '' ) AS OC, IFNULL( lo.remision, '' ) AS remision, IFNULL( lo.OrdSuministro, '' ) ordSuministro FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen O ON L.F_Origen = O.F_ClaOri	INNER JOIN tb_proyectos P ON L.F_Proyecto = P.F_Id INNER JOIN ( SELECT SUM( F_Cant ) AS F_Cant, F_Status, F_IdLote FROM tb_apartado GROUP BY F_IdLote, F_Status ) A ON L.F_IdLote = A.F_IdLote AND A.F_Status = 1 LEFT JOIN (SELECT l.F_FolLot,	l.F_Ubica, MAX( m.F_FecMov ) AS fechaMov, l.F_ClaPro, l.F_Origen FROM tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_Ubica = m.F_UbiMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov <> 51 GROUP BY l.F_IdLote) AS fm ON L.F_FolLot = fm.F_FolLot AND L.F_Ubica = fm.F_Ubica AND fm.F_ClaPro = L.F_ClaPro AND fm.F_origen = L.F_Origen LEFT JOIN (SELECT	l.F_FolLot, l.F_Ubica, MIN( m.F_FecMov ) AS fechaIng, l.F_ClaPro, l.F_Origen FROM	tb_lote AS l INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov AND l.F_ClaPro = m.F_ProMov WHERE m.F_ConMov BETWEEN 1 AND 50 GROUP BY l.F_IdLote) AS fi ON L.F_FolLot = fi.F_FolLot AND L.F_Ubica = fi.F_Ubica AND L.F_ClaPro = fi.F_ClaPro AND L.F_Origen = fi.F_Origen LEFT JOIN (SELECT	l.F_FolLot, l.F_Ubica, c.F_ClaDoc AS documento, c.F_FolRemi AS remision, c.F_OrdCom AS OC, c.F_OrdenSuministro AS OrdSuministro, l.F_ClaPro, l.F_Origen FROM tb_lote AS l	INNER JOIN tb_movinv AS m ON l.F_FolLot = m.F_LotMov INNER JOIN tb_compra AS c ON c.F_Lote = l.F_FolLot AND c.F_ClaDoc = m.F_DocMov GROUP BY l.F_IdLote, c.F_IdCom) AS lo ON L.F_FolLot = lo.F_FolLot AND L.F_Ubica = lo.F_Ubica AND lo.F_ClaPro = L.F_ClaPro AND lo.F_Origen = L.F_Origen INNER JOIN tb_proveedor ON tb_proveedor.F_ClaProve = L.F_ClaPrv WHERE L.F_Proyecto = 2	AND L.F_ExiLot > 0 GROUP BY L.F_ClaPro, L.F_ClaLot, L.F_FecCad,	L.F_Origen, L.F_Proyecto, L.F_Ubica, L.F_IdLote;";
//                ps = con.getConn().prepareStatement(qry);
//                rsTemp = ps.executeQuery();
//                System.out.println(qry);
//                while (rsTemp.next()) {
//                    aux++;
//                    index++;
//                    XSSFRow row = sheet.createRow(index);
//                    row.createCell((int) 0).setCellValue(rsTemp.getString(1));
//                    row.createCell((int) 1).setCellValue(rsTemp.getString(2));
//                    row.createCell((int) 2).setCellValue(rsTemp.getString(3));
//                    row.createCell((int) 3).setCellValue(rsTemp.getString(4));
//                    row.createCell((int) 4).setCellValue(rsTemp.getDate(5));
//                    row.createCell((int) 5).setCellValue(rsTemp.getDate(15));
//                    row.createCell((int) 6).setCellValue(rsTemp.getDate(16));
//                    row.createCell((int) 7).setCellValue(rsTemp.getString(12));
//                    row.createCell((int) 8).setCellValue(rsTemp.getInt(6));
//                    row.createCell((int) 9).setCellValue(rsTemp.getString(7));
//                    row.createCell((int) 10).setCellValue(rsTemp.getString(17));
//                    row.createCell((int) 11).setCellValue(rsTemp.getString(18));
//                    row.createCell((int) 12).setCellValue(rsTemp.getString(19));
//                    row.createCell((int) 13).setCellValue(rsTemp.getString(20));
//                    row.createCell((int) 14).setCellValue("APARTADO");
//                    for (int j = 0; j <= 14; j++) {
//                        row.getCell(j).setCellStyle(styleBord);
//                    }
//                     for (int h = 4; h <= 6; h++) {
//                                 row.getCell(h).setCellStyle(datestyle);
//                            }
//                    
//                }
//            }
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
            Logger.getLogger(ExcelExistenciaProyecto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                response.flushBuffer();
                pw.flush();

                pw.close();
                con.cierraConexion();
            } catch (Exception ex) {
                Logger.getLogger(ExcelExistenciaProyecto.class.getName()).log(Level.SEVERE, null, ex);
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
