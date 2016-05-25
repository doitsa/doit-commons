package br.com.doit.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementação do padrão de projeto Visitor usando programação funcional. Essa implementação permite um esquema de
 * pattern matching usando como critério o tipo dos objetos.
 *
 * Inspirado no artigo https://www.voxxed.com/blog/2016/05/gang-four-patterns-functional-light-part-4/.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class Visitor<A> implements Function<Object, A> {
    @SuppressWarnings("rawtypes")
    public static class Acceptor<A, B> {
        private final Class<B> clazz;
        private final Visitor visitor;

        Acceptor(Visitor<A> visitor, Class<B> clazz) {
            this.visitor = visitor;
            this.clazz = clazz;
        }

        /**
         * Determina a função que deverá ser executada para o tipo determinado anteriormente pelo método
         * {@link Visitor#on(Class)}.
         *
         * @param f
         *            a função que será executada.
         * @return Retorna esse {@code Visitor} para que outros matchers possam ser concatenados.
         */
        @SuppressWarnings("unchecked")
        public Visitor<A> then(Function<B, A> f) {
            visitor.functions.put(clazz, f);

            return visitor;
        }
    }

    private static class DefaultFunctionGuard {}

    private final Map<Class<?>, Function<Object, A>> functions = new HashMap<>();

    /**
     * Executa a função de acordo com o tipo do objeto passado por parâmetro ou a função padrão caso não exista um match
     * de acordo com o tipo do objeto passado por parâmetro.
     *
     * @throws IllegalStateException
     *             se não for possível fazer um match pelo tipo do objeto e não houver uma função padrão.
     */
    @Override
    public A apply(Object o) {
        Class<?> clazz = o.getClass();

        if (!functions.containsKey(clazz)) {
            if (!functions.containsKey(DefaultFunctionGuard.class)) {
                throw new IllegalStateException("No match found.");
            }

            clazz = DefaultFunctionGuard.class;
        }

        return functions.get(clazz).apply(o);
    }

    /**
     * Determina o tipo que será usado para fazer o match e aplicar uma função.
     *
     * @param clazz
     *            a classe que determina o tipo que será usado para fazer o match e aplicar a função.
     * @return Retorna um {@code Acceptor} que vai permitir associar uma função ao tipo passado por parâmetro.
     */
    public <B> Acceptor<A, B> on(Class<B> clazz) {
        return new Acceptor<>(this, clazz);
    }

    /**
     * Operação terminal que permite definir uma função que será executada se não for possível fazer um match.
     *
     * @param f
     *            a função que será executada por padrão.
     * @return Retorna esse {@code Visitor}
     */
    public Function<Object, A> orDefault(Function<Object, A> f) {
        functions.put(DefaultFunctionGuard.class, f);

        return this;
    }
}
