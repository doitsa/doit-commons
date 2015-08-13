package br.com.doit.commons.stream;

import static br.com.doit.commons.stream.ERXCollectors.toNSArray;
import static java.util.stream.Stream.concat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.stream.Stream;

import org.junit.Test;

import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestERXCollectors {
    @Test
    public void collectEmptyNSArrayWhenStreamReturnsNoElements() throws Exception {
        NSArray<?> result = Stream.empty().collect(toNSArray());

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void collectNSArrayWithOneElementWhenStreamReturnsOneElement() throws Exception {
        NSArray<String> result = Stream.of("test").collect(toNSArray());

        assertThat(result.size(), is(1));
        assertThat(result, hasItem("test"));
    }

    @Test
    public void collectNSArrayWithMoreThanOneElementWhenStreamReturnsTwoElements() throws Exception {
        NSArray<String> result = Stream.of("test1", "test2").collect(toNSArray());

        assertThat(result.size(), is(2));
        assertThat(result, hasItems("test1", "test2"));
    }

    @Test
    public void collectCombinedArrayWhenStreamCombinedWithAnotherInParallel() throws Exception {
        NSArray<String> result = concat(Stream.of("test1").parallel(), Stream.of("test2").parallel()).collect(toNSArray());

        assertThat(result.size(), is(2));
        assertThat(result, hasItems("test1", "test2"));
    }
}
