package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(Parameterized.class)
public class TestEither {
    @Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        Object expectedObject = new Object();

        return Arrays.asList(new Object[][] {
                { "Success", Either.right(expectedObject), true, false, expectedObject, null },
                { "Failure", Either.left(expectedObject), false, true, null, expectedObject }
        });
    }

    private final String type;
    private final Either<Object, Object> either;
    private final Object left;
    private final boolean isLeft;
    private final boolean isRight;
    private final Object right;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TestEither(String type, Either<Object, Object> either, boolean isRight, boolean isLeft, Object right, Object left) {
        this.type = type;
        this.either = either;
        this.isRight = isRight;
        this.isLeft = isLeft;
        this.right = right;
        this.left = left;
    }

    @Test
    public void applyCorrectFunctionWhenFolding() throws Exception {
        String result = either.fold(o -> "Failure", o -> "Success");

        assertThat(result, is(type));
    }

    @Test
    public void acceptCorrectConsumerWhenRunning() throws Exception {
        // This is a workaround because we can't set a variable from inside a lambda expression
        List<String> result = new ArrayList<>();

        either.run(o -> result.add("Failure"), o -> result.add("Success"));

        assertThat(result, hasItem(type));
    }

    @Test
    public void eitherIsLeftOrIsRight() throws Exception {
        assertThat(either.isRight(), is(isRight));
        assertThat(either.isLeft(), is(isLeft));
    }

    @Test
    public void returnExpectedLeftOrRight() throws Exception {
        if(either.isLeft()) {
            assertThat(either.left(), is(left));
        } else {
            assertThat(either.right(), is(right));
        }
    }

    @Test
    public void throwExceptionWhenGettingRightFromLeft() throws Exception {
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(is("Cannot get a Right from a Left."));

        Either.left("ABC").right();
    }

    @Test
    public void throwExceptionWhenGettingLeftFromRight() throws Exception {
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(is("Cannot get a Left from a Right."));

        Either.right("ABC").left();
    }

    @Test
    public void returnLeftValueWhenGeneratingToStringForALeft() throws Exception {
        String toString = Either.left("test").toString();

        assertThat(toString, is("Left[test]"));
    }

    @Test
    public void returnRightValueWhenGeneratingToStringForARight() throws Exception {
        String toString = Either.right(BigDecimal.ONE).toString();

        assertThat(toString, is("Right[1]"));
    }
}
