package br.com.doit.commons.lang;

import static org.apache.commons.lang.ArrayUtils.removeElement;

import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     *
     * @param type
     *            O tipo de Enum cujo valor será obtido.
     * @param field
     *            O campo à ser obtido de enum informado.
     * @return Retorna o valor obtido do enum de acordo com a nome do campo, caso não seja possível obter um valor para
     *         a chave, o valor <code>null</code> é retornado.
     */

    public static <T extends Enum<T>> T valueOfOrNull(Class<T> type, String field) {
        return optionalValueOf(type, field).orElse(null);
    }

    /**
     *
     * @param type
     *            O tipo de Enum cujo valor será obtido.
     * @param field
     *            O campo à ser obtido de enum informado.
     * @return Retorna um <code>Optional</code> do valor obtido do enum de acordo com a nome do campo, caso não seja
     *         possível obter um valor para
     *         a chave, o valor <code>Optional.empty()</code> é retornado.
     */
    public static <T extends Enum<T>> Optional<T> optionalValueOf(Class<T> type, String field) {
        if (type == null) {
            return Optional.empty();
        }

        return Stream.of(type.getEnumConstants())
                     .filter(enumTypeValue -> enumTypeValue.toString().equals(field))
                     .findFirst();
    }

    private Enums() {
        // Não deve ser instaciada
    }
}
