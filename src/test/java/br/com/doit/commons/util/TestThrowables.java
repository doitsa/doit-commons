package br.com.doit.commons.util;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.stream.Stream;

import org.apache.commons.lang.UnhandledException;
import org.junit.Test;

public class TestThrowables {
    @Test
    public void throwingFunction() throws Exception {
        NumberFormat formatter = NumberFormat.getIntegerInstance();

        try {
            Stream.of("a")
                  .map(Throwables.rethrowing(formatter::parseObject))
                  .collect(toList()); // We need to call a terminal operation to evaluate the stream.

            fail("Must throw an exception");
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnhandledException.class));
            assertThat(exception.getCause(), instanceOf(ParseException.class));
        }
    }
}
