package br.com.doit.commons.excel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.com.doit.commons.l10n.Messages;

/**
 * Importador de dados de uma planilha do Excel permite extrair as linhas de uma planilha como objetos de tipos comuns
 * como String, Integer, BigDecimal e etc.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class ExcelImporter {
    private static final Messages messages = Messages.getMessages("br/com/doit/commons/excel/messages");

    /**
     * Essa classe é capaz de formatar mensagens de erro adicionando informações sobre a linha e a coluna em que o erro
     * ocorreu. A coluna é convertida para para a letra correspondente em uma planilha do Excel.
     */
    protected static class ExcelErrorMessageFormatter {
        private static final String MESSAGE_WITH_LINE_AND_COLUMN = "MESSAGE_WITH_LINE_AND_COLUMN";

        private static String columnNameForIndex(int index) {
            if (index / 26 == 0) {
                char column = (char) ('A' + index);

                return String.valueOf(column);
            }

            return columnNameForIndex((index / 26) - 1) + columnNameForIndex((index % 26));
        }

        protected static String format(String message, Cell cell) {
            int row = cell.getRowIndex() + 1;
            String column = columnNameForIndex(cell.getColumnIndex());

            return messages.format(MESSAGE_WITH_LINE_AND_COLUMN, message, row, column);
        }
    }

    private static final class ClearDataMaker {
        @Override
        public String toString() {
            return "-";
        }
    }

    private static final String UNRECOGNISABLE_COLUMN = "UNRECOGNISABLE_COLUMN";

    public static final Object CLEAR_DATA_MARKER = new ClearDataMaker();

    private static boolean isEmptyRow(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (Cells.toString(cell) != null) {
                return false;
            }
        }

        return true;
    }

    private final Map<String, Class<?>> config;

    /**
     * Cria uma nova instância do importador de dados do Excel usando a configuração passada por parâmetro.
     *
     * @param config
     *            A configuração das colunas e tipos suportados por esse importador
     */
    public ExcelImporter(Map<String, Class<?>> config) {
        this.config = config;
    }

    private Object extractCellValue(Cell cell, String columnName) {
        if (cell.getCellType() == Cell.CELL_TYPE_STRING && "-".equals(cell.getStringCellValue())) {
            return CLEAR_DATA_MARKER;
        }

        if (Cells.isEmpty(cell)) {
            return null;
        }

        try {
            return Cells.toObject(cell, config.get(columnName));
        } catch (Exception exception) {
            String message = ExcelErrorMessageFormatter.format(exception.getMessage(), cell);

            throw new ExcelImporterException(message, exception);
        }
    }

    /**
     * Extrai as linhas de uma planilha do Excel como uma lista de <code>Map</code>s de acordo com as configurações
     * passadas por parâmetro.
     *
     * @param spreadsheetUrl
     *            A URL da planilha.
     * @return Retorna uma lista de <code>Map</code>s contendo os dados das linhas da planilha.
     */
    public List<Map<String, Object>> extractRows(URL spreadsheetUrl) {
        try (InputStream input = spreadsheetUrl.openStream()) {
            return extractRows(input);
        } catch (IOException exception) {
            throw new ExcelImporterException(exception);
        }
    }

    /**
     * Extrai as linhas de uma planilha do Excel como uma lista de <code>Map</code>s de acordo com as configurações
     * passadas por parâmetro.
     *
     * @param input
     *            O input stream de onde a planilha será lida
     * @return Retorna uma lista de <code>Map</code>s contendo os dados das linhas da planilha.
     */
    public List<Map<String, Object>> extractRows(InputStream input) {
        try {
            Workbook workbook = WorkbookFactory.create(input);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.rowIterator();

            List<String> columnNames = new ArrayList<>();

            if (rows.hasNext()) {
                Row firstRow = rows.next();

                Iterator<Cell> cells = firstRow.cellIterator();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    String columnName = cell.getStringCellValue();

                    if (!config.containsKey(columnName.toUpperCase())) {
                        throw new ExcelImporterException(messages.format(UNRECOGNISABLE_COLUMN, columnName));
                    }

                    columnNames.add(columnName.toUpperCase());
                }
            }

            List<Map<String, Object>> convertedRows = new ArrayList<>();

            while (rows.hasNext()) {
                Row row = rows.next();

                if (isEmptyRow(row)) {
                    continue;
                }

                Map<String, Object> convertedRow = new HashMap<>();

                for (int i = 0; i < row.getPhysicalNumberOfCells() && i < columnNames.size(); i++) {
                    Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);

                    String columnName = columnNames.get(i);

                    Object value = extractCellValue(cell, columnName);

                    convertedRow.put(columnName, value);
                }

                convertedRows.add(convertedRow);
            }

            return convertedRows;
        } catch (InvalidFormatException | IOException exception) {
            throw new ExcelImporterException(exception);
        }
    }
}
