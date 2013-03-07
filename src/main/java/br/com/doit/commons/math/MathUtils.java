package br.com.doit.commons.math;

import static br.com.doit.commons.math.MathUtils.Mode.IGNORING_QUANTITY;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A classe <code>MathUtils</code> contém funções auxiliares para lidar com operações matemáticas.
 * 
 * @see Fraction
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class MathUtils {
    /**
     * Os modos de distribuição do resto, caso a distribuição pró-rata não seja exata.
     * 
     * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
     */
    public enum Mode {
        /**
         * Ignora a quantidade de cada <code>Fraction</code> na hora de distribuir o resto. Nesse modo, é garantido que
         * todo o valor passado por parâmetro será distribuídos entre as <code>Fraction</code>s.
         */
        IGNORING_QUANTITY,

        /**
         * Ignora o resto caso não seja possível distribuir o valor de forma proporcional, levando em conta a quantidade
         * de cada <code>Fraction</code>.
         */
        IGNORING_REMAINDER
    }

    /**
     * Essa classe será usada em conjunto com a operação de distribuição de um valor. Ela não deve ser usada de forma
     * isolada.
     * 
     * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
     */
    public static final class Preparation {
        private static boolean isRemainderDivisibleByQuantity(BigDecimal remainder, BigDecimal quantity) {
            if (ZERO.compareTo(quantity) == 0) {
                return false;
            }

            return ZERO.compareTo(remainder.multiply(new BigDecimal("100")).remainder(quantity)) == 0;
        }

        private final BigDecimal amount;
        private final Mode mode;

        private Preparation(BigDecimal amount, Mode mode) {
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
         */
        public void over(Collection<Fraction> whole) {
            BigDecimal total = total(whole);

            if (ZERO.compareTo(total) == 0) {
                return;
            }

            BigDecimal totalShare = ZERO;

            Map<Fraction, BigDecimal> shares = new HashMap<Fraction, BigDecimal>(whole.size());

            for (Fraction fraction : whole) {
                BigDecimal ratio = amount.multiply(fraction.value()).divide(total, 2, RoundingMode.FLOOR);

                BigDecimal share = ratio.multiply(fraction.quantity());

                shares.put(fraction, share);

                totalShare = totalShare.add(share);
            }

            BigDecimal remainder = amount.subtract(totalShare);

            for (Fraction fraction : whole) {
                BigDecimal share = shares.get(fraction);

                if (isRemainderDivisibleByQuantity(remainder, fraction.quantity()) || mode == IGNORING_QUANTITY) {
                    BigDecimal subtotal = fraction.value().multiply(fraction.quantity());

                    BigDecimal available = subtotal.subtract(share);

                    if (remainder.compareTo(available) > 0) {
                        remainder = remainder.subtract(available);

                        share = subtotal;
                    } else {
                        share = share.add(remainder);

                        remainder = ZERO;
                    }
                }

                fraction.setShare(share);
            }
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
        return new Preparation(amount, Mode.IGNORING_REMAINDER);
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
    public static Preparation distribute(BigDecimal amount, Mode mode) {
        return new Preparation(amount, mode);
    }

    /**
     * Não deve ser instanciada.
     */
    private MathUtils() {
    }

}
