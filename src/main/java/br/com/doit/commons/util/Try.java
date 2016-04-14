package br.com.doit.commons.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A classe {@code Try} representa uma operação que pode resultar em uma exceção ou em um resultado gerado de forma bem
 * sucedida.
 *
 * Essa classe foi de certa forma inspirada na classe {@code Try} de Scala.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 *
 * @param <T>
 *            O tipo de valor que será retornado caso a operação seja bem sucedida.
 */
public abstract class Try<T> {
    private static class Failure<T> extends Try<T> {
        private final RuntimeException exception;

        public Failure(RuntimeException exception) {
            this.exception = exception;
        }

        @Override
        public T get() {
            throw exception;
        }

        @Override
        public void ifFailure(Consumer<? super RuntimeException> consumer) {
            consumer.accept(exception);
        }

        @Override
        public void ifSuccess(Consumer<T> consumer) {
            // Não faz nada
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T orElse(T other) {
            return other;
        }

        @Override
        public T orElseGet(Supplier<T> other) {
            return other.get();
        }

        @Override
        public <E extends Throwable> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
            throw exceptionSupplier.get();
        }
    }

    private static class Success<T> extends Try<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void ifFailure(Consumer<? super RuntimeException> consumer) {
            // Não faz nada
        }

        @Override
        public void ifSuccess(Consumer<T> consumer) {
            consumer.accept(value);
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T orElse(T other) {
            return value;
        }

        @Override
        public T orElseGet(Supplier<T> other) {
            return value;
        }

        @Override
        public <E extends Throwable> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
            return value;
        }
    }

    /**
     * Execute a operação passada por parâmetro e retorna um objeto do tipo {@code Success} se a operação for bem
     * sucedida ou {@code Failure} caso ocorra algum erro.
     *
     * @param statement
     *            A operação que deve ser executada.
     * @return Retorna um objeto do tipo {@code Try} de acordo com o resultado da operação.
     */
    public static final <T> Try<T> run(Supplier<T> statement) {
        try {
            return new Success<T>(statement.get());
        } catch (RuntimeException exception) {
            return new Failure<T>(exception);
        }
    }

    /**
     * Obtém o resultado da operação.
     *
     * @return Retorna o resultado da operação.
     */
    public abstract T get();

    /**
     * Executa a função passada por parâmetro, utilizando a exceção produzida ao executar a operação. Se a
     * operação tiver sido bem sucedida, não faz nada.
     *
     * @param consumer
     *            Uma função que será executa caso a operação tenha sido mal sucedida.
     */
    public abstract void ifFailure(Consumer<? super RuntimeException> consumer);

    /**
     * Executa a função passada por parâmetro, utilizando o resultado da operação caso tenha sido bem sucedida. Se a
     * operação tiver produzido uma falha, não faz nada.
     *
     * @param consumer
     *            Uma função que será executa caso a operação tenha sido bem sucedida.
     */
    public abstract void ifSuccess(Consumer<T> consumer);

    /**
     * Verifica se a tentativa de execução da operação produziu alguma exceção.
     *
     * @return Retorna {@code true} quando uma exceção foi capturada ou {@code false} quando a operação foi realizada
     *         com sucesso.
     */
    public abstract boolean isFailure();

    /**
     * Verifica se a tentativa de execução da operação foi bem sucedida.
     *
     * @return Retorna {@code true} quando a operação foi realizada com sucesso ou {@code false} quando uma exceção foi
     *         capturada.
     */
    public abstract boolean isSuccess();

    /**
     * Obtém o resultado da operação caso tenha sido bem sucedida ou o valor alternativo passado por parâmetro.
     *
     * @param other
     *            Um valor alternativo para ser retornado caso tenha ocorrido uma falha.
     * @return Retorna o resultado da operação ou um valor alternativo.
     */
    public abstract T orElse(T other);

    /**
     * Obtém o resultado da operação caso tenha sido bem sucedida ou o valor alternativo produzido pelo parâmetro
     * {@code other}.
     *
     * @param other
     *            Um fonecedor de valor alternativo para ser retornado caso tenha ocorrido uma falha.
     * @return Retorna o resultado da operação ou um valor alternativo.
     */
    public abstract T orElseGet(Supplier<T> other);

    /**
     * Obtém o resultado da operação caso tenha sido bem sucedida ou lança a exceção informada por parâmetro.
     *
     * @param exceptionSupplier
     *            Função que vai produzir a exceção que deve ser lançada.
     * @return Retorna o resultado da operação ou lança uma exceção.
     * @throws E
     *             O tipo da exceção que será lançada caso a operação tenha produzido alguma exceção.
     */
    public abstract <E extends Throwable> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E;
}
