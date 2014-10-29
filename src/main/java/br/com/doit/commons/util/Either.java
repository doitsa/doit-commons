package br.com.doit.commons.util;

import static org.apache.commons.lang.Validate.isTrue;

/**
 * Essa classe foi de certa forma inspirada na classe <code>Either</code> de
 * Scala.
 *
 * Referência: http://www.ibm.com/developerworks/library/j-ft13/
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 *
 * @param <S>
 *            O tipo do objeto retornado em caso de sucesso
 * @param <F>
 *            O tipo do objeto retornado em caso de falha
 */
public class Either<S, F> {
    public static abstract class Function<T> {
        public abstract void apply(T t);
    }

    public static <S, F> Either<S, F> failure(F failure) {
        return new Either<S, F>(null, failure);
    }

    public static <S, F> Either<S, F> success(S success) {
        return new Either<S, F>(success, null);
    }

    private final F failure;
    private final S success;

    private Either(S success, F failure) {
        isTrue(!(success == null && failure == null),
                "Either must be a success or a failure. Success: " + success + ". Failure: " + failure + ".");

        this.success = success;
        this.failure = failure;
    }

    public F failure() {
        return failure;
    }

    public void fold(Function<S> successFunction, Function<F> failureFunction) {
        if (failure == null) {
            successFunction.apply(success);
        } else {
            failureFunction.apply(failure);
        }
    }

    public boolean isFailure() {
        return failure != null;
    }

    public boolean isSuccess() {
        return success != null;
    }

    public S success() {
        return success;
    }
}
