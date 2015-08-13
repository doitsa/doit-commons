package br.com.doit.commons.stream;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
 * Implementações da class {@link Collector} com funcionalidades relacionadas com WebObjects.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class ERXCollectors {
    static class ToNSArrayCollector<T> implements Collector<T, NSArray<T>, NSArray<T>> {
        @Override
        public Supplier<NSArray<T>> supplier() {
            return NSMutableArray::new;
        }

        @Override
        public BiConsumer<NSArray<T>, T> accumulator() {
            return NSArray::add;
        }

        @Override
        public BinaryOperator<NSArray<T>> combiner() {
            return (array1, array2) -> {
                array1.addAll(array2);
                return array1;
            };
        }

        @Override
        public Function<NSArray<T>, NSArray<T>> finisher() {
            return NSArray::immutableClone;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    /**
     * Retorna um {@code Collector} que acumula os elementos em um novo {@code NSArray}.
     *
     * @param <T>
     *            o tipo dos elementos
     * @return um {@code Collector} que acumula todos os elementos em um {@code NSArray}, de acordo com a ordem em que
     *         eles forem fornecidos
     */
    public static <T> Collector<T, ?, NSArray<T>> toNSArray() {
        return new ToNSArrayCollector<>();
    }

    /**
     * Essa classe não deve ser instanciada.
     */
    private ERXCollectors() {
    }
}
