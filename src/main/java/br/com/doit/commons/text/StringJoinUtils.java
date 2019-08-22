package br.com.doit.commons.text;

import static java.util.stream.Collectors.joining;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class containing methods to simplify the way of joining {@code String}s.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class StringJoinUtils {

    /**
     * Joins the given elements separated by the {@code delimiter} skipping null, empty and blank values.
     *
     * @param delimeter
     *            the sequence of characters to be used between each element
     * @param elements
     *            the elements to join
     * @return A {@code String} representing the concatenation of non-blank elements or an empty {@code String} if all
     *         elements are null, empty or blank.
     */
    public static String joinNonBlank(CharSequence delimeter, CharSequence... elements) {
        return joinNonBlank(delimeter, Stream.of(elements));
    }

    /**
     * Joins the given elements separated by the {@code delimiter} skipping null, empty and blank values.
     *
     * @param delimeter
     *            the sequence of characters to be used between each element
     * @param elements
     *            the elements to join
     * @return A {@code String} representing the concatenation of non-blank elements or an empty {@code String} if all
     *         elements are null, empty or blank.
     */
    public static String joinNonBlank(CharSequence delimeter, Iterable<? extends CharSequence> elements) {
        return joinNonBlank(delimeter, StreamSupport.stream(elements.spliterator(), false));
    }

    /**
     * Joins the given elements separated by the {@code delimiter} skipping null, empty and blank values.
     *
     * @param delimeter
     *            the sequence of characters to be used between each element
     * @param elements
     *            the elements to join
     * @return A {@code String} representing the concatenation of non-blank elements or an empty {@code String} if all
     *         elements are null, empty or blank.
     */
    public static String joinNonBlank(CharSequence delimeter, Stream<? extends CharSequence> elements) {
        return elements.filter(Objects::nonNull)
                       .map(Object::toString)
                       .filter(StringUtils::isNotBlank)
                       .collect(joining(delimeter));
    }

    private StringJoinUtils() {
        // Cannot be instantiated
    }
}
