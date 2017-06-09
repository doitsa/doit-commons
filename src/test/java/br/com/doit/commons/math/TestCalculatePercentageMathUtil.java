package br.com.doit.commons.math;

import static br.com.doit.commons.math.MathUtils.calculatePercentage;
import static java.math.BigDecimal.ZERO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestCalculatePercentageMathUtil {
    @Test
    public void calculatePercentageWhenWholeAndPartAreGreaterThanZero() throws Exception {
        BigDecimal part = new BigDecimal("7.77");
        BigDecimal whole = new BigDecimal("33.33");

        BigDecimal result = calculatePercentage(whole, part);

        assertThat(result, is(new BigDecimal("23.31")));
    }

    @Test
    public void calculatePercentageWhenWholeIsEqualToZero() throws Exception {
        BigDecimal part = new BigDecimal("7.77");
        BigDecimal whole = ZERO;

        BigDecimal result = calculatePercentage(whole, part);

        assertThat(result, is(ZERO));
    }

    @Test
    public void calculatePercentageWhenPartIsEqualToZero() throws Exception {
        BigDecimal part = ZERO;
        BigDecimal whole = new BigDecimal("33.33");

        BigDecimal result = calculatePercentage(whole, part);

        assertThat(result, is(new BigDecimal("0.00")));
    }
}
