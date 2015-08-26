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
import java.util.stream.Collector;

/**
 * Implementações da classe {@link Collector} com funcionalidades relacionadas à classe {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class BigDecimalCollectors {
    static class AverageAccumulator {
        public int count;
        public BigDecimal amount;

        public AverageAccumulator() {
            this(0, BigDecimal.ZERO);
        }

        public AverageAccumulator(int count, BigDecimal amount) {
            this.count = count;
            this.amount = amount;
        }

        public void add(BigDecimal amount) {
            count++;
            this.amount = this.amount.add(amount);
        }

        public Optional<BigDecimal> average() {
            if (count == 0) {
                return Optional.empty();
            }

            return Optional.of(amount.divide(new BigDecimal(count)));
        }
    }

    public static class BigDecimalAveragingCollector implements BigDecimalCollector<AverageAccumulator> {
        @Override
        public Supplier<AverageAccumulator> supplier() {
            return AverageAccumulator::new;
        }

        @Override
        public BiConsumer<AverageAccumulator, BigDecimal> accumulator() {
            return AverageAccumulator::add;
        }

        @Override
        public BinaryOperator<AverageAccumulator> combiner() {
            return (a1, a2) -> new AverageAccumulator(a1.count + a2.count, a1.amount.add(a2.amount));
        }

        @Override
        public Function<AverageAccumulator, Optional<BigDecimal>> finisher() {
            return AverageAccumulator::average;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT));
        }
    }

    /**
     * Retorna um {@code Collector} que calcula a média entre os {@code BigDecimal}s informados.
     *
     * @return um {@code Collector} que calcula a média entre os valores fornecidos.
     */
    public static BigDecimalCollector<?> averaging() {
        return new BigDecimalAveragingCollector();
    }

    /**
     * Essa classe não deve ser instanciada.
     */
    private BigDecimalCollectors() {
    }
}