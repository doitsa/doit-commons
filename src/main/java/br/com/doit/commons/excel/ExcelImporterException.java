package br.com.doit.commons.excel;

/**
 * Exceção que é lançada caso ocorra um erro ao extrair os dados de uma planilha do Excel.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class ExcelImporterException extends RuntimeException {
    public ExcelImporterException(String message) {
        super(message);
    }

    public ExcelImporterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelImporterException(Throwable cause) {
        super(cause);
    }
}
