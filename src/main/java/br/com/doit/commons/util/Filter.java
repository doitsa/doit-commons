/**
 *
 */
package br.com.doit.commons.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author <a href="mailto:rdskill91@gmail.com">Rodrigo de Sousa</a>
 */
public class Filter {
    /**
     * Esse método pode ser usado para filtrar os resultados de uma stream por uma determinada chave.
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
