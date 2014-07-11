package br.com.doit.commons.lang;

import static org.apache.commons.lang.ArrayUtils.removeElement;

/**
 * Classe utilitária para manipular <code>Enum</code>s.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class Enums {
    /**
     * Obtém o conjunto de valores de um <code>Enum</code> não incluindo os valores passados por parâmetro.
     * 
     * @param clazz
     *            A classe o <code>Enum</code>
     * @param excludes
     *            Os valores que não deverão ser retornados
     * @return Retorna um array com todo os valores de um <code>Enum</code> menos aqueles que foram informados por
     *         parâmetro.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T[] valuesExcluding(Class<T> clazz, T... excludes) {
        T[] values = clazz.getEnumConstants();

        for (T exclude : excludes) {
            values = (T[]) removeElement(values, exclude);
        }

        return values;
    }

    private Enums() {
        // Não deve ser instaciada
    }
}
