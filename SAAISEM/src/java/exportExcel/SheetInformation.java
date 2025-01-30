package exportExcel;

import java.sql.PreparedStatement;

/**
 * Contiene la información necesaria para generar una hoja apartir de una
 * consulta.
 *
 * @author Sebastian
 */
public class SheetInformation {

    public static int DEFAULT_INITIAL_ROW = 5;
    public static int WITHOUT_INITIAL_ROW = -1;

    private String sheetName;
    private int initialRow;
    private String[] headers;

    private PreparedStatement preparedStatement;

    public SheetInformation() {
        this.initialRow = WITHOUT_INITIAL_ROW;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getInitialRow() {
        if (initialRow == WITHOUT_INITIAL_ROW) {
            this.initialRow = DEFAULT_INITIAL_ROW;
        }

        return initialRow;
    }

    public void setInitialRow(int initialRow) {
        this.initialRow = initialRow;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
}
