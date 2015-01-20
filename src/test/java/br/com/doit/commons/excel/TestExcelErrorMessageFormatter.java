package br.com.doit.commons.excel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import br.com.doit.commons.excel.ExcelImporter.ExcelErrorMessageFormatter;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestExcelErrorMessageFormatter {
    @Test
    public void formatColumnOneToLetterAWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(0);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna A)"));
    }

    @Test
    public void formatColumnTwentySixToLetterZWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(25);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna Z)"));
    }

    @Test
    public void formatColumnTwentySevenToLettersAAWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(26);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna AA)"));
    }

    @Test
    public void formatColumnFiftyTwoToLettersAZWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(51);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna AZ)"));
    }

    @Test
    public void formatColumnFiftyThreeToLettersBAWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(52);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna BA)"));
    }

    @Test
    public void formatColumnSevenHundredAndTwoToLettersAAAWhenFormattingMessage() throws Exception {
        Cell cell = mock(Cell.class);

        when(cell.getRowIndex()).thenReturn(0);
        when(cell.getColumnIndex()).thenReturn(702);

        String result = ExcelErrorMessageFormatter.format("message", cell);

        assertThat(result, is("message (linha 1, coluna AAA)"));
    }
}
