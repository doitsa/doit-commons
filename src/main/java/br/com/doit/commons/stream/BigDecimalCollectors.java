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
    static class BigDecimalAccumulator {
        public int count;
        public BigDecimal amount;

        public BigDecimalAccumulator() {
            this(0, BigDecimal.ZERO);
        }

        public BigDecimalAccumulator(int count, BigDecimal amount) {
            this.count = count;
            this.amount = amount;
        }

        public void add(BigDecimal amount) {
            count++;
            this.amount = this.amount.add(amount);
        }
    }

    public static class BigDecimalAveragingCollector extends BigDecimalCollector<BigDecimalAccumulator> {
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
            return (a1, a2) -> new BigDecimalAccumulator(a1.count + a2.count, a1.amount.add(a2.amount));
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
