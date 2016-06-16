package br.com.doit.commons.excel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.webobjects.foundation.NSTimestamp;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestExcelImporter {
    private ExcelImporter importer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void extractClearDataMarkerWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/clear_data_syntax.xlsx");

        Map<String, Object> result = importer.extractRows(url).get(0);

        assertThat(result.containsKey("NAME"), is(true));
        assertThat(result.containsKey("AMOUNT"), is(true));
        assertThat(result.containsKey("DATE"), is(true));
        assertThat(result.containsKey("CURRENCY"), is(true));
        assertThat(result.containsKey("BOOLEAN"), is(true));

        assertThat(result.get("NAME"), is(ExcelImporter.CLEAR_DATA_MARKER));
        assertThat(result.get("AMOUNT"), is(ExcelImporter.CLEAR_DATA_MARKER));
        assertThat(result.get("DATE"), is(ExcelImporter.CLEAR_DATA_MARKER));
        assertThat(result.get("CURRENCY"), is(ExcelImporter.CLEAR_DATA_MARKER));
        assertThat(result.get("BOOLEAN"), is(ExcelImporter.CLEAR_DATA_MARKER));
    }

    @Test
    public void extractDataUsingTheCorrectDataTypeWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/multi_types.xlsx");

        Map<String, Object> result = importer.extractRows(url).get(0);

        assertThat(result.containsKey("NAME"), is(true));
        assertThat(result.containsKey("AMOUNT"), is(true));
        assertThat(result.containsKey("DATE"), is(true));
        assertThat(result.containsKey("CURRENCY"), is(true));
        assertThat(result.containsKey("BOOLEAN"), is(true));

        NSTimestamp expectedDate = new NSTimestamp(new LocalDate(2014, 10, 10).toDateTimeAtStartOfDay().toDate());

        assertThat(result.get("NAME"), is((Object) "John Doe"));
        assertThat(result.get("AMOUNT"), is((Object) 123));
        assertThat(result.get("DATE"), is((Object) expectedDate));
        assertThat(result.get("CURRENCY"), is((Object) new BigDecimal("1500.5")));
        assertThat(result.get("BOOLEAN"), is((Object) true));
    }

    @Test
    public void extractOneLineOfDataWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/one_line.xlsx");

        Map<String, Object> result = importer.extractRows(url).get(0);

        assertThat(result.containsKey("NAME"), is(true));
        assertThat(result.containsKey("AMOUNT"), is(true));

        assertThat(result.get("NAME"), is((Object) "John Doe"));
        assertThat(result.get("AMOUNT"), is((Object) 123));
    }

    @Test
    public void emptyDateAndCurrencyCell() throws Exception {
        URL url = getClass().getResource("/importer/empty_date_and_currency_cell.xlsx");

        Map<String, Object> result = importer.extractRows(url).get(0);

        assertThat(result.containsKey("DATE"), is(true));
        assertThat(result.containsKey("AMOUNT"), is(true));
        assertThat(result.containsKey("CURRENCY"), is(true));

        assertThat(result.get("DATE"), nullValue());
        assertThat(result.get("AMOUNT"), nullValue());
        assertThat(result.get("CURRENCY"), nullValue());
    }

    @Test
    public void extractOneLineFromInputStreamWhenProcessingSpreadsheet() throws Exception {
        InputStream input = getClass().getResourceAsStream("/importer/one_line.xlsx");

        List<Map<String, Object>> result = importer.extractRows(input);

        assertThat(result.size(), is(1));
    }

    @Test
    public void extractOneLineOfDataWithBlankColumnWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/one_line_with_blank_column.xlsx");

        Map<String, Object> result = importer.extractRows(url).get(0);

        assertThat(result.containsKey("NAME"), is(true));
        assertThat(result.containsKey("AMOUNT"), is(true));
        assertThat(result.containsKey("DATE"), is(true));
        assertThat(result.containsKey("CURRENCY"), is(true));

        assertThat(result.get("NAME"), is((Object) "John Doe"));
        assertThat(result.get("AMOUNT"), nullValue());
        assertThat(result.get("DATE"), nullValue());
        assertThat(result.get("CURRENCY"), is((Object) new BigDecimal("1500.5")));
    }

    @Test
    public void extractTwoLinesOfDataWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/two_lines.xlsx");

        List<Map<String, Object>> results = importer.extractRows(url);

        assertThat(results.get(0).get("NAME"), is((Object) "John Doe"));
        assertThat(results.get(0).get("AMOUNT"), is((Object) 123));
        assertThat(results.get(1).get("NAME"), is((Object) "Fulano de Tal"));
        assertThat(results.get(1).get("AMOUNT"), is((Object) 321));
    }

    @Test
    public void ignoreEmptyLineWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/empty_line.xlsx");

        List<Map<String, Object>> results = importer.extractRows(url);

        assertThat(results.size(), is(1));
    }

    @Test
    public void ignoreUnidentifiedColumnsWhenProcessingSpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/one_line_with_unidentified_column.xlsx");

        List<Map<String, Object>> results = importer.extractRows(url);

        assertThat(results.get(0).keySet().size(), is(2));
    }

    @Test
    public void returnEmptyListWhenProcessingEmptySpreadsheet() throws Exception {
        URL url = getClass().getResource("/importer/empty.xlsx");

        List<Map<String, Object>> results = importer.extractRows(url);

        assertThat(results.size(), is(0));
    }

    @Before
    public void setup() {
        Map<String, Class<?>> config = new HashMap<>();

        config.put("NAME", String.class);
        config.put("AMOUNT", Integer.class);
        config.put("DATE", NSTimestamp.class);
        config.put("CURRENCY", BigDecimal.class);
        config.put("BOOLEAN", Boolean.class);

        importer = new ExcelImporter(config);
    }

    @Test
    public void includeCellPositingWhenThrowingExceptionForInvalidValue() throws Exception {
        URL url = getClass().getResource("/importer/invalid_value.xlsx");

        thrown.expect(ExcelImporterException.class);
        thrown.expectMessage(is("Unsupported cell type (at line 2, column B)."));

        importer.extractRows(url);
    }

    @Test
    public void throwExceptionWhenProcessingSpreadsheetWithInvalidColumn() throws Exception {
        URL url = getClass().getResource("/importer/invalid_column.xlsx");

        thrown.expect(ExcelImporterException.class);
        thrown.expectMessage(is("Unknown column named INVALID."));

        importer.extractRows(url);
    }
}
