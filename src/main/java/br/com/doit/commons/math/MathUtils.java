package br.com.doit.commons.math;

import static br.com.doit.commons.math.MathUtils.Mode.IGNORING_QUANTITY;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class MathUtils {
    public enum Mode {
        IGNORING_QUANTITY, IGNORING_REMAINDER
    }

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

        public void over(Collection<Fraction> whole) {
            BigDecimal total = total(whole);

            if (ZERO.compareTo(total) == 0) {
                return;
            }

            BigDecimal totalShare = ZERO;

            Map<Fraction, BigDecimal> shares = new HashMap<Fraction, BigDecimal>(whole.size());

            for (Fraction fraction : whole) {
                BigDecimal ration = amount.multiply(fraction.value()).divide(total, 2, RoundingMode.FLOOR);

                BigDecimal share = ration.multiply(fraction.quantity());

                shares.put(fraction, share);

                totalShare = totalShare.add(share);
            }

            BigDecimal remainder = amount.subtract(totalShare);

            for (Fraction fraction : whole) {
                BigDecimal share = shares.get(fraction);

                if (isRemainderDivisibleByQuantity(remainder, fraction.quantity()) || mode == IGNORING_QUANTITY) {
                    share = share.add(remainder);

                    remainder = ZERO;
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

    public static Preparation distribute(BigDecimal amount) {
        return new Preparation(amount, Mode.IGNORING_REMAINDER);
    }

    public static Preparation distribute(BigDecimal amount, Mode mode) {
        return new Preparation(amount, mode);
    }

}
