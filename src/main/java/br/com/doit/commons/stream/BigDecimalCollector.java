package br.com.doit.commons.stream;

import static java.util.stream.Collectors.collectingAndThen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BinaryOperator;
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
public abstract class BigDecimalCollector<A> implements Collector<BigDecimal, A, Optional<BigDecimal>> {
    private BinaryOperator<BigDecimal> divide = (dividend, divisor) -> dividend.divide(divisor);

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
    public Collector<BigDecimal, A, Optional<BigDecimal>> withScale(int scale, RoundingMode roundingMode) {
        Function<Optional<BigDecimal>, Optional<BigDecimal>> rounding = o -> o.map(v -> v.setScale(scale, roundingMode));

        // Isso é necessário pois uma divisão que gere uma dízima periódica deve ser arredondada no momento da divisão e
        // não depois
        divide = (dividend, divisor) -> dividend.divide(divisor, scale, roundingMode);

        return collectingAndThen(this, rounding);
    }

    protected BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return divide.apply(dividend, divisor);
    }
}
