package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;

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

    private final Either<Object, Object> either;
    private final Object left;
    private final boolean isLeft;
    private final boolean isRight;
    private final Object right;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TestEither(String type, Either<Object, Object> either, boolean isRight, boolean isLeft, Object right, Object left) {
        this.either = either;
        this.isRight = isRight;
        this.isLeft = isLeft;
        this.right = right;
        this.left = left;
    }

    @Test
    public void applyCorrectFunctionWhenFoldingWithFunction() throws Exception {
        String result = either.fold(o -> "left", o -> "right");

        if (isLeft) {
            assertThat(result, is("left"));
        } else {
            assertThat(result, is("right"));
        }
    }

    @Test
    public void eitherIsLeftOrIsRight() throws Exception {
        assertThat(either.isRight(), is(isRight));
        assertThat(either.isLeft(), is(isLeft));
    }

    @Test
    public void returnExpectedLeftOrRight() throws Exception {
        assertThat(either.left(), is(left));
        assertThat(either.right(), is(right));
    }

    @Test
    public void throwExceptionWhenCreatingEitherWithBothLeftAndRightNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("Either must have a left or a right. Left: null. Right: null."));

        Either.right(null);
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
