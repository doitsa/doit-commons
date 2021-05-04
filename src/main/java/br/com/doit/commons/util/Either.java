package br.com.doit.commons.util;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Essa classe foi de certa forma inspirada na classe <code>Either</code> de
 * Scala.
 * <p>
 * Referência: http://www.ibm.com/developerworks/library/j-ft13/
 *
 * @param <L>
 *            O tipo do objeto normalmente retornado em caso de falha
 * @param <R>
 *            O tipo do objeto normalmente retornado em caso de sucesso
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class Either<L, R> {
    public static class Left<L, R> extends Either<L, R> {
        private final L left;

        private Left(L left) {
            this.left = left;
        }

        @Override
        public L left() {
            return left;
        }

        @Override
        public R right() {
            throw new NoSuchElementException("Cannot get a Right from a Left.");
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction) {
            return leftFunction.apply(left);
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public void run(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
            leftConsumer.accept(left);
        }

        @Override
        public String toString() {
            return String.format("Left[%s]", left);
        }
    }

    public static class Right<L, R> extends Either<L, R> {
        private final R right;

        private Right(R right) {
            this.right = right;
        }

        @Override
        public L left() {
            throw new NoSuchElementException("Cannot get a Left from a Right.");
        }

        @Override
        public R right() {
            return right;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction) {
            return rightFunction.apply(right);
        }

        @Override
        public void run(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
            rightConsumer.accept(right);
        }

        @Override
        public String toString() {
            return String.format("Right[%s]", right);
        }
    }

    /**
     * Cria um objeto representando o lado direito (correto) de um {@code Either}.
     */
    public static <L, R> Either<L, R> right(R right) {
        return new Right<L, R>(right);
    }

    /**
     * Cria um objeto representando o lado esquerdo (falha) de um {@code Either}.
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Left<L, R>(left);
    }

    public abstract L left();

    /**
     *
     *
     * @return
     */
    public abstract R right();

    /**
     * Verificar se esse {@code Either} corresponde ao lado direito.
     *
     * @return Retorna true se esse {@code Either} for um {@code Right} ou false caso contrário.
     */
    public abstract boolean isRight();

    /**
     * Verificar se esse {@code Either} corresponde ao lado esquerdo.
     *
     * @return Retorna true se esse {@code Either} for um {@code Left} ou false caso contrário.
     */
    public abstract boolean isLeft();

    /**
     * Executa a função da esquerda se esse {@code Either} retornar {@code true} para o método {@code isLeft}. Caso
     * contrário, executa a função da direita se esse {@code Either} retornar {@code true} para o método
     * {@code isRight}.
     *
     * @param <T>
     *            O tipo retornado pelas funções que podem ser aplicadas a esse {@code Either}.
     * @param leftFunction
     *            a função que será executada se esse {@code Either} for um {@code Left}.
     * @param rightFunction
     *            a função que será executada se esse {@code Either} for um {@code Right}.
     * @return Retona o resultado da função aplicada.
     */
    public abstract <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction);

    /**
     * Executa a função da esquerda se esse {@code Either} retornar {@code true} para o método {@code isLeft}. Caso
     * contrário, executa a função da direita se esse {@code Either} retornar {@code true} para o método
     * {@code isRight}.
     *
     * @param leftConsumer
     *            a função que será executada se esse {@code Either} for um {@code Left}.
     * @param rightConsumer
     *            a função que será executada se esse {@code Either} for um {@code Right}.
     */
    public abstract void run(Consumer<L> leftConsumer, Consumer<R> rightConsumer);
}
