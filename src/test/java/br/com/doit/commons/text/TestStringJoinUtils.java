package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestStringJoinUtils {

    @Test
    public void joinStringsForOneStringValue() {
        String result = StringJoinUtils.joinNonBlank(", ", "string1");

        assertThat(result, is("string1"));
    }

    @Test
    public void joinStringsForMoreThanOneStringValue() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", "string1", "string2");

        assertThat(result, is("string1, string2"));
    }

    @Test
    public void returnEmptyWhenJoiningNullString() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", (String) null);

        assertThat(result, is(""));
    }

    @Test
    public void doNotTakeNullWhenJoiningStrings() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", "string1", null, "string2");

        assertThat(result, is("string1, string2"));
    }

    @Test
    public void doNotTakeBlankWhenJoiningStrings() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", "string1", " ", "string2");

        assertThat(result, is("string1, string2"));
    }

    @Test
    public void joinStringsForIterableOfStrings() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", Arrays.asList("string1", "string2"));

        assertThat(result, is("string1, string2"));
    }

    @Test
    public void joinStringsForStreamOfStrings() throws Exception {
        String result = StringJoinUtils.joinNonBlank(", ", Stream.of("string1", "string2"));

        assertThat(result, is("string1, string2"));
    }

}
