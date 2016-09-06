package br.com.doit.commons.stream;

import static br.com.doit.commons.stream.BigDecimalCollectors.averaging;
import static br.com.doit.commons.stream.BigDecimalCollectors.summing;
import static java.util.function.Function.identity;
import static java.util.stream.Stream.concat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestBigDecimalCollectors {
    @Test
    public void averagingWhenStreamReturnsNoElements() throws Exception {
        Optional<BigDecimal> result = Stream.<BigDecimal> empty().collect(averaging());

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void averagingWhenStreamReturnsOneElement() throws Exception {
        Optional<BigDecimal> result = Stream.of(BigDecimal.ONE).collect(averaging());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(BigDecimal.ONE));
    }

    @Test
    public void averagingWhenStreamReturnsTwoElements() throws Exception {
        Optional<BigDecimal> result = Stream.of(BigDecimal.ONE, BigDecimal.TEN).collect(averaging());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(new BigDecimal("5.5")));
    }

    @Test
    public void averagingCombineResultWhenStreamCombinedWithAnotherInParallel() throws Exception {
        Optional<BigDecimal> result = concat(Stream.of(BigDecimal.ONE).parallel(), Stream.of(BigDecimal.TEN).parallel()).collect(averaging());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(new BigDecimal("5.5")));
    }

    @Test
    public void roundValueWhenAveragingWithScaleAndRoundMode() throws Exception {
        Optional<BigDecimal> result = Stream.of(new BigDecimal("0.4"), new BigDecimal("0.3")).collect(averaging().withScale(1, RoundingMode.HALF_EVEN));

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(new BigDecimal("0.4")));
    }

    @Test
    public void avoidArithmeticExceptionWhenAveragingWithScaleAndRoundMode() throws Exception {
        Optional<BigDecimal> result = Stream.of(new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("2")).collect(averaging().withScale(1, RoundingMode.HALF_EVEN));

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(new BigDecimal("2.3")));
    }

    @Test
    public void summingWhenStreamReturnsNoElements() throws Exception {
        BigDecimal result = Stream.<BigDecimal> empty().collect(summing(identity()));

        assertThat(result, is(BigDecimal.ZERO));
    }

    @Test
    public void summingWhenStreamReturnsOneElement() throws Exception {
        BigDecimal result = Stream.of(BigDecimal.ONE).collect(summing(identity()));

        assertThat(result, is(BigDecimal.ONE));
    }

    @Test
    public void summingWhenStreamReturnsTwoElements() throws Exception {
        BigDecimal result = Stream.of(BigDecimal.ONE, BigDecimal.TEN).collect(summing(identity()));

        assertThat(result, is(new BigDecimal("11")));
    }

    @Test
    public void summingCombineResultWhenStreamCombinedWithAnotherInParallel() throws Exception {
        BigDecimal result = concat(Stream.of(BigDecimal.ONE).parallel(), Stream.of(BigDecimal.TEN).parallel()).collect(summing(identity()));

        assertThat(result, is(new BigDecimal("11")));
    }

    @Test
    public void summingApplyFunctionWhenStreamContainsObjects() throws Exception {
        BigDecimal result = Stream.of(Optional.of(BigDecimal.ONE)).collect(summing(Optional::get));

        assertThat(result, is(BigDecimal.ONE));
    }

    @Test
    public void roundValueWhenSummingWithScaleAndRoundMode() throws Exception {
        BigDecimal result = Stream.of(new BigDecimal("0.41"), new BigDecimal("0.31")).collect(summing(identity()).withScale(1, RoundingMode.HALF_EVEN));

        assertThat(result, is(new BigDecimal("0.7")));
    }
}
