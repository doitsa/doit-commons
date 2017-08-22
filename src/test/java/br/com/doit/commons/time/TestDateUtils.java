package br.com.doit.commons.time;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.webobjects.foundation.NSTimestamp;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestDateUtils {
    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalDate() throws Exception {
        LocalDate localDate = LocalDate.of(1983, 1, 7);

        Date result = DateUtils.toDate(localDate);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1983));
        assertThat(calendar.get(Calendar.MONTH), is(0));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(7));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToDate() throws Exception {
        Date result = DateUtils.toDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromDate() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1983-01-07");

        LocalDate result = DateUtils.toLocalDate(date);

        assertThat(result, is(LocalDate.of(1983, 1, 7)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalDate() throws Exception {
        LocalDate result = DateUtils.toLocalDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalDateTime() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(1991, 2, 16, 9, 0, 10);

        Date result = DateUtils.toDateTime(localDateTime);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(9));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(10));
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToDateTime() throws Exception {
        Date result = DateUtils.toDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromDateTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 09:00:10");

        LocalDateTime result = DateUtils.toLocalDateTime(date);

        assertThat(result, is(LocalDateTime.of(1991, 2, 16, 9, 0, 10)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalDateTime() throws Exception {
        LocalDate result = DateUtils.toLocalDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 00:00:00");
        LocalTime localTime = LocalTime.of(9, 0, 31);

        Date result = DateUtils.toDateTime(date, localTime);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(9));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(31));
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToLocalTime() throws Exception {
        Date result = DateUtils.toDateTime(null, null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromLocalTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 09:00:10");

        LocalTime result = DateUtils.toLocalTime(date);

        assertThat(result, is(LocalTime.of(9, 0, 10)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalTime() throws Exception {
        LocalTime result = DateUtils.toLocalTime(null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnMidnightWhenConvertingFromNullLocalTimeAndPassingAValidDate() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 00:00:00");

        Date result = DateUtils.toDateTime(date, null);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNSTimestampWhenConvertingFromLocalDateTime() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp(LocalDateTime.of(2016, 5, 3, 2, 45));

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(2016));
        assertThat(calendar.get(Calendar.MONTH), is(4));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(3));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(2));
        assertThat(calendar.get(Calendar.MINUTE), is(45));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullNSTimestampWhenConvertingFromNullLocalDateTime() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp((LocalDateTime) null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNSTimestampWhenConvertingFromLocalDate() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp(LocalDate.of(2016, 5, 3));

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(2016));
        assertThat(calendar.get(Calendar.MONTH), is(4));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(3));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullNSTimestampWhenConvertingFromNullLocalDate() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp((LocalDate) null);

        assertThat(result, nullValue());
    }
}
