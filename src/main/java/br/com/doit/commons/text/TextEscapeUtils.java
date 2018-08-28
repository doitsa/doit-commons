package br.com.doit.commons.text;

import static org.apache.commons.lang.StringUtils.replace;

/**
 * A classe <code>TextEscapeUtils</code> contém métodos para trocar certos símbolos por outros.
 *
 * @author <a href="mailto:luizalfredocb@gmail.com.br">Luiz</a>
 */
public class TextEscapeUtils {

    /**
     * Troca quebra de linhas de uma {@code String} de System.lineSeparator() para a tag <br/>.
     * Dessa forma, lugares que exibem o endereço interpretando HTML podem exibir as quebras de linha.
     *
     * @param string A {@code String} que terá as quebras de linha substituídas.
     * @return Uma String com a tag <br/> ao invés de System.lineSeparator().
     */
    public static String escapeHtmlLineBreaks(String string) {
        return replace(string, "\n", "<br>");
    }

}
