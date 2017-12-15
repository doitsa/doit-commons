package br.com.doit.commons.text;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * A classe <code>TextNormalizerUtils</code> contém funções auxiliares para facilitar o tratamento/normalização de
 * textos (<code>String</code>). Todas as funções auxiliares são capazes de tratar um parâmetro nulo sem gerar uma
 * exceção.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TextNormalizerUtils {
    /**
     * Remove caracteres não alfanuméricos do texto sem estragar a formatação do texto (espaços, pontos, dois-pontos e
     * hífen são mantidos).
     * 
     * @param text
     *            O texto que deverá ser tratado
     * @return Retorna o texto tratado
     */
    public static String lightStripNonAlphanumericChars(String text) {
        if (text == null) {
            return null;
        }

        return stripChars(stripChars(toAscii(text), "[^a-zA-Z0-9\\s\\.:-]"), "[\\n\\t]").trim();
    }

    private static String stripChars(String text, String pattern) {
        return text.replaceAll(pattern, "");
    }

    /**
     * Remove todos os caracteres não alfanuméricos do texto.
     *
     * @param text
     *            O texto que deverá ser tratado
     * @return Retorna o texto contendo apenas caracteres alfanuméricos
     */
    public static String stripNonAlphanumericChars(String text) {
        if (text == null) {
            return null;
        }

        return stripChars(stripChars(toAscii(text), "[^a-zA-Z0-9\\s]"), "[\\n\\t]").trim();
    }

    /**
     * Remove todos os caracteres não numéricos do texto.
     * 
     * @param text
     *            O texto que deverá ser tratado
     * @return Retorna o texto contendo apenas caracteres numéricos
     */
    public static String stripNonNumericChars(String text) {
        if (text == null) {
            return null;
        }

        return text.replaceAll("[^\\d]", "");
    }

    /**
     * Converte todos os caracteres para a representação ASCII.
     * 
     * @param text
     *            O texto que deverá ser tratado
     * @return Retorna o texto contendo apenas caracteres ASCII
     */
    public static String toAscii(final String text) {
        if (text == null) {
            return null;
        }

        return Normalizer.normalize(text, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Remove espaços em branco antes e depois do texto.
     * 
     * @param text
     *            O texto que deverá ser tratado
     * @return Retorna uma cópia do texto sem espações antes ou depois do texto
     */
    public static String trim(String text) {
        if (text == null) {
            return null;
        }

        return text.trim();
    }

    /**
     * Não deve ser instanciada.
     */
    private TextNormalizerUtils() {
    }
}
