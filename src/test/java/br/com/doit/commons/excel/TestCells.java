package br.com.doit.commons.excel;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

        assertThat(result, is(date));
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
                NSTimestamp.class
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

    private <T> void verifyObjectConversion(Class<T> type, Object[][] parameters) {
        for (Object[] parameter : parameters) {
            when(cell.getCellType()).thenReturn((int) parameter[0]);

            T result = Cells.toObject(cell, type);

            assertThat(result, is(parameter[1]));
        }
    }
}
