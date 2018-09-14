package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestTextEscapeUtils {
    @Test
    public void replaceJavaLineBrakToHTMLBreakTag() throws Exception {
        String result = TextEscapeUtils.escapeHtmlLineBreaks("Take on me (take on me)\nTake me on (take on me)\nI'll be gone\nIn a day or two");

        assertThat(result, is("Take on me (take on me)<br>Take me on (take on me)<br>I'll be gone<br>In a day or two"));
    }
}
