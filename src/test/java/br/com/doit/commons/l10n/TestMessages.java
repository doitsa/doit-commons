package br.com.doit.commons.l10n;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestMessages {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Messages messages;

    @Before
    public void setup() {
        messages = Messages.getMessages("br/com/doit/commons/test/messages");
    }

    @After
    public void tearDown() throws Exception {
        Messages.setSupplierOfLocale(null);

        rewriteResourceBundleContent("key=value\nformatKey=value {0} value {1} value");
    }

    @Test
    public void returnMessageWhenGettingMessageForKey() throws Exception {
        String result = messages.get("key");

        assertThat(result, is("value"));
    }

    @Test
    public void returnNullWhenGettingMessageForKeyNotFound() throws Exception {
        String result = messages.get("notFound");

        assertThat(result, nullValue());
    }

    @Test
    public void returnFormattedMessageWhenFormattingMessageForKey() throws Exception {
        String result = messages.format("formatKey", "aaa", "bbb");

        assertThat(result, is("value aaa value bbb value"));
    }

    @Test
    public void throwExceptionWhenResourceBundleNotFound() throws Exception {
        thrown.expect(MissingResourceException.class);
        thrown.expectMessage(is("Can't find bundle for base name br/com/doit/commons/test/notfound, locale en_US"));

        Messages.getMessages("br/com/doit/commons/test/notfound");
    }

    @Test
    public void selectCorrectLocaleWhenGettingMessage() throws Exception {
        Messages.setSupplierOfLocale(() -> Locale.FRANCE);

        String result = messages.get("key");

        assertThat(result, is("valeur"));

        result = messages.format("formatKey", "aaa", "bbb");

        assertThat(result, is("valeur aaa valeur bbb valeur"));
    }

    @Test
    public void doNotCacheResourceBundleWhenCachingIsDisabled() throws Exception {
        Messages.setIsCacheEnabled(false);

        messages.get("key");

        rewriteResourceBundleContent("key=some");

        String result = messages.get("key");

        assertThat(result, is("some"));
    }

    @Test
    public void cacheResourceBundleWhenCachingIsEnabled() throws Exception {
        Messages.setIsCacheEnabled(true);

        messages.get("key");

        rewriteResourceBundleContent("key=some");

        String result = messages.get("key");

        assertThat(result, is("value"));
    }

    private void rewriteResourceBundleContent(String content) throws Exception {
        URL url = getClass().getResource("/br/com/doit/commons/test/messages.properties");

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(url.toURI())));

        writer.write(content);
        writer.close();
    }
}
