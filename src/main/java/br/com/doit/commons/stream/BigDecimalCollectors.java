package br.com.doit.commons.stream;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Implementações da classe {@link Collector} com funcionalidades relacionadas à classe {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class BigDecimalCollectors {
    /**
     * Retorna um {@code Collector} que calcula a média entre os {@code BigDecimal}s informados.
     *
     * @return Retorna um {@code Collector} que calcula a média entre os valores fornecidos.
     */
    public static BigDecimalCollector<BigDecimal, ?, Optional<BigDecimal>> averaging() {
        return new BigDecimalAveragingCollector();
    }

    /**
     * Retorna um {@code Collector} que calcula a somatória dos {@code BigDecimal}s mapeados pela função fornecida por
     * parâmetro.
     *
     * @param mapper
     *            A função que faz o mapeamento de um elemento para o valor {@code BigDecimal}.
     * @return Retorna um {@code Collector} que calcula a somatória dos valores fornecidos.
     */
    public static <T> BigDecimalCollector<T, ?, BigDecimal> summing(Function<? super T, BigDecimal> mapper) {
        return new BigDecimalSummingCollector<T>(mapper);
    }

    /**
     * Essa classe não deve ser instanciada.
     */
    private BigDecimalCollectors() {
    }
}
