package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestHoursFormat {
    protected HoursFormat formatter;

    @Test
    public void formatLessThan1000Hours() throws Exception {
        String result = formatter.format(59999);

        assertThat(result, is("999:59"));
    }

    @Test
    public void formatLessThan60Minutes() throws Exception {
        String result = formatter.format(59);

        assertThat(result, is("00:59"));
    }

    @Test
    public void formatMoreThan1000Hours() throws Exception {
        String result = formatter.format(60001);

        assertThat(result, is("1.000:01"));
    }

    @Test
    public void formatMoreThan60Minutes() throws Exception {
        String result = formatter.format(61);

        assertThat(result, is("01:01"));
    }

    @Test
    public void formatNegativeTime() throws Exception {
        String result = formatter.format(-61);

        assertThat(result, is("-01:01"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatNotIntegerObjectThrowsException() throws Exception {
        formatter.format("teste");
    }

    @Test
    public void formatNullValue() throws Exception {
        String result = formatter.format(null);

        assertThat(result, is(""));
    }

    @Test
    public void formatZeroMinutes() throws Exception {
        String result = formatter.format(0);

        assertThat(result, is("00:00"));
    }

    @Test
    public void parse1000Hours() throws Exception {
        Object result = formatter.parseObject("1.000");

        assertThat(result.toString(), is("60000"));
    }

    @Test
    public void parse1000HoursWithMinutes() throws Exception {
        Object result = formatter.parseObject("1.000:30");

        assertThat(result.toString(), is("60030"));
    }

    @Test(expected = ParseException.class)
    public void parseInvalidStringFormatThrowsException() throws Exception {
        formatter.parseObject("asfdsfdaf");
    }

    @Test
    public void parseLessThan60Miutes() throws Exception {
        Object result = formatter.parseObject("00:59");

        assertThat((Integer) result, is(59));
    }

    @Test
    public void parseMoreThan60Miutes() throws Exception {
        Object result = formatter.parseObject("01:01");

        assertThat((Integer) result, is(61));
    }

    @Test
    public void parseNegativeNumber() throws Exception {
        Object result = formatter.parseObject("-00:01");

        assertThat((Integer) result, is(-1));
    }

    @Test
    public void parseNegativeNumberEqualsToOneHour() throws Exception {
        Object result = formatter.parseObject("-01");

        assertThat((Integer) result, is(-60));
    }

    @Test
    public void parseNegativeNumberGreaterThen60Minutes() throws Exception {
        Object result = formatter.parseObject("-01:01");

        assertThat((Integer) result, is(-61));
    }

    @Test
    public void parseNullValue() throws Exception {
        Object result = formatter.parseObject(null);

        assertThat(result, nullValue());
    }

    @Test
    public void parseOnlyHour() throws Exception {
        Object result = formatter.parseObject("3");

        assertThat((Integer) result, is(180));
    }

    @Test
    public void parseZeroMinutes() throws Exception {
        Object result = formatter.parseObject("00:00");

        assertThat((Integer) result, is(0));
    }

    @Before
    public void setup() {
        formatter = new HoursFormat();
    }
}
