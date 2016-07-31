package br.com.doit.commons.stream;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Collector capaz de calcular a média de elementos do tipo {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class BigDecimalAveragingCollector extends AbstractBigDecimalCollector<BigDecimal, Optional<BigDecimal>> {
    @Override
    public Supplier<BigDecimalAccumulator> supplier() {
        return BigDecimalAccumulator::new;
    }

    @Override
    public BiConsumer<BigDecimalAccumulator, BigDecimal> accumulator() {
        return BigDecimalAccumulator::add;
    }

    @Override
    public BinaryOperator<BigDecimalAccumulator> combiner() {
        return BigDecimalAccumulator::combine;
    }

    @Override
    public Function<BigDecimalAccumulator, Optional<BigDecimal>> finisher() {
        return acc -> {
            if (acc.count == 0) {
                return Optional.empty();
            }

            return Optional.of(divide(acc.amount, new BigDecimal(acc.count)));
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT));
    }

    @Override
    protected Function<Optional<BigDecimal>, Optional<BigDecimal>> map(Function<BigDecimal, BigDecimal> round) {
        return o -> o.map(round);
    }
}
