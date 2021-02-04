package br.com.doit.commons.lang;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Optional;

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

    @Test
    public void returnValueForExistingEnumValue() throws Exception {
        assertThat(Enums.valueOfOrNull(SampleEnum.class, "ONE"), is(SampleEnum.ONE));
    }

    @Test
    public void returnNullForUnknownEnumValue() throws Exception {
        assertThat(Enums.valueOfOrNull(SampleEnum.class, "TWELVE"), nullValue());
    }

    @Test
    public void returnNulForNullEnumValue() throws Exception {
        assertThat(Enums.valueOfOrNull(SampleEnum.class, null), nullValue());
    }

    @Test
    public void returnNulForNullEnum() throws Exception {
        assertThat(Enums.valueOfOrNull(null, null), nullValue());
    }

    @Test
    public void returnOptinalEnumValueFromString() throws Exception {
        assertThat(Enums.optionalValueOf(SampleEnum.class, "THREE"), is(Optional.of(SampleEnum.THREE)));
    }

    @Test
    public void returnEmptyOptinalForUnknownEnumValueFromString() throws Exception {
        assertThat(Enums.optionalValueOf(SampleEnum.class, "three"), is(Optional.empty()));
    }

    @Test
    public void returnEmptyOptinalForNullEnumValue() throws Exception {
        assertThat(Enums.optionalValueOf(SampleEnum.class, null), is(Optional.empty()));
    }

    @Test
    public void returnEmptyOptinalForNullEnum() throws Exception {
        assertThat(Enums.optionalValueOf(null, null), is(Optional.empty()));
    }
}
