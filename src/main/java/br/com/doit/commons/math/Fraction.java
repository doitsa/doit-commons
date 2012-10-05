package br.com.doit.commons.math;

import java.math.BigDecimal;

/**
 * Uma <code>Fraction</code> corresponde a um pedaço do todo. O todo é determinado pela soma do peso (
 * <code>BigDecimal weight = </code> {@link Fraction#quantity()} * {@link Fraction#value()}) de cada
 * <code>Fraction</code>. Uma <code>Fraction</code> pode ser usada em operações matemáticas, normalmente ligadas com
 * distribuição de valores.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public interface Fraction {
    /**
     * @return Retorna a quantidade dessa <code>Fraction</code>
     */
    public BigDecimal quantity();

    /**
     * Após uma operação matemática de distribuição de valores, esse método é chamado para dizer qual é a parte que cabe
     * a essa <code>Fraction</code> (de acordo com o seu peso).
     * 
     * @param share
     *            O valor correspondente à parte que cabe a essa <code>Fraction</code>
     */
    public void setShare(BigDecimal share);

    /**
     * @return Retorna o valor dessa <code>Fraction</code>
     */
    public BigDecimal value();
}
