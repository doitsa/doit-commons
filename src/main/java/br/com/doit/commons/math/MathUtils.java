package br.com.doit.commons.math;

import static br.com.doit.commons.math.MathUtils.RemainderDistributionMode.IGNORING_QUANTITY;
import static br.com.doit.commons.math.MathUtils.RemainderDistributionMode.STRICTLY_PROPORTIONAL;
import static br.com.doit.commons.math.MathUtils.RemainderDistributionMode.UNEVENLY;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A classe <code>MathUtils</code> contém funções auxiliares para lidar com operações matemáticas.
 * 
 * @see Fraction
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class MathUtils {
    /**
     * Essa classe será usada em conjunto com a operação de distribuição de um valor. Ela não deve ser usada de forma
     * isolada.
     * 
     * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
     */
    public static final class Preparation {
        private static boolean isRemainderDivisibleByQuantity(BigDecimal remainder, BigDecimal quantity) {

            if (ZERO.compareTo(remainder) == 0 || ZERO.compareTo(quantity) == 0) {
                return false;
            }

            return ZERO.compareTo(remainder.multiply(new BigDecimal("100")).remainder(quantity)) == 0;
        }

        private final BigDecimal amount;
        private final RemainderDistributionMode mode;

        private Preparation(BigDecimal amount, RemainderDistributionMode mode) {
            this.amount = amount;
            this.mode = mode;
        }

        /**
         * Distribui um valor de forma proporcional entre o conjunto de <code>Fraction</code>s que formam o todo.
         * Exemplo de uso:
         * 
         * <pre>
         * MathUtils.distribute(new BigDecimal(2)).over(collectionOfFractions);
         * </pre>
         * 
         * @param whole
         *            Uma coleção de <code>Fraction</code>s que representam o todo
         * 
         * @return Caso esteja utilizando o <code>Mode</code> do tipo <code>RETURNING_REMAINDER</code> será retornado o
         *         valor do resto.
         *         Caso esteja utilizando o <code>Mode</code> do tipo <code>IGNORING_QUANTITY</code> será feito a
         *         distribuição do resto, assim será retornado <code>BigDecimal.ZERO</code>.
         */
        public BigDecimal over(Collection<Fraction> whole) {

            whole = sortedByValue(whole);

            BigDecimal total = total(whole);

            if (ZERO.compareTo(total) == 0) {
                return ZERO;
            }

            BigDecimal totalShare = ZERO;

            Map<Fraction, BigDecimal> shares = new HashMap<Fraction, BigDecimal>(whole.size());

            for (Fraction fraction : whole) {
                BigDecimal ratio = amount.multiply(fraction.value()).divide(total, 2, RoundingMode.FLOOR);

                BigDecimal share = ratio.multiply(fraction.quantity()).setScale(2, RoundingMode.HALF_EVEN);

                shares.put(fraction, share);

                totalShare = totalShare.add(share);
            }

            BigDecimal remainder = amount.subtract(totalShare);

            for (Fraction fraction : whole) {
                BigDecimal share = shares.get(fraction);

                if (mode != STRICTLY_PROPORTIONAL
                        &&
                        (isRemainderDivisibleByQuantity(remainder, fraction.quantity()) || mode == IGNORING_QUANTITY)) {

                    BigDecimal subtotal = fraction.value().multiply(fraction.quantity());

                    BigDecimal available = subtotal.subtract(share);

                    if (available.compareTo(ZERO) > 0 && remainder.compareTo(available) > 0) {
                        remainder = remainder.subtract(available);

                        share = subtotal;
                    } else {
                        share = share.add(remainder);

                        remainder = ZERO;
                    }
                }

                fraction.setShare(share);
            }

            return remainder;
        }

        private Collection<Fraction> sortedByValue(Collection<Fraction> whole) {
            List<Fraction> listSortable = new ArrayList<Fraction>(whole);
            Collections.sort(listSortable, Collections.reverseOrder(new Comparator<Fraction>() {
                @Override
                public int compare(Fraction o1, Fraction o2) {
                    return o1.value().compareTo(o2.value());
                }
            }));
            return listSortable;
        }

        private BigDecimal total(Collection<Fraction> whole) {
            BigDecimal total = ZERO;

            for (Fraction fraction : whole) {
                total = total.add(fraction.quantity().multiply(fraction.value()));
            }

            return total;
        }
    }

    /**
     * Os modos de distribuição do resto, caso a distribuição pró-rata não seja exata.
     * 
     * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
     */
    public enum RemainderDistributionMode {
        /**
         * Ignora a quantidade de cada <code>Fraction</code> na hora de distribuir o resto. Nesse modo, é garantido que
         * todo o valor passado por parâmetro será distribuídos entre as <code>Fraction</code>s.
         */
        IGNORING_QUANTITY,

        /**
         * Retorna o valor do resto e ignora a distribuição do mesmo caso não seja possível distribuir o valor de forma
         * proporcional.
         */
        STRICTLY_PROPORTIONAL,

        /**
         * Ignora o resto caso não seja possível distribuir o valor de forma proporcional, levando em conta a quantidade
         * de cada <code>Fraction</code>.
         */
        UNEVENLY
    }

    /**
     * Calcula a porcentagem correspondente à parte de um todo.
     *
     * @param whole
     *            O todo.
     * @param part
     *            A parte do todo.
     * @return Retorna a parte do todo como porcentagem. Se o todo for igual a zero, retorna zero.
     */
    public static BigDecimal calculatePercentage(BigDecimal whole, BigDecimal part) {
        if (whole.compareTo(ZERO) == 0) {
            return ZERO;
        }

        return part.multiply(new BigDecimal("100")).divide(whole, 2, HALF_EVEN);
    }

    /**
     * Distribui um valor de forma proporcional entre o conjunto de <code>Fraction</code>s que formam o todo. Ignora o
     * resto caso a divisão proporcional não seja exata. Exemplo de uso:
     * 
     * <pre>
     * MathUtils.distribute(new BigDecimal(2)).over(collectionOfFractions);
     * </pre>
     * 
     * @param amount
     *            o valor que será distribuído entre as <code>Fraction</code>s que compõem o todo
     */
    public static Preparation distribute(BigDecimal amount) {
        return new Preparation(amount, UNEVENLY);
    }

    /**
     * Distribui um valor de forma proporcional entre o conjunto de <code>Fraction</code>s que formam o todo. Caso a
     * operação gere um resto, este será distribuído de acordo com o modo fornecido por parâmetro. Exemplo de uso:
     * 
     * <pre>
     * MathUtils.distribute(new BigDecimal(2), Mode.IGNORING_QUANTITY).over(collectionOfFractions);
     * </pre>
     * 
     * @param amount
     *            o valor que será distribuído entre as <code>Fraction</code>s que compõem o todo
     * @param mode
     *            define o que deve ser feito com o resto caso a divisão proporcional não seja exata
     */
    public static Preparation distribute(BigDecimal amount, RemainderDistributionMode mode) {
        return new Preparation(amount, mode);
    }

    /**
     * Não deve ser instanciada.
     */
    private MathUtils() {
    }

}
