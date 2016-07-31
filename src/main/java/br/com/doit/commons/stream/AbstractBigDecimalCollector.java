package br.com.doit.commons.stream;

import static java.util.stream.Collectors.collectingAndThen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Essa classe contém a implentação básica da interface {@code BigDecimalCollector} e pode ser usada como base para
 * classes que queiram implementá-la.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
abstract class AbstractBigDecimalCollector<T, R> implements BigDecimalCollector<T, BigDecimalAccumulator, R> {
    private BinaryOperator<BigDecimal> divide = (dividend, divisor) -> dividend.divide(divisor);

    /*
     * (non-Javadoc)
     *
     * @see br.com.doit.commons.stream.BigDecimalCollector#withScale(int, java.math.RoundingMode)
     */
    @Override
    public Collector<T, BigDecimalAccumulator, R> withScale(int scale, RoundingMode roundingMode) {
        Function<R, R> rounding = map(v -> v.setScale(scale, roundingMode));

        // Isso é necessário pois uma divisão que gere uma dízima periódica deve ser arredondada no momento da divisão
        // não depois
        divide = (dividend, divisor) -> dividend.divide(divisor, scale, roundingMode);

        return collectingAndThen(this, rounding);
    }

    /**
     * Operação de divisão que pode conter ou não arredondamento.
     *
     * @param dividend
     *            O valor que será dividido.
     * @param divisor
     *            O valor que será usado para fazer a divisão.
     * @return Retorna o resultado da divisão (podendo ser arredondado).
     */
    protected BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return divide.apply(dividend, divisor);
    }

    /**
     * Mapeia a função de {@code round} para suportar o tipo de resultado esperado por esse collector.
     *
     * @param round
     *            A função de arredondamento que será mapeada.
     * @return Retorna a função de arredondamento mapeada de acordo com o tipo de resultado esperado para esse
     *         collector.
     */
    protected abstract Function<R, R> map(Function<BigDecimal, BigDecimal> round);
}
