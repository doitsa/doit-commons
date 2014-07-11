package br.com.doit.commons.lang;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestEnums {
    private static enum SampleEnum {
        ONE, THREE, TWO;
    }

    @Test
    public void ignoreNullExcludesWhenGettingValues() throws Exception {
        SampleEnum[] result = Enums.valuesExcluding(SampleEnum.class, (SampleEnum) null);

        assertArrayEquals(result, SampleEnum.values());
    }

    @Test
    public void removeValuesWhenTwoExcludesDefined() throws Exception {
        SampleEnum[] result = Enums.valuesExcluding(SampleEnum.class, SampleEnum.THREE, SampleEnum.ONE);

        assertThat(Arrays.asList(result), not(hasItem(SampleEnum.THREE)));
        assertThat(Arrays.asList(result), not(hasItem(SampleEnum.ONE)));

        assertThat(Arrays.asList(result), hasItem(SampleEnum.TWO));
    }

    @Test
    public void removeValueWhenOneExcludesDefined() throws Exception {
        SampleEnum[] result = Enums.valuesExcluding(SampleEnum.class, SampleEnum.THREE);

        assertThat(Arrays.asList(result), not(hasItem(SampleEnum.THREE)));

        assertThat(Arrays.asList(result), hasItem(SampleEnum.ONE));
        assertThat(Arrays.asList(result), hasItem(SampleEnum.TWO));
    }

    @Test
    public void returnAllValuesWhenNoExcludesDefined() throws Exception {
        SampleEnum[] result = Enums.valuesExcluding(SampleEnum.class);

        assertArrayEquals(result, SampleEnum.values());
    }
}
