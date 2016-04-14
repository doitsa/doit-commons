package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(Parameterized.class)
public class TestEither {
    @Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        Object expectedObject = new Object();

        return Arrays.asList(new Object[][] {
                { "Success", Either.right(expectedObject), true, false, expectedObject, null, atLeastOnce(), never() },
                { "Failure", Either.left(expectedObject), false, true, null, expectedObject, never(), atLeastOnce() }
        });
    }

    private final Either<Object, Object> either;
    private final Object left;
    private final VerificationMode leftFoldingOccurrence;
    private final boolean isLeft;
    private final boolean isRight;
    private final Object right;
    private final VerificationMode rightFoldingOccurence;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TestEither(String type, Either<Object, Object> either, boolean isRight, boolean isLeft, Object right, Object left, VerificationMode rightFoldingOccurence, VerificationMode leftFoldingOccurrence) {
        this.either = either;
        this.isRight = isRight;
        this.isLeft = isLeft;
        this.right = right;
        this.left = left;
        this.rightFoldingOccurence = rightFoldingOccurence;
        this.leftFoldingOccurrence = leftFoldingOccurrence;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void applyCorrectFunctionWhenFolding() throws Exception {
        Consumer<Object> leftFunction = mock(Consumer.class);
        Consumer<Object> rightFunction = mock(Consumer.class);

        either.fold(leftFunction, rightFunction);

        verify(rightFunction, rightFoldingOccurence).accept(Mockito.any());
        verify(leftFunction, leftFoldingOccurrence).accept(Mockito.any());
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
}
