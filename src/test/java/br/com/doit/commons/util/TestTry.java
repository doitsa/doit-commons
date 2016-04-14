package br.com.doit.commons.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * TODO: filter(Predicate)
 * TODO: flatMap(Function)
 * TODO: map(Function)
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTry {
    private Try<String> success;
    private Try<String> failure;
    private IllegalArgumentException exception;

    @Mock
    private Consumer<String> consumer;

    @Mock
    private Consumer<RuntimeException> errorConsumer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        success = Try.run(() -> "OK");

        exception = new IllegalArgumentException("Error");

        failure = Try.run(() -> {
            throw exception;
        });
    }

    @Test
    public void isSuccessWhenStatementDoesNotThrowException() throws Exception {
        assertThat(success.isSuccess(), is(true));
        assertThat(success.isFailure(), is(false));
    }

    @Test
    public void isFailureWhenStatementThrowsException() throws Exception {
        assertThat(failure.isSuccess(), is(false));
        assertThat(failure.isFailure(), is(true));
    }

    @Test
    public void returnStatementResultWhenGettingFromSuccess() throws Exception {
        assertThat(success.get(), is("OK"));
    }

    @Test
    public void throwExceptionWhenGettingFromFailure() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(is("Error"));

        failure.get();
    }

    @Test
    public void returnStatementResultWhenCallingOrElseThrowFromSuccess() throws Exception {
        assertThat(success.orElseThrow(() -> new RuntimeException("Error")), is("OK"));
    }

    @Test
    public void throwExceptionWhenCallingOrElseThrowFromFailure() throws Exception {
        thrown.expect(Error.class);
        thrown.expectMessage(is("Another Error"));

        failure.orElseThrow(() -> new Error("Another Error"));
    }

    @Test
    public void returnStatementResultWhenCallingOrElseFromSuccess() throws Exception {
        assertThat(success.orElse("OK2"), is("OK"));
    }

    @Test
    public void returnOtherValueWhenCallingOrElseFromFailure() throws Exception {
        assertThat(failure.orElse("OK2"), is("OK2"));
    }

    @Test
    public void returnStatementResultWhenCallingOrElseGetFromSuccess() throws Exception {
        assertThat(success.orElseGet(() -> "OK2"), is("OK"));
    }

    @Test
    public void returnOtherValueWhenCallingOrElseGetFromFailure() throws Exception {
        assertThat(failure.orElseGet(() -> "OK2"), is("OK2"));
    }

    @Test
    public void executeStatementWhenCallingIfSuccessFromSuccess() throws Exception {
        success.ifSuccess(consumer);

        verify(consumer).accept("OK");
    }

    @Test
    public void doNotExecuteStatementWhenCallingIfSuccessFromFailure() throws Exception {
        failure.ifSuccess(consumer);

        verify(consumer, never()).accept(Mockito.anyString());
    }

    @Test
    public void doNotExecuteStatementWhenCallingIfFailureFromSuccess() throws Exception {
        success.ifFailure(errorConsumer);

        verify(errorConsumer, never()).accept(Mockito.any(RuntimeException.class));
    }

    @Test
    public void executeStatementWhenCallingIfFailureFromFailure() throws Exception {
        failure.ifFailure(errorConsumer);

        verify(errorConsumer).accept(exception);
    }
}
