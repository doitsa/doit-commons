package br.com.doit.commons.stream;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Collector capaz de calcular a somatória de elementos que podem ser mapeados para o tipo {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class BigDecimalSummingCollector<T> extends AbstractBigDecimalCollector<T, BigDecimal> {
    private final Function<? super T, BigDecimal> mapper;

    public BigDecimalSummingCollector(Function<? super T, BigDecimal> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Supplier<BigDecimalAccumulator> supplier() {
        return BigDecimalAccumulator::new;
    }

    @Override
    public BiConsumer<BigDecimalAccumulator, T> accumulator() {
        return (acc, o) -> acc.add(mapper.apply(o));
    }

    @Override
    public BinaryOperator<BigDecimalAccumulator> combiner() {
        return BigDecimalAccumulator::combine;
    }

    @Override
    public Function<BigDecimalAccumulator, BigDecimal> finisher() {
        return acc -> acc.amount;
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT));
    }

    @Override
    protected Function<BigDecimal, BigDecimal> map(Function<BigDecimal, BigDecimal> round) {
        return round;
    }
}