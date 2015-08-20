package br.com.doit.commons.stream;

import static br.com.doit.commons.stream.BigDecimalCollectors.averaging;
import static java.util.stream.Stream.concat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
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
}
