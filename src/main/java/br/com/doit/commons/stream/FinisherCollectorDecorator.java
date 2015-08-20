package br.com.doit.commons.stream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Esse {@code Collector} permite decorar um collector existente e aplicar uma função ao final da operação.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 *
 */
public class FinisherCollectorDecorator<T, A, R> implements Collector<T, A, R> {
    private final Collector<T, A, R> decorated;
    private final Function<R, R> decoration;

    public FinisherCollectorDecorator(Collector<T, A, R> decorated, Function<R, R> decoration) {
        super();

        this.decorated = decorated;
        this.decoration = decoration;
    }

    @Override
    public Supplier<A> supplier() {
        return decorated.supplier();
    }

    @Override
    public BiConsumer<A, T> accumulator() {
        return decorated.accumulator();
    }

    @Override
    public BinaryOperator<A> combiner() {
        return decorated.combiner();
    }

    @Override
    public Function<A, R> finisher() {
        return a -> decoration.apply(decorated.finisher().apply(a));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return decorated.characteristics();
    }
}
