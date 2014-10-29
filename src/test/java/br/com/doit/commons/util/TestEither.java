package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import br.com.doit.commons.util.Either.Function;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(Parameterized.class)
public class TestEither {
    @Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        Object expectedObject = new Object();

        return Arrays.asList(new Object[][] {
                { "Failure", Either.failure(expectedObject), false, true, null, expectedObject, never(), atLeastOnce() },
                { "Success", Either.success(expectedObject), true, false, expectedObject, null, atLeastOnce(), never() }
        });
    }

    private final Either<Object, Object> either;
    private final Object failure;
    private final VerificationMode failureFoldingOccurrence;
    private final boolean isFailure;
    private final boolean isSuccess;
    private final Object success;
    private final VerificationMode successFoldingOccurence;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TestEither(String type, Either<Object, Object> either, boolean isSuccess, boolean isFailure, Object success, Object failure, VerificationMode successFoldingOccurence, VerificationMode failureFoldingOccurrence) {
        this.either = either;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.success = success;
        this.failure = failure;
        this.successFoldingOccurence = successFoldingOccurence;
        this.failureFoldingOccurrence = failureFoldingOccurrence;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void applyCorrectFunctionWhenFolding() throws Exception {
        Function<Object> successFunction = mock(Function.class);
        Function<Object> failureFunction = mock(Function.class);

        either.fold(successFunction, failureFunction);

        verify(successFunction, successFoldingOccurence).apply(Mockito.any());
        verify(failureFunction, failureFoldingOccurrence).apply(Mockito.any());
    }

    @Test
    public void eitherIsFailureOrIsSuccess() throws Exception {
        assertThat(either.isFailure(), is(isFailure));
        assertThat(either.isSuccess(), is(isSuccess));
    }

    @Test
    public void returnExpectedSuccessOrFailure() throws Exception {
        assertThat(either.success(), is(success));
        assertThat(either.failure(), is(failure));
    }

    @Test
    public void throwExceptionWhenCreatingEitherWithBothSuccessAndFailureNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("Either must be a success or a failure. Success: null. Failure: null."));

        Either.failure(null);
    }
}
