package br.com.doit.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implementação do padrão de projeto Visitor usando programação funcional. Essa implementação permite um esquema de
 * pattern matching usando como critério o tipo dos objetos.
 *
 * Inspirado no artigo https://www.voxxed.com/blog/2016/05/gang-four-patterns-functional-light-part-4/.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ConsumerVisitor implements Consumer<Object> {
    public static class Acceptor<B> {
        private final Class<B> clazz;
        private final ConsumerVisitor visitor;

        Acceptor(ConsumerVisitor visitor, Class<B> clazz) {
            this.visitor = visitor;
            this.clazz = clazz;
        }

        /**
         * Determina o consumer que deverá ser executada para o tipo determinado anteriormente pelo método
         * {@link ConsumerVisitor#on(Class)}.
         *
         * @param c
         *            o consumer que será executada.
         * @return Retorna esse {@code ConsumerVisitor} para que outros matchers possam ser concatenados.
         */
        @SuppressWarnings("unchecked")
        public ConsumerVisitor then(Consumer<B> c) {
            visitor.consumers.put(clazz, (Consumer<Object>) c);

            return visitor;
        }
    }

    private static class DefaultConsumerGuard {}

    private final Map<Class<?>, Consumer<Object>> consumers = new HashMap<>();

    /**
     * Executa o consumer de acordo com o tipo do objeto passado por parâmetro ou o consumer padrão caso não exista um match
     * de acordo com o tipo do objeto passado por parâmetro.
     *
     * @throws IllegalStateException
     *             se não for possível fazer um match pelo tipo do objeto e não houver um consumer padrão.
     */
    @Override
    public void accept(Object o) {
        Class<?> clazz = o.getClass();

        if (!consumers.containsKey(clazz)) {
            if (!consumers.containsKey(DefaultConsumerGuard.class)) {
                throw new IllegalStateException("No match found.");
            }

            clazz = DefaultConsumerGuard.class;
        }

        consumers.get(clazz).accept(o);
    }

    /**
     * Determina o tipo que será usado para fazer o match e aplicar um consumer.
     *
     * @param clazz
     *            a classe que determina o tipo que será usado para fazer o match e aplicar o consumer.
     * @return Retorna um {@code Acceptor} que vai permitir associar um consumer ao tipo passado por parâmetro.
     */
    public <B> Acceptor<B> on(Class<B> clazz) {
        return new Acceptor<>(this, clazz);
    }

    /**
     * Operação terminal que permite definir um consumer que será executada se não for possível fazer um match.
     *
     * @param c
     *            oconsumer que será executada por padrão.
     * @return Retorna esse {@code ConsumerVisitor}
     */
    public Consumer<Object> orDefault(Consumer<Object> c) {
        consumers.put(DefaultConsumerGuard.class, c);

        return this;
    }
}
