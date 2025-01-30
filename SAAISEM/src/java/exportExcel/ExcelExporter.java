package exportExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import static java.sql.Types.BIGINT;
import static java.sql.Types.DATE;
import static java.sql.Types.DECIMAL;
import static java.sql.Types.INTEGER;
import static java.sql.Types.SMALLINT;
import static java.sql.Types.TINYINT;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Permite exportar archivos de excel de manera generica apartir de consultas.
 *
 * @author Sebastian
 */
public class ExcelExporter {

    public static int DEFAULT_INITIAL_ROW_HEADER = 0;

    //informacion en settings
    public static final String SETTINGS_FIELD = "settings";
    public static final String FILENAME_FIELD = "filename";
    public static final String MAX_TO_WRITE_FIELD = "maxlength";
    public static final String DEFAULT_WIDTH_FIELD = "width";
    //informacion encabezado
    public static final String NAME_REPORT_FIELD = "name";
    public static final String MEDICAL_UNIT_FIELD = "unit";
    public static final String INITIAL_ROW_HEADER_FIELD = "initHeader";

    //informacion que retorna
    public static final String OUTPUTS_FIELD = "outputs";
    public static final String FINAL_ROW_FIELD = "final";
    public static final String SHEET_NAME_FIELD = "name";
    public static final String WORKBOOK_FIELD = "wb";
    public static final String COLUMNS_FIELD = "columns";

    public static final String RESPONSE_HTTP_FIELD = "response";
    public static final String DIRECTORY_SAVE = "folder";

    private ExcelExporter() {
    }

    /**
     * Permite preparar el archivo de excel para su posterior exportación.
     *
     * @param data
     * @return
     */
    public static JSONObject preparedReport(JSONObject data,
            List<SheetInformation> sheets)
            throws SQLException {

        JSONObject settings = data.getJSONObject(SETTINGS_FIELD);

        SXSSFWorkbook wb = new SXSSFWorkbook(settings.getInt(MAX_TO_WRITE_FIELD));
        SXSSFSheet sheet;
        SXSSFRow rowHeaders;
        SXSSFRow row;
        SXSSFCell cellRow, cellHeader;

        int currentRow, counter, columns, columnType;
        ResultSetMetaData meta;

        JSONObject result = new JSONObject();
        JSONArray outputs = new JSONArray();
        JSONArray numericColumns = new JSONArray();
        JSONObject output;

        CellStyle headerStyle = getDefaultHeaderStyle(wb);
        CellStyle rowStyle = getDefaultRowStyle(wb);

        for (SheetInformation sheetInfo : sheets) {

            //configuracion sheet excel
            sheet = wb.createSheet(sheetInfo.getSheetName());
            sheet.setDefaultColumnWidth(settings.getInt(DEFAULT_WIDTH_FIELD));
            sheet.setAutobreaks(true);

            currentRow = sheetInfo.getInitialRow();
            rowHeaders = sheet.createRow(currentRow);
            counter = 0;

            //se asignan los encabezados
            for (String header : sheetInfo.getHeaders()) {
                cellHeader = rowHeaders.createCell(counter);
                cellHeader.setCellStyle(headerStyle);
                cellHeader.setCellValue(header);
                counter++;
            }
            currentRow++;

            try (ResultSet rs = sheetInfo.getPreparedStatement().executeQuery()) {
                meta = rs.getMetaData();
                columns = meta.getColumnCount();

                while (rs.next()) {
                    row = sheet.createRow(currentRow);
                    counter = 0;
                    for (int i = 1; i <= columns; i++) {
                        cellRow = row.createCell(counter);
                        cellRow.setCellStyle(rowStyle);
                        columnType = meta.getColumnType(i);

                        switch (columnType) {
                            case DECIMAL:
                            case BIGINT:
                            case INTEGER:
                            case SMALLINT:
                            case TINYINT:
                                cellRow.setCellValue(rs.getDouble(i));
                                numericColumns.put(counter);
                                break;
                            case DATE:
                                cellRow.setCellValue(rs.getDate(i));
                                break;
                            default:
                                cellRow.setCellValue(rs.getString(i));
                                cellRow.setCellType(CellType.STRING);
                                break;
                        }

                        counter++;
                    }
                    currentRow++;
                }
            }

            addAutoFilters(sheet, 0, columns, sheetInfo.getInitialRow(), currentRow);
            addSubtotal(sheet, numericColumns, 0, columns, sheetInfo.getInitialRow(), currentRow);

            output = new JSONObject();
            output.put(FINAL_ROW_FIELD, currentRow);
            output.put(SHEET_NAME_FIELD, sheetInfo.getSheetName());
            output.put(COLUMNS_FIELD, columns);
            outputs.put(output);
        }

        //Agrega el encabezado del reporte
        addDefualtHeader(wb.getSheet(sheets.get(0).getSheetName()), settings);

        result.put(SETTINGS_FIELD, settings);
        result.put(OUTPUTS_FIELD, outputs);
        result.put(WORKBOOK_FIELD, wb);

        return result;
    }

    /**
     * Permite adjuntar un archivo de excel en la respuesta HTTP indicada.
     *
     * @param data
     */
    public static void exportInResponseHTTP(JSONObject data) throws IOException {
        HttpServletResponse response = (HttpServletResponse) data.get(RESPONSE_HTTP_FIELD);
        SXSSFWorkbook wb = (SXSSFWorkbook) data.get(WORKBOOK_FIELD);
        JSONObject settings = data.getJSONObject(SETTINGS_FIELD);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires:", "0"); //Evita que se use cache.
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s",
                settings.getString(FILENAME_FIELD)));

        wb.write(response.getOutputStream());
        wb.close();
        wb.dispose();
    }

    public static JSONObject getDefaultSettings(String filename, int maxLength, int width,
            String name, String medicalUnit) {
        JSONObject result = new JSONObject();
        result.put(FILENAME_FIELD, filename);
        result.put(MAX_TO_WRITE_FIELD, maxLength);
        result.put(DEFAULT_WIDTH_FIELD, width);
        result.put(NAME_REPORT_FIELD, name);
        result.put(MEDICAL_UNIT_FIELD, medicalUnit);

        return result;
    }

    public static JSONObject getDefaultSettings() {
        return getDefaultSettings("filename_report.xlsx", 500, 20, "lodimed_report", "all");
    }

    public static JSONObject getDefaultSettings(String filename, String name, String medicalUnit) {
        JSONObject result = getDefaultSettings();
        result.put(FILENAME_FIELD, filename);
        result.put(NAME_REPORT_FIELD, name);
        result.put(MEDICAL_UNIT_FIELD, medicalUnit);

        return result;
    }

    public static CellStyle getDefaultHeaderStyle(SXSSFWorkbook wb) {
        CellStyle headerStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;
    }

    public static CellStyle getDefaultRowStyle(SXSSFWorkbook wb) {
        CellStyle rowStyle = wb.createCellStyle();
        rowStyle.setAlignment(HorizontalAlignment.CENTER);
        rowStyle.setBorderRight(BorderStyle.THIN);
        rowStyle.setRightBorderColor(HSSFColor.ROYAL_BLUE.index);
        rowStyle.setBorderLeft(BorderStyle.THIN);
        rowStyle.setLeftBorderColor(HSSFColor.ROYAL_BLUE.index);
        rowStyle.setBorderTop(BorderStyle.THIN);
        rowStyle.setTopBorderColor(HSSFColor.ROYAL_BLUE.index);
        rowStyle.setBorderBottom(BorderStyle.THIN);
        rowStyle.setBottomBorderColor(HSSFColor.ROYAL_BLUE.index);
        return rowStyle;
    }

    protected static void addAutoFilters(SXSSFSheet sheet,
            int initCol, int endCol, int initRow, int endRow) {

        sheet.setAutoFilter(new CellRangeAddress(initRow, (endRow - 1), initCol, (endCol - 1)));
        sheet.createFreezePane(0, initRow + 1);
    }

    protected static void addSubtotal(SXSSFSheet sheet, JSONArray numericColumns,
            int initCol, int endCol, int initRow, int endRow) {

        if (numericColumns.isEmpty()) {
            return;
        }

        int firstNumericColumn = numericColumns.getInt(0);
        int subtotalRow = endRow + 1;
        SXSSFRow subtotal = null;

        if (firstNumericColumn > initCol) {
            subtotal = sheet.createRow(subtotalRow);

            firstNumericColumn--;
            SXSSFCell legendCell = subtotal.createCell(firstNumericColumn);

            legendCell.setCellStyle(getDefaultHeaderStyle(sheet.getWorkbook()));
            legendCell.setCellValue("Totales");
        }

        SXSSFCell cell;
        CellRangeAddress range;
        int numericColumn;
        int initRowRange = initRow + 1;
        int endRowRange = endRow - 1;

        String formula = "=SUBTOTAL(9,%s)";
        if (subtotal == null) {
            subtotal = sheet.createRow(subtotalRow);
        }

        for (Object aux : numericColumns) {
            numericColumn = Integer.valueOf(aux.toString());

            cell = subtotal.createCell(numericColumn);
            cell.setCellType(CellType.FORMULA);

            range = new CellRangeAddress(initRowRange, endRowRange, numericColumn, numericColumn);
            cell.setCellFormula(String.format(formula, range.formatAsString()));
        }

    }

    public static void addDefualtHeader(SXSSFSheet sheet, JSONObject data) {
        int initRow = data.optInt(INITIAL_ROW_HEADER_FIELD, DEFAULT_INITIAL_ROW_HEADER);
        SXSSFRow row = sheet.createRow(initRow);

        SXSSFCell cellHeader = row.createCell(0);
        cellHeader.setCellStyle(getDefaultHeaderStyle(sheet.getWorkbook()));
        cellHeader.setCellValue("Nombre del reporte");
        SXSSFCell cellValue = row.createCell(1);
        cellValue.setCellStyle(getDefaultRowStyle(sheet.getWorkbook()));
        cellValue.setCellValue(data.getString(NAME_REPORT_FIELD));
        cellValue = row.createCell(2);
        cellValue.setCellStyle(getDefaultRowStyle(sheet.getWorkbook()));
        sheet.addMergedRegion(new CellRangeAddress(initRow, initRow, 1, 2));

        initRow++;
        row = sheet.createRow(initRow);
        cellHeader = row.createCell(0);
        cellHeader.setCellStyle(getDefaultHeaderStyle(sheet.getWorkbook()));
        cellHeader.setCellValue("Unidad médica");
        cellValue = row.createCell(1);
        cellValue.setCellStyle(getDefaultRowStyle(sheet.getWorkbook()));
        cellValue.setCellValue(data.getString(MEDICAL_UNIT_FIELD));
        cellValue = row.createCell(2);
        cellValue.setCellStyle(getDefaultRowStyle(sheet.getWorkbook()));
        sheet.addMergedRegion(new CellRangeAddress(initRow, initRow, 1, 2));

        initRow++;
        row = sheet.createRow(initRow);
        cellHeader = row.createCell(0);
        cellHeader.setCellStyle(getDefaultHeaderStyle(sheet.getWorkbook()));
        cellHeader.setCellValue("Fecha generación");
        cellValue = row.createCell(1);
        CellStyle rowStyle = getDefaultRowStyle(sheet.getWorkbook());
        rowStyle.setDataFormat(sheet.getWorkbook().getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy h:mm AM/PM"));
        cellValue.setCellStyle(rowStyle);
        cellValue.setCellValue(new Date());
        cellValue = row.createCell(2);
        cellValue.setCellStyle(rowStyle);
        sheet.addMergedRegion(new CellRangeAddress(initRow, initRow, 1, 2));

    }

    public static void exportInDirectory(JSONObject data) throws IOException {
       
        SXSSFWorkbook wb = (SXSSFWorkbook) data.get(WORKBOOK_FIELD);
        JSONObject settings = data.getJSONObject(SETTINGS_FIELD);

        File carpeta = new File(data.getString(DIRECTORY_SAVE));
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        String filename = data.getString(DIRECTORY_SAVE) + File.separatorChar + settings.getString(FILENAME_FIELD);

        FileOutputStream fileOut = new FileOutputStream(filename);
        wb.write(fileOut);
        fileOut.close();

        wb.close();

    }

}
