package br.com.doit.commons.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Essa interface adiciona métodos que fazem sentido para todos os collectors relacionados com {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 *
 * @param <A>
 *            o tipo do acumulador que é usado nas operações de redução.
 */
public interface BigDecimalCollector<A> extends Collector<BigDecimal, A, Optional<BigDecimal>> {
    /**
     * Retorna um {@code Collector} que vai arredondar o valor determinado de acordo com a escala e o método de
     * arredondamento.
     *
     * @param scale
     *            O número de casas decimais.
     * @param roundingMode
     *            O método de arredondamento que será aplicado.
     * @return Retorna um {@code Collector} capaz de arredondar o valor retornado pela operação anterior.
     */
    public default Collector<BigDecimal, A, Optional<BigDecimal>> withScale(int scale, RoundingMode roundingMode) {
        Function<Optional<BigDecimal>, Optional<BigDecimal>> rounding = o -> o.map(v -> v.setScale(scale, roundingMode));

        return new FinisherCollectorDecorator<>(this, rounding);
    }
}
