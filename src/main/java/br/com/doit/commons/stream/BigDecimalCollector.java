package br.com.doit.commons.stream;

import java.math.RoundingMode;
import java.util.stream.Collector;

/**
 * Essa interface adiciona métodos que fazem sentido para todos os collectors relacionados com {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public interface BigDecimalCollector<T, A, R> extends Collector<T, A, R> {
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
    Collector<T, A, R> withScale(int scale, RoundingMode roundingMode);
}
