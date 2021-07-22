package br.com.doit.commons.color;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestColorUtils {
    @Test
    public void retrieveColorForLongValue() throws Exception {
        assertThat(ColorUtils.colorFor(123L), is("#BC0495"));
    }

    @Test
    public void confirmBlackIsDark() throws Exception {
        assertTrue(ColorUtils.isDark("#000000"));
    }

    @Test
    public void denyWhiteIsDark() throws Exception {
        assertFalse(ColorUtils.isDark("#ffffff"));
    }
}