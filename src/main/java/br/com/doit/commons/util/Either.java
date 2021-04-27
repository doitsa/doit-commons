package br.com.doit.commons.util;

import static org.apache.commons.lang.Validate.isTrue;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Essa classe foi de certa forma inspirada na classe <code>Either</code> de
 * Scala.
 *
 * Referência: http://www.ibm.com/developerworks/library/j-ft13/
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 *
 * @param <L>
 *            O tipo do objeto normalmente retornado em caso de falha
 * @param <R>
 *            O tipo do objeto normalmente retornado em caso de sucesso
 */
public class Either<L, R> {
    /**
     * Cria um objeto representando o lado direito (correto) de um {@code Either}.
     */
    public static <L, R> Either<L, R> right(R right) {
        return new Either<L, R>(null, right);
    }

    /**
     * Cria um objeto representando o lado esquerdo de um {@code Either}.
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Either<L, R>(left, null);
    }

    private final L left;
    private final R right;

    private Either(L left, R right) {
        isTrue(!(left == null && right == null),
                "Either must have a left or a right. Left: " + left + ". Right: " + right + ".");

        this.left = left;
        this.right = right;
    }

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }

    public boolean isRight() {
        return right != null;
    }

    public boolean isLeft() {
        return left != null;
    }

    /**
     * Executa a função da esquerda se esse {@code Either} retornar {@code true} para o método {@code isLeft}. Caso
     * contrário, executa a função da direita se esse {@code Either} retornar {@code true} para o método
     * {@code isRight}.
     */
    public void fold(Consumer<L> leftFunction, Consumer<R> rightFunction) {
        if (isLeft()) {
            leftFunction.accept(left);
        } else {
            rightFunction.accept(right);
        }
    }

    /**
     * Executa a função da esquerda se esse {@code Either} retornar {@code true} para o método {@code isLeft}. Caso
     * contrário, executa a função da direita se esse {@code Either} retornar {@code true} para o método
     * {@code isRight}. Retona o resultado da função aplicada.
     */
    public <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction) {
        if (isLeft()) {
            return leftFunction.apply(left);
        }

        return rightFunction.apply(right);
    }

    @Override
    public String toString() {
        if (isLeft()) {
            return String.format("Left[%s]", left);
        } else {
            return String.format("Right[%s]", right);
        }
    }
}
