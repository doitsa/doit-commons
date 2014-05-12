package br.com.doit.commons.excel;

import static org.apache.commons.lang.BooleanUtils.toIntegerObject;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import com.webobjects.foundation.NSTimestamp;

/**
 * Classe utilitária com métodos auxiliares para manipular células de uma planilha do Excel.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @see Cell
 */
public class Cells {
    /**
     * Verifica se o valor de uma célula do Excel é nulo ou vazio.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna <code>true</code> se a célula não possuir conteúdo ou <code>false</code> caso contrário.
     */
    public static boolean isNull(Cell cell) {
        return cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK;
    }

    /**
     * Converte o valor de uma célula do Excel para <code>BigDecimal</code>.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna o valor da célula como <code>BigDecimal</code>, <code>null</code> caso o valor da célula seja
     *         vazio ou lança uma exceção caso o tipo de dado na célula não possa ser convertido para
     *         <code>BigDecimal</code>.
     */
    public static BigDecimal toBigDecimal(Cell cell) {
        if (isNull(cell)) {
            return null;
        }

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            return BigDecimal.valueOf(toInteger(cell));
        case Cell.CELL_TYPE_NUMERIC:
            return BigDecimal.valueOf(cell.getNumericCellValue());
        case Cell.CELL_TYPE_STRING:
            return new BigDecimal(cell.getStringCellValue());
        default:
            throw new UnsupportedOperationException("Tipo de célula não suportado");
        }
    }

    /**
     * Converte o valor de uma célula do Excel para <code>NSTimestamp</code>.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna o valor da célula como <code>NSTimestamp</code> ou <code>null</code> caso o valor da célula seja
     *         vazio.
     */
    public static NSTimestamp toDate(Cell cell) {
        if (isNull(cell)) {
            return null;
        }

        return new NSTimestamp(cell.getDateCellValue());
    }

    /**
     * Converte o valor de uma célula do Excel para <code>Integer</code>.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna o valor da célula como <code>Integer</code>, <code>null</code> caso o valor da célula seja
     *         vazio ou lança uma exceção caso o tipo de dado na célula não possa ser convertido para
     *         <code>Integer</code>.
     */
    public static Integer toInteger(Cell cell) {
        if (isNull(cell)) {
            return null;
        }

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            return toIntegerObject(cell.getBooleanCellValue());
        case Cell.CELL_TYPE_NUMERIC:
            return Double.valueOf(cell.getNumericCellValue()).intValue();
        case Cell.CELL_TYPE_STRING:
            return Integer.parseInt(cell.getStringCellValue());
        default:
            throw new UnsupportedOperationException("Tipo de célula não suportado");
        }
    }

    /**
     * Converte o valor de uma célula do Excel e retorna um objeto de acordo com o seu tipo.
     * 
     * <pre>
     * <b>Tipo da Célula</b>    <b>Tipo do Objeto Retornado</b>
     * Campo Texto              <code>java.lang.String</code>
     * Campo Numérico           <code>java.lang.Double</code>
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna o valor da célula de acordo com o seu tipo ou lança uma exceção caso o tipo de dado na célula não
     * seja suportado.
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(Cell cell) {
        if (isNull(cell)) {
            return null;
        }

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return (T) toString(cell);
        case Cell.CELL_TYPE_NUMERIC:
            return (T) new Double(cell.getNumericCellValue());
        default:
            throw new UnsupportedOperationException("Tipo de célula não suportado");
        }
    }

    /**
     * Converte o valor de uma célula do Excel para o tipo fornecido por parâmetro.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @param type
     *            O tipo esperado para o objeto que será retornado
     * @return Retorna o valor da célula de acordo com o tipo fornecido ou lança uma exceção caso o tipo de dado na
     *         célula não seja suportado.
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(Cell cell, Class<T> type) {
        if (Integer.class.isAssignableFrom(type)) {
            return (T) toInteger(cell);
        }

        if (BigDecimal.class.isAssignableFrom(type)) {
            return (T) toBigDecimal(cell);
        }

        if (NSTimestamp.class.isAssignableFrom(type)) {
            return (T) toDate(cell);
        }

        return (T) toString(cell);
    }

    /**
     * Converte o valor de uma célula do Excel para <code>String</code>.
     * 
     * @param cell
     *            Uma célula de uma planilha do Excel.
     * @return Retorna o valor da célula como <code>String</code> ou <code>null</code> caso o valor da célula seja
     *         vazio ou não reconhecido.
     */
    public static String toString(Cell cell) {
        if (isNull(cell)) {
            return null;
        }

        String value;

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            value = ((Boolean) cell.getBooleanCellValue()).toString();
            break;
        case Cell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;

        case Cell.CELL_TYPE_NUMERIC:
            value = new BigDecimal(cell.getNumericCellValue()).toPlainString();
            break;

        default:
            value = null;
        }

        return StringUtils.trimToNull(value);
    }

    private Cells() {
        // Não deve ser instanciado
    }
}
