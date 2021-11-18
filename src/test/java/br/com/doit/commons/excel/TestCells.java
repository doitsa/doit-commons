package br.com.doit.commons.excel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.foundation.NSTimestamp;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCells {
    @Mock
    private Cell cell;

    @Test
    public void returnBigDecimalWhenConvertingToObjectWithTypeBigDecimal() throws Exception {
        Object[][] parameters = {
                { Cell.CELL_TYPE_BOOLEAN, BigDecimal.ZERO },
                { Cell.CELL_TYPE_NUMERIC, new BigDecimal("123.0") },
                { Cell.CELL_TYPE_STRING, new BigDecimal("321") }
        };

        verifyObjectConversion(BigDecimal.class, parameters);
    }

    @Test
    public void returnBooleanWhenConvertingToBooleanWithCustomString() throws Exception {
        Object[][] parameters = {
                { "yes", true },
                { "sim", true },
                { "verdadeiro", true },
                { "TRUE", true },
                { "no", false },
                { "nao", false },
                { "falso", false },
                { "não", false },
                { "FALSE", false }
        };

        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);

        for (Object[] parameter : parameters) {
            when(cell.getStringCellValue()).thenReturn((String) parameter[0]);

            Boolean result = Cells.toBoolean(cell);

            assertThat(parameter[0] + " deveria ser " + parameter[1], result, is(parameter[1]));
        }
    }

    @Test
    public void returnFalseBooleanWhenConvertingToObjectWithTypeBoolean() throws Exception {
        when(cell.getBooleanCellValue()).thenReturn(false);
        when(cell.getNumericCellValue()).thenReturn(0.0);
        when(cell.getStringCellValue()).thenReturn("false");

        Object[][] parameters = {
                { Cell.CELL_TYPE_BOOLEAN, false },
                { Cell.CELL_TYPE_NUMERIC, false },
                { Cell.CELL_TYPE_STRING, false }
        };

        verifyObjectConversion(Boolean.class, parameters);
    }

    @Test
    public void returnIntegerWhenConvertingToObjectWithTypeInteger() throws Exception {
        Object[][] parameters = {
                { Cell.CELL_TYPE_BOOLEAN, 0 },
                { Cell.CELL_TYPE_NUMERIC, 123 },
                { Cell.CELL_TYPE_STRING, 321 }
        };

        verifyObjectConversion(Integer.class, parameters);
    }

    @Test
    public void returnNSTimestampWhenConvertingToObjectWithTypeNSTimestamp() throws Exception {
        Date date = new Date();
        when(cell.getDateCellValue()).thenReturn(date);

        NSTimestamp result = Cells.toObject(cell, NSTimestamp.class);

        assertThat(result, is(new NSTimestamp(date)));
    }

    @Test
    public void returnNullWhenConvertingToObjectAndCellIsBlank() throws Exception {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);

        Object result = Cells.toObject(cell);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNullWhenConvertingToObjectAndCellIsNull() throws Exception {
        Object result = Cells.toObject(null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNullWhenConvertingToObjectWithTypeAndCellIsBlank() throws Exception {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);

        Class<?>[] types = new Class<?>[] {
                String.class,
                Integer.class,
                BigDecimal.class,
                NSTimestamp.class,
                LocalDate.class,
                Boolean.class
        };

        for (Class<?> type : types) {
            Object result = Cells.toObject(cell, type);

            assertThat(result, nullValue());
        }
    }

    @Test
    public void returnStringWhenConvertingToObjectWithTypeString() throws Exception {
        Object[][] parameters = {
                { Cell.CELL_TYPE_BOOLEAN, "false" },
                { Cell.CELL_TYPE_NUMERIC, "123" },
                { Cell.CELL_TYPE_STRING, "321" }
        };

        verifyObjectConversion(String.class, parameters);
    }

    @Test
    public void returnTrueBooleanWhenConvertingToObjectWithTypeBoolean() throws Exception {
        when(cell.getBooleanCellValue()).thenReturn(true);
        when(cell.getNumericCellValue()).thenReturn(1.0);
        when(cell.getStringCellValue()).thenReturn("true");

        Object[][] parameters = {
                { Cell.CELL_TYPE_BOOLEAN, true },
                { Cell.CELL_TYPE_NUMERIC, true },
                { Cell.CELL_TYPE_STRING, true }
        };

        verifyObjectConversion(Boolean.class, parameters);
    }

    @Before
    public void setup() {
        when(cell.getBooleanCellValue()).thenReturn(false);
        when(cell.getNumericCellValue()).thenReturn(123.0);
        when(cell.getStringCellValue()).thenReturn("321");
    }

    @Test
    public void trimStringWhenConvertingToObject() throws Exception {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(cell.getStringCellValue()).thenReturn("   xxx    ");

        String result = Cells.toObject(cell);

        assertThat(result, is("xxx"));
    }

    @Test
    public void trimStringWhenConvertingToNumericAndCellContainsBlankSpaces() throws Exception {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(cell.getStringCellValue()).thenReturn("   10    ");

        Integer result = Cells.toInteger(cell);

        assertThat(result, is(10));
    }

    @Test
    public void returnLocalDateWhenConvertingToObjectWithTypeLocalDate() throws Exception {
        Date date = new Date();
        when(cell.getDateCellValue()).thenReturn(date);

        LocalDate result = Cells.toObject(cell, LocalDate.class);

        assertThat(result, is(LocalDate.now()));
    }

    private <T> void verifyObjectConversion(Class<T> type, Object[][] parameters) {
        for (Object[] parameter : parameters) {
            when(cell.getCellType()).thenReturn((int) parameter[0]);

            T result = Cells.toObject(cell, type);

            assertThat(result, is(parameter[1]));
        }
    }
}
