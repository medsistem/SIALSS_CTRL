package ReportesPuntos;

import ReportesPuntos.cache.CacheQuery;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Exportar consulta de reporteador de estadistica
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
@WebServlet(name = "ExporteReporteador", urlPatterns = {"/entregas/exportar"})
public class ExporteReporteador extends HttpServlet {

    private final int MAXIMUM_ROWS_EXCEL = 1048575;

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
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        HttpSession sesion = request.getSession();
        System.out.println(sesion.getId());
        try (OutputStream out = response.getOutputStream()) {

            ArrayList<String> columnas = (ArrayList<String>) sesion.getAttribute("ultima_columnas");
            String fin = (String) sesion.getAttribute("ultima_fin");
            fin = fin.replace("-", "");
            fin += "_";

            Random randomGenerator = new Random();
            String fileName = "reporte_entregas_";
            fileName += fin;
            fileName += String.valueOf(columnas.size());
            fileName += "_";
            fileName += String.valueOf(randomGenerator.nextInt(100));
            int sheets = 1;

            try (SXSSFWorkbook wb = new SXSSFWorkbook(100)) {
                SXSSFSheet sheet = wb.createSheet("Entregas_" + sheets);
                sheet = setColumnNames(columnas, sheet);

                String path = sesion.getServletContext().getRealPath("/queries/");
                CacheQuery cache = new CacheQuery(path);
                ArrayList<ArrayList<String>> resultadoJSON;

                int size = CacheQuery.CHUCK_SIZE;
                int parte = 0;
                int filaCont = 1;
                int col = 0;
                boolean maximo = false;
                while (!maximo) {
                    resultadoJSON = cache.readArray(sesion.getAttribute("IdUsu").toString(),
                            sesion.getId(), "entregas", parte);

                    SXSSFRow rowData;
                    for (ArrayList<String> fila : resultadoJSON) {
                        col = 0;
                        if (filaCont == MAXIMUM_ROWS_EXCEL) {
                            maximo = true;
                            break;
                        }
                        rowData = sheet.createRow(filaCont);

                        for (Object valor : fila) {
                            SXSSFCell cell = rowData.createCell(col);
                            if (valor != null) {
                                cell.setCellValue(valor.toString());
                            }
                            col++;
                        }

                        filaCont++;
                    }

                    //Signfica que es la última parte.
                    if (resultadoJSON.size() < size) {
                        break;
                    }

                    parte++;

                }

                String disHeader = "Attachment;Filename=\"" + fileName + ".xlsx\"";
                response.setHeader("Content-Disposition", disHeader);

                wb.write(out);

                out.flush();
            }
        }
    }

    public SXSSFSheet setColumnNames(ArrayList<String> columns, SXSSFSheet sheet) {
        sheet.setAutobreaks(true);

        SXSSFRow rowHeadInv = sheet.createRow(0);
        int col = 0;
        for (String columna : columns) {
            rowHeadInv.createCell(col).setCellValue(columna);
            col++;
        }

        return sheet;
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
