package br.com.doit.commons.util;

import java.util.function.Function;

import org.apache.commons.lang.UnhandledException;

/**
 * Utility methods to deal with check exceptions while calling functions that throw checked exceptions.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class Throwables {
    /**
     * Wraps a function that may throw an exception. Useful when calling methods that throw checked exceptions with
     * Streams.
     * <p>
     * Usage:
     *
     * <pre>
     * list.stream()
     *     .map(rethrowing(x -> throwingFunction(x)))
     *     .collect(toList());
     * </pre>
     *
     * @param <T>
     *            the type of the input to the function
     * @param <R>
     *            the type of the result of the function
     * @param <E>
     *            the type of the expected exception
     * @param function
     *            The function to be applied
     * @return
     */
    public static <T, R, E extends Throwable> Function<T, R> rethrowing(ThrowingFunction<T, R, E> function) {
        return param -> {
            try {
                return function.apply(param);
            } catch (Throwable exception) {
                throw new UnhandledException(exception);
            }
        };
    }

    private Throwables() {
        throw new UnsupportedOperationException(Throwables.class.getName() + "can't be instantiated.");
    }
}
