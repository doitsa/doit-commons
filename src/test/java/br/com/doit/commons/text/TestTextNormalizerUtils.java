package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestTextNormalizerUtils {
    @Test
    public void doNotStripBlankSpaceFromTheMiddleOfWords() throws Exception {
        String result = TextNormalizerUtils.stripNonAlphanumericChars("abc def");

        assertThat(result, is("abc def"));
    }

    @Test
    public void lightStripTabsFromTheMiddleOfWords() throws Exception {
        String result = TextNormalizerUtils.lightStripNonAlphanumericChars("abc \t def");

        assertThat(result, is("abc  def"));
    }

    @Test
    public void removeAllLineBreaksWhenLightStrippingChars() {
        String result = TextNormalizerUtils
                                           .lightStripNonAlphanumericChars("Rua \rGuaip\u00e1,\n 1443 , /");

        assertThat(result, is("Rua Guaipa 1443"));
    }

    @Test
    public void normalizeNullString() throws Exception {
        String result = TextNormalizerUtils.toAscii(null);

        assertThat(result, nullValue());
    }

    @Test
    public void normalizeStringWithSpecialChars() throws Exception {
        String result = TextNormalizerUtils
                                           .toAscii("\u00e1\u00e9\u00ed\u00f3\u00fa\u00c1\u00c9\u00cd\u00d3\u00da\u00e7\u00c7\u00e3\u00f5");

        assertThat(result, is("aeiouAEIOUcCao"));
    }

    @Test
    public void removeAllLineBreaks() {
        String result = TextNormalizerUtils
                                           .stripNonAlphanumericChars("Rua \rGuaip\u00e1,\n 1443 , /");

        assertThat(result, is("Rua Guaipa 1443"));
    }

    @Test
    public void stripNonAlphanumericCharsAndConvertSpecialChars() throws Exception {
        String result = TextNormalizerUtils.stripNonAlphanumericChars("123',\u00e1\u00e9\u00ed\u00f3\u00fa.~456");

        assertThat(result, is("123aeiou456"));
    }

    @Test
    public void stripNonAlphanumericCharsFromNullString() throws Exception {
        String result = TextNormalizerUtils.stripNonAlphanumericChars(null);

        assertThat(result, nullValue());
    }

    @Test
    public void stripNonAlphanumericCharsFromString() throws Exception {
        String result = TextNormalizerUtils
                                           .stripNonAlphanumericChars("prefix123',.~456sufix");

        assertThat(result, is("prefix123456sufix"));
    }

    @Test
    public void stripNonNumericCharsFromNullString() throws Exception {
        String result = TextNormalizerUtils.stripNonNumericChars(null);

        assertThat(result, nullValue());
    }

    @Test
    public void stripNonNumericCharsFromString() throws Exception {
        String result = TextNormalizerUtils.stripNonNumericChars("123.456.789-09ABC");

        assertThat(result, is("12345678909"));
    }

    @Test
    public void stripTabsFromTheMiddleOfWords() throws Exception {
        String result = TextNormalizerUtils.stripNonAlphanumericChars("abc \t def");

        assertThat(result, is("abc  def"));
    }

    @Test
    public void trimAfterRemovingSpecials() {
        String result = TextNormalizerUtils
                                           .stripNonAlphanumericChars("Rua Guaip\u00e1, 1443 , /");

        assertThat(result, is("Rua Guaipa 1443"));
    }

    @Test
    public void trimNullString() throws Exception {
        String result = TextNormalizerUtils.trim(null);

        assertThat(result, nullValue());
    }

    @Test
    public void trimStrippedString() throws Exception {
        String result = TextNormalizerUtils.stripNonAlphanumericChars("   abc   ");

        assertThat(result, is("abc"));
    }

    @Test
    public void trimTextRemovesBlanksBeforeAndAfter() throws Exception {
        String result = TextNormalizerUtils.trim("   abc   \n\n ");

        assertThat(result, is("abc"));
    }

    @Test
    public void removingEmojisFromText() throws Exception {
        String result = TextNormalizerUtils.removeEmojis(null);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void doNothingWhenRemovingEmojisFromNullText() throws Exception {
        String emojiText = "小米写的 Привет 🛵 Neque porro *&*!123 quisquam est qui 👋🏽 dolorem ipsum quia 🧏 dolor sit amet, consectetur.";

        String result = TextNormalizerUtils.removeEmojis(emojiText);

        assertThat(result, is("小米写的 Привет Neque porro *&*!123 quisquam est qui dolorem ipsum quia dolor sit amet, consectetur."));

    }

}
