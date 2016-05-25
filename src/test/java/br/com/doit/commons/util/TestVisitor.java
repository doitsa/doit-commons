package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestVisitor {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void applyFunctionWhenVisitorHasOneMatchingType() throws Exception {
        Function<Object, String> visitor = new Visitor<String>()
                .on(String.class).then(s -> s.toUpperCase());

        assertThat(visitor.apply("test"), is("TEST"));
    }

    @Test
    public void applyFunctionWhenVisitorHasTwoPossibleTypes() throws Exception {
        Function<Object, String> visitor = new Visitor<String>()
                .on(String.class).then(s -> s.toUpperCase())
                .on(Integer.class).then(x -> String.valueOf(x + 1));

        assertThat(visitor.apply(1), is("2"));
    }

    @Test
    public void applyDefaultFunctionWhenVisitorHasNoMatchingType() throws Exception {
        Function<Object, String> visitor = new Visitor<String>()
                .on(String.class).then(s -> s.toUpperCase())
                .orDefault(Object::toString);

        assertThat(visitor.apply(1), is("1"));
    }

    @Test
    public void applyFunctionWhenVisitorHasDefaultAndMatchingType() throws Exception {
        Function<Object, String> visitor = new Visitor<String>()
                .on(String.class).then(s -> s.toUpperCase())
                .orDefault(Object::toString);

        assertThat(visitor.apply("test"), is("TEST"));
    }

    @Test
    public void throwExceptionWhenVisitorHasNoMatchingType() throws Exception {
        Function<Object, String> visitor = new Visitor<String>()
                .on(String.class).then(s -> s.toUpperCase());

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("No match found.");

        visitor.apply(1);
    }
}
