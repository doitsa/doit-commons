package br.com.doit.commons.math;

import java.math.BigDecimal;

public interface Fraction {
    public abstract BigDecimal quantity();

    public abstract void setShare(BigDecimal share);

    public abstract BigDecimal value();
}
