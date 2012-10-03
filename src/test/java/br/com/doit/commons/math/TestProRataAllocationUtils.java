package br.com.doit.commons.math;

import static br.com.doit.commons.math.MathUtils.Mode.IGNORING_QUANTITY;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestProRataAllocationUtils {
    private Collection<Fraction> whole;

    private Fraction addFraction(BigDecimal quantity, BigDecimal value) {
        Fraction fraction = mock(Fraction.class);

        when(fraction.quantity()).thenReturn(quantity);
        when(fraction.value()).thenReturn(value);

        whole.add(fraction);

        return fraction;
    }

    @Test
    public void distributeAmountEqualToZeroWhenAtLeastOneFraction() throws Exception {
        Fraction fraction = addFraction(ONE, ONE);

        MathUtils.distribute(ZERO).over(whole);

        verify(fraction).setShare(new BigDecimal("0.00"));
    }

    @Test
    public void distributeAmountWhenNoFraction() throws Exception {
        try {
            MathUtils.distribute(ONE).over(whole);
        } catch (Exception exception) {
            fail("Não deveria dar uma exceção");
        }
    }

    @Test
    public void distributeAmountWhenOneFraction() throws Exception {
        Fraction fraction = addFraction(ONE, ONE);

        MathUtils.distribute(BigDecimal.ONE).over(whole);

        verify(fraction).setShare(new BigDecimal("1.00"));
    }

    @Test
    public void distributeAmountWhenQuantityIsZeroInAtLeastOneFraction() throws Exception {
        Fraction fraction = addFraction(ZERO, ONE);
        Fraction fraction2 = addFraction(ONE, ONE);

        MathUtils.distribute(BigDecimal.ONE).over(whole);

        verify(fraction).setShare(new BigDecimal("0.00"));
        verify(fraction2).setShare(new BigDecimal("1.00"));
    }

    @Test
    public void distributeAmountWhenTotalOfFractionsIsZero() throws Exception {
        Fraction fraction = addFraction(ZERO, ZERO);

        MathUtils.distribute(ONE).over(whole);

        verify(fraction, never()).setShare(Mockito.any(BigDecimal.class));
    }

    @Test
    public void distributeAmountWhenTwoDifferentQuantityFractions() throws Exception {
        Fraction fraction = addFraction(new BigDecimal("2"), new BigDecimal("10"));
        Fraction fraction2 = addFraction(new BigDecimal("8"), new BigDecimal("10"));

        MathUtils.distribute(new BigDecimal("10")).over(whole);

        verify(fraction).setShare(new BigDecimal("2.00"));
        verify(fraction2).setShare(new BigDecimal("8.00"));
    }

    @Test
    public void distributeAmountWhenTwoSameWeightFractions() throws Exception {
        Fraction fraction = addFraction(ONE, ONE);
        Fraction fraction2 = addFraction(ONE, ONE);

        MathUtils.distribute(BigDecimal.ONE).over(whole);

        verify(fraction).setShare(new BigDecimal("0.50"));
        verify(fraction2).setShare(new BigDecimal("0.50"));
    }

    @Test
    public void distributeAmountWhenValueIsZeroInAtLeastOneFraction() throws Exception {
        Fraction fraction = addFraction(ONE, ZERO);
        Fraction fraction2 = addFraction(ONE, ONE);

        MathUtils.distribute(BigDecimal.ONE).over(whole);

        verify(fraction).setShare(new BigDecimal("0.00"));
        verify(fraction2).setShare(new BigDecimal("1.00"));
    }

    @Test
    public void distributeRemainderIgnoringQuantityWhenIgnoringQuantityMode() throws Exception {
        Fraction fraction = addFraction(new BigDecimal("13"), new BigDecimal("100000"));
        Fraction fraction2 = addFraction(new BigDecimal("13"), new BigDecimal("100000"));

        MathUtils.distribute(new BigDecimal("0.36"), IGNORING_QUANTITY).over(whole);

        verify(fraction).setShare(new BigDecimal("0.23"));
        verify(fraction2).setShare(new BigDecimal("0.13"));
    }

    @Test
    public void distributeRemainderWhenAtLeastOneFractionHasQuantityOne() throws Exception {
        Fraction fraction = addFraction(ONE, ONE);
        Fraction fraction2 = addFraction(new BigDecimal("2"), ONE);

        MathUtils.distribute(new BigDecimal("3")).over(whole);

        verify(fraction).setShare(new BigDecimal("1.00"));
        verify(fraction2).setShare(new BigDecimal("2.00"));
    }

    @Test
    public void distributeRemainderWhenFractionHasDivisableQuantity() throws Exception {
        Fraction fraction = addFraction(new BigDecimal("12"), new BigDecimal("100000"));
        Fraction fraction2 = addFraction(new BigDecimal("12"), new BigDecimal("100000"));

        MathUtils.distribute(new BigDecimal("0.36")).over(whole);

        verify(fraction).setShare(new BigDecimal("0.24"));
        verify(fraction2).setShare(new BigDecimal("0.12"));
    }

    @Test
    public void ignoreRemainderWhenFractionHasNoDivisableQuantity() throws Exception {
        Fraction fraction = addFraction(new BigDecimal("13"), new BigDecimal("100000"));
        Fraction fraction2 = addFraction(new BigDecimal("13"), new BigDecimal("100000"));

        MathUtils.distribute(new BigDecimal("0.36")).over(whole);

        verify(fraction).setShare(new BigDecimal("0.13"));
        verify(fraction2).setShare(new BigDecimal("0.13"));
    }

    @Before
    public void setup() {
        whole = new ArrayList<Fraction>();
    }
}
