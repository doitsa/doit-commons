package br.com.doit.commons.stream;

import java.math.BigDecimal;

/**
 * Classe auxiliar que funciona como acumulador de {@code BigDecimal}.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class BigDecimalAccumulator {
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

    public BigDecimalAccumulator combine(BigDecimalAccumulator acc) {
        return new BigDecimalAccumulator(count + acc.count, amount.add(acc.amount));
    }
}
